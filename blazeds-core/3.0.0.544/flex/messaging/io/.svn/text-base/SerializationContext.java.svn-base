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

import java.io.Serializable;

import flex.messaging.util.ClassUtil;

/**
 * A simple context to get settings from an endpoint to a deserializer
 * or serializer.
 *
 * @author Peter Farland
 */
public class SerializationContext implements Serializable
{
    static final long serialVersionUID = -8260273707611027509L;

    // Endpoint serialization configuration flags
    public boolean legacyXMLDocument;
    public boolean legacyXMLNamespaces;
    public boolean legacyCollection;
    public boolean legacyMap;
    public boolean legacyThrowable;
    public boolean legacyBigNumbers;
    public boolean restoreReferences;
    public boolean supportRemoteClass;
    public boolean supportDatesByReference; // Typically used by AMF Version 3 requests

    /**
     * Provides a way to control whether small messages should be sent even
     * if the client can support them. If set to false, small messages
     * will not be sent.
     * 
     * The default is true.
     */
    public boolean enableSmallMessages = true;

    /**
     * Determines whether type information will be used to instantiate a new instance.
     * If set to false, types will be deserialized as flex.messaging.io.ASObject instances
     * with type information retained but not used to create an instance.
     * 
     * Note that types in the flex.* package (and any subpackage) will always be
     * instantiated.
     * 
     * The default is true.
     */
    public boolean instantiateTypes = true;
    public boolean ignorePropertyErrors = true;
    public boolean logPropertyErrors = false;

    private Class deserializer;
    private Class serializer;

    public SerializationContext()
    {
    }

    public Class getDeserializerClass()
    {
        return deserializer;
    }

    public void setDeserializerClass(Class c)
    {
        deserializer = c;
    }

    public Class getSerializerClass()
    {
        return serializer;
    }

    public void setSerializerClass(Class c)
    {
        serializer = c;
    }

    public MessageDeserializer newMessageDeserializer()
    {
        MessageDeserializer deserializer = (MessageDeserializer)ClassUtil.createDefaultInstance(getDeserializerClass(), MessageDeserializer.class);
        return deserializer; 
    }

    public MessageSerializer newMessageSerializer()
    {
        MessageSerializer serializer = (MessageSerializer)ClassUtil.createDefaultInstance(getSerializerClass(), MessageSerializer.class);
        return serializer; 
    }

    public Object clone()
    {
        SerializationContext context = new SerializationContext();
        context.legacyXMLDocument = legacyXMLDocument;
        context.legacyXMLNamespaces = legacyXMLNamespaces;
        context.legacyCollection = legacyCollection;
        context.legacyMap = legacyMap;
        context.legacyThrowable = legacyThrowable;
        context.legacyBigNumbers = legacyBigNumbers;
        context.restoreReferences = restoreReferences;
        context.supportRemoteClass = supportRemoteClass;
        context.supportDatesByReference = supportDatesByReference; // Typically used by AMF Version 3 requests
        context.instantiateTypes = instantiateTypes;
        context.ignorePropertyErrors = ignorePropertyErrors;
        context.logPropertyErrors = logPropertyErrors;
        context.deserializer = deserializer;
        context.serializer = serializer;
        return context;
    }

    private static ThreadLocal contexts = new ThreadLocal();
    
    /**
     * Establishes a SerializationContext for the current thread.
     * Users are not expected to call this function.
     * @param context The current SerializationContext.
     */
    public static void setSerializationContext(SerializationContext context)
    {
        contexts.set(context);
    }

    /**
     * @return The current thread's SerializationContext.
     */
    public static SerializationContext getSerializationContext()
    {
        SerializationContext sc = (SerializationContext)contexts.get();
        if (sc == null)
        {
            sc = new SerializationContext();
            SerializationContext.setSerializationContext(sc);
        }
        return sc;
    }
}
