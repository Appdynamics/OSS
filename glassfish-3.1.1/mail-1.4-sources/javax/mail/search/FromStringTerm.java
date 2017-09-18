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
 * @(#)FromStringTerm.java	1.8 05/08/29
 *
 * Copyright 1998-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package javax.mail.search;

import javax.mail.Message;
import javax.mail.Address;

/**
 * This class implements string comparisons for the From Address
 * header. <p>
 *
 * Note that this class differs from the <code>FromTerm</code> class
 * in that this class does comparisons on address strings rather than Address
 * objects. The string comparisons are case-insensitive.
 *
 * @since       JavaMail 1.1
 */

public final class FromStringTerm extends AddressStringTerm {

    private static final long serialVersionUID = 5801127523826772788L;

    /**
     * Constructor.
     *
     * @param pattern   the address pattern to be compared.
     */
    public FromStringTerm(String pattern) {
	super(pattern);
    }

    /**
     * Check whether the address string specified in the constructor is
     * a substring of the From address of this Message.
     *
     * @param   msg 	The comparison is applied to this Message's From
     *		    	address.
     * @return          true if the match succeeds, otherwise false.
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
	if (!(obj instanceof FromStringTerm))
	    return false;
	return super.equals(obj);
    }
}
