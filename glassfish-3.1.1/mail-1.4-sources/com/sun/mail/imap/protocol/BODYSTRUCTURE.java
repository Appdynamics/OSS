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
 * @(#)BODYSTRUCTURE.java	1.17 05/08/29
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package com.sun.mail.imap.protocol;

import java.util.Vector;
import javax.mail.internet.ParameterList;
import com.sun.mail.iap.*; 

/**
 * A BODYSTRUCTURE response.
 *
 * @version 1.17, 05/08/29
 * @author  John Mani
 */

public class BODYSTRUCTURE implements Item {
    
    public static char [] name = {'B','O','D','Y','S','T','R','U','C','T','U','R','E'};
    public int msgno;

    public String type;		// Type
    public String subtype;	// Subtype
    public String encoding;	// Encoding
    public int lines = -1;	// Size in lines
    public int size = -1;	// Size in bytes
    public String disposition;	// Disposition
    public String id;		// Content-ID
    public String description;	// Content-Description
    public String md5;		// MD-5 checksum
    public String attachment;	// Attachment name
    public ParameterList cParams; // Body parameters
    public ParameterList dParams; // Disposition parameters
    public String[] language;	// Language
    public BODYSTRUCTURE[] bodies; // array of BODYSTRUCTURE objects
				   //  for multipart & message/rfc822
    public ENVELOPE envelope;	// for message/rfc822

    private static int SINGLE	= 1;
    private static int MULTI	= 2;
    private static int NESTED	= 3;
    private int processedType;	// MULTI | SINGLE | NESTED

    public BODYSTRUCTURE(FetchResponse r) throws ParsingException {
	msgno = r.getNumber();

	r.skipSpaces();

	if (r.readByte() != '(')
	    throw new ParsingException(
		"BODYSTRUCTURE parse error: missing ``('' at start");

	if (r.peekByte() == '(') { // multipart
	    type = "multipart";
	    processedType = MULTI;
	    Vector v = new Vector(1);
	    int i = 1;
	    do {
		v.addElement(new BODYSTRUCTURE(r));
		/*
		 * Even though the IMAP spec says there can't be any spaces
		 * between parts, some servers erroneously put a space in
		 * here.  In the spirit of "be liberal in what you accept",
		 * we skip it.
		 */
		r.skipSpaces();
	    } while (r.peekByte() == '(');

	    // setup bodies.
	    bodies = new BODYSTRUCTURE[v.size()];
	    v.copyInto(bodies);

	    subtype = r.readString(); // subtype

	    if (r.readByte() == ')') // done
		return;

	    // Else, we have extension data

	    // Body parameters
	    cParams = parseParameters(r);
	    if (r.readByte() == ')') // done
		return;
	    
	    // Disposition
	    byte b = r.readByte();
	    if (b == '(') {
		disposition = r.readString();
		dParams = parseParameters(r);
		if (r.readByte() != ')') // eat the end ')'
		    throw new ParsingException(
			"BODYSTRUCTURE parse error: " +
			"missing ``)'' at end of disposition in multipart");
	    } else if (b == 'N' || b == 'n') {
		r.skip(2); // skip 'NIL'
	    } else {
		throw new ParsingException(
		    "BODYSTRUCTURE parse error: " +
		    type + "/" + subtype + ": " +
		    "bad multipart disposition, b " + b);
	    }

	    // RFC3501 allows no body-fld-lang after body-fld-disp,
	    // even though RFC2060 required it
	    if ((b = r.readByte()) == ')')
		return; // done

	    if (b != ' ')
		throw new ParsingException(
		    "BODYSTRUCTURE parse error: " +
		    "missing space after disposition");

	    // Language
	    if (r.peekByte() == '(') // a list follows
		language = r.readStringList();
	    else {
		String l = r.readString();
		if (l != null) {
		    String[] la = { l };
		    language = la;
		}
	    }

	    // RFC3501 defines an optional "body location" next,
	    // but for now we ignore it along with other extensions.

	    // Throw away any further extension data
	    while (r.readByte() == ' ')
		parseBodyExtension(r);
	}
	else { // Single part
	    type = r.readString();
	    processedType = SINGLE;
	    subtype = r.readString();

	    // SIMS 4.0 returns NIL for a Content-Type of "binary", fix it here
	    if (type == null) {
		type = "application";
		subtype = "octet-stream";
	    }
	    cParams = parseParameters(r);
	    id = r.readString();
	    description = r.readString();
	    encoding = r.readString();
	    size = r.readNumber();
	    if (size < 0)
		throw new ParsingException(
			    "BODYSTRUCTURE parse error: bad ``size'' element");

	    // "text/*" & "message/rfc822" types have additional data ..
	    if (type.equalsIgnoreCase("text")) {
		lines = r.readNumber();
		if (lines < 0)
		    throw new ParsingException(
			    "BODYSTRUCTURE parse error: bad ``lines'' element");
	    } else if (type.equalsIgnoreCase("message") &&
		     subtype.equalsIgnoreCase("rfc822")) {
		// Nested message
		processedType = NESTED;
		envelope = new ENVELOPE(r);
		BODYSTRUCTURE[] bs = { new BODYSTRUCTURE(r) };
		bodies = bs;
		lines = r.readNumber();
		if (lines < 0)
		    throw new ParsingException(
			    "BODYSTRUCTURE parse error: bad ``lines'' element");
	    } else {
		// Detect common error of including lines element on other types
		r.skipSpaces();
		byte bn = r.peekByte();
		if (Character.isDigit((char)bn)) // number
		    throw new ParsingException(
			    "BODYSTRUCTURE parse error: server erroneously " +
				"included ``lines'' element with type " +
				type + "/" + subtype);
	    }

	    if (r.peekByte() == ')') {
		r.readByte();
		return; // done
	    }

	    // Optional extension data

	    // MD5
	    md5 = r.readString();
	    if (r.readByte() == ')')
		return; // done
	    
	    // Disposition
	    byte b = r.readByte();
	    if (b == '(') {
		disposition = r.readString();
		dParams = parseParameters(r);
		if (r.readByte() != ')') // eat the end ')'
		    throw new ParsingException(
			"BODYSTRUCTURE parse error: " +
			"missing ``)'' at end of disposition");
	    } else if (b == 'N' || b == 'n') {
		r.skip(2); // skip 'NIL'
	    } else {
		throw new ParsingException(
		    "BODYSTRUCTURE parse error: " +
		    type + "/" + subtype + ": " +
		    "bad single part disposition, b " + b);
	    }

	    if (r.readByte() == ')')
		return; // done
	    
	    // Language
	    if (r.peekByte() == '(') // a list follows
		language = r.readStringList();
	    else { // protocol is unnessarily complex here
		String l = r.readString();
		if (l != null) {
		    String[] la = { l };
		    language = la;
		}
	    }

	    // RFC3501 defines an optional "body location" next,
	    // but for now we ignore it along with other extensions.

	    // Throw away any further extension data
	    while (r.readByte() == ' ')
		parseBodyExtension(r);
	}
    }

    public boolean isMulti() {
	return processedType == MULTI;
    }

    public boolean isSingle() {
	return processedType == SINGLE;
    }

    public boolean isNested() {
	return processedType == NESTED;
    }

    private ParameterList parseParameters(Response r)
			throws ParsingException {
	r.skipSpaces();

	ParameterList list = null;
	byte b = r.readByte();
	if (b == '(') {
	    list = new ParameterList();
	    do {
		String name = r.readString();
		String value = r.readString();
		list.set(name, value);
	    } while (r.readByte() != ')');
	} else if (b == 'N' || b == 'n')
	    r.skip(2);
	else
	    throw new ParsingException("Parameter list parse error");
	
	return list;
    }

    private void parseBodyExtension(Response r) throws ParsingException {
	r.skipSpaces();

	byte b = r.peekByte();
	if (b == '(') {
	    r.skip(1); // skip '('
	    do {
		parseBodyExtension(r);
	    } while (r.readByte() != ')');
	} else if (Character.isDigit((char)b)) // number
	    r.readNumber();
	else // nstring
	    r.readString();
    }
}
