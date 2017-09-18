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
 * @(#)MessageChangedEvent.java	1.8 05/08/29
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package javax.mail.event;

import java.util.*;
import javax.mail.*;

/**
 * This class models Message change events.
 *
 * @author John Mani
 */

public class MessageChangedEvent extends MailEvent {

    /** The message's flags changed. */
    public static final int FLAGS_CHANGED 	= 1;
    /** The message's envelope (headers, but not body) changed. */
    public static final int ENVELOPE_CHANGED 	= 2;

    /**
     * The event type.
     *
     * @serial
     */
    protected int type;

    /**
     * The message that changed.
     */
    transient protected Message msg;

    private static final long serialVersionUID = -4974972972105535108L;

    /**
     * Constructor.
     * @param source  	The folder that owns the message
     * @param type	The change type
     * @param msg	The changed message 
     */
    public MessageChangedEvent(Object source, int type, Message msg) {
	super(source);
	this.msg = msg;
	this.type = type;
    }

    /**
     * Return the type of this event.
     * @return  type
     */
    public int getMessageChangeType() {
	return type;
    }

    /**
     * Return the changed Message.
     * @return  the message
     */
    public Message getMessage() {
	return msg;
    }

    /**
     * Invokes the appropriate MessageChangedListener method.
     */
    public void dispatch(Object listener) {
	((MessageChangedListener)listener).messageChanged(this);
    }
}
