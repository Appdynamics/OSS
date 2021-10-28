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

package org.glassfish.hk2.xml.internal.alt.papi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;

import org.glassfish.hk2.utilities.general.GeneralUtilities;
import org.glassfish.hk2.xml.internal.Utilities;
import org.glassfish.hk2.xml.internal.alt.AltAnnotation;
import org.glassfish.hk2.xml.internal.alt.AltClass;
import org.glassfish.hk2.xml.internal.alt.AltMethod;
import org.glassfish.hk2.xml.internal.alt.clazz.ClassAltClassImpl;

/**
 * @author jwells
 *
 */
public class TypeElementAltClassImpl implements AltClass {
    private final TypeElement clazz;
    private final ProcessingEnvironment processingEnv;
    
    private List<AltMethod> methods;
    private List<AltAnnotation> annotations;
    
    public TypeElementAltClassImpl(TypeElement clazz, ProcessingEnvironment processingEnv) {
        this.clazz = clazz;
        this.processingEnv = processingEnv;
    }

    /* (non-Javadoc)
     * @see org.glassfish.hk2.xml.internal.alt.AltClass#getName()
     */
    @Override
    public String getName() {
        return Utilities.convertNameToString(processingEnv.getElementUtils().getBinaryName(clazz));
    }

    /* (non-Javadoc)
     * @see org.glassfish.hk2.xml.internal.alt.AltClass#getSimpleName()
     */
    @Override
    public String getSimpleName() {
        return Utilities.convertNameToString(clazz.getSimpleName());
    }

    /* (non-Javadoc)
     * @see org.glassfish.hk2.xml.internal.alt.AltClass#getAnnotations()
     */
    @Override
    public synchronized List<AltAnnotation> getAnnotations() {
        if (annotations != null) return annotations;
        
        List<? extends AnnotationMirror> annoMirrors = processingEnv.getElementUtils().getAllAnnotationMirrors(clazz);
        
        ArrayList<AltAnnotation> retVal = new ArrayList<AltAnnotation>(annoMirrors.size());
        
        for (AnnotationMirror annoMirror : annoMirrors) {
            AnnotationMirrorAltAnnotationImpl anno = new AnnotationMirrorAltAnnotationImpl(annoMirror, processingEnv);
            
            retVal.add(anno);
        }
        
        annotations = Collections.unmodifiableList(new ArrayList<AltAnnotation>(retVal));
        return annotations;
    }
    
    private static final Set<String> POSSIBLE_NO_HANDLE = new HashSet<String>(Arrays.asList(new String[] {
            "getClass",
            "hashCode",
            "equals",
            "toString",
            "notify",
            "notifyAll",
            "wait"
    }));
    
    private boolean isMethodToGenerate(Element element) {
        if (!ElementKind.METHOD.equals(element.getKind())) return false;
        ExecutableElement executable = (ExecutableElement) element;
        
        String methodName = executable.getSimpleName().toString();
        
        if (!POSSIBLE_NO_HANDLE.contains(methodName)) return true;
        
        List<? extends TypeMirror> parameters = ((ExecutableType) executable.asType()).getParameterTypes();
        
        if ("getClass".equals(methodName) ||
                "hashCode".equals(methodName) ||
                "toString".equals(methodName) ||
                "notify".equals(methodName) ||
                "notifyAll".equals(methodName) ||
                "wait".equals(methodName)) {
            // If any of the above have zero arguments they should not be handled
            if (parameters.size() == 0) return false;
        }
        
        if ("equals".equals(methodName) && (parameters.size() == 1)) {
            // If input is an Object.class...
            TypeMirror param0 = parameters.get(0);
                
            AltClass ac = Utilities.convertTypeMirror(param0, processingEnv);
            if (Object.class.getName().equals(ac.getName())) return false;
        }
        
        if ("wait".equals(methodName) && (parameters.size() == 1)) {
            // If input is a long
            TypeMirror param0 = parameters.get(0);
                
            AltClass ac = Utilities.convertTypeMirror(param0, processingEnv);
            if (ClassAltClassImpl.LONG.equals(ac)) return false;
        }
        
        if ("wait".equals(methodName) && (parameters.size() == 2)) {
            // If input is a long and an int
            TypeMirror param0 = parameters.get(0);
            TypeMirror param1 = parameters.get(1);
                
            AltClass ac0 = Utilities.convertTypeMirror(param0, processingEnv);
            AltClass ac1 = Utilities.convertTypeMirror(param1, processingEnv);
            
            if (ClassAltClassImpl.LONG.equals(ac0) && ClassAltClassImpl.INT.equals(ac1)) return false;
        }
        
        return true;
        
        
    }
    
    

    /* (non-Javadoc)
     * @see org.glassfish.hk2.xml.internal.alt.AltClass#getMethods()
     */
    @Override
    public synchronized List<AltMethod> getMethods() {
        if (methods != null) return methods;
        
        List<? extends Element> innerElements = processingEnv.getElementUtils().getAllMembers(clazz);
        
        TreeMap<String, List<Element>> reorderByEnclosingClass = new TreeMap<String, List<Element>>();
        
        String clazzName = getName();
        for (Element innerElementElement : innerElements) {
            if (isMethodToGenerate(innerElementElement)) {
                TypeElement enclosingElement = (TypeElement) innerElementElement.getEnclosingElement();
                
                String enclosingName = Utilities.convertNameToString(processingEnv.getElementUtils().getBinaryName(enclosingElement));
                
                List<Element> addedList = reorderByEnclosingClass.get(enclosingName);
                if (addedList == null) {
                    addedList = new LinkedList<Element>();
                    
                    reorderByEnclosingClass.put(enclosingName, addedList);
                }
                
                addedList.add(innerElementElement);
            }
        }
        
        List<Element> innerElementsReordered = new ArrayList<Element>(innerElements.size());
        for (Map.Entry<String, List<Element>> listByEnclosing : reorderByEnclosingClass.entrySet()) {
            String enclosingClass = listByEnclosing.getKey();
            if (clazzName.equals(enclosingClass)) continue;
            
            innerElementsReordered.addAll(listByEnclosing.getValue());
        }
        
        List<Element> topClass = reorderByEnclosingClass.get(clazzName);
        if (topClass != null) {
            innerElementsReordered.addAll(topClass);
        }
        
        ArrayList<AltMethod> retVal = new ArrayList<AltMethod>(innerElementsReordered.size());
        for (Element innerElementElement : innerElementsReordered) {
            retVal.add(new ElementAltMethodImpl(innerElementElement, processingEnv));
        }
        
        methods = Collections.unmodifiableList(retVal);
        return methods;
    }
    
    @Override
    public AltClass getSuperParameterizedType(AltClass superclass,
            int paramIndex) {
        if (paramIndex < 0) return null;
        
        String stopName = superclass.getName();
        
        TypeElement currentClass = clazz;
        while (currentClass != null) {
            String currentName = Utilities.convertNameToString(processingEnv.getElementUtils().getBinaryName(currentClass));
            TypeMirror superMirror = currentClass.getSuperclass();
            if (superMirror == null) return null;
            
            if (!(superMirror instanceof DeclaredType)) return null;
            
            DeclaredType superDeclared = (DeclaredType) superMirror;
            
            TypeElement nextClass = (TypeElement) superDeclared.asElement();
            
            String superName = Utilities.convertNameToString(processingEnv.getElementUtils().getBinaryName(nextClass));
            
            if (GeneralUtilities.safeEquals(superName, stopName)) {
                List<? extends TypeMirror> genericParams = superDeclared.getTypeArguments();
                if (genericParams == null || genericParams.isEmpty()) {
                    throw new IllegalStateException("Class " + currentName + " which is a superclass of " + stopName +
                            " does is not a parameterized type");
                }
                
                if (paramIndex >= genericParams.size()) {
                    throw new IllegalStateException("Class " + currentName + " which is a superclass of " + stopName +
                            " does not have " + paramIndex + " types.  It only has " + genericParams.size());
                    
                }
                
                TypeMirror tpe = genericParams.get(paramIndex);
                if (!(tpe instanceof DeclaredType)) return null;
                DeclaredType retValDecl = (DeclaredType) tpe;
                
                TypeElement retValElement = (TypeElement) retValDecl.asElement();
                
                return new TypeElementAltClassImpl(retValElement, processingEnv);
            }
            
            currentClass = nextClass;
        }
        
        return null;
    }

    /* (non-Javadoc)
     * @see org.glassfish.hk2.xml.internal.alt.AltClass#isInterface()
     */
    @Override
    public boolean isInterface() {
        return ElementKind.INTERFACE.equals(clazz.getKind());
    }

    /* (non-Javadoc)
     * @see org.glassfish.hk2.xml.internal.alt.AltClass#isArray()
     */
    @Override
    public boolean isArray() {
        return false;
    }

    /* (non-Javadoc)
     * @see org.glassfish.hk2.xml.internal.alt.AltClass#getComponentType()
     */
    @Override
    public AltClass getComponentType() {
        return null;
    }
    
    @Override
    public int hashCode() {
        return getName().hashCode();
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof AltClass)) return false;
        AltClass oac = (AltClass) o;
        
        return GeneralUtilities.safeEquals(oac.getName(), getName());
    }

    @Override
    public String toString() {
        return "TypeElementAltClassImpl(" + clazz.getQualifiedName() + "," + System.identityHashCode(this) + ")";
    }

    

    
}
