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
 * @(#)FolderNotFoundException.java	1.7 05/08/29
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package javax.mail;

import java.lang.*;

/**
 * This exception is thrown by Folder methods, when those
 * methods are invoked on a non existent folder.
 *
 * @author John Mani
 */

public class FolderNotFoundException extends MessagingException {
    transient private Folder folder;

    private static final long serialVersionUID = 472612108891249403L;

    /**
     * Constructs a MessagingException with no detail message.
     */
    public FolderNotFoundException() {
	super();
    }

    /**
     * Constructs a MessagingException with the specified folder.
     * @param folder	the Folder
     * @since		JavaMail 1.2 
     */
    public FolderNotFoundException(Folder folder) {
	super();
        this.folder = folder;
    }

    /**
     * Constructs a MessagingException with the specified folder and 
     * the specified detail message.
     * @param folder	the Folder
     * @param s		the detail message
     * @since		JavaMail 1.2
     */
    public FolderNotFoundException(Folder folder, String s) {
	super(s);
	this.folder = folder;
    }

    /**
     * Constructs a MessagingException with the specified detail message
     * and the specified folder.
     * @param s		the detail message
     * @param folder	the Folder
     */
    public FolderNotFoundException(String s, Folder folder) {
	super(s);
	this.folder = folder;
    }

    /**
     * Returns the offending Folder object.
     * @return	the Folder object. Note that the returned value can be
     * 		<code>null</code>.
     */
    public Folder getFolder() {
	return folder;
    }
}
