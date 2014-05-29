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

package com.sun.enterprise.deployment.node.ejb;

import com.sun.enterprise.deployment.*;
import com.sun.enterprise.deployment.node.*;
import com.sun.enterprise.deployment.util.DOLUtils;
import com.sun.enterprise.deployment.xml.EjbTagNames;
import com.sun.enterprise.deployment.xml.TagNames;
import org.w3c.dom.Node;

import java.util.*;

/**
 * This class handles ejb bundle xml files
 *
 * @author  Jerome Dochez
 * @version 
 */
public class EjbBundleNode extends BundleNode<EjbBundleDescriptor> {

    public final static XMLElement tag = new XMLElement(EjbTagNames.EJB_BUNDLE_TAG);
    public final static String PUBLIC_DTD_ID = "-//Sun Microsystems, Inc.//DTD Enterprise JavaBeans 2.0//EN";
    public final static String PUBLIC_DTD_ID_12 = "-//Sun Microsystems, Inc.//DTD Enterprise JavaBeans 1.1//EN";
    
    /** The system ID of an ejb-jar document.*/
    public final static String SYSTEM_ID = "http://java.sun.com/dtd/ejb-jar_2_0.dtd";    
    public final static String SYSTEM_ID_12 = "http://java.sun.com/dtd/ejb-jar_1_1.dtd";
    public final static String SCHEMA_ID_21 = "ejb-jar_2_1.xsd";
    public final static String SCHEMA_ID_30 = "ejb-jar_3_0.xsd";
    public final static String SCHEMA_ID = "ejb-jar_3_1.xsd";
    public final static String SPEC_VERSION = "3.1";
    private final static List<String> systemIDs = initSystemIDs();

   /**
    * register this node as a root node capable of loading entire DD files
    * 
    * @param publicIDToDTD is a mapping between xml Public-ID to DTD 
    * @return the doctype tag name
    */
   public static String registerBundle(Map publicIDToDTD) {
        publicIDToDTD.put(PUBLIC_DTD_ID, SYSTEM_ID);
        publicIDToDTD.put(PUBLIC_DTD_ID_12, SYSTEM_ID_12);
        return tag.getQName();
   }    
    
   private static List<String> initSystemIDs() {
        ArrayList<String> systemIDs = new ArrayList<String>();
        systemIDs.add(SCHEMA_ID);
        systemIDs.add(SCHEMA_ID_30);
        systemIDs.add(SCHEMA_ID_21);
        return Collections.unmodifiableList(systemIDs);
   }
   
    // Descriptor class we are using   
   private EjbBundleDescriptor descriptor;   
      
   public EjbBundleNode() {
       super();
       // register sub XMLNodes
       registerElementHandler(new XMLElement(EjbTagNames.SESSION),
                                                            EjbSessionNode.class);           
       registerElementHandler(new XMLElement(EjbTagNames.ENTITY), 
                                                            EjbEntityNode.class);                   
       registerElementHandler(new XMLElement(EjbTagNames.MESSAGE_DRIVEN), 
                                                            MessageDrivenBeanNode.class);          
       registerElementHandler(new XMLElement(EjbTagNames.METHOD_PERMISSION), 
                                                            MethodPermissionNode.class);                  
       registerElementHandler(new XMLElement(EjbTagNames.ROLE), 
                                                            SecurityRoleNode.class, "addRole");       
       registerElementHandler(new XMLElement(EjbTagNames.CONTAINER_TRANSACTION), 
                                                            ContainerTransactionNode.class);       
       registerElementHandler(new XMLElement(EjbTagNames.EXCLUDE_LIST), 
                                                            ExcludeListNode.class);                     
       registerElementHandler(new XMLElement(EjbTagNames.RELATIONSHIPS), 
                                                            RelationshipsNode.class);                     
       registerElementHandler(new XMLElement(TagNames.MESSAGE_DESTINATION),
                                             MessageDestinationNode.class,
                                             "addMessageDestination");
       registerElementHandler(new XMLElement(EjbTagNames.APPLICATION_EXCEPTION),
                                             EjbApplicationExceptionNode.class,
                                             "addApplicationException");
       registerElementHandler(new XMLElement(EjbTagNames.INTERCEPTOR),
                              EjbInterceptorNode.class,
                              "addInterceptor");

       registerElementHandler(new XMLElement(EjbTagNames.INTERCEPTOR_BINDING),
                              InterceptorBindingNode.class,
                              "appendInterceptorBinding");
   }
   
    /**
     * Adds  a new DOL descriptor instance to the descriptor instance associated with 
     * this XMLNode
     *
     * @param descriptor the new descriptor
     */   
   public void addDescriptor(Object newDescriptor) {       
       if (newDescriptor instanceof EjbDescriptor) {
           descriptor.addEjb((EjbDescriptor) newDescriptor);
       } else if (newDescriptor instanceof RelationshipDescriptor) {
           descriptor.addRelationship((RelationshipDescriptor) newDescriptor);
       } else if (newDescriptor instanceof MethodPermissionDescriptor) {
           MethodPermissionDescriptor   nd = (MethodPermissionDescriptor) newDescriptor;
           MethodDescriptor[] array = nd.getMethods();
           for (int i=0;i<array.length;i++) {
                EjbDescriptor target  = descriptor.getEjbByName(array[i].getEjbName());           
                MethodPermission[] mps = nd.getMethodPermissions();
                for (int j=0;j<mps.length;j++) {
                    DOLUtils.getDefaultLogger().fine("Adding mp " + mps[j] + " to " + array[i] + " for ejb " + array[i].getEjbName());
                    target.addPermissionedMethod(mps[j], array[i]);
                }
            }
       } else super.addDescriptor(newDescriptor);       
   }

    public void setElementValue(XMLElement element, String value) {

        if (TagNames.MODULE_NAME.equals(element.getQName())) {
            EjbBundleDescriptor bundleDesc = getDescriptor();
            // ejb-jar.xml <module-name> only applies if this is an ejb-jar
            if( bundleDesc.getModuleDescriptor().getDescriptor() instanceof EjbBundleDescriptor ) {
                bundleDesc.getModuleDescriptor().setModuleName(value);
            }        
        } else {
            super.setElementValue(element, value);
        }
    }

   /**
    * @return the descriptor instance to associate with this XMLNode
    */
    public EjbBundleDescriptor getDescriptor() {
        
        if (descriptor==null) {
            descriptor = (EjbBundleDescriptor) DescriptorFactory.getDescriptor(getXMLPath());
        }
        return descriptor;
    }       
       
   /**
     * @return the XML tag associated with this XMLNode
     */
    protected XMLElement getXMLRootTag() {
        return tag;
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
        table.put(EjbTagNames.EJB_CLIENT_JAR, "setEjbClientJarUri");
        return table;
    }        
        
    /**
     * write the descriptor class to a DOM tree and return it
     *
     * @param parent node for the DOM tree
     * @param the descriptor to write
     * @return the DOM tree top node
     */    
    public Node writeDescriptor(Node parent, EjbBundleDescriptor ejbDesc) {
        Node jarNode = super.writeDescriptor(parent, ejbDesc);
        Node entrepriseBeansNode = appendChild(jarNode, EjbTagNames.EJBS);
        for (Iterator ejbs = ejbDesc.getEjbs().iterator();ejbs.hasNext();) {
            EjbDescriptor ejb = (EjbDescriptor) ejbs.next();
            if (EjbSessionDescriptor.TYPE.equals(ejb.getType())) {
                EjbSessionNode subNode = new EjbSessionNode();
                subNode.writeDescriptor(entrepriseBeansNode, 
                                                                            EjbTagNames.SESSION, ejb);
            }  else if (EjbEntityDescriptor.TYPE.equals(ejb.getType())) {
                EjbEntityNode subNode = new EjbEntityNode();
                subNode.writeDescriptor(entrepriseBeansNode, 
                                                                            EjbTagNames.ENTITY, ejb);
            } else if (EjbMessageBeanDescriptor.TYPE.equals(ejb.getType())) {
                MessageDrivenBeanNode subNode = new MessageDrivenBeanNode();
                subNode.writeDescriptor(entrepriseBeansNode, 
                                                                            EjbTagNames.MESSAGE_DRIVEN, ejb);
            }  else {
                throw new IllegalStateException("Unknow ejb type " + ejb.getType());
            }
        }

        if( ejbDesc.hasInterceptors() ) {

            Node interceptorsNode = appendChild(jarNode, 
                                                EjbTagNames.INTERCEPTORS);
            EjbInterceptorNode interceptorNode = new EjbInterceptorNode();
            for(EjbInterceptor next : ejbDesc.getInterceptors()) {
                interceptorNode.writeDescriptor( interceptorsNode,
                                                 EjbTagNames.INTERCEPTOR, next);
            }

        }
        
        // relationships*
        if (ejbDesc.hasRelationships()) {
            (new RelationshipsNode()).writeDescriptor(jarNode, EjbTagNames.RELATIONSHIPS, ejbDesc);
        }
        
        // assembly-descriptor
        writeAssemblyDescriptor(jarNode, ejbDesc);
        
        appendTextChild(jarNode, EjbTagNames.EJB_CLIENT_JAR, ejbDesc.getEjbClientJarUri());        
        return jarNode;
    }    
    
    /**
     * @return the DOCTYPE of the XML file
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
     * write assembly-descriptor related xml information to the DOM tree
     */
    private void writeAssemblyDescriptor(Node parentNode, EjbBundleDescriptor bundleDescriptor) {
       Node assemblyNode = parentNode.getOwnerDocument().createElement(EjbTagNames.ASSEMBLY_DESCRIPTOR);
       
       // security-role*
       SecurityRoleNode roleNode = new SecurityRoleNode();
       for (Iterator e = bundleDescriptor.getRoles().iterator();e.hasNext();) {
           roleNode.writeDescriptor(assemblyNode, EjbTagNames.ROLE, (Role) e.next());
       }
       
       // method-permission*       
       Map excludedMethodsByEjb = new HashMap();
       MethodPermissionNode mpNode = new MethodPermissionNode();       
       for (Iterator e = bundleDescriptor.getEjbs().iterator();e.hasNext();) {
           EjbDescriptor ejbDesc = (EjbDescriptor) e.next();
           if (ejbDesc instanceof EjbMessageBeanDescriptor)                
               continue;
           Vector excludedMethods = new Vector();
           addMethodPermissions(ejbDesc, ejbDesc.getPermissionedMethodsByPermission(), excludedMethods,  mpNode, assemblyNode);
           addMethodPermissions(ejbDesc, ejbDesc.getStyledPermissionedMethodsByPermission(), excludedMethods, mpNode, assemblyNode);
           if (excludedMethods.size()>0) {
               excludedMethodsByEjb.put(ejbDesc, excludedMethods);
           }
       }
       
       // container-transaction*
       ContainerTransactionNode ctNode = new ContainerTransactionNode();
       for (Iterator e = bundleDescriptor.getEjbs().iterator();e.hasNext();) {
           EjbDescriptor ejbDesc = (EjbDescriptor) e.next();
           ctNode.writeDescriptor(assemblyNode, EjbTagNames.CONTAINER_TRANSACTION, ejbDesc);
       }

       // interceptor-binding*
       Set ejbsForInterceptors = bundleDescriptor.getEjbs();
       InterceptorBindingNode interceptorBindingNode = new 
           InterceptorBindingNode();

        for(Iterator itr = ejbsForInterceptors.iterator(); itr.hasNext();) {

            EjbDescriptor ejbDesc = (EjbDescriptor) itr.next();
            if( ejbDesc.getInterceptorClasses().size() > 0 ) {
                interceptorBindingNode.writeBindings(assemblyNode, 
                                                     ejbDesc);
            }

        }
       

       // message-destination*
       writeMessageDestinations
           (assemblyNode, bundleDescriptor.getMessageDestinations().iterator());
                                
       // exclude-list*              
       if (excludedMethodsByEjb.size()>0) {
           Node excludeListNode = this.appendChild(assemblyNode, EjbTagNames.EXCLUDE_LIST);
           for (Iterator ejbs = excludedMethodsByEjb.keySet().iterator(); ejbs.hasNext();) {
               EjbDescriptor ejbDesc = (EjbDescriptor) ejbs.next();
               Vector excludedMethods = (Vector) excludedMethodsByEjb.get(ejbDesc);
               
               MethodPermissionDescriptor mpd = new MethodPermissionDescriptor();
               mpd.addMethodPermission(MethodPermission.getExcludedMethodPermission());
               mpd.addMethods(excludedMethods);
               mpNode.writeDescriptorInNode(excludeListNode, mpd, ejbDesc);
           }
       }
       
       for(EjbApplicationExceptionInfo next : 
               bundleDescriptor.getApplicationExceptions().values()) {

           EjbApplicationExceptionNode node = new EjbApplicationExceptionNode();
           
           node.writeDescriptor(assemblyNode, EjbTagNames.APPLICATION_EXCEPTION,
                                next);

       }

       if (assemblyNode.hasChildNodes()) {
           parentNode.appendChild(assemblyNode);
       }
    }
    
    private void addMethodPermissions(
            EjbDescriptor ejb, 
            Map mpToMethods,  
            Vector  excludedMethods,
            MethodPermissionNode mpNode, 
            Node assemblyNode) {
                
        if (mpToMethods==null || mpToMethods.size()==0) {
            return;
        }
        
        for (Iterator mpIterator = mpToMethods.keySet().iterator();mpIterator.hasNext();) {
            MethodPermission mp = (MethodPermission) mpIterator.next();
            if (mp.isExcluded()) {
                // we need to be sure the method descriptors knows who owns them
                Set methods = (Set) mpToMethods.get(mp);
                excludedMethods.addAll(methods);
            } else {
                MethodPermissionDescriptor mpd = new MethodPermissionDescriptor();
                mpd.addMethodPermission(mp);
                mpd.addMethods((Set) mpToMethods.get(mp));
                mpNode.writeDescriptor(assemblyNode, EjbTagNames.METHOD_PERMISSION, mpd, ejb);
            }
        }
    }
    
    /**
     * @return the default spec version level this node complies to
     */
    public String getSpecVersion() {
        return SPEC_VERSION;
    }
    
}
