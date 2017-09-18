/*
 * The contents of this file are subject to the terms 
 * of the Common Development and Distribution License 
 * (the "License").  You may not use this file except 
 * in compliance with the License.
 * 
 * You can obtain a copy of the license at 
 * glassfish/bootstrap/legal/CDDLv1.0.txt or 
 * https://glassfish.dev.java.net/public/CDDLv1.0.html. 
 * See the License for the specific language governing 
 * permissions and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL 
 * HEADER in each file and include the License file at 
 * glassfish/bootstrap/legal/CDDLv1.0.txt.  If applicable, 
 * add the following below this CDDL HEADER, with the 
 * fields enclosed by brackets "[]" replaced with your 
 * own identifying information: Portions Copyright [yyyy] 
 * [name of copyright owner]
 */

/*
 * @(#)TransportListener.java	1.7 05/08/29
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package javax.mail.event;

import java.util.*;

/**
 * This is the Listener interface for Transport events
 *
 * @author John Mani
 * @author Max Spivak
 *
 * @see javax.mail.Transport
 * @see javax.mail.event.TransportEvent
 */

public interface TransportListener extends java.util.EventListener {

    /**
     * Invoked when a Message is succesfully delivered.
     * @param	e TransportEvent
     */
    public void messageDelivered(TransportEvent e);

    /**
     * Invoked when a Message is not delivered.
     * @param	e TransportEvent
     * @see TransportEvent
     */
    public void messageNotDelivered(TransportEvent e);

    /**
     * Invoked when a Message is partially delivered.
     * @param	e TransportEvent
     * @see TransportEvent
     */
    public void messagePartiallyDelivered(TransportEvent e);
}
