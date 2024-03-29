/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
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

package com.sun.enterprise.iiop.security;

import com.sun.enterprise.deployment.EjbDescriptor;
import com.sun.enterprise.security.ssl.J2EEKeyManager;
import com.sun.enterprise.security.ssl.SSLUtils;
import com.sun.logging.LogDomains;
import java.security.SecureRandom;
import java.text.MessageFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.KeyManager;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509KeyManager;
import org.glassfish.enterprise.iiop.api.GlassFishORBHelper;
import org.glassfish.enterprise.iiop.api.IIOPSSLUtil;
import org.glassfish.internal.api.SharedSecureRandom;
import org.jvnet.hk2.annotations.Inject;
import org.jvnet.hk2.annotations.Scoped;
import org.jvnet.hk2.annotations.Service;
import org.jvnet.hk2.component.Habitat;
import org.jvnet.hk2.component.Singleton;
import org.omg.IOP.TaggedComponent;
import org.omg.PortableInterceptor.IORInfo;
/**
 *
 * @author Kumar
 */
@Service
@Scoped(Singleton.class)
public class IIOPSSLUtilImpl implements IIOPSSLUtil {
    @Inject
    private SSLUtils sslUtils;
    @Inject
    private Habitat habitat;
    
    private GlassFishORBHelper orbHelper;
    
    private static final Logger _logger ;
    static{
	_logger = LogDomains.getLogger(IIOPSSLUtilImpl.class,LogDomains.SECURITY_LOGGER);
    }
    private Object  appClientSSL;
    
    public Object getAppClientSSL() {
        return this.appClientSSL;
    }
    public void setAppClientSSL(Object ssl) {
        this.appClientSSL = ssl;
    }
    
    public KeyManager[] getKeyManagers(String alias) {
        KeyManager[] mgrs = null;
        try {
            if (alias != null && !sslUtils.isTokenKeyAlias(alias)) {
                throw new IllegalStateException(getFormatMessage(
                        "iiop.cannot_find_keyalias", new Object[]{alias                        }));
            }

            mgrs = sslUtils.getKeyManagers();
            if (alias != null && mgrs != null && mgrs.length > 0) {
                KeyManager[] newMgrs = new KeyManager[mgrs.length];
                for (int i = 0; i < mgrs.length; i++) {
                    if (_logger.isLoggable(Level.FINE)) {
                        StringBuffer msg = new StringBuffer("Setting J2EEKeyManager for ");
                        msg.append(" alias : " + alias);
                        _logger.log(Level.FINE, msg.toString());
                    }
                    newMgrs[i] = new J2EEKeyManager(habitat, (X509KeyManager) mgrs[i], alias);
                }
                mgrs = newMgrs;
            }
        } catch (Exception e) {
            //TODO: log here
            throw new RuntimeException(e);
        }
        return mgrs;
    }
    public TrustManager[] getTrustManagers() {
        try {
        return sslUtils.getTrustManagers();
        } catch (Exception e) {
            //TODO: log here
            throw new RuntimeException(e);
        }
    }
    
     /**
     * This API get the format string from resource bundle of _logger.
     * @param key the key of the message
     * @param params the parameter array of Object
     * @return the format String for _logger
     */
    private String getFormatMessage(String key, Object[] params) {
        return MessageFormat.format(
            _logger.getResourceBundle().getString(key), params);
    }

    public SecureRandom getInitializedSecureRandom() {
        return SharedSecureRandom.get();
    }
    
     public Object getSSLPortsAsSocketInfo(Object ior) {         
          SecurityMechanismSelector selector = Lookups.getSecurityMechanismSelector(habitat);
          return selector.getSSLSocketInfo(ior);
     }
     
     public TaggedComponent createSSLTaggedComponent(IORInfo iorInfo, Object sInfos) {
         List<com.sun.corba.ee.spi.folb.SocketInfo> socketInfos = (List<com.sun.corba.ee.spi.folb.SocketInfo>)sInfos;
         orbHelper = Lookups.getGlassFishORBHelper(habitat);
         TaggedComponent result = null;
          org.omg.CORBA.ORB orb = orbHelper.getORB();
            int sslMutualAuthPort = -1;
            try {
                sslMutualAuthPort = 
		    ((com.sun.corba.ee.spi.legacy.interceptor.IORInfoExt)iorInfo).  
			getServerPort("SSL_MUTUALAUTH");
            } catch (com.sun.corba.ee.spi.legacy.interceptor.UnknownType ute) {
                _logger.log(Level.FINE,".isnert: UnknownType exception", ute);
            }

            if(_logger.isLoggable(Level.FINE)) {
                _logger.log(Level.FINE, 
                            ".insert: sslMutualAuthPort: " 
                            + sslMutualAuthPort);
            }

            CSIV2TaggedComponentInfo ctc = new CSIV2TaggedComponentInfo( orb,
	    sslMutualAuthPort, habitat );
	    EjbDescriptor desc = ctc.getEjbDescriptor(iorInfo) ;
            result = ctc.createSecurityTaggedComponent(socketInfos,desc);  
            return result;
     }

}
