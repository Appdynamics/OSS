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

import javax.ejb.*;

import javax.transaction.UserTransaction;
import java.util.logging.*;
import com.sun.logging.*;
import com.sun.enterprise.deployment.EjbDescriptor;
import com.sun.enterprise.deployment.RoleReference;
import com.sun.ejb.EjbInvocation;
import org.glassfish.api.invocation.ComponentInvocation;

/**
 * Implementation of EJBContext for message-driven beans
 *
 * @author Kenneth Saks
 */

public final class MessageBeanContextImpl
        extends EJBContextImpl
        implements MessageDrivenContext
{

    private boolean afterSetContext = false;

    MessageBeanContextImpl(Object ejb, BaseContainer container)
    {
        super(ejb, container);
    }    

    void setEJBStub(EJBObject ejbStub)
    {
	throw new RuntimeException("No stubs for Message-driven beans");
    }

    void setEJBObjectImpl(EJBObjectImpl ejbo)
    {
	throw new RuntimeException("No EJB Object for Message-driven beans");
    }

    //FIXME later
    EJBObjectImpl getEJBObjectImpl() {
	    throw new RuntimeException("No EJB Object for Message-driven beans");
    }

    public void setContextCalled() {
        this.afterSetContext = true;
    }

    /*****************************************************************
     *    The following are implementations of EJBContext methods.
     ******************************************************************/

    /**
     * 
     */
    public UserTransaction getUserTransaction()
	throws java.lang.IllegalStateException
    {
	// The state check ensures that an exception is thrown if this
	// was called from the constructor or setMessageDrivenContext.
	// The remaining checks are performed by the container.
	if ( !this.afterSetContext ) {
	    throw new java.lang.IllegalStateException("Operation not allowed");
        }

	return ((BaseContainer)getContainer()).getUserTransaction();
    }

    /*
     * Doesn't make any sense to get EJBHome object for 
     * a message-driven ejb.
     */
    public EJBHome getEJBHome() 
    {
        RuntimeException exception = new java.lang.IllegalStateException
            ("getEJBHome not allowed for message-driven beans");
        throw exception;
    }


    protected void checkAccessToCallerSecurity()
        throws java.lang.IllegalStateException
    {
        // A message-driven ejb's state transitions past UNINITIALIZED
        // AFTER ejbCreate
        if ( isUnitialized() || isInEjbRemove() ) {
            throw new java.lang.IllegalStateException("Operation not allowed");
        }

    }

    public boolean isCallerInRole(String roleRef) {
        if ( roleRef == null )
            throw new IllegalStateException("Argument is null");

        checkAccessToCallerSecurity();

        ComponentInvocation inv =
                    EjbContainerUtilImpl.getInstance().getCurrentInvocation();
        if ( inv instanceof EjbInvocation) {
            EjbInvocation ejbInv = (EjbInvocation) inv;
            if( ejbInv.isTimerCallback ) {
                throw new IllegalStateException("isCallerInRole not allowed from timer callback");
            }

        } else {
            throw new IllegalStateException("not invoked from within a message-bean context");
        }

        EjbDescriptor ejbd = container.getEjbDescriptor();
        RoleReference rr = ejbd.getRoleReferenceByName(roleRef);

        if ( rr == null ) {
            throw new IllegalStateException(
                "No mapping available for role reference " + roleRef);
        }

        com.sun.enterprise.security.SecurityManager sm = container.getSecurityManager();
	    return sm.isCallerInRole(roleRef);
    }
    
    public TimerService getTimerService() 
        throws java.lang.IllegalStateException {

        if( !afterSetContext ) {
            throw new java.lang.IllegalStateException("Operation not allowed");
        }

        EJBTimerService timerService =
                        EjbContainerUtilImpl.getInstance().getEJBTimerService();
        if( timerService == null ) {
            throw new EJBException("EJB Timer service not available");
        }
        return new EJBTimerServiceWrapper(timerService, this);
    }

    public void checkTimerServiceMethodAccess() 
        throws java.lang.IllegalStateException {

        // A message-driven ejb's state transitions past UNINITIALIZED
        // AFTER ejbCreate
        if ( isUnitialized() || isInEjbRemove() ) {
            throw new java.lang.IllegalStateException
                ("EJB Timer Service method calls cannot be called in " +
                 " this context");
        } 
    }

}
