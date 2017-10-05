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
package com.sun.enterprise.deployment.annotation.context;

import com.sun.enterprise.deployment.*;
import com.sun.enterprise.deployment.types.*;
import org.glassfish.apf.context.AnnotationContext;

import java.util.Iterator;
import java.util.Set;

/**
 * This provides an abstraction for handle resource references.
 *
 * @Author Shing Wai Chan
 */
public class ResourceContainerContextImpl extends AnnotationContext
        implements ResourceContainerContext, ComponentContext, 
                   ServiceReferenceContainerContext, HandlerContext {

    protected Descriptor descriptor = null;
    protected String componentClassName = null;

    public ResourceContainerContextImpl() { 
    } 

    public ResourceContainerContextImpl(Descriptor descriptor) { 
        this.descriptor = descriptor;         
    } 
    
    /**
     * Add a reference to an ejb.
     *
     * @param ejbReference the ejb reference
     */
    public void addEjbReferenceDescriptor(EjbReference ejbReference) {
        getEjbReferenceContainer().addEjbReferenceDescriptor(ejbReference);
    }
                                                                               
    /**
     * Looks up an ejb reference with the given name.
     * Return null if it is not found.
     *
     * @param the name of the ejb-reference
     */
    public EjbReference getEjbReference(String name) {
        EjbReference ejbRef = null;
        try {
            ejbRef = getEjbReferenceContainer().getEjbReference(name);
            // annotation has a corresponding ejb-local-ref/ejb-ref
            // in xml.  Just add annotation info and continue.
            // This logic might change depending on overriding rules
            // and order in which annotations are read w.r.t. to xml.
            // E.g. sparse overriding in xml or loading annotations
            // first.  
        } catch(IllegalArgumentException e) {
            // DOL API is (unfortunately) defined to return 
            // IllegalStateException if name doesn't exist.

            Application app = getAppFromDescriptor();

            if( app != null ) {
                try {
                    // Check for java:app/java:global dependencies at app-level
                    ejbRef = app.getEjbReferenceByName(name);
                     // Make sure it's added to the container context.
                    addEjbReferenceDescriptor(ejbRef);
                } catch(IllegalArgumentException ee) {}
            }
        }
        return ejbRef;
    }

    protected EjbReferenceContainer getEjbReferenceContainer() {
        return (EjbReferenceContainer)descriptor;
    }

    public void addResourceReferenceDescriptor(ResourceReferenceDescriptor 
                                               resReference) {
        getResourceReferenceContainer().addResourceReferenceDescriptor
            (resReference);
    }
                                                                               
    /**
     * Looks up an resource reference with the given name.
     * Return null if it is not found.
     *
     * @param the name of the resource-reference
     */
    public ResourceReferenceDescriptor getResourceReference(String name) {
        ResourceReferenceDescriptor resourceRef = null;
        try {
            resourceRef = getResourceReferenceContainer().
                getResourceReferenceByName(name);
            // annotation has a corresponding resource-ref
            // in xml.  Just add annotation info and continue.
            // This logic might change depending on overriding rules
            // and order in which annotations are read w.r.t. to xml.
            // E.g. sparse overriding in xml or loading annotations
            // first.  
        } catch(IllegalArgumentException e) {
            // DOL API is (unfortunately) defined to return 
            // IllegalStateException if name doesn't exist.

            Application app = getAppFromDescriptor();

            if( app != null ) {
                try {
                    // Check for java:app/java:global dependencies at app-level
                    resourceRef = app.getResourceReferenceByName(name);
                    // Make sure it's added to the container context.
                    addResourceReferenceDescriptor(resourceRef);
                } catch(IllegalArgumentException ee) {}
            }
        }
        return resourceRef;
    }

    protected ResourceReferenceContainer getResourceReferenceContainer() {
        return (ResourceReferenceContainer)descriptor;
    }


    public void addMessageDestinationReferenceDescriptor
        (MessageDestinationReferenceDescriptor msgDestReference) {
        getMessageDestinationReferenceContainer(
        ).addMessageDestinationReferenceDescriptor(msgDestReference);
    }
                                               
                                                                               
    public MessageDestinationReferenceDescriptor getMessageDestinationReference
        (String name) {
        MessageDestinationReferenceDescriptor msgDestRef = null;
        try {
            msgDestRef = getMessageDestinationReferenceContainer().
                getMessageDestinationReferenceByName(name);
            // annotation has a corresponding message-destination-ref
            // in xml.  Just add annotation info and continue.
            // This logic might change depending on overriding rules
            // and order in which annotations are read w.r.t. to xml.
            // E.g. sparse overriding in xml or loading annotations
            // first.  
        } catch(IllegalArgumentException e) {
            // DOL API is (unfortunately) defined to return 
            // IllegalStateException if name doesn't exist.
        }
        return msgDestRef;
    }

    protected MessageDestinationReferenceContainer 
        getMessageDestinationReferenceContainer()
    {
        return (MessageDestinationReferenceContainer)descriptor;
    }

    public void addJmsDestinationReferenceDescriptor
        (JmsDestinationReferenceDescriptor jmsDestReference) {
        getJmsDestinationReferenceContainer(
        ).addJmsDestinationReferenceDescriptor(jmsDestReference);
    }
                                               
    public JmsDestinationReferenceDescriptor getJmsDestinationReference
        (String name) {
        JmsDestinationReferenceDescriptor jmsDestRef = null;
        try {
            jmsDestRef = getJmsDestinationReferenceContainer().
                getJmsDestinationReferenceByName(name);
            // annotation has a corresponding resource-env-ref
            // in xml.  Just add annotation info and continue.
            // This logic might change depending on overriding rules
            // and order in which annotations are read w.r.t. to xml.
            // E.g. sparse overriding in xml or loading annotations
            // first.  
        } catch(IllegalArgumentException e) {
            // DOL API is (unfortunately) defined to return
            // IllegalStateException if name doesn't exist.

            Application app = getAppFromDescriptor();

            if( app != null ) {
                try {
                    // Check for java:app/java:global dependencies at app-level
                    jmsDestRef = app.getJmsDestinationReferenceByName(name);
                      // Make sure it's added to the container context.
                    addJmsDestinationReferenceDescriptor(jmsDestRef);
                } catch(IllegalArgumentException ee) {}
            }
        }
        return jmsDestRef;
    }

    protected WritableJndiNameEnvironment 
        getJmsDestinationReferenceContainer()
    {
        return (WritableJndiNameEnvironment)descriptor;
    }

    public void addEnvEntryDescriptor(EnvironmentProperty envEntry) {

        getEnvEntryContainer().addEnvironmentProperty(envEntry);

    }
                                               
    public EnvironmentProperty getEnvEntry(String name) {
        EnvironmentProperty envEntry = null;
        try {
            envEntry = getEnvEntryContainer().
                getEnvironmentPropertyByName(name);
            // annotation has a corresponding env-entry
            // in xml.  Just add annotation info and continue.
            // This logic might change depending on overriding rules
            // and order in which annotations are read w.r.t. to xml.
            // E.g. sparse overriding in xml or loading annotations
            // first.  
        } catch(IllegalArgumentException e) {
            // DOL API is (unfortunately) defined to return 
            // IllegalStateException if name doesn't exist.           

            Application app = getAppFromDescriptor();

            if( app != null ) {
                try {
                    // Check for java:app/java:global dependencies at app-level
                    envEntry = app.getEnvironmentPropertyByName(name);
                      // Make sure it's added to the container context.
                    addEnvEntryDescriptor(envEntry);
                } catch(IllegalArgumentException ee) {}
            }

        }
        return envEntry;

    }
    
    protected WritableJndiNameEnvironment getEnvEntryContainer()
    {
        return (WritableJndiNameEnvironment)descriptor;
    }

    public void addEntityManagerFactoryReferenceDescriptor
        (EntityManagerFactoryReferenceDescriptor emfRefDesc) {

        getEmfRefContainer().addEntityManagerFactoryReferenceDescriptor
            (emfRefDesc);

    }
                                               
    public EntityManagerFactoryReferenceDescriptor 
        getEntityManagerFactoryReference(String name) {

        EntityManagerFactoryReferenceDescriptor emfRefDesc = null;

        try {
            emfRefDesc = getEmfRefContainer().
                getEntityManagerFactoryReferenceByName(name);
            // annotation has a corresponding entry
            // in xml.  Just add annotation info and continue.
            // This logic might change depending on overriding rules
            // and order in which annotations are read w.r.t. to xml.
            // E.g. sparse overriding in xml or loading annotations
            // first.  
        } catch(IllegalArgumentException e) {
            // DOL API is (unfortunately) defined to return 
            // IllegalStateException if name doesn't exist.

            Application app = getAppFromDescriptor();

            if( app != null ) {
                try {
                    // Check for java:app/java:global dependencies at app-level
                    emfRefDesc = app.getEntityManagerFactoryReferenceByName(name);
                    // Make sure it's added to the container context.
                    addEntityManagerFactoryReferenceDescriptor(emfRefDesc);
                } catch(IllegalArgumentException ee) {}
            }
        }

        return emfRefDesc;

    }
    
    protected WritableJndiNameEnvironment getEmfRefContainer()
    {
        return (WritableJndiNameEnvironment)descriptor;
    }


    public void addEntityManagerReferenceDescriptor
        (EntityManagerReferenceDescriptor emRefDesc) {

        getEmRefContainer().addEntityManagerReferenceDescriptor
            (emRefDesc);

    }
                                               
    public EntityManagerReferenceDescriptor 
        getEntityManagerReference(String name) {

        EntityManagerReferenceDescriptor emRefDesc = null;

        try {
            emRefDesc = getEmRefContainer().
                getEntityManagerReferenceByName(name);
            // annotation has a corresponding entry
            // in xml.  Just add annotation info and continue.
            // This logic might change depending on overriding rules
            // and order in which annotations are read w.r.t. to xml.
            // E.g. sparse overriding in xml or loading annotations
            // first.  
        } catch(IllegalArgumentException e) {
            // DOL API is (unfortunately) defined to return 
            // IllegalStateException if name doesn't exist.

            Application app = getAppFromDescriptor();

            if( app != null ) {
                try {
                    // Check for java:app/java:global dependencies at app-level
                    emRefDesc = app.getEntityManagerReferenceByName(name);
                    // Make sure it's added to the container context.
                    addEntityManagerReferenceDescriptor(emRefDesc);
                } catch(IllegalArgumentException ee) {}
            }
        }

        return emRefDesc;

    }
    
    protected WritableJndiNameEnvironment getEmRefContainer()
    {
        return (WritableJndiNameEnvironment)descriptor;
    }

   /**
     * @param postConstructDesc
     */
    public void addPostConstructDescriptor(
            LifecycleCallbackDescriptor postConstructDesc) {
        getPostConstructContainer().addPostConstructDescriptor(postConstructDesc);
    }

    /**
     * Look up an post-construct LifecycleCallbackDescriptor with the
     * given name.  Return null if it is not found
     * @param className
     */
    public LifecycleCallbackDescriptor getPostConstruct(String className) {
        LifecycleCallbackDescriptor postConstructDesc = 
            getPostConstructContainer().getPostConstructDescriptorByClass(className);
        return postConstructDesc;
    }

    protected WritableJndiNameEnvironment getPostConstructContainer() {
        return (WritableJndiNameEnvironment)descriptor;
    }

   /**
     * @param preDestroyDesc
     */
    public void addPreDestroyDescriptor(
            LifecycleCallbackDescriptor preDestroyDesc) {
        getPreDestroyContainer().addPreDestroyDescriptor(preDestroyDesc);
    }

    /**
     * Look up an pre-destroy LifecycleCallbackDescriptor with the
     * given name.  Return null if it is not found
     * @param className
     */
    public LifecycleCallbackDescriptor getPreDestroy(String className) {
        LifecycleCallbackDescriptor preDestroyDesc = 
            getPreDestroyContainer().getPreDestroyDescriptorByClass(className);
        return preDestroyDesc;
    }

    protected WritableJndiNameEnvironment getDataSourceDefinitionContainer(){
        return (WritableJndiNameEnvironment)descriptor;
    }

    /**
     * Adds the specified data-source-definition to the receiver.
     * @param desc DataSourceDefinitionDescriptor to add.
     */
    public void addDataSourceDefinitionDescriptor(DataSourceDefinitionDescriptor desc) {
        getDataSourceDefinitionContainer().addDataSourceDefinitionDescriptor(desc);
    }

    /**
     * get all datasource-definition descriptors
     * @return datasource-definition descriptors
     */
    public Set<DataSourceDefinitionDescriptor> getDataSourceDefinitionDescriptors() {
        return getDataSourceDefinitionContainer().getDataSourceDefinitionDescriptors();
    }

    protected WritableJndiNameEnvironment getPreDestroyContainer() {
        return (WritableJndiNameEnvironment)descriptor;
    }

    public String getComponentClassName() {
        return componentClassName;
    }
    
    public HandlerChainContainer[] 
            getHandlerChainContainers(boolean serviceSideHandlerChain, Class declaringClass) {
        // by default return null; appropriate contextx should override this
        return null;
    }
    
    public ServiceReferenceContainer[] getServiceRefContainers() {
        // by default we return our descriptor;
        ServiceReferenceContainer[] containers = new ServiceReferenceContainer[1];
        containers[0] = (ServiceReferenceContainer) descriptor;
        return containers;
    }

    public void addManagedBean(ManagedBeanDescriptor managedBeanDesc) {

        BundleDescriptor bundleDesc = (BundleDescriptor)
                ((BundleDescriptor) descriptor).getModuleDescriptor().getDescriptor();
        bundleDesc.addManagedBean(managedBeanDesc);             
    }

    private Application getAppFromDescriptor() {
        Application app = null;
        if( descriptor instanceof BundleDescriptor ) {
            BundleDescriptor bundle = (BundleDescriptor) descriptor;
            app = bundle.getApplication();
        } else if( descriptor instanceof EjbDescriptor ) {
            app = ((EjbDescriptor)descriptor).getApplication();
        }

        return app;
    }
}
