/*
 * Copyright (c) 2012, 2018 Oracle and/or its affiliates. All rights reserved.
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

package org.glassfish.hk2.tests.locator.arrays;

import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.tests.locator.utilities.LocatorHelper;
import org.junit.Test;

/**
 * @author jwells
 *
 */
public class ArraysTest {
    private final static String TEST_NAME = "ArraysTest";
    private final static ServiceLocator locator = LocatorHelper.create(TEST_NAME, new ArraysModule());
    
    /**
     * Tests that I can inject an array of int
     */
    @Test
    public void testArrayOfInt() {
        ArrayInjectee ai = locator.getService(ArrayInjectee.class);
        
        int value[] = ai.getArrayOfInt();
        Assert.assertNotNull(value);
        Assert.assertEquals(0, value.length);
    }
    
    /**
     * Tests that I can inject a parameterized type array
     */
    @Test
    public void testArrayOfPTList() {
        ArrayInjectee ai = locator.getService(ArrayInjectee.class);
        
        List<String> value[] = ai.getArrayOfListString();
        Assert.assertNotNull(value);
        Assert.assertEquals(0, value.length);
    }
    
    /**
     * Tests that I can inject a different kind of parameterized type array
     */
    @Test
    public void testArrayOfPTMap() {
        ArrayInjectee ai = locator.getService(ArrayInjectee.class);
        
        Map<String,String> value[] = ai.getArrayOfMapStringString();
        Assert.assertNotNull(value);
        Assert.assertEquals(0, value.length);
    }
    
    /**
     * Tests that I can inject an array of a user, non-parameterized object
     */
    @Test
    public void testArrayOfUserObject() {
        ArrayInjectee ai = locator.getService(ArrayInjectee.class);
        
        SimpleService value[] = ai.getArrayOfSimpleService();
        Assert.assertNotNull(value);
        Assert.assertEquals(0, value.length);
    }

}
