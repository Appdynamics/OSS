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
 * @(#)FlagTerm.java	1.12 05/08/29
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package javax.mail.search;

import javax.mail.*;

/**
 * This class implements comparisons for Message Flags.
 *
 * @author Bill Shannon
 * @author John Mani
 */
public final class FlagTerm extends SearchTerm {

    /**
     * Indicates whether to test for the presence or
     * absence of the specified Flag. If <code>true</code>,
     * then test whether all the specified flags are present, else
     * test whether all the specified flags are absent.
     *
     * @serial
     */
    protected boolean set;

    /**
     * Flags object containing the flags to test.
     *
     * @serial
     */
    protected Flags flags;

    private static final long serialVersionUID = -142991500302030647L;

    /**
     * Constructor.
     *
     * @param flags	Flags object containing the flags to check for
     * @param set	the flag setting to check for
     */
    public FlagTerm(Flags flags, boolean set) {
	this.flags = flags;
	this.set = set;
    }

    /**
     * Return the Flags to test.
     */
    public Flags getFlags() {
	return (Flags)flags.clone();
    }

    /**
     * Return true if testing whether the flags are set.
     */
    public boolean getTestSet() {
	return set;
    }

    /**
     * The comparison method.
     *
     * @param msg	The flag comparison is applied to this Message
     * @return		true if the comparson succeeds, otherwise false.
     */
    public boolean match(Message msg) {

	try {
	    Flags f = msg.getFlags();
	    if (set) { // This is easy
		if (f.contains(flags))
		    return true;
		else 
		    return false;
	    }

	    // Return true if ALL flags in the passed in Flags
	    // object are NOT set in this Message.

	    // Got to do this the hard way ...
	    Flags.Flag[] sf = flags.getSystemFlags();

	    // Check each flag in the passed in Flags object
	    for (int i = 0; i < sf.length; i++) {
		if (f.contains(sf[i]))
		    // this flag IS set in this Message, get out.
		    return false;
	    }

	    String[] s = flags.getUserFlags();

	    // Check each flag in the passed in Flags object
	    for (int i = 0; i < s.length; i++) {
		if (f.contains(s[i]))
		    // this flag IS set in this Message, get out.
		    return false;
	    }

	    return true;

	} catch (Exception e) {
	    return false;
	}
    }

    /**
     * Equality comparison.
     */
    public boolean equals(Object obj) {
	if (!(obj instanceof FlagTerm))
	    return false;
	FlagTerm ft = (FlagTerm)obj;
	return ft.set == this.set && ft.flags.equals(this.flags);
    }

    /**
     * Compute a hashCode for this object.
     */
    public int hashCode() {
	return set ? flags.hashCode() : ~flags.hashCode();
    }
}
