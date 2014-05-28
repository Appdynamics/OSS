/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2008-2011 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.grizzly;

import com.sun.grizzly.filter.EchoFilter;
import com.sun.grizzly.filter.ReadFilter;
import com.sun.grizzly.utils.ControllerUtils;
import com.sun.grizzly.utils.TCPIOClient;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestCase;

/**
 * Tests a <code>BaseSelectionKeyHandler</code> configuration, (no keep alive).
 * 
 * @author charlie hunt
 */
public class BaseSelectionKeyHandlerTest extends TestCase {
    private static final int NUMBER_OF_ITERATIONS = 2;
    private static final int KEEP_ALIVE_TIMEOUT = 35 * 1000;
    private static final int PORT = 17501;
    
    public void testSimplePacket() throws IOException {
        // set up server side
        final Controller controller = new Controller();
        final TCPSelectorHandler selectorHandler = new TCPSelectorHandler();
        selectorHandler.setPort(PORT);
        selectorHandler.setSelectionKeyHandler(new BaseSelectionKeyHandler());
        controller.addSelectorHandler(selectorHandler);
        final ProtocolChainInstanceHandler pciHandler =
                new ProtocolChainInstanceHandler() {
            final private ProtocolChain protocolChain =
                                  new DefaultProtocolChain();
            public ProtocolChain poll() {
                return protocolChain;
            }
            public boolean offer(ProtocolChain pc) {
                return true;
            }
        };
        controller.setProtocolChainInstanceHandler(pciHandler);
        pciHandler.poll().addFilter(new ReadFilter());
        pciHandler.poll().addFilter(new EchoFilter());

        // set up client side, start controller & run test
        List<TCPIOClient> clients = null;        
        try {
            final byte[] testData = makeDataToSend();
            ControllerUtils.startController(controller);
            clients = initializeClients("localhost", PORT, 2);
            for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
                for (TCPIOClient client : clients) {
                    client.send(testData);
                    byte[] response = new byte[testData.length];
                    client.receive(response);
                    assertTrue(Arrays.equals(testData, response));
                }
            }
            Controller.logger().log(Level.INFO,
                    "Checking if keep alive is active ...");
            Controller.logger().log(Level.INFO, "Sorry, have to wait for (" + 
                    KEEP_ALIVE_TIMEOUT/1000 + ") " + "seconds ...");
            Thread.sleep(KEEP_ALIVE_TIMEOUT);
            // Clients should still be connected, if not, then we have problem.
            try {
                for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
                    for (TCPIOClient client : clients) {
                        client.send(testData);
                        byte[] response = new byte[testData.length];
                        client.receive(response);
                        assertTrue(Arrays.equals(testData, response));
                    }
                }
            Controller.logger().log(Level.INFO, 
                           "Got the expected result, " +
                           "keep alive was not active ...");
                assertTrue("Got the expected result, " +
                           "keep alive was not active ...", true);
            } catch (IOException ex) {
                assertTrue("Client reporting unexpected error: " + 
                           ex.toString(), false);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(BaseSelectionKeyHandlerTest.class.getName()).
                             log(Level.SEVERE, null, ex);
        } finally {
            if (clients != null) {
                closeClients(clients);
            }
            controller.stop();
        }
    }
    
    
    private List<TCPIOClient> initializeClients(String host, int port, 
                                                int count) throws IOException {
        final List<TCPIOClient> clients = new ArrayList<TCPIOClient>(count);
        for(int i=0; i<count; i++) {
            final TCPIOClient client = new TCPIOClient(host, port);
            client.connect();
            clients.add(client);
        }
        return clients;
    }
    
    private void closeClients(List<TCPIOClient> clients) {
        for(TCPIOClient client : clients) {
            try {
                client.close();
            } catch (IOException ex) {
                Logger.getLogger(BaseSelectionKeyHandlerTest.class.getName()).
                                 log(Level.SEVERE, null, ex);
            }
        }
    }

    private byte[] makeDataToSend() {
        final int TARGET_MSG_FACTOR = 12;  
        // total message length = TARGET_MSG_SIZE x msg.length x # of bytes per char
        final String msg = "Hello Grizzly, How is the bear today? ";
        final StringBuilder sb = new StringBuilder(msg.length()*TARGET_MSG_FACTOR );
        for (int i = 0; i < TARGET_MSG_FACTOR ; i++) {
            sb.append(msg);
        }
        return sb.toString().getBytes();
    }
}
