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
 * @(#)DateTerm.java	1.10 05/08/29
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package javax.mail.search;

import java.util.Date;

/**
 * This class implements comparisons for Dates
 *
 * @author Bill Shannon
 * @author John Mani
 */
public abstract class DateTerm extends ComparisonTerm {
    /**
     * The date.
     *
     * @serial
     */
    protected Date date;

    private static final long serialVersionUID = 4818873430063720043L;

    /**
     * Constructor.
     * @param comparison the comparison type
     * @param date  The Date to be compared against
     */
    protected DateTerm(int comparison, Date date) {
	this.comparison = comparison;
	this.date = date;
    }

    /**
     * Return the Date to compare with.
     */
    public Date getDate() {
	return new Date(date.getTime());
    }

    /**
     * Return the type of comparison.
     */
    public int getComparison() {
	return comparison;
    }

    /**
     * The date comparison method.
     *
     * @param d	the date in the constructor is compared with this date
     * @return  true if the dates match, otherwise false
     */
    protected boolean match(Date d) {
	switch (comparison) {
	    case LE: 
		return d.before(date) || d.equals(date);
	    case LT:
		return d.before(date);
	    case EQ:
		return d.equals(date);
	    case NE:
		return !d.equals(date);
	    case GT:
		return d.after(date);
	    case GE:
		return d.after(date) || d.equals(date);
	    default:
		return false;
	}
    }

    /**
     * Equality comparison.
     */
    public boolean equals(Object obj) {
	if (!(obj instanceof DateTerm))
	    return false;
	DateTerm dt = (DateTerm)obj;
	return dt.date.equals(this.date) && super.equals(obj);
    }

    /**
     * Compute a hashCode for this object.
     */
    public int hashCode() {
	return date.hashCode() + super.hashCode();
    }
}
