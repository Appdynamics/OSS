/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2008 Sun Microsystems, Inc. All rights reserved.
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




package org.apache.catalina.valves;


import java.io.IOException;
import java.io.Writer;
import java.util.Locale;
import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import org.apache.catalina.HttpResponse;
import org.apache.catalina.Logger;
import org.apache.catalina.Request;
import org.apache.catalina.Response;
import org.apache.catalina.util.RequestUtil;
import org.apache.catalina.util.ServerInfo;
import org.apache.catalina.util.StringManager;

/**
 * <p>Implementation of a Valve that outputs HTML error pages.</p>
 *
 * <p>This Valve should be attached at the Host level, although it will work
 * if attached to a Context.</p>
 *
 * <p>HTML code from the Cocoon 2 project.</p>
 *
 * @author Remy Maucherat
 * @author Craig R. McClanahan
 * @author <a href="mailto:nicolaken@supereva.it">Nicola Ken Barozzi</a> Aisa
 * @author <a href="mailto:stefano@apache.org">Stefano Mazzocchi</a>
 * @version $Revision: 1.19 $ $Date: 2007/05/05 05:32:41 $
 */

public class ErrorReportValve
    extends ValveBase {

    private static final java.util.logging.Logger log =
        java.util.logging.Logger.getLogger(
            ErrorReportValve.class.getName());

    /**
     * The descriptive information related to this implementation.
     */
    private static final String info =
        "org.apache.catalina.valves.ErrorReportValve/1.0";

    /**
     * The StringManager for this package.
     */
    protected static final StringManager sm =
        StringManager.getManager(Constants.Package);


    // ----------------------------------------------------- Instance Variables

    /**
     * The debugging detail level for this component.
     */
    private int debug = 0;


    // ------------------------------------------------------------- Properties


    /**
     * Return descriptive information about this Valve implementation.
     */
    public String getInfo() {

        return (info);

    }


    // --------------------------------------------------------- Public Methods


    /**
     * Invoke the next Valve in the sequence. When the invoke returns, check
     * the response state, and output an error report is necessary.
     *
     * @param request The servlet request to be processed
     * @param response The servlet response to be created
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
     public int invoke(Request request, Response response)
         throws IOException, ServletException {

        // Perform the request
        return INVOKE_NEXT;

     }

     public void postInvoke(Request request, Response response)
         throws IOException, ServletException {

        ServletRequest sreq = (ServletRequest) request;
        Throwable throwable =
            (Throwable) sreq.getAttribute(RequestDispatcher.ERROR_EXCEPTION);

        ServletResponse sresp = (ServletResponse) response;
        if (sresp.isCommitted()) {
            return;
        }

        if (throwable != null) {

            // The response is an error
            response.setError();

            // START PWC 6254469
            // Save (and later restore) the response encoding, because the
            // following call to reset() will reset it to the default
            // encoding (ISO-8859-1).
            String responseCharEnc = sresp.getCharacterEncoding();
            // END PWC 6254469

            // Reset the response (if possible)
            try {
                sresp.reset();
            } catch (IllegalStateException e) {
                ;
            }

            // START PWC 6254469
            /*
             * Restore the previously saved response encoding only if it is
             * different from the default (ISO-8859-1). This is important so
             * that a subsequent call to ServletResponse.setLocale() has an
             * opportunity to set it so it corresponds to the resource bundle
             * locale (see 6412710)
             */
            if (responseCharEnc != null && !responseCharEnc.equals(
                    com.sun.grizzly.tcp.Constants.DEFAULT_CHARACTER_ENCODING)) {
                sresp.setCharacterEncoding(responseCharEnc);
            }
            // END PWC 6254469

            ServletResponse sresponse = (ServletResponse) response;
            /* GlassFish 6386229
            if (sresponse instanceof HttpServletResponse)
                ((HttpServletResponse) sresponse).sendError
                    (HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            */
            // START GlassFish 6386229
            ((HttpServletResponse) sresponse).sendError
                (HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            // END GlassFish 6386229
        }

        response.setSuspended(false);

        try {
            report(request, response, throwable);
        } catch (Throwable tt) {
            ;
        }

    }


    /**
     * Return a String rendering of this object.
     */
    public String toString() {

        StringBuffer sb = new StringBuffer("ErrorReportValve[");
        sb.append(container.getName());
        sb.append("]");
        return (sb.toString());

    }


    // ------------------------------------------------------ Protected Methods


    /**
     * Prints out an error report.
     *
     * @param request The request being processed
     * @param response The response being generated
     * @param throwable The exception that occurred (which possibly wraps
     *  a root cause exception
     */
    protected void report(Request request, Response response,
                          Throwable throwable)
        throws IOException {

        /* GlassFish 6386229
        // Do nothing on non-HTTP responses
        if (!(response instanceof HttpResponse))
            return;
        */
        HttpResponse hresponse = (HttpResponse) response;
        /* GlassFish 6386229
        if (!(response instanceof HttpServletResponse))
            return;
        */
        HttpServletResponse hres = (HttpServletResponse) response;
        int statusCode = hresponse.getStatus();

        // Do nothing on a 1xx, 2xx and 3xx status
        // Do nothing if anything has been written already
        if (statusCode < 400 || (response.getContentCount() > 0))
            return;

        Throwable rootCause = null;

        if (throwable != null) {

            if (throwable instanceof ServletException)
                rootCause = ((ServletException) throwable).getRootCause();

        }

        String message = RequestUtil.filter(hresponse.getMessage());
        /* S1AS 4878272
        if (message == null)
            message = "";
        */
        // BEGIN S1AS 4878272
        if (message == null) {
            message = RequestUtil.filter(hresponse.getDetailMessage());
            if (message == null) {
                message = "";
            }
        }
        // END S1AS 4878272

        // Do nothing if there is no report for the specified status code
        String report = null;
        try {
            /* SJSAS 6412710
            report = sm.getString("http." + statusCode, message);
            */
            // START SJSAS 6412710
            report = sm.getString("http." + statusCode, message,
                                  hres.getLocale());
            // END SJSAS 6412710
        } catch (Throwable t) {
            ;
        }
        if (report == null)
            return;

        String errorPage = makeErrorPage(statusCode, message,
                                         throwable, rootCause,
                                         report, hres);

        // START SJSAS 6412710
        /*
         * If throwable is not null, we've already preserved any non-default
         * response encoding in postInvoke(), so that the throwable's exception
         * message can be delivered to the client without any loss of
         * information. The following call to ServletResponse.setLocale()
         * will not override the response encoding in this case.
         * For all other cases, the response encoding will be set according to
         * the resource bundle locale.
         */
        hres.setLocale(sm.getResourceBundleLocale(hres.getLocale()));
        // END SJSAS 6412710

        /* PWC 6254469
        // set the charset part of content type before getting the writer
        */
        try {
            hres.setContentType("text/html");
            /* PWC 6254469
            hres.setCharacterEncoding("UTF-8");
            */
        } catch (Throwable t) {
            if (debug >= 1)
                log("status.setContentType", t);
        }

        try {
            Writer writer = response.getReporter();
            if (writer != null) {
                // If writer is null, it's an indication that the response has
                // been hard committed already, which should never happen
                writer.write(errorPage);
            }
        } catch (IOException e) {
            ;
        } catch (IllegalStateException e) {
            ;
        }

    }


    /**
     * Log a message on the Logger associated with our Container (if any).
     *
     * @param message Message to be logged
     */
    protected void log(String message) {
        Logger logger = container.getLogger();
        if (logger != null) {
            logger.log(this.toString() + ": " + message);
        } else {
            log.info(this.toString() + ": " + message);
        }
    }


    /**
     * Log a message on the Logger associated with our Container (if any).
     *
     * @param message Message to be logged
     * @param t Associated exception
     */
    protected void log(String message, Throwable t) {
        Logger logger = container.getLogger();
        if (logger != null) {
            logger.log(this.toString() + ": " + message, t, Logger.WARNING);
        } else {
            log.log(java.util.logging.Level.WARNING,
                this.toString() + ": " + message, t);
        }
    }


    public static String makeErrorPage(int statusCode,
                                       String message,
                                       Throwable throwable,
                                       Throwable rootCause,
                                       String report,
                                       HttpServletResponse response) {

        // START SJSAS 6412710
        Locale responseLocale = response.getLocale();
        // END SJSAS 6412710

        String serverInfo = ServerInfo.getPublicServerInfo();

        StringBuffer sb = new StringBuffer();

        sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"");
        sb.append(" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
        sb.append("<html><head><title>");
        if (serverInfo != null && !serverInfo.isEmpty()) {
            sb.append(serverInfo).append(" - ");
        }
        /* 6412710
        sb.append(sm.getString("errorReportValve.errorReport"));
        */
        // START SJSAS 6412710
        sb.append(sm.getString("errorReportValve.errorReport",
                               responseLocale));
        // END SJSAS 6412710
        sb.append("</title>");
        sb.append("<style type=\"text/css\"><!--");
        sb.append(org.apache.catalina.util.TomcatCSS.TOMCAT_CSS);
        sb.append("--></style> ");
        sb.append("</head><body>");
        sb.append("<h1>");
        /* SJSAS 6412710
        sb.append(sm.getString("errorReportValve.statusHeader",
                               "" + statusCode, message)).append("</h1>");
        */
        // START SJSAS 6412710
        sb.append(sm.getString("errorReportValve.statusHeader",
                               "" + statusCode, message,
                               responseLocale)).append("</h1>");
        // END SJSAS 6412710
        sb.append("<hr/>");
        sb.append("<p><b>type</b> ");
        if (throwable != null) {
            /* SJSAS 6412710
            sb.append(sm.getString("errorReportValve.exceptionReport"));
            */
            // START SJJAS 6412710
            sb.append(sm.getString("errorReportValve.exceptionReport",
                                   responseLocale));
            // END SJSAS 6412710
        } else {
            /* SJSAS 6412710
            sb.append(sm.getString("errorReportValve.statusReport"));
            */
            // START SJSAS 6412710
            sb.append(sm.getString("errorReportValve.statusReport",
                                   responseLocale));
            // END SJSAS 6412710
        }
        sb.append("</p>");
        sb.append("<p><b>");
        /* SJSAS 6412710
        sb.append(sm.getString("errorReportValve.message"));
        */
        // START SJSAS 6412710
        sb.append(sm.getString("errorReportValve.message",
                               responseLocale));
        // END SJSAS 6412710
        sb.append("</b>");
        sb.append(message).append("</p>");
        sb.append("<p><b>");
        /* SJSAS 6412710
        sb.append(sm.getString("errorReportValve.description"));
        */
        // START SJSAS 6412710
        sb.append(sm.getString("errorReportValve.description",
                               responseLocale));
        // END SJSAS 6412710
        sb.append("</b>");
        sb.append(report);
        sb.append("</p>");

        if (throwable != null) {
            /* GlassFish 823
            String stackTrace = JdkCompat.getJdkCompat()
                .getPartialServletStackTrace(throwable);
            */
            sb.append("<p><b>");
            /* SJSAS 6412710
            sb.append(sm.getString("errorReportValve.exception"));
            */
            // START SJSAS 6412710
            sb.append(sm.getString("errorReportValve.exception",
                                   responseLocale));
            // END SJSAS 6412710
            sb.append("</b> <pre>");
            /* SJSAS 6387790
            sb.append(stackTrace);
            */
            /* GlassFish 823
            // START SJSAS 6387790
            sb.append(RequestUtil.filter(stackTrace));
            // END SJSAS 6387790
            */
            // START GlassFish 823
            sb.append(RequestUtil.filter(String.valueOf(throwable)));
            // END GlassFish 823
            sb.append("</pre></p>");

            while (rootCause != null) {
                /* GlassFish 823
                stackTrace = JdkCompat.getJdkCompat()
                    .getPartialServletStackTrace(rootCause);
                */
                sb.append("<p><b>");
                /* SJSAS 6412710
                sb.append(sm.getString("errorReportValve.rootCause"));
                */
                // START SJSAS 6412710
                sb.append(sm.getString("errorReportValve.rootCause",
                                       responseLocale));
                // END SJSAS 6412710
                sb.append("</b> <pre>");
                /* SJSAS 6387790
                sb.append(stackTrace);
                */
                /* GlassFish 823
                // START SJSAS 6387790
                sb.append(RequestUtil.filter(stackTrace));
                // END SJSAS 6387790
                */
                // START GlassFish 823
                sb.append(RequestUtil.filter(String.valueOf(rootCause)));
                // END GlassFish 823
                sb.append("</pre></p>");

                /* GlassFish 823
                // In case root cause is somehow heavily nested
                try {
                    rootCause = (Throwable)PropertyUtils.getProperty
                                                (rootCause, "rootCause");
                } catch (ClassCastException e) {
                    rootCause = null;
                } catch (IllegalAccessException e) {
                    rootCause = null;
                } catch (NoSuchMethodException e) {
                    rootCause = null;
                } catch (java.lang.reflect.InvocationTargetException e) {
                    rootCause = null;
                }
                */
                // START GlassFish 823
                rootCause = rootCause.getCause();
                // END GlassFish 823
            }

            sb.append("<p><b>");
            /* SJSAS 6412710
            sb.append(sm.getString("errorReportValve.note"));
            */
            // START SJSAS 6412710
            sb.append(sm.getString("errorReportValve.note",
                                   responseLocale));
            // END SJAS 6412710
            sb.append("</b> <u>");
            /* SJSAS 6412710
            sb.append(sm.getString("errorReportValve.rootCauseInLogs",
                                   ServerInfo.getServerInfo()));
            */
            // START SJSAS 6412710
            if (serverInfo != null && !serverInfo.isEmpty()) {
                sb.append(sm.getString("errorReportValve.rootCauseInLogs",
                                       serverInfo, responseLocale));
            } else {
                sb.append(sm.getString("errorReportValve.rootCauseInLogs",
                                       "server", responseLocale));
            }
            // END SJSAS 6412710
            sb.append("</u></p>");

        }

        sb.append("<hr/>");

        if (serverInfo != null && !serverInfo.isEmpty()) {
            sb.append("<h3>").append(serverInfo).append("</h3>");
        }

        sb.append("</body></html>");

        return sb.toString();
    }

}
