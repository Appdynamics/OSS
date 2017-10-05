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
 * MessageDrivenBeanNode.java
 *
 * Created on February 1, 2002, 11:25 AM
 */

package com.sun.enterprise.deployment.node.ejb;

import com.sun.enterprise.deployment.*;
import com.sun.enterprise.deployment.node.*;
import com.sun.enterprise.deployment.xml.EjbTagNames;
import com.sun.enterprise.deployment.xml.TagNames;
import org.w3c.dom.Node;

import java.util.Map;

/**
 * This class handles message-driven related xml information
 *
 * @author  Jerome Dochez
 * @version 
 */
public class MessageDrivenBeanNode extends EjbNode {

    private EjbMessageBeanDescriptor descriptor;
        
    public MessageDrivenBeanNode() {
        super();
        registerElementHandler(new XMLElement(EjbTagNames.ACTIVATION_CONFIG),
                               ActivationConfigNode.class,
                               "setActivationConfigDescriptor");

        registerElementHandler(new XMLElement(EjbTagNames.AROUND_INVOKE_METHOD), AroundInvokeNode.class, "addAroundInvokeDescriptor"); 

        registerElementHandler(new XMLElement(EjbTagNames.AROUND_TIMEOUT_METHOD), AroundTimeoutNode.class, "addAroundTimeoutDescriptor"); 

        registerElementHandler(new XMLElement(TagNames.POST_CONSTRUCT), LifecycleCallbackNode.class, "addPostConstructDescriptor");

        registerElementHandler(new XMLElement(TagNames.PRE_DESTROY), LifecycleCallbackNode.class, "addPreDestroyDescriptor");

        registerElementHandler(new XMLElement(TagNames.DATA_SOURCE), DataSourceDefinitionNode.class, "addDataSourceDefinitionDescriptor");

        registerElementHandler(new XMLElement(EjbTagNames.TIMEOUT_METHOD), MethodNode.class, "setEjbTimeoutMethod");      

        registerElementHandler(new XMLElement(EjbTagNames.ROLE_REFERENCE), SecurityRoleRefNode.class, "addRoleReference");
    }

    /**
     * @return the descriptor instance to associate with this XMLNode
     */    
    public EjbDescriptor getEjbDescriptor() {
        
        if (descriptor==null) {
            descriptor = (EjbMessageBeanDescriptor) DescriptorFactory.getDescriptor(getXMLPath());
            descriptor.setEjbBundleDescriptor((EjbBundleDescriptor) getParentNode().getDescriptor());                        
        }
        return descriptor;
    }        

    /**
     * all sub-implementation of this class can use a dispatch table to map xml element to
     * method name on the descriptor class for setting the element value. 
     *  
     * @return the map with the element name as a key, the setter method as a value
     */    
    protected Map getDispatchTable() {
        // no need to be synchronized for now
        Map table = super.getDispatchTable();

        table.put(EjbTagNames.MESSAGING_TYPE, "setMessageListenerType"); 
        table.put(EjbTagNames.TRANSACTION_TYPE, "setTransactionType");
        table.put(EjbTagNames.MESSAGE_DESTINATION_TYPE, "setDestinationType");
        table.put(TagNames.MESSAGE_DESTINATION_LINK, 
                  "setMessageDestinationLinkName");

        // These are the EJB 2.0 elements that no longer exist in the EJB 2.1
        // schema.  We still set them on the descriptor but they will be
        // written out as activation config properties.
        table.put(EjbTagNames.MSG_SELECTOR, "setJmsMessageSelector");
        table.put(EjbTagNames.JMS_ACKNOWLEDGE_MODE, "setJmsAcknowledgeMode");
        table.put(EjbTagNames.JMS_DEST_TYPE, "setDestinationType");        
        table.put(EjbTagNames.JMS_SUBSCRIPTION_DURABILITY, 
                  "setSubscriptionDurability");        
        return table;
    }    
    
    /**
     * write the descriptor class to a DOM tree and return it
     *
     * @param parent node for the DOM tree
     * @param node name for the root element of this xml fragment      
     * @param the descriptor to write
     * @return the DOM tree top node
     */    
    public Node writeDescriptor(Node parent, String nodeName, Descriptor descriptor) {
        if (! (descriptor instanceof EjbMessageBeanDescriptor)) {
            throw new IllegalArgumentException(getClass() + " cannot handles descriptors of type " + descriptor.getClass());
        }    
        EjbMessageBeanDescriptor ejbDesc = (EjbMessageBeanDescriptor) descriptor;
        
        Node ejbNode = super.writeDescriptor(parent, nodeName, descriptor);
        writeDisplayableComponentInfo(ejbNode, descriptor);
        writeCommonHeaderEjbDescriptor(ejbNode, ejbDesc);
        appendTextChild(ejbNode, EjbTagNames.EJB_CLASS, ejbDesc.getEjbClassName());             
        appendTextChild(ejbNode, EjbTagNames.MESSAGING_TYPE, ejbDesc.getMessageListenerType());


        MethodNode methodNode = new MethodNode();
        
        if( ejbDesc.isTimedObject() ) {
            if (ejbDesc.getEjbTimeoutMethod() != null) {
                methodNode.writeJavaMethodDescriptor
                        (ejbNode, EjbTagNames.TIMEOUT_METHOD,
                         ejbDesc.getEjbTimeoutMethod());
            }

            for ( ScheduledTimerDescriptor timerDesc : ejbDesc.getScheduledTimerDescriptors()) {
                ScheduledTimerNode timerNode = new ScheduledTimerNode();
                timerNode.writeDescriptor(ejbNode, EjbTagNames.TIMER, timerDesc);
            }
        }

        appendTextChild(ejbNode, EjbTagNames.TRANSACTION_TYPE, ejbDesc.getTransactionType());                   

        // message-destination-type
        appendTextChild(ejbNode, TagNames.MESSAGE_DESTINATION_TYPE,
                        ejbDesc.getDestinationType());

        // message-destination-link
        String link = ejbDesc.getMessageDestinationLinkName();
        appendTextChild(ejbNode, TagNames.MESSAGE_DESTINATION_LINK, link);
        
        ActivationConfigNode activationConfigNode = new ActivationConfigNode();
        activationConfigNode.writeDescriptor
            (ejbNode, EjbTagNames.ACTIVATION_CONFIG,
             ejbDesc.getActivationConfigDescriptor());

        // around-invoke-method
        writeAroundInvokeDescriptors(ejbNode, ejbDesc.getAroundInvokeDescriptors().iterator());

        // around-timeout-method
        writeAroundTimeoutDescriptors(ejbNode, ejbDesc.getAroundTimeoutDescriptors().iterator());

        // env-entry*
        writeEnvEntryDescriptors(ejbNode, ejbDesc.getEnvironmentProperties().iterator());
        
        // ejb-ref * and ejb-local-ref*
        writeEjbReferenceDescriptors(ejbNode, ejbDesc.getEjbReferenceDescriptors().iterator());

        // service-ref*
        writeServiceReferenceDescriptors(ejbNode, ejbDesc.getServiceReferenceDescriptors().iterator());
        
        // resource-ref*
        writeResourceRefDescriptors(ejbNode, ejbDesc.getResourceReferenceDescriptors().iterator());
        
        // resource-env-ref*
        writeResourceEnvRefDescriptors(ejbNode, ejbDesc.getJmsDestinationReferenceDescriptors().iterator());        
        
        // message-destination-ref*
        writeMessageDestinationRefDescriptors(ejbNode, ejbDesc.getMessageDestinationReferenceDescriptors().iterator());

        // persistence-context-ref*
        writeEntityManagerReferenceDescriptors(ejbNode, ejbDesc.getEntityManagerReferenceDescriptors().iterator());
        
        // persistence-unit-ref*
        writeEntityManagerFactoryReferenceDescriptors(ejbNode, ejbDesc.getEntityManagerFactoryReferenceDescriptors().iterator());

        // post-construct
        writePostConstructDescriptors(ejbNode, ejbDesc.getPostConstructDescriptors().iterator());

        // pre-destroy
        writePreDestroyDescriptors(ejbNode, ejbDesc.getPreDestroyDescriptors().iterator());

        // datasource-definition*
        writeDataSourceDefinitionDescriptors(ejbNode, ejbDesc.getDataSourceDefinitionDescriptors().iterator());
        
        // security-role-ref*
        writeRoleReferenceDescriptors(ejbNode, ejbDesc.getRoleReferences().iterator());

        // security-identity
        writeSecurityIdentityDescriptor(ejbNode, ejbDesc);
        
        return ejbNode;
    }
}
