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

package com.sun.enterprise.web;

import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.ResourceBundle;
import org.apache.catalina.core.StandardContext;
import com.sun.enterprise.web.session.PersistenceType;
import com.sun.enterprise.deployment.runtime.web.SessionManager;
import com.sun.enterprise.deployment.runtime.web.ManagerProperties;
import com.sun.enterprise.deployment.runtime.web.StoreProperties;
import com.sun.enterprise.deployment.runtime.web.WebProperty;
import com.sun.enterprise.deployment.WebBundleDescriptor;
import com.sun.logging.LogDomains;

/**
 * @author  lwhite
 */
public class SessionManagerConfigurationHelper {

    protected static final Logger _logger = LogDomains.getLogger(
            SessionManagerConfigurationHelper.class, LogDomains.WEB_LOGGER);

    protected WebModule _ctx = null;
    protected SessionManager _smBean = null; 
    protected WebBundleDescriptor _wbd = null;
    protected WebModuleConfig _wmInfo = null;
    protected PersistenceType _persistence = PersistenceType.MEMORY;
    protected String _persistenceFrequency = null;
    protected String _persistenceScope = null;
    private boolean _initialized = false;
    private ArrayList _systemApps = new ArrayList();
    protected ServerConfigLookup serverConfigLookup;

    /** Creates a new instance of SessionManagerConfigurationHelper */
    public SessionManagerConfigurationHelper(
        WebModule ctx, SessionManager smBean, WebBundleDescriptor wbd,
        WebModuleConfig wmInfo, ServerConfigLookup serverConfigLookup) {
        _ctx = ctx;
        _smBean = smBean;
        _wbd = wbd;
        _wmInfo = wmInfo;
        this.serverConfigLookup = serverConfigLookup;
        _systemApps.add("com_sun_web_ui");
        _systemApps.add(Constants.DEFAULT_WEB_MODULE_PREFIX + "admingui");
        _systemApps.add("adminapp");
        _systemApps.add("admingui");        
    }
    
    protected boolean isSystemApp(String appName) {
        return _systemApps.contains(appName);
    }
    
    protected void initializeConfiguration() {
        
        boolean isAppDistributable = false;
        if (_wbd != null) {
            isAppDistributable = _wbd.isDistributable();
	}
        if (_logger.isLoggable(Level.FINEST)) {
            _logger.finest("Web App Distributable (" + getApplicationId(_ctx)
                           + "): " + isAppDistributable);    
        }        
        
        PersistenceType persistence = PersistenceType.MEMORY;
        String persistenceFrequency = null;
        String persistenceScope = null;
        
        boolean isAvailabilityEnabled = 
            serverConfigLookup.calculateWebAvailabilityEnabledFromConfig(_ctx);
        if (_logger.isLoggable(Level.FINEST)) {
            _logger.finest("AvailabilityGloballyEnabled = " +
                           isAvailabilityEnabled);
        }
        if (isAvailabilityEnabled) {
            // These are the global defaults if nothing is
            // set at domain.xml or sun-web.xml
            persistence = PersistenceType.HA;
            persistenceFrequency = "time-based";
            persistenceScope = "session";
        }
        
        PersistenceType serverDefaultPersistenceType =
            serverConfigLookup.getPersistenceTypeFromConfig();

        if (serverDefaultPersistenceType != null) {        
            persistence = serverDefaultPersistenceType;        
            persistenceFrequency = serverConfigLookup.getPersistenceFrequencyFromConfig();
            persistenceScope = serverConfigLookup.getPersistenceScopeFromConfig();
        }
        String insLevelPersistenceTypeString = null;
        if (persistence != null) {
            insLevelPersistenceTypeString = persistence.getType();
        }
        if (_logger.isLoggable(Level.FINEST)) {
            _logger.finest("instance-level persistence-type = " +
                           insLevelPersistenceTypeString);
            _logger.finest("instance-level persistenceFrequency = " +
                           persistenceFrequency);
            _logger.finest("instance-level persistenceScope = " +
                           persistenceScope);
        }
        
        String webAppLevelPersistenceFrequency = null;
        String webAppLevelPersistenceScope = null;

        if (_smBean != null) {
            // The persistence-type controls what properties of the 
            // session manager can be configured
            String pType = _smBean.getAttributeValue(SessionManager.PERSISTENCE_TYPE);
            if (_logger.isLoggable(Level.FINEST)) {
                _logger.finest("webAppLevelPersistenceType = " + pType);
            }
            persistence = PersistenceType.parseType(pType, persistence);

            webAppLevelPersistenceFrequency = getPersistenceFrequency(_smBean);           
            webAppLevelPersistenceScope = getPersistenceScope(_smBean);
            if (_logger.isLoggable(Level.FINEST)) {
                _logger.finest("webAppLevelPersistenceFrequency = " +
                               webAppLevelPersistenceFrequency);
                _logger.finest("webAppLevelPersistenceScope = " +
                               webAppLevelPersistenceScope);
            }
        }
        
        // Use web app level values if they exist (i.e. not null)
        if (webAppLevelPersistenceFrequency != null) {
            persistenceFrequency = webAppLevelPersistenceFrequency;
        }
        if (webAppLevelPersistenceScope != null) {
            persistenceScope = webAppLevelPersistenceScope;
        }
        if (_logger.isLoggable(Level.FINEST)) {
            _logger.finest("IN WebContainer>>ConfigureSessionManager after web level check");
            _logger.finest("AFTER_WEB_PERSISTENCE-TYPE IS = " +
                           persistence.getType());
            _logger.finest("AFTER_WEB_PERSISTENCE_FREQUENCY IS = " +
                           persistenceFrequency);
            _logger.finest("AFTER_WEB_PERSISTENCE_SCOPE IS = " +
                           persistenceScope); 
        }
        
        // Delegate remaining initialization to builder
        String frequency = null;
        String scope = null;
        if ( persistence == PersistenceType.MEMORY 
            | persistence == PersistenceType.FILE 
            | persistence == PersistenceType.CUSTOM) {
            // Deliberately leaving frequency & scope null
        } else {
            frequency = persistenceFrequency;
            scope = persistenceScope;
        }

        // If app is not distributable and non-memory option
        // is attempted, log error and set back to "memory"
        if (!isAppDistributable && persistence != PersistenceType.MEMORY) {
            String wmName = getApplicationId(_ctx);
            if (_logger.isLoggable(Level.FINEST)) {
                _logger.finest("is " + wmName + " a system app: " +
                               isSystemApp(wmName));
            }
            // Suppress log error msg for default-web-module
            // log message only if availabilityenabled = true is attempted
            if (isAvailabilityEnabled &&
                    !wmName.equals(Constants.DEFAULT_WEB_MODULE_NAME) &&
                    !this.isSystemApp(wmName)) { 
                //log error
                Object[] params = { getApplicationId(_ctx), persistence.getType(), frequency, scope };
                _logger.log(Level.INFO,
                            "webcontainer.invalidSessionManagerConfig2",
                            params); 
            }    
            // Set back to memory option
            persistence = PersistenceType.MEMORY;
            frequency = null;
            scope = null;            
        }
        
        // If availability-enabled is false, reset to "memory"
        if (!isAvailabilityEnabled && persistence != PersistenceType.FILE) {
            // Set back to memory option
            persistence = PersistenceType.MEMORY;
            frequency = null;
            scope = null;             
        }
        
        if (_logger.isLoggable(Level.FINEST)) {
            _logger.finest("IN WebContainer>>ConfigureSessionManager before builder factory");
            _logger.finest("FINAL_PERSISTENCE-TYPE IS = " +
                           persistence.getType());
            _logger.finest("FINAL_PERSISTENCE_FREQUENCY IS = " + frequency);
            _logger.finest("FINAL_PERSISTENCE_SCOPE IS = " + scope); 
        }
        
        _persistence = persistence;
        _persistenceFrequency = frequency;
        _persistenceScope = scope;
    }
    
    /**
     * The application id for this web module
     */    
    public String getApplicationId(WebModule ctx) {
        com.sun.enterprise.web.WebModule wm = 
            (com.sun.enterprise.web.WebModule)ctx;
        return wm.getID();
    }
    
    /**
     * Get the persistence frequency for this web module
     * (this is the value from sun-web.xml if defined
     * @param smBean the session manager config bean
     */
    protected String getPersistenceFrequency(SessionManager smBean) {
        String persistenceFrequency = null;        
        ManagerProperties mgrBean = smBean.getManagerProperties();
        if ((mgrBean != null) && (mgrBean.sizeWebProperty() > 0)) {
            WebProperty[] props = mgrBean.getWebProperty();
            for (int i = 0; i < props.length; i++) {
                String name = props[i].getAttributeValue(WebProperty.NAME);
                String value = props[i].getAttributeValue(WebProperty.VALUE);
                if (name.equalsIgnoreCase("persistenceFrequency")) {
                    persistenceFrequency = value;
                }
            }
        }
        return persistenceFrequency;
    }
    
    /**
     * Get the persistence scope for this web module
     * (this is the value from sun-web.xml if defined
     * @param smBean the session manager config bean
     */    
    protected String getPersistenceScope(SessionManager smBean) {
        String persistenceScope = null;
        StoreProperties storeBean = smBean.getStoreProperties();
        if ((storeBean != null) && (storeBean.sizeWebProperty() > 0)) {
            WebProperty[] props = storeBean.getWebProperty();
            for (int i = 0; i < props.length; i++) {
                String name = props[i].getAttributeValue(WebProperty.NAME);
                String value = props[i].getAttributeValue(WebProperty.VALUE);
                if (name.equalsIgnoreCase("persistenceScope")) {
                    persistenceScope = value;
                }
            }
        }
        return persistenceScope;
    } 
    
    protected void checkInitialization() {
        if (!_initialized) {
            initializeConfiguration();
            _initialized = true;
        }
    }    
    
    public PersistenceType getPersistenceType() {
        checkInitialization();
        return _persistence;
    }
    
    public String getPersistenceFrequency() {
        checkInitialization();
        return _persistenceFrequency;
    } 
    
    public String getPersistenceScope() {
        checkInitialization();
        return _persistenceScope;
    }    
}
