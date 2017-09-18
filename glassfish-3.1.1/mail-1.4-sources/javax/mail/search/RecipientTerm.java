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
 * @(#)RecipientTerm.java	1.10 05/08/29
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package javax.mail.search;

import javax.mail.Message;
import javax.mail.Address;

/**
 * This class implements comparisons for the Recipient Address headers.
 *
 * @author Bill Shannon
 * @author John Mani
 */
public final class RecipientTerm extends AddressTerm {

    /**
     * The recipient type.
     *
     * @serial
     */
    protected Message.RecipientType type;

    private static final long serialVersionUID = 6548700653122680468L;

    /**
     * Constructor.
     *
     * @param type	the recipient type
     * @param address	the address to match for
     */
    public RecipientTerm(Message.RecipientType type, Address address) {
	super(address);
	this.type = type;
    }

    /**
     * Return the type of recipient to match with.
     */
    public Message.RecipientType getRecipientType() {
	return type;
    }

    /**
     * The match method.
     *
     * @param msg	The address match is applied to this Message's recepient
     *			address
     * @return		true if the match succeeds, otherwise false
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
	if (!(obj instanceof RecipientTerm))
	    return false;
	RecipientTerm rt = (RecipientTerm)obj;
	return rt.type.equals(this.type) && super.equals(obj);
    }

    /**
     * Compute a hashCode for this object.
     */
    public int hashCode() {
	return type.hashCode() + super.hashCode();
    }
}
