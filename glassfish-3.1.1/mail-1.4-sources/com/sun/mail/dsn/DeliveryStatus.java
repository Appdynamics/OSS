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
 * @(#)DeliveryStatus.java	1.5 06/03/09
 *
 * Copyright 1997-2006 Sun Microsystems, Inc. All Rights Reserved.
 */

package com.sun.mail.dsn;

import java.io.*;
import java.util.*;

import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.*;

import com.sun.mail.util.LineOutputStream;	// XXX

/**
 * A message/delivery-status message content, as defined in
 * <A HREF="http://www.ietf.org/rfc/rfc3464.txt">RFC 3464</A>.
 */
public class DeliveryStatus {

    private static boolean debug = false;

    static {
	try {
	    String s = System.getProperty("mail.dsn.debug");
	    // default to false
	    debug = s != null && !s.equalsIgnoreCase("false");
	} catch (SecurityException sex) {
	    // ignore it
	}
    }

    /**
     * The DSN fields for the message.
     */
    protected InternetHeaders messageDSN;

    /**
     * The DSN fields for each recipient.
     */
    protected InternetHeaders[] recipientDSN;

    /**
     * Construct a delivery status notification with no content.
     */
    public DeliveryStatus() throws MessagingException {
	messageDSN = new InternetHeaders();
	recipientDSN = new InternetHeaders[0];
    }

    /**
     * Construct a delivery status notification by parsing the
     * supplied input stream.
     */
    public DeliveryStatus(InputStream is)
				throws MessagingException, IOException {
	messageDSN = new InternetHeaders(is);
	if (debug)
	    System.out.println("DSN: got messageDSN");
	Vector v = new Vector();
	try {
	    while (is.available() > 0) {
		InternetHeaders h = new InternetHeaders(is);
		if (debug)
		    System.out.println("DSN: got recipientDSN");
		v.addElement(h);
	    }
	} catch (EOFException ex) {
	    if (debug)
		System.out.println("DSN: got EOFException");
	}
	if (debug)
	    System.out.println("DSN: recipientDSN size " + v.size());
	recipientDSN = new InternetHeaders[v.size()];
	v.copyInto(recipientDSN);
    }

    /**
     * Return all the per-message fields in the delivery status notification.
     * The fields are defined as:
     *
     * <pre>
     *    per-message-fields =
     *          [ original-envelope-id-field CRLF ]
     *          reporting-mta-field CRLF
     *          [ dsn-gateway-field CRLF ]
     *          [ received-from-mta-field CRLF ]
     *          [ arrival-date-field CRLF ]
     *          *( extension-field CRLF )
     * </pre>
     */
    // XXX - could parse each of these fields
    public InternetHeaders getMessageDSN() {
	return messageDSN;
    }

    /**
     * Set the per-message fields in the delivery status notification.
     */
    public void setMessageDSN(InternetHeaders messageDSN) {
	this.messageDSN = messageDSN;
    }

    /**
     * Return the number of recipients for which we have
     * per-recipient delivery status notification information.
     */
    public int getRecipientDSNCount() {
	return recipientDSN.length;
    }

    /**
     * Return the delivery status notification information for
     * the specified recipient.
     */
    public InternetHeaders getRecipientDSN(int n) {
	return recipientDSN[n];
    }

    /**
     * Add deliver status notification information for another
     * recipient.
     */
    public void addRecipientDSN(InternetHeaders h) {
	InternetHeaders[] rh = new InternetHeaders[recipientDSN.length + 1];
	System.arraycopy(recipientDSN, 0, rh, 0, recipientDSN.length);
	recipientDSN = rh;
	recipientDSN[recipientDSN.length - 1] = h;
    }

    public void writeTo(OutputStream os)
				throws IOException, MessagingException {
	// see if we already have a LOS
	LineOutputStream los = null;
	if (os instanceof LineOutputStream) {
	    los = (LineOutputStream) os;
	} else {
	    los = new LineOutputStream(os);
	}

	writeInternetHeaders(messageDSN, los);
	los.writeln();
	for (int i = 0; i < recipientDSN.length; i++) {
	    writeInternetHeaders(recipientDSN[i], los);
	    los.writeln();
	}
    }

    private static void writeInternetHeaders(InternetHeaders h,
				LineOutputStream los) throws IOException {
	Enumeration e = h.getAllHeaderLines();
	try {
	    while (e.hasMoreElements())
		los.writeln((String)e.nextElement());
	} catch (MessagingException mex) {
	    Exception ex = mex.getNextException();
	    if (ex instanceof IOException)
		throw (IOException)ex;
	    else
		throw new IOException("Exception writing headers: " + mex);
	}
    }

    public String toString() {
	return "DeliveryStatus: Reporting-MTA=" +
	    messageDSN.getHeader("Reporting-MTA", null) + ", #Recipients=" +
	    recipientDSN.length;
    }
}
