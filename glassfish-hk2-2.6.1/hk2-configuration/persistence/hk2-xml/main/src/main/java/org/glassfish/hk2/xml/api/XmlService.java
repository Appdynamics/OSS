/*
 * Copyright (c) 2014, 2018 Oracle and/or its affiliates. All rights reserved.
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

package org.glassfish.hk2.xml.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Map;

import javax.xml.stream.XMLStreamReader;

import org.jvnet.hk2.annotations.Contract;

/**
 * This is the main service for parsing your XML files using some
 * pluggable parsing technology.
 * @author jwells
 *
 */
@Contract
public interface XmlService {
    /** The default namespace of the document */
    public static final String DEFAULT_NAMESPACE = "##default";
    
    /**
     * Unmarshalls the given URI using the jaxb annotated interface.  The resulting
     * JavaBean tree will be advertised in the ServiceLocator and in the Hub.
     * Will use the registered implementation of {@link org.glassfish.hk2.xml.spi.XmlServiceParser}
     * to parse the file.
     * 
     * @param uri The non-null URI whereby to find the xml corresponding to the class
     * @param jaxbAnnotatedClassOrInterface The non-null class corresonding to the Xml to be parsed
     * @return A non-null handle that can be used to get the unmarshalled data or perform
     * other tasks
     */
    public <T> XmlRootHandle<T> unmarshal(URI uri, Class<T> jaxbAnnotatedInterface);
    
    /**
     * Unmarshalls the given URI using the jaxb annotated interface.
     * Will use the registered implementation of {@link org.glassfish.hk2.xml.spi.XmlServiceParser}
     * to parse the file.
     * 
     * @param uri The non-null URI whereby to find the xml corresponding to the class
     * @param jaxbAnnotatedClassOrInterface The non-null interface corresponding to the Xml to be parsed
     * @param advertiseInRegistry if true the entire tree of parsed xml will be added to the
     * ServiceLocator
     * @param advertiseInHub if true the entire tree of parsed xml will be added to the
     * HK2 configuration Hub (as bean-like maps)
     * @return A non-null handle that can be used to get the unmarshalled data or perform
     * other tasks
     */
    public <T> XmlRootHandle<T> unmarshal(URI uri, Class<T> jaxbAnnotatedInterface,
            boolean advertiseInRegistry, boolean advertiseInHub);
    
    /**
     * Unmarshalls the given URI using the jaxb annotated interface.
     * Will use the registered implementation of {@link org.glassfish.hk2.xml.spi.XmlServiceParser}
     * to parse the file.
     * 
     * @param uri The non-null URI whereby to find the xml corresponding to the class
     * @param jaxbAnnotatedClassOrInterface The non-null interface corresponding to the Xml to be parsed
     * @param advertiseInRegistry if true the entire tree of parsed xml will be added to the
     * ServiceLocator
     * @param advertiseInHub if true the entire tree of parsed xml will be added to the
     * HK2 configuration Hub (as bean-like maps)
     * @param options optional (possibly null) options from the caller
     * @return A non-null handle that can be used to get the unmarshalled data or perform
     * other tasks
     */
    public <T> XmlRootHandle<T> unmarshal(URI uri, Class<T> jaxbAnnotatedInterface,
            boolean advertiseInRegistry, boolean advertiseInHub, Map<String, Object> options);
    
    /**
     * Unmarshals an XML stream using the jaxb annotated interface.
     * Will use a built-in algorithm to read the stream
     * 
     * @param reader The non-null XMLStreamReader representing the XML to be read
     * @param jaxbAnnotatedClassOrInterface The non-null interface corresponding to the Xml to be parsed
     * @param advertiseInRegistry if true the entire tree of parsed xml will be added to the
     * ServiceLocator
     * @param advertiseInHub if true the entire tree of parsed xml will be added to the
     * HK2 configuration Hub (as bean-like maps)
     * @return A non-null handle that can be used to get the unmarshalled data or perform
     * other tasks
     */
    public <T> XmlRootHandle<T> unmarshal(XMLStreamReader reader, Class<T> jaxbAnnotatedInterface,
            boolean advertiseInRegistry, boolean advertiseInHub);
    
    /**
     * Unmarshals an XML stream using the jaxb annotated interface.
     * Will use a built-in algorithm to read the stream
     * 
     * @param reader The non-null XMLStreamReader representing the XML to be read
     * @param jaxbAnnotatedClassOrInterface The non-null interface corresponding to the Xml to be parsed
     * @param advertiseInRegistry if true the entire tree of parsed xml will be added to the
     * ServiceLocator
     * @param advertiseInHub if true the entire tree of parsed xml will be added to the
     * HK2 configuration Hub (as bean-like maps)
     * @param options optional (possibly null) options from the caller
     * @return A non-null handle that can be used to get the unmarshalled data or perform
     * other tasks
     */
    public <T> XmlRootHandle<T> unmarshal(XMLStreamReader reader, Class<T> jaxbAnnotatedInterface,
            boolean advertiseInRegistry, boolean advertiseInHub, Map<String, Object> options);
    
    /**
     * Unmarshals an XML stream using the jaxb annotated interface.
     * Will use the registered implementation of {@link org.glassfish.hk2.xml.spi.XmlServiceParser}
     * to parse the file.
     * The beans will be included in the service registry and the configuration
     * hub as appropriate
     * 
     * @param inputStream The non-null input stream to read.  Will not close this stream
     * @param jaxbAnnotatedClassOrInterface The non-null interface corresponding to the Xml to be parsed
     * @return A non-null handle that can be used to get the unmarshalled data or perform
     * other tasks
     */
    public <T> XmlRootHandle<T> unmarshal(InputStream inputStream, Class<T> jaxbAnnotatedInterface);
    
    /**
     * Unmarshals an XML stream using the jaxb annotated interface.
     * Will use the registered implementation of {@link org.glassfish.hk2.xml.spi.XmlServiceParser}
     * to parse the file.
     * 
     * @param inputStream The non-null input stream to read.  Will not close this stream
     * @param jaxbAnnotatedClassOrInterface The non-null interface corresponding to the Xml to be parsed
     * @param advertiseInRegistry if true the entire tree of parsed xml will be added to the
     * ServiceLocator
     * @param advertiseInHub if true the entire tree of parsed xml will be added to the
     * HK2 configuration Hub (as bean-like maps)
     * @return A non-null handle that can be used to get the unmarshalled data or perform
     * other tasks
     */
    public <T> XmlRootHandle<T> unmarshal(InputStream inputStream, Class<T> jaxbAnnotatedInterface,
            boolean advertiseInRegistry, boolean advertiseInHub);
    
    /**
     * Unmarshals an XML stream using the jaxb annotated interface.
     * Will use the registered implementation of {@link org.glassfish.hk2.xml.spi.XmlServiceParser}
     * to parse the file.
     * 
     * @param inputStream The non-null input stream to read.  Will not close this stream
     * @param jaxbAnnotatedClassOrInterface The non-null interface corresponding to the Xml to be parsed
     * @param advertiseInRegistry if true the entire tree of parsed xml will be added to the
     * ServiceLocator
     * @param advertiseInHub if true the entire tree of parsed xml will be added to the
     * HK2 configuration Hub (as bean-like maps)
     * @param options optional (possibly null) options from the caller
     * @return A non-null handle that can be used to get the unmarshalled data or perform
     * other tasks
     */
    public <T> XmlRootHandle<T> unmarshal(InputStream inputStream, Class<T> jaxbAnnotatedInterface,
            boolean advertiseInRegistry, boolean advertiseInHub, Map<String, Object> options);
    
    /**
     * This creates an empty handle (root will initially be null) corresponding to
     * the given interface class
     * 
     * @param jaxbAnnotationInterface The non-null interface class corresponding to
     * the XML to be parsed
     * @param advertiseInRegistry if true the entire tree of parsed xml will be added to the
     * ServiceLocator
     * @param advertiseInHub if true the entire tree of parsed xml will be added to the
     * HK2 configuration Hub (as bean-like maps)
     * @return A non-null handle that can be used to create a new root bean, but which
     * is not initially tied to any backing file or other input stream
     */
    public <T> XmlRootHandle<T> createEmptyHandle(Class<T> jaxbAnnotationInterface,
            boolean advertiseInRegistry, boolean advertiseInHub);
    
    /**
     * This creates an empty handle (root will initially be null) corresponding to
     * the given interface class
     * 
     * @param jaxbAnnotationInterface The non-null interface class corresponding to
     * the XML to be parsed
     * @return A non-null handle that can be used to create a new root bean, but which
     * is not initially tied to any backing file or other input stream
     */
    public <T> XmlRootHandle<T> createEmptyHandle(Class<T> jaxbAnnotationInterface);
    
    /**
     * This creates an instance of the given bean type
     * of with no fields of the bean filled
     * in.  Objects created with this API can be
     * used in the adder methods of the beans, and
     * will not be validated (but all setters and
     * getters and lookups will work properly)
     * 
     * @return An instance of the bean with
     * no properties set
     */
    public <T> T createBean(Class<T> beanInterface);
    
    /**
     * Will marshal the given tree into the given stream.  This can
     * be called with a rootHandle that was NOT created with this
     * XmlService implementation.  In that way different parsing
     * formats can potentially be converted into each other.  For
     * example an XML document can be converted to equivalent JSON.
     * Not all transformations may be possible.  This method
     * will hold the read lock of the rootHandle so it cannot be
     * modified while being written to the output stream
     * 
     * @param outputStream A non-closed output stream.  This method will
     * not close the output stream
     * @param rootHandle A non-null root handle that may or may not
     * have been created with this XmlService implementation
     * @throws IOException On any exception that might happen
     */
    public <T> void marshal(OutputStream outputStream, XmlRootHandle<T> rootHandle) throws IOException;
    
    /**
     * Will marshal the given tree into the given stream.  This can
     * be called with a rootHandle that was NOT created with this
     * XmlService implementation.  In that way different parsing
     * formats can potentially be converted into each other.  For
     * example an XML document can be converted to equivalent JSON.
     * Not all transformations may be possible.  This method
     * will hold the read lock of the rootHandle so it cannot be
     * modified while being written to the output stream
     * 
     * @param outputStream A non-closed output stream.  This method will
     * not close the output stream
     * @param rootHandle A non-null root handle that may or may not
     * have been created with this XmlService implementation
     * @param options optional (possibly null) options from the caller
     * @throws IOException On any exception that might happen
     */
    public <T> void marshal(OutputStream outputStream, XmlRootHandle<T> rootHandle, Map<String, Object> options) throws IOException;

}
