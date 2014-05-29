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

package org.glassfish.jms.admin.cli;

import com.sun.enterprise.config.serverbeans.*;
import org.glassfish.api.I18n;
import org.glassfish.api.Param;
import org.glassfish.api.ActionReport;
import org.glassfish.api.admin.AdminCommand;
import org.glassfish.api.admin.CommandRunner;
import org.glassfish.api.admin.ParameterMap;
import org.glassfish.api.admin.AdminCommandContext;
import org.glassfish.api.admin.ExecuteOn;
import org.glassfish.config.support.CommandTarget;
import org.glassfish.config.support.TargetType;
import org.glassfish.api.admin.RuntimeType;
import com.sun.enterprise.util.SystemPropertyConstants;
import com.sun.enterprise.util.LocalStringManagerImpl;
import org.jvnet.hk2.annotations.Service;
import org.jvnet.hk2.annotations.Scoped;
import org.jvnet.hk2.annotations.Inject;
import org.jvnet.hk2.component.PerLookup;

import javax.management.ObjectName;
import java.util.Properties;


/**
 * Delete JMS Resource Command
 *
 */
@Service(name="delete-jms-resource")
@Scoped(PerLookup.class)
@I18n("delete.jms.resource")
@ExecuteOn({RuntimeType.DAS})
@TargetType({CommandTarget.DAS,CommandTarget.STANDALONE_INSTANCE,CommandTarget.CLUSTER,CommandTarget.DOMAIN})

public class DeleteJMSResource implements AdminCommand {
    final private static LocalStringManagerImpl localStrings = new LocalStringManagerImpl(DeleteJMSResource.class);

    @Param(optional=true)
    String target = SystemPropertyConstants.DEFAULT_SERVER_INSTANCE_NAME;

    @Param(name="jndi_name", primary=true)
    String jndiName;

    @Inject
    CommandRunner commandRunner;

    @Inject
    Domain domain;
    /**
         * Executes the command with the command parameters passed as Properties
         * where the keys are the paramter names and the values the parameter values
         *
         * @param context information
         */
        public void execute(AdminCommandContext context) {
            final ActionReport report = context.getActionReport();

            if (jndiName == null) {
                report.setMessage(localStrings.getLocalString("delete.jms.resource.noJndiName",
                                "No JNDI name defined for JMS Resource."));
                report.setActionExitCode(ActionReport.ExitCode.FAILURE);
                return;
            }

            ActionReport subReport = report.addSubActionsReport();

            ConnectorResource cresource = null;
            Resource res = domain.getResources().getResourceByName(ConnectorResource.class, jndiName);
            if (res instanceof ConnectorResource)
                    cresource = (ConnectorResource)res;
            /* for (ConnectorResource cr : connResources) {
                 if (cr.getJndiName().equals(jndiName))
                     cresource = cr;
            } */
            if(cresource == null)
            {
              ParameterMap params = new ParameterMap();
              params.set("jndi_name", jndiName);
              params.set("DEFAULT", jndiName);
              params.set("target", target);
	          commandRunner.getCommandInvocation("delete-admin-object", subReport).parameters(params).execute();

              if (ActionReport.ExitCode.FAILURE.equals(subReport.getActionExitCode())){
                    report.setMessage(localStrings.getLocalString("delete.jms.resource.cannotDeleteJMSAdminObject",
                            "Unable to Delete Admin Object."));
                    report.setActionExitCode(ActionReport.ExitCode.FAILURE);
                    return;
              }
            } else
            {
                 // Delete the connector resource and connector connection pool
                String defPoolName = jndiName;
                String poolName = cresource.getPoolName();
                if (poolName != null && poolName.equals(defPoolName))
                {
                     ParameterMap params = new ParameterMap();
                     params.set("DEFAULT", jndiName);
                     params.set("connector_resource_name", jndiName);
                     params.set("target", target);         
		             commandRunner.getCommandInvocation("delete-connector-resource", subReport).parameters(params).execute();

                    if (ActionReport.ExitCode.FAILURE.equals(subReport.getActionExitCode())){
                        report.setMessage(localStrings.getLocalString("delete.jms.resource.cannotDeleteJMSResource",
                            "Unable to Delete Connector Resource."));
                        report.setActionExitCode(ActionReport.ExitCode.FAILURE);
                        return;
                    }


                    params = new ParameterMap();
                    params.set("poolname", jndiName);
                    params.set("DEFAULT", jndiName);
		            commandRunner.getCommandInvocation("delete-connector-connection-pool", subReport).parameters(params).execute();

                    if (ActionReport.ExitCode.FAILURE.equals(subReport.getActionExitCode())){
                        report.setMessage(localStrings.getLocalString("delete.jms.resource.cannotDeleteJMSPool",
                            "Unable to Delete Connector Connection Pool."));
                        report.setActionExitCode(ActionReport.ExitCode.FAILURE);
                        return;
                    }
                    //clear the message set by the delete-connector-connection-pool command this is to prevent the 'connection pool deleted' message from displaying
                    subReport.setMessage("");

                }
                else
                {
                    // There is no connector pool with the default poolName.
                    // However, no need to throw exception as the connector
                    // resource might still be there. Try to delete the
                    // connector-resource without touching the ref. as
                    // ref. might have been deleted while deleting connector-connection-pool
                    // as the ref. is the same.

                     ParameterMap params = new ParameterMap();
                     params.set("DEFAULT", jndiName);
                     params.set("connector_resource_name", jndiName);
                     params.set("target", target);
		             commandRunner.getCommandInvocation("delete-connector-resource", subReport).parameters(params).execute();

                    if (ActionReport.ExitCode.FAILURE.equals(subReport.getActionExitCode())){
                        report.setMessage(localStrings.getLocalString("delete.jms.resource.cannotDeleteJMSResource",
                            "Unable to Delete Connector Resource."));
                        report.setActionExitCode(ActionReport.ExitCode.FAILURE);
                        return;
                    }

                }
    }

        ActionReport.ExitCode ec = ActionReport.ExitCode.SUCCESS;
        report.setActionExitCode(ec);
    }

}
