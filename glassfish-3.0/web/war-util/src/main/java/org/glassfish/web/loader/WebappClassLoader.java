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




package org.glassfish.web.loader;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.*;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.jar.Attributes.Name;

import javax.naming.Binding;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;

import org.apache.naming.JndiPermission;
import org.apache.naming.resources.DirContextURLStreamHandler;
import org.apache.naming.resources.Resource;
import org.apache.naming.resources.ResourceAttributes;
import org.apache.naming.resources.FileDirContext;
import org.glassfish.api.deployment.InstrumentableClassLoader;
import org.jvnet.hk2.component.PreDestroy;

import com.sun.appserv.server.util.PreprocessorUtil;

import com.sun.appserv.BytecodePreprocessor;
import com.sun.appserv.ClassLoaderUtil;
import com.sun.logging.LogDomains;

/**
 * Specialized web application class loader.
 * <p>
 * This class loader is a full reimplementation of the
 * <code>URLClassLoader</code> from the JDK. It is desinged to be fully
 * compatible with a normal <code>URLClassLoader</code>, although its internal
 * behavior may be completely different.
 * <p>
 * <strong>IMPLEMENTATION NOTE</strong> - This class loader faithfully follows
 * the delegation model recommended in the specification. The system class
 * loader will be queried first, then the local repositories, and only then
 * delegation to the parent class loader will occur. This allows the web
 * application to override any shared class except the classes from J2SE.
 * Special handling is provided from the JAXP XML parser interfaces, the JNDI
 * interfaces, and the classes from the servlet API, which are never loaded
 * from the webapp repository.
 * <p>
 * <strong>IMPLEMENTATION NOTE</strong> - Due to limitations in Jasper
 * compilation technology, any repository which contains classes from
 * the servlet API will be ignored by the class loader.
 * <p>
 * <strong>IMPLEMENTATION NOTE</strong> - The class loader generates source
 * URLs which include the full JAR URL when a class is loaded from a JAR file,
 * which allows setting security permission at the class level, even when a
 * class is contained inside a JAR.
 * <p>
 * <strong>IMPLEMENTATION NOTE</strong> - Local repositories are searched in
 * the order they are added via the initial constructor and/or any subsequent
 * calls to <code>addRepository()</code> or <code>addJar()</code>.
 * <p>
 * <strong>IMPLEMENTATION NOTE</strong> - No check for sealing violations or
 * security is made unless a security manager is present.
 *
 * @author Remy Maucherat
 * @author Craig R. McClanahan
 * @version $Revision: 1.1.2.1 $ $Date: 2007/08/17 15:46:27 $
 */
public class WebappClassLoader
    extends URLClassLoader
    implements Reloader, InstrumentableClassLoader, PreDestroy
{
    // ------------------------------------------------------- Static Variables

    private static final Logger logger = LogDomains.getLogger(
        WebappClassLoader.class, LogDomains.WEB_LOGGER);

    private static final ResourceBundle rb = logger.getResourceBundle();

    /**
     * Set of package names which are not allowed to be loaded from a webapp
     * class loader without delegating first.
     */
    private static final String[] packageTriggers = {
        "javax",                                     // Java extensions
        // START PE 4985680
        "sun",                                       // Sun classes
        // END PE 4985680
        "org.xml.sax",                               // SAX 1 & 2
        "org.w3c.dom",                               // DOM 1 & 2
        "org.apache.taglibs.standard",               // JSTL (Java EE 5)
        "com.sun.faces",                             // JSF (Java EE 5)
        "org.apache.commons.logging"                 // Commons logging
    };

    public static final boolean ENABLE_CLEAR_REFERENCES = 
        Boolean.valueOf(System.getProperty("org.apache.catalina.loader.WebappClassLoader.ENABLE_CLEAR_REFERENCES", "true")).booleanValue();

    /**
     * All permission.
     */
    private static final Permission ALL_PERMISSION = new AllPermission();


    // ----------------------------------------------------- Instance Variables

    // START PE 4989455
    /**
     * Use this variable to invoke the security manager when a resource is
     * loaded by this classloader.
     */
    private boolean packageDefinitionEnabled =
         System.getProperty("package.definition") == null ? false : true;
    // END OF PE 4989455

    /**
     * Associated directory context giving access to the resources in this
     * webapp.
     */
    protected DirContext resources = null;

    /**
     * The cache of ResourceEntry for classes and resources we have loaded,
     * keyed by resource name.
     */
    protected ConcurrentHashMap<String, ResourceEntry> resourceEntries =
        new ConcurrentHashMap<String, ResourceEntry>();

    /**
     * The list of not found resources.
     */
    protected ConcurrentHashMap<String, String> notFoundResources =
        new ConcurrentHashMap<String, String>();

    /**
     * The debugging detail level of this component.
     */
    protected int debug = 0;

    /**
     * Should this class loader delegate to the parent class loader
     * <strong>before</strong> searching its own repositories (i.e. the
     * usual Java2 delegation model)?  If set to <code>false</code>,
     * this class loader will search its own repositories first, and
     * delegate to the parent only if the class or resource is not
     * found locally.
     */
    protected boolean delegate = false;

    /**
     * Last time a JAR was accessed.
     */
    protected long lastJarAccessed = 0L;

    /**
     * The list of local repositories, in the order they should be searched
     * for locally loaded classes or resources.
     */
    protected String[] repositories = new String[0];

    /**
     * Repositories URLs, used to cache the result of getURLs.
     */
    protected URL[] repositoryURLs = null;

    /**
     * Repositories translated as path in the work directory (for Jasper
     * originally), but which is used to generate fake URLs should getURLs be
     * called.
     */
    protected File[] files = new File[0];

    /**
     * The list of JARs, in the order they should be searched
     * for locally loaded classes or resources.
     */
    protected JarFile[] jarFiles = new JarFile[0];

    /**
     * The list of JARs, in the order they should be searched
     * for locally loaded classes or resources.
     */
    protected File[] jarRealFiles = new File[0];

    /**
     * The path which will be monitored for added Jar files.
     */
    protected String jarPath = null;

    /**
     * The list of JARs, in the order they should be searched
     * for locally loaded classes or resources.
     */
    protected List<String> jarNames = new ArrayList<String>();

    /**
     * The list of JARs last modified dates, in the order they should be
     * searched for locally loaded classes or resources.
     */
    protected long[] lastModifiedDates = new long[0];

    /**
     * The list of resources which should be checked when checking for
     * modifications.
     */
    protected String[] paths = new String[0];

    /**
     * A list of read File and Jndi Permission's required if this loader
     * is for a web application context.
     */
    private ConcurrentLinkedQueue<Permission> permissionList =
        new ConcurrentLinkedQueue<Permission>();

    /**
     * Path where resources loaded from JARs will be extracted.
     */
    private File loaderDir = null;

    /**
     * The PermissionCollection for each CodeSource for a web
     * application context.
     */
    private ConcurrentHashMap<String, PermissionCollection> loaderPC =
        new ConcurrentHashMap<String, PermissionCollection>();

    /**
     * Instance of the SecurityManager installed.
     */
    private SecurityManager securityManager = null;

    /**
     * The parent class loader.
     */
    private ClassLoader parent = null;

    /**
     * The system class loader.
     */
    private ClassLoader system = null;

    /**
     * Has this component been started?
     */
    protected boolean started = false;

    /**
     * Has external repositories.
     */
    protected boolean hasExternalRepositories = false;

    // START SJSAS 6344989
    /**
     * List of byte code pre-processors per webapp class loader.
     */
    private ConcurrentLinkedQueue<BytecodePreprocessor> byteCodePreprocessors =
            new ConcurrentLinkedQueue<BytecodePreprocessor>();
    // END SJSAS 6344989

    private boolean useMyFaces;

    // START PE 4985680
    /**
     * List of packages that may always be overridden, regardless of whether
     * they belong to a protected namespace (i.e., a namespace that may never
     * be overridden by any webapp)
     */
    private ConcurrentLinkedQueue<String> overridablePackages;
    // END PE 4985680


    // ----------------------------------------------------------- Constructors

    /**
     * Construct a new ClassLoader with no defined repositories and no
     * parent ClassLoader.
     */
    public WebappClassLoader() {
        super(new URL[0]);
        init();
    }


    /**
     * Construct a new ClassLoader with the given parent ClassLoader,
     * but no defined repositories.
     */
    public WebappClassLoader(ClassLoader parent) {
        super(new URL[0], parent);
        init();
    }


    /**
     * Construct a new ClassLoader with the given parent ClassLoader
     * and defined repositories.
     */
    public WebappClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
        init();
    }


    // ------------------------------------------------------------- Properties

    protected class PrivilegedFindResource
        implements PrivilegedAction<ResourceEntry> {

        private File file;
        private String path;

        PrivilegedFindResource(File file, String path) {
            this.file = file;
            this.path = path;
        }

        public ResourceEntry run() {
            return findResourceInternal(file, path);
        }
    }


    protected final class PrivilegedGetClassLoader
        implements PrivilegedAction<ClassLoader> {

        public Class<?> clazz;

        public PrivilegedGetClassLoader(Class<?> clazz){
            this.clazz = clazz;
        }

        public ClassLoader run() {       
            return clazz.getClassLoader();
        }           
    }

    // START PE 4985680
    /**
     * Adds the given package name to the list of packages that may always be
     * overriden, regardless of whether they belong to a protected namespace
     */
    public synchronized void addOverridablePackage(String packageName){
        if (overridablePackages == null){
            overridablePackages = new ConcurrentLinkedQueue<String>();
        }
        overridablePackages.add(packageName);
    }
    // END PE 4985680


    /**
     * Get associated resources.
     */
    public DirContext getResources() {
        return this.resources;
    }


    /**
     * Set associated resources.
     */
    public void setResources(DirContext resources) {
        this.resources = resources;
    }


    public ConcurrentHashMap<String, ResourceEntry> getResourceEntries() {
        return resourceEntries;
    }


    /**
     * Return the debugging detail level for this component.
     */
    public int getDebug() {
        return (this.debug);
    }


    /**
     * Set the debugging detail level for this component.
     *
     * @param debug The new debugging detail level
     */
    public void setDebug(int debug) {

        this.debug = debug;

    }


    /**
     * Return the "delegate first" flag for this class loader.
     */
    public boolean getDelegate() {

        return (this.delegate);

    }


    /**
     * Set the "delegate first" flag for this class loader.
     *
     * @param delegate The new "delegate first" flag
     */
    public void setDelegate(boolean delegate) {

        this.delegate = delegate;

    }

    /**
     * If there is a Java SecurityManager create a read FilePermission
     * or JndiPermission for the file directory path.
     *
     * @param path file directory path
     */
    public void addPermission(String path) {
        if (path == null) {
            return;
        }

        if (securityManager != null) {
            Permission permission = null;
            if( path.startsWith("jndi:") || path.startsWith("jar:jndi:") ) {
                if (!path.endsWith("/")) {
                    path = path + "/";
                }
                permission = new JndiPermission(path + "*");
                addPermission(permission);
            } else {
                if (!path.endsWith(File.separator)) {
                    permission = new FilePermission(path, "read");
                    addPermission(permission);
                    path = path + File.separator;
                }
                permission = new FilePermission(path + "-", "read");
                addPermission(permission);
            }
        }
    }


    /**
     * If there is a Java SecurityManager create a read FilePermission
     * or JndiPermission for URL.
     *
     * @param url URL for a file or directory on local system
     */
    public void addPermission(URL url) {
        if (url != null) {
            addPermission(url.toString());
        }
    }


    /**
     * If there is a Java SecurityManager create a Permission.
     *
     * @param permission permission to add
     */
    public void addPermission(Permission permission) {
        if ((securityManager != null) && (permission != null)) {
            permissionList.add(permission);
        }
    }


    /**
     * Return the JAR path.
     */
    public String getJarPath() {

        return this.jarPath;

    }


    /**
     * Change the Jar path.
     */
    public void setJarPath(String jarPath) {

        this.jarPath = jarPath;

    }


    /**
     * Change the work directory.
     */
    public void setWorkDir(File workDir) {
        this.loaderDir = new File(workDir, "loader_" + this.hashCode());
    }


    public void setUseMyFaces(boolean useMyFaces) {
        this.useMyFaces = useMyFaces;
        if (useMyFaces) {
            addOverridablePackage("javax.faces");
            addOverridablePackage("com.sun.faces");
        }
    }


    // ------------------------------------------------------- Reloader Methods


    /**
     * Add a new repository to the set of places this ClassLoader can look for
     * classes to be loaded.
     *
     * @param repository Name of a source of classes to be loaded, such as a
     *  directory pathname, a JAR file pathname, or a ZIP file pathname
     *
     * @exception IllegalArgumentException if the specified repository is
     *  invalid or does not exist
     */
    public void addRepository(String repository) {

        // Ignore any of the standard repositories, as they are set up using
        // either addJar or addRepository
        if (repository.startsWith("/WEB-INF/lib")
            || repository.startsWith("/WEB-INF/classes"))
            return;

        // Add this repository to our underlying class loader
        try {
            addRepository(new URL(repository));
        } catch (MalformedURLException e) {
            IllegalArgumentException iae = new IllegalArgumentException
                ("Invalid repository: " + repository);
            iae.initCause(e);
            throw iae;
        }

    }

    public void addRepository(URL url) {
        super.addURL(url);
        hasExternalRepositories = true;
    }

    /**
     * Add a new repository to the set of places this ClassLoader can look for
     * classes to be loaded.
     *
     * @param repository Name of a source of classes to be loaded, such as a
     *  directory pathname, a JAR file pathname, or a ZIP file pathname
     *
     * @exception IllegalArgumentException if the specified repository is
     *  invalid or does not exist
     */
    public synchronized void addRepository(String repository, File file) {

        // Note : There should be only one (of course), but I think we should
        // keep this a bit generic

        if (repository == null)
            return;

        if (logger.isLoggable(Level.FINER))
            logger.finer("addRepository(" + repository + ")");

        int i;

        // Add this repository to our internal list
        String[] result = new String[repositories.length + 1];
        for (i = 0; i < repositories.length; i++) {
            result[i] = repositories[i];
        }
        result[repositories.length] = repository;
        repositories = result;

        // Add the file to the list
        File[] result2 = new File[files.length + 1];
        for (i = 0; i < files.length; i++) {
            result2[i] = files[i];
        }
        result2[files.length] = file;
        files = result2;

    }


    public synchronized void addJar(String jar, JarFile jarFile, File file)
        throws IOException {

        if (jar == null)
            return;
        if (jarFile == null)
            return;
        if (file == null)
            return;

        if (logger.isLoggable(Level.FINER))
            logger.finer("addJar(" + jar + ")");

        int i;

        if ((jarPath != null) && (jar.startsWith(jarPath))) {

            String jarName = jar.substring(jarPath.length());
            while (jarName.startsWith("/")) {
                jarName = jarName.substring(1);
            }
            jarNames.add(jarName);
        }

        try {

            // Register the JAR for tracking

            long lastModified =
                ((ResourceAttributes) resources.getAttributes(jar))
                .getLastModified();

            String[] result = new String[paths.length + 1];
            for (i = 0; i < paths.length; i++) {
                result[i] = paths[i];
            }
            result[paths.length] = jar;
            paths = result;

            long[] result3 = new long[lastModifiedDates.length + 1];
            for (i = 0; i < lastModifiedDates.length; i++) {
                result3[i] = lastModifiedDates[i];
            }
            result3[lastModifiedDates.length] = lastModified;
            lastModifiedDates = result3;

        } catch (NamingException e) {
            // Ignore
        }

        JarFile[] result2 = new JarFile[jarFiles.length + 1];
        for (i = 0; i < jarFiles.length; i++) {
            result2[i] = jarFiles[i];
        }
        result2[jarFiles.length] = jarFile;
        jarFiles = result2;

        // Add the file to the list
        File[] result4 = new File[jarRealFiles.length + 1];
        for (i = 0; i < jarRealFiles.length; i++) {
            result4[i] = jarRealFiles[i];
        }
        result4[jarRealFiles.length] = file;
        jarRealFiles = result4;
    }


    /**
     * Have one or more classes or resources been modified so that a reload
     * is appropriate?
     */
    public boolean modified() {

        if (logger.isLoggable(Level.FINER))
            logger.fine("modified()");

        // Checking for modified loaded resources
        int length = paths.length;

        // A rare race condition can occur in the updates of the two arrays
        // It's totally ok if the latest class added is not checked (it will
        // be checked the next time
        int length2 = lastModifiedDates.length;
        if (length > length2)
            length = length2;

        for (int i = 0; i < length; i++) {
            try {
                long lastModified =
                    ((ResourceAttributes) resources.getAttributes(paths[i]))
                    .getLastModified();
                if (lastModified != lastModifiedDates[i]) {
                        if (logger.isLoggable(Level.FINER))
                            logger.finer("  Resource '" + paths[i]
                                  + "' was modified; Date is now: "
                                  + new java.util.Date(lastModified) + " Was: "
                                  + new java.util.Date(lastModifiedDates[i]));
                    return (true);
                }
            } catch (NamingException e) {
                logger.severe("    Resource '" + paths[i] + "' is missing");
                return (true);
            }
        }

        length = jarNames.size();

        // Check if JARs have been added or removed
        if (getJarPath() != null) {

            try {
                NamingEnumeration<Binding> enumeration =
                    resources.listBindings(getJarPath());
                int i = 0;
                while (enumeration.hasMoreElements() && (i < length)) {
                    NameClassPair ncPair = enumeration.nextElement();
                    String name = ncPair.getName();
                    // Ignore non JARs present in the lib folder
// START OF IASRI 4657979
                    if (!name.endsWith(".jar") && !name.endsWith(".zip"))
// END OF IASRI 4657979
                        continue;
                    if (!name.equals(jarNames.get(i))) {
                        // Missing JAR
                        logger.finer("    Additional JARs have been added : '"
                                 + name + "'");
                        return (true);
                    }
                    i++;
                }
                if (enumeration.hasMoreElements()) {
                    while (enumeration.hasMoreElements()) {
                        NameClassPair ncPair = enumeration.nextElement();
                        String name = ncPair.getName();
                        // Additional non-JAR files are allowed
// START OF IASRI 4657979
                        if (name.endsWith(".jar") || name.endsWith(".zip")) {
// END OF IASRI 4657979
                            // There was more JARs
                            logger.finer("    Additional JARs have been added");
                            return (true);
                        }
                    }
                } else if (i < jarNames.size()) {
                    // There was less JARs
                    logger.finer("    Additional JARs have been added");
                    return (true);
                }
            } catch (NamingException e) {
                if (logger.isLoggable(Level.FINER))
                    logger.finer("    Failed tracking modifications of '"
                        + getJarPath() + "'");
            } catch (ClassCastException e) {
                logger.severe("    Failed tracking modifications of '"
                          + getJarPath() + "' : " + e.getMessage());
            }

        }

        // No classes have been modified
        return (false);

    }


    /**
     * Render a String representation of this object.
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("WebappClassLoader (delegate=");
        sb.append(delegate);
        if (repositories != null) {
            sb.append("; repositories=");
            for (int i = 0; i < repositories.length; i++) {
                sb.append(repositories[i]);
                if (i != (repositories.length-1)) {
                    sb.append(",");
                }
            }
        }
        sb.append(")");
        return (sb.toString());
    }


    // ---------------------------------------------------- ClassLoader Methods


    /**
     * Find the specified class in our local repositories, if possible.  If
     * not found, throw <code>ClassNotFoundException</code>.
     *
     * @param name Name of the class to be loaded
     *
     * @exception ClassNotFoundException if the class was not found
     */
    public Class<?> findClass(String name) throws ClassNotFoundException {

        if (logger.isLoggable(Level.FINER))
            logger.finer("    findClass(" + name + ")");

        // (1) Permission to define this class when using a SecurityManager
        // START PE 4989455
        //if (securityManager != null) {
        if ( securityManager != null && packageDefinitionEnabled ){
        // END PE 4989455
            int i = name.lastIndexOf('.');
            if (i >= 0) {
                try {
                    if (logger.isLoggable(Level.FINER))
                        logger.finer("      securityManager.checkPackageDefinition");
                    securityManager.checkPackageDefinition(name.substring(0,i));
                } catch (Exception se) {
                if (logger.isLoggable(Level.FINER))
                    logger.log(Level.FINER, "      -->Exception-->ClassNotFoundException", se);
                    throw new ClassNotFoundException(name, se);
                }
            }
        }

        // Ask our superclass to locate this class, if possible
        // (throws ClassNotFoundException if it is not found)
        Class<?> clazz = null;
        try {
            if (logger.isLoggable(Level.FINER))
                logger.finer("      findClassInternal(" + name + ")");
            try {
                ResourceEntry entry = findClassInternal(name);
                // Create the code source object
                CodeSource codeSource =
                    new CodeSource(entry.codeBase, entry.certificates);
                synchronized (this) {
                    if (entry.loadedClass == null) {
                        /* START GlassFish [680]
                        clazz = defineClass(name, entry.binaryContent, 0,
                                entry.binaryContent.length,
                                codeSource);
                        */
                        // START GlassFish [680]
                        // We use a temporary byte[] so that we don't change
                        // the content of entry in case bytecode
                        // preprocessing takes place.
                        byte[] binaryContent = entry.binaryContent;
                        if (!byteCodePreprocessors.isEmpty()) {
                            // ByteCodePreprpcessor expects name as
                            // java/lang/Object.class
                            String resourceName =
                                name.replace('.', '/') + ".class";
                            for(BytecodePreprocessor preprocessor : byteCodePreprocessors) {
                                binaryContent = preprocessor.preprocess(
                                    resourceName, binaryContent);
                            }
                        }
                        clazz = defineClass(name, binaryContent, 0,
                                binaryContent.length,
                                codeSource);
                        // END GlassFish [680]
                        entry.loadedClass = clazz;
                        entry.binaryContent = null;
                        entry.source = null;
                        entry.codeBase = null;
                        entry.manifest = null;
                        entry.certificates = null;
                    } else {
                        clazz = entry.loadedClass;
                    }
                }
            } catch(ClassNotFoundException cnfe) {
                if (!hasExternalRepositories) {
                    throw cnfe;
                }
            } catch (UnsupportedClassVersionError ucve) {
                String msg = rb.getString(
                    "webappClassLoader.unsupportedVersion");
                msg = MessageFormat.format(msg,
                    new Object[] {name, getJavaVersion()});
                throw new UnsupportedClassVersionError(msg);
            } catch(AccessControlException ace) {
                throw new ClassNotFoundException(name, ace);
            } catch (Throwable t) {
                String msg = rb.getString(
                    "webappClassLoader.unableToLoadClass");
                msg = MessageFormat.format(msg, name, t.toString());
                throw new RuntimeException(msg, t);
            }
            if ((clazz == null) && hasExternalRepositories) {
                try {
                    synchronized(this) {
                        clazz = super.findClass(name);
                    }
                } catch(AccessControlException ace) {
                    throw new ClassNotFoundException(name, ace);
                } catch (RuntimeException e) {
                    if (logger.isLoggable(Level.FINER))
                        logger.log(Level.FINER, "      -->RuntimeException Rethrown", e);
                    throw e;
                }
            }
            if (clazz == null) {
                if (logger.isLoggable(Level.FINER))
                    logger.finer("    --> Returning ClassNotFoundException");
                throw new ClassNotFoundException(name);
            }
        } catch (ClassNotFoundException e) {
            if (logger.isLoggable(Level.FINER))
                logger.finer("    --> Passing on ClassNotFoundException");
            throw e;
        }

        // Return the class we have located
        if (logger.isLoggable(Level.FINER))
            logger.finer("      Returning class " + clazz);
        if (logger.isLoggable(Level.FINER) && clazz!=null) {
            ClassLoader cl;
            if (securityManager != null) {
                cl = AccessController.doPrivileged(
                    new PrivilegedGetClassLoader(clazz));
            } else {
                cl = clazz.getClassLoader();
            }
            logger.finer("      Loaded by " + cl);
        }
        return (clazz);

    }


    /**
     * Find the specified resource in our local repository, and return a
     * <code>URL</code> refering to it, or <code>null</code> if this resource
     * cannot be found.
     *
     * @param name Name of the resource to be found
     */
    public URL findResource(String name) {

        if (logger.isLoggable(Level.FINER))
            logger.finer("    findResource(" + name + ")");

        URL url = null;

        if (".".equals(name)) {
            name = "";
        }

        ResourceEntry entry = resourceEntries.get(name);
        if (entry == null) {
            entry = findResourceInternal(name, name);
        }
        if (entry != null) {
            url = entry.source;
        }

        if ((url == null) && hasExternalRepositories)
            url = super.findResource(name);

        if (logger.isLoggable(Level.FINER)) {
            if (url != null)
                logger.finer("    --> Returning '" + url.toString() + "'");
            else
                logger.finer("    --> Resource not found, returning null");
        }
        return (url);

    }


    /**
     * Return an enumeration of <code>URLs</code> representing all of the
     * resources with the given name.  If no resources with this name are
     * found, return an empty enumeration.
     *
     * @param name Name of the resources to be found
     *
     * @exception IOException if an input/output error occurs
     */
    public Enumeration<URL> findResources(String name) throws IOException {

        if (logger.isLoggable(Level.FINER))
            logger.finer("    findResources(" + name + ")");

        Vector<URL> result = new Vector<URL>();

        int jarFilesLength = jarFiles.length;
        int repositoriesLength = repositories.length;

        int i;

        // Looking at the repositories
        for (i = 0; i < repositoriesLength; i++) {
            try {
                String fullPath = repositories[i] + name;
                resources.lookup(fullPath);
                // Note : Not getting an exception here means the resource was
                // found
                try {
                    result.addElement(getURI(new File(files[i], name)));
                } catch (MalformedURLException e) {
                    // Ignore
                }
            } catch (NamingException e) {
            }
        }

        // Looking at the JAR files
        synchronized (jarFiles) {
            if (openJARs()) {
                for (i = 0; i < jarFilesLength; i++) {
                    JarEntry jarEntry = jarFiles[i].getJarEntry(name);
                    if (jarEntry != null) {
                        try {
                            String jarFakeUrl = getURI(jarRealFiles[i]).toString();
                            jarFakeUrl = "jar:" + jarFakeUrl + "!/" + name;
                            result.addElement(new URL(jarFakeUrl));
                        } catch (MalformedURLException e) {
                            // Ignore
                        }
                    }
                }
            }
        }

        // Adding the results of a call to the superclass
        if (hasExternalRepositories) {

            Enumeration<URL> otherResourcePaths = super.findResources(name);

            while (otherResourcePaths.hasMoreElements()) {
                result.addElement(otherResourcePaths.nextElement());
            }

        }

        return result.elements();

    }

    /**
     * Find the resource with the given name.  A resource is some data
     * (images, audio, text, etc.) that can be accessed by class code in a
     * way that is independent of the location of the code.  The name of a
     * resource is a "/"-separated path name that identifies the resource.
     * If the resource cannot be found, return <code>null</code>.
     * <p>
     * This method searches according to the following algorithm, returning
     * as soon as it finds the appropriate URL.  If the resource cannot be
     * found, returns <code>null</code>.
     * <ul>
     * <li>If the <code>delegate</code> property is set to <code>true</code>,
     *     call the <code>getResource()</code> method of the parent class
     *     loader, if any.</li>
     * <li>Call <code>findResource()</code> to find this resource in our
     *     locally defined repositories.</li>
     * <li>Call the <code>getResource()</code> method of the parent class
     *     loader, if any.</li>
     * </ul>
     *
     * @param name Name of the resource to return a URL for
     */
    public URL getResource(String name) {

        if (logger.isLoggable(Level.FINER))
            logger.finer("getResource(" + name + ")");
        URL url = null;

        /*
         * (1) Delegate to parent if requested, or if the requested resource
         * belongs to one of the packages that are part of the Java EE platform
         */
        if (delegate
                || (name.startsWith("javax") &&
                    (!name.startsWith("javax.faces") || !useMyFaces))
                || name.startsWith("sun")
                || (name.startsWith("com/sun/faces") &&
                    !name.startsWith("com/sun/faces/extensions") &&
                    !useMyFaces)
                || name.startsWith("org/apache/taglibs/standard")) {
            if (logger.isLoggable(Level.FINER))
                logger.finer("  Delegating to parent classloader " + parent);
            ClassLoader loader = parent;
            if (loader == null)
                loader = system;
            url = loader.getResource(name);
            if (url != null) {
            if (logger.isLoggable(Level.FINER))
                logger.finer("  --> Returning '" + url.toString() + "'");
                return (url);
            }
        }

        // (2) Search local repositories
        url = findResource(name);
        if (url != null) {
            // Locating the repository for special handling in the case
            // of a JAR
            ResourceEntry entry = resourceEntries.get(name);
            try {
                String repository = entry.codeBase.toString();
                if ((repository.endsWith(".jar"))
                        && !(name.endsWith(".class"))
                        && !(name.endsWith(".jar"))) {
                    // Copy binary content to the work directory if not present
                    File resourceFile = new File(loaderDir, name);
                    url = resourceFile.toURL();
                }
            } catch (Exception e) {
                // Ignore
            }
            if (logger.isLoggable(Level.FINER))
                logger.finer("  --> Returning '" + url.toString() + "'");
            return (url);
        }

        // (3) Delegate to parent unconditionally if not already attempted
        if (!delegate) {
            ClassLoader loader = parent;
            if (loader == null)
                loader = system;
            url = loader.getResource(name);
            if (url != null) {
                if (logger.isLoggable(Level.FINER))
                    logger.finer("  --> Returning '" + url.toString() + "'");
                return (url);
            }
        }

        // (4) Resource was not found
        if (logger.isLoggable(Level.FINER))
            logger.finer("  --> Resource not found, returning null");
        return (null);

    }


    /**
     * Find the resource with the given name, and return an input stream
     * that can be used for reading it.  The search order is as described
     * for <code>getResource()</code>, after checking to see if the resource
     * data has been previously cached.  If the resource cannot be found,
     * return <code>null</code>.
     *
     * @param name Name of the resource to return an input stream for
     */
    public InputStream getResourceAsStream(String name) {

        if (logger.isLoggable(Level.FINER))
            logger.finer("getResourceAsStream(" + name + ")");
        InputStream stream = null;

        // (0) Check for a cached copy of this resource
        stream = findLoadedResource(name);
        if (stream != null) {
            if (logger.isLoggable(Level.FINER))
                logger.finer("  --> Returning stream from cache");
            return (stream);
        }

        /*
         * (1) Delegate to parent if requested, or if the requested resource
         * belongs to one of the packages that are part of the Java EE platform
         */
        if (delegate
                || name.startsWith("javax")
                || name.startsWith("sun")
                || (name.startsWith("com/sun/faces")
                    && !name.startsWith("com/sun/faces/extensions"))
                || name.startsWith("org/apache/taglibs/standard")) {
            if (logger.isLoggable(Level.FINER))
                logger.finer("  Delegating to parent classloader " + parent);
            ClassLoader loader = parent;
            if (loader == null)
                loader = system;
            stream = loader.getResourceAsStream(name);
            if (stream != null) {
                // FIXME - cache???
                if (logger.isLoggable(Level.FINER))
                    logger.finer("  --> Returning stream from parent");
                return (stream);
            }
        }

        // (2) Search local repositories
        if (logger.isLoggable(Level.FINER))
            logger.finer("  Searching local repositories");
        URL url = findResource(name);
        if (url != null) {
            // FIXME - cache???
            if (logger.isLoggable(Level.FINER))
                logger.finer("  --> Returning stream from local");
            stream = findLoadedResource(name);
            try {
                if (hasExternalRepositories && (stream == null))
                    stream = url.openStream();
            } catch (IOException e) {
                ; // Ignore
            }
            if (stream != null)
                return (stream);
        }

        // (3) Delegate to parent unconditionally
        if (!delegate) {
            if (logger.isLoggable(Level.FINER))
                logger.finer("  Delegating to parent classloader unconditionally " + parent);
            ClassLoader loader = parent;
            if (loader == null)
                loader = system;
            stream = loader.getResourceAsStream(name);
            if (stream != null) {
                // FIXME - cache???
                if (logger.isLoggable(Level.FINER))
                    logger.finer("  --> Returning stream from parent");
                return (stream);
            }
        }

        // (4) Resource was not found
        if (logger.isLoggable(Level.FINER))
            logger.finer("  --> Resource not found, returning null");
        return (null);

    }


    /**
     * Finds all the resources with the given name.
     */
    public Enumeration<URL> getResources(String name) throws IOException {

	final Enumeration[] enums = new Enumeration[2];

        Enumeration<URL> localResources = findResources(name);
        Enumeration<URL> parentResources = null;
        if (parent != null) {
            parentResources = parent.getResources(name);
        } else {
            parentResources = system.getResources(name);
        }

        if (delegate) {
            enums[0] = parentResources;
            enums[1] = localResources;
        } else {
            enums[0] = localResources;
            enums[1] = parentResources;
        }

        return new Enumeration() {

            int index = 0;

            private boolean next() {
                while (index < enums.length) {
                    if (enums[index] != null &&
                            enums[index].hasMoreElements()) {
                        return true;
                    }
                    index++;
                }
                return false;
            }

            public boolean hasMoreElements() {
                return next();
            }

            public URL nextElement() {
                if (!next()) {
                    throw new NoSuchElementException();
                }
                return (URL)enums[index].nextElement();
            }
        };
    }


    /**
     * Load the class with the specified name.  This method searches for
     * classes in the same manner as <code>loadClass(String, boolean)</code>
     * with <code>false</code> as the second argument.
     *
     * @param name Name of the class to be loaded
     *
     * @exception ClassNotFoundException if the class was not found
     */
    public Class<?> loadClass(String name) throws ClassNotFoundException {

        if (logger.isLoggable(Level.FINER)) {
            logger.finer("loadClass(" + name + ")");
        }

        Class<?> clazz = null;

        // Don't load classes if class loader is stopped
        if (!started) {
            String msg = rb.getString("webappClassLoader.notStarted");
            throw new IllegalStateException(
                MessageFormat.format(msg, name));
        }

        // Check our previously loaded local class cache
        clazz = findLoadedClass0(name);
        if (clazz != null) {
            if (logger.isLoggable(Level.FINER)) {
                logger.finer("  Returning class from cache");
            }
            return (clazz);
        }

        // Check our previously loaded class cache
        clazz = findLoadedClass(name);
        if (clazz != null) {
            if (logger.isLoggable(Level.FINER)) {
                logger.finer("  Returning class from cache");
            }
            return (clazz);
        }

        // Permission to access this class when using a SecurityManager
        if ( securityManager != null && packageDefinitionEnabled){
            int i = name.lastIndexOf('.');
            if (i >= 0) {
                try {
                    securityManager.checkPackageAccess(name.substring(0,i));
                } catch (SecurityException se) {
                    String error = "Security Violation, attempt to use " +
                        "Restricted Class: " + name;
                    logger.log(Level.INFO, error, se);
                    throw new ClassNotFoundException(error, se);
                }
            }
        }

        ClassLoader delegateLoader = parent;
        if (delegateLoader == null) {
            delegateLoader = system;
        }

        if (delegate) {
            // Check delegate first
            if (logger.isLoggable(Level.FINER)) {
                logger.finer("  Delegating to classloader1 " + delegateLoader);
            }
            try {
                clazz = delegateLoader.loadClass(name);
                if (clazz != null) {
                    if (logger.isLoggable(Level.FINER)) {
                        logger.finer("  Loading class from delegate");
                    }
                    return clazz;
                }
            } catch (ClassNotFoundException e) {
                // Ignore
            }

            // Check local repositories next
            if (logger.isLoggable(Level.FINER)) {
                logger.finer("  Searching local repositories");
            }
            clazz = findClass(name);
            if (clazz != null) {
                if (logger.isLoggable(Level.FINER)) {
                    logger.finer("  Loading class from local repository");
                }
                return clazz;
            }
        
            throw new ClassNotFoundException(name);
        }


        // Search local repositories first (if permitted)
        boolean filterCoreClasses = filter(name);
        if (!filterCoreClasses) {
            if (logger.isLoggable(Level.FINER)) {
                logger.finer("  Searching local repositories");
            }
            try {
                clazz = findClass(name);
                if (clazz != null) {
                    if (logger.isLoggable(Level.FINER)) {
                        logger.finer("  Loading class from local repository");
                    }
                    return clazz;
                }
            } catch (ClassNotFoundException e) {
                // Ignore
            }
        }

        // Delegate if class was not found locally, or was not permitted to
        // be looked up locally
        if (logger.isLoggable(Level.FINER)) {
            logger.finer("  Delegating to classloader " + delegateLoader);
        }
        try {
            clazz = delegateLoader.loadClass(name);
            if (clazz != null) {
                if (logger.isLoggable(Level.FINER)) {
                    logger.finer("  Loading class from delegate");
                }
                return clazz;
            }
        } catch (ClassNotFoundException e) {
            // Ignore
        }

        /*
         * If filter() returned true and we did not find the class 
         * using the delegate classloader, see if we can find the class
         * locally.
         */
        if (filterCoreClasses) {
            if (logger.isLoggable(Level.FINER)) {
                logger.finer("  Searching local repositories");
            }
            clazz = findClass(name);
            if (clazz != null) {
                if (logger.isLoggable(Level.FINER)) {
                    logger.finer("  Loading class from local repository");
                }
                return clazz;
            }
        }

        throw new ClassNotFoundException(name);
    }


    /**
     * Load the class with the specified name, searching using the following
     * algorithm until it finds and returns the class.  If the class cannot
     * be found, returns <code>ClassNotFoundException</code>.
     * <ul>
     * <li>Call <code>findLoadedClass(String)</code> to check if the
     *     class has already been loaded.  If it has, the same
     *     <code>Class</code> object is returned.</li>
     * <li>If the <code>delegate</code> property is set to <code>true</code>,
     *     call the <code>loadClass()</code> method of the parent class
     *     loader, if any.</li>
     * <li>Call <code>findClass()</code> to find this class in our locally
     *     defined repositories.</li>
     * <li>Call the <code>loadClass()</code> method of our parent
     *     class loader, if any.</li>
     * </ul>
     * If the class was found using the above steps, and the
     * <code>resolve</code> flag is <code>true</code>, this method will then
     * call <code>resolveClass(Class)</code> on the resulting Class object.
     *
     * @param name Name of the class to be loaded
     * @param resolve If <code>true</code> then resolve the class
     *
     * @exception ClassNotFoundException if the class was not found
     */
    public Class<?> loadClass(String name, boolean resolve)
        throws ClassNotFoundException {

        Class<?> clazz = loadClass(name);
        if (clazz != null) {
            if (resolve) {
                resolveClass(clazz);
            }
            return (clazz);
        }

        throw new ClassNotFoundException(name);
    }


    /**
     * Get the Permissions for a CodeSource.  If this instance
     * of WebappClassLoader is for a web application context,
     * add read FilePermission or JndiPermissions for the base
     * directory (if unpacked),
     * the context URL, and jar file resources.
     *
     * @param codeSource where the code was loaded from
     * @return PermissionCollection for CodeSource
     */
    protected PermissionCollection getPermissions(CodeSource codeSource) {

        String codeUrl = codeSource.getLocation().toString();
        PermissionCollection pc;
        if ((pc = loaderPC.get(codeUrl)) == null) {
            pc = super.getPermissions(codeSource);
            if (pc != null) {
                Iterator<Permission> perms = permissionList.iterator();
                while (perms.hasNext()) {
                    Permission p = perms.next();
                    pc.add(p);
                }
                loaderPC.put(codeUrl,pc);
            }
        }
        return (pc);

    }


    /**
     * Returns the search path of URLs for loading classes and resources.
     * This includes the original list of URLs specified to the constructor,
     * along with any URLs subsequently appended by the addURL() method.
     * @return the search path of URLs for loading classes and resources.
     */
    public URL[] getURLs() {

        if (repositoryURLs != null) {
            return repositoryURLs;
        }

        URL[] external = super.getURLs();

        int filesLength = files.length;
        int jarFilesLength = jarRealFiles.length;
        int length = filesLength + jarFilesLength + external.length;
        int i;

        try {

            URL[] urls = new URL[length];
            for (i = 0; i < length; i++) {
                if (i < filesLength) {
                    urls[i] = getURL(files[i]);
                } else if (i < filesLength + jarFilesLength) {
                    urls[i] = getURL(jarRealFiles[i - filesLength]);
                } else {
                    urls[i] = external[i - filesLength - jarFilesLength];
                }
            }

            repositoryURLs = urls;

        } catch (MalformedURLException e) {
            repositoryURLs = new URL[0];
        }

        return repositoryURLs;

    }


    // ------------------------------------------------------ Lifecycle Methods


    private void init() {

        this.parent = getParent();

        /* SJSAS 6317864
        system = getSystemClassLoader();
        */
        // START SJSAS 6317864
        system = this.getClass().getClassLoader();
        // END SJSAS 6317864
        securityManager = System.getSecurityManager();

        if (securityManager != null) {
            refreshPolicy();
        }

        addOverridablePackage("com.sun.faces.extensions");
    }


    /**
     * Start the class loader.
     */
    public void start() {
        started = true;
    }

    public boolean isStarted() {
        return started;
    }

    public void preDestroy() {
        try {
            stop();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Stop the class loader.
     *
     * @exception LifecycleException if a lifecycle error occurs
     */
    public void stop() throws Exception {

        if (!started) {
            return;
        }

        // START GlassFish Issue 587
        purgeELBeanClasses();
        // END GlassFish Issue 587

        /*
         * Clearing references should be done before setting started to
         * false, due to possible side effects.
         * In addition, set this classloader as the Thread's context
         * classloader, see IT 9894 for details
         */
        ClassLoader curCl = null;
        try {
            curCl = Thread.currentThread().getContextClassLoader();
            Thread.currentThread().setContextClassLoader(this);
            clearReferences();
        } finally {
            if (curCl != null) {
                Thread.currentThread().setContextClassLoader(curCl);
            }
        }

        // START SJSAS 6258619
        ClassLoaderUtil.releaseLoader(this);
        // END SJSAS 6258619

        started = false;

        int length = files.length;
        for (int i = 0; i < length; i++) {
            files[i] = null;
        }

        length = jarFiles.length;
        for (int i = 0; i < length; i++) {
            try {
                if (jarFiles[i] != null) {
                    jarFiles[i].close();
                }
            } catch (IOException e) {
                // Ignore
            }
            jarFiles[i] = null;
        }

        try {
            Class<?> clazz = Class.forName("sun.misc.ClassLoaderUtil");
            if (clazz != null) {
                Method m = clazz.getMethod("releaseLoader",
                                           URLClassLoader.class);
                if (m != null) {
                    m.invoke(null, this);
                }
            }
        } catch (Exception e) {
            // ignore
        }

        notFoundResources.clear();
        resourceEntries.clear();
        resources = null;
        repositories = null;
        repositoryURLs = null;
        files = null;
        jarFiles = null;
        jarRealFiles = null;
        jarPath = null;
        jarNames.clear();
        lastModifiedDates = null;
        paths = null;
        hasExternalRepositories = false;
        parent = null;

        permissionList.clear();
        loaderPC.clear();

        if (loaderDir != null) {
            deleteDir(loaderDir);
        }

        DirContextURLStreamHandler.unbind(this);
    }


    /**
     * Used to periodically signal to the classloader to release
     * JAR resources.
     */
    public void closeJARs(boolean force) {
        if (jarFiles.length > 0) {
            synchronized (jarFiles) {
                if (force || (System.currentTimeMillis()
                              > (lastJarAccessed + 90000))) {
                    for (int i = 0; i < jarFiles.length; i++) {
                        try {
                            if (jarFiles[i] != null) {
                                jarFiles[i].close();
                                jarFiles[i] = null;
                            }
                        } catch (IOException e) {
                            if (logger.isLoggable(Level.FINE)) {
                                logger.log(Level.FINE, "Failed to close JAR", e);
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * Clear references.
     */
    protected void clearReferences() {

        // Unregister any JDBC drivers loaded by this classloader
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            if (driver.getClass().getClassLoader() == this) {
                try {
                    DriverManager.deregisterDriver(driver);
                } catch (SQLException e) {
                    logger.log(Level.WARNING, "webappClassLoader.sqlDriverDeregistrationError", e);
                }
            }
        }

        // Null out any static or final fields from loaded classes,
        // as a workaround for apparent garbage collection bugs
        if (ENABLE_CLEAR_REFERENCES) {
            Collection<ResourceEntry> values = resourceEntries.values();
            Iterator<ResourceEntry> loadedClasses = values.iterator();
            /*
             * Step 1: Enumerate all classes loaded by this WebappClassLoader
             * and trigger the initialization of any uninitialized ones.
             * This is to prevent the scenario where the initialization of
             * one class would call a previously cleared class in Step 2 below.
             */
            while(loadedClasses.hasNext()) {
                ResourceEntry entry = loadedClasses.next();
                Class<?> clazz = null;
                synchronized(this) {
                    clazz = entry.loadedClass;
                }
                if (clazz != null) {
                    try {
                        Field[] fields = clazz.getDeclaredFields();
                        for (int i = 0; i < fields.length; i++) {
                            if(Modifier.isStatic(fields[i].getModifiers())) {
                                fields[i].get(null);
                                break;
                            }
                        }
                    } catch(Throwable t) {
                    }
                }
            }

            /**
             * Step 2: Clear all loaded classes
             */
            loadedClasses = values.iterator();
            while (loadedClasses.hasNext()) {
                ResourceEntry entry = loadedClasses.next();
                Class<?> clazz = null;
                synchronized(this) {
                    clazz = entry.loadedClass;
                }
                if (clazz != null) {
                    try {
                        Field[] fields = clazz.getDeclaredFields();
                        for (int i = 0; i < fields.length; i++) {
                            Field field = fields[i];
                            int mods = field.getModifiers();
                            if (field.getType().isPrimitive()
                                    || (field.getName().indexOf("$") != -1)) {
                                continue;
                            }
                            if (Modifier.isStatic(mods)) {
                                try {
                                    field.setAccessible(true);
                                    if (Modifier.isFinal(mods)) {
                                        if (!((field.getType().getName().startsWith("java."))
                                                || (field.getType().getName().startsWith("javax.")))) {
                                            nullInstance(field.get(null));
                                        }
                                    } else {
                                        field.set(null, null);
                                        if (logger.isLoggable(Level.FINE)) {
                                            logger.fine("Set field " + field.getName()
                                                    + " to null in class " + clazz.getName());
                                        }
                                    }
                                } catch (Throwable t) {
                                    if (logger.isLoggable(Level.FINE)) {
                                        logger.log(Level.FINE, "Could not set field " + field.getName()
                                                + " to null in class " + clazz.getName(), t);
                                    }
                                }
                            }
                        }
                    } catch (Throwable t) {
                            if (logger.isLoggable(Level.FINE))  {
                                logger.log(Level.FINE, "Could not clean fields for class " + clazz.getName(), t);
                        }
                    }
                }
            }
        }

        // Clear the classloader reference in the VM's bean introspector
        java.beans.Introspector.flushCaches();

    }


    protected void nullInstance(Object instance) {
        if (instance == null) {
            return;
        }
        Field[] fields = instance.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            int mods = field.getModifiers();
            if (field.getType().isPrimitive()
                    || (field.getName().indexOf("$") != -1)) {
                continue;
            }
            try {
                field.setAccessible(true);
                if (Modifier.isStatic(mods) && Modifier.isFinal(mods)) {
                    // Doing something recursively is too risky
                    continue;
                } else {
                    Object value = field.get(instance);
                    if (null != value) {
                        Class<? extends Object> valueClass = value.getClass();
                            if (logger.isLoggable(Level.FINE))  {
                                logger.fine("Not setting field " + field.getName() +
                                        " to null in object of class " +
                                        instance.getClass().getName() +
                                        " because the referenced object was of type " +
                                        valueClass.getName() +
                                        " which was not loaded by this WebappClassLoader.");
                            }
                        } else {
                            field.set(instance, null);
                             if (logger.isLoggable(Level.FINE))
                                logger.fine("Set field " + field.getName()
                                        + " to null in class " + instance.getClass().getName());
                        }
                    }
            } catch (Throwable t) {
                if (logger.isLoggable(Level.FINE)) {
                    logger.log(Level.FINE,"Could not set field " + field.getName()
                            + " to null in object instance of class "
                            + instance.getClass().getName(), t);
                }
            }
        }
    }


    /**
     * Determine whether a class was loaded by this class loader or one of
     * its child class loaders.
     */
    protected boolean loadedByThisOrChild(Class<? extends Object> clazz) {
        boolean result = false;
        for (ClassLoader classLoader = clazz.getClassLoader();
                null != classLoader; classLoader = classLoader.getParent()) {
            if (classLoader.equals(this)) {
                result = true;
                break;
            }
        }
        return result;
    }
    // ------------------------------------------------------ Protected Methods


    /**
     * Used to periodically signal to the classloader to release JAR resources.
     */
    protected boolean openJARs() {
        if (started && (jarFiles.length > 0)) {
            lastJarAccessed = System.currentTimeMillis();
            if (jarFiles[0] == null) {
                for (int i = 0; i < jarFiles.length; i++) {
                    try {
                        jarFiles[i] = new JarFile(jarRealFiles[i]);
                    } catch (IOException e) {
                        if (logger.isLoggable(Level.FINE)) {
                            logger.log(Level.FINE, "Failed to open JAR", e);
                        }
                        for (int j = 0; j < i; j++) {
                            try {
                                jarFiles[j].close();
                            } catch (Throwable t) {
                                // Ignore
                            }
                        }
                        return false; 
                   }
                }
            }
        }
        return true;
    }


    /**
     * Find specified class in local repositories.
     *
     * @return the loaded class, or null if the class isn't found
     */
    protected ResourceEntry findClassInternal(String name)
        throws ClassNotFoundException {

        if (!validate(name))
            throw new ClassNotFoundException(name);

        String tempPath = name.replace('.', '/');
        String classPath = tempPath + ".class";

        ResourceEntry entry = findResourceInternal(name, classPath);

        if (entry == null)
               throw new ClassNotFoundException(name);

        synchronized (this) {
            Class<?> clazz = entry.loadedClass;
            if (clazz != null)
                return entry;

            if (entry.binaryContent == null)
                throw new ClassNotFoundException(name);
        }

        // Looking up the package
        String packageName = null;
        int pos = name.lastIndexOf('.');
        if (pos != -1)
            packageName = name.substring(0, pos);

        Package pkg = null;

        if (packageName != null) {

// START OF IASRI 4717252
          synchronized (loaderPC) {
// END OF IASRI 4717252
            pkg = getPackage(packageName);

            // Define the package (if null)
            if (pkg == null) {
                if (entry.manifest == null) {
                    definePackage(packageName, null, null, null, null, null,
                                  null, null);
                } else {
                    definePackage(packageName, entry.manifest, entry.codeBase);
                }
            }
// START OF IASRI 4717252
          }
// END OF IASRI 4717252
        }

        if (securityManager != null) {

            // Checking sealing
            if (pkg != null) {
                boolean sealCheck = true;
                if (pkg.isSealed()) {
                    sealCheck = pkg.isSealed(entry.codeBase);
                } else {
                    sealCheck = (entry.manifest == null)
                        || !isPackageSealed(packageName, entry.manifest);
                }
                if (!sealCheck)
                    throw new SecurityException
                        ("Sealing violation loading " + name + " : Package "
                         + packageName + " is sealed.");
            }

        }

        return entry;

    }

    /**
     * Find specified resource in local repositories. This block
     * will execute under an AccessControl.doPrivilege block.
     *
     * @return the loaded resource, or null if the resource isn't found
     */
    private ResourceEntry findResourceInternal(File file, String path){
        ResourceEntry entry = new ResourceEntry();
        try {
            entry.source = getURI(new File(file, path));
            entry.codeBase = getURL(new File(file, path));
        } catch (MalformedURLException e) {
            return null;
        }
        return entry;
    }


    /**
     * Attempts to find the specified resource in local repositories.
     *
     * @return the loaded resource, or null if the resource isn't found
     */
    protected ResourceEntry findResourceInternal(String name, String path) {

        if (!started) {
            String msg = rb.getString("webappClassLoader.notStarted");
            throw new IllegalStateException(
                MessageFormat.format(msg, name));
        }

        if ((name == null) || (path == null)) {
            return null;
        }

        ResourceEntry entry = resourceEntries.get(name);
        if (entry != null) {
            return entry;
        } else if (notFoundResources.containsKey(name)) {
            return null;
        }

        entry = findResourceInternalFromRepositories(name, path);
        if (entry == null) {
            synchronized (jarFiles) {
                entry = findResourceInternalFromJars(name, path);
            }
        }

        if (entry == null) {
            notFoundResources.put(name, name);
            return null;
        }

        // Add the entry in the local resource repository
        // Ensures that all the threads which may be in a race to load
        // a particular class all end up with the same ResourceEntry
        // instance
        ResourceEntry entry2 = resourceEntries.putIfAbsent(name, entry);
        if (entry2 != null) {
            entry = entry2;
        }

        return entry;
    }


    /**
     * Attempts to load the requested resource from this classloader's
     * internal repositories.
     *
     * @return The requested resource, or null if not found
     */
    private ResourceEntry findResourceInternalFromRepositories(String name,
                                                               String path) {

        ResourceEntry entry = null;
        int contentLength = -1;
        InputStream binaryStream = null;
        int repositoriesLength = repositories.length;
        Resource resource = null;

        for (int i=0; (entry == null) && (i < repositoriesLength); i++) {

            try {

                String fullPath = repositories[i] + path;
                Object lookupResult = resources.lookup(fullPath);
                if (lookupResult instanceof Resource) {
                    resource = (Resource) lookupResult;
                }

                // Note : Not getting an exception here means the resource was
                // found
                if (securityManager != null) {
                    PrivilegedAction<ResourceEntry> dp =
                        new PrivilegedFindResource(files[i], path);
                    entry = AccessController.doPrivileged(dp);
                } else {
                    entry = findResourceInternal(files[i], path);
                }

                ResourceAttributes attributes =
                    (ResourceAttributes) resources.getAttributes(fullPath);
                contentLength = (int) attributes.getContentLength();
                entry.lastModified = attributes.getLastModified();

                if (resource != null) {

                    try {
                        binaryStream = resource.streamContent();
                    } catch (IOException e) {
                        return null;
                    }

                    // Register the full path for modification checking
                    // Note: Only syncing on a 'constant' object is needed
                    synchronized (ALL_PERMISSION) {

                        int j;

                        long[] result2 =
                            new long[lastModifiedDates.length + 1];
                        for (j = 0; j < lastModifiedDates.length; j++) {
                            result2[j] = lastModifiedDates[j];
                        }
                        result2[lastModifiedDates.length] = entry.lastModified;
                        lastModifiedDates = result2;

                        String[] result = new String[paths.length + 1];
                        for (j = 0; j < paths.length; j++) {
                            result[j] = paths[j];
                        }
                        result[paths.length] = fullPath;
                        paths = result;

                    }
                }
            } catch (NamingException e) {
            }
        }

        if (entry != null) {
            readEntryData(entry, name, binaryStream, contentLength, null);
        }

        return entry;
    }


    /**
     * Attempts to load the requested resource from this classloader's
     * JAR files.
     *
     * @return The requested resource, or null if not found
     */
    private ResourceEntry findResourceInternalFromJars(String name,
                                                       String path) {

        ResourceEntry entry = null;
        JarEntry jarEntry = null;
        int contentLength = -1;
        InputStream binaryStream = null;

        if (!openJARs()){
            return null;
        }

        int jarFilesLength = jarFiles.length;

        for (int i=0; (entry == null) && (i < jarFilesLength); i++) {
            jarEntry = jarFiles[i].getJarEntry(path);

            if (jarEntry != null) {

                entry = new ResourceEntry();
                try {
                    entry.codeBase = getURL(jarRealFiles[i]);
                    String jarFakeUrl = getURI(jarRealFiles[i]).toString();
                    jarFakeUrl = "jar:" + jarFakeUrl + "!/" + path;
                    entry.source = new URL(jarFakeUrl);
                    entry.lastModified = jarRealFiles[i].lastModified();
                } catch (MalformedURLException e) {
                    return null;
                }

                contentLength = (int) jarEntry.getSize();
                try {
                    entry.manifest = jarFiles[i].getManifest();
                    binaryStream = jarFiles[i].getInputStream(jarEntry);
                } catch (IOException e) {
                    return null;
                }

                // Extract resources contained in JAR to the workdir
                if (!(path.endsWith(".class"))) {
                    byte[] buf = new byte[1024];
                    File resourceFile = new File
                        (loaderDir, jarEntry.getName());
                    if (!resourceFile.exists()) {
                        Enumeration<JarEntry> entries = jarFiles[i].entries();
                        while (entries.hasMoreElements()) {
                            JarEntry jarEntry2 = entries.nextElement();
                            if (!(jarEntry2.isDirectory())
                                && (!jarEntry2.getName().endsWith(".class"))) {
                                resourceFile = new File
                                    (loaderDir, jarEntry2.getName());
                                resourceFile.getParentFile().mkdirs();
                                FileOutputStream os = null;
                                InputStream is = null;
                                try {
                                    is = jarFiles[i].getInputStream(jarEntry2);
                                    os = new FileOutputStream(resourceFile);
                                    while (true) {
                                        int n = is.read(buf);
                                        if (n <= 0) {
                                            break;
                                        }
                                        os.write(buf, 0, n);
                                    }
                                } catch (IOException e) {
                                    // Ignore
                                } finally {
                                    try {
                                        if (is != null) {
                                            is.close();
                                        }
                                    } catch (IOException e) {
                                    }
                                    try {
                                        if (os != null) {
                                            os.close();
                                        }
                                    } catch (IOException e) {
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (entry != null) {
            readEntryData(entry, name, binaryStream, contentLength, jarEntry);
        }

        return entry;
    }


    /**
     * Reads the resource's binary data from the given input stream.
     */
    private void readEntryData(ResourceEntry entry,
                               String name,
                               InputStream binaryStream,
                               int contentLength,
                               JarEntry jarEntry) {

        if (binaryStream == null) {
            return;
        }

        byte[] binaryContent = new byte[contentLength];

        try {
            int pos = 0;

            while (true) {
                int n = binaryStream.read(binaryContent, pos,
                                          binaryContent.length - pos);
                if (n <= 0)
                    break;
                pos += n;
            }
            binaryStream.close();
        } catch (Exception e) {
            String msg = rb.getString("webappClassLoader.readClassError");
            msg = MessageFormat.format(msg, name);
            logger.log(Level.WARNING, msg, e);
            return;
        }

        // START OF IASRI 4709374
        // Preprocess the loaded byte code if bytecode preprocesser is
        // enabled
        if (PreprocessorUtil.isPreprocessorEnabled()) {
            binaryContent =
                PreprocessorUtil.processClass(name, binaryContent);
        }
        // END OF IASRI 4709374

        entry.binaryContent = binaryContent;

        // The certificates are only available after the JarEntry
        // associated input stream has been fully read
        if (jarEntry != null) {
            entry.certificates = jarEntry.getCertificates();
        }
    }


    /**
     * Returns true if the specified package name is sealed according to the
     * given manifest.
     */
    protected boolean isPackageSealed(String name, Manifest man) {

        String path = name.replace('.', '/') + '/';
        Attributes attr = man.getAttributes(path); 
        String sealed = null;
        if (attr != null) {
            sealed = attr.getValue(Name.SEALED);
        }
        if (sealed == null) {
            if ((attr = man.getMainAttributes()) != null) {
                sealed = attr.getValue(Name.SEALED);
            }
        }
        return "true".equalsIgnoreCase(sealed);

    }


    /**
     * Finds the resource with the given name if it has previously been
     * loaded and cached by this class loader, and return an input stream
     * to the resource data.  If this resource has not been cached, return
     * <code>null</code>.
     *
     * @param name Name of the resource to return
     */
    protected InputStream findLoadedResource(String name) {

        ResourceEntry entry = resourceEntries.get(name);
        if (entry != null) {
            if (entry.binaryContent != null)
                return new ByteArrayInputStream(entry.binaryContent);
        }
        return (null);

    }


    /**
     * Finds the class with the given name if it has previously been
     * loaded and cached by this class loader, and return the Class object.
     * If this class has not been cached, return <code>null</code>.
     *
     * @param name Name of the resource to return
     */
    protected Class<?> findLoadedClass0(String name) {

        ResourceEntry entry = resourceEntries.get(name);
        if (entry != null) {
            synchronized(this) {
                return entry.loadedClass;
            }
        }
        return (null);  // FIXME - findLoadedResource()

    }


    /**
     * Refresh the system policy file, to pick up eventual changes.
     */
    protected void refreshPolicy() {

        try {
            // The policy file may have been modified to adjust
            // permissions, so we're reloading it when loading or
            // reloading a Context
            Policy policy = Policy.getPolicy();
            policy.refresh();
        } catch (AccessControlException e) {
            // Some policy files may restrict this, even for the core,
            // so this exception is ignored
        }

    }


    /**
     * Filter classes.
     *
     * @param name class name
     * @return true if the class should be filtered
     */
    protected boolean filter(String name) {

        if (name == null)
            return false;

        // START PE 4985680
        // Special case for performance reason.
        if (name.startsWith("java."))
            return true;
        // END PE 4985680

        // Looking up the package
        String packageName = null;
        int pos = name.lastIndexOf('.');
        if (pos != -1)
            packageName = name.substring(0, pos);
        else
            return false;

        if (overridablePackages != null){
            for (String overridePkg : overridablePackages) {
                if (packageName.startsWith(overridePkg))
                    return false;
            }
        }

        for (int i = 0; i < packageTriggers.length; i++) {
            if (packageName.startsWith(packageTriggers[i]))
                return true;
        }

        return false;

    }


    /**
     * Validate a classname. As per SRV.9.7.2, we must restict loading of
     * classes from J2SE (java.*) and classes of the servlet API
     * (javax.servlet.*). That should enhance robustness and prevent a number
     * of user error (where an older version of servlet.jar would be present
     * in /WEB-INF/lib).
     *
     * @param name class name
     * @return true if the name is valid
     */
    protected boolean validate(String name) {

        if (name == null)
            return false;
        if (name.startsWith("java."))
            return false;

        return true;

    }


    /**
     * Get URL.
     */
    protected URL getURL(File file)
        throws MalformedURLException {

        File realFile = file;
        try {
            realFile = realFile.getCanonicalFile();
        } catch (IOException e) {
            // Ignore
        }
        return realFile.toURI().toURL();

    }


    /**
     * Get URL.
     */
    protected URL getURI(File file)
        throws MalformedURLException {

        try {
            file = file.getCanonicalFile();
        } catch (IOException e) {
            // Ignore
        }

        return file.toURI().toURL();

    }


    /**
     * Delete the specified directory, including all of its contents and
     * subdirectories recursively.
     *
     * @param dir File object representing the directory to be deleted
     */
    protected static void deleteDir(File dir) {

        String files[] = dir.list();
        if (files == null) {
            files = new String[0];
        }
        for (int i = 0; i < files.length; i++) {
            File file = new File(dir, files[i]);
            if (file.isDirectory()) {
                deleteDir(file);
            } else {
                file.delete();
            }
        }
        dir.delete();

    }

    // START SJSAS 6344989
    public void addByteCodePreprocessor(BytecodePreprocessor preprocessor) {
        byteCodePreprocessors.add(preprocessor);
    }
    // END SJSAS 6344989


    // START GlassFish Issue 587
    /*
     * Purges all bean classes that were loaded by this WebappClassLoader
     * from the caches maintained by javax.el.BeanELResolver, in order to
     * avoid this WebappClassLoader from leaking.
     */
    private void purgeELBeanClasses() {

        Field fieldlist[] = javax.el.BeanELResolver.class.getDeclaredFields();
        for (int i = 0; i < fieldlist.length; i++) {
            Field fld = fieldlist[i];
            if (fld.getName().equals("properties")) {
                purgeELBeanClasses(fld);
            }
        }
    }

    /*
     * Purges all bean classes that were loaded by this WebappClassLoader
     * from the cache represented by the given reflected field.
     *
     * @param fld The reflected field from which to remove the bean classes
     * that were loaded by this WebappClassLoader
     */
    private void purgeELBeanClasses(final Field fld) {

	SecurityManager sm = System.getSecurityManager();
	if (sm != null) {
            AccessController.doPrivileged(new PrivilegedAction() {
                    public Object run() {
                        fld.setAccessible(true);
                        return null;
                    }
            });
        } else {
            fld.setAccessible(true);
        }

        Map m = null;
        try {
            m = (Map) fld.get(null);
        } catch (IllegalAccessException iae) {
            logger.log(Level.WARNING, "webappClassLoader.unablePurgeBeanClasses", iae);
            return;
        }

        if (m.size() == 0) {
            return;
        }

        Iterator<Class> iter = m.keySet().iterator();
        while (iter.hasNext()) {
            Class mbeanClass = iter.next();
            if (this.equals(mbeanClass.getClassLoader())) {
                iter.remove();
            }
        }
    }
    // END GlassFish Issue 587

     /**
     * Create and return a temporary loader with the same visibility
      * as this loader. The temporary loader may be used to load
      * resources or any other application classes for the purposes of
      * introspecting them for annotations. The persistence provider
      * should not maintain any references to the temporary loader,
      * or any objects loaded by it.
      *
      * @return A temporary classloader with the same classpath as this loader
      */
     public ClassLoader copy() {
            logger.entering("WebModuleListener$InstrumentableWebappClassLoader", "copy");
            // set getParent() as the parent of the cloned class loader
            return new URLClassLoader(getURLs(), getParent());
     }

     /**
     * Add a new ClassFileTransformer to this class loader. This transfomer should be called for
      * each class loading event.
      *
      * @param transformer new class file transformer to do byte code enhancement.
      */
     public void addTransformer(final ClassFileTransformer transformer) {
        final WebappClassLoader cl = this;
        addByteCodePreprocessor(new BytecodePreprocessor(){
                /*
                 * This class adapts ClassFileTransformer to ByteCodePreprocessor that
                 * is used inside WebappClassLoader.
                 */

                public boolean initialize(Hashtable parameters) {
                    return true;
                }

                public byte[] preprocess(String resourceName, byte[] classBytes) {
                    try {
                        // convert java/lang/Object.class to java/lang/Object
                        String classname = resourceName.substring(0,
                                resourceName.length() - 6); // ".class" size = 6
                        byte[] newBytes = transformer.transform(
                                cl, classname, null, null, classBytes);
                        // ClassFileTransformer returns null if no transformation
                        // took place, where as ByteCodePreprocessor is expected
                        // to return non-null byte array.
                        return newBytes == null ? classBytes : newBytes;
                    } catch (IllegalClassFormatException e) {
                        logger.logp(Level.WARNING,
                                "WebModuleListener$InstrumentableClassLoader$BytecodePreprocessor",
                                "preprocess", e.getMessage());
                        throw new RuntimeException(e);
                    }
                }
            });
     }

     private String getJavaVersion() {

        String version = null;

	SecurityManager sm = System.getSecurityManager();
	if (sm != null) {
            version = (String) AccessController.doPrivileged(
                new PrivilegedAction() {
                    public Object run() {
                        return System.getProperty("java.version");
                    }
            });
        } else {
            version = System.getProperty("java.version");
        }

        return version;
    }

}

