/*
 * Copyright (c) 2014, 2018 Oracle and/or its affiliates. All rights reserved.
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

package org.glassfish.hk2.xml.internal;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.LoaderClassPath;
import javassist.NotFoundException;

import org.glassfish.hk2.api.MultiException;
import org.glassfish.hk2.utilities.cache.Computable;
import org.glassfish.hk2.utilities.cache.HybridCacheEntry;
import org.glassfish.hk2.utilities.cache.LRUHybridCache;
import org.glassfish.hk2.utilities.general.GeneralUtilities;
import org.glassfish.hk2.utilities.reflection.ClassReflectionHelper;
import org.glassfish.hk2.utilities.reflection.Logger;
import org.glassfish.hk2.utilities.reflection.ReflectionHelper;
import org.glassfish.hk2.xml.internal.alt.clazz.ClassAltClassImpl;
import org.glassfish.hk2.xml.jaxb.internal.BaseHK2JAXBBean;

/**
 * @author jwells
 *
 */
public class JAUtilities {
    private final static String ID_PREFIX = "XmlServiceUID-";
    
    private final static boolean DEBUG_PREGEN = AccessController.doPrivileged(new PrivilegedAction<Boolean>() {
        @Override
        public Boolean run() {
            return Boolean.parseBoolean(
                System.getProperty("org.jvnet.hk2.properties.xmlservice.jaxb.pregenerated", "false"));
        }
            
    });
            
    /* package */ final static boolean DEBUG_GENERATION_TIMING = AccessController.doPrivileged(new PrivilegedAction<Boolean>() {
        @Override
        public Boolean run() {
            return Boolean.parseBoolean(
                System.getProperty("org.jvnet.hk2.properties.xmlservice.jaxb.generationtime", "false"));
        }
            
    });
    
    public final static String GET = "get";
    public final static String SET = "set";
    public final static String IS = "is";
    public final static String LOOKUP = "lookup";
    public final static String ADD = "add";
    public final static String REMOVE = "remove";
    public final static String JAXB_DEFAULT_STRING = "##default";
    public final static String JAXB_DEFAULT_DEFAULT = "\u0000";
    
    private final ClassReflectionHelper classReflectionHelper;
    private final ClassPool defaultClassPool;
    private final CtClass superClazz;
    
    private final Computer computer;
    private final LRUHybridCache<Class<?>, ModelImpl> interface2ModelCache;
    
    private final AtomicLong idGenerator = new AtomicLong();
    
    private static Set<ClassLoader> getClassLoaders(final Class<?> myClass) {
        return AccessController.doPrivileged(new PrivilegedAction<Set<ClassLoader>>() {

            @Override
            public Set<ClassLoader> run() {
                Set<ClassLoader> retVal = new LinkedHashSet<ClassLoader>();
                
                ClassLoader ccl = Thread.currentThread().getContextClassLoader();
                if (ccl != null) {
                    retVal.add(ccl);
                }
                
                retVal.add(myClass.getClassLoader());
                
                return retVal;
            }
            
            
        });
    }
    
    /* package */ JAUtilities(ClassReflectionHelper classReflectionHelper) {
        defaultClassPool = ClassPool.getDefault();
        
        for (ClassLoader cl : getClassLoaders(this.getClass())) {
            defaultClassPool.appendClassPath(new LoaderClassPath(cl));
        }
        
        this.classReflectionHelper = classReflectionHelper;
        try {
            superClazz = defaultClassPool.get(BaseHK2JAXBBean.class.getName());
        }
        catch (NotFoundException e) {
            throw new MultiException(e);
        }
        
        computer = new Computer(this);
        interface2ModelCache = new LRUHybridCache<Class<?>, ModelImpl>(Integer.MAX_VALUE - 1, computer);
    }
    
    /**
     * Gets the XmlService wide unique identifier
     * @return A unique identifier for unkeyed multi-children
     */
    public String getUniqueId() {
        return ID_PREFIX + idGenerator.getAndAdd(1L);
    }
    
    /**
     * Goes from interface name (fully qualified) to the associated Model
     * 
     * @param iFace Fully qualified interface name (not proxy)
     * @return The Model for the interface
     */
    public ModelImpl getModel(Class<?> iFace) {
        HybridCacheEntry<ModelImpl> entry = interface2ModelCache.compute(iFace);
        return entry.getValue();
    }
    
    public synchronized void convertRootAndLeaves(Class<?> root, boolean mustConvertAll) {
        long currentTime = 0L;
        if (DEBUG_GENERATION_TIMING) {
            computer.numGenerated = 0;
            computer.numPreGenerated = 0;
            
            currentTime = System.currentTimeMillis();
        }
        
        ModelImpl rootModel = interface2ModelCache.compute(root).getValue();
        if (!mustConvertAll) {
            if (DEBUG_GENERATION_TIMING) {
                currentTime = System.currentTimeMillis() - currentTime;
                
                Logger.getLogger().debug("Took " + currentTime + " to perform " +
                  computer.numGenerated + " generations with " +
                  computer.numPreGenerated + " pre generated with a lazy parser");
            }
            return;
        }
        
        HashSet<Class<?>> cycles = new HashSet<Class<?>>();
        cycles.add(root);
        
        convertAllRootAndLeaves(rootModel, cycles);
        
        if (DEBUG_GENERATION_TIMING) {
            currentTime = System.currentTimeMillis() - currentTime;
            
            Logger.getLogger().debug("Took " + currentTime + " milliseconds to perform " +
              computer.numGenerated + " generations with " +
              computer.numPreGenerated + " pre generated with a non-lazy parser");
        }
    }
    
    private void convertAllRootAndLeaves(ModelImpl rootModel, HashSet<Class<?>> cycles) {
        for (ParentedModel parentModel : rootModel.getAllChildren()) {
            Class<?> convertMe = parentModel.getChildModel().getOriginalInterfaceAsClass();
            
            // Remove cycles
            if (cycles.contains(convertMe)) continue;
            cycles.add(convertMe);
            
            ModelImpl childModel = interface2ModelCache.compute(convertMe).getValue();
            
            convertAllRootAndLeaves(childModel, cycles);
        }
    }
    
    private CtClass getBaseClass() {
        return superClazz;
    }
    
    private ClassPool getClassPool() {
        return defaultClassPool;
    }
    
    public int getNumGenerated() {
        return computer.numGenerated;
    }
    
    public int getNumPreGenerated() {
        return computer.numPreGenerated;
    }
    
    private final class Computer implements Computable<Class<?>, HybridCacheEntry<ModelImpl>> {
        private final JAUtilities jaUtilities;
        private int numGenerated;
        private int numPreGenerated;
        
        private Computer(JAUtilities jaUtilities) {
            this.jaUtilities = jaUtilities;
        }

        /* (non-Javadoc)
         * @see org.glassfish.hk2.utilities.cache.Computable#compute(java.lang.Object)
         */
        @Override
        public HybridCacheEntry<ModelImpl> compute(Class<?> key) {
            String iFaceName = key.getName();
            String proxyName = Utilities.getProxyNameFromInterfaceName(iFaceName);
            
            Class<?> proxyClass = GeneralUtilities.loadClass(key.getClassLoader(), proxyName);
            if (proxyClass == null) {
                numGenerated++;
                
                if (DEBUG_PREGEN) {
                    Logger.getLogger().debug("Pregenerating proxy for " + key.getName());
                }
                
                // Generate the proxy
                try {
                  CtClass generated = Generator.generate(new ClassAltClassImpl(key, classReflectionHelper),
                        jaUtilities.getBaseClass(),
                        jaUtilities.getClassPool());
                  
                  proxyClass = generated.toClass(key.getClassLoader(), key.getProtectionDomain());
                }
                catch (RuntimeException re) {
                    throw new RuntimeException("Could not compile proxy for class " + iFaceName, re);
                }
                catch (Throwable th) {
                    throw new RuntimeException("Could not compile proxy for class " + iFaceName, th);
                }
            }
            else {
                if (DEBUG_PREGEN) {
                    Logger.getLogger().debug("Proxy for " + key.getName() + " was pregenerated");
                }
                
                numPreGenerated++;
            }
            
            Method getModelMethod;
            try {
                getModelMethod = proxyClass.getMethod(Generator.STATIC_GET_MODEL_METHOD_NAME, new Class<?>[0]);
            }
            catch (NoSuchMethodException e) {
                throw new AssertionError("This proxy must have been generated with an old generator, it has no __getModel method for " + key.getName());
            }
            
            ModelImpl retVal;
            try {
                retVal = (ModelImpl) ReflectionHelper.invoke(null, getModelMethod, new Object[0], false);
            }
            catch (RuntimeException re) {
                throw re;
            }
            catch (Throwable e) {
                throw new RuntimeException(e);
            }
            
            if (retVal == null) {
                throw new AssertionError("The __getModel method on " + proxyClass.getName() + " returned null");
            }
            
            retVal.setJAUtilities(jaUtilities, key.getClassLoader());
            
            return interface2ModelCache.createCacheEntry(key, retVal, false);
        }
        
    }
}
