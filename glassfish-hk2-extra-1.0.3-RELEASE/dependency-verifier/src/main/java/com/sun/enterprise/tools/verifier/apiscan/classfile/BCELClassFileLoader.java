/*
 * Copyright (c) 1997, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.enterprise.tools.verifier.apiscan.classfile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * *This is a factory for {@link BCELClassFile}. This is not a public class, as
 * I expect users to use {@link ClassFileLoaderFactory} interface. This class
 * internally uses the the standard Java ClassLoader to load the resource and
 * construct BCELClassFile object out of it.
 *
 * @author Sanjeeb.Sahoo@Sun.COM
 */
class BCELClassFileLoader implements ClassFileLoader {

    private ResourceFinder rf;
    private static String resourceBundleName = "com.sun.enterprise.tools.verifier.apiscan.LocalStrings";
    private static Logger logger = Logger.getLogger("apiscan.classfile", resourceBundleName); // NOI18N
    private final static String myClassName = "BCELClassFileLoader"; // NOI18N

    /**
     * Creates a new instance of BCELClassFileLoader.
     *
     * @param cp that will be used to create a new java.net.URLClassLoader. In
     *           subsequent load operations, this classloader will be used.
     */
    public BCELClassFileLoader(String cp) {
        ArrayList<URL> urls = new ArrayList<URL>();
        for (StringTokenizer st = new StringTokenizer(cp, File.pathSeparator);
             st.hasMoreTokens();) {
            String entry = st.nextToken();
            try {
                // toURI().toURL() takes care of all the escape characters
                // in the absolutePath. The toURI() method encodes all escape
                // characters. Later URLClassLoader decodes and regenerates 
                // correct escape characters.
                urls.add(new File(entry).toURI().toURL());
            } catch (MalformedURLException e) {
                logger.logp(Level.WARNING, myClassName, "init<>", getClass().getName() + ".exception1", new Object[]{entry});
                logger.log(Level.WARNING, "", e);
            }
        }
        //We do not want system class loader or even extension class loadera s our parent.
        //We want only boot class loader as our parent. Boot class loader is represented as null.
        final ClassLoader cl = new URLClassLoader((URL[]) urls.toArray(new URL[0]), null);
        rf = new ClassLoaderBasedResourceFinder(cl);
    }

    /**
     * Creates a new instance of BCELClassFileLoader.
     *
     * @param cl is the classloader that will be used in subsequent load
     *           operations.
     */
    public BCELClassFileLoader(ClassLoader cl) {
        rf = new ClassLoaderBasedResourceFinder(cl);
    }

    public BCELClassFileLoader(ResourceFinder rf) {
        this.rf = rf;
    }

    //See corresponding method of ClassFileLoader
    public ClassFile load(String externalClassName) throws IOException {
        logger.entering("BCELClassFileLoader", "load", externalClassName); // NOI18N
        String resourcePath = externalClassName.replace('.', '/') + ".class";
        InputStream is = rf.findResourceAsStream(resourcePath);
        if (is == null) {
            throw new IOException(resourcePath + " is not found using " + rf);
        }
        try {
            ClassFile cf = new BCELClassFile(is, resourcePath); // NOI18N
            matchClassSignature(cf, externalClassName);
            return cf;
        } finally {
            is.close();
        }
    }

    //This method is neede to be protected against users who are passing us
    //internal class names instead of external class names or
    //when the file actually represents some other class, but it isnot 
    //available in in proper package hierarchy.
    private void matchClassSignature(ClassFile cf, String externalClassName)
            throws IOException {
        String nameOfLoadedClass = cf.getName();
        if (!nameOfLoadedClass.equals(externalClassName)) {
            throw new IOException(externalClassName + ".class represents " +
                    cf.getName() +
                    ". Perhaps your package name is incorrect or you passed the " +
                    "name using internal form instead of using external form.");
        }
    }
}
