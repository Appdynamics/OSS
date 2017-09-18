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
 * @(#)RecipientStringTerm.java	1.8 05/08/29
 *
 * Copyright 1998-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package javax.mail.search;

import javax.mail.Message;
import javax.mail.Address;

/**
 * This class implements string comparisons for the Recipient Address
 * headers. <p>
 *
 * Note that this class differs from the <code>RecipientTerm</code> class
 * in that this class does comparisons on address strings rather than Address
 * objects. The string comparisons are case-insensitive.
 *
 * @since       JavaMail 1.1
 */

public final class RecipientStringTerm extends AddressStringTerm {

    /**
     * The recipient type.
     *
     * @serial
     */
    private Message.RecipientType type;

    private static final long serialVersionUID = -8293562089611618849L;

    /**
     * Constructor.
     *
     * @param type      the recipient type
     * @param pattern   the address pattern to be compared.
     */
    public RecipientStringTerm(Message.RecipientType type, String pattern) {
	super(pattern);
	this.type = type;
    }

    /**
     * Return the type of recipient to match with.
     */
    public Message.RecipientType getRecipientType() {
	return type;
    }

    /**
     * Check whether the address specified in the constructor is
     * a substring of the recipient address of this Message.
     *
     * @param   msg 	The comparison is applied to this Message's recepient
     *		    	address.
     * @return          true if the match succeeds, otherwise false.
     */
    public boolean match(Message msg) {
	Address[] recipients;

	try {
	    recipients = msg.getRecipients(type);
	} catch (Exception e) {
	    return false;
	}

	if (recipients == null)
	    return false;
	
	for (int i=0; i < recipients.length; i++)
	    if (super.match(recipients[i]))
		return true;
	return false;
    }

    /**
     * Equality comparison.
     */
    public boolean equals(Object obj) {
	if (!(obj instanceof RecipientStringTerm))
	    return false;
	RecipientStringTerm rst = (RecipientStringTerm)obj;
	return rst.type.equals(this.type) && super.equals(obj);
    }

    /**
     * Compute a hashCode for this object.
     */
    public int hashCode() {
	return type.hashCode() + super.hashCode();
    }
}
