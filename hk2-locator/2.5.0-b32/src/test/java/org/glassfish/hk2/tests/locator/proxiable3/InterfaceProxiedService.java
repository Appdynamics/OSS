/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012-2015 Oracle and/or its affiliates. All rights reserved.
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

package org.glassfish.hk2.tests.locator.proxiable3;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.glassfish.hk2.api.UseProxy;

/**
 * @author jwells
 *
 */
@Singleton @UseProxy(true)
public class InterfaceProxiedService implements Foo, Bar, Control {
    private int fooCalls;
    private int barCalls;
    
    /**
     * This constructor is here to ensure that there is no zero-arg
     * constructor for this class, making it impossible to proxy
     * with the cglib proxying scheme
     * 
     * @param simple A dummy service, used to ensure there is no
     * zero-arg constructor for this class
     */
    @Inject
    private InterfaceProxiedService(SimpleService simple) {
        
    }

    /* (non-Javadoc)
     * @see org.glassfish.hk2.tests.locator.proxiable3.Control#getFooInvocations()
     */
    @Override
    public int getFooInvocations() {
        return fooCalls;
    }

    /* (non-Javadoc)
     * @see org.glassfish.hk2.tests.locator.proxiable3.Control#getBarInvocations()
     */
    @Override
    public int getBarInvocations() {
        return barCalls;
    }

    /* (non-Javadoc)
     * @see org.glassfish.hk2.tests.locator.proxiable3.Bar#bar()
     */
    @Override
    public void bar() {
        barCalls++;

    }

    /* (non-Javadoc)
     * @see org.glassfish.hk2.tests.locator.proxiable3.Foo#foo()
     */
    @Override
    public void foo() {
        fooCalls++;

    }

    @Override
    public void resetInvocations() {
        barCalls = 0;
        fooCalls = 0;
        
    }

}
