/*
 * Copyright (c) 2014, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package org.glassfish.hk2.tests.interception;

import org.aopalliance.intercept.ConstructorInvocation;
import org.aopalliance.intercept.MethodInvocation;
import org.glassfish.hk2.api.ActiveDescriptor;
import org.glassfish.hk2.api.DynamicConfiguration;
import org.glassfish.hk2.api.DynamicConfigurationService;
import org.glassfish.hk2.api.Filter;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.tests.extras.internal.Utilities;
import org.glassfish.hk2.utilities.BuilderHelper;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the default interception service
 * @author jwells
 */
public class DefaultInterceptionTest {
    public static int cInterceptors = 0;
    public static int mInterceptors = 0;
    
    /**
     * Tests that a non-intercepted method is not intercepted,
     * while an intercepted method is intercepted
     */
    @Test // @org.junit.Ignore
    public void testMethodInterception() {
        ServiceLocator locator = Utilities.getUniqueLocator(BasicRecordingInterceptor.class,
                InterceptedService.class);
        
        BasicRecordingInterceptor interceptor = locator.getService(BasicRecordingInterceptor.class);
        Assert.assertNotNull(interceptor);
        Assert.assertNull(interceptor.getLastInvocation());
        
        InterceptedService interceptedService = locator.getService(InterceptedService.class);
        Assert.assertNotNull(interceptedService);
        
        interceptedService.isIntercepted();
        
        MethodInvocation invocation = interceptor.getLastInvocation();
        Assert.assertNotNull(invocation);
        
        Assert.assertEquals("isIntercepted", invocation.getMethod().getName());
        
        interceptor.clear();
        
        interceptedService.isNotIntercepted();
        
        invocation = interceptor.getLastInvocation();
        Assert.assertNull(invocation);
    }
    
    private static void clearInterceptors(BasicRecordingInterceptor a,
            BasicRecordingInterceptor2 b,
            BasicRecordingInterceptor3 c) {
        a.clear();
        b.clear();
        c.clear();
    }
    
    /**
     * Has a class interceptor and services that have some interceptors and not others etc
     */
    @SuppressWarnings("deprecation")
    @Test // @org.junit.Ignore
    public void testComplexMethodInterception() {
        ServiceLocator locator = Utilities.getUniqueLocator(BasicRecordingInterceptor.class,
                BasicRecordingInterceptor2.class,
                BasicRecordingInterceptor3.class,
                ComplexInterceptedService.class);
        
        BasicRecordingInterceptor interceptor1 = locator.getService(BasicRecordingInterceptor.class);
        BasicRecordingInterceptor2 interceptor2 = locator.getService(BasicRecordingInterceptor2.class);
        BasicRecordingInterceptor3 interceptor3 = locator.getService(BasicRecordingInterceptor3.class);
        
        ComplexInterceptedService interceptedService = locator.getService(ComplexInterceptedService.class);
        Assert.assertNotNull(interceptedService);
        
        {
            interceptedService.interceptedByTwo();
        
            Assert.assertEquals("interceptedByTwo", interceptor1.getLastInvocation().getMethod().getName());
            Assert.assertEquals("interceptedByTwo", interceptor2.getLastInvocation().getMethod().getName());
            Assert.assertNull(interceptor3.getLastInvocation());
        }
        
        clearInterceptors(interceptor1, interceptor2, interceptor3);
        
        {
            interceptedService.interceptedByThree();
        
            Assert.assertEquals("interceptedByThree", interceptor1.getLastInvocation().getMethod().getName());
            Assert.assertNull(interceptor2.getLastInvocation());
            Assert.assertEquals("interceptedByThree", interceptor3.getLastInvocation().getMethod().getName());
        }
        
        clearInterceptors(interceptor1, interceptor2, interceptor3);
        
        {
            interceptedService.interceptedByClass();
        
            Assert.assertEquals("interceptedByClass", interceptor1.getLastInvocation().getMethod().getName());
            Assert.assertNull(interceptor2.getLastInvocation());
            Assert.assertNull(interceptor3.getLastInvocation());
        }
        
        {
            interceptedService.interceptedByAll();
        
            Assert.assertEquals("interceptedByAll", interceptor1.getLastInvocation().getMethod().getName());
            Assert.assertEquals("interceptedByAll", interceptor2.getLastInvocation().getMethod().getName());
            Assert.assertEquals("interceptedByAll", interceptor3.getLastInvocation().getMethod().getName());
        }
        
        {
            interceptedService.interceptedViaStereotype();
        
            Assert.assertEquals("interceptedViaStereotype", interceptor1.getLastInvocation().getMethod().getName());
            Assert.assertEquals("interceptedViaStereotype", interceptor2.getLastInvocation().getMethod().getName());
            Assert.assertEquals("interceptedViaStereotype", interceptor3.getLastInvocation().getMethod().getName());
        }
    }
    
    /**
     * Tests basic constructor interception
     */
    @Test // @org.junit.Ignore
    public void testConstructorInterception() {
        ServiceLocator locator = Utilities.getUniqueLocator(ConstructorRecordingInterceptor.class,
                ConstructorInterceptedService.class);
        
        ConstructorRecordingInterceptor interceptor = locator.getService(ConstructorRecordingInterceptor.class);
        Assert.assertNotNull(interceptor);
        Assert.assertNull(interceptor.getLastInvocation());
        
        ConstructorInterceptedService interceptedService = locator.getService(ConstructorInterceptedService.class);
        Assert.assertNotNull(interceptedService);
        
        ConstructorInvocation invocation = interceptor.getLastInvocation();
        Assert.assertNotNull(invocation);
        
        Assert.assertNotNull(invocation.getConstructor());
        
        Assert.assertEquals(ConstructorInterceptedService.class, invocation.getConstructor().getDeclaringClass());
    }
    
    /**
     * Tests a few things:
     * 1.  Intercepted class has only a class level binder
     * 2.  There is one binder with multiple interceptors having that binder
     * 3.  Both method and constructor interception is used
     * 4.  There is a constructor interceptor that is not used
     */
    @Test
    public void testClassOnlyMultipleIntercptorsWithOneBinder() {
        ServiceLocator locator = Utilities.getUniqueLocator(MultipleInterceptedService.class,
                MultiC1.class,
                MultiC2.class,
                MultiC3.class,
                MultiM1.class,
                MultiM2.class,
                MultiM3.class,
                ConstructorRecordingInterceptor.class);
        
        Assert.assertSame(0, cInterceptors);
        Assert.assertSame(0, mInterceptors);
        
        MultipleInterceptedService mis = locator.getService(MultipleInterceptedService.class);
        
        Assert.assertSame(3, cInterceptors);
        Assert.assertSame(0, mInterceptors);
        
        mis.callMeForAGoodTime();
        
        Assert.assertSame(3, cInterceptors);
        Assert.assertSame(3, mInterceptors);
        
        mis.callMeForAGoodTime();
        
        Assert.assertSame(3, cInterceptors);
        Assert.assertSame(6, mInterceptors);
        
        ConstructorRecordingInterceptor recorder = locator.getService(ConstructorRecordingInterceptor.class);
        Assert.assertNull(recorder.getLastInvocation());
    }
    
    /**
     * Tests that method interceptors can be added and removed
     */
    @Test // @org.junit.Ignore
    public void testDynamicMethodInterception() {
        ServiceLocator locator = Utilities.getUniqueLocator(InterceptedService.class);
        
        BasicRecordingInterceptor interceptorConstant = new BasicRecordingInterceptor();
        BasicRecordingInterceptor2 interceptorConstant2 = new BasicRecordingInterceptor2();
        BasicRecordingInterceptor3 interceptorConstant3 = new BasicRecordingInterceptor3();
        
        {
            InterceptedService interceptedService = locator.getService(InterceptedService.class);
            
            // This is obvious, just sets up a baseline
            interceptedService.isIntercepted();
        
            Assert.assertNull(interceptorConstant.getLastInvocation());
            Assert.assertNull(interceptorConstant2.getLastInvocation());
            Assert.assertNull(interceptorConstant3.getLastInvocation());
            
            clearInterceptors(interceptorConstant, interceptorConstant2, interceptorConstant3);
        }
        
        ActiveDescriptor<?> interceptorDescriptor = ServiceLocatorUtilities.addOneConstant(locator, interceptorConstant);
        
        {
            InterceptedService interceptedService = locator.getService(InterceptedService.class);
            
            // From zero to one
            interceptedService.isIntercepted();
        
            Assert.assertSame("isIntercepted", interceptorConstant.getLastInvocation().getMethod().getName());
            Assert.assertNull(interceptorConstant2.getLastInvocation());
            Assert.assertNull(interceptorConstant3.getLastInvocation());
            
            clearInterceptors(interceptorConstant, interceptorConstant2, interceptorConstant3);
        }
        
        ServiceLocatorUtilities.removeOneDescriptor(locator, interceptorDescriptor);
        
        {
            InterceptedService interceptedService = locator.getService(InterceptedService.class);
            
            // from one to zero
            interceptedService.isIntercepted();
        
            Assert.assertNull(interceptorConstant.getLastInvocation());
            Assert.assertNull(interceptorConstant2.getLastInvocation());
            Assert.assertNull(interceptorConstant3.getLastInvocation());
            
            clearInterceptors(interceptorConstant, interceptorConstant2, interceptorConstant3);
        }
        
        interceptorDescriptor = ServiceLocatorUtilities.addOneConstant(locator, interceptorConstant);
        ActiveDescriptor<?> interceptorDescriptor2 = ServiceLocatorUtilities.addOneConstant(locator, interceptorConstant2);
        
        {
            InterceptedService interceptedService = locator.getService(InterceptedService.class);
            
            // from zero to two
            interceptedService.isIntercepted();
        
            Assert.assertSame("isIntercepted", interceptorConstant.getLastInvocation().getMethod().getName());
            Assert.assertSame("isIntercepted", interceptorConstant2.getLastInvocation().getMethod().getName());
            Assert.assertNull(interceptorConstant3.getLastInvocation());
            
            clearInterceptors(interceptorConstant, interceptorConstant2, interceptorConstant3);
        }
        
        // In this next bit we add interceptorConstant3 and remove interceptorConstant2 in the same configuration
        // run, in order to be sure the system notices the change
        DynamicConfigurationService dynamicConfigurationService = locator.getService(DynamicConfigurationService.class);
        DynamicConfiguration config = dynamicConfigurationService.createDynamicConfiguration();
        
        ActiveDescriptor<?> interceptorDescriptor3 = BuilderHelper.createConstantDescriptor(interceptorConstant3);
        
        Filter unbindFilter = BuilderHelper.createSpecificDescriptorFilter(interceptorDescriptor2);
        
        config.addActiveDescriptor(interceptorDescriptor3);
        config.addUnbindFilter(unbindFilter);
        
        config.commit();
        
        {
            InterceptedService interceptedService = locator.getService(InterceptedService.class);
            
            // from two to a different two
            interceptedService.isIntercepted();
        
            Assert.assertSame("isIntercepted", interceptorConstant.getLastInvocation().getMethod().getName());
            Assert.assertNull(interceptorConstant2.getLastInvocation());
            Assert.assertSame("isIntercepted", interceptorConstant3.getLastInvocation().getMethod().getName());
            
            clearInterceptors(interceptorConstant, interceptorConstant2, interceptorConstant3);
        }
    }
}
