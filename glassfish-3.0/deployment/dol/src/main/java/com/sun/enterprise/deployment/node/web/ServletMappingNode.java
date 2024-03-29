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

package com.sun.enterprise.deployment.node.web;

import com.sun.enterprise.deployment.WebBundleDescriptor;
import com.sun.enterprise.deployment.node.DeploymentDescriptorNode;
import com.sun.enterprise.deployment.node.XMLElement;
import com.sun.enterprise.deployment.node.XMLNode;
import com.sun.enterprise.deployment.util.DOLUtils;
import com.sun.enterprise.deployment.xml.WebTagNames;
import com.sun.enterprise.util.LocalStringManagerImpl;
import com.sun.enterprise.util.net.URLPattern;

import java.util.logging.Level;

/**
 * This node is responsible for handling servlet-mapping subtree node
 *
 * @author  Jerome Dochez
 * @version 
 */
public class ServletMappingNode extends DeploymentDescriptorNode {

    private String servletName;
    private String urlPattern;

    private static LocalStringManagerImpl localStrings =
            new LocalStringManagerImpl(ServletMappingNode.class);

    /**
     * receives notiification of the value for a particular tag
     * 
     * @param element the xml element
     * @param value it's associated value
     */    
    public void setElementValue(XMLElement element, String value) {
        if (WebTagNames.SERVLET_NAME.equals(element.getQName())) {
            servletName = value;
        } 
        if (WebTagNames.URL_PATTERN.equals(element.getQName())) {
            if (!URLPattern.isValid(value)) {
                // try trimming url (in case DD uses extra
                // whitespace for aligning)
                String trimmedUrl = value.trim();
                if ("\"\"".equals(trimmedUrl)) {
                    trimmedUrl = "";
                }

                // If URL Pattern does not start with "/" then 
                // prepend it (for Servlet2.2 Web apps) 
                Object parent = getParentNode().getDescriptor(); 
                if (parent instanceof WebBundleDescriptor &&  
                        ((WebBundleDescriptor) parent).getSpecVersion().equals("2.2")) { 
                    if(!trimmedUrl.startsWith("/") &&
                            !trimmedUrl.startsWith("*.")) { 
                        trimmedUrl = "/" + trimmedUrl; 
                    } 
                }

                if (URLPattern.isValid(trimmedUrl)) {
                    // warn user if url included \r or \n
                    if (URLPattern.containsCRorLF(value)) {
                        DOLUtils.getDefaultLogger().log(Level.WARNING,
                                "enterprise.deployment.backend.urlcontainscrlf",
                                new Object[] { value });
                    }
                    value = trimmedUrl;
                } else {
                    throw new IllegalArgumentException(localStrings
                            .getLocalString(
                                    "enterprise.deployment.invalidurlpattern",
                                    "Invalid URL Pattern: [{0}]",
                                    new Object[] { value }));
                }
            }

            urlPattern = value;

            XMLNode  parentNode = getParentNode();
            if (parentNode instanceof WebCommonNode) {
                ((WebCommonNode) parentNode).addServletMapping(servletName, 
                urlPattern);
            } else {
                DOLUtils.getDefaultLogger().log(Level.SEVERE, "enterprise.deployment.backend.addDescriptorFailure",
                    new Object[]{getXMLRootTag() , "servlet-mapping"});
            }

        } 
    }
    
        
   /**
    * @return the descriptor instance to associate with this XMLNode
    */    
    public Object getDescriptor() {
        return null;
    }
}
