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

import com.sun.enterprise.deployment.web.ServletFilterMapping;

import java.util.*;
import javax.servlet.DispatcherType;

/**
 * Deployment object representing the servlet filter mapping spec
 * @author Martin D. Flynn
 */
public final class ServletFilterMappingDescriptor
    extends Descriptor
    implements com.sun.enterprise.deployment.web.ServletFilterMapping
{

    private static EnumSet allowed_dispatchers;
    
    private EnumSet dispatchers;
    private List<String> servletNames;
    private List<String> urlPatterns;

    /** generic constructor */
    public ServletFilterMappingDescriptor() {
	super(""/*name*/, ""/*description*/);
    }

    /** copy constructor */
    public ServletFilterMappingDescriptor(ServletFilterMappingDescriptor other) {
        super(other);
        dispatchers = (other.dispatchers != null)?
            EnumSet.copyOf(other.dispatchers) : null;
    }

    public void addServletName(String servletName) {
        getServletNames().add(servletName);
    }

    public List<String> getServletNames() {
        if (servletNames == null) {
            servletNames = new LinkedList<String>();
        }
        return servletNames;
    }

    public void addURLPattern(String urlPattern) {
        getURLPatterns().add(urlPattern);
    }

    public List<String> getURLPatterns() {
        if (urlPatterns == null) {
            urlPatterns = new LinkedList<String>();
        }
        return urlPatterns;
    }

    public void addDispatcher(String dispatcher) {
        if (dispatchers == null) {
            dispatchers = EnumSet.noneOf(DispatcherType.class);
        }
        dispatchers.add(Enum.valueOf(DispatcherType.class, dispatcher));
    }
    
    public void removeDispatcher(String dispatcher) {
        if (dispatchers == null) {
            return;
        }
        dispatchers.remove(Enum.valueOf(DispatcherType.class, dispatcher));
    }

    public Set<DispatcherType> getDispatchers() {
        if (dispatchers == null) {
            dispatchers = EnumSet.noneOf(DispatcherType.class);
        }
        return dispatchers;
    }

    public static Set<DispatcherType> getAllowedDispatchers() {
        if (allowed_dispatchers == null) {
            allowed_dispatchers = EnumSet.allOf(DispatcherType.class);
        }
        return allowed_dispatchers;
    }

    /** compare equals */
    public boolean equals(Object obj) {
        if (obj instanceof ServletFilterMapping) {
            ServletFilterMapping o = (ServletFilterMapping) obj;
            if ( this.getName().equals(o.getName())
                    && this.getServletNames().equals(o.getServletNames())
                    && this.getURLPatterns().equals(o.getURLPatterns()) ) {
                return true;
            }
        }

        return false;
    }

    public int hashCode() {
        int result = 17;
        result = 37*result + getName().hashCode();
        result = 37*result + getServletNames().hashCode();
        result = 37*result + getURLPatterns().hashCode();
        return result;
    }
}
