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

package org.glassfish.internal.deployment;

import com.sun.enterprise.module.ModulesRegistry;
import com.sun.enterprise.module.ModuleDefinition;
import com.sun.enterprise.module.Module;
import java.io.ByteArrayOutputStream;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import org.glassfish.api.container.Sniffer;
import org.glassfish.api.deployment.archive.ReadableArchive;
import org.jvnet.hk2.annotations.Inject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.stream.events.StartDocument;

/**
 * Generic implementation of the Sniffer service that can be programmatically instantiated
 *
 * @author Jerome Dochez
 */
public abstract class GenericSniffer implements Sniffer {

    @Inject
    protected ModulesRegistry modulesRegistry;

    final private String containerName;
    final private String appStigma;
    final private String urlPattern;
    
    final private static XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();

    public GenericSniffer(String containerName, String appStigma, String urlPattern) {
        this.containerName = containerName;
        this.appStigma = appStigma;
        this.urlPattern = urlPattern;
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
        if (appStigma != null) {
            try {
                if (location.exists(appStigma)) {
                    return true;
                }
            } catch (IOException e) {
                // ignore
            }
        }
        return false;
    }

    /**
     * Returns the pattern to apply against the request URL
     * If the pattern matches the URL, the service method of the associated
     * container will be invoked
     *
     * @return pattern instance
     */
    public String[] getURLPatterns() {
        if (urlPattern!=null) {
            return new String[] {urlPattern};
        } else {
            return null;
        }
    }

    /**
     * Returns the container name associated with this sniffer
     *
     * @return the container name
     */
    public String getModuleType() {
        return containerName;
    }

   /**
     * Sets up the container libraries so that any imported bundle from the
     * connector jar file will now be known to the module subsystem
     *
     * This method returns a {@link ModuleDefinition} for the module containing
     * the core implementation of the container. That means that this module
     * will be locked as long as there is at least one module loaded in the
     * associated container.
     *
     * @param containerHome is where the container implementation resides
     * @param logger the logger to use
     * @return the module definition of the core container implementation.
     *
     * @throws java.io.IOException exception if something goes sour
     */
    public Module[] setup(String containerHome, Logger logger) throws IOException {
       return null;
    }

    /**
     * Tears down a container, remove all imported libraries from the module
     * subsystem.
     * 
     */
    public void tearDown() {
    }

    /**
     * Returns the list of annotations types that this sniffer is interested in.
     * If an application bundle contains at least one class annotated with
     * one of the returned annotations, the deployment process will not
     * call the handles method but will invoke the containers deployers as if
     * the handles method had been called and returned true.
     *
     * @return list of annotations this sniffer is interested in.
     */
    public Class<? extends Annotation>[] getAnnotationTypes() {
        return new Class[0];
    }

    /**
     * @return whether this sniffer should be visible to user
     *
     */
    public boolean isUserVisible() {
        return false;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Sniffer) {
            Sniffer otherSniffer = (Sniffer)other;
            return getModuleType().equals(otherSniffer.getModuleType());
        } 
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + (getModuleType() != null ? getModuleType().hashCode() : 0);
        return hash;
    }
    
    /**
     * Returns a map of deployment configurations composed by reading from a
     * list of paths in the readable archive.  (For Java EE applications the
     * deployment configurations correspond to the deployment descriptors.)  The 
     * {@link #getDeploymentConfigurationPaths} method returns this list of paths
     * which might exist in archives that this sniffer handles.
     * <p>
     * In each returned map entry the key is a path and the value is the
     * contents of the archive entry at that path.  This method creates a map
     * entry only if the path exists in the readable archive.
     * <p>
     * Sniffers for applications that do not store their configurations as
     * deployment descriptors at predictable paths within an archive are free
     * to override this implementation to return whatever information is 
     * appropriate to that application type.  A key usage of the returned
     * Map is in the application type's GUI plug-in (if desired) to allow 
     * users to customize the deployment configuration after the application
     * has been deployed.  The concrete Sniffer implementation and the
     * GUI plug-in must agree on the conventions for storing deployment
     * configuration inforation in the Map.
     * 
     * @param location the readable archive for the application of interest
     * @return a map from path names to the contents of the archive entries
     * at those paths
     * @throws java.io.IOException in case of errors retrieving an entry or 
     * reading the archive contents at an entry
     */
    public Map<String,String> getDeploymentConfigurations(final ReadableArchive location) throws IOException {
        final Map<String,String> deploymentConfigs = new HashMap<String,String>();

        for (String path : getDeploymentConfigurationPaths()) {
            InputStream is = null;
            try {
                is = location.getEntry(path);
                if (is != null) {
                    String dc = readDeploymentConfig(is);
                    deploymentConfigs.put(path, dc);
                }
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        }
        return deploymentConfigs;
    }

    /**
     * Returns a list of paths within an archive that represents deployment
     * configuration files.  
     * <p>
     * Sniffers that recognize Java EE applications typically override this
     * default implementation to return a list of the deployment descriptors
     * that might appear in the type of Java EE application which the sniffer
     * recognizes.  For example, the WebSniffer implementation of this method
     * returns WEB-INF/web.xml and WEB-INF/sun-web.xml.
     * 
     * @return list of paths in the archive where deployment configuration
     * archive entries might exist
     */
    protected List<String> getDeploymentConfigurationPaths() {
        return Collections.EMPTY_LIST;
    }

    /**
     * @return the set of the sniffers that should not co-exist for the
     * same module. For example, ejb and appclient sniffers should not
     * be returned in the sniffer list for a certain module.
     * This method will be used to validate and filter the retrieved sniffer
     * lists for a certain module
     *
     */
    public String[] getIncompatibleSnifferTypes() {
        return null;
    }

    private String readDeploymentConfig(final InputStream is) throws IOException {
        String encoding = null;
        try {
            is.mark(Integer.MAX_VALUE);
            XMLEventReader rdr = xmlInputFactory.createXMLEventReader(
                    new InputStreamReader(is));
            while (rdr.hasNext()) {
                final XMLEvent ev = rdr.nextEvent();
                if (ev.isStartDocument()) {
                    final StartDocument sd = (StartDocument) ev;
                    encoding = sd.getCharacterEncodingScheme();
                    rdr.close();
                    break;
                }
            }
        } catch (XMLStreamException e) {
            throw new IOException(e);
        }
        if (encoding == null) {
            encoding = "UTF-8";
        }
        is.reset();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int bytesRead;
        final byte [] buffer = new byte[1024];
        while (( bytesRead = is.read(buffer)) != -1 ) {
            baos.write(buffer, 0, bytesRead);
        }
        is.close();
        return new String(baos.toByteArray(), encoding);
    }
}
