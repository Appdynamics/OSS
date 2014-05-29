/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ejb.containers;

import com.sun.ejb.containers.EJBLocalRemoteObject;
import com.sun.ejb.EjbInvocation;
import com.sun.ejb.InvocationInfo;
import com.sun.ejb.containers.util.MethodMap;
import com.sun.enterprise.container.common.spi.util.IndirectlySerializable;
import com.sun.enterprise.deployment.EjbDescriptor;
import com.sun.enterprise.deployment.EjbSessionDescriptor;
import com.sun.enterprise.util.LocalStringManagerImpl;
import com.sun.enterprise.util.Utility;

import javax.ejb.EJBException;
import javax.ejb.EJBLocalHome;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Logger;

/** 
 * Handler for EJBLocalHome invocations through EJBLocalHome proxy.
 *
 * @author Kenneth Saks
 */    

final class EJBLocalHomeInvocationHandler 
    extends ReadOnlyEJBLocalHomeImpl implements InvocationHandler {

    private static final Logger logger =
            EjbContainerUtilImpl.getInstance().getLogger();

    private static LocalStringManagerImpl localStrings =
        new LocalStringManagerImpl(EJBLocalHomeInvocationHandler.class);

    private boolean isStatelessSession_;
    private boolean isEntity_;

    // Our associated proxy object.  Used when a caller needs EJBLocalObject
    // but only has InvocationHandler.
    private EJBLocalHome proxy_;

    private Class localHomeIntfClass_;

    // Cache reference to invocation info.  There is one of these per
    // container.  It's populated during container initialization and
    // passed in when the InvocationHandler is created.  This avoids the
    // overhead of building the method info each time a LocalHome proxy
    // is created.  
    private MethodMap invocationInfoMap_;

    EJBLocalHomeInvocationHandler(EjbDescriptor ejbDescriptor,
                                  Class localHomeIntf,
                                  MethodMap invocationInfoMap) 
        throws Exception {

        if( ejbDescriptor instanceof EjbSessionDescriptor ) {
            isEntity_ = false;
            isStatelessSession_ = 
                ((EjbSessionDescriptor)ejbDescriptor).isStateless();
        } else {
            isStatelessSession_ = false;
            isEntity_ = true;
        }

        invocationInfoMap_ = invocationInfoMap;

        localHomeIntfClass_ = localHomeIntf;

        // NOTE : Container is not set on super-class until after 
        // constructor is called.
    }

    public void setProxy(EJBLocalHome proxy) {
        proxy_ = proxy;
    }

    protected EJBLocalHome getEJBLocalHome() {
        return proxy_;
    }

    /**
     * Called by EJBLocalHome proxy.
     */
    public Object invoke(Object proxy, Method method, Object[] args) 
        throws Throwable {

        ClassLoader originalClassLoader = null;

        // NOTE : be careful with "args" parameter.  It is null
        //        if method signature has 0 arguments.
        try {
        ((BaseContainer) getContainer()).onEnteringContainer();

            // In some cases(e.g. CDI + OSGi combination) ClassLoader
            // is accessed from the current Thread. In those cases we need to set
            // the context classloader to the application's classloader before
            // proceeding. Otherwise, the context classloader could still
            // reflect the caller's class loader.

            if( Thread.currentThread().getContextClassLoader() !=
                getContainer().getClassLoader() ) {
                originalClassLoader = Utility.setContextClassLoader
                    (getContainer().getClassLoader());
            }

        Class methodClass = method.getDeclaringClass();

        if( methodClass == java.lang.Object.class )  {
            return InvocationHandlerUtil.invokeJavaObjectMethod
                (this, method, args);    
        } else if( methodClass == IndirectlySerializable.class ) {
            return this.getSerializableObjectFactory();
        } else if( methodClass == ReadOnlyEJBLocalHome.class ) {
            // ReadOnlyBeanLocalNotifier getReadOnlyBeanLocalNotifier();
            return super.getReadOnlyBeanLocalNotifier();
        }

        // Use optimized version of get that takes param count as an argument.
        InvocationInfo invInfo = (InvocationInfo)
            invocationInfoMap_.get(method, ((args != null) ? args.length : 0) );
            
        if( invInfo == null ) {
            throw new IllegalStateException("Unknown method :" + method);
        } 

        if( (methodClass == javax.ejb.EJBLocalHome.class) ||
            invInfo.ejbIntfOverride ) {
            // There is only one method on javax.ejb.EJBLocalHome
            super.remove(args[0]);
            return null;

        } else if(methodClass == GenericEJBLocalHome.class) {

            // This is a creation request through the EJB 3.0
            // client view, so just create a local business object and 
            // return it.
            EJBLocalObjectImpl localImpl = 
                createEJBLocalBusinessObjectImpl((String) args[0]);
            return localImpl.getClientObject((String) args[0]);
            
        } 

        // Process finder, create method, or home method.
        EJBLocalObjectImpl localObjectImpl = null;
        Object returnValue = null;

        if( !isEntity_ && invInfo.startsWithCreate ) {
            localObjectImpl = createEJBLocalObjectImpl();
            returnValue = localObjectImpl.getClientObject();
        }
 
        if( !isStatelessSession_ ) {

            if( invInfo.targetMethod1 == null ) {

                Object [] params = new Object[] 
                    { invInfo.ejbName, "LocalHome", 
                      invInfo.method.toString() };
                String errorMsg = localStrings.getLocalString
                    ("ejb.bean_class_method_not_found", "", params);
                throw new EJBException(errorMsg);
            }

            EjbInvocation inv = ((BaseContainer) getContainer()).createEjbInvocation();

            inv.isLocal = true;
            inv.isHome  = true;
            inv.method  = method;

            inv.clientInterface = localHomeIntfClass_;

            // Set cached invocation params.  This will save additional lookups
            // in BaseContainer.
            inv.transactionAttribute = invInfo.txAttr;
            inv.securityPermissions = invInfo.securityPermissions;
            inv.invocationInfo = invInfo;

            if( !isEntity_ && invInfo.startsWithCreate ) {
                inv.ejbObject = (EJBLocalRemoteObject) localObjectImpl;
            }

            try {

                container.preInvoke(inv);

                if( invInfo.startsWithCreate ) {

                    Object ejbCreateReturnValue = container.invokeTargetBeanMethod(
                        invInfo.targetMethod1, inv, inv.ejb, args, null);
                    if( isEntity_ ) {
                        container.postCreate(inv, ejbCreateReturnValue);
                        container.invokeTargetBeanMethod(invInfo.targetMethod2, 
                                            inv, inv.ejb, args, null);
                    } 
                    if( inv.ejbObject != null ) {
                        returnValue = ((EJBLocalObjectImpl)inv.ejbObject)
                            .getClientObject();
                    } 
                } else if (invInfo.startsWithFindByPrimaryKey) {
            EntityContainer entityContainer = (EntityContainer) container;
		    returnValue = entityContainer.invokeFindByPrimaryKey(
			invInfo.targetMethod1, inv, args);
                } else if ( invInfo.startsWithFind ) {

                    Object pKeys = container.invokeTargetBeanMethod(invInfo.targetMethod1,
                                      inv, inv.ejb, args, null);
                    returnValue = container.postFind(inv, pKeys, null);
                } else {

                    returnValue = container.invokeTargetBeanMethod(invInfo.targetMethod1,
                                      inv, inv.ejb, args, null);

                }
            } catch(InvocationTargetException ite) {
                inv.exception = ite.getCause();           
            } catch(Throwable c) {
                inv.exception = c;
            } finally {
                container.postInvoke(inv);
            }

            if (inv.exception != null) {
                InvocationHandlerUtil.throwLocalException
                    (inv.exception, method.getExceptionTypes());
            }
        }

        return returnValue;
        } finally {
            if( originalClassLoader != null ) {
                Utility.setContextClassLoader(originalClassLoader);
            }

            ((BaseContainer) getContainer()).onLeavingContainer();
        }
    }
}
