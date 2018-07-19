
package org.glassfish.connectors.config;

import org.jvnet.hk2.annotations.Service;
import org.jvnet.hk2.config.InjectionTarget;
import org.jvnet.hk2.config.NoopConfigInjector;

@Service(name = "security-map", metadata = "target=org.glassfish.connectors.config.SecurityMap,@name=optional,@name=datatype:java.lang.String,@name=leaf,key=@name,keyed-as=org.glassfish.connectors.config.SecurityMap,<backend-principal>=org.glassfish.connectors.config.BackendPrincipal,<principal>=collection:leaf,<user-group>=collection:leaf")
@InjectionTarget(SecurityMap.class)
public class SecurityMapInjector
    extends NoopConfigInjector
{


}
