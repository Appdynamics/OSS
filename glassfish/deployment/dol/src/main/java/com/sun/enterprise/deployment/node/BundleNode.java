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

package com.sun.enterprise.deployment.node;

import com.sun.enterprise.deployment.*;
import com.sun.enterprise.deployment.xml.TagNames;
import com.sun.enterprise.deployment.xml.ApplicationTagNames;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * This class defines all the common behaviour among nodes responsibles for 
 * handling bundles
 *
 * @author Jerome Dochez
 */
public abstract class BundleNode<T extends RootDeploymentDescriptor>
        extends DisplayableComponentNode<T> implements RootXMLNode<T> {
    
    public final static String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
    public final static String W3C_XML_SCHEMA_INSTANCE = "http://www.w3.org/2001/XMLSchema-instance";
        
    protected final static String SCHEMA_LOCATION_TAG = "xsi:schemaLocation";
    
    protected String docType;
    private Map namespaceContext=null;
    
    /**
     * set the DOCTYPE as read in the input XML File
     * @param docType for the xml
     */
    public void setDocType(String docType) {
        this.docType = docType;
        setSpecVersion();
    }
    
    public static Element appendChildNS(Node parent, String elementName, 
        String nameSpace) {
        Element child = getOwnerDocument(parent).createElementNS(nameSpace, elementName);
        parent.appendChild(child);
        return child;
    }

    /**
     * all sub-implementation of this class can use a dispatch table to map xml element to
     * method name on the descriptor class for setting the element value. 
     *  
     * @return the map with the element name as a key, the setter method as a value
     */    
    protected Map getDispatchTable() {
        Map dispatchTable = super.getDispatchTable();
        dispatchTable.put(TagNames.NAME, "setDisplayName");        
        dispatchTable.put(TagNames.VERSION, "setSpecVersion");
        return dispatchTable;
    }
    
    /**
     * receives notiification of the value for a particular tag
     * 
     * @param element the xml element
     * @param value it's associated value
     */
    public void setElementValue(XMLElement element, String value) {    
        if (SCHEMA_LOCATION_TAG.equals(element.getCompleteName())) {    
            // we need to keep all the non j2ee/javaee schemaLocation tags
            StringTokenizer st = new StringTokenizer(value);
            StringBuffer sb = new StringBuffer();
            while (st.hasMoreElements()) {
                String namespace = (String) st.nextElement();
		String schema;
		if (st.hasMoreElements()) {
		    schema = (String) st.nextElement();
		} else {
		    schema = namespace;
		    namespace = TagNames.JAVAEE_NAMESPACE;
		}
                if (namespace.equals(TagNames.J2EE_NAMESPACE)) 
                    continue;
                if (namespace.equals(TagNames.JAVAEE_NAMESPACE)) 
                    continue;
                if (namespace.equals(W3C_XML_SCHEMA)) 
                    continue;
                sb.append(namespace);
                sb.append(" ");
                sb.append(schema);
            }
            String clientSchemaLocation = sb.toString();
            if (clientSchemaLocation!=null && clientSchemaLocation.length()!=0) {
                Object o = getDescriptor();
                if (o instanceof RootDeploymentDescriptor) {
                    ((RootDeploymentDescriptor) o).setSchemaLocation(clientSchemaLocation);
                }
            }
        } else if (element.getQName().equals(TagNames.METADATA_COMPLETE)) {
            Object o = getDescriptor();
            if (o instanceof BundleDescriptor) {
                ((BundleDescriptor) o).setFullAttribute(value);
            }
        } else {
            super.setElementValue(element, value);
        }
    }
    
    /**
     * write the descriptor class to a DOM tree and return it
     *
     * @param parent node for the DOM tree
     * @param descriptor the descriptor to write
     * @return the DOM tree top node
     */    
    public Node writeDescriptor(Node parent, T descriptor) {
        Node bundleNode;
        if (getDocType()==null) {
            // we are using schemas for this DDs
 
            // connector schema still use j2ee name space
            if (descriptor instanceof ConnectorDescriptor) {
                bundleNode = appendChildNS(parent, getXMLRootTag().getQName(),
                    TagNames.J2EE_NAMESPACE);    
            } else {
                bundleNode = appendChildNS(parent, getXMLRootTag().getQName(),
                    TagNames.JAVAEE_NAMESPACE);    
            }
            addBundleNodeAttributes((Element) bundleNode, descriptor);
        } else {              
            bundleNode = appendChild(parent, getXMLRootTag().getQName());
        }
        if (descriptor instanceof Application) {
            Application application = (Application)descriptor;
            // in case of application, write application-name
            appendTextChild(bundleNode, ApplicationTagNames.APPLICATION_NAME, application.getAppName());
        } else {
            appendTextChild(bundleNode, TagNames.MODULE_NAME, descriptor.getModuleDescriptor().getModuleName());
        }

        // description, display-name, icons...
        writeDisplayableComponentInfo(bundleNode, descriptor);
       
        
        return bundleNode;
    }

    protected void writeMessageDestinations(Node parentNode,
                                            Iterator msgDestinations) {
        if ( (msgDestinations == null) || !msgDestinations.hasNext() ) 
            return;
        
        MessageDestinationNode subNode = new MessageDestinationNode();
        for (;msgDestinations.hasNext();) {
            MessageDestinationDescriptor next = 
                (MessageDestinationDescriptor) msgDestinations.next();
            subNode.writeDescriptor(parentNode, 
                                    TagNames.MESSAGE_DESTINATION, next);
        }     
    }
                                            
    /**
     * write the necessary attributes for the root node of this DDs document
     */
    protected void addBundleNodeAttributes(Element bundleNode, RootDeploymentDescriptor descriptor) {
        String schemaLocation;
        // the latest connector schema still use j2ee namespace
        if (descriptor instanceof ConnectorDescriptor) {
            bundleNode.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns", TagNames.J2EE_NAMESPACE);    
            schemaLocation = TagNames.J2EE_NAMESPACE + " " + 
                getSchemaURL();
        } else {
            bundleNode.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns", TagNames.JAVAEE_NAMESPACE);    
            schemaLocation = TagNames.JAVAEE_NAMESPACE + " " + 
                getSchemaURL();
        }
        bundleNode.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xsi", W3C_XML_SCHEMA_INSTANCE);    

        // add all custom global namespaces
        addNamespaceDeclaration(bundleNode, descriptor);
        String clientSchemaLocation = descriptor.getSchemaLocation();
        if (clientSchemaLocation!=null) {
            schemaLocation = schemaLocation + " " + clientSchemaLocation;
        }
        bundleNode.setAttributeNS(W3C_XML_SCHEMA_INSTANCE, SCHEMA_LOCATION_TAG, schemaLocation);
        bundleNode.setAttribute(TagNames.VERSION, getSpecVersion());        

        // Write out full attribute for DD which allows annotations.
        // The full attribute should always be written out as true since 
        // when we come here to write it out, the annotation information
        // has already been processed and saved in DD, so written out DD
        // is always a full DD.
        if (descriptor instanceof BundleDescriptor && 
            !(descriptor instanceof Application) ) {
            BundleDescriptor bundleDesc = (BundleDescriptor)descriptor;
            // In the common case that metadata-complete isn't already set to 
            // true, set it to true.
            // The exception is if the module contains managed beans, which 
            // can only be processed as annotations in the appclient client 
            // container runtime.
            if (! bundleDesc.isDDWithNoAnnotationAllowed() && 
                ! (bundleDesc.getManagedBeans().size() > 0) ) {
                bundleNode.setAttribute(TagNames.METADATA_COMPLETE, "true"); 
            }
        }
    }
    
    /**
     * notify of a new prefix mapping used in this document
     */
    public void addPrefixMapping(String prefix, String uri) {
        // we don't care about the default ones...
        if (uri.equals(TagNames.J2EE_NAMESPACE)) 
            return;
        if (uri.equals(TagNames.JAVAEE_NAMESPACE)) 
            return;
        if (uri.equals(W3C_XML_SCHEMA_INSTANCE)) 
            return;
        super.addPrefixMapping(prefix, uri);
    }
    
    /**
     * @return the complete URL for JAVAEE schemas
     */
    protected String getSchemaURL() {
       // by default, it comes from our web site
       return TagNames.JAVAEE_NAMESPACE + "/" + getSystemID();
    }
       
    /**
     * Sets the specVersion for this descriptor depending on the docType
     */
    protected void setSpecVersion() {
        if (docType==null)
            return;
        StringTokenizer st = new StringTokenizer(docType, "//");        
        while (st.hasMoreElements()) {
            String tmp = st.nextToken();
            if (tmp.startsWith("DTD")) {
                // this is the string we are interested in
                StringTokenizer versionST = new StringTokenizer(tmp);
                while (versionST.hasMoreElements()) {
                    String versionStr = versionST.nextToken();
                    try {
                        Float.valueOf(versionStr);
                        RootDeploymentDescriptor rdd = (RootDeploymentDescriptor) getDescriptor();
                        rdd.setSpecVersion(versionStr);
                        return;
                    } catch(NumberFormatException nfe) {
                        // ignore, this is just the other info of the publicID
                    }
                }
            }
        }            
    }
}
