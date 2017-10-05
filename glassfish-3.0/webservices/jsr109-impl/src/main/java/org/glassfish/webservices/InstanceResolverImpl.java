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

/*
 * InstanceResolverImpl.java
 *
 * Created on May 29, 2007, 10:41 AM
 *
 * @author Mike Grogan
 */

package org.glassfish.webservices;
import com.sun.istack.NotNull;
import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.server.ResourceInjector;
import com.sun.xml.ws.api.server.InstanceResolver;
import com.sun.xml.ws.api.server.WSEndpoint;
import com.sun.xml.ws.api.server.WSWebServiceContext;
import com.sun.xml.ws.api.server.Invoker;
import com.sun.enterprise.container.common.spi.util.InjectionManager;
import com.sun.enterprise.container.common.spi.util.InjectionException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import javax.xml.ws.Provider;
import javax.xml.ws.WebServiceException;


public final class InstanceResolverImpl<T> extends InstanceResolver<T> {
   
    //delegate to this InstanceResolver
    private  InstanceResolver<T> resolver;
    private  T instance;
    private Class<T> classtobeResolved;

    private WSWebServiceContext wsc;
    private WSEndpoint endpoint;

    private final InjectionManager injManager = WebServiceContractImpl.getInstance()
                .getHabitat().getByContract(InjectionManager.class);

    public  InstanceResolverImpl(@NotNull Class<T> clasz) {
        this.classtobeResolved = clasz;

    }

    public @NotNull T resolve(Packet request) {
        //See iss 9721
        //Injection and instantiation is now done lazily

       try {
            instance = (T)injManager.createManagedObject(classtobeResolved);
        } catch (InjectionException e) {
            throw new WebServiceException(e);
        }

        resolver = InstanceResolver.createSingleton(instance);
        getResourceInjector(endpoint).inject(wsc, instance);
        return resolver.resolve(request);
    }

    public void start(WSWebServiceContext wsc, WSEndpoint endpoint) {
        this.wsc = wsc;
        this.endpoint = endpoint;

    }

    public void dispose() {
        try {
            injManager.destroyManagedObject(classtobeResolved);
        } catch (InjectionException e) {
            throw new WebServiceException(e);
        }
    }
    
    private ResourceInjector getResourceInjector(WSEndpoint endpoint) {
        ResourceInjector ri = endpoint.getContainer().getSPI(ResourceInjector.class);
        if(ri==null)
            ri = ResourceInjector.STANDALONE;
        return ri;
    }
    
     /**
     * Wraps this {@link InstanceResolver} into an {@link Invoker}.
     */
	public  //TODO - make this package private.  Cannot do it until this method is removed from base
		//       class com.sun.xml.ws.api.server.InstanceResolver
     @NotNull Invoker createInvoker() {
        return new Invoker() {
            @Override
            public void start(@NotNull WSWebServiceContext wsc, @NotNull WSEndpoint endpoint) {
                InstanceResolverImpl.this.start(wsc,endpoint);
            }

            @Override
            public void dispose() {
                InstanceResolverImpl.this.dispose();
            }

            @Override
            public Object invoke(Packet p, Method m, Object... args) throws InvocationTargetException, IllegalAccessException {
                return m.invoke( resolve(p), args );
            }

            @Override
            public <T> T invokeProvider(@NotNull Packet p, T arg) {
                return ((Provider<T>)resolve(p)).invoke(arg);
            }

            public String toString() {
                return "Default Invoker over "+InstanceResolverImpl.this.toString();
            }
        };
    }


}
