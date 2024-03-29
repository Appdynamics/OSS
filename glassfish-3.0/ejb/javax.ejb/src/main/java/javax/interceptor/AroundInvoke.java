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


package javax.interceptor;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;

/**
 * <p>Defines an interceptor method that interposes on business methods.
 * May be applied to any non-final, non-static method with a single 
 * parameter of type {@link javax.interceptor.InvocationContext} and
 * return type {@link java.lang.Object} of the target class 
 * (or superclass) or of any interceptor class.</p>
 * 
 * <pre>
 * &#064;AroundInvoke
 * public Object intercept(InvocationContext ctx) throws Exception { ... }
 * </pre>
 * 
 * <p>A class may not declare more than one <tt>&#064;AroundInvoke</tt> 
 * method.</p>
 * 
 * <p>An <tt>&#064;AroundInvoke</tt> method can invoke any component or 
 * resource that the method it is intercepting can invoke.</p>
 * 
 * <p><tt>&#064;AroundInvoke</tt> method invocations occur within the same 
 * transaction and security context as the method on which they are 
 * interposing.</p>
 * 
 * <p><tt>&#064;AroundInvoke</tt> methods may throw any exceptions that are 
 * allowed by the throws clause of the method on which they are 
 * interposing. They may catch and suppress exceptions and recover 
 * by calling {@link javax.interceptor.InvocationContext#proceed()}.</p>
 *
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AroundInvoke {
}
