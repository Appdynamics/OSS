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
 * @(#)BASE64MailboxDecoder.java	1.6 05/08/29
 *
 * Copyright 1996-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package com.sun.mail.imap.protocol;

import java.text.StringCharacterIterator;
import java.text.CharacterIterator;

/**
 * See the BASE64MailboxEncoder for a description of the RFC2060 and how
 * mailbox names should be encoded.  This class will do the correct decoding
 * for mailbox names.
 *
 * @version	1.6, 05/08/29
 * @author	Christopher Cotton
 */

public class BASE64MailboxDecoder {
    
    public static String decode(String original) {
	if (original == null || original.length() == 0)
	    return original;

	boolean changedString = false;
	int copyTo = 0;
	// it will always be less than the original
	char[] chars = new char[original.length()]; 
	StringCharacterIterator iter = new StringCharacterIterator(original);
	
	for(char c = iter.first(); c != CharacterIterator.DONE; 
	    c = iter.next()) {

	    if (c == '&') {
		changedString = true;
		copyTo = base64decode(chars, copyTo, iter);
	    } else {
		chars[copyTo++] = c;
	    }
	}
	
	// now create our string from the char array
	if (changedString) {
	    return new String(chars, 0, copyTo);
	} else {
	    return original;
	}	
    }


    protected static int base64decode(char[] buffer, int offset,
				      CharacterIterator iter) {
	boolean firsttime = true;
	int leftover = -1;
	char testing = 0;

	while(true) {
	    // get the first byte
	    byte orig_0 = (byte) iter.next();
	    if (orig_0 == -1) break; // no more chars
	    if (orig_0 == '-') {
		if (firsttime) {
		    // means we got the string "&-" which is turned into a "&"
		    buffer[offset++] = '&';
		}
		// we are done now
		break;
	    }
	    firsttime = false;
	    
	    // next byte
	    byte orig_1 = (byte) iter.next();	    
	    if (orig_1 == -1 || orig_1 == '-')
		break; // no more chars, invalid base64
	    
	    byte a, b, current;
	    a = pem_convert_array[orig_0 & 0xff];
	    b = pem_convert_array[orig_1 & 0xff];
	    // The first decoded byte
	    current = (byte)(((a << 2) & 0xfc) | ((b >>> 4) & 3));

	    // use the leftover to create a Unicode Character (2 bytes)
	    if (leftover != -1) {
		buffer[offset++] = (char)(leftover << 8 | (current & 0xff));
		leftover = -1;
	    } else {
		leftover = current & 0xff;
	    }
	    
	    byte orig_2 = (byte) iter.next();
	    if (orig_2 == '=') { // End of this BASE64 encoding
		continue;
	    } else if (orig_2 == -1 || orig_2 == '-') {
	    	break; // no more chars
	    }
	    	    
	    // second decoded byte
	    a = b;
	    b = pem_convert_array[orig_2 & 0xff];
	    current = (byte)(((a << 4) & 0xf0) | ((b >>> 2) & 0xf));

	    // use the leftover to create a Unicode Character (2 bytes)
	    if (leftover != -1) {
		buffer[offset++] = (char)(leftover << 8 | (current & 0xff));
		leftover = -1;
	    } else {
		leftover = current & 0xff;
	    }

	    byte orig_3 = (byte) iter.next();
	    if (orig_3 == '=') { // End of this BASE64 encoding
		continue;
	    } else if (orig_3 == -1 || orig_3 == '-') {
	    	break;  // no more chars
	    }
	    
	    // The third decoded byte
	    a = b;
	    b = pem_convert_array[orig_3 & 0xff];
	    current = (byte)(((a << 6) & 0xc0) | (b & 0x3f));
	    
	    // use the leftover to create a Unicode Character (2 bytes)
	    if (leftover != -1) {
	    testing = (char)((int)leftover << 8 | ((int) current & 0xff));
		buffer[offset++] = (char)(leftover << 8 | (current & 0xff));
		leftover = -1;
	    } else {
		leftover = current & 0xff;
	    }	    
	}
	
	return offset;
    }

    /**
     * This character array provides the character to value map
     * based on RFC1521, but with the modification from RFC2060
     * which changes the '/' to a ','.
     */  

    protected final static char pem_array[] = {
	'A','B','C','D','E','F','G','H', // 0
	'I','J','K','L','M','N','O','P', // 1
	'Q','R','S','T','U','V','W','X', // 2
	'Y','Z','a','b','c','d','e','f', // 3
	'g','h','i','j','k','l','m','n', // 4
	'o','p','q','r','s','t','u','v', // 5
	'w','x','y','z','0','1','2','3', // 6
	'4','5','6','7','8','9','+',','  // 7
    };

    protected final static byte pem_convert_array[] = new byte[256];

    static {
	for (int i = 0; i < 255; i++)
	    pem_convert_array[i] = -1;
	for(int i = 0; i < pem_array.length; i++)
	    pem_convert_array[pem_array[i]] = (byte) i;
    }    
}
