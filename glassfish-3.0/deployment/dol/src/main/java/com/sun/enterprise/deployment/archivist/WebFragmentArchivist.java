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
import com.sun.enterprise.deployment.WebFragmentDescriptor;
import com.sun.enterprise.deployment.io.DeploymentDescriptorFile;
import com.sun.enterprise.deployment.io.DescriptorConstants;
import com.sun.enterprise.deployment.io.WebFragmentDeploymentDescriptorFile;
import com.sun.enterprise.deployment.util.ApplicationValidator;
import com.sun.enterprise.deployment.util.ModuleContentValidator;
import com.sun.enterprise.deployment.util.WebBundleVisitor;
import com.sun.enterprise.deployment.util.XModuleType;
import org.glassfish.api.deployment.archive.Archive;
import org.glassfish.api.deployment.archive.ReadableArchive;
import org.xml.sax.SAXParseException;

import javax.enterprise.deploy.shared.ModuleType;
import java.io.IOException;
import java.util.Vector;


/**
 * This module is responsible for reading and write web fragment
 * archive files (jar).
 * This is not a @Service, does not have @Scoped(PerLookup.class)
 * and does not implements PrivateArchivist as it will not be
 * looked up through ArchivistFactory.
 *
 * @author  Shing Wai Chan
 * @version 
 */
class WebFragmentArchivist extends Archivist<WebFragmentDescriptor> {

    /** 
     * The DeploymentDescriptorFile handlers we are delegating for XML i/o
     */
    DeploymentDescriptorFile standardDD = new WebFragmentDeploymentDescriptorFile();

    /**
     * @return the  module type handled by this archivist
     * as defined in the application DTD
     *
     */
    @Override
    public XModuleType getModuleType() {
        return null;
    }        

    /**
     * Archivist read XML deployment descriptors and keep the
     * parsed result in the DOL descriptor instances. Sets the descriptor
     * for a particular Archivst type
     */
    public void setDescriptor(Application descriptor) {
        this.descriptor = null;
    }  
    
    /** 
     * @return the location of the web services related deployment 
     * descriptor file inside this archive or null if this archive
     * does not support webservices implementation.
     */
    @Override
    public String getWebServicesDeploymentDescriptorPath() {
        return null;
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
        return null;
    }      
    
    /**
     * @return a default BundleDescriptor for this archivist
     */
    @Override
    public WebFragmentDescriptor getDefaultBundleDescriptor() {
        return new WebFragmentDescriptor();
    }

    /**
     * perform any post deployment descriptor reading action
     *
     * @param descriptor the deployment descriptor for the module
     * @param archive the module archive
     */
    @Override
    protected void postOpen(WebFragmentDescriptor descriptor, ReadableArchive archive)
        throws IOException
    {
        super.postOpen(descriptor, archive);
        WebFragmentDescriptor webFragment = (WebFragmentDescriptor) descriptor;
        ModuleContentValidator mdv = new ModuleContentValidator(archive);
        webFragment.visit(mdv);
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
        return false;
    }

    @Override
    protected String getArchiveExtension() {
        return WEB_FRAGMENT_EXTENSION;
    }
    
    /**
     * @return a list of libraries included in the archivist
     */
    public Vector getLibraries(Archive archive) {
        return null;
    }
}
