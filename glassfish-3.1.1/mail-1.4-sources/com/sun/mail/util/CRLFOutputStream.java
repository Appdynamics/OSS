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
 * @(#)CRLFOutputStream.java	1.11 05/08/29
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package com.sun.mail.util;

import java.io.*;


/**
 * Convert lines into the canonical format, that is, terminate lines with the
 * CRLF sequence.
 * 
 * @author John Mani
 */
public class CRLFOutputStream extends FilterOutputStream {
    protected int lastb = -1;
    protected static byte[] newline;
    static {
	newline = new byte[2];
	newline[0] = (byte)'\r';
	newline[1] = (byte)'\n';
    }

    public CRLFOutputStream(OutputStream os) {
	super(os);
    }

    public void write(int b) throws IOException {
	if (b == '\r') {
	    out.write(newline);
	} else if (b == '\n') {
	    if (lastb != '\r')
		out.write(newline);
	} else {
	    out.write(b);
	}
	lastb = b;
    }

    public void write(byte b[]) throws IOException {
	write(b, 0, b.length);
    }

    public void write(byte b[], int off, int len) throws IOException {
	int start = off;
	
	len += off;
	for (int i = start; i < len ; i++) {
	    if (b[i] == '\r') {
		out.write(b, start, i - start);
		out.write(newline);
		start = i + 1;
	    } else if (b[i] == '\n') {
		if (lastb != '\r') {
		    out.write(b, start, i - start);
		    out.write(newline);
		}
		start = i + 1;
	    }
	    lastb = b[i];
	}
	if ((len - start) > 0)
	    out.write(b, start, len - start);
    }

    /*
     * Just write out a new line, something similar to out.println()
     */
    public void writeln() throws IOException {
        out.write(newline);
    }
}
