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
 * @(#)AndTerm.java	1.9 05/08/29
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package javax.mail.search;

import javax.mail.Message;

/**
 * This class implements the logical AND operator on individual
 * SearchTerms.
 *
 * @author Bill Shannon
 * @author John Mani
 */
public final class AndTerm extends SearchTerm {

    /**
     * The array of terms on which the AND operator should be
     * applied.
     *
     * @serial
     */
    protected SearchTerm[] terms;

    private static final long serialVersionUID = -3583274505380989582L;

    /**
     * Constructor that takes two terms.
     * 
     * @param t1 first term
     * @param t2 second term
     */
    public AndTerm(SearchTerm t1, SearchTerm t2) {
	terms = new SearchTerm[2];
	terms[0] = t1;
	terms[1] = t2;
    }

    /**
     * Constructor that takes an array of SearchTerms.
     * 
     * @param t  array of terms
     */
    public AndTerm(SearchTerm[] t) {
	terms = new SearchTerm[t.length]; // clone the array
	for (int i = 0; i < t.length; i++)
	    terms[i] = t[i];
    }

    /**
     * Return the search terms.
     */
    public SearchTerm[] getTerms() {
	return (SearchTerm[])terms.clone();
    }

    /**
     * The AND operation. <p>
     *
     * The terms specified in the constructor are applied to
     * the given object and the AND operator is applied to their results.
     *
     * @param msg	The specified SearchTerms are applied to this Message
     *			and the AND operator is applied to their results.
     * @return		true if the AND succeds, otherwise false
     */
    public boolean match(Message msg) {
	for (int i=0; i < terms.length; i++)
	    if (!terms[i].match(msg))
		return false;
	return true;
    }

    /**
     * Equality comparison.
     */
    public boolean equals(Object obj) {
	if (!(obj instanceof AndTerm))
	    return false;
	AndTerm at = (AndTerm)obj;
	if (at.terms.length != terms.length)
	    return false;
	for (int i=0; i < terms.length; i++)
	    if (!terms[i].equals(at.terms[i]))
		return false;
	return true;
    }

    /**
     * Compute a hashCode for this object.
     */
    public int hashCode() {
	int hash = 0;
	for (int i=0; i < terms.length; i++)
	    hash += terms[i].hashCode();
	return hash;
    }
}
