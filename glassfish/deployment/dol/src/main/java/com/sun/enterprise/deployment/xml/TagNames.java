/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
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

package com.sun.enterprise.deployment.xml;

/** 
 * I hold the XML tag names common to the dtds across the J2EE platform.
 * @author Jerome Dochez
 */

public interface TagNames {

    public static final String NAME = "display-name";
    public final static String MODULE_NAME = "module-name";
    public static final String ID = "id";
    public static final String DESCRIPTION = "description";
    public static final String VERSION = "version";
    public static final String METADATA_COMPLETE = "metadata-complete";
 
    public static final String ICON = "icon";
    public static final String LARGE_ICON = "large-icon";
    public static final String SMALL_ICON = "small-icon";

    public static final String ENVIRONMENT_PROPERTY = "env-entry";
    public static final String ENVIRONMENT_PROPERTY_NAME = "env-entry-name";
    public static final String ENVIRONMENT_PROPERTY_VALUE = "env-entry-value";
    public static final String ENVIRONMENT_PROPERTY_TYPE = "env-entry-type";

    public static final String EJB_REFERENCE = "ejb-ref";
    public static final String EJB_REFERENCE_NAME = "ejb-ref-name";
    public static final String EJB_REFERENCE_TYPE = "ejb-ref-type";
    public static final String EJB_LINK = "ejb-link";
    
    public static final String EJB_LOCAL_REFERENCE = "ejb-local-ref";

    public static final String LOOKUP_NAME = "lookup-name";
    
    public static final String RESOURCE_REFERENCE = "resource-ref";
    public static final String RESOURCE_REFERENCE_NAME = "res-ref-name";
    public static final String RESOURCE_SHARING_SCOPE = "res-sharing-scope";

    public static final String MESSAGE_DESTINATION_REFERENCE = "message-destination-ref";
    public static final String MESSAGE_DESTINATION_REFERENCE_NAME = "message-destination-ref-name";
    public static final String MESSAGE_DESTINATION = "message-destination";
    public static final String MESSAGE_DESTINATION_NAME = "message-destination-name";
    public static final String MESSAGE_DESTINATION_TYPE = "message-destination-type";
    public static final String MESSAGE_DESTINATION_USAGE = "message-destination-usage";
    public static final String MESSAGE_DESTINATION_LINK = "message-destination-link";

    public static final String RESOURCE_ENV_REFERENCE = "resource-env-ref";
    public static final String RESOURCE_ENV_REFERENCE_NAME = "resource-env-ref-name";
    public static final String RESOURCE_ENV_REFERENCE_TYPE = "resource-env-ref-type";

    public static final String DATA_SOURCE = "data-source";
    public static final String DATA_SOURCE_DESCRIPTION = "description";
    public static final String DATA_SOURCE_NAME = "name";
    public static final String DATA_SOURCE_CLASS_NAME = "class-name";
    public static final String DATA_SOURCE_URL = "url";
    public static final String DATA_SOURCE_SERVER_NAME = "server-name";
    public static final String DATA_SOURCE_PORT_NUMBER = "port-number";
    public static final String DATA_SOURCE_DATABASE_NAME = "database-name";
    public static final String DATA_SOURCE_USER = "user";
    public static final String DATA_SOURCE_PASSWORD = "password";
    public static final String DATA_SOURCE_LOGIN_TIMEOUT = "login-timeout";
    public static final String DATA_SOURCE_TRANSACTIONAL = "transactional";
    public static final String DATA_SOURCE_ISOLATION_LEVEL = "isolation-level";
    public static final String DATA_SOURCE_INITIAL_POOL_SIZE = "initial-pool-size";
    public static final String DATA_SOURCE_MIN_POOL_SIZE = "min-pool-size";
    public static final String DATA_SOURCE_MAX_POOL_SIZE = "max-pool-size";
    public static final String DATA_SOURCE_MAX_IDLE_TIME = "max-idle-time";
    public static final String DATA_SOURCE_MAX_STATEMENTS = "max-statements";
    public static final String DATA_SOURCE_PROPERTY = "property";
    public static final String DATA_SOURCE_PROPERTY_NAME = "name";
    public static final String DATA_SOURCE_PROPERTY_VALUE = "value";

    public static final String PERSISTENCE_CONTEXT_REF = "persistence-context-ref";
    public static final String PERSISTENCE_CONTEXT_REF_NAME = "persistence-context-ref-name";

    public static final String PERSISTENCE_PROPERTY = "persistence-property";

    public static final String PERSISTENCE_UNIT_NAME = "persistence-unit-name";
    public static final String PERSISTENCE_CONTEXT_TYPE = "persistence-context-type";
    public static final String PERSISTENCE_UNIT_REF = "persistence-unit-ref";
    public static final String PERSISTENCE_UNIT_REF_NAME = "persistence-unit-ref-name";

    public static final String JMS_QUEUE_DEST_TYPE = "javax.jms.Queue";
    public static final String JMS_TOPIC_DEST_TYPE = "javax.jms.Topic";

    public static final String RESOURCE_TYPE = "res-type";
    public static final String RESOURCE_AUTHORIZATION = "res-auth";
    
    public static final String ROLE = "security-role";
    public static final String ROLE_NAME = "role-name";    
    public static final String ROLE_REFERENCE = "security-role-ref";
    public static final String ROLE_LINK = "role-link";    
    public static final String RUNAS_SPECIFIED_IDENTITY = "run-as";

    public static final String ENCODING_STYLE = "encoding-style";
    public static final String JAVA_TYPE = "java-type";

    public static final String WEB_SERVICE_ENDPOINT = "web-service-endpoint";
    public static final String XML_NAMESPACE_PREFIX = "xml:";
    public static final String LANG = "lang";

    public static final String NAME_VALUE_PAIR_NAME = "name";
    public static final String NAME_VALUE_PAIR_VALUE = "value";

    // injection tags
    public static final String INJECTION_TARGET = "injection-target";
    public static final String INJECTION_TARGET_CLASS = "injection-target-class";
    public static final String INJECTION_TARGET_NAME = "injection-target-name";
    public static final String MAPPED_NAME = "mapped-name";

    public static final String POST_CONSTRUCT = "post-construct";
    public static final String PRE_DESTROY = "pre-destroy";
    public static final String LIFECYCLE_CALLBACK_CLASS = "lifecycle-callback-class";  
    public static final String LIFECYCLE_CALLBACK_METHOD = "lifecycle-callback-method";  

    // deployment extension related tags
    public static final String DEPLOYMENT_EXTENSION = "deployment-extension";
    public static final String EXTENSION_ELEMENT = "extension-element";
    public static final String NAMESPACE = "namespace";
    public static final String MUST_UNDERSTAND = "mustUnderstand";

    public static final String J2EE_DEFAULTNAMESPACEPREFIX = "j2ee";
    public static final String J2EE_NAMESPACE = "http://java.sun.com/xml/ns/j2ee";
    public static final String JAVAEE_DEFAULTNAMESPACEPREFIX = "javaee";
    public static final String JAVAEE_NAMESPACE = "http://java.sun.com/xml/ns/javaee";
    public static final String XML_NAMESPACE = "http://www.w3.org/XML/1998/namespace";

    public static final String WL_WEB_APP_NAMESPACE = "http://xmlns.oracle.com/weblogic/weblogic-web-app";
    public static final String WL_EJB_JAR_NAMESPACE = "http://xmlns.oracle.com/weblogic/weblogic-ejb-jar";
    public static final String WL_WEBSERVICES_NAMESPACE = "http://xmlns.oracle.com/weblogic/weblogic-webservices";
    public static final String WL_CONNECTOR_NAMESPACE = "http://xmlns.oracle.com/weblogic/weblogic-connector";
    public static final String WL_APPLICATION_NAMESPACE = "http://xmlns.oracle.com/weblogic/weblogic-application"; 
    public static final String WL_APPLICATION_CLIENT_NAMESPACE = "http://xmlns.oracle.com/weblogic/weblogic-application-client"; 

    public static final String PERSISTENCE_XML_NAMESPACE = "urn:ejb3-namespace";
}
