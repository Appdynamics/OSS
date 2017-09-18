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
 * @(#)ASCIIUtility.java	1.10 05/08/29
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */
package com.sun.mail.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;

public class ASCIIUtility {

    // Private constructor so that this class is not instantiated
    private ASCIIUtility() { }
	
    /**
     * Convert the bytes within the specified range of the given byte 
     * array into a signed integer in the given radix . The range extends 
     * from <code>start</code> till, but not including <code>end</code>. <p>
     *
     * Based on java.lang.Integer.parseInt()
     */
    public static int parseInt(byte[] b, int start, int end, int radix)
		throws NumberFormatException {
	if (b == null)
	    throw new NumberFormatException("null");
	
	int result = 0;
	boolean negative = false;
	int i = start;
	int limit;
	int multmin;
	int digit;

	if (end > start) {
	    if (b[i] == '-') {
		negative = true;
		limit = Integer.MIN_VALUE;
		i++;
	    } else {
		limit = -Integer.MAX_VALUE;
	    }
	    multmin = limit / radix;
	    if (i < end) {
		digit = Character.digit((char)b[i++], radix);
		if (digit < 0) {
		    throw new NumberFormatException(
			"illegal number: " + toString(b, start, end)
			);
		} else {
		    result = -digit;
		}
	    }
	    while (i < end) {
		// Accumulating negatively avoids surprises near MAX_VALUE
		digit = Character.digit((char)b[i++], radix);
		if (digit < 0) {
		    throw new NumberFormatException("illegal number");
		}
		if (result < multmin) {
		    throw new NumberFormatException("illegal number");
		}
		result *= radix;
		if (result < limit + digit) {
		    throw new NumberFormatException("illegal number");
		}
		result -= digit;
	    }
	} else {
	    throw new NumberFormatException("illegal number");
	}
	if (negative) {
	    if (i > start + 1) {
		return result;
	    } else {	/* Only got "-" */
		throw new NumberFormatException("illegal number");
	    }
	} else {
	    return -result;
	}
    }

    /**
     * Convert the bytes within the specified range of the given byte 
     * array into a signed integer . The range extends from 
     * <code>start</code> till, but not including <code>end</code>. <p>
     */
    public static int parseInt(byte[] b, int start, int end)
		throws NumberFormatException {
	return parseInt(b, start, end, 10);
    }

    /**
     * Convert the bytes within the specified range of the given byte 
     * array into a signed long in the given radix . The range extends 
     * from <code>start</code> till, but not including <code>end</code>. <p>
     *
     * Based on java.lang.Long.parseLong()
     */
    public static long parseLong(byte[] b, int start, int end, int radix)
		throws NumberFormatException {
	if (b == null)
	    throw new NumberFormatException("null");
	
	long result = 0;
	boolean negative = false;
	int i = start;
	long limit;
	long multmin;
	int digit;

	if (end > start) {
	    if (b[i] == '-') {
		negative = true;
		limit = Long.MIN_VALUE;
		i++;
	    } else {
		limit = -Long.MAX_VALUE;
	    }
	    multmin = limit / radix;
	    if (i < end) {
		digit = Character.digit((char)b[i++], radix);
		if (digit < 0) {
		    throw new NumberFormatException(
			"illegal number: " + toString(b, start, end)
			);
		} else {
		    result = -digit;
		}
	    }
	    while (i < end) {
		// Accumulating negatively avoids surprises near MAX_VALUE
		digit = Character.digit((char)b[i++], radix);
		if (digit < 0) {
		    throw new NumberFormatException("illegal number");
		}
		if (result < multmin) {
		    throw new NumberFormatException("illegal number");
		}
		result *= radix;
		if (result < limit + digit) {
		    throw new NumberFormatException("illegal number");
		}
		result -= digit;
	    }
	} else {
	    throw new NumberFormatException("illegal number");
	}
	if (negative) {
	    if (i > start + 1) {
		return result;
	    } else {	/* Only got "-" */
		throw new NumberFormatException("illegal number");
	    }
	} else {
	    return -result;
	}
    }

    /**
     * Convert the bytes within the specified range of the given byte 
     * array into a signed long . The range extends from 
     * <code>start</code> till, but not including <code>end</code>. <p>
     */
    public static long parseLong(byte[] b, int start, int end)
		throws NumberFormatException {
	return parseLong(b, start, end, 10);
    }

    /**
     * Convert the bytes within the specified range of the given byte 
     * array into a String. The range extends from <code>start</code>
     * till, but not including <code>end</code>. <p>
     */
    public static String toString(byte[] b, int start, int end) {
	int size = end - start;
	char[] theChars = new char[size];

	for (int i = 0, j = start; i < size; )
	    theChars[i++] = (char)(b[j++]&0xff);
	
	return new String(theChars);
    }

    public static String toString(ByteArrayInputStream is) {
	int size = is.available();
	char[] theChars = new char[size];
	byte[] bytes    = new byte[size];

	is.read(bytes, 0, size);
	for (int i = 0; i < size;)
	    theChars[i] = (char)(bytes[i++]&0xff);
	
	return new String(theChars);
    }


    public static byte[] getBytes(String s) {
	char [] chars= s.toCharArray();
	int size = chars.length;
	byte[] bytes = new byte[size];
    	
	for (int i = 0; i < size;)
	    bytes[i] = (byte) chars[i++];
	return bytes;
    }

    public static byte[] getBytes(InputStream is) throws IOException {

	int len;
	int size = 1024;
	byte [] buf;


	if (is instanceof ByteArrayInputStream) {
	    size = is.available();
	    buf = new byte[size];
	    len = is.read(buf, 0, size);
	}
	else {
	    ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    buf = new byte[size];
	    while ((len = is.read(buf, 0, size)) != -1)
		bos.write(buf, 0, len);
	    buf = bos.toByteArray();
	}
	return buf;
    }
}
