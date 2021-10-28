/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package com.sun.enterprise.module.common_impl;

import com.sun.enterprise.module.ManifestConstants;
import com.sun.enterprise.module.ModuleDefinition;
import com.sun.enterprise.module.ModuleDependency;
import com.sun.enterprise.module.ModuleMetadata;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.logging.Logger;

/**
 * {@link ModuleDefinition} implementation that picks up most of the module
 * properties from the manifest file of the jar, as baked in by
 * the hk2-maven-plugin.
 *
 * @author Jerome Dochez
 */
public class DefaultModuleDefinition implements ModuleDefinition {
    
    private final String name;
    private final String version;
    private final String[] publicPkgs;
    protected final List<ModuleDependency> dependencies = new ArrayList<ModuleDependency>();
    protected final List<URI> classPath = new ArrayList<URI>();
    private final String importPolicy;
    private final String lifecyclePolicy;
    private final Manifest manifest;
    /**
     * Main attributes section of the manifest.
     * Always non-null.
     */
    protected final Attributes mainAttributes;
    /**
     * Metadata that works like index.
     */
    private final ModuleMetadata metadata = new ModuleMetadata();

    /** TO DO need to support a URI constructor */
    public DefaultModuleDefinition(File location) throws IOException {
        this(location, null);
    }

    // Sahoo changed it to public so that it can be used by osgi-adapter
    public DefaultModuleDefinition(File location, Attributes attr) throws IOException {

        classPath.add(location.toURI());
        
        Jar jarFile = Jar.create(location);
        Manifest m = jarFile.getManifest();
        if(m==null) m = EMPTY_MANIFEST;
        manifest = m;

        if (attr==null)
            attr = manifest.getMainAttributes();

        // no attributes whatsoever, I just use an empty collection to avoid 
        // testing for null all the time.
        if (attr==null) {
            attr = new Attributes();
        }
        this.mainAttributes = attr;
                
        // name
        if (attr.getValue(ManifestConstants.BUNDLE_NAME)!=null) {
            name = attr.getValue(ManifestConstants.BUNDLE_NAME);
        } else {
            name = location.getName();
        }

        // classpath
        parseClassPath(attr, location.toURI());
        
        // class exported...
        String exported = attr.getValue(ManifestConstants.PKG_EXPORT_NAME);
        ArrayList<String> tmpList = new ArrayList<String>();
        for( String token : new Tokenizer(exported,",")) {
            tmpList.add(token);
        }
        publicPkgs = tmpList.toArray(new String[tmpList.size()]);
        
        importPolicy = attr.getValue(ManifestConstants.IMPORT_POLICY);
        lifecyclePolicy = attr.getValue(ManifestConstants.LIFECYLE_POLICY);
        
        version = "1.0"; // for now ;-)

        parseAttributes(attr);

        jarFile.loadMetadata(metadata);
    }

    /**
     * Parses <tt>{@value ManifestConstants#CLASS_PATH}</tt> from manifest attributes
     * and updates URI list.
     */
    protected void parseClassPath(Attributes attr, URI baseURI) throws IOException {
        String classpath = attr.getValue(ManifestConstants.CLASS_PATH);
        for( String classpathElement : new Tokenizer(classpath," ")) {
            classpathElement = decorateClassPath(classpathElement);
            URI result;
            File ref = new File(classpathElement);
            if (!ref.isAbsolute()) {
                try {
                    result = baseURI.resolve(classpathElement);
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Unable to parse Class-Path entry '"+classpath+"' in "+baseURI);
                }
            } else
                result = ref.toURI();

            assert testClassPath(result);

            // look for /META-INF/services for classloader punch-in
            File file = new File(result);
            if (file.exists()) {
                Jar jar = Jar.create(file);
                Manifest m = jar.getManifest();
                if ((m != null) && (m.getMainAttributes().getValue("HK2-Bundle-Name")==null)) {
                    // if Class-Path refers to other HK2 modules, ignore loading metadata
                    // When someone makes HK2 module bootable from Java (that is, to have the main method),
                    // it often needs to refer to other jars. The parent-first classloader delegation
                    // helps us avoid loading dupliates, but metadata needs to be ignored here.
                    jar.loadMetadata(metadata);
                }
                classPath.add(result);
            } else {
                // even if the pointed resource doesn't exist, don't complain by default,
                // which is consistent with ClassLoader behavior of silently ignoring URLs
                // that doesn't resolve.
                // When the assertion is on, testClassPath above would do the warning.
            }
        }
    }

    /**
     * Optional error diagnostics performed during the development time
     * to check if the URL pointed by the path actually exists.
     */
    private boolean testClassPath(URI uri) {
        try {
            if(uri.getScheme().equals("file")) {
                URLConnection c = uri.toURL().openConnection();
                if(c.getContentLength()==-1)
                    LOGGER.warning(uri+" pointed from "+name+" in classpath doesn't exist");
            }
        } catch (IOException e) {
            throw new AssertionError(e); // file is a valid URL
        }
        return true; // assertion should always succeed.
    }

    /**
     * Extension point to manipulate the classpath element before it's parsed.
     * @see #parseClassPath(Attributes,URI) 
     */
    protected String decorateClassPath(String classpathElement) {
        return classpathElement;
    }

    /**
     * Extensibility point to parse more information from Manifest attributes.
     *
     * @param attr
     *      Main attributes of the manifest. Always non-null.
     */
    protected void parseAttributes(Attributes attr) {
        // noop
    }

    /**
     * Returns the name of the module
     * @return the name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Returns the list of classes and packages that have been designated as
     * public interfaces of this module.
     * @return the list of public packages or classes
     */
    public String[] getPublicInterfaces() {
        return publicPkgs.clone();
    }
    
    /**
     * Returns the list of module dependencies
     * @return the ModuleDependency
     */
    public ModuleDependency[] getDependencies() {
        return dependencies.toArray(new ModuleDependency[dependencies.size()]);
    }
    
    /**
     * Returns the list of URI locations forming the classpath for this module.
     *
     * @return the list of URI locations for this module
     */
    public URI[] getLocations() {
        return classPath.toArray(new URI[classPath.size()]);
    }
    
    /**
     * Returns the module's version
     * @return the module's version
     */
    public String getVersion() {
        return version;
    }
    
    /**
     * Returns the class name implementing the
     * {@link com.sun.enterprise.module.ImportPolicy ImportPolicy} interface for this module or
     * null if there is no such implementation
     * @return the  {@link com.sun.enterprise.module.ImportPolicy ImportPolicy} implementation class name
     */
    public String getImportPolicyClassName() {
        return importPolicy;
    }
    
    /**
     * Returns the class name implementing the
     * {@link com.sun.enterprise.module.LifecyclePolicy LifecyclePolicy} interface for this module or
     * null if there is no such implementation
     * @return the  {@link com.sun.enterprise.module.LifecyclePolicy LifecyclePolicy} implementation class name
     */
    public String getLifecyclePolicyClassName() {
        return lifecyclePolicy;
    }
    
    /**
     * Returns the manifest file from the module's implementation jar file
     *
     * @return
     *      never null.
     */
    public Manifest getManifest() {
        return manifest;
    }

    public ModuleMetadata getMetadata() {
        return metadata;
    }

    /**
     * Assists debugging.
     */
    public String toString() {
        return name+':'+version;
    }

    private static final Manifest EMPTY_MANIFEST = new Manifest();

    private static final Logger LOGGER = Logger.getLogger(DefaultModuleDefinition.class.getName());
}
