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
 * @(#)AddressTerm.java	1.9 05/08/29
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package javax.mail.search;

import javax.mail.Address;

/**
 * This class implements Message Address comparisons.
 *
 * @author Bill Shannon
 * @author John Mani
 */

public abstract class AddressTerm extends SearchTerm {
    /**
     * The address.
     *
     * @serial
     */
    protected Address address;

    private static final long serialVersionUID = 2005405551929769980L;

    protected AddressTerm(Address address) {
	this.address = address;
    }

    /**
     * Return the address to match with.
     */
    public Address getAddress() {
	return address;
    }

    /**
     * Match against the argument Address.
     */
    protected boolean match(Address a) {
	return (a.equals(address));
    }

    /**
     * Equality comparison.
     */
    public boolean equals(Object obj) {
	if (!(obj instanceof AddressTerm))
	    return false;
	AddressTerm at = (AddressTerm)obj;
	return at.address.equals(this.address);
    }

    /**
     * Compute a hashCode for this object.
     */
    public int hashCode() {
	return address.hashCode();
    }
}
