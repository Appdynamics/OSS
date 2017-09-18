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
 * @(#)IntegerComparisonTerm.java	1.8 05/08/29
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package javax.mail.search;

/**
 * This class implements comparisons for integers.
 *
 * @author Bill Shannon
 * @author John Mani
 */
public abstract class IntegerComparisonTerm extends ComparisonTerm {
    /**
     * The number.
     *
     * @serial
     */
    protected int number;

    private static final long serialVersionUID = -6963571240154302484L;

    protected IntegerComparisonTerm(int comparison, int number) {
	this.comparison = comparison;
	this.number = number;
    }

    /**
     * Return the number to compare with.
     */
    public int getNumber() {
	return number;
    }

    /**
     * Return the type of comparison.
     */
    public int getComparison() {
	return comparison;
    }

    protected boolean match(int i) {
	switch (comparison) {
	    case LE: 
		return i <= number;
	    case LT:
		return i < number;
	    case EQ:
		return i == number;
	    case NE:
		return i != number;
	    case GT:
		return i > number;
	    case GE:
		return i >= number;
	    default:
		return false;
	}
    }

    /**
     * Equality comparison.
     */
    public boolean equals(Object obj) {
	if (!(obj instanceof IntegerComparisonTerm))
	    return false;
	IntegerComparisonTerm ict = (IntegerComparisonTerm)obj;
	return ict.number == this.number && super.equals(obj);
    }

    /**
     * Compute a hashCode for this object.
     */
    public int hashCode() {
	return number + super.hashCode();
    }
}
