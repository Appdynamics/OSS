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

package com.sun.enterprise.deployment.annotation.handlers;

import com.sun.enterprise.deployment.AuthorizationConstraintImpl;
import com.sun.enterprise.deployment.Role;
import com.sun.enterprise.deployment.SecurityConstraintImpl;
import com.sun.enterprise.deployment.UserDataConstraintImpl;
import com.sun.enterprise.deployment.WebBundleDescriptor;
import com.sun.enterprise.deployment.WebComponentDescriptor;
import com.sun.enterprise.deployment.WebResourceCollectionImpl;
import com.sun.enterprise.deployment.web.SecurityConstraint;
import com.sun.enterprise.deployment.web.UserDataConstraint;
import com.sun.enterprise.deployment.web.WebResourceCollection;
import com.sun.enterprise.deployment.annotation.context.WebBundleContext;
import com.sun.enterprise.deployment.annotation.context.WebComponentContext;
import com.sun.enterprise.util.net.URLPattern;
import org.glassfish.apf.AnnotationInfo;
import org.glassfish.apf.AnnotationProcessorException;
import org.glassfish.apf.HandlerProcessingResult;
import org.glassfish.apf.ResultType;
import org.jvnet.hk2.annotations.Service;

import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.HttpMethodConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.ServletSecurity.EmptyRoleSemantic;
import javax.servlet.annotation.ServletSecurity.TransportGuarantee;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

/**
 * This handler is responsible in handling
 * javax.servlet.annotation.ServletSecurity.
 *
 * @author Shing Wai Chan
 */
@Service
public class ServletSecurityHandler extends AbstractWebHandler {
    public ServletSecurityHandler() {
    }

    /**
     * @return the annotation type this annotation handler is handling
     */
    @Override
    public Class<? extends Annotation> getAnnotationType() {
        return ServletSecurity.class;
    }

    @Override
    protected HandlerProcessingResult processAnnotation(AnnotationInfo ainfo,
            WebComponentContext[] webCompContexts)
            throws AnnotationProcessorException {

        HandlerProcessingResult result = null;
        for (WebComponentContext webCompContext : webCompContexts) {
            result = processAnnotation(ainfo,
                    webCompContext.getDescriptor());
            if (result.getOverallResult() == ResultType.FAILED) {
                break;
            }
        }
        return result;
    }

    @Override
    protected HandlerProcessingResult processAnnotation(
            AnnotationInfo ainfo, WebBundleContext webBundleContext)
            throws AnnotationProcessorException {

        return getInvalidAnnotatedElementHandlerResult(
            ainfo.getProcessingContext().getHandler(), ainfo);
    }

    @Override
    public Class<? extends Annotation>[] getTypeDependencies() {
        return new Class[] { WebServlet.class };
    }


    private HandlerProcessingResult processAnnotation(
            AnnotationInfo ainfo, WebComponentDescriptor webCompDesc)
            throws AnnotationProcessorException {

        Class webCompClass = (Class)ainfo.getAnnotatedElement();
        if (!HttpServlet.class.isAssignableFrom(webCompClass)) {
            log(Level.SEVERE, ainfo,
                localStrings.getLocalString(
                "enterprise.deployment.annotation.handlers.needtoextend",
                "The Class {0} having annotation {1} need to be a derived class of {2}.",
                new Object[] { webCompClass.getName(), SecurityConstraint.class.getName(), HttpServlet.class.getName() }));
            return getDefaultFailedResult();
        }

        Set<String> urlPatterns = getUrlPatternsWithoutSecurityConstraint(webCompDesc);

        if (urlPatterns != null && urlPatterns.size() > 0) {
            WebBundleDescriptor webBundleDesc = webCompDesc.getWebBundleDescriptor();
            ServletSecurity servletSecurityAn = (ServletSecurity)ainfo.getAnnotation();
            HttpConstraint httpConstraint = servletSecurityAn.value();

            SecurityConstraint securityConstraint =
                    createSecurityConstraint(webBundleDesc,
                    urlPatterns, httpConstraint.rolesAllowed(),
                    httpConstraint.value(),
                    httpConstraint.transportGuarantee(),
                    null);

            // we know there is one WebResourceCollection there
            WebResourceCollection webResColl =
                    securityConstraint.getWebResourceCollections().iterator().next();
            HttpMethodConstraint[] httpMethodConstraints = servletSecurityAn.httpMethodConstraints();
            for (HttpMethodConstraint httpMethodConstraint : httpMethodConstraints) {
                String httpMethod = httpMethodConstraint.value();
                if (httpMethod == null || httpMethod.length() == 0) {
                    return getDefaultFailedResult();
                }

                createSecurityConstraint(webBundleDesc,
                        urlPatterns, httpMethodConstraint.rolesAllowed(),
                        httpMethodConstraint.emptyRoleSemantic(),
                        httpMethodConstraint.transportGuarantee(),
                        httpMethod);

                //exclude this from the top level constraint
                webResColl.addHttpMethodOmission(httpMethod);
            }
        }

        return getDefaultProcessedResult();
    }

    /**
     * Given a WebComponentDescriptor, find the set of urlPattern which does not have
     * any existing url pattern in SecurityConstraint
     * @param webCompDesc
     * @return a list of url String
     */
    public static Set<String> getUrlPatternsWithoutSecurityConstraint(WebComponentDescriptor webCompDesc) {

        Set<String> urlPatternsWithoutSC = new HashSet<String>(webCompDesc.getUrlPatternsSet());

        WebBundleDescriptor webBundleDesc = webCompDesc.getWebBundleDescriptor();
        Set<String> urlPatterns = webCompDesc.getUrlPatternsSet();

        Enumeration<SecurityConstraint> eSecConstr = webBundleDesc.getSecurityConstraints();
        while (eSecConstr.hasMoreElements()) {
            SecurityConstraint sc = eSecConstr.nextElement();
            for (WebResourceCollection wrc : sc.getWebResourceCollections()) {
                urlPatternsWithoutSC.removeAll(wrc.getUrlPatterns());
            }
        }

        return urlPatternsWithoutSC;
    }

    public static SecurityConstraint createSecurityConstraint(
            WebBundleDescriptor webBundleDesc,
            Set<String> urlPatterns, String[] rolesAllowed,
            EmptyRoleSemantic emptyRoleSemantic,
            TransportGuarantee transportGuarantee,
            String httpMethod) {

        SecurityConstraint securityConstraint = new SecurityConstraintImpl();
        WebResourceCollectionImpl webResourceColl = new WebResourceCollectionImpl();
        securityConstraint.addWebResourceCollection(webResourceColl);
        for (String urlPattern : urlPatterns) {
            webResourceColl.addUrlPattern(urlPattern);
        }

        AuthorizationConstraintImpl ac = null;
        if (rolesAllowed != null && rolesAllowed.length > 0) {
            if (emptyRoleSemantic ==  EmptyRoleSemantic.DENY) {
                 throw new IllegalArgumentException(localStrings.getLocalString(
                        "enterprise.deployment.annotation.handlers.denyWithRolesAllowed",
                        "One cannot specify DENY with an non-empty array of rolesAllowed in @ServletSecurity / ServletSecurityElement"));
            }

            ac = new AuthorizationConstraintImpl();
            for (String roleName : rolesAllowed) {
                Role role = new Role(roleName);
                webBundleDesc.addRole(role);
                ac.addSecurityRole(roleName);
            }
        } else if (emptyRoleSemantic == EmptyRoleSemantic.PERMIT) {
            // ac is null
        } else { // DENY
            ac = new AuthorizationConstraintImpl();
        }
        securityConstraint.setAuthorizationConstraint(ac);

        UserDataConstraint udc = new UserDataConstraintImpl();
        udc.setTransportGuarantee(
                ((transportGuarantee == TransportGuarantee.CONFIDENTIAL) ?
                UserDataConstraint.CONFIDENTIAL_TRANSPORT :
                UserDataConstraint.NONE_TRANSPORT));
        securityConstraint.setUserDataConstraint(udc);

        if (httpMethod != null) {
            webResourceColl.addHttpMethod(httpMethod);
        }

        webBundleDesc.addSecurityConstraint(securityConstraint);

        return securityConstraint;
    }
}
