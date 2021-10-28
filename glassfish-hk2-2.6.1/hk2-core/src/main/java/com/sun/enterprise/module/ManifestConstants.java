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

package com.sun.enterprise.module;

import org.jvnet.hk2.annotations.Service;
import com.sun.enterprise.module.bootstrap.ModuleStartup;

/**
 * Constants used in the module manifest.
 * These values define a module.
 *
 * @author dochez
 * @author Kohsuke Kawaguchi
 */
public class ManifestConstants {
    /*
     * All headers are prefixed by HK2 to avoid name collision. By Sahoo
     */

    // No instanciation allowed.
    private ManifestConstants() {
    }

    /**
     * Module name.
     * <p>
     * This uniquely identifies the module within modules,
     * but this shouldn't include the version number.
     * <p>
     * <tt>hk2-module</tt> packaging type uses GROUPID.ARTIFACTID for this.
     */
    public static final String BUNDLE_NAME = "Bundle-SymbolicName";

    /**
     * Bundle Version
     */
    public static final String BUNDLE_VERSION = "Bundle-Version";

    /**
     * Module name that we'll launch.
     * <p>
     * This is equivalent of "Main-Class" for us. We'll find {@link ModuleStartup}
     * from this module.
     */
    public static final String MAIN_BUNDLE = "HK2-Main-Bundle";

    /**
     * Exported package list.
     * <p>
     * The value is a comma separated list of packages that are
     * visible to other modules that depend on this module.
     * For example, "com.sun.foobar.spi,com.sun.foobar.abc"
     * <p>
     * If this manifest entry doesn't exist, all classes will be exported.
     *
     * <p>
     * TODO: expand Maven's <tt>hk2-module</tt> packaging to generate this.
     */
    public static final String PKG_EXPORT_NAME = "HK2-Export-Package";

    /**
     * List of modules that this module depends on.
     * <p>
     * The value is a comma separated list of {@link #BUNDLE_NAME modules names}
     * that this module depends on. Whitespaces are allowed before and after
     * commas.
     * <p>
     *
     * <p>
     * Maven's <tt>hk2-module</tt> packaging uses dependency list in POM
     * to fill in this entry.
     *
     * TODO: expand this field to support version specifier.
     */
    public static final String BUNDLE_IMPORT_NAME = "HK2-Import-Bundles";

    /**
     * List of other jar files in this module.
     * <p>
     * This identifies other jar files in this module, not other modules
     * that this module depends on.
     * <p>
     * See <a href="http://java.sun.com/j2se/1.5.0/docs/guide/jar/jar.html#Main%20Attributes">
     * jar file specification</a> for the format of the value.
     *
     * <p>
     * Maven's <tt>hk2-module</tt> packaging uses dependency list in POM
     * to fill in this entry.
     *
     * @see #CLASS_PATH_ID
     */
    public static final String CLASS_PATH = "Class-Path";

    /**
     * List of other jar files in this module.
     * <p>
     * The value is a whitespace separated list of Maven ID of all the dependencies,
     * which is of the form "GROUPID:ARTIFACTID:TYPE[:CLASSIFIER]:VERSION".
     * <p>
     * This entry contains essentially the same information as the {@link #CLASS_PATH}
     * entry, but this enables HK2 to assemble all the required jars by using Maven.
     *
     * <p>
     * Maven's <tt>hk2-module</tt> packaging uses dependency list in POM
     * to fill in this entry.
     *
     * @see #CLASS_PATH
     */
    public static final String CLASS_PATH_ID = "HK2-Class-Path-Id";

    /**
     * See {@link ImportPolicy}.
     *
     * <p>
     * The value is the fully-qualified class name of a class that implements
     * {@link ImportPolicy}.
     * <p>
     * Maven's <tt>hk2-module</tt> packaging automatically finds such a class
     * and puts its name, provided that the class has @{@link Service} annotation.
     */
    public static final String IMPORT_POLICY = "HK2-Module-Import-Policy";

    /**
     * See {@link LifecyclePolicy}.
     *
     * <p>
     * The value is the fully-qualified class name of a class that implements
     * {@link LifecyclePolicy}.
     * <p>
     * Maven's <tt>hk2-module</tt> packaging automatically finds such a class
     * and puts its name, provided that the class has @{@link Service} annotation.
     */
    public static final String LIFECYLE_POLICY = "HK2-Module-Lifecycle-Policy";

    /**
     * <p>
     * List of repositories that should be set up before launching the main module
     * </p>
     */
    public static final String REPOSITORIES = "HK2-Repositories";
}
