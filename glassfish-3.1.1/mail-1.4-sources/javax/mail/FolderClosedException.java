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
 * @(#)FolderClosedException.java	1.6 05/08/29
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package javax.mail;

/**
 * This exception is thrown when a method is invoked on a Messaging object
 * and the Folder that owns that object has died due to some reason. <p>
 *
 * Following the exception, the Folder is reset to the "closed" state. 
 * All messaging objects owned by the Folder should be considered invalid. 
 * The Folder can be reopened using the "open" method to reestablish the 
 * lost connection. <p>
 *
 * The getMessage() method returns more detailed information about the
 * error that caused this exception. <p>
 *
 * @author John Mani
 */

public class FolderClosedException extends MessagingException {
    transient private Folder folder;

    private static final long serialVersionUID = 1687879213433302315L;
    
    /**
     * Constructor
     * @param folder	the Folder
     */
    public FolderClosedException(Folder folder) {
	this(folder, null);
    }

    /**
     * Constructor
     * @param folder 	the Folder
     * @param message	the detailed error message
     */
    public FolderClosedException(Folder folder, String message) {
	super(message);
	this.folder = folder;
    }

    /**
     * Returns the dead Folder object
     */
    public Folder getFolder() {
	return folder;
    }
}
