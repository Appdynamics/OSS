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
 * @(#)SizeTerm.java	1.8 05/08/29
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package javax.mail.search;

import javax.mail.Message;

/**
 * This class implements comparisons for Message sizes.
 *
 * @author Bill Shannon
 * @author John Mani
 */
public final class SizeTerm extends IntegerComparisonTerm {

    private static final long serialVersionUID = -2556219451005103709L;

    /**
     * Constructor.
     *
     * @param comparison	the Comparison type
     * @param size		the size
     */
    public SizeTerm(int comparison, int size) {
	super(comparison, size);
    }

    /**
     * The match method.
     *
     * @param msg	the size comparator is applied to this Message's size
     * @return		true if the size is equal, otherwise false 
     */
    public boolean match(Message msg) {
	int size;

	try {
	    size = msg.getSize();
	} catch (Exception e) {
	    return false;
	}

	if (size == -1)
	    return false;

	return super.match(size);
    }

    /**
     * Equality comparison.
     */
    public boolean equals(Object obj) {
	if (!(obj instanceof SizeTerm))
	    return false;
	return super.equals(obj);
    }
}
