/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2008 Sun Microsystems, Inc. All rights reserved.
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
 *
 *
 * This file incorporates work covered by the following copyright and
 * permission notice:
 *
 * Copyright 2004 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.catalina.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;
import java.util.logging.*;
import javax.naming.Binding;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;

import org.apache.catalina.core.StandardContext;
import org.apache.naming.resources.Resource;


/**
 * Ensures that all extension dependies are resolved for a WEB application
 * are met. This class builds a master list of extensions available to an
 * applicaiton and then validates those extensions.
 *
 * See http://java.sun.com/j2se/1.4/docs/guide/extensions/spec.html for
 * a detailed explanation of the extension mechanism in Java.
 *
 * @author Greg Murray
 * @author Justyna Horwat
 * @version $Revision: 1.3 $ $Date: 2006/03/12 01:27:08 $
 *
 */
public final class ExtensionValidator {

    private static Logger log = Logger.getLogger(ExtensionValidator.class.getName());

    /**
     * The string resources for this package.
     */
    private static StringManager sm =
        StringManager.getManager("org.apache.catalina.util");
    
    private static HashMap containerAvailableExtensions = null;
    private static ArrayList containerManifestResources = new ArrayList();
    private static ResourceBundle messages = null;


    // ----------------------------------------------------- Static Initializer


    /**
     *  This static initializer loads the container level extensions that are
     *  available to all web applications. This method scans all extension 
     *  directories available via the "java.ext.dirs" System property. 
     *
     *  The System Class-Path is also scanned for jar files that may contain 
     *  available extensions.
     */
    static {

        // check for container level optional packages
        String systemClasspath = System.getProperty("java.class.path");

        StringTokenizer strTok = new StringTokenizer(systemClasspath, 
                                                     File.pathSeparator);

        // build a list of jar files in the classpath
        while (strTok.hasMoreTokens()) {
            String classpathItem = strTok.nextToken();
            if (classpathItem.toLowerCase().endsWith(".jar")) {
                File item = new File(classpathItem);
                if (item.exists()) {
                    try {
                        addSystemResource(item);
                    } catch (IOException e) {
                        log.log(Level.SEVERE,
                                sm.getString("extensionValidator.failload",
                                             item),
                                e);
                    }
                }
            }
        }

        // get the files in the extensions directory
        String extensionsDir = System.getProperty("java.ext.dirs");
        if (extensionsDir != null) {
            StringTokenizer extensionsTok
                = new StringTokenizer(extensionsDir, File.pathSeparator);
            while (extensionsTok.hasMoreTokens()) {
                File targetDir = new File(extensionsTok.nextToken());
                if (!targetDir.exists() || !targetDir.isDirectory()) {
                    continue;
                }
                File[] files = targetDir.listFiles();
                for (int i = 0; i < files.length; i++) {
                    if (files[i].getName().toLowerCase().endsWith(".jar")) {
                        try {
                            addSystemResource(files[i]);
                        } catch (IOException e) {
                            log.log(Level.SEVERE,
                                    sm.getString("extensionValidator.failload",
                                                 files[i]),
                                    e);
                        }
                    }
                }
            }
        }

    }


    // --------------------------------------------------------- Public Methods


    /**
     * Runtime validation of a Web Applicaiton.
     *
     * This method uses JNDI to look up the resources located under a 
     * <code>DirContext</code>. It locates Web Application MANIFEST.MF 
     * file in the /META-INF/ directory of the application and all 
     * MANIFEST.MF files in each JAR file located in the WEB-INF/lib 
     * directory and creates an <code>ArrayList</code> of 
     * <code>ManifestResorce<code> objects. These objects are then passed 
     * to the validateManifestResources method for validation.
     *
     * @param dirContext The JNDI root of the Web Application
     * @param context The context from which the Logger and path to the
     *                application
     *
     * @return true if all required extensions satisfied
     */
    public static synchronized boolean validateApplication(
                                           DirContext dirContext, 
                                           StandardContext context)
                    throws IOException {

        String appName = context.getPath();
        ArrayList appManifestResources = new ArrayList();
        ManifestResource appManifestResource = null;
        // If the application context is null it does not exist and 
        // therefore is not valid
        if (dirContext == null) return false;
        // Find the Manifest for the Web Applicaiton
        InputStream inputStream = null;
        try {
            NamingEnumeration wne = dirContext.listBindings("/META-INF/");
            Binding binding = (Binding) wne.nextElement();
            if (binding.getName().toUpperCase().equals("MANIFEST.MF")) {
                Resource resource = (Resource)dirContext.lookup
                                    ("/META-INF/" + binding.getName());
                inputStream = resource.streamContent();
                Manifest manifest = new Manifest(inputStream);
                inputStream.close();
                inputStream = null;
                ManifestResource mre = new ManifestResource
                    (sm.getString("extensionValidator.web-application-manifest"),
                    manifest, ManifestResource.WAR);
                appManifestResources.add(mre);
            } 
        } catch (NamingException nex) {
            // Application does not contain a MANIFEST.MF file
        } catch (NoSuchElementException nse) {
            // Application does not contain a MANIFEST.MF file
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Throwable t) {
                    // Ignore
                }
            }
        }

        // Locate the Manifests for all bundled JARs
        NamingEnumeration ne = null;
        try {
            if (dirContext != null) {
                ne = dirContext.listBindings("WEB-INF/lib/");
            }
            while ((ne != null) && ne.hasMoreElements()) {
                Binding binding = (Binding)ne.nextElement();
                if (!binding.getName().toLowerCase().endsWith(".jar")) {
                    continue;
                }
                Object obj =
                    dirContext.lookup("/WEB-INF/lib/" + binding.getName());
                if (!(obj instanceof Resource)) {
                    // Probably a directory named xxx.jar - ignore it
                    continue;
                }
                Resource resource = (Resource) obj;
                Manifest jmanifest = getManifest(resource.streamContent());
                if (jmanifest != null) {
                    ManifestResource mre = new ManifestResource(
                                                binding.getName(),
                                                jmanifest, 
                                                ManifestResource.APPLICATION);
                    appManifestResources.add(mre);
                }
            }
        } catch (NamingException nex) {
            // Jump out of the check for this application because it 
            // has no resources
        }

        return validateManifestResources(appName, appManifestResources);
    }


    // -------------------------------------------------------- Private Methods


    /**
     * Validates a <code>ArrayList</code> of <code>ManifestResource</code> 
     * objects. This method requires an application name (which is the 
     * context root of the application at runtime).  
     *
     * <code>false</false> is returned if the extension dependencies
     * represented by any given <code>ManifestResource</code> objects 
     * is not met.
     *
     * This method should also provide static validation of a Web Applicaiton 
     * if provided with the necessary parameters.
     *
     * @param appName The name of the Application that will appear in the 
     *                error messages
     * @param resources A list of <code>ManifestResource</code> objects 
     *                  to be validated.
     *
     * @return true if manifest resource file requirements are met
     */
    private static boolean validateManifestResources(String appName, 
                                                     ArrayList resources) {
        boolean passes = true;
        int failureCount = 0;        
        HashMap availableExtensions = null;

        Iterator it = resources.iterator();
        while (it.hasNext()) {
            ManifestResource mre = (ManifestResource)it.next();
            ArrayList requiredList = mre.getRequiredExtensions();
            if (requiredList == null) {
                continue;
            }

            // build the list of available extensions if necessary
            if (availableExtensions == null) {
                availableExtensions = buildAvailableExtensionsMap(resources);
            }

            // load the container level resource map if it has not been built
            // yet
            if (containerAvailableExtensions == null) {
                containerAvailableExtensions
                    = buildAvailableExtensionsMap(containerManifestResources);
            }

            // iterate through the list of required extensions
            Iterator rit = requiredList.iterator();
            while (rit.hasNext()) {
                Extension requiredExt = (Extension)rit.next();
                String extId = requiredExt.getUniqueId();
                // check the applicaion itself for the extension
                if (availableExtensions != null
                                && availableExtensions.containsKey(extId)) {
                   Extension targetExt = (Extension)
                       availableExtensions.get(extId);
                   if (targetExt.isCompatibleWith(requiredExt)) {
                       requiredExt.setFulfilled(true);
                   }
                // check the container level list for the extension
                } else if (containerAvailableExtensions != null
                        && containerAvailableExtensions.containsKey(extId)) {
                   Extension targetExt = (Extension)
                       containerAvailableExtensions.get(extId);
                   if (targetExt.isCompatibleWith(requiredExt)) {
                       requiredExt.setFulfilled(true);
                   }
                } else {
                    // Failure
                    log.info(sm.getString(
                        "extensionValidator.extension-not-found-error",
                        appName, mre.getResourceName(),
                        requiredExt.getExtensionName()));
                    passes = false;
                    failureCount++;
                }
            }
        }

        if (!passes) {
            log.info(sm.getString(
                     "extensionValidator.extension-validation-error", appName,
                     failureCount + ""));
        }

        return passes;
    }
    
   /* 
    * Build this list of available extensions so that we do not have to 
    * re-build this list every time we iterate through the list of required 
    * extensions. All available extensions in all of the 
    * <code>MainfestResource</code> objects will be added to a 
    * <code>HashMap</code> which is returned on the first dependency list
    * processing pass. 
    *
    * The key is the name + implementation version.
    *
    * NOTE: A list is built only if there is a dependency that needs 
    * to be checked (performance optimization).
    *
    * @param resources A list of <code>ManifestResource</code> objects
    *
    * @return HashMap Map of available extensions
    */
    private static HashMap buildAvailableExtensionsMap(ArrayList resources) {

        HashMap availableMap = null;

        Iterator it = resources.iterator();
        while (it.hasNext()) {
            ManifestResource mre = (ManifestResource)it.next();
            HashMap map = mre.getAvailableExtensions();
            if (map != null) {
                Iterator values = map.values().iterator();
                while (values.hasNext()) {
                    Extension ext = (Extension) values.next();
                    if (availableMap == null) {
                        availableMap = new HashMap();
                        availableMap.put(ext.getUniqueId(), ext);
                    } else if (!availableMap.containsKey(ext.getUniqueId())) {
                        availableMap.put(ext.getUniqueId(), ext);
                    }
                }
            }
        }

        return availableMap;
    }
    
    /**
     * Return the Manifest from a jar file or war file
     *
     * @param inStream Input stream to a WAR or JAR file
     * @return The WAR's or JAR's manifest
     */
    private static Manifest getManifest(InputStream inStream)
            throws IOException {

        Manifest manifest = null;
        JarInputStream jin = null;

        try {
            jin = new JarInputStream(inStream);
            manifest = jin.getManifest();
            jin.close();
            jin = null;
        } finally {
            if (jin != null) {
                try {
                    jin.close();
                } catch (Throwable t) {
                    // Ignore
                }
            }
        }

        return manifest;
    }
    
    /*
     * Checks to see if the given system JAR file contains a MANIFEST, and adds
     * it to the container's manifest resources.
     *
     * @param jarFile The system JAR whose manifest to add
     */
    private static void addSystemResource(File jarFile) throws IOException {

        Manifest manifest = getManifest(new FileInputStream(jarFile));
        if (manifest != null)  {
            ManifestResource mre
                = new ManifestResource(jarFile.getAbsolutePath(),
                                       manifest,
                                       ManifestResource.SYSTEM);
            containerManifestResources.add(mre);
        }
    }

}


