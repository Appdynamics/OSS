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
 * @(#)QDecoderStream.java	1.6 05/08/29
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package com.sun.mail.util;

import java.io.*;

/**
 * This class implements a Q Decoder as defined in RFC 2047
 * for decoding MIME headers. It subclasses the QPDecoderStream class.
 * 
 * @author John Mani
 */

public class QDecoderStream extends QPDecoderStream {

    /**
     * Create a Q-decoder that decodes the specified input stream.
     * @param in        the input stream
     */
    public QDecoderStream(InputStream in) {
	super(in);
    }

    /**
     * Read the next decoded byte from this input stream. The byte
     * is returned as an <code>int</code> in the range <code>0</code>
     * to <code>255</code>. If no byte is available because the end of
     * the stream has been reached, the value <code>-1</code> is returned.
     * This method blocks until input data is available, the end of the
     * stream is detected, or an exception is thrown.
     *
     * @return     the next byte of data, or <code>-1</code> if the end of the
     *             stream is reached.
     * @exception  IOException  if an I/O error occurs.
     */
    public int read() throws IOException {
	int c = in.read();

	if (c == '_') // Return '_' as ' '
	    return ' ';
	else if (c == '=') {
	    // QP Encoded atom. Get the next two bytes ..
	    ba[0] = (byte)in.read();
	    ba[1] = (byte)in.read();
	    // .. and decode them
	    try {
		return ASCIIUtility.parseInt(ba, 0, 2, 16);
	    } catch (NumberFormatException nex) {
		throw new IOException("Error in QP stream " + nex.getMessage());
	    }
	} else
	    return c;
    }
}
