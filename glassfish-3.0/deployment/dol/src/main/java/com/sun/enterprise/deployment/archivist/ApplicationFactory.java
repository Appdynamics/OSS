/*
 * The contents of this file are subject to the terms 
 * of the Common Development and Distribution License
 * (the License).  You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the license at
 * https://glassfish.dev.java.net/public/CDDLv1.0.html or
 * glassfish/bootstrap/legal/CDDLv1.0.txt.
 * See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at glassfish/bootstrap/legal/CDDLv1.0.txt.
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * you own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * Copyright 2006 Sun Microsystems, Inc. All rights reserved.
 */

package com.sun.enterprise.deployment.archivist;

import com.sun.enterprise.deploy.shared.ArchiveFactory;
import com.sun.enterprise.deployment.Application;
import com.sun.enterprise.deployment.BundleDescriptor;
import com.sun.enterprise.deployment.RootDeploymentDescriptor;
import com.sun.enterprise.deployment.io.ApplicationDeploymentDescriptorFile;
import com.sun.enterprise.deployment.util.ModuleDescriptor;
import com.sun.enterprise.deployment.util.ApplicationVisitor;
import com.sun.enterprise.deployment.util.ApplicationValidator;
import com.sun.enterprise.deployment.util.DOLUtils;
import com.sun.enterprise.config.serverbeans.DasConfig;
import com.sun.enterprise.util.LocalStringManagerImpl;
import org.glassfish.api.ContractProvider;
import org.glassfish.api.deployment.archive.ReadableArchive;
import org.jvnet.hk2.annotations.Inject;
import org.jvnet.hk2.annotations.Service;
import org.jvnet.hk2.component.Habitat;
import org.xml.sax.SAXParseException;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;

/**
 * Factory for application object
 *
 * @author Jerome Dochez
 */
@Service
public class ApplicationFactory implements ContractProvider {

    @Inject
    ArchiveFactory archiveFactory;

    @Inject
    Habitat habitat;

    @Inject
    ArchivistFactory archivistFactory;

    @Inject
    DasConfig dasConfig;

    protected static final Logger logger =
            DOLUtils.getDefaultLogger();

    // resources...
    private static LocalStringManagerImpl localStrings = new LocalStringManagerImpl(Archivist.class);

    /**
     * Open a jar file and return an application object for the modules contained
     * in the archive. If the archive is a standalone module, this API will
     * create an empty application and add the standalone module to it
     *
     * @param jarFile the archive file
     * @return the application object
     */
    public Application openArchive(URI jarFile)
            throws IOException, SAXParseException {

        return openArchive(jarFile, false);
    }

    /**
     * Open a jar file and return an application object for the modules contained
     * in the archive/directory. If the archive/directory is a standalone module, this API will
     * create an empty application and add the standalone module to it
     *
     * @param archivist         to use to open the archive file
     * @param jarFile           the archive file
     * @param handleRuntimeInfo set to true to read configuration deployment descriptors
     * @return the application object
     */
    public Application openArchive(Archivist archivist, URI jarFile, boolean handleRuntimeInfo)
            throws IOException, SAXParseException {

        // never read the runtime deployment descriptor before the
        // module type is found and the application object created

        ReadableArchive archive = archiveFactory.openArchive(jarFile);
        Application application = openArchive(archivist, archive, handleRuntimeInfo);
        archive.close();
        return application;
    }

    /**
     * Open a jar file and return an application object for the modules contained
     * in the archive. If the archive is a standalone module, this API will
     * create an empty application and add the standalone module to it
     *
     * @param archivist         to use to open the archive file
     * @param in                the archive abstraction
     * @param handleRuntimeInfo true to read configuration deployment descriptors
     * @return the application object
     */

    public Application openArchive(Archivist archivist, ReadableArchive in, boolean handleRuntimeInfo)
            throws IOException, SAXParseException {

        return openArchive(in.getURI().getSchemeSpecificPart(), archivist, in, handleRuntimeInfo);
    }

    /**
     * Open a jar file and return an application object for the modules contained
     * in the archive. If the archive is a standalone module, this API will
     * create an empty application and add the standalone module to it
     *
     * @param appName           the application moduleID
     * @param archivist         to use to open the archive file
     * @param in                the input archive
     * @param handleRuntimeInfo set to true to read configuration deployment descriptors
     * @return the application object
     */

    public Application openArchive(String appName, Archivist archivist, ReadableArchive in, boolean handleRuntimeInfo)
            throws IOException, SAXParseException {
        // we are not reading the runtime deployment descriptor now...
        archivist.setHandleRuntimeInfo(false);

        RootDeploymentDescriptor descriptor = archivist.open(in);
        Application application;
        if (descriptor instanceof Application) {
            application = (Application) descriptor;
            application.setRegistrationName(appName);
        } else {
            BundleDescriptor aBundle = (BundleDescriptor) descriptor;
            if (aBundle == null) {
                logger.log(Level.SEVERE, localStrings.getLocalString(
                        "enterprise.deployment.cannotreadDDs",
                        "Cannot read the Deployment Descriptors for module {0}",
                        new Object[]{in.getURI()}));
                return null;
            }
            ModuleDescriptor newModule = archivist.createModuleDescriptor(aBundle);
            newModule.setArchiveUri(in.getURI().getSchemeSpecificPart());
            application = Application.createApplication(habitat,appName,newModule);
        }

        // now read the runtime deployment descriptor
        if (handleRuntimeInfo) {
            // now read the runtime deployment descriptors from the original jar file
            archivist.setHandleRuntimeInfo(true);
            archivist.readRuntimeDeploymentDescriptor(in, descriptor);
        }

        // validate
        if (application != null) {
            application.setClassLoader(archivist.getClassLoader());
            application.visit((ApplicationVisitor) new ApplicationValidator());
        }

        return application;

    }

    /**
     * This method creates an Application object from reading the 
     * standard deployment descriptor.
     * @param archive the archive for the application
     */
    public Application createApplicationFromStandardDD(
        ReadableArchive archive) throws IOException, SAXParseException {
        Archivist archivist = archivistFactory.getArchivist(archive, 
            null);
        String xmlValidationLevel = dasConfig.getDeployXmlValidation();
        archivist.setXMLValidationLevel(xmlValidationLevel);
        if (xmlValidationLevel.equals("none")) {
            archivist.setXMLValidation(false);
        }
        RootDeploymentDescriptor desc = archivist.readStandardDeploymentDescriptor(archive);
        Application application = null;
        if (desc instanceof Application) {
            application = (Application)desc;
        } else if (desc instanceof BundleDescriptor) {
            BundleDescriptor aBundle = (BundleDescriptor)desc;
            ModuleDescriptor newModule = archivist.createModuleDescriptor(aBundle);
            newModule.setArchiveUri(archive.getURI().getSchemeSpecificPart());
            String moduleName = newModule.getModuleName();
            application = Application.createApplication(habitat, moduleName, 
                newModule);
        }
        return application;
    }

     
    /**
     * This method populates the rest of the Application object from the
     * previous standard deployment descriptor reading 
     * @param archive the archive for the application
     */
    public Application openWith(Application application, 
        ReadableArchive archive, Archivist archivist)
        throws IOException, SAXParseException {
        archivist.openWith(application, archive);
        // validate
        if (application.isVirtual()) {
            application.setClassLoader(archivist.getClassLoader());
            application.visit((ApplicationVisitor) new ApplicationValidator());
        }
        return application;
    }

    
    /**
     * Open a jar file with the default Archivists and return an application
     * object for the modules contained in the archive.
     * If the archive is a standalone module, this API will
     * create an empty application and add the standalone module to it
     *
     * @param jarFile the  archive file
     * @param handleRuntimeInfo set to true to read configuration deployment descriptors
     * @return the application object
     */
    public Application openArchive(URI jarFile, boolean handleRuntimeInfo)
            throws IOException, SAXParseException {

        ReadableArchive in = archiveFactory.openArchive(jarFile);
        Archivist archivist = archivistFactory.getPrivateArchivistFor(in);
        return openArchive(archivist, jarFile, handleRuntimeInfo);
    }

    /**
     * @param jarFile the .ear file
     * @return the application name from an application .ear file
     */
    public String getApplicationName(File jarFile) throws IOException {

        if (!jarFile.exists()) {
            throw new IOException(localStrings.getLocalString(
                    "enterprise.deployment.exceptionjarfiledoesn'texist",
                    "{0} does not exist", new Object[]{jarFile}));
        }

        /*
        *Add finally clause containing explicit close of jar file.
        */
        JarFile jar = null;
        try {
            jar = new JarFile(jarFile);
            ApplicationDeploymentDescriptorFile node = new ApplicationDeploymentDescriptorFile();
            node.setXMLValidation(false);
            ZipEntry deploymentEntry = jar.getEntry(node.getDeploymentDescriptorPath());
            if (deploymentEntry != null) {
                try {
                    Application application = (Application) node.read(jar.getInputStream(deploymentEntry));
                    return application.getDisplayName();
                } catch (Exception pe) {
                    logger.log(Level.WARNING, "Error occurred", pe);  
                }
            }
        } finally {
            if (jar != null) {
                jar.close();
            }
        }
        return null;
     }
}
