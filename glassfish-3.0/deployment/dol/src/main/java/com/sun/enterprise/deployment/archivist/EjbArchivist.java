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
import com.sun.enterprise.deployment.EjbBundleDescriptor;
import com.sun.enterprise.deployment.annotation.introspection.EjbComponentAnnotationScanner;
import com.sun.enterprise.deployment.io.DeploymentDescriptorFile;
import com.sun.enterprise.deployment.io.DescriptorConstants;
import com.sun.enterprise.deployment.io.EjbDeploymentDescriptorFile;
import com.sun.enterprise.deployment.io.runtime.EjbRuntimeDDFile;
import com.sun.enterprise.deployment.util.*;
import com.sun.enterprise.util.LocalStringManagerImpl;
import org.glassfish.api.deployment.archive.ReadableArchive;
import org.glassfish.deployment.common.DeploymentUtils;
import org.xml.sax.SAXParseException;

import javax.enterprise.deploy.shared.ModuleType;
import java.io.IOException;
import java.util.Set;
import java.util.logging.Level;

import org.jvnet.hk2.annotations.Scoped;
import org.jvnet.hk2.annotations.Service;
import org.jvnet.hk2.component.PerLookup;

/**
 * This class is responsible for handling J2EE EJB Bundlearchive files.
 *
 * @author  Jerome Dochez
 * @version
 */
@Service
@Scoped(PerLookup.class)
public class EjbArchivist extends Archivist<EjbBundleDescriptor> {

    /**
     * The DeploymentDescriptorFile handlers we are delegating for XML i/o
     */
    DeploymentDescriptorFile standardDD = new EjbDeploymentDescriptorFile();

    // resources...
    private static LocalStringManagerImpl localStrings =
	    new LocalStringManagerImpl(EjbArchivist.class);


    /**
     * @return the  module type handled by this archivist
     * as defined in the application DTD
     *
     */
    @Override
    public XModuleType getModuleType() {
        return XModuleType.EJB;
    }

    /**
     * Set the DOL descriptor  for this Archivist, used by super classes
     */
    public void setDescriptor(Application descriptor) {
        // this is acceptable if the application actually represents
        // a standalone module
        Set ejbBundles = ((Application) descriptor).getEjbBundleDescriptors();
        if (ejbBundles.size()>0) {
            this.descriptor = (EjbBundleDescriptor) ejbBundles.iterator().next();
            if (this.descriptor.getModuleDescriptor().isStandalone())
                return;
            else
                this.descriptor=null;
        }
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
        return new EjbRuntimeDDFile();
    }

    /**
     * @return the location of the web services related deployment
     * descriptor file inside this archive or null if this archive
     * does not support webservices implementation.
     */
    @Override
    public String getWebServicesDeploymentDescriptorPath() {
        return DescriptorConstants.EJB_WEBSERVICES_JAR_ENTRY;
    }

    /**
     * @return a default BundleDescriptor for this archivist
     */
    @Override
    public EjbBundleDescriptor getDefaultBundleDescriptor() {
        return  new EjbBundleDescriptor();
    }

    /**
     * perform any post deployment descriptor reading action
     *
     * @param descriptor deployment descriptor for the module
     * @param archive the module archive
     */
    @Override
    protected void postOpen(EjbBundleDescriptor descriptor, ReadableArchive archive)
        throws IOException
    {
        super.postOpen(descriptor, archive);
        EjbBundleDescriptor ejbBundle = (EjbBundleDescriptor) descriptor;
        ModuleContentValidator mdv = new ModuleContentValidator(archive);
        ejbBundle.visit((EjbBundleVisitor)mdv);
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
        descriptor.visit(new EjbBundleValidator());
    }

    @Override
    protected String getArchiveExtension() {
        return EJB_EXTENSION;
    }

    @Override
    protected boolean postHandles(ReadableArchive abstractArchive)
            throws IOException {
        AnnotationDetector detector =
                    new AnnotationDetector(new EjbComponentAnnotationScanner());
        return (!DeploymentUtils.isWebArchive(abstractArchive)) &&
                detector.hasAnnotationInArchive(abstractArchive);
    }
}
