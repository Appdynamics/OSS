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
package com.sun.enterprise.config.serverbeans;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.common.util.logging.LoggingConfigImpl;
import com.sun.grizzly.config.dom.NetworkConfig;
import org.jvnet.hk2.config.types.Property;
import org.jvnet.hk2.config.types.PropertyBag;
import org.glassfish.api.admin.config.Container;
import org.glassfish.api.admin.config.Named;
import org.glassfish.api.admin.config.PropertiesDesc;
import org.glassfish.api.admin.config.PropertyDesc;
import org.glassfish.config.support.datatypes.Port;
import org.glassfish.quality.ToDo;
import org.glassfish.server.ServerEnvironmentImpl;
import org.jvnet.hk2.component.Injectable;
import org.jvnet.hk2.config.Attribute;
import org.jvnet.hk2.config.ConfigBean;
import org.jvnet.hk2.config.ConfigBeanProxy;
import org.jvnet.hk2.config.ConfigView;
import org.jvnet.hk2.config.Configured;
import org.jvnet.hk2.config.DuckTyped;
import org.jvnet.hk2.config.Element;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * The configuration defines the configuration of a server instance that can be
 * shared by other server instances. The availability-service and are SE/EE only
 */

/* @XmlType(name = "", propOrder = {
    "httpService",
    "iiopService",
    "adminService",
    "connectorService",
    "webContainer",
    "ejbContainer",
    "mdbContainer",
    "jmsService",
    "logService",
    "securityService",
    "transactionService",
    "monitoringService",
    "diagnosticService",
    "javaConfig",
    "availabilityService",
    "threadPools",
    "alertService",
    "groupManagementService",
    "managementRules",
    "systemProperty",
    "property"
}) */

@Configured
public interface Config extends ConfigBeanProxy, Injectable, Named, PropertyBag, SystemPropertyBag {

    /**
     *  Name of the configured object
     *
     * @return name of the configured object
     FIXME: should set 'key=true'.  See bugs 6039, 6040
     */
    @NotNull
    @Pattern(regexp="[\\p{L}\\p{N}_][\\p{L}\\p{N}\\-_./;#]*")
    String getName();

    void setName(String value) throws PropertyVetoException;

    /**
     * Gets the value of the dynamicReconfigurationEnabled property.
     *
     * When set to "true" then any changes to the system (e.g. applications
     * deployed, resources created) will be automatically applied to the
     * affected servers without a restart being required. When set to
     * "false" such changes will only be picked up by the affected servers
     * when each server restarts.
     *
     * @return possible object is
     *         {@link String }
     */
    @Attribute (defaultValue="true",dataType=Boolean.class)
    String getDynamicReconfigurationEnabled();

    /**
     * Sets the value of the dynamicReconfigurationEnabled property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    void setDynamicReconfigurationEnabled(String value) throws PropertyVetoException;

    /**
     * Gets the value of the networkConfig property.
     *
     * @return possible object is {@link NetworkConfig }
     */
    @Element(required=true)
    NetworkConfig getNetworkConfig();

    /**
     * Sets the value of the networkConfig property.
     *
     * @param value allowed object is {@link NetworkConfig }
     */
    void setNetworkConfig(NetworkConfig value) throws PropertyVetoException;

    /**
     * Gets the value of the httpService property.
     *
     * @return possible object is
     *         {@link HttpService }
     */
    @Element(required=true)
    HttpService getHttpService();

    /**
     * Sets the value of the httpService property.
     *
     * @param value allowed object is
     *              {@link HttpService }
     */
    void setHttpService(HttpService value) throws PropertyVetoException;

    /**
     * Gets the value of the iiopService property.
     *
     * @return possible object is
     *         {@link IiopService }
     */
    @Element(required=true)
    IiopService getIiopService();

    /**
     * Sets the value of the iiopService property.
     *
     * @param value allowed object is
     *              {@link IiopService }
     */
    void setIiopService(IiopService value) throws PropertyVetoException;

    /**
     * Gets the value of the adminService property.
     *
     * @return possible object is
     *         {@link AdminService }
     */
    @Element(required=true)
    AdminService getAdminService();

    /**
     * Sets the value of the adminService property.
     *
     * @param value allowed object is
     *              {@link AdminService }
     */
    void setAdminService(AdminService value) throws PropertyVetoException;

    /**
     * Gets the value of the connectorService property.
     *
     * @return possible object is
     *         {@link ConnectorService }
     */
    @Element
    ConnectorService getConnectorService();

    /**
     * Sets the value of the connectorService property.
     *
     * @param value allowed object is
     *              {@link ConnectorService }
     */
    void setConnectorService(ConnectorService value) throws PropertyVetoException;

    /**
     * Gets the value of the webContainer property.
     *
     * @return possible object is
     *         {@link WebContainer }
     */
    @Element(required=true)
    WebContainer getWebContainer();

    /**
     * Sets the value of the webContainer property.
     *
     * @param value allowed object is
     *              {@link WebContainer }
     */
    void setWebContainer(WebContainer value) throws PropertyVetoException;

    /**
     * Gets the value of the ejbContainer property.
     *
     * @return possible object is
     *         {@link EjbContainer }
     */
    @Element(required=true)
    EjbContainer getEjbContainer();

    /**
     * Sets the value of the ejbContainer property.
     *
     * @param value allowed object is
     *              {@link EjbContainer }
     */
    void setEjbContainer(EjbContainer value) throws PropertyVetoException;

    /**
     * Gets the value of the mdbContainer property.
     *
     * @return possible object is
     *         {@link MdbContainer }
     */
    @Element(required=true)
    MdbContainer getMdbContainer();

    /**
     * Sets the value of the mdbContainer property.
     *
     * @param value allowed object is
     *              {@link MdbContainer }
     */
    void setMdbContainer(MdbContainer value) throws PropertyVetoException;

    /**
     * Gets the value of the jmsService property.
     *
     * @return possible object is
     *         {@link JmsService }
     */
    @Element
    JmsService getJmsService();

    /**
     * Sets the value of the jmsService property.
     *
     * @param value allowed object is
     *              {@link JmsService }
     */
    void setJmsService(JmsService value) throws PropertyVetoException;

    /**
     * Gets the value of the logService property.
     *
     * @return possible object is
     *         {@link LogService }
     */
    @Element(required=true)
    LogService getLogService();

    /**
     * Sets the value of the logService property.
     *
     * @param value allowed object is
     *              {@link LogService }
     */
    void setLogService(LogService value) throws PropertyVetoException;

    /**
     * Gets the value of the securityService property.
     *
     * @return possible object is
     *         {@link SecurityService }
     */
    @Element(required=true)
    SecurityService getSecurityService();

    /**
     * Sets the value of the securityService property.
     *
     * @param value allowed object is
     *              {@link SecurityService }
     */
    void setSecurityService(SecurityService value) throws PropertyVetoException;

    /**
     * Gets the value of the transactionService property.
     *
     * @return possible object is
     *         {@link TransactionService }
     */
    @Element(required=true)
    TransactionService getTransactionService();

    /**
     * Sets the value of the transactionService property.
     *
     * @param value allowed object is
     *              {@link TransactionService }
     */
    void setTransactionService(TransactionService value) throws PropertyVetoException;

    /**
     * Gets the value of the monitoringService property.
     *
     * @return possible object is
     *         {@link MonitoringService }
     */
    @Element(required=true)
    MonitoringService getMonitoringService();

    /**
     * Sets the value of the monitoringService property.
     *
     * @param value allowed object is
     *              {@link MonitoringService }
     */
    void setMonitoringService(MonitoringService value) throws PropertyVetoException;

    /**
     * Gets the value of the diagnosticService property.
     *
     * @return possible object is
     *         {@link DiagnosticService }
     */
    @Element
    DiagnosticService getDiagnosticService();

    /**
     * Sets the value of the diagnosticService property.
     *
     * @param value allowed object is
     *              {@link DiagnosticService }
     */
    void setDiagnosticService(DiagnosticService value) throws PropertyVetoException;

    /**
     * Gets the value of the javaConfig property.
     *
     * @return possible object is
     *         {@link JavaConfig }
     */
    @Element(required=true)
    JavaConfig getJavaConfig();

    /**
     * Sets the value of the javaConfig property.
     *
     * @param value allowed object is
     *              {@link JavaConfig }
     */
    void setJavaConfig(JavaConfig value) throws PropertyVetoException;

    /**
     * Gets the value of the availabilityService property.
     *
     * @return possible object is
     *         {@link AvailabilityService }
     */
    @Element
    AvailabilityService getAvailabilityService();

    /**
     * Sets the value of the availabilityService property.
     *
     * @param value allowed object is
     *              {@link AvailabilityService }
     */
    void setAvailabilityService(AvailabilityService value) throws PropertyVetoException;

    /**
     * Gets the value of the threadPools property.
     *
     * @return possible object is
     *         {@link ThreadPools }
     */
    @Element(required=true)
    ThreadPools getThreadPools();

    /**
     * Sets the value of the threadPools property.
     *
     * @param value allowed object is
     *              {@link ThreadPools }
     */
    void setThreadPools(ThreadPools value) throws PropertyVetoException;

    /**
     * Gets the value of the alertService property.
     *
     * @return possible object is
     *         {@link AlertService }
     */
    @Element
    AlertService getAlertService();

    /**
     * Sets the value of the alertService property.
     *
     * @param value allowed object is
     *              {@link AlertService }
     */
    void setAlertService(AlertService value) throws PropertyVetoException;

    /**
     * Gets the value of the groupManagementService property.
     *
     * @return possible object is
     *         {@link GroupManagementService }
     */
    @Element
    GroupManagementService getGroupManagementService();

    /**
     * Sets the value of the groupManagementService property.
     *
     * @param value allowed object is
     *              {@link GroupManagementService }
     */
    void setGroupManagementService(GroupManagementService value) throws PropertyVetoException;

    /**
     * Gets the value of the managementRules property.
     *
     * @return possible object is
     *         {@link ManagementRules }
     */
    @Element
    ManagementRules getManagementRules();

    /**
     * Sets the value of the managementRules property.
     *
     * @param value allowed object is
     *              {@link ManagementRules }
     */
    void setManagementRules(ManagementRules value) throws PropertyVetoException;

    /**
     * Gets the value of the systemProperty property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the systemProperty property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSystemProperty().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link SystemProperty }
     */
    @ToDo(priority=ToDo.Priority.IMPORTANT, details="Any more legal system properties?" )
@PropertiesDesc(
    systemProperties=true,
    props={
        @PropertyDesc(name="HTTP_LISTENER_PORT", defaultValue="8080", dataType=Port.class),
        @PropertyDesc(name="HTTP_SSL_LISTENER_PORT", defaultValue="1043", dataType=Port.class),
        @PropertyDesc(name="HTTP_ADMIN_LISTENER_PORT", defaultValue="4848", dataType=Port.class),
        @PropertyDesc(name="IIOP_LISTENER_PORT", defaultValue="3700", dataType=Port.class),
        @PropertyDesc(name="IIOP_SSL_LISTENER_PORT", defaultValue="1060", dataType=Port.class),
        @PropertyDesc(name="IIOP_SSL_MUTUALAUTH_PORT", defaultValue="1061", dataType=Port.class),
        @PropertyDesc(name="JMX_SYSTEM_CONNECTOR_PORT", defaultValue="8686", dataType=Port.class)
    }
    )
    @Element
    List<SystemProperty> getSystemProperty();


    //DuckTyped for accessing the logging.properties file

    @DuckTyped
    Map<String, String> getLoggingProperties();

    @DuckTyped
    String setLoggingProperty(String property, String value);

    @DuckTyped
    Map<String, String> updateLoggingProperties( Map<String, String> properties);

    class Duck {

        public static String setLoggingProperty(Config c, String property, String value){
            ConfigBean cb = (ConfigBean) ((ConfigView)Proxy.getInvocationHandler(c)).getMasterView();
            ServerEnvironmentImpl env = cb.getHabitat().getComponent(ServerEnvironmentImpl.class);
            LoggingConfigImpl loggingConfig = new LoggingConfigImpl();
            loggingConfig.setupConfigDir(env.getConfigDirPath(), env.getLibPath());

            String prop = null;
            try{
                   prop= loggingConfig.setLoggingProperty(property, value);
            } catch (IOException ex){
            }
            return prop;
        }

        public static Map<String, String>getLoggingProperties(Config c) {
            ConfigBean cb = (ConfigBean) ((ConfigView)Proxy.getInvocationHandler(c)).getMasterView();
            ServerEnvironmentImpl env = cb.getHabitat().getComponent(ServerEnvironmentImpl.class);
            LoggingConfigImpl loggingConfig = new LoggingConfigImpl();
            loggingConfig.setupConfigDir(env.getConfigDirPath(), env.getLibPath());

            Map <String, String> map = new HashMap<String, String>() ;
            try {
                map = loggingConfig.getLoggingProperties();
            } catch (IOException ex){
            }
            return map;
        }

        public static Map<String, String>updateLoggingProperties(Config c, Map<String, String>properties){
            ConfigBean cb = (ConfigBean) ((ConfigView)Proxy.getInvocationHandler(c)).getMasterView();
            ServerEnvironmentImpl env = cb.getHabitat().getComponent(ServerEnvironmentImpl.class);
            LoggingConfigImpl loggingConfig = new LoggingConfigImpl();
            loggingConfig.setupConfigDir(env.getConfigDirPath(), env.getLibPath());

            Map <String, String> map = new HashMap<String, String>() ;
            try {
                map = loggingConfig.updateLoggingProperties(properties);
            } catch (IOException ex){
            }
            return map;
        }

    }
    /**
    	Properties as per {@link PropertyBag}
     */
    @ToDo(priority=ToDo.Priority.IMPORTANT, details="Provide PropertyDesc for legal props" )
    @PropertiesDesc(props={})
    @Element
    List<Property> getProperty();

    /**
     * Get the configuration for other types of containers.
     * 
     * @return  list of containers configuration
     */
    @Element("*")
    List<Container> getContainers();
}



