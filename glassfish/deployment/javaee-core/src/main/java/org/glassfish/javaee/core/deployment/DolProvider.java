/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2009-2011 Oracle and/or its affiliates. All rights reserved.
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

package org.glassfish.javaee.core.deployment;

import org.glassfish.hk2.classmodel.reflect.Parser;
import org.glassfish.hk2.classmodel.reflect.Types;
import org.glassfish.internal.deployment.DeploymentTracing;
import org.jvnet.hk2.annotations.Service;
import org.jvnet.hk2.annotations.Inject;
import org.jvnet.hk2.component.Habitat;
import org.glassfish.api.deployment.ApplicationMetaDataProvider;
import org.glassfish.api.deployment.MetaData;
import org.glassfish.api.deployment.DeploymentContext;
import org.glassfish.api.deployment.DeployCommandParameters;
import org.glassfish.api.deployment.archive.ReadableArchive;
import org.glassfish.api.deployment.archive.WritableArchive;
import org.glassfish.api.deployment.archive.ArchiveHandler;
import org.glassfish.api.ActionReport;
import org.glassfish.deployment.common.DeploymentProperties;
import org.glassfish.deployment.common.DeploymentUtils;
import org.glassfish.internal.data.ApplicationInfo;
import org.glassfish.internal.deployment.ApplicationInfoProvider;
import org.xml.sax.SAXParseException;
import com.sun.enterprise.deployment.Application;
import com.sun.enterprise.deployment.BundleDescriptor;
import com.sun.enterprise.deployment.WebBundleDescriptor;
import com.sun.enterprise.deployment.RootDeploymentDescriptor;
import com.sun.enterprise.deployment.ConnectorDescriptor;
import com.sun.enterprise.deployment.util.ModuleDescriptor;
import com.sun.enterprise.deployment.util.DOLUtils;
import com.sun.enterprise.deployment.deploy.shared.DeploymentPlanArchive;
import com.sun.enterprise.deployment.archivist.Archivist;
import com.sun.enterprise.deployment.archivist.ArchivistFactory;
import com.sun.enterprise.deployment.archivist.ApplicationFactory;
import com.sun.enterprise.deployment.archivist.ApplicationArchivist;
import com.sun.enterprise.deployment.archivist.DescriptorArchivist;
import com.sun.enterprise.deploy.shared.ArchiveFactory;
import com.sun.enterprise.config.serverbeans.DasConfig;
import com.sun.enterprise.config.serverbeans.Module;
import com.sun.enterprise.config.serverbeans.Domain;
import com.sun.enterprise.util.LocalStringManagerImpl;

import java.util.Properties;
import java.util.Collection;
import java.io.IOException;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;

/**
 * ApplicationMetada
 */
@Service
public class DolProvider implements ApplicationMetaDataProvider<Application>, 
        ApplicationInfoProvider {

    @Inject
    ArchivistFactory archivistFactory;

    @Inject
    protected ApplicationFactory applicationFactory;

    @Inject
    protected ArchiveFactory archiveFactory;

    @Inject
    protected DescriptorArchivist descriptorArchivist;

    @Inject
    protected ApplicationArchivist applicationArchivist;

    @Inject
    Habitat habitat;

    @Inject
    Domain domain;

    @Inject
    DasConfig dasConfig;

    private static String WRITEOUT_XML = System.getProperty(
        "writeout.xml");

    final private static LocalStringManagerImpl localStrings = new LocalStringManagerImpl(DolProvider.class);


    public MetaData getMetaData() {
        return new MetaData(false, new Class[] { Application.class, WebBundleDescriptor.class }, null);
    }

    public Application load(DeploymentContext dc) throws IOException {

        ReadableArchive sourceArchive = dc.getSource();

        sourceArchive.setExtraData(Types.class, dc.getTransientAppMetaData(Types.class.getName(), Types.class));
        sourceArchive.setExtraData(Parser.class, dc.getTransientAppMetaData(Parser.class.getName(), Parser.class));

        ClassLoader cl = dc.getClassLoader();
        DeployCommandParameters params = dc.getCommandParameters(DeployCommandParameters.class);

        String name = params.name();

        Archivist archivist = archivistFactory.getArchivist(
                sourceArchive, cl);
        if (archivist == null) {
            throw new RuntimeException(localStrings.getLocalString("invalid.javaee.archive", "Archive [{0}] was deployed as a Java EE archive while it does not contain any valid Java EE components. Please check the packaging of the archive.", name));
        }
        archivist.setAnnotationProcessingRequested(true);
        String xmlValidationLevel = dasConfig.getDeployXmlValidation();
        archivist.setXMLValidationLevel(xmlValidationLevel);
        if (xmlValidationLevel.equals("none")) {
            archivist.setXMLValidation(false);
        }
        archivist.setRuntimeXMLValidation(false);

        ApplicationHolder holder = dc.getModuleMetaData(ApplicationHolder.class);
        File deploymentPlan = params.deploymentplan;
        handleDeploymentPlan(deploymentPlan, archivist, sourceArchive, holder);
        
        long start = System.currentTimeMillis();
        Application application=null;
        if (holder!=null) {
            application = holder.app;

            application.setAppName(name);
            application.setClassLoader(cl);

            if (application.isVirtual()) {
                ModuleDescriptor md = application.getStandaloneBundleDescriptor().getModuleDescriptor();
                md.setModuleName(name);
            }

            try {
                applicationFactory.openWith(application, sourceArchive, 
                    archivist);
            } catch(SAXParseException e) {
                throw new IOException(e);
            }
        }
        else {
            // for case where user specified --name
            // and it's a standalone module
            try {
                application = applicationFactory.openArchive(
                    name, archivist, sourceArchive, true);

                application.setAppName(name);

                ModuleDescriptor md = application.getStandaloneBundleDescriptor().getModuleDescriptor();
                md.setModuleName(name);
            } catch(SAXParseException e) {
                throw new IOException(e);
            }
        }

        application.setRegistrationName(name);

        sourceArchive.removeExtraData(Types.class);
        sourceArchive.removeExtraData(Parser.class);

        // write out xml files if needed
        if (Boolean.valueOf(WRITEOUT_XML)) {
            saveAppDescriptor(application, dc);
        }

        Logger.getAnonymousLogger().log(Level.FINE, "DOL Loading time" + (System.currentTimeMillis() - start));

        if (application.isVirtual()) {
            dc.addModuleMetaData(application.getStandaloneBundleDescriptor());
            for (RootDeploymentDescriptor extension : application.getStandaloneBundleDescriptor().getExtensionsDescriptors()) {
                dc.addModuleMetaData(extension);
            }
        }

        addModuleConfig(dc, application);

        validateKeepStateOption(dc, params, application);

        return application;

    }

    /**
     * return the name for the given application
     */
    public String getNameFor(ReadableArchive archive, 
        DeploymentContext context) {
        Application application = null;
        try {
            // for these cases, the standard DD could contain the application
            // name for ear and module name for standalone module
            if (archive.exists("META-INF/application.xml") || 
                archive.exists("WEB-INF/web.xml") ||
                archive.exists("META-INF/ejb-jar.xml") || 
                archive.exists("META-INF/application-client.xml") || 
                archive.exists("META-INF/ra.xml")) {
                application = applicationFactory.createApplicationFromStandardDD(archive);
                DeploymentTracing tracing = null; 
                if (context != null) {
                    tracing = context.getModuleMetaData(DeploymentTracing.class);
                }
                if (tracing!=null) {
                    tracing.addMark(DeploymentTracing.Mark.DOL_LOADED);
                }
                ApplicationHolder holder = new ApplicationHolder(application);
                if (context != null) {
                    context.addModuleMetaData(holder);
                }

                return application.getAppName();
            }
        } catch (Exception e) {
            Logger.getAnonymousLogger().log(Level.WARNING, "Error occurred", e);
        }
        return null;
    }

    /*
     * return the library URIs for the given archive
     */

    public List<URL> getLibraryJars(DeploymentContext context) {

        List<URL> libraryURLs = new ArrayList<URL>();
        try {
            ReadableArchive archive = context.getSource();

            BundleDescriptor bundleDesc =
                context.getModuleMetaData(BundleDescriptor.class);

            libraryURLs.addAll(DOLUtils.getLibraryJars(bundleDesc, archive));

            libraryURLs.addAll(DeploymentUtils.getModuleLibraryJars(context));
        } catch (Exception e) {
            Logger.getAnonymousLogger().log(Level.WARNING, "failed to get library jar: ", e);
        }

        return libraryURLs;
    }

    protected void handleDeploymentPlan(File deploymentPlan,
        Archivist archivist, ReadableArchive sourceArchive, ApplicationHolder holder) throws IOException {
        //Note in copying of deployment plan to the portable archive,
        //we should make sure the manifest in the deployment plan jar
        //file does not overwrite the one in the original archive
        if (deploymentPlan != null) {
            DeploymentPlanArchive dpa = new DeploymentPlanArchive();
            dpa.open(deploymentPlan.toURI());
            // need to revisit for ear case
            WritableArchive targetArchive = archiveFactory.createArchive(
                sourceArchive.getURI());
            if (archivist instanceof ApplicationArchivist) {
                ((ApplicationArchivist)archivist).copyInto(holder.app, dpa, targetArchive, false);
            } else {
               archivist.copyInto(dpa, targetArchive, false);
            }
        }
    }    

    protected void saveAppDescriptor(Application application, 
        DeploymentContext context) throws IOException {
        if (application != null) {
            ReadableArchive archive = archiveFactory.openArchive(
                context.getSourceDir());
            boolean isMkdirs = context.getScratchDir("xml").mkdirs();
            if (isMkdirs) {
                WritableArchive archive2 = archiveFactory.createArchive(
                    context.getScratchDir("xml"));
                descriptorArchivist.write(application, archive, archive2);

                // copy the additional webservice elements etc
                applicationArchivist.copyExtraElements(archive, archive2);
            } else {
                context.getLogger().log(Level.WARNING, "Error in creating directory " + context.getScratchDir("xml").getAbsolutePath());
            }
        }
    }

    private void addModuleConfig(DeploymentContext dc, 
        Application application) {
        DeployCommandParameters params = dc.getCommandParameters(DeployCommandParameters.class);
        if (!params.origin.isDeploy()) {
            return;
        }
        
        try {
            com.sun.enterprise.config.serverbeans.Application app_w = dc.getTransientAppMetaData(com.sun.enterprise.config.serverbeans.Application.APPLICATION, com.sun.enterprise.config.serverbeans.Application.class);
            if (app_w != null) {
                if (application.isVirtual()) {
                    Module modConfig = app_w.createChild(Module.class);
                    app_w.getModule().add(modConfig);
                    modConfig.setName(application.getRegistrationName());
                } else {
                    for (ModuleDescriptor moduleDesc : 
                        application.getModules()) {
                        Module modConfig = app_w.createChild(Module.class);
                        app_w.getModule().add(modConfig);
                        modConfig.setName(moduleDesc.getArchiveUri());
                    }
                }
            }
        } catch (Exception e) {
            Logger.getAnonymousLogger().log(Level.WARNING, "failed to add the module config", e);
        }
    }

    private void validateKeepStateOption(DeploymentContext context, DeployCommandParameters params, Application app) {
        if ((params.keepstate != null && params.keepstate) || 
            app.getKeepState()) {
            if (!isDASTarget(context, params)) {
                // for non-DAS target, and keepstate is set to true either 
                // through deployment option or deployment descriptor
                // explicitly set the deployment option to false
                params.keepstate = false;
                String warningMsg = localStrings.getLocalString("not.support.keepstate.in.cluster", "Ignoring the keepstate setting: the keepstate option is only supported in developer profile and not cluster profile.");
                ActionReport subReport = context.getActionReport().addSubActionsReport();
                subReport.setActionExitCode(ActionReport.ExitCode.WARNING);
                subReport.setMessage(warningMsg);
                context.getLogger().log(Level.WARNING, warningMsg);
            }
        }    
    }

    private boolean isDASTarget(DeploymentContext context, DeployCommandParameters params) {
        if (DeploymentUtils.isDASTarget(params.target)) {
            return true;
        } else if (DeploymentUtils.isDomainTarget(params.target)) {
            List<String> targets = context.getTransientAppMetaData(DeploymentProperties.PREVIOUS_TARGETS, List.class);
            if (targets == null) {
                targets = domain.getAllReferencedTargetsForApplication(
                    params.name);
            }
            if (targets.size() == 1 && 
                DeploymentUtils.isDASTarget(targets.get(0))) {
                return true;
            }
        }
        return false;
    }
}
