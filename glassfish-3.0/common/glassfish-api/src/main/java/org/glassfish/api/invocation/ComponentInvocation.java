/*
 * 
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 2008 Sun Microsystems, Inc. All rights reserved.
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
package org.glassfish.api.invocation;

import org.jvnet.hk2.annotations.Scoped;
import org.jvnet.hk2.annotations.Service;
import org.jvnet.hk2.component.PerLookup;

@Scoped(PerLookup.class)
@Service
public class ComponentInvocation
    implements Cloneable {

    public enum ComponentInvocationType {
        SERVLET_INVOCATION, EJB_INVOCATION,
        APP_CLIENT_INVOCATION, UN_INITIALIZED,
        SERVICE_STARTUP
    }

    private ComponentInvocationType invocationType
            = ComponentInvocationType.UN_INITIALIZED;

    private boolean preInvokeDoneStatus;

    private Boolean auth;

    // the component instance, type Servlet, Filter or EnterpriseBean
    public Object instance;

    // ServletContext for servlet, Container for EJB
    public Object container;

    public Object jndiEnvironment;

    public String componentId;

    public Object transaction;

    // true if transaction commit or rollback is
    // happening for this invocation context
    private boolean transactionCompleting = false;

    //  security context coming in a call
    // security context changes on a runas call - on a run as call
    // the old logged in security context is stored in here.
    public Object oldSecurityContext;
    
    private Object resourceTableKey;

    private ResourceHandler resourceHandler;

    public ComponentInvocation() {
        
    }

    
    public ComponentInvocation(String componentId,
            ComponentInvocationType invocationType,
            Object container) {
        this.componentId = componentId;
        this.invocationType = invocationType;
        this.container = container;
    }


    public ComponentInvocation(String componentId,
            ComponentInvocationType invocationType,
            Object instance, Object container,
            Object transaction) {
        this.componentId = componentId;
        this.invocationType = invocationType;
        this.instance = instance;
        this.container = container;
        this.transaction = transaction;
    }

    public ComponentInvocationType getInvocationType() {
        return invocationType;
    }

    public void setComponentInvocationType(ComponentInvocationType t) {
        this.invocationType = t;
    }

    public Object getInstance() {
        return instance;
    }

    public String getComponentId() {
        return this.componentId;
    }

    public Object getContainer() {
        return container;
    }

    public Object getContainerContext() {
        return container;
    }

    public Object getTransaction() {
        return transaction;
    }

    public void setTransaction(Object t) {
        this.transaction = t;
    }
    
    public Object getTransactionOperationsManager() {
        return null;
    }

    /** 
     * Sets the security context of the call coming in
     */
    public void setOldSecurityContext (Object sc){
	this.oldSecurityContext = sc;
    }
    /**
     * gets the security context of the call that came in
     * before a new context for runas is made
     */
    public Object getOldSecurityContext (){
	return oldSecurityContext;
    }

    public boolean isTransactionCompleting() {
        return transactionCompleting;
    }

    public void setTransactionCompeting(boolean value) {
        transactionCompleting = value;
    }

    public void setResourceTableKey(Object key) {
        this.resourceTableKey = key;
    }

    public Object getResourceTableKey() {
        return resourceTableKey;
    }

    public void setResourceHandler(ResourceHandler h) {
        resourceHandler = h;
    }

    public ResourceHandler getResourceHandler() {
        return resourceHandler;
    }

    public boolean isPreInvokeDone() {
        return preInvokeDoneStatus;
    }

    public void setPreInvokeDone(boolean value) {
        preInvokeDoneStatus = value;
    }

    public Boolean getAuth() {
        return auth;
    }

    public void setAuth(boolean value) {
        auth = value;
    }

    public ComponentInvocation clone() {
        ComponentInvocation newInv = null;
        try {
            newInv = (ComponentInvocation) super.clone();
        } catch (CloneNotSupportedException cnsEx) {
            //Shouldn't happen as we implement Cloneable
            throw new Error(cnsEx);
        }

        newInv.auth = null;
        newInv.preInvokeDoneStatus = false;
        newInv.instance = null;
        newInv.transaction = null;
        newInv.transactionCompleting = false;

        return newInv;
    }
}
