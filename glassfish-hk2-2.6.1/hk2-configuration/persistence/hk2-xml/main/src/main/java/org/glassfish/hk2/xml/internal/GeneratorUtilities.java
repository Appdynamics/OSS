/*
 * Copyright (c) 2017, 2018 Oracle and/or its affiliates. All rights reserved.
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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.namespace.QName;

import org.glassfish.hk2.api.AnnotationLiteral;
import org.glassfish.hk2.xml.api.XmlService;
import org.glassfish.hk2.xml.api.annotations.PluralOf;
import org.glassfish.hk2.xml.internal.alt.AltAnnotation;
import org.glassfish.hk2.xml.internal.alt.AltClass;
import org.glassfish.hk2.xml.internal.alt.AltMethod;
import org.glassfish.hk2.xml.internal.alt.clazz.AnnotationAltAnnotationImpl;

/**
 * @author jwells
 *
 */
public class GeneratorUtilities {
    private final static String XML_VALUE_LOCAL_PART = "##XmlValue";
    public final static String XML_ANY_ATTRIBUTE_LOCAL_PART = "##XmlAnyAttribute";
    
    public static QName convertXmlRootElementName(AltAnnotation root, AltClass clazz) {
        String namespace = root.getStringValue("namespace");
        
        String rootName = root.getStringValue("name");
        
        if (!"##default".equals(rootName)) return QNameUtilities.createQName(namespace, rootName);
        
        String simpleName = clazz.getSimpleName();
        
        char asChars[] = simpleName.toCharArray();
        StringBuffer sb = new StringBuffer();
        
        boolean firstChar = true;
        boolean lastCharWasCapital = false;
        for (char asChar : asChars) {
            if (firstChar) {
                firstChar = false;
                if (Character.isUpperCase(asChar)) {
                    lastCharWasCapital = true;
                    sb.append(Character.toLowerCase(asChar));
                }
                else {
                    lastCharWasCapital = false;
                    sb.append(asChar);
                }
            }
            else {
                if (Character.isUpperCase(asChar)) {
                    if (!lastCharWasCapital) {
                        sb.append('-');
                    }
                    
                    sb.append(Character.toLowerCase(asChar));
                    
                    lastCharWasCapital = true;
                }
                else {
                    sb.append(asChar);
                    
                    lastCharWasCapital = false;
                }
            }
        }
        
        String localPart = sb.toString();
        
        return QNameUtilities.createQName(namespace, localPart);
    }
    
    private static boolean isSpecifiedReference(AltMethod method) {
        AltAnnotation customAnnotation = method.getAnnotation(XmlIDREF.class.getName());
        return (customAnnotation != null);
    }
    
    private static void checkOnlyOne(AltClass convertMe, AltMethod method, AltAnnotation aAnnotation, AltAnnotation bAnnotation) {
        if (aAnnotation != null && bAnnotation != null) {
            throw new IllegalArgumentException("The method " + method + " of " + convertMe + " has both the annotation " + aAnnotation + " and the annotation " +
              bAnnotation + " which is illegal");
        }
    }
    
    public static NameInformation getXmlNameMap(AltClass convertMe) {
        Map<String, XmlElementData> xmlNameMap = new LinkedHashMap<String, XmlElementData>();
        Set<String> unmappedNames = new LinkedHashSet<String>();
        Map<String, String> addMethodToVariableMap = new LinkedHashMap<String, String>();
        Map<String, String> removeMethodToVariableMap = new LinkedHashMap<String, String>();
        Map<String, String> lookupMethodToVariableMap = new LinkedHashMap<String, String>();
        Set<String> referenceSet = new LinkedHashSet<String>();
        Map<String, List<XmlElementData>> aliasMap = new LinkedHashMap<String, List<XmlElementData>>();
        XmlElementData valueData = null;
        XmlElementData xmlAnyAttributeData = null;
        
        boolean hasAnElement = false;
        for (AltMethod originalMethod : convertMe.getMethods()) {
            String originalMethodName = originalMethod.getName();
            
            String setterVariable = Utilities.isSetter(originalMethod);
            if (setterVariable == null) {
                setterVariable = Utilities.isGetter(originalMethod);
                if (setterVariable == null) continue;
            }
            
            if (isSpecifiedReference(originalMethod)) {
                referenceSet.add(setterVariable);
            }
            
            AltAnnotation pluralOf = null;
            AltAnnotation xmlElement = originalMethod.getAnnotation(XmlElement.class.getName());
            AltAnnotation xmlElements = originalMethod.getAnnotation(XmlElements.class.getName());
            AltAnnotation xmlElementWrapper = originalMethod.getAnnotation(XmlElementWrapper.class.getName());
            AltAnnotation xmlAttribute = originalMethod.getAnnotation(XmlAttribute.class.getName());
            AltAnnotation xmlValue = originalMethod.getAnnotation(XmlValue.class.getName());
            AltAnnotation xmlAnyAttribute = originalMethod.getAnnotation(XmlAnyAttribute.class.getName());
            
            String xmlElementWrapperName = (xmlElementWrapper == null) ? null : xmlElementWrapper.getStringValue("name");
            if (xmlElementWrapperName != null && xmlElementWrapperName.isEmpty()) {
                xmlElementWrapperName = setterVariable;
            }
            
            checkOnlyOne(convertMe, originalMethod, xmlElement, xmlElements);
            checkOnlyOne(convertMe, originalMethod, xmlElement, xmlAttribute);
            checkOnlyOne(convertMe, originalMethod, xmlElements, xmlAttribute);
            checkOnlyOne(convertMe, originalMethod, xmlElement, xmlValue);
            checkOnlyOne(convertMe, originalMethod, xmlElements, xmlValue);
            checkOnlyOne(convertMe, originalMethod, xmlAttribute, xmlValue);
            checkOnlyOne(convertMe, originalMethod, xmlElement, xmlAnyAttribute);
            checkOnlyOne(convertMe, originalMethod, xmlElements, xmlAnyAttribute);
            checkOnlyOne(convertMe, originalMethod, xmlAttribute, xmlAnyAttribute);
            checkOnlyOne(convertMe, originalMethod, xmlValue, xmlAnyAttribute);
            
            if (xmlElements != null) {
                hasAnElement = true;
                
                // First add the actual method so it is known to the system
                pluralOf = originalMethod.getAnnotation(PluralOf.class.getName());
                    
                String defaultValue = Generator.JAXB_DEFAULT_DEFAULT;
                    
                xmlNameMap.put(setterVariable, new XmlElementData("",
                        setterVariable,
                        setterVariable,
                        defaultValue,
                        Format.ELEMENT,
                        null,
                        true,
                        xmlElementWrapperName,
                        false,
                        originalMethodName));
                    
                String aliasName = setterVariable;
                    
                AltAnnotation allXmlElements[] = xmlElements.getAnnotationArrayValue("value");
                List<XmlElementData> aliases = new ArrayList<XmlElementData>(allXmlElements.length);
                aliasMap.put(setterVariable, aliases);
                    
                for (AltAnnotation allXmlElement : allXmlElements) {
                    defaultValue = allXmlElement.getStringValue("defaultValue");
                    
                    String allXmlElementNamespace = allXmlElement.getStringValue("namespace");
                    String allXmlElementName = allXmlElement.getStringValue("name");
                    boolean allXmlElementRequired = allXmlElement.getBooleanValue("required");
                    AltClass allXmlElementType = (AltClass) allXmlElement.getAnnotationValues().get("type");
                    String allXmlElementTypeName = (allXmlElementType == null) ? null : allXmlElementType.getName() ;
                    boolean allXmlElementTypeInterface = (allXmlElementType == null) ? true : allXmlElementType.isInterface();
                        
                    if (Generator.JAXB_DEFAULT_STRING.equals(allXmlElementName)) {
                        throw new IllegalArgumentException("The name field of an XmlElement inside an XmlElements must have a specified name");
                    }
                    else {
                        aliases.add(new XmlElementData(
                                allXmlElementNamespace,
                                allXmlElementName,
                                aliasName,
                                defaultValue,
                                Format.ELEMENT,
                                allXmlElementTypeName,
                                allXmlElementTypeInterface,
                                xmlElementWrapperName,
                                allXmlElementRequired,
                                originalMethodName));
                    }
                }
            }
            else if (xmlElement != null) {
                hasAnElement = true;
                
                // Get the pluralOf from the method
                pluralOf = originalMethod.getAnnotation(PluralOf.class.getName());
                    
                String defaultValue = xmlElement.getStringValue("defaultValue");
                
                String namespace = xmlElement.getStringValue("namespace");
                String name = xmlElement.getStringValue("name");
                boolean required = xmlElement.getBooleanValue("required");
                    
                if (Generator.JAXB_DEFAULT_STRING.equals(name)) {
                    xmlNameMap.put(setterVariable, new XmlElementData(
                            namespace,
                            setterVariable,
                            setterVariable,
                            defaultValue,
                            Format.ELEMENT,
                            null,
                            true,
                            xmlElementWrapperName,
                            required,
                            originalMethodName));
                }
                else {
                    xmlNameMap.put(setterVariable, new XmlElementData(
                            namespace,
                            name,
                            name,
                            defaultValue,
                            Format.ELEMENT,
                            null,
                            true,
                            xmlElementWrapperName,
                            required,
                            originalMethodName));
                }
            }
            else if (xmlAttribute != null) {
                String namespace = xmlAttribute.getStringValue("namespace");
                String name = xmlAttribute.getStringValue("name");
                boolean required = xmlAttribute.getBooleanValue("required");
                
                if (Generator.JAXB_DEFAULT_STRING.equals(name)) {
                    xmlNameMap.put(setterVariable, new XmlElementData(
                            namespace,
                            setterVariable,
                            setterVariable,
                            Generator.JAXB_DEFAULT_DEFAULT,
                            Format.ATTRIBUTE,
                            null,
                            true,
                            xmlElementWrapperName,
                            required,
                            originalMethodName));
                }
                else {
                    xmlNameMap.put(setterVariable, new XmlElementData(
                            namespace,
                            name,
                            name,
                            Generator.JAXB_DEFAULT_DEFAULT,
                            Format.ATTRIBUTE,
                            null,
                            true,
                            xmlElementWrapperName,
                            required,
                            originalMethodName));
                }
            }
            else if (xmlValue != null) {
                if (valueData != null) {
                    throw new IllegalArgumentException("There may be only one XmlValue method on " + convertMe);
                }
                
                valueData = new XmlElementData(
                        XmlService.DEFAULT_NAMESPACE,
                        XML_VALUE_LOCAL_PART,
                        XML_VALUE_LOCAL_PART,
                        null,
                        Format.VALUE,
                        null,
                        false,
                        xmlElementWrapperName,
                        true,
                        originalMethodName);
                xmlNameMap.put(setterVariable, valueData);
            }
            else if (xmlAnyAttribute != null) {
                if (xmlAnyAttributeData != null) {
                    throw new IllegalArgumentException("There may be only one XmlAnyAttribute method on " + convertMe);
                }
                
                xmlAnyAttributeData = new XmlElementData(
                        XmlService.DEFAULT_NAMESPACE,
                        XML_ANY_ATTRIBUTE_LOCAL_PART,
                        XML_ANY_ATTRIBUTE_LOCAL_PART,
                        null,
                        Format.ATTRIBUTE,
                        null,
                        false,
                        xmlElementWrapperName,
                        false,
                        originalMethodName);
                xmlNameMap.put(setterVariable, xmlAnyAttributeData);       
            }
            else {
                unmappedNames.add(setterVariable);
            }
            
            if (pluralOf == null) pluralOf = new AnnotationAltAnnotationImpl(new PluralOfDefault(), null);
            
            String unDecapitalizedVariable = originalMethod.getName().substring(3);
            
            addMethodToVariableMap.put(getMethodName(MethodType.ADD, unDecapitalizedVariable, pluralOf), setterVariable);
            removeMethodToVariableMap.put(getMethodName(MethodType.REMOVE, unDecapitalizedVariable, pluralOf), setterVariable);
            lookupMethodToVariableMap.put(getMethodName(MethodType.LOOKUP, unDecapitalizedVariable, pluralOf), setterVariable);
        }
        
        if (valueData != null && hasAnElement) {
            throw new IllegalArgumentException("A bean cannot both have XmlElements and XmlValue methods in " + convertMe);
        }
        
        Set<String> noXmlElementNames = new LinkedHashSet<String>();
        for (String unmappedName : unmappedNames) {
            if (!xmlNameMap.containsKey(unmappedName)) {
                noXmlElementNames.add(unmappedName);
            }
        }
        
        return new NameInformation(xmlNameMap, noXmlElementNames,
                addMethodToVariableMap,
                removeMethodToVariableMap,
                lookupMethodToVariableMap,
                referenceSet,
                aliasMap,
                valueData);
    }
    
    private static String getMethodName(MethodType methodType, String unDecapitalizedVariable, AltAnnotation instructions) {
        String retVal;
        
        switch (methodType) {
            case ADD:
                retVal = instructions.getStringValue("add");
                break;
            case REMOVE:
                retVal = instructions.getStringValue("remove");
                break;
            case LOOKUP:
                retVal = instructions.getStringValue("lookup");
                break;
            default:
                throw new AssertionError("Only ADD, REMOVE and LOOKUP supported");
        }
        
        if (!PluralOf.USE_NORMAL_PLURAL_PATTERN.equals(retVal)) {
            // We got the specific name for the method, overrides any algorithm
            return retVal;
        }
        
        String pluralOf = instructions.getStringValue("value");
        if (!PluralOf.USE_NORMAL_PLURAL_PATTERN.equals(pluralOf)) {
            // We got a specific name for the singular, use it
            switch (methodType) {
            case ADD:
                return JAUtilities.ADD + pluralOf;
            case REMOVE:
                return JAUtilities.REMOVE + pluralOf;
            case LOOKUP:
                return JAUtilities.LOOKUP + pluralOf;
            default:
                throw new AssertionError("Only add, remove and lookup supported");
            }
        }
        
        // Do the algorithm
        if (unDecapitalizedVariable.endsWith("s")) {
            unDecapitalizedVariable = unDecapitalizedVariable.substring(0, unDecapitalizedVariable.length() - 1);
        }
        
        switch (methodType) {
        case ADD:
            return JAUtilities.ADD + unDecapitalizedVariable;
        case REMOVE:
            return JAUtilities.REMOVE + unDecapitalizedVariable;
        case LOOKUP:
            return JAUtilities.LOOKUP + unDecapitalizedVariable;
        default:
            throw new AssertionError("Only add, remove and lookup supported");
        }
        
    }
    
    private static final class PluralOfDefault extends AnnotationLiteral<PluralOf> implements PluralOf {
        private static final long serialVersionUID = 4358923840720264176L;

        /* (non-Javadoc)
         * @see org.glassfish.hk2.xml.api.annotations.PluralOf#value()
         */
        @Override
        public String value() {
            return PluralOf.USE_NORMAL_PLURAL_PATTERN;
        }

        /* (non-Javadoc)
         * @see org.glassfish.hk2.xml.api.annotations.PluralOf#add()
         */
        @Override
        public String add() {
            return PluralOf.USE_NORMAL_PLURAL_PATTERN;
        }

        /* (non-Javadoc)
         * @see org.glassfish.hk2.xml.api.annotations.PluralOf#remove()
         */
        @Override
        public String remove() {
            return PluralOf.USE_NORMAL_PLURAL_PATTERN;
        }

        /* (non-Javadoc)
         * @see org.glassfish.hk2.xml.api.annotations.PluralOf#lookup()
         */
        @Override
        public String lookup() {
            return PluralOf.USE_NORMAL_PLURAL_PATTERN;
        }
        
    }

}
