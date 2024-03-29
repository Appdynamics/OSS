/*
 *
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2008 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 *
 * Contributor(s):
 *
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
package com.sun.enterprise.v3.admin;

import com.sun.enterprise.module.bootstrap.StartupContext;
import com.sun.enterprise.security.store.PasswordAdapter;
import com.sun.enterprise.security.store.IdentityManagement;
import com.sun.enterprise.util.SystemPropertyConstants;
import org.glassfish.internal.api.Init;
import org.glassfish.server.ServerEnvironmentImpl;
import org.jvnet.hk2.annotations.Inject;
import org.jvnet.hk2.annotations.Scoped;
import org.jvnet.hk2.annotations.Service;
import org.jvnet.hk2.component.PostConstruct;
import org.jvnet.hk2.component.Singleton;

import java.io.*;
import java.util.Properties;
import java.util.logging.Logger;
import java.security.KeyStore;

/** An implementation of the @link {IdentityManagement} that manages the password needs of the server.
 *  This implementation consults the Java KeyStore and assumes that the stores are available in server's
 *  configuration area.
 * @author &#2325;&#2375;&#2342;&#2366;&#2352 (km@dev.java.net)
 */
@Service(name="jks-based")
@Scoped(Singleton.class)
public class IdmService implements Init, PostConstruct, IdentityManagement {

    private final Logger logger = Logger.getAnonymousLogger();

    @Inject
    private volatile StartupContext sc = null;

    @Inject
    private volatile ServerEnvironmentImpl env = null;

    private volatile String masterPassword;

    //private final Map<String, String> otherPasswords = Collections.synchronizedMap(new HashMap<String, String>());  //TODO later

    private static final String FIXED_KEY = "master-password"; //the fixed key for master-password file
    private static final String PASSWORDFILE_OPTION_TO_ASMAIN = "-passwordfile"; //note single hyphen, in line with other args to ASMain!
    private static final String STDIN_OPTION_TO_ASMAIN        = "-read-stdin"; //note single hyphen, in line with other args to ASMain!

    private static final String MP_PROPERTY = "AS_ADMIN_MASTERPASSWORD";

    public void postConstruct() {
        boolean success;
        boolean readStdin = sc.getArguments().containsKey(STDIN_OPTION_TO_ASMAIN);
        if (readStdin) {
            success = setFromStdin();
        } else {
            success = setFromMasterPasswordFile();
            if (!success) {
                success = setFromAsMainArguments();
            }
        }
        if (!success) {
            masterPassword = "changeit"; //the default;
        }
        //success = verify();            //See 9592 for details. This saves some time
        //if (!success)
            //logger.warning("THIS SHOULD BE FIXED, IN EMBEDDED CASE, THERE IS NO MASTER PASSWORD SET OR KEYSTORE DOES NOT EXIST ...");
        setJSSEProperties();
        com.sun.enterprise.security.store.IdentityManager.setMasterPassword(masterPassword);
    }

    private void setJSSEProperties() {
        System.setProperty(SystemPropertyConstants.KEYSTORE_PASSWORD_PROPERTY, masterPassword);
        System.setProperty(SystemPropertyConstants.TRUSTSTORE_PASSWORD_PROPERTY, masterPassword);
    }

    /** Returns the master password. Others are expected to call this method instead of relying on JSSE properties.
     *
     * @return char[] as a master password. It's guaranteed that this password is valid
     */
    public char[] getMasterPassword() {
        return masterPassword.toCharArray();
    }

    ///// All Private
    
    private boolean setFromMasterPasswordFile() {
//        long t0 = System.currentTimeMillis();
        try {
            File mp = env.getMasterPasswordFile();
            if (!mp.isFile()) {
                logger.fine("The JCEKS file: " + mp.getAbsolutePath() + " does not exist, master password was not saved on disk during domain creation");
                return false;
            }
            PasswordAdapter p   = new PasswordAdapter(mp.getAbsolutePath(), FIXED_KEY.toCharArray());
            this.masterPassword = p.getPasswordForAlias(FIXED_KEY);
//            long t1 = System.currentTimeMillis();
//            System.out.println("time spent in setFromMasterPasswordFile(): " + (t1-t0) + " ms");
            return true;
        } catch (Exception ex) {
            logger.fine("Error in master-password processing: " + ex.getMessage());
            return false;
        }

    }

    private boolean setFromAsMainArguments() {
        File pwf = null;
        try {
            String[] args = sc.getOriginalArguments();
            int index = 0;
            for (String arg : args) {
                if (PASSWORDFILE_OPTION_TO_ASMAIN.equals(arg)) {
                    if (index == (args.length-1)) {  //-passwordfile is the last argument
                        logger.warning("-passwordfile specified, but the actual file was not, ignoring ...");
                        return false;
                    }
                    pwf = new File(args[index+1]);
                    return readPasswordFile(pwf);
                }
                index++;
            }
            //no -passwordfile found
            return false;
        } catch (Exception ex) {
            String s = "Something wrong with given password file: ";
            String msg = pwf == null ? s : s + pwf.getAbsolutePath();
            logger.fine(msg);
            return false;
        }
    }

    private boolean readPasswordFile(File pwf) {
        Properties p = new Properties();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(pwf));
            p.load(br);
            String mp = p.getProperty(MP_PROPERTY);
            if (mp == null)
                return false;
            masterPassword = mp;  //this would stay in memory, so this needs some security audit, frankly
            return true;
        } catch (IOException e) {
            logger.fine("Passwordfile: " + pwf.getAbsolutePath() + " (a simple property file) could not be processed, ignoring ...");
            return false;
        } finally {
            try {
                if (br != null)
                    br.close();
            } catch(Exception e) {
                // ignore, I know
            }
        }
    }

    private boolean setFromStdin() {
        logger.fine("Reading the master password from stdin> ");
        String s;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            while ((s = br.readLine()) != null) {
                int ind = s.indexOf(MP_PROPERTY);
                if (ind == -1) {
                    return false; // this means stdin isn't behaving. That's bad and shouldn't happen.
                }
                masterPassword = s.substring(MP_PROPERTY.length() + 1); //begIndex is that of "AS_ADMIN_MASTERPASSWORD=; consider trailing '='
            }
            logger.fine("******************* Password from stdin: " + masterPassword);
            return true;
        } catch(Exception e) {
            logger.fine("Stdin isn't behaving, ignoring it ..." + e.getMessage());
            return false;
        }
    }

    private boolean verify() {
//        long t0 = System.currentTimeMillis();
        //only tries to open the keystore
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(env.getJKS());
            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            ks.load(fis, masterPassword.toCharArray());
//            long t1 = System.currentTimeMillis();
//            System.out.println("time spent in verify(): " + (t1-t0) + " ms");
            return true;
        } catch (Exception e) {
            logger.warning(e.getMessage());
            return false;
        } finally {
            try {
                if (fis != null)
                    fis.close();
            } catch(IOException ioe) {
                //ignore, I know ...
            }
        }
    }
}
