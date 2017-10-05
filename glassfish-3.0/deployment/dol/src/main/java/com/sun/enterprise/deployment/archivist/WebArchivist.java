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

import com.sun.enterprise.deployment.Application;
import com.sun.enterprise.deployment.OrderingDescriptor;
import com.sun.enterprise.deployment.WebBundleDescriptor;
import com.sun.enterprise.deployment.WebFragmentDescriptor;
import com.sun.enterprise.deployment.RootDeploymentDescriptor;
import com.sun.enterprise.deployment.annotation.impl.ModuleScanner;
import com.sun.enterprise.deployment.annotation.impl.WarScanner;
import com.sun.enterprise.deployment.io.DeploymentDescriptorFile;
import com.sun.enterprise.deployment.io.DescriptorConstants;
import com.sun.enterprise.deployment.io.WebDeploymentDescriptorFile;
import com.sun.enterprise.deployment.io.runtime.WebRuntimeDDFile;
import com.sun.enterprise.deployment.util.*;
import com.sun.logging.LogDomains;
import org.glassfish.api.deployment.archive.Archive;
import org.glassfish.api.deployment.archive.ReadableArchive;
import org.glassfish.api.admin.ServerEnvironment;
import org.glassfish.deployment.common.DeploymentUtils;
import org.jvnet.hk2.annotations.Scoped;
import org.jvnet.hk2.annotations.Service;
import org.jvnet.hk2.annotations.Inject;
import org.jvnet.hk2.component.PerLookup;
import org.xml.sax.SAXParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.net.URL;


/**
 * This module is responsible for reading and write web applications
 * archive files (war).
 *
 * @author  Jerome Dochez
 * @version
 */
@Service
@Scoped(PerLookup.class)
public class WebArchivist extends Archivist<WebBundleDescriptor> {


    private static final String DEFAULT_WEB_XML = "default-web.xml";

    @Inject
    ServerEnvironment env;

    /**
     * The DeploymentDescriptorFile handlers we are delegating for XML i/o
     */
    DeploymentDescriptorFile standardDD = new WebDeploymentDescriptorFile();

    private WebBundleDescriptor defaultWebXmlBundleDescriptor = null;

    /**
     * @return the  module type handled by this archivist
     * as defined in the application DTD
     *
     */
    @Override
    public XModuleType getModuleType() {
        return XModuleType.WAR;
    }

    /**
     * Archivist read XML deployment descriptors and keep the
     * parsed result in the DOL descriptor instances. Sets the descriptor
     * for a particular Archivst type
     */
    public void setDescriptor(Application descriptor) {
        java.util.Set webBundles = descriptor.getWebBundleDescriptors();
        if (webBundles.size()>0) {
            this.descriptor = (WebBundleDescriptor) webBundles.iterator().next();
            if (this.descriptor.getModuleDescriptor().isStandalone())
                return;
            else
                this.descriptor=null;
        }
    }

    /**
     * @return the location of the web services related deployment
     * descriptor file inside this archive or null if this archive
     * does not support webservices implementation.
     */
    @Override
    public String getWebServicesDeploymentDescriptorPath() {
        return DescriptorConstants.WEB_WEBSERVICES_JAR_ENTRY;
    }

    /**
     * @return the DeploymentDescriptorFile responsible for handling
     * standard deployment descriptor
     */
    @Override
    public DeploymentDescriptorFile getStandardDDFile() {
        return standardDD;
    }

    /**
     * @return if exists the DeploymentDescriptorFile responsible for
     * handling the configuration deployment descriptors
     */
    @Override
    public DeploymentDescriptorFile getConfigurationDDFile() {
        return new WebRuntimeDDFile();
    }

    /**
     * @return a default BundleDescriptor for this archivist
     */
    @Override
    public WebBundleDescriptor getDefaultBundleDescriptor() {
        return new WebBundleDescriptor();
    }

    /**
     * @return a validated WebBundleDescriptor corresponding to default-web.xml
     *         that can be used in webtier.
     */
    public synchronized WebBundleDescriptor getDefaultWebXmlBundleDescriptor() {
        if (defaultWebXmlBundleDescriptor == null) {
            defaultWebXmlBundleDescriptor = getPlainDefaultWebXmlBundleDescriptor();
            ApplicationValidator validator = new ApplicationValidator();
            validator.accept(defaultWebXmlBundleDescriptor );
        }
        return defaultWebXmlBundleDescriptor ;
    }

    /**
     * @return a non-validated WebBundleDescriptor corresponding to default-web.xml
     */
    private WebBundleDescriptor getPlainDefaultWebXmlBundleDescriptor() {
        WebBundleDescriptor defaultWebBundleDesc = new WebBundleDescriptor();
        InputStream fis = null;

        try {
            // parse default-web.xml contents
            URL defaultWebXml = getDefaultWebXML();
            if (defaultWebXml!=null)  {
                fis = defaultWebXml.openStream();
                WebDeploymentDescriptorFile wddf =
                    new WebDeploymentDescriptorFile();
                wddf.setXMLValidation(false);
                defaultWebBundleDesc.addWebBundleDescriptor(wddf.read(fis));
            }
        } catch (Exception e) {
            LogDomains.getLogger(WebArchivist.class, LogDomains.WEB_LOGGER).
                warning("Error in parsing default-web.xml");
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException ioe) {
                // do nothing
            }
        }
        return defaultWebBundleDesc;
    }

    /**
     * Obtains the location of <tt>default-web.xml</tt>.
     * This allows subclasses to load the file from elsewhere.
     *
     * @return
     *      null if not found, in which case the default web.xml will not be read
     *      and <tt>web.xml</tt> in the applications need to have everything.
     */
    protected URL getDefaultWebXML() throws IOException {
        File file = new File(env.getConfigDirPath(),DEFAULT_WEB_XML);
        if (file.exists())
            return file.toURI().toURL();
        else
            return null;
    }


    /**
     * perform any post deployment descriptor reading action
     *
     * @param descriptor the deployment descriptor for the module
     * @param archive the module archive
     */
    @Override
    protected void postOpen(WebBundleDescriptor descriptor, ReadableArchive archive)
        throws IOException
    {
        super.postOpen(descriptor, archive);
        WebBundleDescriptor webBundle = (WebBundleDescriptor) descriptor;
        ModuleContentValidator mdv = new ModuleContentValidator(archive);
        webBundle.visit(mdv);
    }

    /**
     * validates the DOL Objects associated with this archivist, usually
     * it requires that a class loader being set on this archivist or passed
     * as a parameter
     */
    @Override
    public void validate(ClassLoader aClassLoader) {
        ClassLoader cl = aClassLoader;
        if (cl==null) {
            cl = classLoader;
        }
        if (cl==null) {
            return;
        }
        descriptor.setClassLoader(cl);
        descriptor.visit((WebBundleVisitor) new ApplicationValidator());
    }

    /**
     * In the case of web archive, the super handles() method should be able
     * to make a unique identification.  If not, then the archive is definitely
     * not a war.
     */
    @Override
    protected boolean postHandles(ReadableArchive abstractArchive)
            throws IOException {
        return DeploymentUtils.isWebArchive(abstractArchive);
    }

    @Override
    protected String getArchiveExtension() {
        return WEB_EXTENSION;
    }

    /**
     * @return a list of libraries included in the archivist
     */
    public Vector getLibraries(Archive archive) {

        Enumeration<String> entries = archive.entries();
        if (entries==null)
            return null;

        Vector libs = new Vector();
        while (entries.hasMoreElements()) {

            String entryName = entries.nextElement();
            if (!entryName.startsWith("WEB-INF/lib")) {
                continue; // not in WEB-INF...
            }
            if (entryName.endsWith(".jar")) {
                libs.add(entryName);
            }
        }
        return libs;
    }

    @Override
    protected void postAnnotationProcess(WebBundleDescriptor descriptor,
            ReadableArchive archive) throws IOException {
        super.postAnnotationProcess(descriptor, archive);

        // read web-fragment.xml
        List<WebFragmentDescriptor> wfList = readStandardFragments(descriptor, archive);

        // process annotations in web-fragment
        // extension annotation processing will be done in top level
        if (isProcessAnnotation(descriptor)) {
            Map<ExtensionsArchivist, RootDeploymentDescriptor> localExtensions =
                    new HashMap<ExtensionsArchivist, RootDeploymentDescriptor>();
            for (WebFragmentDescriptor wfDesc : wfList) {
                super.readAnnotations(archive, wfDesc, localExtensions);
            }

            // scan manifest classpath
            ModuleScanner scanner = getScanner();
            if (scanner instanceof WarScanner) {
                ((WarScanner)scanner).setScanOtherLibraries(true);
                readAnnotations(archive, descriptor, localExtensions, scanner);
            }
        }

        WebFragmentDescriptor mergedWebFragment = null;
        for (WebFragmentDescriptor wf : wfList) {
            if (mergedWebFragment == null) {
                mergedWebFragment = wf;
            } else {
                mergedWebFragment.addWebBundleDescriptor(wf);
            }
        }

        if (mergedWebFragment != null) {
            descriptor.addWebBundleDescriptor(mergedWebFragment);
        }

        // apply default from default-web.xml to web.xml
        WebBundleDescriptor defaultWebBundleDescriptor = getPlainDefaultWebXmlBundleDescriptor();
        if (defaultWebBundleDescriptor != null) {
            descriptor.addDefaultWebBundleDescriptor(defaultWebBundleDescriptor);
        }
    }

    /**
     * This method will return the list of web fragment in the desired order.
     */
    private List<WebFragmentDescriptor> readStandardFragments(WebBundleDescriptor descriptor,
            ReadableArchive archive) throws IOException {

        List<WebFragmentDescriptor> wfList = new ArrayList<WebFragmentDescriptor>();
        Vector libs = getLibraries(archive);
        if (libs != null && libs.size() > 0) {

            for (int i = 0; i < libs.size(); i++) {
                String lib = (String)libs.get(i);
                Archivist wfArchivist = new WebFragmentArchivist();
                wfArchivist.initializeContext(this);
                wfArchivist.setRuntimeXMLValidation(this.getRuntimeXMLValidation());
                wfArchivist.setRuntimeXMLValidationLevel(
                        this.getRuntimeXMLValidationLevel());
                wfArchivist.setAnnotationProcessingRequested(false);

                WebFragmentDescriptor wfDesc = null;
                ReadableArchive embeddedArchive = archive.getSubArchive(lib);
                try {
                    if (embeddedArchive != null &&
                            wfArchivist.hasStandardDeploymentDescriptor(embeddedArchive)) {
                        try {
                            wfDesc = (WebFragmentDescriptor)wfArchivist.open(embeddedArchive);
                        } catch(SAXParseException ex) {
                            IOException ioex = new IOException();
                            ioex.initCause(ex);
                            throw ioex;
                        }
                    } else {   
                        wfDesc = new WebFragmentDescriptor();
                    }
                } finally {
                    if (embeddedArchive != null) {
                        embeddedArchive.close();
                    }
                }
                wfDesc.setJarName(lib.substring(lib.lastIndexOf('/') + 1));    
                wfList.add(wfDesc);

                descriptor.putJarNameWebFragmentNamePair(wfDesc.getJarName(), wfDesc.getName());

            }

            if (descriptor.getAbsoluteOrderingDescriptor() != null) {
                wfList = descriptor.getAbsoluteOrderingDescriptor().order(wfList);
            } else {
                OrderingDescriptor.sort(wfList);
            }

            for (WebFragmentDescriptor wf : wfList) {
                descriptor.addOrderedLib(wf.getJarName());
            }
        }

        return wfList;
    }
}
