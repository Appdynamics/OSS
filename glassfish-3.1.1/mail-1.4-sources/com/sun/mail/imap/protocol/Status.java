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
 * @(#)Status.java	1.6 05/08/29
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package com.sun.mail.imap.protocol;

import com.sun.mail.iap.*;

/**
 * This class 
 *
 * @version 1.6, 05/08/29
 * @author  John Mani
 */

public class Status { 
    public String mbox = null;
    public int total = -1;
    public int recent = -1;
    public long uidnext = -1;
    public long uidvalidity = -1;
    public int unseen = -1;

    public static String[] standardItems =
	{ "MESSAGES", "RECENT", "UNSEEN", "UIDNEXT", "UIDVALIDITY" };

    public Status(Response r) throws ParsingException {
	mbox = r.readAtomString(); // mailbox := astring
	r.skipSpaces();
	if (r.readByte() != '(')
	    throw new ParsingException("parse error in STATUS");
	
	do {
	    String attr = r.readAtom();
	    if (attr.equalsIgnoreCase("MESSAGES"))
		total = r.readNumber();
	    else if (attr.equalsIgnoreCase("RECENT"))
		recent = r.readNumber();
	    else if (attr.equalsIgnoreCase("UIDNEXT"))
		uidnext = r.readLong();
	    else if (attr.equalsIgnoreCase("UIDVALIDITY"))
		uidvalidity = r.readLong();
	    else if (attr.equalsIgnoreCase("UNSEEN"))
		unseen = r.readNumber();
	} while (r.readByte() != ')');
    }

    public static void add(Status s1, Status s2) {
	if (s2.total != -1)
	    s1.total = s2.total;
	if (s2.recent != -1)
	    s1.recent = s2.recent;
	if (s2.uidnext != -1)
	    s1.uidnext = s2.uidnext;
	if (s2.uidvalidity != -1)
	    s1.uidvalidity = s2.uidvalidity;
	if (s2.unseen != -1)
	    s1.unseen = s2.unseen;
    }
}
