/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
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

package com.sun.enterprise.connectors.jms.system;

import com.sun.enterprise.config.serverbeans.*;
//import org.glassfish.api.monitoring.MonitoringItem;
import org.glassfish.internal.api.Globals;
//import org.glassfish.jms.admin.monitor.config.JmsServiceMI;
import org.jvnet.hk2.annotations.Service;
import org.jvnet.hk2.annotations.Inject;
import org.jvnet.hk2.component.PostConstruct;
import org.jvnet.hk2.component.Habitat;
//import org.jvnet.hk2.config.ConfigSupport;
//import org.jvnet.hk2.config.SingleConfigCode;
//import org.jvnet.hk2.config.TransactionFailure;
import com.sun.appserv.connectors.internal.api.ConnectorRuntime;
import com.sun.appserv.connectors.internal.api.ConnectorConstants;
import com.sun.enterprise.connectors.jms.util.JmsRaUtil;
import com.sun.enterprise.util.*;

import com.sun.appserv.connectors.internal.api.ConnectorRuntimeException;
import com.sun.appserv.connectors.internal.api.ConnectorsUtil;
import com.sun.hk2.component.Holder;
import org.glassfish.internal.api.PostStartup;

import java.util.List;
//import com.sun.enterprise.config.serverbeans.MonitoringService;

//import java.beans.PropertyVetoException;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import java.util.List;

@Service
public class JmsProviderLifecycle implements  PostStartup, PostConstruct{
    private static final String JMS_INITIALIZE_ON_DEMAND = "org.glassfish.jms.InitializeOnDemand";
    //Lifecycle properties
    public static final String EMBEDDED="EMBEDDED";
    public static final String LOCAL="LOCAL";
    public static final String REMOTE="REMOTE";
    public static final String JMS_SERVICE = "jms-service";
    //static Logger _logger = LogDomains.getLogger(JmsProviderLifecycle.class, LogDomains.RSR_LOGGER);

    @Inject
    Habitat habitat;

    //@Inject
    //MonitoringService monitoringService;

    public void postConstruct()
    {
       if (eagerStartupRequired())
       {
        try {
                initializeBroker();
               } catch (ConnectorRuntimeException e) {
                   e.printStackTrace();
                   //_logger.log(Level.WARNING, "Failed to start JMS RA");
                   e.printStackTrace();
               }
       }
        configureConfigListener();
       //createMonitoringConfig();

    }
    private void configureConfigListener(){
        //do a lookup of the config listener to get it started
        JMSConfigListener jmsConfigListener=habitat.getComponent(JMSConfigListener.class);
    }
    public void initializeBroker () throws ConnectorRuntimeException
    {
            String module = ConnectorConstants.DEFAULT_JMS_ADAPTER;
            String loc = ConnectorsUtil.getSystemModuleLocation(module);
            ConnectorRuntime connectorRuntime = habitat.getComponent(ConnectorRuntime.class);
            connectorRuntime.createActiveResourceAdapter(loc, module, null);
    }
    private boolean eagerStartupRequired(){
        JmsService jmsService = getJmsService();
        if(jmsService == null) return false;
        String integrationMode =jmsService.getType();
        List <JmsHost> jmsHostList = jmsService.getJmsHost();
        String defaultJmsHostName = jmsService.getDefaultJmsHost();
        JmsHost defaultJmsHost = null;
        for (JmsHost host : jmsHostList){
            if(defaultJmsHostName != null && defaultJmsHostName.equals(host.getName())) {
                    defaultJmsHost = host;
                break;
            }
        }
        if(defaultJmsHost == null && jmsHostList != null && jmsHostList.size() >0)  {
            defaultJmsHost = jmsHostList.get(0);
        }
	    boolean lazyInit = false;
        if (defaultJmsHost != null)
                lazyInit = Boolean.parseBoolean(defaultJmsHost.getLazyInit());


        //we don't manage lifecycle of remote brokers
        if(REMOTE.equals(integrationMode))
                return false;

         //Initialize on demand is currently enabled based on a system property
        String jmsInitializeOnDemand = System.getProperty(JMS_INITIALIZE_ON_DEMAND);
        //if the system property is true, don't do eager startup
        if ("true".equals(jmsInitializeOnDemand))
            return false;

        if (EMBEDDED.equals(integrationMode) && (!lazyInit))
            return true;

        //local broker has eager startup by default
        if(LOCAL.equals(integrationMode))
            return true;


        return false;
    }

        private JmsService getJmsService(){
        //    return habitat.getComponent(JmsService.class);
            Domain domain = Globals.get(Domain.class);
            String serverName = System.getProperty(SystemPropertyConstants.SERVER_NAME);
            Server server = domain.getServerNamed(serverName);
            Config config = server.getConfig();
            return config.getJmsService();

        }
}
