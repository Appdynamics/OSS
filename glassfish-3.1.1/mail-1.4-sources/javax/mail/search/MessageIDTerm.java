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
 * @(#)MessageIDTerm.java	1.9 05/08/29
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package javax.mail.search;

import javax.mail.Message;

/**
 * This term models the RFC822 "MessageId" - a message-id for 
 * Internet messages that is supposed to be unique per message.
 * Clients can use this term to search a folder for a message given
 * its MessageId. <p>
 *
 * The MessageId is represented as a String.
 *
 * @author Bill Shannon
 * @author John Mani
 */
public final class MessageIDTerm extends StringTerm {

    private static final long serialVersionUID = -2121096296454691963L;

    /**
     * Constructor.
     *
     * @param msgid  the msgid to search for
     */
    public MessageIDTerm(String msgid) {
	// Note: comparison is case-insensitive
	super(msgid);
    }

    /**
     * The match method.
     *
     * @param msg	the match is applied to this Message's 
     *			Message-ID header
     * @return		true if the match succeeds, otherwise false
     */
    public boolean match(Message msg) {
	String[] s;

	try {
	    s = msg.getHeader("Message-ID");
	} catch (Exception e) {
	    return false;
	}

	if (s == null)
	    return false;

	for (int i=0; i < s.length; i++)
	    if (super.match(s[i]))
		return true;
	return false;
    }

    /**
     * Equality comparison.
     */
    public boolean equals(Object obj) {
	if (!(obj instanceof MessageIDTerm))
	    return false;
	return super.equals(obj);
    }
}
