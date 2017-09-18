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
 * @(#)ReadOnlyFolderException.java	1.6 05/08/29
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package javax.mail;

/**
 * This exception is thrown when an attempt is made to open a folder
 * read-write access when the folder is marked read-only. <p>
 *
 * The getMessage() method returns more detailed information about the
 * error that caused this exception. <p>
 *
 * @author Jim Glennon
 */

public class ReadOnlyFolderException extends MessagingException {
    transient private Folder folder;

    private static final long serialVersionUID = 5711829372799039325L;
    
    /**
     * Constructs a MessagingException with the specified folder.
     * @param folder	the Folder
     * @since 		JavaMail 1.2
     */
    public ReadOnlyFolderException(Folder folder) {
	this(folder, null);
    }

    /**
     * Constructs a MessagingException with the specified folder and
     * the specified detail message.
     * @param folder 	the Folder
     * @param message	the detailed error message
     * @since 		JavaMail 1.2
     */
    public ReadOnlyFolderException(Folder folder, String message) {
	super(message);
	this.folder = folder;
    }

    /**
     * Returns the dead Folder object.
     * @since 		JavaMail 1.2
     */
    public Folder getFolder() {
	return folder;
    }
}
