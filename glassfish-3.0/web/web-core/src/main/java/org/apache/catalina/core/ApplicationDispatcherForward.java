/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2009 Sun Microsystems, Inc. All rights reserved.
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
 *
 *
 * This file incorporates work covered by the following copyright and
 * permission notice:
 *
 * Copyright 2004 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package org.apache.catalina.core;

import java.io.*;
import java.util.logging.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.catalina.Context;
import org.apache.catalina.Globals;
import org.apache.catalina.Wrapper;
import org.apache.catalina.connector.ClientAbortException;
import org.apache.catalina.deploy.ErrorPage;
import org.apache.catalina.util.RequestUtil;
import org.apache.catalina.util.ResponseUtil;
import org.apache.catalina.util.StringManager;
import org.apache.catalina.valves.ErrorReportValve;
import org.apache.catalina.connector.ResponseFacade;

/**
 * Class responsible for processing the result of a RD.forward() invocation
 * before committing the response.
 *
 * If sendError() was called during RD.forward(), an attempt is made to match
 * the status code against the error pages of the RD's associated context, or
 * those of the host on which the context has been deployed.
 *
 * The response contents are then committed, to comply with SRV.8.4
 * ("The Forward Method"):
 * 
 *   Before the forward method of the <code>RequestDispatcher</code>
 *   interface returns without exception, the response content must be
 *   sent and committed, and closed by the servlet container.
 *
 *   If an error occurs in the target of the
 *   <code>RequestDispatcher.forward()</code> the exception may be
 *   propogated back through all the calling filters and servlets and
 *   eventually back to the container.
 */
class ApplicationDispatcherForward {

    private static Logger log = Logger.getLogger(
        ApplicationDispatcherForward.class.getName());

    private static final StringManager sm =
        StringManager.getManager(org.apache.catalina.valves.Constants.Package);


    static void commit(ServletRequest request,
                       ServletResponse response,
                       Context context,
                       Wrapper wrapper)
            throws IOException, ServletException {

        if (!(request instanceof HttpServletRequest) ||
                !(response instanceof HttpServletResponse)) {
            closeResponse(response);
            return;
        }

        HttpServletRequest hreq = (HttpServletRequest) request;
        HttpServletResponse hres = (HttpServletResponse) response;
        int statusCode = hres.getStatus();
        Object exception = hreq.getAttribute(
            RequestDispatcher.ERROR_EXCEPTION);
        String errorReportValveClass = 
            ((StandardHost)(context.getParent())).getErrorReportValveClass();
        if (errorReportValveClass != null && statusCode >= 400
                && exception == null) {
            boolean matchFound = status(hreq, hres,
                getResponseFacade(hres), context, wrapper, statusCode);
            if (!matchFound) {
                serveDefaultErrorPage(hreq, hres,
                    getResponseFacade(hres), statusCode);
            }
        }

        /*
         * Commit the response only if no exception
         */
        if (statusCode < 400
                || (exception == null && errorReportValveClass != null)) {
            closeResponse(response);
        }
    }


    /*
     * Searches and processes a custom error page for the given status code.
     *
     * This method attempts to map the given status code to an error page,
     * using the mappings of the given context or those of the host on which
     * the given context has been deployed.
     *
     * If a match is found using the context mappings, the request is forwarded
     * to the error page. Otherwise, if a match is found using the host 
     * mappings, the contents of the error page are returned. If no match is
     * found, no action is taken.
     *
     * @return true if a matching error page was found, false otherwise
     */
    private static boolean status(HttpServletRequest request,
                                  HttpServletResponse response,
                                  ResponseFacade responseFacade,
                                  Context context,
                                  Wrapper wrapper,
                                  int statusCode) {

        /*
         * Attempt error-page mapping only if response.sendError(), as
         * opposed to response.setStatus(), was called.
         */
        if (!responseFacade.isError()) {
            return false;
        }

        boolean matchFound = false;

        ErrorPage errorPage = context.findErrorPage(statusCode);
        if (errorPage != null) {

            matchFound = true;

            // Prevent infinite loop
            String requestPath = (String) request.getAttribute(
                Globals.DISPATCHER_REQUEST_PATH_ATTR);
            if (requestPath == null
                    || !requestPath.equals(errorPage.getLocation())) {
                String message = RequestUtil.filter(responseFacade.getMessage());
                if (message == null) {
                    message = "";
                }
                prepareRequestForDispatch(request,
                                          wrapper,
                                          errorPage.getLocation(),
                                          statusCode,
                                          message);
                custom(request, response, responseFacade, errorPage, context);
            }
        } else {
            errorPage = ((StandardHost) context.getParent()).findErrorPage(
                                            statusCode);
            if (errorPage != null) {
                matchFound = true;
                try {
                    serveErrorPage(response, errorPage, statusCode);
                } catch (Exception e) {
                    log.log(Level.WARNING,
                            "Exception processing " + errorPage, e);
                }
            }
        }

        return matchFound;
    }


    /**
     * Handles an HTTP status code or exception by forwarding control
     * to the location included in the specified errorPage object. 
     */
    private static void custom(HttpServletRequest request,
                               HttpServletResponse response,
                               ResponseFacade responseFacade,
                               ErrorPage errorPage,
                               Context context) {
        try {
            // Forward control to the specified error page
            if (response.isCommitted()) {
                /*
                 * If the target of the RD.forward() has called
                 * response.sendError(), the response will have been committed,
                 * and any attempt to RD.forward() the response to the error
                 * page will cause an IllegalStateException.
                 * Uncommit the response.
                 */
                resetResponse(responseFacade);
            }
            ServletContext servletContext = context.getServletContext();
            ApplicationDispatcher dispatcher = (ApplicationDispatcher)
                servletContext.getRequestDispatcher(errorPage.getLocation());
            dispatcher.dispatch(request, response, DispatcherType.ERROR);
        } catch (IllegalStateException ise) {
            log.log(Level.WARNING, "Exception processing " + errorPage, ise);
        } catch (Throwable t) {
            log.log(Level.WARNING, "Exception processing " + errorPage, t);
        }
    }


    /**
     * Adds request attributes in preparation for RD.forward().
     */
    private static void prepareRequestForDispatch(HttpServletRequest request,
                                                  Wrapper errorServlet,
                                                  String errorPageLocation,
                                                  int errorCode,
                                                  String errorMessage) {
        request.setAttribute(RequestDispatcher.ERROR_REQUEST_URI,
                             request.getRequestURI());

        if (errorServlet != null) {
            // Save the logical name of the servlet in which the error occurred
            request.setAttribute(RequestDispatcher.ERROR_SERVLET_NAME,
                                 errorServlet.getName());
        }

        request.setAttribute(Globals.DISPATCHER_REQUEST_PATH_ATTR,
                             errorPageLocation);

        request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE,
                             Integer.valueOf(errorCode));

        request.setAttribute(RequestDispatcher.ERROR_MESSAGE, errorMessage);
    }

    /**
     * Copies the contents of the given error page to the response.
     */
    private static void serveErrorPage(HttpServletResponse response,
                                       ErrorPage errorPage,
                                       int statusCode)
            throws Exception {

        ServletOutputStream ostream = null;
        PrintWriter writer = null;
        FileReader reader = null;
        BufferedInputStream istream = null;
        IOException ioe = null;

        String message = errorPage.getReason();
        if (message != null && !response.isCommitted()) {
            response.reset();
            response.setStatus(statusCode, message);
        }
         
        try {
            ostream = response.getOutputStream();
        } catch (IllegalStateException e) {
            // If it fails, we try to get a Writer instead if we're
            // trying to serve a text file
            writer = response.getWriter();
        }

        if (writer != null) {
            reader = new FileReader(errorPage.getLocation());
            ioe = ResponseUtil.copy(reader, writer);
            try {
                reader.close();
            } catch (Throwable t) {
                ;
            }
        } else {
            istream = new BufferedInputStream(
                new FileInputStream(errorPage.getLocation()));
            ioe = ResponseUtil.copy(istream, ostream);
            try {
                istream.close();
            } catch (Throwable t) {
                ;
            }
        }

        // Rethrow any exception that may have occurred
        if (ioe != null) {
            throw ioe;
        }
    }


    /**
     * Renders the default error page.
     */
    private static void serveDefaultErrorPage(HttpServletRequest request,
                                              HttpServletResponse response,
                                              ResponseFacade responseFacade,
                                              int statusCode)
            throws IOException, ServletException {

        // Do nothing on a 1xx, 2xx and 3xx status
        if (response.isCommitted() || statusCode < 400
                || responseFacade.getContentCount() > 0
                || Boolean.TRUE.equals(request.getAttribute("org.glassfish.jsp.error_handled"))) {
            return;
        }

        String message = RequestUtil.filter(responseFacade.getMessage());
        if (message == null) {
            message = "";
        }

        // Do nothing if there is no report for the specified status code
        String report = null;
        try {
            report = sm.getString("http." + statusCode, message);
        } catch (Throwable t) {
            ;
        }
        if (report == null) {
            return;
        }

        String responseContents =
            ErrorReportValve.makeErrorPage(statusCode, message, null, null,
                                           report, response);
        // START SJSAS 6412710
        response.setLocale(sm.getResourceBundleLocale(response.getLocale()));
        // END SJSAS 6412710

        try {
            response.setContentType("text/html");
            response.getWriter().write(responseContents);
        } catch (Throwable t) {
            log.log(Level.WARNING, "Exception sending default error page", t);
        }
    }

    
    private static ResponseFacade getResponseFacade(ServletResponse response) {
   
        while (response instanceof ServletResponseWrapper) {
            response = ((ServletResponseWrapper) response).getResponse();
        }

        return ((ResponseFacade) response);
    }
    
    private static void resetResponse(ResponseFacade responseFacade) {
        responseFacade.setSuspended(false);
        responseFacade.setAppCommitted(false);
    }

    private static void closeResponse(ServletResponse response) {
        try {
            PrintWriter writer = response.getWriter();
            writer.flush();
            writer.close();
        } catch (IllegalStateException e) {
            try {
                ServletOutputStream stream = response.getOutputStream();
                stream.flush();
                stream.close();
            } catch (IllegalStateException f) {
                ;
            } catch (IOException f) {
                ;
            }
        } catch (IOException e) {
            ;
        }
    }
}
