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
 * @(#)SentDateTerm.java	1.8 05/08/29
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package javax.mail.search;

import java.util.Date;
import javax.mail.Message;

/**
 * This class implements comparisons for the Message SentDate.
 *
 * @author Bill Shannon
 * @author John Mani
 */
public final class SentDateTerm extends DateTerm {

    private static final long serialVersionUID = 5647755030530907263L;

    /**
     * Constructor.
     *
     * @param comparison	the Comparison type
     * @param date		the date to be compared
     */
    public SentDateTerm(int comparison, Date date) {
	super(comparison, date);
    }

    /**
     * The match method.
     *
     * @param msg	the date comparator is applied to this Message's
     *			sent date
     * @return		true if the comparison succeeds, otherwise false
     */
    public boolean match(Message msg) {
	Date d;

	try {
	    d = msg.getSentDate();
	} catch (Exception e) {
	    return false;
	}

	if (d == null)
	    return false;

	return super.match(d);
    }

    /**
     * Equality comparison.
     */
    public boolean equals(Object obj) {
	if (!(obj instanceof SentDateTerm))
	    return false;
	return super.equals(obj);
    }
}
