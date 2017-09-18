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
 * @(#)DefaultFolder.java	1.11 05/08/29
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package com.sun.mail.imap;

import javax.mail.*;
import javax.mail.internet.*;
import com.sun.mail.util.*;
import com.sun.mail.iap.*;
import com.sun.mail.imap.protocol.*;

/**
 * This class 
 *
 * @version 1.2, 97/12/08
 * @author  John Mani
 */

public class DefaultFolder extends IMAPFolder {
    
    protected DefaultFolder(IMAPStore store) {
	super("", UNKNOWN_SEPARATOR , store);
	exists = true; // of course
	type = HOLDS_FOLDERS; // obviously
    }

    public String getName() {
	return fullName;
    }

    public Folder getParent() {
	return null;
    }

    public Folder[] list(final String pattern) throws MessagingException {
	ListInfo[] li = null;

	li = (ListInfo[])doCommand(new ProtocolCommand() {
	    public Object doCommand(IMAPProtocol p) throws ProtocolException {
		return p.list("", pattern);
	    }
	});

	if (li == null)
	    return new Folder[0];

	IMAPFolder[] folders = new IMAPFolder[li.length];
	for (int i = 0; i < folders.length; i++)
	    folders[i] = new IMAPFolder(li[i], (IMAPStore)store);
	return folders;
    }

    public Folder[] listSubscribed(final String pattern)
				throws MessagingException {
	ListInfo[] li = null;

	li = (ListInfo[])doCommand(new ProtocolCommand() {
	    public Object doCommand(IMAPProtocol p) throws ProtocolException {
		return p.lsub("", pattern);
	    }
	});

	if (li == null)
	    return new Folder[0];

	IMAPFolder[] folders = new IMAPFolder[li.length];
	for (int i = 0; i < folders.length; i++)
	    folders[i] = new IMAPFolder(li[i], (IMAPStore)store);
	return folders;
    }

    public boolean hasNewMessages() throws MessagingException {
	// Not applicable on DefaultFolder
	return false;
    }

    public Folder getFolder(String name) throws MessagingException {
	return new IMAPFolder(name, UNKNOWN_SEPARATOR, (IMAPStore)store);
    }

    public boolean delete(boolean recurse) throws MessagingException {  
	// Not applicable on DefaultFolder
	throw new MethodNotSupportedException("Cannot delete Default Folder");
    }

    public boolean renameTo(Folder f) throws MessagingException {
	// Not applicable on DefaultFolder
	throw new MethodNotSupportedException("Cannot rename Default Folder");
    }

    public void appendMessages(Message[] msgs) throws MessagingException {
	// Not applicable on DefaultFolder
	throw new MethodNotSupportedException("Cannot append to Default Folder");
    }

    public Message[] expunge() throws MessagingException {
	// Not applicable on DefaultFolder
	throw new MethodNotSupportedException("Cannot expunge Default Folder");
    }
}
