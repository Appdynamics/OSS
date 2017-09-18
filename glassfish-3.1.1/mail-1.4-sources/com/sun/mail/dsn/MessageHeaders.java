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
 * @(#)MessageHeaders.java	1.5 06/03/09
 *
 * Copyright 1997-2006 Sun Microsystems, Inc. All Rights Reserved.
 */

package com.sun.mail.dsn;

import java.io.*;

import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.*;

/**
 * A special MimeMessage object that contains only message headers,
 * no content.  Used to represent the MIME type text/rfc822-headers.
 */
public class MessageHeaders extends MimeMessage {

    /**
     * Construct a MessageHeaders object.
     */
    public MessageHeaders() throws MessagingException {
	super((Session)null);
	content = new byte[0];
    }

    /**
     * Constructs a MessageHeaders object from the given InputStream.
     *
     * @param	is	InputStream
     */
    public MessageHeaders(InputStream is) throws MessagingException {
	super(null, is);
	content = new byte[0];
    }

    /**
     * Constructs a MessageHeaders object using the given InternetHeaders.
     *
     * @param	headers	InternetHeaders to use
     */
    public MessageHeaders(InternetHeaders headers) throws MessagingException {
	super((Session)null);
	this.headers = headers;
	content = new byte[0];
    }

    /**
     * Return the size of this message.
     * Always returns zero.
     */
    public int getSize() {
	return 0;
    }

    public InputStream getInputStream() {
	return new ByteArrayInputStream(content);
    }

    protected InputStream getContentStream() {
	return new ByteArrayInputStream(content);
    }

    /**
     * Can't set any content for a MessageHeaders object.
     *
     * @exception	MessagingException	always
     */
    public void setDataHandler(DataHandler dh) throws MessagingException {
	throw new MessagingException("Can't set content for MessageHeaders");
    }

}
