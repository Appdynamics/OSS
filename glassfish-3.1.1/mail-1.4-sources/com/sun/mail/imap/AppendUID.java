/*
 * @(#)AppendUID.java	1.1 05/11/17
 *
 * Copyright 2005 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * SUN PROPRIETARY/CONFIDENTIAL.  Use is subject to license terms.
 * 
 */

package com.sun.mail.imap;

import com.sun.mail.iap.*;

/**
 * Information from the APPENDUID response code
 * defined by the UIDPLUS extension -
 * <A HREF="http://www.ietf.org/rfc/rfc2359.txt">RFC 2359</A>.
 *
 * @version 1.1, 05/11/17
 * @author  Bill Shannon
 */

public class AppendUID { 
    public long uidvalidity = -1;
    public long uid = -1;

    public AppendUID(long uidvalidity, long uid) {
	this.uidvalidity = uidvalidity;
	this.uid = uid;
    }
}
