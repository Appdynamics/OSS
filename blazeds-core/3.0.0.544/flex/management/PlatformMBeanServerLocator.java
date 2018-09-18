/*************************************************************************
 * 
 * ADOBE CONFIDENTIAL
 * __________________
 * 
 *  [2002] - [2007] Adobe Systems Incorporated 
 *  All Rights Reserved.
 * 
 * NOTICE:  All information contained herein is, and remains
 * the property of Adobe Systems Incorporated and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Adobe Systems Incorporated
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Adobe Systems Incorporated.
 */
package flex.management;

import java.lang.management.ManagementFactory;
import javax.management.MBeanServer;

import flex.messaging.log.Log;
import flex.messaging.log.LogCategories;

/**
 * The platform implementation of an MBeanServerLocator for Java 1.5+.
 * This locator exposes the platform MBeanServer.
 * 
 * @author shodgson
 */
public class PlatformMBeanServerLocator implements MBeanServerLocator
{   
    private MBeanServer server;
    
    /*
     * (non-Javadoc)
     * @see flex.management.MBeanServerLocator#getMBeanServer()
     */
    public synchronized MBeanServer getMBeanServer()
    {
        if (server == null)
        {
            server = ManagementFactory.getPlatformMBeanServer();
            if (Log.isDebug())
                Log.getLogger(LogCategories.MANAGEMENT_MBEANSERVER).debug("Using MBeanServer: " + server);
        }
        return server;
    }
    
}