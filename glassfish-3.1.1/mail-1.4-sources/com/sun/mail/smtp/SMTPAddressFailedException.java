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
 * @(#)SMTPAddressFailedException.java	1.5 05/08/29
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package com.sun.mail.smtp;

import javax.mail.SendFailedException;
import javax.mail.internet.InternetAddress;

/**
 * This exception is thrown when the message cannot be sent. <p>
 * 
 * The exception includes the address to which the message could not be
 * sent.  This will usually appear in a chained list of exceptions,
 * one per address, attached to a top level SendFailedException that
 * aggregates all the addresses.
 *
 * @since JavaMail 1.3.2
 */

public class SMTPAddressFailedException extends SendFailedException {
    protected InternetAddress addr;	// address that failed
    protected String cmd;		// command issued to server
    protected int rc;			// return code from SMTP server

    private static final long serialVersionUID = 804831199768630097L;

    /**
     * Constructs an SMTPAddressFailedException with the specified 
     * address, return code, and error string.
     *
     * @param addr	the address that failed
     * @param cmd	the command that was sent to the SMTP server
     * @param rc	the SMTP return code indicating the failure
     * @param err	the error string from the SMTP server
     */
    public SMTPAddressFailedException(InternetAddress addr, String cmd, int rc,
				String err) {
	super(err);
	this.addr = addr;
	this.cmd = cmd;
	this.rc = rc;
    }

    /**
     * Return the address that failed.
     */
    public InternetAddress getAddress() {
	return addr;
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
