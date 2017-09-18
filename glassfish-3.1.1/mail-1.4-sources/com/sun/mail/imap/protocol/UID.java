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
 * @(#)UID.java	1.6 05/08/29
 *
 * Copyright 1998-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package com.sun.mail.imap.protocol;

import com.sun.mail.iap.*; 

/**
 * This class represents the UID data item
 *
 * @version 1.6, 05/08/29
 * @author  John Mani
 */

public class UID implements Item {
    
    public final static char [] name = {'U','I','D'};
    public int msgno;

    public long uid;

    /**
     * Constructor
     */
    public UID(FetchResponse r) throws ParsingException {
	msgno = r.getNumber();
	r.skipSpaces();
	uid = r.readLong();
    }
}
