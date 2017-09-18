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
 * @(#)Address.java	1.9 05/08/29
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package javax.mail;

import java.io.Serializable;

/**
 * This abstract class models the addresses in a message.
 * Subclasses provide specific implementations.  Subclasses
 * will typically be serializable so that (for example) the
 * use of Address objects in search terms can be serialized
 * along with the search terms.
 *
 * @author John Mani
 * @author Bill Shannon
 */

public abstract class Address implements Serializable {

    private static final long serialVersionUID = -5822459626751992278L;

    /**
     * Return a type string that identifies this address type.
     *
     * @return	address type
     * @see	javax.mail.internet.InternetAddress
     */
    public abstract String getType();

    /**
     * Return a String representation of this address object.
     *
     * @return	string representation of this address
     */
    public abstract String toString();

    /**
     * The equality operator.  Subclasses should provide an
     * implementation of this method that supports value equality
     * (do the two Address objects represent the same destination?),
     * not object reference equality.  A subclass must also provide
     * a corresponding implementation of the <code>hashCode</code>
     * method that preserves the general contract of
     * <code>equals</code> and <code>hashCode</code> - objects that
     * compare as equal must have the same hashCode.
     *
     * @param	address	Address object
     */
    public abstract boolean equals(Object address);
}
