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
package org.glassfish.admin.amx.impl.j2ee;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import java.io.OutputStream;
import java.io.ByteArrayOutputStream;

import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.MBeanServerNotification;
import javax.management.Notification;
import javax.management.NotificationListener;

import javax.management.ObjectName;

import com.sun.enterprise.deployment.Application;
import com.sun.enterprise.deployment.archivist.Archivist;
import com.sun.enterprise.deployment.io.DeploymentDescriptorFile;
import com.sun.enterprise.deployment.ApplicationClientDescriptor;
import com.sun.enterprise.deployment.BundleDescriptor;
import com.sun.enterprise.deployment.ConnectorDescriptor;
import com.sun.enterprise.deployment.EjbBundleDescriptor;
import com.sun.enterprise.deployment.EjbDescriptor;
import com.sun.enterprise.deployment.EjbSessionDescriptor;
import com.sun.enterprise.deployment.WebBundleDescriptor;
import com.sun.enterprise.deployment.WebComponentDescriptor;
import com.sun.enterprise.deployment.io.DescriptorConstants;
import com.sun.enterprise.deployment.util.XModuleType;


import org.glassfish.admin.amx.config.AMXConfigProxy;
import org.glassfish.admin.amx.core.Util;
import org.glassfish.admin.amx.impl.j2ee.loader.J2EEInjectedValues;
import org.glassfish.admin.amx.impl.util.ImplUtil;
import org.glassfish.admin.amx.impl.util.ObjectNameBuilder;
import org.glassfish.admin.amx.intf.config.AMXConfigGetters;
import org.glassfish.admin.amx.intf.config.ApplicationRef;
import org.glassfish.admin.amx.intf.config.ResourceRef;
import org.glassfish.admin.amx.intf.config.Server;
import org.glassfish.admin.amx.j2ee.EJB;
import org.glassfish.admin.amx.j2ee.EJBModule;
import org.glassfish.admin.amx.j2ee.EntityBean;
import org.glassfish.admin.amx.j2ee.J2EEApplication;
import org.glassfish.admin.amx.j2ee.J2EEManagedObject;

import org.glassfish.admin.amx.j2ee.MessageDrivenBean;
import org.glassfish.admin.amx.j2ee.Servlet;
import org.glassfish.admin.amx.j2ee.StatefulSessionBean;
import org.glassfish.admin.amx.j2ee.StatelessSessionBean;
import org.glassfish.admin.amx.j2ee.SingletonSessionBean;
import org.glassfish.admin.amx.j2ee.WebModule;
import org.glassfish.admin.amx.util.ClassUtil;
import org.glassfish.admin.amx.util.MapUtil;
import org.glassfish.admin.amx.util.jmx.JMXUtil;


import java.io.EOFException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import org.glassfish.admin.amx.core.proxy.ProxyFactory;
import org.glassfish.admin.amx.intf.config.Domain;
import org.glassfish.admin.amx.j2ee.AppClientModule;
import org.glassfish.admin.amx.j2ee.J2EEServer;
import org.glassfish.admin.amx.j2ee.ResourceAdapter;
import org.glassfish.admin.amx.j2ee.ResourceAdapterModule;
import org.glassfish.internal.data.ApplicationInfo;
import org.glassfish.internal.data.ApplicationRegistry;
import com.sun.enterprise.deployment.archivist.ArchivistFactory;

/**
    Handles registrations of resources and applications associated with a J2EEServer.
    There must be one and only one of these instances per J2EEServer.
 */
final class RegistrationSupport
{
    private static void cdebug(Object o)
    {
        System.out.println("" + o);
    }

    /**
        Associates the ObjectName of a ResourceRef or ApplicationRef with its corresponding
        top-level JSR 77 MBean. Children of those JSR 77 MBeans come and go with their parent.
     */
    private final Map<ObjectName, ObjectName> mConfigRefTo77 = new HashMap<ObjectName, ObjectName>();

    private final J2EEServer mJ2EEServer;

    private final MBeanServer mMBeanServer;

    private final ProxyFactory mProxyFactory;

    private final RefListener mResourceRefListener;

    /** The Server config for this J2EEServer */
    private final Server mServerConfig;
    
    /** type of any resource ref */
    private final String mResourceRefType;
    
    /** type of any application ref */
    private final String mApplicationRefType;
    
    public RegistrationSupport(final J2EEServer server)
    {
        mJ2EEServer = server;
        mMBeanServer = (MBeanServer) server.extra().mbeanServerConnection();
        mProxyFactory = server.extra().proxyFactory();

        mResourceRefType    = Util.deduceType(ResourceRef.class);
        mApplicationRefType = Util.deduceType(ApplicationRef.class);
        mServerConfig = getDomainConfig().getServers().getServer().get( mJ2EEServer.getName() );

        final ObjectName test = mJ2EEServer.objectName();

        mResourceRefListener = new RefListener();

        registerApplications();
    }

    protected void cleanup()
    {
        mResourceRefListener.stopListening();
    }

    public void start()
    {
        mResourceRefListener.startListening();
    }

    /** Maps configuration MBean type to J2EE type */
    public static final Map<String, Class> CONFIG_RESOURCE_TYPES =
            MapUtil.toMap(new Object[]
            {
                "jdbc-resource", JDBCResourceImpl.class,
                "java-mail-resource", JavaMailResourceImpl.class,
                "jca-resource", JCAResourceImpl.class,
                "jms-resource", JMSResourceImpl.class,
                "jndi-resource", JNDIResourceImpl.class,
                "jta-resource", JTAResourceImpl.class,
                "rmi-iiop-resource", RMI_IIOPResourceImpl.class,
                "url-resource", URLResourceImpl.class
            },
            String.class, Class.class);

    private Domain getDomainConfig()
    {
        return new AMXConfigGetters(mJ2EEServer).domainConfig();
    }
    
    

    private String getDeploymentDescriptor(
        final BundleDescriptor bundleDesc )
    {
        final ArchivistFactory archivistFactory = J2EEInjectedValues.getInstance().getArchivistFactory();
        
        String dd = "unavailable";
        ByteArrayOutputStream out = null;
        try
        {
            final Archivist moduleArchivist = archivistFactory.getArchivist(bundleDesc.getModuleDescriptor().getModuleType());
            final DeploymentDescriptorFile ddFile =  moduleArchivist.getStandardDDFile();
            
            out = new ByteArrayOutputStream();
            ddFile.write(bundleDesc, out);
            final String charsetName = "UTF-8";
            dd = out.toString(charsetName);
        }
        catch( final Exception e )
        {
            dd = null;
        }
        finally
        {
            if ( out != null )
            {
                try { out.close(); } catch( Exception ee) {}
            }
        }
                
        return dd;
    }
  
    private ObjectName createAppMBeans(
        org.glassfish.admin.amx.intf.config.Application appConfig,
        final Application application,
        final MetadataImpl meta)
    {
        final String appLocation = appConfig.getLocation();

        final boolean isStandalone = application.isVirtual();
        ObjectName parentMBean = null;
        ObjectName top = null;
        if (isStandalone)
        {
            parentMBean = mJ2EEServer.objectName();
        }
        else
        {
            final String xmlDesc = getDeploymentDescriptor(application);
            if ( xmlDesc != null )
            {
                meta.setDeploymentDescriptor(xmlDesc);
            }
            parentMBean = registerJ2EEChild(mJ2EEServer.objectName(), meta, J2EEApplication.class, J2EEApplicationImpl.class, application.getName());
            top = parentMBean;
        }

        for (final EjbBundleDescriptor desc : application.getEjbBundleDescriptors())
        {
            final ObjectName objectName = registerEjbModuleAndItsComponents(parentMBean, meta, appConfig, desc);
            if (isStandalone)
            {
                assert (top == null);
                top = objectName;
            }
        }

        for (final WebBundleDescriptor desc : application.getWebBundleDescriptors())
        {
            final ObjectName objectName = registerWebModuleAndItsComponents(parentMBean, meta, appConfig, desc);
            if (isStandalone)
            {
                assert (top == null);
                top = objectName;
            }
        }

        for (final ConnectorDescriptor desc : application.getRarDescriptors())
        {
            assert top == null;
            top = registerResourceAdapterModuleAndItsComponents(parentMBean, meta, appConfig, desc, appLocation);
        }

        for (final ApplicationClientDescriptor desc : application.getApplicationClientDescriptors())
        {
            assert top == null;
            top = registerAppClient(parentMBean, meta, appConfig, desc);
        }

        ImplUtil.getLogger().fine("Registered JSR 77 MBeans for application/module: " + top);
        return top;
    }
    
        private org.glassfish.admin.amx.intf.config.Module
    getModuleConfig( final org.glassfish.admin.amx.intf.config.Application appConfig, final String name)
    {
        final Map<String, org.glassfish.admin.amx.intf.config.Module> modules =
            appConfig.childrenMap(org.glassfish.admin.amx.intf.config.Module.class);
        
        if ( modules.get(name) == null )
        {
            throw new IllegalArgumentException( "Can't find module named " + name + " in " + appConfig );
        }
        
        return modules.get(name);
    }

    /* Register ejb module and its' children ejbs which is part of an application */
    private ObjectName registerEjbModuleAndItsComponents(
            final ObjectName parentMBean,
            final MetadataImpl meta,
            final org.glassfish.admin.amx.intf.config.Application appConfig,
            final EjbBundleDescriptor ejbBundleDescriptor )
    {
        final String xmlDesc = getDeploymentDescriptor(ejbBundleDescriptor);
        if ( xmlDesc != null )
        {
            meta.setDeploymentDescriptor( xmlDesc );
        }
        final String moduleName = ejbBundleDescriptor.getModuleName();
        final String appLocation = appConfig.getLocation();
        
        final AMXConfigProxy moduleConfig = getModuleConfig(appConfig, moduleName );
        meta.setCorrespondingConfig(moduleConfig.objectName());
        
        final ObjectName ejbModuleObjectName = registerJ2EEChild(parentMBean, meta, EJBModule.class, EJBModuleImpl.class, moduleName);
        
        meta.remove( Metadata.CORRESPONDING_CONFIG );   // none for an EJB MBean
        meta.remove( Metadata.DEPLOYMENT_DESCRIPTOR );   // none for an EJB MBean
        for (final EjbDescriptor desc : ejbBundleDescriptor.getEjbs())
        {
            final ObjectName ejbObjectName = createEJBMBean(ejbModuleObjectName, meta, desc);
        }
        return ejbModuleObjectName;
    }

    private ObjectName createEJBMBean(
            final ObjectName parentMBean,
            final MetadataImpl meta,
            final EjbDescriptor ejbDescriptor)
    {
        final String ejbName = ejbDescriptor.getName();
        final String ejbType = ejbDescriptor.getType();
        final String ejbSessionType = ejbType.equals("Session") ? ((EjbSessionDescriptor) ejbDescriptor).getSessionType() : null;

        Class<? extends EJB> intf = null;
        Class<? extends EJBImplBase> impl = null;
        if (ejbType.equals("Entity"))
        {
            intf = EntityBean.class;
            impl = EntityBeanImpl.class;
        }
        else if (ejbType.equals("Message-driven"))
        {
            intf = MessageDrivenBean.class;
            impl = MessageDrivenBeanImpl.class;
        }
        else if (ejbType.equals("Session"))
        {
            if (ejbSessionType.equals("Stateless"))
            {
                intf = StatelessSessionBean.class;
                impl = StatelessSessionBeanImpl.class;
            }
            else if (ejbSessionType.equals("Stateful"))
            {
                intf = StatefulSessionBean.class;
                impl = StatefulSessionBeanImpl.class;
            }
            else if (ejbSessionType.equals("Singleton")) // EJB 3.1
            {
                intf = SingletonSessionBean.class;
                impl = SingletonSessionBeanImpl.class;
            }
            else
            {
                throw new IllegalArgumentException( "Unknown ejbSessionType: " + ejbSessionType + ", expected Stateless or Stateful");
            }
        }

        return registerJ2EEChild(parentMBean, meta, intf, impl, ejbName);
    }

    /* Register web module and its' children which is part of an application */
    private ObjectName registerWebModuleAndItsComponents(
            final ObjectName parentMBean,
            final MetadataImpl meta,
            final org.glassfish.admin.amx.intf.config.Application appConfig,
            final WebBundleDescriptor webBundleDescriptor )
    {
        final String xmlDesc = getDeploymentDescriptor(webBundleDescriptor);
        if ( xmlDesc != null )
        {
            meta.setDeploymentDescriptor( xmlDesc );
        }
        
        final String moduleName = webBundleDescriptor.getModuleName();
        final String appLocation = appConfig.getLocation();
        
        final AMXConfigProxy moduleConfig = getModuleConfig(appConfig, moduleName );
        meta.setCorrespondingConfig(moduleConfig.objectName());

        final ObjectName webModuleObjectName = registerJ2EEChild(parentMBean, meta, WebModule.class, WebModuleImpl.class, moduleName);

        meta.remove( Metadata.CORRESPONDING_CONFIG );   // none for a Servlet
        meta.remove( Metadata.DEPLOYMENT_DESCRIPTOR );   // none for an Servlet
        for (final WebComponentDescriptor desc : webBundleDescriptor.getWebComponentDescriptors())
        {
            final String servletName = desc.getCanonicalName();
            
            final ObjectName on = registerJ2EEChild(webModuleObjectName, meta, Servlet.class, ServletImpl.class, servletName);
        }

        return webModuleObjectName;
    }

    public ObjectName registerResourceAdapterModuleAndItsComponents(
            final ObjectName parentMBean,
            final MetadataImpl meta,
            final org.glassfish.admin.amx.intf.config.Application appConfig,
            final ConnectorDescriptor bundleDesc,
            final String appLocation)
    {
        meta.setCorrespondingConfig(appConfig.objectName());
        final ObjectName objectName = createRARModuleMBean(parentMBean, meta, appConfig, bundleDesc);
        
        final AMXConfigProxy moduleConfig = getModuleConfig(appConfig, bundleDesc.getModuleName() );
        meta.setCorrespondingConfig(moduleConfig.objectName());
        
        final ObjectName rarObjectName = registerJ2EEChild(objectName, meta, ResourceAdapter.class, ResourceAdapterImpl.class, bundleDesc.getName());

        return objectName;
    }

    private ObjectName createRARModuleMBean(
            final ObjectName parentMBean,
            final MetadataImpl meta,
            final org.glassfish.admin.amx.intf.config.Application appConfig,
            final ConnectorDescriptor bundleDesc )
    {
        final String appLocation = appConfig.getLocation();
        
        final String xmlDesc = getDeploymentDescriptor(bundleDesc);
        if ( xmlDesc != null )
        {
            meta.setDeploymentDescriptor( xmlDesc );
        }
        final String resAdName = bundleDesc.getModuleName();

        final ObjectName objectName = registerJ2EEChild(parentMBean, meta, ResourceAdapterModule.class, ResourceAdapterModuleImpl.class, resAdName);

        return objectName;
    }

    /* Register application client module */
    public ObjectName registerAppClient(
            final ObjectName parentMBean,
            final MetadataImpl meta,
            final org.glassfish.admin.amx.intf.config.Application appConfig,
            final ApplicationClientDescriptor bundleDesc)
    {
        final String appLocation = appConfig.getLocation();
        final String xmlDesc = getDeploymentDescriptor(bundleDesc);
        if ( xmlDesc != null )
        {
            meta.setDeploymentDescriptor( xmlDesc );
        }

        final String moduleName = bundleDesc.getModuleDescriptor().getModuleName();

        return registerJ2EEChild(parentMBean, meta, AppClientModule.class, AppClientModuleImpl.class, moduleName);
    }

  
    protected void registerApplications()
    {
        final Map<String, ApplicationRef> appRefConfigs = mServerConfig.getApplicationRef();
        for (final ApplicationRef ref : appRefConfigs.values())
        {
            try
            {
                final ObjectName objectName = processApplicationRef(ref);
            }
            catch( final Exception e )
            {
                // log it: we want to continue with other apps, even if this one had a problem
                ImplUtil.getLogger().log( Level.INFO, "Exception registering application: " + ref.getName(), e);
            }
        }
    }
    
   /**
        Examine the MBean to see if it is a ResourceRef that should be manifested under this server,
        and if so, register a JSR 77 MBean for it.
     */
    public ObjectName processApplicationRef(final ApplicationRef ref)
    {
        // find all applications
        final ApplicationRegistry appRegistry = J2EEInjectedValues.getInstance().getApplicationRegistry();

        final MetadataImpl meta = new MetadataImpl();
        meta.setCorrespondingRef(ref.objectName());
        
        final String appName = ref.getName();
        
        final ApplicationInfo appInfo = appRegistry.get(appName);
        if (appInfo == null)
        {
            ImplUtil.getLogger().fine("Unable to get ApplicationInfo for application: " + appName);
            return null;
        }
        final Application app = appInfo.getMetaData(Application.class);
        if ( app == null )
        {
            if ( appInfo.isJavaEEApp() )
            {
                ImplUtil.getLogger().warning("Null from ApplicationInfo.getMetadata(Application.class) for application " + appName );
            }
            return null;
        }
        
        final org.glassfish.admin.amx.intf.config.Application appConfig = new AMXConfigGetters(ref).getApplication(appName);
        if ( appConfig == null )
        {
            ImplUtil.getLogger().warning("Unable to get Application config for: " + appName);
            return null;
        }
        
        meta.setCorrespondingConfig( appConfig.objectName() );
        final ObjectName mbean77 = createAppMBeans(appConfig, app, meta);
        synchronized (mConfigRefTo77)
        {
            mConfigRefTo77.put(ref.objectName(), mbean77);
        }

        return mbean77;
    }


    protected <I extends J2EEManagedObject, C extends J2EEManagedObjectImplBase> ObjectName registerJ2EEChild(
            final ObjectName parent,
            final Metadata metadataIn,
            final Class<I> intf,
            final Class<C> clazz,
            final String name)
    {
        ObjectName objectName = null;
        
        final String j2eeType = Util.deduceType(intf);
        
        // must make a copy! May be an input value that is reused by caller
        final Metadata metadata = new MetadataImpl(metadataIn);
        try
        {
            final Constructor<C> c = clazz.getConstructor(ObjectName.class, Metadata.class);
            final J2EEManagedObjectImplBase impl = c.newInstance(parent, metadata);
            objectName = new ObjectNameBuilder(mMBeanServer, parent).buildChildObjectName(j2eeType, name);
            objectName = mMBeanServer.registerMBean( impl, objectName ).getObjectName();
        }
        catch (final Exception e)
        {
            throw new RuntimeException( "Cannot register " + j2eeType + "=" + name + " as child of " + parent, e);
        }

        return objectName;
    }

      
    /**
        Examine the MBean to see if it is a ResourceRef that should be manifested under this server,
        and if so, register a JSR 77 MBean for it.
     */
    public ObjectName processResourceRef(final ResourceRef ref)
    {
        if (!mResourceRefType.equals(ref.type()))
        {
            throw new IllegalArgumentException("Not a resource-ref: " + ref.objectName());
        }

        if ( ! mServerConfig.objectName().equals(ref.parent().objectName()))
        {
            cdebug("ResourceRef is not a child of server " + mServerConfig.objectName());
            return null;
        }

        // find the referenced resource
        final AMXConfigProxy amxConfig = new AMXConfigGetters(ref).getResource(ref.getName());
        if (amxConfig == null)
        {
            throw new IllegalArgumentException("ResourceRef refers to non-existent resource: " + ref);
        }

        final String configType = amxConfig.type();
        final Class<J2EEManagedObjectImplBase> implClass = CONFIG_RESOURCE_TYPES.get(configType);
        if (implClass == null)
        {
            ImplUtil.getLogger().fine("Unrecognized resource type for JSR 77 purposes: " + amxConfig.objectName());
            return null;
        }
        final Class<J2EEManagedObject> intf = (Class) ClassUtil.getFieldValue(implClass, "INTF");
        final String j2eeType = Util.deduceType(intf);

        ObjectName mbean77 = null;
        try
        {
            final MetadataImpl meta = new MetadataImpl();
            meta.setCorrespondingRef(ref.objectName());
            meta.setCorrespondingConfig(amxConfig.objectName());
            
            mbean77 = registerJ2EEChild(mJ2EEServer.objectName(), meta, intf, implClass, amxConfig.getName());
            synchronized (mConfigRefTo77)
            {
                mConfigRefTo77.put(ref.objectName(), mbean77);
            }
        }
        catch (final Exception e)
        {
            ImplUtil.getLogger().log( Level.INFO, "Can't register JSR 77 MBean for resourceRef: " + ref.objectName(), e);
        }
    //cdebug( "Registered " + child + " for  config resource " + amx.objectName() );
        return mbean77;
    }


    /**
    Listen for registration/unregistration of {@link ResourceRef},
    and associate them with JSR 77 MBeans for this J2EEServer.
    Resources belong to a J2EEServer via ResourceRefs.  So we can stay in the AMX
    world by tracking registration and unregistration of AMX config MBeans of
    type ResourceRef.
     */
    private final class RefListener implements NotificationListener
    {
        public RefListener()
        {
        }
      
        public void handleNotification(final Notification notifIn, final Object handback)
        {
            if (!(notifIn instanceof MBeanServerNotification))
            {
                return;
            }

            final MBeanServerNotification notif = (MBeanServerNotification) notifIn;
            final ObjectName objectName = notif.getMBeanName();
            if ( ! mJ2EEServer.objectName().getDomain().equals(objectName.getDomain()))
            {
                return;
            }
            
            final String type = Util.getTypeProp(objectName);

            if (notif.getType().equals(MBeanServerNotification.REGISTRATION_NOTIFICATION))
            {
                if ( type.equals( mResourceRefType ) )
                {
                    ImplUtil.getLogger().fine("New ResourceRef MBEAN registered: " + objectName);
                    final ResourceRef ref = mProxyFactory.getProxy(objectName, ResourceRef.class);
                    processResourceRef(ref);
                }
                else if ( type.equals( mApplicationRefType ) )
                {
                    ImplUtil.getLogger().fine( "NEW ApplicationRef MBEAN registered: " + objectName);
                    final ApplicationRef ref = mProxyFactory.getProxy(objectName, ApplicationRef.class);
                    processApplicationRef(ref);
                }
            }
            else if (notif.getType().equals(MBeanServerNotification.UNREGISTRATION_NOTIFICATION))
            {
                // determine if it's a config for which a JSR 77  MBean is registered
                synchronized (mConfigRefTo77)
                {
                    final ObjectName mbean77 = mConfigRefTo77.remove(objectName);
                    if (mbean77 != null)
                    {
                        ImplUtil.getLogger().fine( "Unregistering MBEAN for ref: " + objectName);
                        try
                        {
                            mMBeanServer.unregisterMBean(mbean77);
                        }
                        catch (final Exception e)
                        {
                            ImplUtil.getLogger().log( Level.WARNING, "Can't unregister MBean: " + objectName, e);
                        }
                    }
                }
            }
        }

        public void startListening()
        {
            // important: processResourceRef a listener *first* so that we don't miss anything
            try
            {
                mMBeanServer.addNotificationListener(JMXUtil.getMBeanServerDelegateObjectName(), this, null, null);
            }
            catch (final JMException e)
            {
                throw new RuntimeException(e);
            }

            // register all existing 
            final Map<String, ResourceRef> resourceRefs = mServerConfig.getResourceRef();
            for (final ResourceRef ref : resourceRefs.values())
            {
                processResourceRef(ref);
            }
        }

        public void stopListening()
        {
            try
            {
                mMBeanServer.removeNotificationListener(JMXUtil.getMBeanServerDelegateObjectName(), this);
            }
            catch (final JMException e)
            {
                throw new RuntimeException(e);
            }
        }

    }
}





















