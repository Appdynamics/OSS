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

package org.glassfish.hk2.pbuf.internal;

import java.beans.Introspector;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.glassfish.hk2.api.MultiException;
import org.glassfish.hk2.pbuf.api.annotations.Comment;
import org.glassfish.hk2.pbuf.api.annotations.OneOf;
import org.glassfish.hk2.utilities.general.GeneralUtilities;
import org.glassfish.hk2.xml.internal.Generator;
import org.glassfish.hk2.xml.internal.Utilities;

import com.google.protobuf.Method;

/**
 * @author jwells
 *
 */
@SupportedAnnotationTypes("org.glassfish.hk2.pbuf.api.annotations.GenerateProto")
public class PBufGeneratorProcessor extends AbstractProcessor {
    /**
     * Gets rid of warnings and this code should work with all source versions
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }
    
    @Override
    public boolean process(Set<? extends TypeElement> annotations,
            RoundEnvironment roundEnv) {
        Filer filer = processingEnv.getFiler();
        Elements elements = processingEnv.getElementUtils();
        
        for (TypeElement annotation : annotations) {
            Set<? extends Element> clazzes = roundEnv.getElementsAnnotatedWith(annotation);
            
            for (Element clazzElement : clazzes) {
                if (!(clazzElement instanceof TypeElement)) continue;
                
                TypeElement clazz = (TypeElement) clazzElement;
                ElementKind cKind = clazz.getKind();
                if (!ElementKind.INTERFACE.equals(cKind)) continue;
                
                Writer writer = openWriter(clazz, elements, filer);
                try {
                    buildHeader(writer, clazz, elements);
                    
                    List<? extends Element> members = elements.getAllMembers(clazz);
                    handleMethods(writer, clazz, members, elements);
                }
                finally {
                    try {
                        writer.close();
                    }
                    catch (IOException ioe) {
                        throw new MultiException(ioe);
                    }
                }
            }
        }
        
        return true;
    }
    
    private static void writeBlankLine(Writer writer) throws IOException {
        writer.write("\n");
    }
    
    /**
     * Actually four spaces
     * 
     * @param writer
     * @throws IOException
     */
    private static void writeTab(Writer writer) throws IOException {
        writer.write("    ");
    }
    
    private Writer openWriter(TypeElement clazz, Elements elements, Filer filer) {
        PackageElement clazzPackage = elements.getPackageOf(clazz);
        String clazzPackageName = Utilities.convertNameToString(clazzPackage.getQualifiedName());
        String clazzSimpleName = Utilities.convertNameToString(clazz.getSimpleName());
        clazzSimpleName = clazzSimpleName + ".proto";
        
        try {
            FileObject fileObject = filer.createResource(StandardLocation.CLASS_OUTPUT,
                clazzPackageName,
                clazzSimpleName,
                clazz);
            
            return fileObject.openWriter();
        }
        catch (IOException ioe) {
            // TODO: How should errors actually be handled?
            throw new MultiException(ioe);
        }
    }
    
    private Writer buildHeader(Writer writer, TypeElement clazz, Elements elements) {
        PackageElement clazzPackage = elements.getPackageOf(clazz);
        String clazzPackageName = Utilities.convertNameToString(clazzPackage.getQualifiedName());
        String name = Utilities.convertNameToString(clazz.getQualifiedName());
        
        name = name.replace('.', '/');
        
        try {
            writer.write("// Generated by PBufGeneratorProcessor\n");
            writer.write("// Source File: " + name + ".java\n");
            writeBlankLine(writer);
            
            writer.write("syntax = \"proto3\";\n");
            writeBlankLine(writer);
            
            if (clazzPackageName != null && !clazzPackageName.isEmpty()) {
                writer.write("package " + clazzPackageName + ";\n\n");
            }
            
            return writer;
        }
        catch (IOException ioe) {
            // TODO: How should errors actually be handled?
            throw new MultiException(ioe);
        }
    }
    
    private void doComment(Writer writer, String comment, int indentLevel) throws IOException {
        if (comment == null || comment.isEmpty()) return;
        
        StringTokenizer nlTokenizer = new StringTokenizer(comment, "\n\r");
        while(nlTokenizer.hasMoreTokens()) {
            String singleLine = "// " + nlTokenizer.nextToken() + "\n";
            for (int lcv = 0; lcv < indentLevel; lcv++) {
                writeTab(writer);
            }
            
            writer.write(singleLine);
        }
    }
    
    private void handleMethods(Writer writer, TypeElement clazz, List<? extends Element> allMethods, Elements elements) {
        String clazzSimpleName = Utilities.convertNameToString(clazz.getSimpleName());
        
        Comment commentAnnotation = clazz.getAnnotation(Comment.class);
        String comment = null;
        
        if (commentAnnotation != null) {
            comment = commentAnnotation.value();
        }
        
        try {
            List<XmlElementInfo> infos = doImports(writer, clazz, allMethods, elements);
            
            doComment(writer, comment, 0);
            writer.write("message " + clazzSimpleName + " {\n");
            
            String currentOneOf = null;
            
            int indentLevel = 1;
            int number = 1;
            for (XmlElementInfo info : infos) {
                String thisOneOf = info.getOneOf();
                if (!GeneralUtilities.safeEquals(currentOneOf, thisOneOf)) {
                    if (currentOneOf != null) {
                        // Must finish current one-of
                        writeTab(writer);
                        writer.write("}\n");
                        
                        indentLevel = 1;
                    }
                    
                    if (thisOneOf != null) {
                        if (number > 1) {
                            writeBlankLine(writer);
                        }
                        
                        // Must start new one of and increment indentLevel
                        writeTab(writer);
                        
                        writer.write("oneof " + thisOneOf + " {\n");
                        indentLevel = 2;
                    }
                    
                    currentOneOf = thisOneOf;
                }
                
                boolean incrementCount = handleMethod(writer, info, number, indentLevel);
                if (incrementCount) {
                    number++;
                }
            }
            
            if (currentOneOf != null) {
                // Must finish last oneOf
                writeTab(writer);
                writer.write("}\n");
            }
            
            writer.write("}\n");
            writeBlankLine(writer);
        }
        catch (IOException ioe) {
            throw new MultiException(ioe);
        }
    }
    
    private boolean handleMethod(Writer writer, XmlElementInfo info, int number, int indentLevel) throws IOException {
        writeBlankLine(writer);
        
        doComment(writer, info.getComment(), indentLevel);
        
        for (int lcv = 0; lcv < indentLevel; lcv++) {
            writeTab(writer);
        }
        
        String translatedName = PBUtilities.camelCaseToUnderscore(info.getName());
        
        if (info.getChildInfo() == null) {
            writer.write(info.getType() + " " + translatedName + " = " + number);
        }
        else {
            if (info.getChildInfo().isRepeated()) {
                writer.write("repeated ");
            }
            
            writer.write(info.getChildInfo().getName() + " " + translatedName + " = " + number);
        }
        
        writer.write(";\n");
        
        return true;
    }
    
    private static ExecutableElement getFieldFromAnnotation(TypeElement annotation, String field) {
        List<? extends Element> elements = annotation.getEnclosedElements();
        for (Element element : elements) {
            if (!(element instanceof ExecutableElement)) {
                continue;
            }
            
            ExecutableElement ee = (ExecutableElement) element;
            String executableName = Utilities.convertNameToString(ee.getSimpleName());
            
            if (field.equals(executableName)) {
                return ee;
            }
        }
        
        return null;
    }
    
    private static String getDeclaredType(DeclaredType declaredType) {
        TypeElement typeElement = (TypeElement) declaredType.asElement();
        String fullName = Utilities.convertNameToString(typeElement.getQualifiedName());
        
        if (Boolean.class.getName().equals(fullName)) {
            return "bool";
        }
        if (Byte.class.getName().equals(fullName) ||
                Short.class.getName().equals(fullName) ||
                Integer.class.getName().equals(fullName)) {
            return "int32";
        }
        if (Long.class.getName().equals(fullName)) {
            return "int64";
        }
        if (Character.class.getName().equals(fullName) ||
                String.class.getName().equals(fullName)) {
            return "string";
        }
        if (Float.class.getName().equals(fullName)) {
            return "float";
        }
        if (Double.class.getName().equals(fullName)) {
            return "double";
        }
        
        return "string";
    }
    
    private static String getPBufType(ExecutableElement method, TypeMirror mirror) {
        TypeKind kind = mirror.getKind();
        
        switch(kind) {
        case BOOLEAN:
            return "bool";
        case BYTE:
        case SHORT:
        case INT:
            return "int32";
        case LONG:
            return "int64";
        case CHAR:
            return "string";
        case FLOAT:
            return "float";
        case DOUBLE:
            return "double";
        case DECLARED:
            return getDeclaredType((DeclaredType) mirror);
        case ARRAY:
            // TODO: Really?
            return "string";
        default:
            throw new AssertionError("Unknown getter has a unknown return type: " + kind + " for method " + method +
                    " and mirror " + mirror);
        }
    }
    
    private static boolean isSetter(ExecutableElement method) {
        String methodName = Utilities.convertNameToString(method.getSimpleName());
        if (methodName.startsWith("set") && methodName.length() > 3) {
            return true;
        }
        
        return false;
    }
    
    private static boolean isGetter(ExecutableElement method) {
        String methodName = Utilities.convertNameToString(method.getSimpleName());
        if (methodName.startsWith("get") && methodName.length() > 3) {
            return true;
        }
        
        if (!methodName.startsWith("is")) {
            return false;
        }
        if (methodName.length() <= 2) {
            return false;
        }
        
        TypeMirror mirror = method.getReturnType();
        if (mirror.getKind().equals(TypeKind.BOOLEAN)) {
            return true;
        }
        if (mirror.getKind().equals(TypeKind.DECLARED)) {
            DeclaredType declared = (DeclaredType) mirror;
            TypeElement asElement = (TypeElement) declared.asElement();
            
            String elementName = Utilities.convertNameToString(asElement.getQualifiedName());
            if (Boolean.class.getName().equals(elementName)) {
                return true;
            }
        }
        
        return false;
    }
    
    private static String getPBufType(ExecutableElement method) {
        String methodName = Utilities.convertNameToString(method.getSimpleName());
        
        if (isSetter(method)) {
            List<? extends VariableElement> parameters = method.getParameters();
            if (parameters.size() != 1) {
                throw new AssertionError("Unknown setter has more than one input parameter: " + methodName);
            }
            
            TypeMirror asType = parameters.get(0).asType();
            
            String retVal = getPBufType(method, asType);
            return retVal;
        }
        
        if (isGetter(method)) {
            TypeMirror type = method.getReturnType();
            
            if (type == null) {
                throw new AssertionError("Unknown getter does not have a return type: " + methodName);
            }
            
            String retVal = getPBufType(method, type);
            return retVal;
        }
        
        throw new AssertionError("Cannot determing type as XmlElement not on a get or set method " + methodName);
        
    }
    
    /**
     * Returns a non-decapitalized field name
     * 
     * @param method
     * @return
     */
    private static String getRawFieldNameFromMethod(ExecutableElement method) {
        String simpleName = Utilities.convertNameToString(method.getSimpleName());
        
        if (simpleName.startsWith("get") || simpleName.startsWith("set")) {
            return simpleName.substring(3);
        }
        
        if (simpleName.startsWith("is")) {
            return simpleName.substring(2);
        }
        
        throw new AssertionError("Unknown method name " + simpleName + " is neither a getter nor a setter");
        
    }
    
    private static XmlElementInfo getXmlElementName(ExecutableElement method, Elements elements) {
        Comment commentAnnotation = method.getAnnotation(Comment.class);
        
        String comment = null;
        if (commentAnnotation != null) {
            comment = commentAnnotation.value();
        }
        
        OneOf oneOfAnnotation = method.getAnnotation(OneOf.class);
        
        String oneOf = null;
        if (oneOfAnnotation != null) {
            oneOf = oneOfAnnotation.value();
        }
        
        List<? extends AnnotationMirror> annotationMirrors = elements.getAllAnnotationMirrors(method);
        for (AnnotationMirror mirror : annotationMirrors) {
            DeclaredType mirrorType = mirror.getAnnotationType();
            Element e = mirrorType.asElement();
            if (!(e instanceof TypeElement)) {
                continue;
            }
            TypeElement te = (TypeElement) e;
            
            String mirrorTypeName = Utilities.convertNameToString(te.getQualifiedName());
            if (mirrorTypeName.equals(XmlElement.class.getName()) || mirrorTypeName.equals(XmlAttribute.class.getName())) {
                String type = getPBufType(method);
                
                // It has the XmlElement on it, now get the actual name
                Map<? extends ExecutableElement,? extends AnnotationValue> values =
                        elements.getElementValuesWithDefaults(mirror);
                
                ExecutableElement foundNameExecutable = getFieldFromAnnotation(te, "name");
                
                AnnotationValue av = values.get(foundNameExecutable);
                if (av == null) {
                     throw new AssertionError("The value of name must never be null");
                }
                
                String nameValue = null;
                    
                String sValue = (String) av.getValue();
                if (Generator.JAXB_DEFAULT_STRING.equals(sValue)) {
                    nameValue = getRawFieldNameFromMethod(method);
                    
                    nameValue = Introspector.decapitalize(nameValue);
                }
                else {
                    nameValue = sValue;
                }
                
                String sortField = getRawFieldNameFromMethod(method);
                sortField = Introspector.decapitalize(sortField);
                
                return new XmlElementInfo(nameValue, type, sortField, comment, oneOf);
            }
        }
        
        return null;
    }
    
    private static ChildInfo getChildName(ExecutableElement method) {
        TypeMirror tm = method.getReturnType();
        TypeKind kind = tm.getKind();
        
        if (TypeKind.ARRAY.equals(kind)) {
            ArrayType at = (ArrayType) tm;
            
            TypeMirror componentType = at.getComponentType();
            TypeKind componentKind = componentType.getKind();
            
            if (TypeKind.DECLARED.equals(componentKind)) {
                DeclaredType dt = (DeclaredType) componentType;
                Element asElement = dt.asElement();
                
                ElementKind elementKind = asElement.getKind();
                if (ElementKind.INTERFACE.equals(elementKind)) {
                    TypeElement interfaceElement = (TypeElement) asElement;
                    
                    return new ChildInfo(Utilities.convertNameToString(interfaceElement.getQualifiedName()), true);
                }
            }
        }
        else if (TypeKind.DECLARED.equals(kind)) {
            DeclaredType dt = (DeclaredType) tm;
            Element asElement = dt.asElement();
            ElementKind elementKind = asElement.getKind();
            
            if (ElementKind.INTERFACE.equals(elementKind)) {
                TypeElement possibleList = (TypeElement) asElement;
                String possibleListType = Utilities.convertNameToString(possibleList.getQualifiedName());
                
                if (List.class.getName().equals(possibleListType)) {
                    // It is a list, now need the type parameter
                    List<? extends TypeMirror> typeArguments = dt.getTypeArguments();
                    if (typeArguments == null || typeArguments.isEmpty()) {
                        return null;
                    }
                    
                    TypeMirror firstArgument = typeArguments.get(0);
                    TypeKind firstTypeKind = firstArgument.getKind();
                    
                    if (TypeKind.DECLARED.equals(firstTypeKind)) {
                        DeclaredType firstDT = (DeclaredType) firstArgument;
                        Element firstElement = firstDT.asElement();
                        ElementKind firstElementKind = firstElement.getKind();
                        
                        if (!ElementKind.INTERFACE.equals(firstElementKind)) {
                            return null;
                        }
                        
                        TypeElement firstElementType = (TypeElement) firstElement;
                        return new ChildInfo(Utilities.convertNameToString(firstElementType.getQualifiedName()), true);
                    }
                }
                else {
                    // It's a direct child
                    return new ChildInfo(possibleListType, false);
                }
            }
        }
        
        return null;
    }
    
    private List<XmlElementInfo> doImports(Writer writer,
            TypeElement clazz,
            List<? extends Element> allMethods,
            Elements elements) throws IOException {
        String clazzName = Utilities.convertNameToString(clazz.getQualifiedName());
        
        boolean atLeastOne = false;
        
        List<XmlElementInfo> retVal = new ArrayList<XmlElementInfo>(allMethods.size());
        HashSet<String> alreadyDone = new HashSet<String>();
        for (Element element : allMethods) {
            if (!(element instanceof ExecutableElement)) {
                continue;
            }
            
            ExecutableElement method = (ExecutableElement) element;
            XmlElementInfo xmlElementName = getXmlElementName(method, elements);
            if (xmlElementName == null) {
                continue;
            }
            
            retVal.add(xmlElementName);
            
            ChildInfo childName = getChildName(method);
            if (childName == null) {
                continue;
            }
            
            xmlElementName.setChildInfo(childName);
            
            String importName = childName.getName().replace('.', '/') + ".proto";
            if (alreadyDone.contains(importName)) continue;
            alreadyDone.add(importName);
            
            writer.write("import \"" + importName + "\";\n");
            atLeastOne = true;
        }
        
        if (atLeastOne) {
            writeBlankLine(writer);
        }
        
        XmlType xmlType = clazz.getAnnotation(XmlType.class);
        if (xmlType == null) {
            return retVal;
        }
        
        String propOrder[] = xmlType.propOrder();
        
        ElementOrderer comparator = new ElementOrderer(propOrder, clazzName);
        
        TreeSet<XmlElementInfo> sortedMap = new TreeSet<XmlElementInfo>(comparator);
        
        sortedMap.addAll(retVal);
        
        List<XmlElementInfo> sortedRetVal = new ArrayList<XmlElementInfo>(retVal.size());
        
        sortedRetVal.addAll(sortedMap);
        
        return sortedRetVal;
    }
    
    private final static class XmlElementInfo {
        private final String name;
        private final String type;
        private ChildInfo childInfo;
        private final String sortField;
        private final String comment;
        private final String oneOf;
        
        private XmlElementInfo(String name, String type, String sortField, String comment, String oneOf) {
            this.name = name;
            this.type = type;
            this.sortField = sortField;
            this.comment = comment;
            this.oneOf = oneOf;
        }
        
        private String getName() {
            return name;
        }
        
        private String getType() {
            return type;
        }
        
        private void setChildInfo(ChildInfo info) {
            this.childInfo = info;
        }
        
        private ChildInfo getChildInfo() {
            return childInfo;
        }
        
        private String getSortField() {
            return sortField;
        }
        
        private String getComment() {
            return comment;
        }
        
        private String getOneOf() {
            return oneOf;
        }
        
        @Override
        public int hashCode() {
            return sortField.hashCode();
        }
        
        @Override
        public boolean equals(Object o) {
            if (o == null) return false;
            if (!(o instanceof XmlElementInfo)) {
                return false;
            }
            
            XmlElementInfo other = (XmlElementInfo) o;
            
            return other.sortField.equals(sortField);
        }
    }
    
    private final static class ChildInfo {
        private final String name;
        private final boolean repeated;
        
        private ChildInfo(String name, boolean repeated) {
            this.name = name;
            this.repeated = repeated;
        }
        
        private String getName() {
            return name;
        }
        
        private boolean isRepeated() {
            return repeated;
        }
    }
    
    private final static class ElementOrderer implements Comparator<XmlElementInfo> {
        private final Map<String, Integer> sorter = new HashMap<String, Integer>();
        private final String clazzName;
        
        private ElementOrderer(String order[], String clazzName) {
            this.clazzName = clazzName;
            
            for (int lcv = 0; lcv < order.length; lcv++) {
                sorter.put(order[lcv], lcv);
            }
        }

        /* (non-Javadoc)
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        @Override
        public int compare(XmlElementInfo o1, XmlElementInfo o2) {
            String o1SortField = o1.getSortField();
            String o2SortField = o2.getSortField();
            
            if (!sorter.containsKey(o1SortField)) {
                throw new AssertionError("XmlType must contain all XmlElement and XmlAttribute names, the name " + o1SortField +
                        " was not found in XmlType on " + clazzName);
            }
            
            if (!sorter.containsKey(o2SortField)) {
                throw new AssertionError("XmlType must contain all XmlElement and XmlAttribute names, the name " + o1SortField +
                        " was not found in XmlType on " + clazzName);
            }
            
            int o1Spot = sorter.get(o1SortField);
            int o2Spot = sorter.get(o2SortField);
            
            return o1Spot - o2Spot;
        }
    }

}
