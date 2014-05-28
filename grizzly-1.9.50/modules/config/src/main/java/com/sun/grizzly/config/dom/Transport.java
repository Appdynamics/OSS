/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2009-2012 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.grizzly.config.dom;

import org.jvnet.hk2.component.Injectable;
import org.jvnet.hk2.config.Attribute;
import org.jvnet.hk2.config.ConfigBeanProxy;
import org.jvnet.hk2.config.Configured;
import org.jvnet.hk2.config.DuckTyped;
import org.jvnet.hk2.config.types.PropertyBag;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines one specific transport and its properties
 */
@Configured
public interface Transport extends ConfigBeanProxy, Injectable, PropertyBag {
    boolean DISPLAY_CONFIGURATION = false;
    boolean ENABLE_SNOOP = false;
    boolean TCP_NO_DELAY = true;
    boolean KEEPALIVE = false;
    int ACCEPTOR_THREADS = 1;
    int BUFFER_SIZE = 8192;
    int IDLE_KEY_TIMEOUT = 30;
    int LINGER = -1;
    int MAX_CONNECTIONS_COUNT = 4096;
    int READ_TIMEOUT = 30000;
    int SELECTOR_POLL_TIMEOUT = 1000;
    String BYTE_BUFFER_TYPE = "HEAP";
    String CLASSNAME = "com.sun.grizzly.TCPSelectorHandler";

    /**
     * The number of acceptor threads listening for the transport's events
     */
    @Attribute(defaultValue = "" + ACCEPTOR_THREADS, dataType = Integer.class)
    String getAcceptorThreads();

    void setAcceptorThreads(String value);

    @Attribute(defaultValue = "" + BUFFER_SIZE, dataType = Integer.class)
    String getBufferSizeBytes();

    void setBufferSizeBytes(String size);

    /**
     * Type of ByteBuffer, which will be used with transport. Possible values are: HEAP and DIRECT
     */
    @Attribute(defaultValue = BYTE_BUFFER_TYPE)
    String getByteBufferType();

    void setByteBufferType(String value);

    /**
     * Name of class, which implements transport logic
     */
    @Attribute(defaultValue = CLASSNAME)
    String getClassname();

    void setClassname(String value);

    /**
     * Flush Grizzly's internal configuration to the server logs (like number of threads created, how many polled
     * objects, etc.)
     */
    @Attribute(defaultValue = "" + DISPLAY_CONFIGURATION, dataType = Boolean.class)
    String getDisplayConfiguration();

    void setDisplayConfiguration(String bool);

    /**
     * Dump the requests/response information in server.log. Useful for debugging purpose, but significantly reduce
     * performance as the request/response bytes are translated to String.
     */
    @Attribute(defaultValue = "" + ENABLE_SNOOP, dataType = Boolean.class)
    String getEnableSnoop();

    void setEnableSnoop(String bool);

    /**
     * Timeout, after which idle key will be cancelled and channel closed
     */
    @Attribute(defaultValue = "" + IDLE_KEY_TIMEOUT, dataType = Integer.class)
    String getIdleKeyTimeoutSeconds();

    void setIdleKeyTimeoutSeconds(String value);

    /**
     * The max number of connections the transport should handle at the same time
     */
    @Attribute(defaultValue = "" + MAX_CONNECTIONS_COUNT, dataType = Integer.class)
    String getMaxConnectionsCount();

    void setMaxConnectionsCount(String value);

    /**
     * Transport's name, which could be used as reference
     */
    @Attribute(required = true, key = true)
    String getName();

    void setName(String value);

    /**
     * Read operation timeout in ms
     */
    @Attribute(defaultValue = "" + READ_TIMEOUT, dataType = Integer.class)
    String getReadTimeoutMillis();

    void setReadTimeoutMillis(String value);

    /**
     * Use public SelectionKey handler, which was defined earlier in the document.
     */
    @Attribute
    String getSelectionKeyHandler();

    void setSelectionKeyHandler(String value);

    /**
     * The time, in milliseconds, a NIO Selector will block waiting for events (users requests).
     */
    @Attribute(defaultValue = "" + SELECTOR_POLL_TIMEOUT, dataType = Integer.class)
    String getSelectorPollTimeoutMillis();

    void setSelectorPollTimeoutMillis(String timeout);

    /**
     * Write operation timeout in ms
     */
    @Attribute(defaultValue = "" + READ_TIMEOUT, dataType = Integer.class)
    String getWriteTimeoutMillis();

    void setWriteTimeoutMillis(String value);

    @Attribute(defaultValue = "" + TCP_NO_DELAY, dataType = Boolean.class)
    String getTcpNoDelay();

    void setTcpNoDelay(String noDelay);

    @Attribute(defaultValue = "" + LINGER, dataType = Integer.class)
    String getLinger();

    void setLinger(String linger);

    @Attribute(defaultValue = "" + KEEPALIVE, dataType = Boolean.class)
    String getKeepAlive();

    void setKeepAlive(String keepAlive);

    @DuckTyped
    List<NetworkListener> findNetworkListeners();

    @DuckTyped
    Transports getParent();

    class Duck {
        static public List<NetworkListener> findNetworkListeners(Transport transport) {
            NetworkListeners networkListeners =
                    transport.getParent().getParent().getNetworkListeners();
            List<NetworkListener> refs = new ArrayList<NetworkListener>();
            for (NetworkListener listener : networkListeners.getNetworkListener()) {
                if (listener.getTransport().equals(transport.getName())) {
                    refs.add(listener);
                }
            }
            return refs;
        }

        public static Transports getParent(Transport transport) {
            return transport.getParent(Transports.class);
        }
    }
}
