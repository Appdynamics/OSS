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

package com.sun.enterprise.connectors.service;

import com.sun.appserv.connectors.internal.api.ConnectorConstants;
import com.sun.appserv.connectors.internal.api.ConnectorRuntimeException;
import com.sun.enterprise.connectors.ConnectorRegistry;
import com.sun.enterprise.connectors.DeferredResourceConfig;
import com.sun.enterprise.connectors.util.ConnectorDDTransformUtils;
import com.sun.appserv.connectors.internal.api.ConnectorsUtil;
import com.sun.enterprise.connectors.ConnectorRuntime;
import com.sun.enterprise.connectors.ActiveResourceAdapter;
import com.sun.enterprise.connectors.util.ResourcesUtil;
import com.sun.enterprise.deployment.*;
import com.sun.enterprise.resource.pool.PoolManager;
import com.sun.enterprise.config.serverbeans.*;
import com.sun.enterprise.util.io.FileUtils;
import com.sun.logging.LogDomains;
import org.jvnet.hk2.config.ConfigBeanProxy;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;


/**
 * This is the base class for all the connector services. It defines the
 * enviroment of execution (client or server), and holds the reference to
 * connector runtime for inter service method invocations.
 *
 * @author Srikanth P
 */
public class ConnectorService implements ConnectorConstants {
    protected static final Logger _logger = LogDomains.getLogger(ConnectorService.class, LogDomains.RSR_LOGGER);

    protected static final ConnectorRegistry _registry =
            ConnectorRegistry.getInstance();

    protected ConnectorRuntime _runtime;

    /**
     * Default Constructor
     */
    public ConnectorService() {
        _runtime = ConnectorRuntime.getRuntime();
    }

    /**
     * Returns the generated default connection poolName for a
     * connection definition.
     *
     * @param moduleName        rar module name
     * @param connectionDefName connection definition name
     * @return generated connection poolname
     */
    // TODO V3 can the default pool name generation be fully done by connector-admin-service-utils ?
    public String getDefaultPoolName(String moduleName,
                                     String connectionDefName) {
        return moduleName + POOLNAME_APPENDER + connectionDefName;
    }

    /**
     * Returns the generated default connector resource for a
     * connection definition.
     *
     * @param moduleName        resource-adapter name
     * @param connectionDefName connection definition name
     * @return generated default connector resource name
     */
    // TODO V3 can the default resource name generation be fully done by connector-admin-service-utils ?
    public String getDefaultResourceName(String moduleName,
                                         String connectionDefName) {
        //Construct the default resource name as
        // <JNDIName_of_RA>#<connectionDefnName>
        String resourceJNDIName = ConnectorAdminServiceUtils.getReservePrefixedJNDINameForResource(moduleName);
        return resourceJNDIName + RESOURCENAME_APPENDER + connectionDefName;
    }

    public boolean checkAndLoadResource(Object resource, Object pool, String resourceType, String resourceName,
                                        String raName)
            throws ConnectorRuntimeException {
        String resname = ConnectorAdminServiceUtils.getOriginalResourceName(resourceName);
        _logger.fine("ConnectorService :: checkAndLoadResource resolved to load " + resname);

        ResourcesUtil resUtil = ResourcesUtil.createInstance();
        DeferredResourceConfig defResConfig = resUtil.getDeferredResourceConfig(resource, pool, resourceType, raName);
        return loadResourcesAndItsRar(defResConfig);
    }

    public boolean loadResourcesAndItsRar(DeferredResourceConfig defResConfig) {
        if (defResConfig != null) {
            try {
                loadDeferredResources(defResConfig.getResourceAdapterConfig());
                final String rarName = defResConfig.getRarName();
                loadDeferredResourceAdapter(rarName);
                final ConfigBeanProxy[] resToLoad = defResConfig.getResourcesToLoad();
                AccessController.doPrivileged(new PrivilegedAction() {
                    public Object run() {
                        try {
                            loadDeferredResources(resToLoad);
                        } catch (Exception ex) {
                            Object params[] = new Object[]{rarName, ex};
                            _logger.log(Level.SEVERE, "failed.to.load.deferred.resources", params);
                        }
                        return null;
                    }
                });
            } catch (Exception ex) {
                Object params[] = new Object[]{defResConfig.getRarName(), ex};
                _logger.log(Level.SEVERE, "failed.to.load.deferred.ra", params);
                return false;
            }
            return true;
        }
        return false;
    }


    public void loadDeferredResourceAdapter(String rarModuleName)
            throws ConnectorRuntimeException {
        //load the RA if its not already loaded
        if (_registry.getActiveResourceAdapter(rarModuleName) == null) {
            try {
                //Do this only for System RA
                if (ConnectorsUtil.belongsToSystemRA(rarModuleName)) {
                    _runtime.createActiveResourceAdapter(ConnectorsUtil.getSystemModuleLocation(rarModuleName), rarModuleName, null);
                } /* not needed as long as standalone + embedded rars are loaded before recovery
                else if (rarModuleName.indexOf(ConnectorConstants.EMBEDDEDRAR_NAME_DELIMITER) != -1) {
                    createActiveResourceAdapterForEmbeddedRar(rarModuleName);
                } else{
                    _runtime.createActiveResourceAdapter(ConnectorsUtil.getLocation(rarModuleName), rarModuleName, null);
                }*/
            } catch (Exception e) {
                ConnectorRuntimeException ce =
                        new ConnectorRuntimeException(e.getMessage());
                ce.initCause(e);
                throw ce;
            }
        }
    }

    public void createActiveResourceAdapterForEmbeddedRar(String rarModuleName) throws ConnectorRuntimeException {
        ConnectorDescriptor cdesc = loadConnectorDescriptorForEmbeddedRAR(rarModuleName);
        String appName = ConnectorAdminServiceUtils.getApplicationName(rarModuleName);
        String rarFileName = ConnectorAdminServiceUtils
                        .getConnectorModuleName(rarModuleName) + ".rar";
        String loc = ResourcesUtil.createInstance().getApplicationDeployLocation(appName);
        loc = loc + File.separator + FileUtils.makeFriendlyFilename(rarFileName);

        String path = null;
        try {
            URI uri = new URI(loc);
            path = uri.getPath();
        } catch (URISyntaxException use) {
            ConnectorRuntimeException cre = new ConnectorRuntimeException("Invalid path [ "+use.getMessage()+" ]");
            cre.setStackTrace(use.getStackTrace());
            _logger.log(Level.WARNING, cre.getMessage(), cre);
            throw cre;
        }
        // start RA
        _runtime.createActiveResourceAdapter(cdesc, rarModuleName, path);
    }


    public void loadDeferredResources(ConfigBeanProxy[] resourcesToLoad)
            throws Exception {
        if (resourcesToLoad == null || resourcesToLoad.length == 0) {
            return;
        }
        for (ConfigBeanProxy resource : resourcesToLoad) {
            if (resource == null) {
                continue;
            } else /* TODO V3 handle later once configBeans (resource.isEnabled()) is available
                if (resourceUtil.isEnabled(resource))*/ {
                try {
                    _runtime.getResourceDeployer(resource).deployResource(resource);
                } catch (Exception e) {
                    ConnectorRuntimeException cre = new ConnectorRuntimeException(e.getMessage());
                    cre.initCause(e);
                    throw cre;
                }
            }
        }
    }

    /**
     * Obtains the connector Descriptor pertaining to rar.
     * If ConnectorDescriptor is present in registry, it is obtained from
     * registry and returned. Else it is explicitly read from directory
     * where rar is exploded.
     *
     * @param rarName Name of the rar
     * @return ConnectorDescriptor pertaining to rar.
     * @throws ConnectorRuntimeException when unable to get descriptor
     */
    public ConnectorDescriptor getConnectorDescriptor(String rarName)
            throws ConnectorRuntimeException {

        if (rarName == null) {
            return null;
        }
        ConnectorDescriptor desc = null;
        desc = _registry.getDescriptor(rarName);
        if (desc != null) {
            return desc;
        }
        String moduleDir;

        //If the RAR is embedded try loading the descriptor directly
        //using the applicationarchivist
        if (rarName.indexOf(ConnectorConstants.EMBEDDEDRAR_NAME_DELIMITER) != -1){
            try {
                desc = loadConnectorDescriptorForEmbeddedRAR(rarName);
                if (desc != null) return desc;
            } catch (ConnectorRuntimeException e) {
                throw e;
            }
        }

        if (ConnectorsUtil.belongsToSystemRA(rarName)) {
            moduleDir = ConnectorsUtil.getSystemModuleLocation(rarName);
        } else {
            moduleDir = ConnectorsUtil.getLocation(rarName);
        }
        if (moduleDir != null) {
            desc = ConnectorDDTransformUtils.getConnectorDescriptor(moduleDir, rarName);
        } else {
            _logger.log(Level.SEVERE,
                    "rardeployment.no_module_deployed", rarName);
        }
        return desc;
    }


    /**
     * Matching will be switched off in the pool, by default. This will be
     * switched on if the connections with different resource principals reach the pool.
     *
     * @param poolName Name of the pool to switchOn matching.
     * @param rarName  Name of the resource adater.
     */
    public void switchOnMatching(String rarName, String poolName) {
        // At present it is applicable to only JDBC resource adapters
        // Later other resource adapters also become applicable.
        if (rarName.equals(ConnectorConstants.JDBCDATASOURCE_RA_NAME)
                || rarName.equals(ConnectorConstants.JDBCCONNECTIONPOOLDATASOURCE_RA_NAME) ||
                rarName.equals(ConnectorConstants.JDBCXA_RA_NAME)) {

            PoolManager poolMgr = _runtime.getPoolManager();
            boolean result = poolMgr.switchOnMatching(poolName);
            if (!result) {
                try {
                    _runtime.switchOnMatchingInJndi(poolName);
                } catch (ConnectorRuntimeException cre) {
                    // This will never happen.
                }
            }
        }
    }

    public boolean checkAndLoadPool(String poolName) {
        boolean status = false;
        try {
            ResourcesUtil resutil = ResourcesUtil.createInstance();
            ResourcePool pool = _runtime.getConnectionPoolConfig(poolName);
            DeferredResourceConfig defResConfig =
                    resutil.getDeferredResourceConfig(null, pool, ConnectorsUtil.getResourceType(pool), null);

            status = loadResourcesAndItsRar(defResConfig);
        } catch (ConnectorRuntimeException cre) {
            Object params[] = new Object[]{poolName, cre};
            _logger.log(Level.WARNING, "unable.to.load.connection.pool", params);
        }
        return status;
    }

/*
    public boolean checkAndLoadJdbcPool(String poolName) {
        boolean status = false;
        try {
            ResourcesUtil resutil = ResourcesUtil.createInstance();
            JdbcConnectionPool pool = _runtime.getJdbcConnectionPoolConfig(poolName);
            DeferredResourceConfig defResConfig =
                    resutil.getDeferredResourceConfig(null, pool, ConnectorConstants.RES_TYPE_JDBC, null);

            status = loadResourcesAndItsRar(defResConfig);
        } catch (ConnectorRuntimeException cre) {
            _logger.log(Level.WARNING, "unable to load Jdbc Connection Pool [ " + poolName + " ]", cre);
        }
        return status;
    }
*/

    public void ifSystemRarLoad(String rarName)
                           throws ConnectorRuntimeException {
        if(ConnectorsUtil.belongsToSystemRA(rarName)){
            loadDeferredResourceAdapter(rarName);
        }
    }

    //TODO V3 with annotations, is it right a approach to load the descriptor using Archivist ?
    private ConnectorDescriptor loadConnectorDescriptorForEmbeddedRAR(String rarName) throws ConnectorRuntimeException {
        //If the RAR is embedded try loading the descriptor directly
        //using the applicationarchivist
        ResourcesUtil resutil = ResourcesUtil.createInstance();
        String rarFileName = ConnectorAdminServiceUtils.getConnectorModuleName(rarName) + ".rar";
        return resutil.getConnectorDescriptorFromUri(rarName, rarFileName);
    }

    /**
     * Check whether ClassLoader is permitted to access this resource adapter.
     * If the RAR is deployed and is not a standalone RAR, then only the ClassLoader
     * that loaded the archive (any of its child) should be able to access it. Otherwise everybody can
     * access the RAR.
     *
     * @param rarName Resource adapter module name.
     * @param loader  <code>ClassLoader</code> to verify.
     */
    public boolean checkAccessibility(String rarName, ClassLoader loader) {
        ActiveResourceAdapter ar = _registry.getActiveResourceAdapter(rarName);
        if (ar != null && loader != null) { // If RA is deployed

            ClassLoader rarLoader = ar.getClassLoader();

            //If the RAR is not standalone.
            if (rarLoader != null && ConnectorAdminServiceUtils.isEmbeddedConnectorModule(rarName)
                /*&& (!(rarLoader instanceof ConnectorClassFinder))*/) {
                ClassLoader rarLoaderParent = rarLoader.getParent();
                ClassLoader parent = loader;
                while (true) {
                    if (parent.equals(rarLoaderParent)) {
                        return true;
                    }

                    final ClassLoader temp = parent;
                    Object obj = AccessController.doPrivileged(new PrivilegedAction() {
                        public Object run() {
                            return temp.getParent();
                        }
                    });

                    if (obj == null) {
                        break;
                    } else {
                        parent = (ClassLoader) obj;
                    }
                }
                // If no parent matches return false;
                return false;
            }
        }
        return true;
    }
}
