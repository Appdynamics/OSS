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
 * @(#)Header.java	1.5 05/08/29
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package javax.mail;


/**
 * The Header class stores a name/value pair to represent headers.
 *
 * @author John Mani
 */

public class Header {

    /**
     * The name of the header.
     *
     * @since	JavaMail 1.4
     */
    protected String name;

    /**
     * The value of the header.
     *
     * @since	JavaMail 1.4
     */
    protected String value;

    /**
     * Construct a Header object.
     *
     * @param name	name of the header
     * @param value	value of the header
     */
    public Header(String name, String value) {
	this.name = name;
	this.value = value;
    }

    /**
     * Returns the name of this header.
     *
     * @return 		name of the header
     */
    public String getName() {
	return name;
    }

    /**
     * Returns the value of this header.
     *
     * @return 		value of the header
     */
    public String getValue() {
	return value;
    }
}
