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
 * @(#)StoreClosedException.java	1.6 05/08/29
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package javax.mail;

/**
 * This exception is thrown when a method is invoked on a Messaging object
 * and the Store that owns that object has died due to some reason.
 * This exception should be treated as a fatal error; in particular any
 * messaging object belonging to that Store must be considered invalid. <p>
 *
 * The connect method may be invoked on the dead Store object to 
 * revive it. <p>
 *
 * The getMessage() method returns more detailed information about the
 * error that caused this exception. <p>
 *
 * @author John Mani
 */

public class StoreClosedException extends MessagingException {
    transient private Store store;

    private static final long serialVersionUID = -3145392336120082655L;

    /**
     * Constructor
     * @param store	The dead Store object
     */
    public StoreClosedException(Store store) {
	this(store, null);
    }

    /**
     * Constructor
     * @param store	The dead Store object
     * @param message	The detailed error message
     */
    public StoreClosedException(Store store, String message) {
	super(message);
	this.store = store;
    }

    /**
     * Returns the dead Store object 
     */
    public Store getStore() {
	return store;
    }
}
