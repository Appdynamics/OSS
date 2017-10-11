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

package com.sun.enterprise.resource.deployer;

import com.sun.appserv.connectors.internal.spi.ResourceDeployer;
import com.sun.appserv.connectors.internal.api.ConnectorsUtil;
import com.sun.enterprise.deployment.DataSourceDefinitionDescriptor;
import com.sun.enterprise.config.serverbeans.JdbcConnectionPool;
import com.sun.enterprise.config.serverbeans.JdbcResource;
import com.sun.logging.LogDomains;
import org.jvnet.hk2.annotations.Service;
import org.jvnet.hk2.annotations.Inject;
import org.jvnet.hk2.config.ConfigBeanProxy;
import org.jvnet.hk2.config.TransactionFailure;
import org.jvnet.hk2.config.types.Property;
import org.jvnet.hk2.component.Habitat;

import java.beans.PropertyVetoException;
import java.util.List;
import java.util.Properties;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;
import java.util.logging.Level;


/**
 * @author Jagadish Ramu
 */
@Service
public class DataSourceDefinitionDeployer implements ResourceDeployer {

    @Inject
    Habitat habitat;

    private static Logger _logger = LogDomains.getLogger(DataSourceDefinitionDeployer.class, LogDomains.RSR_LOGGER);

    public void deployResource(Object resource) throws Exception {

        final DataSourceDefinitionDescriptor desc = (DataSourceDefinitionDescriptor) resource;

        String poolName = ConnectorsUtil.deriveDataSourceDefinitionPoolName(desc.getResourceId(), desc.getName());
        String resourceName = ConnectorsUtil.deriveDataSourceDefinitionResourceName(desc.getResourceId(), desc.getName());

        if(_logger.isLoggable(Level.FINE)) {
            _logger.log(Level.FINE, "DataSourceDefinitionDeployer.deployResource() : pool-name ["+poolName+"], " +
                    " resource-name ["+resourceName+"]");
        }

        JdbcConnectionPool jdbcCp = new MyJdbcConnectionPool(desc, poolName);

        Collection<ResourceDeployer> deployers = habitat.getAllByContract(ResourceDeployer.class);
        //deploy pool
        getDeployer(jdbcCp, deployers).deployResource(jdbcCp);

        //deploy resource
        JdbcResource jdbcResource = new MyJdbcResource(poolName, resourceName);
        getDeployer(jdbcResource, deployers).deployResource(jdbcResource);
    }

    private ResourceDeployer getDeployer(Object resource, Collection<ResourceDeployer> deployers) {
        ResourceDeployer resourceDeployer = null;
        for (ResourceDeployer deployer : deployers) {
            if (deployer.handles(resource)) {
                resourceDeployer = deployer;
                break;
            }
        }
        return resourceDeployer;
    }

    private DataSourceProperty convertProperty(String name, String value) {
        return new DataSourceProperty(name, value);
    }

    public void undeployResource(Object resource) throws Exception {

        final DataSourceDefinitionDescriptor desc = (DataSourceDefinitionDescriptor) resource;
        Collection<ResourceDeployer> deployers = habitat.getAllByContract(ResourceDeployer.class);

        String poolName = ConnectorsUtil.deriveDataSourceDefinitionPoolName(desc.getResourceId(), desc.getName());
        String resourceName = ConnectorsUtil.deriveDataSourceDefinitionResourceName(desc.getResourceId(), desc.getName());

        if(_logger.isLoggable(Level.FINE)) {
            _logger.log(Level.FINE, "DataSourceDefinitionDeployer.undeployResource() : pool-name ["+poolName+"], " +
                    " resource-name ["+resourceName+"]");
        }

        //undeploy resource
        JdbcResource jdbcResource = new MyJdbcResource(poolName, resourceName);
        getDeployer(jdbcResource, deployers).undeployResource(jdbcResource);

        //undeploy pool
        JdbcConnectionPool jdbcCp = new MyJdbcConnectionPool(desc, poolName);
        getDeployer(jdbcCp, deployers).undeployResource(jdbcCp);
    }

    public void redeployResource(Object resource) throws Exception {
        throw new UnsupportedOperationException("redeploy() not supported for datasource-definition type");
    }

    public void enableResource(Object resource) throws Exception {
        throw new UnsupportedOperationException("enable() not supported for datasource-definition type");
    }

    public void disableResource(Object resource) throws Exception {
        throw new UnsupportedOperationException("disable() not supported for datasource-definition type");
    }

    public boolean handles(Object resource) {
        return resource instanceof DataSourceDefinitionDescriptor;
    }

    class DataSourceProperty implements Property {

        private String name;
        private String value;
        private String description;

        DataSourceProperty(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String value) throws PropertyVetoException {
            this.name = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) throws PropertyVetoException {
            this.value = value;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String value) throws PropertyVetoException {
            this.description = value;
        }

        public ConfigBeanProxy getParent() {
            return null;
        }

        public <T extends ConfigBeanProxy> T getParent(Class<T> tClass) {
            return null;
        }

        public <T extends ConfigBeanProxy> T createChild(Class<T> tClass) throws TransactionFailure {
            return null;
        }

        public void injectedInto(Object o) {
            //do nothing
        }
    }

    class MyJdbcResource implements JdbcResource {

        private String poolName;
        private String jndiName;

        MyJdbcResource(String poolName, String jndiName) {
            this.poolName = poolName;
            this.jndiName = jndiName;
        }

        public String getPoolName() {
            return poolName;
        }

        public void setPoolName(String value) throws PropertyVetoException {
            this.poolName = value;
        }

        public String getObjectType() {
            return null;
        }

        public void setObjectType(String value) throws PropertyVetoException {
        }

        public String getEnabled() {
            return String.valueOf(true);
        }

        public void setEnabled(String value) throws PropertyVetoException {
        }

        public String getDescription() {
            return null;
        }

        public void setDescription(String value) throws PropertyVetoException {
        }

        public List<Property> getProperty() {
            return null;
        }

        public Property getProperty(String name) {
            return null;
        }

        public String getPropertyValue(String name) {
            return null;
        }

        public String getPropertyValue(String name, String defaultValue) {
            return null;
        }

        public ConfigBeanProxy getParent() {
            return null;
        }

        public <T extends ConfigBeanProxy> T getParent(Class<T> tClass) {
            return null;
        }

        public <T extends ConfigBeanProxy> T createChild(Class<T> tClass) throws TransactionFailure {
            return null;
        }

        public void injectedInto(Object o) {
        }

        public String getJndiName() {
            return jndiName;
        }

        public void setJndiName(String value) throws PropertyVetoException {
            this.jndiName = value;
        }
    }

    class MyJdbcConnectionPool implements JdbcConnectionPool {

        private DataSourceDefinitionDescriptor desc;
        private String name;

        public MyJdbcConnectionPool(DataSourceDefinitionDescriptor desc, String name) {
            this.desc = desc;
            this.name = name;
        }

        public String getDatasourceClassname() {
            return desc.getClassName();
        }

        public void setDatasourceClassname(String value) throws PropertyVetoException {
            //do nothing
        }

        public String getResType() {
            String type = "javax.sql.DataSource";
            try {
                Class clz = Thread.currentThread().getContextClassLoader().loadClass(desc.getClassName());
                if (javax.sql.ConnectionPoolDataSource.class.isAssignableFrom(clz)) {
                    type = "javax.sql.ConnectionPoolDataSource";
                } else if (javax.sql.XADataSource.class.isAssignableFrom(clz)) {
                    type = "javax.sql.XADataSource";
                }
            } catch (ClassNotFoundException e) {
                _logger.log(Level.FINEST, "Unable to load class [ " + desc.getClassName() + " ] to " +
                        "determine its res-type, defaulting to javax.sql.DataSource");
                // ignore and default to "javax.sql.DataSource"
            }
            return type;
        }

        public void setResType(String value) throws PropertyVetoException {
            //do nothing
        }

        public String getSteadyPoolSize() {
            int minPoolSize = desc.getMinPoolSize();
            if (minPoolSize == -1) {
                minPoolSize = 8;
            }
            return String.valueOf(minPoolSize);
        }

        public void setSteadyPoolSize(String value) throws PropertyVetoException {
            //do nothing
        }

        public String getMaxPoolSize() {
            int maxPoolSize = desc.getMaxPoolSize();
            if (maxPoolSize == -1) {
                maxPoolSize = 32;
            }
            return String.valueOf(maxPoolSize);
        }

        public void setMaxPoolSize(String value) throws PropertyVetoException {
            //do nothing
        }

        public String getMaxWaitTimeInMillis() {
            return String.valueOf(60000);
        }

        public void setMaxWaitTimeInMillis(String value) throws PropertyVetoException {
            //do nothing
        }

        public String getPoolResizeQuantity() {
            return String.valueOf(2);
        }

        public void setPoolResizeQuantity(String value) throws PropertyVetoException {
            //do nothing
        }

        public String getIdleTimeoutInSeconds() {
            long maxIdleTime = desc.getMaxIdleTime();
            if (maxIdleTime == -1) {
                maxIdleTime = 300;
            }
            return String.valueOf(maxIdleTime);
        }

        public void setIdleTimeoutInSeconds(String value) throws PropertyVetoException {
            //do nothing
        }

        public String getTransactionIsolationLevel() {
            if (desc.getIsolationLevel() == -1) {
                return null;
            } else {
                return ConnectorsUtil.getTransactionIsolationInt(desc.getIsolationLevel());
            }
        }

        public void setTransactionIsolationLevel(String value) throws PropertyVetoException {
            //do nothing
        }

        public String getIsIsolationLevelGuaranteed() {
            return String.valueOf("true");
        }

        public void setIsIsolationLevelGuaranteed(String value) throws PropertyVetoException {
            //do nothing
        }

        public String getIsConnectionValidationRequired() {
            return String.valueOf("false");
        }

        public void setIsConnectionValidationRequired(String value) throws PropertyVetoException {
            //do nothing
        }

        public String getConnectionValidationMethod() {
            return null;
        }

        public void setConnectionValidationMethod(String value) throws PropertyVetoException {
            //do nothing
        }

        public String getValidationTableName() {
            return null;
        }

        public void setValidationTableName(String value) throws PropertyVetoException {
            //do nothing
        }

	public String getValidationClassname() {
	    return null;
	}

	public void setValidationClassname(String value) throws PropertyVetoException {
  	    //do nothing
	}

        public String getFailAllConnections() {
            return String.valueOf("false");
        }

        public void setFailAllConnections(String value) throws PropertyVetoException {
            //do nothing
        }

        public String getNonTransactionalConnections() {
            return String.valueOf(!desc.isTransactional());
        }

        public void setNonTransactionalConnections(String value) throws PropertyVetoException {
            //do nothing
        }

        public String getAllowNonComponentCallers() {
            return String.valueOf("false");
        }

        public void setAllowNonComponentCallers(String value) throws PropertyVetoException {
            //do nothing
        }

        public String getValidateAtmostOncePeriodInSeconds() {
            return String.valueOf(0);
        }

        public void setValidateAtmostOncePeriodInSeconds(String value) throws PropertyVetoException {
            //do nothing
        }

        public String getConnectionLeakTimeoutInSeconds() {
            return String.valueOf(0);
        }

        public void setConnectionLeakTimeoutInSeconds(String value) throws PropertyVetoException {
            //do nothing
        }

        public String getConnectionLeakReclaim() {
            return String.valueOf(false);
        }

        public void setConnectionLeakReclaim(String value) throws PropertyVetoException {
            //do nothing
        }

        public String getConnectionCreationRetryAttempts() {
            return String.valueOf(0);
        }

        public void setConnectionCreationRetryAttempts(String value) throws PropertyVetoException {
            //do nothing
        }

        public String getConnectionCreationRetryIntervalInSeconds() {
            return String.valueOf(10);
        }

        public void setConnectionCreationRetryIntervalInSeconds(String value) throws PropertyVetoException {
            //do nothing
        }

        public String getStatementTimeoutInSeconds() {
            return String.valueOf(-1);
        }

        public void setStatementTimeoutInSeconds(String value) throws PropertyVetoException {
            //do nothing
        }

        public String getLazyConnectionEnlistment() {
            return String.valueOf(false);
        }

        public void setLazyConnectionEnlistment(String value) throws PropertyVetoException {
            //do nothing
        }

        public String getLazyConnectionAssociation() {
            return String.valueOf(false);
        }

        public void setLazyConnectionAssociation(String value) throws PropertyVetoException {
            //do nothing
        }

        public String getAssociateWithThread() {
            return String.valueOf(false);
        }

        public void setAssociateWithThread(String value) throws PropertyVetoException {
            //do nothing
        }

        public String getPooling() {
            return String.valueOf(true);
        }

        public void setPooling(String value) throws PropertyVetoException {
            //do nothing
        }

        public String getStatementCacheSize() {
            return String.valueOf(0);
        }

        public void setStatementCacheSize(String value) throws PropertyVetoException {
            //do nothing
        }

        public String getMatchConnections() {
            return String.valueOf(true);
        }

        public void setMatchConnections(String value) throws PropertyVetoException {
            //do nothing
        }

        public String getMaxConnectionUsageCount() {
            return String.valueOf(0);
        }

        public void setMaxConnectionUsageCount(String value) throws PropertyVetoException {
            //do nothing
        }

        public String getWrapJdbcObjects() {
            return String.valueOf(true);
        }

        public void setWrapJdbcObjects(String value) throws PropertyVetoException {
            //do nothing
        }

        public String getDescription() {
            return desc.getDescription();
        }

        public void setDescription(String value) throws PropertyVetoException {
            //do nothing
        }

        public List<Property> getProperty() {
            Properties p = desc.getProperties();
            List<Property> dataSourceProperties = new ArrayList<Property>();
            for (Object o : p.keySet()) {
                String key = (String) o;
                String value = (String) p.get(o);
                DataSourceProperty dp = convertProperty(key, value);
                dataSourceProperties.add(dp);
            }

            if (desc.getUser() != null) {
                DataSourceProperty property = convertProperty(("user"),
                        String.valueOf(desc.getUser()));
                dataSourceProperties.add(property);
            }

            if (desc.getPassword() != null) {
                DataSourceProperty property = convertProperty(("password"),
                        String.valueOf(desc.getPassword()));
                dataSourceProperties.add(property);
            }

            if (desc.getDatabaseName() != null) {
                DataSourceProperty property = convertProperty(("databaseName"),
                        String.valueOf(desc.getDatabaseName()));
                dataSourceProperties.add(property);
            }

            if (desc.getServerName() != null) {
                DataSourceProperty property = convertProperty(("serverName"),
                        String.valueOf(desc.getServerName()));
                dataSourceProperties.add(property);
            }

            if (desc.getPortNumber() != -1) {
                DataSourceProperty property = convertProperty(("portNumber"),
                        String.valueOf(desc.getPortNumber()));
                dataSourceProperties.add(property);
            }

            //process URL only when standard properties are not set
            if (desc.getUrl() != null && !isStandardPropertiesSet(desc)) {
                DataSourceProperty property = convertProperty(("url"),
                        String.valueOf(desc.getUrl()));
                dataSourceProperties.add(property);
            }

            if (desc.getLoginTimeout() != 0) {
                DataSourceProperty property = convertProperty(("loginTimeout"),
                        String.valueOf(desc.getLoginTimeout()));
                dataSourceProperties.add(property);
            }

            if (desc.getMaxStatements() != -1) {
                DataSourceProperty property = convertProperty(("maxStatements"),
                        String.valueOf(desc.getMaxStatements()));
                dataSourceProperties.add(property);
            }

            return dataSourceProperties;
        }

        private boolean isStandardPropertiesSet(DataSourceDefinitionDescriptor desc){
            boolean result = false;
            if(desc.getServerName() != null && desc.getDatabaseName() != null && desc.getPortNumber() != -1 ){
                result = true;
            }
            return result;
        }

        public Property getProperty(String name) {
            String value = (String) desc.getProperties().get(name);
            return new DataSourceProperty(name, value);
        }

        public String getPropertyValue(String name) {
            return (String) desc.getProperties().get(name);
        }

        public String getPropertyValue(String name, String defaultValue) {
            String value = null;
            value = (String) desc.getProperties().get(name);
            if (value != null) {
                return value;
            } else {
                return defaultValue;
            }
        }

        public ConfigBeanProxy getParent() {
            return null;
        }

        public <T extends ConfigBeanProxy> T getParent(Class<T> tClass) {
            return null;
        }

        public <T extends ConfigBeanProxy> T createChild(Class<T> tClass) throws TransactionFailure {
            return null;
        }

        public void injectedInto(Object o) {
            //do nothing
        }

        public String getName() {
            return name;
        }

        public void setName(String value) throws PropertyVetoException {
            //do nothing
        }

        public String getSqlTraceListeners() {
            return null;
        }

        public void setSqlTraceListeners(String value) throws PropertyVetoException {
            //do nothing
        }

        public String getPing() {            
            return String.valueOf(false);
        }

        public void setPing(String value) throws PropertyVetoException {
            //do nothing
        }

        public String getInitSql() {
            return null;
        }

        public void setInitSql(String value) throws PropertyVetoException {
            //do nothing
        }

        public String getDriverClassname() {
            return desc.getClassName();
        }

        public void setDriverClassname(String value) throws PropertyVetoException {
            //do nothing
        }
    }
}