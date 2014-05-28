/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2009-2010 Oracle and/or its affiliates. All rights reserved.
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
 *
 *
 * This file incorporates work covered by the following copyright and
 * permission notice:
 *
 * Copyright 2004 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sun.grizzly.http.jk.common;

import com.sun.grizzly.http.jk.core.JkChannel;
import com.sun.grizzly.http.jk.core.JkHandler;
import com.sun.grizzly.http.jk.core.Msg;
import com.sun.grizzly.http.jk.core.MsgContext;
import java.io.IOException;

import com.sun.grizzly.tcp.Request;
import com.sun.grizzly.util.LoggerUtils;
import java.util.logging.Level;
/** Pass messages using jni 
 *
 * @author Costin Manolache
 */
public class ChannelJni extends JniHandler implements JkChannel {
    int receivedNote=1;

    public ChannelJni() {
        // we use static for now, it's easier on the C side.
        // Easy to change after we get everything working
    }

    public void init() throws IOException {
        super.initNative("channel.jni:jni");

        if( apr==null ) return;
        
        // We'll be called from C. This deals with that.
        apr.addJkHandler( "channelJni", this );        
        LoggerUtils.getLogger().info("JK: listening on channel.jni:jni" );
        
        if( next==null ) {
            if( nextName!=null ) 
                setNext( wEnv.getHandler( nextName ) );
            if( next==null )
                next=wEnv.getHandler( "dispatch" );
            if( next==null )
                next=wEnv.getHandler( "request" );
            if( LoggerUtils.getLogger().isLoggable(Level.FINEST) )
                LoggerUtils.getLogger().log(Level.FINEST,"Setting default next " + next.getClass().getName());
        }
    }

    /** Receives does nothing - send will put the response
     *  in the same buffer
     */
    public int receive( Msg msg, MsgContext ep )
        throws IOException
    {
        Msg sentResponse=(Msg)ep.getNote( receivedNote );
        ep.setNote( receivedNote, null );

        if( sentResponse == null ) {
            if( LoggerUtils.getLogger().isLoggable(Level.FINEST) )
                LoggerUtils.getLogger().log(Level.FINEST,"No send() prior to receive(), no data buffer");
            // No sent() was done prior to receive.
            msg.reset();
            msg.end();
            sentResponse = msg;
        }
        
        sentResponse.processHeader();

        if( LoggerUtils.getLogger().isLoggable(Level.FINE) )
            sentResponse.dump("received response ");

        if( msg != sentResponse ) {
            LoggerUtils.getLogger().severe( "Error, in JNI mode the msg used for receive() must be identical with the one used for send()");
        }
        
        return 0;
    }

    /** Send the packet. XXX This will modify msg !!!
     *  We could use 2 packets, or sendAndReceive().
     *    
     */
    public int send( Msg msg, MsgContext ep )
        throws IOException
    {
        ep.setNote( receivedNote, null );
        if( LoggerUtils.getLogger().isLoggable(Level.FINEST) ) LoggerUtils.getLogger().log(Level.FINEST,"ChannelJni.send: "  +  msg );

        int rc=super.nativeDispatch( msg, ep, JK_HANDLE_JNI_DISPATCH, 0);

        // nativeDispatch will put the response in the same buffer.
        // Next receive() will just get it from there. Very tricky to do
        // things in one thread instead of 2.
        ep.setNote( receivedNote, msg );
        
        return rc;
    }

    public int flush(Msg msg, MsgContext ep) throws IOException {
        ep.setNote( receivedNote, null );
        return OK;
    }

    public boolean isSameAddress(MsgContext ep) {
        return true;
    }

    public void registerRequest(Request req, MsgContext ep, int count) {
        // Not supported.
    }

    public String getChannelName() {
        return getName();
    }
    /** Receive a packet from the C side. This is called from the C
     *  code using invocation, but only for the first packet - to avoid
     *  recursivity and thread problems.
     *
     *  This may look strange, but seems the best solution for the
     *  problem ( the problem is that we don't have 'continuation' ).
     *
     *  sendPacket will move the thread execution on the C side, and
     *  return when another packet is available. For packets that
     *  are one way it'll return after it is processed too ( having
     *  2 threads is far more expensive ).
     *
     *  Again, the goal is to be efficient and behave like all other
     *  Channels ( so the rest of the code can be shared ). Playing with
     *  java objects on C is extremely difficult to optimize and do
     *  right ( IMHO ), so we'll try to keep it simple - byte[] passing,
     *  the conversion done in java ( after we know the encoding and
     *  if anyone asks for it - same lazy behavior as in 3.3 ).
     */
    public  int invoke(Msg msg, MsgContext ep )  throws IOException {
        if( apr==null ) return -1;
        
        long xEnv=ep.getJniEnv();
        long cEndpointP=ep.getJniContext();

        int type=ep.getType();
        if( LoggerUtils.getLogger().isLoggable(Level.FINEST) ) LoggerUtils.getLogger().log(Level.FINEST,"ChannelJni.invoke: "  + ep + " " + type);

        switch( type ) {
        case JkHandler.HANDLE_RECEIVE_PACKET:
            return receive( msg, ep );
        case JkHandler.HANDLE_SEND_PACKET:
            return send( msg, ep );
        case JkHandler.HANDLE_FLUSH:
            return flush(msg, ep);
        }

        // Reset receivedNote. It'll be visible only after a SEND and before a receive.
        ep.setNote( receivedNote, null );

        // Default is FORWARD - called from C 
        try {
            // first, we need to get an endpoint. It should be
            // per/thread - and probably stored by the C side.
            if( LoggerUtils.getLogger().isLoggable(Level.FINEST) ) LoggerUtils.getLogger().log(Level.FINEST,"Received request " + xEnv);
            
            // The endpoint will store the message pt.
            msg.processHeader();

            if( LoggerUtils.getLogger().isLoggable(Level.FINE) ) msg.dump("Incoming msg ");

            int status= next.invoke(  msg, ep );
            
            if( LoggerUtils.getLogger().isLoggable(Level.FINEST) ) LoggerUtils.getLogger().log(Level.FINEST,"after processCallbacks " + status);
            
            return status;
        } catch( Exception ex ) {
            ex.printStackTrace();
        }
        return 0;
    }    

}
