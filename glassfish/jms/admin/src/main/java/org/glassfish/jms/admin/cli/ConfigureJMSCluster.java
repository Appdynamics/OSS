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

import org.glassfish.api.I18n;
import org.glassfish.api.Param;
import org.glassfish.api.ActionReport;
import org.glassfish.api.admin.*;
import com.sun.enterprise.util.LocalStringManagerImpl;
import com.sun.enterprise.config.serverbeans.*;

import org.glassfish.api.admin.AdminCommand;
import org.glassfish.api.admin.AdminCommandContext;
import org.glassfish.api.admin.RuntimeType;
import com.sun.enterprise.config.serverbeans.Cluster;

import java.util.List;
import java.util.Properties;
import java.util.Map;
import java.beans.PropertyVetoException;
import org.jvnet.hk2.annotations.Service;
import org.jvnet.hk2.annotations.Scoped;
import org.jvnet.hk2.annotations.Inject;
import org.jvnet.hk2.component.PerLookup;
import org.jvnet.hk2.config.types.Property;
import org.jvnet.hk2.config.ConfigSupport;
import org.jvnet.hk2.config.SingleConfigCode;
import org.jvnet.hk2.config.TransactionFailure;


/**
 * Configure JMS Cluster Command
 *
 */
@Service(name="configure-jms-cluster")
@Scoped(PerLookup.class)
@I18n("configure.jms.cluster")
@ExecuteOn({RuntimeType.DAS, RuntimeType.INSTANCE})
//@TargetType({CommandTarget.CLUSTER})

public class ConfigureJMSCluster implements AdminCommand {
    final private static String SUPPORTED_DB_VENDORS = "oracle|postgressql|mysql|derby|db2";
    final private static LocalStringManagerImpl localStrings = new LocalStringManagerImpl(ConfigureJMSCluster.class);
    final private static String MASTER_BROKER = "masterbroker";
    final private static String SHARED_DB = "shareddb";
    final private static String FILE = "file";
    final private static String JDBC = "jdbc";
    final private static String CONVENTIONAL = "conventional";
    final private static String ENHANCED = "enhanced";
    final private static String LOCAL = "local";
    final private static String REMOTE = "remote";
    final private static String EMBEDDED = "embedded";
    final private static String PASSWORD_KEY="AS_ADMIN_JMSDBPASSWORD";
    // [usemasterbroker] [availability-enabled] [dbvendor] [dbuser] [dbpassword admin] [jdbcurl] [properties props] clusterName
    /*
    configure-jms-cluster [--clustertype =conventional | enhanced] [--messagestoretype=jdbc | file] [--configstoretype=masterbroker | shareddb] [--dbvendor] [--dbuser] [--dbpassword]  [--dburl] [--force] [--property (name=value)[:name-value]*] clusterName

    a. Message store type (JDBC | file) – defaults to file

    b. Config store type (MasterBroker | SharedDB) – defaults to MasterBroker
    c. Cluster Type: Conventional | Enhanced (if enhanced, then (a) and (b) are ignored).
     */

    @Param(name="configstoretype", optional=true, alias="cs")
    String configStoreType;

    @Param(name="messagestoretype", optional=true, alias="ms")
    String messageStoreType;

    @Param(name="clustertype", alias="ct", optional=false)
    String clusterType;

    @Param(name="dbvendor", alias="db", optional=true)
    String dbvendor;

    @Param(name="dbuser", alias="user", optional=true)
    String dbuser;

    @Param(name="jmsDbPassword", optional=true, password=true)
    String jmsDbPassword ;

    @Param(name="dburl", alias="url", optional=true)
    String dburl;

    @Param(name="property", optional=true, separator=':')
    Properties props;

    //@Param(name="force", defaultValue="false", optional=true)
    //boolean force;

    @Param(primary=true)
    String clusterName;

    @Inject
    Domain domain;

    Config config;

    /**
     * Executes the command with the command parameters passed as Properties
     * where the keys are the paramter names and the values the parameter values
     *
     * @param context information
     */
    public void execute(AdminCommandContext context) {
        final ActionReport report = context.getActionReport();
       // Server targetServer = domain.getServerNamed(target);
        //String configRef = targetServer.getConfigRef();

        Cluster cluster =domain.getClusterNamed(clusterName);

        if (cluster == null) {
            report.setMessage(localStrings.getLocalString("configure.jms.cluster.invalidClusterName",
                            "No Cluster by this name has been configured"));
            report.setActionExitCode(ActionReport.ExitCode.FAILURE);
            return;
        }

        List instances = cluster.getInstances();
        /*if(!force && instances.size() > 0){
            report.setMessage(localStrings.getLocalString("configure.jms.cluster.clusterWithInstances",
                            "The cluster has existing instances configured. This command should be run before adding instances to the cluster. To force run this command, please follow the instructions in the documentation and use the --force=true option"));
            report.setActionExitCode(ActionReport.ExitCode.FAILURE);
            return;
        } */

        Config config = domain.getConfigNamed(cluster.getConfigRef());
        JmsService jmsService = config.getJmsService();

        if(jmsService == null) {
            report.setMessage(localStrings.getLocalString("configure.jms.cluster.nojmsservice",
                            "No JMS Service element in config"));
            report.setActionExitCode(ActionReport.ExitCode.FAILURE);
            return;
        }

        if(! CONVENTIONAL.equalsIgnoreCase(clusterType) && ! ENHANCED.equalsIgnoreCase(clusterType)){
            report.setMessage(localStrings.getLocalString("configure.jms.cluster.wrongClusterType",
                            "Invalid option sepecified for clustertype. Valid options are conventional and enhanced"));
            report.setActionExitCode(ActionReport.ExitCode.FAILURE);
            return;
        }

        if (CONVENTIONAL.equalsIgnoreCase(clusterType) && ! MASTER_BROKER.equalsIgnoreCase(configStoreType) && ! SHARED_DB.equalsIgnoreCase(configStoreType)){
            report.setMessage(localStrings.getLocalString("configure.jms.cluster.wrongConfigStoreType",
                            "Invalid option sepecified for configstoretype. Valid options are masterbroker and shareddb"));
            report.setActionExitCode(ActionReport.ExitCode.FAILURE);
            return;
        }
        if(ENHANCED.equalsIgnoreCase(clusterType) && configStoreType  != null){
            report.setMessage(localStrings.getLocalString("configure.jms.cluster.wrongStoreType",
                            "configstoretype option is not configurable for Enhanced clusters."));
            report.setActionExitCode(ActionReport.ExitCode.FAILURE);
            return;
        }
        if (CONVENTIONAL.equalsIgnoreCase(clusterType) && ! MASTER_BROKER.equalsIgnoreCase(configStoreType) && ! FILE.equalsIgnoreCase(messageStoreType) && ! JDBC.equalsIgnoreCase(messageStoreType)){
            report.setMessage(localStrings.getLocalString("configure.jms.cluster.wrongMessageStoreType",
                            "Invalid option sepecified for messagestoretype. Valid options are file and jdbc"));
            report.setActionExitCode(ActionReport.ExitCode.FAILURE);
            return;
        }
        if(ENHANCED.equalsIgnoreCase(clusterType) && messageStoreType  != null){
            report.setMessage(localStrings.getLocalString("configure.jms.cluster.wrongmsgStoreType",
                            "messagestoretype option is not configurable for Enhanced clusters."));
            report.setActionExitCode(ActionReport.ExitCode.FAILURE);
            return;
        }

        String integrationMode = jmsService.getType();
        if(REMOTE.equalsIgnoreCase(integrationMode)) {
            report.setMessage(localStrings.getLocalString("configure.jms.cluster.remoteMode",
                            "JMS integration mode should be either EMBEDDED or LOCAL to run this command. Please use the asadmin.set command to change the integration mode"));
            report.setActionExitCode(ActionReport.ExitCode.FAILURE);
            return;
        }
        if(EMBEDDED.equalsIgnoreCase(integrationMode) && ENHANCED.equalsIgnoreCase(clusterType)) {
            report.setMessage(localStrings.getLocalString("configure.jms.cluster.noEmbeddedModeForEnhancedClusters",
                            "EMBEDDED JMS integration mode is not supported for Enhanced clusters. Please use the asadmin.set command to change the JMS integration mode"));
            report.setActionExitCode(ActionReport.ExitCode.FAILURE);
            return;
        }
        if (MASTER_BROKER.equalsIgnoreCase(configStoreType) && FILE.equals(messageStoreType)){

            if(dbvendor != null || dburl != null || dbuser != null) {
                report.setMessage(localStrings.getLocalString("configure.jms.cluster.invalidDboptions",
                            "Database options should not be specified for this configuration"));
                report.setActionExitCode(ActionReport.ExitCode.FAILURE);
                return;
            }
        }
        if(! MASTER_BROKER.equalsIgnoreCase(configStoreType) || ENHANCED.equalsIgnoreCase(clusterType) || JDBC.equalsIgnoreCase(messageStoreType)){
          if (dbvendor == null) {
            report.setMessage(localStrings.getLocalString("configure.jms.cluster.nodbvendor",
                            "No DataBase vendor specified"));
            report.setActionExitCode(ActionReport.ExitCode.FAILURE);
            return;
         }
         else if (dburl == null) {
            report.setMessage(localStrings.getLocalString("configure.jms.cluster.nojdbcurl",
                            "No JDBC URL specified"));
            report.setActionExitCode(ActionReport.ExitCode.FAILURE);
            return;
         }
         else if (! isSupportedDbVendor()){
             report.setMessage(localStrings.getLocalString("configure.jms.cluster.invaliddbvendor",
                            "Invalid DB Vednor specified"));
            report.setActionExitCode(ActionReport.ExitCode.FAILURE);
            return;
         }
        }
        if(CONVENTIONAL.equalsIgnoreCase(clusterType) && configStoreType  == null){
            report.setMessage(localStrings.getLocalString("configure.jms.cluster.noConfigStoreType",
                            "No configstoretype specified. Using the default value - masterbroker"));
            configStoreType="masterbroker";
        }
        if(CONVENTIONAL.equalsIgnoreCase(clusterType) && messageStoreType  == null){
            report.setMessage(localStrings.getLocalString("configure.jms.cluster.noMessagetoreType",
                            "No messagestoretype specified. Using the default value - file"));
            messageStoreType="file";
        }


        config = domain.getConfigNamed(cluster.getConfigRef());

        JmsAvailability jmsAvailability = config.getAvailabilityService().getJmsAvailability();
        JmsService jmsservice = config.getJmsService();
        final Boolean availabilityEnabled = new Boolean(ENHANCED.equalsIgnoreCase(clusterType));

        try {
            ConfigSupport.apply(new SingleConfigCode<JmsAvailability>() {
                public Object run(JmsAvailability param) throws PropertyVetoException, TransactionFailure {
                    param.setAvailabilityEnabled(availabilityEnabled.toString());
                    if(availabilityEnabled.booleanValue()){
                        param.setMessageStoreType(JDBC);
                    }
                    else{
                        param.setConfigStoreType(configStoreType.toLowerCase());
                        param.setMessageStoreType(messageStoreType.toLowerCase());
                    }
                    param.setDbVendor(dbvendor);
                    param.setDbUsername(dbuser);
                    param.setDbPassword(jmsDbPassword);
                    param.setDbUrl(dburl);

                    if(props != null)
                    {
                       for (Map.Entry e: props.entrySet()){
                        Property prop = param.createChild(Property.class);
                        prop.setName((String)e.getKey());
                        prop.setValue((String)e.getValue());
                        param.getProperty().add(prop);
                        }
                    }
                    return param;
                  }
               }, jmsAvailability);

           /* //update the useMasterBroker flag on the JmsService only if availabiltyEnabled is false
            if(!availabilityEnabled.booleanValue()){
              ConfigSupport.apply(new SingleConfigCode<JmsService>() {
                public Object run(JmsService param) throws PropertyVetoException, TransactionFailure {

                    param.setUseMasterBroker(useMasterBroker.toString());
                    return param;
                }
            }, jmsservice);
            }*/

        } catch(TransactionFailure tfe) {
            report.setMessage(localStrings.getLocalString("configure.jms.cluster.fail",
                            "Unable to Configure JMS Cluster for cluster {0}.", clusterName) +
                            " " + tfe.getLocalizedMessage());
            report.setActionExitCode(ActionReport.ExitCode.FAILURE);
            report.setFailureCause(tfe);
        }
        String warning= "";
        if(instances.size() > 0){
            warning=localStrings.getLocalString("configure.jms.cluster.clusterWithInstances",
                                    "WARNING: Please ensure that you have followed the instructions specified in the documentation before running this command with this option. Running this command without the required precautions can lead to inconsistent JMS behavior and corruption of configuration and message stores.");
        }
        report.setMessage(warning + "\n" + localStrings.getLocalString("configure.jms.cluster.success",
                "JMS Cluster Configuration updated for Cluster {0}.", clusterName));
        report.setActionExitCode(ActionReport.ExitCode.SUCCESS);
    }

    private boolean isSupportedDbVendor(){
        if (dbvendor != null)
        {
            return SUPPORTED_DB_VENDORS.contains(dbvendor.toLowerCase());
        }
        return false;
    }
}
