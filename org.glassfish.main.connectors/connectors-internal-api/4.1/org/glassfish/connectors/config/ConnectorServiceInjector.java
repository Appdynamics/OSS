
package org.glassfish.connectors.config;

import org.jvnet.hk2.annotations.Service;
import org.jvnet.hk2.config.InjectionTarget;
import org.jvnet.hk2.config.NoopConfigInjector;

@Service(name = "connector-service", metadata = "target=org.glassfish.connectors.config.ConnectorService,@shutdown-timeout-in-seconds=optional,@shutdown-timeout-in-seconds=default:30,@shutdown-timeout-in-seconds=datatype:java.lang.String,@shutdown-timeout-in-seconds=leaf,@class-loading-policy=optional,@class-loading-policy=default:derived,@class-loading-policy=datatype:java.lang.String,@class-loading-policy=leaf,<property>=collection:org.jvnet.hk2.config.types.Property,<property>=collection:org.jvnet.hk2.config.types.Property")
@InjectionTarget(ConnectorService.class)
public class ConnectorServiceInjector
    extends NoopConfigInjector
{


}
