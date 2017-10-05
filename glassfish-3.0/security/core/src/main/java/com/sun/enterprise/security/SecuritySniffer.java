/*
 * 
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 2008 Sun Microsystems, Inc. All rights reserved.
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
package com.sun.enterprise.security;

import com.sun.enterprise.deployment.annotation.introspection.EjbComponentAnnotationScanner;
import org.glassfish.api.deployment.archive.ReadableArchive;
import org.glassfish.deployment.common.DeploymentUtils;
import org.glassfish.internal.deployment.GenericSniffer;
import org.jvnet.hk2.annotations.Service;
import org.jvnet.hk2.annotations.Inject;
import org.jvnet.hk2.component.Habitat;
import org.jvnet.hk2.component.Inhabitant;

import java.util.logging.Logger;
import java.io.IOException;

import com.sun.enterprise.module.Module;
import java.lang.annotation.Annotation;

/**
 * SecuritySniffer for security related activities
 */
@Service(name="Security")
public class SecuritySniffer extends GenericSniffer {

    final String[] containers = { "com.sun.enterprise.security.SecurityContainer" };

    @Inject
    Habitat habitat;
    
    Inhabitant<SecurityLifecycle> lifecycle;
    private static final Class[] ejbAnnotations = new Class[]{
        javax.ejb.Stateless.class, javax.ejb.Stateful.class,
        javax.ejb.MessageDriven.class, javax.ejb.Singleton.class
    };

    public SecuritySniffer() {
        super("security", "WEB-INF/web.xml", null);
    }

   /**
     * Returns true if the passed file or directory is recognized by this
     * instance.
     *
     * @param location the file or directory to explore
     * @param loader class loader for this application
     * @return true if this sniffer handles this application type
     */
    public boolean handles(ReadableArchive location, ClassLoader loader) {
        return (DeploymentUtils.isWebArchive(location) || DeploymentUtils.isEAR(location) ||
                isJar(location));
    }

    /**
     * Sets up the container libraries so that any imported bundle from the
     * connector jar file will now be known to the module subsystem
     * <p/>
     * This method returns a {@link com.sun.enterprise.module.ModuleDefinition} for the module containing
     * the core implementation of the container. That means that this module
     * will be locked as long as there is at least one module loaded in the
     * associated container.
     *
     * @param containerHome is where the container implementation resides
     * @param logger        the logger to use
     * @return the module definition of the core container implementation.
     * @throws java.io.IOException exception if something goes sour
     */
    @Override
     public Module[] setup(String containerHome, Logger logger) throws IOException {
        lifecycle = habitat.getInhabitantByType(SecurityLifecycle.class);
        lifecycle.get();
        return null;
    }

    /**
     * Tears down a container, remove all imported libraries from the module
     * subsystem.
     */
    @Override
     public void tearDown() {
        if (lifecycle!=null) {
            lifecycle.release();
        }
    }

    /**
     * Returns the list of Containers that this Sniffer enables.
     * <p/>
     * The runtime will look up each container implementing
     * using the names provided in the habitat.
     *
     * @return list of container names known to the habitat for this sniffer
     */
    public String[] getContainersNames() {
        return containers;
    }

    @Override
    public Class<? extends Annotation>[] getAnnotationTypes() {
        return ejbAnnotations;
    }
    
    private boolean isJar(ReadableArchive location) {
        // check for ejb-jar.xml
        boolean result = false;
        try {
                result = location.exists("META-INF/ejb-jar.xml");
            } catch (IOException ioEx) {
                //TODO
            }
        return result;
    }
     
}
