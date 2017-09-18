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
 * @(#)HeaderTerm.java	1.9 05/08/29
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package javax.mail.search;

import javax.mail.Message;

/**
 * This class implements comparisons for Message headers.
 * The comparison is case-insensitive.
 *
 * @author Bill Shannon
 * @author John Mani
 */
public final class HeaderTerm extends StringTerm {
    /**
     * The name of the header.
     *
     * @serial
     */
    protected String headerName;

    private static final long serialVersionUID = 8342514650333389122L;

    /**
     * Constructor.
     *
     * @param headerName The name of the header
     * @param pattern    The pattern to search for
     */
    public HeaderTerm(String headerName, String pattern) {
	super(pattern);
	this.headerName = headerName;
    }

    /**
     * Return the name of the header to compare with.
     */
    public String getHeaderName() {
	return headerName;
    }

    /**
     * The header match method.
     *
     * @param msg	The match is applied to this Message's header
     * @return		true if the match succeeds, otherwise false
     */
    public boolean match(Message msg) {
	String[] headers;

	try {
	    headers = msg.getHeader(headerName);
	} catch (Exception e) {
	    return false;
	}

	if (headers == null)
	    return false;

	for (int i=0; i < headers.length; i++)
	    if (super.match(headers[i]))
		return true;
	return false;
    }

    /**
     * Equality comparison.
     */
    public boolean equals(Object obj) {
	if (!(obj instanceof HeaderTerm))
	    return false;
	HeaderTerm ht = (HeaderTerm)obj;
	// XXX - depends on header comparisons being case independent
	return ht.headerName.equalsIgnoreCase(headerName) && super.equals(ht);
    }

    /**
     * Compute a hashCode for this object.
     */
    public int hashCode() {
	// XXX - depends on header comparisons being case independent
	return headerName.toLowerCase().hashCode() + super.hashCode();
    }
}
