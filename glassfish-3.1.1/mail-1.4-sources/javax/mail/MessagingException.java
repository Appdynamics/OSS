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
 * @(#)MessagingException.java	1.15 06/03/09
 *
 * Copyright 1997-2006 Sun Microsystems, Inc. All Rights Reserved.
 */

package javax.mail;

import java.lang.*;

/**
 * The base class for all exceptions thrown by the Messaging classes
 *
 * @author John Mani
 * @author Bill Shannon
 */

public class MessagingException extends Exception {

    /**
     * The next exception in the chain.
     *
     * @serial
     */
    private Exception next;

    private static final long serialVersionUID = -7569192289819959253L;

    /**
     * Constructs a MessagingException with no detail message.
     */
    public MessagingException() {
	super();
	initCause(null);	// prevent anyone else from setting it
    }

    /**
     * Constructs a MessagingException with the specified detail message.
     *
     * @param s		the detail message
     */
    public MessagingException(String s) {
	super(s);
	initCause(null);	// prevent anyone else from setting it
    }

    /**
     * Constructs a MessagingException with the specified 
     * Exception and detail message. The specified exception is chained
     * to this exception.
     *
     * @param s		the detail message
     * @param e		the embedded exception
     * @see	#getNextException
     * @see	#setNextException
     * @see	#getCause
     */
    public MessagingException(String s, Exception e) {
	super(s);
	next = e;
	initCause(null);	// prevent anyone else from setting it
    }

    /**
     * Get the next exception chained to this one. If the
     * next exception is a MessagingException, the chain
     * may extend further.
     *
     * @return	next Exception, null if none.
     */
    public synchronized Exception getNextException() {
	return next;
    }

    /**
     * Overrides the <code>getCause</code> method of <code>Throwable</code>
     * to return the next exception in the chain of nested exceptions.
     *
     * @return	next Exception, null if none.
     */
    public synchronized Throwable getCause() {
	return next;
    }

    /**
     * Add an exception to the end of the chain. If the end
     * is <strong>not</strong> a MessagingException, this 
     * exception cannot be added to the end.
     *
     * @param	ex	the new end of the Exception chain
     * @return		<code>true</code> if this Exception
     *			was added, <code>false</code> otherwise.
     */
    public synchronized boolean setNextException(Exception ex) {
	Exception theEnd = this;
	while (theEnd instanceof MessagingException &&
	       ((MessagingException)theEnd).next != null) {
	    theEnd = ((MessagingException)theEnd).next;
	}
	// If the end is a MessagingException, we can add this 
	// exception to the chain.
	if (theEnd instanceof MessagingException) {
	    ((MessagingException)theEnd).next = ex;
	    return true;
	} else
	    return false;
    }

    /**
     * Override toString method to provide information on
     * nested exceptions.
     */
    public synchronized String toString() {
	String s = super.toString();
	Exception n = next;
	if (n == null)
	    return s;
	StringBuffer sb = new StringBuffer(s == null ? "" : s);
	while (n != null) {
	    sb.append(";\n  nested exception is:\n\t");
	    sb.append(n.toString());
	    if (n instanceof MessagingException) {
		MessagingException mex = (MessagingException)n;
		n = mex.next;
	    } else {
		n = null;
	    }
	}
	return sb.toString();
    }
}
