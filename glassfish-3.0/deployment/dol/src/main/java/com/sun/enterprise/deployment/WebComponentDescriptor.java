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
package com.sun.enterprise.deployment;

import com.sun.enterprise.util.LocalStringManagerImpl;
import com.sun.enterprise.deployment.web.InitializationParameter;
import com.sun.enterprise.deployment.web.MultipartConfig;
import com.sun.enterprise.deployment.web.SecurityRoleReference;

import java.lang.reflect.Modifier;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Common data and behavior of the deployment
 * information about a JSP or JavaServlet in J2EE.
 *
 * @author Jerome Dochez
 */
public class WebComponentDescriptor extends Descriptor {

    private static LocalStringManagerImpl localStrings =
            new LocalStringManagerImpl(WebComponentDescriptor.class);

    /**
     * Constant for Basic authentication.
     */
    public static final String BASIC_AUTHENTICATION = "basic";
    /**
     * Constant for Form authentication.
     */
    public static final String FORM_AUTHENTICATION = "form";
    /**
     * Constant for Secure authentication.
     */
    public static final String SSL_AUTHENTICATION = "ssl";
    /**
     * Constant for the htpp GET method.
     */
    public static final String GET = "GET";
    /**
     * Constant for the http PUT method.
     */
    public static final String PUT = "PUT";
    /**
     * Constant for the http POST method.
     */
    public static final String POST = "POST";
    /**
     * Constant for the http DELET method.
     */
    public static final String DELETE = "DELETE";

    private Set<InitializationParameter> initializationParameters;
    private Set<String> urlPatterns;
    private String canonicalName;
    private Integer loadOnStartUp = null;
    private Set<SecurityRoleReference> securityRoleReferences;

    private RunAsIdentityDescriptor runAs;
    private WebBundleDescriptor webBundleDescriptor = null;
    private boolean enabled = true;
    private Boolean asyncSupported = null;
    private MultipartConfig multipartConfig = null;
    private List<Method> httpMethods = null;

    /**
     * The default constructor.
     */

    public WebComponentDescriptor() {
    }

    /**
     * The copy constructor.
     */

    public WebComponentDescriptor(WebComponentDescriptor other) {
        setCanonicalName(other.getCanonicalName());
        setServlet(other.isServlet());
        setWebComponentImplementation(
            other.getWebComponentImplementation());
        getInitializationParameterSet().addAll(
            other.getInitializationParameterSet());
        getUrlPatternsSet().addAll(other.getUrlPatternsSet());
        setLoadOnStartUp(other.getLoadOnStartUp());
        getSecurityRoleReferenceSet().addAll(
            other.getSecurityRoleReferenceSet());
        setRunAsIdentity(other.getRunAsIdentity());
        setAsyncSupported(other.isAsyncSupported());
        setMultipartConfig(other.getMultipartConfig());
        setWebBundleDescriptor(other.getWebBundleDescriptor());
    }

    private Set<InitializationParameter> getInitializationParameterSet() {
        if (this.initializationParameters == null) {
            this.initializationParameters = new OrderedSet<InitializationParameter>();
        }
        return initializationParameters;
    }

    /**
     * @return the Set of servlet initialization parameters.
     */
    public Enumeration<InitializationParameter> getInitializationParameters() {
        return (new Vector(getInitializationParameterSet())).elements();
    }

    /**
     * @return a matching initialization parameter by its name if there is one.
     */
    public InitializationParameter getInitializationParameterByName(String name) {
        for (InitializationParameter next : getInitializationParameterSet()) {
            if (next.getName().equals(name)) {
                return next;
            }
        }
        return null;
    }

    /**
     * Adds a servlet initialization parameter to this component.
     */
    public void addInitializationParameter(InitializationParameter initializationParameter) {
        getInitializationParameterSet().add(initializationParameter);
    }

    /**
     * Removes the given servlet initialization parameter from this component.
     */
    public void removeInitializationParameter(InitializationParameter initializationParameter) {
        getInitializationParameterSet().remove(initializationParameter);
    }

    /**
     * @return the set of URL pattern aliases for this component.
     */
    public Set<String> getUrlPatternsSet() {
        if (urlPatterns == null) {
            urlPatterns = new OrderedSet<String>() {
                @Override
                public boolean add(String s) {
                    Map<String, String> up2sname = getUrlPatternToServletNameMap();
                    if (up2sname != null) {
                        String name = getCanonicalName();
                        String oldName = up2sname.put(s, getCanonicalName());
                        if (oldName != null && (!oldName.equals(name))) {
                            throw new IllegalArgumentException(localStrings.getLocalString(
                                "enterprise.deployment.exceptionsameurlpattern",
                                "Servlet [{0}] and Servlet [{1}] have the same url pattern: [{2}]",
                                new Object[] { oldName, name, s }));
                        }
                    }
                    return super.add(s);
                }

                @Override
                public boolean remove(Object o) {
                    boolean result = super.remove(o);
                    if (getWebBundleDescriptor() != null) {
                        getWebBundleDescriptor().resetUrlPatternToServletNameMap();
                    }
                    return result;
                }

                private Map<String, String> getUrlPatternToServletNameMap() {
                    return ((getWebBundleDescriptor() != null) ?
                            getWebBundleDescriptor().getUrlPatternToServletNameMap() :
                            null);
                }
            };
        }
        return urlPatterns;
    }

    /**
     * @return an enumeration of (String) URL pattern aliases for this component.
     */
    public Enumeration<String> getUrlPatterns() {
        return (new Vector(getUrlPatternsSet())).elements();
    }

    /**
     * Adds an alias to this web component.
     */
    public void addUrlPattern(String urlPattern) {
        getUrlPatternsSet().add(urlPattern);
    }

    /**
     * Removes a URL pattern from this web component.
     */
    public void removeUrlPattern(String urlPattern) {
        getUrlPatternsSet().remove(urlPattern);
    }

    void setWebBundleDescriptor(WebBundleDescriptor webBundleDescriptor) {
        this.webBundleDescriptor = webBundleDescriptor;
    }

    /**
     * @return the web app object to which I belong or null
     */
    public WebBundleDescriptor getWebBundleDescriptor() {
        return webBundleDescriptor;
    }

    /**
     * The canonical name for the web component.
     */
    public String getCanonicalName() {
        if (canonicalName == null) {
            canonicalName = getName();
        }
        return canonicalName;
    }

    /**
     * Sets the canonical name of this web component.
     */
    public void setCanonicalName(String canonicalName) {
        this.canonicalName = canonicalName;
    }

    /**
     * @return the order on which this component will be loaded by the web server.
     */
    public Integer getLoadOnStartUp() {
        return loadOnStartUp;
    }

    /**
     * Sets the order on which this component will be loaded by the web server.
     */
    public void setLoadOnStartUp(Integer loadOnStartUp) {
        this.loadOnStartUp = loadOnStartUp;
    }

    /**
     * Sets the order on which this component will be loaded by the web server.
     */
    public void setLoadOnStartUp(String loadOnStartUp) throws NumberFormatException {
        this.loadOnStartUp = Integer.decode(loadOnStartUp);
    }

    Set<SecurityRoleReference> getSecurityRoleReferenceSet() {
        if (this.securityRoleReferences == null) {
            this.securityRoleReferences = new OrderedSet();
        }
        return securityRoleReferences;
    }

    /**
     * @return the Set of security role references that I have.
     */
    public Enumeration<SecurityRoleReference> getSecurityRoleReferences() {
        return (new Vector(this.getSecurityRoleReferenceSet())).elements();
    }

    /**
     * @return a matching role reference by name or null if there is none matching.
     */
    public SecurityRoleReference getSecurityRoleReferenceByName(String roleReferenceName) {
        for (SecurityRoleReference nextRR : getSecurityRoleReferenceSet()) {
            if (nextRR.getRolename().equals(roleReferenceName)) {
                return nextRR;
            }
        }
        return null;
    }

    /**
     * Adds a security role reference to this web component.
     */
    public void addSecurityRoleReference(SecurityRoleReference securityRoleReference) {
        getSecurityRoleReferenceSet().add(securityRoleReference);
    }

    /**
     * Removes the given security role reference from this web component.
     */
    public void removeSecurityRoleReference(SecurityRoleReference securityRoleReference) {
        getSecurityRoleReferenceSet().remove(securityRoleReference);
    }

    /**
     * Sets the run-as of the referee EJB.
     *
     * @param runAs the value of run-as
     */    public void setRunAsIdentity(RunAsIdentityDescriptor runAs) {
        if (this.runAs == null) {
            this.runAs = runAs;
        }
    }

    /**
     * Gets the run-as of the referee EJB.
     *
     * @return the value of run-as.
     */
    public RunAsIdentityDescriptor getRunAsIdentity() {
        return runAs;
    }

    public boolean getUsesCallerIdentity() {
        return (runAs == null);
    }

    public void setUsesCallerIdentity(boolean isCallerID) {
        if (isCallerID) {
            runAs = null;
        } else {
            runAs = new RunAsIdentityDescriptor("");
        }
    }

    public MultipartConfig getMultipartConfig() {
        return multipartConfig;
    }

    public void setMultipartConfig(MultipartConfig multipartConfig) {
        this.multipartConfig = multipartConfig;
    }

    /**
     * DeploymentDescriptorNode.addNodeDescriptor(node) need this.
     */
    public void setMultipartConfig(MultipartConfigDescriptor multipartConfigDesc) {
        this.multipartConfig = multipartConfigDesc;
    }

    public Application getApplication() {
        if (getWebBundleDescriptor() != null) {
            return getWebBundleDescriptor().getApplication();
        }
        return null;
    }

    /**
     * sets the implementation file for this web component, the
     * implementation file is either a servlet class name of a jsp
     * file name.
     *
     * @param implFile the servlet class name or the jsp file
     */
    public void setWebComponentImplementation(String implFile) {
        if (!isServlet && !implFile.startsWith("/")) {
            implFile = "/" + implFile;
        }
        this.implFile = implFile;
    }

    private String implFile = "";
    private boolean isServlet = false;

    public String getWebComponentImplementation() {
        return implFile;
    }

    public boolean isServlet() {
        return isServlet;
    }

    public void setServlet(boolean isServlet) {
        this.isServlet = isServlet;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setAsyncSupported(Boolean asyncSupported) {
        this.asyncSupported = asyncSupported;
    }

    public Boolean isAsyncSupported() {
        return asyncSupported;
    }

    /**
     * This method return an array of user defined http doDelete, doGet,
     * doHead, doOptions, doPost, doPut, doTrace methods.
     * It is used for processing web security annotations.
     * @return an array of methods.
     */
    public Method[] getUserDefinedHttpMethods() {
        if (httpMethods == null) {
            httpMethods = new ArrayList<Method>();

            if (isServlet) {
                List<String> searchingMethods = new ArrayList();
                String[] httpMString = new String[] { "doDelete", "doGet",
                        "doHead", "doOptions", "doPost", "doPut", "doTrace" };
                for (String s : httpMString) {
                    searchingMethods.add(s);
                }

                try {
                    Class implClass = Class.forName(implFile, true,
                        Thread.currentThread().getContextClassLoader()); 
                    Class clazz = implClass;
                    String packageName = null;
                    Package clazzPackage = implClass.getPackage();
                    if (clazzPackage != null) {
                        packageName = clazzPackage.getName();
                    }

                    // the processing is stopped at javax.servlet package as
                    // a) there is no user defined class there
                    // b) there is no security annotations used there
                    while (clazz != null && (!clazz.getName().startsWith("javax.servlet."))
                            && searchingMethods.size() > 0) {
                        Package p = clazz.getPackage();
                        Method[] methods = clazz.getDeclaredMethods(); 
                        for (Method m : methods) {
                            String methodName = m.getName();
                            if (searchingMethods.contains(methodName)) {
                                Class<?> returnType = m.getReturnType();
                                Class<?>[] parameterTypes = m.getParameterTypes();
                                int modifiers = m.getModifiers();
                                boolean isSamePackage = (p == null && clazzPackage == null) ||
                                    (p != null && clazzPackage != null &&
                                        packageName.equals(p.getName()));
                                boolean valid = (Modifier.isPublic(modifiers) ||
                                        Modifier.isProtected(modifiers) ||
                                        ((!Modifier.isPrivate(modifiers)) && isSamePackage));
                                valid = valid && (void.class.equals(returnType)) &&
                                    (parameterTypes.length == 2) &&
                                    (parameterTypes[0].equals(HttpServletRequest.class) &&
                                         parameterTypes[1].equals(HttpServletResponse.class));
                                if (valid) {
                                    httpMethods.add(m);
                                    searchingMethods.remove(methodName);
                                }

                                if (searchingMethods.size() == 0) {
                                    break;
                                }
                            }
                        }
                        clazz = clazz.getSuperclass();
                    }
                } catch(Throwable t) {
                    throw new IllegalStateException(t);
                }
            }
        }

        return httpMethods.toArray(new Method[httpMethods.size()]);
    }

    /* -----------
    */

    /**
     * A formatted string representing my state.
     */
    public void print(StringBuffer toStringBuffer) {
        super.print(toStringBuffer);
        toStringBuffer.append("WebComponentDescriptor\n");
        toStringBuffer.append("\n initializationParameters ").append(initializationParameters);
        toStringBuffer.append("\n urlPatterns ").append(urlPatterns);
        toStringBuffer.append("\n canonicalName ").append(canonicalName);
        toStringBuffer.append("\n loadOnStartUp ").append(loadOnStartUp);
        toStringBuffer.append("\n enabled ").append(enabled);
        toStringBuffer.append("\n asyncSupported ").append(asyncSupported);
        toStringBuffer.append("\n securityRoleReferences ").append(securityRoleReferences);
        toStringBuffer.append("\n multipartConfig ").append(multipartConfig);
        if (isServlet()) {
            toStringBuffer.append("\n servlet className ").append(getWebComponentImplementation());
        } else {
            toStringBuffer.append("\n jspFileName ").append(getWebComponentImplementation());
        }
    }


    public boolean equals(Object other) {
        if (other instanceof WebComponentDescriptor &&
                this.getCanonicalName().equals(((
                        WebComponentDescriptor) other).getCanonicalName())) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result = 17;
        result = 37 * result + getCanonicalName().hashCode();
        return result;
    }


    // this method will combine the information from this "other" 
    // WebComponentDescriptor with current WebComponentDescriptor
    //
    // when there are conflicts between the contents of the two,
    // the value from current WebComponentDescriptor will override
    // the value in "other"
    //
    // Note: in the Set API, we only add value when such value 
    // is not existed in the Set already
    public void add(WebComponentDescriptor other) {
        // do not do anything if the canonical name of the two web 
        // components are different
        if (!getCanonicalName().equals(other.getCanonicalName())) {
            return;
        }
        // do not do anything if the type of the two web 
        // components are different
        if ((isServlet() && !other.isServlet()) ||
                (!isServlet() && other.isServlet())) {
            return;
        }

        // for simple String types, we can rely on Set API
        // the first one take priority, otherwise take the secone one.
        if (getUrlPatternsSet().size() == 0) {
            getUrlPatternsSet().addAll(other.getUrlPatternsSet());
        }

        // for complex types, only added it if the complex type with same 
        // name is not in the set yet 

        for (Iterator<InitializationParameter> initParamIter =
                other.getInitializationParameterSet().iterator();
             initParamIter.hasNext();) {
            InitializationParameter initParam =
                    initParamIter.next();
            if (getInitializationParameterByName(initParam.getName()) == null) {
                getInitializationParameterSet().add(initParam);
            }
        }
        for (Iterator<SecurityRoleReference> secRoleRefIter =
                other.getSecurityRoleReferenceSet().iterator();
             secRoleRefIter.hasNext();) {
            SecurityRoleReference secRoleRef =
                    secRoleRefIter.next();
            if (getSecurityRoleReferenceByName(secRoleRef.getRolename())
                    == null) {
                getSecurityRoleReferenceSet().add(secRoleRef);
            }
        }

        // only set these values if they are not set in the current
        // web component already

        if (getLoadOnStartUp() == null) {
            setLoadOnStartUp(other.getLoadOnStartUp());
        }
        if (isAsyncSupported() == null) {
            setAsyncSupported(other.isAsyncSupported());
        }
        if (getRunAsIdentity() == null) {
            setRunAsIdentity(other.getRunAsIdentity());
        }
        if (getMultipartConfig() == null) {
            setMultipartConfig(other.getMultipartConfig());
        }
        if (getWebComponentImplementation() == null) {
            setWebComponentImplementation(
                    other.getWebComponentImplementation());
        }
    }
}
