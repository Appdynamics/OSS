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
package com.sun.enterprise.jbi.serviceengine.handlers;

import com.sun.enterprise.security.SecurityContext;
import com.sun.enterprise.jbi.serviceengine.comm.MessageExchangeTransport;

import javax.jbi.messaging.MessageExchange;
import javax.security.auth.Subject;
import javax.jbi.messaging.NormalizedMessage;

/**
 * This class handles the security context propagation during JBI message 
 * exchanges.
 * @author Vikas Awasthi
 */
public class JBISecurityHandler implements JBIHandler {

    public void handleInbound(MessageExchangeTransport meTransport) throws Exception {
        if(!isSecurityEnabled())
            return;

        Subject subject = SecurityContext.getCurrent().getSubject();
        NormalizedMessage msg = meTransport.getMessage();
        if(msg == null) return;

        MessageExchange me = meTransport.getMessageExchange();
        if(me.getRole().equals(MessageExchange.Role.PROVIDER) &&
                msg.getProperty(SECURITY_PROPERTY) != null) {
            //clear the security context set by us while processing the incoming message
            SecurityContext.setUnauthenticatedContext();
        }
        
        msg.setProperty(SECURITY_PROPERTY, subject);
    }

    public void handleOutbound(MessageExchangeTransport meTransport) throws Exception {
        if(!isSecurityEnabled())
            return;

        NormalizedMessage msg = meTransport.getMessage();
        if(msg == null) return;

        Subject subject = (Subject)msg.getProperty(SECURITY_PROPERTY);
        if(subject != null) {
            SecurityContext sc = new SecurityContext(subject);
            SecurityContext.setCurrent(sc);
        }
    }

    /**
     * This method can be overridden to control the security propagation
     */
    protected boolean isSecurityEnabled() {
        return (sc_enable==null || sc_enable.equalsIgnoreCase("true"));
    }

    private final String SECURITY_PROPERTY = "javax.jbi.security.subject";
    private String sc_enable = System.getProperty("com.sun.enterprise.jbi.sc.enable");
}
