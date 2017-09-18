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
 * @(#)BadCommandException.java	1.5 05/08/29
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package com.sun.mail.iap;

/**
 * @author John Mani
 */

public class BadCommandException extends ProtocolException {

    private static final long serialVersionUID = 5769722539397237515L;

    /**
     * Constructs an BadCommandException with no detail message.
     */
    public BadCommandException() {
	super();
    }

    /**
     * Constructs an BadCommandException with the specified detail message.
     * @param s		the detail message
     */
    public BadCommandException(String s) {
	super(s);
    }

    /**
     * Constructs an BadCommandException with the specified Response.
     * @param r		the Response
     */
    public BadCommandException(Response r) {
	super(r);
    }
}
