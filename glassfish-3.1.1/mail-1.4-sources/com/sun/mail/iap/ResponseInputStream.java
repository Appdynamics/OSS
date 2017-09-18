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
 * @(#)ResponseInputStream.java	1.8 06/02/27
 *
 * Copyright 1998-2005 Sun Microsystems, Inc. All Rights Reserved.
 */


package com.sun.mail.iap;

import java.io.*;
import com.sun.mail.iap.ByteArray;
import com.sun.mail.util.ASCIIUtility;

/**
 *
 * Inputstream that is used to read a Response.
 *
 * @version 1.8, 06/02/27
 * @author  Arun Krishnan
 */

public class ResponseInputStream {

    private static final int minIncrement = 256;
    private static final int maxIncrement = 256 * 1024;
    private byte[] buffer = null;
    private int sz = 0;
    private int idx= 0;
    private BufferedInputStream bin;

    /**
     * Constructor.
     */
    public ResponseInputStream(InputStream in) {
	bin = new BufferedInputStream(in, 2 * 1024);
    }

    /**
     * Read a Response from the InputStream.
     * @return ByteArray that contains the Response
     */
    public ByteArray readResponse() throws IOException {
	buffer = new byte[128];
	idx = 0;
	sz  = 128;
	read0();
	return new ByteArray(buffer, 0, idx);
    }

    /**
     * Private method that does the actual read. 
     * This method moves data into 'buffer'.
     */
    private void read0() throws IOException {
	// XXX - b needs to be an int, to handle bytes with value 0xff
	int b = 0;
	boolean gotCRLF=false;

	// Read a CRLF terminated line from the InputStream
	while (!gotCRLF &&
	       ((b =  bin.read()) != -1)) {
	    switch (b) {
	    case '\n':
		if ((idx > 0) && buffer[idx-1] == '\r')
		    gotCRLF = true;
	    default:
		if (idx >= sz) {
		    if (sz < maxIncrement)
			growBuffer(sz);			// double it
		    else
			growBuffer(maxIncrement);	// grow by maxIncrement
		}
		buffer[idx++] = (byte)b;
	    }
	}

	if (b == -1)
	    throw new IOException(); // connection broken ?

	// Now lets check for literals : {<digits>}CRLF
	// Note: index needs to >= 5 for the above sequence to occur
	if (idx >= 5 && buffer[idx-3] == '}') {
	    int i;
	    // look for left curly
	    for (i = idx-4; i >=0; i--)
		if (buffer[i] == '{')
		    break;

	    if (i < 0) // Nope, not a literal ?
		return;

	    int count = 0;
	    // OK, handle the literal ..
	    try {
		count = ASCIIUtility.parseInt(buffer, i+1, idx-3);
	    } catch (NumberFormatException e) {
		return;
	    }

	    // Now read 'count' bytes. (Note: count could be 0)
	    if (count > 0) {
		int avail = sz - idx; // available space in buffer
		if (count > avail)
		    // need count-avail more bytes
		    growBuffer(minIncrement > count - avail ? 
				   minIncrement : count - avail);
		/*
		 * read() might not return all the bytes in one shot,
		 * so call repeatedly till we are done
		 */
		int actual;
		while (count > 0) {
	            actual = bin.read(buffer, idx, count);
		    count -= actual;
		    idx += actual;
		}
	    }

	    /*
	     * Recursive call to continue processing till CR-LF ..
	     *   .. the additional bytes get added into 'buffer'
	     */
	     read0();
	}
	return; 
    }

    /* Grow 'buffer' by 'incr' bytes */
    private void growBuffer(int incr) {
	byte[] nbuf = new byte[sz + incr];
	if (buffer != null)
	    System.arraycopy(buffer, 0, nbuf, 0, idx);
	buffer = nbuf;
	sz += incr;
    }
}
