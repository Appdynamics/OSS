/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2009 Sun Microsystems, Inc. All rights reserved.
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

package org.glassfish.jms.admin.cli;

import org.glassfish.api.I18n;
import org.glassfish.api.Param;
import org.glassfish.api.ActionReport;
import org.glassfish.api.admin.AdminCommand;
import org.glassfish.api.admin.AdminCommandContext;
import org.glassfish.internal.api.ServerContext;

import java.util.*;
import java.util.logging.Logger;

import com.sun.enterprise.util.LocalStringManagerImpl;
import com.sun.enterprise.util.SystemPropertyConstants;
import com.sun.enterprise.config.serverbeans.*;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.AttributeList;

import org.jvnet.hk2.annotations.Service;
import org.jvnet.hk2.annotations.Scoped;
import org.jvnet.hk2.annotations.Inject;
import org.jvnet.hk2.component.PerLookup;

import com.sun.logging.LogDomains;

/**
 * Create JMS Destination
 *
 */
@Service(name="create-jmsdest")
@Scoped(PerLookup.class)
@I18n("create.jms.dest")

public class CreateJMSDestination extends JMSDestination implements AdminCommand {


        static Logger logger = LogDomains.getLogger(CreateJMSDestination.class,LogDomains.ADMIN_LOGGER);
        final private static LocalStringManagerImpl localStrings = new LocalStringManagerImpl(CreateJMSDestination.class);

        @Param(name="desttype", shortName="T", optional=false)
        String destType;

        @Param(name="property", optional=true, separator=':')
        Properties props;

        @Param(name="dest_name", primary=true)
        String destName;

        @Param(optional=true)
        String target= SystemPropertyConstants.DEFAULT_SERVER_INSTANCE_NAME;

        @Inject
        com.sun.appserv.connectors.internal.api.ConnectorRuntime connectorRuntime;

        @Inject
        Domain domain;

        @Inject
        Configs configs;

        @Inject
        ServerContext serverContext;


     public void execute(AdminCommandContext context) {

        final ActionReport report = context.getActionReport();

        try{
            validateJMSDestName(destName);
            validateJMSDestType(destType);
        }catch (IllegalArgumentException e){
            report.setMessage(e.getMessage());
            report.setActionExitCode(ActionReport.ExitCode.FAILURE);
            return;
        }

         if (destType.equals(JMS_DEST_TYPE_QUEUE)) {
                if (props == null) {
                    props = new Properties();
                }
                if (!props.containsKey(MAX_ACTIVE_CONSUMERS_PROPERTY) &&
                    !props.containsKey(MAX_ACTIVE_CONSUMERS_ATTRIBUTE) ) {
                    props.put(MAX_ACTIVE_CONSUMERS_ATTRIBUTE, DEFAULT_MAX_ACTIVE_CONSUMERS);
                }
            }
         try{
            createJMSDestination();
         }catch(Exception e){
              report.setMessage(localStrings.getLocalString("create.jms.destination.CannotCreateJMSDest",
                            "Unable to create JMS Destination."));
                    report.setActionExitCode(ActionReport.ExitCode.FAILURE);
                return;
         }
     }


        // create-jmsdest
    private void createJMSDestination() throws Exception {

       MQJMXConnectorInfo mqInfo = getMQJMXConnectorInfo(target, configs,serverContext, domain, connectorRuntime);

        //MBeanServerConnection  mbsc = getMBeanServerConnection(tgtName);
        try {
            MBeanServerConnection mbsc = mqInfo.getMQMBeanServerConnection();
            ObjectName on = new ObjectName(
                DESTINATION_MANAGER_CONFIG_MBEAN_NAME);
            String [] signature = null;
            AttributeList destAttrs = null;
            Object [] params = null;

            if (props != null) {
                destAttrs = convertProp2Attrs(props);
            }

            // setAppserverDefaults(destAttrs, mqInfo);

            if (destType.equalsIgnoreCase(JMS_DEST_TYPE_TOPIC)) {
                destType = DESTINATION_TYPE_TOPIC;
            } else if (destType.equalsIgnoreCase(JMS_DEST_TYPE_QUEUE)) {
                destType = DESTINATION_TYPE_QUEUE;
            }
          if ((destAttrs == null) || (destAttrs.size() == 0)){
                      signature = new String [] {
                     "java.lang.String",
                     "java.lang.String"};
                       params = new Object [] {destType, destName};
                  } else {
                     signature = new String [] {
                     "java.lang.String",
                     "java.lang.String",
                     "javax.management.AttributeList"};
                     params = new Object [] {destType, destName, destAttrs};
                  }

        mbsc.invoke(on, "create", params, signature);
        } catch (Exception e) {
                    logAndHandleException(e, "admin.mbeans.rmb.error_creating_jms_dest");
        } finally {
                    try {
                        if(mqInfo != null) {
                            mqInfo.closeMQMBeanServerConnection();
                        }
                    } catch (Exception e) {
                      handleException(e);
                    }
                }
    }



}
