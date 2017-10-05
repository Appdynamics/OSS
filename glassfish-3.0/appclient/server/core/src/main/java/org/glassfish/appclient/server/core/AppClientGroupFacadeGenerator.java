/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * 
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 2009 Sun Microsystems, Inc. All rights reserved.
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

package org.glassfish.appclient.server.core;

import com.sun.enterprise.deployment.Application;
import com.sun.enterprise.deployment.BundleDescriptor;
import com.sun.enterprise.deployment.deploy.shared.OutputJarArchive;
import com.sun.enterprise.deployment.util.ModuleDescriptor;
import com.sun.enterprise.deployment.util.XModuleType;
import com.sun.enterprise.module.Module;
import com.sun.enterprise.module.ModulesRegistry;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import org.glassfish.api.deployment.DeployCommandParameters;
import org.glassfish.api.deployment.DeploymentContext;
import org.glassfish.api.deployment.archive.WritableArchive;
import org.glassfish.deployment.common.ClientArtifactsManager;
import org.glassfish.deployment.common.DeploymentException;
import org.glassfish.deployment.common.DownloadableArtifacts;
import org.jvnet.hk2.annotations.Inject;
import org.jvnet.hk2.annotations.Scoped;
import org.jvnet.hk2.annotations.Service;
import org.jvnet.hk2.component.PerLookup;

/**
 * Generates the app client group (EAR-level) facade JAR.
 * <p>
 * Because an EAR can contain multiple clients, this might be run multiple
 * times.  To avoid extra work the class stores a flag that it has done its
 * work in the deployment context's transient app data.
 * 
 * @author tjquinn
 */
@Service
@Scoped(PerLookup.class)
public class AppClientGroupFacadeGenerator {

    private static final String GLASSFISH_APPCLIENT_GROUP_FACADE_CLASS_NAME =
            "org.glassfish.appclient.client.AppClientGroupFacade";

    private static final Attributes.Name GLASSFISH_APPCLIENT_GROUP = new Attributes.Name("GlassFish-AppClient-Group");
    private static final String GF_CLIENT_MODULE_NAME = "org.glassfish.appclient.gf-client-module";

    private static final String GROUP_FACADE_ALREADY_GENERATED = "groupFacadeAlreadyGenerated";

    static final String GROUP_FACADE_DOWNLOAD_KEY = "earFacadeDownload";

    private DeploymentContext dc;
    private AppClientDeployerHelper helper;

    @Inject
    private DownloadableArtifacts downloadableArtifacts;

    @Inject
    private ModulesRegistry modulesRegistry;

    private ClassLoader gfClientModuleClassLoader = null;

    void run(final AppClientDeployerHelper helper) {
        dc = helper.dc();
        this.helper = helper;
        if ( ! groupFacadeAlreadyGenerated().get()) {
            generateGroupFacade();
        }
    }

    private ClassLoader gfClientModuleClassLoader() {
        if (gfClientModuleClassLoader == null) {
            for (Module module : modulesRegistry.getModules(GF_CLIENT_MODULE_NAME)) {
                gfClientModuleClassLoader = module.getClassLoader();
            }
        }
        return gfClientModuleClassLoader;
    }

    private AtomicBoolean groupFacadeAlreadyGenerated() {
        AtomicBoolean groupFacadeAlreadyGenerated =
                dc.getTransientAppMetaData(GROUP_FACADE_ALREADY_GENERATED, AtomicBoolean.class);
        if (groupFacadeAlreadyGenerated == null) {
            groupFacadeAlreadyGenerated = new AtomicBoolean(false);
            dc.addTransientAppMetaData(GROUP_FACADE_ALREADY_GENERATED,
                    groupFacadeAlreadyGenerated);
        }
        return groupFacadeAlreadyGenerated;
    }

    private void recordGroupFacadeGeneration() {
        dc.getTransientAppMetaData(GROUP_FACADE_ALREADY_GENERATED, AtomicBoolean.class).set(true);
    }

    private void generateGroupFacade() {

        final Application application = dc.getModuleMetaData(Application.class);
        final Collection<ModuleDescriptor<BundleDescriptor>> appClients =
                application.getModuleDescriptorsByType(XModuleType.CAR);

        final StringBuilder appClientGroupListSB = new StringBuilder();

        /*
        /*
         * For each app client, get its facade's URI to include in the
         * generated EAR facade's client group listing.
         */
        for (Iterator<ModuleDescriptor<BundleDescriptor>> it = appClients.iterator(); it.hasNext(); ) {
            ModuleDescriptor<BundleDescriptor> md = it.next();
            appClientGroupListSB.append((appClientGroupListSB.length() > 0) ? " " : "")
                    .append(earDirUserURIText(dc)).append(appClientFacadeUserURI(md.getArchiveUri()));
        }

        try {
            generateAndRecordEARFacade(
                    dc,
                    dc.getScratchDir("xml"),
                    generatedEARFacadeName(application.getRegistrationName()), 
                        appClientGroupListSB.toString());
            recordGroupFacadeGeneration();
        } catch (Exception e) {
            throw new DeploymentException(e);
        }
    }

    private String earDirUserURIText(final DeploymentContext dc) {
        final DeployCommandParameters deployParams = dc.getCommandParameters(DeployCommandParameters.class);
        final String appName = deployParams.name();
        return appName + "Client/";
    }

    private String appClientFacadeUserURI(String appClientModuleURIText) {
        if (appClientModuleURIText.endsWith("_jar")) {
            appClientModuleURIText = appClientModuleURIText.substring(0, appClientModuleURIText.lastIndexOf("_jar")) + ".jar";
        }
        final int dotJar = appClientModuleURIText.lastIndexOf(".jar");
        String appClientFacadePath = appClientModuleURIText.substring(0, dotJar) + "Client.jar";
        return appClientFacadePath;
    }

    public static String generatedEARFacadeName(final String earName) {
        return generatedEARFacadePrefix(earName) + ".jar";
    }

    public static String generatedEARFacadePrefix(final String earName) {
        return earName + "Client";
    }

    private void generateAndRecordEARFacade(
            final DeploymentContext dc,
            final File appScratchDir,
            final String facadeFileName,
            final String appClientGroupList) throws IOException {

        File generatedJar = new File(appScratchDir, facadeFileName);
        OutputJarArchive facadeArchive = new OutputJarArchive();
        facadeArchive.create(generatedJar.toURI());

        Manifest manifest = facadeArchive.getManifest();
        Attributes mainAttrs = manifest.getMainAttributes();

        mainAttrs.put(Attributes.Name.MANIFEST_VERSION, "1.0");
        mainAttrs.put(Attributes.Name.MAIN_CLASS, GLASSFISH_APPCLIENT_GROUP_FACADE_CLASS_NAME);
        mainAttrs.put(GLASSFISH_APPCLIENT_GROUP, appClientGroupList);


        //Now manifest is ready to be written into the facade jar
        final OutputStream manifestOutputStream = facadeArchive.putNextEntry(JarFile.MANIFEST_NAME);
        manifest.write(manifestOutputStream);
        facadeArchive.closeEntry();

        writeMainClass(facadeArchive);

        /*
         * Other deployers' artifacts - such as generated stubs - go into the
         * group facade.  Each of the client's individual facade JARs then refers
         * to the group facade in their Class-Path so they can see the stubs.
         * This also allows Java SE clients to add the group facade JAR to
         * the runtime class path and see the stubs.  (This allows users who
         * did this in v2 to use the same technique.)
         */
        copyArtifactsFromOtherDeployers(facadeArchive, dc);

        facadeArchive.close();

        Set<DownloadableArtifacts.FullAndPartURIs> downloads =
                    new HashSet<DownloadableArtifacts.FullAndPartURIs>();
        DownloadableArtifacts.FullAndPartURIs download =
                new DownloadableArtifacts.FullAndPartURIs(
                    generatedJar.toURI(), facadeFileName);
        downloads.add(download);
        helper.earLevelDownloads().add(download);
//        downloadableArtifacts.addArtifacts(helper.groupFacadeUserURI(dc).toASCIIString(), downloads);
        dc.addTransientAppMetaData(GROUP_FACADE_DOWNLOAD_KEY, download);
    }

    private void writeMainClass(final WritableArchive facadeArchive) throws IOException {
        final String mainClassResourceName =
                GLASSFISH_APPCLIENT_GROUP_FACADE_CLASS_NAME.replace('.', '/') +
                ".class";
        OutputStream os = facadeArchive.putNextEntry(mainClassResourceName);

        try {
            InputStream is = openByteCodeStream("/" + mainClassResourceName);
            AppClientDeployerHelper.copyStream(is, os);
            is.close();
        } catch (Exception e) {
            throw new DeploymentException(e);
        }

    }
    protected InputStream openByteCodeStream(final String resourceName) throws IOException {
        URL url = gfClientModuleClassLoader().getResource(resourceName);
        if (url == null) {
            throw new IllegalArgumentException(resourceName);
        }
        InputStream is = url.openStream();
        return is;
    }

    private void copyArtifactsFromOtherDeployers(
            final WritableArchive facadeArchive,
            final DeploymentContext dc) throws IOException {
        final ClientArtifactsManager artifacts = ClientArtifactsManager.get(dc);
        for (DownloadableArtifacts.FullAndPartURIs artifact : artifacts.artifacts()) {
            OutputStream os = facadeArchive.putNextEntry(artifact.getPart().toASCIIString());
            InputStream is = new BufferedInputStream(new FileInputStream(new File(artifact.getFull())));
            AppClientDeployerHelper.copyStream(is, os);
            try {
                is.close();
                facadeArchive.closeEntry();
            } catch (IOException ignore) {
            }
        }
    }

}
