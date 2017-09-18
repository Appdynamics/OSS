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
 * @(#)MessageCountEvent.java	1.10 05/08/29
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package javax.mail.event;

import java.util.*;
import javax.mail.*;

/**
 * This class notifies changes in the number of messages in a folder. <p>
 *
 * Note that some folder types may only deliver MessageCountEvents at
 * certain times or after certain operations.  IMAP in particular will
 * only notify the client of MessageCountEvents when a client issues a
 * new command.
 * Refer to RFC 2060 <A HREF="http://www.ietf.org/rfc/rfc2060.txt">
 * http://www.ietf.org/rfc/rfc2060.txt</A> for details.
 * A client may want "poll" the folder by occasionally calling the
 * <code>getMessageCount</code> or <code>isConnected</code> methods
 * to solicit any such notifications.
 *
 * @author John Mani
 */

public class MessageCountEvent extends MailEvent {

    /** The messages were added to their folder */
    public static final int ADDED 		= 1;
    /** The messages were removed from their folder */
    public static final int REMOVED 		= 2;

    /**
     * The event type.
     *
     * @serial
     */
    protected int type;

    /**
     * If true, this event is the result of an explicit
     * expunge by this client, and the messages in this 
     * folder have been renumbered to account for this.
     * If false, this event is the result of an expunge
     * by external sources.
     *
     * @serial
     */
    protected boolean removed;

    /**
     * The messages.
     */
    transient protected Message[] msgs;

    private static final long serialVersionUID = -7447022340837897369L;

    /**
     * Constructor.
     * @param folder  	The containing folder
     * @param type	The event type
     * @param removed	If true, this event is the result of an explicit
     *			expunge by this client, and the messages in this 
     *			folder have been renumbered to account for this.
     *			If false, this event is the result of an expunge
     *			by external sources.
     *
     * @param msgs	The messages added/removed
     */
    public MessageCountEvent(Folder folder, int type, 
			     boolean removed, Message[] msgs) {
	super(folder);
	this.type = type;
	this.removed = removed;
	this.msgs = msgs;
    }

    /**
     * Return the type of this event.
     * @return  type
     */
    public int getType() {
	return type;
    }

    /**
     * Indicates whether this event is the result of an explicit
     * expunge by this client, or due to an expunge from external
     * sources. If <code>true</code>, this event is due to an
     * explicit expunge and hence all remaining messages in this
     * folder have been renumbered. If <code>false</code>, this event
     * is due to an external expunge. <p>
     *
     * Note that this method is valid only if the type of this event
     * is <code>REMOVED</code>
     */
    public boolean isRemoved() {
	return removed;
    }

    /**
     * Return the array of messages added or removed.
     * @return array of messages
     */
    public Message[] getMessages() {
	return msgs;
    }

    /**
     * Invokes the appropriate MessageCountListener method.
     */
    public void dispatch(Object listener) {
	if (type == ADDED)
	    ((MessageCountListener)listener).messagesAdded(this);
	else // REMOVED
	    ((MessageCountListener)listener).messagesRemoved(this);
    }
}
