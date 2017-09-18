/*
 * @(#)UIDSet.java	1.1 05/11/17
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * SUN PROPRIETARY/CONFIDENTIAL.  Use is subject to license terms.
 * 
 */
package com.sun.mail.imap.protocol;

import java.util.Vector;

/**
 * This class holds the 'start' and 'end' for a range of UIDs.
 * Just like MessageSet except using long instead of int.
 */
public class UIDSet {

    public long start;
    public long end;

    public UIDSet() { }

    public UIDSet(long start, long end) {
	this.start = start;
	this.end = end;
    }

    /**
     * Count the total number of elements in a UIDSet
     **/
    public long size() {
	return end - start + 1;
    }

    /*
     * Convert an array of longs into an array of UIDSets
     */
    public static UIDSet[] createUIDSets(long[] msgs) {
	Vector v = new Vector();
	int i,j;

	for (i=0; i < msgs.length; i++) {
	    UIDSet ms = new UIDSet();
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
	UIDSet[] msgsets = new UIDSet[v.size()];	
	v.copyInto(msgsets);
	return msgsets;
    }

    /**
     * Convert an array of UIDSets into an IMAP sequence range
     */
    public static String toString(UIDSet[] msgsets) {
	if (msgsets == null || msgsets.length == 0) // Empty msgset
	    return null; 

	int i = 0;  // msgset index
	StringBuffer s = new StringBuffer();
	int size = msgsets.length;
	long start, end;

	for (;;) {
	    start = msgsets[i].start;
	    end = msgsets[i].end;

	    if (end > start)
		s.append(start).append(':').append(end);
	    else // end == start means only one element
		s.append(start);
	
	    i++; // Next UIDSet
	    if (i >= size) // No more UIDSets
		break;
	    else
		s.append(',');
	}
	return s.toString();
    }

	
    /*
     * Count the total number of elements in an array of UIDSets
     */
    public static long size(UIDSet[] msgsets) {
	long count = 0;

	if (msgsets == null) // Null msgset
	    return 0; 

	for (int i=0; i < msgsets.length; i++)
	    count += msgsets[i].size();
	
	return count;
    }
}
