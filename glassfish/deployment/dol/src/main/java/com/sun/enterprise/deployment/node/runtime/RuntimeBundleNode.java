/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.enterprise.deployment.node.runtime;

import com.sun.enterprise.deployment.BundleDescriptor;
import com.sun.enterprise.deployment.MessageDestinationDescriptor;
import com.sun.enterprise.deployment.RootDeploymentDescriptor;
import com.sun.enterprise.deployment.node.DeploymentDescriptorNode;
import com.sun.enterprise.deployment.node.RootXMLNode;
import com.sun.enterprise.deployment.xml.RuntimeTagNames;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.util.Iterator;

public abstract class RuntimeBundleNode<T extends RootDeploymentDescriptor>
        extends DeploymentDescriptorNode<T> implements RootXMLNode<T> {

    protected T descriptor=null;
    
    public RuntimeBundleNode(T descriptor) {
        this.descriptor = descriptor;
	Init();
    }   

    public RuntimeBundleNode() {
        this(null);
    } 
    
    /**
     * Initializes the child handler;
     */
    protected void Init() {
	// we do not care about standard DDs common tags
	handlers=null;
    }
    
    /**
     * Adds  a new DOL descriptor instance to the descriptor instance associated with 
     * this XMLNode
     *
     * @param descriptor the new descriptor
     */
    public void addDescriptor(Object descriptor) {    
        return;
    }
    
   /**
    * @return the descriptor instance to associate with this XMLNode
    */    
    public T getDescriptor() {
        return descriptor;
    } 
    
    /**
     * @return the default spec version level this node complies to
     */
    public String getSpecVersion() {
        return "1.4";
    }
    
    /**
     * set the DOCTYPE as read in the input XML File
     * @param DOCTYPE
     */
    public void setDocType(String docType) {
        // I do not care about the version of the runtime descriptors
    }
    
    /**
     * Sets the specVersion for this descriptor depending on the docType
     */
    protected void setSpecVersion() {
        // I do not care about the version of the runtime descriptors
    }  

    /**
     * writes the message destination references runtime information
     */
    protected void writeMessageDestinationInfo(Node parent, 
                                               BundleDescriptor descriptor) {
        for(Iterator iter = descriptor.getMessageDestinations().iterator();
            iter.hasNext();) {
            MessageDestinationRuntimeNode node = 
                new MessageDestinationRuntimeNode();
            node.writeDescriptor(parent, RuntimeTagNames.MESSAGE_DESTINATION,
                                 (MessageDestinationDescriptor) iter.next());
        }
    }    
    
    /** 
     * @return true if the runtime bundle node should only process
     * the product FCS DTD declarations
     */
    protected static final boolean restrictDTDDeclarations() {
        if (restrictDTDDeclarations==null) {
            restrictDTDDeclarations = Boolean.valueOf(Boolean.getBoolean("com.sun.aas.deployment.restrictdtddeclarations"));
        }
        return restrictDTDDeclarations.booleanValue();
    }
    
    public static Element appendChildNS(Node parent, String elementName,
        String nameSpace) {
        Element child = getOwnerDocument(parent).createElementNS(nameSpace, elementName);
        parent.appendChild(child);
        return child;
    }

    private static Boolean restrictDTDDeclarations=null;
}
