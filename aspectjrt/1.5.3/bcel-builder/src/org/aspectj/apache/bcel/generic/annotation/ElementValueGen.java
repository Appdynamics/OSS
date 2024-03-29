/* *******************************************************************
 * Copyright (c) 2004 IBM
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 *  
 * Contributors: 
 *     Andy Clement -     initial implementation {date}
 * ******************************************************************/

package org.aspectj.apache.bcel.generic.annotation;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.aspectj.apache.bcel.classfile.annotation.AnnotationElementValue;
import org.aspectj.apache.bcel.classfile.annotation.ArrayElementValue;
import org.aspectj.apache.bcel.classfile.annotation.ClassElementValue;
import org.aspectj.apache.bcel.classfile.annotation.ElementValue;
import org.aspectj.apache.bcel.classfile.annotation.EnumElementValue;
import org.aspectj.apache.bcel.classfile.annotation.SimpleElementValue;
import org.aspectj.apache.bcel.generic.ConstantPoolGen;

public abstract class ElementValueGen {

	protected int type;
	protected ConstantPoolGen cpGen;
	
	
    protected ElementValueGen(int type,ConstantPoolGen cpGen) {
    	this.type = type;
    	this.cpGen = cpGen;
    }
	
	/**
     * Subtypes return an immutable variant of the ElementValueGen
     */
	public abstract ElementValue getElementValue();
    
	public int getElementValueType() {
    	return type;
    }
	
	public abstract String stringifyValue();
	public abstract void dump(DataOutputStream dos) throws IOException;
    
	public static final int STRING = 's';
	public static final int ENUM_CONSTANT = 'e';
	public static final int CLASS = 'c';
	public static final int ANNOTATION = '@';
    public static final int ARRAY = '[';
    
    public static final int PRIMITIVE_INT    = 'I';
    public static final int PRIMITIVE_BYTE   = 'B';
    public static final int PRIMITIVE_CHAR   = 'C';
    public static final int PRIMITIVE_DOUBLE = 'D';
    public static final int PRIMITIVE_FLOAT  = 'F';
    public static final int PRIMITIVE_LONG   = 'J';
    public static final int PRIMITIVE_SHORT  = 'S';
    public static final int PRIMITIVE_BOOLEAN= 'Z';    
    
    public static ElementValueGen readElementValue(DataInputStream dis,ConstantPoolGen cpGen) throws IOException {
		int type= dis.readUnsignedByte();
		switch (type) {
		  case 'B': // byte
		  	return new SimpleElementValueGen(PRIMITIVE_BYTE,dis.readUnsignedShort(),cpGen);
		  case 'C': // char
		  	return new SimpleElementValueGen(PRIMITIVE_CHAR,dis.readUnsignedShort(),cpGen);
		  case 'D': // double
		  	return new SimpleElementValueGen(PRIMITIVE_DOUBLE,dis.readUnsignedShort(),cpGen);
		  case 'F': // float
		  	return new SimpleElementValueGen(PRIMITIVE_FLOAT,dis.readUnsignedShort(),cpGen);
		  case 'I': // int
		  	return new SimpleElementValueGen(PRIMITIVE_INT,dis.readUnsignedShort(),cpGen);
		  case 'J': // long
		  	return new SimpleElementValueGen(PRIMITIVE_LONG,dis.readUnsignedShort(),cpGen);
		  case 'S': // short
		  	return new SimpleElementValueGen(PRIMITIVE_SHORT,dis.readUnsignedShort(),cpGen);
		  case 'Z': // boolean
		  	return new SimpleElementValueGen(PRIMITIVE_BOOLEAN,dis.readUnsignedShort(),cpGen);		  	
		  case 's': // String
		  	return new SimpleElementValueGen(STRING,dis.readUnsignedShort(),cpGen);

		  case 'e': // Enum constant
		  	return new EnumElementValueGen(dis.readUnsignedShort(),dis.readUnsignedShort(),cpGen);

		  case 'c': // Class
		  	return new ClassElementValueGen(dis.readUnsignedShort(),cpGen);
//
//		  case '@': // Annotation
//		  	return new AnnotationElementValueGen(ANNOTATION,Annotation.read(dis,cpGen),cpGen);
//		  	
//		  case '[': // Array
//		  	int numArrayVals = dis.readUnsignedShort();
//		  	List arrayVals = new ArrayList();
//		  	ElementValue[] evalues = new ElementValue[numArrayVals];
//		  	for (int j=0;j<numArrayVals;j++) {
//		  		evalues[j] = ElementValue.readElementValue(dis,cpGen);
//		  	}
//		  	return new ArrayElementValue(ARRAY,evalues,cpGen);

		  default: 
		  	throw new RuntimeException("Unexpected element value kind in annotation: "+type);
		}
	}

	protected ConstantPoolGen getConstantPool() {
		return cpGen;
	}

	/** 
	 * Creates an (modifiable) ElementValueGen copy of an (immutable) ElementValue - constant pool is assumed correct.
	 */
	public static ElementValueGen copy(ElementValue value,ConstantPoolGen cpool,boolean copyPoolEntries) {
		switch (value.getElementValueType()) {
			 case 'B': // byte
			 case 'C': // char
			 case 'D': // double
			 case 'F': // float
			 case 'I': // int
			 case 'J': // long
			 case 'S': // short
			 case 'Z': // boolean
			 case 's': // String
			  return new SimpleElementValueGen((SimpleElementValue)value,cpool,copyPoolEntries);
				
			 case 'e': // Enum constant
			  	return new EnumElementValueGen((EnumElementValue)value,cpool,copyPoolEntries);

			 case '@': // Annotation
			  	return new AnnotationElementValueGen((AnnotationElementValue)value,cpool,copyPoolEntries);
			  	
		     case '[': // Array
		     	return new ArrayElementValueGen((ArrayElementValue)value,cpool,copyPoolEntries);

		     case 'c': // Class
		     	return new ClassElementValueGen((ClassElementValue)value,cpool,copyPoolEntries);
		     	
			default:
				throw new RuntimeException("Not implemented yet! ("+value.getElementValueType()+")");
		}
	}
}
