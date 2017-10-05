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

import com.sun.enterprise.deployment.*;
import com.sun.enterprise.deployment.node.DeploymentDescriptorNode;
import com.sun.enterprise.deployment.node.XMLElement;
import com.sun.enterprise.deployment.runtime.*;
import com.sun.enterprise.deployment.util.DOLUtils;
import com.sun.enterprise.deployment.xml.RuntimeTagNames;
import com.sun.enterprise.deployment.xml.WebServicesTagNames;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;

/**
 * This node handles all runtime information for ejbs
 *
 * @author  Jerome Dochez
 * @version 
 */
public class EjbNode extends DeploymentDescriptorNode {

    private EjbDescriptor descriptor;
    private String availEnabled;
    
    /** Creates new EjbNode */
    public EjbNode() {
        super();
        registerElementHandler(new XMLElement(RuntimeTagNames.RESOURCE_REFERENCE), 
                               ResourceRefNode.class);     
        registerElementHandler(new XMLElement(RuntimeTagNames.EJB_REFERENCE), 
                               EjbRefNode.class);             
        registerElementHandler(new XMLElement(RuntimeTagNames.RESOURCE_ENV_REFERENCE), 
                               ResourceEnvRefNode.class);      
        registerElementHandler(new XMLElement(RuntimeTagNames.MESSAGE_DESTINATION_REFERENCE), 
                               MessageDestinationRefNode.class);      
        registerElementHandler(new XMLElement(WebServicesTagNames.SERVICE_REF),
                               ServiceRefNode.class);
        registerElementHandler(new XMLElement(RuntimeTagNames.CMP), 
                               CmpNode.class);
	registerElementHandler(new XMLElement(RuntimeTagNames.MDB_CONNECTION_FACTORY), 
				MDBConnectionFactoryNode.class);         
        registerElementHandler(new XMLElement(RuntimeTagNames.IOR_CONFIG), 
                               IORConfigurationNode.class,"addIORConfigurationDescriptor");      
        registerElementHandler(new XMLElement(RuntimeTagNames.BEAN_POOL), 
                               BeanPoolNode.class);
        registerElementHandler(new XMLElement(RuntimeTagNames.BEAN_CACHE), 
                               BeanCacheNode.class);   	   			       
        registerElementHandler(new XMLElement(RuntimeTagNames.MDB_RESOURCE_ADAPTER),
                		MDBResourceAdapterNode.class);
        registerElementHandler(new XMLElement(WebServicesTagNames.WEB_SERVICE_ENDPOINT), 
                               WebServiceEndpointRuntimeNode.class);
        registerElementHandler(new XMLElement(RuntimeTagNames.FLUSH_AT_END_OF_METHOD), FlushAtEndOfMethodNode.class);   	   			       
        registerElementHandler(new XMLElement(RuntimeTagNames.CHECKPOINT_AT_END_OF_METHOD), CheckpointAtEndOfMethodNode.class);   	   			       
    }
    
   /**
    * @return the descriptor instance to associate with this XMLNode
    */    
    public Object getDescriptor() {
        return descriptor;
    }
    
    /**
     * receives notification of the value for a particular tag
     * 
     * @param element the xml element
     * @param value it's associated value
     */
    public void setElementValue(XMLElement element, String value) {        
        if (RuntimeTagNames.EJB_NAME.equals(element.getQName())) {
            Object parentDesc = getParentNode().getDescriptor();
            if (parentDesc==null) {
		// In J2EE 1.2.x, all ejbs in app were clubbed under
		// one <enterprise-beans> element.
		// So we get the ejbDescriptor from the application
                Object application = getParentNode().getParentNode().getDescriptor();
                if (application!=null && application instanceof Application) {
                    descriptor = ((Application) application).getEjbByName(value);
                } 
            } else {
                if (parentDesc instanceof EjbBundleDescriptor) {
                    descriptor = ((EjbBundleDescriptor) parentDesc).getEjbByName(value);
                }
            }
            if (descriptor==null) {
                DOLUtils.getDefaultLogger().log(Level.SEVERE, "enterprise.deployment.backend.addDescriptorFailure",
                            new Object[]{element , value}); 
            } else {
                if (availEnabled != null) {
                    descriptor.getIASEjbExtraDescriptors().setAttributeValue(descriptor.getIASEjbExtraDescriptors().AVAILABILITY_ENABLED, availEnabled);
                }
            }
            return;
        } else if (descriptor==null && ! RuntimeTagNames.AVAILABILITY_ENABLED.equals(element.getQName())) {
            DOLUtils.getDefaultLogger().log(Level.SEVERE, "enterprise.deployment.backend.addDescriptorFailure",
                            new Object[] {element.getQName() , value });
        }
        // if this is the availability-enabled attribute, save the value
        // and set it later
	if (RuntimeTagNames.AVAILABILITY_ENABLED.equals(element.getQName())) {
            availEnabled = value;
        } else if (RuntimeTagNames.NAME.equals(element.getQName())) {
	    // principal
            if (Boolean.FALSE.equals(descriptor.getUsesCallerIdentity())
                && descriptor.getRunAsIdentity() != null) {
                    descriptor.getRunAsIdentity().setPrincipal(value);
            }
        } else if (RuntimeTagNames.PASS_BY_REFERENCE.equals(element.getQName())) {
	    descriptor.getIASEjbExtraDescriptors().setPassByReference(Boolean.valueOf(value));
	} else if (RuntimeTagNames.JMS_MAX_MESSAGES_LOAD.equals(element.getQName())) {
	    descriptor.getIASEjbExtraDescriptors().setJmsMaxMessagesLoad((Integer.valueOf(value)).intValue());
	} else if (RuntimeTagNames.IS_READ_ONLY_BEAN.equals(element.getQName())) {
	    descriptor.getIASEjbExtraDescriptors().setIsReadOnlyBean((Boolean.valueOf(value)).booleanValue());
	} else if (RuntimeTagNames.REFRESH_PERIOD_IN_SECONDS.equals(element.getQName())) {
	    descriptor.getIASEjbExtraDescriptors().setRefreshPeriodInSeconds((Integer.valueOf(value)).intValue());
	} else if (RuntimeTagNames.COMMIT_OPTION.equals(element.getQName())) {
	    descriptor.getIASEjbExtraDescriptors().setCommitOption(value);
	} else if (RuntimeTagNames.CMT_TIMEOUT_IN_SECONDS.equals(element.getQName())) {
	    descriptor.getIASEjbExtraDescriptors().setCmtTimeoutInSeconds((Integer.valueOf(value)).intValue());
	} else if (RuntimeTagNames.USE_THREAD_POOL_ID.equals(element.getQName())) {
	    descriptor.getIASEjbExtraDescriptors().setUseThreadPoolId(value);
	} else if (RuntimeTagNames.CHECKPOINTED_METHODS.equals(
            element.getQName())) {
            descriptor.getIASEjbExtraDescriptors().setCheckpointedMethods(
                value);
        }
        else super.setElementValue(element, value);
    }
    
    /**
     * all sub-implementation of this class can use a dispatch table to map xml element to
     * method name on the descriptor class for setting the element value. 
     *  
     * @return the map with the element name as a key, the setter method as a value
     */    
    protected Map getDispatchTable() {    
        Map table = super.getDispatchTable();
        
        table.put(RuntimeTagNames.JNDI_NAME, "setJndiName");
        
        // gen-classes
        table.put(RuntimeTagNames.REMOTE_IMPL, "setEJBObjectImplClassName");
        table.put(RuntimeTagNames.LOCAL_IMPL, "setEJBLocalObjectImplClassName");
        table.put(RuntimeTagNames.REMOTE_HOME_IMPL, "setRemoteHomeImplClassName");
        table.put(RuntimeTagNames.LOCAL_HOME_IMPL, "setLocalHomeImplClassName");        
        
        // for mdbs...
        table.put(RuntimeTagNames.DURABLE_SUBSCRIPTION, "setDurableSubscriptionName");        
        table.put(RuntimeTagNames.MDB_CONNECTION_FACTORY, "setConnectionFactoryName");                
        table.put(RuntimeTagNames.RESOURCE_ADAPTER_MID, "setResourceAdapterMid");                
        return table;
    }
    
    /**
     * Adds  a new DOL descriptor instance to the descriptor instance associated with 
     * this XMLNode
     *
     * @param descriptor the new descriptor
     */
    public void addDescriptor(Object newDescriptor) {
	if (newDescriptor instanceof MdbConnectionFactoryDescriptor) {
	    descriptor.getIASEjbExtraDescriptors().setMdbConnectionFactory(
	    	(MdbConnectionFactoryDescriptor) newDescriptor);
	} else 
	if (newDescriptor instanceof BeanPoolDescriptor) {
	    descriptor.getIASEjbExtraDescriptors().setBeanPool(
		(BeanPoolDescriptor) newDescriptor);
	} else
	if (newDescriptor instanceof BeanCacheDescriptor) {
	    descriptor.getIASEjbExtraDescriptors().setBeanCache(
		(BeanCacheDescriptor) newDescriptor);
	} else
	if (newDescriptor instanceof FlushAtEndOfMethodDescriptor) {
	    descriptor.getIASEjbExtraDescriptors().setFlushAtEndOfMethodDescriptor((FlushAtEndOfMethodDescriptor)newDescriptor);
	} else
	if (newDescriptor instanceof CheckpointAtEndOfMethodDescriptor) {
	    descriptor.getIASEjbExtraDescriptors().setCheckpointAtEndOfMethodDescriptor((CheckpointAtEndOfMethodDescriptor)newDescriptor);
	}
    }
    
    /**
     * write the descriptor class to a DOM tree and return it
     *
     * @param parent node for the DOM tree
     * @param node name for the descriptor
     * @param the descriptor to write
     * @return the DOM tree top node
     */    
    public Node writeDescriptor(Node parent, String nodeName, EjbDescriptor ejbDescriptor) {    
        Element ejbNode = (Element)super.writeDescriptor(parent, nodeName, ejbDescriptor);
        appendTextChild(ejbNode, RuntimeTagNames.EJB_NAME, ejbDescriptor.getName());
        appendTextChild(ejbNode, RuntimeTagNames.JNDI_NAME, ejbDescriptor.getJndiName());
	
        RuntimeDescriptorNode.writeCommonComponentInfo(ejbNode, (Descriptor) ejbDescriptor);

	appendTextChild(ejbNode, RuntimeTagNames.PASS_BY_REFERENCE, 
		String.valueOf(ejbDescriptor.getIASEjbExtraDescriptors().getPassByReference()));

	if (ejbDescriptor instanceof IASEjbCMPEntityDescriptor) {
	    CmpNode cmpNode = new CmpNode();
	    cmpNode.writeDescriptor(ejbNode, RuntimeTagNames.CMP, (IASEjbCMPEntityDescriptor) ejbDescriptor);
	}
	
        // principal
        if ( Boolean.FALSE.equals(ejbDescriptor.getUsesCallerIdentity()) ) {
            RunAsIdentityDescriptor raid = ejbDescriptor.getRunAsIdentity();
            if ( raid != null && raid.getPrincipal() != null ) {
                Node principalNode = appendChild(ejbNode, RuntimeTagNames.PRINCIPAL);
                appendTextChild(principalNode, RuntimeTagNames.NAME,raid.getPrincipal());
            }
        }
	
	if (ejbDescriptor instanceof EjbMessageBeanDescriptor) {
	    EjbMessageBeanDescriptor msgBeanDesc = (EjbMessageBeanDescriptor) ejbDescriptor;
            
            // mdb-connection-factory?
            if (ejbDescriptor.getIASEjbExtraDescriptors().getMdbConnectionFactory()!=null) {
                MDBConnectionFactoryNode mcfNode = new MDBConnectionFactoryNode();
                mcfNode.writeDescriptor(ejbNode, RuntimeTagNames.MDB_CONNECTION_FACTORY, 
                    ejbDescriptor.getIASEjbExtraDescriptors().getMdbConnectionFactory());
            }
            
            // jms-durable-subscription-name
	    if (msgBeanDesc.hasDurableSubscription()) {
		appendTextChild(ejbNode, RuntimeTagNames.DURABLE_SUBSCRIPTION, 
			msgBeanDesc.getDurableSubscriptionName());
	    }
	    appendTextChild(ejbNode, RuntimeTagNames.JMS_MAX_MESSAGES_LOAD, 
	    	String.valueOf(ejbDescriptor.getIASEjbExtraDescriptors().getJmsMaxMessagesLoad()));
	}
	
	// ior-configuration
        IORConfigurationNode iorNode = new IORConfigurationNode();
        for (Iterator iorIterator = ejbDescriptor.getIORConfigurationDescriptors().iterator();
            iorIterator.hasNext();) {
                iorNode.writeDescriptor(ejbNode,RuntimeTagNames.IOR_CONFIG, (EjbIORConfigurationDescriptor) iorIterator.next());
        }               
        
	appendTextChild(ejbNode, RuntimeTagNames.IS_READ_ONLY_BEAN, 
		String.valueOf(ejbDescriptor.getIASEjbExtraDescriptors().isIsReadOnlyBean()));
	appendTextChild(ejbNode, RuntimeTagNames.REFRESH_PERIOD_IN_SECONDS, 
		String.valueOf(ejbDescriptor.getIASEjbExtraDescriptors().getRefreshPeriodInSeconds()));
	appendTextChild(ejbNode, RuntimeTagNames.COMMIT_OPTION, 
		ejbDescriptor.getIASEjbExtraDescriptors().getCommitOption());
	appendTextChild(ejbNode, RuntimeTagNames.CMT_TIMEOUT_IN_SECONDS, 
		String.valueOf(ejbDescriptor.getIASEjbExtraDescriptors().getCmtTimeoutInSeconds()));
	appendTextChild(ejbNode, RuntimeTagNames.USE_THREAD_POOL_ID, 
		ejbDescriptor.getIASEjbExtraDescriptors().getUseThreadPoolId());
	
        // gen-classes
        writeGenClasses(ejbNode, ejbDescriptor);
        
	// bean-pool
	BeanPoolDescriptor beanPoolDesc = ejbDescriptor.getIASEjbExtraDescriptors().getBeanPool();
	if (beanPoolDesc!=null) {
	    BeanPoolNode bpNode = new BeanPoolNode();
	    bpNode.writeDescriptor(ejbNode, RuntimeTagNames.BEAN_POOL, beanPoolDesc);
	}
	
	// bean-cache
	BeanCacheDescriptor beanCacheDesc = ejbDescriptor.getIASEjbExtraDescriptors().getBeanCache();
	if (beanCacheDesc!=null) {
	    BeanCacheNode bcNode = new BeanCacheNode();
	    bcNode.writeDescriptor(ejbNode, RuntimeTagNames.BEAN_CACHE, beanCacheDesc);
	}
	
	if (ejbDescriptor instanceof EjbMessageBeanDescriptor) {
	    EjbMessageBeanDescriptor msgBeanDesc = (EjbMessageBeanDescriptor) ejbDescriptor;	    
            if (msgBeanDesc.hasResourceAdapterMid()) {
                MDBResourceAdapterNode mdb = new MDBResourceAdapterNode();
                mdb.writeDescriptor(ejbNode, RuntimeTagNames.MDB_RESOURCE_ADAPTER, msgBeanDesc);
            }
        } else if( ejbDescriptor instanceof EjbSessionDescriptor ) {
            
            // web-services
            WebServiceEndpointRuntimeNode wsRuntime = new WebServiceEndpointRuntimeNode();
            wsRuntime.writeWebServiceEndpointInfo(ejbNode, ejbDescriptor);
        }

	// flush-at-end-of-method
	FlushAtEndOfMethodDescriptor flushMethodDesc = ejbDescriptor.getIASEjbExtraDescriptors().getFlushAtEndOfMethodDescriptor();
	if (flushMethodDesc!=null) {
	    FlushAtEndOfMethodNode flushNode = new FlushAtEndOfMethodNode();
	    flushNode.writeDescriptor(ejbNode, RuntimeTagNames.FLUSH_AT_END_OF_METHOD, flushMethodDesc);
	}

        // checkpointed-methods
        ejbDescriptor.getIASEjbExtraDescriptors().parseCheckpointedMethods(ejbDescriptor);

        // checkpoint-at-end-of-method
        CheckpointAtEndOfMethodDescriptor checkpointMethodDesc = ejbDescriptor.getIASEjbExtraDescriptors().getCheckpointAtEndOfMethodDescriptor();
        if (checkpointMethodDesc!=null) {
            CheckpointAtEndOfMethodNode checkpointNode = 
                new CheckpointAtEndOfMethodNode();
            checkpointNode.writeDescriptor(ejbNode, RuntimeTagNames.CHECKPOINT_AT_END_OF_METHOD, checkpointMethodDesc);
        }
	
        // availability-enabled
        setAttribute(ejbNode, RuntimeTagNames.AVAILABILITY_ENABLED, (String) ejbDescriptor.getIASEjbExtraDescriptors().getAttributeValue(ejbDescriptor.getIASEjbExtraDescriptors().AVAILABILITY_ENABLED));

        return ejbNode;
    }

    /**
   * write all the classes info generated at deployment 
   * @param parent node for the information
   * @param descriptor the descriptor containing the generated info
   */
    private void writeGenClasses(Node parent, EjbDescriptor ejbDescriptor) {
        // common to all ejbs but mdb...
        Node genClasses = appendChild(parent, RuntimeTagNames.GEN_CLASSES);
        
        appendTextChild(genClasses, RuntimeTagNames.REMOTE_IMPL,
            ejbDescriptor.getEJBObjectImplClassName());
        
        appendTextChild(genClasses, RuntimeTagNames.LOCAL_IMPL,
            ejbDescriptor.getEJBLocalObjectImplClassName());

        appendTextChild(genClasses, RuntimeTagNames.REMOTE_HOME_IMPL,
            ejbDescriptor.getRemoteHomeImplClassName());
        
        appendTextChild(genClasses, RuntimeTagNames.LOCAL_HOME_IMPL,
            ejbDescriptor.getLocalHomeImplClassName());



    }    
}
