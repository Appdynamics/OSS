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

/*
 * IconNode.java
 *
 * Created on August 19, 2002, 9:55 AM
 */

package com.sun.enterprise.deployment.node;

import com.sun.enterprise.deployment.Descriptor;
import com.sun.enterprise.deployment.xml.TagNames;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

/**
 *
 * @author  dochez
 */
public class IconNode extends LocalizedNode {
    
    private String smallIcon = null;
    private String largeIcon = null;
    
    /**
     * @return the descriptor for this node
     */
    public Object getDescriptor() {
        return null;
    }
    
    /**
     * receives notification of the value for a particular tag
     * 
     * @param element the xml element
     * @param value it's associated value
     */
    public void setElementValue(XMLElement element, String value) {
        if (element.getQName().equals(TagNames.SMALL_ICON)) {
            smallIcon = value;
        } else 
        if (element.getQName().equals(TagNames.LARGE_ICON)) {
            largeIcon = value;
        }        
    }
        
    /**
     * notification of the end of XML parsing for this node
     */
    public void postParsing() {
        Object o = getParentNode().getDescriptor();    
        if (o!=null && o instanceof Descriptor) {
            Descriptor descriptor = (Descriptor) o;
            if (largeIcon!=null) {
                descriptor.setLocalizedLargeIconUri(lang, largeIcon);
            }
            if (smallIcon!=null) {
                descriptor.setLocalizedSmallIconUri(lang, smallIcon);
            }            
        }
    }
    
    /**
     * writes all localized icon information
     * 
     * @param parentNode for all icon xml fragments
     * @param descriptor containing the icon information
     */
    public void writeLocalizedInfo(Node parentNode, Descriptor descriptor) {
        Map largeIcons = descriptor.getLocalizedLargeIconUris();
        Map smallIcons = descriptor.getLocalizedSmallIconUris();
        if (largeIcons==null && smallIcons==null) {
            return;
        }
        if (smallIcons!=null) {
            for (Iterator smallItr = smallIcons.keySet().iterator();smallItr.hasNext();) {
                String lang = (String) smallItr.next();
                String smallIconUri = (String) smallIcons.get(lang);                
                String largeIconUri = null;
                if (largeIcons!=null) {
                    largeIconUri = (String) largeIcons.get(lang);
                }
                addIconInfo(parentNode, lang, smallIconUri, largeIconUri);
            }
        } 
        if (largeIcons!=null) {
            for (Iterator largeItr = largeIcons.keySet().iterator();largeItr.hasNext();) {
                String lang = (String) largeItr.next();
                String largeIconUri = (String) largeIcons.get(lang);
                if (smallIcons!=null && smallIcons.get(lang)!=null) {
                    // we already wrote this icon info in the previous loop
                    continue;
                }
                addIconInfo(parentNode, lang, null, largeIconUri);
            }
        }
        
    }
    
    /**
     * writes xml tag and fragment for a particular icon information
     */
    private void addIconInfo(Node node, String lang, String smallIconUri, String largeIconUri) {
        
        Element iconNode =appendChild(node, TagNames.ICON);
        if (lang!=Locale.ENGLISH.getLanguage()) {
	    iconNode.setAttributeNS(TagNames.XML_NAMESPACE, TagNames.XML_NAMESPACE_PREFIX + TagNames.LANG, lang);
	}
        appendTextChild(iconNode, TagNames.SMALL_ICON, smallIconUri);
        appendTextChild(iconNode, TagNames.LARGE_ICON, largeIconUri);
    }
}
