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
 * @(#)RFC822DATA.java	1.6 05/08/29
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package com.sun.mail.imap.protocol;

import java.io.ByteArrayInputStream;
import com.sun.mail.iap.*; 
import com.sun.mail.util.ASCIIUtility;

/**
 * This class 
 *
 * @version 1.6, 05/08/29
 * @author  John Mani
 */

public class RFC822DATA implements Item {
   
    public static char [] name = {'R','F','C','8','2','2'};
    public int msgno;
    public ByteArray data;

    /**
     * Constructor
     * @param port	portnumber to connect to
     */
    public RFC822DATA(FetchResponse r) throws ParsingException {
	msgno = r.getNumber();
	r.skipSpaces();
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
