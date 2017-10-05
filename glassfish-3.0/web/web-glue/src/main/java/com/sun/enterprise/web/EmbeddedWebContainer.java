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
import java.util.ResourceBundle;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.text.MessageFormat;

import org.apache.catalina.Connector;
import org.apache.catalina.ContainerListener;
import org.apache.catalina.Context;
import org.apache.catalina.Host;
import org.apache.catalina.Engine;
import org.apache.catalina.Realm;
import org.apache.catalina.core.StandardEngine;
import org.apache.catalina.startup.Embedded;
import org.apache.catalina.startup.ContextConfig;

//import org.openide.util.Lookup;
import org.glassfish.api.invocation.InvocationManager;
import org.glassfish.internal.api.ServerContext;
import org.jvnet.hk2.annotations.Inject;
import org.jvnet.hk2.annotations.Scoped;
import org.jvnet.hk2.annotations.Service;
import org.jvnet.hk2.component.Habitat;
import org.jvnet.hk2.component.PostConstruct;
import org.jvnet.hk2.component.Singleton;

import com.sun.enterprise.container.common.spi.util.InjectionManager;
import com.sun.enterprise.deployment.WebBundleDescriptor; 
import com.sun.enterprise.web.logger.FileLoggerHandler;
import com.sun.enterprise.web.pluggable.WebContainerFeatureFactory;
import com.sun.logging.LogDomains;
import com.sun.web.server.WebContainerListener;

/**
 * Represents an embedded Catalina web container within the Application Server.
 */

@Service(name="com.sun.enterprise.web.EmbeddedWebContainer")
@Scoped(Singleton.class)
public final class EmbeddedWebContainer extends Embedded implements PostConstruct {
    
    @Inject
    private Habitat habitat;
    
    @Inject
    private ServerContext serverContext;

    protected static final Logger _logger
        = LogDomains.getLogger(EmbeddedWebContainer.class, LogDomains.WEB_LOGGER);

    protected static final ResourceBundle rb = _logger.getResourceBundle();

    private WebContainerFeatureFactory webContainerFeatureFactory;

    private WebContainer webContainer;

    private InvocationManager invocationManager;

    private InjectionManager injectionManager;

    /*
     * The value of the 'file' attribute of the log-service element
     */
    private String logServiceFile;
    
    /*
     * The log level for org.apache.catalina.level as defined in logging.properties 
     */
    private String logLevel;

    private FileLoggerHandler logHandler;
    
    void setWebContainer(WebContainer webContainer) {
        this.webContainer = webContainer;
    }
        
    void setLogServiceFile(String logServiceFile) {
        this.logServiceFile = logServiceFile;
    }
        
    void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }
            
    void setLogHandler(FileLoggerHandler logHandler) {
        this.logHandler = logHandler;
    }
    
    // --------------------------------------------------------- Public Methods
    
    public void postConstruct() {
        webContainerFeatureFactory = habitat.getByContract(
                WebContainerFeatureFactory.class);
        invocationManager = habitat.getByContract(
                InvocationManager.class);
        injectionManager = habitat.getByContract(
                InjectionManager.class);
    }
    
    /**
     * Creates a virtual server.
     *
     * @param vsID Virtual server id
     * @param vsBean Bean corresponding to virtual-server element in domain.xml
     * @param vsDocroot Virtual server docroot
     * @param vsMimeMap Virtual server MIME mappings
     *
     * @return The generated virtual server instance
     */
    public Host createHost(
                    String vsID,
                    com.sun.enterprise.config.serverbeans.VirtualServer vsBean,
                    String vsDocroot,
                    String vsLogFile,
                    MimeMap vsMimeMap) {

        VirtualServer vs = new VirtualServer();

        vs.configure(vsID, vsBean, vsDocroot, vsLogFile, vsMimeMap,
                     logServiceFile, logLevel, logHandler);
         
        ContainerListener listener = loadListener
            ("com.sun.enterprise.web.connector.extension.CatalinaListener");
        if ( listener != null ) {
            vs.addContainerListener(listener);     
        }

        return vs;
    }
    
    /**
     * Create a web module/application.
     *
     * @param ctxPath  Context path for the web module
     * @param location Absolute pathname to the web module directory
     * @param defaultWebXmlLocation Location of default-web.xml
     */
    public Context createContext(String id,
                                 String ctxPath,
                                 File location,
                                 String defaultContextXmlLocation,
                                 String defaultWebXmlLocation, 
                                 boolean useDOLforDeployment,
                                 WebModuleConfig wmInfo) {

        File configFile = null;
        // check contextPath.xml and /META-INF/context.xml if not found
        if (ctxPath.equals("")) {
            configFile = new File(getCatalinaHome()+"/config", "ROOT.xml");
        } else {
            configFile = new File(getCatalinaHome()+"/config", ctxPath+".xml");
        }
        if (!configFile.exists()) {
            configFile = new File(location, Constants.WEB_CONTEXT_XML);
        }
        WebModule context = new WebModule();
        context.setID(id);
        context.setWebContainer(webContainer);
        context.setDebug(debug);
        context.setPath(ctxPath);
        context.setDocBase(location.getAbsolutePath());
        context.setCrossContext(true);
        context.setUseNaming(isUseNaming());
        context.setHasWebXml(wmInfo.getDescriptor() != null);
        context.setWebBundleDescriptor(wmInfo.getDescriptor());
        context.setManagerChecksFrequency(1);
        context.setServerContext(serverContext);
        context.setWebModuleConfig(wmInfo);

        if (configFile.exists()) {
            context.setConfigFile(configFile.getAbsolutePath());
        }
            
        ContextConfig config;
        if (useDOLforDeployment) {            
            config = new WebModuleContextConfig();  
            ((WebModuleContextConfig)config).setDescriptor(
                wmInfo.getDescriptor());
            ((WebModuleContextConfig)config).setHabitat(habitat);
        } else {
            config = new ContextConfig();
        }
        
        config.setDefaultContextXml(defaultContextXmlLocation);
        config.setDefaultWebXml(defaultWebXmlLocation);
        context.addLifecycleListener(config);

        // TODO: should any of those become WebModuleDecorator, too?
        context.addLifecycleListener(new WebModuleListener(webContainer,
                location, wmInfo.getDescriptor()));

        context.addContainerListener(
                new WebContainerListener(invocationManager, injectionManager));

        for( WebModuleDecorator d : habitat.getAllByContract(WebModuleDecorator.class)) {
            d.decorate(context);
        }

        // TODO: monitoring should also hook in via WebModuleDecorator
        //context.addInstanceListener(
        //    "com.sun.enterprise.admin.monitor.callflow.WebContainerListener");
        
        return context;
    }

         
    /**
     * Util method to load classes that might get compiled after this class is
     * compiled.
     */
    private ContainerListener loadListener(String className){
        try{
            Class clazz = Class.forName(className);
            return (ContainerListener)clazz.newInstance();
        } catch (Throwable ex){
            String msg = rb.getString("embedded.loadListener");
            msg = MessageFormat.format(msg, className);
            _logger.log(Level.SEVERE, msg, ex);
        }
        return null;
    }
    
   
    /**
     * Return the list of engines created (from Embedded API)
     */
    public Engine[] getEngines() {
        return engines;
    }

    /**
     * Returns the list of Connector objects associated with this 
     * EmbeddedWebContainer.
     *
     * @return The list of Connector objects associated with this 
     * EmbeddedWebContainer
     */
    public Connector[] getConnectors() {
        return connectors;
    }


    /**
     * Create a customized version of the Tomcat's 5 Coyote Connector. This
     * connector is required in order to support PE Web Programmatic login
     * functionality.
     * @param address InetAddress to bind to, or <code>null</code> if the
     * connector is supposed to bind to all addresses on this server
     * @param port Port number to listen to
     * @param protocol the http protocol to use.
     */
    public Connector createConnector(String address, int port,
				     String protocol) {

        if (address != null) {
            /*
             * InetAddress.toString() returns a string of the form
             * "<hostname>/<literal_IP>". Get the latter part, so that the
             * address can be parsed (back) into an InetAddress using
             * InetAddress.getByName().
             */
            int index = address.indexOf('/');
            if (index != -1) {
                address = address.substring(index + 1);
            }
        }

        _logger.log(Level.FINE,
                    "Creating connector for address='" +
                    ((address == null) ? "ALL" : address) +
                    "' port='" + port + "' protocol='" + protocol + "'");

        WebConnector connector = new WebConnector(webContainer);

        if (address != null) {
            connector.setAddress(address);
        }

        connector.setPort(port);

        if (protocol.equals("ajp")) {
            connector.setProtocolHandlerClassName(
                 "org.apache.jk.server.JkCoyoteHandler");
        } else if (protocol.equals("memory")) {
            connector.setProtocolHandlerClassName(
                 "org.apache.coyote.memory.MemoryProtocolHandler");
        } else if (protocol.equals("https")) {
            connector.setScheme("https");
            connector.setSecure(true);
        }

        return (connector);

    }
    

    /**
     * Create, configure, and return an Engine that will process all
     * HTTP requests received from one of the associated Connectors,
     * based on the specified properties.
     *
     * Do not create the JAAS default realm since all children will
     * have their own.
     */
    public Engine createEngine() {

        StandardEngine engine = new WebEngine(webContainer);

        engine.setDebug(debug);
        // Default host will be set to the first host added
        engine.setLogger(logger);       // Inherited by all children
        engine.setRealm(null);         // Inherited by all children
        
        //ContainerListener listener = loadListener
        //    ("com.sun.enterprise.admin.monitor.callflow.WebContainerListener");
        //if ( listener != null ) {
        //    engine.addContainerListener(listener);
        //}
        return (engine);

    }


    static class WebEngine extends StandardEngine {

        private WebContainer webContainer;

        public WebEngine(WebContainer webContainer) {
            this.webContainer = webContainer;
        }

        public Realm getRealm(){
            return null;
        }

        /**
         * Starts the children (virtual servers) of this StandardEngine
         * concurrently.
         *
        protected void startChildren() {

            
            new File(webContainer.getAppsWorkRoot()).mkdirs();
            new File(webContainer.getModulesWorkRoot()).mkdirs();
            
            ArrayList<LifecycleStarter> starters
                = new ArrayList<LifecycleStarter>();

            Container children[] = findChildren();
            for (int i = 0; i < children.length; i++) {
                if (children[i] instanceof Lifecycle) {
                    LifecycleStarter starter =
                        new LifecycleStarter(((Lifecycle) children[i]));
                    starters.add(starter);
                    starter.submit();
                }
            }

            for (LifecycleStarter starter : starters) {
                Throwable t = starter.waitDone(); 
                if (t != null) {
                    Lifecycle container = starter.getContainer();
                    String msg = rb.getString("embedded.startVirtualServerError");
                    msg = MessageFormat.format(msg, new Object[] { container });
                    _logger.log(Level.SEVERE, msg, t);
                }
            }
        }*/
    }
}
