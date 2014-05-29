/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010-2011 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.enterprise.deployment.node.runtime.application;

import com.sun.enterprise.deployment.Application;
import com.sun.enterprise.deployment.EnvironmentProperty;
import com.sun.enterprise.deployment.Role;
import com.sun.enterprise.deployment.interfaces.SecurityRoleMapper;
import com.sun.enterprise.deployment.runtime.ApplicationParameter;
import com.sun.enterprise.deployment.runtime.WLModuleDescriptor;
import com.sun.enterprise.deployment.node.runtime.RuntimeBundleNode;
import com.sun.enterprise.deployment.node.XMLElement;
import com.sun.enterprise.deployment.node.runtime.common.WLSecurityRoleAssignmentNode;
import com.sun.enterprise.deployment.node.web.InitParamNode;
import com.sun.enterprise.deployment.runtime.common.WLSecurityRoleAssignment;
import com.sun.enterprise.deployment.xml.TagNames;
import com.sun.enterprise.deployment.xml.RuntimeTagNames;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import org.glassfish.security.common.Group;
import org.glassfish.security.common.PrincipalImpl;


/**
 * This node is responsible for handling all WebLogic runtime information for 
 * application.
 */
public class WLApplicationRuntimeNode extends RuntimeBundleNode<Application> {

    public final static String SCHEMA_ID = "weblogic-application.xsd";

    private final static List<String> systemIDs = initSystemIDs();

    private static List<String> initSystemIDs() {
        List<String> systemIDs = new ArrayList<String>();
        systemIDs.add(SCHEMA_ID);
        return Collections.unmodifiableList(systemIDs);
    }


    /** Creates new WLApplicationRuntimeNode */
    public WLApplicationRuntimeNode(Application descriptor) {
        super(descriptor);
    }
    
    /** Creates new WebBundleRuntimeNode */
    public WLApplicationRuntimeNode() {
        super(null);    
    }
    
    /**
     * Initialize the child handlers
     */
    protected void Init() {
        super.Init();
        registerElementHandler(new XMLElement(
                RuntimeTagNames.APPLICATION_PARAM), InitParamNode.class);
        registerElementHandler(new XMLElement(
                RuntimeTagNames.MODULE), WLModuleNode.class);
        registerElementHandler(new XMLElement(RuntimeTagNames.WL_SECURITY_ROLE_ASSIGNMENT),
                WLSecurityRoleAssignmentNode.class);
    }

    /**
     * @return the XML tag associated with this XMLNode
     */
    protected XMLElement getXMLRootTag() {
        return new XMLElement(RuntimeTagNames.WL_APPLICATION_RUNTIME_TAG);
    }    
    
    /** 
     * @return the DOCTYPE that should be written to the XML file
     */
    public String getDocType() {
        return null;
    }
    
    /**
     * @return the SystemID of the XML file
     */
    public String getSystemID() {
        return SCHEMA_ID;
    }

    /**
     * @return the list of SystemID of the XML schema supported
     */
    public List<String> getSystemIDs() {
        return systemIDs;
    }
    
   /**
    * @return the application instance to associate with this XMLNode
    */    
    public Application getDescriptor() {    
	return descriptor;               
    }
    
    /**
     * Adds  a new DOL descriptor instance to the descriptor instance 
     * associated with this XMLNode
     *
     * @param descriptor the new descriptor
     */
    public void addDescriptor(Object newDescriptor) {
        if (newDescriptor instanceof EnvironmentProperty) {
            descriptor.addApplicationParam((ApplicationParameter)newDescriptor);
        } else if (newDescriptor instanceof WLModuleDescriptor) {
            descriptor.addWLModule((WLModuleDescriptor)newDescriptor);
        } else if (newDescriptor instanceof WLSecurityRoleAssignment) {
            WLSecurityRoleAssignment roleMap = (WLSecurityRoleAssignment) newDescriptor;
            if (descriptor!=null && !descriptor.isVirtual()) {
                descriptor.addWLRoleAssignments(roleMap);
                Role role = new Role(roleMap.getRoleName());
                SecurityRoleMapper rm = descriptor.getRoleMapper();
                if (rm != null) {
                    if(roleMap.isExternallyDefined()){
                        rm.assignRole(new Group(roleMap.getRoleName()),
                                role, descriptor);
                    } else {
                        List<String> principals = roleMap.getPrincipalNames();
                        for (int i = 0; i < principals.size(); i++) {
                            rm.assignRole(new PrincipalImpl(principals.get(i)),
                                    role, descriptor);
                        }
                    }
                }
            }
        } else super.addDescriptor(newDescriptor);
    }

    /**
     * write the descriptor class to a DOM tree and return it
     *
     * @param parent node for the DOM tree
     * @param nodeName the node name
     * @param descriptor the descriptor to write
     * @return the DOM tree top node
     */
    public Node writeDescriptor(Node parent, String nodeName, Application application) {
        Element root = appendChildNS(parent, getXMLRootTag().getQName(),
                    TagNames.WL_APPLICATION_NAMESPACE);

        // application-param*
        Set<ApplicationParameter> applicationParams = 
            application.getApplicationParams(); 
        if (!applicationParams.isEmpty()) {
            InitParamNode initParamNode = new InitParamNode();
            for (ApplicationParameter appParam : applicationParams) {
                initParamNode.writeDescriptor(root, 
                        RuntimeTagNames.APPLICATION_PARAM,
                        (EnvironmentProperty)appParam);
            }
        }

        // module*
        WLModuleNode moduleNode = new WLModuleNode();
        for (WLModuleDescriptor md :  application.getWLModules()) {
            moduleNode.writeDescriptor(root, RuntimeTagNames.MODULE, md);
        }

        List<WLSecurityRoleAssignment> wlRoleAssignments = application.getWlRoleAssignments();
        for (int i = 0; i < wlRoleAssignments.size(); i++) {
            WLSecurityRoleAssignmentNode sran = new WLSecurityRoleAssignmentNode();
            sran.writeDescriptor(root, RuntimeTagNames.WL_SECURITY_ROLE_ASSIGNMENT, wlRoleAssignments.get(i));
        }
        return root;
    }
}
