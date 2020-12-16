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

package org.glassfish.hk2.tests.locator.negative.dynamicconfig;

import org.glassfish.hk2.api.Context;
import org.glassfish.hk2.api.DynamicConfiguration;
import org.glassfish.hk2.api.DynamicConfigurationListener;
import org.glassfish.hk2.api.DynamicConfigurationService;
import org.glassfish.hk2.api.ErrorService;
import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.MultiException;
import org.glassfish.hk2.api.PerLookup;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.ValidationService;
import org.glassfish.hk2.tests.locator.utilities.LocatorHelper;
import org.glassfish.hk2.utilities.BuilderHelper;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author jwells
 *
 */
public class NegativeConfigTest {
    /**
     * An injection resolver must be in Singleton scope
     */
    @Test
    public void testPerLookupInjectionResolver() {
        ServiceLocator locator = LocatorHelper.create();
        ServiceLocatorUtilities.addClasses(locator, DynamicConfigErrorService.class);
        
        DynamicConfigErrorService errorService = locator.getService(DynamicConfigErrorService.class);
        Assert.assertNull(errorService.getConfigException());
        
        DynamicConfigurationService dcs = locator.getService(DynamicConfigurationService.class);
        DynamicConfiguration dc = dcs.createDynamicConfiguration();
        
        dc.bind(BuilderHelper.link(BadInjectionResolver.class).
                to(InjectionResolver.class).build());
        
        try {
            dc.commit();
            Assert.fail("Commit should have failed with PerLookup InjectionResolver");
        }
        catch (MultiException me) {
            Assert.assertTrue(me.getMessage(), me.getMessage().contains(
                    " must be in the Singleton scope"));
            
            Assert.assertEquals(errorService.getConfigException(), me);
        }
        
    }
    
    /**
     * A context must be in Singleton scope
     */
    @Test
    public void testPerLookupContext() {
        ServiceLocator locator = LocatorHelper.create();
        ServiceLocatorUtilities.addClasses(locator, DynamicConfigErrorService.class);
        
        DynamicConfigErrorService errorService = locator.getService(DynamicConfigErrorService.class);
        Assert.assertNull(errorService.getConfigException());
        
        DynamicConfigurationService dcs = locator.getService(DynamicConfigurationService.class);
        DynamicConfiguration dc = dcs.createDynamicConfiguration();
        
        dc.bind(BuilderHelper.link(BadContext.class).
                to(Context.class).
                in(PerLookup.class.getName()).
                build());
        
        try {
            dc.commit();
            Assert.fail("Commit should have failed with PerLookup Context");
        }
        catch (MultiException me) {
            Assert.assertTrue(me.getMessage(), me.getMessage().contains(
                    " must be in the Singleton scope"));
            
            Assert.assertEquals(errorService.getConfigException(), me);
        }
        
    }
    
    /**
     * A validation service must be in Singleton scope
     */
    @Test
    public void testPerLookupValidationService() {
        ServiceLocator locator = LocatorHelper.create();
        ServiceLocatorUtilities.addClasses(locator, DynamicConfigErrorService.class);
        
        DynamicConfigErrorService errorService = locator.getService(DynamicConfigErrorService.class);
        Assert.assertNull(errorService.getConfigException());
        
        DynamicConfigurationService dcs = locator.getService(DynamicConfigurationService.class);
        DynamicConfiguration dc = dcs.createDynamicConfiguration();
        
        dc.bind(BuilderHelper.link(BadValidationService.class).
                to(ValidationService.class).
                in(PerLookup.class.getName()).
                build());
        
        try {
            dc.commit();
            Assert.fail("Commit should have failed with PerLookup ValidationService");
        }
        catch (MultiException me) {
            Assert.assertTrue(me.getMessage(), me.getMessage().contains(
                    " must be in the Singleton scope"));
            
            Assert.assertEquals(errorService.getConfigException(), me);
        }
        
    }
    
    /**
     * An error service must be in Singleton scope
     */
    @Test
    public void testPerLookupErrorService() {
        ServiceLocator locator = LocatorHelper.create();
        ServiceLocatorUtilities.addClasses(locator, DynamicConfigErrorService.class);
        
        DynamicConfigErrorService errorService = locator.getService(DynamicConfigErrorService.class);
        Assert.assertNull(errorService.getConfigException());
        
        DynamicConfigurationService dcs = locator.getService(DynamicConfigurationService.class);
        DynamicConfiguration dc = dcs.createDynamicConfiguration();
        
        dc.bind(BuilderHelper.link(BadErrorService.class).
                to(ErrorService.class).
                build());
        
        try {
            dc.commit();
            Assert.fail("Commit should have failed with PerLookup ErrorService");
        }
        catch (MultiException me) {
            Assert.assertTrue(me.getMessage(), me.getMessage().contains(
                    " must be in the Singleton scope"));
            
            Assert.assertEquals(errorService.getConfigException(), me);
        }
        
    }
    
    /**
     * A dynamic configuration listener must be in Singleton scope
     */
    @Test
    public void testPerLookupDynamicConfigurationListener() {
        ServiceLocator locator = LocatorHelper.create();
        ServiceLocatorUtilities.addClasses(locator, DynamicConfigErrorService.class);
        
        DynamicConfigErrorService errorService = locator.getService(DynamicConfigErrorService.class);
        Assert.assertNull(errorService.getConfigException());
        
        DynamicConfigurationService dcs = locator.getService(DynamicConfigurationService.class);
        DynamicConfiguration dc = dcs.createDynamicConfiguration();
        
        dc.bind(BuilderHelper.link(BadDynamicConfigurationListener.class).
                to(DynamicConfigurationListener.class).
                build());
        
        try {
            dc.commit();
            Assert.fail("Commit should have failed with PerLookup DynamicConfigurationListener");
        }
        catch (MultiException me) {
            Assert.assertTrue(me.getMessage(), me.getMessage().contains(
                    " must be in the Singleton scope"));
            
            Assert.assertEquals(errorService.getConfigException(), me);
        }
        
    }

}
