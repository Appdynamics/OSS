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
 * @(#)SMTPSendFailedException.java	1.3 05/08/29
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package com.sun.mail.smtp;

import javax.mail.Address;
import javax.mail.SendFailedException;
import javax.mail.internet.InternetAddress;

/**
 * This exception is thrown when the message cannot be sent. <p>
 * 
 * This exception will usually appear first in a chained list of exceptions,
 * followed by SMTPAddressFailedExceptions and/or
 * SMTPAddressSucceededExceptions, * one per address.
 * This exception corresponds to one of the SMTP commands used to
 * send a message, such as the MAIL, DATA, and "end of data" commands,
 * but not including the RCPT command.
 *
 * @since JavaMail 1.3.2
 */

public class SMTPSendFailedException extends SendFailedException {
    protected InternetAddress addr;	// address that failed
    protected String cmd;		// command issued to server
    protected int rc;			// return code from SMTP server

    private static final long serialVersionUID = 8049122628728932894L;

    /**
     * Constructs an SMTPSendFailedException with the specified 
     * address, return code, and error string.
     *
     * @param cmd	the command that was sent to the SMTP server
     * @param rc	the SMTP return code indicating the failure
     * @param err	the error string from the SMTP server
     */
    public SMTPSendFailedException(String cmd, int rc, String err, Exception ex,
				Address[] vs, Address[] vus, Address[] inv) {
	super(err, ex, vs, vus, inv);
	this.cmd = cmd;
	this.rc = rc;
    }

    /**
     * Return the command that failed.
     */
    public String getCommand() {
	return cmd;
    }

    /**
     * Return the return code from the SMTP server that indicates the
     * reason for the failure.  See
     * <A HREF="http://www.ietf.org/rfc/rfc821.txt">RFC 821</A>
     * for interpretation of the return code.
     */
    public int getReturnCode() {
	return rc;
    }
}
