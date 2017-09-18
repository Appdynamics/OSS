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
 * @(#)ParsingException.java	1.5 05/08/29
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package com.sun.mail.iap;

/**
 * @author John Mani
 */

public class ParsingException extends ProtocolException {

    private static final long serialVersionUID = 7756119840142724839L;

    /**
     * Constructs an ParsingException with no detail message.
     */
    public ParsingException() {
	super();
    }

    /**
     * Constructs an ParsingException with the specified detail message.
     * @param s		the detail message
     */
    public ParsingException(String s) {
	super(s);
    }

    /**
     * Constructs an ParsingException with the specified Response.
     * @param r		the Response
     */
    public ParsingException(Response r) {
	super(r);
    }
}
