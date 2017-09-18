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
 * @(#)IMAPBodyPart.java	1.17 06/03/24
 *
 * Copyright 1997-2006 Sun Microsystems, Inc. All Rights Reserved.
 */

package com.sun.mail.imap;

import java.io.*;

import java.util.Enumeration;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

import com.sun.mail.util.*;
import com.sun.mail.iap.*;
import com.sun.mail.imap.protocol.*;

/**
 * This class 
 *
 * @version 1.4, 97/12/09
 * @author  John Mani
 */

public class IMAPBodyPart extends MimeBodyPart {
    private IMAPMessage message;
    private BODYSTRUCTURE bs;
    private String sectionId;

    // processed values ..
    private String type;
    private String description;

    private boolean headersLoaded = false;

    protected IMAPBodyPart(BODYSTRUCTURE bs, String sid, IMAPMessage message) {
	super();
	this.bs = bs;
	this.sectionId = sid;
	this.message = message;
	// generate content-type
	ContentType ct = new ContentType(bs.type, bs.subtype, bs.cParams);
	type = ct.toString();
    }

    /* Override this method to make it a no-op, rather than throw
     * an IllegalWriteException. This will permit IMAPBodyParts to
     * be inserted in newly crafted MimeMessages, especially when
     * forwarding or replying to messages.
     */
    protected void updateHeaders() {
	return;
    }

    public int getSize() throws MessagingException {
	return bs.size;
    }

    public int getLineCount() throws MessagingException {
	return bs.lines;
    }

    public String getContentType() throws MessagingException {
	return type;
    }

    public String getDisposition() throws MessagingException {
	return bs.disposition;
    }

    public void setDisposition(String disposition) throws MessagingException {
	throw new IllegalWriteException("IMAPBodyPart is read-only");
    }

    public String getEncoding() throws MessagingException {
	return bs.encoding;
    }

    public String getContentID() throws MessagingException {
	return bs.id;
    }

    public String getContentMD5() throws MessagingException {
	return bs.md5;
    }

    public void setContentMD5(String md5) throws MessagingException {
	throw new IllegalWriteException("IMAPBodyPart is read-only");
    }

    public String getDescription() throws MessagingException {
	if (description != null) // cached value ?
	    return description;

	if (bs.description == null)
	    return null;
	
	try {
	    description = MimeUtility.decodeText(bs.description);
	} catch (UnsupportedEncodingException ex) {
	    description = bs.description;
	}

	return description;
    }

    public void setDescription(String description, String charset)
			throws MessagingException {
	throw new IllegalWriteException("IMAPBodyPart is read-only");
    }

    public String getFileName() throws MessagingException {
	String filename = null;
	if (bs.dParams != null)
	    filename = bs.dParams.get("filename");
	if (filename == null && bs.cParams != null)
	    filename = bs.cParams.get("name");
	return filename;
    }

    public void setFileName(String filename) throws MessagingException {
	throw new IllegalWriteException("IMAPBodyPart is read-only");
    }

    protected InputStream getContentStream() throws MessagingException {
	InputStream is = null;
	boolean pk = message.getPeek();	// acquire outisde of message cache lock

        // Acquire MessageCacheLock, to freeze seqnum.
        synchronized(message.getMessageCacheLock()) {
	    IMAPProtocol p = message.getProtocol();
	    if (p.isREV1() && (message.getFetchBlockSize() != -1))
		return new IMAPInputStream(message, sectionId, bs.size, pk);

	    // Else, vanila IMAP4, no partial fetch 

	    // Check whether this message is expunged
	    message.checkExpunged();

	    int seqnum = message.getSequenceNumber();
	    try {
		BODY b;
		if (message.getPeek())
		    b = p.peekBody(seqnum, sectionId);
		else
		    b = p.fetchBody(seqnum, sectionId);
		if (b != null)
		    is = b.getByteArrayInputStream();
	    } catch (ConnectionException cex) {
		throw new FolderClosedException(
			message.getFolder(), cex.getMessage());
	    } catch (ProtocolException pex) { 
		throw new MessagingException(pex.getMessage(), pex);
	    }
	}

	if (is == null)
	    throw new MessagingException("No content");
	else
	    return is;
    }
	    
    public synchronized DataHandler getDataHandler() 
		throws MessagingException {
	if (dh == null) {
	   if (bs.isMulti())
		dh = new DataHandler(
			new IMAPMultipartDataSource(
				this, bs.bodies, sectionId, message)
		     );
	    else if (bs.isNested() && message.getProtocol().isREV1())
		dh = new DataHandler(
			new IMAPNestedMessage(message, 
					      bs.bodies[0],
					      bs.envelope,
					      sectionId),
			type
		     );
	}

	return super.getDataHandler();
    }

    public void setDataHandler(DataHandler content) throws MessagingException {
	throw new IllegalWriteException("IMAPBodyPart is read-only");
    }

    public void setContent(Object o, String type) throws MessagingException {
	throw new IllegalWriteException("IMAPBodyPart is read-only");
    }

    public void setContent(Multipart mp) throws MessagingException {
	throw new IllegalWriteException("IMAPBodyPart is read-only");
    }

    public String[] getHeader(String name) throws MessagingException {
	loadHeaders();
	return super.getHeader(name);
    }

    public void setHeader(String name, String value)
		throws MessagingException {
	throw new IllegalWriteException("IMAPBodyPart is read-only");
    }

    public void addHeader(String name, String value)
		throws MessagingException {
	throw new IllegalWriteException("IMAPBodyPart is read-only");
    }

    public void removeHeader(String name) throws MessagingException {
	throw new IllegalWriteException("IMAPBodyPart is read-only");
    }

    public Enumeration getAllHeaders() throws MessagingException {
	loadHeaders();
	return super.getAllHeaders();
    }

    public Enumeration getMatchingHeaders(String[] names)
		throws MessagingException {
	loadHeaders();
	return super.getMatchingHeaders(names);
    }

    public Enumeration getNonMatchingHeaders(String[] names)
		throws MessagingException {
	loadHeaders();
	return super.getNonMatchingHeaders(names);
    }

    public void addHeaderLine(String line) throws MessagingException {
	throw new IllegalWriteException("IMAPBodyPart is read-only");
    }

    public Enumeration getAllHeaderLines() throws MessagingException {
	loadHeaders();
	return super.getAllHeaderLines();
    }

    public Enumeration getMatchingHeaderLines(String[] names)
		throws MessagingException {
	loadHeaders();
	return super.getMatchingHeaderLines(names);
    }

    public Enumeration getNonMatchingHeaderLines(String[] names)
		throws MessagingException {
	loadHeaders();
	return super.getNonMatchingHeaderLines(names);
    }

    private synchronized void loadHeaders() throws MessagingException {
	if (headersLoaded)
	    return;

	if (headers == null)
	    headers = new InternetHeaders();

	// load headers
	IMAPProtocol p = message.getProtocol();
	if (p.isREV1()) {
	    BODY b = null;

	    // Acquire MessageCacheLock, to freeze seqnum.
	    synchronized(message.getMessageCacheLock()) {
		// fetch again, in the unlikely event folder has been closed
		p = message.getProtocol();

		// Check whether this message got expunged
		message.checkExpunged();

		int seqnum = message.getSequenceNumber();
		try {
		    b = p.peekBody(seqnum, sectionId + ".MIME");
		} catch (ConnectionException cex) {
		    throw new FolderClosedException(
				message.getFolder(), cex.getMessage());
		} catch (ProtocolException pex) {
		    throw new MessagingException(pex.getMessage(), pex);
		}
	    }

	    if (b == null)
		throw new MessagingException("Failed to fetch headers");

	    ByteArrayInputStream bis = b.getByteArrayInputStream();
	    if (bis == null)
		throw new MessagingException("Failed to fetch headers");

	    headers.load(bis);

	} else {

	    // RFC 1730 does not provide for fetching BodyPart headers
	    // So, just dump the RFC1730 BODYSTRUCTURE into the headerStore
	    
	    // Content-Type
	    headers.addHeader("Content-Type", type);
	    // Content-Transfer-Encoding
	    headers.addHeader("Content-Transfer-Encoding", bs.encoding);
	    // Content-Description
	    if (bs.description != null)
		headers.addHeader("Content-Description", bs.description);
	    // Content-ID
	    if (bs.id != null)
		headers.addHeader("Content-ID", bs.id);
	    // Content-MD5
	    if (bs.md5 != null)
		headers.addHeader("Content-MD5", bs.md5);
	}
	headersLoaded = true;
    }
}
