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


package com.sun.enterprise.security.auth.realm.file;

import java.util.*;

import org.glassfish.security.common.PrincipalImpl;
import com.sun.enterprise.security.auth.realm.NoSuchRealmException;
import com.sun.enterprise.security.auth.realm.Realm;
import com.sun.enterprise.security.auth.realm.User;


/**
 * Represents a FileRealm user.
 *
 *
 */
public class FileRealmUser extends PrincipalImpl implements User
{
    private static final String GROUP_KEY = "Groups"; // not needed
    private String[] groups;
    private String realm;
    private Hashtable attributes; 

    private byte[] salt;
    private byte[] hash;
    
    /**
     * Constructor.
     *
     */
    public FileRealmUser(String name)
    {
        super(name);
        attributes = new Hashtable(1);       // not really needed
    }


    /**
     * Constructor.
     *
     * @param name User name.
     * @param groups Group memerships.
     * @param realm Realm.
     * @param salt SSHA salt.
     * @param hash SSHA password hash.
     *
     */
    public FileRealmUser(String name, String[] groups, String realm,
                         byte[] salt, byte[] hash)
    {
        super(name);
        this.groups = groups;
        this.realm = realm;
        this.hash = hash;
        this.salt = salt;
        
        attributes = new Hashtable(1);       // not really needed
        attributes.put(GROUP_KEY, groups);
    }


    /**
     * Returns salt value.
     *
     */
    public byte[] getSalt()
    {
        return salt;
    }


    /**
     * Set salt value.
     *
     */
    public void setSalt(byte[] salt)
    {
        this.salt = salt;
    }


    /**
     * Get hash value.
     *
     */
    public byte[] getHash()
    {
        return hash;
    }


    /**
     * Set hash value.
     *
     */
    public void setHash(byte[] hash)
    {
        this.hash = hash;
    }

    
    /**
     * Returns the realm with which this user is associated
     *
     * @return Realm name.
     * @exception NoSuchRealmException if the realm associated this user
     *            no longer exist
     *
     */
    public Realm getRealm() throws NoSuchRealmException
    {
	return Realm.getInstance(realm);
    }


    /**
     * Return the names of the groups this user belongs to.
     *
     * @return String[] List of group memberships.
     *
     */
    public String[] getGroups()
    {
	return groups;
    }


    /**
     * Set group membership.
     *
     */
    public void setGroups(Vector grp)
    {
        String[] g = new String[grp.size()];
        grp.toArray(g);
        this.groups = g;
        attributes.put(GROUP_KEY, groups);
    }

    
    /**
     * Set group membership.
     *
     */
    public void setGroups(String[] grp)
    {
        this.groups = grp;
    }

    
    /**
     * Return the requested attribute for the user.
     * <P>Not really needed.
     *
     * @param key string identifies the attribute.
     */
    public Object getAttribute (String key)
    {
        return attributes.get(key);
    }

    
    /**
     * Return the names of the supported attributes for this user.
     * <P>Not really needed.
     */
    public Enumeration getAttributeNames () {
	return attributes.keys();
    }


    
}
