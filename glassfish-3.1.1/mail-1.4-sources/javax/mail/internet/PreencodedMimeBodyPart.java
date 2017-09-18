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
 * @(#)PreencodedMimeBodyPart.java	1.2 05/08/29
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package javax.mail.internet;

import java.io.*;
import java.util.Enumeration;
import javax.mail.*;

import com.sun.mail.util.LineOutputStream;

/**
 * A MimeBodyPart that handles data that has already been encoded.
 * This class is useful when constructing a message and attaching
 * data that has already been encoded (for example, using base64
 * encoding).  The data may have been encoded by the application,
 * or may have been stored in a file or database in encoded form.
 * The encoding is supplied when this object is created.  The data
 * is attached to this object in the usual fashion, by using the
 * <code>setText</code>, <code>setContent</code>, or
 * <code>setDataHandler</code> methods.
 *
 * @since	JavaMail 1.4
 */

public class PreencodedMimeBodyPart extends MimeBodyPart {
    private String encoding;

    /**
     * Create a PreencodedMimeBodyPart that assumes the data is
     * encoded using the specified encoding.  The encoding must
     * be a MIME supported Content-Transfer-Encoding.
     */
    public PreencodedMimeBodyPart(String encoding) {
	this.encoding = encoding;
    }

    /**
     * Returns the content transfer encoding specified when
     * this object was created.
     */
    public String getEncoding() throws MessagingException {
	return encoding;
    }

    /**
     * Output the body part as an RFC 822 format stream.
     *
     * @exception MessagingException
     * @exception IOException	if an error occurs writing to the
     *				stream or if an error is generated
     *				by the javax.activation layer.
     * @see javax.activation.DataHandler#writeTo
     */
    public void writeTo(OutputStream os)
			throws IOException, MessagingException {

	// see if we already have a LOS
	LineOutputStream los = null;
	if (os instanceof LineOutputStream) {
	    los = (LineOutputStream) os;
	} else {
	    los = new LineOutputStream(os);
	}

	// First, write out the header
	Enumeration hdrLines = getAllHeaderLines();
	while (hdrLines.hasMoreElements())
	    los.writeln((String)hdrLines.nextElement());

	// The CRLF separator between header and content
	los.writeln();

	// Finally, the content, already encoded.
	getDataHandler().writeTo(os);
	os.flush();
    }

    /**
     * Force the <code>Content-Transfer-Encoding</code> header to use
     * the encoding that was specified when this object was created.
     */
    protected void updateHeaders() throws MessagingException {
	super.updateHeaders();
	MimeBodyPart.setEncoding(this, encoding);
    }
}
