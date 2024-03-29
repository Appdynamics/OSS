/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * 
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
 */

package org.glassfish.appclient.server.core.jws;

import com.sun.grizzly.tcp.http11.GrizzlyAdapter;
import com.sun.grizzly.tcp.http11.GrizzlyRequest;
import com.sun.grizzly.tcp.http11.GrizzlyResponse;
import com.sun.logging.LogDomains;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import org.glassfish.appclient.server.core.jws.servedcontent.Content;
import org.glassfish.appclient.server.core.jws.servedcontent.StaticContent;

/**
 *
 * @author tjquinn
 */
public class RestrictedContentAdapter extends GrizzlyAdapter {

    protected final static String LAST_MODIFIED_HEADER_NAME = "Last-Modified";
    protected final static String DATE_HEADER_NAME = "Date";
    protected final static String IF_MODIFIED_SINCE = "If-Modified-Since";

    private final static String LINE_SEP = System.getProperty("line.separator");
    
    private final Logger logger = LogDomains.getLogger(
            RestrictedContentAdapter.class, LogDomains.ACC_LOGGER);

    private enum State {
        RESUMED,
        SUSPENDED
    }

    private volatile State state = State.RESUMED;

    private final String contextRoot;

    private final ConcurrentHashMap<String,StaticContent> content =
            new ConcurrentHashMap<String,StaticContent>();

    public RestrictedContentAdapter(
            final String contextRoot,
            final Map<String,StaticContent> content) throws IOException {
        this(contextRoot);
        this.content.putAll(content);
        /*
         * Preload the adapter's cache with the static content.  This helps
         * performance but is essential to the operation of the adapter.
         * Normally the Grizzly logic will qualify the URI in the request
         * with the root folder of the adapter.  But the files this
         * adapter needs to serve can come from a variety of places that
         * might not share any common parent directory for the root folder.
         * Preloading the cache with the known static content lets the Grizzly
         * logic serve the files we want, from whereever they are.
         */
        for (Map.Entry<String,StaticContent> sc : content.entrySet()) {
            cache.put(sc.getKey(), sc.getValue().file());
        }
        logger.fine(logPrefix() + "Initial static content loaded " +
                dumpContent());
    }

    public RestrictedContentAdapter(final String contextRoot) {
        /*
         * Turn off the default static resource handling.  We do our own from
         * our service method, rather than letting the StaticResourcesAdapter
         * (superclass) logic have a try at each request first.
         */
        this.contextRoot = contextRoot;
//        this.userFriendlyContextRoot = userFriendlyContextRoot;
        setHandleStaticResources(false);

        setUseSendFile(false);
        commitErrorResponse = true;
    }

    @Override
    public void service(GrizzlyRequest gReq, GrizzlyResponse gResp) {
        try {
            if (!serviceContent(gReq, gResp)) {
                respondNotFound(gResp);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public String contextRoot() {
        return contextRoot;
    }

//    public String userFriendlyContextRoot() {
//        return userFriendlyContextRoot;
//    }

    public synchronized void addContentIfAbsent(final String relativeURIString,
            final StaticContent newContent) throws IOException {
        final StaticContent existingContent = content.get(relativeURIString);
        if (existingContent != null) {
            if ( ! existingContent.equals(newContent)) {
                logger.log(Level.FINE, "enterprise.deployment.appclient.jws.staticContentCollision",
                        new Object[] {relativeURIString, newContent.toString()});
            }
            return;
        }
        this.content.put(relativeURIString, newContent);
        this.cache.put(relativeURIString, newContent.file());
        logger.fine(logPrefix() + "adding static content " +
                relativeURIString + " " + newContent.toString());
    }

    public synchronized void addContentIfAbsent(
            final Map<String,StaticContent> staticContent) throws IOException {
        for (Map.Entry<String,StaticContent> entry : staticContent.entrySet()) {
            addContentIfAbsent(entry.getKey(), entry.getValue());
        }
    }


//    protected String relativizeURIString(final String uriString) {
//        String result;
//        if ( (result = relativizeURIString(contextRoot, uriString)) == null) {
//            if ( ( result = relativizeURIString(userFriendlyContextRoot, uriString)) == null) {
//                logger.log(Level.WARNING, "enterprise.deployment.appclient.jws.uriOutsideContextRoot",
//                    new Object[] {uriString, contextRoot, userFriendlyContextRoot});
//                return null;
//            }
//        }
//        return result;
//    }

    protected String relativizeURIString(final String candidateContextRoot,
            final String uriString) {
        if ( ! uriString.startsWith(candidateContextRoot)) {
            return null;
        }
        /*
         * If the user has requested the main JNLP using the user-friendly
         * context root then there candidate context root is exactly the
         * same as the context root.  There will be no substring after
         * that.
         */
        if (candidateContextRoot.equals(uriString)) {
            return "";
        }
        return uriString.substring(candidateContextRoot.length() + 1);
    }

    protected boolean serviceContent(GrizzlyRequest gReq, GrizzlyResponse gResp) throws IOException {

        String relativeURIString = relativizeURIString(contextRoot, gReq.getRequestURI());

        /*
         * "Forbidden" seems like a more helpful response than "not found"
         * if the corresponding app client has been suspended.
         */
        if (state == State.SUSPENDED) {
            finishErrorResponse(gResp, HttpServletResponse.SC_FORBIDDEN);
            logger.fine(logPrefix() + "is suspended; refused to serve static content requested using "
                    + (relativeURIString == null ? "null" : relativeURIString));
            return true;
        }

        if (relativeURIString == null) {
            logger.fine(logPrefix() + "Could not find static content requested using full request URI = "
                    + gReq.getRequestURI() + " - relativized URI was null");
            respondNotFound(gResp);
            return true;
        }
        /*
         * The Grizzly-managed cache could contain entries for non-existent
         * files that users request.  If the URI indicates it's a request for
         * static content make sure the requested URI is in the predefined staticContent
         * before having Grizzly serve it.
         *
         * Alternatively, if the URI indicates the request is for dynamic content
         * then handle that separately.
         *
         * If the request is for a URI in neither the static nor dynamic
         * content this adapter should serve, then just return a 404.
         */
        final StaticContent sc = content.get(relativeURIString);
        if (sc != null && sc.isAvailable()) {
            processContent(relativeURIString, gReq, gResp);
            return true;
        } else {
            finishErrorResponse(gResp, contentStateToResponseStatus(sc));
            final String scString = (sc == null ? "null" : sc.toString());
            final String scStateString = (sc == null ? "null" : sc.state().toString());
            logger.fine(logPrefix() + "Found static content for " + gReq.getMethod() +
                    ": " + relativeURIString + " -> " + scString +
                    " but could not serve it; its state is " + scStateString);
            return true;
        }
    }

    private void processContent(final String relativeURIString,
            final GrizzlyRequest gReq, final GrizzlyResponse gResp) {
        try {
            final StaticContent sc = content.get(relativeURIString);

            /*
             * No need to actually send the file if the request contains a
             * If-Modified-Since date and the file is not more recent.
             */
            final File fileToSend = sc.file();
            if (fileToSend != null) {
                if (returnIfClientCacheIsCurrent(relativeURIString,
                        gReq, fileToSend.lastModified())) {
                    return;
                }

                /*
                 * The client's cache is obsolete.  Be sure to set the
                 * time header values.
                 */
                gResp.addDateHeader(LAST_MODIFIED_HEADER_NAME, fileToSend.lastModified());
                gResp.addDateHeader(DATE_HEADER_NAME, System.currentTimeMillis());
            }

            /*
             * Delegate to the Grizzly implementation.
             */
            super.service(relativeURIString, gReq.getRequest(), gResp.getResponse());
            final int status = gResp.getStatus();
            if (status != HttpServletResponse.SC_OK) {
                logger.fine(logPrefix() + "Could not serve content for " +
                        relativeURIString + " - status = " + status);
            } else {
                logger.fine(logPrefix() + "Served static content for " + gReq.getMethod() +
                        ":" + sc.toString());
            }
            finishResponse(gResp, status);
        } catch (Exception e) {
//            gResp.getResponse().setErrorException(e);
//            finishErrorResponse(gResp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.log(Level.SEVERE, logPrefix() + relativeURIString, e);
        }
    }

    protected boolean returnIfClientCacheIsCurrent(final String relativeURIString,
            final GrizzlyRequest gReq,
            final long contentTimestamp) {
        final long ifModifiedSinceTime = gReq.getDateHeader(IF_MODIFIED_SINCE);
        boolean result;
        if (result = (ifModifiedSinceTime != -1) &&
            (ifModifiedSinceTime >= contentTimestamp)) {
            finishSuccessResponse(gReq.getResponse(), 
                    HttpServletResponse.SC_NOT_MODIFIED);
            logger.fine(logPrefix() + relativeURIString + " is already current on the client; no downloaded needed");
        }
        return result;
    }

    protected int contentStateToResponseStatus(Content content) throws IOException {
        int status;
        if (content == null) {
            status = HttpServletResponse.SC_NOT_FOUND;
        } else if (content.isAvailable()) {
            status = HttpServletResponse.SC_OK;
        } else {
            status = (content.state() == Content.State.SUSPENDED
                        ? HttpServletResponse.SC_FORBIDDEN
                        : HttpServletResponse.SC_NOT_FOUND);
        }
        return status;
    }
    
    public void suspend() {
        state = State.SUSPENDED;
    }

    public void resume() {
        state = State.RESUMED;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(logPrefix()).append(LINE_SEP);
        for (Map.Entry<String,StaticContent> entry : content.entrySet()) {
            sb.append("  ").append(entry.toString()).append(LINE_SEP);
        }
        return sb.toString();
    }


    protected void finishResponse(final GrizzlyResponse gResp, final int status) {
        gResp.setStatus(status);
        try {
            gResp.finishResponse();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void respondNotFound(final GrizzlyResponse gResp) {
        finishErrorResponse(gResp, HttpServletResponse.SC_NOT_FOUND);
    }

    protected void finishSuccessResponse(final GrizzlyResponse gResp, final int status) {
        finishResponse(gResp, status, false);
    }

    private void finishResponse(final GrizzlyResponse gResp, final int status,
            final boolean treatAsError) {
        gResp.setStatus(status);
        try {
            if (treatAsError && commitErrorResponse) {
                customizedErrorPage(gResp.getRequest().getRequest(), gResp.getResponse());
            }
            gResp.finishResponse();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void finishErrorResponse(final GrizzlyResponse gResp, final int status) {
        finishResponse(gResp, status, true);
    }

    protected String dumpContent() {
        if (content == null) {
            return "  Static content: not initialized";
        }
        if (content.isEmpty()) {
            return "  Static content: empty" + LINE_SEP;
        }
        final StringBuilder sb = new StringBuilder(LINE_SEP).append("  Static content");
        for (Map.Entry<String,StaticContent> entry : content.entrySet()) {
            sb.append("  ").append(entry.getKey()).append(" : ").
                    append(entry.getValue().toString()).append(LINE_SEP);
        }
        sb.append(LINE_SEP).append("  ========");
        return sb.toString();
    }

    protected String logPrefix() {
        return "Adapter[" + contextRoot + "] ";
    }
}
