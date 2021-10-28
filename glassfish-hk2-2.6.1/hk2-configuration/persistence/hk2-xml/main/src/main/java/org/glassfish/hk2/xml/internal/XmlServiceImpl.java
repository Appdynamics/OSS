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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.xml.stream.XMLStreamReader;

import org.glassfish.hk2.api.ActiveDescriptor;
import org.glassfish.hk2.api.DescriptorVisibility;
import org.glassfish.hk2.api.DynamicConfiguration;
import org.glassfish.hk2.api.DynamicConfigurationService;
import org.glassfish.hk2.api.IterableProvider;
import org.glassfish.hk2.api.MultiException;
import org.glassfish.hk2.api.Self;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.Visibility;
import org.glassfish.hk2.configuration.hub.api.Hub;
import org.glassfish.hk2.configuration.hub.api.WriteableBeanDatabase;
import org.glassfish.hk2.utilities.cache.CacheUtilities;
import org.glassfish.hk2.utilities.cache.WeakCARCache;
import org.glassfish.hk2.utilities.reflection.ClassReflectionHelper;
import org.glassfish.hk2.utilities.reflection.Logger;
import org.glassfish.hk2.utilities.reflection.internal.ClassReflectionHelperImpl;
import org.glassfish.hk2.xml.api.XmlHubCommitMessage;
import org.glassfish.hk2.xml.api.XmlRootHandle;
import org.glassfish.hk2.xml.api.XmlService;
import org.glassfish.hk2.xml.jaxb.internal.BaseHK2JAXBBean;
import org.glassfish.hk2.xml.spi.PreGenerationRequirement;
import org.glassfish.hk2.xml.spi.XmlServiceParser;

/**
 * @author jwells
 *
 */
@Singleton
@Visibility(DescriptorVisibility.LOCAL)
public class XmlServiceImpl implements XmlService {
    public final static boolean DEBUG_PARSING = AccessController.doPrivileged(new PrivilegedAction<Boolean>() {
        @Override
        public Boolean run() {
            return Boolean.parseBoolean(
                System.getProperty("org.jvnet.hk2.xmlservice.parser.stream", "false"));
        }
            
    });
    
    @Inject
    private ServiceLocator serviceLocator;
    
    @Inject
    private DynamicConfigurationService dynamicConfigurationService;
    
    @Inject
    private Hub hub;
    
    @Inject
    private IterableProvider<XmlServiceParser> parser;
    
    private final ClassReflectionHelper classReflectionHelper = new ClassReflectionHelperImpl();
    
    private final JAUtilities jaUtilities = new JAUtilities(classReflectionHelper);
    
    @Inject @Self
    private ActiveDescriptor<?> selfDescriptor;
    
    private final WeakCARCache<Package, Map<String, String>> packageNamespaceCache =
            CacheUtilities.createWeakCARCache(new PackageToNamespaceComputable(), 20, true);
    
    /* (non-Javadoc)
     * @see org.glassfish.hk2.xml.api.XmlService#unmarshall(java.net.URI, java.lang.Class, boolean, boolean)
     */
    @Override
    public <T> XmlRootHandle<T> unmarshal(URI uri,
            Class<T> jaxbAnnotatedClassOrInterface) {
        return unmarshal(uri, jaxbAnnotatedClassOrInterface, true, true);
    }
    
    @Override
    public <T> XmlRootHandle<T> unmarshal(InputStream input,
            Class<T> jaxbAnnotatedInterface) {
        return unmarshal(input, jaxbAnnotatedInterface, true, true);
    }
    
    /* (non-Javadoc)
     * @see org.glassfish.hk2.xml.api.XmlService#unmarshall(java.net.URI, java.lang.Class)
     */
    @Override
    public <T> XmlRootHandle<T> unmarshal(URI uri,
            Class<T> jaxbAnnotatedInterface,
            boolean advertiseInRegistry, boolean advertiseInHub) {
        return unmarshal(uri, jaxbAnnotatedInterface, advertiseInRegistry, advertiseInHub, null);
    }

    /* (non-Javadoc)
     * @see org.glassfish.hk2.xml.api.XmlService#unmarshall(java.net.URI, java.lang.Class)
     */
    @Override
    public <T> XmlRootHandle<T> unmarshal(URI uri,
            Class<T> jaxbAnnotatedInterface,
            boolean advertiseInRegistry, boolean advertiseInHub,
            Map<String, Object> options) {
        if (uri == null || jaxbAnnotatedInterface == null) throw new IllegalArgumentException();
        if (!jaxbAnnotatedInterface.isInterface()) {
            throw new IllegalArgumentException("Only an interface can be given to unmarshall: " + jaxbAnnotatedInterface.getName());
        }
        
        XmlServiceParser localParser = parser.named(selfDescriptor.getName()).get();
        if (localParser == null) {
            throw new IllegalStateException("There is no XmlServiceParser implementation");
        }
        
        try {
            boolean generateAll = PreGenerationRequirement.MUST_PREGENERATE.equals(localParser.getPreGenerationRequirement());
            jaUtilities.convertRootAndLeaves(jaxbAnnotatedInterface, generateAll);
            
            ModelImpl model = jaUtilities.getModel(jaxbAnnotatedInterface);
                
            return unmarshallClass(uri, null, model, localParser, null, advertiseInRegistry, advertiseInHub, options);
        }
        catch (RuntimeException re) {
            throw re;
        }
        catch (Throwable e) {
            throw new MultiException(e);
        }
    }
    
    /* (non-Javadoc)
     * @see org.glassfish.hk2.xml.api.XmlService#unmarshall(javax.xml.stream.XMLStreamReader, java.lang.Class, boolean, boolean)
     */
    @Override
    public <T> XmlRootHandle<T> unmarshal(XMLStreamReader reader,
            Class<T> jaxbAnnotatedInterface, boolean advertiseInRegistry,
            boolean advertiseInHub) {
        return unmarshal(reader, jaxbAnnotatedInterface, advertiseInRegistry, advertiseInHub, null);
    }
    
    /* (non-Javadoc)
     * @see org.glassfish.hk2.xml.api.XmlService#unmarshall(javax.xml.stream.XMLStreamReader, java.lang.Class, boolean, boolean)
     */
    @Override
    public <T> XmlRootHandle<T> unmarshal(XMLStreamReader reader,
            Class<T> jaxbAnnotatedInterface, boolean advertiseInRegistry,
            boolean advertiseInHub,
            Map<String, Object> options) {
        if (reader == null || jaxbAnnotatedInterface == null) throw new IllegalArgumentException();
        if (!jaxbAnnotatedInterface.isInterface()) {
            throw new IllegalArgumentException("Only an interface can be given to unmarshall: " + jaxbAnnotatedInterface.getName());
        }
        
        try {
            jaUtilities.convertRootAndLeaves(jaxbAnnotatedInterface, false);
            
            ModelImpl model = jaUtilities.getModel(jaxbAnnotatedInterface);
                
            return unmarshallClass(null, null, model, null, reader, advertiseInRegistry, advertiseInHub, options);
        }
        catch (RuntimeException re) {
            throw re;
        }
        catch (Throwable e) {
            throw new MultiException(e);
        }
    }
    
    /* (non-Javadoc)
     * @see org.glassfish.hk2.xml.api.XmlService#unmarshall(javax.xml.stream.XMLStreamReader, java.lang.Class, boolean, boolean)
     */
    @Override
    public <T> XmlRootHandle<T> unmarshal(InputStream input,
            Class<T> jaxbAnnotatedInterface, boolean advertiseInRegistry,
            boolean advertiseInHub) {
        return unmarshal(input, jaxbAnnotatedInterface, advertiseInRegistry, advertiseInHub, null);
    }
    
    /* (non-Javadoc)
     * @see org.glassfish.hk2.xml.api.XmlService#unmarshall(javax.xml.stream.XMLStreamReader, java.lang.Class, boolean, boolean)
     */
    @Override
    public <T> XmlRootHandle<T> unmarshal(InputStream input,
            Class<T> jaxbAnnotatedInterface, boolean advertiseInRegistry,
            boolean advertiseInHub,
            Map<String, Object> options) {
        if (input == null || jaxbAnnotatedInterface == null) throw new IllegalArgumentException();
        if (!jaxbAnnotatedInterface.isInterface()) {
            throw new IllegalArgumentException("Only an interface can be given to unmarshall: " + jaxbAnnotatedInterface.getName());
        }
        
        XmlServiceParser localParser = parser.named(selfDescriptor.getName()).get();
        if (localParser == null) {
            throw new IllegalStateException("There is no XmlServiceParser implementation");
        }
        
        try {
            jaUtilities.convertRootAndLeaves(jaxbAnnotatedInterface, false);
            
            ModelImpl model = jaUtilities.getModel(jaxbAnnotatedInterface);
                
            return unmarshallClass(null, input, model, localParser, null, advertiseInRegistry, advertiseInHub, options);
        }
        catch (RuntimeException re) {
            throw re;
        }
        catch (Throwable e) {
            throw new MultiException(e);
        }
    }
    
    private <T> XmlRootHandle<T> unmarshallClass(URI uri, InputStream inputStream, ModelImpl model,
            XmlServiceParser localParser, XMLStreamReader reader,
            boolean advertise, boolean advertiseInHub, Map<String, Object> options) throws Exception {
        long elapsedUpToJAXB = 0;
        if (JAUtilities.DEBUG_GENERATION_TIMING) {
            elapsedUpToJAXB = System.currentTimeMillis();
        }
        
        Hk2JAXBUnmarshallerListener listener = new Hk2JAXBUnmarshallerListener(jaUtilities, classReflectionHelper);
        
        long jaxbUnmarshallElapsedTime = 0L;
        if (JAUtilities.DEBUG_GENERATION_TIMING) {
            jaxbUnmarshallElapsedTime = System.currentTimeMillis();
            elapsedUpToJAXB = jaxbUnmarshallElapsedTime - elapsedUpToJAXB;
            Logger.getLogger().debug("Time up to parsing " + uri + " is " + elapsedUpToJAXB + " milliseconds");
        }
        
        T root;
        if (localParser != null) {
            if (uri != null) {
                root = localParser.parseRoot(model, uri, listener, options);
            }
            else {
                root = localParser.parseRoot(model, inputStream, listener, options);
            }
        }
        else {
            root = XmlStreamImpl.parseRoot(this, model, reader, listener);
        }
        
        long elapsedJAXBToAdvertisement = 0;
        if (JAUtilities.DEBUG_GENERATION_TIMING) {
            elapsedJAXBToAdvertisement = System.currentTimeMillis();
            jaxbUnmarshallElapsedTime = elapsedJAXBToAdvertisement - jaxbUnmarshallElapsedTime;
            Logger.getLogger().debug("Time parsing " + uri + " is " + jaxbUnmarshallElapsedTime + " milliseconds " +
              ", now with " + jaUtilities.getNumGenerated() + " proxies generated and " +
                    jaUtilities.getNumPreGenerated() + " pre generated proxies loaded");
        }
        
        DynamicChangeInfo<T> changeControl = new DynamicChangeInfo<T>(jaUtilities,
                hub,
                advertiseInHub,
                this,
                dynamicConfigurationService,
                advertise,
                serviceLocator);
        
        XmlRootHandleImpl<T> retVal = new XmlRootHandleImpl<T>(this, hub, root, model, uri, advertise, advertiseInHub, changeControl);
        
        changeControl.setRoot(retVal);
        
        for (BaseHK2JAXBBean base : listener.getAllBeans()) {
            String instanceName = Utilities.createInstanceName(base);
            base._setInstanceName(instanceName);
            
            base._setDynamicChangeInfo(retVal, changeControl);
            
            if (DEBUG_PARSING) {
                Logger.getLogger().debug("XmlServiceDebug found bean " + base);
            }
        }
        if (DEBUG_PARSING) {
            Logger.getLogger().debug("XmlServiceDebug after parsing all beans in " + uri);
        }
        
        long elapsedPreAdvertisement = 0L;
        if (JAUtilities.DEBUG_GENERATION_TIMING) {
            elapsedPreAdvertisement = System.currentTimeMillis();
            elapsedJAXBToAdvertisement = elapsedPreAdvertisement - elapsedJAXBToAdvertisement;
            Logger.getLogger().debug("Time from parsing to PreAdvertisement " + uri + " is " + elapsedJAXBToAdvertisement + " milliseconds");
        }
        
        DynamicConfiguration config = (advertise) ? dynamicConfigurationService.createDynamicConfiguration() : null ;
        WriteableBeanDatabase wdb = (advertiseInHub) ? hub.getWriteableDatabaseCopy() : null ;
        
        boolean attachedTransaction = false;
        if (config != null && wdb != null) {
            attachedTransaction = true;
            
            wdb.setCommitMessage(new XmlHubCommitMessage() {});
            
            config.registerTwoPhaseResources(wdb.getTwoPhaseResource());
        }
        
        LinkedList<BaseHK2JAXBBean> allBeans = listener.getAllBeans();
        List<ActiveDescriptor<?>> addedDescriptors = new ArrayList<ActiveDescriptor<?>>(allBeans.size());
        for (BaseHK2JAXBBean bean : allBeans) {
            ActiveDescriptor<?> added = Utilities.advertise(wdb, config, bean);
            if (added != null) {
                addedDescriptors.add(added);
            }
        }
        
        long elapsedHK2Advertisement = 0L;
        if (JAUtilities.DEBUG_GENERATION_TIMING) {
            elapsedHK2Advertisement = System.currentTimeMillis();
            elapsedPreAdvertisement = elapsedHK2Advertisement - elapsedPreAdvertisement;
            Logger.getLogger().debug("Time from JAXB to PreAdvertisement " + uri + " is " + elapsedPreAdvertisement + " milliseconds");
        }
        
        if (config != null) {
            config.commit();
        }
        
        long elapsedHubAdvertisement = 0L;
        if (JAUtilities.DEBUG_GENERATION_TIMING) {
            elapsedHubAdvertisement = System.currentTimeMillis();
            elapsedHK2Advertisement = elapsedHubAdvertisement - elapsedHK2Advertisement;
            Logger.getLogger().debug("Time to advertise " + uri + " in HK2 is " + elapsedHK2Advertisement + " milliseconds");
        }
        
        if (wdb != null && !attachedTransaction) {
            wdb.commit(new XmlHubCommitMessage() {});
        }
        
        // The following is put here so that InstanceLifecycleListeners will get invoked at this time
        // rather than later so that defaulting can be done now.  It doesn't hurt because these are
        // all constant descriptors
        for (ActiveDescriptor<?> ad : addedDescriptors) {
            serviceLocator.getServiceHandle(ad).getService();
        }
        
        if (JAUtilities.DEBUG_GENERATION_TIMING) {
            elapsedHubAdvertisement = System.currentTimeMillis() - elapsedHubAdvertisement;
            Logger.getLogger().debug("Time to advertise " + uri + " in Hub is " + elapsedHubAdvertisement + " milliseconds");
        }
        
        return retVal;
    }
    
    

    /* (non-Javadoc)
     * @see org.glassfish.hk2.xml.api.XmlService#createEmptyHandle(java.lang.Class)
     */
    @Override
    public <T> XmlRootHandle<T> createEmptyHandle(
            Class<T> jaxbAnnotatedInterface, boolean advertiseInRegistry,
            boolean advertiseInHub) {
        if (!jaxbAnnotatedInterface.isInterface()) {
            throw new IllegalArgumentException("Only an interface can be given to unmarshall: " + jaxbAnnotatedInterface.getName());
        }
        try {
            jaUtilities.convertRootAndLeaves(jaxbAnnotatedInterface, true);
            
            ModelImpl model = jaUtilities.getModel(jaxbAnnotatedInterface);
            
            DynamicChangeInfo<T> change = new DynamicChangeInfo<T>(jaUtilities,
                    hub,
                    advertiseInHub,
                    this,
                    dynamicConfigurationService,
                    advertiseInRegistry,
                    serviceLocator);
        
            XmlRootHandleImpl<T> retVal = new XmlRootHandleImpl<T>(this,
                    hub, null, model, null, advertiseInRegistry,
                    advertiseInHub, change);
            
            change.setRoot(retVal);
            
            return retVal;
        }
        catch (RuntimeException re) {
            throw re;
        }
        catch (Exception e) {
            throw new MultiException(e);
        }
    }
    
    /* (non-Javadoc)
     * @see org.glassfish.hk2.xml.api.XmlService#createEmptyHandle(java.lang.Class, boolean, boolean)
     */
    @Override
    public <T> XmlRootHandle<T> createEmptyHandle(
            Class<T> jaxbAnnotationInterface) {
        return createEmptyHandle(jaxbAnnotationInterface, true, true);
    }
    
    /* (non-Javadoc)
     * @see org.glassfish.hk2.xml.api.XmlService#createBean(java.lang.Class)
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T createBean(Class<T> beanInterface) {
        if (!beanInterface.isInterface()) {
            throw new IllegalArgumentException("Only an interface can be given to unmarshall: " + beanInterface.getName());
        }
        
        jaUtilities.convertRootAndLeaves(beanInterface, true);
        
        ModelImpl model = jaUtilities.getModel(beanInterface);
        
        T retVal = (T) Utilities.createBean(model.getProxyAsClass());
        
        BaseHK2JAXBBean base = (BaseHK2JAXBBean) retVal;
        
        base._setClassReflectionHelper(classReflectionHelper);
        base._setActive();
        
        return retVal;
    }
    
    @Override
    public <T> void marshal(OutputStream outputStream, XmlRootHandle<T> rootHandle) throws IOException {
        marshal(outputStream, rootHandle, null);
    }
    
    @Override
    public <T> void marshal(OutputStream outputStream, XmlRootHandle<T> rootHandle, Map<String, Object> options) throws IOException {
        XmlServiceParser localParser = parser.named(selfDescriptor.getName()).get();
        if (localParser == null) {
            throw new IllegalStateException("There is no XmlServiceParser implementation");
        }
        
        XmlRootHandleImpl<T> impl = (XmlRootHandleImpl<T>) rootHandle;
        DynamicChangeInfo<T> changeControl = impl.getChangeInfo();
        
        if (changeControl == null) {
            throw new IllegalStateException("May not marshal an unfinished rootHandle");
        }
        
        changeControl.getReadLock().lock();
        try {
            localParser.marshal(outputStream, rootHandle, options);
        }
        finally {
            changeControl.getReadLock().unlock();
        }
    }

    public ClassReflectionHelper getClassReflectionHelper() {
        return classReflectionHelper;
    }
    
    /* package */ DynamicConfigurationService getDynamicConfigurationService() {
        return dynamicConfigurationService;
    }

    public ServiceLocator getServiceLocator() {
        return serviceLocator;
    }
    
    public XmlServiceParser getParser() {
        return parser.named(selfDescriptor.getName()).get();
    }
    
    public Map<String, String> getPackageNamespace(Class<?> clazz) {
        Package p = clazz.getPackage();
        
        return packageNamespaceCache.compute(p);
    }
    
    @PreDestroy
    private void preDestroy() {
        packageNamespaceCache.clear();
    }
}
