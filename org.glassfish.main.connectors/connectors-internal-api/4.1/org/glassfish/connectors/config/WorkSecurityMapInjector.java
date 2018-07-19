
package org.glassfish.connectors.config;

import org.jvnet.hk2.annotations.Service;
import org.jvnet.hk2.config.InjectionTarget;
import org.jvnet.hk2.config.NoopConfigInjector;

@Service(name = "work-security-map", metadata = "target=org.glassfish.connectors.config.WorkSecurityMap,@enabled=optional,@enabled=default:true,@enabled=datatype:java.lang.Boolean,@enabled=leaf,@description=optional,@description=datatype:java.lang.String,@description=leaf,@resource-adapter-name=optional,@resource-adapter-name=datatype:java.lang.String,@resource-adapter-name=leaf,<group-map>=collection:org.glassfish.connectors.config.GroupMap,<principal-map>=collection:org.glassfish.connectors.config.PrincipalMap,@name=required,@name=datatype:java.lang.String,@name=leaf,key=@name,keyed-as=org.glassfish.connectors.config.WorkSecurityMap,@object-type=optional,@object-type=default:user,@object-type=datatype:java.lang.String,@object-type=leaf,@deployment-order=optional,@deployment-order=default:100,@deployment-order=datatype:java.lang.Integer,@deployment-order=leaf")
@InjectionTarget(WorkSecurityMap.class)
public class WorkSecurityMapInjector
    extends NoopConfigInjector
{


}
