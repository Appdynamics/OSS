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

package org.glassfish.hk2.json.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.URI;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import javax.xml.bind.Unmarshaller.Listener;
import javax.xml.namespace.QName;

import org.glassfish.hk2.api.DescriptorVisibility;
import org.glassfish.hk2.api.Visibility;
import org.glassfish.hk2.json.api.JsonUtilities;
import org.glassfish.hk2.xml.api.XmlRootHandle;
import org.glassfish.hk2.xml.api.XmlService;
import org.glassfish.hk2.xml.internal.ChildDataModel;
import org.glassfish.hk2.xml.internal.ChildDescriptor;
import org.glassfish.hk2.xml.internal.ChildType;
import org.glassfish.hk2.xml.internal.ModelImpl;
import org.glassfish.hk2.xml.internal.ParentedModel;
import org.glassfish.hk2.xml.internal.Utilities;
import org.glassfish.hk2.xml.jaxb.internal.BaseHK2JAXBBean;
import org.glassfish.hk2.xml.spi.Model;
import org.glassfish.hk2.xml.spi.PreGenerationRequirement;
import org.glassfish.hk2.xml.spi.XmlServiceParser;

/**
 * @author jwells
 *
 */
@Singleton
@Named(JsonUtilities.JSON_SERVICE_NAME)
@Visibility(DescriptorVisibility.LOCAL)
public class JsonParser implements XmlServiceParser {
    private void skipper(javax.json.stream.JsonParser parser) {
        if (!parser.hasNext()) return;
        
        for(;;) {
            javax.json.stream.JsonParser.Event event = parser.next();
            switch (event) {
            case KEY_NAME:
            case VALUE_FALSE:
            case VALUE_TRUE:
            case VALUE_NULL:
            case VALUE_STRING:
            case VALUE_NUMBER:
                // Skipping done
                break;
            case START_OBJECT:
            case START_ARRAY:
                skipper(parser);
                break;
            case END_OBJECT:
            case END_ARRAY:
                return;
            }
        }
    }
    
    @Inject @Named(JsonUtilities.JSON_SERVICE_NAME)
    private Provider<XmlService> xmlService;
    
    private void parseObject(ModelImpl currentModel, BaseHK2JAXBBean target, BaseHK2JAXBBean parent, Listener listener,
            javax.json.stream.JsonParser parser) {
        try {
            listener.beforeUnmarshal(target, parent);
        }
        catch (RuntimeException re) {
            throw re;
        }
        catch (Throwable th) {
            // TODO: Log
            throw new RuntimeException(th);
        }
        
        if (!parser.hasNext()) {
            throw new IllegalStateException("Expectin an end token from Json parser");
        }
        
        boolean getNextEvent = true;
        do {
            javax.json.stream.JsonParser.Event event = parser.next();
            switch(event) {
            case END_OBJECT:
                try {
                    listener.afterUnmarshal(target, parent);
                }
                catch (RuntimeException re) {
                    throw re;
                }
                catch (Throwable th) {
                    // TODO: Log
                    throw new RuntimeException(th);
                }
                
                getNextEvent = false;
                break;
            case KEY_NAME:
                String keyName = parser.getString();
                QName qKeyName = new QName(keyName);
                
                ChildDescriptor descriptor = currentModel.getChildDescriptor(qKeyName);
                if (descriptor == null) {
                    skipper(parser);
                }
                else {
                    ParentedModel parentedModel = descriptor.getParentedModel();
                    if (parentedModel == null) {
                        ChildDataModel childDataModel = descriptor.getChildDataModel();
                    
                        javax.json.stream.JsonParser.Event attributeEvent = parser.next();
                        switch (attributeEvent) {
                        case VALUE_STRING:
                            target._setProperty(XmlService.DEFAULT_NAMESPACE, keyName, parser.getString());
                            break;
                        case VALUE_NUMBER:
                            if (parser.isIntegralNumber()) {
                                target._setProperty(XmlService.DEFAULT_NAMESPACE, keyName, new Integer(parser.getInt()));
                            }
                            else {
                                target._setProperty(XmlService.DEFAULT_NAMESPACE, keyName, new Long(parser.getLong()));
                            }
                            break;
                        case VALUE_NULL:
                            target._setProperty(XmlService.DEFAULT_NAMESPACE, keyName, null);
                            break;
                        case VALUE_TRUE:
                            target._setProperty(XmlService.DEFAULT_NAMESPACE, keyName, Boolean.TRUE);
                            break;
                        case VALUE_FALSE:
                            target._setProperty(XmlService.DEFAULT_NAMESPACE, keyName, Boolean.FALSE);
                            break;
                        default:
                            throw new IllegalStateException("Uknown value type: " + attributeEvent + " for " + childDataModel + " for " + currentModel);
                        }
                    }
                    else {
                        ModelImpl childModel = parentedModel.getChildModel();
                    
                        javax.json.stream.JsonParser.Event childTypeEvent = parser.next();
                    
                        if (javax.json.stream.JsonParser.Event.START_ARRAY.equals(childTypeEvent)) {
                            List<BaseHK2JAXBBean> myList = new LinkedList<BaseHK2JAXBBean>();
                        
                            for (;;) {
                                javax.json.stream.JsonParser.Event arrayEvent = parser.next();
                                if (javax.json.stream.JsonParser.Event.END_ARRAY.equals(arrayEvent)) {
                                    // Finished loop!
                                    break;
                                }
                            
                                if (!javax.json.stream.JsonParser.Event.START_OBJECT.equals(arrayEvent)) {
                                    throw new AssertionError("Do not know how to handle this case inside an array expecting an object" + arrayEvent);
                                }
                            
                                BaseHK2JAXBBean oneChild = Utilities.createBean(childModel.getProxyAsClass());
                            
                                parseObject(childModel, oneChild, target, listener, parser);
                            
                                myList.add(oneChild);
                            }
                        
                            if (ChildType.LIST.equals(parentedModel.getChildType())) {
                                target._setProperty(XmlService.DEFAULT_NAMESPACE, keyName, myList);
                            }
                            else if (ChildType.ARRAY.equals(parentedModel.getChildType())) {
                                Object array = Array.newInstance(childModel.getOriginalInterfaceAsClass(), myList.size());
                            
                                int lcv = 0;
                                for (BaseHK2JAXBBean bean : myList) {
                                    Array.set(array, lcv, bean);
                                    lcv++;
                                }
                            
                                target._setProperty(XmlService.DEFAULT_NAMESPACE, keyName, array);
                            }
                            else {
                                throw new AssertionError("The model says DIRECT but I got an ARRAY start so bombing quite badly");
                            }
                        
                        }
                        else if (javax.json.stream.JsonParser.Event.START_OBJECT.equals(childTypeEvent)) {
                            if (!ChildType.DIRECT.equals(parentedModel.getChildType())) {
                                throw new AssertionError("The model says " + parentedModel.getChildType() + " but I got an START_OBJECT start so bombing quite badly");
                            }
                        
                            BaseHK2JAXBBean oneChild = Utilities.createBean(childModel.getProxyAsClass());
                        
                            parseObject(childModel, oneChild, target, listener, parser);
                        
                            target._setProperty(XmlService.DEFAULT_NAMESPACE, keyName, oneChild);
                        }
                        else {
                            throw new IllegalStateException("Unknown start of child event: " + event);
                        }
                    }
                }
                break;
            default:
                throw new IllegalArgumentException("Unkown event: " + event);
            }
        }
        while (getNextEvent);
         
        
    }

    /* (non-Javadoc)
     * @see org.glassfish.hk2.xml.spi.XmlServiceParser#parseRoot(org.glassfish.hk2.xml.spi.Model, java.net.URI, javax.xml.bind.Unmarshaller.Listener)
     */
    @Override
    public <T> T parseRoot(Model rootModel, URI location, Listener listener, Map<String, Object> options)
            throws Exception {
        InputStream input = location.toURL().openStream();
        try {
            return parseRoot(rootModel, input, listener, options);
        }
        finally {
            input.close();
        }
    }
    
    /* (non-Javadoc)
     * @see org.glassfish.hk2.xml.spi.XmlServiceParser#parseRoot(org.glassfish.hk2.xml.spi.Model, java.net.URI, javax.xml.bind.Unmarshaller.Listener)
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T parseRoot(Model rootModel, InputStream input, Listener listener, Map<String, Object> options)
            throws Exception {
        javax.json.stream.JsonParser parser = Json.createParser(input);
        
        try {
            if (!parser.hasNext()) {
                T root = (T) Utilities.createBean(rootModel.getProxyAsClass());
                
                listener.beforeUnmarshal(root, null);
                listener.afterUnmarshal(root, null);
                
                return root;
            }
            
            javax.json.stream.JsonParser.Event event = parser.next();
            if (!javax.json.stream.JsonParser.Event.START_OBJECT.equals(event)) {
                throw new AssertionError("Unknown start of JSON object: " + event);
            }
            
            BaseHK2JAXBBean root = Utilities.createBean(rootModel.getProxyAsClass());
            
            parseObject((ModelImpl) rootModel, root, null, listener, parser);
            
            return (T) root;
        }
        finally {
            parser.close();
        }
    }

    /* (non-Javadoc)
     * @see org.glassfish.hk2.xml.spi.XmlServiceParser#getPreGenerationRequirement()
     */
    @Override
    public PreGenerationRequirement getPreGenerationRequirement() {
        return PreGenerationRequirement.LAZY_PREGENERATION;
    }

    /* (non-Javadoc)
     * @see org.glassfish.hk2.xml.spi.XmlServiceParser#marshall(java.io.OutputStream, org.glassfish.hk2.xml.api.XmlRootHandle)
     */
    @Override
    public <T> void marshal(OutputStream outputStream, XmlRootHandle<T> rootHandle, Map<String, Object> options)
            throws IOException {
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        
        T root = rootHandle.getRoot();
        
        JsonObject rootObject = createJsonObject((BaseHK2JAXBBean) root, objectBuilder);
        
        Map<String, Object> config = new HashMap<String, Object>();
        config.put(JsonGenerator.PRETTY_PRINTING, Boolean.TRUE);
        
        JsonWriterFactory writerFactory = Json.createWriterFactory(config);
        JsonWriter writer = writerFactory.createWriter(outputStream);
        try {
            writer.writeObject(rootObject);
        }
        finally {
            writer.close();
        }
    }
    
    @SuppressWarnings("unchecked")
    private JsonObject createJsonObject(BaseHK2JAXBBean bean, JsonObjectBuilder builder) {
        if (bean == null) {
            return builder.build();
        }
        
        ModelImpl model = bean._getModel();
        Map<QName, ChildDescriptor> allChildren = model.getAllChildrenDescriptors();
        for (Map.Entry<QName, ChildDescriptor> entry : allChildren.entrySet()) {
            QName keyNameQName = entry.getKey();
            String keyName = keyNameQName.getLocalPart();
            
            if (!bean._isSet(keyName)) continue;
            Object value = bean._getProperty(XmlService.DEFAULT_NAMESPACE, keyName);
            
            ParentedModel parented = entry.getValue().getParentedModel();
            if (parented != null) {
                if (ChildType.DIRECT.equals(parented.getChildType())) {
                    if (value != null) {
                        builder = builder.add(keyName, createJsonObject((BaseHK2JAXBBean) value, Json.createObjectBuilder()));
                    }
                }
                else if (ChildType.LIST.equals(parented.getChildType())) {
                    List<BaseHK2JAXBBean> list = (List<BaseHK2JAXBBean>) value;
                    if (list != null && !list.isEmpty()) {
                        JsonArrayBuilder jsonArray = Json.createArrayBuilder();
                        
                        for (BaseHK2JAXBBean item : list) {
                            JsonObjectBuilder childBuilder = Json.createObjectBuilder();
                            
                            JsonObject obj = createJsonObject(item, childBuilder);
                            jsonArray.add(obj);
                        }
                        
                        builder.add(keyName, jsonArray);
                    }
                }
                else if (ChildType.ARRAY.equals(parented.getChildType())) {
                    int length = Array.getLength(value);
                    if (length > 0) {
                        JsonArrayBuilder jsonArray = Json.createArrayBuilder();
                        
                        for (int lcv = 0; lcv < length; lcv++) {
                            BaseHK2JAXBBean item = (BaseHK2JAXBBean) Array.get(value, lcv);
                            
                            JsonObjectBuilder childBuilder = Json.createObjectBuilder();
                            
                            JsonObject obj = createJsonObject(item, childBuilder);
                            jsonArray.add(obj);
                        }
                        
                        builder.add(keyName, jsonArray);
                    }
                }
                else {
                    throw new AssertionError("Unknown childType " + parented.getChildType());
                }
            }
            else {
                if (value == null) {
                    builder.addNull(keyName);
                }
                else if (value instanceof Integer) {
                    builder.add(keyName, ((Integer) value).intValue());
                }
                else if (value instanceof Long) {
                    builder.add(keyName, ((Long) value).longValue());
                }
                else if (value instanceof Boolean) {
                    builder.add(keyName, ((Boolean) value).booleanValue());
                }
                else {
                    builder.add(keyName, value.toString());
                }
            }
            
        }
        
        return builder.build();
    }

}
