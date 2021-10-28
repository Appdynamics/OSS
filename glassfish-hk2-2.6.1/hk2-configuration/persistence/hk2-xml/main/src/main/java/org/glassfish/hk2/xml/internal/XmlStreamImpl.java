/*
 * Copyright (c) 2016, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package org.glassfish.hk2.xml.internal;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.Unmarshaller.Listener;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.glassfish.hk2.utilities.general.GeneralUtilities;
import org.glassfish.hk2.utilities.general.IndentingXMLStreamWriter;
import org.glassfish.hk2.utilities.reflection.ClassReflectionHelper;
import org.glassfish.hk2.utilities.reflection.Logger;
import org.glassfish.hk2.utilities.reflection.ReflectionHelper;
import org.glassfish.hk2.xml.api.XmlHk2ConfigurationBean;
import org.glassfish.hk2.xml.api.XmlRootHandle;
import org.glassfish.hk2.xml.api.XmlService;
import org.glassfish.hk2.xml.jaxb.internal.BaseHK2JAXBBean;
import org.glassfish.hk2.xml.spi.Model;

/**
 * @author jwells
 *
 */
public class XmlStreamImpl {
    private final static boolean DEBUG_PARSING = XmlServiceImpl.DEBUG_PARSING;
    private final static QName ANY_ATTRIBUTE_QNAME = QNameUtilities.createQName(XmlService.DEFAULT_NAMESPACE, GeneratorUtilities.XML_ANY_ATTRIBUTE_LOCAL_PART);
    
    @SuppressWarnings("unchecked")
    public static <T> T parseRoot(XmlServiceImpl xmlService,
            Model rootModel,
            XMLStreamReader reader,
            Unmarshaller.Listener listener) throws Exception {
        Class<?> rootProxyClass = rootModel.getProxyAsClass();
        
        ClassReflectionHelper classReflectionHelper = xmlService.getClassReflectionHelper();
        
        BaseHK2JAXBBean hk2Root = Utilities.createBean(rootProxyClass);
        hk2Root._setClassReflectionHelper(classReflectionHelper);
        if (DEBUG_PARSING) {
            Logger.getLogger().debug("XmlServiceDebug Created root bean with model " + hk2Root._getModel());
        }
        
        Map<ReferenceKey, BaseHK2JAXBBean> referenceMap = new HashMap<ReferenceKey, BaseHK2JAXBBean>();
        List<UnresolvedReference> unresolved = new LinkedList<UnresolvedReference>();
        
        while(reader.hasNext()) {
            int event = reader.next();
            if (DEBUG_PARSING) {
                Logger.getLogger().debug("XmlServiceDebug got xml event (A) " + eventToString(event));
            }
            
            switch(event) {
            case XMLStreamConstants.START_ELEMENT:
                HashMap<String, String> rootNamespace = new HashMap<String, String>();
                String defaultNamespace = null;
                
                int namespaceCount = reader.getNamespaceCount();
                for (int nLcv = 0; nLcv < namespaceCount; nLcv++) {
                    String namespacePrefix = reader.getNamespacePrefix(nLcv);
                    String namespaceURI = reader.getNamespaceURI(nLcv);
                    
                    if (namespacePrefix == null) {
                        defaultNamespace = namespaceURI;
                    }
                    else {
                        rootNamespace.put(namespacePrefix, namespaceURI);
                    }
                }
                
                String elementTagNamespace = QNameUtilities.getNamespace(reader.getName(), defaultNamespace);
                String elementTag = reader.getName().getLocalPart();
                if (DEBUG_PARSING) {
                    Logger.getLogger().debug("XmlServiceDebug starting document tag " + elementTag);
                }
                
                handleElement(hk2Root,
                        null,
                        reader,
                        classReflectionHelper,
                        listener,
                        referenceMap,
                        unresolved,
                        elementTagNamespace,
                        elementTag,
                        rootNamespace,
                        defaultNamespace);
                
                break;
            case XMLStreamConstants.END_DOCUMENT:
                // Resolve any forward references
                Utilities.fillInUnfinishedReferences(referenceMap, unresolved);
                
                if (DEBUG_PARSING) {
                    Logger.getLogger().debug("XmlServiceDebug finished reading document");
                }
                
                return (T) hk2Root;
            default:
                // Do nothing
            }
            
        }
        
        throw new IllegalStateException("Unexpected end of XMLReaderStream");
    }
    
    private static <T> void handleElement(BaseHK2JAXBBean target,
            BaseHK2JAXBBean parent,
            XMLStreamReader reader,
            ClassReflectionHelper classReflectionHelper,
            Listener listener,
            Map<ReferenceKey, BaseHK2JAXBBean> referenceMap,
            List<UnresolvedReference> unresolved,
            String outerElementNamespace,
            String outerElementTag,
            Map<String, String> namespaceMap,
            String defaultNamespace) throws Exception {
        listener.beforeUnmarshal(target, parent);
        
        Map<QName, List<BaseHK2JAXBBean>> listChildren = new HashMap<QName, List<BaseHK2JAXBBean>>();
        Map<QName, List<BaseHK2JAXBBean>> arrayChildren = new HashMap<QName, List<BaseHK2JAXBBean>>();
        
        Map<QName, List<Object>> listNonChild = new HashMap<QName, List<Object>>();
        Map<QName, ArrayInformation> arrayNonChild = new HashMap<QName, ArrayInformation>();
        
        ModelImpl targetModel = target._getModel();
        Map<QName, ChildDataModel> nonChildProperties = targetModel.getNonChildProperties();
        Map<QName, ParentedModel> childProperties = targetModel.getChildrenByName();
        Set<String> allWrappers = targetModel.getAllXmlWrappers();
        
        Map<QName, String> xmlAnyAttributeData = new LinkedHashMap<QName, String>();
        int numAttributes = reader.getAttributeCount();
        for (int lcv = 0; lcv < numAttributes; lcv++) {
            String attributeNamespace = QNameUtilities.fixNamespace(reader.getAttributeNamespace(lcv));
            String attributeName = reader.getAttributeLocalName(lcv);
            String attributeValue = reader.getAttributeValue(lcv);
            
            QName attributeQName = QNameUtilities.createQName(attributeNamespace, attributeName, defaultNamespace);
            
            if (DEBUG_PARSING) {
                Logger.getLogger().debug("XmlServiceDebug handling attribute " + attributeQName + " with value " + attributeValue);
            }
            
            ChildDataModel childDataModel = nonChildProperties.get(attributeQName);
            if (childDataModel == null) {
                xmlAnyAttributeData.put(attributeQName, attributeValue);
                
                continue;
            }
            if (!Format.ATTRIBUTE.equals(childDataModel.getFormat())) continue;
            
            Class<?> childType = targetModel.getNonChildType(attributeNamespace, attributeName);
            
            if (!childDataModel.isReference()) {
                Object convertedValue = Utilities.getDefaultValue(attributeValue, childType, namespaceMap);
                target._setProperty(attributeNamespace, attributeName, convertedValue);
            }
            else {
                if (DEBUG_PARSING) {
                    Logger.getLogger().debug("XmlServiceDebug attribute " + attributeName + " is a reference");
                }
                
                // Reference
                ReferenceKey rk = new ReferenceKey(childDataModel.getChildType(), attributeValue);
                BaseHK2JAXBBean reference = referenceMap.get(rk);
                if (reference != null) {
                    target._setProperty(attributeNamespace, attributeName, reference);
                }
                else {
                    unresolved.add(new UnresolvedReference(childDataModel.getChildType(), attributeValue, attributeNamespace, attributeName, target));
                }
            }
        }
        
        ChildDataModel childDataModel = nonChildProperties.get(ANY_ATTRIBUTE_QNAME);
        if (childDataModel != null) {
            target._setProperty(ANY_ATTRIBUTE_QNAME, xmlAnyAttributeData);
        }
        
        
        while(reader.hasNext()) {
            int event = reader.next();
            if (DEBUG_PARSING) {
                Logger.getLogger().debug("XmlServiceDebug got xml event (B) " + eventToString(event));
            }
            
            switch(event) {
            case XMLStreamConstants.START_ELEMENT:
                Map<String, String> effectiveNamespaceMap = null;
                {
                    effectiveNamespaceMap = new HashMap<String, String>(namespaceMap);
                    int namespaceCount = reader.getNamespaceCount();
                    for (int nLcv = 0; nLcv < namespaceCount; nLcv++) {
                        String namespacePrefix = reader.getNamespacePrefix(nLcv);
                        String namespaceURI = reader.getNamespaceURI(nLcv);
                        
                        if (namespacePrefix == null) {
                            defaultNamespace = namespaceURI;
                        }
                        else {
                            effectiveNamespaceMap.put(reader.getNamespacePrefix(nLcv), reader.getNamespaceURI(nLcv));
                        }
                    }
                }
                
                String elementTagNamespace = QNameUtilities.getNamespace(reader.getName(), defaultNamespace);
                String elementTag = reader.getName().getLocalPart();
                
                QName elementTagQName = QNameUtilities.createQName(elementTagNamespace, elementTag, defaultNamespace);
                
                if (DEBUG_PARSING) {
                    Logger.getLogger().debug("XmlServiceDebug starting parse of element " + elementTag);
                }
                
                ChildDataModel cdm = nonChildProperties.get(elementTagQName);
                if (cdm != null && Format.ELEMENT.equals(cdm.getFormat())) {
                    String elementValue = advanceNonChildElement(reader, elementTag);
                    
                    Class<?> childType = cdm.getChildTypeAsClass();
                    
                    if (!cdm.isReference()) {
                        if (List.class.equals(childType)) {
                            Class<?> listType = cdm.getChildListTypeAsClass();
                            
                            Object convertedValue = Utilities.getDefaultValue(elementValue, listType, effectiveNamespaceMap);
                            
                            List<Object> listObjects = listNonChild.get(elementTagQName);
                            if (listObjects == null) {
                                listObjects = new LinkedList<Object>();
                                listNonChild.put(elementTagQName, listObjects);
                            }
                            
                            listObjects.add(convertedValue);
                        }
                        else if (childType.isArray() && !byte.class.equals(childType.getComponentType())) {
                            Class<?> aType = childType.getComponentType();
                            
                            Object convertedValue = Utilities.getDefaultValue(elementValue, aType, effectiveNamespaceMap);
                            
                            ArrayInformation ai = arrayNonChild.get(elementTagQName);
                            if (ai == null) {
                                ai = new ArrayInformation(aType);
                                arrayNonChild.put(elementTagQName, ai);
                            }
                            
                            ai.add(convertedValue);
                        }
                        else {
                            Object convertedValue = Utilities.getDefaultValue(elementValue, childType, effectiveNamespaceMap);
                        
                            target._setProperty(elementTagNamespace, elementTag, convertedValue);
                        }
                    }
                    else {
                        ReferenceKey referenceKey = new ReferenceKey(cdm.getChildType(), elementValue);
                        BaseHK2JAXBBean reference = referenceMap.get(referenceKey);
                        
                        if (reference != null) {
                            target._setProperty(elementTagNamespace, elementTag, reference);
                        }
                        else {
                            unresolved.add(new UnresolvedReference(cdm.getChildType(),
                                    elementValue, elementTagNamespace, elementTag, target));
                        }
                    }
                    
                    break;
                }
                
                ParentedModel informedChild = childProperties.get(elementTagQName);
                if (informedChild != null) {
                    ModelImpl grandChild = informedChild.getChildModel();
                    
                    BaseHK2JAXBBean hk2Root = Utilities.createBean(grandChild.getProxyAsClass());
                    hk2Root._setClassReflectionHelper(classReflectionHelper);
                    if (DEBUG_PARSING) {
                        Logger.getLogger().debug("XmlServiceBean created child bean of " + outerElementNamespace + "," + outerElementTag + " with model " + hk2Root._getModel());
                    }
                    
                    handleElement(hk2Root,
                            target,
                            reader,
                            classReflectionHelper,
                            listener,
                            referenceMap,
                            unresolved,
                            outerElementTag,
                            elementTag,
                            effectiveNamespaceMap,
                            defaultNamespace);
                    
                    Object realThing = hk2Root;
                    if (informedChild.getAdapter() != null) {
                        XmlAdapter<Object,Object> adapter = ReflectionHelper.cast(informedChild.getAdapterObject());
                        
                        realThing = adapter.unmarshal(hk2Root);
                    }
                    
                    if (informedChild.getChildType().equals(ChildType.DIRECT)) {
                        target._setProperty(elementTagNamespace, elementTag, realThing);
                    }
                    else if (informedChild.getChildType().equals(ChildType.LIST)) {
                        List<BaseHK2JAXBBean> cList = listChildren.get(elementTagQName);
                        if (cList == null) {
                            cList = new ArrayList<BaseHK2JAXBBean>();
                            listChildren.put(elementTagQName, cList);
                        }
                        cList.add(hk2Root);
                    }
                    else if (informedChild.getChildType().equals(ChildType.ARRAY)) {
                        List<BaseHK2JAXBBean> cList = arrayChildren.get(elementTagQName);
                        if (cList == null) {
                            cList = new LinkedList<BaseHK2JAXBBean>();
                            arrayChildren.put(elementTagQName, cList);
                        }
                        cList.add(hk2Root);
                    }
                    
                    break;
                }
                
                if (allWrappers.contains(elementTag)) {
                    skipWrapperElement(target,
                            parent,
                            reader,
                            classReflectionHelper,
                            listener,
                            referenceMap,
                            unresolved,
                            elementTagNamespace,
                            elementTag,
                            effectiveNamespaceMap,
                            defaultNamespace,
                            elementTag,
                            listChildren,
                            arrayChildren);
                    break;
                }
                
                // If here we have an unknown stanza, just skip it
                if (DEBUG_PARSING) {
                    Logger.getLogger().debug("XmlServiceBean found unknown element in " + outerElementTag + " named " + elementTag + " skipping");
                }
                
                skip(reader, elementTag);
                
                break;
            case XMLStreamConstants.CHARACTERS:
                ChildDataModel valueModel = targetModel.getValueData();
                if (valueModel != null) {
                    String text = reader.getText();
                    
                    Class<?> childType = valueModel.getChildTypeAsClass();
                    
                    String propNamespace = targetModel.getValuePropertyNamespace();
                    String propName = targetModel.getValueProperty();
                    
                    Object convertedValue = Utilities.getDefaultValue(text, childType, namespaceMap);
                    
                    target._setProperty(propNamespace, propName, convertedValue);
                }
                break;
            case XMLStreamConstants.END_ELEMENT:
                for (Map.Entry<QName, List<BaseHK2JAXBBean>> entry : listChildren.entrySet()) {
                    String namespace = QNameUtilities.getNamespace(entry.getKey(), defaultNamespace);
                    String key = entry.getKey().getLocalPart();
                    
                    // Kind of cheating with the erasure, but hey, it works!
                    target._setProperty(namespace, key, entry.getValue());
                }
                
                for (Map.Entry<QName, List<BaseHK2JAXBBean>> entry : arrayChildren.entrySet()) {
                    QName childTag = entry.getKey();
                    String childTagNamespace = QNameUtilities.getNamespace(childTag, defaultNamespace);
                    String childTagKey = childTag.getLocalPart();
                    
                    ParentedModel pn = targetModel.getChild(childTagNamespace, childTagKey);
                    Class<?> childType = pn.getChildModel().getOriginalInterfaceAsClass();
                    
                    List<BaseHK2JAXBBean> individuals = entry.getValue();
                    
                    Object actualArray = Array.newInstance(childType, individuals.size());
                    
                    int index = 0;
                    for (BaseHK2JAXBBean individual : individuals) {
                        Array.set(actualArray, index++, individual);
                    }
                    
                    target._setProperty(childTag, actualArray);
                }
                
                for (Map.Entry<QName, List<Object>> entry : listNonChild.entrySet()) {
                    QName childTag = entry.getKey();
                    List<Object> value = entry.getValue();
                    
                    target._setProperty(childTag, value);
                }
                
                for (Map.Entry<QName, ArrayInformation> entry : arrayNonChild.entrySet()) {
                    QName childTag = entry.getKey();
                    ArrayInformation ai = entry.getValue();
                    
                    Object actualArray = Array.newInstance(ai.getAType(), ai.getValues().size());
                    
                    int lcv = 0;
                    for (Object value : ai.getValues()) {
                        Array.set(actualArray, lcv++, value);
                    }
                    
                    target._setProperty(childTag, actualArray);
                }
                
                listener.afterUnmarshal(target, parent);
                
                // Put the finished product into the reference map
                QName keyProp = target._getKeyPropertyName();
                if (keyProp != null) {
                    String keyVal = (String) target._getProperty(keyProp);
                    String myType = target._getModel().getOriginalInterface();
                    if (keyVal != null && myType != null) {
                        referenceMap.put(new ReferenceKey(myType, keyVal), target);
                    }
                }
                
                if (DEBUG_PARSING) {
                    Logger.getLogger().debug("XmlServiceDebug ending parse of element " + outerElementTag);
                }
                
                return;
            case XMLStreamConstants.COMMENT:
                break;
            default:
                // Do nothing
            }
            
        }
        
        
    }
    
    private static String advanceNonChildElement(XMLStreamReader reader, String outerTag) throws Exception {
        String retVal = null;
        
        while (reader.hasNext()) {
            int nextEvent = reader.next();
            if (DEBUG_PARSING) {
                Logger.getLogger().debug("XmlServiceDebug got xml event (C) " + eventToString(nextEvent));
            }
            
            switch (nextEvent) {
            case XMLStreamConstants.CHARACTERS:
                String text = reader.getText();
                retVal = text.trim();
                
                if (DEBUG_PARSING) {
                    Logger.getLogger().debug("XmlServiceDebug characters of tag " + outerTag + " is " + retVal);
                }
                
                break;
            case XMLStreamConstants.END_ELEMENT:
                if (DEBUG_PARSING) {
                    Logger.getLogger().debug("XmlServiceDebug ending parse of non-child element " + outerTag);
                }
                
                return retVal;
            default:
                // Everything else would be comments or other stuff
                // If not we have a bigger problem
            }
        }
        
        return retVal;
    }
    
    private static <T> void skipWrapperElement(BaseHK2JAXBBean target,
            BaseHK2JAXBBean parent,
            XMLStreamReader reader,
            ClassReflectionHelper classReflectionHelper,
            Listener listener,
            Map<ReferenceKey, BaseHK2JAXBBean> referenceMap,
            List<UnresolvedReference> unresolved,
            String outerElementTagNamespace,
            String outerElementTag,
            Map<String, String> namespaceMap,
            String defaultNamespace,
            String xmlWrapper,
            Map<QName, List<BaseHK2JAXBBean>> listChildren,
            Map<QName, List<BaseHK2JAXBBean>> arrayChildren) throws Exception {
        ModelImpl targetModel = target._getModel();
        Map<QName, ParentedModel> childProperties = targetModel.getChildrenByName();
        
        while (reader.hasNext()) {
            int event = reader.next();
            
            if (DEBUG_PARSING) {
                String name = null;
                
                if (reader.hasName()) {
                    name = reader.getName().getLocalPart();
                }
                
                Logger.getLogger().debug("XmlServiceDebug got xml event (E) " + eventToString(event) + " with name " + name);
            }
            
            switch (event) {
            case XMLStreamConstants.START_ELEMENT:
                Map<String, String> effectiveNamespaceMap = new HashMap<String, String>(namespaceMap);
                int namespaceCount = reader.getNamespaceCount();
                for (int nLcv = 0; nLcv < namespaceCount; nLcv++) {
                    String namespacePrefix = reader.getNamespacePrefix(nLcv);
                    String namespaceURI = reader.getNamespaceURI(nLcv);
                    
                    if (namespacePrefix == null) {
                        defaultNamespace = namespaceURI;
                    }
                    else {
                        effectiveNamespaceMap.put(namespacePrefix, namespaceURI);
                    }
                }
                
                String elementTagNamespace = QNameUtilities.getNamespace(reader.getName(), defaultNamespace);
                String elementTag = reader.getName().getLocalPart();
                
                QName elementTagQName = QNameUtilities.createQName(elementTagNamespace, elementTag, defaultNamespace);
                
                ParentedModel informedChild = childProperties.get(elementTagQName);
                if (informedChild != null && GeneralUtilities.safeEquals(xmlWrapper, informedChild.getXmlWrapperTag())) {
                    ModelImpl grandChild = informedChild.getChildModel();
                    
                    BaseHK2JAXBBean hk2Root = Utilities.createBean(grandChild.getProxyAsClass());
                    hk2Root._setClassReflectionHelper(classReflectionHelper);
                    if (DEBUG_PARSING) {
                        Logger.getLogger().debug("XmlServiceBean created child bean of " + outerElementTagNamespace + "," + outerElementTag + " with model " + hk2Root._getModel());
                    }
                    
                    handleElement(hk2Root,
                            target,
                            reader,
                            classReflectionHelper,
                            listener,
                            referenceMap,
                            unresolved,
                            elementTagNamespace,
                            elementTag,
                            effectiveNamespaceMap,
                            defaultNamespace);
                    
                    if (informedChild.getChildType().equals(ChildType.DIRECT)) {
                        target._setProperty(elementTagNamespace, elementTag, hk2Root);
                    }
                    else if (informedChild.getChildType().equals(ChildType.LIST)) {
                        List<BaseHK2JAXBBean> cList = listChildren.get(elementTagQName);
                        if (cList == null) {
                            cList = new ArrayList<BaseHK2JAXBBean>();
                            listChildren.put(elementTagQName, cList);
                        }
                        cList.add(hk2Root);
                    }
                    else if (informedChild.getChildType().equals(ChildType.ARRAY)) {
                        List<BaseHK2JAXBBean> cList = arrayChildren.get(elementTagQName);
                        if (cList == null) {
                            cList = new LinkedList<BaseHK2JAXBBean>();
                            arrayChildren.put(elementTagQName, cList);
                        }
                        cList.add(hk2Root);
                    }
                    
                }
                break;
            case XMLStreamConstants.END_ELEMENT:
                reader.getName().getLocalPart();
                return;
            default:
                // All others ignored
            }
            
        }
        
    }
    
    private static void skip(XMLStreamReader reader, String skipOverTag) throws Exception {
        while (reader.hasNext()) {
            int event = reader.next();
            if (DEBUG_PARSING) {
                String name = null;
                
                if (reader.hasName()) {
                    name = reader.getName().getLocalPart();
                }
                
                Logger.getLogger().debug("XmlServiceDebug got xml event (D) " + eventToString(event) + " with name " + name);
            }
            
            if (XMLStreamConstants.END_ELEMENT != event) continue;
                
            String elementTag = reader.getName().getLocalPart();
            if (skipOverTag.equals(elementTag)) return;
        }
    }
    
    private static String eventToString(int event) {
        switch (event) {
        case XMLStreamConstants.START_ELEMENT : return "START_ELEMENT" ;
        case XMLStreamConstants.END_ELEMENT : return "END_ELEMENT" ;
        case XMLStreamConstants.PROCESSING_INSTRUCTION : return "PROCESSING_INSTRUCTION" ;
        case XMLStreamConstants.CHARACTERS : return "CHARACTERS" ;
        case XMLStreamConstants.COMMENT : return "COMMENT" ;
        case XMLStreamConstants.SPACE : return "SPACE" ;
        case XMLStreamConstants.START_DOCUMENT : return "START_DOCUMENT" ;
        case XMLStreamConstants.END_DOCUMENT : return "END_DOCUMENT" ;
        case XMLStreamConstants.ENTITY_REFERENCE : return "ENTITY_REFERENCE" ;
        case XMLStreamConstants.ATTRIBUTE : return "ATTRIBUTE" ;
        case XMLStreamConstants.DTD : return "DTD" ;
        case XMLStreamConstants.CDATA : return "CDATA" ;
        case XMLStreamConstants.NAMESPACE : return "NAMESPACE" ;
        case XMLStreamConstants.NOTATION_DECLARATION : return "NOTATION_DECLARATION" ;
        case XMLStreamConstants.ENTITY_DECLARATION : return "ENTITY_DECLARATION" ;
        default : return "UNKNOWN EVENT: " + event;
        }
    }
    
    public static <T> void marshall(OutputStream outputStream, XmlRootHandle<T> root) throws IOException {
        try {
            marshallXmlStream(outputStream, root);
        }
        catch (XMLStreamException xse) {
            throw new IOException(xse);
        }
        
        
    }
    
    private static <T> void marshallXmlStream(OutputStream outputStream, XmlRootHandle<T> root) throws XMLStreamException {
        XMLStreamWriter rawWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(outputStream);
        IndentingXMLStreamWriter indenter = new IndentingXMLStreamWriter(rawWriter);
        try {
            indenter.writeStartDocument();
            
            XmlHk2ConfigurationBean bean = (XmlHk2ConfigurationBean) root.getRoot();
            if (bean != null) {
                marshallElement(indenter, bean, null);
            }
            
            indenter.writeEndDocument();
        }
        finally {
            rawWriter.close();
        }
    }
    
    @SuppressWarnings("unchecked")
    private static void marshallElement(XMLStreamWriter indenter, XmlHk2ConfigurationBean bean, ParentedModel parented) throws XMLStreamException {
        ModelImpl model = bean._getModel();
        Map<String, Object> beanLikeMap = bean._getBeanLikeMap();
        
        String xmlTag;
        if (parented == null) {
            QName rootName = model.getRootName();
            
            // TODO, how do I get the prefix?
            xmlTag = rootName.getLocalPart();
        }
        else {
            xmlTag = parented.getChildXmlTag();
        }
        
        indenter.writeStartElement(xmlTag);
        
        Map<QName, ChildDataModel> attributeModels = model.getAllAttributeChildren();
        for (Map.Entry<QName, ChildDataModel> entry : attributeModels.entrySet()) {
            QName attributeQName = entry.getKey();
            ChildDataModel childDataModel = entry.getValue();
            
            String attributeTagKey = attributeQName.getLocalPart();
            
            Object value = beanLikeMap.get(attributeTagKey);
            if (value == null) continue;
            
            String valueAsString;
            if (!childDataModel.isReference()) {
                valueAsString = value.toString();
            }
            else {
                XmlHk2ConfigurationBean reference = (XmlHk2ConfigurationBean) value;
                valueAsString = reference._getKeyValue();
            }
            
            if (GeneralUtilities.safeEquals(valueAsString, childDataModel.getDefaultAsString())) continue;
            
            indenter.writeAttribute(attributeTagKey, valueAsString);
        }
        
        Map<QName, ChildDescriptor> elementDescriptors = model.getAllElementChildren();
        for (Map.Entry<QName, ChildDescriptor> entry : elementDescriptors.entrySet()) {
            QName elementQName = entry.getKey();
            ChildDescriptor descriptor = entry.getValue();
            
            String elementTagKey = elementQName.getLocalPart();
            
            Object value = beanLikeMap.get(elementTagKey);
            if (value == null) continue;
            
            ParentedModel parentedChild = descriptor.getParentedModel();
            if (parentedChild != null) {
                if (AliasType.HAS_ALIASES.equals(parentedChild.getAliasType())) {
                    continue;
                }
                
                if (ChildType.LIST.equals(parentedChild.getChildType())) {
                    List<XmlHk2ConfigurationBean> asList = (List<XmlHk2ConfigurationBean>) value;
                    if (asList.isEmpty()) continue;
                    
                    for (XmlHk2ConfigurationBean child : asList) {
                        marshallElement(indenter, child, parentedChild);
                    }
                }
                else if (ChildType.ARRAY.equals(parentedChild.getChildType())) {
                    int length = Array.getLength(value);
                    if (length <= 0) continue;
                    
                    for (int lcv = 0; lcv < length; lcv++) {
                        XmlHk2ConfigurationBean child = (XmlHk2ConfigurationBean) Array.get(value, lcv);
                        
                        marshallElement(indenter, child, parentedChild);
                    }
                }
                else {
                    // Direct
                    marshallElement(indenter, (XmlHk2ConfigurationBean) value, parentedChild);
                }
            }
            else {
                ChildDataModel childDataModel = descriptor.getChildDataModel();
                
                if (!childDataModel.isReference()) {
                    if (!value.getClass().isArray()) {
                        String valueAsString = value.toString();
                    
                        indenter.writeStartElement(elementTagKey);
                        indenter.writeCharacters(valueAsString);
                        indenter.writeEndElement();
                    }
                    else {
                        int length = Array.getLength(value);
                        
                        for (int lcv = 0; lcv < length; lcv++) {
                            Object indexedValue = Array.get(value, lcv);
                            
                            String valueAsString = indexedValue.toString();
                            
                            indenter.writeStartElement(elementTagKey);
                            indenter.writeCharacters(valueAsString);
                            indenter.writeEndElement();
                        }
                    }
                }
                else {
                    XmlHk2ConfigurationBean reference = (XmlHk2ConfigurationBean) value;
                    String keyValue = reference._getKeyValue();
                    
                    indenter.writeStartElement(elementTagKey);
                    indenter.writeCharacters(keyValue);
                    indenter.writeEndElement();
                    
                }
            }
        }
        
        indenter.writeEndElement();
    }
    
    private static class ArrayInformation {
        private final Class<?> aType;
        private final List<Object> values = new LinkedList<Object>();
        
        private ArrayInformation(Class<?> aType) {
            this.aType = aType;
        }
        
        private void add(Object addMe) {
            values.add(addMe);
        }
        
        private Class<?> getAType() {
            return aType;
        }
        
        private List<Object> getValues() {
            return values;
        }
        
    }
    
}
