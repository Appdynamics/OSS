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

import java.lang.reflect.Method;

/**
 * Deployment object representing the lifecycle-callback.
 *
 * @author Shing Wai Chan
 */
public class LifecycleCallbackDescriptor extends Descriptor {

    private String lifecycleCallbackClass;
    private String lifecycleCallbackMethod;
    private String defaultLifecycleCallbackClass;
    private MetadataSource metadataSource = MetadataSource.XML;

    public enum CallbackType {

        POST_CONSTRUCT,
        PRE_DESTROY,
        PRE_PASSIVATE,
        POST_ACTIVATE 

    }

    public void setLifecycleCallbackClass(String clazz) {
        lifecycleCallbackClass = clazz;
    }

    public String getLifecycleCallbackClass() {
        if (lifecycleCallbackClass == null || 
            lifecycleCallbackClass.trim().equals("")) {
            return defaultLifecycleCallbackClass;
        } else {
            return lifecycleCallbackClass;
        }
    }

    public void setDefaultLifecycleCallbackClass(String clazz) {
        defaultLifecycleCallbackClass = clazz;
    }

    public String getDefaultLifecycleCallbackClass() {
        return defaultLifecycleCallbackClass;
    }

    public void setLifecycleCallbackMethod(String method) {
        lifecycleCallbackMethod = method;
    }

    public String getLifecycleCallbackMethod() {
        return lifecycleCallbackMethod;
    }

    /**
     * Given a classloader, find the Method object corresponding to this
     * lifecycle callback.  
     *
     * @throw Exception if no method found
     */
    public Method getLifecycleCallbackMethodObject(ClassLoader loader) 
        throws Exception {

        Method m = null;

        if( getLifecycleCallbackClass() == null ) {
            throw new IllegalArgumentException("no lifecycle class defined");
        }

        Class clazz = loader.loadClass(getLifecycleCallbackClass());
        
        // Check for the method within this class only.
        // This does not include super-classses.
        for(Method next : clazz.getDeclaredMethods()) {
            
            // Overloading is not allowed for AroundInvoke or
            // Callback methods so match by name only.
            if( next.getName().equals(lifecycleCallbackMethod) ) {
                m = next;
                break;
            }
        }

        if( m == null ) {
            throw new NoSuchMethodException("no method matching " +
                                            lifecycleCallbackMethod);
        }

        return m;
    }

    public MetadataSource getMetadataSource() {
        return metadataSource;
    }

    public void setMetadataSource(MetadataSource metadataSource) {
        this.metadataSource = metadataSource;
    }
}
