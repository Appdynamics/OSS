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
 * @(#)LineOutputStream.java	1.7 05/08/29
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package com.sun.mail.util;

import java.io.*;
import javax.mail.MessagingException;

/**
 * This class is to support writing out Strings as a sequence of bytes
 * terminated by a CRLF sequence. The String must contain only US-ASCII
 * characters.<p>
 *
 * The expected use is to write out RFC822 style headers to an output
 * stream. <p>
 *
 * @author John Mani
 */

public class LineOutputStream extends FilterOutputStream {
    private static byte[] newline;

    static {
	newline = new byte[2];
	newline[0] = (byte)'\r';
	newline[1] = (byte)'\n';
    }

    public LineOutputStream(OutputStream out) {
	super(out);
    }

    public void writeln(String s) throws MessagingException {
	try {
	    byte[] bytes = ASCIIUtility.getBytes(s);
	    out.write(bytes);
	    out.write(newline);
	} catch (Exception ex) {
	    throw new MessagingException("IOException", ex);
	}
    }

    public void writeln() throws MessagingException {
	try {
	    out.write(newline);
	} catch (Exception ex) {
	    throw new MessagingException("IOException", ex);
	}
    }
}
