
package org.glassfish.connectors.config;

import org.jvnet.hk2.annotations.Service;
import org.jvnet.hk2.config.InjectionTarget;
import org.jvnet.hk2.config.NoopConfigInjector;

@Service(name = "resource-adapter-config", metadata = "target=org.glassfish.connectors.config.ResourceAdapterConfig,@name=optional,@name=datatype:java.lang.String,@name=leaf,@thread-pool-ids=optional,@thread-pool-ids=datatype:java.lang.String,@thread-pool-ids=leaf,@resource-adapter-name=optional,@resource-adapter-name=datatype:java.lang.String,@resource-adapter-name=leaf,key=@resource-adapter-name,keyed-as=org.glassfish.connectors.config.ResourceAdapterConfig,<property>=collection:org.jvnet.hk2.config.types.Property,<property>=collection:org.jvnet.hk2.config.types.Property,@object-type=optional,@object-type=default:user,@object-type=datatype:java.lang.String,@object-type=leaf,@deployment-order=optional,@deployment-order=default:100,@deployment-order=datatype:java.lang.Integer,@deployment-order=leaf")
@InjectionTarget(ResourceAdapterConfig.class)
public class ResourceAdapterConfigInjector
    extends NoopConfigInjector
{


}
