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

/*
 * MessageExchangeTransport.java
 *
 * Created on November 16, 2006, 12:13 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.sun.enterprise.jbi.serviceengine.comm;

import com.sun.enterprise.jbi.serviceengine.util.soap.EndpointMetaData;
import com.sun.xml.ws.api.message.Message;
import com.sun.xml.ws.api.message.Packet;

import java.util.Map;
import javax.jbi.messaging.ExchangeStatus;
import javax.jbi.messaging.NormalizedMessage;
import javax.jbi.messaging.MessageExchange;

/**
 *
 * Interface to send message to and receive message from NMR.
 *
 * @author bhavani
 */
public interface MessageExchangeTransport {
    
    public UnWrappedMessage receive(EndpointMetaData emd); // Receive unwrapped message.
    
    public Exception receiveError();
    
    public void send(Packet packet, EndpointMetaData emd)
    throws Exception;
    
    public void sendStatus(ExchangeStatus status);
    
    public void sendError(Exception exception);

    /**
     * The following methods are required by the old code path.
     */
    public NormalizedMessage receiveNormalized();
    
    public void sendNormalized() throws Exception;
    
    public MessageExchange getMessageExchange();
    
    public NormalizedMessage getMessage();
    public Map<String, Object> getMessageProperties();
    public void setMessageProperties(Map<String, Object> props);
}
