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
package org.glassfish.ejb.deployment.annotation.handlers;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.interceptor.AroundInvoke;
import javax.interceptor.AroundTimeout;
import javax.interceptor.Interceptors;
import javax.ejb.PostActivate;
import javax.ejb.PrePassivate;

import com.sun.enterprise.deployment.EjbDescriptor;
import com.sun.enterprise.deployment.EjbBundleDescriptor;
import com.sun.enterprise.deployment.InterceptorBindingDescriptor;
import com.sun.enterprise.deployment.LifecycleCallbackDescriptor;
import com.sun.enterprise.deployment.EjbInterceptor;
import static com.sun.enterprise.deployment.LifecycleCallbackDescriptor.CallbackType;
import com.sun.enterprise.deployment.EjbSessionDescriptor;
import com.sun.enterprise.deployment.MethodDescriptor;
import org.glassfish.apf.AnnotationInfo;
import org.glassfish.apf.AnnotationProcessorException;
import org.glassfish.apf.HandlerProcessingResult;
import org.glassfish.apf.ProcessingContext;
import com.sun.enterprise.deployment.annotation.context.EjbContext;
import com.sun.enterprise.deployment.annotation.context.EjbInterceptorContext;
import org.glassfish.ejb.deployment.annotation.handlers.AbstractAttributeHandler;
import org.glassfish.apf.impl.ComponentDefinition;
import org.jvnet.hk2.annotations.Service;

/**
 * This handler is responsible for handling javax.ejb.Interceptors
 *
 */
@Service
public class InterceptorsHandler extends AbstractAttributeHandler {
    
    public InterceptorsHandler() {
    }
    
    /**
     * @return the annoation type this annotation handler is handling
     */
    public Class<? extends Annotation> getAnnotationType() {
        return Interceptors.class;
    }    

    protected boolean supportTypeInheritance() {
        return true;
    }

    protected HandlerProcessingResult processAnnotation(AnnotationInfo ainfo,
            EjbContext[] ejbContexts) throws AnnotationProcessorException {

        Interceptors interceptors = (Interceptors) ainfo.getAnnotation();

        
        EjbBundleDescriptor ejbBundle = 
            ((EjbDescriptor)ejbContexts[0].getDescriptor()).
                getEjbBundleDescriptor();
        
        // Process each of the interceptor classes.
        for(Class interceptor : interceptors.value()) {
            processInterceptorClass(interceptor, ejbBundle, ainfo);
        }

        for(EjbContext next : ejbContexts) {

            EjbDescriptor ejbDescriptor = (EjbDescriptor) next.getDescriptor();

            // Create binding information.  
            InterceptorBindingDescriptor binding = 
                new InterceptorBindingDescriptor();

            binding.setEjbName(ejbDescriptor.getName());

            for(Class interceptor : interceptors.value()) {
                binding.appendInterceptorClass(interceptor.getName());
            }
            
            if(ElementType.METHOD.equals(ainfo.getElementType())) {
                Method m = (Method) ainfo.getAnnotatedElement();
                MethodDescriptor md = 
                    new MethodDescriptor(m, MethodDescriptor.EJB_BEAN);
                binding.setBusinessMethod(md);
            }

            // All binding information processed from annotations should go
            // before the binding information processed from the descriptors.
            // Since descriptors are processed first, always place the binding
            // info at the front.  The binding information from the descriptor
            // is ordered, but there is no prescribed order in which the 
            // annotations are processed, so all that matters is that it's
            // before the descriptor bindings and that the descriptor binding
            // order is preserved.
            ejbBundle.prependInterceptorBinding(binding);
        }

        return getDefaultProcessedResult();
    }

    private void processInterceptorClass(Class interceptorClass,
            EjbBundleDescriptor ejbBundle, AnnotationInfo ainfo) 
        throws AnnotationProcessorException {
                
        Set<LifecycleCallbackDescriptor> aroundInvokeDescriptors =
            new HashSet<LifecycleCallbackDescriptor>();
        Set<LifecycleCallbackDescriptor> aroundTimeoutDescriptors =
            new HashSet<LifecycleCallbackDescriptor>();
        Set<LifecycleCallbackDescriptor> postActivateDescriptors =
            new HashSet<LifecycleCallbackDescriptor>();
        Set<LifecycleCallbackDescriptor> prePassivateDescriptors =
            new HashSet<LifecycleCallbackDescriptor>();
        
        ComponentDefinition cdef = new ComponentDefinition(interceptorClass);
        for(Method m : cdef.getMethods()) {
            if( m.getAnnotation(AroundInvoke.class) != null ) {
                aroundInvokeDescriptors.add(getLifecycleCallbackDescriptor(m));
            }
            if( m.getAnnotation(AroundTimeout.class) != null ) {
                aroundTimeoutDescriptors.add(getLifecycleCallbackDescriptor(m));
            }
            if( m.getAnnotation(PostActivate.class) != null ) {
                postActivateDescriptors.add(getLifecycleCallbackDescriptor(m));
            }
            if( m.getAnnotation(PrePassivate.class) != null ) {
                prePassivateDescriptors.add(getLifecycleCallbackDescriptor(m));
            }
        }
        
        EjbInterceptor interceptor =
            ejbBundle.getInterceptorByClassName(interceptorClass.getName());
        if (interceptor == null) {
            interceptor = new EjbInterceptor();
            interceptor.setInterceptorClassName(interceptorClass.getName());
            // Add interceptor to the set of all interceptors in the ejb-jar
            ejbBundle.addInterceptor(interceptor);
        }
        
        if (aroundInvokeDescriptors.size() > 0) {
            interceptor.addAroundInvokeDescriptors(aroundInvokeDescriptors);
        }
        
        if (aroundTimeoutDescriptors.size() > 0) {
            interceptor.addAroundTimeoutDescriptors(aroundTimeoutDescriptors);
        }
        
        if (postActivateDescriptors.size() > 0) {
            interceptor.addCallbackDescriptors(CallbackType.POST_ACTIVATE,
                postActivateDescriptors);
        }
        
        if (prePassivateDescriptors.size() > 0) {
            interceptor.addCallbackDescriptors(CallbackType.PRE_PASSIVATE,
                prePassivateDescriptors);
        }

        // process resource related annotations
        EjbInterceptorContext ejbInterceptorContext =
            new EjbInterceptorContext(interceptor);
        ProcessingContext procContext = ainfo.getProcessingContext();
        procContext.pushHandler(ejbInterceptorContext);
        procContext.getProcessor().process(
            procContext, new Class[] { interceptorClass });
        return;
    }

    /**
     * @return an array of annotation types this annotation handler would 
     * require to be processed (if present) before it processes it's own 
     * annotation type.
     */
    public Class<? extends Annotation>[] getTypeDependencies() {
        return getEjbAnnotationTypes();
    }

    private LifecycleCallbackDescriptor getLifecycleCallbackDescriptor(Method m) {
        LifecycleCallbackDescriptor lccDesc = new LifecycleCallbackDescriptor();
        lccDesc.setLifecycleCallbackClass(m.getDeclaringClass().getName());
        lccDesc.setLifecycleCallbackMethod(m.getName());
        return lccDesc;
    }
}
