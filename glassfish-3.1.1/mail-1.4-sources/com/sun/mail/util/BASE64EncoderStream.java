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
 * @(#)BASE64EncoderStream.java	1.8 05/08/29
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package com.sun.mail.util;

import java.io.*;

/**
 * This class implements a BASE64 Encoder. It is implemented as
 * a FilterOutputStream, so one can just wrap this class around
 * any output stream and write bytes into this filter. The Encoding
 * is done as the bytes are written out.
 * 
 * @author John Mani
 * @author Bill Shannon
 */

public class BASE64EncoderStream extends FilterOutputStream {
    private byte[] buffer; 	// cache of bytes that are yet to be encoded
    private int bufsize = 0;	// size of the cache
    private int count = 0; 	// number of bytes that have been output
    private int bytesPerLine;	// number of bytes per line
    private int lineLimit;	// number of input bytes to output bytesPerLine
    private boolean noCRLF = false;

    private static byte[] newline = new byte[] { '\r', '\n' };

    /**
     * Create a BASE64 encoder that encodes the specified input stream
     * @param out        the output stream
     * @param bytesPerLine  number of bytes per line. The encoder inserts
     * 			a CRLF sequence after the specified number of bytes,
     *			unless bytesPerLine is Integer.MAX_VALUE, in which
     *			case no CRLF is inserted.  bytesPerLine is rounded
     *			down to a multiple of 4.
     */
    public BASE64EncoderStream(OutputStream out, int bytesPerLine) {
	super(out);
	buffer = new byte[3];
	if (bytesPerLine == Integer.MAX_VALUE || bytesPerLine < 4) {
	    noCRLF = true;
	    bytesPerLine = 76;
	}
	this.bytesPerLine = (bytesPerLine / 4) * 4; // Rounded down to 4n
        lineLimit = bytesPerLine / 4 * 3;
    }

    /**
     * Create a BASE64 encoder that encodes the specified input stream.
     * Inserts the CRLF sequence after outputting 76 bytes.
     * @param out        the output stream
     */
    public BASE64EncoderStream(OutputStream out) {
	this(out, 76);	
    }

    /**
     * Encodes <code>len</code> bytes from the specified
     * <code>byte</code> array starting at offset <code>off</code> to
     * this output stream.
     *
     * @param      b     the data.
     * @param      off   the start offset in the data.
     * @param      len   the number of bytes to write.
     * @exception  IOException  if an I/O error occurs.
     */
    public void write(byte[] b, int off, int len) throws IOException {
	int inx;

	// finish off incomplete coding unit, and incomplete line
	while ((bufsize != 0 || count != 0) && len > 0) {
	    write(b[off++]);
	    len--;
	}

	// do bulk encoding a line at a time.
	// reuse a single output buffer.
	byte[] outbuf;
	if (noCRLF) {
	    outbuf = new byte[bytesPerLine];
	} else {
	    outbuf = new byte[bytesPerLine + 2];
	    outbuf[bytesPerLine] = (byte)'\r';
	    outbuf[bytesPerLine + 1] = (byte)'\n';
	}
	for (inx = 0; inx + lineLimit < len; inx += lineLimit)
	    out.write(encode(b, off + inx, lineLimit, outbuf));

	// handle remaining partial line
	for (; inx < len; inx++)
	    write(b[off + inx]);
    }

    /**
     * Encodes <code>b.length</code> bytes to this output stream.
     * @param      b   the data to be written.
     * @exception  IOException  if an I/O error occurs.
     */
    public void write(byte[] b) throws IOException {
	write(b, 0, b.length);
    }

    /**
     * Encodes the specified <code>byte</code> to this output stream.
     * @param      c   the <code>byte</code>.
     * @exception  IOException  if an I/O error occurs.
     */
    public void write(int c) throws IOException {
	buffer[bufsize++] = (byte)c;
	if (bufsize == 3) { // Encoding unit = 3 bytes
	    encode();
	    bufsize = 0;
	}
    }

    /**
     * Flushes this output stream and forces any buffered output bytes
     * to be encoded out to the stream. 
     * @exception  IOException  if an I/O error occurs.
     */
    public void flush() throws IOException {
	if (bufsize > 0) { // If there's unencoded characters in the buffer ..
	    encode();      // .. encode them
	    bufsize = 0;
	}
	out.flush();
    }

    /**
     * Forces any buffered output bytes to be encoded out to the stream
     * and closes this output stream
     */
    public void close() throws IOException {
	flush();
	out.close();
    }

    /** This array maps the characters to their 6 bit values */
    private final static char pem_array[] = {
	'A','B','C','D','E','F','G','H', // 0
	'I','J','K','L','M','N','O','P', // 1
	'Q','R','S','T','U','V','W','X', // 2
	'Y','Z','a','b','c','d','e','f', // 3
	'g','h','i','j','k','l','m','n', // 4
	'o','p','q','r','s','t','u','v', // 5
	'w','x','y','z','0','1','2','3', // 6
	'4','5','6','7','8','9','+','/'  // 7
    };

    private void encode() throws IOException {
	// If writing out this encoded unit will cause overflow,
	// start a new line.
	if (count + 4 > bytesPerLine) {
	    if (!noCRLF)
		out.write(newline);
	    count = 0;
	}
	out.write(encode(buffer, 0, bufsize, null));
	// increment count
	count += 4;
    }

    /**
     * Base64 encode a byte array.  No line breaks are inserted.
     * This method is suitable for short strings, such as those
     * in the IMAP AUTHENTICATE protocol, but not to encode the
     * entire content of a MIME part.
     */
    public static byte[] encode(byte[] inbuf) {
	if (inbuf.length == 0)
	    return inbuf;
	return encode(inbuf, 0, inbuf.length, null);
    }

    /**
     * Internal use only version of encode.  Allow specifying which
     * part of the input buffer to encode.  If outbuf is non-null,
     * it's used as is.  Otherwise, a new output buffer is allocated.
     */
    private static byte[] encode(byte[] inbuf, int off, int size,
				byte[] outbuf) {
	if (outbuf == null)
	    outbuf = new byte[((size + 2) / 3) * 4];
	int inpos, outpos;
	int val;
	for (inpos = off, outpos = 0; size >= 3; size -= 3, outpos += 4) {
	    val = inbuf[inpos++] & 0xff;
	    val <<= 8;
	    val |= inbuf[inpos++] & 0xff;
	    val <<= 8;
	    val |= inbuf[inpos++] & 0xff;
	    outbuf[outpos+3] = (byte)pem_array[val & 0x3f];
	    val >>= 6;
	    outbuf[outpos+2] = (byte)pem_array[val & 0x3f];
	    val >>= 6;
	    outbuf[outpos+1] = (byte)pem_array[val & 0x3f];
	    val >>= 6;
	    outbuf[outpos+0] = (byte)pem_array[val & 0x3f];
	}
	// done with groups of three, finish up any odd bytes left
	if (size == 1) {
	    val = inbuf[inpos++] & 0xff;
	    val <<= 4;
	    outbuf[outpos+3] = (byte)'=';	// pad character;
	    outbuf[outpos+2] = (byte)'=';	// pad character;
	    outbuf[outpos+1] = (byte)pem_array[val & 0x3f];
	    val >>= 6;
	    outbuf[outpos+0] = (byte)pem_array[val & 0x3f];
	} else if (size == 2) {
	    val = inbuf[inpos++] & 0xff;
	    val <<= 8;
	    val |= inbuf[inpos++] & 0xff;
	    val <<= 2;
	    outbuf[outpos+3] = (byte)'=';	// pad character;
	    outbuf[outpos+2] = (byte)pem_array[val & 0x3f];
	    val >>= 6;
	    outbuf[outpos+1] = (byte)pem_array[val & 0x3f];
	    val >>= 6;
	    outbuf[outpos+0] = (byte)pem_array[val & 0x3f];
	}
	return outbuf;
    }

    /*** begin TEST program
    public static void main(String argv[]) throws Exception {
	FileInputStream infile = new FileInputStream(argv[0]);
	BASE64EncoderStream encoder = new BASE64EncoderStream(System.out);
	int c;

	while ((c = infile.read()) != -1)
	    encoder.write(c);
	encoder.close();
    }
    *** end TEST program **/
}
