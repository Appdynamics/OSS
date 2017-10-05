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

package com.sun.enterprise.deployment.node.runtime.web;

import com.sun.enterprise.deployment.*;
import com.sun.enterprise.deployment.interfaces.SecurityRoleMapper;
import com.sun.enterprise.deployment.node.XMLElement;
import com.sun.enterprise.deployment.node.runtime.*;
import com.sun.enterprise.deployment.node.runtime.common.EjbRefNode;
import com.sun.enterprise.deployment.node.runtime.common.ResourceEnvRefNode;
import com.sun.enterprise.deployment.node.runtime.common.ResourceRefNode;
import com.sun.enterprise.deployment.node.runtime.common.SecurityRoleMappingNode;
import com.sun.enterprise.deployment.runtime.common.*;
import com.sun.enterprise.deployment.runtime.web.*;
import com.sun.enterprise.deployment.runtime.web.ClassLoader;
import com.sun.enterprise.deployment.types.EjbReference;
import com.sun.enterprise.deployment.util.DOLUtils;
import com.sun.enterprise.deployment.xml.DTDRegistry;
import com.sun.enterprise.deployment.xml.RuntimeTagNames;
import com.sun.enterprise.deployment.xml.WebServicesTagNames;
import org.glassfish.security.common.Group;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * This node is responsible for handling all runtime information for 
 * web bundle.
 *
 * @author  Jerome Dochez
 * @version 
 */
public class WebBundleRuntimeNode extends RuntimeBundleNode<WebBundleDescriptor> {

    WebBundleDescriptor descriptor=null;
        
    /** Creates new WebBundleRuntimeNode */
    public WebBundleRuntimeNode(WebBundleDescriptor descriptor) {
        super(descriptor);
        this.descriptor = descriptor;        
	getSunDescriptor();
    }
    
    /** Creates new WebBundleRuntimeNode */
    public WebBundleRuntimeNode() {
        super(null);    
    }
    
    /**
     * Initialize the child handlers
     */
    protected void Init() {
        // we do not care about our standard DDS handles
        handlers = null;
        
        registerElementHandler(new XMLElement(RuntimeTagNames.SECURITY_ROLE_MAPPING), 
                               SecurityRoleMappingNode.class);
	registerElementHandler(new XMLElement(RuntimeTagNames.SERVLET),
			       com.sun.enterprise.deployment.node.runtime.ServletNode.class);
	registerElementHandler(new XMLElement(RuntimeTagNames.IDEMPOTENT_URL_PATTERN), IdempotentUrlPatternNode.class);
	registerElementHandler(new XMLElement(RuntimeTagNames.SESSION_CONFIG),
			       SessionConfigNode.class);
	registerElementHandler(new XMLElement(RuntimeTagNames.RESOURCE_ENV_REFERENCE), 
                               ResourceEnvRefNode.class);  
        registerElementHandler(new XMLElement(RuntimeTagNames.MESSAGE_DESTINATION_REFERENCE),
                               MessageDestinationRefNode.class);

        registerElementHandler(new XMLElement(RuntimeTagNames.RESOURCE_REFERENCE), 
                               ResourceRefNode.class);
        registerElementHandler(new XMLElement(RuntimeTagNames.EJB_REFERENCE), 
                               EjbRefNode.class);    
        
	registerElementHandler(new XMLElement(RuntimeTagNames.CACHE), 
                               CacheNode.class); 
	
	registerElementHandler(new XMLElement(RuntimeTagNames.CLASS_LOADER), 
                               ClassLoaderNode.class); 
	
        registerElementHandler(new XMLElement(RuntimeTagNames.JSP_CONFIG), 
                               WebPropertyContainerNode.class);   
        
        registerElementHandler(new XMLElement(RuntimeTagNames.LOCALE_CHARSET_INFO), 
                               LocaleCharsetInfoNode.class);   
  	
	registerElementHandler(new XMLElement(RuntimeTagNames.PROPERTY),
                               WebPropertyContainerNode.class);   
			       
        registerElementHandler(new XMLElement(WebServicesTagNames.SERVICE_REF),
                               ServiceRefNode.class); 
        registerElementHandler(new XMLElement(RuntimeTagNames.MESSAGE_DESTINATION), 
        			MessageDestinationRuntimeNode.class);
        registerElementHandler(new XMLElement(WebServicesTagNames.WEB_SERVICE),
             			WebServiceRuntimeNode.class);
        registerElementHandler(new XMLElement(RuntimeTagNames.VALVE),
                               ValveNode.class);

    }
    
    /**
     * @return the XML tag associated with this XMLNode
     */
    protected XMLElement getXMLRootTag() {
        return new XMLElement(RuntimeTagNames.S1AS_WEB_RUNTIME_TAG);
    }    
    
    /** 
     * @return the DOCTYPE that should be written to the XML file
     */
    public String getDocType() {
        return DTDRegistry.SUN_WEBAPP_300_DTD_PUBLIC_ID;
    }
    
    /**
     * @return the SystemID of the XML file
     */
    public String getSystemID() {
        return DTDRegistry.SUN_WEBAPP_300_DTD_SYSTEM_ID;
    }

    /**
     * @return NULL for all runtime nodes.
     */
    public List<String> getSystemIDs() {
        return null;
    }
    
   /**
    * register this node as a root node capable of loading entire DD files
    * 
    * @param publicIDToDTD is a mapping between xml Public-ID to DTD 
    * @return the doctype tag name
    */
   public static String registerBundle(Map publicIDToDTD) {    
       publicIDToDTD.put(DTDRegistry.SUN_WEBAPP_230_DTD_PUBLIC_ID, DTDRegistry.SUN_WEBAPP_230_DTD_SYSTEM_ID);
       publicIDToDTD.put(DTDRegistry.SUN_WEBAPP_231_DTD_PUBLIC_ID, DTDRegistry.SUN_WEBAPP_231_DTD_SYSTEM_ID);
       publicIDToDTD.put(DTDRegistry.SUN_WEBAPP_240_DTD_PUBLIC_ID, DTDRegistry.SUN_WEBAPP_240_DTD_SYSTEM_ID);
       publicIDToDTD.put(DTDRegistry.SUN_WEBAPP_241_DTD_PUBLIC_ID, DTDRegistry.SUN_WEBAPP_241_DTD_SYSTEM_ID);
       publicIDToDTD.put(DTDRegistry.SUN_WEBAPP_250_DTD_PUBLIC_ID, DTDRegistry.SUN_WEBAPP_250_DTD_SYSTEM_ID);
       publicIDToDTD.put(DTDRegistry.SUN_WEBAPP_300_DTD_PUBLIC_ID, DTDRegistry.SUN_WEBAPP_300_DTD_SYSTEM_ID);
       if (!restrictDTDDeclarations()) {
          publicIDToDTD.put(DTDRegistry.SUN_WEBAPP_240beta_DTD_PUBLIC_ID, DTDRegistry.SUN_WEBAPP_240beta_DTD_SYSTEM_ID);
       }
       
       return RuntimeTagNames.S1AS_WEB_RUNTIME_TAG;       
   }    
    
   /**
    * @return the descriptor instance to associate with this XMLNode
    */    
    public Object getSunDescriptor() {    
        
	return descriptor.getSunDescriptor();                
    }
    
       /**
    * @return the web bundle descriptor instance to associate with this XMLNode
    */    
    public WebBundleDescriptor getDescriptor() {    
	return descriptor;               
    }
    
    public WebBundleDescriptor getWebBundleDescriptor() {    
        return getDescriptor();               
    }

    /**
     * Adds  a new DOL descriptor instance to the descriptor instance associated with 
     * this XMLNode
     *
     * @param descriptor the new descriptor
     */
    public void addDescriptor(Object newDescriptor) {
	if (newDescriptor instanceof EjbRef) {
	    EjbRef ejbRef = (EjbRef) newDescriptor;
	    descriptor.getSunDescriptor().addEjbRef(ejbRef);
            try {
	        EjbReference ref = descriptor.getEjbReference(ejbRef.getEjbRefName());
	        ref.setJndiName(ejbRef.getJndiName());
            } catch (IllegalArgumentException iae) {
                DOLUtils.getDefaultLogger().warning(iae.getMessage());
            }
	} else
	if (newDescriptor instanceof ResourceRef) {
	    ResourceRef resourceRef = (ResourceRef) newDescriptor;
	    descriptor.getSunDescriptor().addResourceRef(resourceRef);
            try {
	        ResourceReferenceDescriptor rrd = descriptor.getResourceReferenceByName(resourceRef.getResRefName());
	        rrd.setJndiName(resourceRef.getJndiName());
    	        DefaultResourcePrincipal drp = resourceRef.getDefaultResourcePrincipal();
    	        if (drp!=null) {
		    ResourcePrincipal rp = new ResourcePrincipal(drp.getName(), drp.getPassword());
    		    rrd.setResourcePrincipal(rp);
	        }
            } catch (IllegalArgumentException iae) {
                DOLUtils.getDefaultLogger().warning(iae.getMessage());
            }
	} else 
	if (newDescriptor instanceof ResourceEnvRef) {
	    ResourceEnvRef resourceEnvRef = (ResourceEnvRef) newDescriptor;
	    descriptor.getSunDescriptor().addResourceEnvRef(resourceEnvRef);
            try {
	        JmsDestinationReferenceDescriptor  rrd = descriptor.getJmsDestinationReferenceByName(resourceEnvRef.getResourceEnvRefName());
	        rrd.setJndiName(resourceEnvRef.getJndiName());
            } catch (IllegalArgumentException iae) {
                DOLUtils.getDefaultLogger().warning(iae.getMessage());
            }
	} else 
	if (newDescriptor instanceof WebComponentDescriptor) {
	    WebComponentDescriptor servlet = (WebComponentDescriptor) newDescriptor;
            // for backward compatibility with s1as schema2beans generated desc
            Servlet s1descriptor = new Servlet();
            s1descriptor.setServletName(servlet.getCanonicalName());
            if (servlet.getRunAsIdentity()!=null) {
                s1descriptor.setPrincipalName(servlet.getRunAsIdentity().getPrincipal());
            }
	    descriptor.getSunDescriptor().addServlet(s1descriptor);
	} else
        if (newDescriptor instanceof ServiceReferenceDescriptor) {
            descriptor.addServiceReferenceDescriptor((ServiceReferenceDescriptor) newDescriptor);
        } else 
        if (newDescriptor instanceof SecurityRoleMapping) {
            SecurityRoleMapping srm = (SecurityRoleMapping) newDescriptor;
            descriptor.getSunDescriptor().addSecurityRoleMapping(srm);
            // store it in the application using pure DOL descriptors...
            Application app = descriptor.getApplication();
            if (app!=null) {
                Role role = new Role(srm.getRoleName());
                SecurityRoleMapper rm = app.getRoleMapper();
                if (rm != null) {
                    List<PrincipalNameDescriptor> principals = srm.getPrincipalNames();
                    for (int i = 0; i < principals.size(); i++) {
                        rm.assignRole(principals.get(i).getPrincipal(),
                            role, descriptor);
                    }
                    List<String> groups = srm.getGroupNames();
                    for (int i = 0; i < groups.size(); i++) {
                        rm.assignRole(new Group(groups.get(i)),
                            role, descriptor);
                    }
                }
            }                
        } else if (newDescriptor instanceof IdempotentUrlPattern) {
            descriptor.getSunDescriptor().addIdempotentUrlPattern(
                (IdempotentUrlPattern)newDescriptor);
        } else if (newDescriptor instanceof SessionConfig) {
            descriptor.getSunDescriptor().setSessionConfig(
                (SessionConfig)newDescriptor);
        } else if (newDescriptor instanceof Cache) {
            descriptor.getSunDescriptor().setCache(
                (Cache)newDescriptor);
        } else if (newDescriptor instanceof ClassLoader) {
            descriptor.getSunDescriptor().setClassLoader(
                (ClassLoader)newDescriptor);
        } else if (newDescriptor instanceof JspConfig) {
            descriptor.getSunDescriptor().setJspConfig(
                (JspConfig)newDescriptor);
        } else if (newDescriptor instanceof LocaleCharsetInfo) {
            descriptor.getSunDescriptor().setLocaleCharsetInfo(
                (LocaleCharsetInfo)newDescriptor);
        } else if (newDescriptor instanceof WebProperty) {
            descriptor.getSunDescriptor().addWebProperty(
                (WebProperty)newDescriptor);
        } else if (newDescriptor instanceof Valve) {
            descriptor.getSunDescriptor().addValve(
                (Valve)newDescriptor);
        }
	else super.addDescriptor(descriptor);
    }

    public void startElement(XMLElement element, Attributes attributes) {
        if (element.getQName().equals(RuntimeTagNames.PARAMETER_ENCODING)) {
            SunWebApp sunWebApp = (SunWebApp)getSunDescriptor();
            sunWebApp.setParameterEncoding(true);
            for (int i=0; i<attributes.getLength();i++) {
                if (RuntimeTagNames.DEFAULT_CHARSET.equals(
                    attributes.getQName(i))) {
                    sunWebApp.setAttributeValue(SunWebApp.PARAMETER_ENCODING, SunWebApp.DEFAULT_CHARSET, attributes.getValue(i));
                }
                if (RuntimeTagNames.FORM_HINT_FIELD.equals(
                    attributes.getQName(i))) {
                    sunWebApp.setAttributeValue(SunWebApp.PARAMETER_ENCODING, SunWebApp.FORM_HINT_FIELD, attributes.getValue(i));
                }
            }
        } else super.startElement(element, attributes);
    }

    /**
     * parsed an attribute of an element
     *  
     * @param the element name
     * @param the attribute name
     * @param the attribute value
     * @return true if the attribute was processed
     */ 
    protected boolean setAttributeValue(XMLElement elementName,
        XMLElement attributeName, String value) {
        SunWebApp sunWebApp = (SunWebApp)getSunDescriptor();
        if (attributeName.getQName().equals(RuntimeTagNames.ERROR_URL)) {
            sunWebApp.setAttributeValue(sunWebApp.ERROR_URL, value);
            return true;
        }
        if (attributeName.getQName().equals(RuntimeTagNames.HTTPSERVLET_SECURITY_PROVIDER)) {
            sunWebApp.setAttributeValue(sunWebApp.HTTPSERVLET_SECURITY_PROVIDER, value);
            return true;
        }

        return false;
    }


    /**
     * receives notification of the value for a particular tag
     * 
     * @param element the xml element
     * @param value it's associated value
     */
    public void setElementValue(XMLElement element, String value) {
        if (element.getQName().equals(RuntimeTagNames.CONTEXT_ROOT)) {
            // only set the context root for standalone war;
            // for embedded war, the context root will be set 
            // using the value in application.xml
            Application app = descriptor.getApplication();
            if ( (app == null) || (app!=null && app.isVirtual()) ) {
                descriptor.setContextRoot(value);
            }
        } else
	super.setElementValue(element, value);
    }

    /**
     * write the descriptor class to a DOM tree and return it
     *
     * @param parent node for the DOM tree
     * @param the descriptor to write
     * @return the DOM tree top node
     */    
    public Node writeDescriptor(Node parent, WebBundleDescriptor bundleDescriptor) {    
        Element web = (Element)super.writeDescriptor(parent, bundleDescriptor); 
	
	SunWebApp sunWebApp = bundleDescriptor.getSunDescriptor();
        
        // context-root?
	appendTextChild(web, RuntimeTagNames.CONTEXT_ROOT, bundleDescriptor.getContextRoot());
        
	// security-role-mapping
	SecurityRoleMapping[] roleMappings = sunWebApp.getSecurityRoleMapping();
	if (roleMappings!=null && roleMappings.length>0) {
	    SecurityRoleMappingNode srmn = new SecurityRoleMappingNode();
	    for (int i=0;i<roleMappings.length;i++) {
		srmn.writeDescriptor(web, RuntimeTagNames.SECURITY_ROLE_MAPPING, roleMappings[i]);
	    }
	}
	
	// servlet
	Set servlets = bundleDescriptor.getServletDescriptors();
	if (servlets!=null) {
	    com.sun.enterprise.deployment.node.runtime.ServletNode node = 
                new com.sun.enterprise.deployment.node.runtime.ServletNode();
	    for (Iterator itr=servlets.iterator();itr.hasNext();) {
                WebComponentDescriptor servlet = (WebComponentDescriptor) itr.next();
		node.writeDescriptor(web, RuntimeTagNames.SERVLET, servlet);
	    }
	}
	
        // idempotent-url-pattern
        IdempotentUrlPattern[] patterns = sunWebApp.getIdempotentUrlPatterns();
        if (patterns != null && patterns.length > 0) {
            IdempotentUrlPatternNode node = new IdempotentUrlPatternNode();
            for (int i = 0;i < patterns.length; i++) {
                node.writeDescriptor(web, RuntimeTagNames.IDEMPOTENT_URL_PATTERN, patterns[i]);
            }
        }      

	// session-config?
	if (sunWebApp.getSessionConfig()!=null) {
	    SessionConfigNode scn = new SessionConfigNode();
	    scn.writeDescriptor(web, RuntimeTagNames.SESSION_CONFIG, sunWebApp.getSessionConfig());
	}
        
	// ejb-ref*
	EjbRef[] ejbRefs = sunWebApp.getEjbRef();
	if (ejbRefs!=null && ejbRefs.length>0) {
	    EjbRefNode node = new EjbRefNode();
	    for (int i=0;i<ejbRefs.length;i++) {
		node.writeDescriptor(web, RuntimeTagNames.EJB_REF, ejbRefs[i]);
	    }
	}        
	
	// resource-ref*
	ResourceRef[] resourceRefs = sunWebApp.getResourceRef();
	if (resourceRefs!=null && resourceRefs.length>0) {
	    ResourceRefNode node = new ResourceRefNode();
	    for (int i=0;i<resourceRefs.length;i++) {
		node.writeDescriptor(web, RuntimeTagNames.RESOURCE_REF, resourceRefs[i]);
	    }
	}
        
	// resource-env-ref*
	ResourceEnvRef[] resourceEnvRefs = sunWebApp.getResourceEnvRef();
	if (resourceEnvRefs!=null && resourceEnvRefs.length>0) {
	    ResourceEnvRefNode node = new ResourceEnvRefNode();
	    for (int i=0;i<resourceEnvRefs.length;i++) {
		node.writeDescriptor(web, RuntimeTagNames.RESOURCE_ENV_REF, resourceEnvRefs[i]);
	    }
	}	
        
	// service-ref*
	if (bundleDescriptor.hasServiceReferenceDescriptors()) {
	    ServiceRefNode serviceNode = new ServiceRefNode();
	    for (Iterator serviceItr=bundleDescriptor.getServiceReferenceDescriptors().iterator();
	    		serviceItr.hasNext();) {
		ServiceReferenceDescriptor next = (ServiceReferenceDescriptor) serviceItr.next();
		serviceNode.writeDescriptor(web, WebServicesTagNames.SERVICE_REF, next);
		}
	}        

        // message-destination-ref*
        MessageDestinationRefNode.writeMessageDestinationReferences(web,
            bundleDescriptor);

	
	// cache?
	Cache cache = sunWebApp.getCache();
	if (cache!=null) {
	    CacheNode cn = new CacheNode();
	    cn.writeDescriptor(web, RuntimeTagNames.CACHE, cache);
	}
	
	// class-loader?
        ClassLoader classLoader = sunWebApp.getClassLoader();
        if (classLoader!=null) {
            ClassLoaderNode cln = new ClassLoaderNode();
            cln.writeDescriptor(web, RuntimeTagNames.CLASS_LOADER, classLoader);
        }

	// jsp-config?
	if (sunWebApp.getJspConfig()!=null) {
	    WebPropertyNode propertyNode = new WebPropertyNode();
	    Node jspConfig = appendChild(web, RuntimeTagNames.JSP_CONFIG);
	    propertyNode.writeDescriptor(jspConfig, RuntimeTagNames.PROPERTY, sunWebApp.getJspConfig().getWebProperty());
	}
	
	// locale-charset-info?
	if (sunWebApp.getLocaleCharsetInfo()!=null) {
	    LocaleCharsetInfoNode localeNode = new LocaleCharsetInfoNode();
	    localeNode.writeDescriptor(web, RuntimeTagNames.LOCALE_CHARSET_INFO, 
	    	sunWebApp.getLocaleCharsetInfo());
	}	
	
        // parameter-encoding?
        if (sunWebApp.isParameterEncoding()) {
            Element parameter = (Element) appendChild(web, RuntimeTagNames.PARAMETER_ENCODING);

            if (sunWebApp.getAttributeValue(SunWebApp.PARAMETER_ENCODING, SunWebApp.FORM_HINT_FIELD)!=null) {
                setAttribute(parameter, RuntimeTagNames.FORM_HINT_FIELD,
                (String) sunWebApp.getAttributeValue(SunWebApp.PARAMETER_ENCODING, SunWebApp.FORM_HINT_FIELD));
            }

            if (sunWebApp.getAttributeValue(SunWebApp.PARAMETER_ENCODING, SunWebApp.DEFAULT_CHARSET)!=null) {
                setAttribute(parameter, RuntimeTagNames.DEFAULT_CHARSET,
                (String) sunWebApp.getAttributeValue(SunWebApp.PARAMETER_ENCODING, SunWebApp.DEFAULT_CHARSET));
            }
        }          

	// property*
	if (sunWebApp.getWebProperty()!=null) {
	    WebPropertyNode props = new WebPropertyNode();
	    props.writeDescriptor(web, RuntimeTagNames.PROPERTY, sunWebApp.getWebProperty());
	}
        
        // valve*
        if (sunWebApp.getValve()!=null) {
            ValveNode valve = new ValveNode();
            valve.writeDescriptor(web, RuntimeTagNames.VALVE,
                                  sunWebApp.getValve());
        }

	// message-destination*
        RuntimeDescriptorNode.writeMessageDestinationInfo(web, bundleDescriptor);

	// webservice-description*
        WebServiceRuntimeNode webServiceNode = new WebServiceRuntimeNode();
        webServiceNode.writeWebServiceRuntimeInfo(web, bundleDescriptor);

        // error-url
        if (sunWebApp.getAttributeValue(sunWebApp.ERROR_URL) != null) {
            setAttribute(web, RuntimeTagNames.ERROR_URL, sunWebApp.getAttributeValue(sunWebApp.ERROR_URL));
        }

        // httpservlet-security-provider
        if (sunWebApp.getAttributeValue(sunWebApp.HTTPSERVLET_SECURITY_PROVIDER) != null) {
            setAttribute(web, RuntimeTagNames.HTTPSERVLET_SECURITY_PROVIDER, 
                         sunWebApp.getAttributeValue(sunWebApp.HTTPSERVLET_SECURITY_PROVIDER));
        }

        return web;
    }
}
