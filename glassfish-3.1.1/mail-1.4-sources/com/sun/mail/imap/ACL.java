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
 * @(#)ACL.java	1.4 05/08/29
 *
 * Copyright 2000-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package com.sun.mail.imap;

import java.util.*;

/**
 * An access control list entry for a particular authentication identifier
 * (user or group).  Associates a set of Rights with the identifier.
 * See RFC 2086.
 * <p>
 *
 * @author Bill Shannon
 */

public class ACL implements Cloneable {

    private String name;
    private Rights rights;

    /**
     * Construct an ACL entry for the given identifier and with no rights.
     *
     * @param	name	the identifier name
     */
    public ACL(String name) {
	this.name = name;
	this.rights = new Rights();
    }

    /**
     * Construct an ACL entry for the given identifier with the given rights.
     *
     * @param	name	the identifier name
     * @param	rights	the rights
     */
    public ACL(String name, Rights rights) {
	this.name = name;
	this.rights = rights;
    }

    /**
     * Get the identifier name for this ACL entry.
     *
     * @return	the identifier name
     */
    public String getName() {
	return name;
    }

    /**
     * Set the rights associated with this ACL entry.
     *
     * @param	rights	the rights
     */
    public void setRights(Rights rights) {
	this.rights = rights;
    }

    /**
     * Get the rights associated with this ACL entry.
     * Returns the actual Rights object referenced by this ACL;
     * modifications to the Rights object will effect this ACL.
     *
     * @return	the rights
     */
    public Rights getRights() {
	return rights;
    }

    /**
     * Clone this ACL entry.
     */
    public Object clone() throws CloneNotSupportedException {
	ACL acl = (ACL)super.clone();
	acl.rights = (Rights)this.rights.clone();
	return acl;
    }
}
