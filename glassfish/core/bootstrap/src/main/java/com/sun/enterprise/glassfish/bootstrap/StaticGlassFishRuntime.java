/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010-2011 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
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
package com.sun.enterprise.glassfish.bootstrap;

import com.sun.enterprise.module.ModulesRegistry;
import com.sun.enterprise.module.bootstrap.Main;
import com.sun.enterprise.module.bootstrap.ModuleStartup;
import com.sun.enterprise.module.bootstrap.StartupContext;
import com.sun.enterprise.module.common_impl.AbstractFactory;
import org.glassfish.embeddable.BootstrapProperties;
import org.glassfish.embeddable.GlassFish;
import org.glassfish.embeddable.GlassFishException;
import org.glassfish.embeddable.GlassFishProperties;
import org.glassfish.embeddable.GlassFishRuntime;
import org.jvnet.hk2.component.Habitat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The GlassFishRuntime implementation for NonOSGi environments.
 *
 * @author bhavanishankar@dev.java.net
 */
public class StaticGlassFishRuntime extends GlassFishRuntime {

    private Main main;
    private HashMap gfMap = new HashMap<String, GlassFish>();
    private static Logger logger = Util.getLogger();
    private static final String autoDelete = "org.glassfish.embeddable.autoDelete";

    public StaticGlassFishRuntime(Main main) {
        this.main = main;
    }

    /**
     * Creates a new GlassFish instance and add it to a Map of instances
     * created by this runtime.
     *
     * @param glassFishProperties
     * @return
     * @throws Exception
     */
    @Override
    public synchronized GlassFish newGlassFish(GlassFishProperties glassFishProperties)
            throws GlassFishException {
        // set env props before updating config, because configuration update may actually trigger
        // some code to be executed which may be depending on the environment variable values.
        try {
            // Don't set temporarily created instanceroot in the user supplied
            // GlassFishProperties, hence clone it.
            Properties cloned = new Properties();
            cloned.putAll(glassFishProperties.getProperties());

            final GlassFishProperties gfProps = new GlassFishProperties(cloned);
            setEnv(gfProps);

            final StartupContext startupContext = new StartupContext(gfProps.getProperties());
            ModulesRegistry modulesRegistry = AbstractFactory.getInstance().createModulesRegistry();
            final Habitat habitat = main.createHabitat(modulesRegistry, startupContext);
            final ModuleStartup gfKernel = main.findStartupService(modulesRegistry, habitat, null, startupContext);
            // create a new GlassFish instance
            GlassFishImpl gfImpl = new GlassFishImpl(gfKernel, habitat, gfProps.getProperties()) {
                @Override
                public void dispose() throws GlassFishException {
                    try {
                        super.dispose();
                    } finally {
                        if ("true".equalsIgnoreCase(gfProps.getProperties().
                                getProperty(autoDelete)) && gfProps.getInstanceRoot() != null) {
                            File instanceRoot = new File(gfProps.getInstanceRoot());
                            if (instanceRoot.exists()) { // might have been deleted already.
                                Util.deleteRecursive(instanceRoot);
                            }
                        }
                    }
                }
            };
            // Add this newly created instance to a Map
            gfMap.put(gfProps.getInstanceRoot(), gfImpl);
            return gfImpl;
        } catch (GlassFishException e) {
            throw e;
        } catch(Exception e) {
            throw new GlassFishException(e);
        }
    }

    @Override
    public synchronized void shutdown() throws GlassFishException {
        for (Object gf : gfMap.values()) {
            try {
                ((GlassFish) gf).dispose();
            } catch (IllegalStateException ex) {
                // ignore.
            }
        }
        gfMap.clear();
        try {
            shutdownInternal();
        } catch (GlassFishException ex) {
            logger.info(ex.getMessage());
        }
    }

    private void setEnv(GlassFishProperties gfProps) throws Exception {
        /*
        final String installRootValue = properties.getProperty(Constants.INSTALL_ROOT_PROP_NAME);
        if (installRootValue != null && !installRootValue.isEmpty()) {
        File installRoot = new File(installRootValue);
        System.setProperty(Constants.INSTALL_ROOT_PROP_NAME, installRoot.getAbsolutePath());
        final Properties asenv = ASMainHelper.parseAsEnv(installRoot);
        for (String s : asenv.stringPropertyNames()) {
        System.setProperty(s, asenv.getProperty(s));
        }
        System.setProperty(Constants.INSTALL_ROOT_URI_PROP_NAME, installRoot.toURI().toString());
        } */
        String instanceRootValue = gfProps.getInstanceRoot();
        if (instanceRootValue == null) {
            instanceRootValue = createTempInstanceRoot(gfProps);
            gfProps.setInstanceRoot(instanceRootValue);
//            gfProps.setProperty(Constants.INSTANCE_ROOT_URI_PROP_NAME,
//                    new File(instanceRootValue).toURI().toString());
        }

        File instanceRoot = new File(instanceRootValue);
        System.setProperty(Constants.INSTANCE_ROOT_PROP_NAME, instanceRoot.getAbsolutePath());
        System.setProperty(Constants.INSTANCE_ROOT_URI_PROP_NAME, instanceRoot.toURI().toString());

        String installRootValue = System.getProperty("org.glassfish.embeddable.installRoot");
        if (installRootValue == null) {
            installRootValue = instanceRoot.getAbsolutePath();
            JarUtil.extractRars(installRootValue);
        }
        JarUtil.setEnv(installRootValue);

        File installRoot = new File(installRootValue);

        // Some legacy code might depend on setting installRoot as system property.
        // Ideally everyone should depend only on StartupContext.
        System.setProperty(Constants.INSTALL_ROOT_PROP_NAME, installRoot.getAbsolutePath());
        System.setProperty(Constants.INSTALL_ROOT_URI_PROP_NAME, installRoot.toURI().toString());

        // StartupContext requires the INSTALL root to be set in the GlassFishProperties.
        gfProps.getProperties().setProperty(Constants.INSTALL_ROOT_PROP_NAME,
                installRoot.getAbsolutePath());
        gfProps.getProperties().setProperty(Constants.INSTALL_ROOT_URI_PROP_NAME,
                installRoot.toURI().toString());
    }

    private String createTempInstanceRoot(GlassFishProperties gfProps)
            throws Exception {
        String tmpDir =  gfProps.getProperties().getProperty("glassfish.embedded.tmpdir",
                System.getProperty("glassfish.embedded.tmpdir"));
        if (tmpDir == null) {
            tmpDir = System.getProperty("java.io.tmpdir");
        } else {
            new File(tmpDir).mkdirs();
        }
        File instanceRoot = File.createTempFile("gfembed", "tmp", new File(tmpDir));
        instanceRoot.delete();
        instanceRoot.mkdir(); // convert the file into a directory.
        try {
            String configDir = "config/";
            String[] resources = new String[]{"keyfile", "server.policy",
                    "cacerts.jks", "keystore.jks", "login.conf", "admin-keyfile"};
            new File(instanceRoot, configDir).mkdirs();
            new File(instanceRoot, "docroot").mkdirs();
            ClassLoader cl = getClass().getClassLoader();
            for (String resource : resources) {
                String resourceName = configDir + resource;
                copy(cl.getResource(resourceName), new File(
                        instanceRoot.getAbsolutePath(), resourceName), false);
            }
            String configFileURI = gfProps.getConfigFileURI();
            URL configFileRL = configFileURI == null ? getClass().getClassLoader().getResource(
                    "org/glassfish/embed/domain.xml") : URI.create(configFileURI).toURL();
                copy(configFileRL, new File(instanceRoot, configDir + "domain.xml"), false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        String autoDeleteVal = gfProps.getProperties().getProperty(autoDelete, "true");
        gfProps.getProperties().setProperty(autoDelete, autoDeleteVal);
        return instanceRoot.getAbsolutePath();
    }

    public static void copy(URL u, File destFile, boolean overwrite) {
        if (u == null || destFile == null) return;
        try {
            if (!destFile.exists() || overwrite) {
                if (!destFile.toURI().equals(u.toURI())) {
                    InputStream stream = u.openStream();
                    destFile.getParentFile().mkdirs();
                    Util.copy(stream, new FileOutputStream(destFile), stream.available());
                    if (logger.isLoggable(Level.FINER)) {
                        logger.finer("Copied " + u.toURI() + " to " + destFile.toURI());
                    }
                }
            }
        } catch (Exception ex) {
        }
    }

}
