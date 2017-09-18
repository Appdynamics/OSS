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
 * @(#)multipart_mixed.java	1.10 05/08/29
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */
package com.sun.mail.handlers;

import java.io.*;
import java.awt.datatransfer.DataFlavor;
import javax.activation.*;
import javax.mail.MessagingException;
import javax.mail.internet.*;


public class multipart_mixed implements DataContentHandler {
    private ActivationDataFlavor myDF = new ActivationDataFlavor(
	    javax.mail.internet.MimeMultipart.class,
	    "multipart/mixed", 
	    "Multipart");

    /**
     * Return the DataFlavors for this <code>DataContentHandler</code>.
     *
     * @return The DataFlavors
     */
    public DataFlavor[] getTransferDataFlavors() { // throws Exception;
	return new DataFlavor[] { myDF };
    }

    /**
     * Return the Transfer Data of type DataFlavor from InputStream.
     *
     * @param df The DataFlavor
     * @param ins The InputStream corresponding to the data
     * @return String object
     */
    public Object getTransferData(DataFlavor df, DataSource ds) {
	// use myDF.equals to be sure to get ActivationDataFlavor.equals,
	// which properly ignores Content-Type parameters in comparison
	if (myDF.equals(df))
	    return getContent(ds);
	else
	    return null;
    }
    
    /**
     * Return the content.
     */
    public Object getContent(DataSource ds) {
	try {
	    return new MimeMultipart(ds); 
	} catch (MessagingException e) {
	    return null;
	}
    }
    
    /**
     * Write the object to the output stream, using the specific MIME type.
     */
    public void writeTo(Object obj, String mimeType, OutputStream os) 
			throws IOException {
	if (obj instanceof MimeMultipart) {
	    try {
		((MimeMultipart)obj).writeTo(os);
	    } catch (MessagingException e) {
		throw new IOException(e.toString());
	    }
	}
    }
}
