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
 * @(#)AddressException.java	1.8 05/08/29
 *
 * Copyright 1996-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package javax.mail.internet;

/**
 * The exception thrown when a wrongly formatted address is encountered.
 *
 * @author Bill Shannon
 * @author Max Spivak
 */

public class AddressException extends ParseException {
    /**
     * The string being parsed.
     *
     * @serial
     */
    protected String ref = null;

    /**
     * The index in the string where the error occurred, or -1 if not known.
     *
     * @serial
     */
    protected int pos = -1;

    private static final long serialVersionUID = 9134583443539323120L;

    /**
     * Constructs an AddressException with no detail message.
     */
    public AddressException() {
	super();
    }

    /**
     * Constructs an AddressException with the specified detail message.
     * @param s		the detail message
     */
    public AddressException(String s) {
	super(s);
    }

    /**
     * Constructs an AddressException with the specified detail message
     * and reference info.
     *
     * @param s		the detail message
     */

    public AddressException(String s, String ref) {
	super(s);
	this.ref = ref;
    }
    /**
     * Constructs an AddressException with the specified detail message
     * and reference info.
     *
     * @param s		the detail message
     */
    public AddressException(String s, String ref, int pos) {
	super(s);
	this.ref = ref;
	this.pos = pos;
    }

    /**
     * Get the string that was being parsed when the error was detected
     * (null if not relevant).
     */
    public String getRef() {
	return ref;
    }

    /**
     * Get the position with the reference string where the error was
     * detected (-1 if not relevant).
     */
    public int getPos() {
	return pos;
    }

    public String toString() {
	String s = super.toString();
	if (ref == null)
	    return s;
	s += " in string ``" + ref + "''";
	if (pos < 0)
	    return s;
	return s + " at position " + pos;
    }
}
