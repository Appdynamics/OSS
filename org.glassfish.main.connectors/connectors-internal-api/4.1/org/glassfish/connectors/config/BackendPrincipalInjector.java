
package org.glassfish.connectors.config;

import org.jvnet.hk2.annotations.Service;
import org.jvnet.hk2.config.InjectionTarget;
import org.jvnet.hk2.config.NoopConfigInjector;

@Service(name = "backend-principal", metadata = "target=org.glassfish.connectors.config.BackendPrincipal,@user-name=optional,@user-name=datatype:java.lang.String,@user-name=leaf,key=@user-name,keyed-as=org.glassfish.connectors.config.BackendPrincipal,@password=optional,@password=datatype:java.lang.String,@password=leaf")
@InjectionTarget(BackendPrincipal.class)
public class BackendPrincipalInjector
    extends NoopConfigInjector
{


}
