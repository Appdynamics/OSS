/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010-2011 Oracle and/or its affiliates. All rights reserved.
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

package org.glassfish.javaee.services;

import org.glassfish.api.ActionReport;
import org.glassfish.api.admin.CommandRunner;
import org.glassfish.api.admin.ParameterMap;
import org.glassfish.resource.common.PoolInfo;
import org.glassfish.resource.common.ResourceInfo;
import org.jvnet.hk2.annotations.Scoped;
import org.jvnet.hk2.annotations.Service;
import org.jvnet.hk2.annotations.Inject;
import org.jvnet.hk2.component.*;
import org.jvnet.hk2.config.*;
import org.glassfish.internal.api.*;

import java.beans.PropertyChangeEvent;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.*;

import com.sun.enterprise.config.serverbeans.*;
import com.sun.appserv.connectors.internal.api.ConnectorsUtil;
import com.sun.appserv.connectors.internal.api.ConnectorRuntime;
import com.sun.appserv.connectors.internal.spi.ResourceDeployer;
import com.sun.logging.LogDomains;
import org.jvnet.hk2.config.ObservableBean;


/**
 * Resource manager to bind various application or module scoped resources during
 * startup, create/update/delete of resource/pool
 * @author Jagadish Ramu
 */
@Scoped(Singleton.class)
@Service(name="ApplicationScopedResourcesManager")
public class ApplicationScopedResourcesManager implements PostStartup, PostConstruct, PreDestroy, ConfigListener {

    private static final Logger _logger = LogDomains.getLogger(ResourceManager.class,LogDomains.RESOURCE_BUNDLE);

    @Inject
    private ResourcesBinder resourcesBinder;

    @Inject
    private Habitat deployerHabitat;

    @Inject
    private Habitat habitat;

    @Inject
    private Habitat connectorRuntimeHabitat;

    @Inject
    private Applications applications;

    ConnectorRuntime runtime;


    public void postConstruct() {
        Collection<Application> apps = applications.getApplications();
        if(apps != null){
            for(Application app : apps){
                Resources resources = app.getResources();
                if(resources != null){
                    deployResources(resources.getResources());
                }
                List<Module> modules = app.getModule();
                if(modules != null){
                    for(Module module : modules){
                        Resources moduleResources = module.getResources();
                        if(moduleResources != null){
                            deployResources(moduleResources.getResources());
                        }
                    }
                }
            }
        }
    }

    public Resources getResources(String applicationName){
        Application app = applications.getApplication(applicationName);
        if(app != null){
            return app.getResources();
        }
        return null;
    }

    public void deployResources(String applicationName){
        Application app = applications.getApplication(applicationName);
        if(app != null){
            Resources appScopedResources = app.getResources();
            if(appScopedResources != null){
                deployResources(appScopedResources.getResources());
            }

            List<Module> modules = app.getModule();
            if(modules != null){
                for(Module module : modules){
                    Resources moduleScopedResources = module.getResources();
                    if(moduleScopedResources != null){
                        deployResources(moduleScopedResources.getResources());
                    }
                }
            }
        }

    }

    /**
     * deploy resources
     * @param resources list
     */
    public void deployResources(Collection<Resource> resources) {
        if (resources != null) {
            String applicationName = null;
            String moduleName = null;

            for (Resource resource : resources) {
                Object parentObject = resource.getParent().getParent();
                if(parentObject instanceof Application){
                    applicationName = ((Application)parentObject).getName();
                }else if(parentObject instanceof Module){
                    moduleName = ((Module)parentObject).getName();
                    applicationName = ((Application)((Module)parentObject).getParent()).getName();
                }

                if (resource instanceof BindableResource) {
                    BindableResource bindableResource = (BindableResource) resource;
                    ResourceInfo resourceInfo =
                            new ResourceInfo(bindableResource.getJndiName(), applicationName, moduleName);
                    resourcesBinder.deployResource(resourceInfo, resource);
                } else if (resource instanceof ResourcePool) {
                    // ignore, as they are loaded lazily
                } else {
                    // only other resources left are RAC, CWSM
                    try {
                        getResourceDeployer(resource).deployResource(resource, applicationName, moduleName);
                    } catch (Exception e) {
                        Object[] params = {ConnectorsUtil.getGenericResourceInfo(resource), e};
                        _logger.log(Level.WARNING, "resources.resource-manager.deploy-resource-failed", params);
                    }
                }
            }
            addListenerToResources(resources);
        }
    }
    /**
     * Check whether connector-runtime is initialized.
     * @return boolean representing connector-runtime initialization status.
     */
    public boolean isConnectorRuntimeInitialized() {
        Collection<Inhabitant<? extends ConnectorRuntime>> inhabitants =
                connectorRuntimeHabitat.getInhabitants(ConnectorRuntime.class);
        for(Inhabitant inhabitant : inhabitants){
            // there will be only one implementation of connector-runtime
            return inhabitant.isInstantiated();
        }
        return true; // to be safe
    }


    /**
     * Do cleanup of system-resource-adapter, resources, pools
     */
    public void preDestroy() {
        if (isConnectorRuntimeInitialized()) {
            Collection<Application> apps = applications.getApplications();
            if(apps != null){
                for(Application app : apps){
                    Resources resources = app.getResources();
                    if(resources != null){
                        undeploySystemResources(resources);
                    }
                    List<Module> modules = app.getModule();
                    if(modules != null){
                        for(Module module : modules){
                            Resources moduleScopedResources = module.getResources();
                            if(moduleScopedResources != null){
                                undeploySystemResources(moduleScopedResources);
                            }
                        }
                    }
                }
            }
        } else {
            if(_logger.isLoggable(Level.FINEST)){
                _logger.finest("ConnectorRuntime not initialized, hence skipping " +
                        "resource-adapters shutdown, resources, pools cleanup");
            }
        }
    }

    public void undeployResources(String applicationName){
        Application app = applications.getApplication(applicationName);
        if(app != null){
            List<Module> modules = app.getModule();
            if(modules != null){
                for(Module module : modules){
                    Resources moduleScopedResources = module.getResources();
                    if(moduleScopedResources != null){
                        undeployResources(moduleScopedResources);
                    }
                }
            }
            Resources appScopedResources = app.getResources();
            if(appScopedResources != null){
                undeployResources(appScopedResources);
            }
        }
    }

    private void undeploySystemResources(Resources resources) {
        Collection<Resource> systemResources =
                ConnectorsUtil.getAllSystemRAResourcesAndPools(resources);
        for(Resource resource :  systemResources){
            undeployResource(resource);
        }
    }

    public void undeployResources(Collection<Resource> resources){
        for(Resource resource : resources){
            undeployResource(resource);
        }
    }


    /**
     * undeploy the given set of resources<br>
     * resources (bindable) are removed first and then the pools
     * @param resources list of resources
     */
    public void undeployResources(Resources resources){

        for(Resource resource : resources.getResources()){
            //destroy all resources first and then pools
            if(!(resource instanceof ResourcePool) && !(resource instanceof ResourceAdapterConfig)){
            // no need to undeploy resource-adapter-config as it (config) will be removed by the end of undeploy
            // operation of the application.
                undeployResource(resource);
            }
        }
        Collection<ResourcePool> pools = resources.getResources(ResourcePool.class);
        for(ResourcePool pool : pools){
            undeployResource(pool);
        }
    }

    private void undeployResource(Resource resource){
        try{
            getResourceDeployer(resource).undeployResource(resource);
        }catch(Exception e){
            Object[] params = {ConnectorsUtil.getGenericResourceInfo(resource), e};
            _logger.log(Level.WARNING, "resources.resource-manager.undeploy-resource-failed", params);
        }finally{
            removeListenerFromResource(resource);
        }
    }

    private void addListenerToResources(Collection<Resource> resources){
        if(resources != null){
            for(Resource resource : resources){
                addListenerToResource(resource);
            }
        }
    }

    private void addListenerToResource(Resource instance) {
        ObservableBean bean = null;
        if(_logger.isLoggable(Level.FINEST)){
            debug("adding listener : " + instance);
        }
        bean = (ObservableBean) ConfigSupport.getImpl((ConfigBeanProxy)instance);
        bean.addListener(this);
    }

    /**
     * Remove listener from all resources - JDBC Connection Pools/JDBC Resources/
     * Connector Connection Pools/ Connector Resources.
     * Invoked from preDestroy()
     */
    private void removeListenerFromResources(Resources resources) {
        if(resources != null){
            for (Resource configuredResource : resources.getResources()) {
                removeListenerFromResource(configuredResource);
            }
        }
    }

    /**
     * Remove listener from a generic resource (JDBC Connection Pool/Connector
     * Connection Pool/JDBC resource/Connector resource)
     * Used in the case of delete asadmin command
     * @param instance remove the resource from listening to resource events
     */
    private void removeListenerFromResource(Resource instance) {
        ObservableBean bean = null;
        debug("removing listener : " + instance);
        bean = (ObservableBean) ConfigSupport.getImpl((ConfigBeanProxy)instance);
        bean.removeListener(this);
    }

    /**
     * Given a <i>resource</i> instance, appropriate deployer will be provided
     *
     * @param resource resource instance
     * @return ResourceDeployer
     */
    private ResourceDeployer getResourceDeployer(Object resource){
        Collection<ResourceDeployer> deployers = deployerHabitat.getAllByContract(ResourceDeployer.class);

        for(ResourceDeployer deployer : deployers){
            if(deployer.handles(resource)){
                return deployer;
            }
        }
        return null;
    }

    private ConnectorRuntime getConnectorRuntime() {
        if(runtime == null){
            runtime = habitat.getComponent(ConnectorRuntime.class, null);
        }
        return runtime;
    }

    private void  debug(String message){
        if (_logger.isLoggable(Level.FINEST)) {
            _logger.finest("[ASR] [ApplicationScopedResourcesManager] " + message);
        }
     }

    /**
     * Notification that @Configured objects that were injected have changed
     *
     * @param events list of changes
     */
    public UnprocessedChangeEvents changed(PropertyChangeEvent[] events) {
        return ConfigSupport.sortAndDispatch(events, new ConfigChangeHandler(events), _logger);
    }

    class ConfigChangeHandler implements Changed {

        //TODO no need to store events ?
        PropertyChangeEvent[] events;

        private ConfigChangeHandler(PropertyChangeEvent[] events) {
            this.events = events;
        }

        /**
         * Notification of a change on a configuration object
         *
         * @param type            CHANGE means the changedInstance has mutated.
         * @param changedType     type of the configuration object
         * @param changedInstance changed instance.
         */
        public <T extends ConfigBeanProxy> NotProcessed changed(Changed.TYPE type, Class<T> changedType,
                                                                T changedInstance) {
            NotProcessed np ;
            ClassLoader contextCL = Thread.currentThread().getContextClassLoader();
            try {
                //use connector-class-loader so as to get access to classes from resource-adapters
                ClassLoader ccl = getConnectorRuntime().getConnectorClassLoader();
                Thread.currentThread().setContextClassLoader(ccl);
                switch (type) {
                    case ADD:
                        np = handleAddEvent(changedInstance);
                        break;
                    case CHANGE:
                        np = handleChangeEvent(changedInstance);
                        break;
                    case REMOVE:
                        np = handleRemoveEvent(changedInstance);
                        break;

                    default:
                        np = new NotProcessed("Unrecognized type of change: " + type);
                        break;
                }
                return np;
            } finally {
                Thread.currentThread().setContextClassLoader(contextCL);
            }
        }
        private <T extends ConfigBeanProxy> NotProcessed handleAddEvent(T instance) {
            NotProcessed np = null;
            if(instance instanceof Application){
                Resources resources = ((Application)instance).getResources();
                if(resources != null){
                    addListenerToResources(resources.getResources());
                    pingConnectionPool(resources);
                }

                Application app = (Application)instance;
                List<Module> modules = app.getModule();
                if(modules != null){
                    for(Module module : modules){
                        if(module.getResources() !=null && module.getResources().getResources() != null){
                            addListenerToResources(module.getResources().getResources());
                            pingConnectionPool(module.getResources());
                        }
                    }
                }
            }
            return np;
        }

        private <T extends ConfigBeanProxy> NotProcessed handleRemoveEvent(T instance) {
            NotProcessed np = null;
            if(instance instanceof Application){
                removeListenerFromResources(((Application)instance).getResources());

                Application app = (Application)instance;
                List<Module> modules = app.getModule();
                if(modules != null){
                    for(Module module : modules){
                        if(module.getResources() !=null && module.getResources().getResources() != null){
                            removeListenerFromResources(module.getResources());
                        }
                    }
                }
            }
            return np;
        }


        private <T extends ConfigBeanProxy> NotProcessed handleChangeEvent(T instance) {
            NotProcessed np = null;
            //TODO V3 handle enabled / disabled /redeploy ?
            debug("handling change event");
            try {
                if (ConnectorsUtil.isValidEventType(instance)) {
                    if(_logger.isLoggable(Level.FINEST)){
                        debug("redeploying resource : " + instance);
                    }
                    getResourceDeployer(instance).redeployResource(instance);
                } else if (ConnectorsUtil.isValidEventType(instance.getParent())) {
                    if(_logger.isLoggable(Level.FINEST)){
                        debug("redeploying resource due to property change : " + instance.getParent());
                    }
                    //Added in case of a property change
                    //check for validity of the property's parent and redeploy
                    getResourceDeployer(instance.getParent()).redeployResource(instance.getParent());
                }
            } catch (Exception ex) {
                final String msg = ApplicationScopedResourcesManager.class.getName() + " : " +
                        "Error while handling change Event";
                _logger.severe(msg);
                np = new NotProcessed(msg);
            }
            return np;
        }
    }

    private void pingConnectionPool(Resources resources) {
        if(resources != null){
            if(resources.getResources() != null){
                for(Resource resource : resources.getResources()){
                    if(resource instanceof ResourcePool){
                        ResourcePool pool = (ResourcePool)resource;
                        boolean ping = false;
                        if(pool instanceof JdbcConnectionPool){
                            ping = Boolean.valueOf(((JdbcConnectionPool)pool).getPing());
                        }else if (pool instanceof ConnectorConnectionPool) {
                            ping = Boolean.valueOf(((ConnectorConnectionPool)pool).getPing());
                        }
                        if(ping){
                            PoolInfo poolInfo = ConnectorsUtil.getPoolInfo(pool);
                            CommandRunner commandRunner = habitat.getComponent(CommandRunner.class);
                            ActionReport report = habitat.getComponent(ActionReport.class);
                            CommandRunner.CommandInvocation invocation =
                                    commandRunner.getCommandInvocation("ping-connection-pool", report);
                            ParameterMap params = new ParameterMap();
                            params.add("appname",poolInfo.getApplicationName());
                            params.add("modulename",poolInfo.getModuleName());
                            params.add("DEFAULT", poolInfo.getName());
                            invocation.parameters(params).execute();
                            if(report.getActionExitCode() == ActionReport.ExitCode.SUCCESS){
                                _logger.log(Level.INFO, "app-scoped.ping.connection.pool.success", poolInfo);
                            }else{
                                Object args[] = new Object[]{poolInfo, report.getFailureCause()};
                                _logger.log(Level.WARNING, "app-scoped.ping.connection.pool.failed", args);
                            }
                        }
                    }
                }
            }
        }
    }
}
