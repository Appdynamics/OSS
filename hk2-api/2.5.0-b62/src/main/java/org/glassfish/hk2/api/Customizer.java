/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2015-2016 Oracle and/or its affiliates. All rights reserved.
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
package org.glassfish.hk2.api;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Gives the type (and optional name) of a customizer service
 * to use when an unknown method on a bean interface is
 * encountered.  Customizers are found in the hk2 service
 * registry
 * <p>
 * This annotation is for use with the hk2-xml configuration
 * system
 * 
 * @author jwells
 *
 */
@Documented
@Retention(RUNTIME)
@Target(TYPE)
public @interface Customizer {
    /**
     * The class of the customizer to lookup for
     * this bean
     * 
     * @return the class of the customizer for this bean
     */
    public Class<?>[] value();
    
    /**
     * The name of the customizer to lookup for
     * this bean
     * 
     * @return the name of the customizer for this bean
     */
    public String[] name() default {};
    
    /**
     * If true then if a bean method is not mirrored in
     * the customizer a RuntimeException will be thrown.
     * Otherwise unknown methods are treated as a no-op.
     * Setting this to false must be used with care as
     * any method with a scalar return will throw a null
     * pointer exception if no method can be found in the
     * customizer methods since converting null to a
     * scalar value does not work
     * 
     * @return true if an unknown method called on a bean
     * at runtime which does not have a mirrored method
     * on the customizer should raise a RuntimeException
     */
    public boolean failWhenMethodNotFound() default true;

}
