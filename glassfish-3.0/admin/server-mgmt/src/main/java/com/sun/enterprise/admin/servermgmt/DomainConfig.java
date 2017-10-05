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

package com.sun.enterprise.admin.servermgmt;

import java.util.HashMap;
import java.util.Properties;
import java.util.Iterator;

import java.util.Map;

import com.sun.enterprise.universal.glassfish.ASenvPropertyReader;
import com.sun.enterprise.util.SystemPropertyConstants;

/**
 * This class defines the keys that are used to create the domain config object.
 * Almost all the methods of DomainsManager require the domain config to be
 * passed as java.util.Map, the key set of which is defined here.
 */
public class DomainConfig extends RepositoryConfig
{
    /**
     * These constants define the possbile Hash Map keys that can reside
     * in DomainConfig
	 * MAKE SURE THAT KEYS FOR PORTS END IN THE STRING "PORT" (case ignored) - this
	 * is used in PEDomainConfigValidator to ensure that the ports are unique!
     */
    public static final String K_USER           = "domain.user";
    public static final String K_PASSWORD       = "domain.password";
    public static final String K_NEW_MASTER_PASSWORD = "domain.newMasterPassword";
    public static final String K_MASTER_PASSWORD = "domain.masterPassword";
    public static final String K_SAVE_MASTER_PASSWORD = "domain.saveMasterPassword";
    public static final String K_ADMIN_PORT     = "domain.adminPort";    
    public static final String K_INSTANCE_PORT  = "domain.instancePort";
    public static final String K_PROFILE        = "domain.profile";
    public static final String K_DOMAINS_ROOT    = "domains.root";
    public static final String K_HOST_NAME      = "domain.hostName";
    public static final String K_JMS_PASSWORD   = "jms.password";
    public static final String K_JMS_PORT       = "jms.port";
    public static final String K_JMS_USER       = "jms.user";
    public static final String K_ORB_LISTENER_PORT  = "orb.listener.port";    
    public static final String K_SERVERID      = "server.id";
    public static final String K_TEMPLATE_NAME = "template.name";
    public static final String K_HTTP_SSL_PORT = "http.ssl.port";
    public static final String K_IIOP_SSL_PORT = "orb.ssl.port";
    public static final String K_IIOP_MUTUALAUTH_PORT = "orb.mutualauth.port";
    public static final String K_OSGI_SHELL_TELNET_PORT = "osgi.shell.telnet.port";

    public static final String K_DEBUG = "domain.debug";   
    public static final String K_VERBOSE = "domain.verbose";             
    public static final String K_VALIDATE_PORTS = "domain.validatePorts";
    //This token is used for SE/EE only now, but it is likely that we will want to expose it
    //in PE (i.e. to access the exposed Mbeans). Remember that the http jmx port (used by
    //asadmin) will not be exposed pubically.
    public static final String K_JMX_PORT       = "domain.jmxPort";
    public static final String K_EXTRA_PASSWORDS = "domain.extraPasswords";
    
    public static final int K_FLAG_START_DOMAIN_NEEDS_ADMIN_USER = 0x1;    
    public static final String KEYTOOLOPTIONS = "keytooloptions";
    /**
     * The DomainConfig always contains the K_DOMAINS_ROOT and K_HOST_NAME
     * attributes.
     */
    public DomainConfig(String domainName, String domainRoot) throws DomainException
    {  
        super(domainName, domainRoot);
        try {            
            put(K_DOMAINS_ROOT, domainRoot);
            // net to get fully qualified host, not just hostname
            ASenvPropertyReader pr = new ASenvPropertyReader();
            Map<String, String> envProperties = pr.getProps();
            put(K_HOST_NAME,
                envProperties.get(SystemPropertyConstants.HOST_NAME_PROPERTY));
        } catch (Exception ex) {
            throw new DomainException(ex);
        }
    }
 
    /**
     * This constructor is used at domain creation time only.
     */
    public DomainConfig(String domainName, Integer adminPort, String domainRoot, 
        String adminUser, String adminPassword, String masterPassword,
        Boolean saveMasterPassword, Integer instancePort,
        String jmsUser, String jmsPassword, Integer jmsPort, 
        Integer orbPort, Integer httpSSLPort, 
        Integer iiopSSLPort, Integer iiopMutualAuthPort,
        Integer jmxAdminPort, Integer osgiShellTelnetPort,
        Properties domainProperties) throws DomainException
    {
        this(domainName, domainRoot);
        try {
            put(K_ADMIN_PORT, adminPort);
            put(K_JMS_USER, jmsUser);
            put(K_JMS_PASSWORD, jmsPassword);
            put(K_PASSWORD, adminPassword);
            put(K_MASTER_PASSWORD, masterPassword);
            put(K_SAVE_MASTER_PASSWORD, saveMasterPassword);
            put(K_USER, adminUser);
            put(K_INSTANCE_PORT, instancePort);
            put(K_JMS_PORT, jmsPort);
            put(K_ORB_LISTENER_PORT, orbPort);
            put(K_HTTP_SSL_PORT, httpSSLPort);
            put(K_IIOP_SSL_PORT, iiopSSLPort);
            put(K_IIOP_MUTUALAUTH_PORT, iiopMutualAuthPort);            
            put(K_JMX_PORT, jmxAdminPort);
            put(K_OSGI_SHELL_TELNET_PORT, osgiShellTelnetPort);

            if(domainProperties!=null) {
                Iterator iterator = domainProperties.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = (String)iterator.next();
                    String value = (String)domainProperties.get(key);
                    put(key,value);
                }
            }
        } catch (Exception ex) {
            throw new DomainException(ex);
        }
    }
    
    public String getDomainName() {
        return super.getRepositoryName();
    }
    
    public String getDomainRoot()
    {
        return super.getRepositoryRoot();
    }

  public Map getPorts(){
	final Iterator it = ((Map) this).keySet().iterator();
	final Map result = new HashMap();
	while (it.hasNext()){
	  String key = (String) it.next();
	  if (key.toLowerCase().endsWith("port")){
		result.put(key, this.get(key));
	  }
	}
	return result;
  }
	
    public String getProfile() {
        return ( (String) get(K_PROFILE) );
    }
}
