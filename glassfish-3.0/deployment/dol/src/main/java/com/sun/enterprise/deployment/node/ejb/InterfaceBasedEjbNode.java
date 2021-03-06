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

import com.sun.enterprise.deployment.EjbDescriptor;
import com.sun.enterprise.deployment.RoleReference;
import com.sun.enterprise.deployment.node.SecurityRoleRefNode;
import com.sun.enterprise.deployment.node.XMLElement;
import com.sun.enterprise.deployment.xml.EjbTagNames;
import org.w3c.dom.Node;

import java.util.Iterator;
import java.util.Map;

/**
 * This class is responsible for reading/writing all information
 * common to all EJB which are interfaces based (entity, session)
 *
 * @author  Jerome Dochez
 * @version 
 */
public abstract class InterfaceBasedEjbNode extends EjbNode {

   public InterfaceBasedEjbNode() {
       super();
       // register sub XMLNodes
       registerElementHandler(new XMLElement(EjbTagNames.ROLE_REFERENCE), SecurityRoleRefNode.class, "addRoleReference");       
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
        table.put(EjbTagNames.HOME, "setHomeClassName");
        table.put(EjbTagNames.REMOTE, "setRemoteClassName");        
        table.put(EjbTagNames.LOCAL_HOME, "setLocalHomeClassName");
        table.put(EjbTagNames.LOCAL, "setLocalClassName");        
        table.put(EjbTagNames.BUSINESS_LOCAL, "addLocalBusinessClassName");
        table.put(EjbTagNames.BUSINESS_REMOTE, "addRemoteBusinessClassName");
        table.put(EjbTagNames.SERVICE_ENDPOINT_INTERFACE,
                  "setWebServiceEndpointInterfaceName");
        return table;
    }    
    
    /**
     * write the common descriptor info to a DOM tree and return it
     *
     * @param parent node for the DOM tree
     * @param the descriptor to write
     */    
    protected void writeCommonHeaderEjbDescriptor(Node ejbNode, EjbDescriptor descriptor) {    
        super.writeCommonHeaderEjbDescriptor(ejbNode, descriptor);
        appendTextChild(ejbNode, EjbTagNames.HOME, descriptor.getHomeClassName());               
        appendTextChild(ejbNode, EjbTagNames.REMOTE, descriptor.getRemoteClassName());               
        appendTextChild(ejbNode, EjbTagNames.LOCAL_HOME, descriptor.getLocalHomeClassName());               
        appendTextChild(ejbNode, EjbTagNames.LOCAL, descriptor.getLocalClassName());                   

        for(String next : descriptor.getLocalBusinessClassNames()) {
            appendTextChild(ejbNode, EjbTagNames.BUSINESS_LOCAL, next);
        }

        for(String next : descriptor.getRemoteBusinessClassNames()) {
            appendTextChild(ejbNode, EjbTagNames.BUSINESS_REMOTE, next);
        }

        if( descriptor.isLocalBean()) {
            appendChild(ejbNode, EjbTagNames.LOCAL_BEAN);
        }

        appendTextChild(ejbNode, EjbTagNames.SERVICE_ENDPOINT_INTERFACE,
                        descriptor.getWebServiceEndpointInterfaceName());
        appendTextChild(ejbNode, EjbTagNames.EJB_CLASS, 
                        descriptor.getEjbClassName());              
    }
}
