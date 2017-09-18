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
 * @(#)ConnectionEvent.java	1.8 05/08/29
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package javax.mail.event;

import java.util.*;
import javax.mail.*;

/**
 * This class models Connection events.
 *
 * @author John Mani
 */

public class ConnectionEvent extends MailEvent  {

    /** A connection was opened. */
    public static final int OPENED 		= 1;
    /** A connection was disconnected (not currently used). */
    public static final int DISCONNECTED 	= 2;
    /** A connection was closed. */
    public static final int CLOSED 		= 3;

    /**
     * The event type.
     *
     * @serial
     */
    protected int type;

    private static final long serialVersionUID = -1855480171284792957L;

    /**
     * Constructor
     * @param source  The source object
     */
    public ConnectionEvent(Object source, int type) {
	super(source);
	this.type = type;
    }

    /**
     * Return the type of this event
     * @return  type
     */
    public int getType() {
	return type;
    }

    /**
     * Invokes the appropriate ConnectionListener method
     */
    public void dispatch(Object listener) {
	if (type == OPENED)
	    ((ConnectionListener)listener).opened(this);
	else if (type == DISCONNECTED)
	    ((ConnectionListener)listener).disconnected(this);
	else if (type == CLOSED)
	    ((ConnectionListener)listener).closed(this);
    }
}
