/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2011 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package com.sun.enterprise.security.ssl.impl;

import com.sun.enterprise.security.ssl.manager.UnifiedX509KeyManager;
import com.sun.enterprise.security.ssl.manager.UnifiedX509TrustManager;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.security.KeyStore;
import java.security.Provider;

//V3:Commented import com.sun.enterprise.config.ConfigContext;
import com.sun.enterprise.server.pluggable.SecuritySupport;
import com.sun.logging.LogDomains;
import java.io.IOException;
import java.security.AccessControlException;
import java.security.AccessController;
import java.security.Key;
import java.security.Permission;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.PropertyPermission;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509KeyManager;
import javax.net.ssl.X509TrustManager;
import org.glassfish.api.admin.ProcessEnvironment;
import org.glassfish.api.admin.ProcessEnvironment.ProcessType;
import org.glassfish.internal.embedded.Server;
import org.glassfish.internal.api.Globals;
import org.jvnet.hk2.annotations.Inject;
import org.jvnet.hk2.annotations.Service;
import org.jvnet.hk2.annotations.Scoped;
import org.jvnet.hk2.component.Habitat;
import org.jvnet.hk2.component.Singleton;

/**
 * This implements SecuritySupport used in PluggableFeatureFactory.
 * @author Shing Wai Chan
 */
// TODO: when we have two SecuritySupport implementations,
// we create Habitat we'll select which SecuritySupport implementation to use.
@Service
@Scoped(Singleton.class)
public class SecuritySupportImpl extends SecuritySupport {
    private static final String DEFAULT_KEYSTORE_PASS = "changeit";
    private static final String DEFAULT_TRUSTSTORE_PASS = "changeit";

    protected static final Logger _logger =
            LogDomains.getLogger(SecuritySupportImpl.class, LogDomains.SECURITY_SSL_LOGGER);
    protected static boolean initialized = false;
    protected static final List<KeyStore> keyStores = new ArrayList<KeyStore>();
    protected static final List<KeyStore> trustStores = new ArrayList<KeyStore>();
    protected static final List<char[]> keyStorePasswords = new ArrayList<char[]>();
    protected static final List<String> tokenNames = new ArrayList<String>();
    private MasterPasswordImpl masterPasswordHelper = null;
    private static boolean instantiated = false;
    private Date initDate = new Date();

    @Inject
    private Habitat habitat;
    @Inject
    private ProcessEnvironment penv;

    public SecuritySupportImpl() {
        this(true);
    }

    protected SecuritySupportImpl(boolean init) {
        if (init) {
            initJKS();
        }
    }

    private void initJKS() {
        String keyStoreFileName = null;
        String trustStoreFileName = null;

        keyStoreFileName = System.getProperty(keyStoreProp);
        trustStoreFileName = System.getProperty(trustStoreProp);

        char[] keyStorePass = null;
        char[] trustStorePass = null;
        if (!isInstantiated()) {
            if (masterPasswordHelper == null && Globals.getDefaultHabitat() != null) {
                masterPasswordHelper = Globals.getDefaultHabitat().getByType(MasterPasswordImpl.class);
            }
            if (masterPasswordHelper instanceof MasterPasswordImpl) {
                keyStorePass = masterPasswordHelper.getMasterPassword();
                trustStorePass = keyStorePass;
            }
        }
        if (keyStorePass == null) {
            keyStorePass = System.getProperty(KEYSTORE_PASS_PROP, DEFAULT_KEYSTORE_PASS).toCharArray();
            trustStorePass = System.getProperty(TRUSTSTORE_PASS_PROP, DEFAULT_TRUSTSTORE_PASS).toCharArray();
        }

        if (!initialized) {
            loadStores(
                    null,
                    null,
                    keyStoreFileName,
                    keyStorePass,
                    System.getProperty(KEYSTORE_TYPE_PROP, KeyStore.getDefaultType()),
                    trustStoreFileName,
                    trustStorePass,
                    System.getProperty(TRUSTSTORE_TYPE_PROP, KeyStore.getDefaultType()));
            Arrays.fill(keyStorePass, ' ');
            Arrays.fill(trustStorePass, ' ');
            initialized = true;
        }
    }

    private boolean isEmbeddedServer() {
        List<String> servers = Server.getServerNames();
        if (!servers.isEmpty()) {
            return true;
        }
        return false;
    }

    private static synchronized boolean isInstantiated() {
        if (!instantiated) {
            instantiated = true;
            return false;
        }
        return true;
    }

    /**
     * This method will load keystore and truststore and add into
     * corresponding list.
     * @param tokenName
     * @param provider
     * @param keyStorePass
     * @param keyStoreFile
     * @param keyStoreType
     * @param trustStorePass
     * @param trustStoreFile
     * @param trustStoreType
     */
    /*protected synchronized static void loadStores(String tokenName, 
    String storeType, Provider provider,
    String keyStoreFile, String keyStorePass,
    String trustStoreFile, String trustStorePass) {*/
    protected synchronized static void loadStores(
            String tokenName,
            Provider provider,
            String keyStoreFile,
            char[] keyStorePass,
            String keyStoreType,
            String trustStoreFile,
            char[] trustStorePass,
            String trustStoreType) {

        try {
            KeyStore keyStore = loadKS(keyStoreType, provider, keyStoreFile,
                    keyStorePass);
            KeyStore trustStore = loadKS(trustStoreType, provider, trustStoreFile,
                    trustStorePass);
            keyStores.add(keyStore);
            trustStores.add(trustStore);
            keyStorePasswords.add(Arrays.copyOf(keyStorePass, keyStorePass.length));
            tokenNames.add(tokenName);
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

    /**
     * This method load keystore with given keystore file and
     * keystore password for a given keystore type and provider.
     * It always return a non-null keystore.
     * @param keyStoreType
     * @param provider
     * @param keyStoreFile
     * @param keyStorePass
     * @retun keystore loaded
     */
    private static KeyStore loadKS(String keyStoreType, Provider provider,
            String keyStoreFile, char[] keyStorePass)
            throws Exception {
        KeyStore ks = null;
        if (provider != null) {
            ks = KeyStore.getInstance(keyStoreType, provider);
        } else {
            ks = KeyStore.getInstance(keyStoreType);
        }
        char[] passphrase = keyStorePass;

        FileInputStream istream = null;
        BufferedInputStream bstream = null;
        try {
            if (keyStoreFile != null) {
                if (_logger.isLoggable(Level.FINE)) {
                    _logger.log(Level.FINE, "Loading keystoreFile = {0}, keystorePass = {1}",
                            new Object[]{keyStoreFile, keyStorePass});
                }
                istream = new FileInputStream(keyStoreFile);
                bstream = new BufferedInputStream(istream);
            }

            ks.load(bstream, passphrase);
        } finally {
            if (bstream != null) {
                bstream.close();
            }
            if (istream != null) {
                istream.close();
            }
        }
        return ks;
    }

    // --- implements SecuritySupport ---
    /**
     * This method returns an array of keystores containing keys and
     * certificates.
     */
    public KeyStore[] getKeyStores() {
        return keyStores.toArray(new KeyStore[keyStores.size()]);
    }

    public KeyStore loadNullStore(String type, int index) throws KeyStoreException,
            IOException, NoSuchAlgorithmException, CertificateException {
        KeyStore ret = KeyStore.getInstance(type);
        ret.load(null, keyStorePasswords.get(index));
        return ret;
    }

    public KeyManager[] getKeyManagers(String algorithm) throws IOException,
            KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException {
        KeyStore[] kstores = getKeyStores();
        ArrayList<KeyManager> keyManagers = new ArrayList<KeyManager>();
        for (int i = 0; i < kstores.length; i++) {
            checkCertificateDates(kstores[i]);
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(
                    (algorithm != null) ? algorithm : KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(kstores[i], keyStorePasswords.get(i));
            KeyManager[] kmgrs = kmf.getKeyManagers();
            if (kmgrs != null) {
                keyManagers.addAll(Arrays.asList(kmgrs));
            }
        }

        KeyManager keyManager = new UnifiedX509KeyManager(
                keyManagers.toArray(new X509KeyManager[keyManagers.size()]),
                getTokenNames());
        return new KeyManager[]{keyManager};
    }

    public TrustManager[] getTrustManagers(String algorithm) throws IOException,
            KeyStoreException, NoSuchAlgorithmException {
        KeyStore[] tstores = getTrustStores();
        ArrayList<TrustManager> trustManagers = new ArrayList<TrustManager>();
        for (KeyStore tstore : tstores) {
            checkCertificateDates(tstore);
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(
                    (algorithm != null) ? algorithm : TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(tstore);
            TrustManager[] tmgrs = tmf.getTrustManagers();
            if (tmgrs != null) {
                trustManagers.addAll(Arrays.asList(tmgrs));
            }
        }
        TrustManager trustManager;
        if (trustManagers.size() == 1) {
            trustManager = trustManagers.get(0);
        } else {
            trustManager = new UnifiedX509TrustManager(trustManagers.toArray(new X509TrustManager[trustManagers.size()]));
        }
        return new TrustManager[]{trustManager};
    }
    /*
     * Check X509 certificates in a store for expiration.
     */

    private void checkCertificateDates(KeyStore store)
            throws KeyStoreException {

        Enumeration<String> aliases = store.aliases();
        while (aliases.hasMoreElements()) {
            Certificate cert = store.getCertificate(aliases.nextElement());
            if (cert instanceof X509Certificate) {
                if (((X509Certificate) cert).getNotAfter().before(initDate)) {
                    _logger.log(Level.SEVERE,
                            "java_security.expired_certificate",
                            cert);
                }
            }
        }
    }

    /**
     * This method returns an array of truststores containing certificates.
     */
    public KeyStore[] getTrustStores() {
        return trustStores.toArray(new KeyStore[trustStores.size()]);
    }

    public boolean verifyMasterPassword(final char[] masterPass) {
        return Arrays.equals(masterPass, keyStorePasswords.get(0));
    }

    /**
     * This method returns an array of token names in order corresponding to
     * array of keystores.
     */
    public String[] getTokenNames() {
        return tokenNames.toArray(new String[tokenNames.size()]);
    }

    /**
     * @param  token 
     * @return a keystore
     */
    public KeyStore getKeyStore(String token) {
        int idx = getTokenIndex(token);
        if (idx < 0) {
            return null;
        }
        return keyStores.get(idx);
    }

    /**
     * @param  token 
     * @return a truststore
     */
    public KeyStore getTrustStore(String token) {
        int idx = getTokenIndex(token);
        if (idx < 0) {
            return null;
        }
        return trustStores.get(idx);
    }

    /**
     * @return returned index 
     */
    private int getTokenIndex(String token) {
        int idx = -1;
        if (token != null) {
            idx = tokenNames.indexOf(token);
            if (idx < 0 && _logger.isLoggable(Level.FINEST)) {
                _logger.log(Level.FINEST, "token {0} is not found", token);
            }
        }
        return idx;
    }

    public void synchronizeKeyFile(Object configContext, String fileRealmName) throws Exception {
        //throw new UnsupportedOperationException("Not supported yet in V3.");
    }

    public void checkPermission(String key) {
        try {
            // Checking a random permission to check if it is server.
            if(isEmbeddedServer() || habitat == null
                    || isACC() || isNotServerORACC()){
                return;
            }
            Permission perm = new RuntimePermission("SSLPassword");
            AccessController.checkPermission(perm);
        } catch (AccessControlException e) {
            String message = e.getMessage();
            Permission perm = new PropertyPermission(key, "read");
            if (message != null) {
                message = message.replace(e.getPermission().toString(), perm.toString());
            }
            throw new AccessControlException(message, perm);
        }
    }

    public boolean isACC() {
        return penv.getProcessType().equals(ProcessType.ACC);
    }

    public boolean isNotServerORACC() {
        return penv.getProcessType().equals(ProcessType.Other);
    }

    public PrivateKey getPrivateKeyForAlias(String alias, int keystoreIndex) throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException {
        checkPermission(KEYSTORE_PASS_PROP);
        Key key = keyStores.get(keystoreIndex).getKey(alias, keyStorePasswords.get(keystoreIndex));
        if (key instanceof PrivateKey) {
            return (PrivateKey) key;
        } else {
            return null;
        }
    }
}
