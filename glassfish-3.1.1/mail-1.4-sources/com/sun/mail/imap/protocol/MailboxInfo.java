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
 * @(#)MailboxInfo.java	1.9 05/08/29
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package com.sun.mail.imap.protocol;

import javax.mail.Flags;
import com.sun.mail.iap.*;

/**
 * This class 
 *
 * @version 1.9, 05/08/29
 * @author  John Mani
 */

public class MailboxInfo { 
    public Flags availableFlags = null;
    public Flags permanentFlags = null;
    public int total = -1;
    public int recent = -1;
    public int first = -1;
    public int uidvalidity = -1;
    public int uidnext = -1;
    public int mode;

    public MailboxInfo(Response[] r) throws ParsingException {
	for (int i = 0; i < r.length; i++) {
	    if (r[i] == null || !(r[i] instanceof IMAPResponse))
		continue;

	    IMAPResponse ir = (IMAPResponse)r[i];

	    if (ir.keyEquals("EXISTS")) {
		total = ir.getNumber();
		r[i] = null; // remove this response
	    }
	    else if (ir.keyEquals("RECENT")) {
		recent = ir.getNumber();
		r[i] = null; // remove this response
	    }
	    else if (ir.keyEquals("FLAGS")) {
		availableFlags = new FLAGS(ir);
		r[i] = null; // remove this response
	    }
	    else if (ir.isUnTagged() && ir.isOK()) {
		/* should be one of:
		   	* OK [UNSEEN 12]
		   	* OK [UIDVALIDITY 3857529045]
		   	* OK [PERMANENTFLAGS (\Deleted)]
		   	* OK [UIDNEXT 44]
		*/
		ir.skipSpaces();

		if (ir.readByte() != '[') {	// huh ???
		    ir.reset();
		    continue;
		}

		boolean handled = true;
		String s = ir.readAtom();
		if (s.equalsIgnoreCase("UNSEEN"))
		    first = ir.readNumber();
		else if (s.equalsIgnoreCase("UIDVALIDITY"))
		    uidvalidity = ir.readNumber();
		else if (s.equalsIgnoreCase("PERMANENTFLAGS"))
		    permanentFlags = new FLAGS(ir);
		else if (s.equalsIgnoreCase("UIDNEXT"))
		    uidnext = ir.readNumber();
		else
		    handled = false;	// possibly an ALERT

		if (handled)
		    r[i] = null; // remove this response
		else
		    ir.reset();	// so ALERT can be read
	    }
	}

	/*
	 * The PERMANENTFLAGS response code is optional, and if
	 * not present implies that all flags in the required FLAGS
	 * response can be changed permanently.
	 */
	if (permanentFlags == null) {
	    if (availableFlags != null)
		permanentFlags = new Flags(availableFlags);
	    else
		permanentFlags = new Flags();
	}
    }
}
