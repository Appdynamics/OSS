/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2009-2011 Oracle and/or its affiliates. All rights reserved.
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
package org.glassfish.admin.rest;

import com.sun.enterprise.admin.util.AuthenticationInfo;
import com.sun.enterprise.admin.util.HttpConnectorAddress;
import com.sun.enterprise.config.serverbeans.Domain;
import com.sun.enterprise.config.serverbeans.SecureAdmin;
import com.sun.enterprise.config.serverbeans.SecureAdminInternalUser;
import com.sun.enterprise.security.ssl.SSLUtils;
import com.sun.enterprise.util.LocalStringManagerImpl;
import com.sun.enterprise.v3.common.ActionReporter;
import com.sun.jersey.api.client.Client;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.sun.jersey.client.urlconnection.HttpURLConnectionFactory;
import com.sun.jersey.client.urlconnection.URLConnectionClientHandler;
import org.glassfish.admin.rest.provider.ProviderUtil;
import javax.ws.rs.core.UriInfo;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.PathSegment;
import org.glassfish.admin.rest.utils.xml.RestActionReporter;
import org.glassfish.api.ActionReport.MessagePart;
import org.glassfish.api.admin.ParameterMap;
import org.glassfish.security.common.MasterPassword;
import org.jvnet.hk2.component.Habitat;

/**
 * Utilities class. Extended by ResourceUtil and ProviderUtil utilities. Used by
 * resource and providers.
 *
 * @author Rajeshwar Patil
 */
public class Util {
    public final static LocalStringManagerImpl localStrings = new LocalStringManagerImpl(Util.class);
    private static Client client;

    private Util() {
    }

    /**
     * Returns name of the resource from UriInfo.
     */
    public static String getResourceName(UriInfo uriInfo) {
        return upperCaseFirstLetter(eleminateHypen(getName(uriInfo.getPath(), '/')));
    }

    /**
     * Returns name of the resource parent from UriInfo.
     */
    public static String getParentName(UriInfo uriInfo) {
        if (uriInfo == null) {
            return null;
        }
        return getParentName(uriInfo.getPath());
    }

    public static String getGrandparentName(UriInfo uriInfo) {
        if (uriInfo == null) {
            return null;
        }
        return getGrandparentName(uriInfo.getPath());
    }

    /**
     * Returns just the name of the given fully qualified name.
     */
    public static String getName(String typeName) {
        return getName(typeName, '.');
    }

    /**
     * Returns just the name of the given fully qualified name.
     */
    public static String getName(String typeName, char delimiter) {
        if ((typeName == null) || ("".equals(typeName))) {
            return typeName;
        }

        //elimiate last char from typeName if its a delimiter
        if (typeName.length() - 1 == typeName.lastIndexOf(delimiter)) {
            typeName = typeName.substring(0, typeName.length() - 1);
        }

        if ((typeName != null) && (typeName.length() > 0)) {
            int index = typeName.lastIndexOf(delimiter);
            if (index != -1) {
                return typeName.substring(index + 1);
            }
        }
        return typeName;
    }

    /**
     * returns just the parent name of the resource from the resource url.
     */
    public static String getParentName(String url) {
        if ((url == null) || ("".equals(url))) {
            return url;
        }
        String name = getName(url, '/');
        // Find the : to skip past the protocal part of the URL, as that is causing
        // problems with resources named 'http'.
        int nameIndex = url.indexOf(name, url.indexOf(":") + 1);
        return getName(url.substring(0, nameIndex - 1), '/');
    }

    public static String getGrandparentName(String url) {
        if ((url == null) || ("".equals(url))) {
            return url;
        }
        String name = getParentName(url);
        // Find the : to skip past the protocal part of the URL, as that is causing
        // problems with resources named 'http'.
        int nameIndex = url.indexOf(name, url.indexOf(":") + 1);
        return getName(url.substring(0, nameIndex - 1), '/');
    }

    /**
     * Removes any hypens ( - ) from the given string.
     * When it removes a hypen, it converts next immediate
     * character, if any,  to an Uppercase.(schema2beans convention)
     * @param string the input string
     * @return a <code>String</code> resulted after removing the hypens
     */
    public static String eleminateHypen(String string) {
        if (!(string == null || string.length() <= 0)) {
            int index = string.indexOf('-');
            while (index != -1) {
                if (index == 0) {
                    string = string.substring(1);
                } else {
                    if (index == (string.length() - 1)) {
                        string = string.substring(0, string.length() - 1);
                    } else {
                        string = string.substring(0, index)
                                + upperCaseFirstLetter(string.substring(index + 1));
                    }
                }
                index = string.indexOf('-');
            }
        }
        return string;
    }

    public static String decode(String string) {
        String ret = string;

        try {
            ret = URLDecoder.decode(string, "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }

        return ret;
    }

    /**
     * Converts the first letter of the given string to Uppercase.
     *
     * @param string the input string
     * @return the string with the Uppercase first letter
     */
    public static String upperCaseFirstLetter(String string) {
        if (string == null || string.length() <= 0) {
            return string;
        }
        return string.substring(0, 1).toUpperCase(Locale.US) + string.substring(1);
    }

    /**
     * Returns the html for the given message.
     *
     * @param uriInfo the uriInfo context of the request
     * @return String the html representation of the given message
     */
    protected static String getHtml(String message, UriInfo uriInfo, boolean delete) {
        String result = ProviderUtil.getHtmlHeader(uriInfo.getBaseUri().toASCIIString());
        String uri = uriInfo.getAbsolutePath().toString();
        if (delete) {
            uri = uri + "/..";
        }
        String name = upperCaseFirstLetter(eleminateHypen(getName(uri, '/')));
        String parentName =
                upperCaseFirstLetter(eleminateHypen(getParentName(uri)));

        result = result + "<h1>" + name + "</h1>";
        result = result + message;//+ "<br><br>";
        result = result + "<a href=\"" + uri + "\">Back</a>";

        //  result =  result +  "<br>";
        result = result + "</body></html>";
        return result;
    }

    /**
     * Constructs a method name from  element's dtd name
     * name for a given prefix.(schema2beans convention)
     *
     * @param elementName the given element name
     * @param prefix the given prefix
     * @return a method name formed from the given name and the prefix
     */
    public static String methodNameFromDtdName(String elementName, String prefix) {
        return methodNameFromBeanName(eleminateHypen(elementName), prefix);
    }

    /**
     * Constructs a method name from  element's bean
     * name for a given prefix.(schema2beans convention)
     *
     * @param elementName the given element name
     * @param prefix the given prefix
     * @return a method name formed from the given name and the prefix
     */
    public static String methodNameFromBeanName(String elementName, String prefix) {
        if ((null == elementName) || (null == prefix)
                || (prefix.length() <= 0)) {
            return elementName;
        }
        String methodName = upperCaseFirstLetter(elementName);
        return methodName = prefix + methodName;
    }

    public static Client getJerseyClient(Habitat habitat) {
        if (client == null) {
            // Tell Jersey to use HttpURLConnectionFactory that delegates to HttpConnectorAddress
            // URLConnection obtained form HttpConnectorAddress is set up for authentication required for secure admin communication
            HttpURLConnectionFactory urlConnectionFactory = new ConnectionFactory(habitat);
            client = new Client(new URLConnectionClientHandler(urlConnectionFactory));
        }

        return client;
    }


    /**
     * Apply changes passed in <code>data</code> using CLI "set".
     * @param data The set of changes to be applied
     * @return ActionReporter containing result of "set" execution
     */
    public static ActionReporter applyChanges(Map<String, String> data, UriInfo uriInfo, Habitat habitat) {
        List<PathSegment> pathSegments = uriInfo.getPathSegments();

        // Discard the last segment if it is empty. This happens if some one accesses the resource
        // with trailing '/' at end like in htto://host:port/mangement/domain/.../pathelement/
        PathSegment lastSegment = pathSegments.get(pathSegments.size() - 1);
        if(lastSegment.getPath().isEmpty()) {
            pathSegments = pathSegments.subList(0, pathSegments.size() - 1);
        }

        List<PathSegment> candidatePathSegment = null;
        if(pathSegments.size() != 1) {
            // Discard "domain"
            candidatePathSegment = pathSegments.subList(1, pathSegments.size());
        } else {
            // We are being called for a config change at domain level.
            // CLI "set" requires name to be of form domain.<attribute-name>.
            // Preserve "domain"
            candidatePathSegment = pathSegments;
        }

        final StringBuilder sb = new StringBuilder();
        for(PathSegment pathSegment :  candidatePathSegment) {
            sb.append(pathSegment.getPath());
            sb.append('.');
        }
        String setBasePath = sb.toString();
        ParameterMap parameters = new ParameterMap();
        Map<String, String> currentValues = getCurrentValues(setBasePath, habitat);

        for (Map.Entry<String, String> entry : data.entrySet()) {
            String currentValue = currentValues.get(setBasePath + entry.getKey());
            if ((currentValue == null) || entry.getValue().equals("") || (!currentValue.equals(entry.getValue()))) {
                parameters.add("DEFAULT", setBasePath + entry.getKey() + "=" + entry.getValue());
            }
        }
        if (!parameters.entrySet().isEmpty()) {
           return ResourceUtil.runCommand("set", parameters, habitat, ""); //TODO The last parameter is resultType and is not used. Refactor the called method to remove it
        } else {
            return new RestActionReporter(); // noop
        }
    }

    private static Map<String, String> getCurrentValues(String basePath, Habitat habitat) {
        Map<String, String> values = new HashMap<String, String>();
        final String path = (basePath.endsWith(".")) ? basePath.substring(0, basePath.length()-1) : basePath;
        RestActionReporter gr = ResourceUtil.runCommand("get", new ParameterMap() {{
            add ("DEFAULT", path);
        }}, habitat, "");

        MessagePart top = gr.getTopMessagePart();
        for (MessagePart child : top.getChildren()) {
            String message = child.getMessage();
            if (message.contains("=")) {
                String[] parts = message.split("=");
                values.put(parts[0], (parts.length > 1) ? parts[1] : "");
            }
        }

        return values;
    }

    public static void deleteFile(String fileName, String mimeType, InputStream fileStream) {
        System.out.println("Deleting file....");
        if (fileName.contains(".")) {
                File f = new File(new File(System.getProperty("java.io.tmpdir"), System.getProperty("user.name")), fileName);
              f.delete();
        }
    }

    public static File saveFile(String fileName, String mimeType, InputStream fileStream) {
        BufferedOutputStream out = null;
        File f = null;
        try {
            if (fileName.contains(".")) {
                File tmpFile = new File(System.getProperty("java.io.tmpdir"), System.getProperty("user.name"));
                if(!tmpFile.exists()) {
                    tmpFile.mkdir();
                }
                f = new File(tmpFile, fileName);
            }

            out = new BufferedOutputStream(new FileOutputStream(f));
            byte[] buffer = new byte[32 * 1024];
            int bytesRead = 0;
            while ((bytesRead = fileStream.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            return f;
        } catch (IOException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    /**
     * HttpURLConnectionFactory for obtaining connection from a Jersey Client.
     */
    private static class ConnectionFactory implements HttpURLConnectionFactory {
        Habitat habitat;

        private ConnectionFactory(Habitat habitat) {
            this.habitat = habitat;
        }

        /**
         * Code in this method is copied from ServerRemoteAdminCommand to make it easier for sustaining to port back
         * these changes. In trunk, HttpConnectorAddress will be refactored such that all the code below will be
         * abstracted into HttpConnectorAddress.
         */
        @Override
        public HttpURLConnection getHttpURLConnection(URL url) throws IOException {
            final Domain domain = habitat.getComponent(Domain.class);
            SecureAdmin secureAdmin = domain.getSecureAdmin();
            final String certAlias = SecureAdmin.Util.isUsingUsernamePasswordAuth(secureAdmin) ? null : SecureAdmin.Util.DASAlias(secureAdmin);
            HttpConnectorAddress httpConnectorAddress = new HttpConnectorAddress(habitat.getComponent(SSLUtils.class).getAdminSocketFactory(certAlias, "TLS"));
            AuthenticationInfo authenticationInfo = authenticationInfo(habitat, secureAdmin);
            if (authenticationInfo != null) {
                httpConnectorAddress.setAuthenticationInfo(authenticationInfo);
            }
            HttpURLConnection httpURLConnection = (HttpURLConnection) httpConnectorAddress.openConnection(url); // The URL constructed for REST will always be Http(s) so, it is ok to cast here.
            //TODO following code is copied from ServerRemoteAdminCommand.addAdditionalHeaders. When the clean up happens on trunk. this code needs to be accounted for.
            // Here are comments copied from javadoc of the method
            //* Adds the admin indicator header to the request so. Do this whether
            //* secure admin is enabled or not, because the indicator is unique among
            //* domains to help make sure only processes in the same domain talk to
            //* each other.
            final String indicatorValue = SecureAdmin.Util.configuredAdminIndicator(secureAdmin);
            if(indicatorValue != null) {
                httpURLConnection.setRequestProperty(SecureAdmin.Util.ADMIN_INDICATOR_HEADER_NAME, indicatorValue);
            }
            //Hack - httpConnectorAddress calls httpURLConnection.setRequestProperty("Content-type", "application/octet-stream"); before returning connection above
            //Our resources are only capable of accepting xml, json and form. Override here to apploication/json (randomly picked one of the three). This should not be required after HttpConnectorAddress refactoring in trunk.
            httpURLConnection.setRequestProperty("Content-type", "application/json");
            return httpURLConnection;
        }

        /**
         * Code in this method is copied from ServerRemoteAdminCommand to make it easier for sustaining to port back
         * these changes. In trunk, HttpConnectorAddress will be refactored such that all the code below will be
         * abstracted into HttpConnectorAddress.
         */
        private AuthenticationInfo authenticationInfo(Habitat habitat, SecureAdmin secureAdmin) {
            AuthenticationInfo result = null;
            if (SecureAdmin.Util.isUsingUsernamePasswordAuth(secureAdmin)) {
                final SecureAdminInternalUser secureAdminInternalUser = SecureAdmin.Util.secureAdminInternalUser(secureAdmin);
                if (secureAdminInternalUser != null) {
                    try {
                        String pw = habitat.getComponent(MasterPassword.class).getMasterPasswordAdapter().getPasswordForAlias(secureAdminInternalUser.getPasswordAlias());
                        result = new AuthenticationInfo(secureAdminInternalUser.getUsername(), pw);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
            return result;
        }
    }
}
