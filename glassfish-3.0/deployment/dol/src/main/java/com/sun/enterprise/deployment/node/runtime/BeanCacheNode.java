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

import com.sun.enterprise.deployment.node.DeploymentDescriptorNode;
import com.sun.enterprise.deployment.node.XMLElement;
import com.sun.enterprise.deployment.runtime.BeanCacheDescriptor;
import com.sun.enterprise.deployment.xml.RuntimeTagNames;
import org.w3c.dom.Node;

import java.util.Map;

/**
 * This node handles the bean-cache untime deployment descriptors 
 *
 * @author  Jerome Dochez
 * @version 
 */
public class BeanCacheNode extends DeploymentDescriptorNode {

    protected BeanCacheDescriptor descriptor=null;
    
   /**
    * @return the descriptor instance to associate with this XMLNode
    */    
    public Object getDescriptor() {
        if (descriptor==null) {
	    descriptor = new BeanCacheDescriptor();
	}
        return descriptor;
    }
    
    /**
     * receives notification of the value for a particular tag
     * 
     * @param element the xml element
     * @param value it's associated value
     */
    public void setElementValue(XMLElement element, String value) {
	if (RuntimeTagNames.IS_CACHE_OVERFLOW_ALLOWED.equals(element.getQName())) {
	    descriptor.setIsCacheOverflowAllowed(Boolean.valueOf(value));
	} else 
        super.setElementValue(element, value);
    }
    
    /**
     * all sub-implementation of this class can use a dispatch table to map xml element to
     * method name on the descriptor class for setting the element value. 
     *  
     * @return the map with the element name as a key, the setter method as a value
     */    
    protected Map getDispatchTable() {  
	Map dispatchTable = super.getDispatchTable();
	dispatchTable.put(RuntimeTagNames.MAX_CACHE_SIZE, "setMaxCacheSize");
	dispatchTable.put(RuntimeTagNames.RESIZE_QUANTITY, "setResizeQuantity");
	dispatchTable.put(RuntimeTagNames.CACHE_IDLE_TIMEOUT_IN_SECONDS, "setCacheIdleTimeoutInSeconds");
	dispatchTable.put(RuntimeTagNames.REMOVAL_TIMEOUT_IN_SECONDS, "setRemovalTimeoutInSeconds");
	dispatchTable.put(RuntimeTagNames.VICTIM_SELECTION_POLICY, "setVictimSelectionPolicy");
	return dispatchTable;
    }
    
    /**
     * write the descriptor class to a DOM tree and return it
     *
     * @param parent node for the DOM tree
     * @param node name for the descriptor
     * @param the descriptor to write
     * @return the DOM tree top node
     */    
    public Node writeDescriptor(Node parent, String nodeName, BeanCacheDescriptor descriptor) {
	Node beanCacheNode = super.writeDescriptor(parent, nodeName, descriptor);
	appendTextChild(beanCacheNode, RuntimeTagNames.MAX_CACHE_SIZE, descriptor.getMaxCacheSize());	
	appendTextChild(beanCacheNode, RuntimeTagNames.RESIZE_QUANTITY, descriptor.getResizeQuantity());	
	appendTextChild(beanCacheNode, RuntimeTagNames.IS_CACHE_OVERFLOW_ALLOWED, String.valueOf(descriptor.isIsCacheOverflowAllowed()));
	appendTextChild(beanCacheNode, RuntimeTagNames.CACHE_IDLE_TIMEOUT_IN_SECONDS, descriptor.getCacheIdleTimeoutInSeconds());	
	appendTextChild(beanCacheNode, RuntimeTagNames.REMOVAL_TIMEOUT_IN_SECONDS, descriptor.getRemovalTimeoutInSeconds());	
	appendTextChild(beanCacheNode, RuntimeTagNames.VICTIM_SELECTION_POLICY, descriptor.getVictimSelectionPolicy());	
	return beanCacheNode;
    }
}
