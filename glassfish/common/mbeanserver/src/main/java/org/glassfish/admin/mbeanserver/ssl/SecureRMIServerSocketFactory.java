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

package org.glassfish.admin.mbeanserver.ssl;

import com.sun.grizzly.config.SSLConfigHolder;
import com.sun.grizzly.config.dom.Ssl;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLServerSocket;
import javax.rmi.ssl.SslRMIServerSocketFactory;
import org.glassfish.admin.mbeanserver.JMXSslConfigHolder;
import org.glassfish.admin.mbeanserver.Util;
import org.jvnet.hk2.component.Habitat;

/**
 *
 * @author prasad
 */
    /**
     * Inner class for SSL support for JMX connection using RMI.
     */
    public class SecureRMIServerSocketFactory
            extends SslRMIServerSocketFactory {

        private final InetAddress mAddress;
        private final Ssl ssl;
        // The list of cipher suite
        private volatile String[] enabledCipherSuites = null;
        //the list of protocols
        private volatile String[] enabledProtocols = null;
        private final Object cipherSuitesSync = new Object();
        private final Object protocolsSync = new Object();
        private final Habitat habitat;
        private Map socketMap = new HashMap<Integer, Socket>();

        public SecureRMIServerSocketFactory(final Ssl sslConfig,
                                            final Habitat habitat,
                                            final InetAddress addr) {
            mAddress = addr;
            ssl = sslConfig;
            this.habitat = habitat;
            Util.getLogger().info("Creating a SecureRMIServerSocketFactory @ " +
                    addr.getHostAddress() + "with ssl config = " + ssl.toString());

        }


        @Override
        public boolean equals(Object obj) {
            if (obj instanceof SecureRMIServerSocketFactory) {
                 return(this.hashCode()==obj.hashCode());
            } else  {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return ssl.hashCode() + mAddress.hashCode();
        }

        @Override
        public ServerSocket createServerSocket(int port) throws IOException {
            //debug( "MyRMIServerSocketFactory.createServerSocket(): " + mAddress + " : " + port );
            if(socketMap.containsKey(new Integer(port))) {
                return (ServerSocket)socketMap.get(new Integer(port));
            }

            final int backlog = 5;  // plenty
            // we use a custom class here. The reason is mentioned in the class.
            final JMXSslConfigHolder sslConfigHolder;
            try {
                sslConfigHolder = new JMXSslConfigHolder(ssl, habitat);
            } catch (SSLException ssle) {
                throw new IllegalStateException(ssle);
            }

            sslConfigHolder.configureSSL();
            final SSLContext context = sslConfigHolder.getSSLContext();
            SSLServerSocket sslSocket =
                    (SSLServerSocket) context.getServerSocketFactory().
                    createServerSocket(port, backlog, mAddress);
            configureSSLSocket(sslSocket, sslConfigHolder);
            Util.getLogger().info("SSLServerSocket " +
                    sslSocket.getLocalSocketAddress() + "and "+ sslSocket.toString()+" created");

            //sslSocket.startHandshake();
            //debug( "MyRMIServerSocketFactory.createServerSocket(): " + mAddress + " : " + port );
            socketMap.put(new Integer(port), sslSocket);
            return sslSocket;
        }

        private void configureSSLSocket(SSLServerSocket sslSocket,
                SSLConfigHolder sslConfigHolder) {
            if (sslConfigHolder.getEnabledCipherSuites() != null) {
                if (enabledCipherSuites == null) {
                    synchronized (cipherSuitesSync) {
                        if (enabledCipherSuites == null) {
                            enabledCipherSuites = configureEnabledCiphers(sslSocket,
                                    sslConfigHolder.getEnabledCipherSuites());
                        }
                    }
                }

                sslSocket.setEnabledCipherSuites(enabledCipherSuites);
            }

            if (sslConfigHolder.getEnabledProtocols() != null) {
                if (enabledProtocols == null) {
                    synchronized (protocolsSync) {
                        if (enabledProtocols == null) {
                            enabledProtocols = configureEnabledProtocols(sslSocket,
                                    sslConfigHolder.getEnabledProtocols());
                        }
                    }
                }
                sslSocket.setEnabledProtocols(enabledProtocols);
            }

            sslSocket.setUseClientMode(sslConfigHolder.isClientMode());
        }

        /**
         * Return the list of allowed protocol.
         * @return String[] an array of supported protocols.
         */
        private final static String[] configureEnabledProtocols(
                SSLServerSocket socket, String[] requestedProtocols) {

            String[] supportedProtocols = socket.getSupportedProtocols();
            String[] protocols = null;
            ArrayList<String> list = null;
            for (String supportedProtocol : supportedProtocols) {
                /*
                 * Check to see if the requested protocol is among the
                 * supported protocols, i.e., may be enabled
                 */
                for (String protocol : requestedProtocols) {
                    protocol = protocol.trim();
                    if (supportedProtocol.equals(protocol)) {
                        if (list == null) {
                            list = new ArrayList<String>();
                        }
                        list.add(protocol);
                        break;
                    }
                }
            }

            if (list != null) {
                protocols = list.toArray(new String[list.size()]);
            }

            return protocols;
        }

        /**
         * Determines the SSL cipher suites to be enabled.
         *
         * @return Array of SSL cipher suites to be enabled, or null if none of the
         * requested ciphers are supported
         */
        private final static String[] configureEnabledCiphers(SSLServerSocket socket,
                String[] requestedCiphers) {

            String[] supportedCiphers = socket.getSupportedCipherSuites();
            String[] ciphers = null;
            ArrayList<String> list = null;
            for (String supportedCipher : supportedCiphers) {
                /*
                 * Check to see if the requested protocol is among the
                 * supported protocols, i.e., may be enabled
                 */
                for (String cipher : requestedCiphers) {
                    cipher = cipher.trim();
                    if (supportedCipher.equals(cipher)) {
                        if (list == null) {
                            list = new ArrayList<String>();
                        }
                        list.add(cipher);
                        break;
                    }
                }
            }

            if (list != null) {
                ciphers = list.toArray(new String[list.size()]);
            }

            return ciphers;
        }
    }