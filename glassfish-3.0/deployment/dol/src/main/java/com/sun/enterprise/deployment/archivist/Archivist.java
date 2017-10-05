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

package com.sun.enterprise.deployment.archivist;

import com.sun.enterprise.deploy.shared.ArchiveFactory;
import com.sun.enterprise.deployment.*;
import com.sun.enterprise.deployment.annotation.factory.AnnotatedElementHandlerFactory;
import com.sun.enterprise.deployment.annotation.factory.SJSASFactory;
import com.sun.enterprise.deployment.annotation.impl.ModuleScanner;
import com.sun.enterprise.deployment.io.DeploymentDescriptorFile;
import static com.sun.enterprise.deployment.io.DescriptorConstants.PERSISTENCE_DD_ENTRY;
import com.sun.enterprise.deployment.io.PersistenceDeploymentDescriptorFile;
import com.sun.enterprise.deployment.io.WebServicesDeploymentDescriptorFile;
import com.sun.enterprise.deployment.util.*;
import com.sun.enterprise.util.LocalStringManagerImpl;
import com.sun.enterprise.util.io.FileUtils;
import com.sun.enterprise.util.shared.ArchivistUtils;
import org.glassfish.apf.*;
import org.glassfish.apf.Scanner;
import com.sun.enterprise.deployment.annotation.impl.ModuleScanner;
import org.glassfish.apf.impl.DefaultErrorHandler;
import org.glassfish.apf.impl.ProcessingResultImpl;
import org.glassfish.api.deployment.archive.Archive;
import org.glassfish.api.deployment.archive.ReadableArchive;
import org.glassfish.api.deployment.archive.WritableArchive;
import org.glassfish.deployment.common.InstalledLibrariesResolver;
import org.jvnet.hk2.annotations.Inject;
import org.jvnet.hk2.annotations.Contract;
import org.jvnet.hk2.component.ComponentException;
import org.jvnet.hk2.component.Habitat;
import org.xml.sax.SAXParseException;

import javax.enterprise.deploy.shared.ModuleType;
import java.io.*;
import java.util.*;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This abstract class contains all common behaviour for Achivisits. Archivists
 * classes are responsible for reading and writing correct J2EE Archives
 *
 * @author Jerome Dochez
 */
@Contract
public abstract class Archivist<T extends RootDeploymentDescriptor> {

    protected static final Logger logger =
            DOLUtils.getDefaultLogger();

    public static final String MANIFEST_VERSION_VALUE = "1.0";

    // the path for the underlying archive file
    protected String path;

    // should we read or save the runtime info.
    protected boolean handleRuntimeInfo = true;

    // default should be false in production
    protected boolean annotationProcessingRequested = false;

    // attributes of this archive
    protected Manifest manifest;
    
    // resources...
    private static final LocalStringManagerImpl localStrings =
	    new LocalStringManagerImpl(Archivist.class);    
    
    // class loader to use when validating the DOL
    protected ClassLoader classLoader = null;

    // boolean for XML validation
    private boolean isValidatingXML = true;

    // boolean for runtime XML validation
    private boolean isValidatingRuntimeXML = false;

    // xml validation error level reporting/recovering
    private String validationLevel = "parsing";

    // runtime xml validation error level reporting/recovering
    private String runtimeValidationLevel = "parsing";

    // error handler for annotation processing
    private ErrorHandler annotationErrorHandler = null;

    private static final String WSDL = ".wsdl";
    private static final String XML = ".xml";
    private static final String XSD = ".xsd";


    protected static final String APPLICATION_EXTENSION = ".ear";
    protected static final String APPCLIENT_EXTENSION = ".jar";
    protected static final String WEB_EXTENSION = ".war";
    protected static final String WEB_FRAGMENT_EXTENSION = ".jar";
    protected static final String EJB_EXTENSION = ".jar";
    protected static final String CONNECTOR_EXTENSION = ".rar";
    //Used to detect the uploaded files which always end in ".tmp"
    protected static final String UPLOAD_EXTENSION = ".tmp";

    private static final String PROCESS_ANNOTATION_FOR_OLD_DD =
            "process.annotation.for.old.dd";

    private static final boolean processAnnotationForOldDD =
            Boolean.getBoolean(PROCESS_ANNOTATION_FOR_OLD_DD); 
    
    protected T descriptor;

    @Inject
    Habitat habitat;

    @Inject
    SJSASFactory annotationFactory;

    @Inject
    ArchiveFactory archiveFactory;

    @Inject(optional = true)
    ExtensionsArchivist[] extensionsArchivists;

    /**
     * Creates new Archivist
     */
    public Archivist() {
        annotationErrorHandler = new DefaultErrorHandler();
    }

    /**
     * initializes this instance from another archivist, this is used
     * to transfer contextual information between archivists, for
     * example whether we should handle runtime information and such
     */
    protected void initializeContext(Archivist other) {
        handleRuntimeInfo = other.isHandlingRuntimeInfo();
        annotationProcessingRequested = other.isAnnotationProcessingRequested();
        isValidatingXML = other.isValidatingXML;
        validationLevel = other.validationLevel;
        classLoader = other.classLoader;
        annotationErrorHandler = other.annotationErrorHandler;
    }

    /**
     * Archivist read XML deployment descriptors and keep the
     * parsed result in the DOL descriptor instances. Sets the descriptor
     * for a particular Archivist type
     *
     * @parem descriptor for this archivist instnace
     */
    public void setDescriptor(T descriptor) {
        this.descriptor = descriptor;
    }

    /**
     * @return the Descriptor for this archvist
     */
    public T getDescriptor() {
        return descriptor;
    }


    /**
     * Open a new archive file, read the XML descriptor and set the  constructed
     * DOL descriptor instance
     *
     * @param archive the archive file path
     * @return the deployment descriptor for this archive
     */
    public T open(ReadableArchive archive)
            throws IOException, SAXParseException {
        return open(archive, (Application) null);
    }

    public T open(final ReadableArchive descriptorArchive,
            final ReadableArchive contentArchive) throws IOException, SAXParseException {
        return open(descriptorArchive, contentArchive, null);
    }
    /**
     * Creates the DOL object graph for an app for which the descriptor(s)
     * reside in one archive and the content resides in another.
     * <p>
     * This allows the app client container to use both the generated JAR
     * which contains the descriptors that are filled in during deployment and
     * also the developer's original JAR which contains the classes that
     * might be subject to annotation processing.
     *
     * @param descriptorArchive archive containing the descriptor(s)
     * @param contentArchive archive containing the classes, etc.
     * @param app owning DOL application (if any)
     * @return DOL object graph for the application
     * 
     * @throws IOException
     * @throws SAXParseException
     */
    public T open(final ReadableArchive descriptorArchive,
            final ReadableArchive contentArchive,
            final Application app)
            throws IOException, SAXParseException {
        setManifest(contentArchive.getManifest());

        T descriptor = readDeploymentDescriptors(descriptorArchive, contentArchive, app);
        if (descriptor != null) {
            postOpen(descriptor, contentArchive);
        }
        return descriptor;
    }

    public T open(ReadableArchive archive,
            Application app) throws IOException, SAXParseException {
        return open(archive, archive, app);
    }

    // fill in the rest of the application with an application object
    // populated from previus reading of the standard deployment descriptor
    public Application openWith(Application app, ReadableArchive archive)
            throws IOException, SAXParseException {

        setManifest(archive.getManifest());

        // application archivist will override this method
        if (app.isVirtual()) {
            T descriptor = readRestDeploymentDescriptors((T)app.getStandaloneBundleDescriptor(), archive, archive, app);
            if (descriptor != null) {
                postOpen(descriptor, archive);
            }
            if (descriptor instanceof BundleDescriptor) {
                ((BundleDescriptor)descriptor).setApplication(app);
            }
        }
        return app;
    }


    /**
     * Open a new archive file, read the XML descriptor and set the  constructed
     * DOL descriptor instance
     *
     * @param path the archive file path
     * @return the deployment descriptor for this archive
     */
    public T open(String path)
            throws IOException, SAXParseException {

        this.path = path;
        File file = new File(path);
        if (!file.exists()) {
            throw new FileNotFoundException(path);
        }
        return open(file);
    }

    /**
     * open a new archive file using a file descriptor
     *
     * @param file the archive to open
     */
    public T open(File file) throws IOException, SAXParseException {

        path = file.getAbsolutePath();
        ReadableArchive archive = archiveFactory.openArchive(file);
        T descriptor = open(archive);

        archive.close();

        // attempt validation
        validate(null);

        return descriptor;    
    }

    /**
     * perform any action after all standard DDs is read
     *
     * @param descriptor the deployment descriptor for the module
     * @param archive the module archive
     */
    protected void postStandardDDsRead(T descriptor,
                                       ReadableArchive archive) throws IOException {
    }

    /**
     * perform any action after annotation processed
     *
     * @param descriptor the deployment descriptor for the module
     * @param archive the module archive
     */
    protected void postAnnotationProcess(T descriptor,
                                         ReadableArchive archive) throws IOException {
    }

    /**
     * perform any action after all runtime DDs read
     *
     * @param descriptor the deployment descriptor for the module
     * @param archive the module archive
     */
    protected void postRuntimeDDsRead(T descriptor,
                                      ReadableArchive archive) throws IOException {
    }

    /**
     * perform any post deployment descriptor reading action
     *
     * @param descriptor the deployment descriptor for the module
     * @param archive the module archive
     */
    protected void postOpen(T descriptor, ReadableArchive archive)
            throws IOException {
    }

    /**
     * Read the standard deployment descriptors (can contained in one or
     * many file) and return the corresponding initialized descriptor instance.
     * By default, the standard deployment descriptors are all contained in
     * the xml file characterized with the path returned by
     *
     * @return the initialized descriptor
     */
    private T readDeploymentDescriptors(ReadableArchive archive)
            throws IOException, SAXParseException {
        return readDeploymentDescriptors(archive, archive, null);
    }

    private T readDeploymentDescriptors(ReadableArchive descriptorArchive,
            ReadableArchive contentArchive,
            Application app) throws IOException, SAXParseException {

        // read the standard deployment descriptors
        T descriptor = readStandardDeploymentDescriptor(descriptorArchive);
        if (descriptor instanceof BundleDescriptor) {
            ((BundleDescriptor)descriptor).setApplication(app);
        }

        ModuleDescriptor newModule = createModuleDescriptor(descriptor);
        newModule.setArchiveUri(contentArchive.getURI().getSchemeSpecificPart());
        return readRestDeploymentDescriptors(descriptor, descriptorArchive, contentArchive, app);
    }

    private T readRestDeploymentDescriptors (T descriptor, 
        ReadableArchive descriptorArchive, ReadableArchive contentArchive, 
        Application app) throws IOException, SAXParseException {
        Map<ExtensionsArchivist, RootDeploymentDescriptor> extensions = new HashMap<ExtensionsArchivist, RootDeploymentDescriptor>();
        if (extensionsArchivists!=null) {
            for (ExtensionsArchivist extension : extensionsArchivists) {
                if (extension.supportsModuleType(getModuleType())) {
                    Object o = extension.open(this, descriptorArchive, descriptor);
                    if (o instanceof RootDeploymentDescriptor) {
                        extension.addExtension(descriptor, (RootDeploymentDescriptor) o);
                        extensions.put(extension, (RootDeploymentDescriptor) o);
                    } else {
                        extensions.put(extension, null); // maybe annotation processing will yield results
                    }
                }
            }
        }

        postStandardDDsRead(descriptor, contentArchive);

        readAnnotations(contentArchive, descriptor, extensions);
        postAnnotationProcess(descriptor, contentArchive);

        // now read the runtime deployment descriptors
        readRuntimeDeploymentDescriptor(descriptorArchive, descriptor);

        // read extensions runtime deployment descriptors if any
        for (Map.Entry<ExtensionsArchivist, RootDeploymentDescriptor> extension : extensions.entrySet()) {
            // after standard DD and annotations are processed, we should
            // an extension descriptor now
            if (extension.getValue() != null) {
                extension.getKey().readRuntimeDeploymentDescriptor(this, descriptorArchive, extension.getValue());
            }
        }

        postRuntimeDDsRead(descriptor, contentArchive);

        return descriptor;
    }

    protected void readAnnotations(ReadableArchive archive, T descriptor,
                                 Map<ExtensionsArchivist, RootDeploymentDescriptor> extensions)
            throws IOException {

        readAnnotations(archive, descriptor, extensions, null);
    }

    protected void readAnnotations(ReadableArchive archive, T descriptor,
                                 Map<ExtensionsArchivist, RootDeploymentDescriptor> extensions,
                                 ModuleScanner scanner)
            throws IOException {
        
        if (isProcessAnnotation(descriptor)) {
            try {
                if (scanner == null) {
                    scanner = getScanner();
                }
                ProcessingResult result = processAnnotations(descriptor, scanner, archive);

                // process extensions annotations if any
                for (Map.Entry<ExtensionsArchivist, RootDeploymentDescriptor> extension : extensions.entrySet()) {
                    try {
                        if (extension.getValue()==null) {
                            RootDeploymentDescriptor o = extension.getKey().getDefaultDescriptor();


                            if( o != null ) {
                                o.setModuleDescriptor(descriptor.getModuleDescriptor());
                            }
                          

                            processAnnotations(o, extension.getKey().getScanner(), archive);
                            if (o!=null && !o.isEmpty() && (o instanceof RootDeploymentDescriptor)) {
                                extension.getKey().addExtension(descriptor, o);
                                extensions.put(extension.getKey(), (RootDeploymentDescriptor) o);
                            }
                        } else {
                            processAnnotations(extension.getValue(), extension.getKey().getScanner(), archive);
                        }
                    } catch (AnnotationProcessorException ex) {
                        DOLUtils.getDefaultLogger().severe(ex.getMessage());
                        DOLUtils.getDefaultLogger().log(Level.FINE, ex.getMessage(), ex);
                        throw new IllegalStateException(ex);
                    }
                }

                if (result != null &&
                        ResultType.FAILED.equals(result.getOverallResult())) {
                    DOLUtils.getDefaultLogger().severe(localStrings.getLocalString(
                            "enterprise.deployment.archivist.annotationprocessingfailed",
                            "Annotations processing failed for {0}",
                            new Object[]{archive.getURI()}));
                }
                //XXX for backward compatible in case of having cci impl in EJB
            } catch (NoClassDefFoundError err) {
                if (DOLUtils.getDefaultLogger().isLoggable(Level.WARNING)) {
                    DOLUtils.getDefaultLogger().warning(
                            "Error in annotation processing: " + err);
                }
            } catch (AnnotationProcessorException ex) {
                DOLUtils.getDefaultLogger().severe(ex.getMessage());
                DOLUtils.getDefaultLogger().log(Level.FINE, ex.getMessage(), ex);
                throw new IllegalStateException(ex);
            }
        } else if (DOLUtils.getDefaultLogger().isLoggable(Level.FINE)) {
            DOLUtils.getDefaultLogger().fine("Annotation is not processed for this archive.");
        }
    }

    /**
     * Returns the scanner for this archivist, usually it is the scanner regitered
     * with the same module type as this archivist, but subclasses can return a
     * different version
     *
     */
    public ModuleScanner getScanner() {
        
        Scanner scanner = null;
        try {
            scanner = habitat.getComponent(Scanner.class, getModuleType().toString());
            if (scanner==null || !(scanner instanceof ModuleScanner)) {
                logger.log(Level.SEVERE, "Cannot find module scanner for " + this.getManifest());
            }
        } catch (ComponentException e) {
            // XXX To do
            logger.log(Level.SEVERE, "Cannot find scanner for " + this.getModuleType(), e);
        }
        return (ModuleScanner)scanner;
    }

    /**
     * Process annotations in a bundle descriptor, the annoation processing
     * is dependent on the type of descriptor being passed.
     */
    public ProcessingResult processAnnotations(T bundleDesc,
                                               ReadableArchive archive)
            throws AnnotationProcessorException, IOException {
        
        return processAnnotations(bundleDesc, getScanner(), archive);

    }
    
    /**
     * Process annotations in a bundle descriptor, the annoation processing
     * is dependent on the type of descriptor being passed.
     */
    protected ProcessingResult processAnnotations(RootDeploymentDescriptor bundleDesc,
                                               ModuleScanner scanner,
                                               ReadableArchive archive)
            throws AnnotationProcessorException, IOException {

        if (scanner == null) {
            return null;
        }

        AnnotatedElementHandler aeHandler =
                AnnotatedElementHandlerFactory.createAnnotatedElementHandler(
                        bundleDesc);

        if (aeHandler == null) {
            return null;
        }

        scanner.process(archive, bundleDesc, classLoader);

        if (!scanner.getElements().isEmpty()) {
            if (bundleDesc.isDDWithNoAnnotationAllowed()) {
                // if we come into this block, it means an old version
                // of deployment descriptor has annotation which is not correct
                // throw exception in this case
                String ddName =
                        getStandardDDFile().getDeploymentDescriptorPath();
                String explodedArchiveName =
                        new File(archive.getURI()).getName();
                String archiveName = FileUtils.revertFriendlyFilenameExtension(
                        explodedArchiveName);
                throw new AnnotationProcessorException(
                        localStrings.getLocalString(
                                "enterprise.deployment.oldDDwithAnnotation",
                                "{0} in archive {1} is of version {2}, which cannot support annotations in an application.  Please upgrade the deployment descriptor to be a version supported by Java EE 5.0 (or later).",
                                new Object[]{ddName, archiveName, bundleDesc.getSpecVersion()}));
            }
            AnnotationProcessor ap = annotationFactory.getAnnotationProcessor();
            ProcessingContext ctx = ap.createContext();
            if (annotationErrorHandler != null) {
                ctx.setErrorHandler(annotationErrorHandler);
            }
            ctx.setProcessingInput(scanner);
            ctx.pushHandler(aeHandler);

            // Make sure there is a classloader available on the descriptor
            // during annotation processing.
            ClassLoader originalBundleClassLoader = null;
            try {
                originalBundleClassLoader = bundleDesc.getClassLoader();
            } catch (Exception e) {
                // getClassLoader can throw exception if not available
            }

            // Only set classloader if it's not already set.
            if (originalBundleClassLoader == null) {
                bundleDesc.setClassLoader(classLoader);
            }

            try {
                return ap.process(ctx);
            } finally {
                if (originalBundleClassLoader == null) {
                    bundleDesc.setClassLoader(null);
                }
            }
        }
        return null;
    }

    /**
     * Read the standard deployment descriptors (can contained in one or
     * many file) and return the corresponding initialized descriptor instance.
     * By default, the standard deployment descriptors are all contained in
     * the xml file characterized with the path returned by
     *
     * @return the initialized descriptor
     * @link getDeploymentDescriptorPath
     */
    public T readStandardDeploymentDescriptor(ReadableArchive archive)
            throws IOException, SAXParseException {

        InputStream is = null;

        try {
            is = archive.getEntry(getStandardDDFile().getDeploymentDescriptorPath());
            if (is != null) {
                DeploymentDescriptorFile<T> ddFile = getStandardDDFile();
                ddFile.setXMLValidation(getXMLValidation());
                ddFile.setXMLValidationLevel(validationLevel);
                if (archive.getURI() != null) {
                    ddFile.setErrorReportingString(archive.getURI().getSchemeSpecificPart());
                }
                T result = ddFile.read(is);
                return result;
            } else {
                /*
                 *Always return at least the default, because the info is needed 
                 *when an app is loaded during a server restart and there might not
                 *be a physical descriptor file.
                 */
                return getDefaultBundleDescriptor();
            }
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    /**
     * Read the runtime deployment descriptors (can contained in one or
     * many file) set the corresponding information in the passed descriptor.
     * By default, the runtime deployment descriptors are all contained in
     * the xml file characterized with the path returned by
     *
     * @param archive the archive
     * @param descriptor the initialized deployment descriptor
     * @link getRuntimeDeploymentDescriptorPath
     */
    public void readRuntimeDeploymentDescriptor(ReadableArchive archive, T descriptor)
            throws IOException, SAXParseException {

        // if we are not supposed to handle runtime info, just pass
        String ddFileEntryName = getRuntimeDeploymentDescriptorPath();
        if (!isHandlingRuntimeInfo() || ddFileEntryName == null) {
            return;
        }

        InputStream is = null;
        try {
            // apply the runtime settings if any
            is = archive.getEntry(ddFileEntryName);
            DeploymentDescriptorFile confDD = getConfigurationDDFile();
            if (archive.getURI() != null) {
                confDD.setErrorReportingString(archive.getURI().getSchemeSpecificPart());
            }

            if (is != null && confDD != null) {
                confDD.setXMLValidation(getRuntimeXMLValidation());
                confDD.setXMLValidationLevel(runtimeValidationLevel);
                confDD.read(descriptor, is);
            }
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ioe) {
                }
            }
        }
    }

    /**
     * Read the runtime deployment descriptors (can contained in one or
     * many file) from a deployment plan archive,  set the corresponding
     * information in the passed descriptor.
     */
    public void readRuntimeDDFromDeploymentPlan(
            ReadableArchive planArchive, T descriptor)
            throws IOException, SAXParseException {

        // if we are not supposed to handle runtime info, just pass
        String runtimeDDPath = getRuntimeDeploymentDescriptorPath();
        if (runtimeDDPath == null || planArchive == null) {
            return;
        }

        // list of entries in the deployment plan
        Vector dpEntries = new Vector();
        for (Enumeration e = planArchive.entries(); e.hasMoreElements();) {
            dpEntries.add(e.nextElement());
        }

        String entry = runtimeDDPath.substring(runtimeDDPath.lastIndexOf('/') + 1);
        if (dpEntries.contains(entry)) {
            readRuntimeDDFromDeploymentPlan(entry, planArchive, descriptor);
        }
    }

    /**
     * Read the runtime deployment descriptors (can contained in one or
     * many file) from a deployment plan archive,  set the corresponding
     * information in the passed descriptor.
     */
    protected void readRuntimeDDFromDeploymentPlan(
            String entry, ReadableArchive planArchive, T descriptor)
            throws IOException, SAXParseException {

        InputStream is = null;
        try {
            is = planArchive.getEntry(entry);
            DeploymentDescriptorFile confDD = getConfigurationDDFile();
            if (is != null && confDD != null) {
                if (planArchive.getURI() != null) {
                    confDD.setErrorReportingString(planArchive.getURI().getSchemeSpecificPart());
                }
                confDD.setXMLValidation(getXMLValidation());
                confDD.read(descriptor, is);
            }
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ioe) {
                }
            }
        }
    }


    /*
     * write the J2EE module represented by this instance to a new 
     * J2EE archive file
     * 
     */
    public void write() throws IOException {
        write(path);
    }

    /**
     * saves the archive
     *
     * @param outPath the file to use
     */
    public void write(String outPath) throws IOException {
        ReadableArchive in = archiveFactory.openArchive(new File(path));
        write(in, outPath);
        in.close();
    }

    /**
     * save the archive
     *
     * @param in archive to copy old elements from
     * @param outPath the file to use
     */
    public void write(ReadableArchive in, String outPath) throws IOException {

        ReadableArchive oldArchive = null;
        try {
            oldArchive = archiveFactory.openArchive(new File(outPath));
        } catch (IOException ioe) {
            // there could be many reasons why we cannot open this archive, 
            // we should continue
        }
        WritableArchive out = null;
        BufferedOutputStream bos = null;
        try {
            File outputFile=null;
            if (oldArchive != null && oldArchive.exists() &&
                    !(oldArchive instanceof WritableArchive)) {
                // this is a rewrite, get a temp file name...
                // I am creating a tmp file just to get a name
                outputFile = getTempFile(outPath);
                outputFile.delete();
                out = archiveFactory.createArchive(outputFile);
                oldArchive.close();
            } else {
                out = archiveFactory.createArchive(new File(outPath));
            }

            // write archivist content
            writeContents(in, out);
            out.close();
            in.close();

            // if we were using a temp file, time to rewrite the original
            if (outputFile != null) {
                ReadableArchive finalArchive = archiveFactory.openArchive(new File(outPath));
                finalArchive.delete();
                ReadableArchive tmpArchive = archiveFactory.openArchive(outputFile);
                tmpArchive.renameTo(outPath);
            }
        } catch (IOException ioe) {
            // cleanup
            if (out != null) {
                try {
                    out.close();
                    //out.delete(); <-- OutputJarArchive.delete isn't supported.
                } catch (IOException outIoe) {
                    // ignore exceptions here, otherwise this will end up masking the real
                    // IOException in 'ioe'.
                }
            }
            // propagate exception
            throw ioe;
        }
    }

    public void write(ReadableArchive in, WritableArchive out) throws IOException {
        writeContents(in, out);
    }

    /**
     * writes the content of an archive to a JarFile
     *
     * @param out jar output stream to write to
     */
    protected void writeContents(WritableArchive out) throws IOException {
        ReadableArchive in = archiveFactory.openArchive(new File(path));
        writeContents(in, out);
        in.close();
    }


    /**
     * writes the content of an archive to another archive
     *
     * @param in input archive
     * @param out output archive
     */
    protected void writeContents(ReadableArchive in, WritableArchive out) throws IOException {

        writeContents(in, out, null);
    }


    /**
     * writes the content of an archive to a JarFile
     *
     * @param in input  archive
     * @param out archive output stream to write to
     * @param entriesToSkip files to not write from the original archive
     */
    protected void writeContents(ReadableArchive in, WritableArchive out, Vector entriesToSkip)
            throws IOException {

        // Copy original jarFile elements
        if (in != null && in.exists()) {
            if (entriesToSkip == null) {
                entriesToSkip = getListOfFilesToSkip();
            } else {
                entriesToSkip.addAll(getListOfFilesToSkip());
            }
            copyJarElements(in, out, entriesToSkip);
        }

        // now the deployment descriptors
        writeDeploymentDescriptors(out);

        // manifest file
        if (manifest != null) {
            OutputStream os = out.putNextEntry(JarFile.MANIFEST_NAME);
            manifest.write(new DataOutputStream(os));
            out.closeEntry();
        }
    }

    /**
     * writes the deployment descriptors (standard and runtime)
     * to a JarFile using the right deployment descriptor path
     *
     * @out the abstract archive file to write to
     */
    public void writeDeploymentDescriptors(WritableArchive out) throws IOException {

        // Standard DDs
        writeStandardDeploymentDescriptors(out);

        // the rest...
        writeExtraDeploymentDescriptors(out);
    }

    /**
     * writes the standard deployment descriptors to an abstract archive
     *
     * @param out archive to write to
     */
    public void writeStandardDeploymentDescriptors(WritableArchive out) throws IOException {

        OutputStream os = out.putNextEntry(getDeploymentDescriptorPath());
        writeStandardDeploymentDescriptors(os);
        out.closeEntry();

        Descriptor desc = getDescriptor();

        // only bundle descriptor can have web services
        if (desc instanceof BundleDescriptor) {
            writeWebServicesDescriptors((BundleDescriptor) desc, out);
        }
    }

    /**
     * writes the runtime deployment descriptors to an abstract archive
     *
     * @param out output archive
     */
    public void writeRuntimeDeploymentDescriptors(WritableArchive out) throws IOException {

        T desc = getDescriptor();

        // Runtime DDs
        if (isHandlingRuntimeInfo()) {
            DeploymentDescriptorFile confDD = getConfigurationDDFile();
            if (confDD != null) {
                OutputStream os = out.putNextEntry(getRuntimeDeploymentDescriptorPath());
                confDD.write(desc, os);
                out.closeEntry();
            }
        }

    }

    /**
     * write all extra deployment descriptors (like cmp related and runtime dds)
     *
     * @out the abstract archive file to write to
     */
    protected void writeExtraDeploymentDescriptors(WritableArchive out) throws IOException {
        writeRuntimeDeploymentDescriptors(out);
    }

    /**
     * writes the standard deployment descriptor to an output stream
     *
     * @param os stream to write out the descriptors
     */
    public void writeStandardDeploymentDescriptors(OutputStream os) throws IOException {
        getStandardDDFile().write(getDescriptor(), os);
    }

    /**
     * writes de configuration deployment descriptor to a new XML file
     *
     * @param os stream to write the configuration deployment descriptors
     */
    public void writeRuntimeDeploymentDescriptors(OutputStream os) throws IOException {
        DeploymentDescriptorFile confDD = getConfigurationDDFile();
        if (confDD != null) {
            confDD.write(getDescriptor(), os);
        }
    }

    /**
     * Write web services related descriptors
     * @param desc the module descriptor
     * @param out the output archive
     */
    protected void writeWebServicesDescriptors(BundleDescriptor desc, WritableArchive out)
            throws IOException {
        if (desc.hasWebServices()) {
            DeploymentDescriptorFile webServicesDD = getWebServicesDDFile(desc);
            OutputStream os = out.putNextEntry(webServicesDD.getDeploymentDescriptorPath());
            webServicesDD.write(desc, os);
            out.closeEntry();
        }
    }

    /**
     * @return the location of the DeploymentDescriptor file for a
     *         particular type of J2EE Archive
     */
    public String getDeploymentDescriptorPath() {
        return getStandardDDFile().getDeploymentDescriptorPath();
    }

    /**
     * @return the location of the web services related deployment
     *         descriptor file inside this archive or null if this archive
     *         does not support webservices implementation.
     */
    public String getWebServicesDeploymentDescriptorPath() {
        return null;
    }

    /**
     * @return the location of the runtime deployment descriptor file
     *         for a particular type of J2EE Archive
     */
    public String getRuntimeDeploymentDescriptorPath() {
        DeploymentDescriptorFile ddFile = getConfigurationDDFile();
        if (ddFile != null) {
            return ddFile.getDeploymentDescriptorPath();
        } else {
            return null;
        }
    }

    /**
     * @return true if the passed Archive contains runtime
     *         deployment descriptors information
     */
    public boolean containsRuntimeDeploymentDescriptors(Archive in) {

        String ddFileName = getRuntimeDeploymentDescriptorPath();
        if (ddFileName == null) {
            return false;
        }
        for (Enumeration e = in.entries(); e.hasMoreElements();) {
            String entryName = (String) e.nextElement();
            if (entryName.equals(ddFileName)) {
                return true;
            }
        }
        // we iterated all archive elements, could not find our 
        // runtime DD file, it's a pure j2ee archive
        return false;
    }

    /**
     * Archivists can be associated with a module descriptor once the
     * XML deployment descriptors have been read and the DOL tree
     * is initialized.
     */
    public void setModuleDescriptor(ModuleDescriptor<T> module) {
        setDescriptor(module.getDescriptor());
        setManifest(module.getManifest());
    }

    /**
     * Perform Optional packages dependencies checking on an archive
     */
    public boolean performOptionalPkgDependenciesCheck(ReadableArchive archive) throws IOException {

        boolean dependenciesSatisfied = true;
        Manifest m = archive.getManifest();
        if (m != null) {
            dependenciesSatisfied = InstalledLibrariesResolver.resolveDependencies(m, archive.getURI().getSchemeSpecificPart());
        }
        // now check my libraries.
        Vector<String> libs = getLibraries(archive);
        if (libs != null) {
            for (String libUri : libs) {
                JarInputStream jis = null;
                try {
                    jis = new JarInputStream(archive.getEntry(libUri));
                    m = jis.getManifest();
                    if (m != null) {
                        if (!InstalledLibrariesResolver.resolveDependencies(m, libUri)) {
                            dependenciesSatisfied = false;
                        }
                    }
                } finally {
                    if (jis != null)
                        jis.close();
                }
            }
        }
        return dependenciesSatisfied;
    }

    public boolean supportsModuleType(XModuleType moduleType) {
        return getModuleType().equals(moduleType);
    }

    /**
     * @return the  module type handled by this archivist
     *         as defined in the application DTD
     */              
    public abstract XModuleType getModuleType();

    /**
     * @return the DeploymentDescriptorFile responsible for handling
     *         standard deployment descriptor
     */
    public abstract DeploymentDescriptorFile<T> getStandardDDFile();

    /**
     * @return if exists the DeploymentDescriptorFile responsible for
     *         handling the configuration deployment descriptors
     */
    public abstract DeploymentDescriptorFile getConfigurationDDFile();

    /**
     * @return a default BundleDescriptor for this archivist
     */
    public abstract T getDefaultBundleDescriptor();


    /**
     * @param desc the bundle descriptor
     * @return the DeploymentDescriptorFile responsible for
     *         handling the web services deployment descriptors
     */
    public DeploymentDescriptorFile getWebServicesDDFile(RootDeploymentDescriptor desc) {
        return new WebServicesDeploymentDescriptorFile(desc);
    }

    /**
     * @return The archive extension handled by a specific archivist
     */
    protected abstract String getArchiveExtension();

    /**
     * @return true if the archivist is handling the provided archive
     */
    protected abstract boolean postHandles(ReadableArchive archive) throws IOException;

    public boolean hasStandardDeploymentDescriptor(ReadableArchive archive)
            throws IOException {
        InputStream stIs = archive.getEntry(getDeploymentDescriptorPath());
        if (stIs != null) {
            stIs.close();
            return true;
        }
        return false;
    }

    public boolean hasRuntimeDeploymentDescriptor(ReadableArchive archive)
            throws IOException {

        //check null: since .par archive does not have runtime dds
        if (getConfigurationDDFile() != null) {
            InputStream runIs = archive.getEntry(
                    getConfigurationDDFile().getDeploymentDescriptorPath());
            if (runIs != null) {
                runIs.close();
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if this archivist is capable of handling the archive type
     *
     * @param archive the input archive
     * @return true if this archivist can handle this archive
     */
    public boolean handles(ReadableArchive archive) {
        try {
            //first, check the existence of any deployment descriptors
            if (hasStandardDeploymentDescriptor(archive) ||
                    hasRuntimeDeploymentDescriptor(archive)) {
                return true;
            }

            // Java EE 5 Specification: Section EE.8.4.2.1

            //second, check file extension if any, excluding .jar as it needs
            //additional processing
            String uri = archive.getURI().toString();
            File file = new File(archive.getURI());
            if (!file.isDirectory() && !uri.endsWith(Archivist.EJB_EXTENSION)) {
                if (uri.endsWith(getArchiveExtension())) {
                    return true;
                }
            }

            //finally, still not returned here, call for additional processing
            if (postHandles(archive)) {
                return true;
            }
        } catch (IOException e) {
            return false;
        }
        return false;
    }

    /**
     * creates a new module descriptor for this archivist
     *
     * @return the new module descriptor
     */
    public ModuleDescriptor createModuleDescriptor(T descriptor) {
        ModuleDescriptor newModule = descriptor.getModuleDescriptor();
        setDescriptor(descriptor);
        return newModule;
    }


    /**
     * print the current descriptor associated with this archivist
     */
    public void printDescriptor() {
        getDescriptor().visit((DescriptorVisitor) new TracerVisitor());
    }

    /**
     * sets if this archivist saves the runtime info
     *
     * @param handleRuntimeInfo to true to save the runtime info
     */
    public void setHandleRuntimeInfo(boolean handleRuntimeInfo) {
        this.handleRuntimeInfo = handleRuntimeInfo;
    }

    /**
     * @return true if this archivist will save the runtime info
     */
    public boolean isHandlingRuntimeInfo() {
        return handleRuntimeInfo;
    }

    /**
     * sets if this archivist process annotation
     *
     * @param annotationProcessingRequested to true to process annotation
     */
    public void setAnnotationProcessingRequested(
            boolean annotationProcessingRequested) {
        this.annotationProcessingRequested = annotationProcessingRequested;
    }

    /**
     * @return true if this archivist will process annotation
     */
    public boolean isAnnotationProcessingRequested() {
        return annotationProcessingRequested;
    }

    /**
     * sets annotation ErrorHandler for this archivist
     *
     * @param annotationErrorHandler
     */
    public void setAnnotationErrorHandler(ErrorHandler annotationErrorHandler) {
        this.annotationErrorHandler = annotationErrorHandler;
    }

    /**
     * @return annotation ErrorHandler of this archivist
     */
    public ErrorHandler getAnnotationErrorHandler() {
        return annotationErrorHandler;
    }

    /**
     * sets the manifest file for this archive
     *
     * @param m manifest to use at saving time
     */
    public void setManifest(Manifest m) {
        manifest = m;
    }

    /**
     * @return the manifest file for this archive
     */
    public Manifest getManifest() {
        return manifest;
    }

    /**
     * Sets the class-path for this archive
     *
     * @param newClassPath the new class-path
     */
    public void setClassPath(String newClassPath) {

        if (manifest == null) {
            manifest = new Manifest();
        }

        Attributes atts = manifest.getMainAttributes();
        atts.putValue(Attributes.Name.CLASS_PATH.toString(), newClassPath);
    }

    /**
     * @return the class-path as set in the manifest associated
     *         with the archive
     */
    public String getClassPath() {

        if (manifest == null) {
            return null;
        }

        Attributes atts = manifest.getMainAttributes();
        return atts.getValue(Attributes.Name.CLASS_PATH);
    }

    /**
     * @return a list of libraries included in the archivist
     */
    public Vector getLibraries(Archive archive) {

        Enumeration<String> entries = archive.entries();
        if (entries == null)
            return null;

        Vector libs = new Vector();
        while (entries.hasMoreElements()) {

            String entryName = entries.nextElement();
            if (entryName.indexOf('/') != -1) {
                continue; // not on the top level
            }
            if (entryName.endsWith(".jar")) {
                libs.add(entryName);
            }
        }
        return libs;
    }

    /**
     * @returns an entry name unique amongst names in this archive based on the triel name.
     */
    protected String getUniqueEntryFilenameFor(Archive archive, String trialName) throws IOException {
        Vector entriesNames = new Vector();
        Enumeration e = archive.entries();
        while (e != null && e.hasMoreElements()) {
            entriesNames.add(e.nextElement());
        }
        return Descriptor.createUniqueFilenameAmongst(trialName, entriesNames);
    }

    public void saveRuntimeInfo(File output) throws IOException {
        // if output file is null, we overwrite the current archive...
        File outputFile = output;
        if (outputFile == null) {
            outputFile = getTempFile(path);
        }

        // copy all entries from source to target except the 
        // runtime descriptor file
        WritableArchive out = archiveFactory.createArchive(outputFile);
        ReadableArchive in = archiveFactory.openArchive(new File(path));
        Vector skipFiles = new Vector();
        skipFiles.add(getRuntimeDeploymentDescriptorPath());
        copyInto(in, out, skipFiles);
        in.close();

        // now save the runtime deployment descriptor...
        OutputStream os = out.putNextEntry(getRuntimeDeploymentDescriptorPath());
        writeRuntimeDeploymentDescriptors(os);
        out.closeEntry();
        out.close();

        // if we overwrote the old archive, need to rename the tmp now
        if (output == null) {
            ReadableArchive finalArchive = archiveFactory.openArchive(new File(path));
            finalArchive.delete();
            ReadableArchive tmpArchive = archiveFactory.openArchive(outputFile);
            tmpArchive.renameTo(path);
        }

    }

    /**
     * apply runtimne info to this archive descriptors and saves it
     */
    public void applyRuntimeInfo(File runtimeDD, File output) throws IOException, SAXParseException {

        // update the runtime info
        getConfigurationDDFile().read(getDescriptor(), new FileInputStream(runtimeDD));

        // save the runtime info...
        saveRuntimeInfo(output);
    }

    /**
     * utility method to get a tmp file in the current user directory of the provided
     * directory
     *
     * @param fileOrDirPath path to a file or directory to use as temp location (use parent directory
     *             if a file is provided)
     */
    protected static File getTempFile(String fileOrDirPath) throws IOException {
        if (fileOrDirPath != null) {
            return getTempFile(new File(fileOrDirPath));
        } else {
            return getTempFile((File) null);
        }
    }

    /**
     * @return the list of files that should not be copied from the old archive
     *         when a save is performed.
     */
    public Vector getListOfFilesToSkip() {

        Vector filesToSkip = new Vector();
        filesToSkip.add(getDeploymentDescriptorPath());
        if (manifest != null) {
            filesToSkip.add(JarFile.MANIFEST_NAME);
        }
        if (getRuntimeDeploymentDescriptorPath() != null) {
            filesToSkip.add(getRuntimeDeploymentDescriptorPath());
        }

        // Can't depend on having a descriptor, so skip all possible
        // web service deployment descriptor paths.
        filesToSkip.addAll(WebServicesDeploymentDescriptorFile.
                getAllDescriptorPaths());

        return filesToSkip;
    }

    /**
     * utility method to get a tmp file in the current user directory of the provided
     * directory
     *
     * @param fileOrDir file or directory to use as temp location (use parent directory
     *             if a file is provided)
     */
    protected static File getTempFile(File fileOrDir) throws IOException {

        File dir = null;
        if (fileOrDir == null) {
            dir = new File(System.getProperty("user.dir"));
        } else {
            if (!fileOrDir.isDirectory()) {
                dir = fileOrDir.getParentFile();
                if (dir == null) {
                    dir = new File(System.getProperty("user.dir"));
                }
            } else {
                dir = fileOrDir;
            }
        }
        return File.createTempFile("tmp", ".jar", dir);
    }

    /**
     * add a file to an output abstract archive
     *
     * @param archive abstraction to use when adding the file
     * @param filePath to the file to add
     * @param entryName the entry name in the archive
     */
    protected static void addFileToArchive(WritableArchive archive, String filePath, String entryName)
            throws IOException {

        FileInputStream is = new FileInputStream(new File(filePath));
        OutputStream os = archive.putNextEntry(entryName);
        ArchivistUtils.copyWithoutClose(is, os);
        is.close();
        archive.closeEntry();
    }

    /**
     * copy all contents of a jar file to a new jar file except
     * for all the deployment descriptors files
     *
     * @param in  jar file
     * @param out jar file
     * @param ignoreList vector of entry name to not copy from to source jar file
     */
    protected void copyJarElements(ReadableArchive in, WritableArchive out, Vector ignoreList)
            throws IOException {

        Enumeration entries = in.entries();
        // we need to add the list of existing entries in the output
        // archive to the list of files to ignore to avoid any collision
        for (Enumeration outEntriesItr = out.entries(); outEntriesItr.hasMoreElements();) {
            if (ignoreList == null) {
                ignoreList = new Vector();
            }
            ignoreList.add(outEntriesItr.nextElement());
        }
        if (entries != null) {
            for (; entries.hasMoreElements();) {
                String anEntry = (String) entries.nextElement();
                if (ignoreList == null || !ignoreList.contains(anEntry)) {
                    InputStream is = in.getEntry(anEntry);
                    OutputStream os = out.putNextEntry(anEntry);
                    ArchivistUtils.copyWithoutClose(is, os);
                    is.close();
                    out.closeEntry();
                }
            }
        }
    }

    /**
     * rename a tmp file
     *
     * @param from name
     * @param to name
     */
    protected boolean renameTmp(String from, String to) throws IOException {

        ReadableArchive finalArchive = archiveFactory.openArchive(new File(to));
        finalArchive.delete();
        ReadableArchive tmpArchive = archiveFactory.openArchive(new File(from));
        boolean success = tmpArchive.renameTo(to);
        if (!success) {
            throw new IOException("Error renaming JAR");
        }
        return success;
    }

    /**
     * Sets the path for this archivist's archive file
     */
    public void setArchiveUri(String path) {
        this.path = path;
    }

    /**
     * @return the path for this archivist's archive file
     */
    public String getPath() {
        return path;
    }

    /**
     * Sets the classloader for this archivist
     *
     * @param classLoader  classLoader
     */
    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    /**
     * Gets the classloader for this archivist
     *
     * @return classLoader
     */
    public ClassLoader getClassLoader() {
        return classLoader;
    }

    /**
     * Turn on or off the XML Validation for all standard deployment
     * descriptors loading
     *
     * @param validate set to true to turn on XML validation
     */
    public void setXMLValidation(boolean validate) {
        isValidatingXML = validate;
    }

    /**
     * @return true if the Deployment Descriptors XML will be validated
     *         while reading.
     */
    public boolean getXMLValidation() {
        return isValidatingXML;
    }

    /**
     * Turn on or off the XML Validation for runtime deployment
     * descriptors loading
     *
     * @param validate set to true to turn on XML validation
     */
    public void setRuntimeXMLValidation(boolean validate) {
        isValidatingRuntimeXML = validate;
    }

    /**
     * @return true if the runtime XML will be validated
     *         while reading.
     */
    public boolean getRuntimeXMLValidation() {
        return isValidatingRuntimeXML;
    }

    /**
     * Sets the xml validation error reporting/recovering level.
     * The reporting level is active only when xml validation is
     * turned on @see setXMLValidation.
     * so far, two values can be passed, medium which reports the
     * xml validation and continue and full which reports the
     * xml validation and stop the xml parsing.
     */
    public void setXMLValidationLevel(String level) {
        validationLevel = level;
    }

    /**
     * @return the xml validation reporting level
     */
    public String getXMLValidationLevel() {
        return validationLevel;
    }

    /**
     * Sets the runtime xml validation error reporting/recovering level.
     * The reporting level is active only when xml validation is
     * turned on @see setXMLValidation.
     * so far, two values can be passed, medium which reports the
     * xml validation and continue and full which reports the
     * xml validation and stop the xml parsing.
     */
    public void setRuntimeXMLValidationLevel(String level) {
        runtimeValidationLevel = level;
    }

    /**
     * @return the runtime xml validation reporting level
     */
    public String getRuntimeXMLValidationLevel() {
        return runtimeValidationLevel;
    }

    /**
     * validates the DOL Objects associated with this archivist, usually
     * it requires that a class loader being set on this archivist or passed
     * as a parameter
     */
    public void validate(ClassLoader aClassLoader) {
    }

    /**
     * Copy this archivist to a new abstract archive
     *
     * @param target the new archive to use to copy our contents into
     */
    public void copyInto(WritableArchive target) throws IOException {
        ReadableArchive source = archiveFactory.openArchive(new File(path));
        copyInto(source, target);
    }

    /**
     * Copy source archivist to a target abstract archive.  By default,
     * every entry in source archive will be copied to the target archive,
     * including the manifest of the source archive.
     *
     * @param source        the source archive to copy from
     * @param target        the target archive to copy to
     */
    public void copyInto(ReadableArchive source, WritableArchive target) throws IOException {
        copyInto(source, target, null, true);
    }

    /**
     * Copy source archivist to a target abstract archive.  By default,
     * every entry in source archive will be copied to the target archive.
     *
     * @param source            the source archive to copy from
     * @param target            the target archive to copy to
     * @param overwriteManifest if true, the manifest in source archive
     *                          overwrites the one in target archive
     */
    public void copyInto(ReadableArchive source, WritableArchive target, boolean overwriteManifest) throws IOException {
        copyInto(source, target, null, overwriteManifest);
    }

    /**
     * Copy source archivist to a target abstract archive.  By default, the manifest
     * in source archive overwrites the one in target archive.
     *
     * @param source        the source archive to copy from
     * @param target        the target archive to copy to
     * @param entriesToSkip the entries that will be skipped by target archive
     */
    public void copyInto(ReadableArchive source, WritableArchive target, Vector entriesToSkip)
            throws IOException {
        copyInto(source, target, entriesToSkip, true);
    }

    /**
     * Copy this archivist to a new abstract archive
     *
     * @param source            the source archive to copy from
     * @param target            the target archive to copy to
     * @param entriesToSkip     the entries that will be skipped by target archive
     * @param overwriteManifest if true, the manifest in source archive
     *                          overwrites the one in target archive
     */
    public void copyInto(ReadableArchive source, WritableArchive target,
                         Vector entriesToSkip, boolean overwriteManifest)
            throws IOException {

        copyJarElements(source, target, entriesToSkip);
        if (overwriteManifest) {
            Manifest m = source.getManifest();
            if (m != null) {
                OutputStream os = target.putNextEntry(JarFile.MANIFEST_NAME);
                m.write(os);
                target.closeEntry();
            }
        }
    }

    /**
     * extract a entry of this archive to a file
     *
     * @param entryName entry name
     * @param out file to copy the entry into
     */
    public void extractEntry(String entryName, File out) throws IOException {
        ReadableArchive archive = archiveFactory.openArchive(new File(path));
        InputStream is = archive.getEntry(entryName);
        OutputStream os = new BufferedOutputStream(new FileOutputStream(out));
        ArchivistUtils.copy(new BufferedInputStream(is), os);
        archive.close();
    }

    // only copy the entry if the destination archive does not have this entry
    public void copyAnEntry(ReadableArchive in,
                                   WritableArchive out, String entryName) 
        throws IOException {
        InputStream is = null;
        InputStream is2 = null;
        ReadableArchive in2 = archiveFactory.openArchive(out.getURI());
        try {
            is = in.getEntry(entryName);
            is2 = in2.getEntry(entryName);
            if (is != null && is2 == null) {
                OutputStream os = out.putNextEntry(entryName);
                ArchivistUtils.copyWithoutClose(is, os);
            }
        } finally {
            /*
             *Close any streams that were opened.
             */
            in2.close();
            if (is != null) {
                is.close();
            }
            if (is2 != null) {
                is2.close();
            }
            out.closeEntry();
        }
    }

    public void copyStandardDeploymentDescriptors(ReadableArchive in,
                                                  WritableArchive out) throws IOException {
        String entryName = getDeploymentDescriptorPath();
        copyAnEntry(in, out, entryName);

        Descriptor desc = getDescriptor();

        // only bundle descriptor can have web services
        if (desc instanceof BundleDescriptor) {
            BundleDescriptor desc2 = (BundleDescriptor) desc;
            if (desc2.hasWebServices()) {
                DeploymentDescriptorFile webServicesDD =
                        getWebServicesDDFile((BundleDescriptor) desc2);
                String anEntry = webServicesDD.getDeploymentDescriptorPath();
                copyAnEntry(in, out, anEntry);
            }
        }
    }

    // copy wsdl and mapping files etc 
    public void copyExtraElements(ReadableArchive in,
                                         WritableArchive out) throws IOException {
        Enumeration entries = in.entries();
        if (entries != null) {
            for (; entries.hasMoreElements();) {
                String anEntry = (String) entries.nextElement();
                if (anEntry.endsWith(PERSISTENCE_DD_ENTRY)) {
                    // Don't copy persistence.xml file because they are some times
                    // bundled inside war/WEB-INF/lib/*.jar and hence we always
                    // read them from exploded directory.
                    // see Integration Notice #80587
                    continue;
                }
                if (anEntry.indexOf(WSDL) != -1 ||
                        anEntry.indexOf(XML) != -1 ||
                        anEntry.indexOf(XSD) != -1) {
                    copyAnEntry(in, out, anEntry);
                }
            }
        }
    }

    // for backward compat, we are not implementing those methods directly
    public Object readMetaInfo(ReadableArchive archive) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    protected boolean isProcessAnnotation(T descriptor) {
        // if the system property is set to process annotation for pre-JavaEE5
        // DD, the semantics of isFull flag is: full attribute is set to 
        // true in DD. Otherwise the semantics is full attribute set to 
        // true or it is pre-JavaEE5 DD.
        boolean isFull = false;
        if (processAnnotationForOldDD) {
            isFull = descriptor.isFullAttribute();
        } else {
            isFull = descriptor.isFullFlag();
        }

        // only process annotation when these two requirements satisfied:
        // 1. It is not a full deployment descriptor
        // 2. It is called through dynamic deployment
        return (!isFull && annotationProcessingRequested && classLoader != null);
    }
}
