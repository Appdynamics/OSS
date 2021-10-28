/*
 * Copyright (c) 2015, 2018 Oracle and/or its affiliates. All rights reserved.
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

package org.glassfish.hk2.xml.test.customizer;

import java.net.URL;

import org.glassfish.hk2.api.MultiException;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.xml.api.XmlRootHandle;
import org.glassfish.hk2.xml.api.XmlService;
import org.glassfish.hk2.xml.test.basic.beans.Commons;
import org.glassfish.hk2.xml.test.utilities.Utilities;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author jwells
 *
 */
public class CustomizerTest {
    private final static String PREFIX = "Go ";
    private final static String POSTFIX = " Eagles";
    
    public final static boolean C4 = false;
    public final static int C5 = 5;
    public final static long C6 = 6L;
    public final static float C7 = (float) 7.0;
    public final static double C8 = 8.0;
    public final static byte C9 = 9;
    public final static short C10 = 10;
    public final static char C11 = 'E';
    
    public final static String ALICE_NAME = "Alice";
    public final static String BOB_NAME = "Bob";
    public final static String CAROL_NAME = "Carol";
    public final static String DAVE_NAME = "Dave";
    
    /**
     * Tests that a basic customizer works properly
     */
    @Test // @org.junit.Ignore
    public void testBasicCustomizerOnRoot() throws Exception {
        ServiceLocator locator = Utilities.createLocator(CustomizerOne.class);
        XmlService xmlService = locator.getService(XmlService.class);
        
        URL url = getClass().getClassLoader().getResource(Commons.MUSEUM1_FILE);
        
        XmlRootHandle<MuseumBean> rootHandle = xmlService.unmarshal(url.toURI(), MuseumBean.class);
        MuseumBean museum = rootHandle.getRoot();
        
        CustomOne custom1 = locator.getService(CustomOne.class);
        CustomTwo custom2 = locator.getService(CustomTwo.class);
        
        verifyBasicCustomizersWorked(museum, custom1, custom2, true);
    }
    
    /**
     * Tests that multiple customizers works
     */
    @Test // @org.junit.Ignore
    public void testTwoCustomizersWorks() throws Exception {
        ServiceLocator locator = Utilities.createLocator(CustomizerTwoFirstHalf.class,
                CustomizerTwoSecondHalf.class);
        XmlService xmlService = locator.getService(XmlService.class);
        
        URL url = getClass().getClassLoader().getResource(Commons.MUSEUM1_FILE);
        
        XmlRootHandle<MuseumBeanDoubleCustomized> rootHandle = xmlService.unmarshal(url.toURI(), MuseumBeanDoubleCustomized.class);
        MuseumBeanDoubleCustomized museum = rootHandle.getRoot();
        
        CustomOne custom1 = locator.getService(CustomOne.class);
        CustomTwo custom2 = locator.getService(CustomTwo.class);
        
        verifyBasicCustomizersWorked(museum, custom1, custom2, true);
    }
    
    /**
     * Tests that multiple named customizers works
     */
    @Test // @org.junit.Ignore
    public void testTwoNamedCustomizersWorks() throws Exception {
        ServiceLocator locator = Utilities.createLocator(CustomizerTwoFirstHalf.class,
                CustomizerTwoSecondHalf.class,
                CustomizerThreeFirstHalf.class,
                CustomizerThreeSecondHalf.class);
        XmlService xmlService = locator.getService(XmlService.class);
        
        URL url = getClass().getClassLoader().getResource(Commons.MUSEUM1_FILE);
        
        XmlRootHandle<MuseumBeanDoubleNamedCustomized> rootHandle = xmlService.unmarshal(url.toURI(), MuseumBeanDoubleNamedCustomized.class);
        MuseumBeanDoubleNamedCustomized museum = rootHandle.getRoot();
        
        CustomOne custom1 = locator.getService(CustomOne.class, CAROL_NAME);
        CustomTwo custom2 = locator.getService(CustomTwo.class, DAVE_NAME);
        
        verifyBasicCustomizersWorked(museum, custom1, custom2, false);
        
        CustomOne custom1_neg = locator.getService(CustomOne.class, ALICE_NAME);
        CustomTwo custom2_neg = locator.getService(CustomTwo.class, BOB_NAME);
        
        Assert.assertFalse(custom1_neg.getCustomizer2Called());
        Assert.assertFalse(custom2_neg.getFauxAddCalled());
    }
    
    private void verifyBasicCustomizersWorked(MuseumBean museum, CustomOne custom1, CustomTwo custom2, boolean badFails) throws Exception {
        
        String retVal = museum.customizer1(PREFIX, POSTFIX);
        Assert.assertEquals(retVal, PREFIX + Commons.BEN_FRANKLIN + POSTFIX);
        
        Assert.assertFalse(custom1.getCustomizer2Called());
        
        museum.customizer2();
        
        Assert.assertTrue(custom1.getCustomizer2Called());
        
        long[] c3 = museum.customizer3(null);
        Assert.assertNotNull(c3);
        Assert.assertEquals(0, c3.length);
        
        Assert.assertEquals(C4, museum.customizer4());
        Assert.assertEquals(C5, museum.customizer5());
        Assert.assertEquals(C6, museum.customizer6());
        Assert.assertEquals(0, Float.compare(C7, museum.customizer7()));
        Assert.assertEquals(0, Double.compare(C8, museum.customizer8()));
        Assert.assertEquals(C9, museum.customizer9());
        Assert.assertEquals(C10, museum.customizer10());
        Assert.assertEquals(C11, museum.customizer11());
        
        int varSize = museum.customizer12(C4, C5, C6, C7, C8, C9, C10, C11);
        Assert.assertEquals(0, varSize);
        
        int i1[] = new int[] { 1, 2, 3 };
        int i2[] = new int[] { 4, 5, 6, 7 };
        varSize = museum.customizer12(C4, C5, C6, C7, C8, C9, C10, C11, i1, i2);
        
        Assert.assertEquals(2, varSize);
        
        Assert.assertFalse(custom2.getFauxAddCalled());
        museum.addListener(null);
        Assert.assertTrue(custom2.getFauxAddCalled());
        
        String[] uppers = museum.toUpper(new String[] { "Go", "Eagles" });
        Assert.assertEquals("GO", uppers[0]);
        Assert.assertEquals("EAGLES", uppers[1]);
        Assert.assertEquals(2, uppers.length);
        
        try {
          museum.theVeryBadNotGoodMethod();
          if (badFails) {
              Assert.fail("The verby bad not good method should not have worked");
          }
        }
        catch (MultiException me) {
            if (!badFails) {
                throw me;
            }
        }
    }
    
    private final static String TEST_STRING = "TestString for ";
    
    /**
     * Tests that a customizer method can take a method with the bean
     * itself as the first argument
     * 
     * @throws Exception
     */
    @Test // @org.junit.Ignore
    public void testCustomizerWithBeanAsFirstArgument() throws Exception {
        ServiceLocator locator = Utilities.createLocator(SpecializedEmployeeCustomizer.class);
        XmlService xmlService = locator.getService(XmlService.class);
        
        URL url = getClass().getClassLoader().getResource(Commons.ACME1_FILE);
        
        XmlRootHandle<EmployeesBean> rootHandle = xmlService.unmarshal(url.toURI(), EmployeesBean.class);
        EmployeesBean employees = rootHandle.getRoot();
        Assert.assertNotNull(employees);
        
        EmployeeBean bob = locator.getService(EmployeeBean.class, Commons.BOB);
        EmployeeBean carol = locator.getService(EmployeeBean.class, Commons.CAROL);
        
        Assert.assertNotNull(bob);
        Assert.assertNotNull(carol);
        
        Assert.assertEquals(TEST_STRING + Commons.BOB, bob.doACustomThing(TEST_STRING));
        Assert.assertEquals(TEST_STRING + Commons.CAROL, carol.doACustomThing(TEST_STRING));
    }

}
