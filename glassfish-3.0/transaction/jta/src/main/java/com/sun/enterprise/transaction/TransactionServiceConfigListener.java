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
package com.sun.enterprise.transaction;

import java.util.*;
import java.util.logging.*;
import java.beans.PropertyChangeEvent;

import com.sun.logging.LogDomains;
import com.sun.enterprise.util.i18n.StringManager;

import org.jvnet.hk2.annotations.Service;
import org.jvnet.hk2.annotations.Inject;
import org.jvnet.hk2.config.ConfigListener;
import org.jvnet.hk2.config.UnprocessedChangeEvent;
import org.jvnet.hk2.config.UnprocessedChangeEvents;
import org.jvnet.hk2.config.types.Property;

import com.sun.enterprise.config.serverbeans.ModuleMonitoringLevels;
import com.sun.enterprise.config.serverbeans.TransactionService;
import com.sun.enterprise.config.serverbeans.ServerTags;

import com.sun.enterprise.transaction.api.JavaEETransactionManager;

/**
 * ConfigListener class for TransactionService and TransactionService 
 * monitoring level changes
 *
 * @author Marina Vatkina
 */
@Service
public class TransactionServiceConfigListener implements ConfigListener {

    private static final Logger _logger = LogDomains.getLogger(
            TransactionServiceConfigListener.class, LogDomains.JTA_LOGGER);

    // Injecting @Configured type triggers the corresponding change 
    // events to be sent to this instance
    @Inject private TransactionService ts;

    // Listen to monitoring level changes
    @Inject(optional=true) private ModuleMonitoringLevels ml = null;

    private JavaEETransactionManager tm;

    // Sting Manager for Localization
    private static StringManager sm 
           = StringManager.getManager(TransactionServiceConfigListener.class);

    /**
     * Clears the transaction associated with the caller thread
     */
    public void setTM(JavaEETransactionManager tm) {
        this.tm = tm;
    }

/****************************************************************************/
/** Implementation of org.jvnet.hk2.config.ConfigListener *********************/
/****************************************************************************/
    public UnprocessedChangeEvents changed(PropertyChangeEvent[] events) {

        // Events that we can't process now because they require server restart.
        List<UnprocessedChangeEvent> unprocessedEvents = new ArrayList<UnprocessedChangeEvent>();

        for (PropertyChangeEvent event : events) {
            String eventName = event.getPropertyName();
            Object oldValue = event.getOldValue();
            Object newValue = event.getNewValue();
            boolean accepted = true;

            _logger.log(Level.FINE, "Got TransactionService change event ==== "
                    + event.getSource() + " " 
                    + eventName + " " + oldValue + " " + newValue);

            if (oldValue != null && oldValue.equals(newValue)) {
                _logger.log(Level.FINE, "Event " + eventName 
                        + " did not change existing value of " + oldValue);
                continue;
            }

           if (event.getSource() instanceof ModuleMonitoringLevels) {
                if (eventName.equals(ServerTags.TRANSACTION_SERVICE)) {
                    String oldlevel = oldValue.toString();
                    String newlevel = newValue.toString();
                    _logger.log(Level.FINE, "Changing transaction monitoring level"); 
                    if ("OFF".equals(newlevel)) {
                        tm.setMonitoringEnabled(false);
                    } else if ("LOW".equals(newlevel) || "HIGH".equals(newlevel)) {
                        tm.setMonitoringEnabled(true);
                    } 
                } // else skip
           } else if (eventName.equals(ServerTags.TIMEOUT_IN_SECONDS)) {
                try {
                    tm.setDefaultTransactionTimeout(Integer.parseInt((String)newValue,10));
                    _logger.log(Level.FINE," Transaction Timeout interval event processed for: " + newValue);
                } catch (Exception ex) {
                    _logger.log(Level.WARNING,"transaction.reconfig_txn_timeout_failed",ex);
                } // timeout-in-seconds

            } else if (eventName.equals(ServerTags.KEYPOINT_INTERVAL)
                    || eventName.equals(ServerTags.RETRY_TIMEOUT_IN_SECONDS)) {
                tm.handlePropertyUpdate(eventName, newValue);
                _logger.log(Level.FINE, eventName + " reconfig event processed for new value: " + newValue);

            } else if (event.getPropertyName().equals("value")) {
                eventName = ((Property)event.getSource()).getName();
                _logger.log(Level.FINE, "Got Property change event for " + eventName);

                // Not handled dynamically. Restart is required.
                accepted = false;

            } else if (event.getPropertyName().equals("name")
                    || event.getPropertyName().equals("property")) {
                // skip - means a new property added, was processed above as "value".
                _logger.log(Level.FINE, "...skipped");

            } else {
                // Not handled dynamically. Restart is required.
                accepted = false;
            }

            if (!accepted) {
                String msg = sm.getString("enterprise_distributedtx.restart_required",
                        eventName);
                _logger.log(Level.INFO, msg);
                unprocessedEvents.add(new UnprocessedChangeEvent(event, msg));
            }
        }
        return (unprocessedEvents.size() > 0) 
                ? new UnprocessedChangeEvents(unprocessedEvents) : null;
    }

}
