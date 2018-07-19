
package org.glassfish.connectors.config;

import org.jvnet.hk2.annotations.Service;
import org.jvnet.hk2.config.InjectionTarget;
import org.jvnet.hk2.config.NoopConfigInjector;

@Service(name = "principal-map", metadata = "target=org.glassfish.connectors.config.PrincipalMap,@eis-principal=optional,@eis-principal=datatype:java.lang.String,@eis-principal=leaf,key=@eis-principal,keyed-as=org.glassfish.connectors.config.PrincipalMap,@mapped-principal=optional,@mapped-principal=datatype:java.lang.String,@mapped-principal=leaf")
@InjectionTarget(PrincipalMap.class)
public class PrincipalMapInjector
    extends NoopConfigInjector
{


}
