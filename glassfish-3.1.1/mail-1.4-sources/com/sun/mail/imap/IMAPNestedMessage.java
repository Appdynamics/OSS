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
 * @(#)IMAPNestedMessage.java	1.4 05/08/29
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package com.sun.mail.imap;

import java.io.*;
import javax.mail.*;
import com.sun.mail.imap.protocol.*;

/**
 * This class implements a nested IMAP message
 *
 * @author  John Mani
 */

public class IMAPNestedMessage extends IMAPMessage {
    private IMAPMessage msg; // the enclosure of this nested message

    /**
     * Package private constructor. <p>
     *
     * Note that nested messages have no containing folder, nor 
     * a message number.
     */
    IMAPNestedMessage(IMAPMessage m, BODYSTRUCTURE b, ENVELOPE e, String sid) {
	super(m._getSession());
	msg = m;
	bs = b;
	envelope = e;
	sectionId = sid;
    }

    /*
     * Get the enclosing message's Protocol object. Overrides
     * IMAPMessage.getProtocol().
     */
    protected IMAPProtocol getProtocol() throws FolderClosedException {
	return msg.getProtocol();
    }

    /*
     * Get the enclosing message's messageCacheLock. Overrides
     * IMAPMessage.getMessageCacheLock().
     */
    protected Object getMessageCacheLock() {
	return msg.getMessageCacheLock();
    }

    /*
     * Get the enclosing message's sequence number. Overrides
     * IMAPMessage.getSequenceNumber().
     */
    protected int getSequenceNumber() {
	return msg.getSequenceNumber();
    }

    /*
     * Check whether the enclosing message is expunged. Overrides 
     * IMAPMessage.checkExpunged().
     */
    protected void checkExpunged() throws MessageRemovedException {
	msg.checkExpunged();
    }

    /*
     * Check whether the enclosing message is expunged. Overrides
     * Message.isExpunged().
     */
    public boolean isExpunged() {
	return msg.isExpunged();
    }

    /*
     * Get the enclosing message's fetchBlockSize. 
     */
    protected int getFetchBlockSize() {
	return msg.getFetchBlockSize();
    }

    /*
     * IMAPMessage uses RFC822.SIZE. We use the "size" field from
     * our BODYSTRUCTURE.
     */
    public int getSize() throws MessagingException {
	return bs.size;
    }

    /*
     * Disallow setting flags on nested messages
     */
    public synchronized void setFlags(Flags flag, boolean set) 
			throws MessagingException {
	// Cannot set FLAGS on a nested IMAP message	
	throw new MethodNotSupportedException(
		"Cannot set flags on this nested message");
    }
}
