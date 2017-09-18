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
 * @(#)DefaultFolder.java	1.7, 05/08/29
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package com.sun.mail.pop3;

import javax.mail.*;

/**
 * The POP3 DefaultFolder.  Only contains the "INBOX" folder.
 *
 * @version 1.7, 05/08/29
 * @author Christopher Cotton
 */
public class DefaultFolder extends Folder {

    DefaultFolder(POP3Store store) {
	super(store);
    }

    public String getName() {
	return "";
    }

    public String getFullName() {
	return "";
    }

    public Folder getParent() {
	return null;
    }

    public boolean exists() {
	return true;
    }

    public Folder[] list(String pattern) throws MessagingException {
	Folder[] f = { getInbox() };
	return f;
    }

    public char getSeparator() {
	return '/';
    }

    public int getType() {
	return HOLDS_FOLDERS;
    }

    public boolean create(int type) throws MessagingException {
	return false;
    }

    public boolean hasNewMessages() throws MessagingException {
	return false;
    }

    public Folder getFolder(String name) throws MessagingException {
	if (!name.equalsIgnoreCase("INBOX")) {
	    throw new MessagingException("only INBOX supported");
	} else {
	    return getInbox();
	}
    }

    protected Folder getInbox() throws MessagingException {
	return getStore().getFolder("INBOX");
    }
    

    public boolean delete(boolean recurse) throws MessagingException {
	throw new MethodNotSupportedException("delete");
    }

    public boolean renameTo(Folder f) throws MessagingException {
	throw new MethodNotSupportedException("renameTo");
    }

    public void open(int mode) throws MessagingException {
	throw new MethodNotSupportedException("open");
    }

    public void close(boolean expunge) throws MessagingException {
	throw new MethodNotSupportedException("close");
    }

    public boolean isOpen() {
	return false;
    }

    public Flags getPermanentFlags() {
	return new Flags(); // empty flags object
    }

    public int getMessageCount() throws MessagingException {
	return 0;
    }

    public Message getMessage(int msgno) throws MessagingException {
	throw new MethodNotSupportedException("getMessage");
    }

    public void appendMessages(Message[] msgs) throws MessagingException {
	throw new MethodNotSupportedException("Append not supported");	
    }

    public Message[] expunge() throws MessagingException {
	throw new MethodNotSupportedException("expunge");	
    }
}
