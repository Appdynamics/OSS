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

package org.glassfish.webservices;


import com.sun.enterprise.deployment.Application;

import com.sun.logging.LogDomains;

import org.apache.catalina.util.Base64;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.glassfish.webservices.monitoring.Endpoint;
import org.glassfish.webservices.monitoring.WebServiceEngineImpl;
import org.glassfish.webservices.monitoring.WebServiceTesterServlet;
import org.glassfish.ejb.api.EjbEndpointFacade;
import org.glassfish.ejb.spi.WSEjbEndpointRegistry;

/**
 * Servlet responsible for invoking EJB webservice endpoint.
 *
 * Most of this code used to be in
 * com.sun.enterprise.webservice.EjbWebServiceValve.
 *
 * @author	Qingqing Ouyang
 * @author	Kenneth Saks
 * @author	Jan Luehe
 */
public class EjbWebServiceServlet extends HttpServlet {

    private Logger logger = LogDomains.getLogger(this.getClass(),LogDomains.WEBSERVICES_LOGGER);

    private ResourceBundle rb = logger.getResourceBundle()   ;
    private static final Base64 base64Helper = new Base64();
    private static final String AUTHORIZATION_HEADER = "authorization";
    private SecurityService secServ;

    public EjbWebServiceServlet() {
        super();
        if (org.glassfish.internal.api.Globals.getDefaultHabitat() != null) {
            secServ = org.glassfish.internal.api.Globals.get(SecurityService.class);
        }
    }

    protected void service(HttpServletRequest hreq,
                           HttpServletResponse hresp)

            throws ServletException, IOException {

        boolean dispatch = true;

        String requestUriRaw = hreq.getRequestURI();
        String requestUri = (requestUriRaw.charAt(0) == '/') ?
                requestUriRaw.substring(1) : requestUriRaw;
        String query = hreq.getQueryString();

        // check if it is a tester servlet invocation
        if ("Tester".equalsIgnoreCase(query)) {
            Endpoint endpoint = WebServiceEngineImpl.getInstance().getEndpoint(hreq.getRequestURI());
            if((endpoint.getDescriptor().isSecure()) ||
                    (endpoint.getDescriptor().getMessageSecurityBinding() != null)) {
                String message = endpoint.getDescriptor().getWebService().getName() +
                        "is a secured web service; Tester feature is not supported for secured services";
                (new WsUtil()).writeInvalidMethodType(hresp, message);
                return;
            }
            if (endpoint!=null && Boolean.parseBoolean(endpoint.getDescriptor().getDebugging())) {
                dispatch = false;
                WebServiceTesterServlet.invoke(hreq, hresp,
                        endpoint.getDescriptor());
            }
        }

        if (dispatch) {
            WebServiceEjbEndpointRegistry wsejbEndpointRegistry = (WebServiceEjbEndpointRegistry) org.glassfish.internal.api.Globals.getDefaultHabitat().getComponent(
                    WSEjbEndpointRegistry.class);
            EjbRuntimeEndpointInfo ejbEndpoint =
                    wsejbEndpointRegistry.getEjbWebServiceEndpoint(requestUri, hreq.getMethod(), query);

            if (ejbEndpoint != null) {
                /*
                 * We can actually assert that ejbEndpoint is != null,
                 * because this EjbWebServiceServlet would not have been
                 * invoked otherwise
                 */
                dispatchToEjbEndpoint(hreq, hresp, ejbEndpoint);
            }
        }
    }


    private void dispatchToEjbEndpoint(HttpServletRequest hreq,
                                       HttpServletResponse hresp,
                                       EjbRuntimeEndpointInfo ejbEndpoint) {

        String scheme = hreq.getScheme();
        String expectedScheme = ejbEndpoint.getEndpoint().isSecure() ?
                "https" : "http";

        if( !expectedScheme.equalsIgnoreCase(scheme) ) {
            logger.log(Level.WARNING, "Invalid request scheme for Endpoint " +
                    ejbEndpoint.getEndpoint().getEndpointName() + ". " +
                    "Expected " + expectedScheme + " . Received " + scheme);
            return;
        }


        EjbEndpointFacade container = ejbEndpoint.getContainer();
        ClassLoader savedClassLoader = null;

        boolean authenticated = false;
        try {
            // Set context class loader to application class loader
            savedClassLoader = Thread.currentThread().getContextClassLoader();
            Thread.currentThread().setContextClassLoader(container.getEndpointClassLoader());

            // compute realmName
            String realmName = null;
            Application app = ejbEndpoint.getEndpoint().getBundleDescriptor().getApplication();
            if (app != null) {
                realmName = app.getRealm();
            }
            if (realmName == null) {
                realmName = ejbEndpoint.getEndpoint().getRealm();
            }

            if (realmName == null) {
                // use the same logic as BasicAuthenticator
                realmName = hreq.getServerName() + ":" + hreq.getServerPort();
            }

            try {
                if (secServ != null) {
                    WebServiceContextImpl context = (WebServiceContextImpl) ((EjbRuntimeEndpointInfo) ejbEndpoint).getWebServiceContext();
                    authenticated = secServ.doSecurity(hreq, ejbEndpoint, realmName, context);
                }

            } catch(Exception e) {
                //sendAuthenticationEvents(false, hreq.getRequestURI(), null);
                logger.log(Level.WARNING, "authentication failed for " +
                        ejbEndpoint.getEndpoint().getEndpointName(),
                        e);
            }

            if (!authenticated) {
                hresp.setHeader("WWW-Authenticate",
                        "Basic realm=\"" + realmName + "\"");
                hresp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            // depending on the jaxrpc or jax-ws version, this will return the
            // right dispatcher.
            EjbMessageDispatcher msgDispatcher = ejbEndpoint.getMessageDispatcher();
            msgDispatcher.invoke(hreq, hresp, getServletContext(), ejbEndpoint);

        } catch(Throwable t) {
            logger.log(Level.WARNING, "", t);
        } finally {
            // remove any security context from the thread local before returning
            if (secServ != null) {
                secServ.resetSecurityContext();
                secServ.resetPolicyContext();
            }
            // Restore context class loader
            Thread.currentThread().setContextClassLoader(savedClassLoader);
        }
        return;
    }
}
