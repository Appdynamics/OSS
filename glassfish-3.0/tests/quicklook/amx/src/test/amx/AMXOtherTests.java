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
import org.glassfish.admin.amx.intf.config.grizzly.Protocols;
import org.glassfish.admin.amx.intf.config.grizzly.Protocol;
import org.glassfish.admin.amx.intf.config.grizzly.NetworkListener;
import org.glassfish.admin.amx.monitoring.*;
import org.glassfish.admin.amx.util.CollectionUtil;
import org.glassfish.admin.amx.util.ExceptionUtil;
import org.glassfish.admin.amx.util.MapUtil;
import org.glassfish.admin.amx.logging.Logging;
import org.glassfish.admin.amx.annotation.*;

import static org.glassfish.admin.amx.base.ConnectorRuntimeAPIProvider.*;
/** 
 */
//@Test(groups={"amx"}, description="AMXProxy tests", sequential=false, threadPoolSize=5)
@Test(
    sequential=false, threadPoolSize=10,
    groups =
    {
        "amx"
    },
    description = "test varioius miscellaneous AMX feature"
)
public final class AMXOtherTests extends AMXTestBase
{
    public AMXOtherTests()
    {
    }
    
    @Test
    public void testConnectorRuntimeAPIProvider()
    {
        final ConnectorRuntimeAPIProvider api = getDomainRootProxy().getExt().getConnectorRuntimeAPIProvider();
        assert api != null;
        
        final Map<String,JdbcConnectionPool> pools = getDomainConfig().getResources().getJdbcConnectionPool();
        for ( final String poolName :  pools.keySet() )
        {
            final JdbcConnectionPool pool = pools.get(poolName);
            final String datasourceClassname = pool.getDatasourceClassname();
            final Map<String,Object> props = api.getConnectionDefinitionPropertiesAndDefaults( datasourceClassname, pool.getResType());
            //System.out.println( "Props for " + poolName +": " + props );
            
            final Map<String,Object> pingResults = api.pingJDBCConnectionPool(poolName);
            assert pingResults != null;
            assert pingResults.get(ConnectorRuntimeAPIProvider.PING_CONNECTION_POOL_KEY) != null || pingResults.get(REASON_FAILED_KEY) != null;
            //System.out.println( "pingResults for: " + poolName + ": " + pingResults );
        }

        final Map<String,Object> customResources = api.getBuiltInCustomResources();
        //System.out.println( "BuiltInCustomResources: " + customResources );
        
        final Map<String,Object> systemConnectors = api.getSystemConnectorsAllowingPoolCreation();
        //System.out.println( "SystemConnectorsAllowingPoolCreation: " + MapUtil.toString(systemConnectors) );
    }
    
    @Test
    public void testVariousGetters()
    {
        final RuntimeRoot runtimeRoot = getDomainRootProxy().getRuntime();
        final Map<String,ServerRuntime>  serverRuntimes = runtimeRoot.getServerRuntime();
        
        final Protocols protocols = getDomainConfig().getConfigs().getConfig().get("server-config").getNetworkConfig().getProtocols();
        final Protocol protocol = protocols.getProtocol().values().iterator().next();
        final List<NetworkListener>   listeners = protocol.findNetworkListeners();
        assert listeners.size() != 0;
        for( final NetworkListener listener : listeners )
        {
            // the loop causes a ClassCastException if something is wrong
        }
    }
    
}




































