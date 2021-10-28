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
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import org.glassfish.hk2.xml.internal.Utilities;
import org.glassfish.hk2.xml.internal.alt.AltAnnotation;
import org.glassfish.hk2.xml.internal.alt.AltClass;
import org.glassfish.hk2.xml.internal.alt.AltMethod;
import org.glassfish.hk2.xml.internal.alt.MethodInformationI;
import org.glassfish.hk2.xml.internal.alt.clazz.ClassAltClassImpl;

/**
 * @author jwells
 *
 */
public class ElementAltMethodImpl implements AltMethod {
    private final ExecutableElement method;
    private final ProcessingEnvironment processingEnv;
    private List<AltClass> parameters;
    private AltClass returnType;
    private Map<String, AltAnnotation> annotations;
    private MethodInformationI methodInformation;
    
    public ElementAltMethodImpl(Element method, ProcessingEnvironment processingEnv) {
        this.method = (ExecutableElement) method;
        this.processingEnv = processingEnv;
    }

    /* (non-Javadoc)
     * @see org.glassfish.hk2.xml.internal.alt.AltMethod#getName()
     */
    @Override
    public String getName() {
        return Utilities.convertNameToString(method.getSimpleName());
    }

    /* (non-Javadoc)
     * @see org.glassfish.hk2.xml.internal.alt.AltMethod#getReturnType()
     */
    @Override
    public synchronized AltClass getReturnType() {
        if (returnType != null) return returnType;
        
        ExecutableType executable = (ExecutableType) method.asType();
        TypeMirror returnMirror = executable.getReturnType();
        
        AltClass retVal = Utilities.convertTypeMirror(returnMirror, processingEnv);
        
        returnType = retVal;
        return returnType;
    }

    /* (non-Javadoc)
     * @see org.glassfish.hk2.xml.internal.alt.AltMethod#getParameterTypes()
     */
    @Override
    public synchronized List<AltClass> getParameterTypes() {
        if (parameters != null) return parameters;
        
        ExecutableType executable = (ExecutableType) method.asType();
        List<? extends TypeMirror> paramMirrors = executable.getParameterTypes();
        
        List<AltClass> retVal = new ArrayList<AltClass>(paramMirrors.size());
        
        for (TypeMirror paramMirror : paramMirrors) {
            retVal.add(Utilities.convertTypeMirror(paramMirror, processingEnv));
        }
        
        parameters = Collections.unmodifiableList(retVal);
        return parameters;
    }

    /* (non-Javadoc)
     * @see org.glassfish.hk2.xml.internal.alt.AltMethod#getFirstTypeArgument()
     */
    @Override
    public AltClass getFirstTypeArgument() {
        TypeMirror typeMirror = method.getReturnType();
        if (!(typeMirror instanceof DeclaredType)) return null;
        
        
        
        DeclaredType declaredReturn = (DeclaredType) typeMirror;
        List<? extends TypeMirror> types = declaredReturn.getTypeArguments();
        if (types == null || types.size() < 1) return null;
        
        TypeMirror firstTypeMirror = types.get(0);
        
        return Utilities.convertTypeMirror(firstTypeMirror, processingEnv);
    }

    /* (non-Javadoc)
     * @see org.glassfish.hk2.xml.internal.alt.AltMethod#getFirstTypeArgumentOfParameter(int)
     */
    @Override
    public AltClass getFirstTypeArgumentOfParameter(int index) {
        VariableElement ve = method.getParameters().get(index);
        TypeMirror tm = ve.asType();
        if (!TypeKind.DECLARED.equals(tm.getKind())) {
            return ClassAltClassImpl.OBJECT;
        }
        
        DeclaredType dt = (DeclaredType) tm;
        List<? extends TypeMirror> typeParams = dt.getTypeArguments();
        if (typeParams == null || typeParams.size() < 1) {
            return ClassAltClassImpl.OBJECT;
        }
        
        TypeMirror firstTypeParam = typeParams.get(0);
        if (!TypeKind.DECLARED.equals(firstTypeParam.getKind())) {
            return ClassAltClassImpl.OBJECT;
        }
        
        Element ele = ((DeclaredType) firstTypeParam).asElement();
        if (!(ele instanceof TypeElement)) {
            return ClassAltClassImpl.OBJECT;
        }
        
        return new TypeElementAltClassImpl((TypeElement) ele, processingEnv);
    }

    /* (non-Javadoc)
     * @see org.glassfish.hk2.xml.internal.alt.AltMethod#getAnnotation(java.lang.String)
     */
    @Override
    public synchronized AltAnnotation getAnnotation(String annotation) {
        if (annotations == null) {
            getAnnotations();
        }
        
        return annotations.get(annotation);
    }

    /* (non-Javadoc)
     * @see org.glassfish.hk2.xml.internal.alt.AltMethod#getAnnotations()
     */
    @Override
    public synchronized List<AltAnnotation> getAnnotations() {
        if (annotations != null) {
            return Collections.unmodifiableList(new ArrayList<AltAnnotation>(annotations.values()));
        }
        
        Map<String, AltAnnotation> retVal = new LinkedHashMap<String, AltAnnotation>();
        
        for (AnnotationMirror annoMirror : method.getAnnotationMirrors()) {
            AnnotationMirrorAltAnnotationImpl addMe = new AnnotationMirrorAltAnnotationImpl(annoMirror, processingEnv);
            
            retVal.put(addMe.annotationType(), addMe);
        }
        
        annotations = Collections.unmodifiableMap(retVal);
        return Collections.unmodifiableList(new ArrayList<AltAnnotation>(annotations.values()));
    }
    
    @Override
    public void setMethodInformation(MethodInformationI methodInfo) {
        methodInformation = methodInfo;
    }

    @Override
    public MethodInformationI getMethodInformation() {
        return methodInformation;
    }

    /* (non-Javadoc)
     * @see org.glassfish.hk2.xml.internal.alt.AltMethod#isVarArgs()
     */
    @Override
    public boolean isVarArgs() {
        return method.isVarArgs();
    }

    @Override
    public String toString() {
        return "ElementAltMethodImpl(" + method + ")";
    }
}
