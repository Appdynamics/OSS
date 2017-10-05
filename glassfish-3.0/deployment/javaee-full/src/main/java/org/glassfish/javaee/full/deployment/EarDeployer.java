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
package org.glassfish.javaee.full.deployment;

import org.glassfish.api.deployment.*;
import org.glassfish.api.deployment.archive.ReadableArchive;
import org.glassfish.api.container.Container;
import org.glassfish.api.ActionReport;
import org.glassfish.api.event.Events;
import org.glassfish.api.admin.*;
import org.glassfish.api.container.Sniffer;
import org.glassfish.internal.deployment.Deployment;
import org.glassfish.internal.deployment.ExtendedDeploymentContext;
import org.glassfish.internal.data.*;
import org.glassfish.internal.deployment.SnifferManager;
import org.glassfish.deployment.common.DeploymentContextImpl;
import org.glassfish.deployment.common.DeploymentUtils;
import org.jvnet.hk2.annotations.Service;
import org.jvnet.hk2.annotations.Inject;
import org.jvnet.hk2.annotations.Scoped;
import org.jvnet.hk2.component.Habitat;
import org.jvnet.hk2.component.PerLookup;
import com.sun.enterprise.deployment.*;
import com.sun.enterprise.deployment.util.ModuleDescriptor;
import com.sun.enterprise.deployment.util.XModuleType;
import com.sun.enterprise.deploy.shared.ArchiveFactory;
import com.sun.enterprise.deployment.deploy.shared.Util;
import com.sun.logging.LogDomains;

import java.util.*;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.io.IOException;
import java.io.File;
import org.glassfish.deployment.common.DeploymentException;
import org.glassfish.deployment.common.DummyApplication;

/**
 * EarDeployer to deploy composite Java EE applications.
 * todo : could be generified into any composite applications.
 *
 * @author Jerome Dochez
 */
@Service
@Scoped(PerLookup.class)
public class EarDeployer implements Deployer {

//    private static final Class GLASSFISH_APPCLIENT_GROUP_FACADE_CLASS =
//            org.glassfish.appclient.client.AppClientGroupFacade.class;
// Currently using a string instead of a Class constant to avoid a circular
// dependency.  
    @Inject
    Habitat habitat;

    @Inject
    Deployment deployment;

    @Inject
    ServerEnvironment env;

    @Inject
    ApplicationRegistry appRegistry;

    @Inject
    protected SnifferManager snifferManager;

    @Inject
    ArchiveFactory archiveFactory;

    @Inject
    Events events;

    final static Logger logger = LogDomains.getLogger(DeploymentUtils.class, LogDomains.DPL_LOGGER);
    
    public MetaData getMetaData() {
        return new MetaData(false, null, new Class[] { Application.class});
    }

    public Object loadMetaData(Class type, DeploymentContext context) {
        return null;
    }

    public boolean prepare(final DeploymentContext context) {

        final Application application = context.getModuleMetaData(Application.class);

        DeployCommandParameters deployParams = context.getCommandParameters(DeployCommandParameters.class);
        final String appName = deployParams.name();
        
        final ApplicationInfo appInfo = new CompositeApplicationInfo(events, application, context.getSource(), appName);
        for (Object m : context.getModuleMetadata()) {
            appInfo.addMetaData(m);
        }

        try {
            doOnAllBundles(application, new BundleBlock<ModuleInfo>() {
                public ModuleInfo doBundle(ModuleDescriptor bundle) throws Exception {
                    ModuleInfo info = prepareBundle(bundle, application, subContext(application, context, bundle.getArchiveUri()));
                    info.addMetaData(application);
                    BundleDescriptor bundleDesc = application.getModuleByUri(
                        bundle.getArchiveUri());
                    info.addMetaData(bundleDesc);
                    for (RootDeploymentDescriptor ext : 
                        bundleDesc.getExtensionsDescriptors()) {
                        info.addMetaData(ext);
                    }
                    appInfo.addModule(info);
                    return info;
                }

            });
        } catch(DeploymentException dde) {
            throw dde;
        } catch(Exception e) {
            DeploymentException de = new DeploymentException(e.getMessage());
            de.initCause(e);
            throw de;
        }

        context.addModuleMetaData(appInfo);
        generateArtifacts(context);
        return true;
    }

    protected void generateArtifacts(final DeploymentContext context) throws DeploymentException {
        /*
         * The EAR-level app client artifact - the app client group facade -
         * is generated the first time an app client facade is generated.  At
         * that time all other deployers have run so any client artifacts
         * they create will be available and can be packaged into the group
         * facade.
         */
    }
        
    
    private class CompositeApplicationInfo extends ApplicationInfo {

        final Application application;

        private CompositeApplicationInfo(Events events, Application application, ReadableArchive source, String name) {
            super(events, source, name);
            this.application = application;
        }

        @Override
        protected ExtendedDeploymentContext getSubContext(ModuleInfo module, ExtendedDeploymentContext context) {
            return subContext(application, context, module.getName());
        }

     }


    /**
     * Performs the same runnable task on each specified bundle.
     *
     * @param bundles the bundles on which to perform the task
     * @param runnable the task to perform
     * @throws Exception
     */
    private void doOnBundles(
            final Collection<ModuleDescriptor<BundleDescriptor>> bundles,
            final BundleBlock runnable) throws Exception {
        for (ModuleDescriptor module : bundles) {
            runnable.doBundle(module);
        }
    }

    private Collection<ModuleDescriptor<BundleDescriptor>>
                doOnAllTypedBundles(Application application, XModuleType type, BundleBlock runnable)
                    throws Exception {

        final Collection<ModuleDescriptor<BundleDescriptor>> typedBundles = application.getModuleDescriptorsByType(type);
        doOnBundles(typedBundles, runnable);
        return typedBundles;
    }

    private void doOnAllBundles(Application application, BundleBlock runnable) throws Exception {

        Collection<ModuleDescriptor> bundles = 
            new LinkedHashSet<ModuleDescriptor>();
        bundles.addAll(application.getModules());

        // if the initialize-in-order flag is set
        // we load the modules by their declaration order in application.xml
        if (application.isInitializeInOrder()) {
            for (final ModuleDescriptor bundle : bundles) {
                runnable.doBundle(bundle);
            }
        }
        
        // otherwise we load modules by default order: connector, ejb, web and
        // saving app client for last (because other submodules might generated
        // artifacts that should be included in the generated app client JAR
        else {
            // first we take care of the connectors
            bundles.removeAll(doOnAllTypedBundles(application, XModuleType.RAR, runnable));

            // now the EJBs
            bundles.removeAll(doOnAllTypedBundles(application, XModuleType.EJB, runnable));

            // finally the war files.
            bundles.removeAll(doOnAllTypedBundles(application, XModuleType.WAR, runnable));

            // extract the app client bundles to take care of later
            Collection<ModuleDescriptor<BundleDescriptor>> appClientBundles = 
                    application.getModuleDescriptorsByType(XModuleType.CAR);
            bundles.removeAll(appClientBundles);
            
            // now ther remaining bundles
            for (final ModuleDescriptor bundle : bundles) {
                runnable.doBundle(bundle);
            }

            // Last, deal with the app client bundles
            doOnBundles(appClientBundles, runnable);
        } 
    }

    private ModuleInfo prepareBundle(final ModuleDescriptor md, Application application, final ExtendedDeploymentContext bundleContext)
        throws Exception {

        List<EngineInfo> orderedContainers = null;

        ProgressTracker tracker = bundleContext.getTransientAppMetaData(ExtendedDeploymentContext.TRACKER, ProgressTracker.class);

        try {
            // let's get the list of sniffers
            Collection<Sniffer> sniffers = 
                getSniffersForModule(bundleContext, md, application);
            // let's get the list of containers interested in this module
            orderedContainers = deployment.setupContainerInfos(null, sniffers, bundleContext);
        } catch(Exception e) {
            logger.log(Level.WARNING, "Error occurred", e);  
            throw e;
        }
        return deployment.prepareModule(orderedContainers, md.getArchiveUri(), bundleContext, tracker);
    }

    public ApplicationContainer load(Container container, DeploymentContext context) {

        return new DummyApplication();
    }

    public void unload(ApplicationContainer appContainer, DeploymentContext context) {
        // nothing to do
    }

    public void clean(DeploymentContext context) {
        // nothing to do
    }

    private interface BundleBlock<T> {

        public T doBundle(ModuleDescriptor bundle) throws Exception;
    }
    
    private ExtendedDeploymentContext subContext(final Application application, final DeploymentContext context, final String moduleUri) {
                
                ExtendedDeploymentContext moduleContext = ((ExtendedDeploymentContext)context).getModuleDeploymentContexts().get(moduleUri);
                if (moduleContext != null) {
                    return moduleContext;
                }


                final ReadableArchive subArchive;
                try {
                    subArchive = context.getSource().getSubArchive(moduleUri);
                    subArchive.setParentArchive(context.getSource());
                } catch(IOException ioe) {
                    logger.log(Level.WARNING, "Error occurred", ioe);  
                    return null;
                }
                
                final Properties moduleProps = 
                    getModuleProps(context, moduleUri);

                ActionReport subReport = 
                    context.getActionReport().addSubActionsReport();
                moduleContext = new DeploymentContextImpl(subReport, logger, context.getSource(),
                        context.getCommandParameters(OpsParams.class), env) {

                    @Override
                    public ClassLoader getClassLoader() {
                        try {
                            if (context.getClassLoader() == null) { 
                                return null;
                            }
                            EarClassLoader appCl = EarClassLoader.class.cast(context.getClassLoader());
                            if (((ExtendedDeploymentContext)context).
                                getPhase() == Phase.PREPARE) {
                                return appCl;
                            } else {
                                return appCl.getModuleClassLoader(moduleUri);
                            }
                        } catch (ClassCastException e) {
                            return context.getClassLoader();
                        }                        
                    }

                    @Override
                    public ClassLoader getFinalClassLoader() {
                        try {
                            EarClassLoader finalEarCL = (EarClassLoader) context.getFinalClassLoader();
                            return finalEarCL.getModuleClassLoader(moduleUri);
                        } catch (ClassCastException e) {
                            return context.getClassLoader();
                        }
                    } 
                    @Override
                    public ReadableArchive getSource() {
                        return subArchive;
                    }

                    @Override
                    public Properties getAppProps() {
                        return context.getAppProps();
                    }

                    @Override
                    public <U extends OpsParams> U getCommandParameters(Class<U> commandParametersType) {
                        return context.getCommandParameters(commandParametersType);
                    }

                    @Override
                    public void addTransientAppMetaData(String metaDataKey, 
                        Object metaData) {
                        context.addTransientAppMetaData(metaDataKey, 
                            metaData);
                    }

                    @Override
                    public  <T> T getTransientAppMetaData(String metaDataKey, 
                        Class<T> metadataType) {
                        return context.getTransientAppMetaData(metaDataKey, 
                            metadataType);
                    }

                    @Override
                    public Properties getModuleProps() {
                        return moduleProps;
                    }

                    @Override
                    public ReadableArchive getOriginalSource() {
                        try {
                            File appRoot = context.getSourceDir();
                            File origModuleFile = new File(appRoot, moduleUri); 
                            return archiveFactory.openArchive(
                                origModuleFile);
                        } catch (IOException ioe) {
                            return null;
                        }
                    }

                    @Override
                    public File getScratchDir(String subDirName) {
                        String modulePortion = Util.getURIName(
                            getSource().getURI());
                        return (new File(super.getScratchDir(subDirName), 
                            modulePortion));
                    }

                    @Override
                    public <T> T getModuleMetaData(Class<T> metadataType) {
                        try {
                            return metadataType.cast(application.getModuleByUri(moduleUri));
                        } catch (Exception e) {
                            // let's first try the extensions mechanisms...
                            if (RootDeploymentDescriptor.class.isAssignableFrom(metadataType)) {
                                for (RootDeploymentDescriptor extension  : application.getModuleByUri(moduleUri).getExtensionsDescriptors((Class<RootDeploymentDescriptor>) metadataType)) {
                                    // we assume there can only be one type of
                                    if (extension!=null) {
                                        try {
                                            return metadataType.cast(extension);
                                        } catch (Exception e1) {
                                            // next one...
                                        }
                                    }
                                }
                                
                            }

                            return context.getModuleMetaData(metadataType);
                        }
                    }
                };

                ((ExtendedDeploymentContext)context).getModuleDeploymentContexts().put(moduleUri, moduleContext);
                return moduleContext;
    }

    private Properties getModuleProps(DeploymentContext context, 
        String moduleUri) {
        Map<String, Properties> modulePropsMap = context.getModulePropsMap();
        Properties moduleProps = modulePropsMap.get(moduleUri);
        if (moduleProps == null) {
            moduleProps = new Properties();
            modulePropsMap.put(moduleUri, moduleProps);
        }
        return moduleProps;
    }


    private String getTypeFromModuleType(XModuleType moduleType) {
        if (moduleType.equals(XModuleType.WAR)) {
            return "web";
        } else if (moduleType.equals(XModuleType.EJB)) {
            return "ejb";
        } else if (moduleType.equals(XModuleType.CAR)) {
            return "appclient";
        } else if (moduleType.equals(XModuleType.RAR)) {
            return "connector";
        }
        return null;
    }

    // get the list of sniffers for sub module and filter out the 
    // incompatible ones
    private Collection<Sniffer> getSniffersForModule(
        DeploymentContext bundleContext, 
        ModuleDescriptor md, Application application) {
        ReadableArchive source = bundleContext.getSource();
        Collection<Sniffer> sniffers = snifferManager.getSniffers(source, bundleContext.getClassLoader());
        String type = getTypeFromModuleType(md.getModuleType());
        Sniffer mainSniffer = null;
        for (Sniffer sniffer : sniffers) {
            if (sniffer.getModuleType().equals(type)) { 
                mainSniffer = sniffer; 
            }
        }

        if (mainSniffer == null) {
            return new ArrayList();
        }

        String [] incompatibleTypes = mainSniffer.getIncompatibleSnifferTypes();
        
        List<Sniffer> sniffersToRemove = new ArrayList<Sniffer>();
        for (Sniffer sniffer : sniffers) {
            for (String incompatType : incompatibleTypes) {
                if (sniffer.getModuleType().equals(incompatType)) {
                    logger.warning(type + " module [" + 
                        md.getArchiveUri() + 
                        "] contains characteristics of other module type: " +
                        incompatType);

                    sniffersToRemove.add(sniffer);
                }
            }
        }

        sniffers.removeAll(sniffersToRemove);

        return sniffers;
    }
}
