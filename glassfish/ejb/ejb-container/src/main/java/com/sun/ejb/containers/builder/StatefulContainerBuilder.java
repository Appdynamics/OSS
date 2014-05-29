/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ejb.containers.builder;

import java.io.File;
import java.io.Serializable;
import java.util.Map;
import java.util.logging.Logger;
import java.util.logging.Level;

import com.sun.appserv.util.cache.CacheListener;

import com.sun.ejb.base.container.util.CacheProperties;

import com.sun.ejb.base.sfsb.util.CheckpointPolicyImpl;
import com.sun.ejb.base.sfsb.util.ScrambledKeyGenerator;
import com.sun.ejb.base.sfsb.util.SimpleKeyGenerator;
import com.sun.ejb.base.sfsb.util.EJBServerConfigLookup;

import com.sun.ejb.containers.StatefulSessionContainer;

import com.sun.ejb.containers.BaseContainer;

import com.sun.ejb.containers.util.cache.FIFOSessionCache;
import com.sun.ejb.containers.util.cache.LruSessionCache;
import com.sun.ejb.containers.util.cache.NRUSessionCache;
import com.sun.ejb.containers.util.cache.UnBoundedSessionCache;

import com.sun.enterprise.config.serverbeans.*;
import com.sun.enterprise.deployment.EjbDescriptor;

import com.sun.ejb.spi.container.SFSBContainerInitialization;

import org.glassfish.api.deployment.DeployCommandParameters;
import org.glassfish.api.deployment.DeploymentContext;
import org.glassfish.gms.bootstrap.GMSAdapter;
import org.glassfish.gms.bootstrap.GMSAdapterService;
import org.glassfish.ha.store.api.BackingStore;
import org.glassfish.ha.store.api.BackingStoreConfiguration;
import org.glassfish.ha.store.api.BackingStoreException;
import org.glassfish.ha.store.api.BackingStoreFactory;
import org.glassfish.ha.store.util.KeyTransformer;
import org.glassfish.ha.store.util.SimpleMetadata;
import org.jvnet.hk2.annotations.Service;
import org.jvnet.hk2.annotations.Scoped;
import org.jvnet.hk2.annotations.Inject;
import org.jvnet.hk2.component.PerLookup;
import org.jvnet.hk2.component.Habitat;

/**
 * A builder for StatefulSessionContainer. Takes care of
 * building / initializing the StatefulSessionContainer
 * with the following classes:
 * a) Cache (LRU / NRU / FIFO / UnBounded)
 * b) SFSBStoreManager (Using PersistenceStrategyBuilder)
 * c) Cache passivation task (if idle-timeout is greater than 0)
 * d) Passivated sessions removal task (if removal-timeout is greater than 0)
 * e) CheckpointPolicy (if ha enabled)
 * f) SFSBUUIDUtil
 * g) BeanLifecycleManager
 *
 * @author Mahesh Kannan
 */
@Service
@Scoped(PerLookup.class)
public class StatefulContainerBuilder
        extends BaseContainerBuilder {
    private static final Level TRACE_LEVEL = Level.FINE;

    private StatefulSessionContainer sfsbContainer;

    private SFSBContainerInitialization containerInitialization;

    @Inject
    private Habitat habitat;

    @Inject
    private CacheProperties cacheProps;

    @Inject
    private EJBServerConfigLookup ejbConfigLookup;

    @Inject(optional = true)
    private AvailabilityService availabilityService;

    @Inject(optional = true)
    private EjbContainerAvailability ejbAvailability;

    @Inject
    EjbContainer ejbContainerConfig;

    @Inject(optional = true)
    GMSAdapterService gmsAdapterService;
    
    private LruSessionCache sessionCache;

    private BackingStore<Serializable, SimpleMetadata> backingStore;

    private boolean HAEnabled = false;

    private boolean asyncReplication = true;

    private SimpleKeyGenerator keyGen;

    public StatefulContainerBuilder() {
        super();
    }

    public BaseContainer createContainer(
            EjbDescriptor ejbDescriptor, ClassLoader loader)
            throws Exception {
        cacheProps.init(ejbDescriptor);
        ejbConfigLookup.initWithEjbDescriptor(ejbDescriptor);
        sfsbContainer = new StatefulSessionContainer(ejbDescriptor, loader);
        containerInitialization = (SFSBContainerInitialization) sfsbContainer;

        return sfsbContainer;
    }

    public void buildComponents()
            throws Exception {
        if (availabilityService != null) {
            this.HAEnabled = Boolean.valueOf(availabilityService.getAvailabilityEnabled());
            _logger.log(Level.INFO, "TopLevel AvailabilityService.getAvailabilityEnabled => " + this.HAEnabled);
            if ((this.HAEnabled) && (ejbAvailability != null)) {
                this.HAEnabled = Boolean.valueOf(ejbAvailability.getAvailabilityEnabled());
                _logger.log(Level.INFO, "TopLevel EjbAvailabilityService.getAvailabilityEnabled => " + this.HAEnabled);
            }

            boolean appLevelHAEnabled = false;
            try {
                if (HAEnabled) {
                    DeploymentContext dc = getDynamicDeploymentContext();
                    if (dc != null) {
                        DeployCommandParameters params = dc.getCommandParameters(DeployCommandParameters.class);
                        if (params != null) {
                            appLevelHAEnabled = params.availabilityenabled;
                            asyncReplication = params.asyncreplication;
                        }
                    }
                    
                    _logger.log(Level.INFO, "**Global AvailabilityEnabled => " + this.HAEnabled
                        + "; isAppHAEnabled: " + appLevelHAEnabled);
                }
            } catch (Exception ex) {
                _logger.log(Level.WARNING, "Exception while trying to determine availability-enabled settings for this app", ex);
                appLevelHAEnabled = false;
            }

            HAEnabled = HAEnabled && appLevelHAEnabled;
            _logger.log(Level.INFO, "StatefulContainerBuilder AvailabilityEnabled for this app => " + this.HAEnabled);
        }



        buildCheckpointPolicy(this.HAEnabled);
        buildSFSBUUIDUtil();

        //First build BackingStore before Cache is built
        buildStoreManager();

        buildCache();
        scheduleTimerTasks(sfsbContainer);
    }

    /************************* Private Methods *************************/
    /**
     * ***************************************************************
     */

    private final void buildCheckpointPolicy(boolean haEnabled) {
        containerInitialization.setCheckpointPolicy(
                new CheckpointPolicyImpl(haEnabled));
    }

    private void buildSFSBUUIDUtil() {
        //Just for debugging purpose,  we instantiate
        //  two different key generators
        keyGen = HAEnabled
                ? new ScrambledKeyGenerator(getIPAddress(), getPort())
                : new SimpleKeyGenerator(getIPAddress(), getPort());
        containerInitialization.setSFSBUUIDUtil(keyGen);
    }

    private void buildStoreManager()
        throws BackingStoreException {

        String persistenceStoreType = "file";

        if (ejbAvailability != null) {
            persistenceStoreType = HAEnabled
                ? ejbAvailability.getSfsbHaPersistenceType() : ejbAvailability.getSfsbPersistenceType();
            if ("ha".equals(persistenceStoreType)) {
                persistenceStoreType = "replicated";
            } else if ("memory".equals(persistenceStoreType)) {
                persistenceStoreType = "file";
            }
        }


        BackingStoreFactory factory = null;
        try {
            factory = habitat.getComponent(BackingStoreFactory.class, persistenceStoreType);
        } catch (Exception ex) {
            _logger.log(Level.WARNING, "Could not instantiate backing store factory for type: " + persistenceStoreType, ex);
            persistenceStoreType = "noop";
            try {
                factory = habitat.getComponent(BackingStoreFactory.class, persistenceStoreType);
            } catch (Exception ex2) {
                _logger.log(Level.WARNING, "Could not instantiate backing store factory for type: " + persistenceStoreType, ex2);
                throw new BackingStoreException("Could not instantiate BackingStore Factory for persistence type: " + persistenceStoreType, ex);
            }
        }

        BackingStoreConfiguration<Serializable, SimpleMetadata> conf = new BackingStoreConfiguration<Serializable, SimpleMetadata>();
        String storeName = ejbDescriptor.getName() + "-" + ejbDescriptor.getUniqueId() + "-BackingStore";

        _logger.log(Level.INFO, "StatefulContainerBuilder.buildStoreManager() storeName: " + storeName);
        
        String subDirName = "";

        subDirName += ejbDescriptor.getName() + "-" + ejbDescriptor.getUniqueId();
        
        conf.setShortUniqueName(""+ejbDescriptor.getUniqueId()).setStoreName(storeName)
                .setStoreType(persistenceStoreType)
                .setBaseDirectory(new File(ejbContainerConfig.getSessionStore(), subDirName))
                .setKeyClazz(Serializable.class)
                .setValueClazz(SimpleMetadata.class)
                .setClassLoader(StatefulContainerBuilder.class.getClassLoader());


        Map<String, Object> vendorMap = conf.getVendorSpecificSettings();
        vendorMap.put("local.caching", true);
        vendorMap.put("start.gms", false);
        vendorMap.put("async.replication", asyncReplication);
        vendorMap.put("broadcast.remove.expired", false);
        vendorMap.put("value.class.is.thread.safe", true);
        vendorMap.put("key.transformer", keyGen);

        if (gmsAdapterService != null) {
            GMSAdapter gmsAdapter = gmsAdapterService.getGMSAdapter();
            if (gmsAdapter != null) {
                conf.setClusterName(gmsAdapter.getClusterName());
                conf.setInstanceName(gmsAdapter.getModule().getInstanceName());
            }
        }


        try {
            sfsbContainer.setSfsbHaPersistenceType(persistenceStoreType);
            this.backingStore = factory.createBackingStore(conf);
        } catch (Exception ex) {
            _logger.log(Level.WARNING, "Could not instantiate BackingStore for persistence type: " + persistenceStoreType, ex);
            throw new BackingStoreException("Could not instantiate BackingStore for persistence type: " + persistenceStoreType, ex);
        }
        _logger.log(Level.WARNING, "StatefulContainerbuilder instantiated store: " +
            backingStore + "; ha-enabled: " + HAEnabled + " ==> " + conf);
    }

    private void buildCache() {
        String cacheName = ejbDescriptor.getEjbClassName();
        String victimPolicy = cacheProps.getVictimSelectionPolicy();

        if (cacheProps.getMaxCacheSize() <= 0) {
            sessionCache = new UnBoundedSessionCache(cacheName, sfsbContainer,
                    cacheProps.getCacheIdleTimeoutInSeconds(),
                    cacheProps.getRemovalTimeoutInSeconds());
        } else if ("lru".equalsIgnoreCase(victimPolicy)) {
            sessionCache = new LruSessionCache(cacheName, sfsbContainer,
                    cacheProps.getCacheIdleTimeoutInSeconds(),
                    cacheProps.getRemovalTimeoutInSeconds());
        } else if ("fifo".equalsIgnoreCase(victimPolicy)) {
            sessionCache = new FIFOSessionCache(cacheName, sfsbContainer,
                    cacheProps.getCacheIdleTimeoutInSeconds(),
                    cacheProps.getRemovalTimeoutInSeconds());
        } else {
            sessionCache = new NRUSessionCache(cacheName, sfsbContainer,
                    cacheProps.getCacheIdleTimeoutInSeconds(),
                    cacheProps.getRemovalTimeoutInSeconds());
        }


        float ratio = (float) (1.0 * cacheProps.getNumberOfVictimsToSelect()
                / cacheProps.getMaxCacheSize());
        float loadFactor = (float) (1.0 - ratio);
        if (loadFactor < 0 || loadFactor > 1) {
            loadFactor = 0.75f;
        }

        if (cacheProps.getMaxCacheSize() <= 0) {
            sessionCache.init(16 * 1024, loadFactor, null);
        } else {
            sessionCache.init(cacheProps.getMaxCacheSize(), loadFactor, null);
        }

        sessionCache.addCacheListener((CacheListener) sfsbContainer);

        sfsbContainer.setSessionCache(sessionCache);
        sessionCache.setBackingStore(backingStore);
        sfsbContainer.setBackingStore(this.backingStore);
        if (cacheProps.getNumberOfVictimsToSelect() >
                sfsbContainer.MIN_PASSIVATION_BATCH_COUNT) {
            sfsbContainer.setPassivationBatchCount(
                    cacheProps.getNumberOfVictimsToSelect());
        }

        if (_logger.isLoggable(TRACE_LEVEL)) {
            _logger.log(TRACE_LEVEL, "Created cache [for "
                    + ejbDescriptor.getName() + "] "
                    + cacheProps + "; loadFactor: "
                    + loadFactor
                    + "; backingStore: " + this.backingStore);
        }
    }

    private void scheduleTimerTasks(StatefulSessionContainer container) {
        String ejbName = ejbDescriptor.getEjbClassName();

        if (cacheProps.getCacheIdleTimeoutInSeconds() > 0) {
            long timeout = cacheProps.getCacheIdleTimeoutInSeconds() * 1000L;
            try {
                sfsbContainer.invokePeriodically(timeout, timeout,
                        new CachePassivatorTask(ejbName, sessionCache, _logger));
                if (_logger.isLoggable(TRACE_LEVEL)) {
                    _logger.log(TRACE_LEVEL,
                            "[SFSBBuilder]: Added CachePassivator for: "
                                    + ejbName + ". To run after "
                                    + timeout + " millis...");
                }

            } catch (Throwable th) {
                _logger.log(Level.WARNING,
                        "ejb.sfsb_helper_add_idle_passivatortask_failed", th);
            }
        }

        if (cacheProps.getRemovalTimeoutInSeconds() > 0) {
            long timeout = cacheProps.getRemovalTimeoutInSeconds() * 1000L;
            try {
                sfsbContainer.invokePeriodically(timeout, timeout,
                        new ExpiredSessionsRemovalTask(ejbName,
                                this.sfsbContainer, _logger));
                if (_logger.isLoggable(TRACE_LEVEL)) {
                    _logger.log(TRACE_LEVEL,
                            "[SFSBBuilder]: Added StorePassivator for: "
                                    + ejbName + ". To run after "
                                    + "after " + timeout + " millis...");
                }
            } catch (Throwable th) {
                _logger.log(Level.WARNING,
                        "ejb.sfsb_helper_add_remove_passivatortask_failed", th);
            }
        }

    }

}

class CachePassivatorTask
        implements Runnable {

    private LruSessionCache cache;
    private Logger logger;
    private String name;

    CachePassivatorTask(String name, LruSessionCache cache, Logger logger) {
        this.name = name;
        this.cache = cache;
        this.logger = logger;
    }

    public void run() {
        try {
            cache.trimTimedoutItems(Integer.MAX_VALUE);
        } catch (Exception ex) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING,
                        "ejb.sfsb_helper_remove_idle_beans_failed", ex);
            }
        }
    }
}

class ExpiredSessionsRemovalTask
        implements Runnable {
    private StatefulSessionContainer container;
    private Logger logger;
    private String name;

    ExpiredSessionsRemovalTask(String name,
                               StatefulSessionContainer container, Logger logger) {
        this.name = name;
        this.container = container;
        this.logger = logger;
    }

    public void run() {
        try {
            container.removeExpiredSessions();
        } catch (Exception ex) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING,
                        "ejb.sfsb_helper_remove_expired_beans_failed", ex);
            }
        }
    }
}












