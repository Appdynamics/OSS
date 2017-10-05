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

package com.sun.enterprise.deployment.node.runtime;

import com.sun.enterprise.deployment.Application;
import com.sun.enterprise.deployment.EjbBundleDescriptor;
import com.sun.enterprise.deployment.Role;
import com.sun.enterprise.deployment.interfaces.SecurityRoleMapper;
import com.sun.enterprise.deployment.node.XMLElement;
import com.sun.enterprise.deployment.node.runtime.common.SecurityRoleMappingNode;
import com.sun.enterprise.deployment.runtime.common.PrincipalNameDescriptor;
import com.sun.enterprise.deployment.runtime.common.SecurityRoleMapping;
import com.sun.enterprise.deployment.xml.DTDRegistry;
import com.sun.enterprise.deployment.xml.RuntimeTagNames;
import org.glassfish.security.common.Group;
import org.w3c.dom.Node;

import java.util.List;
import java.util.Map;

/**
 * This node handles runtime deployment descriptors for ejb bundle
 * 
 * @author  Jerome Dochez
 * @version 
 */
public class EjbBundleRuntimeNode extends 
        RuntimeBundleNode<EjbBundleDescriptor> {
    
    EjbBundleDescriptor descriptor=null;    
    
    /** Creates new EjbBundleRuntimeNode */
    public EjbBundleRuntimeNode(EjbBundleDescriptor descriptor) {
        super(descriptor);
        this.descriptor = descriptor;      
        registerElementHandler(new XMLElement(RuntimeTagNames.SECURITY_ROLE_MAPPING), 
                               SecurityRoleMappingNode.class);
        registerElementHandler(new XMLElement(RuntimeTagNames.EJBS), 
                               EntrepriseBeansRuntimeNode.class);   
    }
    
    /** Creates new EjbBundleRuntimeNode */
    public EjbBundleRuntimeNode() {
        super(null);    
    }    
    
    /** 
     * @return the DOCTYPE that should be written to the XML file
     */
    public String getDocType() {
	return DTDRegistry.SUN_EJBJAR_310_DTD_PUBLIC_ID;
    }
    
    /**
     * @return the SystemID of the XML file
     */
    public String getSystemID() {
	return DTDRegistry.SUN_EJBJAR_310_DTD_SYSTEM_ID;
    }

    /**
     * @return NULL for all runtime nodes.
     */
    public List<String> getSystemIDs() {
        return null;
    }
    
    /**
     * @return the XML tag associated with this XMLNode
     */    
    protected XMLElement getXMLRootTag() {
        return new XMLElement(RuntimeTagNames.S1AS_EJB_RUNTIME_TAG);
    }   
    
   /**
    * register this node as a root node capable of loading entire DD files
    * 
    * @param publicIDToDTD is a mapping between xml Public-ID to DTD 
    * @return the doctype tag name
    */
   public static String registerBundle(Map publicIDToDTD) {    
       publicIDToDTD.put(DTDRegistry.SUN_EJBJAR_200_DTD_PUBLIC_ID, DTDRegistry.SUN_EJBJAR_200_DTD_SYSTEM_ID);
       publicIDToDTD.put(DTDRegistry.SUN_EJBJAR_201_DTD_PUBLIC_ID, DTDRegistry.SUN_EJBJAR_201_DTD_SYSTEM_ID);
       publicIDToDTD.put(DTDRegistry.SUN_EJBJAR_210_DTD_PUBLIC_ID, DTDRegistry.SUN_EJBJAR_210_DTD_SYSTEM_ID);
       publicIDToDTD.put(DTDRegistry.SUN_EJBJAR_211_DTD_PUBLIC_ID, DTDRegistry.SUN_EJBJAR_211_DTD_SYSTEM_ID);
       publicIDToDTD.put(DTDRegistry.SUN_EJBJAR_300_DTD_PUBLIC_ID, DTDRegistry.SUN_EJBJAR_300_DTD_SYSTEM_ID);
       publicIDToDTD.put(DTDRegistry.SUN_EJBJAR_310_DTD_PUBLIC_ID, DTDRegistry.SUN_EJBJAR_310_DTD_SYSTEM_ID);

       if (!restrictDTDDeclarations()) {
           publicIDToDTD.put(DTDRegistry.SUN_EJBJAR_210beta_DTD_PUBLIC_ID, DTDRegistry.SUN_EJBJAR_210beta_DTD_SYSTEM_ID);
       }           
       return RuntimeTagNames.S1AS_EJB_RUNTIME_TAG;
   }    
    
   /**
    * @return the descriptor instance to associate with this XMLNode
    */    
    public EjbBundleDescriptor getDescriptor() {
        return descriptor;
    }                

    /**
     * Adds  a new DOL descriptor instance to the descriptor instance associated with 
     * this XMLNode
     *
     * @param descriptor the new descriptor
     */
    public void addDescriptor(Object newDescriptor) {
        if (newDescriptor instanceof SecurityRoleMapping) {
            SecurityRoleMapping roleMap = (SecurityRoleMapping)newDescriptor;
            descriptor.addSecurityRoleMapping(roleMap);
            Application app = descriptor.getApplication();
            if (app!=null) {
                Role role = new Role(roleMap.getRoleName());
                SecurityRoleMapper rm = app.getRoleMapper();
                if (rm != null) {
                    List<PrincipalNameDescriptor> principals = roleMap.getPrincipalNames();
                    for (int i = 0; i < principals.size(); i++) {
                        rm.assignRole(principals.get(i).getPrincipal(),
                            role, descriptor);
                    }
                    List<String> groups = roleMap.getGroupNames();
                    for (int i = 0; i < groups.size(); i++) {
                        rm.assignRole(new Group(groups.get(i)),
                            role, descriptor);
                    }
                }
            }
        }
    }

    /**
     * write the descriptor class to a DOM tree and return it
     *
     * @param parent node for the DOM tree
     * @param the descriptor to write
     * @return the DOM tree top node
     */    
    public Node writeDescriptor(Node parent, EjbBundleDescriptor bundleDescriptor) {    
        Node ejbs = super.writeDescriptor(parent, bundleDescriptor);

        // security-role-mapping*
        List<SecurityRoleMapping> roleMappings = bundleDescriptor.getSecurityRoleMappings();
        for (int i = 0; i < roleMappings.size(); i++) { 
            SecurityRoleMappingNode srmn = new SecurityRoleMappingNode();
            srmn.writeDescriptor(ejbs, RuntimeTagNames.SECURITY_ROLE_MAPPING, roleMappings.get(i));
        }
	
	    // entreprise-beans
        EntrepriseBeansRuntimeNode ejbsNode = new EntrepriseBeansRuntimeNode();
        ejbsNode.writeDescriptor(ejbs, RuntimeTagNames.EJBS, bundleDescriptor);
        return ejbs;
    }
}
