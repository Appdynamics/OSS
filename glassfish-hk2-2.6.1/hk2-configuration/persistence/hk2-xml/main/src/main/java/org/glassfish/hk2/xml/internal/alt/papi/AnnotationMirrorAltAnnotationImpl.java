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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import org.glassfish.hk2.xml.internal.Utilities;
import org.glassfish.hk2.xml.internal.alt.AltAnnotation;
import org.glassfish.hk2.xml.internal.alt.AltClass;
import org.glassfish.hk2.xml.internal.alt.AltEnum;

/**
 * @author jwells
 *
 */
public class AnnotationMirrorAltAnnotationImpl implements AltAnnotation {
    private final AnnotationMirror annotation;
    private final ProcessingEnvironment processingEnv;
    private String type;
    private Map<String, Object> values;
    
    public AnnotationMirrorAltAnnotationImpl(AnnotationMirror annotation, ProcessingEnvironment processingEnv) {
        this.annotation = annotation;
        this.processingEnv = processingEnv;
    }

    /* (non-Javadoc)
     * @see org.glassfish.hk2.xml.internal.alt.AltAnnotation#annotationType()
     */
    @Override
    public synchronized String annotationType() {
        if (type != null) return type;
        
        DeclaredType dt = annotation.getAnnotationType();
        TypeElement clazzType = (TypeElement) dt.asElement();
        type = Utilities.convertNameToString(clazzType.getQualifiedName());
        
        return type;
    }

    /* (non-Javadoc)
     * @see org.glassfish.hk2.xml.internal.alt.AltAnnotation#getStringValue(java.lang.String)
     */
    @Override
    public String getStringValue(String methodName) {
        getAnnotationValues();
        
        return (String) values.get(methodName);
    }

    /* (non-Javadoc)
     * @see org.glassfish.hk2.xml.internal.alt.AltAnnotation#getBooleanValue(java.lang.String)
     */
    @Override
    public boolean getBooleanValue(String methodName) {
        getAnnotationValues();
        
        return (Boolean) values.get(methodName);
    }
    
    @Override
    public synchronized String[] getStringArrayValue(String methodName) {
        getAnnotationValues();
        
        Object retVal = values.get(methodName);
        if (retVal == null || !(retVal instanceof String[])) {
            return null;
        }
        
        return (String[]) values.get(methodName);
    }
    
    @Override
    public AltAnnotation[] getAnnotationArrayValue(String methodName) {
        getAnnotationValues();
        
        return (AltAnnotation[]) values.get(methodName);
    }
    
    @Override
    public AltClass getClassValue(String methodName) {
        getAnnotationValues();
        
        return (AltClass) values.get(methodName);
    }

    /* (non-Javadoc)
     * @see org.glassfish.hk2.xml.internal.alt.AltAnnotation#getAnnotationValues()
     */
    @Override
    public synchronized Map<String, Object> getAnnotationValues() {
        if (values != null) return values;
        
        Map<? extends ExecutableElement, ? extends AnnotationValue> rawValues =
                processingEnv.getElementUtils().getElementValuesWithDefaults(annotation);
        Map<String, Object> retVal = new TreeMap<String, Object>();
        
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : rawValues.entrySet()) {
            ExecutableElement annoMethod = entry.getKey();
            AnnotationValue annoValue = entry.getValue();
            
            String key = Utilities.convertNameToString(annoMethod.getSimpleName());
            Object value = annoValue.getValue();
            
            if (value instanceof TypeMirror) {
                // The annotation method is a java.lang.Class
                value = Utilities.convertTypeMirror((TypeMirror) value, processingEnv);
            }
            else if (value instanceof VariableElement) {
                // The annotation method is an Enum
                VariableElement variable = (VariableElement) value;
                
                TypeElement enclosing = (TypeElement) variable.getEnclosingElement();
                
                String annoClassName = Utilities.convertNameToString(enclosing.getQualifiedName());
                String annoVal = Utilities.convertNameToString(variable.getSimpleName());
                
                value = new StringAltEnumImpl(annoClassName, annoVal);
            }
            else if (value instanceof AnnotationMirror) {
                throw new AssertionError("The annotation " + annotation + " key " + key + " has unimplemented type AnnotationMirror");
            }
            else if (value instanceof List) {
                // The annotation method returns an array of something
                ArrayType returnType = (ArrayType) annoMethod.getReturnType();
                TypeMirror arrayTypeMirror = returnType.getComponentType();
                TypeKind arrayTypeKind = arrayTypeMirror.getKind();
                
                @SuppressWarnings("unchecked")
                List<? extends AnnotationValue> array = ((List<? extends AnnotationValue>) value);
                
                if (TypeKind.INT.equals(arrayTypeMirror.getKind())) {
                    int[] iValue = new int[array.size()];
                    
                    int lcv = 0;
                    for (AnnotationValue item : array) {
                        iValue[lcv++] = (Integer) item.getValue();
                    }
                    
                    value = iValue;
                }
                else if (TypeKind.DECLARED.equals(arrayTypeMirror.getKind())) {
                    AltClass[] cValue = new AltClass[array.size()];
                    AltEnum[] eValue = new AltEnum[array.size()];
                    String[] sValue = new String[array.size()];
                    AltAnnotation[] aValue = new AltAnnotation[array.size()];
                    
                    boolean isClass = true;
                    boolean isEnum = true;
                    boolean isAnnos = false;
                    int lcv = 0;
                    for (AnnotationValue item : array) {
                        Object itemValue = item.getValue();
                        if (itemValue instanceof TypeMirror) {
                            isClass = true;
                            isEnum = false;
                            isAnnos = false;
                            
                            cValue[lcv++] = Utilities.convertTypeMirror((TypeMirror) itemValue, processingEnv);
                        }
                        else if (itemValue instanceof VariableElement) {
                            isClass = false;
                            isEnum = true;
                            isAnnos = false;
                            
                            VariableElement variable = (VariableElement) itemValue;
                            
                            TypeElement enclosing = (TypeElement) variable.getEnclosingElement();
                            
                            String annoClassName = Utilities.convertNameToString(enclosing.getQualifiedName());
                            String annoVal = Utilities.convertNameToString(variable.getSimpleName());
                            
                            eValue[lcv++] = new StringAltEnumImpl(annoClassName, annoVal);
                        }
                        else if (itemValue instanceof String) {
                            isClass = false;
                            isEnum = false;
                            isAnnos = false;
                            
                            sValue[lcv++] = (String) itemValue;
                        }
                        else if (itemValue instanceof List) {
                            throw new AssertionError("Unimplemented declared List type in " + this);
                        }
                        else if (itemValue instanceof AnnotationMirror) {
                            isClass = false;
                            isEnum = false;
                            isAnnos = true;
                            
                            aValue[lcv++] = new AnnotationMirrorAltAnnotationImpl((AnnotationMirror) itemValue, processingEnv);
                        }
                        else {
                            throw new AssertionError("Unknown declared type: " + itemValue.getClass().getName());
                        }
                    }
                    
                    if (isClass) {
                        value = cValue;
                    }
                    else if (isEnum) {
                        value = eValue;
                    }
                    else if (isAnnos) {
                        value = aValue;
                    }
                    else {
                        value = sValue;
                    }
                }
                else if (TypeKind.LONG.equals(arrayTypeMirror.getKind())) {
                    long[] iValue = new long[array.size()];
                    
                    int lcv = 0;
                    for (AnnotationValue item : array) {
                        iValue[lcv++] = (Long) item.getValue();
                    }
                    
                    value = iValue;
                }
                else if (TypeKind.SHORT.equals(arrayTypeMirror.getKind())) {
                    short[] iValue = new short[array.size()];
                    
                    int lcv = 0;
                    for (AnnotationValue item : array) {
                        iValue[lcv++] = (Short) item.getValue();
                    }
                    
                    value = iValue;
                }
                else if (TypeKind.CHAR.equals(arrayTypeMirror.getKind())) {
                    char[] iValue = new char[array.size()];
                    
                    int lcv = 0;
                    for (AnnotationValue item : array) {
                        iValue[lcv++] = (Character) item.getValue();
                    }
                    
                    value = iValue;
                }
                else if (TypeKind.FLOAT.equals(arrayTypeMirror.getKind())) {
                    float[] iValue = new float[array.size()];
                    
                    int lcv = 0;
                    for (AnnotationValue item : array) {
                        iValue[lcv++] = (Float) item.getValue();
                    }
                    
                    value = iValue;
                }
                else if (TypeKind.DOUBLE.equals(arrayTypeMirror.getKind())) {
                    double[] iValue = new double[array.size()];
                    
                    int lcv = 0;
                    for (AnnotationValue item : array) {
                        iValue[lcv++] = (Double) item.getValue();
                    }
                    
                    value = iValue;
                }
                else if (TypeKind.BOOLEAN.equals(arrayTypeMirror.getKind())) {
                    boolean[] iValue = new boolean[array.size()];
                    
                    int lcv = 0;
                    for (AnnotationValue item : array) {
                        iValue[lcv++] = (Boolean) item.getValue();
                    }
                    
                    value = iValue;
                }
                else if (TypeKind.BYTE.equals(arrayTypeMirror.getKind())) {
                    byte[] iValue = new byte[array.size()];
                    
                    int lcv = 0;
                    for (AnnotationValue item : array) {
                        iValue[lcv++] = (Byte) item.getValue();
                    }
                    
                    value = iValue;
                    
                }
                else {
                    throw new AssertionError("Array type " + arrayTypeKind + " is not implemented");
                }
            }
            
            retVal.put(key, value);
        }
        
        values = Collections.unmodifiableMap(retVal);
        return values;
    }
    
    @Override
    public String toString() {
        return "AnnotationMirrorAltAnnotationImpl(" + annotationType() + ")";
    }

    

    

}
