/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package org.glassfish.admin.mbeanserver;

import org.jvnet.hk2.annotations.Service;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.glassfish.api.Startup;
import org.glassfish.api.Async;

import org.glassfish.external.amx.BootAMXMBean;

import org.jvnet.hk2.component.PostConstruct;
import org.jvnet.hk2.component.Habitat;
import org.jvnet.hk2.annotations.Inject;

import java.util.List;
import java.util.ArrayList;

import java.lang.management.ManagementFactory;
import com.sun.enterprise.config.serverbeans.AdminService;
import com.sun.enterprise.config.serverbeans.JmxConnector;
import com.sun.enterprise.config.serverbeans.AmxPref;
import com.sun.enterprise.config.serverbeans.Domain;

import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXServiceURL;


import java.io.IOException;
import java.util.Set;
import javax.management.JMException;
import org.glassfish.api.event.EventListener;
import org.glassfish.api.event.EventTypes;
import org.glassfish.api.event.Events;
import org.glassfish.internal.api.*;

/**
Responsible for creating the {@link BootAMXMBean}, and starting JMXConnectors,
which will initialize (boot) AMX when a connection arrives.
 */
@Service
public final class JMXStartupService implements PostStartup, PostConstruct
{
    private static void debug(final String s)
    {
        System.out.println("### " + s);
    }
    @Inject
    private MBeanServer mMBeanServer;
    @Inject
    private Domain mDomain;
    @Inject
    private AdminService mAdminService;
    @Inject
    private Habitat mHabitat;
    @Inject
    Events mEvents;
    @Inject
    volatile static Habitat habitat;
    
    private volatile BootAMX mBootAMX;
    private volatile JMXConnectorsStarterThread mConnectorsStarterThread;


    public JMXStartupService()
    {
        mMBeanServer = ManagementFactory.getPlatformMBeanServer();
    }

    private final class ShutdownListener implements EventListener
    {
        public void event(EventListener.Event event)
        {
            if (event.is(EventTypes.SERVER_SHUTDOWN))
            {
                shutdown();
            }
        }
    }


    public void postConstruct()
    {
        mBootAMX = BootAMX.create(mHabitat, mMBeanServer);

        final List<JmxConnector> configuredConnectors = mAdminService.getJmxConnector();

        // AmxPref might not exist
        final AmxPref amxPref = mDomain.getAmxPref();
        final boolean autoStart = amxPref == null ? AmxPref.AUTO_START_DEFAULT : Boolean.valueOf(amxPref.getAutoStart());
        
        mConnectorsStarterThread = new JMXConnectorsStarterThread(mMBeanServer, configuredConnectors, mBootAMX, ! autoStart);
        mConnectorsStarterThread.start();
        
        // start AMX *first* (if auto start) so that it's ready
        if ( autoStart )
        {
            new BootAMXThread(mBootAMX).start();
        }
        
        mEvents.register(new ShutdownListener());
    }


    private synchronized void shutdown()
    {
        Util.getLogger().fine("JMXStartupService: shutting down AMX and JMX");

        mConnectorsStarterThread.shutdown();
        mConnectorsStarterThread = null;
        
        mBootAMX.shutdown();
        mBootAMX = null;
        
        // we can't block here waiting, we have to assume that the rest of the AMX modules do the right thing
        Util.getLogger().log( java.util.logging.Level.INFO, "JMXStartupService and JMXConnectors have been shut down." );
    }
    
    private static final class BootAMXThread extends Thread
    {
        private final BootAMX mBooter;
        public BootAMXThread(final BootAMX booter)
        {
            mBooter = booter;
        }
        public void run()
        {
            mBooter.bootAMX();
        }
    }

    /**
    Thread that starts the configured JMXConnectors.
     */
    private static final class JMXConnectorsStarterThread extends Thread
    {
        private final List<JmxConnector> mConfiguredConnectors;
        private final MBeanServer mMBeanServer;
        private final BootAMX mAMXBooterNew;
        private final boolean mNeedBootListeners;

        public JMXConnectorsStarterThread(
            final MBeanServer mbs,
            final List<JmxConnector> configuredConnectors,
            final BootAMX amxBooter,
            final boolean  needBootListeners )
        {
            mMBeanServer = mbs;
            mConfiguredConnectors = configuredConnectors;
            mAMXBooterNew = amxBooter;
            mNeedBootListeners = needBootListeners;
        }


        void shutdown()
        {
            for (final JMXConnectorServer connector : mConnectorServers)
            {
                try
                {
                    final JMXServiceURL address = connector.getAddress();
                    connector.stop();
                    Util.getLogger().info("JMXStartupService: Stopped JMXConnectorServer: " + address);
                }
                catch (final Exception e)
                {
                    e.printStackTrace();
                }
            }
            mConnectorServers.clear();
        }


        private static String toString(final JmxConnector c)
        {
            return "JmxConnector config: { name = " + c.getName() +
                ", Protocol = " + c.getProtocol() +
                ", Address = " + c.getAddress() +
                ", Port = " + c.getPort() +
                ", AcceptAll = " + c.getAcceptAll() +
                ", AuthRealmName = " + c.getAuthRealmName() +
                ", SecurityEnabled = " + c.getSecurityEnabled() +
                "}";
        }


        private JMXConnectorServer startConnector(final JmxConnector connConfig)
            throws IOException
        {
            Util.getLogger().fine("Starting JMXConnector: " + toString(connConfig));

            final String protocol = connConfig.getProtocol();

            final String address = connConfig.getAddress();
            final int port = Integer.parseInt(connConfig.getPort());
            final String authRealmName = connConfig.getAuthRealmName();
            final boolean securityEnabled = Boolean.parseBoolean(connConfig.getSecurityEnabled());
        
            JMXConnectorServer server = null;
            final BootAMXListener listener = mNeedBootListeners ? new BootAMXListener(server, mAMXBooterNew) : null;
            if (protocol.equals("rmi_jrmp"))
            {
                final RMIConnectorStarter starter = new RMIConnectorStarter(mMBeanServer, address, port, protocol, authRealmName, securityEnabled, habitat, listener);
                server = starter.start();
            }
            else if (protocol.equals("jmxmp"))
            {
                final JMXMPConnectorStarter starter = new JMXMPConnectorStarter(mMBeanServer, address, port, authRealmName, securityEnabled, habitat, listener);
                server = starter.start();
            }
            else
            {
                throw new IllegalArgumentException("JMXStartupService.startConnector(): Unknown protocol: " + protocol);
            }

            final JMXServiceURL url = server.getAddress();
            Util.getLogger().info("JMXStartupService: Started JMXConnector, JMXService URL = " + url);

            try
            {
                ObjectName objectName = new ObjectName(JMX_CONNECTOR_SERVER_PREFIX + ",protocol=" + protocol + ",name=" + connConfig.getName());
                objectName = mMBeanServer.registerMBean(server, objectName).getObjectName();
            }
            catch (final Exception e)
            {
                // it's not critical to have it registered as an MBean
                e.printStackTrace();
            }

            // test that it works
            /*
            final JMXConnector conn = JMXConnectorFactory.connect(url);
            final MBeanServerConnection mbsc = conn.getMBeanServerConnection();
            mbsc.getDomains();
             */

            return server;
        }
        private final List<JMXConnectorServer> mConnectorServers = new ArrayList<JMXConnectorServer>();


        public void run()
        {            
            for (final JmxConnector c : mConfiguredConnectors)
            {
                if (!Boolean.parseBoolean(c.getEnabled()))
                {
                    Util.getLogger().info("JMXStartupService: JMXConnector " + c.getName() + " is disabled, skipping.");
                    continue;
                }

                try
                {
                    final JMXConnectorServer server = startConnector(c);
                    mConnectorServers.add(server);
                }
                catch (final Throwable t)
                {
                    Util.getLogger().warning("Cannot start JMX connector: " + toString(c) + ": " + t);
                    //t.printStackTrace();
                }
            }
        }
    }
    
    public static final String JMX_CONNECTOR_SERVER_PREFIX = "jmxremote:type=jmx-connector-server";

    public static final Set<ObjectName> getJMXConnectorServers(final MBeanServer server)
    {
        try
        {
            final ObjectName queryPattern = new ObjectName(JMX_CONNECTOR_SERVER_PREFIX + ",*");
            return server.queryNames(queryPattern, null);
        }
        catch (final Exception e)
        {
            throw new RuntimeException(e);
        }
    }


    /**
    Return the JMXServiceURLs for all connectors we've loaded.
     */
    public static JMXServiceURL[] getJMXServiceURLs(final MBeanServer server)
    {
        final Set<ObjectName> objectNames = getJMXConnectorServers(server);

        final List<JMXServiceURL> urls = new ArrayList<JMXServiceURL>();
        for (final ObjectName objectName : objectNames)
        {
            try
            {
                urls.add((JMXServiceURL) server.getAttribute(objectName, "Address"));
            }
            catch (JMException e)
            {
                e.printStackTrace();
                // ignore
            }
        }

        return urls.toArray(new JMXServiceURL[urls.size()]);
    }

}











