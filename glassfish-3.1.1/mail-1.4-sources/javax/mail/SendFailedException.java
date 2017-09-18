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
 * @(#)SendFailedException.java	1.10 05/08/29
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package javax.mail;

/**
 * This exception is thrown when the message cannot be sent.<p>
 * 
 * The exception includes those addresses to which the message could not be
 * sent as well as the valid addresses to which the message was sent and
 * valid addresses to which the message was not sent.
 *
 * @see	javax.mail.Transport#send
 * @see	javax.mail.Transport#sendMessage
 * @see	javax.mail.event.TransportEvent
 *
 * @author John Mani
 * @author Max Spivak
 */

public class SendFailedException extends MessagingException {
    transient protected Address[] invalid;
    transient protected Address[] validSent;
    transient protected Address[] validUnsent;

    private static final long serialVersionUID = -6457531621682372913L;

    /**
     * Constructs a SendFailedException with no detail message.
     */
    public SendFailedException() {
	super();
    }

    /**
     * Constructs a SendFailedException with the specified detail message.
     * @param s		the detail message
     */
    public SendFailedException(String s) {
	super(s);
    }

    /**
     * Constructs a SendFailedException with the specified 
     * Exception and detail message. The specified exception is chained
     * to this exception.
     * @param s		the detail message
     * @param e		the embedded exception
     * @see	#getNextException
     * @see	#setNextException
     */
    public SendFailedException(String s, Exception e) {
	super(s, e);
    }


    /**
     * Constructs a SendFailedException with the specified string
     * and the specified address objects.
     *
     * @param msg	the detail message
     * @param ex        the embedded exception
     * @param validSent valid addresses to which message was sent
     * @param validUnsent valid addresses to which message was not sent
     * @param invalid 	the invalid addresses
     * @see	#getNextException
     * @see	#setNextException
     */
    public SendFailedException(String msg, Exception ex, Address[] validSent, 
			       Address[] validUnsent, Address[] invalid) {
	super(msg, ex);
	this.validSent = validSent;
	this.validUnsent = validUnsent;
	this.invalid = invalid;
    }

    /**
     * Return the addresses to which this message was sent succesfully.
     * @return Addresses to which the message was sent successfully or null
     */
    public Address[] getValidSentAddresses() {
	return validSent;
    }

    /**
     * Return the addresses that are valid but to which this message 
     * was not sent.
     * @return Addresses that are valid but to which the message was 
     *         not sent successfully or null
     */
    public Address[] getValidUnsentAddresses() {
	return validUnsent;
    }

    /**
     * Return the addresses to which this message could not be sent.
     *
     * @return Addresses to which the message sending failed or null;
     */
    public Address[] getInvalidAddresses() {
	return invalid;
    }
}
