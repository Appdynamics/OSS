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
 * @(#)PasswordAuthentication.java	1.5 05/08/29
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package javax.mail;


/**
 * The class PasswordAuthentication is a data holder that is used by
 * Authenticator.  It is simply a repository for a user name and a password.
 *
 * @see java.net.PasswordAuthentication
 * @see javax.mail.Authenticator
 * @see javax.mail.Authenticator#getPasswordAuthentication()
 *
 * @author  Bill Foote
 * @version 1.5, 08/29/05
 */

public final class PasswordAuthentication {

    private String userName;
    private String password;

    /** 
     * Initialize a new PasswordAuthentication
     * @param userName the user name
     * @param password The user's password
     */
    public PasswordAuthentication(String userName, String password) {
	this.userName = userName;
	this.password = password;
    }

    /**
     * @return the user name
     */
    public String getUserName() {
	return userName;
    }

    /**
     * @return the password
     */
    public String getPassword() {
	return password;
    }
}
