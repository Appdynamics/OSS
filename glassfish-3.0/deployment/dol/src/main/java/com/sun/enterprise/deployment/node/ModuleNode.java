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

package com.sun.enterprise.deployment.node;

import com.sun.enterprise.deployment.util.ModuleDescriptor;
import com.sun.enterprise.deployment.util.XModuleType;
import com.sun.enterprise.deployment.xml.ApplicationTagNames;
import org.w3c.dom.Node;

import javax.enterprise.deploy.shared.ModuleType;
import java.util.Map;

/**
 * This node is responsible for handling the module xml fragment from 
 * application.xml files
 *
 * @author  Jerome Dochez
 * @version 
 */
public class ModuleNode extends DeploymentDescriptorNode {    
    
   /**
     * all sub-implementation of this class can use a dispatch table to map xml element to
     * method name on the descriptor class for setting the element value. 
     *  
     * @return the map with the element name as a key, the setter method as a value
     */    
    protected Map getDispatchTable() {    
        Map table = super.getDispatchTable();
        table.put(ApplicationTagNames.ALTERNATIVE_DD, "setAlternateDescriptor");
        table.put(ApplicationTagNames.CONTEXT_ROOT, "setContextRoot");
        return table;
    }     
        
    /**
     * receives notification of the value for a particular tag
     * 
     * @param element the xml element
     * @param value it's associated value
     */    
    public void setElementValue(XMLElement element, String value) {
        ModuleDescriptor descriptor = (ModuleDescriptor) getDescriptor();
         if (element.getQName().equals(ApplicationTagNames.WEB_URI)) {            
            descriptor.setModuleType(XModuleType.WAR);
            descriptor.setArchiveUri(value);                
        } else if (element.getQName().equals(ApplicationTagNames.EJB)) {
            descriptor.setModuleType(XModuleType.EJB);
            descriptor.setArchiveUri(value);
        } else if (element.getQName().equals(ApplicationTagNames.CONNECTOR)) {
            descriptor.setModuleType(XModuleType.RAR);
            descriptor.setArchiveUri(value);
        } else if (element.getQName().equals(ApplicationTagNames.APPLICATION_CLIENT)) {
            descriptor.setModuleType(XModuleType.CAR);
            descriptor.setArchiveUri(value);
        } else if (element.getQName().equals(ApplicationTagNames.WEB)) {
            descriptor.setModuleType(XModuleType.WAR);
        } else super.setElementValue(element, value);
    }    
        
    /**
     * write the descriptor class to a DOM tree and return it
     *
     * @param parent node in the DOM tree 
     * @param node name for the root element of this xml fragment      
     * @param the descriptor to write
     * @return the DOM tree top node
     */
    public Node writeDescriptor(Node parent, String nodeName, ModuleDescriptor descriptor) {   
        
        Node module = appendChild(parent, nodeName);
        if (XModuleType.WAR.equals(descriptor.getModuleType())) {            
            Node modType = appendChild(module, ApplicationTagNames.WEB);
            appendTextChild(modType, ApplicationTagNames.WEB_URI, descriptor.getArchiveUri());
            forceAppendTextChild(modType, ApplicationTagNames.CONTEXT_ROOT, descriptor.getContextRoot());

        } else {
            // default initialization if ejb...
            String type = ApplicationTagNames.EJB;
            if (XModuleType.CAR.equals(descriptor.getModuleType())) {
                type = ApplicationTagNames.APPLICATION_CLIENT;
            } else if (XModuleType.RAR.equals(descriptor.getModuleType())) {
                type = ApplicationTagNames.CONNECTOR;
            }
            appendTextChild(module, type, descriptor.getArchiveUri());
        }
        appendTextChild(module,ApplicationTagNames.ALTERNATIVE_DD, descriptor.getAlternateDescriptor());
        return module;
    }
}
