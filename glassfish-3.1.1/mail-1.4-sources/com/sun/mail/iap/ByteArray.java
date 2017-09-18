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
 * @(#)ByteArray.java	1.5 05/08/29
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package com.sun.mail.iap;

import java.io.ByteArrayInputStream;

/**
 * A simple wrapper around a byte array, with a start position and
 * count of bytes.
 *
 * @version 1.5, 05/08/29
 * @author  John Mani
 */

public class ByteArray {
    private byte[] bytes; // the byte array
    private int start;	  // start position
    private int count;	  // count of bytes

    /**
     * Constructor
     */
    public ByteArray(byte[] b, int start, int count) {
	bytes = b;
	this.start = start;
	this.count = count;
    }

    /**
     * Returns the internal byte array. Note that this is a live
     * reference to the actual data, not a copy.
     */
    public byte[] getBytes() {
	return bytes;
    }

    /**
     * Returns a new byte array that is a copy of the data.
     */
    public byte[] getNewBytes() {
	byte[] b = new byte[count];
	System.arraycopy(bytes, start, b, 0, count);
	return b;
    }

    /**
     * Returns the start position
     */
    public int getStart() {
	return start;
    }

    /**
     * Returns the count of bytes
     */
    public int getCount() {
	return count;
    }

    /**
     * Returns a ByteArrayInputStream.
     */
    public ByteArrayInputStream toByteArrayInputStream() {
	return new ByteArrayInputStream(bytes, start, count);
    }
}
