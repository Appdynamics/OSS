/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2011 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.web.server;

import com.sun.enterprise.container.common.spi.util.InjectionManager;
import com.sun.enterprise.web.WebComponentInvocation;
import com.sun.enterprise.web.WebModule;
import com.sun.logging.LogDomains;
import org.apache.catalina.ContainerEvent;
import org.apache.catalina.ContainerListener;
import org.glassfish.api.invocation.ComponentInvocation;
import org.glassfish.api.invocation.InvocationManager;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
//END OF IASRI 4660742

/**
 * This class implements the Tomcat ContainerListener interface and
 * handles Context and Session related events.
 * @author Tony Ng
 */
public final class WebContainerListener 
    implements ContainerListener {

    // START OF IASRI 4660742
    private static final Logger _logger=LogDomains.getLogger(
        WebContainerListener.class, LogDomains.WEB_LOGGER);
    // END OF IASRI 4660742

    private static final ResourceBundle rb = _logger.getResourceBundle();

    static private HashSet<String> beforeEvents = new HashSet<String>();
    static private HashSet<String> afterEvents = new HashSet<String>();

    static {
        // preInvoke events
        beforeEvents.add(ContainerEvent.BEFORE_CONTEXT_INITIALIZED);
        beforeEvents.add(ContainerEvent.BEFORE_CONTEXT_DESTROYED);
        beforeEvents.add(ContainerEvent.BEFORE_CONTEXT_ATTRIBUTE_ADDED);
        beforeEvents.add(ContainerEvent.BEFORE_CONTEXT_ATTRIBUTE_REMOVED);
        beforeEvents.add(ContainerEvent.BEFORE_CONTEXT_ATTRIBUTE_REPLACED);
        beforeEvents.add(ContainerEvent.BEFORE_REQUEST_INITIALIZED);
        beforeEvents.add(ContainerEvent.BEFORE_REQUEST_DESTROYED);
        beforeEvents.add(ContainerEvent.BEFORE_SESSION_CREATED);
        beforeEvents.add(ContainerEvent.BEFORE_SESSION_DESTROYED);
        beforeEvents.add(ContainerEvent.BEFORE_SESSION_ATTRIBUTE_ADDED);
        beforeEvents.add(ContainerEvent.BEFORE_SESSION_ATTRIBUTE_REMOVED);
        beforeEvents.add(ContainerEvent.BEFORE_SESSION_ATTRIBUTE_REPLACED);
        beforeEvents.add(ContainerEvent.BEFORE_SESSION_VALUE_UNBOUND);
        beforeEvents.add(ContainerEvent.BEFORE_FILTER_INITIALIZED);
        beforeEvents.add(ContainerEvent.BEFORE_FILTER_DESTROYED);

        // postInvoke events
        afterEvents.add(ContainerEvent.AFTER_CONTEXT_INITIALIZED);
        afterEvents.add(ContainerEvent.AFTER_CONTEXT_DESTROYED);
        afterEvents.add(ContainerEvent.AFTER_CONTEXT_ATTRIBUTE_ADDED);
        afterEvents.add(ContainerEvent.AFTER_CONTEXT_ATTRIBUTE_REMOVED);
        afterEvents.add(ContainerEvent.AFTER_CONTEXT_ATTRIBUTE_REPLACED);
        afterEvents.add(ContainerEvent.AFTER_REQUEST_INITIALIZED);
        afterEvents.add(ContainerEvent.AFTER_REQUEST_DESTROYED);
        afterEvents.add(ContainerEvent.AFTER_SESSION_CREATED);
        afterEvents.add(ContainerEvent.AFTER_SESSION_DESTROYED);
        afterEvents.add(ContainerEvent.AFTER_SESSION_ATTRIBUTE_ADDED);
        afterEvents.add(ContainerEvent.AFTER_SESSION_ATTRIBUTE_REMOVED);
        afterEvents.add(ContainerEvent.AFTER_SESSION_ATTRIBUTE_REPLACED);
        afterEvents.add(ContainerEvent.AFTER_SESSION_VALUE_UNBOUND);
        afterEvents.add(ContainerEvent.AFTER_FILTER_INITIALIZED);
        afterEvents.add(ContainerEvent.AFTER_FILTER_DESTROYED);
    }

    private InvocationManager invocationMgr;
    private InjectionManager injectionMgr;

    public WebContainerListener(InvocationManager invocationMgr,
                                InjectionManager injectionMgr) {
        this.invocationMgr = invocationMgr;
        this.injectionMgr = injectionMgr;
    }

    public void containerEvent(ContainerEvent event) {
        if(_logger.isLoggable(Level.FINEST)) {
	    _logger.log(Level.FINEST,"ContainerEvent: " +
                        event.getType() + "," +
                        event.getContainer() + "," +
                        event.getData());
        }

        String type = event.getType();

        try {
            WebModule wm = (WebModule) event.getContainer();
            if (beforeEvents.contains(type)) {
                preInvoke(wm);
            } else if (afterEvents.contains(type)) {
                if (type.equals(ContainerEvent.AFTER_FILTER_DESTROYED) ||
                        type.equals(ContainerEvent.AFTER_CONTEXT_DESTROYED)) {
                    preDestroy(event);
                }
                postInvoke(wm);
            } else if (ContainerEvent.PRE_DESTROY.equals(type)) {
                preInvoke(wm);
                preDestroy(event);
                postInvoke(wm);
            }
        } catch (Throwable t) {
            String msg = rb.getString(
                "containerListener.exceptionDuringHandleEvent");
            msg = MessageFormat.format(msg,
                new Object[] { type, event.getContainer() });
            _logger.log(Level.SEVERE, msg, t);
        }
    }

    private void preInvoke(WebModule ctx) {
        WebModule wm = (WebModule)ctx;
        ComponentInvocation inv = new WebComponentInvocation(wm);
        invocationMgr.preInvoke(inv);
    }

    private void postInvoke(WebModule ctx) {
        WebModule wm = (WebModule)ctx;
        ComponentInvocation inv = new WebComponentInvocation(wm);
        invocationMgr.postInvoke(inv);
    }

    /**
     * Invokes preDestroy on the instance embedded in the given ContainerEvent.
     *
     * @param event The ContainerEvent to process
     */
    private void preDestroy(ContainerEvent event) {
        try {
            injectionMgr.destroyManagedObject(event.getData());
        } catch (Throwable t) {
            String msg = rb.getString(
                "containerListener.exceptionDuringDestroyManagedObject");
            msg = MessageFormat.format(msg,
                new Object[] { event.getData(), event.getContainer() });
            _logger.log(Level.SEVERE, msg, t);
        }
    }
}
