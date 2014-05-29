/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2007-2012 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.enterprise.v3.services.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.glassfish.api.event.EventListener;
import org.glassfish.api.event.EventTypes;
import org.glassfish.api.event.RestrictTo;

import com.sun.enterprise.config.serverbeans.Config;
import com.sun.enterprise.config.serverbeans.HttpService;
import com.sun.enterprise.config.serverbeans.IiopListener;
import com.sun.enterprise.config.serverbeans.IiopService;
import com.sun.enterprise.config.serverbeans.JmsHost;
import com.sun.enterprise.config.serverbeans.JmsService;
import com.sun.enterprise.config.serverbeans.Server;
import com.sun.enterprise.config.serverbeans.VirtualServer;
import com.sun.enterprise.util.Result;
import com.sun.enterprise.util.StringUtils;
import com.sun.enterprise.v3.services.impl.monitor.GrizzlyMonitoring;
import com.sun.grizzly.config.dom.NetworkConfig;
import com.sun.grizzly.config.dom.NetworkListener;
import com.sun.grizzly.config.dom.NetworkListeners;
import com.sun.grizzly.config.dom.Protocol;
import com.sun.grizzly.tcp.Adapter;
import com.sun.grizzly.util.FutureImpl;
import com.sun.grizzly.util.http.mapper.Mapper;
import com.sun.logging.LogDomains;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.glassfish.api.FutureProvider;
import org.glassfish.api.Startup;
import org.glassfish.api.admin.ServerEnvironment;
import org.glassfish.api.container.EndpointRegistrationException;
import org.glassfish.api.container.RequestDispatcher;
import org.glassfish.api.deployment.ApplicationContainer;
import org.glassfish.api.event.Events;
import org.glassfish.flashlight.provider.ProbeProviderFactory;
import org.jvnet.hk2.annotations.Inject;
import org.jvnet.hk2.annotations.Scoped;
import org.jvnet.hk2.annotations.Service;
import org.jvnet.hk2.component.Habitat;
import org.jvnet.hk2.component.PostConstruct;
import org.jvnet.hk2.component.PreDestroy;
import org.jvnet.hk2.component.Singleton;
import org.jvnet.hk2.config.ConfigSupport;
import org.jvnet.hk2.config.ConfigBeanProxy;
import org.jvnet.hk2.config.ObservableBean;
import org.jvnet.hk2.config.Transactions;

/**
 * The Network Service is responsible for starting grizzly and register the
 * top level proxy. It is also providing a runtime service where other
 * services (like admin for instance) can register endpoints proxy to
 * particular context root.
 *
 * @author Jerome Dochez
 */
@Service
@Scoped(Singleton.class)
public class GrizzlyService implements Startup, RequestDispatcher, PostConstruct, PreDestroy, EventListener, FutureProvider<Result<Thread>> {

    @Inject(name=ServerEnvironment.DEFAULT_INSTANCE_NAME)
    Config config;

    @Inject
    Habitat habitat;

    @Inject(name = ServerEnvironment.DEFAULT_INSTANCE_NAME)
    private Server server;

    @Inject
    Transactions transactions;

    @Inject
    ProbeProviderFactory probeProviderFactory;

    @Inject
    Events events;
    
    final Logger logger = LogDomains.getLogger(GrizzlyService.class, LogDomains.CORE_LOGGER);

    private final Collection<NetworkProxy> proxies = new LinkedBlockingQueue<NetworkProxy>();
    private final String JMS_DEFAULT_LISTENER_IP="0.0.0.0";
    private final String JMS_DEFAULT_HOST="localhost";

    List<Future<Result<Thread>>> futures;

    Collection<String> hosts = new ArrayList<String>();

    private final GrizzlyMonitoring monitoring;

    private static final String NETWORK_CONFIG_PREFIX = "";

    private final ConcurrentLinkedQueue<MapperUpdateListener> mapperUpdateListeners =
            new ConcurrentLinkedQueue<MapperUpdateListener>();

    private DynamicConfigListener configListener;

    private FutureImpl<Boolean> serverReadyFuture = new FutureImpl<Boolean>();
    
    public GrizzlyService() {
        futures = new ArrayList<Future<Result<Thread>>>();
        monitoring = new GrizzlyMonitoring();
    }
    
    /**
     * Add the new proxy to our list of proxies.
     * @param proxy new proxy to be added
     */
    public void addNetworkProxy(NetworkProxy proxy) {
        proxies.add(proxy);               
    }
    
    
    /**
     * Remove the proxy from our list of proxies by listener.
     * @param listener removes the proxy associated with the specified listener
     * @return <tt>true</tt>, if proxy removed,
     *         <tt>false</tt> if no proxy was associated with the specified listener.
     */    
    public boolean removeNetworkProxy(NetworkListener listener) {
        return (removeNetworkProxy(lookupNetworkProxy(listener)));
    }

    
    /**
     * Remove the proxy from our list of proxies by id.
     * @return <tt>true</tt>, if proxy on specified port was removed,
     *         <tt>false</tt> if no proxy was associated with the port.
     */
    public boolean removeNetworkProxy(String id) {
        NetworkProxy proxy = null;
        for (NetworkProxy p : proxies) {
            if (p instanceof GrizzlyProxy) {
                GrizzlyProxy grizzlyProxy = (GrizzlyProxy) p;
                if (grizzlyProxy.networkListener != null &&
                        grizzlyProxy.networkListener.getName() != null &&
                        grizzlyProxy.networkListener.getName().equals(id)) {
                    proxy = p;
                    break;
                }
            }
        }

        return removeNetworkProxy(proxy);
    }

    /**
     * Remove the  proxy from our list of proxies.
     * @return <tt>true</tt>, if proxy on specified port was removed,
     *         <tt>false</tt> otherwise.
     */
    public boolean removeNetworkProxy(NetworkProxy proxy) {
        if (proxy != null) {
            proxy.stop();
            proxy.destroy();
            proxies.remove(proxy);
            return true;
        }

        return false;
    }

    /**
     * Lookup {@link GrizzlyProxy}, which corresponds to the {@link NetworkListener}.
     * 
     * @param listener {@link NetworkListener}.
     * @return {@link GrizzlyProxy}, or <tt>null</tt>, if correspondent {@link GrizzlyProxy} wasn't found.
     */
    public NetworkProxy lookupNetworkProxy(NetworkListener listener) {
        int listenerPort = -1;
        InetAddress address = null;
        try {
            listenerPort = Integer.parseInt(listener.getPort());
        } catch (NumberFormatException e) {
            if (logger.isLoggable(Level.FINE)) {
                logger.log(Level.FINE, e.toString());
            }
        }

        try {
            address = InetAddress.getByName(listener.getAddress());
        } catch (UnknownHostException uhe) {
            if (logger.isLoggable(Level.FINE)) {
                logger.log(Level.FINE, uhe.toString());
            }
        }

        if (listenerPort != -1) {
            for (NetworkProxy p : proxies) {
                if (p.getPort() == listenerPort && p.getAddress().equals(address)) {
                    return p;
                }
            }
        }

        final String listenerId = listener.getName();
        
        for (NetworkProxy p : proxies) {
            if (p instanceof GrizzlyProxy) {
                GrizzlyProxy grizzlyProxy = (GrizzlyProxy) p;
                if (grizzlyProxy.networkListener != null &&
                        grizzlyProxy.networkListener.getName() != null &&
                        grizzlyProxy.networkListener.getName().equals(listenerId)) {
                    return p;
                }
            }
        }

        return null;
        
    }

    /**
     * Is there any {@link MapperUpdateListener} registered?
     */
    public boolean hasMapperUpdateListener(){
        return (!mapperUpdateListeners.isEmpty());
    }

    /**
     * Adds {@link MapperUpdateListener} to listeners queue.
     * 
     * @param listener the listener to be added.
     * @return <tt>true</tt>, if listener was successfully added,
     * or <tt>false</tt> otherwise.
     */
    public boolean addMapperUpdateListener(MapperUpdateListener listener) {
        return mapperUpdateListeners.add(listener);
    }

    /**
     * Removes {@link MapperUpdateListener} to listeners queue.
     *
     * @param listener the listener to be removed.
     * @return <tt>true</tt>, if listener was successfully removed,
     * or <tt>false</tt> otherwise.
     */
    public boolean removeMapperUpdateListener(MapperUpdateListener listener) {
        return mapperUpdateListeners.remove(listener);
    }

    /**
     * Notify all {@link MapperUpdateListener}s about update happened.
     * 
     * @param networkListener {@link NetworkListener}, which {@link Mapper} got changed
     * @param mapper new {@link Mapper} value
     */
    public void notifyMapperUpdateListeners(NetworkListener networkListener,
            Mapper mapper) {
        final HttpService httpService = config.getHttpService();
        for(MapperUpdateListener listener : mapperUpdateListeners) {
            listener.update(httpService, networkListener, mapper);
        }
    }

    private final ConcurrentHashMap<String, ReentrantReadWriteLock> mapperLockMap =
            new ConcurrentHashMap<String, ReentrantReadWriteLock>();
    
    private final ReentrantReadWriteLock mapperLock = new ReentrantReadWriteLock();
    
    public ReentrantReadWriteLock obtainMapperLock(String name) {
//        ReentrantReadWriteLock lock = mapperLockMap.get(name);
//        
//        if (lock == null) {
//            final ReentrantReadWriteLock tmpLock = new ReentrantReadWriteLock();
//            lock = mapperLockMap.putIfAbsent(name, tmpLock);
//            if (lock == null) {
//                lock = tmpLock;
//            }
//        }
//        
//        return lock;
        return mapperLock;
    }

    void removeMapperLock(String name) {
//        mapperLockMap.remove(name);
    }
    
    /**
     * Gets the logger.
     *
     * @return the logger
     */   
    public Logger getLogger() {
        return logger;
    }


    /**
     * Gets the habitat.
     *
     * @return the habitat
     */   
    public Habitat getHabitat() {
        return habitat;
    }

    public GrizzlyMonitoring getMonitoring() {
        return monitoring;
    }

    final Future<Boolean> getServerReadyFuture() {
        return serverReadyFuture;
    }

    /**
     * Returns the life expectency of the service
     *
     * @return the life expectency.
     */
    @Override
    public Lifecycle getLifecycle() {
        return Lifecycle.SERVER;                
    }

    @Override
    public void event(@RestrictTo(EventTypes.SERVER_READY_NAME) Event event) {
        if (event.is(EventTypes.SERVER_READY)) {
            serverReadyFuture.setResult(Boolean.TRUE);
        }
    }

    /**
     * The component has been injected with any dependency and
     * will be placed into commission by the subsystem.
     */
    @Override
    public void postConstruct() {
        events.register(this);
        
        NetworkConfig networkConfig = config.getNetworkConfig();
        configListener = new DynamicConfigListener(config);

        ObservableBean bean = (ObservableBean) ConfigSupport.getImpl(networkConfig.getNetworkListeners());
        bean.addListener(configListener);
        bean = (ObservableBean) ConfigSupport.getImpl(config.getHttpService());
        bean.addListener(configListener);
        bean = (ObservableBean) ConfigSupport.getImpl(server);
        bean.addListener(configListener);

        //transactions.addListenerForType(SystemProperty.class, configListener);

        configListener.setGrizzlyService(this);
        configListener.setLogger(logger);

        try {
            futures = new ArrayList<Future<Result<Thread>>>();
            for (NetworkListener listener : networkConfig.getNetworkListeners().getNetworkListener()) {
                createNetworkProxy(listener);
            }

            /*
             * Ideally (and ultimately), all services that need lazy Init will add a network-listener element
             * in the domain.xml with protocol = "light-weight-listener". And a LWL instance would have been created
             * by the above loop. But for v3-FCS, IIOP and JMS listener will not
             * be able to reach that stage - hence we create a dummy network listener object here and use that
             * to create proxies etc. Whenever, IIOP and JMS listeners move to use network-listener elements,
             * then this code can be removed
             */

            final IiopService iiopService = config.getIiopService();
            if (iiopService != null) {
                List<IiopListener> iiopListenerList = iiopService.getIiopListener();
                for (IiopListener oneListener : iiopListenerList) {
                    if (Boolean.valueOf(oneListener.getEnabled()) && Boolean.valueOf(oneListener.getLazyInit())) {
                        NetworkListener dummy = new DummyNetworkListener();
                        dummy.setPort(oneListener.getPort());
                        dummy.setAddress(oneListener.getAddress());
                        dummy.setProtocol("light-weight-listener");
                        dummy.setTransport("tcp");
                        dummy.setName("iiop-service");
                        createNetworkProxy(dummy);
                    }
                }
            }

            /*
             * Do the same as above for JMS listeners also but only for MQ's EMBEDDED MODE
             */
            final JmsService jmsService = config.getJmsService();
            if (jmsService != null) {
                if ("EMBEDDED".equalsIgnoreCase(jmsService.getType())) {
                    List<JmsHost> jmsHosts = jmsService.getJmsHost();
                    for (JmsHost oneHost : jmsHosts) {
                        if (Boolean.valueOf(oneHost.getLazyInit())) {
                            String jmsHost = null;
                            if (oneHost.getHost() != null && JMS_DEFAULT_HOST.equals(oneHost.getHost()))
                                jmsHost = JMS_DEFAULT_LISTENER_IP;
                            else
                                jmsHost = oneHost.getHost();
                            NetworkListener dummy = new DummyNetworkListener();
                            dummy.setPort(oneHost.getPort());
                            dummy.setAddress(jmsHost);
                            dummy.setProtocol("light-weight-listener");
                            dummy.setTransport("tcp");
                            dummy.setName("mq-service");
                            createNetworkProxy(dummy);
                        }
                    }
                }
            }

            registerNetworkProxy();
        } catch(RuntimeException e) { // So far postConstruct can not throw any other exception type
            logger.log(Level.SEVERE, "Unable to start v3. Closing all ports",e);
            for(NetworkProxy proxy : proxies) {
                try {
                    proxy.stop();
                } catch(Exception proxyStopException) {
                    logger.log(Level.SEVERE, "Exception closing port: "
                            + proxy.getPort() , proxyStopException);
                }
            }

            throw e;
        }

        registerMonitoringStatsProviders();
    }

    @Override
    public List<Future<Result<Thread>>> getFutures() {
        return futures;
    }

    /*
     * Creates a new NetworkProxy for a particular HttpListner
     * @param listener NetworkListener
     * @param networkConfig HttpService
     */
    public synchronized Future<Result<Thread>> createNetworkProxy(NetworkListener listener) {

        if (!Boolean.valueOf(listener.getEnabled())) {
            logger.log(Level.INFO, "Network listener {0} on port {1}"
                    + " disabled per domain.xml",
                    new Object[]{listener.getName(), listener.getPort()});
            return null;
        }

        // create the proxy for the port.
        GrizzlyProxy proxy = new GrizzlyProxy(this, listener);
        if (!("light-weight-listener".equals(listener.getProtocol()))) {
            final NetworkConfig networkConfig = listener.getParent(NetworkListeners.class).getParent(NetworkConfig.class);
            // attach all virtual servers to this port
            for (VirtualServer vs : networkConfig.getParent(Config.class).getHttpService().getVirtualServer()) {
                List<String> vsListeners =
                    StringUtils.parseStringList(vs.getNetworkListeners(), " ,");
                if (vsListeners == null || vsListeners.isEmpty() ||
                        vsListeners.contains(listener.getName())) {
                    if (!hosts.contains(vs.getId())){
                        hosts.add(vs.getId());
                    }
                }
            }
            addChangeListener(listener);
            addChangeListener(listener.findThreadPool());
            addChangeListener(listener.findTransport());

            final Protocol protocol = listener.findHttpProtocol();
            if (protocol != null) {
                addChangeListener(protocol);
                addChangeListener(protocol.getHttp());
                addChangeListener(protocol.getHttp().getFileCache());
                addChangeListener(protocol.getSsl());
            }
        }

        final Future<Result<Thread>> future =  proxy.start();

        // add the new proxy to our list of proxies.
        proxies.add(proxy);
        futures.add(future);
        return future;
    }

    private void addChangeListener(ConfigBeanProxy bean) {
        if(bean != null) {
            ((ObservableBean) ConfigSupport.getImpl(bean)).addListener(configListener);
        }
    }


    /*
     * Registers all proxies
     */
    public void registerNetworkProxy() {
        for (org.glassfish.api.container.Adapter subAdapter :
            habitat.getAllByContract(org.glassfish.api.container.Adapter.class)) {
            //@TODO change EndportRegistrationException processing if required
            try {
                if (!subAdapter.isRegistered()) {
                    registerAdapter(subAdapter);
                    subAdapter.setRegistered(true);
                }
            } catch(EndpointRegistrationException e) {
                logger.log(Level.WARNING, 
                        "GrizzlyService endpoint registration problem", e);
            }
        }
    }
    
    
    /**
     * The component is about to be removed from commission
     */
    public void preDestroy() {
        for (NetworkProxy proxy : proxies) {
            proxy.stop();
        }
        unregisterMonitoringStatsProviders();
    }

    /*
     * Registers a new endpoint (proxy implementation) for a particular
     * context-root. All request coming with the context root will be dispatched
     * to the proxy instance passed in.
     * @param contextRoot for the proxy
     * @param endpointAdapter servicing requests.
     */
    @Override
    public void registerEndpoint(String contextRoot, Adapter endpointAdapter,
                                 ApplicationContainer container) throws EndpointRegistrationException {

        registerEndpoint(contextRoot, endpointAdapter, container, null);
    }

    /*
     * Registers a new endpoint (proxy implementation) for a particular
     * context-root. All request coming with the context root will be dispatched
     * to the proxy instance passed in.
     * @param contextRoot for the proxy
     * @param endpointAdapter servicing requests.
     * @param application container
     * @param virtualServers comma separated list of the virtual servers
     */
    @Override
    public void registerEndpoint(String contextRoot, Adapter endpointAdapter,
        ApplicationContainer container, String virtualServers) throws EndpointRegistrationException {
        List<String> virtualServerList;
        if (virtualServers == null) {
            virtualServerList = 
                config.getHttpService().getNonAdminVirtualServerList();
        } else{
            virtualServerList = 
                StringUtils.parseStringList(virtualServers, ",");
        }
        registerEndpoint(contextRoot, virtualServerList, endpointAdapter, container);
    }


    /*
     * Registers a new endpoint (proxy implementation) for a particular
     * context-root. All request coming with the context root will be dispatched
     * to the proxy instance passed in.
     * @param contextRoot for the proxy
     * @param endpointAdapter servicing requests.
     */
    @Override
    public void registerEndpoint(String contextRoot, Collection<String> vsServers,
            Adapter endpointAdapter,
            ApplicationContainer container) throws EndpointRegistrationException {
            
        Collection<AddressInfo> addressInfos = getAddressInfoFromVirtualServers(vsServers);
        for (AddressInfo info : addressInfos) {
            registerEndpoint(contextRoot,
                             info.address,
                             info.port,
                             vsServers,
                             endpointAdapter,
                             container);
        }
    }


    /**
     * Registers a new endpoint for the given context root at the given port
     * number.
     */
    @Override
    public void registerEndpoint(String contextRoot,
                                 InetAddress address,
                                 int port,
                                 Collection<String> vsServers,
                                 Adapter endpointAdapter,
                                 ApplicationContainer container) throws EndpointRegistrationException {
        for (NetworkProxy proxy : proxies) {
            if (proxy.getPort() == port && proxy.getAddress().equals(address)) {
                proxy.registerEndpoint(contextRoot, vsServers,
                                       endpointAdapter, container);
            }
        }
    }


    /**
     * Removes the context-root from our list of endpoints.
     */
    @Override
    public void unregisterEndpoint(String contextRoot) throws EndpointRegistrationException {
        unregisterEndpoint(contextRoot, null);
    }

    /**
     * Removes the context-root from our list of endpoints.
     */
    @Override
    public void unregisterEndpoint(String contextRoot, 
            ApplicationContainer app) throws EndpointRegistrationException {
        for (NetworkProxy proxy : proxies) {
            proxy.unregisterEndpoint(contextRoot, app);
        }
    }

    /**
     * Probe provider that implements each probe provider method as a 
     * no-op.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public static class NoopInvocationHandler implements InvocationHandler {

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) {
            // Deliberate no-op
            return null;
        }
    }

    protected void registerMonitoringStatsProviders() {
        monitoring.registerThreadPoolStatsProviderGlobal(NETWORK_CONFIG_PREFIX);
        monitoring.registerKeepAliveStatsProviderGlobal(NETWORK_CONFIG_PREFIX);
        monitoring.registerFileCacheStatsProviderGlobal(NETWORK_CONFIG_PREFIX);
        monitoring.registerConnectionQueueStatsProviderGlobal(NETWORK_CONFIG_PREFIX);
    }

    protected void unregisterMonitoringStatsProviders() {
        monitoring.unregisterThreadPoolStatsProviderGlobal(NETWORK_CONFIG_PREFIX);
        monitoring.unregisterKeepAliveStatsProviderGlobal(NETWORK_CONFIG_PREFIX);
        monitoring.unregisterFileCacheStatsProviderGlobal(NETWORK_CONFIG_PREFIX);
        monitoring.unregisterConnectionQueueStatsProviderGlobal(NETWORK_CONFIG_PREFIX);
    }

    private void registerAdapter(org.glassfish.api.container.Adapter a) throws EndpointRegistrationException {
        int port            = a.getListenPort();
        InetAddress address = a.getListenAddress();
        List<String> vs     = a.getVirtualServers();
        String cr           = a.getContextRoot();
        this.registerEndpoint(cr, address, port, vs, a, null);
    }

    // get the ports from the http listeners that are associated with 
    // the virtual servers
    private List<AddressInfo> getAddressInfoFromVirtualServers(Collection<String> virtualServers) {
        List<AddressInfo> addressInfos = new ArrayList<AddressInfo>();
        List<NetworkListener> networkListenerList = config.getNetworkConfig().getNetworkListeners().getNetworkListener();

        for (String vs : virtualServers) {
            VirtualServer virtualServer = 
                config.getHttpService().getVirtualServerByName(vs);
            if (virtualServer == null) {
                // non-existent virtual server
                logger.warning("Skip registering endpoint with non existent virtual server: " + vs);
                continue;
            }
            String vsNetworkListeners = virtualServer.getNetworkListeners();
            List<String> vsNetworkListenerList =
                StringUtils.parseStringList(vsNetworkListeners, ",");
            if (vsNetworkListenerList != null && !vsNetworkListenerList.isEmpty()) {
                for (String vsNetworkListener : vsNetworkListenerList) {
                    for (NetworkListener networkListener : networkListenerList) {
                        if (networkListener.getName().equals(vsNetworkListener) &&
                            Boolean.valueOf(networkListener.getEnabled())) {
                            addressInfos.add(new AddressInfo(networkListener.getAddress(),
                                                             networkListener.getPort()));
                            break;
                        }
                    }
                }
            }
        } 
        return addressInfos;
    }


    // ---------------------------------------------------------- Nested Classes


    private static final class AddressInfo {

        private InetAddress address;
        private final int port;

        private AddressInfo(String address, String port) {
            this.port = Integer.parseInt(port);
            try {
                this.address = InetAddress.getByName(address);
            } catch (UnknownHostException ignore) {
            }
        }

    } // END AddressInfo
}
