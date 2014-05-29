/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
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

package org.glassfish.appclient.server.core.jws.servedcontent;

import com.sun.enterprise.security.ssl.JarSigner;
import com.sun.enterprise.util.i18n.StringManager;
import com.sun.logging.LogDomains;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jvnet.hk2.annotations.Scoped;
import org.jvnet.hk2.annotations.Service;
import org.jvnet.hk2.component.PostConstruct;
import org.jvnet.hk2.component.Singleton;

/**
 * Signs a specified JAR file.
 *<p>
 *This implementation searches the available keystores for the signing alias
 *indicated in the domain.xml config or, if not specified, the default alias,
 *the first time it is invoked to sign a JAR file.  After the first requested
 *signing it uses the same alias and provider to sign all JARs.
 *<p>
 *The public interface to this class is the static signJar method.  
 *
 * @author tjquinn
 */
@Service
@Scoped(Singleton.class)
public class ASJarSigner implements PostConstruct {
    
    /** property name optionally set by the admin in domain.xml to select an alias for signing */
    public static final String USER_SPECIFIED_ALIAS_PROPERTYNAME = "com.sun.aas.jws.signing.alias";

    /** keystore type for JKS keystores */
    private static final String JKS_KEYSTORE_TYPE_VALUE = "jks";
    
    /** default alias for signing if the admin does not specify one */
    private static final String DEFAULT_ALIAS_VALUE = "s1as";

    private static final String DEFAULT_DIGEST_ALGORITHM = "SHA1";
    private static final String DEFAULT_KEY_ALGORITHM = "RSA";

//    /** user-specified signing alias */
//    private final String userAlias; // = System.getProperty(USER_SPECIFIED_ALIAS_PROPERTYNAME);
    
    private static final StringManager localStrings = StringManager.getManager(ASJarSigner.class);

    private Logger logger;

    public void postConstruct() {
        logger =
            LogDomains.getLogger(ASJarSigner.class,
            LogDomains.CORE_LOGGER);
    }

    /**
     *Creates a signed jar from the specified unsigned jar.
     *@param unsignedJar the unsigned JAR file
     *@param signedJar the signed JAR to be created
     *@return the elapsed time to sign the JAR (in milliseconds)
     *@throws Exception getting the keystores from SSLUtils fails
     */
    public long signJar(final File unsignedJar, final File signedJar,
        String alias) throws Exception {

        if (alias == null) {
            alias = DEFAULT_ALIAS_VALUE;
        }
        long startTime = System.currentTimeMillis();
        long duration = 0;
        synchronized(this) {
            try {
                JarSigner jarSigner = new JarSigner(DEFAULT_DIGEST_ALGORITHM,
                        DEFAULT_KEY_ALGORITHM);
                jarSigner.signJar(unsignedJar, signedJar, alias);
            } catch (Throwable t) {
                /*
                 *In case of any problems, make sure there is no ill-formed signed
                 *jar file left behind.
                 */
                signedJar.delete();

                /*
                 *The jar signer will have written some information to System.out
                 *and/or System.err.  Refer the user to those earlier messages.
                 */
                throw new Exception(localStrings.getString("jws.sign.errorSigning", 
                        signedJar.getAbsolutePath(), alias), t);
            } finally {
                duration = System.currentTimeMillis() - startTime;
                logger.log(Level.FINE, "Signing {0} took {1} ms",
                        new Object[]{unsignedJar.getAbsolutePath(), duration});
            }
        }
        return duration;
    }

    /**
     *Wraps any underlying exception.
     *<p>
     *This is primarily used to insulate calling logic from
     *the large variety of exceptions that can occur during signing
     *from which the caller cannot really recover.
     */
    public static class ASJarSignerException extends Exception {
        public ASJarSignerException(String msg, Throwable t) {
            super(msg, t);
        }
    }
}
