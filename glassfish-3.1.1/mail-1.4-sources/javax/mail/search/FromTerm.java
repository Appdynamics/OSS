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
 * @(#)FromTerm.java	1.8 05/08/29
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package javax.mail.search;

import javax.mail.Message;
import javax.mail.Address;

/**
 * This class implements comparisons for the From Address header.
 *
 * @author Bill Shannon
 * @author John Mani
 */
public final class FromTerm extends AddressTerm {

    private static final long serialVersionUID = 5214730291502658665L;

    /**
     * Constructor
     * @param address	The Address to be compared
     */
    public FromTerm(Address address) {
	super(address);
    }

    /**
     * The address comparator.
     *
     * @param msg	The address comparison is applied to this Message
     * @return		true if the comparison succeeds, otherwise false
     */
    public boolean match(Message msg) {
	Address[] from;

	try {
	    from = msg.getFrom();
	} catch (Exception e) {
	    return false;
	}

	if (from == null)
	    return false;

	for (int i=0; i < from.length; i++)
	    if (super.match(from[i]))
		return true;
	return false;
    }

    /**
     * Equality comparison.
     */
    public boolean equals(Object obj) {
	if (!(obj instanceof FromTerm))
	    return false;
	return super.equals(obj);
    }
}
