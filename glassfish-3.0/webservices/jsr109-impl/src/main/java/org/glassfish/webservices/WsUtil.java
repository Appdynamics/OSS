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
package org.glassfish.webservices;

import com.sun.enterprise.v3.admin.AdminAdapter;
import com.sun.enterprise.v3.admin.adapter.AdminConsoleAdapter;
import com.sun.grizzly.config.dom.NetworkListener;
import com.sun.xml.ws.api.server.SDDocumentSource;
import com.sun.xml.ws.api.WSBinding;
import com.sun.xml.ws.api.BindingID;
import com.sun.logging.LogDomains;
import com.sun.enterprise.deployment.*;
import com.sun.enterprise.deployment.util.ModuleDescriptor;
import com.sun.enterprise.deployment.util.WebServerInfo;
import com.sun.enterprise.deployment.util.VirtualServerInfo;
import com.sun.enterprise.deployment.web.UserDataConstraint;
import com.sun.enterprise.deployment.web.SecurityConstraint;
import com.sun.enterprise.container.common.spi.util.InjectionException;
import com.sun.enterprise.container.common.spi.util.InjectionManager;
import com.sun.enterprise.config.serverbeans.Config;
import com.sun.enterprise.util.LocalStringManagerImpl;
import com.sun.xml.rpc.spi.model.*;
import javax.servlet.http.*;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.Stub;
import javax.xml.rpc.ServiceFactory;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.rpc.soap.SOAPFaultException;
import javax.xml.rpc.handler.HandlerInfo;
import javax.xml.rpc.handler.HandlerRegistry;
import com.sun.xml.rpc.spi.JaxRpcObjectFactory;
import com.sun.xml.rpc.spi.model.Model;
import com.sun.xml.rpc.spi.model.ModelProperties;
import com.sun.xml.rpc.spi.model.Port;
import com.sun.xml.rpc.spi.model.Service;
import com.sun.xml.rpc.spi.runtime.ClientTransportFactory;
import com.sun.xml.rpc.spi.runtime.ClientTransportFactoryTypes;
import com.sun.xml.rpc.spi.runtime.SOAPConstants;
import com.sun.xml.rpc.spi.runtime.StreamingHandler;
import com.sun.xml.rpc.spi.runtime.StubBase;
import com.sun.xml.rpc.spi.runtime.Tie;
import com.sun.xml.rpc.spi.tools.WSDLUtil;


import java.util.*;
import java.net.*;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.xml.parsers.*;
import javax.xml.ws.soap.SOAPBinding;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.http.HTTPBinding;
import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;
import javax.servlet.http.HttpServletResponse;

import org.jvnet.hk2.component.Habitat;
import org.w3c.dom.*;
import org.w3c.dom.Node;

import java.io.*;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.security.PrivilegedActionException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.glassfish.api.admin.ServerEnvironment;
import org.glassfish.deployment.common.DeploymentException;


/**
 * Web service related utilities.
 *
 * @author Kenneth Saks
 */
public class WsUtil {

    // proprietary property for enabling logging of stub requests/responses
    public static final String CLIENT_TRANSPORT_LOG_PROPERTY =
      "com.sun.enterprise.webservice.client.transport.log";

    // xslt processing parameters for final wsdl transformation
    public static final String ENDPOINT_ADDRESS_PARAM_NAME =
        "endpointAddressParam";

    public final String WSDL_IMPORT_NAMESPACE_PARAM_NAME = 
        "wsdlImportNamespaceParam";
    public static final String WSDL_IMPORT_LOCATION_PARAM_NAME = 
        "wsdlImportLocationParam";
    public static final String WSDL_INCLUDE_LOCATION_PARAM_NAME = 
        "wsdlIncludeLocationParam";

    public final String SCHEMA_IMPORT_NAMESPACE_PARAM_NAME = 
        "schemaImportNamespaceParam";
    public static final String SCHEMA_IMPORT_LOCATION_PARAM_NAME = 
        "schemaImportLocationParam";    
    public static final String SCHEMA_INCLUDE_LOCATION_PARAM_NAME = 
        "schemaIncludeLocationParam";    

    private Config config = null;
    private Habitat habitat = null;

    private List<NetworkListener> networkListeners = null;

    public WsUtil() {
        config = WebServiceContractImpl.getInstance().getConfig();
        habitat = WebServiceContractImpl.getInstance().getHabitat();
    }

    // @@@ These are jaxrpc-implementation specific MessageContextProperties
    // that should be added to jaxrpc spi
    private static final String ONE_WAY_OPERATION =
        "com.sun.xml.rpc.server.OneWayOperation";
    private static final String CLIENT_BAD_REQUEST =
        "com.sun.xml.rpc.server.http.ClientBadRequest";
    
    private static final String SECURITY_POLICY_NAMESPACE_URI = 
            "http://schemas.xmlsoap.org/ws/2005/07/securitypolicy";

    private static Logger logger = LogDomains.getLogger(WsUtil.class,LogDomains.WEBSERVICES_LOGGER);
    private ResourceBundle rb = logger.getResourceBundle();

    private JaxRpcObjectFactory rpcFactory;

    /*public WsUtil() {
       // rpcFactory = JaxRpcObjectFactory.newInstance();
    }

    *//**
     * Notification of server startup, good place for services initialization
     *//*
    public static void start() {

        // force monitoring initialization
        WebServiceEngineImpl.getInstance();
        ApplicationLoaderEventListener listener = ServiceEngineUtil.getListener();
        ApplicationLoaderEventNotifier.getInstance().addListener(listener);
    }
    *//**
     * Serve up the FINAL wsdl associated with this web service.
     * @return true for success, false for failure
     */
    public boolean handleGet(HttpServletRequest request, 
                             HttpServletResponse response, 
                             WebServiceEndpoint endpoint) throws IOException {

        MimeHeaders headers = getHeaders(request);
        if( hasSomeTextXmlContent(headers) ) {
            String message = "Received HTTP GET containing text/xml " +
                " content for endpoint " + endpoint.getEndpointName() + 
                " at " + endpoint.getEndpointAddressUri() +
                " . HTTP POST should be used instead."; 
                        

            writeInvalidMethodType(response, message);
            
            logger.info(message);
            
            return false;
        }
        
        URL wsdlUrl = null;

        String requestUriRaw = request.getRequestURI();
        String requestUri    = (requestUriRaw.charAt(0) == '/') ?
            requestUriRaw.substring(1) : requestUriRaw;
        String queryString = request.getQueryString();

        WebService webService = endpoint.getWebService();

        if( queryString == null ) {

            // Get portion of request uri representing location within a module
            String wsdlPath = endpoint.getWsdlContentPath(requestUri);

            if( wsdlPath != null) {
                ModuleDescriptor module = 
                    webService.getBundleDescriptor().getModuleDescriptor();

                if( wsdlPath.equals(webService.getWsdlFileUri())){
                    // If the request is for the main wsdl document, return
                    // the final wsdl instead of the wsdl from the module.
                    wsdlUrl = webService.getWsdlFileUrl();
                } else if( isWsdlContent(wsdlPath, 
                                         webService.getBundleDescriptor()) ) {
                    // For relative document imports. These documents do not
                    // require modification during deployment, so serve them
                    // up directly from the packaged module.  isWsdlContent()
                    // check ensures we don't serve up arbitrary content from
                    // the module.
                    URL finalWsdlUrl = webService.getWsdlFileUrl();
                    String finalWsdlPath = finalWsdlUrl.getPath();
                    // remove the final wsdl uri from the above path
                    String wsdlDirPath = finalWsdlPath.substring(0, finalWsdlPath.length()-webService.getWsdlFileUri().length());
                    File wsdlDir = new File(wsdlDirPath);
                    File wsdlFile = new File(wsdlDir, wsdlPath.replace('/', File.separatorChar));
                    try {
                        wsdlUrl = wsdlFile.toURL();
                    } catch(MalformedURLException mue) {
                        logger.log(Level.INFO, "Failure serving WSDL for " +
                                   webService.getName(), mue);
                    } 
                        
                }
            }

        } else if( queryString.equalsIgnoreCase("WSDL") ) {
            wsdlUrl = webService.getWsdlFileUrl();
        }

        boolean success = false;
        if( wsdlUrl != null ) {
            
            InputStream is = null;
            try {
                response.setContentType("text/xml");
                response.setStatus(HttpServletResponse.SC_OK);
                // if the requested file is the main wsdl document, we are going
                // to reprocess it through the XML transformer to change the final
                // endpoint URL correct for this particular web server instance.
                // This is necessary in the case of SE/EE installations where
                // the application can be running on different machines and ports
                // than the one they were deployed on (DAS).
                if (wsdlUrl.toURI().equals(webService.getWsdlFileUrl().toURI())) {
                    // get the application module ID
                    try {
                        String moduleID = endpoint.getBundleDescriptor().getApplication().getRegistrationName();
                        WebServerInfo wsi = getWebServerInfoForDAS();
                        URL url = webService.getWsdlFileUrl();
                        File originalWsdlFile = new File(url.getPath()+"__orig");
                        if(!originalWsdlFile.exists()) {
                            originalWsdlFile = new File(url.getPath());
                        }
                        generateFinalWsdl(originalWsdlFile.toURL(), webService, wsi, response.getOutputStream());
                    } catch(Exception e) {
                        // if this fail, we revert to service the untouched 
                        // repository item.
                        URLConnection urlCon = wsdlUrl.openConnection();
                        urlCon.setUseCaches(false);
                        is = urlCon.getInputStream();                    
                        copyIsToOs(is, response.getOutputStream());
                    }
                } else {
                    // Copy bytes into output. Disable caches to avoid jar URL
                    // caching problem.
                    URLConnection urlCon = wsdlUrl.openConnection();
                    urlCon.setUseCaches(false);
                    is = urlCon.getInputStream();                    
                    copyIsToOs(is, response.getOutputStream());
                }
                success = true;
                if( logger.isLoggable(Level.FINE) ) {
                    logger.fine("Serving up final wsdl " + wsdlUrl + " for " + 
                                request.getRequestURL() + 
                                (queryString != null ? ("?"+queryString) : ""));
                }
            } catch(Exception e) {
                logger.log(Level.INFO, "Failure serving WSDL for web service " +
                           webService.getName(), e);
            } finally {
                if(is != null) {
                    try {
                        is.close();
                    } catch(IOException ex) {}
                }
            }
        }

        if( !success ) {
            String message = 
                "Invalid wsdl request " +  request.getRequestURL() +
                (queryString != null ? ("?"+queryString) : "") +
                " for web service "+ webService.getName();
                
            logger.info(message);

            writeInvalidMethodType(response, message);
        }
        
        return success;
    }

    private void copyIsToOs(InputStream is, OutputStream os) throws IOException {
        byte[] buf = new byte[4096];
        int len = 0;
        while (len != -1) {
            try {
                len = is.read(buf, 0, buf.length);
            } catch (EOFException eof){
                break;
            }

            if(len != -1) {
                os.write(buf, 0, len);
            }
        }
        os.flush();
        is.close();
        os.close();
    }

    /**
     * All wsdl files and wsdl imported files live under a well-known
     * wsdl directory. 
     * @param uri module uri
     */
    public boolean isWsdlContent(String uri, BundleDescriptor bundle) {
        String wsdlDir = getWsdlDir(bundle);
        return (uri != null) && uri.startsWith(wsdlDir);
    }

    /**
     * @return module-specific dedicated wsdl directory 
     */
    public String getWsdlDir(BundleDescriptor bundle) {
        boolean isWar = (bundle instanceof WebBundleDescriptor);
        return isWar ? "WEB-INF/wsdl" : "META-INF/wsdl";
    }

    /**
     * Set up a stub for request/response SOAP message logging.
     */
    public void setClientTransportLog(ServiceReferenceDescriptor serviceRef,
                                      Stub stub, String transportLogUrlStr) {
        try {
            
            final String logUrlString  = transportLogUrlStr;

            OutputStream os = (OutputStream)
                java.security.AccessController.doPrivileged
                (new java.security.PrivilegedExceptionAction() {
                    public java.lang.Object run() throws Exception {
                        URL transportLogUrl = new URL(logUrlString);
                        File transportFile =new File(transportLogUrl.getFile());
                        return new FileOutputStream(transportFile, true);
                    }
                });

          /*  ClientTransportFactory transportFactory =
                rpcFactory.createClientTransportFactory(
                        ClientTransportFactoryTypes.HTTP, os);

            if( stub instanceof StubBase ) {
                ((StubBase)stub)._setTransportFactory(transportFactory);
                logger.info("Logging client transport for service-ref " +
                            serviceRef.getName() + " to file " + 
                            transportLogUrlStr);
            }*/

        } catch(PrivilegedActionException pae) {
            logger.log(Level.INFO, "", pae.getCause());
        } catch(Throwable t) {
            logger.log(Level.INFO, "", t);
        }
    }

    /**
     * Collect all relative imports from a web service's main wsdl document.
     *
     *@param wsdlRelativeImports outupt param in which wsdl relative imports 
     * will be added
     *
     *@param schemaRelativeImports outupt param in which schema relative 
     * imports will be added
     */
    public void parseRelativeImports(URL wsdlFileUrl, 
                                      Collection wsdlRelativeImports,
                                      Collection wsdlIncludes,
                                      Collection schemaRelativeImports,
                                       Collection schemaIncludes)
        throws Exception {
        
        // We will use our little parser rather than using JAXRPC's heavy weight WSDL parser
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);

        InputStream is = null;
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            is = wsdlFileUrl.openStream();
            Document document = builder.parse(is);
            procesSchemaImports(document, schemaRelativeImports);
            procesWsdlImports(document, wsdlRelativeImports);
            procesSchemaIncludes(document, schemaIncludes);
            procesWsdlIncludes(document, wsdlIncludes);
        } catch (SAXParseException spe) {
            // Error generated by the parser
            logger.log(Level.SEVERE,"\n** Parsing error" + ", line " +
                spe.getLineNumber() + ", uri " + spe.getSystemId());
            // Use the contained exception, if any
            Exception x = spe;
            if (spe.getException() != null) {
                x = spe.getException();
            }
            logger.log(Level.SEVERE,"Error occured", x);
        } catch (Exception sxe) {
            logger.log(Level.SEVERE, "Error parsing WSDL" + sxe.getMessage());
        } finally {
            try {
                if(is != null) {
                    is.close();
                }
            } catch (IOException io) {}
        }
    }

    private void addImportsAndIncludes(NodeList list, Collection result,
                        String namespace, String location) throws SAXException,
                        ParserConfigurationException, IOException, SAXParseException {
        for(int i=0; i<list.getLength(); i++) {
            String givenLocation = null;
            Node element = list.item(i);
            NamedNodeMap attrs = element.getAttributes();
            Node n= attrs.getNamedItem(location);
            if(n != null) {
                givenLocation = n.getNodeValue();
            }
            if((givenLocation == null) ||
                ((givenLocation != null) && givenLocation.startsWith("http"))) {
                continue;
            }
            Import imp = new Import();
            imp.setLocation(givenLocation);
            if(namespace != null) {
                n = attrs.getNamedItem(namespace);
                if(n != null) {
                    imp.setNamespace(n.getNodeValue());
                }
            }
            result.add(imp);
        }        
        return;
    }
    
    private void procesSchemaImports(Document document, Collection schemaImportCollection) throws SAXException,
                ParserConfigurationException, IOException {
        NodeList schemaImports =
                         document.getElementsByTagNameNS("http://www.w3.org/2001/XMLSchema", "import");
        addImportsAndIncludes(schemaImports, schemaImportCollection, "namespace", "schemaLocation");
    }

    private void procesWsdlImports(Document document, Collection wsdlImportCollection) throws SAXException,
                ParserConfigurationException, IOException {
        NodeList wsdlImports =
                         document.getElementsByTagNameNS("http://schemas.xmlsoap.org/wsdl/", "import");
        addImportsAndIncludes(wsdlImports, wsdlImportCollection, "namespace", "location");
    }        

    private void procesSchemaIncludes(Document document, Collection schemaIncludeCollection) throws SAXException,
                ParserConfigurationException, IOException{
        NodeList schemaIncludes = 
                         document.getElementsByTagNameNS("http://www.w3.org/2001/XMLSchema", "include");
        addImportsAndIncludes(schemaIncludes, schemaIncludeCollection, null, "schemaLocation");
    }

    private void procesWsdlIncludes(Document document, Collection wsdlIncludesCollection) throws SAXException,
                ParserConfigurationException, IOException{
        NodeList wsdlIncludes =
                         document.getElementsByTagNameNS("http://schemas.xmlsoap.org/wsdl/", "include");
        addImportsAndIncludes(wsdlIncludes, wsdlIncludesCollection, null, "location");
    }

    /**
     * Transform the deployed WSDL document for a given webservice by
     * replacing the ENDPOINT ADDRESS for each port with the actual 
     * endpoint address on which it will be listening.  
     * 
     */
    public void generateFinalWsdl(URL wsdlFileUrl, WebService webService, WebServerInfo wsi,
                                  File finalWsdlFile) throws Exception {
       OutputStream outputStream =
            new BufferedOutputStream(new FileOutputStream(finalWsdlFile));
        generateFinalWsdl(wsdlFileUrl, webService, wsi, outputStream);
                                      
    }
    
    public void generateFinalWsdl(URL wsdlFileUrl, WebService webService, WebServerInfo wsi,
                                  OutputStream outputStream) throws Exception {
                                      

        Collection wsdlRelativeImports = new HashSet();
        Collection wsdlIncludes = new HashSet();
        Collection schemaRelativeImports = new HashSet();
        Collection schemaIncludes = new HashSet();
        if( webService.hasUrlPublishing() ) {
            parseRelativeImports(wsdlFileUrl, wsdlRelativeImports,
                         wsdlIncludes, schemaRelativeImports, schemaIncludes);
        }
        
        Collection endpoints = webService.getEndpoints();

        // a WSDL file can contain several ports associated to a service.
        // however Deployment descriptors can be expressed in two ways 
        // to describe such a scenario in webservices.xml :
        //  - One webservice-description with 2 port-components
        //  - Two webservice-description with 1 port-component
        // The issue with #1, is that we need to configure the XSL with
        // the two ports so that the resulting unique WSDL has the correct
        // endpoint information and the JAXRPC stubs generated correctly.
        // So we need to check if this bundle is declaring more webservice
        // descriptor pointing to the same WSDL file...
        Collection endpointsCopy = new ArrayList();
        endpointsCopy.addAll(endpoints);
        
        BundleDescriptor bundle = webService.getBundleDescriptor();
        WebServicesDescriptor wsd = bundle.getWebServices();
        Collection webServices = wsd.getWebServices();
        if (webServices.size()>1) {
            for (Iterator wsIter = webServices.iterator();wsIter.hasNext();) {
                WebService aWS = (WebService) wsIter.next();
                if (webService.getName().equals(aWS.getName())) {
                    continue;
                }
                // this is another web service defined in the same bundle.
                // let's check if it points to the same WSDL file
                if ((webService.getWsdlFileUri() != null) &&
                     (aWS.getWsdlFileUri() != null) &&
                     (webService.getWsdlFileUri().equals(aWS.getWsdlFileUri()))) {
                    endpointsCopy.addAll(aWS.getEndpoints());
                } else if ((webService.getWsdlFileUrl() != null) &&
                     (aWS.getWsdlFileUrl() != null) &&
                     ((webService.getWsdlFileUrl().toString())
                        .equals(aWS.getWsdlFileUrl().toString()))) {
                    endpointsCopy.addAll(aWS.getEndpoints());
                }
            }
        }

        
        // Load the wsdl file bytes with caching turned off.  This is
        // to avoid a jar url consistency problem that can arise if we
        // overwrite the module file later on in deployment.
        InputStream wsdlInputStream = new BufferedInputStream(wsdlFileUrl.openStream());
        Source XsltWsdlDocument = new StreamSource(wsdlInputStream);
        Templates templates = createTemplatesFor
            (endpointsCopy, wsdlRelativeImports, wsdlIncludes, 
                schemaRelativeImports, schemaIncludes);
                                                 
        Transformer transformer = templates.newTransformer();


        // WSDL is associated with webservice, but url is endpoint-specific,
        // so let WebService choose which endpoint to use.
        WebServiceEndpoint endpointForImport = 
            webService.pickEndpointForRelativeImports();
        URL root= wsi.getWebServerRootURL(endpointForImport.isSecure());
        URL finalWsdlUrl = endpointForImport.composeFinalWsdlUrl(root);
        
        int wsdlImportNum = 0;
        for(Iterator iter = wsdlRelativeImports.iterator(); iter.hasNext();){
            Import next = (Import) iter.next();
            transformer.setParameter
                (WSDL_IMPORT_NAMESPACE_PARAM_NAME + wsdlImportNum, 
                 next.getNamespace());
            
            // Convert each relative import into an absolute import, using
            // the final wsdl's Url as the context
            URL relativeUrl  = new URL(finalWsdlUrl, next.getLocation());
            transformer.setParameter
                (WSDL_IMPORT_LOCATION_PARAM_NAME + wsdlImportNum, relativeUrl);
            
            wsdlImportNum++;
        }

        int schemaImportNum = 0;
        for(Iterator iter = schemaRelativeImports.iterator(); iter.hasNext();){
            Import next = (Import) iter.next();
            transformer.setParameter
                (SCHEMA_IMPORT_NAMESPACE_PARAM_NAME + schemaImportNum, 
                 next.getNamespace());
            
            // Convert each relative import into an absolute import, using
            // the final wsdl's Url as the context
            URL relativeUrl  = new URL(finalWsdlUrl, next.getLocation());
            transformer.setParameter
                (SCHEMA_IMPORT_LOCATION_PARAM_NAME + schemaImportNum, 
                 relativeUrl);
            
            schemaImportNum++;
        }        

        int wsdlIncludeNum = 0;
        for(Iterator iter = wsdlIncludes.iterator(); iter.hasNext();){
            Import next = (Import) iter.next();
            URL relativeUrl  = new URL(finalWsdlUrl, next.getLocation());
            transformer.setParameter
                (WSDL_INCLUDE_LOCATION_PARAM_NAME + wsdlIncludeNum, relativeUrl);            
            wsdlIncludeNum++;
        }

        int schemaIncludeNum = 0;
        for(Iterator iter = schemaIncludes.iterator(); iter.hasNext();){
            Import next = (Import) iter.next();
            URL relativeUrl  = new URL(finalWsdlUrl, next.getLocation());
            transformer.setParameter
                (SCHEMA_INCLUDE_LOCATION_PARAM_NAME + schemaIncludeNum, 
                 relativeUrl);            
            schemaIncludeNum++;
        }        

        int endpointNum = 0;
        for(Iterator iter = endpointsCopy.iterator(); iter.hasNext();) {
            WebServiceEndpoint next = (WebServiceEndpoint) iter.next();

            // Get a URL for the root of the webserver, where the host portion
            // is a canonical host name.  Since this will be used to compose the
            // endpoint address that is written into WSDL, it's better to use
            // hostname as opposed to IP address.
            // The protocol and port will be based on whether the endpoint 
            // has a transport guarantee of INTEGRAL or CONFIDENTIAL.
            // If yes, https will be used.  Otherwise, http will be used.
            URL rootURL = wsi.getWebServerRootURL(next.isSecure());

            URL actualAddress = next.composeEndpointAddress(rootURL);

            transformer.setParameter(ENDPOINT_ADDRESS_PARAM_NAME + endpointNum, 
                                     actualAddress.toExternalForm());

            String endpointType = next.implementedByEjbComponent() ?
                "EJB" : "Servlet";

            logger.log(Level.INFO,
                                 "enterprise.deployment.endpoint.registration",
                       new Object[] { endpointType,
                                      next.getEndpointName(), actualAddress });
            
            endpointNum++;
        }

        transformer.transform(XsltWsdlDocument, new StreamResult(outputStream));
        wsdlInputStream.close();
        outputStream.close();

        return;
    }

    public HandlerInfo createHandlerInfo(WebServiceHandler handler,
                                         ClassLoader loader) 
        throws Exception {

        QName[] headers = new QName[handler.getSoapHeaders().size()];
        int i = 0;
        for(Iterator iter = handler.getSoapHeaders().iterator();
            iter.hasNext();) {
            headers[i] = (QName) iter.next();
            i++;
        }

        Map properties = new HashMap();
        for(Iterator iter = handler.getInitParams().iterator(); 
            iter.hasNext();) {
            NameValuePairDescriptor next = (NameValuePairDescriptor) 
                iter.next();
            properties.put(next.getName(), next.getValue());
        }

        Class handlerClass = loader.loadClass(handler.getHandlerClass());
        return new HandlerInfo(handlerClass, properties, headers);
    }

   /**
     * Accessing wsdl URL might involve file system access, so wrap
     * operation in a doPrivileged block.
     */
    public URL privilegedGetServiceRefWsdl
        (ServiceReferenceDescriptor desc) throws Exception {
        URL wsdlFileURL;
        try {
            final ServiceReferenceDescriptor serviceRef = desc;
            wsdlFileURL = (URL) java.security.AccessController.doPrivileged
                (new java.security.PrivilegedExceptionAction() {
                        public java.lang.Object run() throws Exception {
                            URL retVal;
                            if(serviceRef.hasWsdlOverride()) {
                                retVal = serviceRef.getWsdlOverride();
                            } else {
                                // Upon server restart, wsdlfileURL can be null
                                // check that and return value from wsdlFileURI
                                if(serviceRef.getWsdlFileUrl() != null) {
                                    retVal = serviceRef.getWsdlFileUrl();
                                } else {
                                    if(serviceRef.getWsdlFileUri().startsWith("http")) {
                                        retVal = new URL(serviceRef.getWsdlFileUri());
                                    } else {
                                        if ((serviceRef.getWsdlFileUri().startsWith("WEB-INF")|| serviceRef.getWsdlFileUri().startsWith("META-INF"))) {

                                            //This can be the case when the toURL fails
                                            //because in its implementation it looks for user.dir
                                            // which sometimes can vary based on where vm is launched
                                            // so in this case
                                            //resolve from application path
                                            WebServiceContractImpl wscImpl  = WebServiceContractImpl.getInstance();
                                            ServerEnvironment se = wscImpl.getHabitat().getByContract(ServerEnvironment.class);

                                            File appFile = new File(se.getApplicationRepositoryPath(),serviceRef.getBundleDescriptor().getApplication().getAppName());
                                            if (appFile.exists()) {
                                               retVal = new File(appFile,serviceRef.getWsdlFileUri()).toURL();
                                            } else {
                                               //Fix for 6853656 and 6868695
                                               //In case of appclients the wsdl will be in the classpath
                                               //This will work for launches using the appclient command and
                                               // for Java Web Start launches
                                                
                                               retVal = Thread.currentThread().getContextClassLoader().getResource(serviceRef.getWsdlFileUri()) ;
                                            }
                                        }else {
                                            retVal = new File(serviceRef.getWsdlFileUri()).toURL();
                                        }
                                    }
                                }
                            }
                            return retVal;
                        }
                    });
        } catch(PrivilegedActionException pae) {
            logger.log(Level.WARNING, "", pae);
            Exception e = new Exception();
            e.initCause(pae.getCause());
            throw e;
        }
        return wsdlFileURL;
    }

    public boolean isJAXWSbasedService(WebService ws) {
        boolean jaxwsEndPtFound = false;
        boolean jaxrpcEndPtFound = false;
        for (WebServiceEndpoint endpoint : ws.getEndpoints()) {
            String implClassName;
            if (endpoint.implementedByEjbComponent()) {
                implClassName = endpoint.getEjbComponentImpl().getEjbClassName();
            } else {
                implClassName = endpoint.getWebComponentImpl().getWebComponentImplementation();
            }
            Class implClass;
            try {
                implClass = Thread.currentThread().getContextClassLoader().loadClass(implClassName);
            } catch(Exception e) {
                logger.log(Level.SEVERE, "Unable to load impl class ", e);
                continue;
            }
            if (implClass!=null) {
                if(implClass.getAnnotation(javax.xml.ws.WebServiceProvider.class) != null) {
                    // if we already found a jaxrpcendpoint, flag error since we do not support jaxws+jaxrpc endpoint
                    // in the same service
                    if(jaxrpcEndPtFound) {
                        logger.log(Level.SEVERE, rb.getString("jaxws-jaxrpc.error"));
                        continue;
                    }
                    //This is a JAXWS endpoint with @WebServiceProvider
                    //Do not run wsgen for this endpoint
                    jaxwsEndPtFound = true;
                    continue;
                }
                if(implClass.getAnnotation(javax.jws.WebService.class) != null) {
                    // if we already found a jaxrpcendpoint, flag error since we do not support jaxws+jaxrpc endpoint
                    // in the same service
                    if(jaxrpcEndPtFound) {
                        logger.log(Level.SEVERE, rb.getString("jaxws-jaxrpc.error"));
                        continue;
                    }
                    // This is a JAXWS endpoint with @WebService;
                    jaxwsEndPtFound = true;
                    continue;
                }
                if (JAXWSServlet.class.isAssignableFrom(implClass)) {
                     // if we already found a jaxrpcendpoint, flag error since we do not support jaxws+jaxrpc endpoint
                    // in the same service
                    if(jaxrpcEndPtFound) {
                        logger.log(Level.SEVERE, rb.getString("jaxws-jaxrpc.error"));
                        continue;
                    }
                    // This is a JAXWS endpoint with @WebService;
                    jaxwsEndPtFound = true;
                    continue;
                } else {
                    // this is a jaxrpc endpoint
                    // if we already found a jaxws endpoint, flag error since we do not support jaxws+jaxrpc endpoint
                    // in the same service
                    if(jaxwsEndPtFound) {
                        logger.log(Level.SEVERE, rb.getString("jaxws-jaxrpc.error"));
                        continue;
                    }
                    // Set spec version to 1.1 to indicate later the wscompile should be run
                    // We do this here so that jaxrpc endpoint having J2EE1.4 or JavaEE5
                    // descriptors will work properly
                    jaxrpcEndPtFound = true;
                    ws.getWebServicesDescriptor().setSpecVersion("1.1");
                }
            }
        }
        return jaxwsEndPtFound;
    }

    public javax.xml.rpc.Service createConfiguredService
        (ServiceReferenceDescriptor desc) throws Exception {
        
        final ServiceReferenceDescriptor serviceRef = desc;
        javax.xml.rpc.Service service = null;
        try {

            // Configured service can be created with any kind of URL.
            // Since resolving it might require file system access,
            // do operation in a doPrivivileged block.  JAXRPC RI should
            // probably have the doPrivileged as well.

            final URL wsdlFileURL = privilegedGetServiceRefWsdl(serviceRef);
            final QName serviceName = serviceRef.getServiceName();
            final ServiceFactory serviceFactory = ServiceFactory.newInstance();

            service = (javax.xml.rpc.Service) 
                java.security.AccessController.doPrivileged
                (new java.security.PrivilegedExceptionAction() {
                        public java.lang.Object run() throws Exception {
                            return serviceFactory.createService
                                (wsdlFileURL, serviceName);
                        }
                    });

        } catch(PrivilegedActionException pae) {
            logger.log(Level.WARNING, "", pae);
            Exception e = new Exception();
            e.initCause(pae.getCause());
            throw e;
        }

        return service;
    }

    public void configureHandlerChain(ServiceReferenceDescriptor serviceRef,
                                      javax.xml.rpc.Service service, 
                                      Iterator ports, ClassLoader loader)
        throws Exception {

        if( !serviceRef.hasHandlers() ) {
            return;
        }
        
        HandlerRegistry registry = service.getHandlerRegistry();
        
        while(ports.hasNext()) {
            QName nextPort = (QName) ports.next();

            List handlerChain = registry.getHandlerChain(nextPort);
            Collection soapRoles = new HashSet(); 

            for(Iterator iter = serviceRef.getHandlers().iterator();
                iter.hasNext();) {
                WebServiceHandler nextHandler = (WebServiceHandler) iter.next();
                Collection portNames = nextHandler.getPortNames();
                if( portNames.isEmpty() || 
                    portNames.contains(nextPort.getLocalPart()) ) {
                    soapRoles.addAll(nextHandler.getSoapRoles());
                    HandlerInfo handlerInfo = 
                        createHandlerInfo(nextHandler, loader);
                    handlerChain.add(handlerInfo);
                }
            }
        }
    }

    /**
     * Create an xslt template for transforming the packaged webservice
     * WSDL to a final WSDL.
     */
    private Templates createTemplatesFor(Collection endpoints,
                                         Collection wsdlRelativeImports,
                                         Collection wsdlIncludes,
                                         Collection schemaRelativeImports,
                                         Collection schemaIncludes) throws Exception {

        // create the stylesheet
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(bos, "UTF-8");

        writer.write("<xsl:transform version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:wsdl=\"http://schemas.xmlsoap.org/wsdl/\" xmlns:soap=\"http://schemas.xmlsoap.org/wsdl/soap/\" xmlns:soap12=\"http://schemas.xmlsoap.org/wsdl/soap12/\">\n");

        int wsdlImportNum = 0;
        for(Iterator iter = wsdlRelativeImports.iterator(); iter.hasNext();) {

            Import next = (Import) iter.next();
            String importNamespaceParam =
                WSDL_IMPORT_NAMESPACE_PARAM_NAME + wsdlImportNum;
            String importLocationParam =
                WSDL_IMPORT_LOCATION_PARAM_NAME + wsdlImportNum;
            writer.write("<xsl:param name=\"" + importNamespaceParam + "\"/>\n");
            writer.write("<xsl:param name=\"" + importLocationParam + "\"/>\n");

            writer.write("<xsl:template match=\"/\"><xsl:apply-templates mode=\"copy\"/></xsl:template>\n");
            writer.write("<xsl:template match=\"wsdl:definitions/wsdl:import[@location='");
            writer.write(next.getLocation());
            writer.write("']\" mode=\"copy\">");
            writer.write("<wsdl:import>");
            writer.write("<xsl:attribute name=\"namespace\"><xsl:value-of select=\"$" + importNamespaceParam + "\"/>");
            writer.write("</xsl:attribute>");
            writer.write("<xsl:attribute name=\"location\"><xsl:value-of select=\"$" + importLocationParam + "\"/>");
            writer.write("</xsl:attribute>");
            writer.write("</wsdl:import></xsl:template>");

            wsdlImportNum++;
        }

        int wsdlIncludeNum = 0;
        for(Iterator iter = wsdlIncludes.iterator(); iter.hasNext();) {

            Import next = (Import) iter.next();
            String importLocationParam =
                WSDL_INCLUDE_LOCATION_PARAM_NAME + wsdlIncludeNum;
            writer.write("<xsl:param name=\"" + importLocationParam + "\"/>\n");

            writer.write("<xsl:template match=\"/\"><xsl:apply-templates mode=\"copy\"/></xsl:template>\n");
            writer.write("<xsl:template match=\"wsdl:definitions/wsdl:include[@location='");
            writer.write(next.getLocation());
            writer.write("']\" mode=\"copy\">");
            writer.write("<wsdl:include>");
            writer.write("<xsl:attribute name=\"location\"><xsl:value-of select=\"$" + importLocationParam + "\"/>");
            writer.write("</xsl:attribute>");
            writer.write("</wsdl:include></xsl:template>");

            wsdlIncludeNum++;
        }

        int schemaImportNum = 0;
        for(Iterator iter = schemaRelativeImports.iterator(); iter.hasNext();) {

            Import next = (Import) iter.next();
            String importNamespaceParam =
                SCHEMA_IMPORT_NAMESPACE_PARAM_NAME + schemaImportNum;
            String importLocationParam =
                SCHEMA_IMPORT_LOCATION_PARAM_NAME + schemaImportNum;
            writer.write("<xsl:param name=\"" + importNamespaceParam + "\"/>\n");
            writer.write("<xsl:param name=\"" + importLocationParam + "\"/>\n");

            writer.write("<xsl:template match=\"/\"><xsl:apply-templates mode=\"copy\"/></xsl:template>\n");
            writer.write("<xsl:template match=\"wsdl:definitions/wsdl:types/xsd:schema/xsd:import[@schemaLocation='");
            writer.write(next.getLocation());
            writer.write("']\" mode=\"copy\">");
            writer.write("<xsd:import>");
            writer.write("<xsl:attribute name=\"namespace\"><xsl:value-of select=\"$" + importNamespaceParam + "\"/>");
            writer.write("</xsl:attribute>");
            writer.write("<xsl:attribute name=\"schemaLocation\"><xsl:value-of select=\"$" + importLocationParam + "\"/>");
            writer.write("</xsl:attribute>");
            writer.write("</xsd:import></xsl:template>");

            schemaImportNum++;
        }

        int schemaIncludeNum = 0;
        for(Iterator iter = schemaIncludes.iterator(); iter.hasNext();) {

            Import next = (Import) iter.next();
            String importLocationParam =
                SCHEMA_INCLUDE_LOCATION_PARAM_NAME + schemaIncludeNum;
            writer.write("<xsl:param name=\"" + importLocationParam + "\"/>\n");

            writer.write("<xsl:template match=\"/\"><xsl:apply-templates mode=\"copy\"/></xsl:template>\n");
            writer.write("<xsl:template match=\"wsdl:definitions/wsdl:types/xsd:schema/xsd:include[@schemaLocation='");
            writer.write(next.getLocation());
            writer.write("']\" mode=\"copy\">");
            writer.write("<xsd:include>");
            writer.write("<xsl:attribute name=\"schemaLocation\"><xsl:value-of select=\"$" + importLocationParam + "\"/>");
            writer.write("</xsl:attribute>");
            writer.write("</xsd:include></xsl:template>");

            schemaIncludeNum++;
        }

        int endpointNum = 0;
        for(Iterator iter = endpoints.iterator(); iter.hasNext();) {
            WebServiceEndpoint endpoint = (WebServiceEndpoint) iter.next();

            if( !endpoint.hasWsdlPort() ) {
                throw new Exception("No WSDL port specified for endpoint " +
                                    endpoint.getEndpointName());
            }
            if( !endpoint.hasServiceName() ) {
                throw new Exception("Runtime settings error.  Cannot find " +
                                    "service name for endpoint " +
                                    endpoint.getEndpointName());
            }

            String actualAddressParam =
                ENDPOINT_ADDRESS_PARAM_NAME + endpointNum;

            writer.write("<xsl:param name=\"" + actualAddressParam + "\"/>\n");

            writer.write("<xsl:template match=\"/\"><xsl:apply-templates mode=\"copy\"/></xsl:template>\n");

            writer.write("<xsl:template match=\"wsdl:definitions[@targetNamespace='");
            writer.write(endpoint.getServiceName().getNamespaceURI());
            writer.write("']/wsdl:service[@name='");
            writer.write(endpoint.getServiceName().getLocalPart());
            writer.write("']/wsdl:port[@name='");
            writer.write(endpoint.getWsdlPort().getLocalPart());
            writer.write("']/"+endpoint.getSoapAddressPrefix()+":address\" mode=\"copy\">");
            writer.write("<"+endpoint.getSoapAddressPrefix()+":address><xsl:attribute name=\"location\"><xsl:value-of select=\"$" + actualAddressParam + "\"/>");
            writer.write("</xsl:attribute></"+endpoint.getSoapAddressPrefix()+":address></xsl:template>");

            endpointNum++;
        }

        writer.write("<xsl:template match=\"@*|node()\" mode=\"copy\"><xsl:copy><xsl:apply-templates select=\"@*\" mode=\"copy\"/><xsl:apply-templates mode=\"copy\"/></xsl:copy></xsl:template>\n");
        writer.write("</xsl:transform>\n");
        writer.close();
        byte[] stylesheet = bos.toByteArray();

        if( logger.isLoggable(Level.FINE) ) {
            logger.fine(new String(stylesheet));
        }

        Source stylesheetSource =
            new StreamSource(new ByteArrayInputStream(stylesheet));
        TransformerFactory transformerFactory= TransformerFactory.newInstance();
        Templates templates = transformerFactory.newTemplates(stylesheetSource);

        return templates;
    }

    /**
     * @return Set of service endpoint interface class names supported by
     * a generated service interface.
     *
     * @return Collection of String class names
     */
    public Collection getSEIsFromGeneratedService
        (Class generatedServiceInterface) throws Exception {

        Collection seis = new HashSet();

        Method[] declaredMethods =
            generatedServiceInterface.getDeclaredMethods();

        // Use naming convention from jaxrpc spec to derive SEI class name.
        for(int i = 0; i < declaredMethods.length; i++) {
            Method next = declaredMethods[i];
            Class returnType = next.getReturnType();
            if( next.getName().startsWith("get") &&
                (next.getDeclaringClass() != javax.xml.rpc.Service.class) &&
                java.rmi.Remote.class.isAssignableFrom(returnType) ) {
                seis.add(returnType.getName());
            }
        }

        return seis;
    }

   /* *//**
     * Called from client side deployment object on receipt of final
     * wsdl from server.  
     *
     *@param clientPublishUrl Url of directory on local file system to which
     * wsdl is published
     *
     *@param finalWsdlUri location relative to publish directory where final 
     * wsdl should be written, in uri form. 
     *
     *@return file to which final wsdl was written 
     *//*
    public File publishFinalWsdl(URL clientPublishUrl, String finalWsdlUri,
                                 byte[] finalWsdlBytes)
        throws Exception 
    {
        File finalWsdlFile = null;
        FileOutputStream fos = null;
        try {
            finalWsdlFile = new File
                (clientPublishUrl.getFile(),
                 finalWsdlUri.replace('/', File.separatorChar));
            File parent = finalWsdlFile.getParentFile();
            if( !parent.exists() ) {
                boolean madeDirs = parent.mkdirs();
                if( !madeDirs ) {
                    throw new IOException("Error creating " + parent);
                }
            }
            fos = new FileOutputStream(finalWsdlFile);
            fos.write(finalWsdlBytes, 0, finalWsdlBytes.length);
        } finally {
            if( fos != null ) {
                try { 
                    fos.close();
                } catch(IOException ioe) {
                    logger.log(Level.INFO, "", ioe);
                }
            }
        }
        return finalWsdlFile;
    }

    *//**
     * Find a Port object within the JAXRPC Model.
     */
    public Port getPortFromModel(Model model, QName portName) {
        
        for(Iterator serviceIter = model.getServices(); serviceIter.hasNext();){
            Service next = (Service) serviceIter.next();
            for(Iterator portIter = next.getPorts(); portIter.hasNext();) {
                Port nextPort = (Port) portIter.next();
                if( portsEqual(nextPort, portName) ) {
                    return nextPort;
                }
            }
        }
        return null;
    }

    /**
     * Find a Service in which a particular port is defined.  Assumes port
     * QName is unique within a WSDL.
     */
    public Service getServiceForPort(Model model, QName thePortName) {

        for(Iterator serviceIter = model.getServices(); 
            serviceIter.hasNext();) {
            Service nextService = (Service) serviceIter.next();
            for(Iterator portIter = nextService.getPorts(); 
                portIter.hasNext();) {
                
                Port nextPort = (Port) portIter.next();
                if( portsEqual(nextPort, thePortName) ) {
                    return nextService;
                }
            }
        }

        return null;
    }

    /**
     * Logic for matching a port qname with a Port object from 
     * the JAXRPC-RI Model.
     */
    public boolean portsEqual(Port port, QName candidatePortName) {

        boolean equal = false;

        // Better to use Port object's WSDL_PORT property for port
        // QName than Port.getName().  If that returns null, use
        // Port.getName().
        QName portPropertyName = (QName) port.getProperty
            (ModelProperties.PROPERTY_WSDL_PORT_NAME);

        if( portPropertyName != null ) {
            equal = candidatePortName.equals(portPropertyName);
        } else {
            equal = candidatePortName.equals(port.getName());
        }

        return equal;
    }

    // Return collection of Port objects
    public Collection getAllPorts(Model model) {
        Collection ports = new HashSet();
        for(Iterator serviceIter = model.getServices(); 
            serviceIter.hasNext();) {
            Service next = (Service) serviceIter.next();
            ports.addAll(next.getPortsList());
        }
        return ports;
    }

    /**
     *@return a method object representing the target of a web service 
     * invocation
     */
    public Method getInvMethod(Tie webServiceTie, MessageContext context) 
        throws Exception {

        // Use tie to get Method from SOAP message inv.webServiceTie

        SOAPMessageContext soapMsgContext = (SOAPMessageContext) context;
        SOAPMessage message = soapMsgContext.getMessage();
        StreamingHandler streamingHandler = (StreamingHandler) webServiceTie;
        int opcode = streamingHandler.getOpcodeForRequestMessage(message);
        return streamingHandler.getMethodForOpcode(opcode);
    }

    /**
     * Convenience method for throwing a SOAP fault exception.  
     */
    public void throwSOAPFaultException(String faultString, MessageContext msgContext) {
        
        SOAPMessage soapMessage = 
                ((SOAPMessageContext)msgContext).getMessage();
        throwSOAPFaultException(faultString, soapMessage);
        
    }
    
    public void throwSOAPFaultException(String faultString,
                                        SOAPMessage soapMessage) 
        throws SOAPFaultException {

        SOAPFaultException sfe = null;

        try {

            SOAPPart sp = soapMessage.getSOAPPart();
            SOAPEnvelope se = sp.getEnvelope();

            // Consume the request
            SOAPBody sb = se.getBody();

            // Access the child elements of body
            Iterator iter = sb.getChildElements();

            // Response should only include the fault, so remove
            // any request body nodes.
            if (iter.hasNext()) {
                SOAPBodyElement requestBody = (SOAPBodyElement)iter.next();
                // detach this node from the tree
                requestBody.detachNode();
            }


            SOAPFault soapFault = sb.addFault();

            se.setEncodingStyle(SOAPConstants.URI_ENCODING);

            String faultActor = "http://schemas.xmlsoap.org/soap/actor/next";
            QName faultCode = SOAPConstants.FAULT_CODE_SERVER;

            soapFault.setFaultCode("env:" + faultCode.getLocalPart());
            soapFault.setFaultString(faultString);
            soapFault.setFaultActor(faultActor);

            sfe = new SOAPFaultException(faultCode, faultActor, faultString,
                                         null);
        } catch(SOAPException se) {
            logger.log(Level.FINE, "", se);
        }

        if( sfe != null ) {
            throw sfe;
        }
    }


    void writeReply(HttpServletResponse response, 
       com.sun.xml.rpc.spi.runtime.SOAPMessageContext messageContext) 
        throws IOException, SOAPException
    {

        // In case of a one-way operation, send no reply or fault.
        if (isMessageContextPropertySet(messageContext, ONE_WAY_OPERATION)) {
            return;
        }
          
        SOAPMessage reply = messageContext.getMessage();
        int statusCode = 0;
        if (messageContext.isFailure()) {
            
            if (isMessageContextPropertySet(messageContext,   
                                            CLIENT_BAD_REQUEST)) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST); 
                setContentTypeAndFlush(response);
                return;
                
            } else {
              response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            
        } else {           
            response.setStatus(HttpServletResponse.SC_OK);
        }
        
        OutputStream os = response.getOutputStream();
        String[] headers = reply.getMimeHeaders().getHeader("Content-Type");
        if (headers != null && headers.length > 0) {
            response.setContentType(headers[0]);
        } else {
            response.setContentType("text/xml");
        }

        putHeaders(reply.getMimeHeaders(), response);
        
        reply.writeTo(os);
        os.flush();
              
    }

    private static void putHeaders(MimeHeaders headers, 
                                   HttpServletResponse response) {
        headers.removeHeader("Content-Type");
        headers.removeHeader("Content-Length");
        Iterator it = headers.getAllHeaders();
        while (it.hasNext()) {
            MimeHeader header = (MimeHeader)it.next();
            response.setHeader(header.getName(), header.getValue());
        }
    }
    public static void raiseException(HttpServletResponse resp, String binding, String faultString) {
        
        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        
        if (HTTPBinding.HTTP_BINDING.equals(binding)) {
            resp.setContentType("text/xml");
            try {
                PrintWriter writer = new PrintWriter(resp.getOutputStream());
                writer.println("<error>" + faultString + "</error>");
            } catch(IOException ioe) {
                logger.log(Level.WARNING, "Cannot write out a HTTP XML exception : " + ioe.getMessage());
            }
        } else {
            String protocol;
            if (SOAPBinding.SOAP12HTTP_BINDING.equals(binding)) {
                protocol = javax.xml.soap.SOAPConstants.SOAP_1_2_PROTOCOL;
            } else {
                protocol = javax.xml.soap.SOAPConstants.SOAP_1_1_PROTOCOL;
            }
            SOAPMessage fault = WsUtil.getSOAPFault(protocol, faultString);
            
            if (fault!=null) {
                resp.setContentType("text/xml");
                try {
                    fault.writeTo(resp.getOutputStream());
                } catch(Exception ex) {
                    logger.log(Level.WARNING, "cannot write SOAPFault to the HTTP response", ex);
                }
            }
        }
    }
    
    public static SOAPMessage getSOAPFault(String protocol, String faultString) {

        if (protocol==null) {
            protocol = javax.xml.soap.SOAPConstants.SOAP_1_1_PROTOCOL;
        }
        try {
            MessageFactory factory = MessageFactory.newInstance(protocol);
            if (factory==null) {
                factory = MessageFactory.newInstance();
            }
            SOAPMessage message = factory.createMessage();
            SOAPBody body = message.getSOAPBody();
            SOAPFault fault = body.addFault();
            fault.setFaultString(faultString);
            SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
            String envelopeNamespace = envelope.getNamespaceURI();
            QName faultCode = new QName(envelopeNamespace, "Server", "env");
            fault.setFaultCode(faultCode);
            return message;
        } catch(SOAPException e) {
            logger.log(Level.WARNING, "Cannot create soap fault for " + faultString);
        }
        return null;
    }

    void writeInvalidContentType(HttpServletResponse response)
        throws SOAPException, IOException {
        //bad client content-type
        response.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE); 
        
        setContentTypeAndFlush(response);
    }

    void writeInvalidMethodType(HttpServletResponse response, 
                                String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head><title>");
        
        out.println("Invalid Method Type");
        out.println("</title></head>");
        out.println("<body>");
        out.println(message);
        out.println("</body>");
        out.println("</html>");
        
    }

    private void writeNotFoundErrorPage(HttpServletResponse response, 
                                        String message) throws IOException {

        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head><title>");
        out.println("J2EE Web Services");       
        out.println("</title></head>");
        out.println("<body>");
        out.println("<h1>404 Not Found: " + message + "</h1>");       
        out.println("</body>");
        out.println("</html>");

    }

    /*
     * Used to send back the message after a 4XX response code has been set
     */
    private void setContentTypeAndFlush(HttpServletResponse response) 
        throws IOException {
        response.setContentType("text/xml");
        response.flushBuffer(); // prevent html message in response
        response.getWriter().close();
    }
    
    boolean hasSomeTextXmlContent(MimeHeaders headers) {
        return ( hasTextXmlContentType(headers) &&
                 (getContentLength(headers) > 0) );
    }
    
    private int getContentLength(MimeHeaders headers) {
        String[] contentLength = headers.getHeader("Content-Length");

        int length = 0;

        if ((contentLength != null) && (contentLength.length > 0)) {
            length = new Integer(contentLength[0]).intValue();
        }

        return length;
    }

    boolean hasTextXmlContentType(MimeHeaders headers) {
        
        String[] contentTypes = headers.getHeader("Content-Type");
        if ((contentTypes != null) && (contentTypes.length >= 1)){
            if (contentTypes[0].indexOf("text/xml") != -1){
                return true;
            }
        }
        return false;
    }

    boolean isMessageContextPropertySet
        (com.sun.xml.rpc.spi.runtime.SOAPMessageContext messageContext, 
         String property){
        
        Object prop =  messageContext.getProperty(property);
        if (prop != null) {
            if ( (prop instanceof String) &&
                 ((String)prop).equalsIgnoreCase("true") ) {
                return true;
            }
        }

        return false;
    }

    MimeHeaders getHeaders(HttpServletRequest request) {
        Enumeration e = request.getHeaderNames();
        MimeHeaders headers = new MimeHeaders();

        while (e.hasMoreElements()) {
            String headerName = (String)e.nextElement();
            String headerValue = request.getHeader(headerName);
            headers.addHeader(headerName, headerValue);
        }

        return headers;
    } 
    
    public WebServerInfo getWebServerInfoForDAS() {
        WebServerInfo wsi = new WebServerInfo();

        if(this.networkListeners == null) {
            List<Integer> adminPorts = new ArrayList<Integer>();

            for (org.glassfish.api.container.Adapter subAdapter :
                    habitat.getAllByContract(org.glassfish.api.container.Adapter.class)) {
                if (subAdapter instanceof AdminAdapter) {
                    AdminAdapter aa = (AdminAdapter) subAdapter;
                    adminPorts.add(aa.getListenPort());
                } else if (subAdapter instanceof AdminConsoleAdapter) {
                    AdminConsoleAdapter aca = (AdminConsoleAdapter) subAdapter;
                    adminPorts.add(aca.getListenPort());
                }
            }

            for (NetworkListener nl : config.getNetworkConfig().getNetworkListeners().getNetworkListener()) {

                if(!adminPorts.contains(Integer.valueOf(nl.getPort()))) { // get rid of admin ports
                    if(networkListeners == null)
                        networkListeners = new ArrayList<NetworkListener>();

                    networkListeners.add(nl);
                }
            }
        }

        for(NetworkListener listener : networkListeners) {
            String host = listener.getAddress();
            if(listener.getAddress().equals("0.0.0.0"))
                host = "localhost"; // ugly!
            else {
            // TODO - resolve to hostname; it should be optional and turned off
            // by default
            // try {
            //     host = InetAddress.getByName(listener.getAddress()).getHostName();
            // } catch (UnknownHostException ex) {
            //     // silently ignore
            // }
            }

            if(listener.findHttpProtocol().getSecurityEnabled().equals("false"))
                wsi.setHttpVS(new VirtualServerInfo("http", host, Integer.parseInt(listener.getPort())));
            else if(listener.findHttpProtocol().getSecurityEnabled().equals("true"))
                wsi.setHttpsVS(new VirtualServerInfo("https", host, Integer.parseInt(listener.getPort())));
        }

        return wsi;
    }

    /**
     * @return the web server information based on a http request
     *//*
    public WebServerInfo getWebServerInfo(String moduleID, HttpServletRequest request) throws ConfigException {

    
        // let's get our admin context
        ConfigContext cc = ApplicationServer.getServerContext().getConfigContext();
        Server server = ServerBeansFactory.getServerBean(cc);
        Config config = ServerBeansFactory.getConfigBean(cc);
        ApplicationRef ref = server.getApplicationRefByRef(moduleID);
        return(getWebServerInfo(cc, config, ref.getVirtualServers(), null, null));
    }
    
   /* private WebServerInfo getWebServerInfo(ConfigContext cc, Config config,
                            String virtualServers, String targetName, String targetHostName) throws ConfigException {
        HttpService httpService = config.getHttpService();
        VirtualServer[] vServers = null;
        List httpListeners = new ArrayList();
        if (virtualServers==null) {
            vServers = httpService.getVirtualServer();
            if (vServers!=null) {
                for (int i=0;i<vServers.length;i++) {
                    VirtualServer vs = vServers[i];
                    // admin virtual server does not count
                    if (vs.getId().equals(com.sun.enterprise.web.VirtualServer.ADMIN_VS))
                        continue;
                    
                    String httpListenersString = vs.getHttpListeners();
                    if (httpListenersString!=null) {
                        String[] httpListenerIDs = httpListenersString.split(",");
                        if (httpListenerIDs!=null) {
                            for (int j=0;j<httpListenerIDs.length;j++) {
                                httpListeners.add(httpService.getHttpListenerById(httpListenerIDs[j]));
                            }
                        }
                    }
                }
            }
        } else {
            String virtualServerNames[] = virtualServers.split(",");
            if (virtualServerNames!=null) {
                for (int i=0;i<virtualServerNames.length;i++) {
                    VirtualServer vs = httpService.getVirtualServerById(virtualServerNames[i]);
                    String httpListenersString = vs.getHttpListeners();
                    if (httpListenersString!=null) {
                        String[] httpListenerIDs = httpListenersString.split(",");
                        if (httpListenerIDs!=null) {
                            for (int j=0;j<httpListenerIDs.length;j++) {
                                httpListeners.add(httpService.getHttpListenerById(httpListenerIDs[i]));
                            }
                        }
                    }
                }
            }
        }
    
        WebServerInfo wsi = new WebServerInfo();
        for (int i=0;i<httpListeners.size();i++) {
            HttpListener hl = (HttpListener) httpListeners.get(i);
            if (!hl.isEnabled()) 
                continue;
            
            if(targetHostName == null) {
                targetHostName = hl.getServerName();
                if (targetHostName==null || targetHostName.length()==0) {
                    targetHostName = getDefaultHostName();
                }
            }
            try {
                targetHostName = InetAddress.getByName(targetHostName).getCanonicalHostName();
            } catch (java.net.UnknownHostException unex) {
                throw new ConfigException(unex.getMessage(), unex);
            }
            String redirectPortNb = hl.getRedirectPort();
            String portNb;
            if (redirectPortNb!=null) {
                portNb = redirectPortNb;
            } else {
                portNb = hl.getPort();
            }
            if(targetName == null) {
                targetName = ApplicationServer.getServerContext().getInstanceName();
            }
            PropertyResolver pr = new PropertyResolver(cc, targetName);
            String resolvedPort = pr.resolve(portNb);
            
            int port = Integer.parseInt(resolvedPort);
            if (hl.isSecurityEnabled()) {
                wsi.setHttpsVS(new VirtualServerInfo("https", targetHostName, port));                
            } else {
                wsi.setHttpVS(new VirtualServerInfo("http", targetHostName, port));
            }

            if (wsi.getHttpVS()!=null && wsi.getHttpsVS()!=null) 
                break;
        }        
        return wsi;    
    }
    
    private String getDefaultHostName() {
        String defaultHostName = "localhost";
        try {
            InetAddress host = InetAddress.getLocalHost();
            defaultHostName = host.getCanonicalHostName();
        } catch(java.net.UnknownHostException uhe) {
            deploymentLogger.log(Level.FINEST, "mbean.get_local_host_error", uhe);
            deploymentLogger.log(Level.INFO, "mbean.use_default_host");
        }
        return defaultHostName;         
    }

    private static Logger _logger = null;
    
    *//**
     * @return the default Logger implementation for this package
     */
    public static Logger getDefaultLogger() {
        return logger;
    }    

    // resources...
    static LocalStringManagerImpl localStrings = 
        new LocalStringManagerImpl(WsUtil.class);
    
    public static LocalStringManagerImpl getDefaultStringManager() {
        return localStrings;
    }

    public void validateEjbEndpoint(WebServiceEndpoint ejbEndpoint) {
        EjbDescriptor ejbDescriptor = ejbEndpoint.getEjbComponentImpl();
        EjbBundleDescriptor bundle = ejbDescriptor.getEjbBundleDescriptor();
        WebServicesDescriptor webServices = bundle.getWebServices();
        Collection endpoints = 
            webServices.getEndpointsImplementedBy(ejbDescriptor);
        if( endpoints.size() == 1 ) {
            if( ejbDescriptor.hasWebServiceEndpointInterface() ) {
                if(!ejbEndpoint.getServiceEndpointInterface().equals
                   (ejbDescriptor.getWebServiceEndpointInterfaceName())) {
                    String msg = "Ejb " + ejbDescriptor.getName() + 
                        " service endpoint interface does not match " +
                        " port component " + ejbEndpoint.getEndpointName();
                    throw new IllegalStateException(msg);
                }
            } else {
                String msg = "Ejb " + ejbDescriptor.getName() + 
                    " must declare <service-endpoint> interface";
                throw new IllegalStateException(msg);
            }
        } else if( endpoints.size() > 1 ) {
            String msg = "Ejb " + ejbDescriptor.getName() + 
                " implements " + endpoints.size() + " web service endpoints " +
                " but must only implement 1";
            throw new IllegalStateException(msg);
        }
    }

    public void updateServletEndpointRuntime(WebServiceEndpoint endpoint) {
 
        // Copy the value of the servlet impl bean class into
        // the runtime information.  This way, we'll still 
        // remember it after the servlet-class element has been 
        // replaced with the name of the container's servlet class.
        endpoint.saveServletImplClass();

        WebComponentDescriptor webComp = 
            (WebComponentDescriptor) endpoint.getWebComponentImpl();

        WebBundleDescriptor bundle = webComp.getWebBundleDescriptor();
        WebServicesDescriptor webServices = bundle.getWebServices();
        Collection endpoints = 
            webServices.getEndpointsImplementedBy(webComp);

        if( endpoints.size() > 1 ) {
            String msg = "Servlet " + endpoint.getWebComponentLink() + 
                " implements " + endpoints.size() + " web service endpoints " +
                " but must only implement 1";
            throw new IllegalStateException(msg);
        }

        if( endpoint.getEndpointAddressUri() == null ) {
            Set urlPatterns = webComp.getUrlPatternsSet();
            if( urlPatterns.size() == 1 ) {

                // Set endpoint-address-uri runtime info to uri.
                // Final endpoint address will still be relative to context roo

                String uri = (String) urlPatterns.iterator().next();
                endpoint.setEndpointAddressUri(uri);

                // Set transport guarantee in runtime info if transport 
                // guarantee is INTEGRAL or CONDIFIDENTIAL for any 
                // security constraint with this url-pattern.
                Collection constraints = 
                    bundle.getSecurityConstraintsForUrlPattern(uri);
                for(Iterator i = constraints.iterator(); i.hasNext();) {
                    SecurityConstraint next = (SecurityConstraint) i.next();
                        
                    UserDataConstraint dataConstraint = 
                        next.getUserDataConstraint();
                    String guarantee = (dataConstraint != null) ?
                        dataConstraint.getTransportGuarantee() : null;

                    if( (guarantee != null) && 
                        ( guarantee.equals
                          (UserDataConstraint.INTEGRAL_TRANSPORT) || 
                          guarantee.equals
                          (UserDataConstraint.CONFIDENTIAL_TRANSPORT) ) ) {
                        endpoint.setTransportGuarantee(guarantee);
                        break;
                    }
                }
            } else {
                String msg = "Endpoint " + endpoint.getEndpointName() +
                    " has not been assigned an endpoint address " +
                    " and is associated with servlet " + 
                    webComp.getCanonicalName() + " , which has " +
                    urlPatterns.size() + " url patterns"; 
                throw new IllegalStateException(msg);
            } 
        }
    }    
   


    /*public void downloadFile(URL httpUrl, File toFile) throws Exception {
        InputStream is = null;
        FileOutputStream os = null;
        try {
            if(!toFile.createNewFile()) {
                throw new Exception("Unable to create new File " + toFile.getAbsolutePath());
            }
            is = httpUrl.openStream();

            os = new FileOutputStream(toFile, true);
            int readCount;
            byte[] buffer = new byte[10240]; // Read 10KB at a time
            while(true) {
                readCount = is.read(buffer, 0, 10240);
                if(readCount != -1) {
                    os.write(buffer, 0, readCount);
                } else {
                    break;
                }
            }
        } finally {
            if(is != null) {
                is.close();
            }
            if(os != null) {
                os.flush();
                os.close();
            }
        }
    }   */
    
    public Collection getWsdlsAndSchemas(File pkgedWsdl) throws Exception {
         
        ArrayList<SDDocumentSource> cumulative =  new ArrayList<SDDocumentSource>();
        getWsdlsAndSchemas(pkgedWsdl, cumulative);
	 
        //if there are circular imports of wsdls, the original wsdl might
        //be in this Collection of imported metadata documents.  If so, remove it.
        URL id = pkgedWsdl.toURL();
        SDDocumentSource toRemove = null;
        
        for (SDDocumentSource source: cumulative) {
            if ((id.toString()).equals(source.getSystemId().toString())) {
                toRemove = source;
            }
        }       
        if (toRemove != null) {
            cumulative.remove(toRemove);
        }
        
        return cumulative;
    }

    public void getWsdlsAndSchemas(File wsdl, 
                                   ArrayList<SDDocumentSource> cumulative) throws Exception {
        
        // Get a list of wsdl and schema relative imports in this wsdl
        Collection<Import> wsdlRelativeImports = new HashSet();
        Collection<Import> schemaRelativeImports = new HashSet();
        Collection<Import> wsdlIncludes = new HashSet();
        Collection<Import> schemaIncludes = new HashSet();
        String wsdlRoot = wsdl.getParent();

        parseRelativeImports(wsdl.toURL(), wsdlRelativeImports, wsdlIncludes,
                schemaRelativeImports, schemaIncludes);
        
        wsdlRelativeImports.addAll(wsdlIncludes);
        schemaRelativeImports.addAll(schemaIncludes);
      

        // List of all schema relative imports
        for(Import next : schemaRelativeImports) {
            String location = next.getLocation();
            location = location.replaceAll("/", "\\"+File.separator);
            File file = new File(wsdlRoot + File.separator + location)
                    .getCanonicalFile();
            addFileAndDecendents(file, cumulative);
        }
        // List of all wsdl relative imports
        for(Import next : wsdlRelativeImports) {
            String location = next.getLocation();
            location = location.replaceAll("/", "\\"+File.separator);

            File currentWsdlFile = new File(wsdlRoot+File.separator+location)
                    .getCanonicalFile();
            addFileAndDecendents(currentWsdlFile,  cumulative);
        }
    }

    /*
     * Add the File and wsdls and schemas imported by it to a list of metadata
     * documents used to initialize an endpoint.  Canonicalize the paths and check
     * whether the documents have already been added to the list.
     */
    private void addFileAndDecendents(File file,
            ArrayList<SDDocumentSource> cumulative) throws Exception {

        try {
            //JAX-WS has trouble with "..'s" in paths here
            file = file.getCanonicalFile();
        } catch (IOException e) {
            //hope JAX-WS can deal with the original path
        }

        //make sure we have not processed this file before
        URL id = file.toURL();
        boolean alreadyProcessed = false;

        for (SDDocumentSource source: cumulative) {
            if ((id.toString()).equals(source.getSystemId().toString())) {
                alreadyProcessed = true;
                break;
            }
        }
        if (!alreadyProcessed) {
            cumulative.add(0, SDDocumentSource.create(id));
            getWsdlsAndSchemas(file, cumulative);
        }

    }
    
    /*private void downloadWsdlsAndSchemas(WebService ws, URL httpUrl, File wsdlDir) throws Exception {
        // First make required directories and download this wsdl file
        wsdlDir.mkdirs();
        String fileName = httpUrl.toString().substring(httpUrl.toString().lastIndexOf("/")+1);
        File toFile = new File(wsdlDir.getAbsolutePath()+File.separator+fileName);
        downloadFile(httpUrl, toFile);        

        // Get a list of wsdl and schema relative imports in this wsdl
        Collection<Import> wsdlRelativeImports = new HashSet();
        Collection<Import> schemaRelativeImports = new HashSet();
        Collection<Import> wsdlIncludes = new HashSet();
        Collection<Import> schemaIncludes = new HashSet();
        parseRelativeImports(httpUrl, wsdlRelativeImports, wsdlIncludes,
                schemaRelativeImports, schemaIncludes);
        wsdlRelativeImports.addAll(wsdlIncludes);
        schemaRelativeImports.addAll(schemaIncludes);

        // Download all schema relative imports
        String urlWithoutFileName = httpUrl.toString().substring(0, httpUrl.toString().lastIndexOf("/"));
        for(Import next : schemaRelativeImports) {
            String location = next.getLocation();
            location = location.replaceAll("/", "\\"+File.separator);
            if(location.lastIndexOf(File.separator) != -1) {
                File newDir = new File(wsdlDir.getAbsolutePath()+File.separator+
                location.substring(0, location.lastIndexOf(File.separator)));
                newDir.mkdirs();
            }
            downloadFile(new URL(urlWithoutFileName+"/"+next.getLocation()),
                        new File(wsdlDir.getAbsolutePath()+File.separator+location));
        }

        // Download all wsdl relative imports
        for(Import next : wsdlRelativeImports) {
            String newWsdlLocation = next.getLocation();
            newWsdlLocation = newWsdlLocation.replaceAll("/",  "\\"+File.separator);
            File newWsdlDir;
            if(newWsdlLocation.lastIndexOf(File.separator) != -1) {
                newWsdlDir = new File(wsdlDir.getAbsolutePath() + File.separator +
                newWsdlLocation.substring(0, newWsdlLocation.lastIndexOf(File.separator)));
            } else {
                newWsdlDir = wsdlDir;
            }
            downloadWsdlsAndSchemas(ws, new URL(urlWithoutFileName+"/"+next.getLocation()), newWsdlDir);
        }
        return;
    }

    class JarFilter implements FilenameFilter {
        public boolean accept(File dir, String name) {
            return (name.endsWith(".jar"));
        }
    }

    private String addJarsToClassPath(String cp, String dirName) throws IASDeploymentException {
        try {
           File dir = new File(dirName);
           if(dir.exists()) {
               Iterator fileSetIter = com.sun.enterprise.util.FileUtil.getAllFilesUnder(dir, new JarFilter(), false).iterator();
               while(fileSetIter.hasNext()) {
                   cp+=(File.pathSeparator+((File)fileSetIter.next()).getAbsolutePath());
               }
           }
        } catch (IOException ioex) {
            throw new IASDeploymentException("IOException : " + ioex.getMessage() + 
                    " when trying to get list of files under " + dirName);
        }        
        return cp;
    }

    private String addModuleDirsToClassPath(String cp, String dirName)  {
         File dir = new File(dirName);
         if (dir.exists()) {
             cp+=File.pathSeparator+ dir.getAbsolutePath();

         }

         return cp;
    }
    
    private String getWsgenClassPath(File classesDir, String webinfLibDir,
            String appLibDirPath, String moduleDir, Application app) throws IASDeploymentException {
        // First thing in the classpath is modules' classes directory
        String classpath = classesDir.getAbsolutePath();
        
        // Next add the Jar files in WEB-INF/lib, if any
        if(webinfLibDir != null) {
            classpath = addJarsToClassPath(classpath, webinfLibDir);
        }
        
        // Next add the jar files in the EAR level lib directory
        if(appLibDirPath != null) {
            classpath = addJarsToClassPath(classpath, appLibDirPath);
            classpath = addJarsToClassPath(classpath,new File(appLibDirPath).getParentFile().getAbsolutePath());
        }

        //Add expanded modules to classpath
        for (Iterator modules = app.getModule(); modules.hasNext();) {

            ModuleDescriptor md = (ModuleDescriptor) modules.next();

            String moduleUri = md.getArchiveUri();
            String parentPath = new File(appLibDirPath).getParentFile(
                ).getAbsolutePath();
            String moduleRoot = DeploymentUtils.getEmbeddedModulePath(
                parentPath, moduleUri);

            classpath = addModuleDirsToClassPath(classpath,moduleRoot);             
        }
        
        // Now add the classpath elements in the modules Manifest entries
        FileInputStream is = null;
        try {
            File mfFile = new File(moduleDir+File.separator+"META-INF"+
                    File.separator+"MANIFEST.MF");
            if(mfFile.exists()) {
                is = new FileInputStream(mfFile);
                Manifest ms = new Manifest(is);
                Attributes attrMap = ms.getMainAttributes();
                String mfCp = attrMap.getValue(Attributes.Name.CLASS_PATH);
                if(mfCp != null && mfCp.length() != 0) {
                    StringTokenizer strTok = new StringTokenizer(mfCp, " \t");
                    while(strTok.hasMoreTokens()) {
                        String givenCP = strTok.nextToken();
                        // Append moduleDir to all relative classPaths
                        if(!givenCP.startsWith(File.separator)) {
                            //Fix for 2629
                            // Based on J2EE spec the referenced jars 
                            // in Classpath
                            // must be relative to the referencing jars
                            givenCP = new File(moduleDir).getParent()+File.separator+givenCP;
                        }
                        classpath+=(File.pathSeparator+givenCP);
                    }
                }
            }
        } catch (Exception e) {
            throw new IASDeploymentException("Exception : " + e.getMessage() + 
                    " when trying to process MANIFEST file under " + moduleDir);            
        } finally {
            if(is != null) {
                try {
                    is.close();
                } catch(IOException t) {}
            }
        }
        return classpath;
    }
    
    *//**
     * Creates missing wsdl from SEI/SIB declared in the deployment descriptors or 
     * through the @WebService annotation
     *//*
    public void genWSInfo(Application app, DeploymentRequest request) throws IASDeploymentException {

        Set<BundleDescriptor> bundles = app.getBundleDescriptors();
        for(BundleDescriptor bundle : bundles) {
            WebServicesDescriptor wsDesc = bundle.getWebServices();
            for (WebService ws : wsDesc.getWebServices()) {

                // for modules this is domains/<domain-name>/j2ee-modules/<module-name>
                // for apps this is domains/<domain-name>/j2ee-apps/<app-name>/<foo_war> (in case of embedded wars)
                //  or domains/<domain-name>/j2ee-apps/<app-name>/<foo_jar> (in case of embedded jars)
                File moduleDir = request.getDeployedDirectory();
                File wsdlDir = request.getGeneratedXMLDirectory();
                File stubsDir = request.getStubsDirectory();
                
                if(request.isApplication()) {
                    String subDirName = DeploymentUtils.getRelativeEmbeddedModulePath(moduleDir.getAbsolutePath(), bundle.getModuleDescriptor().getArchiveUri());
                    //moduleDir = new File(moduleDir, subDirName.replaceAll("\\.", "_"));
                    moduleDir =new File(moduleDir, subDirName);
                    wsdlDir =new File( wsdlDir,subDirName);
                  //  wsdlDir = new File(wsdlDir, subDirName.replaceAll("\\.",  "_"));
                }
                File classesDir;
                String webinfLibDir = null;
                if (XModuleType.WAR.equals(bundle.getModuleType())) {
                    classesDir = new File(moduleDir, "WEB-INF"+File.separator+"classes");                    
                    webinfLibDir = moduleDir.getAbsolutePath() + File.separator + "WEB-INF"+File.separator+"lib";
                } else if (XModuleType.EJB.equals(bundle.getModuleType())) {
                    classesDir = moduleDir;
                } else {
                    // unknown module type with @WebService, just ignore...
                    continue;
                }
                wsdlDir = new File(wsdlDir, bundle.getWsdlDir().replaceAll("/", "\\"+File.separator));

                // Check if catalog file is present, if so get mapped WSDLs
                String wsdlFileUri;
                File wsdlFile;
                checkCatalog(bundle, ws, moduleDir);
                if (ws.hasWsdlFile()) {
                    // If wsdl file is an http URL, download that WSDL and all embedded relative wsdls, schemas
                    if (ws.getWsdlFileUri().startsWith("http")) {
                        try {
                            downloadWsdlsAndSchemas(ws, new URL(ws.getWsdlFileUri()), wsdlDir);                                                
                        } catch(Exception e) {
                            throw new IASDeploymentException(e.toString(), e);
                        }
                        wsdlFileUri = ws.getWsdlFileUri().substring(ws.getWsdlFileUri().lastIndexOf("/")+1);
                        wsdlFile = new File(wsdlDir, wsdlFileUri);
                        
                        // at this point, we don't care we got it from and it simplifies 
                        // the rest of the deployment process to just think that is was 
                        // generated during deployment
                        // ws.setWsdlFileUri(null);
                    
                    } else {
                        wsdlFileUri = ws.getWsdlFileUri();                        
                        if(wsdlFileUri.startsWith("/")) {
                            wsdlFile = new File(wsdlFileUri);     
                        } else {
                            wsdlFile = new File(moduleDir, wsdlFileUri);     
                        }
                        if (!wsdlFile.exists()) {
                            throw new IASDeploymentException("WebService " + ws.getName() + " wsdl file " 
                                    + ws.getWsdlFileUri() + " not found in archive " 
                                    + bundle.getModuleDescriptor().getArchiveUri());
                        }
                    }

                } else {
                    //make required dirs in case they are not present
                    wsdlFileUri = JAXBRIContext.mangleNameToClassName(ws.getName()) + ".wsdl";   
                    wsdlDir.mkdirs();
                    wsdlFile = new File(wsdlDir, wsdlFileUri);                    
                }
                for (WebServiceEndpoint endpoint : ws.getEndpoints()) {

                    String implClassName;
                    boolean jaxwsEndPtFound = false;
                    boolean jaxrpcEndPtFound = false;
                    if (endpoint.implementedByEjbComponent()) {
                        implClassName = endpoint.getEjbComponentImpl().getEjbClassName();
                    } else {
                        implClassName = endpoint.getWebComponentImpl().getWebComponentImplementation();
                    }
                    
                    // check this is NOT a provider interface
                    Class implClass;
                    try {
                        implClass = app.getClassLoader().loadClass(implClassName);
                    } catch(Exception e) {
                            throw new IASDeploymentException("WebService " + ws.getName() + "implementation " 
                                    + implClassName + " not found in archive " 
                                    + bundle.getModuleDescriptor().getArchiveUri());                        
                    }
                    
                    if (implClass!=null) {
                        if(implClass.getAnnotation(javax.xml.ws.WebServiceProvider.class) != null) {
                            // if we already found a jaxrpcendpoint, flag error since we do not support jaxws+jaxrpc endpoint
                            // in the same service
                            if(jaxrpcEndPtFound) {
                                throw new IASDeploymentException("WebService " + ws.getName() + 
                                        "has a JAXWS and a JAXRPC endpoint; this is not supported now");
                            }
                            //This is a JAXWS endpoint with @WebServiceProvider
                            //Do not run wsgen for this endpoint
                            jaxwsEndPtFound = true;
                            continue;
                        }
                        if(implClass.getAnnotation(javax.jws.WebService.class) != null) {
                    
                            // if we already found a jaxrpcendpoint, flag error since we do not support jaxws+jaxrpc endpoint
                            // in the same service
                            if(jaxrpcEndPtFound) {
                                throw new IASDeploymentException("WebService " + ws.getName() + 
                                        "has a JAXWS and a JAXRPC endpoint; this is not supported now");
                            }
                            // This is a JAXWS endpoint with @WebService; Invoke wsgen
                            jaxwsEndPtFound = true;
                            String wsgenClassPath = getWsgenClassPath(classesDir, webinfLibDir, 
                                request.getDeployedDirectory().getAbsolutePath()+File.separator+app.getLibraryDirectory(), 
                                moduleDir.getAbsolutePath(),app);
                            boolean wsgenDone =
                                runWsGen(implClassName, wsdlFile.exists(), wsgenClassPath, 
                                    stubsDir, wsdlDir, endpoint.getServiceName(), endpoint.getWsdlPort());
                            if(!wsgenDone) {
                                // wsgen failed; if WSDL file were present, just throw a warning
                                // assuming that the user would have packaged everything
                                if(!wsdlFile.exists()) {
                                    throw new IASDeploymentException("WSGEN FAILED");
                                } else {
                                    logger.log(Level.WARNING,
                                        "wsgen failed - proceeding under the assumption that the user packaged all required objects properly");
                                }
                            }
                            try {
                                endpoint.getWebService().setWsdlFileUrl(wsdlFile.toURI().toURL());
                            } catch(java.net.MalformedURLException mue) {
                                throw new IASDeploymentException("WSGEN Failed");
                            }
                            logger.log(Level.INFO, "wsgen successful");
                        } else {
                            // this is a jaxrpc endpoint
                            // if we already found a jaxws endpoint, flag error since we do not support jaxws+jaxrpc endpoint
                            // in the same service
                            if(jaxwsEndPtFound) {
                                throw new IASDeploymentException("WebService " + ws.getName() + 
                                        "has a JAXWS and a JAXRPC endpoint; this is not supported now");
                            }
                            // Set spec version to 1.1 to indicate later the wscompile should be run
                            // We do this here so that jaxrpc endpoint having J2EE1.4 or JavaEE5
                            // descriptors will work properly
                            jaxrpcEndPtFound = true;
                            ws.getWebServicesDescriptor().setSpecVersion("1.1");
                        }
                    }
                }
            }
        }
    }
*/
   /* public URL resolveCatalog(File catalogFile, String wsdlFile, WebService ws) throws IASDeploymentException {
        
        try {


            URL retVal = null;
            // Get an entity resolver
            org.xml.sax.EntityResolver resolver = 
                    XmlUtil.createEntityResolver(catalogFile.toURL());
            org.xml.sax.InputSource source = resolver.resolveEntity(null, wsdlFile);
            if(source != null) {
                String mappedEntry = source.getSystemId();
                // For entries with relative paths, Entity resolver always
                // return file://<absolute path
                if(mappedEntry.startsWith("file:")) {
                    File f = new File(mappedEntry.substring(mappedEntry.indexOf(":")+1));
                    if(!f.exists()) {
                        throw new IASDeploymentException("File " + mappedEntry + " not found");
                    }
                    retVal = f.toURI().toURL();
                    if(ws != null) {
                        ws.setWsdlFileUri(f.getAbsolutePath());
                        ws.setWsdlFileUrl(retVal);
                    }
                } else if(mappedEntry.startsWith("http")) {
                    retVal = new URL(mappedEntry);
                    if(ws != null) {
                        ws.setWsdlFileUrl(retVal);
                    }
                }
            }
            return retVal;

        } catch (Throwable t) {
            throw new IASDeploymentException("Exception while processing catalog " 
                    + catalogFile.getAbsolutePath() + "; Reason " + t.getMessage());
        }        
    
    }
    
    // If catalog file is present, get the mapped WSDL for given WSDL and replace the value in
    // the given WebService object
    private void checkCatalog(BundleDescriptor bundle, WebService ws, File moduleDir) 
                            throws IASDeploymentException {
        // If no catalog file is present, return
        File catalogFile = new File(moduleDir,
                bundle.getDeploymentDescriptorDir() +
                File.separator + "jax-ws-catalog.xml");
        if(!catalogFile.exists()) {
            return;
        }
        resolveCatalog(catalogFile, ws.getWsdlFileUri(), ws);
    }
    
    public static Class generateAndLoad(ClassGeneratorFactory cgf,
                                         ClassLoader loader) {

        cgf.evaluate();

        Properties props = new Properties();
        if( logger.isLoggable(Level.FINE) ) {

            props.put(Wrapper.DUMP_AFTER_SETUP_VISITOR, "true");
            props.put(Wrapper.TRACE_BYTE_CODE_GENERATION, "true");
            props.put(Wrapper.USE_ASM_VERIFIER, "true");

            try {

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PrintStream ps = new PrintStream(baos);

                Wrapper._sourceCode(ps, props);
                logger.log(Level.FINE,baos.toString());

            } catch(Exception e) {
                logger.log(Level.FINE, "exception generating src", e);
            }

        }

        return Wrapper._generate(loader, null, props);

    }
    */
    /*
     * Calls @PostConstruct method in the implementor
     */
    public void doPostConstruct(Class impl, Object implObj) {
        invokeServiceMethod(javax.annotation.PostConstruct.class, impl,
                implObj);
    }
    
    /*
     * Calls @PreDestroy method in the implementor
     */
    public void doPreDestroy(WebServiceEndpoint ep, ClassLoader loader) {
        // Call @PreDestroy in endpoint, if any
        try {
            Class impl = Class.forName(ep.getServletImplClass(),
                                true, loader);
            invokeServiceMethod(javax.annotation.PreDestroy.class, impl,
                    impl.newInstance());
        } catch (Throwable ex) {
            logger.log(Level.SEVERE, "Class " + ep.getServletImplClass() +
                            "not found during PreDestroy processing", ex);
        }
                                                                                
        // Call @PreDestroy in the handlers if any
        if(!ep.hasHandlerChain()) {
            return;
        }
        for(Iterator<WebServiceHandlerChain> hc = ep.getHandlerChain().iterator();
                                hc.hasNext();) {
            WebServiceHandlerChain thisHc = hc.next();
            for(Iterator<WebServiceHandler> h = thisHc.getHandlers().iterator(); 
                                    h.hasNext();) {
                WebServiceHandler thisHandler = h.next();
                try {
                    Class handlerClass = Class.forName(thisHandler.getHandlerClass(),
                                            true, loader);
                    invokeServiceMethod(javax.annotation.PreDestroy.class, handlerClass,
                            handlerClass.newInstance());
                } catch (Throwable ex) {
                    logger.log(Level.SEVERE, "Handler class " + thisHandler.getHandlerClass() +
                                    "not found during PreDestroy processing", ex);
                }
            }
        }        
    }

    
    /*
     * Calls the PostConstruct / PreDestroy method
     */
    private void invokeServiceMethod(Class annType, Class impl, final Object implObj) {
        Method[] methods = impl.getDeclaredMethods();
        // Only one method can have @PostConstruct / @PreDestroy
        // Call the first method with this annotation and return
        for(final Method method : methods) {
            if (method.getAnnotation(annType) != null) {
                try {
                    AccessController.doPrivileged(new PrivilegedExceptionAction() {
                        public Object run() throws IllegalAccessException,
                                InvocationTargetException {
                            if (!method.isAccessible()) {
                                method.setAccessible(true);
                            }
                            method.invoke(implObj, new Object[]{});
                            return null;
                        }
                    });
                } catch(Throwable e) {
                    // Should we log or throw an exception
                    logger.log(Level.SEVERE, 
                            "Failure while calling PostConstruct/PreDestroy method", e);
                }
                break;
            } 
        }
    }
    
    private boolean matchQNamePatterns(QName cfgQName, QName givenPattern) {
        if (givenPattern.getNamespaceURI().equals(cfgQName.getNamespaceURI())) {
            String expr = givenPattern.getLocalPart().replaceAll("\\*",  ".*");
            return java.util.regex.Pattern.matches(expr, cfgQName.getLocalPart());
        }
        return false;        
    }

    private boolean patternsMatch(WebServiceHandlerChain hc, QName svcName,
                                    QName portName, String bindingId) {
        // If service name pattern in handler chain does not match the service name
        // for this endpoint, skip handler processing
        if(hc.getServiceNamePattern() != null && svcName != null) {
            QName svcPattern = QName.valueOf(hc.getServiceNamePattern());
            if(!matchQNamePatterns(svcName, svcPattern)) {
                return false;
            }
        }

        // If port name pattern in handler chain does not match the port name
        // for this endpoint, skip handler processing
        if(hc.getPortNamePattern() != null && portName != null) {
            QName portPattern = QName.valueOf(hc.getPortNamePattern());
            if(!matchQNamePatterns(portName, portPattern)) {
                return false;
            }
        }

        // Check if the binding protocol for this endpoint is present
        // as part of the protocol-bindings list
        if(hc.getProtocolBindings() != null && bindingId != null) {
            String givenBindings = hc.getProtocolBindings();
            if( (bindingId.equals(HTTPBinding.HTTP_BINDING)) &&
                ((givenBindings.indexOf(HTTPBinding.HTTP_BINDING) != -1) ||
                 (givenBindings.indexOf(WebServiceEndpoint.XML_TOKEN) != -1))) {
                return true;
            }
            if( (bindingId.equals(SOAPBinding.SOAP11HTTP_BINDING)) &&
                ((givenBindings.indexOf(SOAPBinding.SOAP11HTTP_BINDING) != -1) ||
                 (givenBindings.indexOf(WebServiceEndpoint.SOAP11_TOKEN) != -1))) {
                return true;
            }
            if( (bindingId.equals(SOAPBinding.SOAP12HTTP_BINDING)) &&
                ((givenBindings.indexOf(SOAPBinding.SOAP12HTTP_BINDING) != -1) ||
                 (givenBindings.indexOf(WebServiceEndpoint.SOAP12_TOKEN) != -1))) {
                return true;
            }
            if( (bindingId.equals(SOAPBinding.SOAP11HTTP_MTOM_BINDING)) &&
                ((givenBindings.indexOf(SOAPBinding.SOAP11HTTP_MTOM_BINDING) != -1) ||
                 (givenBindings.indexOf(WebServiceEndpoint.SOAP11_MTOM_TOKEN) != -1))) {
                return true;
            }
            if( (bindingId.equals(SOAPBinding.SOAP12HTTP_MTOM_BINDING)) &&
                ((givenBindings.indexOf(SOAPBinding.SOAP12HTTP_MTOM_BINDING) != -1) ||
                 (givenBindings.indexOf(WebServiceEndpoint.SOAP12_MTOM_TOKEN) != -1))) {
                return true;
            }
        }
        return true;
    }
     
    private List<Handler> processConfiguredHandlers(List<WebServiceHandler> handlersList, Set<String> roles) {
        List<Handler> handlerChain = new ArrayList<Handler>();
        for(WebServiceHandler h : handlersList) {
            Handler handler = null;
            Map<String, Object> params = new HashMap<String, Object>();
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            // Get Handler Class instance
            Class handlerClass;
            try {
                handlerClass = Class.forName(h.getHandlerClass(), true, loader);
                handler = (Handler)handlerClass.newInstance();
            } catch (Throwable t) {
                logger.log(Level.SEVERE, "Unable to load handler class " + 
                        h.getHandlerClass(),t);
                
                continue;
            }
            
            // perform injection
            try {
                
                WebServiceContractImpl wscImpl = WebServiceContractImpl.getInstance();
                InjectionManager injManager = wscImpl.getHabitat().getByContract(InjectionManager.class);
                injManager.injectInstance(handler);
            } catch(InjectionException e) {
                logger.severe("Handler " + h.getHandlerClass() + 
                            " instance injection failed : " + e.getMessage());
                continue;
            }
            
            // Call @PostConstruct if any
            doPostConstruct(handlerClass, handler);
            
            // Add soap-roles
            Collection<String> rolesColl = h.getSoapRoles();
            roles.addAll(rolesColl);
            
            // Add this handler to the mail list
            handlerChain.add(handler);
        }
        return handlerChain;
    }            
    
    public void configureJAXWSServiceHandlers(WebServiceEndpoint ep, 
            String bindingId, WSBinding bindingObj) {
        // No handler chains; do nothing
        if(!ep.hasHandlerChain()) {
            return;
        }
        LinkedList handlerChainList = ep.getHandlerChain();
        List<Handler> finalHandlerList = new ArrayList<Handler>();
        Set<String> roles = new HashSet();
        for(Iterator<WebServiceHandlerChain> i = handlerChainList.iterator(); i.hasNext();) {
            WebServiceHandlerChain hc = i.next();
            // Apply the serviceName / portName / bindings filter to ensure
            // that the handlers are for this endpoint
            if(!patternsMatch(hc, ep.getServiceName(), ep.getWsdlPort(), bindingId)) {
                continue;
            }
            // OK - this handler has to be configured for this endpoint
            // Iterate through all handlers that have been configured
            List<Handler> handlerInfo = processConfiguredHandlers(hc.getHandlers(), roles);
            finalHandlerList.addAll(handlerInfo);
        }
        // Processing of all handlers over; 
        // set final list of handler in RuntimeEndpointInfo
        bindingObj.setHandlerChain(finalHandlerList);
        // Set soap roles for soap bindings only
        if(bindingObj instanceof javax.xml.ws.soap.SOAPBinding) {
            ((javax.xml.ws.soap.SOAPBinding)bindingObj).setRoles(roles);
        }        
    }

    public void configureJAXWSClientHandlers(javax.xml.ws.Service svcClass, ServiceReferenceDescriptor desc) {

        // Create a resolver and get all ports for the Service

        HandlerResolverImpl resolver = new HandlerResolverImpl();

        Set<String> roles = new HashSet();
        
        Iterator<QName> ports = svcClass.getPorts();

        // Set handler chain for each port of this service
        while(ports.hasNext()) {
            QName nextPort = ports.next();
            LinkedList handlerChainList = desc.getHandlerChain();
            for(Iterator<WebServiceHandlerChain> i = handlerChainList.iterator(); i.hasNext();) {
                WebServiceHandlerChain hc = i.next();
                // Apply the serviceName / portName filter to ensure
                // that the handlers are for this service and this port
                if(!patternsMatch(hc, desc.getServiceName(), nextPort, null)) {
                    continue;
                }
                // Decide for what all protocols this handler should be applied
                ArrayList<String> protocols = new ArrayList<String>();
                if(hc.getProtocolBindings() == null) {
                    // No protocol bindings given in descriptor; apply this handler
                    // for all protocols
                    protocols.add(HTTPBinding.HTTP_BINDING);
                    protocols.add(SOAPBinding.SOAP11HTTP_BINDING);
                    protocols.add(SOAPBinding.SOAP12HTTP_BINDING);
                    protocols.add(SOAPBinding.SOAP11HTTP_MTOM_BINDING);
                    protocols.add(SOAPBinding.SOAP12HTTP_MTOM_BINDING);
                } else {
                    // protocols specified; handlers are for only these protocols
                    String specifiedProtocols = hc.getProtocolBindings();
                    if((specifiedProtocols.indexOf(HTTPBinding.HTTP_BINDING) != -1) ||
                         (specifiedProtocols.indexOf(WebServiceEndpoint.XML_TOKEN) != -1)) {
                        protocols.add(HTTPBinding.HTTP_BINDING);
                    }
                    if((specifiedProtocols.indexOf(SOAPBinding.SOAP11HTTP_BINDING) != -1) ||
                         (specifiedProtocols.indexOf(WebServiceEndpoint.SOAP11_TOKEN) != -1)) {
                        protocols.add(SOAPBinding.SOAP11HTTP_BINDING);
                    }
                    if((specifiedProtocols.indexOf(SOAPBinding.SOAP12HTTP_BINDING) != -1) ||
                         (specifiedProtocols.indexOf(WebServiceEndpoint.SOAP12_TOKEN) != -1)) {
                        protocols.add(SOAPBinding.SOAP12HTTP_BINDING);
                    }                    
                    if((specifiedProtocols.indexOf(SOAPBinding.SOAP11HTTP_MTOM_BINDING) != -1) ||
                         (specifiedProtocols.indexOf(WebServiceEndpoint.SOAP11_MTOM_TOKEN) != -1)) {
                        protocols.add(SOAPBinding.SOAP11HTTP_MTOM_BINDING);
                    }
                    if((specifiedProtocols.indexOf(SOAPBinding.SOAP12HTTP_MTOM_BINDING) != -1) ||
                         (specifiedProtocols.indexOf(WebServiceEndpoint.SOAP12_MTOM_TOKEN) != -1)) {
                        protocols.add(SOAPBinding.SOAP12HTTP_MTOM_BINDING);
                    }                    
                }
                // Iterate through all handlers that have been configured
                List<WebServiceHandler> handlersList = hc.getHandlers();
                // From this list, remove those handlers that have port-name that is different
                // than the current port
                for(WebServiceHandler thisOne : handlersList) {
                    Collection portNames = thisOne.getPortNames();
                    if(!portNames.isEmpty() && 
                        !portNames.contains(nextPort.getLocalPart())) {
                        handlersList.remove(thisOne);
                    }
                }
                // Now you have the handlers that need to be added; process them
                List<Handler> handlerInfo = processConfiguredHandlers(handlersList, roles);
                // Now you have the handler list; Set it in resolver;
                // one set for each protocol
                for(Iterator<String> s = protocols.iterator(); s.hasNext();) {
                    javax.xml.ws.handler.PortInfo portInfo;
                    portInfo = new PortInfoImpl(BindingID.parse(s.next()),
                                    nextPort, desc.getServiceName());
                    resolver.setHandlerChain(portInfo, handlerInfo);
                }
            }
        }
        // Now that processing of all handlers is over, set HandlerResolver for 
	// the service

        svcClass.setHandlerResolver(resolver);
        
        //XXX TODO : What to do with soap roles on client side ?
    }

    /* This util is to implement the jaxws table that defines how MTOM is set
    *  
    *   BindingType                        -  enable-mtom in DD  - final MTOM value
    *
    *   SOAPXX_BINDING                            none                        false
    *   SOAPXX_BINDING                            false                         false
    *   SOAPXX_BINDING                            true                          true
    *   SOAPXX_MTOM_BINDING               none                         true
    *   SOAPXX_MTOM_BINDING               false                         false
    *   SOAPXX_MTOM_BINDING               true                          true
    */
    public boolean getMtom(WebServiceEndpoint ep) {
        String currentBinding = ep.getProtocolBinding();
        if((ep.getMtomEnabled() == null) &&
            (SOAPBinding.SOAP11HTTP_MTOM_BINDING.equals(currentBinding) ||
             SOAPBinding.SOAP12HTTP_MTOM_BINDING.equals(currentBinding))) {
            return true;
        }
        if((new Boolean(ep.getMtomEnabled())).booleanValue() && 
            (SOAPBinding.SOAP11HTTP_BINDING.equals(currentBinding) ||
             SOAPBinding.SOAP12HTTP_BINDING.equals(currentBinding) ||
             SOAPBinding.SOAP11HTTP_MTOM_BINDING.equals(currentBinding) ||
             SOAPBinding.SOAP12HTTP_MTOM_BINDING.equals(currentBinding))) {
            return true;
        }
        return false;
    }
    
   /* // Used by the message layer security subsystem to get an efficient
    // representation of the contained message from the SOAPMessageContext. 
    // when called by jaxrpc Hander or SystemHandlerDelegate
    public static SOAPMessage getMessage
    (javax.xml.rpc.handler.soap.SOAPMessageContext c) {
	// TODO
	return c.getMessage();
    }

    private static boolean readProperty = true;
    private static ExpressMessageFactoryImpl soapMsgFactory = null;
    private static final String EXPRESS_MSG_FACTORY = 
        "com.sun.enterprise.webservice.expressMsgFactory";

    // when called by jaxws Hander or SystemHandlerDelegate
    // 2nd parameter should be true if you don't need to   
    // peek into the message for operationName
    private static synchronized SOAPMessage getMessage
    (javax.xml.ws.handler.soap.SOAPMessageContext c, boolean nameKnown) {

	SOAPMessage message = null;
	
	// handle else case, and try to avoid returning null message
	if (message == null) {
	    message = c.getMessage();
	}
	
	return message;
    }
    
    // when called by jaxws Hander or SystemHandlerDelegate
    public static synchronized SOAPMessage getMessage
    (javax.xml.ws.handler.soap.SOAPMessageContext c) {
	return getMessage(c,true);
    }

    // when called by jaxws Hander or SystemHandlerDelegate and when
    // the operation name must be available from the message
    public static synchronized SOAPMessage getMessageWithName
    (javax.xml.ws.handler.soap.SOAPMessageContext c) {
	return getMessage(c,false);
    }
    
    *//**
         * Checks to see whether WS-Security is enabled or not.
         *
         * @param policyMap policy map for {@link this} assembler
         * @param wsdlPort wsdl:port
         * @return true if Security is enabled, false otherwise
         *//*
    //TODO - this code has been copied from PipelineAssemblerFactoryImpl.java and needs
    //to be maintained in both places.  In the future, code needs to be moved somewhere
    //where it can be invoked from both places.
    public static  boolean isSecurityEnabled(PolicyMap policyMap, WSDLPort wsdlPort) {
        if (policyMap == null || wsdlPort == null)
            return false;

        try {
            PolicyMapKey endpointKey = policyMap.createWsdlEndpointScopeKey(wsdlPort.getOwner().getName(),
                    wsdlPort.getName());
            Policy policy = policyMap.getEndpointEffectivePolicy(endpointKey);

            if ((policy != null) && policy.contains(SECURITY_POLICY_NAMESPACE_URI)) {
                return true;
            }

            for (WSDLBoundOperation wbo : wsdlPort.getBinding().getBindingOperations()) {
                PolicyMapKey operationKey = policyMap.createWsdlOperationScopeKey(wsdlPort.getOwner().getName(),
                        wsdlPort.getName(),
                        wbo.getName());
                policy = policyMap.getOperationEffectivePolicy(operationKey);
                if ((policy != null) && policy.contains(SECURITY_POLICY_NAMESPACE_URI))
                    return true;

                policy = policyMap.getInputMessageEffectivePolicy(operationKey);
                if ((policy != null) && policy.contains(SECURITY_POLICY_NAMESPACE_URI))
                    return true;

                policy = policyMap.getOutputMessageEffectivePolicy(operationKey);
                if ((policy != null) && policy.contains(SECURITY_POLICY_NAMESPACE_URI))
                    return true;

                policy = policyMap.getFaultMessageEffectivePolicy(operationKey);
                if ((policy != null) && policy.contains(SECURITY_POLICY_NAMESPACE_URI))
                    return true;
            }
        } catch (PolicyException e) {
            return false;
        }

        return false;
    }*/

}






