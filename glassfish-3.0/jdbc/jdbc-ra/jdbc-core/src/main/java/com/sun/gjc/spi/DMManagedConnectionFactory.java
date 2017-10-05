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

package com.sun.gjc.spi;

import com.sun.gjc.common.DataSourceObjectBuilder;
import com.sun.gjc.common.DataSourceSpec;
import com.sun.gjc.util.SecurityUtils;
import com.sun.logging.LogDomains;

import javax.resource.ResourceException;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.security.PasswordCredential;
import java.sql.DriverManager;
import java.util.Hashtable;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Driver Manager <code>ManagedConnectionFactory</code> implementation for Generic JDBC Connector.
 *
 * @author Evani Sai Surya Kiran
 * @version 1.0, 02/07/31
 */

public class DMManagedConnectionFactory extends ManagedConnectionFactory {

    Properties props;

    private static Logger _logger;

    static {
        _logger = LogDomains.getLogger(DMManagedConnectionFactory.class, LogDomains.RSR_LOGGER);
    }

    private boolean debug = false;

    /**
     * Creates a new physical connection to the underlying EIS resource
     * manager.
     *
     * @param subject       <code>Subject</code> instance passed by the application server
     * @param cxRequestInfo <code>ConnectionRequestInfo</code> which may be created
     *                      as a result of the invocation <code>getConnection(user, password)</code>
     *                      on the <code>DataSource</code> object
     * @return <code>ManagedConnection</code> object created
     * @throws ResourceException if there is an error in instantiating the
     *                           <code>DataSource</code> object used for the
     *                           creation of the <code>ManagedConnection</code> object
     * @throws SecurityException if there ino <code>PasswordCredential</code> object
     *                           satisfying this request
     */
    public javax.resource.spi.ManagedConnection createManagedConnection(javax.security.auth.Subject subject,
                                                                        ConnectionRequestInfo cxRequestInfo) throws ResourceException {
        logFine("In createManagedConnection");
        if (dsObjBuilder == null) {
            dsObjBuilder = new DataSourceObjectBuilder(spec);
        }
        PasswordCredential pc = SecurityUtils.getPasswordCredential(this, subject, cxRequestInfo);

        try {
            Class.forName(spec.getDetail(DataSourceSpec.CLASSNAME));
        } catch (ClassNotFoundException cnfe) {
            _logger.log(Level.SEVERE, "jdbc.exc_cnfe", cnfe);
            throw new ResourceException("The driver could not be loaded: " + spec.getDetail(DataSourceSpec.CLASSNAME));
        }

        java.sql.Connection dsConn = null;

        Properties driverProps = new Properties();
        //Will return a set of properties that would have setURL and <url> as objects
        //Get a set of normal case properties
        Hashtable properties = dsObjBuilder.parseDriverProperties(spec, false);
        Set<String> keys = (Set<String>)properties.keySet();
        for( String key : keys ) {
            String value = null;
            Vector values = (Vector) properties.get(key);
            if(!values.isEmpty() && values.size() == 1) {
                value = (String) values.firstElement();
            } else if(values.size() > 1) {
                logFine("More than one value for key : " + key);
            }
            String prop = getParsedKey(key);
            driverProps.put(prop, value);
            if(prop.equalsIgnoreCase("URL")) {
                if(spec.getDetail(DataSourceSpec.URL) == null) {
                    setConnectionURL(value);
                }
            }
        }

        try {
            if (cxRequestInfo != null) {
                driverProps.setProperty("user", pc.getUserName());
                driverProps.setProperty("password", new String(pc.getPassword()));
            } else {
                String user = spec.getDetail(DataSourceSpec.USERNAME);
                String password = spec.getDetail(DataSourceSpec.PASSWORD);
                if(user != null) {
                    driverProps.setProperty("user", user);
                }
                if(password != null) {
                    driverProps.setProperty("password", password);
                }
            }

            dsConn = DriverManager.getConnection(spec.getDetail(DataSourceSpec.URL), driverProps);

        } catch (java.sql.SQLException sqle) {
            _logger.log(Level.SEVERE, "jdbc.exc_create_mc", sqle);
            throw new javax.resource.spi.ResourceAllocationException("The connection could not be allocated: " +
                    sqle.getMessage());
        }

        com.sun.gjc.spi.ManagedConnection mc = constructManagedConnection(
                null, dsConn, pc, this);

        //GJCINT
        setIsolation(mc);
        isValid(mc);
        return mc;
    }

    /**
     * Parses the key and removes the "set" string at the beginning of the 
     * property.
     * @param key
     * @return
     */
    private String getParsedKey(String key) throws ResourceException {
        String parsedKey = null;
        int indexOfSet = -1;
        try {
            indexOfSet = key.indexOf("set");
        } catch (NullPointerException npe) {
            if (debug) {
                _logger.log(Level.FINE, "jdbc.exc_caught_ign", npe.getMessage());
            }

        }
        if (indexOfSet == 0) {
            //Find the key String

            try {
                parsedKey = key.substring(indexOfSet + 3, key.length()).trim();
            } catch (IndexOutOfBoundsException iobe) {
                if (debug) {
                    _logger.log(Level.FINE, "jdbc.exc_caught_ign", iobe.getMessage());
                }
            }
            if (parsedKey.equals("")) {
                throw new ResourceException("Invalid driver properties string - " +
                        "Key cannot be an empty string");
            }
        }
        return parsedKey;
        
    }

    /**
     * This method checks if the properties object is null or not.
     * If the properties object is null, it creates a new Properties
     * object and inserts the default "user" and "password" key value
     * pairs. It checks if any other properties have been set or not
     * and includes the key value pairs for those properties.
     *
     * @return props    <code>Properties</code> object conatining properties for getting a connection
     * @throws ResourceException if the driver properties string and delimiter are not proper
     */
    //TODO remove unused method
    private Properties getPropertiesObj() throws ResourceException {
        if (props != null) {
            return props;
        }

        props = new Properties();
        props.setProperty("user", getUser());
        props.setProperty("password", getPassword());

        String driverProps = spec.getDetail(DataSourceSpec.DRIVERPROPERTIES);
        String delimiter = spec.getDetail(DataSourceSpec.DELIMITER);

        if (driverProps != null && driverProps.trim().equals("") == false) {
            if (delimiter == null || delimiter.equals("")) {
                throw new ResourceException("Invalid driver properties string - " +
                        "delimiter not properly set!!");
            }

            StringTokenizer st = new StringTokenizer(driverProps, delimiter);
            while (st.hasMoreTokens()) {
                String keyValuePair = null;
                try {
                    keyValuePair = st.nextToken();
                } catch (NoSuchElementException nsee) {
                    throw new ResourceException("Invalid driver properties string - " +
                            "Key value pair not available: " + nsee.getMessage());
                }

                int indexOfEqualsSign = -1;
                try {
                    indexOfEqualsSign = keyValuePair.indexOf("=");
                    if (indexOfEqualsSign == -1) {
                        throw new ResourceException("Invalid driver properties string - " +
                                "Key value pair should be of the form key = value");
                    }
                } catch (NullPointerException npe) {
                    if (debug) {
                        _logger.log(Level.FINE, "jdbc.exc_caught_ign", npe.getMessage());
                    }

                }

                String key = null;
                try {
                    key = keyValuePair.substring(0, indexOfEqualsSign).trim();
                } catch (IndexOutOfBoundsException iobe) {
                    if (debug) {
                        _logger.log(Level.FINE, "jdbc.exc_caught_ign", iobe.getMessage());
                    }
                }
                if (key.equals("")) {
                    throw new ResourceException("Invalid driver properties string - " +
                            "Key cannot be an empty string");
                }

                String value = null;
                try {
                    value = keyValuePair.substring(indexOfEqualsSign + 1).trim();
                } catch (IndexOutOfBoundsException iobe) {
                    if (debug) {
                        _logger.log(Level.FINE, "jdbc.exc_caught_ign", iobe.getMessage());
                    }
                }

                props.setProperty(key, value);
            }
        }

        return props;
    }

    /**
     * Check if this <code>ManagedConnectionFactory</code> is equal to
     * another <code>ManagedConnectionFactory</code>.
     *
     * @param other <code>ManagedConnectionFactory</code> object for checking equality with
     * @return true    if the property sets of both the
     *         <code>ManagedConnectionFactory</code> objects are the same
     *         false	otherwise
     */
    public boolean equals(Object other) {
        logFine("In equals");

        /**
         * The check below means that two ManagedConnectionFactory objects are equal
         * if and only if their properties are the same.
         */
        if (other instanceof com.sun.gjc.spi.DMManagedConnectionFactory) {
            com.sun.gjc.spi.DMManagedConnectionFactory otherMCF =
                    (com.sun.gjc.spi.DMManagedConnectionFactory) other;
            return this.spec.equals(otherMCF.spec);
        }
        return false;
    }

    /**
     * Sets the login timeout.
     *
     * @param loginTimeOut <code>String</code>
     * @see <code>getLoginTimeOut</code>
     */
    public void setLoginTimeOut(String loginTimeOut) {
        int timeOut = 0;
        try {
            timeOut = Integer.valueOf(loginTimeOut);
            DriverManager.setLoginTimeout(timeOut);
            spec.setDetail(DataSourceSpec.LOGINTIMEOUT, loginTimeOut);
        } catch (Exception e) {
            if (debug) {
                _logger.log(Level.FINE, "jdbc.exc_caught_ign", e.getMessage());
            }
        }
    }

    public void setURL(String url) {
        spec.setDetail(DataSourceSpec.URL, url);
    }
    
    public String getURL() {
        return spec.getDetail(DataSourceSpec.URL);
    }
    
    /**
     * Sets the connection url.
     *
     * @param url <code>String</code>
     * @see <code>getConnectionURL</code>
     */
    public void setConnectionURL(String url) {
        spec.setDetail(DataSourceSpec.URL, url);
    }

    /**
     * Gets the connection url.
     *
     * @return url
     * @see <code>setConnectionURL</code>
     */
    public String getConnectionURL() {
        return spec.getDetail(DataSourceSpec.URL);
    }

    public Object getDataSource() throws ResourceException {
        return null;
    }
}
