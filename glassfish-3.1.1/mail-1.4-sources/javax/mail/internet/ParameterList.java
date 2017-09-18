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
 * @(#)ParameterList.java	1.12 05/08/29
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package javax.mail.internet;

import java.util.*;
import java.io.*;

/**
 * This class holds MIME parameters (attribute-value pairs).
 * The <code>mail.mime.encodeparameters</code> and
 * <code>mail.mime.decodeparameters</code> System properties
 * control whether encoded parameters, as specified by 
 * <a href="http://www.ietf.org/rfc/rfc2231.txt">RFC 2231</a>,
 * are supported.  By default, such encoded parameters are not
 * supported. <p>
 *
 * Also, in the current implementation, setting the System property
 * <code>mail.mime.decodeparameters.strict</code> to <code>"true"</code>
 * will cause a <code>ParseException</code> to be thrown for errors
 * detected while decoding encoded parameters.  By default, if any
 * decoding errors occur, the original (undecoded) string is used.
 *
 * @version 1.12, 05/08/29
 * @author  John Mani
 * @author  Bill Shannon
 */

public class ParameterList {

    /**
     * The map of name, value pairs.
     * The value object is either a String, for unencoded
     * values, or a Value object, for encoded values.
     */
    private Map list = new LinkedHashMap();	// keep parameters in order

    private static boolean encodeParameters = false;
    private static boolean decodeParameters = false;
    private static boolean decodeParametersStrict = false;

    static {
	try {
	    String s = System.getProperty("mail.mime.encodeparameters");
	    // default to false
	    encodeParameters = s != null && s.equalsIgnoreCase("true");
	    s = System.getProperty("mail.mime.decodeparameters");
	    // default to false
	    decodeParameters = s != null && s.equalsIgnoreCase("true");
	    s = System.getProperty("mail.mime.decodeparameters.strict");
	    // default to false
	    decodeParametersStrict = s != null && s.equalsIgnoreCase("true");
	} catch (SecurityException sex) {
	    // ignore it
	}
    }

    /**
     * A struct to hold an encoded value.
     * A parsed encoded value is stored as both the
     * decoded value and the original encoded value
     * (so that toString will produce the same result).
     * An encoded value that is set explicitly is stored
     * as the original value and the encoded value, to
     * ensure that get will return the same value that
     * was set.
     */
    private static class Value {
	String value;
	String encodedValue;
    }

    /**
     * Map the LinkedHashMap's keySet iterator to an Enumeration.
     */
    private static class ParamEnum implements Enumeration {
	private Iterator it;

	ParamEnum(Iterator it) {
	    this.it = it;
	}

	public boolean hasMoreElements() {
	    return it.hasNext();
	}

	public Object nextElement() {
	    return it.next();
	}
    }

    /**
     * No-arg Constructor.
     */
    public ParameterList() { 
    }

    /**
     * Constructor that takes a parameter-list string. The String
     * is parsed and the parameters are collected and stored internally.
     * A ParseException is thrown if the parse fails. 
     * Note that an empty parameter-list string is valid and will be 
     * parsed into an empty ParameterList.
     *
     * @param	s	the parameter-list string.
     * @exception	ParseException if the parse fails.
     */
    public ParameterList(String s) throws ParseException {
	HeaderTokenizer h = new HeaderTokenizer(s, HeaderTokenizer.MIME);
	HeaderTokenizer.Token tk;
	int type;
	String name;

	for (;;) {
	    tk = h.next();
	    type = tk.getType();

	    if (type == HeaderTokenizer.Token.EOF) // done
		return;

	    if ((char)type == ';') {
		// expect parameter name
		tk = h.next();
		// tolerate trailing semicolon, even though it violates the spec
		if (tk.getType() == HeaderTokenizer.Token.EOF)
		    return;
		// parameter name must be a MIME Atom
		if (tk.getType() != HeaderTokenizer.Token.ATOM)
		    throw new ParseException("Expected parameter name, " +
					    "got \"" + tk.getValue() + "\"");
		name = tk.getValue().toLowerCase();

		// expect '='
		tk = h.next();
		if ((char)tk.getType() != '=')
		    throw new ParseException("Expected '=', " +
					    "got \"" + tk.getValue() + "\"");

		// expect parameter value
		tk = h.next();
		type = tk.getType();
		// parameter value must be a MIME Atom or Quoted String
		if (type != HeaderTokenizer.Token.ATOM &&
		    type != HeaderTokenizer.Token.QUOTEDSTRING)
		    throw new ParseException("Expected parameter value, " +
					    "got \"" + tk.getValue() + "\"");

		String value = tk.getValue();
		if (decodeParameters && name.endsWith("*")) {
		    name = name.substring(0, name.length() - 1);
		    list.put(name, decodeValue(value));
		} else
		    list.put(name, value);
	    } else
		throw new ParseException("Expected ';', " +
					    "got \"" + tk.getValue() + "\"");
	}
    }

    /**
     * Return the number of parameters in this list.
     * 
     * @return  number of parameters.
     */
    public int size() {
	return list.size();
    }

    /**
     * Returns the value of the specified parameter. Note that 
     * parameter names are case-insensitive.
     *
     * @param name	parameter name.
     * @return		Value of the parameter. Returns 
     *			<code>null</code> if the parameter is not 
     *			present.
     */
    public String get(String name) {
	String value;
	Object v = list.get(name.trim().toLowerCase());
	if (v instanceof Value)
	    value = ((Value)v).value;
	else
	    value = (String)v;
	return value;
    }

    /**
     * Set a parameter. If this parameter already exists, it is
     * replaced by this new value.
     *
     * @param	name 	name of the parameter.
     * @param	value	value of the parameter.
     */
    public void set(String name, String value) {
	list.put(name.trim().toLowerCase(), value);
    }

    /**
     * Set a parameter. If this parameter already exists, it is
     * replaced by this new value.  If the
     * <code>mail.mime.encodeparameters</code> System property
     * is true, and the parameter value is non-ASCII, it will be
     * encoded with the specified charset, as specified by RFC 2231.
     *
     * @param	name 	name of the parameter.
     * @param	value	value of the parameter.
     * @param	charset	charset of the parameter value.
     * @since	JavaMail 1.4
     */
    public void set(String name, String value, String charset) {
	if (encodeParameters) {
	    Value ev = encodeValue(value, charset);
	    // was it actually encoded?
	    if (ev != null)
		list.put(name.trim().toLowerCase(), ev);
	    else
		set(name, value);
	} else
	    set(name, value);
    }

    /**
     * Removes the specified parameter from this ParameterList.
     * This method does nothing if the parameter is not present.
     *
     * @param	name	name of the parameter.
     */
    public void remove(String name) {
	list.remove(name.trim().toLowerCase());
    }

    /**
     * Return an enumeration of the names of all parameters in this
     * list.
     *
     * @return Enumeration of all parameter names in this list.
     */
    public Enumeration getNames() {
	return new ParamEnum(list.keySet().iterator());
    }

    /**
     * Convert this ParameterList into a MIME String. If this is
     * an empty list, an empty string is returned.
     *
     * @return		String
     */
    public String toString() {
	return toString(0);
    }

    /**
     * Convert this ParameterList into a MIME String. If this is
     * an empty list, an empty string is returned.
     *   
     * The 'used' parameter specifies the number of character positions
     * already taken up in the field into which the resulting parameter
     * list is to be inserted. It's used to determine where to fold the
     * resulting parameter list.
     *
     * @param used      number of character positions already used, in
     *                  the field into which the parameter list is to
     *                  be inserted.
     * @return          String
     */  
    public String toString(int used) {
        StringBuffer sb = new StringBuffer();
        Iterator e = list.keySet().iterator();
 
        while (e.hasNext()) {
            String name = (String)e.next();
            String value;
	    Object v = list.get(name);
	    if (v instanceof Value) {
		value = ((Value)v).encodedValue;
		name += '*';
	    } else
		value = (String)v;
            value = quote(value);
            sb.append("; ");
            used += 2;
            int len = name.length() + value.length() + 1;
            if (used + len > 76) { // overflows ...
                sb.append("\r\n\t"); // .. start new continuation line
                used = 8; // account for the starting <tab> char
            }
	    sb.append(name).append('=');
	    used += name.length() + 1;
	    if (used + value.length() > 76) { // still overflows ...
		// have to fold value
		String s = MimeUtility.fold(used, value);
		sb.append(s);
		int lastlf = s.lastIndexOf('\n');
		if (lastlf >= 0)	// always true
		    used += s.length() - lastlf - 1;
		else
		    used += s.length();
	    } else {
		sb.append(value);
		used += value.length();
	    }
        }
 
        return sb.toString();
    }
 
    // Quote a parameter value token if required.
    private String quote(String value) {
	return MimeUtility.quote(value, HeaderTokenizer.MIME);
    }

    private static final char hex[] = {
	'0','1', '2', '3', '4', '5', '6', '7',
	'8','9', 'A', 'B', 'C', 'D', 'E', 'F'
    };

    /**
     * Encode a parameter value, if necessary.
     * If the value is encoded, a Value object is returned.
     * Otherwise, null is returned.
     */
    private Value encodeValue(String value, String charset) {
	if (MimeUtility.checkAscii(value) == MimeUtility.ALL_ASCII)
	    return null;	// no need to encode it

	byte[] b;	// charset encoded bytes from the strong
	try {
	    b = value.getBytes(MimeUtility.javaCharset(charset));
	} catch (UnsupportedEncodingException ex) {
	    return null;
	}
	StringBuffer sb = new StringBuffer(b.length + charset.length() + 2);
	sb.append(charset).append("''");
	for (int i = 0; i < b.length; i++) {
	    char c = (char)(b[i] & 0xff);
	    // do we need to encode this character?
	    if (c <= ' ' || c >= 0x7f || c == '*' || c == '\'' || c == '%' ||
		    HeaderTokenizer.MIME.indexOf(c) >= 0) {
		sb.append('%').append(hex[c>>4]).append(hex[c&0xf]);
	    } else
		sb.append(c);
	}
	Value v = new Value();
	v.value = value;
	v.encodedValue = sb.toString();
	return v;
    }

    /**
     * Decode a parameter value.
     */
    private Value decodeValue(String value) throws ParseException {
	Value v = new Value();
	v.encodedValue = value;
	v.value = value;	// in case we fail to decode it
	try {
	    int i = value.indexOf('\'');
	    if (i <= 0) {
		if (decodeParametersStrict)
		    throw new ParseException(
			"Missing charset in encoded value: " + value);
		return v;	// not encoded correctly?  return as is.
	    }
	    String charset = value.substring(0, i);
	    int li = value.indexOf('\'', i + 1);
	    if (li < 0) {
		if (decodeParametersStrict)
		    throw new ParseException(
			"Missing language in encoded value: " + value);
		return v;	// not encoded correctly?  return as is.
	    }
	    String lang = value.substring(i + 1, li);
	    value = value.substring(li + 1);

	    /*
	     * Decode the ASCII characters in value
	     * into an array of bytes, and then convert
	     * the bytes to a String using the specified
	     * charset.  We'll never need more bytes than
	     * encoded characters, so use that to size the
	     * array.
	     */
	    byte[] b = new byte[value.length()];
	    int bi;
	    for (i = 0, bi = 0; i < value.length(); i++) {
		char c = value.charAt(i);
		if (c == '%') {
		    String hex = value.substring(i + 1, i + 3);
		    c = (char)Integer.parseInt(hex, 16);
		    i += 2;
		}
		b[bi++] = (byte)c;
	    }
	    v.value = new String(b, 0, bi, MimeUtility.javaCharset(charset));
	} catch (NumberFormatException nex) {
	    if (decodeParametersStrict)
		throw new ParseException(nex.toString());
	} catch (UnsupportedEncodingException uex) {
	    if (decodeParametersStrict)
		throw new ParseException(uex.toString());
	} catch (StringIndexOutOfBoundsException ex) {
	    if (decodeParametersStrict)
		throw new ParseException(ex.toString());
	}
	return v;
    }
}
