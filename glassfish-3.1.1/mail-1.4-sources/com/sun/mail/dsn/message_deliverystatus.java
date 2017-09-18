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
 * @(#)message_deliverystatus.java	1.3 06/02/27
 *
 * Copyright 1997-2006 Sun Microsystems, Inc. All Rights Reserved.
 */

package com.sun.mail.dsn;

import java.io.*;
import java.util.Properties;
import java.awt.datatransfer.DataFlavor;
import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.*;


/**
 * @version	1.3, 06/02/27
 */
public class message_deliverystatus implements DataContentHandler {

    ActivationDataFlavor ourDataFlavor = new ActivationDataFlavor(
	DeliveryStatus.class,
	"message/delivery-status", 
	"Delivery Status");

    /**
     * return the DataFlavors for this <code>DataContentHandler</code>
     * @return The DataFlavors.
     */
    public DataFlavor[] getTransferDataFlavors() {
	return new DataFlavor[] { ourDataFlavor };
    }

    /**
     * return the Transfer Data of type DataFlavor from InputStream
     * @param df The DataFlavor.
     * @param ins The InputStream corresponding to the data.
     * @return a Message object
     */
    public Object getTransferData(DataFlavor df, DataSource ds)
				throws IOException {
	// make sure we can handle this DataFlavor
	if (ourDataFlavor.equals(df))
	    return getContent(ds);
	else
	    return null;
    }
    
    /**
     * Return the content.
     */
    public Object getContent(DataSource ds) throws IOException {
	// create a new DeliveryStatus
	try {
	    /*
	    Session session;
	    if (ds instanceof MessageAware) {
		javax.mail.MessageContext mc =
			((MessageAware)ds).getMessageContext();
		session = mc.getSession();
	    } else {
		// Hopefully a rare case.  Also hopefully the application
		// has created a default Session that can just be returned
		// here.  If not, the one we create here is better than
		// nothing, but overall not a really good answer.
		session = Session.getDefaultInstance(new Properties(), null);
	    }
	    return new DeliveryStatus(session, ds.getInputStream());
	    */
	    return new DeliveryStatus(ds.getInputStream());
	} catch (MessagingException me) {
	    throw new IOException("Exception creating DeliveryStatus in " +
		    "message/devliery-status DataContentHandler: " +
		    me.toString());
	}
    }
    
    /**
     */
    public void writeTo(Object obj, String mimeType, OutputStream os) 
			throws IOException {
	// if the object is a DeliveryStatus, we know how to write that out
	if (obj instanceof DeliveryStatus) {
	    DeliveryStatus ds = (DeliveryStatus)obj;
	    try {
		ds.writeTo(os);
	    } catch (MessagingException me) {
		throw new IOException(me.toString());
	    }
	    
	} else {
	    throw new IOException("unsupported object");
	}
    }
}
