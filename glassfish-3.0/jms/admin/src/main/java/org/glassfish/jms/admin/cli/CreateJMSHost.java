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





package org.glassfish.jms.admin.cli;

import org.glassfish.api.I18n;
import org.glassfish.api.Param;
import org.glassfish.api.ActionReport;
import org.glassfish.api.admin.AdminCommand;
import org.glassfish.api.admin.AdminCommandContext;
import com.sun.enterprise.util.LocalStringManagerImpl;
import com.sun.enterprise.util.SystemPropertyConstants;
import com.sun.enterprise.config.serverbeans.*;

import java.util.Properties;
import java.beans.PropertyVetoException;
import org.jvnet.hk2.annotations.Service;
import org.jvnet.hk2.annotations.Scoped;
import org.jvnet.hk2.annotations.Inject;
import org.jvnet.hk2.component.PerLookup;
import org.jvnet.hk2.config.ConfigSupport;
import org.jvnet.hk2.config.SingleConfigCode;
import org.jvnet.hk2.config.TransactionFailure;

/**
 * Create Admin Object Command
 *
 */
@Service(name="create-jms-host")
@Scoped(PerLookup.class)
@I18n("create.jms.host")
public class CreateJMSHost implements AdminCommand {

    final private static LocalStringManagerImpl localStrings = new LocalStringManagerImpl(CreateJMSHost.class);
    //[target target] [mqhost localhost] [mqport 7676] [mquser admin] [mqpassword admin] jms_host_name

    @Param(name="mqhost", defaultValue="localhost")
    String mqhost;

    @Param(name="mqport", defaultValue="7676")
    String mqport;

    @Param(name="mquser", defaultValue="admin")
    String mquser;

    @Param(name="mqpassword", defaultValue="admin")
    String mqpassword;

    @Param(optional=true)
    String target = SystemPropertyConstants.DEFAULT_SERVER_INSTANCE_NAME;

    @Param(name="jms_host_name", primary=true)
    String jmsHostName;

    @Inject
    Configs configs;

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

        Server targetServer = domain.getServerNamed(target);
        String configRef = targetServer.getConfigRef();

        if (jmsHostName == null) {
            report.setMessage(localStrings.getLocalString("create.jms.host.noJmsHost",
                            "No JMS Host name specified."));
            report.setActionExitCode(ActionReport.ExitCode.FAILURE);
            return;
        }
        JmsService jmsservice = null;
        for (Config c : configs.getConfig()) {

               if(configRef.equals(c.getName()))
                     jmsservice = c.getJmsService();
            }

        // ensure we don't already have one of this name
        for (JmsHost jmsHost : jmsservice.getJmsHost()) {
                if (jmsHostName.equals(jmsHost.getName())) {
                    report.setMessage(localStrings.getLocalString("create.jms.host.duplicate",
                            "A JMS Host named {0} already exists.", jmsHostName));
                    report.setActionExitCode(ActionReport.ExitCode.FAILURE);
                    return;
                }
            }

        try {
            ConfigSupport.apply(new SingleConfigCode<JmsService>() {
                public Object run(JmsService param) throws PropertyVetoException, TransactionFailure {

                    JmsHost jmsHost = param.createChild(JmsHost.class); //TODO: need a way to create a JmsHost instance
                    jmsHost.setAdminPassword(mqpassword);
                    jmsHost.setAdminUserName(mquser);
                    jmsHost.setName(jmsHostName);
                    jmsHost.setHost(mqhost);
                    jmsHost.setPort(mqport);

                    param.getJmsHost().add(jmsHost);

                    return jmsHost;
                }
            }, jmsservice);
        } catch(TransactionFailure tfe) {
            report.setMessage(localStrings.getLocalString("create.jms.host.fail",
                            "Unable to create jms host {0}.", jmsHostName) +
                            " " + tfe.getLocalizedMessage());
            report.setActionExitCode(ActionReport.ExitCode.FAILURE);
            report.setFailureCause(tfe);
        }
        report.setMessage(localStrings.getLocalString("create.jms.host.success",
                "Jms Host {0} created.", jmsHostName));
        report.setActionExitCode(ActionReport.ExitCode.SUCCESS);
    }

}
