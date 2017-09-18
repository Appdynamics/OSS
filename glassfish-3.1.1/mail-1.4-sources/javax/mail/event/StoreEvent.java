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
 * @(#)StoreEvent.java	1.10 05/08/29
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package javax.mail.event;

import java.util.*;
import javax.mail.*;

/**
 * This class models notifications from the Store connection. These
 * notifications can be ALERTS or NOTICES. ALERTS must be presented
 * to the user in a fashion that calls the user's attention to the
 * message.
 *
 * @author John Mani
 */

public class StoreEvent extends MailEvent {

    /**
     * Indicates that this message is an ALERT.
     */
    public static final int ALERT 		= 1;

    /**
     * Indicates that this message is a NOTICE.
     */
    public static final int NOTICE 		= 2;

    /**
     * The event type.
     *
     * @serial
     */
    protected int type;

    /**
     * The message text to be presented to the user.
     *
     * @serial
     */
    protected String message;

    private static final long serialVersionUID = 1938704919992515330L;

    /**
     * Constructor.
     * @param store  The source Store
     */
    public StoreEvent(Store store, int type, String message) {
	super(store);
	this.type = type;
	this.message = message;
    }

    /**
     * Return the type of this event.
     *
     * @return  type
     * @see #ALERT
     * @see #NOTICE
     */
    public int getMessageType() {
	return type;
    }

    /**
     * Get the message from the Store.
     *
     * @return message from the Store
     */
    public String getMessage() {
	return message;
    }

    /**
     * Invokes the appropriate StoreListener method.
     */
    public void dispatch(Object listener) {
	((StoreListener)listener).notification(this);
    }
}
