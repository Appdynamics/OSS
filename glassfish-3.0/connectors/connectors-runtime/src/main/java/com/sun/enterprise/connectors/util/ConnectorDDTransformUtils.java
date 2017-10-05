/*
* DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
*
* Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
*
* The contents of this file are subject to the terms of either the GNU
* General Public License Version 2 only ("GPL") or the Common Development
* and Distribution License("CDDL") (collectively, the "License").  You
* may not use this file except in compliance with the License. You can obtain
* a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
* or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
* language governing permissions and limitations under the License.
*
* When distributing the software, include this License Header Notice in each
* file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
* Sun designates this particular file as subject to the "Classpath" exception
* as provided by Sun in the GPL Version 2 section of the License file that
* accompanied this code.  If applicable, add the following below the License
* Header, with the fields enclosed by brackets [] replaced by your own
* identifying information: "Portions Copyrighted [year]
* [name of copyright owner]"
*
* Contributor(s):
*
* If you wish your version of this file to be governed by only the CDDL or
* only the GPL Version 2, indicate your decision by adding "[Contributor]
* elects to include this software in this distribution under the [CDDL or GPL
* Version 2] license."  If you don't indicate a single choice of license, a
* recipient has the option to distribute your version of this file under
* either the CDDL, the GPL Version 2 or to extend the choice of license to
* its licensees as provided above.  However, if you add GPL Version 2 code
* and therefore, elected the GPL Version 2 license, then the option applies
* only if the new code is made subject to such option by the copyright
* holder.
*/

package com.sun.enterprise.connectors.util;

import com.sun.appserv.connectors.internal.api.ConnectorRuntimeException;
import org.jvnet.hk2.config.types.Property;
import com.sun.enterprise.connectors.ConnectorDescriptorInfo;
import com.sun.enterprise.connectors.ConnectorRuntime;
import com.sun.enterprise.deploy.shared.FileArchive;
import com.sun.enterprise.deployment.*;
import com.sun.enterprise.deployment.archivist.ConnectorArchivist;
import com.sun.enterprise.deployment.deploy.shared.MemoryMappedArchive;
import com.sun.logging.LogDomains;
import org.xml.sax.SAXParseException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * This is an util class pertaining to the connector deployment descriptor
 * i.e ra.xml and sun-ra.xml. This consist of methods to obtain the deployment
 * descriptor and perform various transformations on that  to
 * obtain/constructs classes/objects pertaining to the Connector modules
 * like ConnectorDescriptorInfo.
 *
 * @author Srikanth P
 */

public class ConnectorDDTransformUtils {

    static Logger _logger = LogDomains.getLogger(ConnectorDDTransformUtils.class, LogDomains.RSR_LOGGER);

    /**
     * Constructs ConnectorDescriptorInfo object from the
     * ConnectionDefDescriptor object (deployment descriptor of connector
     * module)
     *
     * @param connectionDefDescriptor ConnectionDefDescriptor object which
     *                                represents the ra.xml and sun-ra.xml
     * @return Transformed ConnectorDescriptorInfo object
     */

    public static ConnectorDescriptorInfo getConnectorDescriptorInfo(
            ConnectionDefDescriptor connectionDefDescriptor) {

        ConnectorDescriptorInfo connectorDescInfo =
                new ConnectorDescriptorInfo();
        connectorDescInfo.setConnectionDefinitionName(
                connectionDefDescriptor.getConnectionFactoryIntf());
        connectorDescInfo.setManagedConnectionFactoryClass(
                connectionDefDescriptor.getManagedConnectionFactoryImpl());
        connectorDescInfo.setConnectionFactoryClass(
                connectionDefDescriptor.getConnectionFactoryImpl());
        connectorDescInfo.setConnectionFactoryInterface(
                connectionDefDescriptor.getConnectionFactoryIntf());
        connectorDescInfo.setConnectionInterface(
                connectionDefDescriptor.getConnectionIntf());
        connectorDescInfo.setConnectionClass(
                connectionDefDescriptor.getConnectionImpl());
        connectorDescInfo.setMCFConfigProperties(
                connectionDefDescriptor.getConfigProperties());
        return connectorDescInfo;
    }

    /**
     * merges the properties mentioned in first parameter with the Set of
     * properties mentioned in second parameter.
     * Values of first parameter takes precedence over second.
     * First parameter represents properties present in domain.xml
     * Second parameter contains values mentioned in deployment descriptors.
     *
     * @param props      Array of  properties that needs to be merged with
     *                   properties mentioned in deployement descriptor. These values
     *                   takes precedence over  values present in deployment descriptors.
     * @param Properties present in deployment descriptor.
     * @return Set of merged properties.
     */
    public static Set mergeProps(List<Property> props, Set defaultMCFProps) {
        HashSet mergedSet = new HashSet();

        if (defaultMCFProps != null) {
            Object[] defaultProps = defaultMCFProps.toArray();

            for (int i = 0; i < defaultProps.length; i++) {
                mergedSet.add(defaultProps[i]);
            }
        }

        for (Property property : props) {
            ConnectorConfigProperty  ep = new ConnectorConfigProperty (
                    property.getName(), property.getValue(), null);
            if (defaultMCFProps.contains(ep)) {
                //get the environment property in the mergedset
                Iterator iter = defaultMCFProps.iterator();
                while (iter.hasNext()) {
                    ConnectorConfigProperty  envProp =
                            (ConnectorConfigProperty ) iter.next();
                    //and if they are equal, set ep's type to envProp's type

                    //This set is important because envProp has the ra.xml
                    //specified property-Type. When the ra-bean-class does
                    //not have any getter method for a property, the property
                    //Type specified in ra.xml should be used.
                    if (envProp.equals(ep)) {
                        if (envProp.getType() != null) {
                            ep.setType(envProp.getType());
                        }
                    }
                }

                _logger.log(Level.FINER,
                        "After merging props with defaultMCFProps: envPropName: "
                                + ep.getName() + " envPropValue : " + ep.getValue());
                mergedSet.remove(ep);
            }
            mergedSet.add(ep);
        }
        return mergedSet;
    }

    /**
     * Get the ConnectorDescriptor object which represents the ra.xml and
     * sun-ra.xml from an exploded rar module.
     *
     * @param moduleDir Directory where rar is exploded.
     * @return ConnectorDescriptor object which
     *         represents the ra.xml and sun-ra.xml
     * @throws ConnectorRuntimeException if ra.xml could not be located or
     *                                   invalid. For 1.0 type rar if sun-ra.xml is not present or
     *                                   invalid this exception is thrown. For 1.5 type rar sun-ra.xml
     *                                   should not be  present.
     */
    public static ConnectorDescriptor getConnectorDescriptor(String moduleDir, String rarModuleName)
            throws ConnectorRuntimeException {

        try {

            File module = new File(moduleDir);

            FileArchive fileArchive = new FileArchive();
            fileArchive.open(module.toURI());  // directory where rar is exploded
            ConnectorRuntime runtime = ConnectorRuntime.getRuntime();
            ClassLoader loader = runtime.createConnectorClassLoader(moduleDir, null, rarModuleName);

            ConnectorArchivist connectorArchivist = runtime.getConnectorArchvist();
            //TODO V3 what happens to embedded .rar ? as its parent classloader should be application CL
            //setting the classloader so that annotation processor can make use of it.
            connectorArchivist.setClassLoader(loader);
            //fileArchive.entries("META-INF/ra.xml");
            //TODO V3 need to check whether ra.xml is present, if so, check the version. process annotations
            //only if its 1.6 or above ?
            connectorArchivist.setAnnotationProcessingRequested(true);

            return connectorArchivist.open(fileArchive);
        } catch (IOException ex) {
            ConnectorRuntimeException cre = new ConnectorRuntimeException(
                    "Failed to read the connector deployment descriptors");
            cre.initCause(ex);
            _logger.log(Level.SEVERE, "rardeployment.connector_descriptor_read_error", moduleDir);
            _logger.log(Level.SEVERE, "", cre);
            throw cre;
        } catch (SAXParseException ex) {
            ConnectorRuntimeException cre = new ConnectorRuntimeException(
                    "Failed to parse the connector deployment descriptors");
            cre.initCause(ex);
            _logger.log(Level.SEVERE, "rardeployment.connector_descriptor_parse_error", moduleDir);
            _logger.log(Level.SEVERE, "", cre);
            throw cre;
        }
    }

    /**
     * Obtain all the ConnectionDefDescriptor(abstracts the
     * <connector-definition> element in ra.xml) objects that are
     * present in given ra.xml file.
     *
     * @param connectorDesc ConnectorDescriptor object which
     *                      represents the ra.xml and sun-ra.xml
     * @return Array of ConnectionDefDescriptor objects which represent the
     *         <connection-definition> element.
     */
    public static ConnectionDefDescriptor[] getConnectionDefs(ConnectorDescriptor connectorDesc) {
        ConnectionDefDescriptor[] connectionDefDescs = new ConnectionDefDescriptor[0];
        OutboundResourceAdapter ora =
                connectorDesc.getOutboundResourceAdapter();
        if (ora != null) {
            Set connectionDefs = ora.getConnectionDefs();
            Iterator iter = connectionDefs.iterator();
            int size = connectionDefs.size();
            connectionDefDescs = new ConnectionDefDescriptor[size];
            for (int i = 0; i < size; ++i) {
                connectionDefDescs[i] =
                        (ConnectionDefDescriptor) iter.next();
            }
        }
        return connectionDefDescs;
    }

    public static String getResourceAdapterClassName
            (String rarLocation) {
        //Use the deployment APIs to get the name of the resourceadapter
        //class through the connector descriptor
        try {
            FileInputStream fis = new FileInputStream(rarLocation);
            MemoryMappedArchive mma = new MemoryMappedArchive(fis);
            ConnectorArchivist ca = new ConnectorArchivist();
            ConnectorDescriptor cd = (ConnectorDescriptor) ca.open(mma);
            return cd.getResourceAdapterClass();
        } catch (IOException e) {
            _logger.info(e.getMessage());
            _logger.log(Level.FINE, "Error while trying to read connector" +
                    "descriptor to get resource-adapter properties", e);
        } catch (SAXParseException e) {
            _logger.info(e.getMessage());
            _logger.log(Level.FINE, "Error while trying to read connector" +
                    "descriptor to get resource-adapter properties", e);
        }
        return null;
    }

    /**
     *  Returns all the message listeners present in the connectorDescriptor
     *  which abstracts the ra.xml
     *  @param desc connectorDescriptor which abstracts the ra.xml
     *  @return Array of MessageListener objects
     */

     public MessageListener[] getMessageListeners(ConnectorDescriptor desc) {

         InboundResourceAdapter inboundRA = null;
         Set messageListenerSet = null;
         if(desc != null &&
                 (inboundRA = desc.getInboundResourceAdapter()) != null) {
             messageListenerSet = inboundRA.getMessageListeners();
         }

         if(messageListenerSet == null) {
             return null;
         }
         int size = messageListenerSet.size();
         MessageListener[] messageListeners =
                  new MessageListener[size];
         Iterator iter = messageListenerSet.iterator();
         for(int i=0;i<size;++i){
             messageListeners[i] = (MessageListener)iter.next();
         }
         return messageListeners;
     }
}

