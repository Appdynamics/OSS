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

package org.apache.catalina.realm;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.cert.X509Certificate;
import java.util.*;
//START SJSAS 6202703
import java.text.SimpleDateFormat;
//END SJSAS 6202703
import java.util.logging.*;

import javax.management.Attribute;
import javax.management.MBeanRegistration;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
//START SJSAS 6202703
import org.apache.catalina.Authenticator;
import org.apache.catalina.authenticator.AuthenticatorBase;
//END SJSAS 6202703

import org.apache.catalina.Container;
import org.apache.catalina.Context;
import org.apache.catalina.Globals;
import org.apache.catalina.HttpRequest;
import org.apache.catalina.HttpResponse;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.Realm;
import org.apache.catalina.connector.Response;
import org.apache.catalina.core.ContainerBase;
import org.apache.catalina.deploy.LoginConfig;
import org.apache.catalina.deploy.SecurityConstraint;
import org.apache.catalina.deploy.SecurityCollection;
import org.apache.catalina.util.HexUtils;
import org.apache.catalina.util.LifecycleSupport;
import org.apache.catalina.util.MD5Encoder;
import org.apache.catalina.util.StringManager;
import org.apache.tomcat.util.modeler.Registry;
// START SJSWS 6324431
import org.apache.catalina.core.StandardContext;
// END SJSWS 6324431

/**
 * Simple implementation of <b>Realm</b> that reads an XML file to configure
 * the valid users, passwords, and roles.  The file format (and default file
 * location) are identical to those currently supported by Tomcat 3.X.
 *
 * @author Craig R. McClanahan
 * @version $Revision: 1.14 $ $Date: 2007/04/18 17:27:23 $
 */

public abstract class RealmBase
    implements Lifecycle, Realm, MBeanRegistration {

    private static Logger log = Logger.getLogger(RealmBase.class.getName());
    
    //START SJSAS 6202703
    /**
     * "Expires" header always set to Date(1), so generate once only
     */
    private static final String DATE_ONE =
            (new SimpleDateFormat(Response.HTTP_RESPONSE_DATE_HEADER,
            Locale.US)).format(new Date(1));
    //END SJSAS 6202703

    // ----------------------------------------------------- Instance Variables

     /**
     * The debugging detail level for this component.
     */
    protected int debug = 0;

    /**
     * The Container with which this Realm is associated.
     */
    protected Container container = null;


    /**
     * Flag indicating whether a check to see if the request is secure is
     * required before adding Pragma and Cache-Control headers when proxy 
     * caching has been disabled
     */
    protected boolean checkIfRequestIsSecure = false;


    /**
     * Digest algorithm used in storing passwords in a non-plaintext format.
     * Valid values are those accepted for the algorithm name by the
     * MessageDigest class, or <code>null</code> if no digesting should
     * be performed.
     */
    protected String digest = null;

    /**
     * The encoding charset for the digest.
     */
    protected String digestEncoding = null;


    /**
     * Descriptive information about this Realm implementation.
     */
    protected static final String info =
        "org.apache.catalina.realm.RealmBase/1.0";


    /**
     * The lifecycle event support for this component.
     */
    protected LifecycleSupport lifecycle = new LifecycleSupport(this);


    /**
     * The MessageDigest object for digesting user credentials (passwords).
     */
    protected MessageDigest md = null;


    /**
     * The MD5 helper object for this class.
     */
    protected static final MD5Encoder md5Encoder = new MD5Encoder();


    /**
     * MD5 message digest provider.
     */
    protected static MessageDigest md5Helper;


    /**
     * The string manager for this package.
     */
    protected static final StringManager sm =
        StringManager.getManager(Constants.Package);


    /**
     * Has this component been started?
     */
    protected boolean started = false;


    /**
     * The property change support for this component.
     */
    protected PropertyChangeSupport support = new PropertyChangeSupport(this);


    /**
     * Should we validate client certificate chains when they are presented?
     */
    protected boolean validate = true;


    // ------------------------------------------------------------- Properties


    /**
     * Return the Container with which this Realm has been associated.
     */
    public Container getContainer() {

        return (container);

    }

    /**
     * Return the debugging detail level for this component.
     */
    public int getDebug() {

        return (this.debug);

    }


    /**
     * Set the debugging detail level for this component.
     *
     * @param debug The new debugging detail level
     */
    public void setDebug(int debug) {

        this.debug = debug;

    }

    /**
     * Set the Container with which this Realm has been associated.
     *
     * @param container The associated Container
     */
    public void setContainer(Container container) {

        Container oldContainer = this.container;
        this.container = container;
        this.checkIfRequestIsSecure = container.isCheckIfRequestIsSecure();
        support.firePropertyChange("container", oldContainer, this.container);

    }

    /**
     * Return the digest algorithm  used for storing credentials.
     */
    public String getDigest() {

        return digest;

    }


    /**
     * Set the digest algorithm used for storing credentials.
     *
     * @param digest The new digest algorithm
     */
    public void setDigest(String digest) {

        this.digest = digest;

    }

    /**
     * Returns the digest encoding charset.
     *
     * @return The charset (may be null) for platform default
     */
    public String getDigestEncoding() {
        return digestEncoding;
    }

    /**
     * Sets the digest encoding charset.
     *
     * @param charset The charset (null for platform default)
     */
    public void setDigestEncoding(String charset) {
        digestEncoding = charset;
    }

    /**
     * Return descriptive information about this Realm implementation and
     * the corresponding version number, in the format
     * <code>&lt;description&gt;/&lt;version&gt;</code>.
     */
    public String getInfo() {

        return info;

    }


    /**
     * Return the "validate certificate chains" flag.
     */
    public boolean getValidate() {

        return (this.validate);

    }


    /**
     * Set the "validate certificate chains" flag.
     *
     * @param validate The new validate certificate chains flag
     */
    public void setValidate(boolean validate) {

        this.validate = validate;

    }


    // --------------------------------------------------------- Public Methods


    
    /**
     * Add a property change listener to this component.
     *
     * @param listener The listener to add
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {

        support.addPropertyChangeListener(listener);

    }


    /**
     * Return the Principal associated with the specified username and
     * credentials, if there is one; otherwise return <code>null</code>.
     *
     * @param username Username of the Principal to look up
     * @param credentials Password or other credentials to use in
     *  authenticating this username
     */
    public Principal authenticate(String username, String credentials) {

        String serverCredentials = getPassword(username);

        boolean validated ;
        if ( serverCredentials == null ) {
            validated = false;
        } else if(hasMessageDigest()) {
            validated = serverCredentials.equalsIgnoreCase(digest(credentials));
        } else {
            validated = serverCredentials.equals(credentials);
        }
        if(! validated ) {
            return null;
        }
        return getPrincipal(username);
    }


    /**
     * Return the Principal associated with the specified username, which
     * matches the digest calculated using the given parameters using the
     * method described in RFC 2069; otherwise return <code>null</code>.
     *
     * @param username Username of the Principal to look up
     * @param clientDigest Digest which has been submitted by the client
     * @param nOnce Unique (or supposedly unique) token which has been used
     * for this request
     * @param realm Realm name
     * @param md5a2 Second MD5 digest used to calculate the digest :
     * MD5(Method + ":" + uri)
     */
    public Principal authenticate(String username, String clientDigest,
                                  String nOnce, String nc, String cnonce,
                                  String qop, String realm,
                                  String md5a2) {

        String md5a1 = getDigest(username, realm);
        if (md5a1 == null)
            return null;
        String serverDigestValue = md5a1 + ":" + nOnce + ":" + nc + ":"
            + cnonce + ":" + qop + ":" + md5a2;

        byte[] valueBytes = null;
        if(getDigestEncoding() == null) {
            valueBytes = serverDigestValue.getBytes();
        } else {
            try {
                valueBytes = serverDigestValue.getBytes(getDigestEncoding());
            } catch (UnsupportedEncodingException uee) {
                log.log(Level.SEVERE,
                        "Illegal digestEncoding: " + getDigestEncoding(),
                        uee);
                throw new IllegalArgumentException(uee.getMessage());
            }
        }

        String serverDigest = null;
        // Bugzilla 32137
        synchronized(md5Helper) {
            serverDigest = md5Encoder.encode(md5Helper.digest(valueBytes));
        }

        if (log.isLoggable(Level.FINE)) {
            log.fine("Digest : " + clientDigest + " Username:" + username 
                     + " ClientSigest:" + clientDigest + " nOnce:" + nOnce 
                     + " nc:" + nc + " cnonce:" + cnonce + " qop:" + qop 
                     + " realm:" + realm + "md5a2:" + md5a2 
                     + " Server digest:" + serverDigest);
        }
        
        if (serverDigest.equals(clientDigest))
            return getPrincipal(username);
        else
            return null;
    }



    /**
     * Return the Principal associated with the specified chain of X509
     * client certificates.  If there is none, return <code>null</code>.
     *
     * @param certs Array of client certificates, with the first one in
     *  the array being the certificate of the client itself.
     */
    public Principal authenticate(X509Certificate certs[]) {

        if ((certs == null) || (certs.length < 1))
            return (null);

        // Check the validity of each certificate in the chain
        if (log.isLoggable(Level.FINE))
            log.fine("Authenticating client certificate chain");
        if (validate) {
            for (int i = 0; i < certs.length; i++) {
                if (log.isLoggable(Level.FINE))
                    log.fine("Checking validity for '" +
                             certs[i].getSubjectDN().getName() + "'");
                try {
                    certs[i].checkValidity();
                } catch (Exception e) {
                    if (log.isLoggable(Level.FINE))
                        log.log(Level.FINE, "Validity exception", e);
                    return (null);
                }
            }
        }

        // Check the existence of the client Principal in our database
        return (getPrincipal(certs[0].getSubjectDN().getName()));

    }

    
    /**
     * Execute a periodic task, such as reloading, etc. This method will be
     * invoked inside the classloading context of this container. Unexpected
     * throwables will be caught and logged.
     */
    public void backgroundProcess() {
    }


    /**
     * Return the SecurityConstraints configured to guard the request URI for
     * this request, or <code>null</code> if there is no such constraint.
     *
     * @param request Request we are processing
     * @param context Context the Request is mapped to
     */
    public SecurityConstraint[] findSecurityConstraints(
            HttpRequest request, Context context) {

        ArrayList results = null;
        // Are there any defined security constraints?
        if (!context.hasConstraints()) {
            if (log.isLoggable(Level.FINE))
                log.fine("  No applicable constraints defined");
            return (null);
        }

        HttpServletRequest hreq = (HttpServletRequest) request.getRequest();
        // Check each defined security constraint
        String uri = request.getRequestPathMB().toString();
        
        // START SJSWS 6324431
        String origUri = uri;
        boolean caseSensitiveMapping = 
            ((StandardContext)context).isCaseSensitiveMapping();
        if (uri != null && !caseSensitiveMapping) {
            uri = uri.toLowerCase();
        }
        // END SJSWS 6324431

        String method = hreq.getMethod();
        boolean found = false;

        List<SecurityConstraint> constraints = context.getConstraints();
        Iterator<SecurityConstraint> i = constraints.iterator(); 
        while (i.hasNext()) {
            SecurityConstraint constraint = i.next();
            SecurityCollection[] collection = constraint.findCollections();
                     
            // If collection is null, continue to avoid an NPE
            // See Bugzilla 30624
            if (collection == null) {
                continue;
            }

            if (log.isLoggable(Level.FINEST)) {
                /* SJSWS 6324431
                log.trace("  Checking constraint '" + constraints[i] +
                    "' against " + method + " " + uri + " --> " +
                    constraints[i].included(uri, method));
                */
                // START SJSWS 6324431
                log.finest("Checking constraint '" + constraint +
                           "' against " + method + " " + origUri +
                           " --> " +
                           constraint.included(uri, method, 
                                               caseSensitiveMapping));
                // END SJSWS 6324431
            }
            /* SJSWS 6324431
            if (log.isDebugEnabled() && constraints[i].included(
                    uri, method)) {
                log.debug("  Matched constraint '" + constraints[i] +
                    "' against " + method + " " + uri);
            }
            */
            // START SJSWS 6324431
            if (log.isLoggable(Level.FINE)
                    && constraint.included(uri, method,
                                           caseSensitiveMapping)) {
                log.fine("  Matched constraint '" + constraint +
                         "' against " + method + " " + origUri);
            }
            // END SJSWS 6324431

            for (int j=0; j < collection.length; j++){
                String[] patterns = collection[j].findPatterns();
                // If patterns is null, continue to avoid an NPE
                // See Bugzilla 30624
                if ( patterns == null) {
                    continue;
                }

                for(int k=0; k < patterns.length; k++) {
                    /* SJSWS 6324431
                    if(uri.equals(patterns[k])) {
                    */
                    // START SJSWS 6324431
                    String pattern = caseSensitiveMapping ? patterns[k] :
                        patterns[k].toLowerCase();
                    if (uri != null && uri.equals(pattern)) {
                    // END SJSWS 6324431
                        found = true;
                        if(collection[j].findMethod(method)) {
                            if(results == null) {
                                results = new ArrayList();
                            }
                            results.add(constraint);
                        }
                    }
                }
            }
        } // while

        if (found) {
            return resultsToArray(results);
        }

        int longest = -1;

        i = constraints.iterator(); 
        while (i.hasNext()) {
            SecurityConstraint constraint = i.next();
            SecurityCollection [] collection =
                constraint.findCollections();
            
            // If collection is null, continue to avoid an NPE
            // See Bugzilla 30624
            if ( collection == null) {
                continue;
            }

            if (log.isLoggable(Level.FINEST)) {
                /* SJSWS 6324431
                log.trace("  Checking constraint '" + constraints[i] +
                    "' against " + method + " " + uri + " --> " +
                    constraints[i].included(uri, method));
                */
                // START SJSWS 6324431
                log.finest("  Checking constraint '" + constraint +
                           "' against " + method + " " + origUri +
                           " --> " +
                           constraint.included(uri, method,
                                               caseSensitiveMapping));
                // END SJSWS 6324431
            }
            /* SJSWS 6324431
            if (log.isDebugEnabled() && constraints[i].included(
                    uri, method)) {
                log.debug("  Matched constraint '" + constraints[i] +
                    "' against " + method + " " + uri);
            }
            */
            // START SJSWS 6324431
            if (log.isLoggable(Level.FINE) &&
                    constraint.included(uri, method,
                                        caseSensitiveMapping)) {
                log.fine("  Matched constraint '" + constraint +
                         "' against " + method + " " + origUri);
            }
            // END SJSWS 6324431

            for (int j=0; j < collection.length; j++){
                String[] patterns = collection[j].findPatterns();
                // If patterns is null, continue to avoid an NPE
                // See Bugzilla 30624
                if (patterns == null) {
                    continue;
                }

                boolean matched = false;
                int length = -1;
                for (int k=0; k < patterns.length; k++) {
                    /* SJSWS 6324431
                    String pattern = patterns[k];
                    */
                    // START SJSWS 6324431
                    String pattern = caseSensitiveMapping ?
                        patterns[k]:patterns[k].toLowerCase();
                    // END SJSWS 6324431
                    if (pattern.startsWith("/") &&
                            pattern.endsWith("/*") && 
                            pattern.length() >= longest) {
                            
                        if (pattern.length() == 2) {
                            matched = true;
                            length = pattern.length();
                        } else if (uri != null
                                && (pattern.regionMatches(
                                        0,uri,0,pattern.length()-1)
                                    || (pattern.length()-2 == uri.length()
                                        && pattern.regionMatches(
                                            0,uri,0,pattern.length()-2)))) {
                            matched = true;
                            length = pattern.length();
                        }
                    }
                }
                if (matched) {
                    found = true;
                    if (length > longest) {
                        if (results != null) {
                            results.clear();
                        }
                        longest = length;
                    }
                    if (collection[j].findMethod(method)) {
                        if (results == null) {
                            results = new ArrayList();
                        }
                        results.add(constraint);
                    }
                }
            }
        } // while

        if (found) {
            return resultsToArray(results);
        }

        i = constraints.iterator(); 
        while (i.hasNext()) {
            SecurityConstraint constraint = i.next();
            SecurityCollection[] collection = constraint.findCollections();

            // If collection is null, continue to avoid an NPE
            // See Bugzilla 30624
            if ( collection == null) {
                continue;
            }
            
            if (log.isLoggable(Level.FINEST)) {
                /* SJSWS 6324431
                log.trace("  Checking constraint '" + constraints[i] +
                    "' against " + method + " " + uri + " --> " +
                    constraints[i].included(uri, method));
                */
                // START SJSWS 6324431
                log.finest("  Checking constraint '" + constraint +
                           "' against " + method + " " + origUri +
                           " --> " +
                           constraint.included(uri, method, 
                                               caseSensitiveMapping));
                // END SJSWS 6324431
            }
            /* SJSWS 6324431
            if (log.isDebugEnabled() && constraints[i].included(
                    uri, method)) {
                log.debug("  Matched constraint '" + constraints[i] +
                    "' against " + method + " " + uri);
            }
            */
            // START SJSWS 6324431
            if (log.isLoggable(Level.FINE) &&
                    constraint.included(uri, method,
                                        caseSensitiveMapping)) {
                log.fine("  Matched constraint '" + constraint +
                         "' against " + method + " " + origUri);
            }
            // END SJSWS 6324431

            boolean matched = false;
            int pos = -1;
            for (int j=0; j < collection.length; j++){
                String [] patterns = collection[j].findPatterns();
                // If patterns is null, continue to avoid an NPE
                // See Bugzilla 30624
                if (patterns == null) {
                    continue;
                }

                for(int k=0; k < patterns.length && !matched; k++) {
                    /* SJSWS 6324431
                    String pattern = patterns[k];
                    */
                    // START SJSWS 6324431
                    String pattern = caseSensitiveMapping ? 
                        patterns[k]:patterns[k].toLowerCase();
                    // END SJSWS 6324431
                    if (uri != null && pattern.startsWith("*.")){
                        int slash = uri.lastIndexOf("/");
                        int dot = uri.lastIndexOf(".");
                        if (slash >= 0 && dot > slash &&
                                dot != uri.length()-1 &&
                                uri.length()-dot == pattern.length()-1) {
                            if (pattern.regionMatches(
                                    1,uri,dot,uri.length()-dot)) {
                                matched = true;
                                pos = j;
                            }
                        }
                    }
                }
            }
    
            if (matched) {
                found = true;
                if (collection[pos].findMethod(method)) {
                    if(results == null) {
                        results = new ArrayList();
                    }
                    results.add(constraint);
                }
            }
        } // while

        if (found) {
            return resultsToArray(results);
        }

        i = constraints.iterator(); 
        while (i.hasNext()) {
            SecurityConstraint constraint = i.next();
            SecurityCollection[] collection = constraint.findCollections();
            
            // If collection is null, continue to avoid an NPE
            // See Bugzilla 30624
            if (collection == null) {
                continue;
            }

            if (log.isLoggable(Level.FINEST)) {
                /* SJSWS 6324431
                log.trace("  Checking constraint '" + constraints[i] +
                    "' against " + method + " " + uri + " --> " +
                    constraints[i].included(uri, method));
                */
                // START SJSWS 6324431
                log.finest("  Checking constraint '" + constraint +
                           "' against " + method + " " + origUri +
                           " --> " +
                           constraint.included(uri, method,
                                               caseSensitiveMapping));
                // END SJSWS 6324431
            }
            /* SJSWS 6324431
            if (log.isDebugEnabled() && constraints[i].included(
                    uri, method)) {
                log.debug("  Matched constraint '" + constraints[i] +
                    "' against " + method + " " + uri);
            }
            */
            // START SJSWS 6324431
            if (log.isLoggable(Level.FINE) &&
                    constraint.included(uri, method,
                                        caseSensitiveMapping)) {
                log.fine("  Matched constraint '" + constraint +
                         "' against " + method + " " + origUri);
            }
            // END SJSWS 6324431

            for (int j=0; j < collection.length; j++){
                String[] patterns = collection[j].findPatterns();

                // If patterns is null, continue to avoid an NPE
                // See Bugzilla 30624
                if (patterns == null) {
                    continue;
                }

                boolean matched = false;
                for (int k=0; k < patterns.length && !matched; k++) {
                    /* SJSWS 6324431
                    String pattern = patterns[k];
                    */
                    // START SJSWS 6324431
                    String pattern = caseSensitiveMapping ? 
                        patterns[k]:patterns[k].toLowerCase();
                    // END SJSWS 6324431
                    if (pattern.equals("/")){
                        matched = true;
                    }
                }
                if (matched) {
                    if (results == null) {
                        results = new ArrayList();
                    }                    
                    results.add(constraint);
                }
            }
        } // while

        if (results == null) {
            // No applicable security constraint was found
            if (log.isLoggable(Level.FINE))
                log.fine("  No applicable constraint located");
        }

        return resultsToArray(results);
    }
 
    /**
     * Convert an ArrayList to a SecurityContraint [].
     */
    private SecurityConstraint [] resultsToArray(ArrayList results) {
        if(results == null) {
            return null;
        }
        SecurityConstraint [] array = new SecurityConstraint[results.size()];
        results.toArray(array);
        return array;
    }

    
    /**
     * Perform access control based on the specified authorization constraint.
     * Return <code>true</code> if this constraint is satisfied and processing
     * should continue, or <code>false</code> otherwise.
     *
     * @param request Request we are processing
     * @param response Response we are creating
     * @param constraints Security constraint we are enforcing
     * @param context The Context to which client of this class is attached.
     *
     * @exception IOException if an input/output error occurs
     */
    public boolean hasResourcePermission(HttpRequest request,
                                         HttpResponse response,
                                         SecurityConstraint []constraints,
                                         Context context)
        throws IOException {

        if (constraints == null || constraints.length == 0)
            return (true);

        // Specifically allow access to the form login and form error pages
        // and the "j_security_check" action
        LoginConfig config = context.getLoginConfig();
        if ((config != null) &&
            (Constants.FORM_METHOD.equals(config.getAuthMethod()))) {
            String requestURI = request.getRequestPathMB().toString();
            String loginPage = config.getLoginPage();
            if (loginPage.equals(requestURI)) {
                if (log.isLoggable(Level.FINE))
                    log.fine(" Allow access to login page " + loginPage);
                return (true);
            }
            String errorPage = config.getErrorPage();
            if (errorPage.equals(requestURI)) {
                if (log.isLoggable(Level.FINE))
                    log.fine(" Allow access to error page " + errorPage);
                return (true);
            }
            if (requestURI.endsWith(Constants.FORM_ACTION)) {
                if (log.isLoggable(Level.FINE))
                    log.fine(" Allow access to username/password submission");
                return (true);
            }
        }

        // Which user principal have we already authenticated?
        Principal principal = ((HttpServletRequest)request.getRequest())
                                                            .getUserPrincipal();
        for(int i=0; i < constraints.length; i++) {
            SecurityConstraint constraint = constraints[i];
            String roles[] = constraint.findAuthRoles();
            if (roles == null)
                roles = new String[0];

            if (constraint.getAllRoles())
                return (true);

            if (log.isLoggable(Level.FINE))
                log.fine("  Checking roles " + principal);

            if (roles.length == 0) {
                if(constraint.getAuthConstraint()) {

                    /* S1AS 4878272
                    ((HttpServletResponse) response.getResponse()).sendError
                        (HttpServletResponse.SC_FORBIDDEN,
                         sm.getString("realmBase.forbidden"));
                    */
                    // BEGIN S1AS 4878272
                    ((HttpServletResponse) response.getResponse()).sendError
                        (HttpServletResponse.SC_FORBIDDEN);
                    response.setDetailMessage(sm.getString("realmBase.forbidden"));
                    // END S1AS 4878272

                    if (log.isLoggable(Level.FINE)) log.fine("No roles ");
                    return (false); // No listed roles means no access at all
                } else {
                    log.fine("Passing all access");
                    return (true);
                }
            } else if (principal == null) {
                if (log.isLoggable(Level.FINE))
                    log.fine("  No user authenticated, cannot grant access");

                /* S1AS 4878272
                ((HttpServletResponse) response.getResponse()).sendError
                    (HttpServletResponse.SC_FORBIDDEN,
                     sm.getString("realmBase.notAuthenticated"));
                */
                // BEGIN S1AS 4878272
                ((HttpServletResponse) response.getResponse()).sendError
                    (HttpServletResponse.SC_FORBIDDEN);
                response.setDetailMessage(sm.getString("realmBase.notAuthenticated"));
                // END S1AS 4878272
                return (false);
            }


            for (int j = 0; j < roles.length; j++) {
                if (hasRole(principal, roles[j])) {
                    if (log.isLoggable(Level.FINE))
                        log.fine("Role found:  " + roles[j]);
                    return (true);
                } else {
                    if (log.isLoggable(Level.FINE))
                        log.fine("No role found:  " + roles[j]);
                }
            }
        }
        // Return a "Forbidden" message denying access to this resource
        /* S1AS 4878272
        ((HttpServletResponse) response.getResponse()).sendError
        */
        // BEGIN S1AS 4878272
        ((HttpServletResponse) response.getResponse()).sendError
            (HttpServletResponse.SC_FORBIDDEN);
        response.setDetailMessage(sm.getString("realmBase.forbidden"));
        // END S1AS 4878272
        return (false);

    }
    
    //START SJSAS 6232464
    /**
     * Return <code>true</code> if the specified Principal has the specified
     * security role, within the context of this Realm; otherwise return
     * <code>false</code>.  This method can be overridden by Realm
     * implementations. The default implementation is to forward to
     * hasRole(Principal principal, String role).
     *
     * @param request Request we are processing
     * @param response Response we are creating
     * @param principal Principal for whom the role is to be checked
     * @param role Security role to be checked
     */
    public boolean hasRole(HttpRequest request, 
                           HttpResponse response, 
                           Principal principal, 
                           String role) {
        return hasRole(principal, role);
    }
    //END SJSAS 6232464
    
    //START SJSAS 6202703
    /**
     * Checks whether or not authentication is needed.
     * Returns an int, one of AUTHENTICATE_NOT_NEEDED, AUTHENTICATE_NEEDED,
     * or AUTHENTICATED_NOT_AUTHORIZED.
     *
     * @param request Request we are processing
     * @param response Response we are creating
     * @param constraints Security constraint we are enforcing
     * @param disableProxyCaching whether or not to disable proxy caching for
     *        protected resources.
     * @param securePagesWithPragma true if we add headers which 
     * are incompatible with downloading office documents in IE under SSL but
     * which fix a caching problem in Mozilla.
     * @param ssoEnabled true if sso is enabled
     * @exception IOException if an input/output error occurs
     */
    public int preAuthenticateCheck(HttpRequest request,
                                    HttpResponse response,
                                    SecurityConstraint[] constraints,
                                    boolean disableProxyCaching,
                                    boolean securePagesWithPragma,
                                    boolean ssoEnabled)
                                    throws IOException {
        for(int i=0; i < constraints.length; i++) {
            if (constraints[i].getAuthConstraint()) {
                disableProxyCaching(request, response, disableProxyCaching, securePagesWithPragma);
                return Realm.AUTHENTICATE_NEEDED;
            }
        }
        return Realm.AUTHENTICATE_NOT_NEEDED;
    }
    
    
    /**
     * Authenticates the user making this request, based on the specified
     * login configuration.  Return <code>true</code> if any specified
     * requirements have been satisfied, or <code>false</code> if we have
     * created a response challenge already.
     *
     * @param request Request we are processing
     * @param response Response we are creating
     * @param context The Context to which client of this class is attached.
     * @param authentication the current authenticator.
     * @exception IOException if an input/output error occurs
     */
    public boolean invokeAuthenticateDelegate(HttpRequest request,
                                              HttpResponse response,
                                              Context context,
                                              Authenticator authenticator,
                                              boolean calledFromAuthenticate)
          throws IOException {
        LoginConfig config = context.getLoginConfig();
        return ((AuthenticatorBase) authenticator).authenticate(
                        request, response, config);
    }
    
    /**
     * Post authentication for given request and response.
     *
     * @param request Request we are processing
     * @param response Response we are creating
     * @param context The Context to which client of this class is attached.
     * @exception IOException if an input/output error occurs
     */
    public boolean invokePostAuthenticateDelegate(HttpRequest request,
                                              HttpResponse response,
                                              Context context)
          throws IOException {
         return true;
    }
    
    //END SJSAS 6202703

    /**
     * Return <code>true</code> if the specified Principal has the specified
     * security role, within the context of this Realm; otherwise return
     * <code>false</code>.  This method can be overridden by Realm
     * implementations, but the default is adequate when an instance of
     * <code>GenericPrincipal</code> is used to represent authenticated
     * Principals from this Realm.
     *
     * @param principal Principal for whom the role is to be checked
     * @param role Security role to be checked
     */
    public boolean hasRole(Principal principal, String role) {

        // Should be overriten in JAASRealm - to avoid pretty inefficient conversions
        if ((principal == null) || (role == null) ||
            !(principal instanceof GenericPrincipal))
            return (false);

        GenericPrincipal gp = (GenericPrincipal) principal;
        if (!(gp.getRealm() == this)) {
            log.fine("Different realm " + this + " " + gp.getRealm());//    return (false);
        }
        boolean result = gp.hasRole(role);
        if (log.isLoggable(Level.FINE)) {
            String name = principal.getName();
            if (result)
                log.fine(sm.getString("realmBase.hasRoleSuccess", name, role));
            else
                log.fine(sm.getString("realmBase.hasRoleFailure", name, role));
        }
        return (result);

    }

    
    /**
     * Enforce any user data constraint required by the security constraint
     * guarding this request URI.  Return <code>true</code> if this constraint
     * was not violated and processing should continue, or <code>false</code>
     * if we have created a response already.
     *
     * @param request Request we are processing
     * @param response Response we are creating
     * @param constraints Security constraint being checked
     *
     * @exception IOException if an input/output error occurs
     */
    public boolean hasUserDataPermission(HttpRequest request,
                                         HttpResponse response,
                                         SecurityConstraint[] constraints)
        throws IOException {

        // Is there a relevant user data constraint?
        if (constraints == null || constraints.length == 0) {
            if (log.isLoggable(Level.FINE))
                log.fine("  No applicable security constraint defined");
            return (true);
        }
        for(int i=0; i < constraints.length; i++) {
            SecurityConstraint constraint = constraints[i];
            String userConstraint = constraint.getUserConstraint();
            if (userConstraint == null) {
                if (log.isLoggable(Level.FINE))
                    log.fine("  No applicable user data constraint defined");
                return (true);
            }
            if (userConstraint.equals(Constants.NONE_TRANSPORT)) {
                if (log.isLoggable(Level.FINE))
                    log.fine("  User data constraint has no restrictions");
                return (true);
            }

        }
        // Validate the request against the user data constraint
        if (request.getRequest().isSecure()) {
            if (log.isLoggable(Level.FINE))
                log.fine("  User data constraint already satisfied");
            return (true);
        }
        // Initialize variables we need to determine the appropriate action
        HttpServletRequest hrequest = (HttpServletRequest) request.getRequest();
        HttpServletResponse hresponse = (HttpServletResponse) 
                                                         response.getResponse();
        int redirectPort = request.getConnector().getRedirectPort();

        // Is redirecting disabled?
        if (redirectPort <= 0) {
            if (log.isLoggable(Level.FINE))
                log.fine("  SSL redirect is disabled");
            /* S1AS 4878272
            hresponse.sendError
            response.sendError
                (HttpServletResponse.SC_FORBIDDEN,
                 hrequest.getRequestURI());
            */
            // BEGIN S1AS 4878272
            hresponse.sendError(HttpServletResponse.SC_FORBIDDEN);
            response.setDetailMessage(hrequest.getRequestURI());
            // END S1AS 4878272
            return (false);
        }

        // Redirect to the corresponding SSL port
        StringBuffer file = new StringBuffer();
        String protocol = "https";
        String host = hrequest.getServerName();
        // Protocol
        file.append(protocol).append("://").append(host);
        // Host with port
        if(redirectPort != 443) {
            file.append(":").append(redirectPort);
        }
        // URI
        file.append(hrequest.getRequestURI());
        String requestedSessionId = hrequest.getRequestedSessionId();
        if ((requestedSessionId != null) &&
            hrequest.isRequestedSessionIdFromURL()) {
            file.append(";" + Globals.SESSION_PARAMETER_NAME + "=");
            file.append(requestedSessionId);
        }
        String queryString = hrequest.getQueryString();
        if (queryString != null) {
            file.append('?');
            file.append(queryString);
        }
        if (log.isLoggable(Level.FINE))
            log.fine("Redirecting to " + file.toString());
        hresponse.sendRedirect(file.toString());
        return (false);

    }
    
    
    /**
     * Remove a property change listener from this component.
     *
     * @param listener The listener to remove
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {

        support.removePropertyChangeListener(listener);

    }


    // ------------------------------------------------------ Lifecycle Methods


    /**
     * Add a lifecycle event listener to this component.
     *
     * @param listener The listener to add
     */
    public void addLifecycleListener(LifecycleListener listener) {

        lifecycle.addLifecycleListener(listener);

    }


    /**
     * Gets the (possibly empty) list of lifecycle listeners associated
     * with this Realm.
     */
    public List<LifecycleListener> findLifecycleListeners() {
        return lifecycle.findLifecycleListeners();
    }


    /**
     * Remove a lifecycle event listener from this component.
     *
     * @param listener The listener to remove
     */
    public void removeLifecycleListener(LifecycleListener listener) {
        lifecycle.removeLifecycleListener(listener);
    }

    /**
     * Prepare for the beginning of active use of the public methods of this
     * component.  This method should be called before any of the public
     * methods of this component are utilized.  It should also send a
     * LifecycleEvent of type START_EVENT to any registered listeners.
     *
     * @exception LifecycleException if this component detects a fatal error
     *  that prevents this component from being used
     */
    public void start() throws LifecycleException {

        // Validate and update our current component state
        if (started) {
            log.info(sm.getString("realmBase.alreadyStarted"));
            return;
        }
        if( !initialized ) {
            init();
        }
        lifecycle.fireLifecycleEvent(START_EVENT, null);
        started = true;

        // Create a MessageDigest instance for credentials, if desired
        if (digest != null) {
            try {
                md = MessageDigest.getInstance(digest);
            } catch (NoSuchAlgorithmException e) {
                throw new LifecycleException
                    (sm.getString("realmBase.algorithm", digest), e);
            }
        }

    }


    /**
     * Gracefully terminate the active use of the public methods of this
     * component.  This method should be the last one called on a given
     * instance of this component.  It should also send a LifecycleEvent
     * of type STOP_EVENT to any registered listeners.
     *
     * @exception LifecycleException if this component detects a fatal error
     *  that needs to be reported
     */
    public void stop()
        throws LifecycleException {

        // Validate and update our current component state
        if (!started) {
            log.info(sm.getString("realmBase.notStarted"));
            return;
        }
        lifecycle.fireLifecycleEvent(STOP_EVENT, null);
        started = false;

        // Clean up allocated resources
        md = null;
        
        destroy();
    
    }
    
    public void destroy() {
    
        // unregister this realm
        if ( oname!=null ) {   
            try {   
                Registry.getRegistry(null, null).unregisterComponent(oname); 
                log.fine( "unregistering realm " + oname );   
            } catch( Exception ex ) {   
                log.log(Level.SEVERE, "Can't unregister realm " + oname, ex);
            }      
        }
          
    }

    // ------------------------------------------------------ Protected Methods


    /**
     * Digest the password using the specified algorithm and
     * convert the result to a corresponding hexadecimal string.
     * If exception, the plain credentials string is returned.
     *
     * @param credentials Password or other credentials to use in
     *  authenticating this username
     */
    protected String digest(String credentials)  {

        // If no MessageDigest instance is specified, return unchanged
        if (hasMessageDigest() == false)
            return (credentials);

        // Digest the user credentials and return as hexadecimal
        synchronized (this) {
            try {
                md.reset();
    
                byte[] bytes = null;
                if(getDigestEncoding() == null) {
                    bytes = credentials.getBytes();
                } else {
                    try {
                        bytes = credentials.getBytes(getDigestEncoding());
                    } catch (UnsupportedEncodingException uee) {
                        log.log(Level.SEVERE,
                                "Illegal digestEncoding: " + getDigestEncoding(),
                                uee);
                        throw new IllegalArgumentException(uee.getMessage());
                    }
                }
                md.update(bytes);

                return (HexUtils.convert(md.digest()));
            } catch (Exception e) {
                log.log(Level.SEVERE, sm.getString("realmBase.digest"), e);
                return (credentials);
            }
        }

    }

    protected boolean hasMessageDigest() {
        return !(md == null);
    }

    /**
     * Return the digest associated with given principal's user name.
     */
    protected String getDigest(String username, String realmName) {
        if (md5Helper == null) {
            try {
                md5Helper = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                log.log(Level.SEVERE, "Couldn't get MD5 digest: ", e);
                throw new IllegalStateException(e.getMessage());
            }
        }

    	if (hasMessageDigest()) {
    		// Use pre-generated digest
    		return getPassword(username);
    	}
    	
        String digestValue = username + ":" + realmName + ":"
            + getPassword(username);

        byte[] valueBytes = null;
        if(getDigestEncoding() == null) {
            valueBytes = digestValue.getBytes();
        } else {
            try {
                valueBytes = digestValue.getBytes(getDigestEncoding());
            } catch (UnsupportedEncodingException uee) {
                log.log(Level.SEVERE,
                        "Illegal digestEncoding: " + getDigestEncoding(),
                        uee);
                throw new IllegalArgumentException(uee.getMessage());
            }
        }

        byte[] digest = null;
        // Bugzilla 32137
        synchronized(md5Helper) {
            digest = md5Helper.digest(valueBytes);
        }

        return md5Encoder.encode(digest);
    }


    /**
     * Return a short name for this Realm implementation, for use in
     * log messages.
     */
    protected abstract String getName();


    /**
     * Return the password associated with the given principal's user name.
     */
    protected abstract String getPassword(String username);


    /**
     * Return the Principal associated with the given user name.
     */
    protected abstract Principal getPrincipal(String username);


    /**
     * Log a message on the Logger associated with our Container (if any)
     *
     * @param message Message to be logged
     */
    protected void log(String message) {
        org.apache.catalina.Logger logger = null;
        String name = null;
        if (container != null) {
            logger = container.getLogger();
            name = container.getName();
        }
        if (logger != null) {
            logger.log(getName()+"[" + name + "]: " + message);
        } else {
            log.info(getName()+"[" + name + "]: " + message);
        }
    }


    /**
     * Log a message on the Logger associated with our Container (if any)
     *
     * @param message Message to be logged
     * @param t Associated exception
     */
    protected void log(String message, Throwable t) {
        org.apache.catalina.Logger logger = null;
        String name = null;
        if (container != null) {
            logger = container.getLogger();
            name = container.getName();
        }
        if (logger != null) {
            logger.log(getName()+"[" + name + "]: " + message, t,
                org.apache.catalina.Logger.WARNING);
        } else {
            log.log(Level.WARNING, getName()+"[" + name + "]: " + message, t);
        }
    }
    
    //START SJSAS 6202703
    protected void disableProxyCaching(HttpRequest request,
                                       HttpResponse response, 
                                       boolean disableProxyCaching,
                                       boolean securePagesWithPragma) {
        HttpServletRequest hsrequest = (HttpServletRequest) request.getRequest();
        // Make sure that constrained resources are not cached by web proxies
        // or browsers as caching can provide a security hole
        if (disableProxyCaching
                && !"POST".equalsIgnoreCase(hsrequest.getMethod())
                && (!checkIfRequestIsSecure || !hsrequest.isSecure())) {
            HttpServletResponse sresponse =
                    (HttpServletResponse) response.getResponse();
            if (securePagesWithPragma) {
                // FIXME: These cause problems with downloading office docs
                // from IE under SSL and may not be needed for newer Mozilla
                // clients.
                sresponse.setHeader("Pragma", "No-cache");
                sresponse.setHeader("Cache-Control", "no-cache");
            } else {
                sresponse.setHeader("Cache-Control", "private");
            }
            sresponse.setHeader("Expires", DATE_ONE);
        }
    }
    //END SJSAS 6202703


    // -------------------- JMX and Registration  --------------------

    protected String type;
    protected String domain;
    protected String host;
    protected String path;
    protected ObjectName oname;
    protected ObjectName controller;
    protected MBeanServer mserver;

    public ObjectName getController() {
        return controller;
    }

    public void setController(ObjectName controller) {
        this.controller = controller;
    }

    public ObjectName getObjectName() {
        return oname;
    }

    public String getDomain() {
        return domain;
    }

    public String getType() {
        return type;
    }

    public ObjectName preRegister(MBeanServer server,
                                  ObjectName name) throws Exception {
        oname=name;
        mserver=server;
        domain=name.getDomain();

        type=name.getKeyProperty("type");
        host=name.getKeyProperty("host");
        path=name.getKeyProperty("path");

        return name;
    }

    public void postRegister(Boolean registrationDone) {
    }

    public void preDeregister() throws Exception {
    }

    public void postDeregister() {
    }

    protected boolean initialized=false;
    
    public void init() {
        if( initialized && container != null ) return;
        
        initialized=true;
        if( container== null ) {
            ObjectName parent=null;
            // Register with the parent
            try {
                if( host == null ) {
                    // global
                    parent=new ObjectName(domain +":type=Engine");
                } else if( path==null ) {
                    parent=new ObjectName(domain +
                            ":type=Host,host=" + host);
                } else {
                    parent=new ObjectName(domain +":j2eeType=WebModule,name=//" +
                            host + path);
                }
                if( mserver.isRegistered(parent ))  {
                    log.fine("Register with " + parent);
                    mserver.setAttribute(parent, new Attribute("realm", this));
                }
            } catch (Exception e) {
                log.info("Parent not available yet: " + parent);  
            }
        }
        
        if( oname==null ) {
            // register
            try {
                ContainerBase cb=(ContainerBase)container;
                oname=new ObjectName(cb.getDomain()+":type=Realm" + cb.getContainerSuffix());
                Registry.getRegistry(null, null).registerComponent(this, oname, null );
                log.fine("Register Realm "+oname);
            } catch (Throwable e) {
                log.log(Level.SEVERE,  "Can't register " + oname, e);
            }
        }

    }


    // BEGIN IASRI 4808401, 4934562
    /**
     * Return an alternate principal from the request if available.
     * Tomcat realms do not implement this so always return null as default.
     *
     * @param req The request object.
     * @return Alternate principal or null.
     */
    public Principal getAlternatePrincipal(HttpRequest req) {
        return null;
    }

        
    /**
     * Return an alternate auth type from the request if available.
     * Tomcat realms do not implement this so always return null as default.
     *
     * @param req The request object.
     * @return Alternate auth type or null.
     */
    public String getAlternateAuthType(HttpRequest req) {
        return null;
    }
    // END IASRI 4808401


    // BEGIN IASRI 4856062,4918627,4874504
    /**
     * Set the name of the associated realm.
     *
     * @param name the name of the realm.
     */
    public void setRealmName(String name, String authMethod) {
        // DO NOTHING. PRIVATE EXTENSION
    }


    /**
     * Returns the name of the associated realm.
     *
     * @return realm name or null if not set.
     */
    public String getRealmName(){
        // DO NOTHING. PRIVATE EXTENSION
        return null;
    }
    // END IASRI 4856062,4918627,4874504
    public Principal authenticate(HttpServletRequest hreq) {
        throw new UnsupportedOperationException();
    }

}
