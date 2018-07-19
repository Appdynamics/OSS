
package org.glassfish.connectors.config;

import org.jvnet.hk2.annotations.Service;
import org.jvnet.hk2.config.InjectionTarget;
import org.jvnet.hk2.config.NoopConfigInjector;

@Service(name = "group-map", metadata = "target=org.glassfish.connectors.config.GroupMap,@eis-group=optional,@eis-group=datatype:java.lang.String,@eis-group=leaf,key=@eis-group,keyed-as=org.glassfish.connectors.config.GroupMap,@mapped-group=optional,@mapped-group=datatype:java.lang.String,@mapped-group=leaf")
@InjectionTarget(GroupMap.class)
public class GroupMapInjector
    extends NoopConfigInjector
{


}
