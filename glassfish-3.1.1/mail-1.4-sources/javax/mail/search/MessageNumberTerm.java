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
 * @(#)MessageNumberTerm.java	1.9 05/08/29
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package javax.mail.search;

import javax.mail.Message;

/**
 * This class implements comparisons for Message numbers.
 *
 * @author Bill Shannon
 * @author John Mani
 */
public final class MessageNumberTerm extends IntegerComparisonTerm {

    private static final long serialVersionUID = -5379625829658623812L;

    /**
     * Constructor.
     *
     * @param number  the Message number
     */
    public MessageNumberTerm(int number) {
	super(EQ, number);
    }

    /**
     * The match method.
     *
     * @param msg	the Message number is matched with this Message
     * @return		true if the match succeeds, otherwise false
     */
    public boolean match(Message msg) {
	int msgno;

	try {
	    msgno = msg.getMessageNumber();
	} catch (Exception e) {
	    return false;
	}
	
	return super.match(msgno);
    }

    /**
     * Equality comparison.
     */
    public boolean equals(Object obj) {
	if (!(obj instanceof MessageNumberTerm))
	    return false;
	return super.equals(obj);
    }
}
