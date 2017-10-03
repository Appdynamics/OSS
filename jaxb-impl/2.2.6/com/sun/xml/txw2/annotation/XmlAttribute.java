/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2005-2010 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.xml.txw2.annotation;

import com.sun.xml.txw2.TypedXmlWriter;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Specifies that the invocation of the method will produce an attribute.
 *
 * <p>
 * The method signature has to match the form <tt>R foo(DT1,DT2,..)</tt>
 *
 * <p>
 * R is either <tt>void</tt> or the type to which the interface that declares
 * this method is assignable. In the case of the latter, the method will return
 * <tt>this</tt> object, allowing you to chain the multiple attribute method
 * invocations like {@link StringBuffer}.
 *
 * <p>
 * DTi must be datatype objects.
 *
 * <p>
 * When this method is called, a new attribute is added to the current element,
 * whose value is whitespace-separated text from each of the datatype objects.
 *
 * @author Kohsuke Kawaguchi
 */
@Retention(RUNTIME)
@Target({METHOD})
public @interface XmlAttribute {
    /**
     * The local name of the attribute.
     *
     * <p>
     * If left unspecified, the method name is used as the attribute name.
     *
     */
    String value() default "";

    /**
     * The namespace URI of the attribute.
     */
    String ns() default "";
}
