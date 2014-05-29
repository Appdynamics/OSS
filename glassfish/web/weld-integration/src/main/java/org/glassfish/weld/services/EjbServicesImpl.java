/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2009-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
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

package org.glassfish.weld.services;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.PostActivate;
import javax.ejb.PrePassivate;
import javax.enterprise.inject.spi.InterceptionType;
import javax.enterprise.inject.spi.Interceptor;
import javax.interceptor.AroundInvoke;
import javax.interceptor.AroundTimeout;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.glassfish.ejb.api.EjbContainerServices;
import org.glassfish.weld.ejb.EjbDescriptorImpl;
import org.glassfish.weld.ejb.SessionObjectReferenceImpl;
import org.jboss.weld.ejb.api.SessionObjectReference;
import org.jboss.weld.ejb.spi.EjbDescriptor;
import org.jboss.weld.ejb.spi.EjbServices;
import org.jboss.weld.ejb.spi.InterceptorBindings;
import org.jvnet.hk2.component.Habitat;

import com.sun.enterprise.deployment.EjbBundleDescriptor;
import com.sun.enterprise.deployment.EjbInterceptor;
import com.sun.enterprise.deployment.EjbSessionDescriptor;
import com.sun.enterprise.deployment.LifecycleCallbackDescriptor;

/**
 * An implementation of th <code>EJBServices</code> Weld SPI. The Weld 
 * implementation uses this SPI to resolve EJB and register CDI Interceptors
 * for EJBs. 
 */
public class EjbServicesImpl implements EjbServices
{

    private Habitat habitat;
    private Logger logger = Logger.getLogger(EjbServicesImpl.class.getName());


    public EjbServicesImpl(Habitat h) {
        habitat = h;
    }

   /**
    * Request a reference to an EJB session object from the container. If the
    * EJB being resolved is a stateful session bean, the container should ensure
    * the session bean is created before this method returns.
    * 
    * @param ejbDescriptor the ejb to resolve
    * @return a reference to the session object
    */
    public SessionObjectReference resolveEjb(EjbDescriptor<?> ejbDescriptor) {

        SessionObjectReference sessionObj = null;

        // All we need to do is create a reference based on one of the beans'
        // client views, so just choose one and get its corresponding portable
        // JNDI name.
        String globalJndiName = getDefaultGlobalJndiName(ejbDescriptor);
        if( globalJndiName != null ) {
            try {

                InitialContext ic = new InitialContext();

                Object ejbRef = ic.lookup(globalJndiName);

                EjbContainerServices containerServices = habitat.getByContract(EjbContainerServices.class);

                sessionObj = new SessionObjectReferenceImpl(containerServices, ejbRef);

            } catch(NamingException ne) {
               throw new IllegalStateException("Error resolving session object reference for ejb name " +
                       ejbDescriptor.getBeanClass() + " and jndi name " + globalJndiName, ne);
            }
        }  else {
           throw new IllegalArgumentException("Not enough type information to resolve ejb for " +
               " ejb name " + ejbDescriptor.getBeanClass());
        }

	    return sessionObj;

    }
   

    private String getDefaultGlobalJndiName(EjbDescriptor ejbDesc) {

        EjbSessionDescriptor sessionDesc = (EjbSessionDescriptor)
                ((EjbDescriptorImpl) ejbDesc).getEjbDescriptor();

        String clientView = null;

        if( sessionDesc.isLocalBean() ) {
            clientView = sessionDesc.getEjbClassName();
        } else if( sessionDesc.getLocalBusinessClassNames().size() >= 1) {
            clientView = sessionDesc.getLocalBusinessClassNames().iterator().next();
        } else if( sessionDesc.getRemoteBusinessClassNames().size() >= 1) {
            clientView = sessionDesc.getRemoteBusinessClassNames().iterator().next();
        }

        return (clientView != null) ? sessionDesc.getPortableJndiName(clientView) : null;

    }

    public void registerInterceptors(EjbDescriptor<?> ejbDesc, InterceptorBindings interceptorBindings) {

        // Work around bug that ejbDesc might be internal 299 descriptor.
        if( ejbDesc instanceof org.jboss.weld.ejb.InternalEjbDescriptor ) {
            ejbDesc = ((org.jboss.weld.ejb.InternalEjbDescriptor)ejbDesc).delegate();
        }

         com.sun.enterprise.deployment.EjbDescriptor glassfishEjbDesc = (com.sun.enterprise.deployment.EjbDescriptor)
                ((EjbDescriptorImpl) ejbDesc).getEjbDescriptor();

        // Convert to EjbInterceptor
        // First create master list of EjbInterceptor descriptors
        for(Interceptor next : interceptorBindings.getAllInterceptors()) {
            logger.log(Level.FINE, "trying to register:" + next);
            // Add interceptor to list all interceptors in ejb descriptor
            if( !(glassfishEjbDesc.hasInterceptorClass(next.getBeanClass().getName()))) {
                logger.log(Level.FINE, "Adding interceptor: " 
                        + next.getBeanClass().getName() + " for EJB:" 
                        + glassfishEjbDesc.getEjbClassName());
                EjbInterceptor ejbInt = makeEjbInterceptor(next, glassfishEjbDesc.getEjbBundleDescriptor());
                glassfishEjbDesc.addInterceptorClass(ejbInt);
            }
        }

        // Create ordered list of EjbInterceptor for each lifecycle interception type and append to
        // EjbDescriptor.   299 interceptors are always added after any interceptors defined via
        // EJB-defined metadata, so the ordering will be correct since all the ejb interceptors
        // have already been processed.
        List<EjbInterceptor> postConstructChain =
                makeInterceptorChain(InterceptionType.POST_CONSTRUCT,
                        interceptorBindings.getLifecycleInterceptors(InterceptionType.POST_CONSTRUCT),
                                glassfishEjbDesc);
        glassfishEjbDesc.appendToInterceptorChain(postConstructChain);

        List<EjbInterceptor> preDestroyChain =
                makeInterceptorChain(InterceptionType.PRE_DESTROY,
                        interceptorBindings.getLifecycleInterceptors(InterceptionType.PRE_DESTROY),
                                glassfishEjbDesc);
        glassfishEjbDesc.appendToInterceptorChain(preDestroyChain);

        List<EjbInterceptor> prePassivateChain =
                makeInterceptorChain(InterceptionType.PRE_PASSIVATE,
                        interceptorBindings.getLifecycleInterceptors(InterceptionType.PRE_PASSIVATE),
                                glassfishEjbDesc);
        glassfishEjbDesc.appendToInterceptorChain(prePassivateChain);

        List<EjbInterceptor> postActivateChain =
                makeInterceptorChain(InterceptionType.POST_ACTIVATE,
                        interceptorBindings.getLifecycleInterceptors(InterceptionType.POST_ACTIVATE),
                                glassfishEjbDesc);
        glassfishEjbDesc.appendToInterceptorChain(postActivateChain);


        // 299-provided list is organized as per-method.  Append each method chain to EjbDescriptor.
        
        Class ejbBeanClass = null;

        try {
            ClassLoader cl = glassfishEjbDesc.getEjbBundleDescriptor().getClassLoader();
            ejbBeanClass = cl.loadClass(glassfishEjbDesc.getEjbClassName());
        } catch(ClassNotFoundException cnfe) {
            throw new IllegalStateException("Cannot load bean class " + glassfishEjbDesc.getEjbClassName(),
                    cnfe);
        }

        for(Method m : ejbBeanClass.getMethods()) {
            List<EjbInterceptor> aroundInvokeChain =
                makeInterceptorChain(InterceptionType.AROUND_INVOKE,
                        interceptorBindings.getMethodInterceptors(InterceptionType.AROUND_INVOKE, m),
                                glassfishEjbDesc);
            glassfishEjbDesc.addMethodLevelChain(aroundInvokeChain, m, true);


            List<EjbInterceptor> aroundTimeoutChain =
                makeInterceptorChain(InterceptionType.AROUND_TIMEOUT,
                        interceptorBindings.getMethodInterceptors(InterceptionType.AROUND_TIMEOUT, m),
                                glassfishEjbDesc);
            glassfishEjbDesc.addMethodLevelChain(aroundTimeoutChain, m, false);

        }                                     

        return;

    }

    private List<EjbInterceptor> makeInterceptorChain(InterceptionType interceptionType,
                                         List<Interceptor<?>> lifecycleList,
                                         com.sun.enterprise.deployment.EjbDescriptor ejbDesc ) {

        List<EjbInterceptor> ejbInterceptorList = new LinkedList<EjbInterceptor>();

        if( lifecycleList == null ) {
            return ejbInterceptorList;
        }

        for(Interceptor next : lifecycleList ) {

            EjbInterceptor ejbInt = makeEjbInterceptor(next, ejbDesc.getEjbBundleDescriptor());
            LifecycleCallbackDescriptor lifecycleDesc = new LifecycleCallbackDescriptor();

            lifecycleDesc.setLifecycleCallbackClass(next.getBeanClass().getName());
            lifecycleDesc.setLifecycleCallbackMethod(getInterceptorMethod(next.getBeanClass(),
                    getInterceptorAnnotationType(interceptionType)));

            switch(interceptionType) {
                case POST_CONSTRUCT :
                    ejbInt.addPostConstructDescriptor(lifecycleDesc);
                    break;
                case PRE_DESTROY :
                    ejbInt.addPreDestroyDescriptor(lifecycleDesc);
                    break;
                case PRE_PASSIVATE :
                    ejbInt.addPrePassivateDescriptor(lifecycleDesc);
                    break;
                case POST_ACTIVATE :
                    ejbInt.addPostActivateDescriptor(lifecycleDesc);
                    break;
                case AROUND_INVOKE :
                    ejbInt.addAroundInvokeDescriptor(lifecycleDesc);
                    break;
                case AROUND_TIMEOUT :
                    ejbInt.addAroundTimeoutDescriptor(lifecycleDesc);
                    break;
                default :
                      throw new IllegalArgumentException("Invalid lifecycle interception type " +
                        interceptionType);
            }

            ejbInterceptorList.add(ejbInt);

        }

        return ejbInterceptorList;

    }

    private Class getInterceptorAnnotationType(InterceptionType interceptionType) {

        switch(interceptionType) {
            case POST_CONSTRUCT :
                return PostConstruct.class;
            case PRE_DESTROY :
                return PreDestroy.class;
            case PRE_PASSIVATE :
                return PrePassivate.class;
            case POST_ACTIVATE :
                return PostActivate.class;
            case AROUND_INVOKE :
                return AroundInvoke.class;
            case AROUND_TIMEOUT :
                return AroundTimeout.class;
        }

        throw new IllegalArgumentException("Invalid interception type " +
                        interceptionType);
    }

    private String getInterceptorMethod(Class interceptorClass, Class annotation) {

        for(Method next : interceptorClass.getDeclaredMethods()) {
            if( next.getAnnotation(annotation) != null ) {
                return next.getName();
            }
        }

        throw new IllegalStateException("Interceptor Class " + interceptorClass + " has no method annotated with " +
            annotation);

    }

    private EjbInterceptor makeEjbInterceptor(Interceptor interceptor, EjbBundleDescriptor bundle) {

        EjbInterceptor ejbInt = new EjbInterceptor();
        ejbInt.setBundleDescriptor(bundle);
        ejbInt.setInterceptorClassName(interceptor.getBeanClass().getName());
        ejbInt.setCDIInterceptor(true);

        return ejbInt;
    }

    private EjbInterceptor getEjbInterceptorByClassName(Set<EjbInterceptor> allInterceptors, String name) {

        for(EjbInterceptor next : allInterceptors) {
            if( next.getInterceptorClassName().equals(name) ) {
                return next;
            }
        }
        throw new IllegalArgumentException("No interceptor with class name " + name);
    }

    public void cleanup() {
        //Nothing to do here.
    }

}
