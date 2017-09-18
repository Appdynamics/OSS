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
 * @(#)NotTerm.java	1.9 05/08/29
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package javax.mail.search;

import javax.mail.Message;

/**
 * This class implements the logical NEGATION operator.
 *
 * @author Bill Shannon
 * @author John Mani
 */
public final class NotTerm extends SearchTerm {
    /**
     * The search term to negate.
     *
     * @serial
     */
    protected SearchTerm term;

    private static final long serialVersionUID = 7152293214217310216L;

    public NotTerm(SearchTerm t) {
	term = t;
    }

    /**
     * Return the term to negate.
     */
    public SearchTerm getTerm() {
	return term;
    }

    /* The NOT operation */
    public boolean match(Message msg) {
	return !term.match(msg);
    }

    /**
     * Equality comparison.
     */
    public boolean equals(Object obj) {
	if (!(obj instanceof NotTerm))
	    return false;
	NotTerm nt = (NotTerm)obj;
	return nt.term.equals(this.term);
    }

    /**
     * Compute a hashCode for this object.
     */
    public int hashCode() {
	return term.hashCode() << 1;
    }
}
