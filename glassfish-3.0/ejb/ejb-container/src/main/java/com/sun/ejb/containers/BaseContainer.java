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
package com.sun.ejb.containers;

import com.sun.ejb.*;
import com.sun.ejb.codegen.ServiceInterfaceGenerator;
import com.sun.ejb.codegen.AsmSerializableBeanGenerator;
import com.sun.ejb.base.stats.MonitoringRegistryMediator;
import com.sun.ejb.containers.interceptors.InterceptorManager;
import com.sun.ejb.containers.interceptors.SystemInterceptorProxy;
import com.sun.ejb.containers.util.MethodMap;
import com.sun.ejb.portable.EJBMetaDataImpl;
import com.sun.ejb.spi.io.IndirectlySerializable;
import com.sun.ejb.spi.stats.EJBMethodStatsManager;
import org.glassfish.enterprise.iiop.api.ProtocolManager;
import org.glassfish.enterprise.iiop.api.RemoteReferenceFactory;
import org.glassfish.enterprise.iiop.spi.EjbContainerFacade;
import com.sun.enterprise.admin.monitor.callflow.Agent;
import com.sun.enterprise.admin.monitor.callflow.CallFlowInfo;
import com.sun.enterprise.admin.monitor.callflow.ComponentType;
import com.sun.enterprise.admin.monitor.callflow.ContainerTypeOrApplicationType;
import com.sun.enterprise.admin.monitor.registry.MonitoredObjectType;
import com.sun.enterprise.transaction.api.JavaEETransaction;
import com.sun.enterprise.transaction.api.JavaEETransactionManager;
import com.sun.enterprise.container.common.spi.util.ComponentEnvManager;
import com.sun.enterprise.container.common.spi.util.InjectionManager;
import com.sun.enterprise.container.common.spi.JavaEEContainer;
import com.sun.enterprise.deployment.*;
import com.sun.enterprise.deployment.runtime.IASEjbExtraDescriptors;
import com.sun.enterprise.deployment.util.TypeUtil;
import com.sun.enterprise.util.LocalStringManagerImpl;
import com.sun.enterprise.util.Utility;
import org.glassfish.api.invocation.ComponentInvocation;
import org.glassfish.api.invocation.InvocationManager;
import org.glassfish.api.naming.GlassfishNamingManager;
import org.glassfish.enterprise.iiop.api.GlassFishORBHelper;
import org.glassfish.ejb.spi.WSEjbEndpointRegistry;
import org.glassfish.ejb.api.EjbEndpointFacade;
import org.glassfish.internal.api.Globals;
import org.glassfish.deployment.common.DeploymentException;

import com.sun.enterprise.container.common.spi.JCDIService;

import com.sun.ejb.monitoring.stats.*;
import com.sun.ejb.monitoring.probes.*;
import org.glassfish.external.probe.provider.StatsProviderManager;
import org.glassfish.external.probe.provider.PluginPoint;
import org.glassfish.flashlight.provider.ProbeProviderFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.*;
import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.StringRefAddr;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.*;

import java.io.Serializable;
import java.lang.reflect.*;
import java.rmi.AccessException;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jvnet.hk2.component.Habitat;

/**
 * This class implements part of the com.sun.ejb.Container interface.
 * It implements the container's side of the EJB-to-Container
 * contract definweed by the EJB 2.0 spec.
 * It contains code shared by SessionBeans, EntityBeans and MessageDrivenBeans.
 * Its subclasses provide the remaining implementation of the
 * container functionality.
 *
 */

public abstract class BaseContainer
    implements Container, EjbContainerFacade, JavaEEContainer
{
    public enum ContainerType {
        STATELESS, STATEFUL, SINGLETON, MESSAGE_DRIVEN, ENTITY, READ_ONLY
    };

    protected static final Logger _logger =
            EjbContainerUtilImpl.getInstance().getLogger();
    
    protected static final Class[] NO_PARAMS = new Class[] {};
    
    protected Object[] logParams = null;

    protected ContainerType containerType;

    // constants for EJB(Local)Home/EJB(Local)Object methods,
    // used in authorizeRemoteMethod and authorizeLocalMethod
    private static final int EJB_INTF_METHODS_LENGTH = 16;
    static final int EJBHome_remove_Handle      = 0;
    static final int EJBHome_remove_Pkey        = 1;
    static final int EJBHome_getEJBMetaData	    = 2;
    static final int EJBHome_getHomeHandle      = 3;
    static final int EJBLocalHome_remove_Pkey   = 4;
    static final int EJBObject_getEJBHome       = 5;
    static final int EJBObject_getPrimaryKey    = 6;
    static final int EJBObject_remove		    = 7;
    static final int EJBObject_getHandle        = 8;
    static final int EJBObject_isIdentical      = 9;
    static final int EJBLocalObject_getEJBLocalHome = 10;
    static final int EJBLocalObject_getPrimaryKey   = 11;
    static final int EJBLocalObject_remove      = 12;
    static final int EJBLocalObject_isIdentical	= 13;
    static final int EJBHome_create             = 14;
    static final int EJBLocalHome_create        = 15;

    // true if home method, false if component intf method.
    // Used for setting info on invocation object during authorization.
    private static final boolean[] EJB_INTF_METHODS_INFO =
    { true,  true,  true,  true,  true,
      false, false, false, false, false,
      false, false, false, false, 
      true,  true };            
    
    private static final String USER_TX = "java:comp/UserTransaction";

    private static final byte HOME_KEY = (byte)0xff;
    private static final byte[] homeInstanceKey = {HOME_KEY};

    protected ClassLoader loader = null;
    protected Class ejbClass = null;
    protected Class sfsbSerializedClass = null;
    protected Method ejbPassivateMethod = null;
    protected Method ejbActivateMethod = null;
    protected Method ejbRemoveMethod = null;
    protected Method ejbTimeoutMethod = null;

    protected Class webServiceEndpointIntf = null;
   
    // true if exposed as a web service endpoint.
    protected boolean isWebServiceEndpoint = false;
    
    private boolean isTimedObject_ = false;

    /*****************************************
     *    Data members for Local views       *
     *****************************************/

    // True if bean has a LocalHome/Local view 
    // OR a Local business view OR both.
    protected boolean isLocal=false;

    // True if bean exposes a local home view
    protected boolean hasLocalHomeView=false;

    // True if bean exposes a local business view
    protected boolean hasLocalBusinessView=false;

    protected boolean hasOptionalLocalBusinessView = false;

    protected Class ejbGeneratedOptionalLocalBusinessIntfClass;
    //
    // Data members for LocalHome/Local view
    //

    // LocalHome interface written by developer
    protected Class localHomeIntf = null;

    // Local interface written by developer
    private Class localIntf = null;

    // Client reference to ejb local home
    protected EJBLocalHome ejbLocalHome;

    // Implementation of ejb local home.  May or may not be the same
    // object as ejbLocalHome, for example in the case of dynamic proxies.
    protected EJBLocalHomeImpl ejbLocalHomeImpl;

    // Constructor used to instantiate ejb local object proxy.
    private Constructor ejbLocalObjectProxyCtor;

    //
    // Data members for 3.x Local business view
    //
    
    // Internal interface describing operation used to create an
    // instance of a local business object. (GenericEJBLocalHome)
    protected Class localBusinessHomeIntf = null;
    protected Class ejbOptionalLocalBusinessHomeIntf = null;

    // Local business interface written by developer
    protected Set<Class> localBusinessIntfs = new HashSet();

    // Client reference to internal local business home interface.
    // This is only seen by internal ejb code that instantiates local
    // business objects during lookups.
    protected GenericEJBLocalHome ejbLocalBusinessHome;

    protected GenericEJBLocalHome ejbOptionalLocalBusinessHome;

    // Implementation of internal local business home interface.
    protected EJBLocalHomeImpl ejbLocalBusinessHomeImpl;

    // Implementation of internal local business home interface.
    protected EJBLocalHomeImpl ejbOptionalLocalBusinessHomeImpl;

    // Constructor used to instantiate local business object proxy.
    private Constructor ejbLocalBusinessObjectProxyCtor;

    // Constructor used to instantiate local business object proxy.
    private Constructor ejbOptionalLocalBusinessObjectProxyCtor;

    /*****************************************
     *     Data members for Remote views     *
     *****************************************/

    // True if bean has a RemoteHome/Remote view 
    // OR a Remote business view OR both.
    protected boolean isRemote=false;
    
    // True if bean exposes a RemoteHome view
    protected boolean hasRemoteHomeView=false;

    // True if bean exposes a Remote Business view.
    protected boolean hasRemoteBusinessView=false;

    //
    // Data members for RemoteHome/Remote view
    //

    // Home interface written by developer.
    protected Class homeIntf = null;
    
    // Remote interface written by developer.
    protected Class remoteIntf = null;

    // Container implementation of EJB Home. May or may not be the same
    // object as ejbHome, for example in the case of dynamic proxies.
    protected EJBHomeImpl ejbHomeImpl;

    // EJB Home reference used by ORB Tie within server to deliver 
    // invocation.
    protected EJBHome ejbHome;

    // Client reference to EJB Home.
    protected EJBHome ejbHomeStub;

    // Remote interface proxy class
    private Class ejbObjectProxyClass;

    // Remote interface proxy constructor.
    private Constructor ejbObjectProxyCtor;

    // RemoteReference Factory for RemoteHome view
    protected RemoteReferenceFactory remoteHomeRefFactory = null;

    //
    // Data members for 3.x Remote business view
    //

    // Internal interface describing operation used to create an
    // instance of a remote business object. 
    protected Class remoteBusinessHomeIntf = null;

    // Container implementation of internal EJB Business Home. May or may
    // not be same object as ejbRemoteBusinessHome, for example in the 
    // case of dynamic proxies.
    protected EJBHomeImpl ejbRemoteBusinessHomeImpl;

    // EJB Remote Business Home reference used by ORB Tie within server 
    // to deliver invocation.
    protected EJBHome ejbRemoteBusinessHome;

    // Client reference to internal Remote EJB Business Home.  This is
    // only seen by internal EJB code that instantiates remote business
    // objects during lookups.
    protected EJBHome ejbRemoteBusinessHomeStub;



    // Holds information such as remote reference factory that are associated
    // with a particular remote business interface
    protected Map<String, RemoteBusinessIntfInfo> remoteBusinessIntfInfo
        = new HashMap<String, RemoteBusinessIntfInfo>();

    Map<Method, List<ScheduledTimerDescriptor>> schedules =
            new HashMap<Method, List<ScheduledTimerDescriptor>>();

    //
    // END -- Data members for Remote views    
    // 

    protected EJBMetaData metadata = null;

    protected com.sun.enterprise.security.SecurityManager securityManager;
    
    protected boolean isSession;
    protected boolean isStatelessSession;
    protected boolean isStatefulSession;
    protected boolean isMessageDriven;
    protected boolean isEntity;
    protected boolean isSingleton;

    protected EjbDescriptor ejbDescriptor;
    protected String componentId; // unique id for java:comp namespace lookup
    
    
    protected Map invocationInfoMap = new HashMap();
    protected Map<TimerPrimaryKey, Method> scheduleIds = 
            new HashMap<TimerPrimaryKey, Method>();

    // Need a separate map for web service methods since it's possible for
    // an EJB Remote interface to be a subtype of the Service Endpoint
    // Interface.  In that case, it's ambiguous to do a lookup based only
    // on a java.lang.reflect.Method
    protected Map webServiceInvocationInfoMap = new HashMap();

    // optimized method map for proxies to resolve invocation info
    private MethodMap proxyInvocationInfoMap;

    protected Method[] ejbIntfMethods;
    protected InvocationInfo[] ejbIntfMethodInfo;

    protected Properties envProps;
    boolean isBeanManagedTran=false;
    
    
    protected boolean debugMonitorFlag = false;
    
    private static LocalStringManagerImpl localStrings = null;
    
    private ThreadLocal threadLocalContext = new ThreadLocal();

    protected static final int CONTAINER_INITIALIZING = -1;
    protected static final int CONTAINER_STARTED = 0;
    protected static final int CONTAINER_STOPPED = 1;
    protected static final int CONTAINER_UNDEPLOYED = 3;
    protected static final int CONTAINER_ON_HOLD = 4;
    
    protected int containerState = CONTAINER_INITIALIZING;
    
    protected int cmtTimeoutInSeconds = 0;

    protected HashMap			    methodMonitorMap;
    protected boolean			    monitorOn = false;
    protected MonitoringRegistryMediator    registryMediator;

    protected EJBMethodStatsManager	    ejbMethodStatsManager;
    protected EjbMonitoringStatsProvider    ejbProbeListener;
    protected EjbMonitoringProbeProvider    ejbProbeNotifier;
    protected EjbTimedObjectStatsProvider   timerProbeListener;
    protected EjbTimedObjectProbeProvider   timerProbeNotifier;
    protected EjbPoolStatsProvider          poolProbeListener;
    protected EjbCacheProbeProvider         cacheProbeNotifier;
    protected EjbCacheStatsProvider         cacheProbeListener;

    protected ContainerInfo                 containerInfo;
        
    private String _debugDescription;

    //protected TimedObjectMonitorableProperties toMonitorProps = null;
    
    //protected Agent callFlowAgent;
    
    protected CallFlowInfo callFlowInfo;

    protected InterceptorManager interceptorManager;

    protected static final Class[] lifecycleCallbackAnnotationClasses = {
        PostConstruct.class, PrePassivate.class,
        PostActivate.class, PreDestroy.class
    };
    
    private Set<Class> monitoredGeneratedClasses = new HashSet<Class>();

    protected InvocationManager invocationManager;

    protected InjectionManager injectionManager;

    protected GlassfishNamingManager namingManager;

    protected JavaEETransactionManager transactionManager;

    private EjbInvocationFactory invFactory;

    private ProtocolManager protocolMgr;

    protected EjbContainerUtil ejbContainerUtilImpl = EjbContainerUtilImpl.getInstance();

    protected EjbOptionalIntfGenerator optIntfClassLoader;

    private Set<String> publishedPortableGlobalJndiNames = new HashSet<String>();

    private Set<String> publishedNonPortableGlobalJndiNames = new HashSet<String>();

    private Set<String> publishedInternalGlobalJndiNames = new HashSet<String>();


    private Map<String, JndiInfo> jndiInfoMap = new HashMap<String, JndiInfo>();

    private String optIntfClassName;

    // Used to track whether we've done the base container cleanup (JNDI entries, etc.)
    // Only.  Not applicable to concrete containers.
    private boolean baseContainerCleanupDone = false;

    // True if there is at least one asynchronous method exposed from the bean.
    private boolean hasAsynchronousInvocations = false;

    // Information about a web service ejb endpoint.  Used as a conduit
    // between webservice runtime and ejb container.  Contains a Remote
    // servant used by jaxrpc to call web service business method.
    private WebServiceEndpoint webServiceEndpoint;

    //The Webservices Ejb Endpoint Registry contract
    // used to register and unregister ejb webservices endpoints
    private WSEjbEndpointRegistry wsejbEndpointRegistry;

    /**
     * This constructor is called from ContainerFactoryImpl when an
     * EJB Jar is deployed.
     */
    protected BaseContainer(ContainerType type, EjbDescriptor ejbDesc, ClassLoader loader)
        throws Exception
    {
        this.containerType = type;
        
        try {
            this.loader = loader;
            this.ejbDescriptor = ejbDesc;
            //this.callFlowAgent = ejbContainerUtilImpl.getCallFlowAgent();

            logParams = new Object[1];
            logParams[0] =  ejbDesc.getName();
            invocationManager = ejbContainerUtilImpl.getInvocationManager();
            injectionManager = ejbContainerUtilImpl.getInjectionManager();
            namingManager = ejbContainerUtilImpl.getGlassfishNamingManager();
            transactionManager = ejbContainerUtilImpl.getTransactionManager();
            
            // get Class objects for creating new EJBs
            ejbClass = loader.loadClass(ejbDescriptor.getEjbImplClassName());
            
            IASEjbExtraDescriptors iased = ejbDesc.getIASEjbExtraDescriptors();
            cmtTimeoutInSeconds = iased.getCmtTimeoutInSeconds();


            if( ejbDescriptor.getType().equals(EjbMessageBeanDescriptor.TYPE) )
            {
                assertFullProfile("is a Message-Driven Bean");

                isMessageDriven = true;
                EjbMessageBeanDescriptor mdb =
                (EjbMessageBeanDescriptor) ejbDescriptor;

                if ( mdb.getTransactionType().equals("Bean") ) {
                    isBeanManagedTran = true;
                }
                else {
                    isBeanManagedTran = false;
                }

                // TODO Temporary workaround to instantiate the ORB and Remote naming manager
                // to allow client lookups of JMS queues/topics/connectionfactories
                // For V3 FCS, there will be a sniffer listening on the naming port that will
                // instantiate the orb/remote naming service on demand upon initial access.
                // Once that's available, this call will be removed.
                initializeProtocolManager();
            }
            else {
                
                if(ejbDescriptor.getType().equals(EjbEntityDescriptor.TYPE)) {
                    assertFullProfile("is an Entity Bean");
                    isEntity = true;
                } else {
                    isSession = true;
                    EjbSessionDescriptor sd = (EjbSessionDescriptor) ejbDescriptor;

                    if( !sd.isSessionTypeSet() ) {
                        throw new RuntimeException("Invalid ejb Descriptor. Session type not set for "
                           + " ejb " + sd.getName() + " : " + sd);                       
                    }

                    if (sd.isSingleton()) {
                        isSingleton = true;
                    } else {
                        isStatelessSession = sd.isStateless();
                        isStatefulSession  = !isStatelessSession;

                        if( isStatefulSession ) {

                            /**
                             * If bean class isn't explicitly marked Serializable, generate
                             * a subclass that is.   We do this with a generator that uses
                             * ASM directly instead of the CORBA codegen library since none
                             * of the corba .jars are part of the Web Profile.
                             */
                            if( !Serializable.class.isAssignableFrom(ejbClass) ) {

                                sfsbSerializedClass = EJBUtils.loadGeneratedSerializableClass(ejbClass.getClassLoader(),
                                        ejbClass.getName());
                            }

                        }
                    }
                    if ( sd.getTransactionType().equals("Bean") ) {
                        isBeanManagedTran = true;
                    } else {
                        isBeanManagedTran = false;
                    }

                    hasAsynchronousInvocations = sd.hasAsynchronousMethods();

                    if( hasAsynchronousInvocations ) {
                        assertFullProfile("defines asynchronous session bean methods");
                    }


                }

                if ( ejbDescriptor.isRemoteInterfacesSupported() ||
                     ejbDescriptor.isRemoteBusinessInterfacesSupported() ) {

                    assertFullProfile("exposes a Remote client view");

                    initializeProtocolManager();

                }

                if ( ejbDescriptor.isRemoteInterfacesSupported() ) {

                    isRemote = true;
                    hasRemoteHomeView = true;

                    String homeClassName = ejbDescriptor.getHomeClassName();

                    homeIntf = loader.loadClass(homeClassName);
                    remoteIntf = loader.loadClass
                        (ejbDescriptor.getRemoteClassName());

                    String id = 
                        Long.toString(ejbDescriptor.getUniqueId()) + "_RHome";

                    remoteHomeRefFactory =
                        getProtocolManager().getRemoteReferenceFactory(this, true, id);

                }
                
                if( ejbDescriptor.isRemoteBusinessInterfacesSupported() ) {
                    
                    isRemote = true;
                    hasRemoteBusinessView = true;

                    remoteBusinessHomeIntf = 
                        EJBUtils.loadGeneratedGenericEJBHomeClass(loader);

                    for(String next : 
                            ejbDescriptor.getRemoteBusinessClassNames()) {

                        // The generated remote business interface and the
                        // client wrapper for the business interface are 
                        // produced dynamically.  The following call must be 
                        // made before any EJB 3.0 Remote business interface 
                        // runtime behavior is needed for a particular
                        // classloader.
                        EJBUtils.loadGeneratedRemoteBusinessClasses
                            (loader, next);
                        
                        String nextGen =
                            EJBUtils.getGeneratedRemoteIntfName(next);

                        Class genRemoteIntf = loader.loadClass(nextGen);

                        RemoteBusinessIntfInfo info = 
                            new RemoteBusinessIntfInfo();
                        info.generatedRemoteIntf = genRemoteIntf;
                        info.remoteBusinessIntf  = loader.loadClass(next);

                        // One remote reference factory for each remote 
                        // business interface.  Id must be unique across
                        // all ejb containers.
                        String id = Long.toString(ejbDescriptor.getUniqueId()) 
                             + "_RBusiness" + "_" + genRemoteIntf.getName();

                        info.referenceFactory = getProtocolManager().
                            getRemoteReferenceFactory(this, false, id);

                        remoteBusinessIntfInfo.put(genRemoteIntf.getName(),
                                                   info);
                        
                        addToGeneratedMonitoredMethodInfo(nextGen, genRemoteIntf); 
                    }

                }

                if ( ejbDescriptor.isLocalInterfacesSupported() ) {
                    // initialize class objects for LocalHome/LocalIntf etc.
                    isLocal = true;
                    hasLocalHomeView = true;

                    String localHomeClassName = 
                        ejbDescriptor.getLocalHomeClassName();

                    localHomeIntf = 
                        loader.loadClass(localHomeClassName);
                    localIntf = loader.loadClass
                        (ejbDescriptor.getLocalClassName());
                }

                if( ejbDescriptor.isLocalBusinessInterfacesSupported() ) {
                    isLocal = true;
                    hasLocalBusinessView = true;

                    localBusinessHomeIntf = GenericEJBLocalHome.class;

                    for(String next :
                            ejbDescriptor.getLocalBusinessClassNames() ) {
                        Class clz = loader.loadClass(next);
                        localBusinessIntfs.add(clz);
                        addToGeneratedMonitoredMethodInfo(next, clz);
                    }
                }

                if( ejbDescriptor.isLocalBean() ) {
                    isLocal = true;
                    hasOptionalLocalBusinessView = true;

                    ejbOptionalLocalBusinessHomeIntf = GenericEJBLocalHome.class;
                    Class clz = loader.loadClass(ejbDescriptor.getEjbClassName());
                    addToGeneratedMonitoredMethodInfo(ejbDescriptor.getEjbClassName(), clz);

                    this.optIntfClassName = EJBUtils.getGeneratedOptionalInterfaceName(ejbClass.getName());
                    optIntfClassLoader = new EjbOptionalIntfGenerator(loader);
                    ((EjbOptionalIntfGenerator) optIntfClassLoader).generateOptionalLocalInterface(ejbClass, optIntfClassName);
                    ejbGeneratedOptionalLocalBusinessIntfClass = optIntfClassLoader.loadClass(optIntfClassName);
                }

                if( isStatelessSession || isSingleton ) {
                    EjbBundleDescriptor bundle =
                        ejbDescriptor.getEjbBundleDescriptor();
                    WebServicesDescriptor webServices = bundle.getWebServices();
                    Collection endpoints =
                        webServices.getEndpointsImplementedBy(ejbDescriptor);
                    // JSR 109 doesn't require support for a single ejb
                    // implementing multiple port ex.
                    if( endpoints.size() == 1 ) {

                        assertFullProfile("is a Web Service Endpoint");

                        WebServiceEndpoint endpoint = (WebServiceEndpoint)
                            endpoints.iterator().next();
                        webServiceEndpointIntf = loader.loadClass
                           (ejbDescriptor.getWebServiceEndpointInterfaceName());
                        isWebServiceEndpoint = true;
                    }
                }


                try{
                    // get Method objects for ejbPassivate/Activate/ejbRemove
                    ejbPassivateMethod = 
                        ejbClass.getMethod("ejbPassivate", NO_PARAMS);
                    ejbActivateMethod = 
                        ejbClass.getMethod("ejbActivate", NO_PARAMS);
                    ejbRemoveMethod = 
                        ejbClass.getMethod("ejbRemove", NO_PARAMS);
                } catch(NoSuchMethodException nsme) {
                    // ignore.  Will happen for EJB 3.0 session beans
                }

                
            }
            
            if ( ejbDescriptor.isTimedObject() ) {

                assertFullProfile("uses the EJB Timer Service");

                MethodDescriptor ejbTimeoutMethodDesc = 
                    ejbDescriptor.getEjbTimeoutMethod();
                // Can be a @Timeout or @Schedule or TimedObject
                if (ejbTimeoutMethodDesc != null) {
                    Method method = ejbTimeoutMethodDesc.getMethod(ejbDescriptor);
                    processEjbTimeoutMethod(method);

                    ejbTimeoutMethod = method;
                }


                for (ScheduledTimerDescriptor schd : ejbDescriptor.getScheduledTimerDescriptors()) {
                    Method method = schd.getTimeoutMethod().getMethod(ejbDescriptor);
                    if (method == null) {
                        // This should've been caught in EjbBundleValidator
                        throw new EJBException("Class " + ejbClass.getName() +
                            " does not define timeout method " + schd.getTimeoutMethod().getFormattedString());
                    }

                    if( _logger.isLoggable(Level.FINE) ) {
	                _logger.log(Level.FINE, "... processing " + method );
                    }
                    processEjbTimeoutMethod(method);

                    List<ScheduledTimerDescriptor> list = schedules.get(method);
                    if (list == null) {
                        list = new ArrayList<ScheduledTimerDescriptor>();
                        schedules.put(method, list);
                    }
                    list.add(schd);
                }
                
            }
            if( isTimedObject_ ) {
                if( !isStatefulSession ) {
                    // EJBTimerService should be accessed only if needed 
                    // not to cause it to be loaded if it's not used.
                    EJBTimerService timerService = 
                        ejbContainerUtilImpl.getEJBTimerService();
                    if( timerService != null ) {
                        timerService.timedObjectCount();
                    }
                } else {
                    isTimedObject_ = false;
                    throw new EJBException("Ejb " + ejbDescriptor.getName() +
                        " is invalid. Stateful session ejbs can not" +
                        " be Timed Objects");
                }
            }

            preInitialize(ejbDesc, loader);
            
            initializeEjbInterfaceMethods();

            if( needSystemInterceptorProxy() ) {
                addSystemInterceptorProxy();
            }

            // NOTE : InterceptorManager initialization delayed until transition to START state.
            
            initializeInvocationInfo();

            setupEnvironment();

        } catch (Exception ex) {
            _logger.log(Level.FINE,"ejb.basecontainer_exception",logParams);
            _logger.log(Level.FINE,"", ex);
            throw ex;
        }

	_debugDescription = "ejbName: " + ejbDescriptor.getName()
		+ "; containerId: " + ejbDescriptor.getUniqueId();
	_logger.log(Level.FINE, "Instantiated container for: "
		+ _debugDescription);
    }

    protected ProtocolManager getProtocolManager() {
    	return protocolMgr;
    }
    
    public ContainerType getContainerType() {
        return containerType;
    }


    protected void doEJBHomeRemove(Object pk, Method m, boolean isLocal)
        throws RemoteException, RemoveException {
        throw new UnsupportedOperationException("EJBHome.remove() called on non entity container");
    }

    private void addToGeneratedMonitoredMethodInfo(String qualifiedClassName,
            Class generatedClass) {
        monitoredGeneratedClasses.add(generatedClass);
    }
    
    private void initializeProtocolManager() {

        try {

            GlassFishORBHelper orbHelper = ejbContainerUtilImpl.getORBHelper();
            protocolMgr = orbHelper.getProtocolManager();

        } catch(Throwable t) {
            throw new RuntimeException("IIOP Protocol Manager initialization failed.  " +
            "Possible cause is that ORB is not available in this " + 
            ((ejbContainerUtilImpl.isEmbeddedServer())? 
                    "embedded container, or server instance is running and required ports are in use" : 
                    "container")
            , t );
        }

    }
    
    protected void preInitialize(EjbDescriptor ejbDesc, ClassLoader loader) {
        //Overridden in sub classes
    }

    public void checkUserTransactionLookup(ComponentInvocation inv)
        throws javax.naming.NameNotFoundException {
        if (! this.isBeanManagedTran) {
            throw new javax.naming.NameNotFoundException("Lookup of java:comp/UserTransaction "
                    + "not allowed for Container managed Transaction beans");
        }
    }

    protected final void createCallFlowAgent(ComponentType compType) {
        
        this.callFlowInfo = new CallFlowInfoImpl(
                this, ejbDescriptor, compType);
    }

    public String toString() {
	return _debugDescription;
    }

    public final void setStartedState() {

        if( containerState == CONTAINER_STARTED ) {
            return;
        }

         // NOTE : we used to initialize interceptor manager in the ctor but we need to delay
         // the initialization to account for the possiblity of a 299-enabled app.  In
         // that case, the 299-defined ejb interceptors are not added until the
         // deployment load() phase.   That's ok, as long as everything is initialized
         // before any bean instances are created or any ejb invocations take place.
         // Therefore, moving the initialization to the point that we transition into the
         // ejb container START state.

        try {

            initializeInterceptorManager();

            for(Object o : invocationInfoMap.values()) {
                InvocationInfo next = (InvocationInfo) o;
                setInterceptorChain(next);
            }
            for(Object o : this.webServiceInvocationInfoMap.values()) {
                InvocationInfo next = (InvocationInfo) o;
                setInterceptorChain(next);
            }

        } catch(Exception e) {
            throw new RuntimeException(e);
        }

        containerState = CONTAINER_STARTED;
    }

    private void setInterceptorChain(InvocationInfo info) {
        if( info.aroundMethod != null ) {
            MethodDescriptor md = new MethodDescriptor(info.aroundMethod, MethodDescriptor.EJB_BEAN);
            if (info.isEjbTimeout) {
                info.interceptorChain =
                        interceptorManager.getAroundTimeoutChain(md, info.aroundMethod);
            } else {
                info.interceptorChain =
                        interceptorManager.getAroundInvokeChain(md, info.aroundMethod);
            }
        }
    }
    
    public final void setStoppedState() {
        containerState = CONTAINER_STOPPED;
    }

    public final boolean isStopped() {
        return containerState == CONTAINER_STOPPED;
    }

    public final void setUndeployedState() {
        containerState = CONTAINER_UNDEPLOYED;
    }

    public final boolean isUndeployed() {
	    return (containerState == CONTAINER_UNDEPLOYED);
    }

    public final boolean isTimedObject() {
        return isTimedObject_;
    }
    
    final boolean isBeanManagedTx() {
        return isBeanManagedTran;
    }

    public final ClassLoader getContainerClassLoader() {
        return loader;
    }
    
    public final ClassLoader getClassLoader() {
        return loader;
    }

    
    
    final long getContainerId() {
        return ejbDescriptor.getUniqueId();
    }
    
    
    public final EjbDescriptor getEjbDescriptor() {
        return ejbDescriptor;
    }
    
    /**
     * Method defined on JavaEEContainer
     */
    public final Descriptor getDescriptor() {
        return getEjbDescriptor();
    }

    public final EJBMetaData getEJBMetaData() {
        return metadata;
    }
    
    final UserTransaction getUserTransaction() {
        // Only session beans with bean-managed transactions
        // or message-driven beans with bean-managed transactions
        // can programmatically demarcate transactions.
        if ( (isSession || isMessageDriven) && isBeanManagedTran ) {
            try {
                UserTransaction utx = (UserTransaction)
                namingManager.getInitialContext().lookup(USER_TX);
                return utx;
            } catch ( Exception ex ) {
                _logger.log(Level.FINE, "ejb.user_transaction_exception", ex);
                throw new EJBException("Unable to lookup UserTransaction", ex);
            }
        }
        else
            throw new IllegalStateException(
                "ERROR: only SessionBeans with bean-managed transactions" + 
                "can obtain UserTransaction");
        
    }
    
    public boolean isHAEnabled() {
        return false;
    }
    
    /**
     * EJB spec makes a distinction between access to the UserTransaction
     * object itself and access to its methods.  getUserTransaction covers
     * the first check and this method covers the second.  It is called
     * by the UserTransaction implementation to verify access.
     */
    public boolean userTransactionMethodsAllowed(ComponentInvocation inv) {
        // Overridden by containers that allowed BMT;
        return false;
    }
    
    public final EJBHome getEJBHomeStub() {
        return ejbHomeStub;
    }
    
    public final EJBHome getEJBHome() {
        return ejbHome;
    }

    /**
     * Return an object that implements ejb's local home interface.
     * If dynamic proxies are being used, this is the proxy itself,
     * it can't be directly cast to an EJBLocalHomeImpl.
     */
    public final EJBLocalHome getEJBLocalHome() {
        return ejbLocalHome;
    }

    /**
     * Return an object that implements ejb's local business home interface.
     */
    public final GenericEJBLocalHome getEJBLocalBusinessHome(String clientViewClassName) {

        return isLocalBeanClass(clientViewClassName)
            ? ejbOptionalLocalBusinessHome
            : ejbLocalBusinessHome;
    }

    boolean isLocalBeanClass(String className) {

        return hasOptionalLocalBusinessView &&
                ( className.equals(ejbClass.getName()) ||
                  className.equals(ejbGeneratedOptionalLocalBusinessIntfClass.getName()) );
    }

    public final Class getEJBClass() {
        return ejbClass;
    }
    
    public final com.sun.enterprise.security.SecurityManager getSecurityManager() {
        return securityManager;
    }
    
    public final void setSecurityManager(com.sun.enterprise.security.SecurityManager sm)
        throws Exception
    {
        securityManager = sm;
    }
    
    final Properties getEnvironmentProperties() {
        return envProps;
    }
    
    /**
     * Create an EJBObject reference from the instanceKey
     *	Called from EJBObjectOutputStream.SerializableRemoteRef
     *	during deserialization of a remote-ref
     * @param instanceKey instanceKey of the ejbobject
     * @param generatedRemoteBusinessIntf non-null, this is a remote business view and the param
     *           is the name of the generated remote business interface.
     *           Otherwise, this is for the RemoteHome view
     */
    public java.rmi.Remote createRemoteReferenceWithId
        (byte[] instanceKey, String generatedRemoteBusinessIntf) {
                                                     
        final Thread currentThread = Thread.currentThread();
        final ClassLoader previousClassLoader =
            currentThread.getContextClassLoader();
        final ClassLoader myClassLoader = loader;
	try {
            if(System.getSecurityManager() == null) {
                currentThread.setContextClassLoader(myClassLoader);
            } else {
                java.security.AccessController.doPrivileged(
                        new java.security.PrivilegedAction() {
                    public java.lang.Object run() {
                        currentThread.setContextClassLoader(myClassLoader);
                        return null;
                    }
                });
            }
            java.rmi.Remote remoteRef = null;
            if( generatedRemoteBusinessIntf == null ) {
                remoteRef = remoteHomeRefFactory.createRemoteReference
                    (instanceKey);
            } else {
                RemoteReferenceFactory remoteBusinessRefFactory =
                   remoteBusinessIntfInfo.get(generatedRemoteBusinessIntf).
                    referenceFactory;

                remoteRef = remoteBusinessRefFactory.createRemoteReference
                    (instanceKey);
            }
            return remoteRef;
        } finally {
            if(System.getSecurityManager() == null) {
                currentThread.setContextClassLoader(previousClassLoader);
            } else {
                java.security.AccessController.doPrivileged(
                        new java.security.PrivilegedAction() {
                    public java.lang.Object run() {
                        currentThread.setContextClassLoader(previousClassLoader);
                        return null;
                    }
                });
            }
        }
    }


    private void assertFullProfile(String description) {

        if( ejbContainerUtilImpl.isEJBLite() ) {
            throw new RuntimeException("Invalid application.  EJB " +
                    ejbDescriptor.getName() + " " + description
                    + ". This feature is not part of the EJB 3.1 Lite API");
        }
    }


    /**
     * Called from the ContainerFactory during initialization.
     */
    void initializeHome()
        throws Exception
    {

        if( isWebServiceEndpoint ) {

            EjbBundleDescriptor bundle =
                ejbDescriptor.getEjbBundleDescriptor();
            WebServicesDescriptor webServices = bundle.getWebServices();
            Collection myEndpoints =
                webServices.getEndpointsImplementedBy(ejbDescriptor);


            // An ejb can only be exposed through 1 web service endpoint
            Iterator iter = myEndpoints.iterator();
            webServiceEndpoint =
 					(com.sun.enterprise.deployment.WebServiceEndpoint) iter.next();

            Class serviceEndpointIntfClass =
                    loader.loadClass(webServiceEndpoint.getServiceEndpointInterface());

            if (!serviceEndpointIntfClass.isInterface()) {
                ServiceInterfaceGenerator generator = new ServiceInterfaceGenerator(loader, ejbClass);
                serviceEndpointIntfClass = EJBUtils.generateSEI(generator,  generator.getGeneratedClass(),
                        loader, this.ejbClass);
                if (serviceEndpointIntfClass==null) {
                    throw new RuntimeException("Error generating the SEI");
                }

            }

            Class tieClass=null;

            WebServiceInvocationHandler invocationHandler =
                    new WebServiceInvocationHandler(ejbClass, webServiceEndpoint, serviceEndpointIntfClass,
                                                    ejbContainerUtilImpl,webServiceInvocationInfoMap);


            invocationHandler.setContainer(this);
            Object servant = (Object) Proxy.newProxyInstance
                    (loader, new Class[] { serviceEndpointIntfClass },
                    invocationHandler);

            // starting in 2.0, there is no more generated Ties
            if (webServiceEndpoint.getTieClassName()!=null) {
                tieClass = loader.loadClass(webServiceEndpoint.getTieClassName());
            }

            // Create a facade for container services to be used by web services runtime.
            EjbEndpointFacade endpointFacade =
                        new EjbEndpointFacadeImpl(this, ejbContainerUtilImpl);


            wsejbEndpointRegistry = Globals.getDefaultHabitat().getComponent(
                    WSEjbEndpointRegistry.class);
            if (wsejbEndpointRegistry != null ) {
                wsejbEndpointRegistry.registerEndpoint(webServiceEndpoint,endpointFacade,servant,tieClass);
            } else {
                throw new DeploymentException("EJB based Webservice endpoint is detected but there is" +
                        "no webservices module installed to handle it \n" );

            }


        }

        Map<String, Object> intfsForPortableJndi = new HashMap<String, Object>();

        // Root of portable global JNDI name for this bean
        String javaGlobalName = getJavaGlobalJndiNamePrefix();

        if (isRemote) {

            // This is either the default glassfish-specific (non-portable)
            // global JNDI name or the one specified via mappedName(), sun-ejb-jar.xml,
            // etc.
            String glassfishSpecificJndiName = ejbDescriptor.getJndiName();

            // If the explicitly specified name is the same as the portable name,
            // don't register any of the glassfish-specific names to prevent
            // clashes. 
            if( (glassfishSpecificJndiName != null) &&
                    ( glassfishSpecificJndiName.equals("") ||
                      glassfishSpecificJndiName.equals(javaGlobalName) ) ) {
                glassfishSpecificJndiName = null;
            }

            
            if( hasRemoteHomeView ) {
                this.ejbHomeImpl = instantiateEJBHomeImpl();
                this.ejbHome = ejbHomeImpl.getEJBHome();           

                // Since some containers might create multiple EJBObjects for
                // the same ejb, make sure we use the same Proxy class to 
                // instantiate all the proxy instances.              
                ejbObjectProxyClass = 
                    Proxy.getProxyClass(loader, new Class[] { remoteIntf });
                ejbObjectProxyCtor = ejbObjectProxyClass.
                    getConstructor(new Class[] { InvocationHandler.class });

                //
                // Make sure all Home/Remote interfaces conform to RMI-IIOP
                // rules.  Checking for conformance here keeps the exposed 
                // deployment/startup error behavior consistent since when 
                // rmic is used during codegen it makes equivalent checks and
                // treats any validation problems as fatal errors. 
                //
                // These same checks will be made when setTarget is called
                // in POARemoteReferenceFactory.preinvoke, but that happens
                // only when the actual invocation is made, so it's better to
                // know at container initialization time if there is a problem.
                //

                getProtocolManager().validateTargetObjectInterfaces(this.ejbHome);

                // Unlike the Home, each of the concrete containers are
                // responsible for creating the EJBObjects, so just create
                // a dummy EJBObjectImpl for validation purposes.
                EJBObjectImpl dummyEJBObjectImpl = instantiateEJBObjectImpl();
                EJBObject dummyEJBObject = (EJBObject)
                    dummyEJBObjectImpl.getEJBObject();
                getProtocolManager().validateTargetObjectInterfaces(dummyEJBObject);

                // Remotereference factory needs instances of
                // Home and Remote to get repository Ids since it doesn't have
                // stubs and ties.  This must be done before any Home or Remote
                // references are created.
                remoteHomeRefFactory.setRepositoryIds(homeIntf, remoteIntf);
                             
                // get a remote ref for the EJBHome
                ejbHomeStub = (EJBHome) remoteHomeRefFactory.createHomeReference(homeInstanceKey);

                // Add 2.x Home for later portable JNDI name processing.
                intfsForPortableJndi.put(ejbDescriptor.getHomeClassName(), ejbHomeStub);

                // If there's a glassfish-specific JNDI name, any 2.x Home object is always
                // regsitered under that name.  This preserves backward compatibility since
                // this was the original use of the jndi name.
                if( glassfishSpecificJndiName != null ) {

                    JndiInfo jndiInfo = JndiInfo.newNonPortableRemote(glassfishSpecificJndiName, ejbHomeStub);
                    jndiInfoMap.put(jndiInfo.name, jndiInfo);
                }

            }

            
            if( hasRemoteBusinessView ) {
                this.ejbRemoteBusinessHomeImpl = 
                    instantiateEJBRemoteBusinessHomeImpl();

                this.ejbRemoteBusinessHome = 
                    ejbRemoteBusinessHomeImpl.getEJBHome();


                // RMI-IIOP validation
                getProtocolManager().validateTargetObjectInterfaces(this.ejbRemoteBusinessHome);

                for(RemoteBusinessIntfInfo next : 
                        remoteBusinessIntfInfo.values()) {
                        
                    next.proxyClass = Proxy.getProxyClass
                        (loader, new Class[] { next.generatedRemoteIntf });
                        
                    next.proxyCtor = next.proxyClass.
                       getConstructor(new Class[] { InvocationHandler.class });

                    // Remotereference factory needs instances of
                    // Home and Remote to get repository Ids since it 
                    // doesn't have stubs and ties.  This must be done before 
                    // any Home or Remote references are created.
                    next.referenceFactory.setRepositoryIds
                        (remoteBusinessHomeIntf, next.generatedRemoteIntf);

                    // Create home stub from the remote reference factory
                    // associated with one of the remote business interfaces.
                    // It doesn't matter which remote reference factory is
                    // selected, so just do it the first time through the loop.
                    if( ejbRemoteBusinessHomeStub == null ) {
                        ejbRemoteBusinessHomeStub = (EJBHome) next.referenceFactory.
                            createHomeReference(homeInstanceKey);
                    }

                }

                EJBObjectImpl dummyEJBObjectImpl = instantiateRemoteBusinessObjectImpl();


                // Internal jndi name under which remote business home is registered for
                // glassfish-specific remote business JNDI names
                String remoteBusinessHomeJndiName = null;

                if( glassfishSpecificJndiName != null ) {

                    remoteBusinessHomeJndiName =
                        EJBUtils.getRemote30HomeJndiName(glassfishSpecificJndiName);
                }

                // Convenience location for common case of 3.0 session bean with only
                // 1 remote business interface and no adapted remote home.  Allows a
                // stand-alone client to access 3.0 business interface by using simple
                // jndi name.  Each remote business interface is also always available
                // at <jndi-name>#<business_interface_name>.  This is needed for the
                // case where the bean has an adapted remote home and/or multiple business
                // interfaces.
                String simpleRemoteBusinessJndiName = null;

                if( (glassfishSpecificJndiName != null) && !hasRemoteHomeView &&
                        remoteBusinessIntfInfo.size() == 1) {

                    simpleRemoteBusinessJndiName = glassfishSpecificJndiName;
                }


                // We need a separate name for the internal generated home object to
                // support the portable global JNDI names for business interfaces.
                // There won't necessarily be a glassfish-specific name specified so
                // it's cleaner to just always use a separate ones.
                String internalHomeJndiNameForPortableRemoteNames =
                    EJBUtils.getRemote30HomeJndiName(javaGlobalName);


                for(RemoteBusinessIntfInfo next : remoteBusinessIntfInfo.values()) {

                    java.rmi.Remote dummyEJBObject = dummyEJBObjectImpl.
                        getEJBObject(next.generatedRemoteIntf.getName());
                        
                    getProtocolManager().validateTargetObjectInterfaces(dummyEJBObject);

                    if( glassfishSpecificJndiName != null ) {

                        next.jndiName = EJBUtils.getRemoteEjbJndiName
                            (true, next.remoteBusinessIntf.getName(), glassfishSpecificJndiName);

                        Reference remoteBusRef = new Reference(next.remoteBusinessIntf.getName(),
                            new StringRefAddr("url", remoteBusinessHomeJndiName),
                            "com.sun.ejb.containers.RemoteBusinessObjectFactory", null);


                        // Glassfish-specific JNDI name for fully-qualified 3.0 Remote business interface.
                        JndiInfo jndiInfo = JndiInfo.newNonPortableRemote(next.jndiName, remoteBusRef);
                        jndiInfoMap.put(jndiInfo.name, jndiInfo);
                    }

                    if( simpleRemoteBusinessJndiName != null ) {

                        Reference remoteBusRef = new Reference
                            (next.remoteBusinessIntf.getName(),
                            new StringRefAddr("url", remoteBusinessHomeJndiName),
                            "com.sun.ejb.containers.RemoteBusinessObjectFactory", null);

                        // Glassfish-specific JNDI name for simple 3.0 Remote business interface lookup.
                        // Applicable when the bean exposes only a single Remote 3.x client view.
                        JndiInfo jndiInfo = JndiInfo.newNonPortableRemote
                                (simpleRemoteBusinessJndiName, remoteBusRef);
                        jndiInfoMap.put(jndiInfo.name, jndiInfo);
                    }

                    Reference remoteBusRef = new Reference(next.remoteBusinessIntf.getName(),
                            new StringRefAddr("url", internalHomeJndiNameForPortableRemoteNames),
                            "com.sun.ejb.containers.RemoteBusinessObjectFactory", null);

                    // Always register portable JNDI name for each remote business view
                    intfsForPortableJndi.put(next.remoteBusinessIntf.getName(), remoteBusRef);

                }


                if( remoteBusinessHomeJndiName != null ) {
                    // Glassfish-specific JNDI name for internal generated
                    // home object used by container
                    JndiInfo jndiInfo = JndiInfo.newNonPortableRemote
                        (remoteBusinessHomeJndiName, ejbRemoteBusinessHomeStub);
                    jndiInfo.setInternal(true);
                    jndiInfoMap.put(jndiInfo.name, jndiInfo);
                }

                // Always registeer internal name for home in support of portable global
                // remote business JNDI names.
                JndiInfo jndiInfo = JndiInfo.newPortableRemote
                        (internalHomeJndiNameForPortableRemoteNames, ejbRemoteBusinessHomeStub);
                jndiInfo.setInternal(true);
                jndiInfoMap.put(jndiInfo.name, jndiInfo);

                // If there isn't any jndi name from the descriptor, set one so the
                // lookup logic that depends on ejbDescriptor.getJndiName() will work.
                if( glassfishSpecificJndiName == null ) {
                    ejbDescriptor.setJndiName(javaGlobalName);
                }


            }
            
        }

        if (isLocal) {

            if( hasLocalHomeView ) {
                this.ejbLocalHomeImpl = instantiateEJBLocalHomeImpl();
                this.ejbLocalHome = ejbLocalHomeImpl.getEJBLocalHome();

                // Since some containers might create multiple EJBLocalObjects
                // for the same ejb, make sure we use the same Proxy class to 
                // instantiate all the proxy instances.  
                Class ejbLocalObjectProxyClass = 
                    Proxy.getProxyClass(loader, 
                                    new Class[] { IndirectlySerializable.class,
                                                  localIntf });
                ejbLocalObjectProxyCtor = ejbLocalObjectProxyClass.
                    getConstructor(new Class[] { InvocationHandler.class });

                // Portable JNDI name for EJB 2.x LocalHome.  We don't provide a
                // glassfish-specific way of accessing Local EJBs.

                JavaGlobalJndiNamingObjectProxy namingProxy =
                    new JavaGlobalJndiNamingObjectProxy(this, localHomeIntf.getName());

                intfsForPortableJndi.put(localHomeIntf.getName(), namingProxy);
            }

            if( hasLocalBusinessView ) {
                ejbLocalBusinessHomeImpl =
                    instantiateEJBLocalBusinessHomeImpl();
                ejbLocalBusinessHome = (GenericEJBLocalHome)
                    ejbLocalBusinessHomeImpl.getEJBLocalHome();

                Class[] proxyInterfaces =
                    new Class[ localBusinessIntfs.size() + 1 ];
                proxyInterfaces[0] = IndirectlySerializable.class;
                int index = 1;
                for(Class next : localBusinessIntfs) {
                    proxyInterfaces[index] = next;
                    index++;
                }

                Class proxyClass = Proxy.getProxyClass(loader,proxyInterfaces);
                ejbLocalBusinessObjectProxyCtor = proxyClass.
                    getConstructor(new Class[] { InvocationHandler.class });

                for (Class next : localBusinessIntfs) {
                    // Portable JNDI name for EJB 3.x Local business interface.
                    // We don't provide a glassfish-specific way of accessing Local EJBs.
                    JavaGlobalJndiNamingObjectProxy namingProxy =
                        new JavaGlobalJndiNamingObjectProxy(this, next.getName());
                    intfsForPortableJndi.put(next.getName(), namingProxy);
                }

            }

            if(hasOptionalLocalBusinessView) {
                EJBLocalHomeImpl obj = instantiateEJBOptionalLocalBusinessHomeImpl();
                ejbOptionalLocalBusinessHomeImpl = (EJBLocalHomeImpl) obj;

                ejbOptionalLocalBusinessHome = (GenericEJBLocalHome)
                    ejbOptionalLocalBusinessHomeImpl.getEJBLocalHome();

                Class[] proxyInterfaces =
                    new Class[ 2 ];
                proxyInterfaces[0] = IndirectlySerializable.class;
                String optionalIntfName = EJBUtils.getGeneratedOptionalInterfaceName(
                        ejbClass.getName());

                proxyInterfaces[1] = ejbGeneratedOptionalLocalBusinessIntfClass =
                        optIntfClassLoader.loadClass(optionalIntfName);

                Class proxyClass = Proxy.getProxyClass(loader,proxyInterfaces);
                ejbOptionalLocalBusinessObjectProxyCtor = proxyClass.
                    getConstructor(new Class[] { InvocationHandler.class });

                // Portable JNDI name for no-interface view.
                // We don't provide a glassfish-specific way of accessing the
                // no-interface view of a session bean.
                JavaGlobalJndiNamingObjectProxy namingProxy =
                    new JavaGlobalJndiNamingObjectProxy(this, ejbClass.getName());

                intfsForPortableJndi.put(ejbClass.getName(), namingProxy);

            }
            
        }



        for(String intf : intfsForPortableJndi.keySet()) {

            String fullyQualifiedJavaGlobalName = javaGlobalName + "!" + intf;
            Object namingProxy = intfsForPortableJndi.get(intf);
            boolean local = (namingProxy instanceof JavaGlobalJndiNamingObjectProxy);

            if (intfsForPortableJndi.size() == 1) {

                JndiInfo jndiInfo = local ?
                    JndiInfo.newPortableLocal(javaGlobalName, namingProxy) :
                    JndiInfo.newPortableRemote(javaGlobalName, namingProxy);
                jndiInfoMap.put(jndiInfo.name, jndiInfo);


            }

            JndiInfo jndiInfo = local ?
                JndiInfo.newPortableLocal(fullyQualifiedJavaGlobalName, namingProxy) :
                JndiInfo.newPortableRemote(fullyQualifiedJavaGlobalName, namingProxy);
            jndiInfoMap.put(jndiInfo.name, jndiInfo);

        }

        for(Map.Entry<String, JndiInfo> entry : jndiInfoMap.entrySet()) {
            JndiInfo jndiInfo = entry.getValue();

            try {

                jndiInfo.publish(this.namingManager);

                if( jndiInfo.internal ) {
                    publishedInternalGlobalJndiNames.add(jndiInfo.name);
                } else {

                    if( jndiInfo.portable ) {
                        publishedPortableGlobalJndiNames.add(jndiInfo.name);
                    }  else {
                        publishedNonPortableGlobalJndiNames.add(jndiInfo.name);
                    }
                }


            } catch(Exception e) {
                throw new RuntimeException("Error while binding JNDI name " + jndiInfo.name +
                    " for EJB : " + this.ejbDescriptor.getName(), e);

            }
           
        }

        if( !publishedPortableGlobalJndiNames.isEmpty() ) {
            _logger.log(Level.INFO, "Portable JNDI names for EJB " +
                        this.ejbDescriptor.getName() + " : " + publishedPortableGlobalJndiNames);
        }

        if( !publishedNonPortableGlobalJndiNames.isEmpty() ) {
            _logger.log(Level.INFO, "Glassfish-specific (Non-portable) JNDI names for EJB " +
                        this.ejbDescriptor.getName() + " : " + publishedNonPortableGlobalJndiNames);
        }

        if( !publishedInternalGlobalJndiNames.isEmpty() ) {
            _logger.log(Level.FINE, "Internal container JNDI names for EJB " +
                        this.ejbDescriptor.getName() + " : " + publishedInternalGlobalJndiNames);
        }
        
        // create EJBMetaData
        Class primaryKeyClass = null;
        if ( isEntity ) {
            EjbEntityDescriptor ed = (EjbEntityDescriptor)ejbDescriptor;
            primaryKeyClass = loader.loadClass(ed.getPrimaryKeyClassName());
        }
        metadata = new EJBMetaDataImpl(ejbHomeStub, homeIntf, remoteIntf,
            primaryKeyClass, isSession, isStatelessSession);
    }

    protected String getJavaGlobalJndiNamePrefix() {

        String appName = null;

        Application app = ejbDescriptor.getApplication();
        if ( ! app.isVirtual() ) {
            appName = ejbDescriptor.getApplication().getAppName();
        }

        EjbBundleDescriptor ejbBundle = ejbDescriptor.getEjbBundleDescriptor();
        String modName = ejbBundle.getModuleDescriptor().getModuleName();

        String ejbName = ejbDescriptor.getName();

        StringBuffer javaGlobalPrefix = new StringBuffer("java:global/");

        if (appName != null) {
            javaGlobalPrefix.append(appName);
            javaGlobalPrefix.append("/");
        }
        
        javaGlobalPrefix.append(modName);
        javaGlobalPrefix.append("/");
        javaGlobalPrefix.append(ejbName);


        return javaGlobalPrefix.toString();
    }

    protected void injectEjbInstance(Object instance, EJBContextImpl context) throws Exception {
        
        Habitat h = ejbContainerUtilImpl.getDefaultHabitat();

        JCDIService jcdiService = h.getByContract(JCDIService.class);

        EjbBundleDescriptor ejbBundle = ejbDescriptor.getEjbBundleDescriptor();

        Object[] interceptorInstances = null;

        if( (jcdiService != null) && jcdiService.isJCDIEnabled(ejbBundle)) {

            JCDIService.JCDIInjectionContext jcdiCtx =
                    jcdiService.injectEJBInstance(ejbDescriptor, instance);

            context.setJCDIInjectionContext(jcdiCtx);

            Class[] interceptorClasses = interceptorManager.getInterceptorClasses();

            interceptorInstances = new Object[interceptorClasses.length];

            for(int i = 0; i < interceptorClasses.length; i++) {
                // 299 impl will instantiate and inject the instance, but PostConstruct
                // is still our responsibility
                interceptorInstances[i] = jcdiService.createManagedObject(interceptorClasses[i],
                        ejbBundle, false).getInstance();
            }

            interceptorManager.initializeInterceptorInstances(interceptorInstances);

        } else {
            injectionManager.injectInstance(instance, ejbDescriptor, false);

            interceptorInstances = interceptorManager.createInterceptorInstances();

            for (Object interceptorInstance : interceptorInstances) {
				injectionManager.injectInstance(interceptorInstance,
						ejbDescriptor, false);
			}
        }

        context.setInterceptorInstances(interceptorInstances);

    }

    protected void cleanupInstance(EJBContextImpl context) {

        JCDIService.JCDIInjectionContext jcdiCtx = context.getJCDIInjectionContext();
        if( jcdiCtx != null ) {
            jcdiCtx.cleanup(false);
        }

    }
    
    /**
     * Return the EJBObject/EJBHome Proxy for the given ejbId and instanceKey.
     * Called from the ProtocolManager when a remote invocation arrives.
     * @exception NoSuchObjectLocalException if the target object does not exist
     */
    public java.rmi.Remote getTargetObject(byte[] instanceKey, 
                                          String generatedRemoteBusinessIntf) {
               
        externalPreInvoke();
        boolean remoteHomeView = (generatedRemoteBusinessIntf == null);
        if ( instanceKey.length == 1 && instanceKey[0] == HOME_KEY ) {
            return remoteHomeView ? 
                ejbHomeImpl.getEJBHome() :
                ejbRemoteBusinessHomeImpl.getEJBHome();
        } else {

            java.rmi.Remote targetObject = null;
            EJBObjectImpl ejbObjectImpl  = null;


            if( remoteHomeView ) {
                ejbObjectImpl = getEJBObjectImpl(instanceKey);
                // In rare cases for sfsbs and entity beans, this can be null.
                if( ejbObjectImpl != null ) {
                    targetObject = ejbObjectImpl.getEJBObject();
                }
            } else {
                ejbObjectImpl = getEJBRemoteBusinessObjectImpl(instanceKey);
                // In rare cases for sfsbs and entity beans, this can be null.
                if( ejbObjectImpl != null ) {
                    targetObject = ejbObjectImpl.
                        getEJBObject(generatedRemoteBusinessIntf);
                }
            }
            
            return targetObject;
        }
    }

    /**
     * Release the EJBObject/EJBHome object.
     * Called from the ProtocolManager after a remote invocation completes.
     */
    public void releaseTargetObject(java.rmi.Remote remoteObj) {
        externalPostInvoke();
    }

    public void externalPreInvoke() {
        BeanContext bc = new BeanContext();
        final Thread currentThread = Thread.currentThread();
        bc.previousClassLoader = currentThread.getContextClassLoader();
        if ( getClassLoader().equals(bc.previousClassLoader) == false ) {

	    if (System.getSecurityManager() == null) {
	        currentThread.setContextClassLoader( getClassLoader());
	    } else {
	        java.security.AccessController.doPrivileged(
			      new java.security.PrivilegedAction() {
		                  public java.lang.Object run() {
				      currentThread.setContextClassLoader( getClassLoader());
				      return null;
				  }
		});
	    }
            bc.classLoaderSwitched = true;
        }

        ArrayListStack beanContextStack = 
            (ArrayListStack) threadLocalContext.get();
                
        if ( beanContextStack == null ) {
            beanContextStack = new ArrayListStack();
            threadLocalContext.set(beanContextStack);
        } 
        beanContextStack.push(bc);
    }

    public void externalPostInvoke() {
        try {
            ArrayListStack beanContextStack = 
                (ArrayListStack) threadLocalContext.get();                
            
            final BeanContext bc = (BeanContext) beanContextStack.pop();
            if ( bc.classLoaderSwitched == true ) {
	            if (System.getSecurityManager() == null) {
		            Thread.currentThread().setContextClassLoader(bc.previousClassLoader);
		        } else {
		            java.security.AccessController.doPrivileged(
				        new java.security.PrivilegedAction() {
		                      public java.lang.Object run() {
					  Thread.currentThread().setContextClassLoader(
								    bc.previousClassLoader);
					  return null;
                              }});
		        }
            }
        } catch ( Exception ex ) {
            _logger.log(Level.FINE, "externalPostInvoke ex", ex);
        }
    }

    private boolean doPreInvokeAuthorization(EjbInvocation inv) {

        // preInvocation authorization does not apply if this is a timer callback
        // OR if it's a remove operation initiated via the 299 SPI
        boolean skipPreInvokeAuth = inv.isTimerCallback ||
                ( inv.isLocal &&
                  inv.method.equals(ejbIntfMethods[EJBLocalObject_remove]) &&
                  !((EJBLocalObjectImpl)inv.ejbObject).isLocalHomeView() );

       return !skipPreInvokeAuth;
    }


    /**
     * Called from EJBObject/EJBHome before invoking on EJB.
     * Set the EJB instance in the EjbInvocation.
     */
    public void preInvoke(EjbInvocation inv) {

        if( _logger.isLoggable(Level.FINE) ) {
            _logger.log(Level.FINE, "Entering BaseContainer::preInvoke : " + inv);
        }

        try {
            if (containerState != CONTAINER_STARTED) {
                throw new EJBException("Attempt to invoke when container is in "
                                       + containerStateToString(containerState));
            }
            
            if( inv.method == null ) {
                throw new EJBException("Attempt to invoke container with null " +
                                       " invocation method");
            }

            if( inv.invocationInfo == null ) {

                inv.invocationInfo = getInvocationInfo(inv);

                if( inv.invocationInfo == null ) {
                    throw new EJBException("EjbInvocation Info lookup failed for " +
                                           "method " + inv.method);
                }
            }

            inv.transactionAttribute = inv.invocationInfo.txAttr;
            inv.container = this;



            if (inv.mustInvokeAsynchronously()) {
                return;
            }

            if ( doPreInvokeAuthorization(inv) ) {
                if (! authorize(inv)) {
                    throw new AccessLocalException(
                        "Client not authorized for this invocation.");
                }
            }

            // Cache value of txManager.getStatus() in invocation to avoid
            // multiple thread-local accesses of that value during pre-invoke
            // stage.
            inv.setPreInvokeTxStatus(transactionManager.getStatus());

            ComponentContext ctx = getContext(inv);
            inv.context = ctx;
            
            inv.instance = inv.ejb = ctx.getEJB();
            InvocationInfo info = inv.invocationInfo;
            
            inv.useFastPath = (info.isTxRequiredLocalCMPField) && (inv.foundInTxCache);
            //    _logger.log(Level.INFO, "Use fastPath() ==> " + info.method);
            
            if (!inv.useFastPath) {
                // Sets thread-specific state for Transaction, Naming, Security,
                // etc
                invocationManager.preInvoke(inv);

                // Do Tx machinery
                preInvokeTx(inv);

                // null out invocation preInovkeTxStatus since the cache value
                // is obsolete
                inv.setPreInvokeTxStatus(null);

                enlistExtendedEntityManagers(ctx);
            }
            
            if (ejbMethodStatsManager.isMethodMonitorOn()) {
                ejbMethodStatsManager.preInvoke(inv.method);
            }
            
        }
        catch ( Exception ex ) {
            _logger.log(Level.FINE, "ejb.preinvoke_exception", logParams);
            _logger.log(Level.FINE, "", ex);
            
            EJBException ejbEx;
            if ( ex instanceof EJBException ) {
                ejbEx = (EJBException)ex;
            } else {
                ejbEx = new EJBException(ex);
            }
            
            throw new PreInvokeException(ejbEx);
        }
    }
    
    protected void enlistExtendedEntityManagers(ComponentContext ctx) {
        if (isStatefulSession && (ctx.getTransaction() != null)) {
            JavaEETransaction j2eeTx = (JavaEETransaction) ctx.getTransaction();
            SessionContextImpl sessionCtx = (SessionContextImpl) ctx;
            Map<EntityManagerFactory, EntityManager> entityManagerMap = 
                sessionCtx.getExtendedEntityManagerMap();
                    
            for (Map.Entry<EntityManagerFactory, EntityManager> entry : 
                     entityManagerMap.entrySet()) {
                EntityManagerFactory emf = entry.getKey();
                EntityManager extendedEm = entry.getValue();

                EntityManager extendedEmAssociatedWithTx = 
                    j2eeTx.getExtendedEntityManager(emf);

                // If there's not already an EntityManager registered for
                // this extended EntityManagerFactory within the current tx
                if (extendedEmAssociatedWithTx == null) {
                    j2eeTx.addExtendedEntityManagerMapping(emf,
                                                           extendedEm);
                    sessionCtx.setEmfRegisteredWithTx(emf, true);

                    // Tell persistence provider to associate the extended
                    // entity manager with the transaction.
                    // @@@ Comment this out when joinTransaction supported on
                    // EntityManager API.
                    extendedEm.joinTransaction();
                }
            }
        }
    }
    
    /**
     * Containers that allow extended EntityManager will override this method.
     */
    public EntityManager lookupExtendedEntityManager(EntityManagerFactory emf) {
        throw new IllegalStateException(
                "EntityManager with PersistenceContextType.EXTENDED " + 
                "is not supported for this bean type");
    }

    public void webServicePostInvoke(EjbInvocation inv) {
        // postInvokeTx is handled by WebServiceInvocationHandler.
        // Invoke postInvoke with instructions to skip tx processing portion.
        postInvoke(inv, false);
    }

    /**
     * Called from EJBObject/EJBHome after invoking on bean.
     */
    public void postInvoke(EjbInvocation inv) {
        postInvoke(inv, true);
    }

    protected void postInvoke(EjbInvocation inv, boolean doTxProcessing) {
        if (containerState != CONTAINER_STARTED) {
                throw new EJBException("Attempt to invoke when container is in "
                                       + containerStateToString(containerState));
        }

        inv.setDoTxProcessingInPostInvoke(doTxProcessing);
        if (inv.mustInvokeAsynchronously()) {
            EjbAsyncInvocationManager asyncManager =
                    ((EjbContainerUtilImpl) ejbContainerUtilImpl).getEjbAsyncInvocationManager();
            asyncManager.submit(inv);
            return;
        }

        if (ejbMethodStatsManager.isMethodMonitorOn()) {
            ejbMethodStatsManager.postInvoke(inv.method, inv.exception);
        }


        if ( inv.ejb != null ) {
            // counterpart of invocationManager.preInvoke
            if (! inv.useFastPath) {
                invocationManager.postInvoke(inv);
                if (isStatefulSession
                        && (((EJBContextImpl) inv.context).getTransaction() != null)) {

                    SessionContextImpl sessionCtx = (SessionContextImpl) inv.context;
                    JavaEETransaction j2eeTx = (JavaEETransaction) sessionCtx
                            .getTransaction();

                    Map<EntityManagerFactory, EntityManager> entityManagerMap = sessionCtx
                            .getExtendedEntityManagerMap();
                    for (EntityManagerFactory emf : entityManagerMap.keySet()) {

                        if (sessionCtx.isEmfRegisteredWithTx(emf)) {
                            j2eeTx.removeExtendedEntityManagerMapping(emf);
                            sessionCtx.setEmfRegisteredWithTx(emf, false);
                        }
                    }
                }
            } else {
                doTxProcessing = doTxProcessing && (inv.exception != null);
            }
            
            try {
                if( doTxProcessing ) {
                    postInvokeTx(inv);
                }
            } catch (Exception ex) {
                _logger.log(Level.FINE, "ejb.postinvoketx_exception", ex);
                if (ex instanceof EJBException)
                    inv.exception = (EJBException) ex;
                else
                    inv.exception = new EJBException(ex);
            }
            
            releaseContext(inv);
        }

        if ( inv.exception != null ) {
            
            // Unwrap the PreInvokeException if necessary
            if ( inv.exception instanceof PreInvokeException ) {
                inv.exception = ((PreInvokeException)inv.exception).exception;
            }

            // Log system exceptions by default and application exceptions only
            // when log level is FINE or higher.

            if( isSystemUncheckedException(inv.exception) ) {
                _logger.log(Level.WARNING, "A system exception occurred during an invocation on EJB " +
                                           ejbDescriptor.getName() + " method " + inv.beanMethod, inv.exception);
            } else {
                _logger.log(Level.FINE, "An application exception occurred during an invocation on EJB " +
                                           ejbDescriptor.getName() + " method " + inv.beanMethod, inv.exception);
            }
            
            if ( inv.isRemote ) {

                if( protocolMgr != null ) {
                    // For remote business case, exception mapping is performed
                    // in client wrapper.
                    // TODO need extra logic to handle implementation-specific ejb exceptions
                    // (ParallelAccessEXCeption etc. that used to be handled by iiop glue code
                    inv.exception = mapRemoteException(inv);
                }
                    
                // The most useful portion of the system exception is logged 
                // above.  Only log mapped form when log level is FINE or 
                // higher.
                _logger.log(Level.FINE, "", inv.exception);

            } else {

                if( inv.isBusinessInterface ) {
                    inv.exception = 
                        mapLocal3xException(inv.exception);
                }

            }

        }
        /*TODO
        if ( AppVerification.doInstrument()) {
            // need to pass the method, exception info,
            // and EJB descriptor to get app info
            AppVerification.getInstrumentLogger().doInstrumentForEjb(
            ejbDescriptor, inv.method, inv.exception);

        }
        */

        if( _logger.isLoggable(Level.FINE) ) {
            _logger.log(Level.FINE, "Leaving BaseContainer::postInvoke : " + inv);
        }
    }



    /**
     * Check if caller is authorized to invoke the method.
     * Only called for EJBLocalObject and EJBLocalHome methods,
     * from EJBLocalHome|ObjectImpl classes.
     * @param method an integer identifying the method to be checked,
     *		        must be one of the EJBLocal{Home|Object}_* constants.
     */
    void authorizeLocalMethod(int method) {

        EjbInvocation inv = invFactory.create();
        inv.isLocal = true;
        inv.isHome = EJB_INTF_METHODS_INFO[method];
        inv.method = ejbIntfMethods[method];
        inv.invocationInfo = ejbIntfMethodInfo[method];

        if ( !authorize(inv) ) {
            throw new AccessLocalException(
                "Client is not authorized for this invocation.");
        }
    }
    
    /**
     * Check if caller is authorized to invoke the method.
     * Only called for EJBObject and EJBHome methods,
     * from EJBHome|ObjectImpl classes.
     * @param method an integer identifying the method to be checked,
     *		        must be one of the EJB{Home|Object}_* constants.
     */
    void authorizeRemoteMethod(int method)
        throws RemoteException
    {
        EjbInvocation inv = invFactory.create();
        inv.isLocal = false;
        inv.isHome = EJB_INTF_METHODS_INFO[method];
        inv.method = ejbIntfMethods[method];
        inv.invocationInfo = ejbIntfMethodInfo[method];

        if ( !authorize(inv) ) {
            AccessException ex = new AccessException(
                "Client is not authorized for this invocation.");
            // TODO see note above about additional special exception handling needed
            Throwable t = mapRemoteException(inv);
            if ( t instanceof RuntimeException )
                throw (RuntimeException)t;
            else if ( t instanceof RemoteException )
                throw (RemoteException)t;
            else
                throw ex; // throw the AccessException
        }
    }

    /**
     * Check timeout method and set it accessable
     */
    private void processEjbTimeoutMethod(Method method) throws Exception {
        Class[] params = method.getParameterTypes();
        if( (params.length == 0 || 
            (params.length == 1 && params[0] == javax.ejb.Timer.class)) &&
            (method.getReturnType() == Void.TYPE) ) {
            
            isTimedObject_ = true;

            final Method ejbTimeoutAccessible = method;
            // Since timeout method can have any kind of access
            // setAccessible to true.
            if(System.getSecurityManager() == null) {
                if( !ejbTimeoutAccessible.isAccessible() ) {
                    ejbTimeoutAccessible.setAccessible(true);
                }
            } else {
                java.security.AccessController.doPrivileged(
                        new java.security.PrivilegedExceptionAction() {
                    public java.lang.Object run() throws Exception {
                        if( !ejbTimeoutAccessible.isAccessible() ) {
                            ejbTimeoutAccessible.setAccessible(true);
                        }
                        return null;
                    }
                });
            }
        } else {
            throw new EJBException
                ("Invalid @Timeout or @Schedule signature for " + 
                 method + " @Timeout or @Schedule method must return void" +
                 " and be a no-arg method or take a single javax.ejb.Timer param");
        }
    }

    /**
     * Encapsulate logic used to map invocation method to invocation info.
     * At present, we have two different maps, one for webservice invocation
     * info and one for everything else.  That might change in the future.
     */
    private InvocationInfo getInvocationInfo(EjbInvocation inv) {
        return inv.isWebService ?
            (InvocationInfo) webServiceInvocationInfoMap.get(inv.method) :
            (InvocationInfo) invocationInfoMap.get(inv.method);        
    }

    private Throwable mapRemoteException(EjbInvocation inv) {


        Throwable originalException = inv.exception;
        Throwable mappedException = originalException;

        // If it's an asnyc invocation and we're mapping an exception it
        // means this is the thread of execution.  The exception won't directly
        // flow over the wire as a remote exception from the orb's perspective.
        // If it's asychronous we know it's a remote business interface, not the
        // 2.x client view.
        if( inv.invocationInfo.isAsynchronous() ) {


            if( java.rmi.Remote.class.isAssignableFrom(inv.clientInterface) ) {
                mappedException = protocolMgr.mapException(originalException);

                if( mappedException == originalException) {

                    if( originalException instanceof EJBException ) {
                        mappedException = new RemoteException
                             (originalException.getMessage(),originalException);
                    }

                }
            } else {

                mappedException = mapLocal3xException(originalException);
                
            }

        } else {

            // Synchronous invocation.  First let the protocol manager perform
            // its mapping.

            mappedException = protocolMgr.mapException(originalException);

            // If no mapping happened
            if( mappedException == originalException) {


                if( inv.isBusinessInterface ) {

                    // Wrap it up in a special exception so the
                    // client can unwrap it and ensure that the client receives EJBException.
                    if(originalException instanceof EJBException) {
                        mappedException = new InternalEJBContainerException
                            (originalException.getMessage(), originalException);
                    }

                } else {
                    if( originalException instanceof EJBException ) {
                        mappedException = new RemoteException
                            (originalException.getMessage(), originalException);
                    }                
                }
            }
            
        }

        if( _logger.isLoggable(Level.FINE)) {

            _logger.log(Level.FINE, "Mapped original remote exception " +
                    originalException + " to exception " + mappedException +
                    " for " + inv);

        }

        return mappedException;
    }

    private Throwable mapLocal3xException(Throwable t) {

        Throwable mappedException = null;

        if( t instanceof TransactionRolledbackLocalException ) {
            mappedException = new EJBTransactionRolledbackException();
            mappedException.initCause(t);
        } else if( t instanceof TransactionRequiredLocalException ) {
            mappedException = new EJBTransactionRequiredException();
            mappedException.initCause(t);
        } else if( t instanceof NoSuchObjectLocalException ) {
            mappedException = new NoSuchEJBException();
            mappedException.initCause(t);
        } else if( t instanceof AccessLocalException ) {
            mappedException = new EJBAccessException();
            mappedException.initCause(t);
        }
        
        return (mappedException != null) ? mappedException : t;

    }

    /**
     * Common code to handle EJB security manager authorization call.
     */
    public boolean authorize(EjbInvocation inv) {

        // There are a few paths (e.g. authorizeLocalMethod, 
        // authorizeRemoteMethod, Ejb endpoint pre-handler )
        // for which invocationInfo is not set.  We get better
        // performance with the security manager on subsequent
        // invocations of the same method if invocationInfo is
        // set on the invocation.  However, the authorization
        // does not depend on it being set.  So, try to set
        // invocationInfo but in this case don't treat it as
        // an error if it's not available.  
        if( inv.invocationInfo == null ) {
            
            inv.invocationInfo = getInvocationInfo(inv);
                        
        }

        // Internal methods for 3.0 bean creation so there won't 
        // be corresponding permissions in the security policy file.  
        if( (inv.method.getDeclaringClass() == localBusinessHomeIntf) 
            ||
            (inv.method.getDeclaringClass() == remoteBusinessHomeIntf) ) {
            return true;
        }
       
        boolean authorized = securityManager.authorize(inv);
        
        if( !authorized ) {

            if( inv.context != null ) {
                // This means that an enterprise bean context was created
                // during the authorization call because of a callback from
                // a JACC enterprise bean handler. Since the invocation will 
                // not proceed due to the authorization failure, we need 
                // to release the enterprise bean context.
                releaseContext(inv);
            }
        }

        return authorized;
    }

    /**
     * Create an array of all methods in the standard EJB interfaces:
     * javax.ejb.EJB(Local){Home|Object} .
     */
    private void initializeEjbInterfaceMethods()
        throws Exception
    {
        ejbIntfMethods = new Method[EJB_INTF_METHODS_LENGTH];
        
        if ( isRemote ) {
            ejbIntfMethods[ EJBHome_remove_Handle ] =
                EJBHome.class.getMethod("remove",
                                    new Class[]{javax.ejb.Handle.class});
            ejbIntfMethods[ EJBHome_remove_Pkey ] =
                EJBHome.class.getMethod("remove",
                                        new Class[]{java.lang.Object.class});
            ejbIntfMethods[ EJBHome_getEJBMetaData ] =
                EJBHome.class.getMethod("getEJBMetaData", NO_PARAMS);
            ejbIntfMethods[ EJBHome_getHomeHandle ] =
                EJBHome.class.getMethod("getHomeHandle", NO_PARAMS);
            
            ejbIntfMethods[ EJBObject_getEJBHome ] =
                EJBObject.class.getMethod("getEJBHome", NO_PARAMS);
            ejbIntfMethods[ EJBObject_getPrimaryKey ] =
                EJBObject.class.getMethod("getPrimaryKey", NO_PARAMS);
            ejbIntfMethods[ EJBObject_remove ] =
                EJBObject.class.getMethod("remove", NO_PARAMS);
            ejbIntfMethods[ EJBObject_getHandle ] =
                EJBObject.class.getMethod("getHandle", NO_PARAMS);
            ejbIntfMethods[ EJBObject_isIdentical ] =
                EJBObject.class.getMethod("isIdentical",
            new Class[]{javax.ejb.EJBObject.class});
            
            if ( isStatelessSession ) {
                if( hasRemoteHomeView ) {
                    ejbIntfMethods[ EJBHome_create ] =
                        homeIntf.getMethod("create", NO_PARAMS);
                }
            }
        }
        
        if ( isLocal ) {
            ejbIntfMethods[ EJBLocalHome_remove_Pkey ] =
                EJBLocalHome.class.getMethod("remove",
                    new Class[]{java.lang.Object.class});
            
            ejbIntfMethods[ EJBLocalObject_getEJBLocalHome ] =
                EJBLocalObject.class.getMethod("getEJBLocalHome", NO_PARAMS);
            ejbIntfMethods[ EJBLocalObject_getPrimaryKey ] =
                EJBLocalObject.class.getMethod("getPrimaryKey", NO_PARAMS);
            ejbIntfMethods[ EJBLocalObject_remove ] =
                EJBLocalObject.class.getMethod("remove", NO_PARAMS);
            ejbIntfMethods[ EJBLocalObject_isIdentical ] =
                EJBLocalObject.class.getMethod("isIdentical",
            new Class[]{javax.ejb.EJBLocalObject.class});
            
            if ( isStatelessSession ) {
                if( hasLocalHomeView ) {
                    Method m = localHomeIntf.getMethod("create", NO_PARAMS);
                    ejbIntfMethods[ EJBLocalHome_create ] = m;
                }
            }
        }
        
    }
    
    private void destroyTimers() {
        if( isTimedObject() ) {
            // EJBTimerService should be accessed only if needed 
            // not to cause it to be loaded if it's not used.
            EJBTimerService ejbTimerService = ejbContainerUtilImpl.getEJBTimerService();
            if( ejbTimerService != null ) {
                ejbTimerService.destroyTimers(getContainerId());
            }
        }
    }
    
    // internal API, implemented in subclasses
    abstract EJBObjectImpl createEJBObjectImpl()
        throws CreateException, RemoteException;

    // Only applies to concrete session containers
    EJBObjectImpl createRemoteBusinessObjectImpl() throws CreateException, 
        RemoteException
    {
        throw new EJBException(
            "Internal ERROR: BaseContainer.createRemoteBusinessObject called");
    }
    
    // internal API, implemented in subclasses
    EJBLocalObjectImpl createEJBLocalObjectImpl()
        throws CreateException
    {
        throw new EJBException(
            "Internal ERROR: BaseContainer.createEJBLocalObject called");
    }

    // Only implemented in Stateless , Stateful, and Singleton session containers
    EJBLocalObjectImpl createEJBLocalBusinessObjectImpl(boolean localBeanView)
        throws CreateException
    {
        throw new EJBException(
            "Internal ERROR: BaseContainer.createEJBLocalBusinessObject called");
    }

    EJBLocalObjectImpl createEJBLocalBusinessObjectImpl(String clientIntf)
        throws CreateException
    {

        boolean useLocalBeanView = isLocalBeanClass(clientIntf);
        return createEJBLocalBusinessObjectImpl(useLocalBeanView);

    }

    /**
     * Called when a remote invocation arrives for an EJB.
     * Implemented in subclasses.
     */
    abstract EJBObjectImpl getEJBObjectImpl(byte[] streamKey);
    
    EJBObjectImpl getEJBRemoteBusinessObjectImpl(byte[] streamKey) {
	throw new EJBException
          ("Internal ERROR: BaseContainer.getRemoteBusinessObjectImpl called");
    }

    EJBLocalObjectImpl getEJBLocalObjectImpl(Object key) {
	throw new EJBException
            ("Internal ERROR: BaseContainer.getEJBLocalObjectImpl called");
    }

    EJBLocalObjectImpl getEJBLocalBusinessObjectImpl(Object key) {
	throw new EJBException
            ("Internal ERROR: BaseContainer.getEJBLocalBusinessObjectImpl called");
    }

    EJBLocalObjectImpl getOptionalEJBLocalBusinessObjectImpl(Object key) {
    throw new EJBException
            ("Internal ERROR: BaseContainer.getOptionalEJBLocalBusinessObjectImpl called");
    }

    /**
     * Check if the given EJBObject/LocalObject has been removed.
     * @exception NoSuchObjectLocalException if the object has been removed.
     */
    void checkExists(EJBLocalRemoteObject ejbObj)  {
        throw new EJBException(
            "Internal ERROR: BaseContainer.checkExists called");
    }

    protected final ComponentContext getContext(EjbInvocation inv)
        throws EJBException {

        return (inv.context == null) ? _getContext(inv) : inv.context;

    }
    
    // internal API, implemented in subclasses
    protected abstract ComponentContext _getContext(EjbInvocation inv)
        throws EJBException;

    // internal API, implemented in subclasses
    protected abstract void releaseContext(EjbInvocation inv)
        throws EJBException;
    
    abstract boolean passivateEJB(ComponentContext context);
    
    // internal API, implemented in subclasses
    abstract void forceDestroyBean(EJBContextImpl sc)
        throws EJBException;
    
    abstract void removeBean(EJBLocalRemoteObject ejbo, Method removeMethod,
            boolean local)
        throws RemoveException, EJBException, RemoteException;
    
    // default implementation
    public void removeBeanUnchecked(Object pkey) {
        throw new EJBException(
            "removeBeanUnchecked only works for EntityContainer");
    }

    // default implementation
    public void removeBeanUnchecked(EJBLocalObject bean) {
        throw new EJBException(
            "removeBeanUnchecked only works for EntityContainer");
    }

    public void preSelect() {
        throw new EJBException(
	    "preSelect only works for EntityContainer");
    }

    // default implementation
    public EJBLocalObject getEJBLocalObjectForPrimaryKey(Object pkey, EJBContext ctx)
    {
	throw new EJBException("getEJBLocalObjectForPrimaryKey(pkey, ctx) only works for EntityContainer");
    }
    
    // default implementation
    public EJBLocalObject getEJBLocalObjectForPrimaryKey(Object pkey) {
        throw new EJBException(
            "getEJBLocalObjectForPrimaryKey only works for EntityContainer");
    }
    
    // default implementation
    public EJBObject getEJBObjectForPrimaryKey(Object pkey) {
        throw new EJBException(
            "getEJBObjectForPrimaryKey only works for EntityContainer");
    }
    
    // internal API, implemented in subclasses
    boolean isIdentical(EJBObjectImpl ejbo, EJBObject other)
        throws RemoteException
    {
        throw new EJBException(
            "Internal ERROR: BaseContainer.isIdentical called");
    }

    /**
     * Called-back from security implementation through EjbInvocation
     * when a jacc policy provider wants an enterprise bean instance.
     */
    public Object getJaccEjb(EjbInvocation inv) {
        Object bean = null;

        // Access to an enterprise bean instance is undefined for
        // anything but business method invocations through
        // Remote , Local, and ServiceEndpoint interfaces.
        if( ( (inv.invocationInfo != null) && 
              inv.invocationInfo.isBusinessMethod )
            || 
            inv.isWebService ) {

            // In the typical case the context will not have been
            // set when the policy provider invokes this callback.
            // There are some cases where it is ok for it to have been
            // set, e.g. if the policy provider invokes the callback
            // twice within the same authorization decision.
            if( inv.context == null ) {

                try {
                    inv.context = getContext(inv);
                    bean = inv.context.getEJB();
                    // NOTE : inv.ejb is not set here.  Post-invoke logic for
                    // BaseContainer and webservices uses the fact that 
                    // inv.ejb is non-null as an indication that that
                    // BaseContainer.preInvoke() proceeded past a certain 
                    // point, which affects which cleanup needs to be 
                    // performed.  It would be better to have explicit 
                    // state in the invocation that says which cleanup 
                    // steps are necessary(e.g. for invocationMgr.postInvoke
                    // , postInvokeTx, etc) but I'm keeping the logic the 
                    // same for now.   BaseContainer.authorize() will 
                    // explicitly handle the case where a context was 
                    // created as a result of this call and the 
                    // authorization failed, which means the context needs
                    // be released.

                } catch(EJBException e) {
                    _logger.log(Level.WARNING, "ejb.context_failure_jacc", 
                                logParams[0]);
                    _logger.log(Level.WARNING, "", e);
                }

            } else {
                bean = inv.context.getEJB();
            }
        }

        return bean;
    }

    public void assertValidLocalObject(Object o) throws EJBException 
    {
        boolean valid = false;
        String errorMsg = "";

        if( (o != null) && (o instanceof EJBLocalObject) ) {
            // Given object is always the client view EJBLocalObject.
            // Use utility method to translate it to EJBLocalObjectImpl
            // so we handle both the generated and proxy case.
            EJBLocalObjectImpl ejbLocalObjImpl = 
                EJBLocalObjectImpl.toEJBLocalObjectImpl( (EJBLocalObject) o);
            BaseContainer otherContainer = 
                (BaseContainer) ejbLocalObjImpl.getContainer();
            if( otherContainer.getContainerId() == getContainerId() ) {
                valid = true;
            } else {
                errorMsg = "Local objects of ejb-name " + otherContainer.ejbDescriptor.getName() +
                        " and ejb-name " + ejbDescriptor.getName() +
                         " are from different containers" ;

            }
        } else {
            errorMsg = (o != null) ?
                    "Parameter instance of class '" +  o.getClass().getName() +
                           "' is not a valid local interface instance for bean " +
                                      ejbDescriptor.getName()
               :
             "A null parameter is not a valid local interface of bean " +  ejbDescriptor.getName();
        }
        
        if( !valid ) {
            throw new EJBException(errorMsg);
        }
            
    }

    /**
     * Asserts validity of RemoteHome objects.  This was defined for the
     * J2EE 1.4 implementation and is exposed through Container SPI.
     */ 
    public void assertValidRemoteObject(Object o) throws EJBException 
    {
        boolean valid = false;
        String errorMsg = "";
	    Exception causeException = null;

        if( (o != null) && (o instanceof EJBObject) ) {
            String className = o.getClass().getName();

            // Given object must be an instance of the remote stub class for
            // this ejb.
            if (hasRemoteHomeView) {
		        try {
		            valid = remoteHomeRefFactory.hasSameContainerID(
				        (org.omg.CORBA.Object) o);
		        } catch (Exception ex) {
		            causeException = ex;
		                errorMsg =   "Parameter instance of class '" + className +
                        "' is not a valid remote interface instance for bean "
                        + ejbDescriptor.getName();
		        }
            } else {
                errorMsg = "Parameter instance of class '" + className +
                        "' is not a valid remote interface instance for bean "
                        + ejbDescriptor.getName();

            }
        } else {
            errorMsg = (o != null) ?
                    "Parameter instance of class '" +  o.getClass().getName() +
                           "' is not a valid remote interface instance for bean " +
                                      ejbDescriptor.getName()
               :
             "A null parameter is not a valid remote interface of bean " +  ejbDescriptor.getName();
        }


        if( !valid ) {
            if (causeException != null) {
		        throw new EJBException(errorMsg, causeException);
	        } else {
		        throw new EJBException(errorMsg);
	        }
        }
    }

    /**
     * 
     */
    protected final int getTxAttr(Method method, String methodIntf) 
        throws EJBException 
    {

        InvocationInfo invInfo = 
            methodIntf.equals(MethodDescriptor.EJB_WEB_SERVICE) ?
            (InvocationInfo) webServiceInvocationInfoMap.get(method) :
            (InvocationInfo) invocationInfoMap.get(method);

        if( invInfo != null ) {
            return invInfo.txAttr;
        } else {
            throw new EJBException("Transaction Attribute not found for method"
                                   + method);
        }
    }
    
    // Get the transaction attribute for a method.
    // Note: this method object is of the remote/EJBHome interface
    // class, not the EJB class.  (except for MDB's message listener
    // callback method or TimedObject ejbTimeout method)
    protected final int getTxAttr(EjbInvocation inv)
        throws EJBException
    {
        if ( inv.transactionAttribute != TX_NOT_INITIALIZED ) {
            return inv.transactionAttribute;
        }

        int txAttr = getTxAttr(inv.method, inv.getMethodInterface());
        inv.transactionAttribute = txAttr;
        return inv.transactionAttribute;

    }
    
    
    // Check if a method is a business method.
    // Note: this method object is of the EJB's remote/home/local interfaces,
    // not the EJB class.
    final boolean isBusinessMethod(Method method) {
        Class methodClass = method.getDeclaringClass();
        
        // All methods on the Home/LocalHome & super-interfaces
        // are not business methods.
        // All methods on javax.ejb.EJBObject and EJBLocalObject
        // (e.g. remove) are not business methods.
        // All remaining methods are business methods
        
        if ( isRemote ) {
            if ( (hasRemoteHomeView && 
                  ( (methodClass == homeIntf) ||
                    methodClass.isAssignableFrom(homeIntf) ))
                 ||
                 (hasRemoteBusinessView && 
                  ( (methodClass == remoteBusinessHomeIntf) ||
                    methodClass.isAssignableFrom(remoteBusinessHomeIntf) ))
                 ||
                 (methodClass == EJBObject.class ) ) {
                return false;
            }
        }
        if ( isLocal ) {
            if ( (hasLocalHomeView && 
                  ( (methodClass == localHomeIntf) ||
                    methodClass.isAssignableFrom(localHomeIntf) )) 
                 ||
                 (hasLocalBusinessView &&
                  ( (methodClass == localBusinessHomeIntf) ||
                    methodClass.isAssignableFrom(localBusinessHomeIntf) ))   
                 || 
                 (methodClass == EJBLocalObject.class)) {
                return false;
            }
        }
        // NOTE : Web Service client view contains ONLY
        // business methods
        
        return true;
    }
    
    // Check if a method is a finder / home method.
    // Note: this method object is of the EJB's remote/home/local interfaces,
    // not the EJB class.
    final boolean isHomeFinder(Method method) {
        Class methodClass = method.getDeclaringClass();
        
        // MDBs and SessionBeans cant have finder/home methods.
        if ( isMessageDriven || isSession ) {
            return false;
        }
        
        if ( isRemote ) {
            if ( (hasRemoteHomeView &&
                  methodClass.isAssignableFrom(homeIntf))
                 && (methodClass != EJBHome.class)
                 && (!method.getName().startsWith("create")) ) {
                return true;
            }
        }
        if ( isLocal ) {
            // No need to check LocalBusiness view b/c home/finder methods
            // only apply to entity beans.
            if ( (hasLocalHomeView &&
                  methodClass.isAssignableFrom(localHomeIntf)) 
                 && (methodClass != EJBLocalHome.class)
                 && (!method.getName().startsWith("create")) ) {
                return true;
            }
        }
        
        return false;
    }
    
    
    // Check if a method is a create / finder / home method.
    // Note: this method object is of the EJB's remote/home/local interfaces,
    // not the EJB class.
    final boolean isCreateHomeFinder(Method method) {
        Class methodClass = method.getDeclaringClass();
        
        if ( isMessageDriven ) {
            return false;
        }
        
        if ( hasRemoteHomeView 
             && methodClass.isAssignableFrom(homeIntf)
             && (methodClass != EJBHome.class) ) {
            return true;
        }
        
        if ( hasRemoteBusinessView
             && methodClass.isAssignableFrom(remoteBusinessHomeIntf)
             && (methodClass != EJBHome.class) ) {
            return true;
        }

        if ( hasLocalHomeView 
             && methodClass.isAssignableFrom(localHomeIntf)
             && (methodClass != EJBLocalHome.class) ) {
            return true;
        }

        if ( hasLocalBusinessView
             && methodClass.isAssignableFrom(localBusinessHomeIntf)
             && (methodClass != EJBLocalHome.class) ) {
            return true;
        }
        
        
        return false;
    }

    private InvocationInfo addInvocationInfo(Method method, String methodIntf,
                                   Class originalIntf)
        throws EJBException {

        return addInvocationInfo(method, methodIntf, originalIntf, false, false);
    }
    private InvocationInfo addInvocationInfo(Method method, String methodIntf,
                                   Class originalIntf, boolean isEjbTimeout)
        throws EJBException {

        return addInvocationInfo(method, methodIntf, originalIntf, isEjbTimeout, false);
    }

    private InvocationInfo addInvocationInfo(Method method, String methodIntf,
                                   Class originalIntf, boolean isEjbTimeout, 
                                   boolean optionalLocalBusView)
        throws EJBException
        
    {
        boolean flushEnabled = findFlushEnabledAttr(method, methodIntf);
        int txAttr = findTxAttr(method, methodIntf);
        InvocationInfo info = createInvocationInfo
            (method, txAttr, flushEnabled, methodIntf, originalIntf);
        boolean isHomeIntf = (methodIntf.equals(MethodDescriptor.EJB_HOME)
                || methodIntf.equals(MethodDescriptor.EJB_LOCALHOME));
        if (! isHomeIntf) {
            Method beanMethod = null;
            if (!isEjbTimeout) {
                try {
                    beanMethod = getEJBClass().getMethod(
                            method.getName(), method.getParameterTypes());
                } catch (NoSuchMethodException nsmEx) {
                    //TODO
                }
            } else {
                // For a timeout it is the method
                beanMethod = method;
            }

            if( beanMethod != null ) {
                // Can't set AroundInvoke/AroundTimeout chains here, but set up some
                // state on info object so it can be done right after InterceptorManager
                // is initialized. 
                info.aroundMethod = beanMethod;
                info.isEjbTimeout = isEjbTimeout;
            }


            // Asynchronous method initialization        
            if ( isEligibleForAsync(originalIntf, methodIntf) ) {

                Method targetMethod = optionalLocalBusView ? beanMethod : method;

                boolean isAsync = ((EjbSessionDescriptor) ejbDescriptor).
                        isAsynchronousMethod(targetMethod);

                if( isAsync ) {

                    // Check return type
                    if( optionalLocalBusView ) {

                        boolean beanMethodReturnTypeVoid = beanMethod.getReturnType().equals(Void.TYPE);
                        boolean beanMethodReturnTypeFuture = beanMethod.getReturnType().equals(Future.class);

                        if ( !beanMethodReturnTypeVoid && !beanMethodReturnTypeFuture ){
                            throw new RuntimeException("Invalid no-interface view asynchronous method '"
                                    + beanMethod + "' for bean " + ejbDescriptor.getName() +
                                    ". Async method exposed through no-interface view must " +
                                    " have return type void or java.lang.concurrent.Future<V>");
                        }

                    } else {

                        // Use actual interface method instead of method from generated interface
                        Method intfMethod = null;
                        try {
                            intfMethod = originalIntf.getMethod(
                                method.getName(), method.getParameterTypes());
                        } catch (NoSuchMethodException nsmEx) {
                            throw new RuntimeException("No matching async intf method for method '" +
                                beanMethod + "' on bean " + ejbDescriptor.getName());
                        }

                        if( beanMethod == null ) {

                            throw new RuntimeException("No matching bean class method for async method '" +
                                intfMethod + "' on bean " + ejbDescriptor.getName());
                        }

                        boolean beanMethodReturnTypeVoid = beanMethod.getReturnType().equals(Void.TYPE);
                        boolean beanMethodReturnTypeFuture = beanMethod.getReturnType().equals(Future.class);

                        boolean intfMethodReturnTypeVoid = intfMethod.getReturnType().equals(Void.TYPE);
                        boolean intfMethodReturnTypeFuture = intfMethod.getReturnType().equals(Future.class);

                        boolean bothVoid = intfMethodReturnTypeVoid && beanMethodReturnTypeVoid;
                        boolean bothFuture = intfMethodReturnTypeFuture && beanMethodReturnTypeFuture;

                        boolean valid = false;

                        if( bothVoid ) {
                            valid = true;
                        } else if( bothFuture ) {
                            valid = true;
                        }

                        if( !valid ) {
                            throw new RuntimeException("Invalid asynchronous bean class / interface " +
                                    "method signatures for bean " + ejbDescriptor.getName() +
                                    ". beanMethod = '" + beanMethod + "' , interface method = '"
                                        +  intfMethod + "'");
                        }
                    }

                    info.setIsAsynchronous(true);

                }
            }
        }
        
        if( methodIntf.equals(MethodDescriptor.EJB_WEB_SERVICE) ) {
            webServiceInvocationInfoMap.put(method, info);
        } else {
            invocationInfoMap.put(method, info);        
        }
                
        return info;
    }

    private boolean isEligibleForAsync(Class originalIntf, String methodIntf) {

        boolean eligibleForAsync = false;

        if( methodIntf.equals(MethodDescriptor.EJB_LOCAL) ||
            methodIntf.equals(MethodDescriptor.EJB_REMOTE) ) {

            boolean is2xClientView = (EJBObject.class.isAssignableFrom(originalIntf) ||
                        EJBLocalObject.class.isAssignableFrom(originalIntf));
            eligibleForAsync = !is2xClientView;
        }

        return eligibleForAsync;           
    }

    /**
     * Create invocation info for one method.  
     *
     * @param originalIntf Leaf interface for the given view.  Not set for
     * methodIntf == bean.  
     */
    private InvocationInfo createInvocationInfo(Method method, int txAttr, 
                                                boolean flushEnabled,
                                                String methodIntf,
                                                Class originalIntf) 
        throws EJBException {

        InvocationInfo invInfo = new InvocationInfo(method);

        invInfo.ejbName = ejbDescriptor.getName();
        invInfo.txAttr = txAttr;
        invInfo.securityPermissions = Container.SEC_NOT_INITIALIZED;
        invInfo.methodIntf = methodIntf;

        invInfo.isBusinessMethod = isBusinessMethod(method);
        invInfo.isCreateHomeFinder = isCreateHomeFinder(method);
        invInfo.isHomeFinder = isHomeFinder(method);

        invInfo.startsWithCreate = method.getName().startsWith("create");
        invInfo.startsWithFind = method.getName().startsWith("find");
        invInfo.startsWithRemove = method.getName().startsWith("remove");
        invInfo.startsWithFindByPrimaryKey = 
            method.getName().startsWith("findByPrimaryKey");
        invInfo.flushEnabled = flushEnabled;

        if( methodIntf.equals(MethodDescriptor.EJB_LOCALHOME) ) {
            if( method.getDeclaringClass() != EJBLocalHome.class ) {
                setHomeTargetMethodInfo(invInfo, true);
            }
        } else if( methodIntf.equals(MethodDescriptor.EJB_HOME) ) {
            if( method.getDeclaringClass() != EJBHome.class ) {
                setHomeTargetMethodInfo(invInfo, false);
            }
        } else if( methodIntf.equals(MethodDescriptor.EJB_LOCAL) ) {
            if( method.getDeclaringClass() != EJBLocalObject.class ) {
                setEJBObjectTargetMethodInfo(invInfo, true, originalIntf);
            }
        } else if( methodIntf.equals(MethodDescriptor.EJB_REMOTE) ) {
            if( method.getDeclaringClass() != EJBObject.class ) {
                setEJBObjectTargetMethodInfo(invInfo, false, originalIntf);
            }
        } 

        setConcurrencyInvInfo(method, methodIntf, invInfo);

        if( _logger.isLoggable(Level.FINE) ) {
            _logger.log(Level.FINE, invInfo.toString());
        }



        return invInfo;
    }

    private void setConcurrencyInvInfo(Method invInfoMethod, String methodIntf,
                                       InvocationInfo invInfo) {

        MethodLockInfo lockInfo = null;

        // Set READ/WRITE lock info.  Only applies to singleton beans.
        if( isSingleton ) {
            EjbSessionDescriptor singletonDesc = (EjbSessionDescriptor) ejbDescriptor;
            List<MethodDescriptor> readLockMethods = singletonDesc.getReadLockMethods();
            List<MethodDescriptor> writeLockMethods = singletonDesc.getWriteLockMethods();

            for(MethodDescriptor readLockMethodDesc : readLockMethods) {
                Method readLockMethod = readLockMethodDesc.getMethod(singletonDesc);
                if(implMethodMatchesInvInfoMethod(invInfoMethod, methodIntf, readLockMethod)) {

                    lockInfo = new MethodLockInfo();
                    lockInfo.setLockType(LockType.READ);
                    break;
                }
            }

            if( lockInfo == null ) {
                for(MethodDescriptor writeLockMethodDesc : writeLockMethods) {
                    Method writeLockMethod = writeLockMethodDesc.getMethod(singletonDesc);
                    if(implMethodMatchesInvInfoMethod(invInfoMethod, methodIntf, writeLockMethod)) {

                        lockInfo = new MethodLockInfo();
                        lockInfo.setLockType(LockType.WRITE);
                        break;
                    }
                }
            }
        }

        // Set AccessTimeout info
        if( isSingleton || isStatefulSession ) {

            EjbSessionDescriptor sessionDesc = (EjbSessionDescriptor) ejbDescriptor;
            List<EjbSessionDescriptor.AccessTimeoutHolder> accessTimeoutInfo =
                    sessionDesc.getAccessTimeoutInfo();


            for(EjbSessionDescriptor.AccessTimeoutHolder accessTimeoutHolder : accessTimeoutInfo) {
                MethodDescriptor accessTimeoutMethodDesc = accessTimeoutHolder.method;
                Method accessTimeoutMethod = accessTimeoutMethodDesc.getMethod(sessionDesc);
                if(implMethodMatchesInvInfoMethod(invInfoMethod, methodIntf, accessTimeoutMethod)) {

                    if( lockInfo == null ) {
                        lockInfo = new MethodLockInfo();
                    }

                    lockInfo.setTimeout(accessTimeoutHolder.value , accessTimeoutHolder.unit);

                    break;
                }
            }
        }

        if( lockInfo != null) {
            invInfo.methodLockInfo = lockInfo;
        }

    }

    private boolean implMethodMatchesInvInfoMethod
            (Method invInfoMethod, String methodIntf, Method implMethod) {

        boolean match = false;

        if( methodIntf.equals(MethodDescriptor.EJB_BEAN) ) {
            // Declaring class must match in addition to signature
            match = ( implMethod.getDeclaringClass().equals(invInfoMethod.getDeclaringClass()) &&
                      TypeUtil.sameMethodSignature(implMethod, invInfoMethod) );

        } else {
            match = Modifier.isPublic(implMethod.getModifiers()) &&
                    Modifier.isPublic(invInfoMethod.getModifiers()) &&
                    TypeUtil.sameMethodSignature(implMethod, invInfoMethod);
        }

        return match;
    }

    protected InvocationInfo postProcessInvocationInfo(
            InvocationInfo invInfo) {
        return invInfo;
    }
    
    private void setHomeTargetMethodInfo(InvocationInfo invInfo, 
                                         boolean isLocal) 
        throws EJBException {
                                         
        Class homeIntfClazz = isLocal ? 
            javax.ejb.EJBLocalHome.class : javax.ejb.EJBHome.class;

        boolean isEntity = (ejbDescriptor instanceof EjbEntityDescriptor);

        Class methodClass  = invInfo.method.getDeclaringClass();
        Class[] paramTypes = invInfo.method.getParameterTypes();
        String methodName  = invInfo.method.getName();

        try {
            Method m = homeIntfClazz.getMethod(methodName, paramTypes);
            // Attempt to override Home/LocalHome method.  Print warning
            // but don't treat it as a fatal error. At runtime, 
            // the EJBHome/EJBLocalHome method will be called.
            String[] params = { m.toString(),invInfo.method.toString() };
            _logger.log(Level.WARNING, 
                        "ejb.illegal_ejb_interface_override", params);
            invInfo.ejbIntfOverride = true;
            return;
        } catch(NoSuchMethodException nsme) {
        }

        try {
            if( invInfo.startsWithCreate ) {
                
                String extraCreateChars = 
                    methodName.substring("create".length());
                invInfo.targetMethod1 = ejbClass.getMethod
                    ("ejbCreate" + extraCreateChars, paramTypes);
                
                if( isEntity ) {
                    invInfo.targetMethod2 = ejbClass.getMethod
                        ("ejbPostCreate" + extraCreateChars, paramTypes);
                }
                
            } else if ( invInfo.startsWithFind ) {
                
                String extraFinderChars = methodName.substring("find".length());
                invInfo.targetMethod1 = ejbClass.getMethod
                    ("ejbFind" + extraFinderChars, paramTypes);
                
            } else {

                // HOME method

                String upperCasedName = 
                    methodName.substring(0,1).toUpperCase() +
                    methodName.substring(1);
                invInfo.targetMethod1 = ejbClass.getMethod
                    ("ejbHome" + upperCasedName, paramTypes);
            }
        } catch(NoSuchMethodException nsme) {
            
            if( (methodClass == localBusinessHomeIntf) ||
                (methodClass == remoteBusinessHomeIntf) ||
                (methodClass == ejbOptionalLocalBusinessHomeIntf ||
                (methodClass == GenericEJBHome.class)) ) {
                // Not an error.  This is the case where the EJB 3.0
                // client view is being used and there is no corresponding
                // create/init method.
            } else if (isStatelessSession || isSingleton) {
                // Ignore.  Not an error.  
                // EJB 3.0 Stateless session ejbCreate/PostConstruct
                // is decoupled from RemoteHome/LocalHome create().
            } else {

                Method initMethod = null;
                if( isSession ) {
                    EjbSessionDescriptor sessionDesc = 
                        (EjbSessionDescriptor) ejbDescriptor;

                    for(EjbInitInfo next : sessionDesc.getInitMethods()) {
                        MethodDescriptor beanMethod = next.getBeanMethod();
                        Method m = beanMethod.getMethod(sessionDesc);
                        if( next.getCreateMethod().getName().equals(methodName)
                            &&
                            TypeUtil.sameParamTypes(m, invInfo.method) ) {
                            initMethod = m;
                            break;
                        }
                    }
                }
                
                if( initMethod != null ) {
                    invInfo.targetMethod1 = initMethod;
                } else {
                    Object[] params = { logParams[0], 
                                        (isLocal ? "LocalHome" : "Home"),
                                        invInfo.method.toString() };
                    _logger.log(Level.WARNING, 
                                "ejb.bean_class_method_not_found", params);
                    // Treat this as a warning instead of a fatal error.
                    // That matches the behavior of the generated code.
                    // Mark the target methods as null.  If this method is
                    // invoked at runtime it will be result in an exception 
                    // from the invocation handlers.
                    invInfo.targetMethod1 = null;
                    invInfo.targetMethod2 = null;
                }
            }
        }
    }

    private void setEJBObjectTargetMethodInfo(InvocationInfo invInfo,
                                              boolean isLocal,
                                              Class originalIntf) 
        throws EJBException {

        Class ejbIntfClazz = isLocal ? 
            javax.ejb.EJBLocalObject.class : javax.ejb.EJBObject.class;

        Class[] paramTypes = invInfo.method.getParameterTypes();
        String methodName  = invInfo.method.getName();

        // Check for 2.x Remote/Local bean attempts to override 
        // EJBObject/EJBLocalObject operations.  
        if( ejbIntfClazz.isAssignableFrom(originalIntf) ) {
            try {
                Method m = ejbIntfClazz.getMethod(methodName, paramTypes);
                // Attempt to override EJBObject/EJBLocalObject method.  Print 
                // warning but don't treat it as a fatal error. At runtime, the
                // EJBObject/EJBLocalObject method will be called.
                String[] params = { m.toString(),invInfo.method.toString() };
                _logger.log(Level.WARNING, 
                            "ejb.illegal_ejb_interface_override", params);
                invInfo.ejbIntfOverride = true;
                return;
            } catch(NoSuchMethodException nsme) {
            }
        }

        try {
            invInfo.targetMethod1 = ejbClass.getMethod(methodName, paramTypes);

            if( isSession && isStatefulSession ) {
                MethodDescriptor methodDesc = new MethodDescriptor
                    (invInfo.targetMethod1, MethodDescriptor.EJB_BEAN);

                // Assign removal info to inv info.  If this method is not
                // an @Remove method, result will be null.
                invInfo.removalInfo = ((EjbSessionDescriptor)ejbDescriptor).
                    getRemovalInfo(methodDesc);
            }

        } catch(NoSuchMethodException nsme) {
            Object[] params = { logParams[0] + ":" + nsme.toString(), 
                                (isLocal ? "Local" : "Remote"),
                                invInfo.method.toString() };
            _logger.log(Level.WARNING, 
                        "ejb.bean_class_method_not_found", params);
            // Treat this as a warning instead of a fatal error.
            // That matches the behavior of the generated code.
            // Mark the target methods as null.  If this method is
            // invoked at runtime it will be result in an exception from
            // the invocation handlers.
            invInfo.targetMethod1 = null;            
        }
    }

    //Overridden in StatefulContainerOnly
    protected String[] getPre30LifecycleMethodNames() {
        return new String[] {
            "ejbCreate", "ejbRemove", "ejbPassivate", "ejbActivate"
        };
    };
    
    private void initializeInterceptorManager() throws Exception {
        this.interceptorManager = new InterceptorManager(_logger, this,
                lifecycleCallbackAnnotationClasses,
                getPre30LifecycleMethodNames());
    }

    void registerSystemInterceptor(Object o) {

        if (needSystemInterceptorProxy()) {
            interceptorManager.registerRuntimeInterceptor(o);
        }
    }

    private boolean needSystemInterceptorProxy() {

        // TODO only really needed if JAX-RS needs to dynamically register an
        // interceptor during web application init.  Can optimize this out
        // by checking for the existence of any JAX-RS resources in module.
        // Only applies to stateless and singleton session beans.
        return isSession && !isStatefulSession;

    }

    private void addSystemInterceptorProxy() {

        InterceptorDescriptor interceptorDesc = SystemInterceptorProxy.createInterceptorDesc();
        ejbDescriptor.addFrameworkInterceptor(interceptorDesc);

    }
    
    /*
     * Used by message bean container to register message-listener methods
     */
    protected void registerTxAttrForMethod(Method method, String methodIntf) {
        addInvocationInfo(method, methodIntf, null);
    }
    
    
    private void initializeInvocationInfo()
        throws Exception
    {
        
        if( isMessageDriven ) {
            // message listener method initialization performed by 
            // message bean container 
        } else {
            if ( isRemote ) {

                if( hasRemoteHomeView ) {
                    // Process Remote intf
                    Method[] methods = remoteIntf.getMethods();
                    for ( int i=0; i<methods.length; i++ ) {
                        Method method = methods[i];                    
                        addInvocationInfo(method, MethodDescriptor.EJB_REMOTE,
                                          remoteIntf);
                    }
                    
                    // Process EJBHome intf
                    methods = homeIntf.getMethods();
                    for ( int i=0; i<methods.length; i++ ) {
                        Method method = methods[i];                   
                        addInvocationInfo(method, MethodDescriptor.EJB_HOME,
                                          homeIntf);
                    }
                }

                if( hasRemoteBusinessView ) {

                    for(RemoteBusinessIntfInfo next : 
                            remoteBusinessIntfInfo.values()) {
                        // Get methods from generated remote intf but pass
                        // actual business interface as original interface.
                        Method[] methods = 
                            next.generatedRemoteIntf.getMethods();
                        for ( int i=0; i<methods.length; i++ ) {
                            Method method = methods[i];                    
                            addInvocationInfo(method, 
                                              MethodDescriptor.EJB_REMOTE,
                                              next.remoteBusinessIntf);
                        }
                    }
                    
                    // Process internal EJB RemoteBusinessHome intf
                    Method[] methods = remoteBusinessHomeIntf.getMethods();
                    for ( int i=0; i<methods.length; i++ ) {
                        Method method = methods[i];                   
                        addInvocationInfo(method, MethodDescriptor.EJB_HOME,
                                          remoteBusinessHomeIntf);
                    } 
                }
            }

            if ( isLocal ) {
                if( hasLocalHomeView ) {
                    // Process Local interface
                    Method[] methods = localIntf.getMethods();
                    for ( int i=0; i<methods.length; i++ ) {
                        Method method = methods[i];                    
                        InvocationInfo info = addInvocationInfo(method, MethodDescriptor.EJB_LOCAL,
                                          localIntf);
                        postProcessInvocationInfo(info);
                    }

                    // Process LocalHome interface
                    methods = localHomeIntf.getMethods();
                    for ( int i=0; i<methods.length; i++ ) {
                        Method method = methods[i];                    
                        addInvocationInfo(method, 
                                          MethodDescriptor.EJB_LOCALHOME,
                                          localHomeIntf);
                    }
                }

                if( hasLocalBusinessView ) {

                    // Process Local Business interfaces
                    for(Class localBusinessIntf : localBusinessIntfs) {
                        Method[] methods = localBusinessIntf.getMethods();
                        for ( int i=0; i<methods.length; i++ ) {
                            Method method = methods[i];                    
                            addInvocationInfo(method, 
                                              MethodDescriptor.EJB_LOCAL,
                                              localBusinessIntf);
                        }
                    }

                    // Process (internal) Local Business Home interface
                    Method[] methods = localBusinessHomeIntf.getMethods();
                    for ( int i=0; i<methods.length; i++ ) {
                        Method method = methods[i];                    
                        addInvocationInfo(method, 
                                          MethodDescriptor.EJB_LOCALHOME,
                                          localBusinessHomeIntf);
                    }
                }

                if (hasOptionalLocalBusinessView) {
                    
                    // Process generated Optional Local Business interface
                    String optClassName = EJBUtils.getGeneratedOptionalInterfaceName(ejbClass.getName());
                    ejbGeneratedOptionalLocalBusinessIntfClass = optIntfClassLoader.loadClass(optClassName);
                    Method[] methods = ejbGeneratedOptionalLocalBusinessIntfClass.getMethods();
                    for ( int i=0; i<methods.length; i++ ) {
                        Method method = methods[i];
                        addInvocationInfo(method,
                                          MethodDescriptor.EJB_LOCAL,
                                          ejbGeneratedOptionalLocalBusinessIntfClass, 
                                          false, true);
                    }

                    // Process generated Optional Local Business interface
                    Method[] optHomeMethods = ejbOptionalLocalBusinessHomeIntf.getMethods();
                    for ( int i=0; i<optHomeMethods.length; i++ ) {
                        Method method = optHomeMethods[i];
                        addInvocationInfo(method,
                                          MethodDescriptor.EJB_LOCALHOME,
                                          ejbOptionalLocalBusinessHomeIntf);
                    }


                }

                if( !hasLocalHomeView ) {
                    // Add dummy local business interface remove method so that internal
                    // container remove operations will work.  (needed for internal 299 contract)
                    addInvocationInfo(this.ejbIntfMethods[EJBLocalObject_remove],
                            MethodDescriptor.EJB_LOCAL,
                            javax.ejb.EJBLocalObject.class);
                }
            }

            if ( isWebServiceEndpoint ) {
                // Process Service Endpoint interface
                Method[] methods = webServiceEndpointIntf.getMethods();
                for ( int i=0; i<methods.length; i++ ) {
                    Method method = methods[i];                   
                    addInvocationInfo(method,MethodDescriptor.EJB_WEB_SERVICE,
                                      webServiceEndpointIntf);
                }
            }



        }
        
        if( isTimedObject() ) {
            if (ejbTimeoutMethod != null) {
                processTxAttrForScheduledTimeoutMethod(ejbTimeoutMethod);
            }

            for (Method m : schedules.keySet()) {
                processTxAttrForScheduledTimeoutMethod(m);
            }
        }

        // Create a map implementation that is optimized
        // for method lookups.  This is especially important for local
        // invocations through dynamic proxies, where the overhead of the
        // the (method -> invocationInfo) lookup has been measured to be 
        // 6X greater than the overhead of the reflective call itself.
        proxyInvocationInfoMap = new MethodMap(invocationInfoMap);

        
        // Store InvocationInfo by standard ejb interface method type
        // to avoid an invocation info map lookup during authorizeLocalMethod
        // and authorizeRemoteMethod.
        ejbIntfMethodInfo = new InvocationInfo[EJB_INTF_METHODS_LENGTH];
        for(int i = 0; i < ejbIntfMethods.length; i++) {
            Method m = ejbIntfMethods[i];
            ejbIntfMethodInfo[i] = (InvocationInfo) invocationInfoMap.get(m);
        }
    }
    
    // Search for the transaction attribute for a method.
    // This is only used during container initialization.  After that,
    // tx attributes can be looked up with variations of getTxAttr()
    protected int findTxAttr(Method method, String methodIntf) {
        int txAttr = -1;
        
        if ( isBeanManagedTran )
            return TX_BEAN_MANAGED;

        MethodDescriptor md = new MethodDescriptor(method, methodIntf);
        ContainerTransaction ct = ejbDescriptor.getContainerTransactionFor(md);
                                                                    
        if ( ct != null ) {
            String attr = ct.getTransactionAttribute();
            if ( attr.equals(ContainerTransaction.NOT_SUPPORTED) )
                txAttr = TX_NOT_SUPPORTED;
            else if ( attr.equals(ContainerTransaction.SUPPORTS) )
                txAttr = TX_SUPPORTS;
            else if ( attr.equals(ContainerTransaction.REQUIRED) )
                txAttr = TX_REQUIRED;
            else if ( attr.equals(ContainerTransaction.REQUIRES_NEW) )
                txAttr = TX_REQUIRES_NEW;
            else if ( attr.equals(ContainerTransaction.MANDATORY) )
                txAttr = TX_MANDATORY;
            else if ( attr.equals(ContainerTransaction.NEVER) )
                txAttr = TX_NEVER;
        }
        
        if ( txAttr == -1 ) {
            throw new EJBException("Transaction Attribute not found for method "
                + method);
        }
        
        // For EJB2.0 CMP EntityBeans, container is only required to support
        // REQUIRED/REQUIRES_NEW/MANDATORY, see EJB2.0 section 17.4.1.
        if ( isEntity ) {
            if (((EjbEntityDescriptor)ejbDescriptor).getPersistenceType().
                equals(EjbEntityDescriptor.CONTAINER_PERSISTENCE)) {
                EjbCMPEntityDescriptor e= (EjbCMPEntityDescriptor)ejbDescriptor;
                if ( !e.getIASEjbExtraDescriptors().isIsReadOnlyBean() && 
                     e.isEJB20() ) {
                    if ( txAttr != TX_REQUIRED && txAttr != TX_REQUIRES_NEW
                    && txAttr != TX_MANDATORY )
                        throw new EJBException( 
                            "Transaction attribute for EJB2.0 CMP EntityBeans" + 
                            " must be Required/RequiresNew/Mandatory");
                }
            }
        }

        return txAttr;
    }

    /**
     * Verify transaction attribute on the timeout or schedule method and process
     * this method if it's correct.
     */
    private void processTxAttrForScheduledTimeoutMethod(Method m) {
        int txAttr = findTxAttr(m, MethodDescriptor.EJB_BEAN);
        if( isBeanManagedTran ||
            txAttr == TX_REQUIRED ||
            txAttr == TX_REQUIRES_NEW ||
            txAttr == TX_NOT_SUPPORTED ) {
            addInvocationInfo(m, MethodDescriptor.EJB_BEAN, null, true);
        } else {
            throw new EJBException("Timeout method " + m +
                               "must have TX attribute of " +
                               "TX_REQUIRES_NEW or TX_REQUIRED or " +
                               "TX_NOT_SUPPORTED for ejb " +
                               ejbDescriptor.getName());
        }
    }
    
    // Check if the user has enabled flush at end of method flag
    // This is only used during container initialization and set into   
    // the invocation info object. This method is over-riden in the
    // EntityContainer.
    protected boolean findFlushEnabledAttr(Method method, String methodIntf) {
            
        //Get the flushMethodDescriptor and then find if flush has been
        //enabled for this method
        MethodDescriptor md = new MethodDescriptor(method, methodIntf);
        boolean flushEnabled = 
            ejbDescriptor.getIASEjbExtraDescriptors().isFlushEnabledFor(md);

        return flushEnabled;
    }
    
    private EJBHomeImpl instantiateEJBHomeImpl() throws Exception {

        EJBHomeInvocationHandler handler =
            new EJBHomeInvocationHandler(ejbDescriptor, homeIntf,
                                         proxyInvocationInfoMap);

        EJBHomeImpl homeImpl = handler;

        // Maintain insertion order
        Set proxyInterfacesSet = new LinkedHashSet();

        if( ejbDescriptor.getIASEjbExtraDescriptors().isIsReadOnlyBean() ) {
            proxyInterfacesSet.add(ReadOnlyEJBHome.class);
        }

        proxyInterfacesSet.add(homeIntf); 

        Class[] proxyInterfaces = (Class [])
            proxyInterfacesSet.toArray(new Class[0]);

        EJBHome ejbHomeProxy = (EJBHome) 
            Proxy.newProxyInstance( loader, proxyInterfaces, handler);
        
        handler.setProxy(ejbHomeProxy);

        homeImpl.setContainer(this);
            
        return homeImpl;

    }

    private EJBHomeImpl instantiateEJBRemoteBusinessHomeImpl() 
        throws Exception {

        EJBHomeInvocationHandler handler =
            new EJBHomeInvocationHandler(ejbDescriptor,
                                         remoteBusinessHomeIntf,
                                         proxyInvocationInfoMap);

        EJBHomeImpl remoteBusinessHomeImpl = handler;

        EJBHome ejbRemoteBusinessHomeProxy = (EJBHome) 
            Proxy.newProxyInstance(loader, 
                                   new Class[] { remoteBusinessHomeIntf },
                                   handler);
        
        handler.setProxy(ejbRemoteBusinessHomeProxy);

        remoteBusinessHomeImpl.setContainer(this);
            
        return remoteBusinessHomeImpl;

    }

    EjbInvocation createEjbInvocation() {
        return invFactory.create();
    }

    EjbInvocation createEjbInvocation(Object ejb, ComponentContext context) {
        return invFactory.create(ejb, context);
    }

    private EJBLocalHomeImpl instantiateEJBLocalHomeImpl()
        throws Exception {

        // LocalHome impl
        EJBLocalHomeInvocationHandler invHandler = 
            new EJBLocalHomeInvocationHandler(ejbDescriptor, 
                                              localHomeIntf,
                                              proxyInvocationInfoMap);
        
        EJBLocalHomeImpl homeImpl = invHandler;
        
        // Maintain insertion order
        Set proxyInterfacesSet = new LinkedHashSet();
        
        proxyInterfacesSet.add(IndirectlySerializable.class);
        if( ejbDescriptor.getIASEjbExtraDescriptors().isIsReadOnlyBean()) {
            proxyInterfacesSet.add(ReadOnlyEJBLocalHome.class);
        }
        proxyInterfacesSet.add(localHomeIntf);
        
        Class[] proxyInterfaces = (Class[])
            proxyInterfacesSet.toArray(new Class[0]);
        
        // Client's EJBLocalHome object
        EJBLocalHome proxy = (EJBLocalHome) Proxy.newProxyInstance
            (loader, proxyInterfaces, invHandler);
            
        invHandler.setProxy(proxy);

        homeImpl.setContainer(this);

        return homeImpl;
    }

    private EJBLocalHomeImpl instantiateEJBLocalBusinessHomeImpl()
        throws Exception {

        EJBLocalHomeInvocationHandler invHandler =
            new EJBLocalHomeInvocationHandler(ejbDescriptor,
                                              localBusinessHomeIntf,
                                              proxyInvocationInfoMap);

        EJBLocalHomeImpl homeImpl = invHandler;

        EJBLocalHome proxy = (EJBLocalHome) Proxy.newProxyInstance
            (loader, new Class[] { IndirectlySerializable.class,
                                   localBusinessHomeIntf }, invHandler);

        invHandler.setProxy(proxy);

        homeImpl.setContainer(this);

        return homeImpl;
    }


    private EJBLocalHomeImpl instantiateEJBOptionalLocalBusinessHomeImpl()
        throws Exception {

        EJBLocalHomeInvocationHandler invHandler =
            new EJBLocalHomeInvocationHandler(ejbDescriptor,
                                              localBusinessHomeIntf,
                                              proxyInvocationInfoMap);

        EJBLocalHomeImpl homeImpl = invHandler;

        EJBLocalHome proxy = (EJBLocalHome) Proxy.newProxyInstance
            (loader, new Class[] { IndirectlySerializable.class,
                                   ejbOptionalLocalBusinessHomeIntf }, invHandler);

        invHandler.setProxy(proxy);

        homeImpl.setContainer(this);

        return homeImpl;
    }


    protected EJBLocalObjectImpl instantiateEJBLocalObjectImpl() 
        throws Exception {
        EJBLocalObjectImpl localObjImpl = null;

        EJBLocalObjectInvocationHandler handler = 
            new EJBLocalObjectInvocationHandler(proxyInvocationInfoMap,
                                                localIntf);

        localObjImpl = handler;
        EJBLocalObject localObjectProxy = (EJBLocalObject) 
                ejbLocalObjectProxyCtor.newInstance( new Object[] { handler });
        handler.setProxy(localObjectProxy);

        localObjImpl.setContainer(this);

        return localObjImpl;
    }

    protected EJBLocalObjectImpl instantiateEJBLocalBusinessObjectImpl()
        throws Exception {

        EJBLocalObjectInvocationHandler handler =
            new EJBLocalObjectInvocationHandler(proxyInvocationInfoMap, false);

        EJBLocalObjectImpl localBusinessObjImpl = handler;

        Object localObjectProxy = ejbLocalBusinessObjectProxyCtor.newInstance
            ( new Object[] { handler });

        localBusinessObjImpl.setContainer(this);

        for (Class businessIntfClass : localBusinessIntfs) {
            EJBLocalObjectInvocationHandlerDelegate delegate =
                new EJBLocalObjectInvocationHandlerDelegate(
                        businessIntfClass, getContainerId(), handler);
            Proxy proxy = (Proxy) Proxy.newProxyInstance(
                    loader, new Class[] { IndirectlySerializable.class,
                                   businessIntfClass}, delegate);
            localBusinessObjImpl.mapClientObject(businessIntfClass.getName(),
                    proxy);
        }
        return localBusinessObjImpl;
    }

    protected EJBLocalObjectImpl instantiateOptionalEJBLocalBusinessObjectImpl()
        throws Exception {

        EJBLocalObjectInvocationHandler handler =
            new EJBLocalObjectInvocationHandler(proxyInvocationInfoMap, true);

        EJBLocalObjectImpl localBusinessObjImpl = handler;

        Object localObjectProxy = ejbOptionalLocalBusinessObjectProxyCtor.newInstance
            ( new Object[] { handler });

        localBusinessObjImpl.setContainer(this);

        Class businessIntfClass = ejbGeneratedOptionalLocalBusinessIntfClass;
        EJBLocalObjectInvocationHandlerDelegate delegate =
            new EJBLocalObjectInvocationHandlerDelegate(
                    businessIntfClass, getContainerId(), handler);
        Proxy proxy = (Proxy) Proxy.newProxyInstance(
                loader, new Class[] { IndirectlySerializable.class,
                               businessIntfClass}, delegate);

        String beanSubClassName = ejbGeneratedOptionalLocalBusinessIntfClass.getName() + "__Bean__";

        ((EjbOptionalIntfGenerator) optIntfClassLoader).generateOptionalLocalInterfaceSubClass(
                ejbClass, beanSubClassName, ejbGeneratedOptionalLocalBusinessIntfClass);

        Class dummyIntfTst = optIntfClassLoader.loadClass(ejbGeneratedOptionalLocalBusinessIntfClass.getName());
                       
        Class subClass = optIntfClassLoader.loadClass(beanSubClassName);
        OptionalLocalInterfaceProvider provider =
                (OptionalLocalInterfaceProvider) subClass.newInstance();
        provider.setOptionalLocalIntfProxy(proxy);
        localBusinessObjImpl.mapClientObject(ejbClass.getName(), provider);

        return localBusinessObjImpl;
    }
    
    protected EJBObjectImpl instantiateEJBObjectImpl() throws Exception {
        
        EJBObjectInvocationHandler handler =
            new EJBObjectInvocationHandler(proxyInvocationInfoMap,
                                           remoteIntf);        

        EJBObjectImpl ejbObjImpl = handler;

        EJBObject ejbObjectProxy = (EJBObject)
            ejbObjectProxyCtor.newInstance( new Object[] { handler });

        handler.setEJBObject(ejbObjectProxy);

        ejbObjImpl.setContainer(this);

        return ejbObjImpl;
    }

    protected EJBObjectImpl instantiateRemoteBusinessObjectImpl() 
        throws Exception {
        
        // There is one EJBObjectImpl instance, which is an instance of
        // the handler.   That handler instance is shared by the dynamic
        // proxy for each remote business interface.  We need to create a
        // different proxy for each remote business interface because 
        // otherwise the target object given to the orb will be invalid
        // if the same method happens to be declared on multiple remote
        // business interfaces.
        EJBObjectInvocationHandler handler =
            new EJBObjectInvocationHandler(proxyInvocationInfoMap);        

        EJBObjectImpl ejbBusinessObjImpl = handler;

        for(RemoteBusinessIntfInfo next : 
                remoteBusinessIntfInfo.values()) {

            EJBObjectInvocationHandlerDelegate delegate = 
                new EJBObjectInvocationHandlerDelegate(next.remoteBusinessIntf,
                                                       handler);

            java.rmi.Remote ejbBusinessObjectProxy = (java.rmi.Remote)
                next.proxyCtor.newInstance( new Object[] { delegate });

            ejbBusinessObjImpl.setEJBObject(next.generatedRemoteIntf.getName(),
                                            ejbBusinessObjectProxy);

        }

        ejbBusinessObjImpl.setContainer(this);

        return ejbBusinessObjImpl;
    }

    // default implementation
    public void postCreate(EjbInvocation inv, Object primaryKey)
        throws CreateException
    {
        throw new EJBException("Internal error");
    }
    
    // default implementation
    public Object postFind(EjbInvocation inv, Object primaryKeys,
        Object[] findParams)
        throws FinderException
    {
        throw new EJBException("Internal error");
    }
    
    
    private void setupEnvironment()
        throws javax.naming.NamingException
    {
        // call the NamingManager to setup the java:comp/env namespace
        // for this EJB.

        ComponentEnvManager envManager = ejbContainerUtilImpl.getComponentEnvManager();
        componentId = envManager.bindToComponentNamespace(ejbDescriptor);
        invFactory = new EjbInvocationFactory(componentId, this);
        ejbContainerUtilImpl.registerContainer(this);
        // create envProps object to be returned from EJBContext.getEnvironment
        Set env = ejbDescriptor.getEnvironmentProperties();
        SafeProperties safeProps = new SafeProperties();
        safeProps.copy(env);
        envProps = safeProps;
    }
    
    /**
     * Called from NamingManagerImpl during java:comp/env lookup.
     */
    public String getComponentId() {
        return componentId;
    }
    
    /**
     * Called after all the components in the container's application
     * have deployed successfully.
     */
    public void startApplication(boolean deploy) {
        _logger.log(Level.FINE,"Application deployment successful : " + 
                    this);

        // By now all existing timers should have been restored.
        if( isTimedObject_ ) {
            // EJBTimerService should be accessed only if needed 
            // not to cause it to be loaded if it's not used.
            EJBTimerService timerService = 
                ejbContainerUtilImpl.getEJBTimerService();
            if( timerService != null ) {
                scheduleIds = timerService.recoverAndCreateSchedules(
                        getContainerId(), schedules, deploy);
            }
        }

        setStartedState();
    }
    
    /**
     *
     */
    boolean callEJBTimeout(RuntimeTimerState timerState,
                           EJBTimerService timerService) throws Exception {
     
        boolean redeliver = false;
     
        if (containerState != CONTAINER_STARTED) {
            throw new EJBException("Attempt to invoke when container is in "
                                   + containerStateToString(containerState));
        }
     
        EjbInvocation inv = invFactory.create();

        inv.isTimerCallback = true;
     
        // Let preInvoke do tx attribute lookup.
        inv.transactionAttribute = Container.TX_NOT_INITIALIZED;
     
        // There is never any client tx context so no need to do authorization.
        // If run-as is specified for the bean, it should be used.
        inv.securityPermissions = com.sun.ejb.Container.SEC_UNCHECKED;
     
        inv.method = getTimeoutMethod(timerState); 
        inv.beanMethod = inv.method;

        prepareEjbTimeoutParams(inv, timerState, timerService);

        // Delegate to subclass for i.ejbObject / i.isLocal setup.
        doTimerInvocationInit(inv, timerState);
     
        ClassLoader originalClassLoader = null;
     
        try {
            originalClassLoader = Utility.setContextClassLoader(loader);

            preInvoke(inv);

            // AroundTimeout interceptors will be checked for timeout methods
            intercept(inv);
     
            if( !isBeanManagedTran && (transactionManager.getStatus() ==
                                       Status.STATUS_MARKED_ROLLBACK) ) {
                redeliver = true;
                _logger.log(Level.FINE, "ejbTimeout called setRollbackOnly");
            }
     
        } catch(InvocationTargetException ite) {
            // A runtime exception thrown from ejbTimeout, independent of
            // its transactional setting(CMT, BMT, etc.), should result in
            // a redelivery attempt.  The instance that threw the runtime
            // exception will be destroyed, as per the EJB spec.
            redeliver = true;
            inv.exception = ite.getCause();
            _logger.log(Level.FINE, "ejbTimeout threw Runtime exception",
                       inv.exception);
        } catch(Throwable c) {
            redeliver = true;
            _logger.log(Level.FINE, "Exception while processing ejbTimeout", c);
            inv.exception = c;
        } finally {

            // Only call postEjbTimeout if there are no errors so far.
            if( !redeliver ) {
                boolean success =                 
                    timerService.postEjbTimeout(timerState.getTimerId());
                redeliver = !success;
            }
            
            postInvoke(inv);

            // If transaction commit fails, set redeliver flag.
            if( (redeliver == false) && (inv.exception != null) ) {
                redeliver = true;
            }

            if( originalClassLoader != null ) {
                Utility.setContextClassLoader(originalClassLoader);
            }

        }
     
        return redeliver;
    }

    Method getTimeoutMethod(RuntimeTimerState timerState) {
        Method m = scheduleIds.get(timerState.getTimerId());
        return (m != null) ? m : ejbTimeoutMethod;
    }

    void prepareEjbTimeoutParams(EjbInvocation inv, RuntimeTimerState timerState,
                           EJBTimerService timerService) {
        // Create a TimerWrapper for AroundTimeout and as a method argument.
        javax.ejb.Timer timer  = new TimerWrapper(timerState.getTimerId(),
                                            timerService);
        inv.timer = timer;

        if (inv.method.getParameterTypes().length == 1) {
            Object[] args  = { timer };
            inv.methodParams = args;
        } else {
            inv.methodParams = null;
        }
    }
     
    final void onEnteringContainer() {
        ejbProbeNotifier.ejbContainerEnteringEvent(getContainerId(),
                containerInfo.appName, containerInfo.modName, 
                containerInfo.ejbName);
        //callFlowAgent.startTime(ContainerTypeOrApplicationType.EJB_CONTAINER);
    }

    final void onLeavingContainer() {
        ejbProbeNotifier.ejbContainerLeavingEvent(getContainerId(),
                containerInfo.appName, containerInfo.modName, 
                containerInfo.ejbName);
        //callFlowAgent.endTime();
    }

    final void onEjbMethodStart(int methodIndex) {
        Method method = ejbIntfMethods[methodIndex];
        if (method != null) {
            onEjbMethodStart(method);
        }
    }

    final void onEjbMethodEnd(int methodIndex, Throwable th) {
        Method method = ejbIntfMethods[methodIndex];
        if (method != null) {
            onEjbMethodEnd(method, th);
        }
    }

    final void onEjbMethodStart(Method method) {
        ejbProbeNotifier.ejbMethodStartEvent(getContainerId(),
                callFlowInfo.getApplicationName(),
                callFlowInfo.getModuleName(),
                callFlowInfo.getComponentName(),
                method);
        //callFlowAgent.ejbMethodStart(callFlowInfo);
    }
    
    final void onEjbMethodEnd(Method method, Throwable th) {
        ejbProbeNotifier.ejbMethodEndEvent(getContainerId(),
                callFlowInfo.getApplicationName(),
                callFlowInfo.getModuleName(),
                callFlowInfo.getComponentName(),
                th,
                method);
        //callFlowAgent.ejbMethodEnd(callFlowInfo);
    }
    
    Object invokeTargetBeanMethod(Method beanClassMethod, EjbInvocation inv, Object target,
            Object[] params, com.sun.enterprise.security.SecurityManager mgr)
            throws Throwable {
        try {
            onEjbMethodStart(inv.method);
            if (inv.useFastPath) {
                return inv.getBeanMethod().invoke(inv.ejb, inv.methodParams);
            } else {

                return securityManager.invoke(beanClassMethod, inv.isLocal, target,
                        params);
            }
        } catch (InvocationTargetException ite) {
            inv.exception = ite.getCause();
            throw ite;
        } catch(Throwable c) {
            inv.exception = c;
            throw c;
        } finally {
            onEjbMethodEnd(inv.method, inv.exception);
        }
    }
    
    /**
     * This is implemented by concrete containers that support TimedObjects.
     */
    void doTimerInvocationInit(EjbInvocation inv, RuntimeTimerState timerState )
        throws Exception {
        throw new EJBException("This container doesn't support TimedObjects");
    }
    
    /**
     * Undeploy event.
     * Code must be able to gracefully handle redundant undeploy/shutdown
     * calls for the same container intance.
     * 
     */
    public final void undeploy() {

        try {
        
            if ( !isUndeployed() ) {
            
                setUndeployedState();

                try {
                   destroyTimers();
                } catch(Exception e) {
                    _logger.log(Level.FINE, "Error destroying timers for " +
                                ejbDescriptor.getName(), e);
                }

                // Shutdown with undeploy
                doConcreteContainerShutdown(true);

                // BaseContainer cleanup
                doContainerCleanup();
            }
        } catch(Throwable t) {
            // Make sure we don't propagate an exception since that could
            // prevent the cleanup of some other component.
            _logger.log(Level.FINE, "BsaeContainer::undeploy exception", t);
        }

    }

    /**
     * Container shutdown event. This happens for every kind of
     * shutdown other than undeploy.  It could mean the server
     * is shutting down or that the app has been disabled while
     * the server is still running.  The two cases are handled
     * the same. We must be able to gracefully handle redundant
     * shutdown calls for the same container intance.
     */
    public final void onShutdown() {

        try {
            if ( !isStopped() ) {
            
                setStoppedState();

                // Cleanup without undeploy
                doConcreteContainerShutdown(false);

                // BaseContainer cleanup
                doContainerCleanup();
            }
        } catch(Throwable t) {
            // Make sure we don't propagate an exception since that could
            // prevent the cleanup of some other component.
            _logger.log(Level.FINE, "BsaeContainer::onShutdown exception", t);
        }
    }

    // Concrete container shutdown actions
    protected abstract void doConcreteContainerShutdown(boolean appBeingUndeployed);

    /**
     * Perform common container shutdown actions.  NOTE that this should be done
     * defensively so that we attempt to do as much cleanup as possible, even
     * in the face of errors.  This might be called after
     * an unsuccessful deployment, in which case some of the services might
     * not have been initialized.
     */


    private void doContainerCleanup() {

        if( baseContainerCleanupDone ) {
            return;
        }

        try {
            if( isWebServiceEndpoint && (webServiceEndpoint != null) ) {
                String endpointAddress =
                    webServiceEndpoint.getEndpointAddressUri();
                if (wsejbEndpointRegistry != null) {
                    wsejbEndpointRegistry.unregisterEndpoint(endpointAddress);
                }
            }

            // NOTE : Pipe cleanup that used to done here is now encapsulated within
            // endpoint registry unregisterEndpoint operation

        } catch(Exception e) {
            _logger.log(Level.FINE, "Error unregistering ejb endpoint for " +
                        ejbDescriptor.getName(), e);
        }


        if( hasAsynchronousInvocations ) {
            EjbAsyncInvocationManager asyncManager =
                ((EjbContainerUtilImpl) ejbContainerUtilImpl).getEjbAsyncInvocationManager();
            asyncManager.cleanupContainerTasks(this);
        }
        

        final Thread currentThread = Thread.currentThread();
        final ClassLoader previousClassLoader =
            currentThread.getContextClassLoader();

        // Unpublish all portable and non-portable JNDI names
        for(Map.Entry<String, JndiInfo> entry : jndiInfoMap.entrySet()) {
            JndiInfo jndiInfo = entry.getValue();

            try {
                jndiInfo.unpublish(this.namingManager);
            } catch(Exception e) {
                _logger.log(Level.FINE, "Error while unbinding JNDI name " + jndiInfo.name +
                        " for EJB : " + this.ejbDescriptor.getName(), e);
             }
        }

        try {
            if(System.getSecurityManager() == null) {
                currentThread.setContextClassLoader(loader);
            } else {
                java.security.AccessController.doPrivileged(
                        new java.security.PrivilegedAction() {
                    public java.lang.Object run() {
                        currentThread.setContextClassLoader(loader);
                        return null;
                    }
                });
            }

            if ( isRemote ) {
                try {

                    if( hasRemoteHomeView ) {

                        remoteHomeRefFactory.destroyReference(ejbHomeStub,
                                                              ejbHome);

                        // Hints to release stub-related meta-data in ORB
                        remoteHomeRefFactory.cleanupClass(homeIntf);
                        remoteHomeRefFactory.cleanupClass(remoteIntf);
                        remoteHomeRefFactory.cleanupClass(ejbHome.getClass());
                        remoteHomeRefFactory.cleanupClass(ejbObjectProxyClass);
                        
                        // destroy the factory itself
                        remoteHomeRefFactory.destroy();
                    }

                    if( hasRemoteBusinessView ) {

                        // Home related cleanup
                        RemoteReferenceFactory remoteBusinessRefFactory =
                        remoteBusinessIntfInfo.values().iterator().
                            next().referenceFactory;
                        remoteBusinessRefFactory.destroyReference
                            (ejbRemoteBusinessHomeStub, ejbRemoteBusinessHome);

                        remoteBusinessRefFactory.cleanupClass(remoteBusinessHomeIntf);
                        remoteBusinessRefFactory.cleanupClass(ejbRemoteBusinessHome.getClass());

                        // Cleanup for each remote business interface
                        for(RemoteBusinessIntfInfo next : remoteBusinessIntfInfo.values()) {

                            next.referenceFactory.cleanupClass(next.generatedRemoteIntf);

                            next.referenceFactory.cleanupClass(next.proxyClass);
                        
                            // destroy the factory itself
                            next.referenceFactory.destroy();
                        }
      
                    }
                    
                } catch ( Exception ex ) {
                    _logger.log(Level.FINE, "ejb.undeploy_exception", 
                        logParams);
                    _logger.log(Level.FINE, "", ex);
                }
            }

	        try {
		        ejbContainerUtilImpl.getComponentEnvManager().
                            unbindFromComponentNamespace(ejbDescriptor);
	        } catch (javax.naming.NamingException namEx) {
		        _logger.log(Level.FINE, "ejb.undeploy_exception",
                        logParams);
		        _logger.log(Level.FINE, "", namEx);
	        }

            ejbContainerUtilImpl.unregisterContainer(this);
            
            unregisterProbeListeners();

        } finally {
            if(System.getSecurityManager() == null) {
                currentThread.setContextClassLoader(previousClassLoader);
            } else {
                java.security.AccessController.doPrivileged(
                        new java.security.PrivilegedAction() {
                    public java.lang.Object run() {
                        currentThread.setContextClassLoader(previousClassLoader);
                        return null;
                    }
                });
            }
        }


        baseContainerCleanupDone = true;
        
        _logger.log(Level.FINE, "**** [BaseContainer]: Successfully Undeployed " +
                    ejbDescriptor.getName() + " ...");
        

    }

    private void unregisterProbeListeners() {
        registryMediator.undeploy();
        registryMediator = null;
        ejbMethodStatsManager = null;
        try {
            ejbProbeListener.unregister();
            ProbeProviderFactory probeFactory = ejbContainerUtilImpl.getProbeProviderFactory();
            probeFactory.unregisterProbeProvider(ejbProbeNotifier);

            if (timerProbeListener != null) {
                timerProbeListener.unregister();
                probeFactory.unregisterProbeProvider(timerProbeNotifier);
            }
            if (poolProbeListener != null) {
                poolProbeListener.unregister();
            }
            if (cacheProbeListener != null) {
                cacheProbeListener.unregister();
                if (cacheProbeNotifier != null) {
                    probeFactory.unregisterProbeProvider(cacheProbeNotifier);
                }
            }
        } catch (Exception ex) {
            if (_logger.isLoggable(Level.FINE)) {
                _logger.log(Level.FINE, "Error unregistering the ProbeProvider");
            }
        }
    }
    /**
     * Called when server instance is Ready
     */
    public void onReady() {}

    
    /**
     * Called when server instance is terminating. This method is the last
     * one called during server shutdown.
     */
    public void onTermination() {}
    
    
    /***************************************************************************
     * The following methods implement transaction management machinery
     * in a reusable way for both SessionBeans and EntityBeans
     **************************************************************************/
    
    /**
     * This is called from preInvoke before every method invocation
     * on the EJB instance, including ejbCreate, ejbFind*, ejbRemove.
     * Also called from MessageBeanContainer, WebServiceInvocationHandler, etc,
     * so we can't assume that BaseContainer.preInvoke(EjbInvocation) has run.
     * Therefore, handle inv.invocationInfo defensively since it might not have
     * been initialized.
     */
    protected final void preInvokeTx(EjbInvocation inv)
        throws Exception
    {
        Method method = inv.method;       

        if (inv.invocationInfo==null) {

            inv.invocationInfo = getInvocationInfo(inv);

            if( inv.invocationInfo == null ) {
                throw new EJBException("EjbInvocation Info lookup failed for " +
                                       "method " + inv.method);
            } else {
                inv.transactionAttribute = inv.invocationInfo.txAttr;
            }
        }
        
        // Get existing Tx status: this tells us if the client
        // started a transaction which was propagated on this invocation.
        Integer preInvokeTxStatus = inv.getPreInvokeTxStatus();
        int status = (preInvokeTxStatus != null) ?
            preInvokeTxStatus.intValue() : transactionManager.getStatus();
        
        //For MessageDrivenBeans,ejbCreate/ejbRemove must be called without a Tx.
        // For SessionBeans, ejbCreate/ejbRemove must be called without a Tx.
        // For EntityBeans, ejbCreate/ejbRemove/ejbFind must be called with a Tx
        // so no special work needed.
        if ( (isStatefulSession || isStatelessSession) &&
             !inv.invocationInfo.isBusinessMethod ) {
            // EJB2.0 section 7.5.7 says that ejbCreate/ejbRemove etc are called
            // without a Tx. So suspend the client's Tx if any.
            
            // Note: ejbRemove cannot be called when EJB is associated with
            // a Tx, according to EJB2.0 section 7.6.4. This check is done in
            // the container's implementation of removeBean().
            
            if ( status != Status.STATUS_NO_TRANSACTION ) {
                // client request is associated with a Tx
                try {
                    inv.clientTx = transactionManager.suspend();
                } catch (SystemException ex) {
                    throw new EJBException(ex);
                }
            }
            return;
        }
        
        // isNullTx is true if the client sent a null tx context
        // (i.e. a tx context with a null Coordinator objref)
        // or if this server's tx interop mode flag is false.
        // Follow the tables in EJB2.0 sections 19.6.2.2.1 and 19.6.2.2.2.
        boolean isNullTx = false;
        if (inv.isRemote) {
            isNullTx = transactionManager.isNullTransaction();
        }
        
        int txAttr = getTxAttr(inv);
        
        EJBContextImpl context = (EJBContextImpl)inv.context;
        
        // Note: in the code below, inv.clientTx is set ONLY if the
        // client's Tx is actually suspended.
        
        // get the Tx associated with the EJB from previous invocation,
        // if any.
        Transaction prevTx = context.getTransaction();
        
        switch (txAttr) {
            case TX_BEAN_MANAGED:
                // TX_BEAN_MANAGED rules from EJB2.0 Section 17.6.1, Table 13
                // Note: only MDBs and SessionBeans can be TX_BEAN_MANAGED
                if ( status != Status.STATUS_NO_TRANSACTION ) {
                    // client request associated with a Tx, always suspend
                    inv.clientTx = transactionManager.suspend();
                }
                if ( isStatefulSession && prevTx != null
                && prevTx.getStatus() != Status.STATUS_NO_TRANSACTION ) {
                    // Note: if prevTx != null , then it means
                    // afterCompletion was not called yet for the
                    // previous transaction on the EJB.
                    
                    // The EJB was previously associated with a Tx which was
                    // begun by the EJB itself in a previous invocation.
                    // This is only possible for stateful SessionBeans
                    // not for StatelessSession or Entity.
                    transactionManager.resume(prevTx);
                    
                    // This allows the TM to enlist resources
                    // used by the EJB with the transaction
                    transactionManager.enlistComponentResources();
                }
                
                break;
                
            case TX_NOT_SUPPORTED:
                if ( status != Status.STATUS_NO_TRANSACTION )
                    inv.clientTx = transactionManager.suspend();
                checkUnfinishedTx(prevTx, inv);
                if ( isEntity )
                    preInvokeNoTx(inv);
                break;
                
            case TX_MANDATORY:
                if ( isNullTx || status == Status.STATUS_NO_TRANSACTION )
                    throw new TransactionRequiredLocalException();
                
                useClientTx(prevTx, inv);
                break;
                
            case TX_REQUIRED:
                if ( isNullTx )
                    throw new TransactionRequiredLocalException();
                
                if ( status == Status.STATUS_NO_TRANSACTION ) {
                    inv.clientTx = null;
                    startNewTx(prevTx, inv);
                }
                else { // There is a client Tx
                    inv.clientTx = transactionManager.getTransaction();
                    useClientTx(prevTx, inv);
                }
                break;
                
            case TX_REQUIRES_NEW:
                if ( status != Status.STATUS_NO_TRANSACTION )
                    inv.clientTx = transactionManager.suspend();
                startNewTx(prevTx, inv);
                break;
                
            case TX_SUPPORTS:
                if ( isNullTx )
                    throw new TransactionRequiredLocalException();
                
                if ( status != Status.STATUS_NO_TRANSACTION )
                    useClientTx(prevTx, inv);
                else { // we need to invoke the EJB with no Tx.
                    checkUnfinishedTx(prevTx, inv);
                    if ( isEntity )
                        preInvokeNoTx(inv);
                }
                break;
                
            case TX_NEVER:
                if ( isNullTx || status != Status.STATUS_NO_TRANSACTION )
                    throw new EJBException(
                        "EJB cannot be invoked in global transaction");
                
                else { // we need to invoke the EJB with no Tx.
                    checkUnfinishedTx(prevTx, inv);
                    if ( isEntity )
                        preInvokeNoTx(inv);
                }
                break;
                
            default:
                throw new EJBException("Bad transaction attribute");
        }
    }
    
    
    // Called before invoking a bean with no Tx or with a new Tx.
    // Check if the bean is associated with an unfinished tx.
    protected void checkUnfinishedTx(Transaction prevTx, EjbInvocation inv) {
        try {
            if ( !isMessageDriven && !isStatelessSession && !isSingleton && (prevTx != null) &&
            	(prevTx.getStatus() != Status.STATUS_NO_TRANSACTION) ) {
                // An unfinished tx exists for the bean.
                // so we cannot invoke the bean with no Tx or a new Tx.
                throw new IllegalStateException(
                    "Bean is associated with a different unfinished transaction");
            }
        } catch (SystemException ex) {
            _logger.log(Level.FINE, "ejb.checkUnfinishedTx_exception", ex);
            throw new EJBException(ex);
        }
    }
    
    
    private void startNewTx(Transaction prevTx, EjbInvocation inv)
        throws Exception
    {
        checkUnfinishedTx(prevTx, inv);
        
        if (cmtTimeoutInSeconds > 0) {
            transactionManager.begin(cmtTimeoutInSeconds);
        } else {
            transactionManager.begin();
        }
        
        EJBContextImpl context = (EJBContextImpl)inv.context;
        Transaction tx = transactionManager.getTransaction();
        if (! isSingleton) {
            context.setTransaction(tx);
        }
        
        // This allows the TM to enlist resources used by the EJB
        // with the transaction
        transactionManager.enlistComponentResources();
        
        // register synchronization for methods other than finders/home methods
        Method method = inv.method;
        if ( !inv.invocationInfo.isHomeFinder ) {
            // Register for Synchronization notification
            ejbContainerUtilImpl.getContainerSync(tx).addBean(context);
        }
        
        // Call afterBegin/ejbLoad. If ejbLoad throws exceptions,
        // the completeNewTx machinery called by postInvokeTx
        // will rollback the tx. Since we have already registered
        // a Synchronization object with the TM, the afterCompletion
        // will get called.
        afterBegin(context);
    }
    
    // Called from preInvokeTx before invoking the bean with the client's Tx
    // Also called from EntityContainer.removeBean for cascaded deletes
    protected void useClientTx(Transaction prevTx, EjbInvocation inv) {
        Transaction clientTx;
        int status=-1;
        int prevStatus=-1;
        try {
            // Note: inv.clientTx will not be set at this point.
            clientTx = transactionManager.getTransaction();
            status = clientTx.getStatus();  // clientTx cant be null
            if ( prevTx != null )
                prevStatus = prevTx.getStatus();
        } catch (Exception ex) {
            try {
                transactionManager.setRollbackOnly();
            } catch ( Exception e ) { 
				//FIXME: Use LogStrings.properties
				_logger.log(Level.FINEST, "", e); 
			} 
            throw new TransactionRolledbackLocalException("", ex);
        }
        
        // If the client's tx is going to rollback, it is fruitless
        // to invoke the EJB, so throw an exception back to client.
        if ( status == Status.STATUS_MARKED_ROLLBACK
        || status == Status.STATUS_ROLLEDBACK
        || status == Status.STATUS_ROLLING_BACK )
            throw new TransactionRolledbackLocalException(
                "Client's transaction aborted");
        
        if( isStatefulSession ) {

            SessionContextImpl sessionCtx = (SessionContextImpl) inv.context;
            Map<EntityManagerFactory, EntityManager> entityManagerMap =
                sessionCtx.getExtendedEntityManagerMap();

            JavaEETransaction clientJ2EETx = (JavaEETransaction) clientTx;
            for(EntityManagerFactory emf : entityManagerMap.keySet()) {

                // Make sure there is no Transactional persistence context
                // for the same EntityManagerFactory as this SFSB's 
                // Extended persistence context for the propagated transaction.
                if( clientJ2EETx.getTxEntityManager(emf) != null ) {
                    throw new EJBException("There is an active transactional persistence context for the same EntityManagerFactory as the current stateful session bean's extended persistence context");
                }

                // Now see if there's already a *different* extended 
                // persistence context within this transaction for the 
                // same EntityManagerFactory.
                EntityManager em = clientJ2EETx.getExtendedEntityManager(emf);
                if( (em != null) && entityManagerMap.get(emf) != em ) {
                    throw new EJBException("Detected two different extended persistence contexts for the same EntityManagerFactory within a transaction");
                }

            }
            
        }

        if ( prevTx == null
        || prevStatus == Status.STATUS_NO_TRANSACTION ) {
            // First time the bean is running in this new client Tx
            EJBContextImpl context = (EJBContextImpl)inv.context;

            //Must change this for singleton
            if (! isSingleton) {
                context.setTransaction(clientTx);
            }
            try {
                transactionManager.enlistComponentResources();
                
                if ( !isStatelessSession && !isMessageDriven && !isSingleton) {
                    // Create a Synchronization object.
                    
                    // Not needed for stateless beans or message-driven beans
                    // or singletons because they cant have Synchronization callbacks,
                    // and they cant be associated with a tx across
                    // invocations.
                    // Register sync for methods other than finders/home methods
                    Method method = inv.method;
                    if ( !inv.invocationInfo.isHomeFinder ) {
                        ejbContainerUtilImpl.getContainerSync(clientTx).addBean(
                        context);
                    }
                    
                    afterBegin(context);
                }
            } catch (Exception ex) {
                try {
                    transactionManager.setRollbackOnly();
                } catch ( Exception e ) { 
					//FIXME: Use LogStrings.properties
					_logger.log(Level.FINEST, "", e); 
				} 
                throw new TransactionRolledbackLocalException("", ex);
            }
        }
        else { // Bean already has a transaction associated with it.
            if ( !prevTx.equals(clientTx) ) {
                // There is already a different Tx in progress !!
                // Note: this can only happen for stateful SessionBeans.
                // EntityBeans will get a different context for every Tx.
                if ( isSession ) {
                    // Row 2 in Table E
                    throw new IllegalStateException(
                    "EJB is already associated with an incomplete transaction");
                }
            }
            else { // Bean was invoked again with the same transaction
                // This allows the TM to enlist resources used by the EJB
                // with the transaction
                try {
                    transactionManager.enlistComponentResources();
                } catch (Exception ex) {
                    try {
                        transactionManager.setRollbackOnly();
                    } catch ( Exception e ) { 
                        //FIXME: Use LogStrings.properties
                        _logger.log(Level.FINEST, "", e); 
					}
                    throw new TransactionRolledbackLocalException("", ex);
                }
            }
        }
    }
    
    
    /**
     * postInvokeTx is called after every invocation on the EJB instance,
     * including ejbCreate/ejbFind---/ejbRemove.
     * NOTE: postInvokeTx is called even if the EJB was not invoked
     * because of an exception thrown from preInvokeTx.
     */
    public void postInvokeTx(EjbInvocation inv)
        throws Exception
    {
        
        InvocationInfo invInfo = inv.invocationInfo;
        Throwable exception = inv.exception;
        
        // For SessionBeans, ejbCreate/ejbRemove was called without a Tx,
        // so resume client's Tx if needed.
        // For EntityBeans, ejbCreate/ejbRemove/ejbFind must be called with a Tx
        // so no special processing needed.
        if ( (isStatefulSession || isStatelessSession) && !invInfo.isBusinessMethod ) {
            // check if there was a suspended client Tx
            if ( inv.clientTx != null )
                transactionManager.resume(inv.clientTx);
            
            if ( exception != null
                 && exception instanceof PreInvokeException ) {
                inv.exception = ((PreInvokeException)exception).exception;
            }
            
            return;
        }
        
        EJBContextImpl context = (EJBContextImpl)inv.context;
        
        int status = transactionManager.getStatus();
        int txAttr = inv.invocationInfo.txAttr;
        
        Throwable newException = exception; // default
        
        // Note: inv.exception may have been thrown by the container
        // during preInvoke (i.e. bean may never have been invoked).
        
        // Exception and Tx handling rules. See EJB2.0 Sections 17.6, 18.3.
        switch (txAttr) {
            case TX_BEAN_MANAGED:
                // EJB2.0 section 18.3.1, Table 16
                // Note: only SessionBeans can be TX_BEAN_MANAGED
                newException = checkExceptionBeanMgTx(context, exception,
                status);
                if ( inv.clientTx != null ) {
                    // there was a client Tx which was suspended
                    transactionManager.resume(inv.clientTx);
                }
                break;
                
            case TX_NOT_SUPPORTED:
            case TX_NEVER:
                // NotSupported and Never are handled in the same way
                // EJB2.0 sections 17.6.2.1, 17.6.2.6.
                // EJB executed in no Tx
                if ( exception != null )
                    newException = checkExceptionNoTx(context, exception);
                if ( isEntity )
                    postInvokeNoTx(inv);
                
                if ( inv.clientTx != null ) {
                    // there was a client Tx which was suspended
                    transactionManager.resume(inv.clientTx);
                }
                
                break;
                
            case TX_MANDATORY:
                // EJB2.0 section 18.3.1, Table 15
                // EJB executed in client's Tx
                if ( exception != null )
                    newException = checkExceptionClientTx(context, exception);
                break;
                
            case TX_REQUIRED:
                // EJB2.0 section 18.3.1, Table 15
                if ( inv.clientTx == null ) {
                    // EJB executed in new Tx started in preInvokeTx
                    newException = completeNewTx(context, exception, status);
                }
                else {
                    // EJB executed in client's tx
                    if ( exception != null ) {
                        newException = checkExceptionClientTx(context,
                        exception);
                    }
                }
                break;
                
            case TX_REQUIRES_NEW:
                // EJB2.0 section 18.3.1, Table 15
                // EJB executed in new Tx started in preInvokeTx
                newException = completeNewTx(context, exception, status);
                
                if ( inv.clientTx != null ) {
                    // there was a client Tx which was suspended
                    transactionManager.resume(inv.clientTx);
                }
                break;
                
            case TX_SUPPORTS:
                // EJB2.0 section 18.3.1, Table 15
                if ( status != Status.STATUS_NO_TRANSACTION ) {
                    // EJB executed in client's tx
                    if ( exception != null ) {
                        newException = checkExceptionClientTx(context,
                        exception);
                    }
                }
                else {
                    // EJB executed in no Tx
                    if ( exception != null )
                        newException = checkExceptionNoTx(context, exception);
                    if ( isEntity )
                        postInvokeNoTx(inv);
                }
                break;
                
            default:
        }
        
        inv.exception = newException;
        
        // XXX If any of the TM commit/rollback/suspend calls throws an
        // exception, should the transaction be rolled back if not already so ?
    }
    
    
    private Throwable checkExceptionBeanMgTx(EJBContextImpl context,
            Throwable exception, int status)
        throws Exception
    {
        Throwable newException = exception;
        // EJB2.0 section 18.3.1, Table 16
        if ( exception != null
        && exception instanceof PreInvokeException ) {
            // A PreInvokeException was thrown, so bean was not invoked
            newException= ((PreInvokeException)exception).exception;
        }
        else if ( status == Status.STATUS_NO_TRANSACTION ) {
            // EJB was invoked, EJB's Tx is complete.
            if ( exception != null )
                newException = checkExceptionNoTx(context, exception);
        }
        else {
            // EJB was invoked, EJB's Tx is incomplete.
            // See EJB2.0 Section 17.6.1
            if ( isStatefulSession ) {
                if ( !isSystemUncheckedException(exception) ) {
                    if( isAppExceptionRequiringRollback(exception) ) {
                        transactionManager.rollback();
                    } else {
                        transactionManager.suspend();
                    }
                }
                else {
                    // system/unchecked exception was thrown by EJB
                    try {
                        forceDestroyBean(context);
                    } finally {
                        transactionManager.rollback();
                    }
                    newException = processSystemException(exception);
                }
            }
            else if( isStatelessSession  ) { // stateless SessionBean
                try {
                    forceDestroyBean(context);
                } finally {
                    transactionManager.rollback();
                }
                newException = new EJBException(
                    "Stateless SessionBean method returned without" + 
                    " completing transaction");
                _logger.log(Level.FINE,
                    "ejb.incomplete_sessionbean_txn_exception",logParams);
                _logger.log(Level.FINE,"",newException);
            }
            else if( isSingleton ) {
                try {
                    forceDestroyBean(context);
                } finally {
                    transactionManager.rollback();
                }
                newException = new EJBException(
                    "Singleton SessionBean method returned without" + 
                    " completing transaction");
                _logger.log(Level.FINE,
                    "ejb.incomplete_sessionbean_txn_exception",logParams);
                _logger.log(Level.FINE,"",newException);
            }
            else { // MessageDrivenBean
                try {
                    forceDestroyBean(context);
                } finally {
                    transactionManager.rollback();
                }
                newException = new EJBException(
                    "MessageDrivenBean method returned without" + 
                    " completing transaction");
                _logger.log(Level.FINE,
                    "ejb.incomplete_msgdrivenbean_txn_exception",logParams);
                _logger.log(Level.FINE,"",newException.toString());
                
            }
        }
        return newException;
    }
    
    private Throwable checkExceptionNoTx(EJBContextImpl context, 
        Throwable exception)
        throws Exception
    {
        if ( exception instanceof PreInvokeException )
            // A PreInvokeException was thrown, so bean was not invoked
            return ((PreInvokeException)exception).exception;
        
        // If PreInvokeException was not thrown, EJB was invoked with no Tx
        Throwable newException = exception;
        if ( isSystemUncheckedException(exception) ) {
            // Table 15, EJB2.0
            newException = processSystemException(exception);
            forceDestroyBean(context);
        }
        return newException;
    }
    
    // this is the counterpart of useClientTx
    // Called from postInvokeTx after invoking the bean with the client's Tx
    // Also called from EntityContainer.removeBean for cascaded deletes
    protected Throwable checkExceptionClientTx(EJBContextImpl context,
        Throwable exception)
        throws Exception
    {
        if ( exception instanceof PreInvokeException )
            // A PreInvokeException was thrown, so bean was not invoked
            return ((PreInvokeException)exception).exception;
        
        // If PreInvokeException wasn't thrown, EJB was invoked with client's Tx
        Throwable newException = exception;
        if ( isSystemUncheckedException(exception) ) {
            // Table 15, EJB2.0
            try {
                forceDestroyBean(context);
            } finally {
                transactionManager.setRollbackOnly();
            }
            if ( exception instanceof Exception ) {
                newException = new TransactionRolledbackLocalException(
                	"Exception thrown from bean", (Exception)exception);
            } else {
                newException = new TransactionRolledbackLocalException(
                	"Exception thrown from bean: "+exception.toString());
                newException.initCause(exception);
            }
        } else if( isAppExceptionRequiringRollback(exception ) ) {
            transactionManager.setRollbackOnly();
        }

        return newException;
    }
    
    // this is the counterpart of startNewTx
    private Throwable completeNewTx(EJBContextImpl context, Throwable exception, int status)
        throws Exception
    {
        Throwable newException = exception;
        if ( exception instanceof PreInvokeException )
            newException = ((PreInvokeException)exception).exception;
        
        if ( status == Status.STATUS_NO_TRANSACTION ) {
            // no tx was started, probably an exception was thrown
            // before tm.begin() was called
            return newException;
        }

        if ( isStatefulSession && (context instanceof SessionContextImpl)) {
            ((SessionContextImpl) context).setTxCompleting(true);
        }

        // A new tx was started, so we must commit/rollback
        if ( newException != null
        && isSystemUncheckedException(newException) ) {
            // EJB2.0 section 18.3.1, Table 15
            // Rollback the Tx we started
            try {
                forceDestroyBean(context);
            } finally {
                transactionManager.rollback();
            }
            newException = processSystemException(newException);
        }
        else {
            try {
                if ( status == Status.STATUS_MARKED_ROLLBACK ) {
                    // EJB2.0 section 18.3.1, Table 15, and 18.3.6:
                    // rollback tx, no exception
                    if (transactionManager.isTimedOut()) {
                        _logger.log(Level.WARNING, "ejb.tx_timeout",
                                    new Object[] { 
                            transactionManager.getTransaction(),
                                ejbDescriptor.getName()});
                    }
                    transactionManager.rollback();
                }
                else {
                    if( (newException != null) &&
                        isAppExceptionRequiringRollback(newException) ) {
                        transactionManager.rollback();
                    } else {
                        // Note: if exception is an application exception
                        // we do a commit as in EJB2.0 Section 18.3.1, 
                        // Table 15. Commit the Tx we started
                        transactionManager.commit();
                    }
                }
            } catch (RollbackException ex) {
                _logger.log(Level.FINE, "ejb.transaction_abort_exception", ex);
                // EJB2.0 section 18.3.6
                newException = new EJBException("Transaction aborted", ex);
            } catch ( Exception ex ) {
                _logger.log(Level.FINE, "ejb.cmt_exception", ex);
                // Commit or rollback failed.
                // EJB2.0 section 18.3.6
                newException = new EJBException("Unable to complete" +
                    " container-managed transaction.", ex);
            }
        }
        return newException;
    }
    
    
    
    // Implementation of Container method.
    // Called from UserTransactionImpl after the EJB started a Tx,
    // for TX_BEAN_MANAGED EJBs only.
    public final void doAfterBegin(ComponentInvocation ci) {
        EjbInvocation inv = (EjbInvocation)ci;
        try {
            // Associate the context with tx so that on subsequent
            // invocations with the same tx, we can do the appropriate
            // tx.resume etc.
            EJBContextImpl sc = (EJBContextImpl)inv.context;
            Transaction tx = transactionManager.getTransaction();
            if (! isSingleton) {
                sc.setTransaction(tx);
            }
            
            // Register Synchronization with TM so that we can
            // dissociate the context from tx in afterCompletion
            ejbContainerUtilImpl.getContainerSync(tx).addBean(sc);
                                                                   
            enlistExtendedEntityManagers(sc);
            // Dont call container.afterBegin() because
            // TX_BEAN_MANAGED EntityBeans are not allowed,
            // and SessionSync calls on TX_BEAN_MANAGED SessionBeans
            // are not allowed.
        } catch (SystemException ex) {
            throw new EJBException(ex);
        } catch (RollbackException ex) {
            throw new EJBException(ex);
        } catch (IllegalStateException ex) {
            throw new EJBException(ex);
        }
    }
    
    // internal APIs, called from ContainerSync, implemented in subclasses
    abstract void afterBegin(EJBContextImpl context);
    abstract void beforeCompletion(EJBContextImpl context);
    abstract void afterCompletion(EJBContextImpl context, int status);
    
    void preInvokeNoTx(EjbInvocation inv) {
        throw new EJBException(
            "Internal Error: BaseContainer.preInvokeNoTx called");
    }
    
    void postInvokeNoTx(EjbInvocation inv) {
        throw new EJBException(
            "Internal Error: BaseContainer.postInvokeNoTx called");
    }
    
    private Throwable processSystemException(Throwable sysEx) {
        Throwable newException;
        if ( sysEx instanceof EJBException)
            return sysEx;
        
        // EJB2.0 section 18.3.4
        if ( sysEx instanceof NoSuchEntityException ) { // for EntityBeans only
            newException = new NoSuchObjectLocalException
                ("NoSuchEntityException thrown by EJB method.");
            newException.initCause(sysEx);
        } else {
            newException = new EJBException();
            newException.initCause(sysEx);
        }

        return newException;
    }
    
    protected boolean isApplicationException(Throwable exception) {
        return !isSystemUncheckedException(exception);
    }

    protected boolean isSystemUncheckedException(Throwable exception) {
        if ( exception != null &&
             ( exception instanceof RuntimeException
               || exception instanceof Error
               || exception instanceof RemoteException ) ) {

            Class clazz = exception.getClass();
            String exceptionClassName = clazz.getName();
            Map<String, EjbApplicationExceptionInfo> appExceptions = 
                    ejbDescriptor.getEjbBundleDescriptor().getApplicationExceptions();
            while (clazz != null) {
                String eClassName = clazz.getName();
                if (appExceptions.containsKey(eClassName)) {
                    if( exceptionClassName.equals(eClassName)) {
                        // Exact exception is specified as an ApplicationException
                        return false;
                    } else {
                        // Superclass exception is not inherited
                        return !appExceptions.get(eClassName).getInherited();
                    }
                }
                clazz = clazz.getSuperclass();
            }

            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns true if this exception is an Application Exception and
     * it requires rollback of the transaction in which it was thrown.
     */
    protected boolean isAppExceptionRequiringRollback
        (Throwable exception) {

        boolean appExceptionRequiringRollback = false;

        if ( exception != null ) {

            Class clazz = exception.getClass();
            String exceptionClassName = clazz.getName();
            Map<String, EjbApplicationExceptionInfo> appExceptions = 
                    ejbDescriptor.getEjbBundleDescriptor().getApplicationExceptions();
            while (clazz != null) {
                String eClassName = clazz.getName();
                if (appExceptions.containsKey(eClassName)) {
                    if( exceptionClassName.equals(eClassName) ||
                            appExceptions.get(eClassName).getInherited() == true) {
                        // Exact exception is specified as an ApplicationException
                        // or superclass exception is inherited
                        appExceptionRequiringRollback = appExceptions.get(eClassName).getRollback();
                    }
                    break;
                }
                clazz = clazz.getSuperclass();
            }
        }

        return appExceptionRequiringRollback;
    }
    
    public void setMonitorOn(boolean flag) {
        monitorOn = flag;
    }
    
    public boolean getDebugMonitorFlag() {
        return debugMonitorFlag;
    }
    
    public void setDebugMonitorFlag(boolean flag) {
        debugMonitorFlag = flag;
    }
    
    protected static final String containerStateToString(int state) {
        switch (state) {
            case CONTAINER_INITIALIZING:
                return "Initializing";
            case CONTAINER_STARTED:
                return "Started";
            case CONTAINER_STOPPED:
                return "STOPPED";
            case CONTAINER_UNDEPLOYED:
                return "Undeployed";
            case CONTAINER_ON_HOLD:
                return "ON_HOLD";
        }
        return "Unknown Container state: " + state;
    }

    protected final boolean isRemoteInterfaceSupported() {
        return hasRemoteHomeView;
    }

    protected final boolean isLocalInterfaceSupported() {
        return hasLocalHomeView;
    }
    

    /**
     * Called from various places within the container that are responsible
     * for dispatching invocations to business methods.  This method has
     * the exception semantics of Method.invoke().  Any exception that
     * originated from the business method or application code within an
     * interceptor will be propagated as the cause within an 
     * InvocationTargetException.
     * 
     */
    Object intercept(EjbInvocation inv)
        throws Throwable
    {
        Object result = null;
        if (inv.mustInvokeAsynchronously()) {
            EjbAsyncInvocationManager asyncManager =
                    ((EjbContainerUtilImpl) ejbContainerUtilImpl).getEjbAsyncInvocationManager();
            Future future = inv.isLocal ?
                    asyncManager.createLocalFuture(inv) :
                    asyncManager.createRemoteFuture(inv, this, (GenericEJBHome) ejbRemoteBusinessHomeStub );
            result = (inv.invocationInfo.method.getReturnType() == void.class)
                    ? null : future;
        }  else {
            result = __intercept(inv);
        }
        return result;
    }

    private Object __intercept(EjbInvocation inv)
        throws Throwable
    {
        Object result = null;
        if (interceptorManager.hasInterceptors()) {
            try {
                onEjbMethodStart(inv.method);
                result = interceptorManager.intercept(inv.getInterceptorChain(), inv);
            } catch(Throwable t) {
                inv.exception = t;
                throw new InvocationTargetException(t);
            } finally {
                onEjbMethodEnd(inv.method,  inv.exception);
            }
        } else { // invoke() has the same exc. semantics as Method.invoke
            result = this.invokeTargetBeanMethod(inv.getBeanMethod(), inv, inv.ejb,
                    inv.methodParams, null);
        }

        return result;
    }

    /**
     * Called from Interceptor Chain to invoke the actual bean method.
     * This method must throw any exception from the bean method *as is*,
     * without being wrapped in an InvocationTargetException.  The exception
     * thrown from this method will be propagated through the application's
     * interceptor code, so it must not be changed in order for any exception
     * handling logic in that code to function properly.
     */
    public Object invokeBeanMethod(EjbInvocation inv)
        throws Throwable
    {
        try {

            return securityManager.invoke(inv.getBeanMethod(), inv.isLocal, inv.ejb,
                                       inv.getParameters());
          
        } catch(InvocationTargetException ite) {
            throw ite.getCause();
        }
    }
    
    protected abstract EjbMonitoringStatsProvider getMonitoringStatsProvider(
            String appName, String modName, String ejbName);

    private MonitoredObjectType getEJBMonitoredObjectType()
    {
        MonitoredObjectType type    = MonitoredObjectType.NONE;
        /*TODO
        final Class<? extends BaseContainer>  thisClass = this.getClass();
        
        if ( StatelessSessionContainer.class.isAssignableFrom( thisClass ) )
        {
            type    = MonitoredObjectType.STATELESS_BEAN;
        }
        else if ( StatefulSessionContainer.class.isAssignableFrom( thisClass ) )
        {
            type    = MonitoredObjectType.STATEFUL_BEAN;
        }
        else if ( EntityContainer.class.isAssignableFrom( thisClass )  )
        {
            type    = MonitoredObjectType.ENTITY_BEAN;
        }
        else if ( MessageBeanContainer.class.isAssignableFrom( thisClass ) )
        {
            type    = MonitoredObjectType.MESSAGE_DRIVEN_BEAN;
        }
        else
        {
            throw new RuntimeException( "getEJBMonitoredObjectType: unknown: " + this.getClass().getName() );
        }
        */
        return type;
    }


    
    protected void createMonitoringRegistryMediator() {
	String appName = null;
	String modName = null;
	String ejbName = null;
	try {
	    appName = (ejbDescriptor.getApplication().isVirtual())
		? null: ejbDescriptor.getApplication().getRegistrationName();
	    if (appName == null) {
		modName = ejbDescriptor.getApplication().getRegistrationName();
	    } else {
		String archiveuri = ejbDescriptor.getEjbBundleDescriptor().
		    getModuleDescriptor().getArchiveUri();
		modName = 
		    com.sun.enterprise.util.io.FileUtils.makeFriendlyFilename(archiveuri);
	    }
	    ejbName = ejbDescriptor.getName();
            containerInfo = new ContainerInfo(appName, modName, ejbName);

	    this.registryMediator =
		new MonitoringRegistryMediator( getEJBMonitoredObjectType(), ejbName, modName, appName);

	    this.ejbMethodStatsManager = registryMediator.getEJBMethodStatsManager();

            ejbProbeListener = getMonitoringStatsProvider(appName, modName, ejbName);
            ejbProbeListener.addMethods(getContainerId(), appName, modName, ejbName, getMonitoringMethodsArray());
            ejbProbeListener.register();

	    _logger.log(Level.FINE, "Created MonitoringRegistryMediator: appName: "
                + appName + "; modName: " + modName + "; ejbName: " + ejbName);
	} catch (Exception ex) {
	    _logger.log(Level.SEVERE, "[**BaseContainer**] Could not create MonitorRegistryMediator. appName: " + appName + "; modName: " + modName + "; ejbName: " + ejbName, ex);
	}

        // Always create to avoid NPE
        try {
            ProbeProviderFactory probeFactory = ejbContainerUtilImpl.getProbeProviderFactory();
            String invokerId = EjbMonitoringUtils.getInvokerId(appName, modName, ejbName);
            ejbProbeNotifier = probeFactory.getProbeProvider(EjbMonitoringProbeProvider.class, invokerId);
            if (_logger.isLoggable(Level.FINE)) {
                _logger.log(Level.FINE, "Got ProbeProvider: " + ejbProbeNotifier.getClass().getName());
            }
        } catch (Exception ex) {
            ejbProbeNotifier = new EjbMonitoringProbeProvider();
            if (_logger.isLoggable(Level.FINE)) {
                _logger.log(Level.FINE, "Error getting the EjbMonitoringProbeProvider");
            }
        }

    }

    protected void populateMethodMonitorMap() {
        boolean hasGeneratedClasses = (monitoredGeneratedClasses.size() > 0);
        Method[] methods = getMonitoringMethodsArray(hasGeneratedClasses);
        populateMethodMonitorMap(methods, hasGeneratedClasses);

    }

    protected Method[] getMonitoringMethodsArray() {
        return getMonitoringMethodsArray((monitoredGeneratedClasses.size() > 0));
    }

    protected Method[] getMonitoringMethodsArray(boolean hasGeneratedClasses) {
        Method[] methods = null;
        if (hasGeneratedClasses) {
            List<Method> methodList = new ArrayList<Method>();
            for (Class clz : monitoredGeneratedClasses) {
                for (Method m : clz.getDeclaredMethods()) {
                    methodList.add(m);
                }
            }
            methods = methodList.toArray(new Method[0]);
        } else {
            Vector methodVec = ejbDescriptor.getMethods();
            int sz = methodVec.size();
            methods = new Method[sz];
            for (int i = 0; i < sz; i++) {
                methods[i] = (Method) methodVec.get(i);
            }
        }

        return methods;
    }

    protected void populateMethodMonitorMap(Method[] methods) {
        populateMethodMonitorMap(methods, false);
    }
    
    protected void populateMethodMonitorMap(Method[] methods,
            boolean prefixClassName) {
	/*
	methodMonitorMap = new HashMap();
	MethodMonitor[] methodMonitors = new MethodMonitor[methods.length];
	for (int i=0; i<methods.length; i++ ) {
	    methodMonitors[i] = new MethodMonitor(methods[i]);
	    methodMonitorMap.put(methods[i], methodMonitors[i]);
	}
	
	registryMediator.registerProvider(methodMonitors);
	*/
	registryMediator.registerEJBMethods(methods, prefixClassName);
	_logger.log(Level.FINE, "[Basecontainer] Registered Method Monitors");
    }

    void logMonitoredComponentsData() {
	registryMediator.logMonitoredComponentsData(
	    _logger.isLoggable(Level.FINE));
    }

    protected void doFlush( EjbInvocation inv ) {
    }

    protected void registerMonitorableComponents() {
        createMonitoringRegistryMediator();
        registerTimerMonitorableComponent();
    }

    protected void registerTimerMonitorableComponent() {
        if( isTimedObject() ) {
            //toMonitorProps = new TimedObjectMonitorableProperties();
	    //registryMediator.registerProvider( toMonitorProps );
            String invokerId = EjbMonitoringUtils.getInvokerId(containerInfo.appName, containerInfo.modName, containerInfo.ejbName);
            try {
                ProbeProviderFactory probeFactory = ejbContainerUtilImpl.getProbeProviderFactory();
                timerProbeNotifier = probeFactory.getProbeProvider(EjbTimedObjectProbeProvider.class, invokerId);
                if (_logger.isLoggable(Level.FINE)) {
                    _logger.log(Level.INFO, "Got TimerProbeProvider: " + timerProbeNotifier.getClass().getName());
                }
            } catch (Exception ex) {
                timerProbeNotifier = new EjbTimedObjectProbeProvider();
                if (_logger.isLoggable(Level.FINE)) {
                    _logger.log(Level.INFO, "Error getting the TimerProbeProvider");
                }
            }
            timerProbeListener = new EjbTimedObjectStatsProvider(
                    containerInfo.appName, containerInfo.modName, containerInfo.ejbName, getContainerId());
            timerProbeListener.register();
	}
        _logger.log(Level.FINE, "[BaseContainer] registered timer monitorable");
    }

    protected void incrementCreatedTimedObject() {
        timerProbeNotifier.ejbTimerCreatedEvent();
        //toMonitorProps.incrementTimersCreated();
    }

    protected void incrementRemovedTimedObject() {
        timerProbeNotifier.ejbTimerRemovedEvent();
        //toMonitorProps.incrementTimersRemoved();
    }

    protected void incrementDeliveredTimedObject() {
        timerProbeNotifier.ejbTimerDeliveredEvent();
        //toMonitorProps.incrementTimersDelivered();
    }

    private static class JndiInfo {

        private JndiInfo(String name, Object object) {
            this.name = name;
            this.object = object;
        }

        static JndiInfo newPortableLocal(String name, Object obj) {
            JndiInfo jndiInfo = new JndiInfo(name, obj);
            jndiInfo.local = true;
            jndiInfo.portable = true;
            return jndiInfo;
        }

        static JndiInfo newPortableRemote(String name, Object obj) {
            JndiInfo jndiInfo = new JndiInfo(name, obj);
            jndiInfo.local = false;
            jndiInfo.portable = true;
            jndiInfo.cosNaming = isCosNamingObject(obj);
            return jndiInfo;
        }

        static JndiInfo newNonPortableRemote(String name, Object obj) {
            JndiInfo jndiInfo = new JndiInfo(name, obj);
            jndiInfo.local = false;
            jndiInfo.portable = false;
            jndiInfo.cosNaming = isCosNamingObject(obj);
            return jndiInfo;
        }



        void publish(GlassfishNamingManager nm) throws NamingException {

            // If it's a portable name, use rebind since the name is guaranteed
            // to be unique.  Otherwise, use bind() so we detect any clashes.
            // NOTE : Will need to revisit this if we allow a developer-specified
            // portable JNDI name.
            boolean rebind = portable;

            if( cosNaming ) {
                nm.publishCosNamingObject(name, object, rebind);
            } else {
                nm.publishObject(name, object, rebind);
            }

            publishedSuccessfully = true;

        }

        void unpublish(GlassfishNamingManager nm) throws NamingException {

            if( publishedSuccessfully ) {
                if( cosNaming ) {
                    nm.unpublishCosNamingObject(name);
                } else {
                    nm.unpublishObject(name);
                }
            } else {
                _logger.log(Level.FINE, "Skipping unpublish of " + name + " because it was " +
                           "never published successfully in the first place");
            }
        }

        public void setInternal(boolean flag) {
            internal = flag;

        }

        private static boolean isCosNamingObject(Object obj) {
            return ((obj instanceof java.rmi.Remote) ||
                    (obj instanceof org.omg.CORBA.Object));
        }

        String name;
        Object object;
        boolean local;
        boolean cosNaming;
        boolean portable;
        boolean internal;
        boolean publishedSuccessfully;


    }

} //BaseContainer{}

final class CallFlowInfoImpl
    implements CallFlowInfo
{
    
    private final BaseContainer container;
    
    private final EjbDescriptor ejbDescriptor;
    
    private final String appName;
    
    private final String modName;
    
    private final String ejbName;
    
    private final ComponentType componentType;
    
    CallFlowInfoImpl(BaseContainer container, EjbDescriptor descriptor,
            ComponentType compType) {
        this.container = container;
        this.ejbDescriptor = descriptor;
        
        this.appName = (ejbDescriptor.getApplication().isVirtual()) ? null
                : ejbDescriptor.getApplication().getRegistrationName();
        String archiveuri = ejbDescriptor.getEjbBundleDescriptor()
                .getModuleDescriptor().getArchiveUri();
        this.modName = com.sun.enterprise.util.io.FileUtils
                .makeFriendlyFilename(archiveuri);
        this.ejbName = ejbDescriptor.getName();
        
        this.componentType = compType;
    }
    
    public String getApplicationName() {
        return appName;
    }
    
    public String getModuleName() {
        return modName;
    }
    
    public String getComponentName() {
        return ejbName;
    }
    
    public ComponentType getComponentType() {
        return componentType;
    }
    
    public java.lang.reflect.Method getMethod() {
        EjbInvocation inv = (EjbInvocation)
            EjbContainerUtilImpl.getInstance().getCurrentInvocation();
        
        return inv.method;
    }
    
    public String getTransactionId() {
        JavaEETransaction tx = null;
        try {
            tx =
                (JavaEETransaction) EjbContainerUtilImpl.getInstance().
                        getTransactionManager().getTransaction();
        } catch (Exception ex) {
            //TODO: Log exception
        }
        
        return (tx == null) ? null : ""+tx; //TODO tx.getTransactionId();
    }
    
    public String getCallerPrincipal() {
        java.security.Principal principal = 
                container.getSecurityManager().getCallerPrincipal();
        
        return (principal != null) ? principal.getName() : null;
    }
    
    public Throwable getException() {
        return ((EjbInvocation) EjbContainerUtilImpl.getInstance().getCurrentInvocation()).exception;
    }
}

final class RemoteBusinessIntfInfo {

    Class generatedRemoteIntf;
    Class remoteBusinessIntf;
    String jndiName;
    RemoteReferenceFactory referenceFactory;
    Class proxyClass;
    Constructor proxyCtor;

}


/**
 * PreInvokeException is used to wrap exceptions thrown
 * from BaseContainer.preInvoke, so it indicates that the bean's
 * method will not be called.
 */
final class PreInvokeException extends EJBException {
    
    Exception exception;
    
    PreInvokeException(Exception ex) {
        this.exception = ex;
    }
} //PreInvokeException{}

final class SafeProperties extends Properties {
    private static final String errstr =
    	"Environment properties cannot be modified";
    private static final String ejb10Prefix = "ejb10-properties/";
    
    public void load(java.io.InputStream inStream) {
        throw new RuntimeException(errstr);
    }
    public Object put(Object key, Object value) {
        throw new RuntimeException(errstr);
    }
    public void putAll(Map t) {
        throw new RuntimeException(errstr);
    }
    public Object remove(Object key) {
        throw new RuntimeException(errstr);
    }
    public void clear() {
        throw new RuntimeException(errstr);
    }
    void copy(Set s) {
        Iterator i = s.iterator();
        defaults = new Properties();
        while ( i.hasNext() ) {
            EnvironmentProperty p = (EnvironmentProperty)i.next();
            if ( p.getName().startsWith(ejb10Prefix) ) {
                String newName = p.getName().substring(ejb10Prefix.length());
                defaults.put(newName, p.getValue());
            }
        }
    }
    
    private void readObject(java.io.ObjectInputStream stream)
        throws java.io.IOException, ClassNotFoundException
    {
        defaults = (Properties)stream.readObject();
    }
    
    private void writeObject(java.io.ObjectOutputStream stream)
        throws java.io.IOException
    {
        stream.writeObject(defaults);
    }
} //SafeProperties{}

/** TODO: remove
final class TimedObjectMonitorableProperties 
    implements com.sun.ejb.spi.stats.EJBTimedObjectStatsProvider
    {

    long timersCreated    = 0;
    long timersRemoved    = 0;
    long timersDelivered  = 0;
    boolean toMonitor     = false;

    public TimedObjectMonitorableProperties() {
        timersCreated    = 0;
        timersRemoved    = 0;
        timersDelivered  = 0;
        toMonitor        = false;
    } 

    public void incrementTimersCreated() {
        if( toMonitor ) {
            synchronized( this ) {
                timersCreated++;
            }
        }
    }

    public long getTimersCreated() {
        return timersCreated;
    }

    public void incrementTimersRemoved() {
        if( toMonitor ) {
            synchronized( this ) {
                timersRemoved++;
            }
        }
    }

    public long getTimersRemoved() {
        return timersRemoved;
    }

    public void incrementTimersDelivered() {
        if( toMonitor ) {
            synchronized( this ) {
                timersDelivered++;
            }
        }
    }

    public long getTimersDelivered() {
        return timersDelivered;
    }

    public void appendStats(StringBuffer sbuf) {
	sbuf.append("[Timers: ")
	    .append("Created=").append(timersCreated).append("; ")
	    .append("Removed=").append(timersRemoved).append("; ")
	    .append("Delivered=").append(timersDelivered).append("; ");
	sbuf.append("]");
    }

    public void monitoringLevelChanged( boolean monitoringOn ) {
        timersCreated   = 0;
        timersRemoved   = 0;
        timersDelivered = 0;
        toMonitor       = monitoringOn;
    }

} //TimedObjectMonitorableProperties{}
**/

final class ContainerInfo {
    String appName;
    String modName;
    String ejbName;

    ContainerInfo(String appName, String modName, String ejbName) {
        this.appName = appName;
        this.modName = modName;
        this.ejbName = ejbName;
    }
}
