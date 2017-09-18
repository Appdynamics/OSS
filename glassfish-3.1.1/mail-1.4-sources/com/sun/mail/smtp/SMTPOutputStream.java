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
 * @(#)SMTPOutputStream.java	1.10 05/08/29
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package com.sun.mail.smtp;

import java.io.*;
import com.sun.mail.util.CRLFOutputStream;

/**
 * In addition to converting lines into the canonical format,
 * i.e., terminating lines with the CRLF sequence, escapes the "."
 * by adding another "." to any "." that appears in the beginning
 * of a line.  See RFC821 section 4.5.2.
 * 
 * @author Max Spivak
 * @see CRLFOutputStream
 */
public class SMTPOutputStream extends CRLFOutputStream {
    public SMTPOutputStream(OutputStream os) {
	super(os);
    }

    public void write(int b) throws IOException {
	// if that last character was a newline, and the current
	// character is ".", we always write out an extra ".".
	if ((lastb == '\n' || lastb == '\r' || lastb == -1) && b == '.') {
	    out.write('.');
	}
	
	super.write(b);
    }

    /* 
     * This method has been added to improve performance.
     */
    public void write(byte b[], int off, int len) throws IOException {
	int lastc = (lastb == -1) ? '\n' : lastb;
	int start = off;
	
	len += off;
	for (int i = off; i < len; i++) {
	    if ((lastc == '\n' || lastc == '\r') && b[i] == '.') {
		super.write(b, start, i - start);
		out.write('.');
		start = i;
	    }
	    lastc = b[i];
	}
	if ((len - start) > 0)
	    super.write(b, start, len - start);
    }

    /**
     * Override flush method in FilterOutputStream.
     *
     * The MimeMessage writeTo method flushes its buffer at the end,
     * but we don't want to flush data out to the socket until we've
     * also written the terminating "\r\n.\r\n".
     *
     * We buffer nothing so there's nothing to flush.  We depend
     * on the fact that CRLFOutputStream also buffers nothing.
     * SMTPTransport will manually flush the socket before reading
     * the response.
     */
    public void flush() {
	// do nothing
    }
}
