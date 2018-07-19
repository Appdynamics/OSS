
package org.glassfish.connectors.config;

import org.jvnet.hk2.annotations.Service;
import org.jvnet.hk2.config.InjectionTarget;
import org.jvnet.hk2.config.NoopConfigInjector;

@Service(name = "connector-resource", metadata = "target=org.glassfish.connectors.config.ConnectorResource,@pool-name=optional,@pool-name=datatype:java.lang.String,@pool-name=leaf,@pool-name=optional,@pool-name=datatype:java.lang.String,@pool-name=leaf,@enabled=optional,@enabled=default:true,@enabled=datatype:java.lang.Boolean,@enabled=leaf,@enabled=optional,@enabled=default:true,@enabled=datatype:java.lang.Boolean,@enabled=leaf,@description=optional,@description=datatype:java.lang.String,@description=leaf,<property>=collection:org.jvnet.hk2.config.types.Property,<property>=collection:org.jvnet.hk2.config.types.Property,@jndi-name=optional,@jndi-name=datatype:java.lang.String,@jndi-name=leaf,key=@jndi-name,keyed-as=com.sun.enterprise.config.serverbeans.BindableResource,@object-type=optional,@object-type=default:user,@object-type=datatype:java.lang.String,@object-type=leaf,@deployment-order=optional,@deployment-order=default:100,@deployment-order=datatype:java.lang.Integer,@deployment-order=leaf")
@InjectionTarget(ConnectorResource.class)
public class ConnectorResourceInjector
    extends NoopConfigInjector
{


}
