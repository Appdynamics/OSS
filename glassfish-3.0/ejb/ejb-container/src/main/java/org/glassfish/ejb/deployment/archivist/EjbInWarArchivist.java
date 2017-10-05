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

package org.glassfish.ejb.deployment.archivist;

import com.sun.enterprise.deployment.archivist.*;
import com.sun.enterprise.deployment.EjbBundleDescriptor;
import com.sun.enterprise.deployment.RootDeploymentDescriptor;
import com.sun.enterprise.deployment.util.XModuleType;
import org.glassfish.ejb.deployment.annotation.impl.EjbInWarScanner;
import com.sun.enterprise.deployment.annotation.impl.ModuleScanner;
import com.sun.enterprise.deployment.io.DeploymentDescriptorFile;
import com.sun.enterprise.deployment.io.DescriptorConstants;
import com.sun.enterprise.deployment.io.EjbDeploymentDescriptorFile;
import com.sun.enterprise.deployment.io.runtime.EjbRuntimeDDFile;
import com.sun.hk2.component.Holder;
import org.glassfish.apf.Scanner;
import org.glassfish.api.deployment.archive.Archive;
import org.glassfish.api.deployment.archive.ReadableArchive;
import org.jvnet.hk2.annotations.Inject;
import org.jvnet.hk2.annotations.Scoped;
import org.jvnet.hk2.annotations.Service;
import org.jvnet.hk2.component.Habitat;
import org.jvnet.hk2.component.PerLookup;
import org.xml.sax.SAXParseException;

import javax.enterprise.deploy.shared.ModuleType;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * @author Mahesh Kannan
 */
@Service
@Scoped(PerLookup.class)
public class EjbInWarArchivist
        extends ExtensionsArchivist {

    @Inject
    Holder<EjbInWarScanner> scanner;

    
    /**
     * @return the DeploymentDescriptorFile responsible for handling
     *         standard deployment descriptor
     */
    @Override                                                  
    public DeploymentDescriptorFile getStandardDDFile(RootDeploymentDescriptor descriptor) {
        return new EjbDeploymentDescriptorFile() {
            public String getDeploymentDescriptorPath() {
                return "WEB-INF/ejb-jar.xml";  //TODO Add this to DescriptorConstants.class
            }
        };
    }

    @Override
    public DeploymentDescriptorFile getConfigurationDDFile(RootDeploymentDescriptor descriptor) {
        return new EjbRuntimeDDFile() {
            public String getDeploymentDescriptorPath() {
                return "WEB-INF/" + "sun-" + "ejb-jar.xml"; //TODO Add this to DescriptorConstants.class
            }
        };
    }

    /**
     * Returns the scanner for this archivist, usually it is the scanner registered
     * with the same module type as this archivist, but subclasses can return a
     * different version
     *
     */
    @Override
    public ModuleScanner getScanner() {
        return scanner.get();
    }


    @Override
    public boolean supportsModuleType(XModuleType moduleType) {
        return XModuleType.WAR ==moduleType;
    }

    @Override
    public XModuleType getModuleType() {
        return XModuleType.EjbInWar;
    }

    public RootDeploymentDescriptor getDefaultDescriptor() {
        return new EjbBundleDescriptor();
    }
}

