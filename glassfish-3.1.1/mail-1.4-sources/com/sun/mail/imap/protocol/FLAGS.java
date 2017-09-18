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
 * @(#)FLAGS.java	1.8 05/08/29
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package com.sun.mail.imap.protocol;

import javax.mail.Flags;
import com.sun.mail.iap.*; 

/**
 * This class 
 *
 * @version 1.8, 05/08/29
 * @author  John Mani
 */

public class FLAGS extends Flags implements Item {

    // IMAP item name
    public static char[] name = {'F','L','A','G','S'};
    public int msgno;

    private static final long serialVersionUID = 439049847053756670L;

    /**
     * Constructor
     */
    public FLAGS(IMAPResponse r) throws ParsingException {
	msgno = r.getNumber();

	r.skipSpaces();
	String[] flags = r.readSimpleList();
	if (flags != null) { // if not empty flaglist
	    for (int i = 0; i < flags.length; i++) {
		String s = flags[i];
		if (s.length() >= 2 && s.charAt(0) == '\\') {
		    switch (Character.toUpperCase(s.charAt(1))) {
		    case 'S': // \Seen
			add(Flags.Flag.SEEN);
			break;
		    case 'R': // \Recent
			add(Flags.Flag.RECENT);
			break;
		    case 'D':
			if (s.length() >= 3) {
			    char c = s.charAt(2);
			    if (c == 'e' || c == 'E') // \Deleted
				add(Flags.Flag.DELETED);
			    else if (c == 'r' || c == 'R') // \Draft
				add(Flags.Flag.DRAFT);
			} else
			    add(s);	// unknown, treat it as a user flag
			break;
		    case 'A': // \Answered
			add(Flags.Flag.ANSWERED);
			break;
		    case 'F': // \Flagged
			add(Flags.Flag.FLAGGED);
			break;
		    case '*': // \*
			add(Flags.Flag.USER);
			break;
		    default:
			add(s);		// unknown, treat it as a user flag
			break;
		    }
		} else
		    add(s);
	    }
	}
    }
}
