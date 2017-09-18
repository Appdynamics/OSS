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
 * @(#)BodyTerm.java	1.10 05/08/29
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package javax.mail.search;

import javax.mail.*;

/**
 * This class implements searches on a Message Body.
 * All parts of the message that are of MIME type "text/*" are searched.
 * 
 * @author Bill Shannon
 * @author John Mani
 */
public final class BodyTerm extends StringTerm {

    private static final long serialVersionUID = -4888862527916911385L;

    /**
     * Constructor
     * @param pattern	The String to search for
     */
    public BodyTerm(String pattern) {
	// Note: comparison is case-insensitive
	super(pattern);
    }

    /**
     * The match method.
     *
     * @param msg	The pattern search is applied on this Message's body
     * @return		true if the pattern is found; otherwise false 
     */
    public boolean match(Message msg) {
	return matchPart(msg);
    }

    /**
     * Search all the parts of the message for any text part
     * that matches the pattern.
     */
    private boolean matchPart(Part p) {
	try {
	    /*
	     * Using isMimeType to determine the content type avoids
	     * fetching the actual content data until we need it.
	     */
	    if (p.isMimeType("text/*")) {
		String s = (String)p.getContent();
		if (s == null)
		    return false;
		/*
		 * We invoke our superclass' (i.e., StringTerm) match method.
		 * Note however that StringTerm.match() is not optimized 
		 * for substring searches in large string buffers. We really
		 * need to have a StringTerm subclass, say BigStringTerm, 
		 * with its own match() method that uses a better algorithm ..
		 * and then subclass BodyTerm from BigStringTerm.
		 */ 
		return super.match(s);
	    } else if (p.isMimeType("multipart/*")) {
		Multipart mp = (Multipart)p.getContent();
		int count = mp.getCount();
		for (int i = 0; i < count; i++)
		    if (matchPart(mp.getBodyPart(i)))
			return true;
	    } else if (p.isMimeType("message/rfc822")) {
		return matchPart((Part)p.getContent());
	    }
	} catch (Exception ex) {
	}
	return false;
    }

    /**
     * Equality comparison.
     */
    public boolean equals(Object obj) {
	if (!(obj instanceof BodyTerm))
	    return false;
	return super.equals(obj);
    }
}
