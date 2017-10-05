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
import com.sun.enterprise.deployment.node.DeploymentDescriptorNode;
import com.sun.enterprise.deployment.node.DescriptorFactory;
import com.sun.enterprise.deployment.node.MethodNode;
import com.sun.enterprise.deployment.node.XMLElement;
import com.sun.enterprise.deployment.xml.EjbTagNames;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;

/**
 * This class handles all the method-permission xml tag
 * information 
 *
 * @author  Jerome Dochez
 * @version 
 */
public class MethodPermissionNode extends DeploymentDescriptorNode {

    private MethodPermissionDescriptor descriptor;
    
    /** Creates new MethodPermissionNode */
    public MethodPermissionNode() {       
        super();
       registerElementHandler(new XMLElement(EjbTagNames.METHOD), 
                                                            MethodNode.class, "addMethod");                 
    }   
    
   /**
    * @return the descriptor instance to associate with this XMLNode
    */       
    public Object getDescriptor() {
        
       if (descriptor==null) {
            descriptor = (MethodPermissionDescriptor) DescriptorFactory.getDescriptor(getXMLPath());
        }
        return descriptor;
    }   
    
    /**
     * SAX Parser API implementation, we don't really care for now.
     */
    public void startElement(XMLElement element, Attributes attributes) {
        
        if (EjbTagNames.UNCHECKED.equals(element.getQName())) {
            descriptor.addMethodPermission(MethodPermission.getUncheckedMethodPermission());
        } else 
            super.startElement(element, attributes);
    }
        
    /**
     * receives notiification of the value for a particular tag
     * 
     * @param element the xml element
     * @param value it's associated value
     */    
    public void setElementValue(XMLElement element, String value) {
        if (EjbTagNames.ROLE_NAME.equals(element.getQName())) {
            Role role = new Role(value);
            descriptor.addMethodPermission(new MethodPermission(role));
        } else {
            super.setElementValue(element, value);
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
    public Node writeDescriptor(Node parent, String nodeName, MethodPermissionDescriptor descriptor, 
                EjbDescriptor ejb) {        
                    
        Node subNode = super.writeDescriptor(parent, nodeName, descriptor);        
        return writeDescriptorInNode(subNode, descriptor, ejb);
    }
    
    
    /**
     * Write the descriptor in a DOM tree which root element is provided
     * 
     * @param subNode the root element for the DOM fragment
     * @param descriptor the method permisison descriptor
     * @param ejb the ejb descriptor the above method permission belongs to
     */
    public Node writeDescriptorInNode(Node subNode, MethodPermissionDescriptor descriptor, 
                EjbDescriptor ejb) {        
                    
        writeLocalizedDescriptions(subNode, descriptor);
        
        MethodPermission[] mps = descriptor.getMethodPermissions();
        if (mps.length==0)
            return null;
        
        if (!mps[0].isExcluded()) {
            if (mps[0].isUnchecked()) {
                appendChild(subNode, EjbTagNames.UNCHECKED);
            } else {
                for (int i=0;i<mps.length;i++) {
                    appendTextChild(subNode, EjbTagNames.ROLE_NAME, mps[i].getRole().getName());
                }        
            }
        } 
                
        MethodDescriptor[] methods = descriptor.getMethods();
        MethodNode mn = new MethodNode();
        for (int i=0;i<methods.length;i++) {            
            String ejbName = ejb.getName();
            mn.writeDescriptor(subNode, EjbTagNames.METHOD, methods[i], ejbName);
        }            
        return subNode;
    }
}
