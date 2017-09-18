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
 * @(#)ListInfo.java	1.10 05/08/29
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package com.sun.mail.imap.protocol;

import java.util.Vector;

import com.sun.mail.iap.*;

/**
 * A LIST response.
 *
 * @version 1.10, 05/08/29
 * @author  John Mani
 * @author  Bill Shannon
 */

public class ListInfo { 
    public String name = null;
    public char separator = '/';
    public boolean hasInferiors = true;
    public boolean canOpen = true;
    public int changeState = INDETERMINATE;
    public String[] attrs;

    public static final int CHANGED		= 1;
    public static final int UNCHANGED		= 2;
    public static final int INDETERMINATE	= 3;

    public ListInfo(IMAPResponse r) throws ParsingException {
	String[] s = r.readSimpleList();

	Vector v = new Vector();	// accumulate attributes
	if (s != null) {
	    // non-empty attribute list
	    for (int i = 0; i < s.length; i++) {
		if (s[i].equalsIgnoreCase("\\Marked"))
		    changeState = CHANGED;
		else if (s[i].equalsIgnoreCase("\\Unmarked"))
		    changeState = UNCHANGED;
		else if (s[i].equalsIgnoreCase("\\Noselect"))
		    canOpen = false;
		else if (s[i].equalsIgnoreCase("\\Noinferiors"))
		    hasInferiors = false;
		v.addElement(s[i]);
	    }
	}
	attrs = new String[v.size()];
	v.copyInto(attrs);

	r.skipSpaces();
	if (r.readByte() == '"') {
	    if ((separator = (char)r.readByte()) == '\\')
		// escaped separator character
		separator = (char)r.readByte();	
	    r.skip(1); // skip <">
	} else // NIL
	    r.skip(2);
	
	r.skipSpaces();
	name = r.readAtomString();

	// decode the name (using RFC2060's modified UTF7)
	name = BASE64MailboxDecoder.decode(name);
    }
}
