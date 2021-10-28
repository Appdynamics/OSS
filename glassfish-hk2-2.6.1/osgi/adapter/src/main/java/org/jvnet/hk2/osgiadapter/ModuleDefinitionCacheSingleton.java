/*
 * Copyright (c) 2015, 2018 Oracle and/or its affiliates. All rights reserved.
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

package org.jvnet.hk2.osgiadapter;

import com.sun.enterprise.module.ModuleDefinition;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import java.io.*;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static org.jvnet.hk2.osgiadapter.Logger.logger;

class ModuleDefinitionCacheSingleton {

    private static ModuleDefinitionCacheSingleton _instance;

    private Map<URI, ModuleDefinition> cachedData = new HashMap<URI, ModuleDefinition>();
    private boolean cacheInvalidated = false;

    private ModuleDefinitionCacheSingleton() {
        try {
            loadCachedData();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public synchronized static ModuleDefinitionCacheSingleton getInstance() {
       if (_instance == null) {
           _instance = new ModuleDefinitionCacheSingleton();
       }

       return _instance;
    }

    public synchronized  void cacheModuleDefinition(URI uri, ModuleDefinition md) {
       if (!cachedData.containsKey(uri)) {
           cacheInvalidated = true;
       } else {
           // should check if md is the same
       }

       cachedData.put(uri, md);
    }

    public synchronized void remove(URI uri) {
        if (cachedData.remove(uri) != null) {
            cacheInvalidated =true;
        }
    }
    /**
     * Loads the inhabitants metadata from the cache. metadata is saved in a file
     * called inhabitants
     *
     * @throws Exception if the file cannot be read correctly
     */
    private void loadCachedData() throws Exception {
        String cacheLocation = getProperty(Constants.HK2_CACHE_DIR);
        if (cacheLocation == null) {
            return;
        }
        File io = new File(cacheLocation, Constants.INHABITANTS_CACHE);
        if (!io.exists()) return;
        if(logger.isLoggable(Level.FINE)) {
            logger.logp(Level.INFO, getClass().getSimpleName(), "loadCachedData", "HK2 cache file = {0}", new Object[]{io});
        }

       ObjectInputStream stream = new ObjectInputStream(new BufferedInputStream(new GZIPInputStream(new FileInputStream(io), getBufferSize())));

       cachedData = (Map<URI, ModuleDefinition>) stream.readObject();
       stream.close();
    }

    /**
     * Saves the inhabitants metadata to the cache in a file called inhabitants
     * @throws java.io.IOException if the file cannot be saved successfully
     */
    public synchronized void saveCache() throws IOException {

        if (!cacheInvalidated) {
            return;
        }

        String cacheLocation = getProperty(Constants.HK2_CACHE_DIR);
        if (cacheLocation == null) {
            return;
        }
        File io = new File(cacheLocation, Constants.INHABITANTS_CACHE);
        if(logger.isLoggable(Level.FINE)) {
            logger.logp(Level.INFO, getClass().getSimpleName(), "saveCache", "HK2 cache file = {0}", new Object[]{io});
        }
        if (io.exists()) io.delete();
        io.createNewFile();
        Map<URI, ModuleDefinition> data = new HashMap<URI, ModuleDefinition>();
        for (ModuleDefinition m : cachedData.values()) {
            data.put(m.getLocations()[0], m);
        }
        ObjectOutputStream os = new ObjectOutputStream(new BufferedOutputStream(new GZIPOutputStream(new FileOutputStream(io)), getBufferSize()));

        os.writeObject(data);
        os.close();

        cacheInvalidated =false;
    }

    private int getBufferSize() {
        int bufsize = Constants.DEFAULT_BUFFER_SIZE;
        try {
            bufsize = Integer.valueOf(getProperty(Constants.HK2_CACHE_IO_BUFFER_SIZE));
        } catch (Exception e) {
        }
        if(logger.isLoggable(Level.FINE)) {
            logger.logp(Level.FINE, "OSGiModulesRegistryImpl", "getBufferSize", "bufsize = {0}", new Object[]{bufsize});
        }
        return bufsize;
    }

    public synchronized ModuleDefinition get(URI uri) {
        ModuleDefinition md = cachedData.get(uri);

        return md;
    }

    public void invalidate() {
        cacheInvalidated = true;
    }

    public boolean isCacheInvalidated() {
        return cacheInvalidated;
    }

    private String getProperty(String property) {
        BundleContext bctx = null;
        try {
            bctx = FrameworkUtil.getBundle(getClass()).getBundleContext();
        } catch (Exception e) {
        }
        String value = bctx != null ? bctx.getProperty(property) : null;
        return value != null ? value : System.getProperty(property);
    }
}
