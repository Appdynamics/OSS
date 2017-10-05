/*
 * The contents of this file are subject to the terms 
 * of the Common Development and Distribution License 
 * (the License).  You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the license at 
 * https://glassfish.dev.java.net/public/CDDLv1.0.html or
 * glassfish/bootstrap/legal/CDDLv1.0.txt.
 * See the License for the specific language governing 
 * permissions and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL 
 * Header Notice in each file and include the License file 
 * at glassfish/bootstrap/legal/CDDLv1.0.txt.  
 * If applicable, add the following below the CDDL Header, 
 * with the fields enclosed by brackets [] replaced by
 * you own identifying information: 
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * Copyright 2006 Sun Microsystems, Inc. All rights reserved.
 */

/* CVS information
 * $Header: /cvs/glassfish/jmx-remote/rjmx-impl/src/java/com/sun/enterprise/admin/jmx/remote/server/callers/AbstractMethodCaller.java,v 1.3 2005/12/25 04:26:37 tcfujii Exp $
 * $Revision: 1.3 $
 * $Date: 2005/12/25 04:26:37 $
*/

package com.sun.enterprise.admin.jmx.remote.server.callers;

import javax.management.MBeanServerConnection;
import javax.management.remote.message.MBeanServerRequestMessage;
import javax.management.remote.message.MBeanServerResponseMessage;

/**
 *
 * @author  kedar
 */
public abstract class AbstractMethodCaller implements MBeanServerConnectionMethodCaller {
    
    protected int METHOD_ID;
    protected final MBeanServerConnection mbsc;
    
    protected AbstractMethodCaller(MBeanServerConnection mbsc) {
        this.mbsc = mbsc;
    }
    
    public abstract MBeanServerResponseMessage call(MBeanServerRequestMessage request);
    
    public boolean canCall(MBeanServerRequestMessage request) {
        return ( METHOD_ID == request.getMethodId() );
    }
}