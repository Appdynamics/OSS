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

package com.sun.enterprise.deployment.archivist;

import com.sun.enterprise.deployment.Application;
import com.sun.enterprise.deployment.ApplicationClientDescriptor;
import com.sun.enterprise.deployment.RootDeploymentDescriptor;
import com.sun.enterprise.deployment.io.AppClientDeploymentDescriptorFile;
import com.sun.enterprise.deployment.io.DeploymentDescriptorFile;
import com.sun.enterprise.deployment.io.runtime.AppClientRuntimeDDFile;
import com.sun.enterprise.deployment.io.runtime.GFAppClientRuntimeDDFile;
import com.sun.enterprise.deployment.util.ApplicationValidator;
import com.sun.enterprise.deployment.util.DOLUtils;
import com.sun.enterprise.deployment.util.ModuleContentValidator;
import com.sun.enterprise.deployment.util.XModuleType;
import org.glassfish.api.deployment.archive.ReadableArchive;
import org.glassfish.api.deployment.archive.WritableArchive;
import org.jvnet.hk2.annotations.Scoped;
import org.jvnet.hk2.annotations.Service;
import org.jvnet.hk2.component.PerLookup;
import org.xml.sax.SAXParseException;

import javax.enterprise.deploy.shared.ModuleType;
import java.io.IOException;
import java.util.Map;
import java.util.Vector;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.logging.Level;

/**
 * This class is responsible for handling J2EE app client files.
 *
 * @author Sheetal Vartak
 */
@Service
@Scoped(PerLookup.class)
public class AppClientArchivist extends Archivist<ApplicationClientDescriptor> {

    public static final Attributes.Name GLASSFISH_APPCLIENT =
            new Attributes.Name("GlassFish-AppClient");

    public static final Attributes.Name GLASSFISH_CLIENT_PU_SCAN_TARGETS_NAME =
            new Attributes.Name("GlassFish-Client-PersistenceUnit-Scan-Targets");

    public static final Attributes.Name GLASSFISH_GROUP_FACADE =
            new Attributes.Name("GlassFish-Group-Facade");

    public static final Attributes.Name GLASSFISH_ANCHOR_DIR =
            new Attributes.Name("GlassFish-Anchor");
    
    private String mainClassNameToRun = null;

    DeploymentDescriptorFile standardDD = new AppClientDeploymentDescriptorFile();

    /**
     * Creates new ApplicationClientArchvisit
     */
    public AppClientArchivist() {
        handleRuntimeInfo = true;
    }

    /**
     * @return the  module type handled by this archivist
     *         as defined in the application DTD
     */
    @Override
    public XModuleType getModuleType() {
        return XModuleType.CAR;
    }

    public ApplicationClientDescriptor open(final ReadableArchive archive,
            final String mainClassNameToRun) throws IOException, SAXParseException {
        this.mainClassNameToRun = mainClassNameToRun;
        return super.open(archive);
    }

    public void setDescriptor(Application application) {

        // this is acceptable if the application actually represents
        // a standalone module
        java.util.Set appClientBundles = application.getApplicationClientDescriptors();
        if (appClientBundles.size() > 0) {
            this.descriptor = (ApplicationClientDescriptor) appClientBundles.iterator().next();
            if (this.descriptor.getModuleDescriptor().isStandalone())
                return;
            else
                this.descriptor = null;
        }
        DOLUtils.getDefaultLogger().log(Level.SEVERE, "enterprise.deployment.backend.descriptorFailure", new Object[]{this});
        throw new RuntimeException("Error setting descriptor " + descriptor + " in " + this);
    }

    /**
     * @return the DeploymentDescriptorFile responsible for handling
     *         standard deployment descriptor
     */
    @Override
    public DeploymentDescriptorFile<ApplicationClientDescriptor> getStandardDDFile() {
        return standardDD;
    }

    /**
     * @return if exists the DeploymentDescriptorFile responsible for
     *         handling the configuration deployment descriptors
     */
    @Override
    public DeploymentDescriptorFile getConfigurationDDFile() {
        return new GFAppClientRuntimeDDFile();
    }

    /**
     * @return if exists the DeploymentDescriptorFile responsible for
     *         handling the Sun configuration deployment descriptors
     */
    public DeploymentDescriptorFile getSunConfigurationDDFile() {
        return new AppClientRuntimeDDFile();
    }

    /**
     * @return a default BundleDescriptor for this archivist
     */
    @Override
    public ApplicationClientDescriptor getDefaultBundleDescriptor() {
        ApplicationClientDescriptor appClientDesc =
                new ApplicationClientDescriptor();
        return appClientDesc;
    }

    /**
     * validates the DOL Objects associated with this archivist, usually
     * it requires that a class loader being set on this archivist or passed
     * as a parameter
     */
    @Override
    public void validate(ClassLoader aClassLoader) {
        ClassLoader cl = aClassLoader;
        if (cl == null) {
            cl = classLoader;
        }
        if (cl == null) {
            return;
        }
        descriptor.setClassLoader(cl);
        descriptor.visit(new ApplicationValidator());
    }

    /**
     * perform any action after all standard DDs is read
     *
     * @param descriptor the deployment descriptor for the module
     * @param archive    the module archive
     * @param extensions map of extension archivists
     */
    @Override
    protected void postStandardDDsRead(ApplicationClientDescriptor descriptor, ReadableArchive archive,
                Map<ExtensionsArchivist, RootDeploymentDescriptor> extensions)
                throws IOException {
        super.postStandardDDsRead(descriptor, archive, extensions);
        // look for MAIN_CLASS
        if (mainClassNameToRun == null) {
            Manifest m = archive.getManifest();
            mainClassNameToRun = getMainClassName(m);
        }
        descriptor.setMainClassName(mainClassNameToRun);
    }

    /**
     * perform any post deployment descriptor reading action
     *
     * @param descriptor the deployment descriptor for the module
     * @param archive    the module archive
     */
    @Override
    protected void postOpen(ApplicationClientDescriptor descriptor, ReadableArchive archive)
            throws IOException {

        super.postOpen(descriptor, archive);

        runValidations(descriptor, archive);
    }

    protected void runValidations(final ApplicationClientDescriptor acDesc,
            final ReadableArchive archive) {
        ModuleContentValidator mdv = new ModuleContentValidator(archive);
        acDesc.visit(mdv);
    }

    /**
     * writes the content of an archive to a JarFile
     *
     * @param in            the input  archive
     * @param out           the archive output stream to write to
     * @param entriesToSkip the files to not write from the original archive
     */
    @Override
    protected void writeContents(ReadableArchive in, WritableArchive out, Vector entriesToSkip)
            throws IOException {

        // prepare the manifest file to add the main class entry
        if (manifest == null) {
            manifest = new Manifest();
        }
        manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, MANIFEST_VERSION_VALUE);
        manifest.getMainAttributes().put(Attributes.Name.MAIN_CLASS,
                ((ApplicationClientDescriptor) getDescriptor()).getMainClassName());

        super.writeContents(in, out, entriesToSkip);
    }

    /**
     * @return the manifest attribute Main-class
     */
    public String getMainClassName(Manifest m) {
        if (m != null) {
            return m.getMainAttributes().getValue(Attributes.Name.MAIN_CLASS);
        }
        return null;
    }

    @Override
    protected boolean postHandles(ReadableArchive archive)
            throws IOException {
        //check the main-class attribute
        if (getMainClassName(archive.getManifest()) != null) {
            return true;
        }

        return false;
    }

    protected String getArchiveExtension() {
        return APPCLIENT_EXTENSION;
    }
}
