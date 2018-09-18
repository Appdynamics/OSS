/*************************************************************************
 * 
 * ADOBE CONFIDENTIAL
 * __________________
 * 
 *  [2002] - [2007] Adobe Systems Incorporated 
 *  All Rights Reserved.
 * 
 * NOTICE:  All information contained herein is, and remains
 * the property of Adobe Systems Incorporated and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Adobe Systems Incorporated
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Adobe Systems Incorporated.
 */
package flex.messaging.io;

import java.util.List;
import java.io.Externalizable;
import java.io.Serializable;

import flex.messaging.io.amf.ASObject;
import flex.messaging.util.ClassUtil;

/**
 * Simple abstract implementation of PropertyProxy's common properties. Specific
 * sub-classes need to provide the full implementation focusing on the retrieval 
 * of the instance traits or "list of properties" and a specific value for
 * a given property name.
 * 
 * @see flex.messaging.io.PropertyProxy
 */
public abstract class AbstractProxy implements PropertyProxy, Serializable
{
    protected Object defaultInstance;
    protected String alias;
    protected boolean dynamic;
    protected boolean externalizable;
    protected boolean includeReadOnly;
    protected SerializationDescriptor descriptor;
    protected SerializationContext context;

    protected AbstractProxy(Object defaultInstance)
    {
        this.defaultInstance = defaultInstance;
        if (defaultInstance != null)
        {
            alias = defaultInstance.getClass().getName();
        }
    }
    
    public Object getDefaultInstance()
    {
        return defaultInstance;
    }

    public void setDefaultInstance(Object instance)
    {
        defaultInstance = instance;
    }

    /** 
     * A utility method which returns the Class from the given Class name
     * using the current type context's class loader.
     */
    public static Class getClassFromClassName(String className)
    {
        TypeMarshallingContext typeContext = TypeMarshallingContext.getTypeMarshallingContext();
        return ClassUtil.createClass(className, typeContext.getClassLoader());
    }
    
    /**
     * A utility method which creates an instance from a given class name.  It assumes
     * the class has a zero arg constructor.
     */
    public static Object createInstanceFromClassName(String className)
    {
        Class desiredClass = getClassFromClassName(className);
        return ClassUtil.createDefaultInstance(desiredClass, null);
    }

    public Object createInstance(String className)
    {
        Object instance;

        if (className == null || className.length() == 0)
        {
            instance = new ASObject();
        }
        else if (className.startsWith(">")) // Handle [RemoteClass] (no server alias)
        {
            instance = new ASObject();
            ((ASObject)instance).setType(className);
        }
        else
        {
            SerializationContext context = getSerializationContext();
            if (context.instantiateTypes || className.startsWith("flex."))
            {
                return createInstanceFromClassName(className);
            }
            else
            {
                // Just return type info with an ASObject...
                instance = new ASObject();
                ((ASObject)instance).setType(className);
            }
        }
        return instance;
    }

    public List getPropertyNames()
    {
        return getPropertyNames(getDefaultInstance());
    }

    public Class getType(String propertyName)
    {
        return getType(getDefaultInstance(), propertyName);
    }
    
    public Object getValue(String propertyName)
    {
        return getValue(getDefaultInstance(), propertyName);
    }

    public void setValue(String propertyName, Object value)
    {
        setValue(getDefaultInstance(), propertyName, value);
    }

    public void setAlias(String value)
    {
        alias = value;
    }

    public String getAlias()
    {
        return alias;
    }

    public void setDynamic(boolean value)
    {
        dynamic = value;
    }

    public boolean isDynamic()
    {
        return dynamic;
    }

    public boolean isExternalizable()
    {
        return externalizable;
    }

    public void setExternalizable(boolean value)
    {
        externalizable = value;
    }

    public boolean isExternalizable(Object instance)
    {
        return instance instanceof Externalizable;
    }

    public SerializationContext getSerializationContext()
    {
        if (context == null)
        {
            return SerializationContext.getSerializationContext();
        }
        return context;
    }

    public void setSerializationContext(SerializationContext value)
    {
        context = value;
    }

    public void setIncludeReadOnly(boolean value)
    {
        includeReadOnly = value;
    }

    public boolean getIncludeReadOnly()
    {
        return includeReadOnly;
    }

    public SerializationDescriptor getDescriptor()
    {
        return descriptor;
    }

    public void setDescriptor(SerializationDescriptor descriptor)
    {
        this.descriptor = descriptor;
    }

    /**
     * This is called after the serialization finishes.  We return the same object
     * here... this is an opportunity to replace the instance we use once we have
     * gathered all of the state into a temporary object. 
     */
    public Object instanceComplete(Object instance)
    {
        return instance;
    }

    /**
     * Returns the instance to serialize in place of the supplied instance.
     */
    public Object getInstanceToSerialize(Object instance)
    {
        return instance;
    }
    
    public Object clone()
    {
        return null;
    }

    public String toString()
    {
        if (defaultInstance != null)
            return "[Proxy(inst=" + defaultInstance + ") proxyClass=" + getClass() + " descriptor=" + descriptor + "]";
        else
            return "[Proxy(proxyClass=" + getClass() + " descriptor=" + descriptor + "]";
    }

    protected void setCloneFieldsFrom(AbstractProxy source)
    {
        setDescriptor(source.getDescriptor());
        setDefaultInstance(source.getDefaultInstance());
        context = source.context;
        includeReadOnly = source.includeReadOnly;
    }
}
