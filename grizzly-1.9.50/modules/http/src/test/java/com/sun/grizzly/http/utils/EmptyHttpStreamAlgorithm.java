/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2007-2010 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.grizzly.http.utils;

import com.sun.grizzly.http.SelectorThread;
import com.sun.grizzly.standalone.StaticStreamAlgorithm;
import com.sun.grizzly.util.OutputWriter;
import com.sun.grizzly.util.SSLOutputWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

/**
 * Algorithm sends echo HTTP response to sender
 *
 * @author Alexey Stashok
 */
public class EmptyHttpStreamAlgorithm extends StaticStreamAlgorithm {
    private static AtomicInteger requestNum = new AtomicInteger(0);
    
    /**
     * HTTP end line.
     */
    private final static String NEWLINE = "\r\n";
    
    /**
     * Header String.
     */
    private final static String headers = "Connection:close" + NEWLINE
                + "Cache-control: private" + NEWLINE
                + "Content-Type: application/octet-stream" + NEWLINE
                + "Content-Length: ";
    
    /**
     * HTTP SC_FOUND header.
     */
    private final static ByteBuffer SC_FOUND =
            ByteBuffer.wrap( ("HTTP/1.1 200 OK" 
                + NEWLINE).getBytes());


    private boolean isSecured;
    
    public EmptyHttpStreamAlgorithm() {
    }

    public EmptyHttpStreamAlgorithm(boolean isSecured) {
        this.isSecured = isSecured;
    }

    @Override
    public boolean parse(ByteBuffer buffer) {
        buffer.flip();
        if (buffer.hasRemaining()) {
            byte[] data = new byte[buffer.remaining()];
            int position = buffer.position();
            buffer.get(data);
            buffer.position(position);

            try {
                ByteBuffer intBuf = ByteBuffer.allocate(4);
                int reply = requestNum.incrementAndGet();
                intBuf.putInt(0, reply);
                
                if (!isSecured) {
                    OutputWriter.flushChannel(socketChannel, SC_FOUND.slice());
                    OutputWriter.flushChannel(socketChannel, ByteBuffer.wrap((headers + 4 + NEWLINE + NEWLINE).getBytes()));
                    OutputWriter.flushChannel(socketChannel, intBuf);
                } else {
                    SSLOutputWriter.flushChannel(socketChannel, SC_FOUND.slice());
                    SSLOutputWriter.flushChannel(socketChannel, ByteBuffer.wrap((headers + 4 + NEWLINE + NEWLINE).getBytes()));
                    SSLOutputWriter.flushChannel(socketChannel, intBuf);
                }
            } catch (IOException e) {
                SelectorThread.logger().log(Level.FINE, 
                        "Exception (could happen if http request comes in " +
                        "several chunks. If yes - ignore) HttpEcho \"" 
                        + new String(data) + "\"", e);
            }
        }

        buffer.clear();
        return false;
    }
    
    public static void resetRequestCounter() {
        requestNum.set(0);
    }
}
