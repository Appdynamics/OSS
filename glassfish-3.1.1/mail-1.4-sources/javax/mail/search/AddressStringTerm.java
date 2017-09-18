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
 * @(#)AddressStringTerm.java	1.9 05/08/29
 *
 * Copyright 1998-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package javax.mail.search;

import javax.mail.Message;
import javax.mail.Address;
import javax.mail.internet.InternetAddress;

/**
 * This abstract class implements string comparisons for Message 
 * addresses. <p>
 *
 * Note that this class differs from the <code>AddressTerm</code> class
 * in that this class does comparisons on address strings rather than
 * Address objects.
 *
 * @since       JavaMail 1.1
 */

public abstract class AddressStringTerm extends StringTerm {

    private static final long serialVersionUID = 3086821234204980368L;

    /**
     * Constructor.
     *
     * @param pattern   the address pattern to be compared.
     */
    protected AddressStringTerm(String pattern) {
	super(pattern, true); // we need case-insensitive comparison.
    }

    /**
     * Check whether the address pattern specified in the constructor is
     * a substring of the string representation of the given Address
     * object. <p>
     *
     * Note that if the string representation of the given Address object
     * contains charset or transfer encodings, the encodings must be 
     * accounted for, during the match process. <p>
     *
     * @param   a 	The comparison is applied to this Address object.
     * @return          true if the match succeeds, otherwise false.
     */
    protected boolean match(Address a) {
	if (a instanceof InternetAddress) {
	    InternetAddress ia = (InternetAddress)a;
	    // We dont use toString() to get "a"'s String representation,
	    // because InternetAddress.toString() returns a RFC 2047 
	    // encoded string, which isn't what we need here.

	    return super.match(ia.toUnicodeString());
	} else
	    return super.match(a.toString());
    }

    /**
     * Equality comparison.
     */
    public boolean equals(Object obj) {
	if (!(obj instanceof AddressStringTerm))
	    return false;
	return super.equals(obj);
    }
}
