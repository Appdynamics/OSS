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
 * @(#)MessageRemovedException.java	1.6 05/08/29
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package javax.mail;

/**
 * The exception thrown when an invalid method is invoked on an expunged
 * Message. The only valid methods on an expunged Message are
 * <code>isExpunged()</code> and <code>getMessageNumber()</code>.
 *
 * @see	   javax.mail.Message#isExpunged()
 * @see	   javax.mail.Message#getMessageNumber()
 * @author John Mani
 */

public class MessageRemovedException extends MessagingException {

    private static final long serialVersionUID = 1951292550679528690L;

    /**
     * Constructs a MessageRemovedException with no detail message.
     */
    public MessageRemovedException() {
	super();
    }

    /**
     * Constructs a MessageRemovedException with the specified detail message.
     * @param s		the detail message
     */
    public MessageRemovedException(String s) {
	super(s);
    }
}
