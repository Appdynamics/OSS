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
 * @(#)SubjectTerm.java	1.8 05/08/29
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package javax.mail.search;

import javax.mail.Message;

/**
 * This class implements comparisons for the Message Subject header.
 * The comparison is case-insensitive.
 *
 * @author Bill Shannon
 * @author John Mani
 */
public final class SubjectTerm extends StringTerm {

    private static final long serialVersionUID = 7481568618055573432L;

    /**
     * Constructor.
     *
     * @param pattern  the pattern to search for
     */
    public SubjectTerm(String pattern) {
	// Note: comparison is case-insensitive
	super(pattern);
    }

    /**
     * The match method.
     *
     * @param msg	the pattern match is applied to this Message's 
     *			subject header
     * @return		true if the pattern match succeeds, otherwise false
     */
    public boolean match(Message msg) {
	String subj;

	try {
	    subj = msg.getSubject();
	} catch (Exception e) {
	    return false;
	}

	if (subj == null)
	    return false;

	return super.match(subj);
    }

    /**
     * Equality comparison.
     */
    public boolean equals(Object obj) {
	if (!(obj instanceof SubjectTerm))
	    return false;
	return super.equals(obj);
    }
}
