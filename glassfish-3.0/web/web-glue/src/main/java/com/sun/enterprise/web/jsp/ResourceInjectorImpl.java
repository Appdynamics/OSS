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

package com.sun.enterprise.web.jsp;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.jsp.tagext.JspTag;
import org.apache.catalina.core.ApplicationContextFacade;
import org.apache.catalina.core.StandardContext;
import org.glassfish.jsp.api.ResourceInjector;
import org.glassfish.internal.api.ServerContext;
import com.sun.enterprise.container.common.spi.util.InjectionManager;
import com.sun.enterprise.deployment.JndiNameEnvironment;
import com.sun.enterprise.web.WebModule;
import com.sun.logging.LogDomains; 

/**
 * Implementation of org.glassfish.jsp.api.ResourceInjector
 *
 * @author Jan Luehe
 */
public class ResourceInjectorImpl implements ResourceInjector {

    protected static final Logger _logger = LogDomains.getLogger(
            ResourceInjectorImpl.class, LogDomains.WEB_LOGGER);
    protected static final ResourceBundle _rb = _logger.getResourceBundle(); 

    private InjectionManager injectionMgr;
    private JndiNameEnvironment desc;
    private WebModule webModule;

    public ResourceInjectorImpl(WebModule webModule) {
        this.webModule = webModule;
        this.desc = webModule.getWebBundleDescriptor();
        ServerContext serverContext = webModule.getServerContext();
        if (serverContext == null) {
            throw new IllegalStateException(
                    _rb.getString("resource.injector.noservercontext"));
        }
        this.injectionMgr = serverContext.getDefaultHabitat().getByContract(
            InjectionManager.class);
    }

    /**
     * Instantiates and injects the given tag handler class.
     *
     * @param clazz the TagHandler class to be instantiated and injected
     *
     * @throws Exception if an error has occurred during instantiation or
     * injection
     */
    public <T extends JspTag> T createTagHandlerInstance(Class<T> clazz)
            throws Exception {
        return webModule.getWebContainer().createTagHandlerInstance(
            webModule, clazz);
    }

    /**
     * Invokes any @PreDestroy methods defined on the instance's class
     * (and super-classes).
     *
     * @param handler The tag handler instance whose PreDestroy-annotated
     * method to call
     */
    public void preDestroy(JspTag handler) {
        if (desc != null) {
            try {
                injectionMgr.invokeInstancePreDestroy(handler, desc);
            } catch (Exception e) {
                String msg = _rb.getString(
                    "webcontainer.exceptionDuringJspTagHandlerPredestroy");
                msg = MessageFormat.format(msg, handler);
                _logger.log(Level.WARNING, msg, e);
            }
        }
    }

}
