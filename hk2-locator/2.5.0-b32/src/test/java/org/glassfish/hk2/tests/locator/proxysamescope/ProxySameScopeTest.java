/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2015 Oracle and/or its affiliates. All rights reserved.
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

package org.glassfish.hk2.tests.locator.proxysamescope;

import junit.framework.Assert;

import org.glassfish.hk2.api.ProxyCtl;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.tests.locator.utilities.LocatorHelper;
import org.junit.Test;

/**
 * @author jwells
 *
 */
public class ProxySameScopeTest {
    private final static String TEST_NAME = "ProxySameScopeTest";
    private final static ServiceLocator locator = LocatorHelper.create(TEST_NAME, new ProxiableSameScopeModule());
    
    /**
     * Tests a basic same-scope scenario.  Ensures the services injected
     * within the same scope are NOT proxied
     */
    @Test
    public void testSameScopeServicesNoProxy() {
        ProxiableServiceB b = locator.getService(ProxiableServiceB.class);
        Assert.assertNotNull(b);
        Assert.assertTrue(b instanceof ProxyCtl);
        
        Assert.assertNotNull(b.getViaConstructor());
        Assert.assertNotNull(b.getViaMethod());
        Assert.assertNotNull(b.getViaField());
        
        Assert.assertFalse(b.getViaConstructor() instanceof ProxyCtl);
        Assert.assertFalse(b.getViaField() instanceof ProxyCtl);
        Assert.assertFalse(b.getViaMethod() instanceof ProxyCtl);
    }
    
    /**
     * Two proxiable scopes have proxyForSameScope set to false, make
     * sure services in these two scopes are proxied
     */
    @Test
    public void testDifferentScopedProxyForSameScopeFalseAreProxied() {
        ProxiableServiceC c = locator.getService(ProxiableServiceC.class);
        Assert.assertNotNull(c);
        Assert.assertTrue(c instanceof ProxyCtl);
        
        c.check();
    }
    
    /**
     * Two services in the same scope with proxyForSameScope set to
     * false but the one service (ServiceD) has an explicit instruction
     * to proxy for same scope anyway
     */
    @Test
    public void testSameScopeButSpecificallyToldNotToProxy() {
        ProxiableServiceE e = locator.getService(ProxiableServiceE.class);
        Assert.assertNotNull(e);
        Assert.assertTrue(e instanceof ProxyCtl);
        
        e.check();
    }
    
    /**
     * Two services in the same scope that has ProxyForSameService set to true
     * but where one of the services is explicitly set to ProxyForSameService
     * set to to false
     */
    @Test
    public void testSameProxiableScopeWithSpecificProxySameScopeFalseService() {
        ProxiableServiceG g = locator.getService(ProxiableServiceG.class);
        Assert.assertNotNull(g);
        Assert.assertTrue(g instanceof ProxyCtl);
        
        g.check();
    }
    
    /**
     * Tests a proxiable singleton that has ProxyForSameScope set
     * to false injected into another singleton (should not be
     * proxied)
     */
    @Test
    public void testProxiableSingletonNotLazyIntoSingleton() {
        SingletonServiceA a = locator.getService(SingletonServiceA.class);
        Assert.assertNotNull(a);
        
        a.check();
    }
    
    /**
     * Tests a proxiable singleton that has ProxyForSameScope set
     * to false injected into a PerLookup (should be proxied)
     */
    @Test
    public void testProxiableSingletonNotLazyIntoPerLookup() {
        PerLookupServiceA a = locator.getService(PerLookupServiceA.class);
        Assert.assertNotNull(a);
        
        a.check();
    }

}
