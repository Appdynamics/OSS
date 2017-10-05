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
 * $Header: /cvs/glassfish/jmx-remote/rjmx-impl/src/java/com/sun/enterprise/admin/jmx/remote/server/callers/RemoveNotifLsnrCaller.java,v 1.4 2005/12/25 04:26:39 tcfujii Exp $
 * $Revision: 1.4 $
 * $Date: 2005/12/25 04:26:39 $
*/

package com.sun.enterprise.admin.jmx.remote.server.callers;

import java.util.ArrayList;
import java.util.logging.Logger;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.NotificationListener;
import javax.management.remote.message.MBeanServerRequestMessage;
import javax.management.remote.message.MBeanServerResponseMessage;

import com.sun.enterprise.admin.jmx.remote.server.notification.ServerNotificationManager;
import com.sun.enterprise.admin.jmx.remote.DefaultConfiguration;

/** Invokes the method removeNotificationListener of the MBeanServerConnection.
 * @see MBeanServerRequestMessage#REMOVE_NOTIFICATION_LISTENER
 * @author Kedar Mhaswade
 * @since S1AS8.0
 * @version 1.0
 */

public class RemoveNotifLsnrCaller extends AbstractMethodCaller {
    
    private ServerNotificationManager notifyMgr = null;

    private static final Logger logger = Logger.getLogger(
        DefaultConfiguration.JMXCONNECTOR_LOGGER);/*, 
        DefaultConfiguration.LOGGER_RESOURCE_BUNDLE_NAME );*/

    public RemoveNotifLsnrCaller(MBeanServerConnection mbsc, ServerNotificationManager mgr) {
        super(mbsc);
        METHOD_ID = MBeanServerRequestMessage.REMOVE_NOTIFICATION_LISTENER;
        this.notifyMgr = mgr;
    }
    
    public MBeanServerResponseMessage call(MBeanServerRequestMessage request) {
//        final Object result		= new UnsupportedOperationException("" + METHOD_ID);
        Object result		= null;
        boolean isException = false;

        ObjectName objname  = (ObjectName) request.getParams()[0];
        String cid = (String) request.getParams()[1];
        String[] ids = (String[]) request.getParams()[2];

        NotificationListener proxy = null;
        try {
            for (int i=0; i < ids.length; i++) {
                proxy = (NotificationListener)
                            notifyMgr.removeNotificationListener(objname, ids[i]);
                mbsc.removeNotificationListener(objname, proxy);
            }
        } catch (Exception e) {
            result = e;
            isException = true;
        }
        return ( new MBeanServerResponseMessage(METHOD_ID, result, isException) );
    }
}
