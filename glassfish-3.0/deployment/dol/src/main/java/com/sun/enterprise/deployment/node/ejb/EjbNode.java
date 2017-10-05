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

package com.sun.enterprise.deployment.node.ejb;

import com.sun.enterprise.deployment.*;
import com.sun.enterprise.deployment.node.*;
import com.sun.enterprise.deployment.types.EjbReference;
import com.sun.enterprise.deployment.util.DOLUtils;
import com.sun.enterprise.deployment.xml.EjbTagNames;
import com.sun.enterprise.deployment.xml.TagNames;
import com.sun.enterprise.deployment.xml.WebServicesTagNames;
import org.w3c.dom.Node;

import java.util.Map;
import java.util.Iterator;
import java.util.logging.Level;

/**
 * This class is responsible for handling all common information
 * shared by all types of entreprise beans (MDB, session, entity)
 *
 * @author  Jerome Dochez
 * @version 
 */
public abstract class EjbNode extends DisplayableComponentNode {

    /** Creates new EjbNode */
    public EjbNode() {
        super();
        registerElementHandler(new XMLElement(TagNames.ENVIRONMENT_PROPERTY), 
                                                             EnvEntryNode.class, "addEnvironmentProperty");                          
        registerElementHandler(new XMLElement(EjbTagNames.EJB_REFERENCE), EjbReferenceNode.class);     
        registerElementHandler(new XMLElement(EjbTagNames.EJB_LOCAL_REFERENCE), EjbLocalReferenceNode.class);     
        registerElementHandler(new XMLElement(WebServicesTagNames.SERVICE_REF), ServiceReferenceNode.class, "addServiceReferenceDescriptor");
        registerElementHandler(new XMLElement(EjbTagNames.RESOURCE_REFERENCE), 
                                                             ResourceRefNode.class, "addResourceReferenceDescriptor");
        registerElementHandler(new XMLElement(TagNames.DATA_SOURCE), DataSourceDefinitionNode.class, "addDataSourceDefinitionDescriptor");
        registerElementHandler(new XMLElement(EjbTagNames.SECURITY_IDENTITY),
                                                            SecurityIdentityNode.class);             
        registerElementHandler(new XMLElement(TagNames.RESOURCE_ENV_REFERENCE), 
                                                            ResourceEnvRefNode.class, "addJmsDestinationReferenceDescriptor");               
        registerElementHandler(new XMLElement(TagNames.MESSAGE_DESTINATION_REFERENCE), MessageDestinationRefNode.class);
        registerElementHandler(new XMLElement(TagNames.PERSISTENCE_CONTEXT_REF), EntityManagerReferenceNode.class, "addEntityManagerReferenceDescriptor");
        registerElementHandler(new XMLElement(TagNames.PERSISTENCE_UNIT_REF), EntityManagerFactoryReferenceNode.class, "addEntityManagerFactoryReferenceDescriptor");

        // Use special method for overrides because more than one schedule can be specified on a single method
        registerElementHandler(new XMLElement(EjbTagNames.TIMER), ScheduledTimerNode.class, "addScheduledTimerDescriptorFromDD");
    }

    /**
     * Adds  a new DOL descriptor instance to the descriptor instance associated with 
     * this XMLNode
     *
     * @param descriptor the new descriptor
     */    
    public void addDescriptor(Object  newDescriptor) {       
        if (newDescriptor instanceof EjbReference) {            
            if (DOLUtils.getDefaultLogger().isLoggable(Level.FINE)) {
                DOLUtils.getDefaultLogger().fine("Adding ejb ref " + newDescriptor);
            }
            getEjbDescriptor().addEjbReferenceDescriptor(
                        (EjbReference) newDescriptor);
        } else  if (newDescriptor instanceof RunAsIdentityDescriptor) {
            if (DOLUtils.getDefaultLogger().isLoggable(Level.FINE)) {
                DOLUtils.getDefaultLogger().fine("Adding security-identity" + newDescriptor);                   
            }
            getEjbDescriptor().setUsesCallerIdentity(false);
	    getEjbDescriptor().setRunAsIdentity((RunAsIdentityDescriptor) newDescriptor);
        } else if( newDescriptor instanceof 
                   MessageDestinationReferenceDescriptor ) {
            MessageDestinationReferenceDescriptor msgDestRef =
                (MessageDestinationReferenceDescriptor) newDescriptor;
            EjbBundleDescriptor ejbBundle = (EjbBundleDescriptor) 
                getParentNode().getDescriptor();
            // EjbBundle might not be set yet on EjbDescriptor, so set it
            // explicitly here.
            msgDestRef.setReferringBundleDescriptor(ejbBundle);
            getEjbDescriptor().addMessageDestinationReferenceDescriptor
                (msgDestRef);
        } else {
            super.addDescriptor(newDescriptor);
        }
    }      
    
    public Object getDescriptor() {
        return getEjbDescriptor();
    }
    
    public abstract EjbDescriptor getEjbDescriptor();
    
    /**
     * all sub-implementation of this class can use a dispatch table to map xml element to
     * method name on the descriptor class for setting the element value. 
     *  
     * @return the map with the element name as a key, the setter method as a value
     */    
    protected Map getDispatchTable() {
        // no need to be synchronized for now
        Map table = super.getDispatchTable();
        table.put(EjbTagNames.EJB_NAME, "setName");
        table.put(EjbTagNames.EJB_CLASS, "setEjbClassName");        
        table.put(TagNames.MAPPED_NAME, "setMappedName");        
        return table;
    }    
        
    /**
     * write the common descriptor info to a DOM tree and return it
     *
     * @param parent node for the DOM tree
     * @param the descriptor to write
     */    
    protected void writeCommonHeaderEjbDescriptor(Node ejbNode, Descriptor descriptor) {
        EjbDescriptor ejbDescriptor = (EjbDescriptor)descriptor;
        appendTextChild(ejbNode, EjbTagNames.EJB_NAME, descriptor.getName());                       
        appendTextChild(ejbNode, TagNames.MAPPED_NAME, ejbDescriptor.getMappedName());                       
    }    
    
    /**
     * write the security identity information about an EJB
     *
     * @param parent node for the DOM tree
     * @param the EJB descriptor the security information to be retrieved
     */        
    protected void writeSecurityIdentityDescriptor(Node parent,  EjbDescriptor descriptor) {
        
        if (!descriptor.getUsesCallerIdentity() && descriptor.getRunAsIdentity()==null) 
            return;
        
        SecurityIdentityNode node = new SecurityIdentityNode();
        node.writeDescriptor(parent, EjbTagNames.SECURITY_IDENTITY,  descriptor);
    }

    /**
     * write  the security role references to the DOM Tree
     *
     * @param parentNode for the DOM tree
     * @param refs iterator over the RoleReference descriptors to write
     */
    protected void writeRoleReferenceDescriptors(Node parentNode, Iterator refs) {
        SecurityRoleRefNode node = new SecurityRoleRefNode();
        for (;refs.hasNext();) {
            RoleReference roleRef = (RoleReference) refs.next();
            node.writeDescriptor(parentNode, EjbTagNames.ROLE_REFERENCE, roleRef);
        }
    }
}
