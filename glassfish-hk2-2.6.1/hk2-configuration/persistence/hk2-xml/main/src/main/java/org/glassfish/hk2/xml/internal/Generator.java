/*
 * Copyright (c) 2015, 2018 Oracle and/or its affiliates. All rights reserved.
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

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.SignatureAttribute;
import javassist.bytecode.SignatureAttribute.ClassType;
import javassist.bytecode.SignatureAttribute.MethodSignature;
import javassist.bytecode.SignatureAttribute.TypeArgument;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.AnnotationMemberValue;
import javassist.bytecode.annotation.ArrayMemberValue;
import javassist.bytecode.annotation.BooleanMemberValue;
import javassist.bytecode.annotation.ByteMemberValue;
import javassist.bytecode.annotation.CharMemberValue;
import javassist.bytecode.annotation.ClassMemberValue;
import javassist.bytecode.annotation.DoubleMemberValue;
import javassist.bytecode.annotation.EnumMemberValue;
import javassist.bytecode.annotation.FloatMemberValue;
import javassist.bytecode.annotation.IntegerMemberValue;
import javassist.bytecode.annotation.LongMemberValue;
import javassist.bytecode.annotation.MemberValue;
import javassist.bytecode.annotation.ShortMemberValue;
import javassist.bytecode.annotation.StringMemberValue;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;

import org.glassfish.hk2.api.MultiException;
import org.glassfish.hk2.utilities.reflection.Logger;
import org.glassfish.hk2.xml.api.annotations.Hk2XmlPreGenerate;
import org.glassfish.hk2.xml.internal.alt.AdapterInformation;
import org.glassfish.hk2.xml.internal.alt.AltAnnotation;
import org.glassfish.hk2.xml.internal.alt.AltClass;
import org.glassfish.hk2.xml.internal.alt.AltEnum;
import org.glassfish.hk2.xml.internal.alt.AltMethod;
import org.glassfish.hk2.xml.internal.alt.MethodInformationI;
import org.glassfish.hk2.xml.internal.alt.clazz.AnnotationAltAnnotationImpl;
import org.glassfish.hk2.xml.internal.alt.clazz.ClassAltClassImpl;
import org.glassfish.hk2.xml.jaxb.internal.XmlElementImpl;
import org.glassfish.hk2.xml.jaxb.internal.XmlRootElementImpl;
import org.jvnet.hk2.annotations.Contract;

/**
 * @author jwells
 *
 */
public class Generator {
    private final static boolean DEBUG_METHODS = AccessController.doPrivileged(new PrivilegedAction<Boolean>() {
        @Override
        public Boolean run() {
            return Boolean.parseBoolean(
                System.getProperty("org.jvnet.hk2.xmlservice.compiler.methods", "false"));
        }
            
    });
    
    public final static String JAXB_DEFAULT_STRING = "##default";
    public final static String JAXB_DEFAULT_DEFAULT = "\u0000";
    public final static String NO_CHILD_PACKAGE = "java.";
    public final static String STATIC_GET_MODEL_METHOD_NAME = "__getModel";
    private final static String QUOTE = "\"";
    
    private final static Set<String> NO_COPY_ANNOTATIONS = new HashSet<String>(Arrays.asList(new String[] {
            Contract.class.getName(),
            XmlTransient.class.getName(),
            Hk2XmlPreGenerate.class.getName(),
            "org.jvnet.hk2.config.Configured",
    }));
    
    private final static Set<String> NO_COPY_ANNOTATIONS_METHOD = new HashSet<String>(Arrays.asList(new String[] {
            // Done as a String so that javax.annotation need not be on compiler classpath
            "javax.validation.Valid"
    }));
    
    /**
     * Converts the given interface into a JAXB implementation proxy
     * 
     * @param convertMe
     * @param superClazz
     * @param defaultClassPool
     * @return
     * @throws Throwable
     */
    public static CtClass generate(AltClass convertMe,
            CtClass superClazz,
            ClassPool defaultClassPool) throws Throwable {
        String modelOriginalInterface = convertMe.getName();
        
        String modelTranslatedClass = Utilities.getProxyNameFromInterfaceName(modelOriginalInterface);
        if (DEBUG_METHODS) {
            Logger.getLogger().debug("Converting " + convertMe.getName() + " to " + modelTranslatedClass);
        }
        
        CtClass targetCtClass = defaultClassPool.makeClass(modelTranslatedClass);
        ClassFile targetClassFile = targetCtClass.getClassFile();
        targetClassFile.setVersionToJava5();
        ConstPool targetConstPool = targetClassFile.getConstPool();
        
        ModelImpl compiledModel = new ModelImpl(modelOriginalInterface, modelTranslatedClass);
        AnnotationsAttribute ctAnnotations = null;
        String propOrder[] = null;
        for (AltAnnotation convertMeAnnotation : convertMe.getAnnotations()) {
            if (NO_COPY_ANNOTATIONS.contains(convertMeAnnotation.annotationType())) {
                // We do NOT want the generated class to be in the set of contracts, so
                // skip this one if it is there.
                // We also DO want our own class to be processed by JAXB even
                // if the interface is not.  This is needed for the Eclipselink
                // Moxy version of JAXB, which does some processing of interfaces
                // we do not want them to do
                continue;
            }
            
            if (ctAnnotations == null) {
                ctAnnotations = new AnnotationsAttribute(targetConstPool, AnnotationsAttribute.visibleTag);
            }
            
            if (XmlRootElement.class.getName().equals(convertMeAnnotation.annotationType())) {
                QName modelRootName = GeneratorUtilities.convertXmlRootElementName(convertMeAnnotation, convertMe);
                compiledModel.setRootName(modelRootName);
                
                String modelRootNameNamespace = QNameUtilities.getNamespace(modelRootName);
                String modelRootNameKey = modelRootName.getLocalPart();
                
                XmlRootElement replacement = new XmlRootElementImpl(modelRootNameNamespace, modelRootNameKey);
               
                createAnnotationCopy(targetConstPool, replacement, ctAnnotations);
            }
            else {
                createAnnotationCopy(targetConstPool, convertMeAnnotation, ctAnnotations);
            }
            
            if (XmlType.class.getName().equals(convertMeAnnotation.annotationType())) {
                propOrder = convertMeAnnotation.getStringArrayValue("propOrder");
            }
        }
        if (ctAnnotations != null) {
            targetClassFile.addAttribute(ctAnnotations);
        }
        
        CtClass originalCtClass = defaultClassPool.getOrNull(convertMe.getName());
        if (originalCtClass == null) {
            originalCtClass = defaultClassPool.makeInterface(convertMe.getName());
        }
        
        targetCtClass.setSuperclass(superClazz);
        targetCtClass.addInterface(originalCtClass);
        
        NameInformation xmlNameMap = GeneratorUtilities.getXmlNameMap(convertMe);
        Set<QName> alreadyAddedNaked = new LinkedHashSet<QName>();
        
        List<AltMethod> allMethods = convertMe.getMethods();
        if (DEBUG_METHODS) {
            Logger.getLogger().debug("Analyzing " + allMethods.size() + " methods of " + convertMe.getName());
        }
        
        allMethods = Utilities.prioritizeMethods(allMethods, propOrder, xmlNameMap);
        
        Set<String> setters = new LinkedHashSet<String>();
        Map<String, MethodInformationI> getters = new LinkedHashMap<String, MethodInformationI>();
        Map<String, GhostXmlElementData> elementsMethods = new LinkedHashMap<String, GhostXmlElementData>();
        for (AltMethod wrapper : allMethods) {
            MethodInformationI mi = Utilities.getMethodInformation(wrapper, xmlNameMap);
            if (mi.isKey()) {
                compiledModel.setKeyProperty(mi.getRepresentedProperty());
            }
            
            String miRepPropNamespace = QNameUtilities.getNamespace(mi.getRepresentedProperty());
            String miRepProp = (mi.getRepresentedProperty() == null) ? null : mi.getRepresentedProperty().getLocalPart();
            
            if (!MethodType.CUSTOM.equals(mi.getMethodType())) {
                createInterfaceForAltClassIfNeeded(mi.getGetterSetterType(), defaultClassPool);
            }
            else {
                // Custom methods can have all sorts of missing types, need to do all return and param types
                AltMethod original = mi.getOriginalMethod();
                AltClass originalReturn = original.getReturnType();
                if (!ClassAltClassImpl.VOID.equals(originalReturn)) {
                    createInterfaceForAltClassIfNeeded(originalReturn, defaultClassPool);
                }
                
                for (AltClass parameter : original.getParameterTypes()) {
                    createInterfaceForAltClassIfNeeded(parameter, defaultClassPool);
                }
                
            }
            
            if (DEBUG_METHODS) {
                Logger.getLogger().debug("Analyzing method " + mi + " of " + convertMe.getSimpleName());
            }
            
            String name = wrapper.getName();
            
            StringBuffer sb = new StringBuffer("public ");
            
            AltClass originalRetType = wrapper.getReturnType();
            boolean isVoid;
            if (originalRetType == null || void.class.getName().equals(originalRetType.getName())) {
                sb.append("void ");
                isVoid = true;
            }
            else {
                sb.append(getCompilableClass(originalRetType) + " ");
                isVoid = false;
            }
            
            sb.append(name + "(");
            
            AltClass childType = null;
            boolean getterOrSetter = false;
            boolean isReference = false;
            boolean isSetter = false;
            if (MethodType.SETTER.equals(mi.getMethodType())) {
                getterOrSetter = true;
                isSetter = true;
                setters.add(mi.getRepresentedProperty().getLocalPart());
                
                childType = mi.getBaseChildType();
                
                isReference = mi.isReference();
                
                sb.append(getCompilableClass(mi.getGetterSetterType()) +
                        " arg0) { super._setProperty(\"" + miRepPropNamespace + "\",\"" + miRepProp + "\", arg0); }");
            }
            else if (MethodType.GETTER.equals(mi.getMethodType())) {
                getterOrSetter = true;
                getters.put(mi.getRepresentedProperty().getLocalPart(), mi);
                
                childType = mi.getBaseChildType();
                
                isReference = mi.isReference();
                
                String cast = "";
                String superMethodName = "_getProperty";
                if (int.class.getName().equals(mi.getGetterSetterType().getName())) {
                    superMethodName += "I"; 
                }
                else if (long.class.getName().equals(mi.getGetterSetterType().getName())) {
                    superMethodName += "J";
                }
                else if (boolean.class.getName().equals(mi.getGetterSetterType().getName())) {
                    superMethodName += "Z";
                }
                else if (byte.class.getName().equals(mi.getGetterSetterType().getName())) {
                    superMethodName += "B";
                }
                else if (char.class.getName().equals(mi.getGetterSetterType().getName())) {
                    superMethodName += "C";
                }
                else if (short.class.getName().equals(mi.getGetterSetterType().getName())) {
                    superMethodName += "S";
                }
                else if (float.class.getName().equals(mi.getGetterSetterType().getName())) {
                    superMethodName += "F";
                }
                else if (double.class.getName().equals(mi.getGetterSetterType().getName())) {
                    superMethodName += "D";
                }
                else {
                    cast = "(" + getCompilableClass(mi.getGetterSetterType()) + ") ";
                }
                
                sb.append(") { return " + cast + "super." + superMethodName + "(\"" + miRepPropNamespace + "\",\"" + miRepProp + "\"); }");
            }
            else if (MethodType.LOOKUP.equals(mi.getMethodType())) {
                sb.append("java.lang.String arg0) { return (" + getCompilableClass(originalRetType) +
                        ") super._lookupChild(\"" + miRepPropNamespace + "\",\"" + miRepProp + "\", arg0); }");
                
            }
            else if (MethodType.ADD.equals(mi.getMethodType())) {
                String returnClause = "";
                if (!isVoid) {
                    createInterfaceForAltClassIfNeeded(originalRetType, defaultClassPool);
                    
                    returnClause = "return (" + getCompilableClass(originalRetType) + ") ";
                }
                
                List<AltClass> paramTypes = wrapper.getParameterTypes();
                if (paramTypes.size() == 0) {
                    sb.append(") { " + returnClause + "super._doAdd(\"" + miRepPropNamespace + "\",\"" + miRepProp + "\", null, null, -1); }");
                }
                else if (paramTypes.size() == 1) {
                    createInterfaceForAltClassIfNeeded(paramTypes.get(0), defaultClassPool);
                    
                    sb.append(paramTypes.get(0).getName() + " arg0) { " + returnClause + "super._doAdd(\"" + miRepPropNamespace + "\",\"" + miRepProp + "\",");
                    
                    if (paramTypes.get(0).isInterface()) {
                        sb.append("arg0, null, -1); }");
                    }
                    else if (String.class.getName().equals(paramTypes.get(0).getName())) {
                        sb.append("null, arg0, -1); }");
                    }
                    else {
                        sb.append("null, null, arg0); }");
                    }
                }
                else {
                    sb.append(paramTypes.get(0).getName() + " arg0, int arg1) { " + returnClause + "super._doAdd(\"" + miRepPropNamespace + "\",\"" + miRepProp + "\",");
                    
                    if (paramTypes.get(0).isInterface()) {
                        createInterfaceForAltClassIfNeeded(paramTypes.get(0), defaultClassPool);
                        
                        sb.append("arg0, null, arg1); }");
                    }
                    else {
                        sb.append("null, arg0, arg1); }");
                    }
                }
            }
            else if (MethodType.REMOVE.equals(mi.getMethodType())) {
                List<AltClass> paramTypes = wrapper.getParameterTypes();
                String cast = "";
                String function = "super._doRemoveZ(\"";
                String doReturn = "return";
                if (!boolean.class.getName().equals(originalRetType.getName())) {
                    if (void.class.getName().equals(originalRetType.getName())) {
                        doReturn = "";
                    }
                    else {
                        cast = "(" + getCompilableClass(originalRetType) + ") ";
                        
                    }
                    
                    function = "super._doRemove(\"";
                }
                
                if (paramTypes.size() == 0) {
                    sb.append(") { " + doReturn + " " + cast + function +
                            miRepPropNamespace + "\",\"" + miRepProp + "\", null, -1, null); }");
                }
                else if (String.class.getName().equals(paramTypes.get(0).getName())) {
                    sb.append("java.lang.String arg0) { " + doReturn + " " + cast  + function +
                            miRepPropNamespace + "\",\"" + miRepProp + "\", arg0, -1, null); }");
                }
                else if (int.class.getName().equals(paramTypes.get(0).getName())) {
                    sb.append("int arg0) { " + doReturn + " " + cast + function +
                            miRepPropNamespace + "\",\"" + miRepProp + "\", null, arg0, null); }");
                }
                else {
                    sb.append(paramTypes.get(0).getName() + " arg0) { " + doReturn + " " + cast + function +
                            miRepPropNamespace + "\",\"" + miRepProp + "\", null, -1, arg0); }");
                }
            }
            else if (MethodType.CUSTOM.equals(mi.getMethodType())) {
                List<AltClass> paramTypes = wrapper.getParameterTypes();
                
                StringBuffer classSets = new StringBuffer();
                StringBuffer valSets = new StringBuffer();
                
                int lcv = 0;
                for (AltClass paramType : paramTypes) {
                    createInterfaceForAltClassIfNeeded(paramType, defaultClassPool);
                    
                    if (lcv == 0) {
                        sb.append(getCompilableClass(paramType) + " arg" + lcv);
                    }
                    else {
                        sb.append(", " + getCompilableClass(paramType) + " arg" + lcv);
                    }
                    
                    classSets.append("mParams[" + lcv + "] = " + getCompilableClass(paramType) + ".class;\n");
                    valSets.append("mVars[" + lcv + "] = ");
                    if (int.class.getName().equals(paramType.getName())) {
                        valSets.append("new java.lang.Integer(arg" + lcv + ");\n");
                    }
                    else if (long.class.getName().equals(paramType.getName())) {
                        valSets.append("new java.lang.Long(arg" + lcv + ");\n");
                    }
                    else if (boolean.class.getName().equals(paramType.getName())) {
                        valSets.append("new java.lang.Boolean(arg" + lcv + ");\n");
                    }
                    else if (byte.class.getName().equals(paramType.getName())) {
                        valSets.append("new java.lang.Byte(arg" + lcv + ");\n");
                    }
                    else if (char.class.getName().equals(paramType.getName())) {
                        valSets.append("new java.lang.Character(arg" + lcv + ");\n");
                    }
                    else if (short.class.getName().equals(paramType.getName())) {
                        valSets.append("new java.lang.Short(arg" + lcv + ");\n");
                    }
                    else if (float.class.getName().equals(paramType.getName())) {
                        valSets.append("new java.lang.Float(arg" + lcv + ");\n");
                    }
                    else if (double.class.getName().equals(paramType.getName())) {
                        valSets.append("new java.lang.Double(arg" + lcv + ");\n");
                    }
                    else {
                        valSets.append("arg" + lcv + ";\n");
                    }
                    
                    lcv++;
                }
                
                sb.append(") { Class[] mParams = new Class[" + paramTypes.size() + "];\n");
                sb.append("Object[] mVars = new Object[" + paramTypes.size() + "];\n");
                sb.append(classSets.toString());
                sb.append(valSets.toString());
                
                String cast = "";
                String superMethodName = "_invokeCustomizedMethod";
                if (int.class.getName().equals(originalRetType.getName())) {
                    superMethodName += "I"; 
                }
                else if (long.class.getName().equals(originalRetType.getName())) {
                    superMethodName += "J";
                }
                else if (boolean.class.getName().equals(originalRetType.getName())) {
                    superMethodName += "Z";
                }
                else if (byte.class.getName().equals(originalRetType.getName())) {
                    superMethodName += "B";
                }
                else if (char.class.getName().equals(originalRetType.getName())) {
                    superMethodName += "C";
                }
                else if (short.class.getName().equals(originalRetType.getName())) {
                    superMethodName += "S";
                }
                else if (float.class.getName().equals(originalRetType.getName())) {
                    superMethodName += "F";
                }
                else if (double.class.getName().equals(originalRetType.getName())) {
                    superMethodName += "D";
                }
                else if (!isVoid) {
                    cast = "(" + getCompilableClass(originalRetType) + ") ";
                }
                
                if (!isVoid) {
                    sb.append("return " + cast);
                }
                sb.append("super." + superMethodName + "(\"" + name + "\", mParams, mVars);}");
            }
            else {
                throw new AssertionError("Unknown method type: " + mi.getMethodType());
            }
            
            if (DEBUG_METHODS) {
                // Hidden behind static because of potential expensive toString costs
                Logger.getLogger().debug("Adding method for " + convertMe.getSimpleName() + " with implementation " + sb);
            }
            
            CtMethod addMeCtMethod;
            try {
                addMeCtMethod = CtNewMethod.make(sb.toString(), targetCtClass);
            }
            catch (CannotCompileException cce) {
                MultiException me = new MultiException(cce);
                me.addError(new AssertionError("Cannot compile method with source " + sb.toString()));
                throw me;
            }
            if (wrapper.isVarArgs()) {
                addMeCtMethod.setModifiers(addMeCtMethod.getModifiers() | Modifier.VARARGS);
            }
            
            if (mi.getListParameterizedType() != null) {
                addListGenericSignature(addMeCtMethod, mi.getListParameterizedType(), isSetter);
            }
            
            MethodInfo methodInfo = addMeCtMethod.getMethodInfo();
            ConstPool methodConstPool = methodInfo.getConstPool();
           
            ctAnnotations = null;
            for (AltAnnotation convertMeAnnotation : wrapper.getAnnotations()) {
                if (NO_COPY_ANNOTATIONS_METHOD.contains(convertMeAnnotation.annotationType())) {
                    continue;
                }
                
                if (ctAnnotations == null) {
                    ctAnnotations = new AnnotationsAttribute(methodConstPool, AnnotationsAttribute.visibleTag);
                }
                
                if ((childType != null) && XmlElement.class.getName().equals(convertMeAnnotation.annotationType())) {
                        
                    String translatedClassName = Utilities.getProxyNameFromInterfaceName(childType.getName());
                    java.lang.annotation.Annotation anno = new XmlElementImpl(
                            convertMeAnnotation.getStringValue("name"),
                            convertMeAnnotation.getBooleanValue("nillable"),
                            convertMeAnnotation.getBooleanValue("required"),
                            convertMeAnnotation.getStringValue("namespace"),
                            convertMeAnnotation.getStringValue("defaultValue"),
                            translatedClassName);
                    
                    createAnnotationCopy(methodConstPool, anno, ctAnnotations);
                }
                else if (getterOrSetter && XmlElements.class.getName().equals(convertMeAnnotation.annotationType())) {
                    QName representedProperty = mi.getRepresentedProperty();
                    
                    AltAnnotation elements[] = convertMeAnnotation.getAnnotationArrayValue("value");
                    if (elements == null) elements = new AltAnnotation[0];
                    
                    elementsMethods.put(representedProperty.getLocalPart(), new GhostXmlElementData(elements, mi.getGetterSetterType()));
                    
                    // Note the XmlElements is NOT copied to the proxy, the ghost methods will get the XmlElements
                }
                else {  
                    createAnnotationCopy(methodConstPool, convertMeAnnotation, ctAnnotations);
                }
            }
            
            if (getterOrSetter) {
                String originalMethodName = mi.getOriginalMethodName();
                List<XmlElementData> aliases = xmlNameMap.getAliases(mi.getRepresentedProperty().getLocalPart());
                boolean required = mi.isRequired();
                
                if ((childType != null) || (aliases != null)) {
                    if (!isReference) {
                        AliasType aType = (aliases == null) ? AliasType.NORMAL : AliasType.HAS_ALIASES ;
                        AltClass useChildType = (childType == null) ? mi.getListParameterizedType() : childType ;
                        AdapterInformation adapterInformation = mi.getAdapterInformation();
                        String adapterClass = (adapterInformation == null) ? null : adapterInformation.getAdapter().getName();
                        
                        if (useChildType.isInterface()) {
                            compiledModel.addChild(
                                useChildType.getName(),
                                QNameUtilities.getNamespace(mi.getRepresentedProperty()),
                                mi.getRepresentedProperty().getLocalPart(),
                                mi.getRepresentedProperty().getLocalPart(),
                                getChildType(mi.isList(), mi.isArray()),
                                mi.getDefaultValue(),
                                aType,
                                mi.getWrapperTag(),
                                adapterClass,
                                required,
                                originalMethodName);
                        }
                        else {
                            compiledModel.addNonChild(
                                    QNameUtilities.getNamespace(mi.getRepresentedProperty()),
                                    mi.getRepresentedProperty().getLocalPart(),
                                    mi.getDefaultValue(),
                                    mi.getGetterSetterType().getName(),
                                    mi.getListParameterizedType().getName(),
                                    false,
                                    Format.ELEMENT,
                                    aType,
                                    null,
                                    required,
                                    originalMethodName);
                        }
                        
                        if (aliases != null) {
                            for (XmlElementData alias : aliases) {
                                String aliasType = alias.getType();
                                if (aliasType == null) aliasType = useChildType.getName();
                                
                                if (alias.isTypeInterface()) {
                                    compiledModel.addChild(aliasType,
                                        alias.getNamespace(),
                                        alias.getName(),
                                        alias.getAlias(),
                                        getChildType(mi.isList(), mi.isArray()),
                                        alias.getDefaultValue(),
                                        AliasType.IS_ALIAS,
                                        mi.getWrapperTag(),
                                        adapterClass,
                                        required,
                                        originalMethodName);
                                }
                                else {
                                    compiledModel.addNonChild(
                                            alias.getNamespace(),
                                            alias.getName(),
                                            alias.getDefaultValue(),
                                            mi.getGetterSetterType().getName(),
                                            aliasType,
                                            false,
                                            Format.ELEMENT,
                                            AliasType.IS_ALIAS,
                                            alias.getAlias(),
                                            required,
                                            originalMethodName);
                                }
                            }
                        }
                    }
                    else {
                        // Is a reference
                        String listPType = (mi.getListParameterizedType() == null) ? null : mi.getListParameterizedType().getName() ;
                        compiledModel.addNonChild(
                                mi.getRepresentedProperty(),
                                mi.getDefaultValue(),
                                mi.getGetterSetterType().getName(),
                                listPType,
                                true,
                                mi.getFormat(),
                                AliasType.NORMAL,
                                null,
                                required,
                                originalMethodName);
                    }
                }
                else {
                    String listPType = (mi.getListParameterizedType() == null) ? null : mi.getListParameterizedType().getName() ;
                    compiledModel.addNonChild(
                            mi.getRepresentedProperty(),
                            mi.getDefaultValue(),
                            mi.getGetterSetterType().getName(),
                            listPType,
                            false,
                            mi.getFormat(),
                            AliasType.NORMAL,
                            null,
                            required,
                            originalMethodName);
                }
            }
            
            if (getterOrSetter && childType != null &&
                    xmlNameMap.hasNoXmlElement(mi.getRepresentedProperty().getLocalPart()) &&
                    !alreadyAddedNaked.contains(mi.getRepresentedProperty())) {
                alreadyAddedNaked.add(mi.getRepresentedProperty());
                if (ctAnnotations == null) {
                    ctAnnotations = new AnnotationsAttribute(methodConstPool, AnnotationsAttribute.visibleTag);
                }
                
                java.lang.annotation.Annotation convertMeAnnotation;
                String translatedClassName = Utilities.getProxyNameFromInterfaceName(childType.getName());
                convertMeAnnotation = new XmlElementImpl(
                        JAXB_DEFAULT_STRING,
                        false,
                        false,
                        JAXB_DEFAULT_STRING,
                        JAXB_DEFAULT_DEFAULT,
                        translatedClassName);
                
                if (DEBUG_METHODS) {
                    Logger.getLogger().debug("Adding ghost XmlElement for " + convertMe.getSimpleName() + " with data " + convertMeAnnotation);
                }
                
                createAnnotationCopy(methodConstPool, convertMeAnnotation, ctAnnotations);
            }
            
            if (ctAnnotations != null) {
                methodInfo.addAttribute(ctAnnotations);
            }
            
            targetCtClass.addMethod(addMeCtMethod);
        }
        
        // Now generate the invisible setters for JAXB
        for (Map.Entry<String, MethodInformationI> getterEntry : getters.entrySet()) {
            String getterProperty = getterEntry.getKey();
            MethodInformationI mi = getterEntry.getValue();
            
            String miRepPropNamespace = QNameUtilities.getNamespace(mi.getRepresentedProperty());
            String miRepProp = mi.getRepresentedProperty().getLocalPart();
            
            if (setters.contains(getterProperty)) continue;
            
            String getterName = mi.getOriginalMethod().getName();
            String setterName = Utilities.convertToSetter(getterName);
            
            StringBuffer sb = new StringBuffer("private void " + setterName + "(");
            sb.append(getCompilableClass(mi.getGetterSetterType()) + " arg0) { super._setProperty(\"" + miRepPropNamespace + "\",\"" + miRepProp + "\", arg0); }");
            
            CtMethod addMeCtMethod = CtNewMethod.make(sb.toString(), targetCtClass);
            targetCtClass.addMethod(addMeCtMethod);
            
            if (mi.getListParameterizedType() != null) {
                addListGenericSignature(addMeCtMethod, mi.getListParameterizedType(), true);
            }
            
            if (DEBUG_METHODS) {
                Logger.getLogger().debug("Adding ghost setter method for " + convertMe.getSimpleName() + " with implementation " + sb);
            }
        }
        
        int elementCount = 0;
        for (Map.Entry<String, GhostXmlElementData> entry : elementsMethods.entrySet()) {
            String basePropName = entry.getKey();
            GhostXmlElementData gxed = entry.getValue();
            
            AltAnnotation[] xmlElements = gxed.xmlElements;
            
            String baseSetSetterName = "set_" + basePropName;
            String baseGetterName = "get_" + basePropName;
            
            for (AltAnnotation xmlElement : xmlElements) {
                String elementName = xmlElement.getStringValue("name");
                String elementNamespace = xmlElement.getStringValue("namespace");
                
                String ghostMethodName = baseSetSetterName + "_" + elementCount;
                String ghostMethodGetName = baseGetterName + "_" + elementCount;
                elementCount++;
                
                AltClass ac = (AltClass) xmlElement.getAnnotationValues().get("type");
                
                {
                    // Create the setter
                    
                    StringBuffer ghostBufferSetter = new StringBuffer("private void " + ghostMethodName + "(");
                    ghostBufferSetter.append(getCompilableClass(gxed.getterSetterType) +
                        " arg0) { super._setProperty(\"" + elementNamespace + "\",\"" + elementName + "\", arg0); }");
                    
                    if (DEBUG_METHODS) {
                        Logger.getLogger().debug("Adding ghost XmlElements setter method for " + convertMe.getSimpleName() + " with implementation " + ghostBufferSetter);
                    }
                
                    CtMethod elementsCtMethod = CtNewMethod.make(ghostBufferSetter.toString(), targetCtClass);
                
                    addListGenericSignature(elementsCtMethod, ac, true);
                
                    targetCtClass.addMethod(elementsCtMethod);
                }
                
                {
                    // Create the getter
                    
                    StringBuffer ghostBufferGetter = new StringBuffer("private " + getCompilableClass(gxed.getterSetterType) + " " + ghostMethodGetName +
                        "() { return (" + getCompilableClass(gxed.getterSetterType) + ") super._getProperty(\"" + elementNamespace + "\",\"" + elementName + "\"); }");
                
                    CtMethod elementsCtMethodGetter = CtNewMethod.make(ghostBufferGetter.toString(), targetCtClass);
                
                    addListGenericSignature(elementsCtMethodGetter, ac, false);
                
                    MethodInfo elementsMethodInfo = elementsCtMethodGetter.getMethodInfo();
                    ConstPool elementsMethodConstPool = elementsMethodInfo.getConstPool();
                
                    AnnotationsAttribute aa = new AnnotationsAttribute(elementsMethodConstPool, AnnotationsAttribute.visibleTag);
                
                    String translatedClassName;
                    if (ac.isInterface()) {
                        translatedClassName = Utilities.getProxyNameFromInterfaceName(ac.getName());
                    }
                    else {
                        translatedClassName = ac.getName();
                    } 
                    
                    XmlElement xElement = new XmlElementImpl(
                        elementName,
                        xmlElement.getBooleanValue("nillable"),
                        xmlElement.getBooleanValue("required"),
                        xmlElement.getStringValue("namespace"),
                        xmlElement.getStringValue("defaultValue"),
                        translatedClassName);
                
                    createAnnotationCopy(elementsMethodConstPool, xElement, aa);
                
                    elementsMethodInfo.addAttribute(aa);
                
                    if (DEBUG_METHODS) {
                        Logger.getLogger().debug("Adding ghost XmlElements getter method for " + convertMe.getSimpleName() + " with implementation " + ghostBufferGetter);
                        Logger.getLogger().debug("with XmlElement " + xElement);
                    }
                
                    targetCtClass.addMethod(elementsCtMethodGetter);
                }
            }
        }
        
        generateStaticModelFieldAndAbstractMethodImpl(targetCtClass,
                compiledModel,
                defaultClassPool);
        
        return targetCtClass;
    }
    
    /* package */ static ChildType getChildType(boolean isList, boolean isArray) {
        if (isList) return ChildType.LIST;
        if (isArray) return ChildType.ARRAY;
        return ChildType.DIRECT;
    }
    
    private static void generateStaticModelFieldAndAbstractMethodImpl(CtClass targetCtClass,
            ModelImpl model,
            ClassPool defaultClassPool) throws CannotCompileException, NotFoundException {
        StringBuffer sb = new StringBuffer();
        
        sb.append("private static final org.glassfish.hk2.xml.internal.ModelImpl INIT_MODEL() {\n");
        sb.append("org.glassfish.hk2.xml.internal.ModelImpl retVal = new org.glassfish.hk2.xml.internal.ModelImpl(\"");
        
        sb.append(model.getOriginalInterface() + "\",\"" + model.getTranslatedClass() + "\");\n");
        
        if (model.getRootName() != null) {
            String rootNameNamespace = QNameUtilities.getNamespace(model.getRootName());
            String rootNameKey = model.getRootName().getLocalPart();
            
            sb.append("retVal.setRootName(\"" + rootNameNamespace + "\",\"" + rootNameKey + "\");\n");
        }
        
        if (model.getKeyProperty() != null) {
            String keyPropNamespace = QNameUtilities.getNamespace(model.getKeyProperty());
            String keyPropKey = model.getKeyProperty().getLocalPart();
            
            sb.append("retVal.setKeyProperty(\"" + keyPropNamespace + "\",\"" + keyPropKey + "\");\n");
        }
        
        Map<QName, ChildDescriptor> allChildren = model.getAllChildrenDescriptors();
        for (Map.Entry<QName, ChildDescriptor> entry : allChildren.entrySet()) {
            QName xmlTagQName = entry.getKey();
            ChildDescriptor descriptor = entry.getValue();
            
            String xmlTagNamespace = QNameUtilities.getNamespace(xmlTagQName);
            String xmlTag = xmlTagQName.getLocalPart();
            
            ParentedModel parentedModel = descriptor.getParentedModel();
            if (parentedModel != null) {
                
                sb.append("retVal.addChild(" +
                        asParameter(parentedModel.getChildInterface()) + "," +
                        asParameter(parentedModel.getChildXmlNamespace()) + "," +
                        asParameter(parentedModel.getChildXmlTag()) + "," +
                        asParameter(parentedModel.getChildXmlAlias()) + "," +
                        asParameter(parentedModel.getChildType()) + "," +
                        asParameter(parentedModel.getGivenDefault()) + "," +
                        asParameter(parentedModel.getAliasType()) + "," +
                        asParameter(parentedModel.getXmlWrapperTag()) + "," +
                        asParameter(parentedModel.getAdapter()) + "," +
                        asBoolean(parentedModel.isRequired()) + "," +
                        asParameter(parentedModel.getOriginalMethodName()) + ");\n");
            }
            else {
                ChildDataModel childDataModel = descriptor.getChildDataModel();
                
                sb.append("retVal.addNonChild(" +
                        asParameter(xmlTagNamespace) + "," +
                        asParameter(xmlTag) + "," +
                        asParameter(childDataModel.getDefaultAsString()) + "," +
                        asParameter(childDataModel.getChildType()) + "," +
                        asParameter(childDataModel.getChildListType()) + "," +
                        asBoolean(childDataModel.isReference()) + "," +
                        asParameter(childDataModel.getFormat()) + "," +
                        asParameter(childDataModel.getAliasType()) + "," +
                        asParameter(childDataModel.getXmlAlias()) + "," +
                        asBoolean(childDataModel.isRequired()) + "," +
                        asParameter(childDataModel.getOriginalMethodName()) + ");\n");
            }
        }
        
        sb.append("return retVal; }");
        
        if (DEBUG_METHODS) {
            // Hidden behind static because of potential expensive toString costs
            Logger.getLogger().debug("Adding static model generator for " + targetCtClass.getSimpleName() +
                    " with implementation " + sb);
        }
        
        targetCtClass.addMethod(CtNewMethod.make(sb.toString(), targetCtClass));
        
        CtClass modelCt = defaultClassPool.get(ModelImpl.class.getName());
        
        CtField sField = new CtField(modelCt, "MODEL", targetCtClass);
        sField.setModifiers(Modifier.STATIC | Modifier.FINAL | Modifier.PRIVATE);
        
        targetCtClass.addField(sField, CtField.Initializer.byCall(targetCtClass, "INIT_MODEL"));
        
        CtMethod aMethod = CtNewMethod.make(
                "public org.glassfish.hk2.xml.internal.ModelImpl _getModel() { return MODEL; }" , targetCtClass);
        
        targetCtClass.addMethod(aMethod);
        
        CtMethod sMethod = CtNewMethod.make(
                "public static final org.glassfish.hk2.xml.internal.ModelImpl __getModel() { return MODEL; }" , targetCtClass);
        
        targetCtClass.addMethod(sMethod);
    }
    
    private static String asParameter(String me) {
        if (me == null) return "null";
        if (JAXB_DEFAULT_DEFAULT.equals(me)) {
            return "null";
        }
        
        return QUOTE + me + QUOTE;
    }
    
    private static String asBoolean(boolean bool) {
        return bool ? "true" : "false" ;
    }
    
    private static String asParameter(Format format) {
        String preCursor = Format.class.getName() + ".";
        switch (format) {
        case ATTRIBUTE:
            return preCursor + "ATTRIBUTE";
        case ELEMENT:
            return preCursor + "ELEMENT";
        case VALUE:
            return preCursor + "VALUE";
        default:
            throw new AssertionError("unknown Format " + format);
        }
    }
    
    private static String asParameter(ChildType ct) {
        switch(ct) {
        case DIRECT:
            return ChildType.class.getName() + ".DIRECT";
        case LIST:
            return ChildType.class.getName() + ".LIST";
        case ARRAY:
            return ChildType.class.getName() + ".ARRAY";
        default:
            throw new AssertionError("unknown ChildType " + ct);
        }
    }
    
    private static String asParameter(AliasType ct) {
        switch(ct) {
        case NORMAL:
            return AliasType.class.getName() + ".NORMAL";
        case HAS_ALIASES:
            return AliasType.class.getName() + ".HAS_ALIASES";
        case IS_ALIAS:
            return AliasType.class.getName() + ".IS_ALIAS";
        default:
            throw new AssertionError("unknown ChildType " + ct);
        }
    }
    
    private static void createAnnotationCopy(ConstPool parent, java.lang.annotation.Annotation javaAnnotation,
            AnnotationsAttribute retVal) throws Throwable {
        createAnnotationCopy(parent, new AnnotationAltAnnotationImpl(javaAnnotation, null), retVal);
    }
    
    private static void createAnnotationCopy(ConstPool parent, AltAnnotation javaAnnotation,
            AnnotationsAttribute retVal) throws Throwable {
        Annotation addMe = createAnnotationCopyOnly(parent, javaAnnotation);
        
        retVal.addAnnotation(addMe);
    }
    
    private static Annotation createAnnotationCopyOnly(ConstPool parent, AltAnnotation javaAnnotation) throws Throwable {
        Annotation annotation = new Annotation(javaAnnotation.annotationType(), parent);
        
        Map<String, Object> annotationValues = javaAnnotation.getAnnotationValues();
        for (Map.Entry<String, Object> entry : annotationValues.entrySet()) {
            String valueName = entry.getKey();
            Object value = entry.getValue();
            
            Class<?> javaAnnotationType = value.getClass();
            if (String.class.equals(javaAnnotationType)) {
                annotation.addMemberValue(valueName, new StringMemberValue((String) value, parent));
            }
            else if (Boolean.class.equals(javaAnnotationType)) {
                boolean bvalue = (Boolean) value;
                
                annotation.addMemberValue(valueName, new BooleanMemberValue(bvalue, parent));
            }
            else if (AltClass.class.isAssignableFrom(javaAnnotationType)) {
                AltClass altJavaAnnotationType = (AltClass) value;
                
                String sValue;
                if (javaAnnotation.annotationType().equals(XmlElement.class.getName()) &&
                        (javaAnnotation.getStringValue("getTypeByName") != null)) {
                    sValue = javaAnnotation.getStringValue("getTypeByName");
                }
                else {
                    sValue = altJavaAnnotationType.getName();
                }
                
                annotation.addMemberValue(valueName, new ClassMemberValue(sValue, parent));
            }
            else if (Integer.class.equals(javaAnnotationType)) {
                int ivalue = (Integer) value;
                
                annotation.addMemberValue(valueName, new IntegerMemberValue(parent, ivalue));
            }
            else if (Long.class.equals(javaAnnotationType)) {
                long lvalue = (Long) value;
                
                annotation.addMemberValue(valueName, new LongMemberValue(lvalue, parent));
            }
            else if (Double.class.equals(javaAnnotationType)) {
                double dvalue = (Double) value;
                
                annotation.addMemberValue(valueName, new DoubleMemberValue(dvalue, parent));
            }
            else if (Byte.class.equals(javaAnnotationType)) {
                byte bvalue = (Byte) value;
                
                annotation.addMemberValue(valueName, new ByteMemberValue(bvalue, parent));
            }
            else if (Character.class.equals(javaAnnotationType)) {
                char cvalue = (Character) value;
                
                annotation.addMemberValue(valueName, new CharMemberValue(cvalue, parent));
            }
            else if (Short.class.equals(javaAnnotationType)) {
                short svalue = (Short) value;
                
                annotation.addMemberValue(valueName, new ShortMemberValue(svalue, parent));
            }
            else if (Float.class.equals(javaAnnotationType)) {
                float fvalue = (Float) value;
                
                annotation.addMemberValue(valueName, new FloatMemberValue(fvalue, parent));
            }
            else if (AltEnum.class.isAssignableFrom(javaAnnotationType)) {
                AltEnum evalue = (AltEnum) value;
                
                EnumMemberValue jaEnum = new EnumMemberValue(parent);
                jaEnum.setType(evalue.getDeclaringClass());
                jaEnum.setValue(evalue.getName());
                    
                annotation.addMemberValue(valueName, jaEnum);
            }
            else if (javaAnnotationType.isArray()) {
                Class<?> typeOfArray = javaAnnotationType.getComponentType();
                
                MemberValue arrayValue[];
                if (int.class.equals(typeOfArray)) {
                    int[] iVals = (int[]) value;
                    
                    arrayValue = new MemberValue[iVals.length];
                    for (int lcv = 0; lcv < iVals.length; lcv++) {
                        arrayValue[lcv] = new IntegerMemberValue(parent, iVals[lcv]);
                    }
                }
                else if (String.class.equals(typeOfArray)) {
                    String[] iVals = (String[]) value;
                    
                    arrayValue = new MemberValue[iVals.length];
                    for (int lcv = 0; lcv < iVals.length; lcv++) {
                        arrayValue[lcv] = new StringMemberValue(iVals[lcv], parent);
                    }
                }
                else if (long.class.equals(typeOfArray)) {
                    long[] iVals = (long[]) value;
                    
                    arrayValue = new MemberValue[iVals.length];
                    for (int lcv = 0; lcv < iVals.length; lcv++) {
                        arrayValue[lcv] = new LongMemberValue(iVals[lcv], parent);
                    }
                }
                else if (boolean.class.equals(typeOfArray)) {
                    boolean[] iVals = (boolean[]) value;
                    
                    arrayValue = new MemberValue[iVals.length];
                    for (int lcv = 0; lcv < iVals.length; lcv++) {
                        arrayValue[lcv] = new BooleanMemberValue(iVals[lcv], parent);
                    }
                }
                else if (float.class.equals(typeOfArray)) {
                    float[] iVals = (float[]) value;
                    
                    arrayValue = new MemberValue[iVals.length];
                    for (int lcv = 0; lcv < iVals.length; lcv++) {
                        arrayValue[lcv] = new FloatMemberValue(iVals[lcv], parent);
                    }
                }
                else if (double.class.equals(typeOfArray)) {
                    double[] iVals = (double[]) value;
                    
                    arrayValue = new MemberValue[iVals.length];
                    for (int lcv = 0; lcv < iVals.length; lcv++) {
                        arrayValue[lcv] = new DoubleMemberValue(iVals[lcv], parent);
                    }
                }
                else if (byte.class.equals(typeOfArray)) {
                    byte[] iVals = (byte[]) value;
                    
                    arrayValue = new MemberValue[iVals.length];
                    for (int lcv = 0; lcv < iVals.length; lcv++) {
                        arrayValue[lcv] = new ByteMemberValue(iVals[lcv], parent);
                    }
                }
                else if (char.class.equals(typeOfArray)) {
                    char[] iVals = (char[]) value;
                    
                    arrayValue = new MemberValue[iVals.length];
                    for (int lcv = 0; lcv < iVals.length; lcv++) {
                        arrayValue[lcv] = new CharMemberValue(iVals[lcv], parent);
                    }
                }
                else if (short.class.equals(typeOfArray)) {
                    short[] iVals = (short[]) value;
                    
                    arrayValue = new MemberValue[iVals.length];
                    for (int lcv = 0; lcv < iVals.length; lcv++) {
                        arrayValue[lcv] = new ShortMemberValue(iVals[lcv], parent);
                    }
                }
                else if (AltEnum.class.isAssignableFrom(typeOfArray)) {
                    AltEnum[] iVals = (AltEnum[]) value;
                    
                    arrayValue = new MemberValue[iVals.length];
                    for (int lcv = 0; lcv < iVals.length; lcv++) {
                        EnumMemberValue jaEnum = new EnumMemberValue(parent);
                        jaEnum.setType(iVals[lcv].getDeclaringClass());
                        jaEnum.setValue(iVals[lcv].getName());
                        
                        arrayValue[lcv] = jaEnum;
                    }
                }
                else if (AltClass.class.isAssignableFrom(typeOfArray)) {
                    AltClass[] iVals = (AltClass[]) value;
                    
                    arrayValue = new MemberValue[iVals.length];
                    for (int lcv = 0; lcv < iVals.length; lcv++) {
                        AltClass arrayElementClass = iVals[lcv];
                        
                        arrayValue[lcv] = new ClassMemberValue(arrayElementClass.getName(), parent);
                    }
                }
                else if (AltAnnotation.class.isAssignableFrom(typeOfArray)) {
                    AltAnnotation[] aVals = (AltAnnotation[]) value;
                    
                    arrayValue = new MemberValue[aVals.length];
                    for (int lcv = 0; lcv < aVals.length; lcv++) {
                        AltAnnotation arrayAnnotation = aVals[lcv];
                        
                        arrayValue[lcv] = new AnnotationMemberValue(createAnnotationCopyOnly(parent, arrayAnnotation), parent);
                    }
                }
                else {
                    throw new AssertionError("Array type " + typeOfArray.getName() + " is not yet implemented for " + valueName);
                }
                
                ArrayMemberValue arrayMemberValue = new ArrayMemberValue(parent);
                arrayMemberValue.setValue(arrayValue);
                
                annotation.addMemberValue(valueName, arrayMemberValue);
                
            }
            else {
                throw new AssertionError("Annotation type " + javaAnnotationType.getName() + " is not yet implemented for " + valueName);
            }
            
        }
        
        return annotation;
    }
    
    private static String getCompilableClass(AltClass clazz) {
        int depth = 0;
        while (clazz.isArray()) {
            depth++;
            clazz = clazz.getComponentType();
        }
        
        StringBuffer sb = new StringBuffer(clazz.getName());
        for (int lcv = 0; lcv < depth; lcv++) {
            sb.append("[]");
        }
        
        return sb.toString();
    }
    
    private static AltClass getUltimateNonArrayClass(AltClass clazz) {
        if (clazz == null) return null;
        
        while (clazz.isArray()) {
            clazz = clazz.getComponentType();
        }
        
        return clazz;
    }
    
    private static void createInterfaceForAltClassIfNeeded(AltClass toFix, ClassPool defaultClassPool) {
        if (toFix == null) return;
        toFix = getUltimateNonArrayClass(toFix);
        
        String fixerClass = toFix.getName();
        if (defaultClassPool.getOrNull(fixerClass) == null) {
            defaultClassPool.makeInterface(fixerClass);
        }
    }
    
    private static void addListGenericSignature(CtMethod method, AltClass listPT, boolean isSetter) {
        ClassType intSignature = new ClassType(listPT.getName());
        TypeArgument typeArguments[] = new TypeArgument[1];
        typeArguments[0] = new TypeArgument(intSignature);
    
        ClassType listSignature = new ClassType(List.class.getName(), typeArguments);
        
        MethodSignature ms;
        if (isSetter) {
            SignatureAttribute.Type params[] = new SignatureAttribute.Type[1];
            params[0] = listSignature;
            
            ms = new MethodSignature(null, params, null, null);
        }
        else {
            ms = new MethodSignature(null, null, listSignature, null);
            
            
        }
        
        method.setGenericSignature(ms.encode());
    }
    
    private static class GhostXmlElementData {
        private final AltAnnotation xmlElements[];
        private final AltClass getterSetterType;
        
        private GhostXmlElementData(AltAnnotation xmlElements[], AltClass getterSetterType) {
            this.xmlElements = xmlElements;
            this.getterSetterType = getterSetterType;
        }
    }

}
