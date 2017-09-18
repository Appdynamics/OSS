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
 * @(#)ProtocolException.java	1.7 05/08/29
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package com.sun.mail.iap;

/**
 * @author John Mani
 */

public class ProtocolException extends Exception {
    protected transient Response response = null;

    private static final long serialVersionUID = -4360500807971797439L;

    /**
     * Constructs a ProtocolException with no detail message.
     */
    public ProtocolException() {
	super();
    }

    /**
     * Constructs a ProtocolException with the specified detail message.
     * @param s		the detail message
     */
    public ProtocolException(String s) {
	super(s);
    }

    /**
     * Constructs a ProtocolException with the specified Response object.
     */
    public ProtocolException(Response r) {
	super(r.toString());
	response = r;
    }

    /**
     * Return the offending Response object.
     */
    public Response getResponse() {
	return response;
    }
}
