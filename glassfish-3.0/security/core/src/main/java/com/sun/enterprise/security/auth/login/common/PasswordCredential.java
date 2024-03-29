/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package com.sun.enterprise.security.auth.login.common;

/**
 * This class holds the user password for the shared password realm and the
 * realm name. This credential is added as a private credential to the
 * JAAS subject.
 */

public class PasswordCredential {
    private String username;
    private String password;
    private String realm;
    private boolean readOnly = false;
    // target_name is filled in by the SecSecurityServer interceptor
    // only when a CSIv2 GSSUP authenticator is received.

    private byte[] target_name = {};

    /**
     * Construct a credential with the specified password and realm name.
     * @param the password.
     * @param the realm name. The only value supported for now is "default".
     */
    public PasswordCredential(String user, String password, String realm)
    {
	this.username = user;
	this.password = password;
	this.realm = realm;

        if (this.username == null ) { this.username = ""; }
        if (this.password == null ) { this.password = ""; }
        if (this.realm == null ) { this.realm = ""; }
    }

    
    /**
     * called by SecServerRequestInterceptor 
     * The object if created on the server side is readonly
     */
    public PasswordCredential(String user, String password,
                              String realm, byte[] target_name)
    {
        this(user, password, realm);
	this.target_name = target_name;
        readOnly = true;
    }

    
    /**
     * Return the realm name.
     * @return the realm name. Only value supported for now is "default".
     */
    public String getRealm() {
	return realm;
    }

    
    /**
     * Return the username.
     * @return the user name.
     */
    public String getUser() {
	return username;
    }

    public void setRealm(String realm){
        if(!readOnly){
            this.realm = realm;
        }
    }
    
    /**
     * Return the password.
     * @return the password.
     */
    public String getPassword() {
	return password;
    }

    
    /**
     * Return the target_name
     * @return the target_name
     */
    public byte[] getTargetName() {
	return this.target_name;
    }

    /**
     * Compare two instances of the credential and return true if they are
     * the same and false otherwise.
     * @param the object that this instance is being compared to.
     * @return true if the instances are equal, false otherwise
     */
    public boolean equals(Object o) {
	if(o instanceof PasswordCredential) {
	    PasswordCredential pc = (PasswordCredential) o;
	    if(pc.getUser().equals(username) && 
		pc.getPassword().equals(password) && 
		pc.getRealm().equals(realm)) {
		return true;
	    }
	}
	return false;
    }

    
    /**
     * Return the hashCode computed from the password and realm name.
     * @return the hash code.
     */
    public int hashCode() {
	return username.hashCode() + password.hashCode() + realm.hashCode();
    }

    
    /**
     * The string representation of the credential.
     */
    public String toString() {
	String s = "Realm=" + realm;
	s = s + " Username=" + username;
	s = s + " Password=" + "########";
	s = s + " TargetName = " + new String(target_name);
	return s;
    }

}
