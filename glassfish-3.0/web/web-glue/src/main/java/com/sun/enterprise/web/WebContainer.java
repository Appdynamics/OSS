/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
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
 */

package com.sun.enterprise.web;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.MessageFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.servlet.*;
import javax.servlet.jsp.JspFactory;
import javax.servlet.jsp.tagext.JspTag;
import com.sun.appserv.server.util.Version;
import com.sun.common.util.logging.LoggingConfigImpl;
import com.sun.enterprise.config.serverbeans.Config;
import com.sun.enterprise.config.serverbeans.ConfigBeansUtilities;
import com.sun.enterprise.config.serverbeans.DasConfig;
import com.sun.enterprise.config.serverbeans.Domain;
import com.sun.enterprise.config.serverbeans.HttpService;
import com.sun.enterprise.config.serverbeans.SecurityService;
import com.sun.enterprise.config.serverbeans.Server;
import com.sun.enterprise.config.serverbeans.SessionProperties;
import com.sun.enterprise.container.common.spi.util.ComponentEnvManager;
import com.sun.enterprise.container.common.spi.util.InjectionManager;
import com.sun.enterprise.container.common.spi.util.JavaEEObjectStreamFactory;
import com.sun.enterprise.deployment.WebBundleDescriptor;
import com.sun.enterprise.deployment.WebComponentDescriptor;
import com.sun.enterprise.deployment.archivist.WebArchivist;
import com.sun.enterprise.deployment.runtime.web.ManagerProperties;
import com.sun.enterprise.deployment.runtime.web.SessionManager;
import com.sun.enterprise.deployment.runtime.web.StoreProperties;
import com.sun.enterprise.deployment.runtime.web.SunWebApp;
import com.sun.enterprise.deployment.runtime.web.WebProperty;
import com.sun.enterprise.deployment.util.WebValidatorWithoutCL;
import com.sun.enterprise.security.integration.RealmInitializer;
import com.sun.enterprise.util.Result;
import com.sun.enterprise.util.StringUtils;
import com.sun.enterprise.util.io.FileUtils;
import com.sun.enterprise.v3.services.impl.ContainerMapper;
import com.sun.enterprise.v3.services.impl.GrizzlyService;
import com.sun.enterprise.web.connector.coyote.PECoyoteConnector;
import com.sun.enterprise.web.logger.FileLoggerHandler;
import com.sun.enterprise.web.logger.IASLogger;
import com.sun.enterprise.web.pluggable.WebContainerFeatureFactory;
import com.sun.enterprise.web.reconfig.WebConfigListener;
import com.sun.grizzly.config.ContextRootInfo;
import com.sun.grizzly.config.dom.NetworkConfig;
import com.sun.grizzly.config.dom.NetworkListener;
import com.sun.grizzly.util.buf.MessageBytes;
import org.jvnet.hk2.config.types.Property;
import com.sun.grizzly.util.http.mapper.Mapper;
import com.sun.grizzly.util.http.mapper.MappingData;
import com.sun.hk2.component.ConstructorWomb;
import com.sun.logging.LogDomains;
import java.net.BindException;
import org.apache.catalina.Connector;
import org.apache.catalina.Container;
import org.apache.catalina.Context;
import org.apache.catalina.Deployer;
import org.apache.catalina.Engine;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.Loader;
import org.apache.catalina.Realm;
import org.apache.catalina.connector.Request;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardEngine;
import org.apache.catalina.util.RequestUtil;
import org.apache.catalina.util.ServerInfo;
import org.apache.jasper.runtime.JspFactoryImpl;
import org.apache.jasper.xmlparser.ParserUtils;
import org.glassfish.api.admin.ServerEnvironment;
import org.glassfish.api.event.EventListener;
import org.glassfish.api.event.EventListener.Event;
import org.glassfish.api.event.EventTypes;
import org.glassfish.api.event.Events;
import org.glassfish.api.invocation.InvocationManager;
import org.glassfish.api.web.TldProvider;
import org.glassfish.internal.api.ClassLoaderHierarchy;
import org.glassfish.internal.api.ServerContext;
import org.glassfish.internal.data.ApplicationRegistry;
import org.glassfish.internal.deployment.Deployment;
import org.glassfish.web.admin.monitor.HttpServiceStatsProviderBootstrap;
import org.glassfish.web.admin.monitor.JspProbeProvider;
import org.glassfish.web.admin.monitor.RequestProbeProvider;
import org.glassfish.web.admin.monitor.ServletProbeProvider;
import org.glassfish.web.admin.monitor.SessionProbeProvider;
import org.glassfish.web.admin.monitor.WebModuleProbeProvider;
import org.glassfish.web.admin.monitor.WebStatsProviderBootstrap;
import org.glassfish.web.valve.GlassFishValve;
import org.jvnet.hk2.annotations.Inject;
import org.jvnet.hk2.annotations.Scoped;
import org.jvnet.hk2.annotations.Service;
import org.jvnet.hk2.component.Habitat;
import org.jvnet.hk2.component.PostConstruct;
import org.jvnet.hk2.component.PreDestroy;
import org.jvnet.hk2.component.Singleton;
import org.jvnet.hk2.config.ConfigSupport;
import org.jvnet.hk2.config.ObservableBean;
import org.xml.sax.EntityResolver;

/**
 * Web container service
 *
 * @author jluehe
 * @author amyroh
 */
@SuppressWarnings({"StringContatenationInLoop"})
@Service(name="com.sun.enterprise.web.WebContainer")
@Scoped(Singleton.class)
public class WebContainer implements org.glassfish.api.container.Container, PostConstruct, PreDestroy, EventListener {

    // -------------------------------------------------- Constants

    public static final String DISPATCHER_MAX_DEPTH="dispatcher-max-depth";

    static final int DEFAULT_REAP_INTERVAL = 60;   // 1 minute

    public static final String JWS_APPCLIENT_EAR_NAME = "__JWSappclients";
    public static final String JWS_APPCLIENT_WAR_NAME = "sys";
    private static final String JWS_APPCLIENT_MODULE_NAME = JWS_APPCLIENT_EAR_NAME + ":" + JWS_APPCLIENT_WAR_NAME + ".war";

    private static final String DOL_DEPLOYMENT =
            "com.sun.enterprise.web.deployment.backend";

    private static final String MONITORING_NODE_SEPARATOR = "/";

    /**
     * The logger to use for logging ALL web container related messages.
     */
    protected static final Logger _logger = LogDomains.getLogger(
            WebContainer.class, LogDomains.WEB_LOGGER);

    protected static final ResourceBundle rb = _logger.getResourceBundle();

    /**
     * The current <code>WebContainer</code> instance used (single).
     */
    protected static WebContainer webContainer;

    /**
     * Are we using Tomcat deployment backend or DOL?
     */
    protected static boolean useDOLforDeployment = true;

    // ----------------------------------------------------- Instance Variables

    @Inject
    Domain domain;
    
    @Inject
    private Habitat habitat;

    @Inject
    ServerContext _serverContext;

    @Inject
    public ApplicationRegistry appRegistry;

    @Inject
    ComponentEnvManager componentEnvManager;

    @Inject(optional=true)
    DasConfig dasConfig;

    @Inject
    Events events;

    @Inject
    ClassLoaderHierarchy clh;

    @Inject
    GrizzlyService grizzlyService;

    @Inject
    LoggingConfigImpl logConfig;

    //@Inject
    //MonitoringService monitoringService;

    @Inject
    JavaEEObjectStreamFactory javaEEObjectStreamFactory;

    @Inject
    FileLoggerHandler logHandler;

    HashMap<String, Integer> portMap = new HashMap<String, Integer>();
    HashMap<String, WebConnector> connectorMap = new HashMap<String, WebConnector>();

    EmbeddedWebContainer _embedded;
    Engine engine;
    String instanceName;

    private String logLevel = "INFO";
    
    
    private WebConnector jkConnector;

    /**
     * Allow disabling accessLog mechanism
     */
    protected boolean globalAccessLoggingEnabled = true;

    /**
     * AccessLog buffer size for storing logs.
     */
    protected String globalAccessLogBufferSize = null;

    /**
     * AccessLog interval before the valve flush its buffer to the disk.
     */
    protected String globalAccessLogWriteInterval = null;

    /**
     * The default-redirect port
     */
    protected int defaultRedirectPort = -1;

    /**
     * <tt>false</tt> when the Grizzly File Cache is enabled. When disabled
     * the Servlet Container temporary Naming cache is used when loading the
     * resources.
     */
    protected boolean catalinaCachingAllowed = true;

    @Inject
    protected ServerEnvironment instance = null;

    // TODO
    //protected WebModulesManager webModulesManager = null;
    //protected AppsManager appsManager = null;

    /**
     * The schema2beans object that represents the root node of server.xml.
     */
    private Server _serverBean = null;

    /**
     * Controls the verbosity of the web container subsystem's debug messages.
     *
     * This value is non-zero only when the iAS level is one of FINE, FINER
     * or FINEST.
     */
    protected int _debug = 0;

    /**
     * Top-level directory for files generated (compiled JSPs) by
     *  standalone web modules.
     */
    private String _modulesWorkRoot = null;

    // START S1AS 6178005
    /**
     * Top-level directory where ejb stubs of standalone web modules are stored
     */
    private String modulesStubRoot = null;
    // END S1AS 6178005

    /**
     * Absolute path for location where all the deployed
     * standalone modules are stored for this Server Instance.
     */
    protected File _modulesRoot = null;

    /**
     * Top-level directory for files generated by application web modules.
     */
    private String _appsWorkRoot = null;

    // START S1AS 6178005
    /**
     * Top-level directory where ejb stubs for applications are stored.
     */
    private String appsStubRoot = null;
    // END S1AS 6178005

    /**
     * Indicates whether dynamic reloading is enabled (as specified by
     * the dynamic-reload-enabled attribute of <applications> in server.xml)
     */
    private boolean _reloadingEnabled = false;

    /**
     * The number of seconds between checks for modified classes (if
     * dynamic reloading is enabled).
     *
     * This value is specified by the reload-poll-interval attribute of
     * <applications> in server.xml.
     */
    private int _pollInterval = 2;

    /**
     * Has this component been started yet?
     */
    protected boolean _started = false;

    /**
     * The global (at the http-service level) ssoEnabled property.
     */
    protected boolean globalSSOEnabled = true;

    protected WebContainerFeatureFactory webContainerFeatureFactory;

    /**
     * The value of the instance-level session property named "enableCookies"
     */
    boolean instanceEnableCookies = true;

    private ServerConfigLookup serverConfigLookup;

    protected JspProbeProvider jspProbeProvider = null;
    protected RequestProbeProvider requestProbeProvider = null;
    protected ServletProbeProvider servletProbeProvider = null;
    protected SessionProbeProvider sessionProbeProvider = null;
    protected WebModuleProbeProvider webModuleProbeProvider = null;

    protected WebConfigListener configListener = null;

    // Indicates whether we are being shut down
    private boolean isShutdown = false;

    private final Object mapperUpdateSync = new Object();
    
    private SecurityService securityService = null;

    private WebStatsProviderBootstrap webStatsProviderBootstrap = null;

    private InjectionManager injectionMgr;

    private InvocationManager invocationMgr;

    private Collection<TldProvider> tldProviders;

    /**
     * Static initialization
     */
    static {
        if (System.getProperty(DOL_DEPLOYMENT) != null){
            useDOLforDeployment = Boolean.valueOf(System.getProperty(DOL_DEPLOYMENT));
        }
    }

    public void postConstruct() {

        createProbeProviders();

        injectionMgr = habitat.getByContract(InjectionManager.class);
        invocationMgr = habitat.getByContract(InvocationManager.class);
        tldProviders = habitat.getAllByContract(TldProvider.class);

        //createMonitoringConfig();
        createStatsProviders();

        setJspFactory();

        _modulesWorkRoot =
            instance.getWebModuleCompileJspPath().getAbsolutePath();
        _appsWorkRoot =
            instance.getApplicationCompileJspPath().getAbsolutePath();
        _modulesRoot = instance.getApplicationRepositoryPath();

        // START S1AS 6178005
        modulesStubRoot = instance.getModuleStubPath();
        appsStubRoot = instance.getApplicationStubPath().getAbsolutePath();
        // END S1AS 6178005

        // TODO: ParserUtils should become a @Service and it should initialize itself.
        // TODO: there should be only one EntityResolver for both DigesterFactory
        // and ParserUtils
        File root = _serverContext.getInstallRoot();
        File libRoot = new File(root, "lib");
        File schemas = new File(libRoot, "schemas");
        File dtds = new File(libRoot, "dtds");

        try {
            ParserUtils.setSchemaResourcePrefix(schemas.toURI().toURL().toString());
            ParserUtils.setDtdResourcePrefix(dtds.toURI().toURL().toString());
            ParserUtils.setEntityResolver(habitat.getComponent(EntityResolver.class, "web"));
        } catch(MalformedURLException e) {
            _logger.log(Level.SEVERE, "webContainer.exceptionSetSchemasDtdsLocation", e);
        }

        instanceName = _serverContext.getInstanceName();

        webContainerFeatureFactory = habitat.getComponent(
                PEWebContainerFeatureFactoryImpl.class);

        Config cfg = habitat.getComponent(Config.class);
        serverConfigLookup = new ServerConfigLookup(cfg, clh);
        configureDynamicReloadingSettings();
        setDebugLevel();

        String maxDepth = null;
        if(cfg.getWebContainer()!=null)
            maxDepth = cfg.getWebContainer().getPropertyValue(DISPATCHER_MAX_DEPTH);
        if (maxDepth != null) {
            int depth = -1;
            try {
                depth = Integer.parseInt(maxDepth);
            } catch (NumberFormatException e) {}

            if (depth > 0) {
                Request.setMaxDispatchDepth(depth);
                if (_logger.isLoggable(Level.FINE)) {
                    _logger.fine("Maximum depth for nested request "
                            + "dispatches set to "
                            + maxDepth);
                }
            }
        }

        String logServiceFile = null;
        Map<String, String> logProps = null;
        try {
            logProps = logConfig.getLoggingProperties();
            if (logProps != null) {
                logServiceFile = logProps.get("com.sun.enterprise.server.logging.GFFileHandler.file");
                logLevel = logProps.get("org.apache.catalina.level");
            }
        } catch (IOException ioe) {
            _logger.log(Level.SEVERE, "webContainer.unableDetermineServerLogLocation", ioe);
        }

        _embedded = habitat.getByType(EmbeddedWebContainer.class);
        _embedded.setWebContainer(this);
        _embedded.setLogServiceFile(logServiceFile);
        _embedded.setLogLevel(logLevel);
        _embedded.setLogHandler(logHandler);

        _embedded.setCatalinaHome(instance.getDomainRoot().getAbsolutePath());
        _embedded.setCatalinaBase(instance.getDomainRoot().getAbsolutePath());
        _embedded.setUseNaming(false);
        if (_debug > 1)
            _embedded.setDebug(_debug);
        _embedded.setLogger(new IASLogger(_logger));

        // TODO (Sahoo): Stop using ModuleImpl
        engine = _embedded.createEngine();
        engine.setParentClassLoader(EmbeddedWebContainer.class.getClassLoader());
        _embedded.addEngine(engine);
        ((StandardEngine) engine).setDomain(_serverContext.getDefaultDomainName());
        engine.setName(_serverContext.getDefaultDomainName());
        
        /*
         * Set the server info. 
         * By default, the server info is taken from Version#getVersion.
         * However, customers may override it via the product.name system
         * property.
         * Some customers prefer not to disclose the server info
         * for security reasons, in which case they would set the value of the 
         * product.name system property to the empty string. In this case,
         * the server name will not be publicly disclosed via the "Server"
         * HTTP response header (which will be suppressed) or any container
         * generated error pages. However, it will still appear in the
         * server logs (see IT 6900).
         */
        String serverInfo = System.getProperty("product.name");
        if (serverInfo == null) {
            ServerInfo.setServerInfo(Version.getVersion());
            ServerInfo.setPublicServerInfo(Version.getVersion());
        } else if (serverInfo.isEmpty()) {
            ServerInfo.setServerInfo(Version.getVersion());
            ServerInfo.setPublicServerInfo(serverInfo);
        } else {
            ServerInfo.setServerInfo(serverInfo);
            ServerInfo.setPublicServerInfo(serverInfo);
        }

        initInstanceSessionProperties();

        ConstructorWomb<WebConfigListener> womb =
                new ConstructorWomb<WebConfigListener>(
                WebConfigListener.class,
                habitat,
                null);
        configListener = womb.get(null);

        ObservableBean bean = (ObservableBean) ConfigSupport.getImpl(
                configListener.httpService);
        bean.addListener(configListener);
        
        bean = (ObservableBean) ConfigSupport.getImpl(
                cfg.getNetworkConfig().getNetworkListeners());
        bean.addListener(configListener);
         
        // embedded mode does not have manager-propertie in domain.xml
        if (configListener.managerProperties!=null) {
            ObservableBean managerBean = (ObservableBean) ConfigSupport.getImpl(
                configListener.managerProperties);
            managerBean.addListener(configListener);      
        }
        
        configListener.setContainer(this);
        configListener.setLogger(_logger);

        events.register(this);

        grizzlyService.addMapperUpdateListener(configListener);

        List<Config> configs = domain.getConfigs().getConfig();
        for (Config aConfig : configs) {

            HttpService httpService = aConfig.getHttpService();
            NetworkConfig networkConfig = aConfig.getNetworkConfig();
            securityService = aConfig.getSecurityService();

            // Configure HTTP listeners
            List<NetworkListener> listeners = networkConfig.getNetworkListeners().getNetworkListener();
            for (NetworkListener listener : listeners) {
                if (ConfigBeansUtilities.toBoolean(listener.getJkEnabled())) {
                    createJKConnector(listener, httpService);
                } else {
                    createHttpListener(listener, httpService);
                }
            }
            createJKConnector(null, httpService);

            setDefaultRedirectPort(defaultRedirectPort);

            // Configure virtual servers
            createHosts(httpService, securityService);
        }

        loadSystemDefaultWebModules();
                       
        //_lifecycle.fireLifecycleEvent(START_EVENT, null);
        _started = true;

        /*
         * Start the embedded container.
         * Make sure to set the thread's context classloader to the
         * classloader of this class (see IT 8866 for details)
         */
        ClassLoader current = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(
            getClass().getClassLoader());
        try {
            /*
             * Trigger a call to sun.awt.AppContext.getAppContext().
             * This will pin the classloader of this class in memory
             * and fix a memory leak affecting instances of WebappClassLoader
             * that was caused by a JRE implementation change in 1.6.0_15
             * onwards. See IT 11110
             */
            ImageIO.getCacheDirectory();
            _embedded.start();
        } catch (LifecycleException le) {
            _logger.log(Level.SEVERE,
                "webcontainer.exceptionDuringEmbeddedStart", le);
            return;
        } finally {
            // Restore original context classloader
            Thread.currentThread().setContextClassLoader(current);
        }
    }

    public void event(Event event) {
        if (event.is(Deployment.ALL_APPLICATIONS_PROCESSED)) {
            // configure default web modules for virtual servers after all
            // applications are processed
            loadDefaultWebModulesAfterAllAppsProcessed();
        } else if (event.is(EventTypes.PREPARE_SHUTDOWN)) {
            isShutdown = true;
        }
    }

    /**
     * Notifies any interested listeners that all ServletContextListeners
     * of the web module represented by the given WebBundleDescriptor
     * have been invoked at their contextInitialized method
     */
    void afterServletContextInitializedEvent(WebBundleDescriptor wbd) {
        events.send(new Event<WebBundleDescriptor>(
            WebBundleDescriptor.AFTER_SERVLET_CONTEXT_INITIALIZED_EVENT, wbd),
            false);
    }

    public void preDestroy() {
        try {
            _embedded.stop();
        } catch(LifecycleException le) {
            _logger.log(Level.SEVERE,
                "webcontainer.exceptionDuringEmbeddedStop", le);
            return;
        }
    }

    JavaEEObjectStreamFactory getJavaEEObjectStreamFactory() {
        return javaEEObjectStreamFactory;
    }

    public boolean isShutdown() {
        return isShutdown;
    }

    Collection<TldProvider> getTldProviders() {
        return tldProviders;
    }
     
    /**
     * Gets the probe provider for servlet related events.
     */
    public ServletProbeProvider getServletProbeProvider() {
        return servletProbeProvider;
    }


    /**
     * Gets the probe provider for jsp related events.
     */
    public JspProbeProvider getJspProbeProvider() {
        return jspProbeProvider;
    }


    /**
     * Gets the probe provider for session related events.
     */
    public SessionProbeProvider getSessionProbeProvider() {
        return sessionProbeProvider;
    }


    /**
     * Gets the probe provider for request/response related events.
     */
    public RequestProbeProvider getRequestProbeProvider() {
        return requestProbeProvider;
    }

    /**
     * Gets the probe provider for web module related events.
     */
    public WebModuleProbeProvider getWebModuleProbeProvider() {
        return webModuleProbeProvider;
    }

    public String getName() {
        return "Web";
    }

    public Class<? extends WebDeployer> getDeployer() {
        return WebDeployer.class;
    }

    InvocationManager getInvocationManager() {
        return invocationMgr;
    }

    public WebConnector getJkConnector() {
        return jkConnector;
    }

    /**
     * Instantiates and injects the given Servlet class for the given
     * WebModule
     */
    <T extends Servlet> T createServletInstance(WebModule module,
                Class<T> clazz) throws Exception {
        WebComponentInvocation inv = new WebComponentInvocation(module);
        try {
            invocationMgr.preInvoke(inv);
            return (T) injectionMgr.createManagedObject(clazz);
        } finally {
            invocationMgr.postInvoke(inv);
        }
    }

    /**
     * Instantiates and injects the given Filter class for the given
     * WebModule
     */
    <T extends Filter> T createFilterInstance(WebModule module,
                Class<T> clazz) throws Exception {
        WebComponentInvocation inv = new WebComponentInvocation(module);
        try {
            invocationMgr.preInvoke(inv);
            return (T) injectionMgr.createManagedObject(clazz);
        } finally {
            invocationMgr.postInvoke(inv);
        }
    }

    /**
     * Instantiates and injects the given EventListener class for the
     * given WebModule
     */
    <T extends java.util.EventListener> T createListenerInstance(
                WebModule module, Class<T> clazz) throws Exception {
        WebComponentInvocation inv = new WebComponentInvocation(module);
        try {
            invocationMgr.preInvoke(inv);
            return (T) injectionMgr.createManagedObject(clazz);
        } finally {
            invocationMgr.postInvoke(inv);
        }
    }

    /**
     * Instantiates and injects the given tag handler class for the given
     * WebModule
     */
    public <T extends JspTag> T createTagHandlerInstance(WebModule module,
                Class<T> clazz) throws Exception {
        WebComponentInvocation inv = new WebComponentInvocation(module);
        try {
            invocationMgr.preInvoke(inv);
            return (T) injectionMgr.createManagedObject(clazz);
        } finally {
            invocationMgr.postInvoke(inv);
        }
    }

    /**
     * Use an network-listener subelements and creates a corresponding
     * Tomcat Connector for each.
     *
     * @param httpService The http-service element
     * @param listener the configuration element.
     */
    protected WebConnector createHttpListener(NetworkListener listener,
                                              HttpService httpService) {
        return createHttpListener(listener, httpService, null);
    }


    protected WebConnector createHttpListener(NetworkListener listener,
                                              HttpService httpService,
                                              Mapper mapper) {

        if (!Boolean.valueOf(listener.getEnabled())) {
            return null;
        }

        int port = 8080;
        WebConnector connector;

        checkHostnameUniqueness(listener.getName(), httpService);

        try {
            port = Integer.parseInt(listener.getPort());
        } catch (NumberFormatException nfe) {
            String msg = rb.getString("pewebcontainer.http_listener.invalid_port");
            msg = MessageFormat.format(msg, listener.getPort(),
                                       listener.getName());
            throw new IllegalArgumentException(msg);
        }

        if (mapper == null) {
            for (Mapper m : habitat.getAllByContract(Mapper.class)) {
                if (m.getPort() == port) {
                    mapper = m;
                    break;
                }
            }
        }

        String defaultVS =  listener.findHttpProtocol().getHttp().getDefaultVirtualServer();
        if (!defaultVS.equals(org.glassfish.api.web.Constants.ADMIN_VS)){
            // Before we start a WebConnector, let's makes sure there is
            // not another Container already listening on that port
            MessageBytes host = MessageBytes.newInstance();
            char[] c = defaultVS.toCharArray();
            host.setChars(c, 0, c.length);

            MessageBytes mb = MessageBytes.newInstance();
            mb.setChars(new char[]{'/'}, 0, 1);

            MappingData md = new MappingData();
            try{
                mapper.map(host,mb,md);
            } catch (Exception e){
                if (_logger.isLoggable(Level.FINE)){
                    _logger.log(Level.FINE,"",e);
                }
            }

            if (md.context != null && md.context instanceof ContextRootInfo){
                ContextRootInfo r = (ContextRootInfo)md.context;
                if (!(r.getAdapter() instanceof ContainerMapper)){
                    new BindException("Port " + port +  " is already used by Container: "
                            + r.getAdapter() +
                            " and will not get started.").printStackTrace();
                    return null;
                }
            }
        }


        /*
         * Create Connector. Connector is SSL-enabled if
         * 'security-enabled' attribute in <http-listener>
         * element is set to TRUE.
         */
        boolean isSecure = Boolean.valueOf(listener.findHttpProtocol().getSecurityEnabled());
        if (isSecure && defaultRedirectPort == -1) {
            defaultRedirectPort = port;
        }
        String address = listener.getAddress();
        if ("any".equals(address) || "ANY".equals(address)
                || "INADDR_ANY".equals(address)) {
            address = null;
            /*
             * Setting 'address' to NULL will cause Tomcat to pass a
             * NULL InetAddress argument to the java.net.ServerSocket
             * constructor, meaning that the server socket will accept
             * connections on any/all local addresses.
             */
        }

        connector = (WebConnector)_embedded.createConnector(
            address, port, isSecure);

        connector.setMapper(mapper);
       

        _logger.info("Created HTTP listener " + listener.getName() +
                     " on port " + listener.getPort());
        
        connector.setName(listener.getName());
        connector.setInstanceName(instanceName);
        connector.configure(listener, isSecure, httpService);

        if ( _logger.isLoggable(Level.FINE)){
            _logger.log(Level.FINE, "create.listenerport",
                new Object[] {port, connector});
        }

        _embedded.addConnector(connector);

        portMap.put(listener.getName(), Integer.valueOf(listener.getPort()));
        connectorMap.put(listener.getName(), connector);

        // If we already know the redirect port, then set it now
        // This situation will occurs when dynamic reconfiguration occurs
        if ( defaultRedirectPort != -1 ){
            connector.setRedirectPort(defaultRedirectPort);
        }

        ObservableBean httpListenerBean = (ObservableBean) ConfigSupport.getImpl(
            listener);
        httpListenerBean.addListener(configListener);

        return connector;
    }

    /**
     * Starts the AJP connector that will listen to call from Apache using
     * mod_jk, mod_jk2 or mod_ajp.
     */
    protected WebConnector createJKConnector(NetworkListener listener, 
            HttpService httpService) {

        int port = 8009;
        boolean isSecure = false;
        String address = null;

        if (listener == null) {
            String portString =
                    System.getProperty("com.sun.enterprise.web.connector.enableJK");
            if (portString == null) {
                // do not create JK Connector if property is not set
                return null;
            } else {
                try {
                    port = Integer.parseInt(portString);
                } catch (NumberFormatException ex) {
                    // use default port 8009
                    port = 8009;
                }
            }   
        } else {
            port = Integer.parseInt(listener.getPort());                
            isSecure = Boolean.valueOf(
                            listener.findHttpProtocol().getSecurityEnabled());
            address = listener.getAddress();
        }
            
        if (isSecure && defaultRedirectPort == -1) {
            defaultRedirectPort = port;
        }
        
        if ("any".equals(address) || "ANY".equals(address)
                || "INADDR_ANY".equals(address)) {
            address = null;
            /*
             * Setting 'address' to NULL will cause Tomcat to pass a
             * NULL InetAddress argument to the java.net.ServerSocket
             * constructor, meaning that the server socket will accept
             * connections on any/all local addresses.
             */
        }

        jkConnector = (WebConnector) _embedded.createConnector(address,
                                                                port, "ajp");
        jkConnector.configureJKProperties();

        String defaultHost = "server";
        String jkConnectorName = "jk-connector";
        if (listener !=null) {
            defaultHost = listener.findHttpProtocol().getHttp().getDefaultVirtualServer();
            jkConnectorName = listener.getName();     
        }
        jkConnector.setDefaultHost(defaultHost);
        jkConnector.setName(jkConnectorName);
        jkConnector.setDomain(_serverContext.getDefaultDomainName());
        jkConnector.setInstanceName(instanceName);
        if (listener!=null) {
            jkConnector.configure(listener, isSecure, httpService);
            portMap.put(listener.getName(), Integer.valueOf(listener.getPort()));
            connectorMap.put(listener.getName(), jkConnector);
        }
        
        _logger.log(Level.INFO, "Apache mod_jk/jk2 attached to virtual-server "
                                + defaultHost + " listening on port: "
                                + port);
        for (Mapper m : habitat.getAllByContract(Mapper.class)) {
            if (m.getPort() == port){
                jkConnector.setMapper(m);
                break;
            }
        }

        _embedded.addConnector(jkConnector);
       
        
        return jkConnector;

    }

    /**
     * Assigns the given redirect port to each Connector whose corresponding
     * http-listener element in domain.xml does not specify its own
     * redirect-port attribute.
     *
     * The given defaultRedirectPort corresponds to the port number of the
     * first security-enabled http-listener in domain.xml.
     *
     * This method does nothing if none of the http-listener elements is
     * security-enabled, in which case Tomcat's default redirect port (443)
     * will be used.
     *
     * @param defaultRedirectPort The redirect port to be assigned to any
     * Connector object that doesn't specify its own
     */
    private void setDefaultRedirectPort(int defaultRedirectPort) {
        if (defaultRedirectPort != -1) {
            Connector[] connectors = _embedded.getConnectors();
            for (Connector connector : connectors) {
                if (connector.getRedirectPort() == -1) {
                    connector.setRedirectPort(defaultRedirectPort);
                }
            }
        }
    }

    /**
     * Configure http-service properties.
     * @deprecated most of these properties are handled elsewhere.  validate and remove outdated properties checks
     */
    public void configureHttpServiceProperties(HttpService httpService,
                                               PECoyoteConnector connector){
        // Configure Connector with <http-service> properties
        List<Property> httpServiceProps = httpService.getProperty();

        // Set default ProxyHandler impl, may be overriden by
        // proxyHandler property
        connector.setProxyHandler(new ProxyHandlerImpl());

        globalSSOEnabled = ConfigBeansUtilities.toBoolean(httpService.getSsoEnabled());
        globalAccessLoggingEnabled = ConfigBeansUtilities.toBoolean(httpService.getAccessLoggingEnabled());
        globalAccessLogWriteInterval = httpService.getAccessLog().getWriteIntervalSeconds();
        globalAccessLogBufferSize = httpService.getAccessLog().getBufferSizeBytes();
        if (httpServiceProps != null) {
            for (Property httpServiceProp : httpServiceProps) {
                String propName = httpServiceProp.getName();
                String propValue = httpServiceProp.getValue();

                if (connector.configureHttpListenerProperty(propName, propValue)) {
                    continue;
                }

                if ("connectionTimeout".equals(propName)) {
                    connector.setConnectionTimeout(Integer.parseInt(propValue));
                } else if ("tcpNoDelay".equals(propName)) {
                    connector.setTcpNoDelay(ConfigBeansUtilities.toBoolean(propValue));
                } else if ("traceEnabled".equals(propName)) {
                    connector.setAllowTrace(ConfigBeansUtilities.toBoolean(propValue));
                } else if ("authPassthroughEnabled".equals(propName)) {
                    connector.setAuthPassthroughEnabled(
                                    ConfigBeansUtilities.toBoolean(propValue));
                } else if ("ssl-session-timeout".equals(propName)) {
                    connector.setSSLSessionTimeout(propValue);
                } else if ("ssl3-session-timeout".equals(propName)) {
                    connector.setSSL3SessionTimeout(propValue);
                } else if ("ssl-cache-entries".equals(propName)) {
                    connector.setSSLSessionCacheSize(propValue);
                } else if ("proxyHandler".equals(propName)) {
                    connector.setProxyHandler(propValue);
                } else {
                    _logger.log(Level.WARNING,
                        "pewebcontainer.invalid_http_service_property",
                        httpServiceProp.getName());
                }
            }
        }
    }

    /*
     * Ensures that the host names of all virtual servers associated with the
     * HTTP listener with the given listener id are unique.
     *
     * @param listenerId The id of the HTTP listener whose associated virtual
     * servers are checked for uniqueness of host names
     * @param httpService The http-service element whose virtual servers are
     * checked
     */
    private void checkHostnameUniqueness(String listenerId,
                                         HttpService httpService) {

        List<com.sun.enterprise.config.serverbeans.VirtualServer> listenerVses = null;

        // Determine all the virtual servers associated with the given listener
        for (com.sun.enterprise.config.serverbeans.VirtualServer vse : httpService.getVirtualServer()) {
            List<String> vsListeners = StringUtils.parseStringList(vse.getNetworkListeners(), ",");
            for (int j=0; vsListeners!=null && j<vsListeners.size(); j++) {
                if (listenerId.equals(vsListeners.get(j))) {
                    if (listenerVses == null) {
                        listenerVses = new ArrayList<com.sun.enterprise.config.serverbeans.VirtualServer>();
                    }
                    listenerVses.add(vse);
                    break;
                }
            }
        }
        if (listenerVses == null) {
            return;
        }

        for (int i=0; i<listenerVses.size(); i++) {
            com.sun.enterprise.config.serverbeans.VirtualServer vs
                = listenerVses.get(i);
            List hosts = StringUtils.parseStringList(vs.getHosts(), ",");
            for (int j=0; hosts!=null && j<hosts.size(); j++) {
                String host = (String) hosts.get(j);
                for (int k=0; k<listenerVses.size(); k++) {
                    if (k <= i) {
                        continue;
                    }
                    com.sun.enterprise.config.serverbeans.VirtualServer otherVs
                        = listenerVses.get(k);
                    List otherHosts = StringUtils.parseStringList(otherVs.getHosts(), ",");
                    for (int l=0; otherHosts!=null && l<otherHosts.size(); l++) {
                        if (host.equals(otherHosts.get(l))) {
                            _logger.log(Level.SEVERE,
                                        "pewebcontainer.duplicate_host_name",
                                        new Object[] { host, vs.getId(),
                                                       otherVs.getId(),
                                                       listenerId });
                        }
                    }
		}
            }
        }
    }


    /**
     * Enumerates the virtual-server subelements of the given http-service
     * element, and creates a corresponding Host for each.
     *
     * @param httpService The http-service element
     * @param securityService The security-service element
     */
    protected void createHosts(HttpService httpService, SecurityService securityService) {

        List<com.sun.enterprise.config.serverbeans.VirtualServer> virtualServers = httpService.getVirtualServer();
        for (com.sun.enterprise.config.serverbeans.VirtualServer vs : virtualServers) {
            createHost(vs, httpService, securityService);
            _logger.info("Created virtual server " + vs.getId());
        }
    }


    /**
     * Creates a Host from a virtual-server config bean.
     *
     * @param vsBean The virtual-server configuration bean
     * @param httpService The http-service element.
     * @param securityService The security-service element
     */
    public VirtualServer createHost(
        com.sun.enterprise.config.serverbeans.VirtualServer vsBean,
        HttpService httpService,
        SecurityService securityService) {

        MimeMap mm = null;
        String vs_id = vsBean.getId();

        String docroot = vsBean.getPropertyValue("docroot");
        if (docroot == null) {
            docroot = vsBean.getDocroot();
        }
        
        validateDocroot(docroot,
                        vs_id,
                        vsBean.getDefaultWebModule());

        VirtualServer vs = createHost(vs_id, vsBean, docroot, mm
        );

        // cache control
        Property cacheProp = vsBean.getProperty("setCacheControl");
        if ( cacheProp != null ){
            vs.configureCacheControl(cacheProp.getValue());
        }

        PEAccessLogValve accessLogValve = vs.getAccessLogValve();
        boolean startAccessLog = accessLogValve.configure(
            vs_id, vsBean, httpService, domain,
            habitat, webContainerFeatureFactory,
            globalAccessLogBufferSize, globalAccessLogWriteInterval);
        if (startAccessLog
                && vs.isAccessLoggingEnabled(globalAccessLoggingEnabled)) {
            vs.addValve((GlassFishValve) accessLogValve);
        }

        if (_logger.isLoggable(Level.FINEST)) {
            _logger.log(Level.FINEST, "Created virtual server " + vs_id);
        }

        /*
         * We must configure the Host with its associated port numbers and
         * alias names before adding it as an engine child and thereby
         * starting it, because a MapperListener, which is associated with
         * an HTTP listener and receives notifications about Host
         * registrations, relies on these Host properties in order to determine
         * whether a new Host needs to be added to the HTTP listener's Mapper.
         */
        configureHost(vs, securityService);

        // Add Host to Engine
        engine.addChild(vs);

        ObservableBean virtualServerBean = (ObservableBean) ConfigSupport.getImpl(vsBean);
        virtualServerBean.addListener(configListener);

        return vs;
    }


    /**
     * Validate the docroot properties of a virtual-server.
     */
    protected void validateDocroot(String docroot, String vs_id,
                                   String defaultWebModule){
        if (docroot == null) {
            return;
        }

        boolean isValid = new File(docroot).exists();
        if (!isValid) {
            String msg = rb.getString(
                "pewebcontainer.virtual_server.invalid_docroot");
            msg = MessageFormat.format(msg, vs_id, docroot);
            throw new IllegalArgumentException(msg);
        }
    }


    /**
     * Configures the given virtual server.
     *
     * @param vs The virtual server to be configured
     * @param securityService The security-service element
     */
    protected void configureHost(VirtualServer vs, SecurityService securityService) {

        com.sun.enterprise.config.serverbeans.VirtualServer vsBean = vs.getBean();

        vs.configureAliases();

        // Set the ports with which this virtual server is associated
        List<String> listeners = StringUtils.parseStringList(
            vsBean.getNetworkListeners(), ",");
        if (listeners == null) {
            return;
        }
        
        HashSet<NetworkListener> httpListeners = new HashSet<NetworkListener>();
        for (String listener : listeners) {
            boolean found = false;
            for (NetworkListener httpListener : habitat.getAllByContract(NetworkListener.class)) {
                if (httpListener.getName().equals(listener)) {
                    httpListeners.add(httpListener);
                    found = true;
                    break;
                }
            }
            if (!found) {
                String msg = rb.getString(
                    "webcontainer.listenerReferencedByHostNotExist");
                msg = MessageFormat.format(msg, listener, vs.getName());
                _logger.log(Level.SEVERE, msg);
            }
        }

        configureHostPortNumbers(vs, httpListeners);
        vs.configureCatalinaProperties();
        vs.configureAuthRealm(securityService);
    }


    /**
     * Configures the given virtual server with the port numbers of its
     * associated http listeners.
     *
     * @param vs The virtual server to configure
     * @param listeners The http listeners with which the given virtual
     * server is associated
     */
    protected void configureHostPortNumbers(VirtualServer vs,
        HashSet<NetworkListener> listeners){

        boolean addJkListenerPort = jkConnector != null &&
            !vs.getName().equalsIgnoreCase(
                org.glassfish.api.web.Constants.ADMIN_VS);

        ArrayList<Integer> portsList = new ArrayList<Integer>();
        for (NetworkListener listener : listeners){
            if (Boolean.valueOf(listener.getEnabled())){
                Integer port = portMap.get(listener.getName());
                if (port != null) {
                    portsList.add(port);
                }
            } else {
                if (vs.getName().equalsIgnoreCase(
                        org.glassfish.api.web.Constants.ADMIN_VS)) {
                    String msg = rb.getString(
                        "pewebcontainer.httpListener.mustNotDisable");
                    msg = MessageFormat.format(msg, listener.getName(),
                                               vs.getName());
                    throw new IllegalArgumentException(msg);
                }
            }
        }

        int numPorts = portsList.size();
        if (addJkListenerPort) {
            numPorts++;
        }
        if (numPorts > 0) {
            int[] ports = new int[numPorts];
            int i=0;
            for (i=0; i<portsList.size(); i++) {
                ports[i] = portsList.get(i);
                 if (_logger.isLoggable(Level.FINE)) {
                     _logger.fine("Virtual Server " + vs.getID() +
                                  " set port " + ports[i]);
                 }
            }
            if (addJkListenerPort) {
                ports[i] = jkConnector.getPort();
                if (_logger.isLoggable(Level.FINE)) {
                    _logger.fine("Virtual Server " + vs.getID() +
                                 " set jk port " + ports[i]);
                }
            }
            vs.setPorts(ports);
        }
    }


    // ------------------------------------------------------ Public Methods

    /**
     * Create a virtual server/host.
     */
    public VirtualServer createHost(String vsID,
        com.sun.enterprise.config.serverbeans.VirtualServer vsBean,
        String docroot, MimeMap mimeMap) {

        // Initialize the docroot
        VirtualServer vs = (VirtualServer) _embedded.createHost(vsID,
            vsBean, docroot, vsBean.getLogFile(), mimeMap);
        vs.configureState();
        vs.configureRemoteAddressFilterValve();
        vs.configureRemoteHostFilterValve();
        vs.configureSingleSignOn(globalSSOEnabled, webContainerFeatureFactory);
        vs.configureRedirect();
        vs.configureErrorPage();
        vs.configureErrorReportValve();

        return vs;
    }


    /**
     * Gracefully terminate the active use of the public methods of this
     * component.  This method should be the last one called on a given
     * instance of this component.
     *
     * @exception IllegalStateException if this component has not been started
     * @exception LifecycleException if this component detects a fatal error
     *  that needs to be reported
     */
    public void stop() throws LifecycleException {
        // Validate and update our current component state
        if (!_started) {
            String msg = rb.getString("webcontainer.notStarted");
            throw new LifecycleException(msg);
        }

        _started = false;

        // stop the embedded container
        try{
            _embedded.stop();
        } catch (LifecycleException ex){
            if (!ex.getMessage().contains("has not been started")){
                throw ex;
            }
        }
    }


    // ------------------------------------------------------ Private Methods
    
        
    /**
     * Configures a default web module for each virtual server based on the
     * virtual server's docroot if a virtual server does not specify 
     * any default-web-module, and none of its web modules are loaded at "/"
     * 
     * Needed in postConstruct before Deployment.ALL_APPLICATIONS_PROCESSED
     * for "jsp from docroot before web container start" scenario
     */
        
    public void loadSystemDefaultWebModules() {      
        
        WebModuleConfig wmInfo = null;
        String defaultPath = null;
        
        Container[] vsArray = getEngine().findChildren();
        for (Container aVsArray : vsArray) {
            if (aVsArray instanceof VirtualServer) {
                VirtualServer vs = (VirtualServer) aVsArray;
                /*
                * Let AdminConsoleAdapter handle any requests for
                * the root context of the '__asadmin' virtual-server, see
                * https://glassfish.dev.java.net/issues/show_bug.cgi?id=5664
                */
                if (org.glassfish.api.web.Constants.ADMIN_VS.equals(
                        vs.getName())) {
                    continue;
                }
                            
                // Create default web module off of virtual
                // server's docroot if necessary                   
                wmInfo = vs.createSystemDefaultWebModuleIfNecessary(
                            habitat.getComponent(
                            WebArchivist.class));
                if (wmInfo != null) {
                    defaultPath = wmInfo.getContextPath();
                    loadStandaloneWebModule(vs, wmInfo);
                }
                _logger.info("Virtual server " + vs.getName() +
                                " loaded system default web module"); 
            }
        }
        
    }
    
        
    /**
     * Configures a default web module for each virtual server 
     * if default-web-module is defined.
     */
    public void loadDefaultWebModulesAfterAllAppsProcessed() {
        
        String defaultPath = null;

        Container[] vsArray = getEngine().findChildren();
        for (Container aVsArray : vsArray) {
            if (aVsArray instanceof VirtualServer) {
                VirtualServer vs = (VirtualServer) aVsArray;
                /*
                * Let AdminConsoleAdapter handle any requests for
                * the root context of the '__asadmin' virtual-server, see
                * https://glassfish.dev.java.net/issues/show_bug.cgi?id=5664
                */
                if (org.glassfish.api.web.Constants.ADMIN_VS.equals(
                        vs.getName())) {
                    continue;
                }
                WebModuleConfig wmInfo = vs.getDefaultWebModule(domain,
                    habitat.getComponent(
                        WebArchivist.class),
                        appRegistry);
                if (wmInfo != null) {
                    defaultPath = wmInfo.getContextPath();
                    // Virtual server declares default-web-module
                    try {
                        updateDefaultWebModule(vs, vs.getPorts(), wmInfo);
                    } catch (LifecycleException le) {
                        String msg = rb.getString(
                            "webcontainer.defaultWebModuleError");
                        msg = MessageFormat.format(msg, defaultPath,
                            vs.getName());
                        _logger.log(Level.SEVERE, msg, le);
                    }
                    _logger.info("Virtual server " + vs.getName() + 
                        " loaded default web module " + defaultPath);
                } else {
                    // No need to create default web module off of virtual
                    // server's docroot since system web modules are already
                    // created in WebContainer.postConstruct
                }
            }
        }
    }

             
    /**
     * Load a default-web-module on the specified virtual server.
     */
    public void loadDefaultWebModule(
            com.sun.enterprise.config.serverbeans.VirtualServer vsBean) {   
        
        VirtualServer virtualServer = (VirtualServer)
            getEngine().findChild(vsBean.getId());

        if (virtualServer!=null) {
            loadDefaultWebModule(virtualServer);
        }
    }
    
    
     /**
     * Load a default-web-module on the specified virtual server.
     */   
    public void loadDefaultWebModule(VirtualServer vs) {      
        
        String defaultPath = null;
        WebModuleConfig wmInfo = vs.getDefaultWebModule(domain,
                habitat.getComponent(
                WebArchivist.class),
                appRegistry);
        if (wmInfo != null) {
            defaultPath = wmInfo.getContextPath();
            // Virtual server declares default-web-module
            try {
                updateDefaultWebModule(vs, vs.getPorts(), wmInfo);
            } catch (LifecycleException le) {
                String msg = rb.getString("webcontainer.defaultWebModuleError");
                msg = MessageFormat.format(msg, defaultPath, vs.getName());
                _logger.log(Level.SEVERE, msg, le);
            }

        } else {
            // Create default web module off of virtual
            // server's docroot if necessary                   
            wmInfo = vs.createSystemDefaultWebModuleIfNecessary(
                            habitat.getComponent(
                            WebArchivist.class));
            if (wmInfo != null) {
                defaultPath = wmInfo.getContextPath();
                loadStandaloneWebModule(vs, wmInfo);
            }
        }
        
        _logger.info("Virtual server " + vs.getName() +
                        " loaded default web module " + defaultPath);
    }


    /**
     * Load the specified web module as a standalone module on the specified
     * virtual server.
     */
    protected void loadStandaloneWebModule(VirtualServer vs,
                                           WebModuleConfig wmInfo) {
        try {
            loadWebModule(vs, wmInfo, "null", null);
        } catch (Throwable t) {
            String msg = rb.getString("webContainer.loadWebModuleError");
            msg = MessageFormat.format(msg, wmInfo.getName());
            _logger.log(Level.SEVERE, msg, t);
        }
    }


    /**
     * Whether or not a component (either an application or a module) should be
     * enabled is defined by the "enable" attribute on both the
     * application/module element and the application-ref element.
     *
     * @param moduleName The name of the component (application or module)
     * @return boolean
     */
    protected boolean isEnabled(String moduleName) {
        // TODO dochez : optimize
        /*
        Domain domain = habitat.getComponent(Domain.class);
        applications = domain.getApplications().getLifecycleModuleOrJ2EeApplicationOrEjbModuleOrWebModuleOrConnectorModuleOrAppclientModuleOrMbeanOrExtensionModule();
        com.sun.enterprise.config.serverbeans.WebModule webModule = null;
        for (Object module : applications) {
            if (module instanceof WebModule) {
                if (moduleName.equals(((com.sun.enterprise.config.serverbeans.WebModule) module).getName())) {
                    webModule = (com.sun.enterprise.config.serverbeans.WebModule) module;
                }
            }
        }    em
        ServerContext env = habitat.getComponent(ServerContext.class);
        List<Server> servers = domain.getServers().getServer();
        Server thisServer = null;
        for (Server server : servers) {
            if (env.getInstanceName().equals(server.getName())) {
                thisServer = server;
            }
        }
        List<ApplicationRef> appRefs = thisServer.getApplicationRef();
        ApplicationRef appRef = null;
        for (ApplicationRef ar : appRefs) {
            if (ar.getRef().equals(moduleName)) {
                appRef = ar;
            }
        }

        return ((webModule != null && Boolean.valueOf(webModule.getEnabled())) &&
                (appRef != null && Boolean.valueOf(appRef.getEnabled())));
         */
        return true;
    }

    /**
     * Creates and configures a web module for each virtual server
     * that the web module is hosted under.
     *
     * If no virtual servers have been specified, then the web module will
     * not be loaded.
     */
    public List<Result<WebModule>> loadWebModule(
            WebModuleConfig wmInfo, String j2eeApplication,
            Properties deploymentProperties) {
        List<Result<WebModule>> results = new ArrayList<Result<WebModule>>();
        String vsIDs = wmInfo.getVirtualServers();
        List vsList = StringUtils.parseStringList(vsIDs, " ,");
        if (vsList == null || vsList.isEmpty()) {
            _logger.log(Level.INFO,
                "webcontainer.webModuleNotLoadedNoVirtualServers",
                wmInfo.getName());
            return results;
        }

        if (_logger.isLoggable(Level.FINE)) {
            _logger.log(Level.FINE, "About to load web module " +
                wmInfo.getName() + " to virtual servers " + vsIDs);
        }

        Container[] vsArray = getEngine().findChildren();
        for (Container aVsArray : vsArray) {
            if (aVsArray instanceof VirtualServer) {
                VirtualServer vs = (VirtualServer) aVsArray;
                if (vsList.contains(vs.getID()) || verifyAlias(vsList, vs)) {
                    WebModule ctx = null;
                    try {
                        ctx = loadWebModule(vs, wmInfo, j2eeApplication,
                            deploymentProperties);
                        results.add(new Result(ctx));
                    } catch (Throwable t) {
                        if (ctx != null) {
                            ctx.setAvailable(false);
                        }
                        results.add(new Result(t));
                    }
                }
            }
        }

        return results;
    }


    /**
     * Deploy on aliases as well as host.
     */
    private boolean verifyAlias(List vsList,VirtualServer vs){
        for(int i=0; i < vs.getAliases().length; i++){
            if (vsList.contains(vs.getAliases()[i]) ){
                return true;
            }
        }
        return false;
    }


    /**
     * Creates and configures a web module and adds it to the specified
     * virtual server.
     */
    private WebModule loadWebModule(
                VirtualServer vs,
                WebModuleConfig wmInfo,
                String j2eeApplication,
                Properties deploymentProperties)
            throws Exception {

        String wmName = wmInfo.getName();
        String wmContextPath = wmInfo.getContextPath();

        if (wmContextPath.indexOf('%') != -1) {
            try {
                RequestUtil.URLDecode(wmContextPath, "UTF-8");
            } catch (Exception e) {
                String msg = rb.getString(
                    "webcontainer.invalidEncodedContextRoot");
                msg = MessageFormat.format(msg, wmName, wmContextPath);
                throw new Exception(msg);            
            }
        }

        if (wmContextPath.length() == 0 &&
                vs.getDefaultWebModuleID() != null) {
            String msg = rb.getString("webcontainer.defaultWebModuleConflict");
            msg = MessageFormat.format(msg,
                    new Object[] { wmName, vs.getID() });
            throw new Exception(msg);
        }

        if (wmName.indexOf(Constants.NAME_SEPARATOR) != -1) {
            wmInfo.setWorkDirBase(_appsWorkRoot);
            // START S1AS 6178005
            wmInfo.setStubBaseDir(appsStubRoot);
            // END S1AS 6178005
        } else {
            wmInfo.setWorkDirBase(_modulesWorkRoot);
            // START S1AS 6178005
            wmInfo.setStubBaseDir(modulesStubRoot);
            // END S1AS 6178005
        }

        String displayContextPath = null;
        if (wmContextPath.length() == 0)
            displayContextPath = "/";
        else
            displayContextPath = wmContextPath;

        Map<String, AdHocServletInfo> adHocPaths = null;
        Map<String, AdHocServletInfo> adHocSubtrees = null;
        WebModule ctx = (WebModule)vs.findChild(wmContextPath);
        if (ctx != null) {
            if (ctx instanceof AdHocWebModule) {
                /*
                 * Found ad-hoc web module which has been created by web
                 * container in order to store mappings for ad-hoc paths
                 * and subtrees.
                 * All these mappings must be propagated to the context
                 * that is being deployed.
                 */
                if (ctx.hasAdHocPaths()) {
                    adHocPaths = ctx.getAdHocPaths();
                }
                if (ctx.hasAdHocSubtrees()) {
                    adHocSubtrees = ctx.getAdHocSubtrees();
                }
                vs.removeChild(ctx);
            } else if (Constants.DEFAULT_WEB_MODULE_NAME
                    .equals(ctx.getModuleName())) {
                /*
                 * Dummy context that was created just off of a docroot,
                 * (see
                 * VirtualServer.createSystemDefaultWebModuleIfNecessary()).
                 * Unload it so it can be replaced with the web module to be
                 * loaded
                 */
                unloadWebModule(wmContextPath,
                                ctx.getJ2EEApplication(),
                                vs.getName(),
                                true,
                                null);
            } else if (!ctx.getAvailable()){
                /*
                 * Context has been marked unavailable by a previous
                 * call to disableWebModule. Mark the context as available and
                 * return
                 */
                ctx.setAvailable(true);
                return ctx;
            } else {
                String msg = rb.getString("webcontainer.duplicateContextRoot");
                throw new Exception(MessageFormat.format(msg, vs.getID(),
                    ctx.getModuleName(), displayContextPath, wmName));
            }
        }

        if (_logger.isLoggable(Level.FINEST)) {
            Object[] params = { wmName, vs.getID(), displayContextPath };
            _logger.log(Level.FINEST, "webcontainer.loadModule", params);
        }

        File docBase = null;
        if (JWS_APPCLIENT_MODULE_NAME.equals(wmName)) {
            docBase = new File(System.getProperty("com.sun.aas.installRoot"));
        } else {
            docBase = wmInfo.getLocation();
        }

        ctx = (WebModule) _embedded.createContext(
                wmName,
                wmContextPath,
                docBase,
                vs.getDefaultContextXmlLocation(),
                vs.getDefaultWebXmlLocation(),
                useDOLforDeployment,
                wmInfo);

        // for now disable JNDI
        ctx.setUseNaming(false);

        // Set JSR 77 object name and attributes
        Engine engine = (Engine) vs.getParent();
        if (engine != null) {
            ctx.setEngineName(engine.getName());
            ctx.setJvmRoute(engine.getJvmRoute());
        }
        String j2eeServer = _serverContext.getInstanceName();
        String domain = _serverContext.getDefaultDomainName();
        String server = domain + ":j2eeType=J2EEServer,name=" + j2eeServer;
        // String[] javaVMs = J2EEModuleUtil.getjavaVMs();
        ctx.setDomain(domain);

        ctx.setJ2EEServer(j2eeServer);
        ctx.setJ2EEApplication(j2eeApplication);
        ctx.setCachingAllowed(false);
        ctx.setCacheControls(vs.getCacheControls());
        ctx.setBean(wmInfo.getBean());

        if (adHocPaths != null) {
            ctx.addAdHocPaths(adHocPaths);
        }
        if (adHocSubtrees != null) {
            ctx.addAdHocSubtrees(adHocSubtrees);
        }

        // Object containing web.xml information
        WebBundleDescriptor wbd = wmInfo.getDescriptor();

        // Set the context root
        if (wbd != null) {
            ctx.setContextRoot(wbd.getContextRoot());
        } else {
            // Should never happen.
            _logger.log(Level.WARNING, "webContainer.unableSetContextRoot", wmInfo);
        }

        //
        // Ensure that the generated directory for JSPs in the document root
        // (i.e. those that are serviced by a system default-web-module)
        // is different for each virtual server.
        String wmInfoWorkDir = wmInfo.getWorkDir();
        if (wmInfoWorkDir != null) {
            StringBuilder workDir = new StringBuilder(wmInfo.getWorkDir());
            if (wmName.equals(Constants.DEFAULT_WEB_MODULE_NAME)) {
                workDir.append("-");
                workDir.append(FileUtils.makeFriendlyFilename(vs.getID()));
            }
            ctx.setWorkDir(workDir.toString());
        }

        ClassLoader parentLoader = wmInfo.getParentLoader();
        if (parentLoader == null) {
            // Use the shared classloader as the parent for all
            // standalone web-modules
            parentLoader = _serverContext.getSharedClassLoader();
        }
        ctx.setParentClassLoader(parentLoader);


        if (wbd != null) {
            // Determine if an alternate DD is set for this web-module in
            // the application
            ctx.configureAlternateDD(wbd);
            ctx.configureWebServices(wbd);
        }

        // Object containing sun-web.xml information
        SunWebApp iasBean = null;

        // The default context is the only case when wbd == null
        if (wbd != null) {
            iasBean = wbd.getSunDescriptor();
        }

        // set the sun-web config bean
        ctx.setIasWebAppConfigBean(iasBean);

        // Configure SingleThreadedServletPools, work/tmp directory etc
        ctx.configureMiscSettings(iasBean, vs, displayContextPath);

        // Configure alternate docroots if dummy web module
        if (ctx.getID().startsWith(Constants.DEFAULT_WEB_MODULE_NAME)) {
            ctx.setAlternateDocBases(vs.getProperties());
        }

        // Configure the class loader delegation model, classpath etc
        Loader loader = ctx.configureLoader(iasBean);

        // Set the class loader on the DOL object
        if (wbd != null && wbd.hasWebServices()) {
            wbd.addExtraAttribute("WEBLOADER", loader);
        }

        // Configure the session manager and other related settings
        ctx.configureSessionSettings(wbd, wmInfo);

        // set i18n info from locale-charset-info tag in sun-web.xml
        ctx.setI18nInfo();

        if (wbd != null) {
            String resourceType = wmInfo.getObjectType();
            boolean isSystem = resourceType != null &&
                    resourceType.startsWith("system-");
            // security will generate policy for system default web module
            if (!wmName.startsWith(Constants.DEFAULT_WEB_MODULE_NAME)) {
                // TODO : v3 : dochez Need to remove dependency on security
                Realm realm = habitat.getByContract(Realm.class);
                if ("null".equals(j2eeApplication)) {
                    /*
                     * Standalone webapps inherit the realm referenced by
                     * the virtual server on which they are being deployed,
                     * unless they specify their own
                     */
                    if (realm != null && realm instanceof RealmInitializer) {
                        ((RealmInitializer)realm).initializeRealm(
                            wbd, isSystem, vs.getAuthRealmName());
                        ctx.setRealm(realm);
                    }
                } else {
                    if (realm != null && realm instanceof RealmInitializer) {
                        ((RealmInitializer)realm).initializeRealm(
                            wbd, isSystem, null);
                        ctx.setRealm(realm);
                    }
                }
            }

            // post processing DOL object for standalone web module
            if (wbd.getApplication() != null &&
                    wbd.getApplication().isVirtual()) {
                wbd.visit(new WebValidatorWithoutCL());
            }
        }

        // Add virtual server mime mappings, if present
        addMimeMappings(ctx, vs.getMimeMap());

        String moduleName = Constants.DEFAULT_WEB_MODULE_NAME;
        String monitoringNodeName = moduleName;
        if (wbd != null && wbd.getApplication() != null) {
            // Not a dummy web module
            com.sun.enterprise.deployment.Application app =
                wbd.getApplication();
            ctx.setStandalone(app.isVirtual());
            // S1AS BEGIN WORKAROUND FOR 6174360
            if (app.isVirtual()) {
                // Standalone web module
                moduleName = app.getRegistrationName();
                monitoringNodeName = wbd.getModuleID();
            } else {
                // Nested (inside EAR) web module
                moduleName = wbd.getModuleDescriptor().getArchiveUri();
                StringBuilder sb = new StringBuilder();
                sb.append(app.getRegistrationName()).
                    append(MONITORING_NODE_SEPARATOR).append(moduleName);
                monitoringNodeName = sb.toString().replaceAll("\\.", "\\\\.").
                    replaceAll("_war", "\\\\.war");
            }
            // S1AS END WORKAROUND FOR 6174360
        }
        ctx.setModuleName(moduleName);
        ctx.setMonitoringNodeName(monitoringNodeName);

        List<String> servletNames = new ArrayList<String>();
        if (wbd != null) {
            for (WebComponentDescriptor webCompDesc : wbd.getWebComponentDescriptors()) {
                if (webCompDesc.isServlet()) {
                    servletNames.add(webCompDesc.getCanonicalName());
                }
            }
        }

        webStatsProviderBootstrap.registerApplicationStatsProviders(monitoringNodeName,
                vs.getName(), servletNames);

        vs.addChild(ctx);

        ctx.loadSessions(deploymentProperties);

        return ctx;
    }


    /*
     * Updates the given virtual server with the given default path.
     *
     * The given default path corresponds to the context path of one of the
     * web contexts deployed on the virtual server that has been designated
     * as the virtual server's new default-web-module.
     *
     * @param virtualServer The virtual server to update
     * @param ports The port numbers of the HTTP listeners with which the
     * given virtual server is associated
     * @param defaultContextPath The context path of the web module that has
     * been designated as the virtual server's new default web module, or null
     * if the virtual server no longer has any default-web-module
     */
    protected void updateDefaultWebModule(VirtualServer virtualServer,
            int[] ports,
            WebModuleConfig wmInfo)
            throws LifecycleException {

        String defaultContextPath = null;
        if (wmInfo!=null) {
            defaultContextPath = wmInfo.getContextPath();
        }
        if (defaultContextPath != null
                && !defaultContextPath.startsWith("/")) {
            defaultContextPath = "/" + defaultContextPath;
            wmInfo.getDescriptor().setContextRoot(defaultContextPath);
        }

        Connector[] connectors = _embedded.findConnectors();
        for (Connector connector : connectors) {
            PECoyoteConnector conn = (PECoyoteConnector) connector;
            int port = conn.getPort();
            for (int port1 : ports) {
                if (port == port1) {
                    Mapper mapper = conn.getMapper();
                    try {
                        mapper.setDefaultContextPath(virtualServer.getName(),
                            defaultContextPath);
                        virtualServer.setDefaultContextPath(defaultContextPath);
                    } catch (Exception e) {
                        throw new LifecycleException(e);
                    }            
                }
            }
        }
    }


    /**
     * Utility Method to access the ServerContext
     */
    public ServerContext getServerContext() {
        return _serverContext;
    }


    ServerConfigLookup getServerConfigLookup() {
        return serverConfigLookup;
    }


    File getLibPath() {
        return instance.getLibPath();
    }


    /**
     * The application id for this web module
     * HERCULES:add
     */
    public String getApplicationId(WebModule wm) {
        return wm.getID();
    }


    /**
     * Return the Absolute path for location where all the deployed
     * standalone modules are stored for this Server Instance.
     */
    public File getModulesRoot() {
        return _modulesRoot;
    }


    /**
     * Get the persistence frequency for this web module
     * (this is the value from sun-web.xml if defined
     * @param smBean  the session manager config bean
     * HERCULES:add
     */
    private String getPersistenceFrequency(SessionManager smBean) {
        String persistenceFrequency = null;
        ManagerProperties mgrBean = smBean.getManagerProperties();
        if (mgrBean != null && mgrBean.sizeWebProperty() > 0) {
            WebProperty[] props = mgrBean.getWebProperty();
            for (WebProperty prop : props) {
                String name = prop.getAttributeValue(WebProperty.NAME);
                String value = prop.getAttributeValue(WebProperty.VALUE);
                if (name == null || value == null) {
                    throw new IllegalArgumentException(
                        rb.getString("webcontainer.nullWebProperty"));
                }
                if (name.equalsIgnoreCase("persistenceFrequency")) {
                    persistenceFrequency = value;
                    break;
                }
            }
        }
        return persistenceFrequency;
    }

    /**
     * Get the persistence scope for this web module
     * (this is the value from sun-web.xml if defined
     * @param smBean the session manager config bean
     * HERCULES:add
     */
    private String getPersistenceScope(SessionManager smBean) {
        String persistenceScope = null;
        StoreProperties storeBean = smBean.getStoreProperties();
        if (storeBean != null && storeBean.sizeWebProperty() > 0) {
            WebProperty[] props = storeBean.getWebProperty();
            for (WebProperty prop : props) {
                String name = prop.getAttributeValue(WebProperty.NAME);
                String value = prop.getAttributeValue(WebProperty.VALUE);
                if (name == null || value == null) {
                    throw new IllegalArgumentException(
                        rb.getString("webcontainer.nullWebProperty"));
                }
                if (name.equalsIgnoreCase("persistenceScope")) {
                    persistenceScope = value;
                    break;
                }
            }
        }
        return persistenceScope;
    }


    /**
     * Undeploy a web application.
     *
     * @param contextRoot the context's name to undeploy
     * @param appName the J2EE appname used at deployment time
     * @param virtualServers List of current virtual-server object.
     */
    public void unloadWebModule(String contextRoot,
            String appName,
            String virtualServers,
            Properties props) {
        unloadWebModule(contextRoot, appName, virtualServers, false, props);
    }

    /**
     * Undeploy a web application.
     *
     * @param contextRoot the context's name to undeploy
     * @param appName the J2EE appname used at deployment time
     * @param virtualServers List of current virtual-server object.
     * @param dummy true if the web module to be undeployed is a dummy web
     *        module, that is, a web module created off of a virtual server's
     *        docroot
     */
    public void unloadWebModule(String contextRoot,
            String appName,
            String virtualServers,
            boolean dummy,
            Properties props) {

        if (_logger.isLoggable(Level.FINEST)) {
            _logger.finest("WebContainer.unloadWebModule(): contextRoot: "
                    + contextRoot + " appName:" + appName);
        }

        // tomcat contextRoot starts with "/"
        if (contextRoot.length() != 0 && !contextRoot.startsWith("/") ) {
            contextRoot = "/" + contextRoot;
        } else if ("/".equals(contextRoot)) {
            // Make corresponding change as in WebModuleConfig.getContextPath()
            contextRoot = "";
        }

        List hostList = StringUtils.parseStringList(virtualServers, " ,");
        boolean unloadFromAll = hostList == null || hostList.isEmpty();
        boolean hasBeenUndeployed = false;
        VirtualServer host = null;
        WebModule context = null;
        Container[] hostArray = getEngine().findChildren();
        for(Container aHostArray : hostArray) {
            host = (VirtualServer)aHostArray;
            /**
             * Related to Bug: 4904290: Do not unload from __asadmin
             */
            if (unloadFromAll && host.getName().equalsIgnoreCase(
                    org.glassfish.api.web.Constants.ADMIN_VS)) {
                continue;
            }
            if (unloadFromAll || hostList.contains(host.getName())
                    || verifyAlias(hostList, host)) {
                context = (WebModule)host.findChild(contextRoot);
                if(context != null) {
                    context.saveSessions(props);
                    host.removeChild(context);

                    webStatsProviderBootstrap.unregisterApplicationStatsProviders(
                            context.getMonitoringNodeName(), host.getName());

                    try {
                        /*
                         * If the webapp is being undeployed as part of a
                         * domain shutdown, we don't want to destroy it,
                         * as that would remove any sessions persisted to
                         * file. Any active sessions need to survive the
                         * domain shutdown, so that they may be resumed
                         * after the domain has been restarted.
                         */
                        if(!isShutdown) {
                            context.destroy();
                        }
                    } catch(Exception ex) {
                        String msg = rb.getString(
                            "webcontainer.webmodule.exceptionDuringDestroy");
                        msg = MessageFormat.format(msg, contextRoot,
                            host.getName());
                        _logger.log(Level.WARNING, msg, ex);
                    }
                    if(_logger.isLoggable(Level.FINEST)) {
                        _logger.log(Level.FINEST,
                            "[WebContainer] Context " + contextRoot
                                + " undeployed from " + host);
                    }
                    hasBeenUndeployed = true;
                    host.fireContainerEvent(Deployer.REMOVE_EVENT, context);
                    /*
                     * If the web module that has been unloaded
                     * contained any mappings for ad-hoc paths,
                     * those mappings must be preserved by registering an
                     * ad-hoc web module at the same context root
                     */
                    if (context.hasAdHocPaths()
                            || context.hasAdHocSubtrees()) {
                        WebModule wm = createAdHocWebModule(
                            context.getID(),
                            host,
                            contextRoot,
                            context.getJ2EEApplication());
                        wm.addAdHocPaths(context.getAdHocPaths());
                        wm.addAdHocSubtrees(context.getAdHocSubtrees());
                    }
                    // START GlassFish 141
                    if(!dummy) {
                        WebModuleConfig wmInfo =
                            host.createSystemDefaultWebModuleIfNecessary(
                                habitat.getComponent(
                                    WebArchivist.class));
                        if(wmInfo != null) {
                            loadStandaloneWebModule(host, wmInfo);
                        }
                    }
                    // END GlassFish 141
                }
            }
        }

        if (!hasBeenUndeployed) {
            _logger.log(Level.SEVERE, "webContaienr.undeployError", contextRoot);
        }
    }


    /**
     * Suspends the web application with the given appName that has been
     * deployed at the given contextRoot on the given virtual servers.
     *
     * @param contextRoot the context root
     * @param appName the J2EE appname used at deployment time
     * @param hosts the list of virtual servers
     */
    public boolean suspendWebModule(String contextRoot,
                                    String appName,
                                    String hosts) {
        boolean hasBeenSuspended = false;
        List hostList = StringUtils.parseStringList(hosts, " ,");
        if (hostList == null || hostList.isEmpty()) {
            return hasBeenSuspended;
        }

        // tomcat contextRoot starts with "/"
        if (contextRoot.length() != 0 && !contextRoot.startsWith("/") ) {
            contextRoot = "/" + contextRoot;
        }
        VirtualServer host = null;
        Context context = null;
        for (Container aHostArray : getEngine().findChildren()) {
            host = (VirtualServer)aHostArray;
            if (hostList.contains(host.getName()) ||
                    verifyAlias(hostList, host)) {
                context = (Context)host.findChild(contextRoot);
                if (context != null) {
                    context.setAvailable(false);
                    if (_logger.isLoggable(Level.FINEST)) {
                        _logger.log(Level.FINEST,
                             "[WebContainer] Context "
                                + contextRoot + " disabled from "
                                + host);
                    }
                    hasBeenSuspended = true;
                }
            }
        }

        if (!hasBeenSuspended){
            _logger.log(Level.WARNING, "webContainer.disableWebModuleError", contextRoot);
        }

        return hasBeenSuspended;
    }


    /**
     * Save the server-wide dynamic reloading settings for use when
     * configuring each web module.
     */
    private void configureDynamicReloadingSettings() {
        if (dasConfig != null) {
            _reloadingEnabled = Boolean.parseBoolean(dasConfig.getDynamicReloadEnabled());
            String seconds = dasConfig.getDynamicReloadPollIntervalInSeconds();
            if (seconds != null) {
                try {
                    _pollInterval = Integer.parseInt(seconds);
                } catch (NumberFormatException e) {
                }
            }
        }
    }


    /**
     * Sets the debug level for Catalina's containers based on the logger's
     * log level.
     */
    private void setDebugLevel() {
        Level logLevel = _logger.getLevel() != null ?
                _logger.getLevel() : Level.INFO;
        if (logLevel.equals(Level.FINE))
            _debug = 1;
        else if (logLevel.equals(Level.FINER))
            _debug = 2;
        else if (logLevel.equals(Level.FINEST))
            _debug = 5;
        else
            _debug = 0;
    }


    /**
     * Get the lifecycle listeners associated with this lifecycle. If this
     * Lifecycle has no listeners registered, a zero-length array is returned.
     */
    public LifecycleListener[] findLifecycleListeners() {
        return new LifecycleListener[0];
    }


    /**
     * Gets all the virtual servers whose http-listeners attribute value
     * contains the given http-listener id.
     */
    List<VirtualServer> getVirtualServersForHttpListenerId(
            HttpService httpService, String httpListenerId) {

        if (httpListenerId == null) {
            return null;
        }

        List<VirtualServer> result = new ArrayList<VirtualServer>();

        for (com.sun.enterprise.config.serverbeans.VirtualServer vs : httpService.getVirtualServer()) {
            List<String> listeners = StringUtils.parseStringList(vs.getNetworkListeners(), ",");
            if (listeners != null) {
                ListIterator<String> iter = listeners.listIterator();
                while (iter.hasNext()) {
                    if (httpListenerId.equals(iter.next())) {
                        VirtualServer match = (VirtualServer)
                            getEngine().findChild(vs.getId());
                        if (match != null) {
                            result.add(match);
                        }
                        break;
                    }
                }
            }
        }

        return result;
    }


    /**
     * Adds the given mime mappings to those of the specified context, unless
     * they're already present in the context (that is, the mime mappings of
     * the specified context, which correspond to those in default-web.xml,
     * can't be overridden).
     *
     * @param ctx The StandardContext to whose mime mappings to add
     * @param mimeMap The mime mappings to be added
     */
    private void addMimeMappings(StandardContext ctx, MimeMap mimeMap) {
        if (mimeMap == null) {
            return;
        }

        for (Iterator itr = mimeMap.getExtensions(); itr.hasNext(); ) {
            String extension = (String) itr.next();
            if (ctx.findMimeMapping(extension) == null) {
                ctx.addMimeMapping(extension, mimeMap.getType(extension));
            }
        }
    }

    /**
     * Return the parent/top-level container in _embedded for virtual
     * servers.
     */
    public Engine getEngine() {
        return _embedded.getEngines()[0];
    }
    

    /**
     * Registers the given ad-hoc path at the given context root.
     *
     * @param path The ad-hoc path to register
     * @param ctxtRoot The context root at which to register
     * @param appName The name of the application with which the ad-hoc path is
     * associated
     * @param servletInfo Info about the ad-hoc servlet that will service
     * requests on the given path
     */
    public void registerAdHocPath(
            String path,
            String ctxtRoot,
            String appName,
            AdHocServletInfo servletInfo) {

        registerAdHocPathAndSubtree(path, null, ctxtRoot, appName,
                                    servletInfo);
    }


    /**
     * Registers the given ad-hoc path and subtree at the given context root.
     *
     * @param path The ad-hoc path to register
     * @param subtree The ad-hoc subtree path to register
     * @param ctxtRoot The context root at which to register
     * @param appName The name of the application with which the ad-hoc path
     * and subtree are associated
     * @param servletInfo Info about the ad-hoc servlet that will service
     * requests on the given ad-hoc path and subtree
     */
    public void registerAdHocPathAndSubtree(
            String path,
            String subtree,
            String ctxtRoot,
            String appName,
            AdHocServletInfo servletInfo) {

        WebModule wm = null;

        Container[] vsList = getEngine().findChildren();
        for (Container aVsList : vsList) {
            VirtualServer vs = (VirtualServer) aVsList;
            if (vs.getName().equalsIgnoreCase(
                    org.glassfish.api.web.Constants.ADMIN_VS)) {
                // Do not deploy on admin vs
                continue;
            }
            wm = (WebModule) vs.findChild(ctxtRoot);
            if (wm == null) {
                wm = createAdHocWebModule(vs, ctxtRoot, appName);
            }
            wm.addAdHocPathAndSubtree(path, subtree, servletInfo);
        }
    }


    /**
     * Unregisters the given ad-hoc path from the given context root.
     *
     * @param path The ad-hoc path to unregister
     * @param ctxtRoot The context root from which to unregister
     */
    public void unregisterAdHocPath(String path, String ctxtRoot) {
        unregisterAdHocPathAndSubtree(path, null, ctxtRoot);
    }


    /**
     * Unregisters the given ad-hoc path and subtree from the given context
     * root.
     *
     * @param path The ad-hoc path to unregister
     * @param subtree The ad-hoc subtree to unregister
     * @param ctxtRoot The context root from which to unregister
     */
    public void unregisterAdHocPathAndSubtree(String path,
            String subtree,
            String ctxtRoot) {

        WebModule wm = null;

        Container[] vsList = getEngine().findChildren();
        for (Container aVsList : vsList) {
            VirtualServer vs = (VirtualServer) aVsList;
            if (vs.getName().equalsIgnoreCase(
                    org.glassfish.api.web.Constants.ADMIN_VS)) {
                // Do not undeploy from admin vs, because we never
                // deployed onto it
                continue;
            }
            wm = (WebModule) vs.findChild(ctxtRoot);
            if (wm == null) {
                continue;
            }
            /*
            * If the web module was created by the container for the
            * sole purpose of mapping ad-hoc paths and subtrees,
            * and does no longer contain any ad-hoc paths or subtrees,
            * remove the web module.
            */
            wm.removeAdHocPath(path);
            wm.removeAdHocSubtree(subtree);
            if (wm instanceof AdHocWebModule && !wm.hasAdHocPaths()
                && !wm.hasAdHocSubtrees()) {
                vs.removeChild(wm);
                try {
                    wm.destroy();
                } catch (Exception ex) {
                    String msg = rb.getString(
                        "webcontainer.webmodule.exceptionDuringDestroy");
                    msg = MessageFormat.format(msg, wm.getPath(), vs.getName());
                    _logger.log(Level.WARNING, msg, ex);
                }
            }
        }
    }

    /*
     * Creates an ad-hoc web module and registers it on the given virtual
     * server at the given context root.
     *
     * @param vs The virtual server on which to add the ad-hoc web module
     * @param ctxtRoot The context root at which to register the ad-hoc
     * web module
     * @param appName The name of the application to which the ad-hoc module
     * being generated belongs
     *
     * @return The newly created ad-hoc web module
     */
    private WebModule createAdHocWebModule(
            VirtualServer vs,
            String ctxtRoot,
            String appName) {
        return createAdHocWebModule(appName, vs, ctxtRoot, appName);
    }

    /*
     * Creates an ad-hoc web module and registers it on the given virtual
     * server at the given context root.
     *
     * @param id the id of the ad-hoc web module
     * @param vs The virtual server on which to add the ad-hoc web module
     * @param ctxtRoot The context root at which to register the ad-hoc
     * web module
     * @param appName The name of the application to which the ad-hoc module
     * being generated belongs
     *
     * @return The newly created ad-hoc web module
     */
    private WebModule createAdHocWebModule(
            String id,
            VirtualServer vs,
            String ctxtRoot,
            String appName) {

        AdHocWebModule wm = new AdHocWebModule();
        wm.setID(id);
        wm.setWebContainer(this);

        wm.restrictedSetPipeline(new WebPipeline(wm));

        // The Parent ClassLoader of the AdhocWebModule was null
        // [System ClassLoader]. With the new hierarchy, the thread context
        // classloader needs to be set.
        //if (Boolean.getBoolean(com.sun.enterprise.server.PELaunch.USE_NEW_CLASSLOADER_PROPERTY)) {
        wm.setParentClassLoader(
                Thread.currentThread().getContextClassLoader());
        //}

        wm.setContextRoot(ctxtRoot);
        wm.setJ2EEApplication(appName);
        wm.setName(ctxtRoot);
        wm.setDocBase(vs.getAppBase());
        wm.setEngineName(vs.getParent().getName());

        String domain = _serverContext.getDefaultDomainName();
        wm.setDomain(domain);

        String j2eeServer = _serverContext.getInstanceName();
        wm.setJ2EEServer(j2eeServer);

        wm.setCrossContext(true);
        //wm.setJavaVMs(J2EEModuleUtil.getjavaVMs());

        vs.addChild(wm);

        return wm;
    }


    /**
     * Removes the dummy module (the module created off of a virtual server's
     * docroot) from the given virtual server if such a module exists.
     *
     * @param vs The virtual server whose dummy module is to be removed
     */
    void removeDummyModule(VirtualServer vs) {
        WebModule ctx = (WebModule)vs.findChild("");
        if (ctx != null
                && Constants.DEFAULT_WEB_MODULE_NAME.equals(
                ctx.getModuleName())) {
            unloadWebModule("", ctx.getJ2EEApplication(),
                    vs.getName(), true, null);
        }
    }


    /**
     * Initializes the instance-level session properties (read from
     * config.web-container.session-config.session-properties in domain.xml).
     */
    private void initInstanceSessionProperties() {

        SessionProperties spBean =
            serverConfigLookup.getInstanceSessionProperties();

        if (spBean == null || spBean.getProperty() == null) {
            return;
        }

        List<Property> props = spBean.getProperty();
        if (props == null) {
            return;
        }

        for (Property prop : props) {
            String propName = prop.getName();
            String propValue = prop.getValue();
            if (propName == null || propValue == null) {
                throw new IllegalArgumentException(
                    rb.getString("webcontainer.nullWebProperty"));
            }

            if (propName.equalsIgnoreCase("enableCookies")) {
                instanceEnableCookies = ConfigBeansUtilities.toBoolean(propValue);
            } else {
                Object[] params = { propName };
                _logger.log(Level.INFO, "webcontainer.notYet", params);
            }
        }
    }

    private static synchronized void setJspFactory() {
        if (JspFactory.getDefaultFactory() == null) {
            JspFactory.setDefaultFactory(new JspFactoryImpl());
        }
    }


    /**
     * Delete virtual-server.
     * @param httpService element which contains the configuration info.
     */
    public void deleteHost(HttpService httpService) throws LifecycleException{

        VirtualServer virtualServer;

        // First we need to find which virtual-server was deleted. In
        // reconfig/VirtualServerReconfig, it is impossible to lookup
        // the vsBean because the element is removed from domain.xml
        // before handleDelete is invoked.
        Container[] virtualServers = getEngine().findChildren();
        for (int i=0;i < virtualServers.length; i++){
            for (com.sun.enterprise.config.serverbeans.VirtualServer vse : httpService.getVirtualServer()) {
                if ( virtualServers[i].getName().equals(vse.getId())){
                    virtualServers[i] = null;
                    break;
                }
            }
        }
        for (Container virtualServer1 : virtualServers) {
            virtualServer = (VirtualServer) virtualServer1;
            if (virtualServer != null) {
                if (virtualServer.getID().equals(
                        org.glassfish.api.web.Constants.ADMIN_VS)) {
                    throw new LifecycleException(
                        "Cannot delete admin virtual-server.");
                }
                Container[] webModules = virtualServer.findChildren();
                for (Container webModule : webModules) {
                    unloadWebModule(webModule.getName(),
                        webModule.getName(),
                        virtualServer.getID(),
                        null);
                }
                try {
                    virtualServer.destroy();
                } catch (Exception e) {
                    String msg = rb.getString("webContainer.destroyVsError");
                    msg = MessageFormat.format(msg, virtualServer.getID());
                    _logger.log(Level.WARNING, msg, e);
                }
            }
        }
    }


    /**
     * Updates a virtual-server element.
     *
     * @param vsBean the virtual-server config bean.
     */
    public void updateHost(com.sun.enterprise.config.serverbeans.VirtualServer vsBean) throws LifecycleException {

        if (org.glassfish.api.web.Constants.ADMIN_VS.equals(vsBean.getId())) {
            return;
        }
        final VirtualServer vs = (VirtualServer)getEngine().findChild(vsBean.getId());

        if (vs ==null) {
            _logger.log(Level.WARNING, "webContainer.cannotUpdateNonExistenceVs", vsBean.getId());
            return;
        }

        // Must retrieve the old default-web-module before updating the
        // virtual server with the new vsBean, because default-web-module is
        // read from vsBean
        String oldDefaultWebModule = vs.getDefaultWebModuleID();

        vs.setBean(vsBean);

        vs.setLogFile(vsBean.getLogFile(), logLevel, logHandler);

        vs.configureState();

        vs.clearAliases();
        vs.configureAliases();

        vs.reconfigureAccessLog(globalAccessLogBufferSize,
                                           globalAccessLogWriteInterval,
                                           habitat,
                                           domain,
                                           globalAccessLoggingEnabled);

        // support both docroot property and attribute
        String docroot = vsBean.getPropertyValue("docroot");
        if (docroot == null) {
            docroot = vsBean.getDocroot();
        }
        if (docroot != null) {
            updateDocroot(docroot, vs, vsBean);
        }
        
        List<Property> props = vs.getProperties();
        for (Property prop : props) {
            updateHostProperties(vsBean, prop.getName(), prop.getValue(), securityService, vs);
        }
        vs.configureSingleSignOn(globalSSOEnabled, webContainerFeatureFactory);
        vs.reconfigureAccessLog(globalAccessLogBufferSize, globalAccessLogWriteInterval, habitat, domain,
            globalAccessLoggingEnabled);

        int[] oldPorts = vs.getPorts();

        List<String> listeners = StringUtils.parseStringList(
            vsBean.getNetworkListeners(), ",");
        if (listeners != null) {
            HashSet<NetworkListener> networkListeners = new HashSet<NetworkListener>();
            for (String listener : listeners) {
                boolean found = false;
                for (NetworkListener httpListener : habitat.getAllByContract(NetworkListener.class)) {
                    if (httpListener.getName().equals(listener)) {
                        networkListeners.add(httpListener);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    String msg = rb.getString(
                        "webcontainer.listenerReferencedByHostNotExist");
                    msg = MessageFormat.format(msg, listener, vs.getName());
                    _logger.log(Level.SEVERE, msg);
                }
            }
            // Update the port numbers with which the virtual server is
            // associated
            configureHostPortNumbers(vs, networkListeners);
        } else {
            // The virtual server is not associated with any http listeners
            vs.setPorts(new int[0]);
        }

        int[] newPorts = vs.getPorts();

        // Disassociate the virtual server from all http listeners that
        // have been removed from its http-listeners attribute
        for (int oldPort : oldPorts) {
            boolean found = false;
            for (int newPort : newPorts) {
                if (oldPort == newPort) {
                    found = true;
                }
            }
            if (!found) {
                // http listener was removed
                Connector[] connectors = _embedded.findConnectors();
                for (Connector connector : connectors) {
                    WebConnector conn = (WebConnector) connector;
                    if (oldPort == conn.getPort()) {
                        try {
                            conn.getMapperListener().unregisterHost(
                                vs.getJmxName());
                        } catch (Exception e) {
                            throw new LifecycleException(e);
                        }
                    }
                }

            }
        }

        // Associate the virtual server with all http listeners that
        // have been added to its http-listeners attribute
        for (int newPort : newPorts) {
            boolean found = false;
            for (int oldPort : oldPorts) {
                if (newPort == oldPort) {
                    found = true;
                }
            }
            if (!found) {
                // http listener was added
                Connector[] connectors = _embedded.findConnectors();
                for (Connector connector : connectors) {
                    WebConnector conn = (WebConnector)
                        connector;
                    if (newPort == conn.getPort()) {
                        if (!conn.isAvailable()) {
                            conn.start();
                        }
                        try {
                            conn.getMapperListener().registerHost(
                                vs.getJmxName());
                        } catch (Exception e) {
                            throw new LifecycleException(e);
                        }
                    }
                }
            }
        }

        // Remove the old default web module if one was configured, by
        // passing in "null" as the default context path
        if (oldDefaultWebModule != null) {
            updateDefaultWebModule(vs, oldPorts, null);
        }

        /*
         * Add default web module if one has been configured for the
         * virtual server. If the module declared as the default web module
         * has already been deployed at the root context, we don't have
         * to do anything.
         */
        WebModuleConfig wmInfo = vs.getDefaultWebModule(domain,
                        habitat.getComponent(WebArchivist.class),
                        appRegistry);
        if ((wmInfo != null) && (wmInfo.getContextPath() != null) &&
                !"".equals(wmInfo.getContextPath()) &&
                !"/".equals(wmInfo.getContextPath())) {
            // Remove dummy context that was created off of docroot, if such
            // a context exists
            removeDummyModule(vs);
            updateDefaultWebModule(vs,
                                   vs.getPorts(),
                                   wmInfo);
        } else {
            WebModuleConfig wmc =
                vs.createSystemDefaultWebModuleIfNecessary(
                    habitat.getComponent(WebArchivist.class));
            if ( wmc != null) {
                loadStandaloneWebModule(vs,wmc);
            }
        }

        /*
         * Need to update connector and mapper restart is required when
         * virtual-server.http-listeners is changed dynamically
         */
        Collection<NetworkListener> httpListeners = habitat.getAllByContract(NetworkListener.class);
        if (httpListeners != null) {
            for (NetworkListener httpListener : httpListeners) {
                updateConnector(httpListener, habitat.getByType(HttpService.class));
            }
        }

    }


    /**
     * Update virtual-server properties.
     */
    public void updateHostProperties(
        com.sun.enterprise.config.serverbeans.VirtualServer vsBean,
        String name, String value, SecurityService securityService, final VirtualServer vs) {
        if (vs == null) {
            return;
        }
        vs.setBean(vsBean);

        if (name == null) {
            return;
        }

        if (name.startsWith("alternatedocroot_")) {
            updateAlternateDocroot(vs);
        } else if ("setCacheControl".equals(name)){
            vs.configureCacheControl(value);
        } else if (Constants.ACCESS_LOGGING_ENABLED.equals(name)){
            vs.reconfigureAccessLog(globalAccessLogBufferSize,
                globalAccessLogWriteInterval, habitat, domain,
                globalAccessLoggingEnabled);
        } else if (Constants.ACCESS_LOG_PROPERTY.equals(name)){
            vs.reconfigureAccessLog(globalAccessLogBufferSize,
                globalAccessLogWriteInterval, habitat, domain,
                globalAccessLoggingEnabled);
        } else if (Constants.ACCESS_LOG_WRITE_INTERVAL_PROPERTY.equals(name)){
            vs.reconfigureAccessLog(globalAccessLogBufferSize,
                                    globalAccessLogWriteInterval,
                                    habitat,
                                    domain,
                                    globalAccessLoggingEnabled);
        } else if (Constants.ACCESS_LOG_BUFFER_SIZE_PROPERTY.equals(name)){
            vs.reconfigureAccessLog(globalAccessLogBufferSize,
                                    globalAccessLogWriteInterval,
                                    habitat,
                                    domain,
                                    globalAccessLoggingEnabled);
        } else if ("allowRemoteHost".equals(name)
                || "denyRemoteHost".equals(name)) {
            vs.configureRemoteHostFilterValve();
        } else if ("allowRemoteAddress".equals(name)
                || "denyRemoteAddress".equals(name)) {
            vs.configureRemoteAddressFilterValve();
        } else if (Constants.SSO_ENABLED.equals(name)) {
            vs.configureSingleSignOn(globalSSOEnabled, webContainerFeatureFactory);
        } else if ("authRealm".equals(name)) {
            vs.configureAuthRealm(securityService);
        } else if (name.startsWith("send-error")) {
            vs.configureErrorPage();
        } else if (Constants.ERROR_REPORT_VALVE.equals(name)) {
            vs.setErrorReportValveClass(value);
        } else if (name.startsWith("redirect")) {
            vs.configureRedirect();
        } else if (name.startsWith("contextXmlDefault")) {
            vs.setDefaultContextXmlLocation(value);
        }
    }


    /**
     * Processes an update to the http-service element, by updating each
     * http-listener
     */
    public void updateHttpService(HttpService httpService) throws LifecycleException {

        if (httpService == null) {
            return;
        }

        /*
         * Update each virtual server with the sso-enabled and
         * access logging related properties of the updated http-service
         */
        globalSSOEnabled = ConfigBeansUtilities.toBoolean(httpService.getSsoEnabled());
        globalAccessLogWriteInterval = httpService.getAccessLog().getWriteIntervalSeconds();
        globalAccessLogBufferSize = httpService.getAccessLog().getBufferSizeBytes();
        globalAccessLoggingEnabled = ConfigBeansUtilities.toBoolean(httpService.getAccessLoggingEnabled());

        List<com.sun.enterprise.config.serverbeans.VirtualServer> virtualServers =
            httpService.getVirtualServer();
        for (com.sun.enterprise.config.serverbeans.VirtualServer virtualServer : virtualServers) {
            final VirtualServer vs = (VirtualServer) getEngine().findChild(virtualServer.getId());
            if (vs != null) {
                vs.configureSingleSignOn(globalSSOEnabled, webContainerFeatureFactory);
                vs.reconfigureAccessLog(globalAccessLogBufferSize, globalAccessLogWriteInterval, habitat, domain,
                    globalAccessLoggingEnabled);
                updateHost(virtualServer);
            }
        }

        Collection<NetworkListener> listeners = habitat.getAllByContract(NetworkListener.class);
        if (listeners != null) {
            for (NetworkListener httpListener : listeners) {
                updateConnector(httpListener, httpService);
            }
        }
    }


    /**
     * Update an http-listener property
     * @param listener the configuration bean.
     * @param propName the property name
     * @param propValue the property value
     */
    public void updateConnectorProperty(NetworkListener listener,
                                        String propName,
                                        String propValue)
        throws LifecycleException{

        WebConnector connector = connectorMap.get(listener.getName());
        if (connector != null) {
            connector.configHttpProperties(listener.findHttpProtocol().getHttp(),
                listener.findTransport(), listener.findHttpProtocol().getSsl());
            connector.configureHttpListenerProperty(propName, propValue);
        }
    }


    /**
     * Update an network-listener
     * @param httpService the configuration bean.
     */
    public void updateConnector(NetworkListener networkListener,
                                HttpService httpService)
            throws LifecycleException {

        synchronized(mapperUpdateSync) {
            // Disable dynamic reconfiguration of the http listener at which
            // the admin related webapps (including the admingui) are accessible.
            // Notice that in GlassFish v3, we support a domain.xml configuration
            // that does not declare any admin-listener, in which case the
            // admin-related webapps are accessible on http-listener-1.
            if (networkListener.findHttpProtocol().getHttp().getDefaultVirtualServer().equals(
                    org.glassfish.api.web.Constants.ADMIN_VS) ||
                "http-listener-1".equals(networkListener.getName()) &&
                connectorMap.get("admin-listener") == null) {
                return;
            }

            WebConnector connector = connectorMap.get(networkListener.getName());
            if (connector != null) {
                deleteConnector(connector);
            }

            if (!Boolean.valueOf(networkListener.getEnabled())) {
                return;
            }

            connector = addConnector(networkListener, httpService, false);

            // Update the list of ports of all associated virtual servers with
            // the listener's new port number, so that the associated virtual
            // servers will be registered with the listener's request mapper when
            // the listener is started
            List<VirtualServer> virtualServers =
                    getVirtualServersForHttpListenerId(httpService, networkListener.getName());
            if (virtualServers != null) {
                Mapper mapper = connector.getMapper();
                for (VirtualServer vs : virtualServers) {
                    boolean found = false;
                    int[] ports = vs.getPorts();
                    for (int i = 0; i < ports.length; i++) {
                        if (ports[i] == connector.getPort()) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        int[] newPorts = new int[ports.length + 1];
                        System.arraycopy(ports, 0, newPorts, 0, ports.length);
                        newPorts[ports.length] = connector.getPort();
                        vs.setPorts(newPorts);
                    }

                    // Check if virtual server has default-web-module configured,
                    // and if so, configure the http listener's mapper with this
                    // information
                    String defaultWebModulePath = vs.getDefaultContextPath(
                            domain, appRegistry);
                    if (defaultWebModulePath != null) {
                        try {
                            mapper.setDefaultContextPath(vs.getName(),
                                    defaultWebModulePath);
                            vs.setDefaultContextPath(defaultWebModulePath);
                        } catch (Exception e) {
                            throw new LifecycleException(e);
                        }
                    }
                }
            }

            connector.start();
        }
    }

    /**
     * Method gets called, when GrizzlyService changes HTTP Mapper, associated
     * with specific port.
     *
     * @param httpService {@link HttpService}
     * @param httpListener {@link NetworkListener}, which {@link Mapper} was changed
     * @param mapper new {@link Mapper} value
     */
    public void updateMapper(HttpService httpService, NetworkListener httpListener,
            Mapper mapper) {
        synchronized(mapperUpdateSync) {
            WebConnector connector = connectorMap.get(httpListener.getName());
            if (connector != null && connector.getMapper() != mapper) {
                try {
                    updateConnector(httpListener, httpService);
                } catch (LifecycleException le) {
                    _logger.log(Level.SEVERE, "webcontainer.exceptionConfigHttpService", le);
                }
            }
        }
    }


    public WebConnector addConnector(NetworkListener httpListener,
                                     HttpService httpService,
                                     boolean start)
                throws LifecycleException {

        synchronized(mapperUpdateSync) {
            int port = Integer.parseInt(httpListener.getPort());

            // Add the port number of the new http-listener to its
            // default-virtual-server, so that when the new http-listener
            // and its MapperListener are started, they will recognize the
            // default-virtual-server as one of their own, and add it to the
            // Mapper
            String virtualServerName = httpListener.findHttpProtocol().getHttp().getDefaultVirtualServer();
            VirtualServer vs =
                    (VirtualServer) getEngine().findChild(virtualServerName);
            int[] oldPorts = vs.getPorts();
            int[] newPorts = new int[oldPorts.length + 1];
            System.arraycopy(oldPorts, 0, newPorts, 0, oldPorts.length);
            newPorts[oldPorts.length] = port;
            vs.setPorts(newPorts);

            Mapper mapper = null;
            for (Mapper m : habitat.getAllByContract(Mapper.class)) {
                if (m.getPort() == port) {
                    mapper = m;
                    break;
                }
            }           
                    
            WebConnector connector = 
                    createHttpListener(httpListener, httpService, mapper);
           
            if (connector.getRedirectPort() == -1) {
                connector.setRedirectPort(defaultRedirectPort);
            }

            if (start) {
                connector.start();
            }
            return connector;
        }
    }


    /**
     * Stops and deletes the specified http listener.
     */
    public void deleteConnector(WebConnector connector)
            throws LifecycleException{

        int port = connector.getPort();

        Connector[] connectors = _embedded.findConnectors();
        for (Connector connector1 : connectors) {
            WebConnector conn = (WebConnector) connector1;
            if (port == conn.getPort()) {
                _embedded.removeConnector(conn);
                portMap.remove(connector.getName());
                connectorMap.remove(connector.getName());
            }
        }
    }


    /**
     * Stops and deletes the specified http listener.
     */
    public void deleteConnector(NetworkListener httpListener)
            throws LifecycleException{

        Connector[] connectors = _embedded.findConnectors();
        int port = Integer.parseInt(httpListener.getPort());
        for (Connector connector : connectors) {
            WebConnector conn = (WebConnector) connector;
            if (port == conn.getPort()) {
                _embedded.removeConnector(conn);
                portMap.remove(httpListener.getName());
                connectorMap.remove(httpListener.getName());
            }
        }

    }


    /**
     * Reconfigures the access log valve of each virtual server with the
     * updated attributes of the <access-log> element from domain.xml.
     */
    public void updateAccessLog(HttpService httpService) {
        Container[] virtualServers = getEngine().findChildren();
        for (Container virtualServer : virtualServers) {
            ((VirtualServer) virtualServer).reconfigureAccessLog(
                httpService, webContainerFeatureFactory);
        }
    }


    /**
     * Updates the docroot of the given virtual server
     */
    private void updateDocroot(
            String docroot,
            VirtualServer vs,
            com.sun.enterprise.config.serverbeans.VirtualServer vsBean) {

        validateDocroot(docroot, vsBean.getId(),
                        vsBean.getDefaultWebModule());
        vs.setAppBase(docroot);
        removeDummyModule(vs);
        WebModuleConfig wmInfo =
            vs.createSystemDefaultWebModuleIfNecessary(
                habitat.getComponent(WebArchivist.class));
        if (wmInfo != null) {
            loadStandaloneWebModule(vs, wmInfo);
        }
    }


    private void updateAlternateDocroot(VirtualServer vs) {
        removeDummyModule(vs);
        WebModuleConfig wmInfo = vs.createSystemDefaultWebModuleIfNecessary(
            habitat.getComponent(WebArchivist.class));
        if (wmInfo != null) {
            loadStandaloneWebModule(vs, wmInfo);
        }
    }


    /**
     * is Tomcat using default domain name as its domain
     */
    protected boolean isTomcatUsingDefaultDomain() {
        // need to be careful and make sure tomcat jmx mapping works
        // since setting this to true might result in undeployment problems
        return true;
    }


    /**
     * Creates probe providers for Servlet, JSP, Session, and
     * Request/Response related events.
     *
     * While the Servlet, JSP, and Session related probe providers are
     * shared by all web applications (where every web application
     * qualifies its probe events with its application name), the
     * Request/Response related probe provider is shared by all HTTP
     * listeners.
     */
    private void createProbeProviders() {
        webModuleProbeProvider = new WebModuleProbeProvider();
        servletProbeProvider = new ServletProbeProvider();
        jspProbeProvider = new JspProbeProvider();
        sessionProbeProvider = new SessionProbeProvider();
        requestProbeProvider = new RequestProbeProvider();
    }


    /**
     * Creates statistics providers for Servlet, JSP, Session, and
     * Request/Response related events.
     */
    private void createStatsProviders() {
        HttpServiceStatsProviderBootstrap httpStatsProviderBootstrap =
                habitat.getByType(HttpServiceStatsProviderBootstrap.class);
        webStatsProviderBootstrap =
                habitat.getByType(WebStatsProviderBootstrap.class);
    }


    /*
     * Loads the class with the given name using the common classloader,
     * which is responsible for loading any classes from the domain's
     * lib directory
     *
     * @param className the name of the class to load
     */
    public Class loadCommonClass(String className) throws Exception {
        return clh.getCommonClassLoader().loadClass(className);
    }
}
