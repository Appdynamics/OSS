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
package amxtest;

import org.testng.annotations.*;
import org.testng.Assert;

import javax.management.ObjectName;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

import org.glassfish.admin.amx.intf.config.*;
import org.glassfish.admin.amx.core.*;
import org.glassfish.admin.amx.base.*;
import org.glassfish.admin.amx.config.*;
//import org.glassfish.admin.amx.j2ee.*;
import org.glassfish.admin.amx.monitoring.*;
import org.glassfish.admin.amx.util.CollectionUtil;
import org.glassfish.admin.amx.util.ExceptionUtil;
import org.glassfish.admin.amx.util.jmx.JMXUtil;
import org.glassfish.admin.amx.logging.Logging;
import org.glassfish.admin.amx.annotation.*;


/** 
    These tests are designed to exercise the AMXProxyHandler code.
 */
//@Test(groups={"amx"}, description="AMXProxy tests", sequential=false, threadPoolSize=5)
@Test(
    sequential=false, threadPoolSize=10,
    groups =
    {
        "amx"
    },
    description = "test the functionality of AMX dymamic proxy eg AMXProxy"
)
public final class AMXProxyTests extends AMXTestBase
{
    public AMXProxyTests()
    {
    }
    
    private String attrName( final Method m )
    {
        if ( ! JMXUtil.isIsOrGetter(m) ) return null;
        
        return JMXUtil.getAttributeName(m);
    }
    
    
    private <T extends AMXProxy> void testProxyInterface(final AMXProxy amx, Class<T> clazz)
    {
        final List<String> problems = _testProxyInterface(amx, clazz);
        assert problems.size() == 0 : CollectionUtil.toString( problems, "\n" );
    }
    
    
    private <T extends AMXProxy> List<String> _testProxyInterface(final AMXProxy amxIn, Class<T> clazz)
    {
        assert amxIn != null : "_testProxyInterface(): null proxy for class " + clazz.getName();
        
        final List<String> problems = new ArrayList<String>();
        
        final T amx = amxIn.as(clazz);
        
        final String nameProp = amx.nameProp();
        assert Util.getNameProp(amx.objectName()) == nameProp;
        
        assert amx.parentPath() != null;
        assert amx.type() != null;
        assert amx.valid();
        assert amx.childrenSet() != null;
        assert amx.childrenMaps() != null;
        assert amx.attributesMap() != null;
        final Set<String> attrNames = amx.attributeNames();
        assert attrNames != null;
        final ObjectName objectName = amx.objectName();
        assert objectName != null;
        assert amx.extra() != null;
        
        final Extra extra = amx.extra();
        //assert extra.mbeanServerConnection() == getMBeanServerConnection();
        assert extra.proxyFactory() == getProxyFactory();
        assert extra.java().length() >= 100;
        
        assert extra.mbeanInfo() != null;
        assert extra.interfaceName() != null;
        assert extra.genericInterface() != null;
        assert extra.group() != null;
        assert extra.descriptor() != null;
        extra.isInvariantMBeanInfo();   // just call it
        extra.subTypes();   // just call it
        extra.supportsAdoption();   // just call it
        if ( extra.globalSingleton() )
        {
            assert extra.singleton();
        }
            
        
        final Method[] methods = clazz.getMethods();
        final Set<String> attrNamesFromMethods = new HashSet<String>();
        for( final Method m : methods )
        {
            if ( JMXUtil.isIsOrGetter(m) )
            {
                final String attrName = attrName(m);
                final ChildGetter childGetter = m.getAnnotation(ChildGetter.class);
                if ( attrNames.contains(attrName) && childGetter != null )
                {
                    println( "Warning: Attribute " + attrName +
                        " exists in " + objectName + ", but has superfluous @ChildGetter annotation" );
                }
                
                try
                {
                    final Object result = m.invoke( amx, (Object[])null);
                    
                    attrNamesFromMethods.add( attrName(m) );
                }
                catch( final Exception e )
                {
                    problems.add( "Error invoking " + m.getName() + "() on " + objectName + " = " + e );
                }
            }
        }
        if ( clazz != AMXProxy.class && clazz != AMXConfigProxy.class )
        {
            // see whether the interface is missing any getters
            final Set<String> missing = new HashSet<String>(attrNames);
            missing.removeAll(attrNamesFromMethods);
            if ( missing.size() != 0 )
            {
                println( clazz.getName() + " missing getters attributes: " + missing );
            }
        }
        return problems;
    }

    @Test
    public void testDomainRoot()
    {
        final DomainRoot dr = getDomainRootProxy();
        testProxyInterface( dr, DomainRoot.class );
        
        // sanity check:  see that the various attributes are reachable through its proxy
        assert dr.getAMXReady();
        assert dr.getDebugPort() != null;
        assert dr.getApplicationServerFullVersion() != null;
        assert dr.getInstanceRoot() != null;
        assert dr.getDomainDir() != null;
        assert dr.getConfigDir() != null;
        assert dr.getInstallDir() != null;
        assert dr.getUptimeMillis() != null;

    }

    @Test
    public void testExt()
    {
        testProxyInterface( getExt(), Ext.class );
    }

    @Test
    public void testQuery()
    {
        testProxyInterface( getQueryMgr(), Query.class );
    }

    @Test
    public void testBulkAccess()
    {
        testProxyInterface( getDomainRootProxy().getBulkAccess(), BulkAccess.class );
    }

    @Test
    public void testTools()
    {
        testProxyInterface( getDomainRootProxy().getTools(), Tools.class );
    }

    @Test
    public void testMonitoringRoot()
    {
        testProxyInterface( getDomainRootProxy().getMonitoringRoot(), MonitoringRoot.class );
    }

    @Test
    public void testRuntimeRoot()
    {
        testProxyInterface( getDomainRootProxy().getRuntime(), RuntimeRoot.class );
    }

    @Test
    public void testServerRuntime()
    {
        final RuntimeRoot runtime = getDomainRootProxy().getRuntime();
        final Map<String,ServerRuntime>  serverRuntimes = runtime.getServerRuntime();
        assert serverRuntimes.keySet().size() != 0;
        for( final ServerRuntime sr : serverRuntimes.values() )
        {
            testProxyInterface( sr, ServerRuntime.class );
        }
    }

    @Test
    public void testSystemInfo()
    {
        testProxyInterface( getDomainRootProxy().getSystemInfo(), SystemInfo.class );
    }

    @Test
    public void testLogging()
    {
        testProxyInterface( getDomainRootProxy().getLogging(), Logging.class );
    }

    @Test
    public void testPathnames()
    {
        testProxyInterface( getDomainRootProxy().getPathnames(), Pathnames.class );
    }

    @Test
    public void testDomainConfig()
    {
        final Domain dc = getDomainConfig();
        testProxyInterface( dc, Domain.class );
        
        assert dc.getApplicationRoot() != null;
        dc.getLocale(); // can be null
        assert dc.getLogRoot() != null;
    }

    @Test
    public void testApplications()
    {
        testProxyInterface( getDomainConfig().getApplications(), Applications.class );
    }

    @Test
    public void testResources()
    {
        final Resources resources = getDomainConfig().getResources();
        testProxyInterface( resources, Resources.class );
        
        final Map<String,CustomResource> cr = resources.getCustomResource();
        assert cr != null;
        
        final Map<String,JndiResource> jndi = resources.getJndiResource();
        assert jndi != null;
        
        final Map<String,JdbcResource> jdbc = resources.getJdbcResource();
        assert jdbc != null;
        
        final Map<String,JdbcConnectionPool> jdbcCP = resources.getJdbcConnectionPool();
        assert jdbcCP != null;
        
        final Map<String,ConnectorResource> connR = resources.getConnectorResource();
        assert connR != null;
        
        final Map<String,ConnectorConnectionPool> ccp = resources.getConnectorConnectionPool();
        assert ccp != null;
        
        final Map<String,AdminObjectResource> ao = resources.getAdminObjectResource();
        assert ao != null;
        
        final Map<String,ResourceAdapter> ra = resources.getResourceAdapter();
        assert ra != null;
        
        final Map<String,MailResource> mr = resources.getMailResource();
        assert mr != null;
    }

    @Test
    public void testConfigs()
    {
        testProxyInterface( getDomainConfig().getConfigs(), Configs.class );
    }

    @Test
    public void testSystemApplications()
    {
        testProxyInterface( getDomainConfig().getSystemApplications(), SystemApplications.class );
    }

    @Test
    public void testServers()
    {
        testProxyInterface( getDomainConfig().getServers(), Servers.class );
    }
        
    /** test all MBeans that contain Property */
    @Test
    public void testPropertyParent()
    {
        final Set<AMXProxy> parentsWithProperty = findAllContainingType( Util.deduceType(Property.class) );
        
        for( final AMXProxy amx : parentsWithProperty )
        {
            final PropertiesAccess pa = amx.as(PropertiesAccess.class);
            final Map<String, Property> children = pa.getProperty();
            for( final Property prop : children.values() ) { prop.getValue(); }
        }
    }
    
    /** test all MBeans that contain SystemProperty */
    @Test
    public void testSystemPropertyParent()
    {
        final Set<AMXProxy> parentsWithProperty = findAllContainingType( Util.deduceType(SystemProperty.class) );
        
        for( final AMXProxy amx : parentsWithProperty )
        {
            final SystemPropertiesAccess pa = amx.as(SystemPropertiesAccess.class);
            final Map<String, SystemProperty> children = pa.getSystemProperty();
            for( final SystemProperty prop : children.values() ) { prop.getValue(); }
        }
    }
    
    /** test all MBeans generically */
    @Test
    public void testForBogusAnnotations()
    {
        final List<Class<? extends AMXProxy>> interfaces = getInterfaces().all();
        
        for( final Class<? extends AMXProxy>  intf : interfaces )
        {
            final Method[] methods = intf.getMethods();
            for( final Method m : methods )
            {
                final ChildGetter cg = m.getAnnotation(ChildGetter.class);
                final ManagedAttribute ma = m.getAnnotation(ManagedAttribute.class);
                final ManagedOperation mo = m.getAnnotation(ManagedOperation.class);
                final String desc = intf.getName() + "." + m.getName() + "()";
                final int numArgs = m.getParameterTypes().length;
                
                assert ma == null || mo == null :
                    "Can't have both @ManagedAttribute and @ManagedOperation: " + desc;

                if ( cg != null )
                {
                    assert numArgs == 0 :
                        "@ChildGetter cannot be applied to method with arguments: " + desc;
                        
                    assert ma == null && mo == null :
                        "@ManagedAttribute/@ManagedOperation not applicable with @ChildGetter: " + desc;
                }
                
                if ( mo != null )
                {
                    // operations that mimic getters are bad. We can't prevent all such things
                    // but we can object to such oddball usage in an AMX interface
                    if ( numArgs == 0 && m.getName().startsWith("get") )
                    {
                        assert false : "testForBogusAnnotations: @ManagedOperation should be @ManagedAttribute: " + desc;
                    }
                }
            }
        }
    }
    

    @Test
    public void testSystemStatus()
    {
        final SystemStatus ss = getDomainRootProxy().getExt().getSystemStatus();

        final List<UnprocessedConfigChange> changes = SystemStatus.Helper.toUnprocessedConfigChange( ss.getRestartRequiredChanges() );

        final Set<AMXProxy> pools = getQueryMgr().queryType( Util.deduceType(JdbcConnectionPool.class) );

        for (final AMXProxy pool : pools)
        {
            final Map<String, Object> result = ss.pingJdbcConnectionPool(pool.getName());
            assert result != null;
        }
    }
    
    @Test
    public void testAutoConvert()
    {
        // make sure there is a required change so the change list will be non-empty
        final JavaConfig jc = getDomainConfig().getConfigs().getConfig().get("server-config").getJavaConfig();
        final String[] jvmOptions = jc.getJvmOptions();
        assert jvmOptions != null;
        for( int i = 0; i < jvmOptions.length; ++i )
        {
            final String option = jvmOptions[i];
            if ( option.startsWith( "-Xmx" ) )
            {
                //System.out.println( "CHANGING OPTION " + option );
                jvmOptions[i] = "-Xmx999m";
                jc.setJvmOptions( jvmOptions );
                
                jvmOptions[i] = option;
                jc.setJvmOptions( jvmOptions ); // set back to old value
                // we should now have two changes in the list
                break;
            }
        }
        
        final SystemStatus systemStatus = getDomainRootProxy().getExt().getSystemStatus();
        
        final List<UnprocessedConfigChange>  changes = SystemStatus.Helper.toUnprocessedConfigChange( systemStatus.getRestartRequiredChanges() );
        assert changes != null && changes.size() >= 2;
        System.out.println( "CHANGE COUNT: " + changes.size() );
        for( final UnprocessedConfigChange change : changes )
        {
            change.toString();  // force it to exist and function properly
           // System.out.println( "" + change );
        }
    }
    
    
    /** test all MBeans generically */
    @Test
    public void testAllGenerically()
    {
        final Interfaces interfaces = getInterfaces();
        final List<String> problems = new ArrayList<String>();

        for( final AMXProxy amx : getQueryMgr().queryAll() )
        {
            assert amx != null : "testAllGenerically(): null proxy in query list";
            try
            {
                final List<String> p = _testProxyInterface( amx, interfaces.get(amx.type()) );
                problems.addAll(p);
            }
            catch( final Throwable t )
            {
                final Throwable rootCause = ExceptionUtil.getRootCause(t);
                problems.add( rootCause.getMessage() );
            }
        }

        if ( problems.size() != 0 )
        {
            System.out.println( "\nPROBLEMS:\n" + CollectionUtil.toString(problems, "\n\n") );
            assert false : "" + problems;
        }
    }
    
    
    @Test
    public void testSingletonOrNot()
    {
        final Domain domain = getDomainConfig();
        final Configs configs = getDomainConfig().getConfigs();
        
        try
        {
            domain.child(Configs.class);
        }
        catch( final Exception e )
        {
            assert false : e;
        }
        
        try
        {
            configs.child(Config.class);
            assert false : "expecting failure";
        }
        catch( final Exception e )
        {
            // good
        }
    }
    
    

    /**
      Used to test the AMXProxyHandler processing of Set/List/Map/[] on any AMX MBean that
      has children of type Property ("property").
      */
    interface ChildGetterProxy extends AMXProxy
    {
        @ChildGetter    // type should be derived from method name
        Map<String, Property> getProperty();
        
        @ChildGetter(type="property")
        ObjectName[]  propertiesAsObjectNames();
        
        @ChildGetter(type="property")
        Property[]  propertiesAsArray();
        
        @ChildGetter(type="property")
        Set<Property>  propertiesAsSet();
        
        @ChildGetter(type="property")
        List<Property> propertiesAsList();
        
        @ChildGetter(type="property")
        Map<String,Property>  propertiesAsMap();
    }
    
    
    void _testChildGetter( final AMXProxy amx)
    {
        // we assume there are always properties on Domain
        ChildGetterProxy getter = amx.as(ChildGetterProxy.class);
        
        final Map<String,Property>  property = getter.getProperty();
        assert property.keySet().size() != 0 : " no properties found for " + amx.objectName();
        for( final Property prop : property.values() ) { prop.getValue(); }
        final int numProps = property.keySet().size();
        
        //System.out.println( "Testing properties on: " + amx.objectName() + " = " + numProps);
            
        final Set<Property>   asSet = getter.propertiesAsSet();
        assert asSet.size() == numProps : " no properties found for " + amx.objectName();
        for( final Property prop : asSet ) { prop.getValue(); }
        
        final List<Property>  asList = getter.propertiesAsList();
        assert asList.size() == numProps : " no properties found for " + amx.objectName();
        for( final Property prop : asList ) { prop.getValue(); }
        
        final Map<String,Property>  asMap = getter.propertiesAsMap();
        assert asMap.keySet().size() == numProps : " no properties found for " + amx.objectName();
        for( final Property prop : asMap.values() ) { prop.getValue(); }
        
        final ObjectName[]    asObjectNameArray = getter.propertiesAsObjectNames();
        assert asObjectNameArray.length == numProps : " no properties found for " + amx.objectName();
        
        final Property[]      asArray = getter.propertiesAsArray();
        assert asArray.length == numProps : " no properties found for " + amx.objectName();
        for( final Property prop : asList ) { prop.getValue(); }
    }
        
    @Test
    public void testChildGetterVariants()
    {
        final Set<AMXProxy> parentsWithProperty = findAllContainingType( Util.deduceType(Property.class) );
        
        for( final AMXProxy amx : parentsWithProperty )
        {
            _testChildGetter( amx );
        }
    }
    
    @Test
    public void testJ2EEDomain() throws ClassNotFoundException
    {
        if ( haveJSR77() )
        {
            testProxyInterface( getDomainRootProxy().getJ2EEDomain(), getJ2EEDomainClass() );
        }
    }
    
}




































