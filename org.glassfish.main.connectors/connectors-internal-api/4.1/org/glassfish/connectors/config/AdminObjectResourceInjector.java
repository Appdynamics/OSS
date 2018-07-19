
package org.glassfish.connectors.config;

import org.jvnet.hk2.annotations.Service;
import org.jvnet.hk2.config.InjectionTarget;
import org.jvnet.hk2.config.NoopConfigInjector;

@Service(name = "admin-object-resource", metadata = "target=org.glassfish.connectors.config.AdminObjectResource,@res-type=optional,@res-type=datatype:java.lang.String,@res-type=leaf,@res-adapter=optional,@res-adapter=datatype:java.lang.String,@res-adapter=leaf,@class-name=optional,@class-name=datatype:java.lang.String,@class-name=leaf,@enabled=optional,@enabled=default:true,@enabled=datatype:java.lang.Boolean,@enabled=leaf,@enabled=optional,@enabled=default:true,@enabled=datatype:java.lang.Boolean,@enabled=leaf,@description=optional,@description=datatype:java.lang.String,@description=leaf,<property>=collection:org.jvnet.hk2.config.types.Property,<property>=collection:org.jvnet.hk2.config.types.Property,@object-type=optional,@object-type=default:user,@object-type=datatype:java.lang.String,@object-type=leaf,@deployment-order=optional,@deployment-order=default:100,@deployment-order=datatype:java.lang.Integer,@deployment-order=leaf,@jndi-name=optional,@jndi-name=datatype:java.lang.String,@jndi-name=leaf,key=@jndi-name,keyed-as=com.sun.enterprise.config.serverbeans.BindableResource")
@InjectionTarget(AdminObjectResource.class)
public class AdminObjectResourceInjector
    extends NoopConfigInjector
{


}
