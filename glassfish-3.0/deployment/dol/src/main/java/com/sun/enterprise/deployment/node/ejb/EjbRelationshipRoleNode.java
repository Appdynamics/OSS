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
 * EjbRelationshipRoleNode.java
 *
 * Created on February 1, 2002, 3:22 PM
 */

package com.sun.enterprise.deployment.node.ejb;

import com.sun.enterprise.deployment.EjbBundleDescriptor;
import com.sun.enterprise.deployment.EjbCMPEntityDescriptor;
import com.sun.enterprise.deployment.RelationRoleDescriptor;
import com.sun.enterprise.deployment.node.*;
import com.sun.enterprise.deployment.xml.EjbTagNames;
import org.w3c.dom.Node;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is responsible for handling the ejb-relationship-role xml elements
 *
 * @author  Jerome Dochez
 * @version 
 */
public class EjbRelationshipRoleNode extends DeploymentDescriptorNode {

    RelationRoleDescriptor descriptor;

    public EjbRelationshipRoleNode() {
       super();
       registerElementHandler(new XMLElement(EjbTagNames.DESCRIPTION), LocalizedInfoNode.class);           
    }
    
    /**
     * @return the handler registered for the subtag element of the curent  XMLNode
     */
    public  XMLNode getHandlerFor(XMLElement element) {
        if (EjbTagNames.RELATIONSHIP_ROLE_SOURCE.equals(element.getQName())) {
            Map dispatchTable = new HashMap();
            dispatchTable.put(EjbTagNames.DESCRIPTION, "setRoleSourceDescription");
            ConfigurableNode newNode = new ConfigurableNode(getDescriptor(), dispatchTable, element);
            return newNode;
        } if (EjbTagNames.CMR_FIELD.equals(element.getQName())) {
            Map dispatchTable = new HashMap();
            dispatchTable.put(EjbTagNames.DESCRIPTION, "setCMRFieldDescription");
            ConfigurableNode newNode = new ConfigurableNode(getDescriptor(), dispatchTable, element);
            return newNode;            
        } else {
            return super.getHandlerFor(element);
        }       
    }  
    
    /**
     *  @return true if the element tag can be handled by any registered sub nodes of the
     * current XMLNode
     */
    public boolean handlesElement(XMLElement element) {
        if (EjbTagNames.RELATIONSHIP_ROLE_SOURCE.equals(element.getQName())) {
            return true;
        } 
        if (EjbTagNames.CMR_FIELD.equals(element.getQName())) {    
            return true;
        } 
        return super.handlesElement(element);
    }
    
   /**
    * @return the descriptor instance to associate with this XMLNode
    */    
    public Object getDescriptor() {
        if (descriptor==null) {
            descriptor = (RelationRoleDescriptor) DescriptorFactory.getDescriptor(getXMLPath());
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
        table.put(EjbTagNames.EJB_RELATIONSHIP_ROLE_NAME, "setName");
        table.put(EjbTagNames.CMR_FIELD_NAME, "setCMRField");
        table.put(EjbTagNames.CMR_FIELD_TYPE, "setCMRFieldType");
        return table;
    }    
    
    /** 
     * receives notification of the end of an XML element by the Parser
     * 
     * @param element the xml tag identification
     * @return true if this node is done processing the XML sub tree
     */
    public boolean endElement(XMLElement element) {
        if (EjbTagNames.CASCADE_DELETE.equals(element.getQName())) {
                descriptor.setCascadeDelete(true);
        }
        return super.endElement(element);
    }    
    
    /**
     * receives notiification of the value for a particular tag
     * 
     * @param element the xml element
     * @param value it's associated value
     */
    public void setElementValue(XMLElement element, String value) {    
        if (EjbTagNames.MULTIPLICITY.equals(element.getQName())) {
            if ( value.equals("Many") )
                descriptor.setIsMany(true);
            else if ( value.equals("One") )
                descriptor.setIsMany(false);
            else if ( value.equals("many") ) // for backward compat with 1.3 FCS
                descriptor.setIsMany(true);
            else if ( value.equals("one") ) // for backward compat with 1.3 FCS
                descriptor.setIsMany(false);
            else 
                throw new IllegalArgumentException("Error in value of multiplicity element in EJB deployment descriptor XML: the value must be One or Many");
        } else if (EjbTagNames.EJB_NAME.equals(element.getQName())) {
            // let's get our bunlde descriptor...
                EjbBundleDescriptor bundleDesc = getEjbBundleDescriptor();
                EjbCMPEntityDescriptor desc = (EjbCMPEntityDescriptor)bundleDesc.getEjbByName(value);
                if (desc!=null){
                    descriptor.setPersistenceDescriptor(desc.getPersistenceDescriptor());            
                } else {
                    throw new IllegalArgumentException("Cannot find ejb " + value + " in bundle for relationship " + descriptor.getName());
                }                
        } else super.setElementValue(element, value);
    }
    
    private EjbBundleDescriptor getEjbBundleDescriptor() {
        XMLNode parent = getParentNode();
        Object parentDesc = parent.getDescriptor();
        while (parent!=null && !(parentDesc instanceof EjbBundleDescriptor)) {
            parent = parent.getParentNode();
            if (parent !=null)
                parentDesc = parent.getDescriptor();
        }
        if (parent!=null) {
            return (EjbBundleDescriptor) parentDesc;
        }  else {
            throw new IllegalArgumentException("Cannot find bundle descriptor");
        }            
    } 
    
    /**
     * write the descriptor class to a DOM tree and return it
     *
     * @param parent node in the DOM tree 
     * @param node name for the root element of this xml fragment      
     * @param the descriptor to write
     * @return the DOM tree top node
     */
    public Node writeDescriptor(Node parent, String nodeName, RelationRoleDescriptor descriptor) {   
        Node roleNode = super.writeDescriptor(parent, nodeName, descriptor);
        LocalizedInfoNode localizedNode = new LocalizedInfoNode();
        localizedNode.writeLocalizedMap(roleNode, EjbTagNames.DESCRIPTION, descriptor.getLocalizedDescriptions());        
        if (descriptor.getRelationRoleName() != null) {
            appendTextChild(roleNode, EjbTagNames.EJB_RELATIONSHIP_ROLE_NAME, 
                descriptor.getRelationRoleName());        
        }
        
        // multiplicity
        if (descriptor.getIsMany()) {
            appendTextChild(roleNode, EjbTagNames.MULTIPLICITY, "Many");        
        } else {
            appendTextChild(roleNode, EjbTagNames.MULTIPLICITY, "One");        
        }
        
        // cascade-delete
        if (descriptor.getCascadeDelete()) {
            appendChild(roleNode, EjbTagNames.CASCADE_DELETE);        
        }
        
        Node roleSourceNode = appendChild(roleNode, EjbTagNames.RELATIONSHIP_ROLE_SOURCE);
        appendTextChild(roleSourceNode, EjbTagNames.DESCRIPTION, descriptor.getRoleSourceDescription());
        appendTextChild(roleSourceNode, EjbTagNames.EJB_NAME, descriptor.getOwner().getName());
        
	// cmr-field
	if (descriptor.getCMRField() != null ) {
            Node cmrFieldNode = appendChild(roleNode, EjbTagNames.CMR_FIELD);
            
	    // description
            appendTextChild(cmrFieldNode, EjbTagNames.DESCRIPTION, 
                                        descriptor.getCMRFieldDescription());
	    // cmr-field-name
	    appendTextChild(cmrFieldNode, EjbTagNames.CMR_FIELD_NAME, 
                                        descriptor.getCMRField());
	    // cmr-field-type
            appendTextChild(cmrFieldNode, EjbTagNames.CMR_FIELD_TYPE,
                                        descriptor.getCMRFieldType());
	}              
        return roleNode;
    }        
}
