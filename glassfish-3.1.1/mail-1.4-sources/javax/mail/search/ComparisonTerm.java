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
 * @(#)ComparisonTerm.java	1.8 05/08/29
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package javax.mail.search;

/**
 * This class models the comparison operator. This is an abstract
 * class; subclasses implement comparisons for different datatypes.
 *
 * @author Bill Shannon
 * @author John Mani
 */
public abstract class ComparisonTerm extends SearchTerm {
    public static final int LE = 1;
    public static final int LT = 2;
    public static final int EQ = 3;
    public static final int NE = 4;
    public static final int GT = 5;
    public static final int GE = 6;

    /**
     * The comparison.
     *
     * @serial
     */
    protected int comparison;

    private static final long serialVersionUID = 1456646953666474308L;

    /**
     * Equality comparison.
     */
    public boolean equals(Object obj) {
	if (!(obj instanceof ComparisonTerm))
	    return false;
	ComparisonTerm ct = (ComparisonTerm)obj;
	return ct.comparison == this.comparison;
    }

    /**
     * Compute a hashCode for this object.
     */
    public int hashCode() {
	return comparison;
    }
}
