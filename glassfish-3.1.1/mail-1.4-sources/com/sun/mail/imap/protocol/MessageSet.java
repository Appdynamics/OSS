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
 * @(#)MessageSet.java	1.4 05/08/29
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */
package com.sun.mail.imap.protocol;

import java.util.Vector;

/**
 * This class holds the 'start' and 'end' for a range of messages
 */
public class MessageSet {

    public int start;
    public int end;

    public MessageSet() { }

    public MessageSet(int start, int end) {
	this.start = start;
	this.end = end;
    }

    /**
     * Count the total number of elements in a MessageSet
     **/
    public int size() {
	return end - start + 1;
    }

    /*
     * Convert an array of integers into an array of MessageSets
     */
    public static MessageSet[] createMessageSets(int[] msgs) {
	Vector v = new Vector();
	int i,j;

	for (i=0; i < msgs.length; i++) {
	    MessageSet ms = new MessageSet();
	    ms.start = msgs[i];

	    // Look for contiguous elements
	    for (j=i+1; j < msgs.length; j++) {
		if (msgs[j] != msgs[j-1] +1)
		    break;
	    }
	    ms.end = msgs[j-1];
	    v.addElement(ms);
	    i = j-1; // i gets incremented @ top of the loop
	}
	MessageSet[] msgsets = new MessageSet[v.size()];	
	v.copyInto(msgsets);
	return msgsets;
    }

    /**
     * Convert an array of MessageSets into an IMAP sequence range
     */
    public static String toString(MessageSet[] msgsets) {
	if (msgsets == null || msgsets.length == 0) // Empty msgset
	    return null; 

	int i = 0;  // msgset index
	StringBuffer s = new StringBuffer();
	int size = msgsets.length;
	int start, end;

	for (;;) {
	    start = msgsets[i].start;
	    end = msgsets[i].end;

	    if (end > start)
		s.append(start).append(':').append(end);
	    else // end == start means only one element
		s.append(start);
	
	    i++; // Next MessageSet
	    if (i >= size) // No more MessageSets
		break;
	    else
		s.append(',');
	}
	return s.toString();
    }

	
    /*
     * Count the total number of elements in an array of MessageSets
     */
    public static int size(MessageSet[] msgsets) {
	int count = 0;

	if (msgsets == null) // Null msgset
	    return 0; 

	for (int i=0; i < msgsets.length; i++)
	    count += msgsets[i].size();
	
	return count;
    }
}
