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
 * @(#)Namespaces.java	1.5 05/08/29
 *
 * Copyright 2000-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package com.sun.mail.imap.protocol;

import java.util.*;
import com.sun.mail.iap.*;

/**
 * This class and its inner class represent the response to the
 * NAMESPACE command.
 *
 * @see RFC2342
 * @author Bill Shannon
 */

public class Namespaces {

    /**
     * A single namespace entry.
     */
    public static class Namespace {
	/**
	 * Prefix string for the namespace.
	 */
	public String prefix;

	/**
	 * Delimiter between names in this namespace.
	 */
	public char delimiter;

	/**
	 * Parse a namespace element out of the response.
	 */
	public Namespace(Response r) throws ProtocolException {
	    // Namespace_Element = "(" string SP (<"> QUOTED_CHAR <"> / nil)
	    //		*(Namespace_Response_Extension) ")"
	    if (r.readByte() != '(')
		throw new ProtocolException(
					"Missing '(' at start of Namespace");
	    // first, the prefix
	    prefix = BASE64MailboxDecoder.decode(r.readString());
	    r.skipSpaces();
	    // delimiter is a quoted character or NIL
	    if (r.peekByte() == '"') {
		r.readByte();
		delimiter = (char)r.readByte();
		if (delimiter == '\\')
		    delimiter = (char)r.readByte();
		if (r.readByte() != '"')
		    throw new ProtocolException(
				    "Missing '\"' at end of QUOTED_CHAR");
	    } else {
		String s = r.readAtom();
		if (s == null)
		    throw new ProtocolException("Expected NIL, got null");
		if (!s.equalsIgnoreCase("NIL"))
		    throw new ProtocolException("Expected NIL, got " + s);
		delimiter = 0;
	    }
	    // at end of Namespace data?
	    if (r.peekByte() != ')') {
		// otherwise, must be a Namespace_Response_Extension
		//    Namespace_Response_Extension = SP string SP
		//	    "(" string *(SP string) ")"
		r.skipSpaces();
		r.readString();
		r.skipSpaces();
		r.readStringList();
	    }
	    if (r.readByte() != ')')
		throw new ProtocolException("Missing ')' at end of Namespace");
	}
    };

    /**
     * The personal namespaces.
     * May be null.
     */
    public Namespace[] personal;

    /**
     * The namespaces for other users.
     * May be null.
     */
    public Namespace[] otherUsers;

    /**
     * The shared namespace.
     * May be null.
     */
    public Namespace[] shared;

    /**
     * Parse out all the namespaces.
     */
    public Namespaces(Response r) throws ProtocolException {
	personal = getNamespaces(r);
	otherUsers = getNamespaces(r);
	shared = getNamespaces(r);
    }

    /**
     * Parse out one of the three sets of namespaces.
     */
    private Namespace[] getNamespaces(Response r) throws ProtocolException {
	r.skipSpaces();
	//    Namespace = nil / "(" 1*( Namespace_Element) ")"
	if (r.peekByte() == '(') {
	    Vector v = new Vector();
	    r.readByte();
	    do {
		Namespace ns = new Namespace(r);
		v.addElement(ns);
	    } while (r.peekByte() != ')');
	    r.readByte();
	    Namespace[] nsa = new Namespace[v.size()];
	    v.copyInto(nsa);
	    return nsa;
	} else {
	    String s = r.readAtom();
	    if (s == null)
		throw new ProtocolException("Expected NIL, got null");
	    if (!s.equalsIgnoreCase("NIL"))
		throw new ProtocolException("Expected NIL, got " + s);
	    return null;
	}
    }
}
