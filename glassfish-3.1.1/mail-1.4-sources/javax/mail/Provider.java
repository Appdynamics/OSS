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
 * @(#)Provider.java	1.10 05/08/29
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package javax.mail;

/**
 * The Provider is a class that describes a protocol 
 * implementation.  The values typically come from the
 * javamail.providers and javamail.default.providers
 * resource files.  An application may also create and
 * register a Provider object to dynamically add support
 * for a new provider.
 *
 * @version 1.10, 05/08/29
 * @author Max Spivak
 * @author Bill Shannon
 */
public class Provider {

    /**
     * This inner class defines the Provider type.
     * Currently, STORE and TRANSPORT are the only two provider types 
     * supported.
     */

    public static class Type {
	public static final Type STORE     = new Type("STORE");
	public static final Type TRANSPORT = new Type("TRANSPORT");

	private String type;

	private Type(String type) {
	    this.type = type;
	}

	public String toString() {
	    return type;
	}
    }

    private Type type;
    private String protocol, className, vendor, version;

    /**
     * Create a new provider of the specified type for the specified
     * protocol.  The specified class implements the provider.
     *
     * @param type      Type.STORE or Type.TRANSPORT
     * @param protocol  valid protocol for the type
     * @param classname class name that implements this protocol
     * @param vendor    optional string identifying the vendor (may be null)
     * @param version   optional implementation version string (may be null)
     * @since JavaMail 1.4
     */
    public Provider(Type type, String protocol, String classname, 
	     String vendor, String version) {
	this.type = type;
	this.protocol = protocol;
	this.className = classname;
	this.vendor = vendor;
	this.version = version;
    }

    /** Returns the type of this Provider */
    public Type getType() {
	return type;
    }

    /** Returns the protocol supported by this Provider */
    public String getProtocol() {
	return protocol;
    }

    /** Returns name of the class that implements the protocol */
    public String getClassName() {
	return className;
    }

    /** Returns name of vendor associated with this implementation or null */
    public String getVendor() {
	return vendor;
    }

    /** Returns version of this implementation or null if no version */
    public String getVersion() {
	return version;
    }

    /** Overrides Object.toString() */
    public String toString() {
	String s = "javax.mail.Provider[" + type + "," +
		    protocol + "," + className;

	if (vendor != null)
	    s += "," + vendor;

	if (version != null)
	    s += "," + version;

	s += "]";
	return s;
    }
}
