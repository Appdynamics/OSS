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
 * @(#)BODY.java	1.7 05/08/29
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package com.sun.mail.imap.protocol;

import java.io.ByteArrayInputStream;
import com.sun.mail.iap.*;
import com.sun.mail.util.ASCIIUtility;

/**
 * The BODY fetch response item.
 *
 * @version 1.7, 05/08/29
 * @author  John Mani
 */

public class BODY implements Item {
    
    public static char [] name = {'B','O','D','Y'};

    public int msgno;
    public ByteArray data;
    public String section;
    public int origin = 0;

    /**
     * Constructor
     */
    public BODY(FetchResponse r) throws ParsingException {
	msgno = r.getNumber();

	r.skipSpaces();

	int b;
	while ((b = r.readByte()) != ']') { // skip section
	    if (b == 0)
		throw new ParsingException(
			"BODY parse error: missing ``]'' at section end");
	}

	
	if (r.readByte() == '<') { // origin
	    origin = r.readNumber();
	    r.skip(1); // skip '>';
	}

	data = r.readByteArray();
    }

    public ByteArray getByteArray() {
	return data;
    }

    public ByteArrayInputStream getByteArrayInputStream() {
	if (data != null)
	    return data.toByteArrayInputStream();
	else
	    return null;
    }
}
