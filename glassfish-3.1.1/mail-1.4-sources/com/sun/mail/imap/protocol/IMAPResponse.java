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
 * @(#)IMAPResponse.java	1.4 05/08/29
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package com.sun.mail.imap.protocol;

import java.io.*;
import java.util.*;
import com.sun.mail.util.*;
import com.sun.mail.iap.*;

/**
 * This class represents a response obtained from the input stream
 * of an IMAP server.
 *
 * @version 1.4, 05/08/29
 * @author  John Mani
 */

public class IMAPResponse extends Response {
    private String key;
    private int number;

    public IMAPResponse(Protocol c) throws IOException, ProtocolException {
	super(c);

	// continue parsing if this is an untagged response
	if (isUnTagged() && !isOK() && !isNO() && !isBAD() && !isBYE()) {
	    key = readAtom();

	    // Is this response of the form "* <number> <command>"
	    try {
		number = Integer.parseInt(key);
		key = readAtom();
	    } catch (NumberFormatException ne) { }
	}
    }

    /**
     * Copy constructor.
     */
    public IMAPResponse(IMAPResponse r) {
	super((Response)r);
	key = r.key;
	number = r.number;
    }

    /**
     * Read a list of space-separated "flag_extension" sequences and 
     * return the list as a array of Strings. An empty list is returned
     * as null.  This is an IMAP-ism, and perhaps this method should 
     * moved into the IMAP layer.
     */
    public String[] readSimpleList() {
	skipSpaces();

	if (buffer[index] != '(') // not what we expected
	    return null;
	index++; // skip '('

	Vector v = new Vector();
	int start;
	for (start = index; buffer[index] != ')'; index++) {
	    if (buffer[index] == ' ') { // got one item
		v.addElement(ASCIIUtility.toString(buffer, start, index));
		start = index+1; // index gets incremented at the top
	    }
	}
	if (index > start) // get the last item
	    v.addElement(ASCIIUtility.toString(buffer, start, index));
	index++; // skip ')'
	
	int size = v.size();
	if (size > 0) {
	    String[] s = new String[size];
	    v.copyInto(s);
	    return s;
	} else  // empty list
	    return null;
    }

    public String getKey() {
	return key;
    }

    public boolean keyEquals(String k) {
	if (key != null && key.equalsIgnoreCase(k))
	    return true;
	else
	    return false;
    }

    public int getNumber() {
	return number;
    }

    public static IMAPResponse readResponse(Protocol p) 
			throws IOException, ProtocolException {
	IMAPResponse r = new IMAPResponse(p);
	if (r.keyEquals("FETCH"))
	    r = new FetchResponse(r);
	return r;
    }
}
