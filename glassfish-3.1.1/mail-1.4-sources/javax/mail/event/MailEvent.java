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
 * @(#)MailEvent.java	1.6 05/08/29
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package javax.mail.event;

import java.util.EventObject;

/**
 * Common base class for mail events, defining the dispatch method.
 *
 * @author Bill Shannon
 */

public abstract class MailEvent extends EventObject {
    private static final long serialVersionUID = 1846275636325456631L;

    public MailEvent(Object source) {
        super(source);
    }

    /**
     * This method invokes the appropriate method on a listener for
     * this event. Subclasses provide the implementation.
     */
    public abstract void dispatch(Object listener);
}
