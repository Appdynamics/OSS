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

package org.glassfish.hk2.xml.test.precompile;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.xml.api.XmlRootHandle;
import org.glassfish.hk2.xml.api.XmlService;
import org.glassfish.hk2.xml.test.precompile.anno.EverythingBagel;
import org.glassfish.hk2.xml.test.precompile.anno.GreekEnum;
import org.glassfish.hk2.xml.test.precompile.dom.EntertainmentBean;
import org.glassfish.hk2.xml.test.precompile.dom.FootballBean;
import org.glassfish.hk2.xml.test.precompile.dom.SportsBean;
import org.glassfish.hk2.xml.test1.utilities.Utilities;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author jwells
 *
 */
public class PreCompiledDomTest {
    private final static String FREETIME_FILE = "freetime.xml";
    private final static String PRE_COMPILED_FILE = "pre-compiled.xml";
    private final static String SIMPLE_FILE = "simple.xml";
    private final static String ALICE = "Alice";
    private final static String BOB = "Bob";
    private final static String CAROL = "Carol";
    private final static String DAVE = "Dave";
    private final static String ENGLEBERT = "Englebert";
    
    /**
     * Checks to see that the pre-compiled class
     * is available prior to it getting generated,
     * and then that the system can see everything
     * properly
     */
    @Test // @org.junit.Ignore
    public void testPreCompiledGotPreCompile() throws Exception {
        PreCompiledTest.ensurePreCompilation();
        
        ServiceLocator locator = Utilities.createDomLocator(MyCustomizer.class);
        XmlService xmlService = locator.getService(XmlService.class);
        
        URL url = getClass().getClassLoader().getResource(PRE_COMPILED_FILE);
        
        XmlRootHandle<PreCompiledRoot> rootHandle = xmlService.unmarshal(url.toURI(), PreCompiledRoot.class);
        Assert.assertNotNull(rootHandle);
        
        PreCompiledRoot root = rootHandle.getRoot();
        
        Assert.assertEquals(ALICE, root.getPreCompiledMultiChild().get(0).getName());
        Assert.assertEquals("d1", root.getPreCompiledMultiChild().get(0).getData());
        
        Assert.assertEquals(BOB, root.getPreCompiledMultiChild().get(1).getName());
        Assert.assertEquals("d2", root.getPreCompiledMultiChild().get(1).getData());
        
        Assert.assertEquals(CAROL, root.getMultiChild().get(0).getName());
        Assert.assertEquals(1, root.getMultiChild().get(0).getFoo());
        
        Assert.assertEquals(DAVE, root.getMultiChild().get(1).getName());
        Assert.assertEquals(2, root.getMultiChild().get(1).getFoo());
        
        Assert.assertEquals(7001, root.getPreCompiledDirectChild().getPort());
        Assert.assertEquals("thirteen", root.getDirectChild().getIdentifier());
        
        PreCompiledArrayChild preCompiledArrayChildren[] = root.getPreCompiledArrayChild();
        Assert.assertEquals(1, preCompiledArrayChildren.length);
        
        Assert.assertEquals(ENGLEBERT, preCompiledArrayChildren[0].getName());
        Assert.assertEquals("foo", preCompiledArrayChildren[0].getAttribute());
        
        ArrayChild arrayChildren[] = root.getArrayChild();
        Assert.assertEquals(2, arrayChildren.length);
        
        Assert.assertEquals(1011L, arrayChildren[0].getTime());
        Assert.assertEquals(2022L, arrayChildren[1].getTime());
        
        MyCustomizer customizer = locator.getService(MyCustomizer.class);
        
        Assert.assertFalse(customizer.getACustomizedThingWithParametersCalled());
        root.aCustomizedThingWithParameters(10.00, null, null);
        Assert.assertTrue(customizer.getACustomizedThingWithParametersCalled());
        
        Assert.assertFalse(customizer.getACustomizedThingWithParameterCalled());
        CustomizedReturn cra[] = root.aCustomizedThingWithParameter(null);
        Assert.assertNull(cra);
        Assert.assertTrue(customizer.getACustomizedThingWithParameterCalled());
        
        Assert.assertFalse(customizer.getGetCustomizedReturnerCalled());
        cra = root.getCustomizedReturner();
        Assert.assertNotNull(cra);
        Assert.assertEquals(6, cra.length);
        Assert.assertTrue(customizer.getGetCustomizedReturnerCalled());
    }
    
    /**
     * The SimpleBean has no children, but has all kinds of other types
     * such as arrays
     * 
     * @throws Exception
     */
    @Test // @org.junit.Ignore
    public void testSimple() throws Exception {
        Assert.assertNotNull(PreCompiledTest.getAssociatedClass(SimpleBean.class));
        
        ServiceLocator locator = Utilities.createDomLocator(SimpleBeanCustomizer.class);
        XmlService xmlService = locator.getService(XmlService.class);
        
        URL url = getClass().getClassLoader().getResource(SIMPLE_FILE);
        
        XmlRootHandle<SimpleBean> rootHandle = xmlService.unmarshal(url.toURI(), SimpleBean.class);
        Assert.assertNotNull(rootHandle);
        
        SimpleBean root = rootHandle.getRoot();
        
        Assert.assertEquals(BOB, root.getName());
        
        Method setBagelMethod = root.getClass().getMethod("getBagelPreference", new Class<?>[] { });
        EverythingBagel bagel = setBagelMethod.getAnnotation(EverythingBagel.class);
        
        Assert.assertEquals((byte) 13, bagel.byteValue());
        Assert.assertTrue(bagel.booleanValue());
        Assert.assertEquals('e', bagel.charValue());
        Assert.assertEquals((short) 13, bagel.shortValue());
        Assert.assertEquals(13, bagel.intValue());
        Assert.assertEquals(13L, bagel.longValue());
        Assert.assertEquals(0, Float.compare((float) 13.00, bagel.floatValue()));
        Assert.assertEquals(0, Double.compare(13.00, bagel.doubleValue()));
        Assert.assertEquals("13", bagel.stringValue());
        Assert.assertEquals(PreCompiledRoot.class, bagel.classValue());
        Assert.assertEquals(GreekEnum.BETA, bagel.enumValue());
        
        Assert.assertTrue(Arrays.equals(new byte[] { 13, 14 }, bagel.byteArrayValue()));
        Assert.assertTrue(Arrays.equals(new boolean[] { true, false }, bagel.booleanArrayValue()));
        Assert.assertTrue(Arrays.equals(new char[] { 'e', 'E' }, bagel.charArrayValue()));
        Assert.assertTrue(Arrays.equals(new short[] { 13, 14 }, bagel.shortArrayValue()));
        Assert.assertTrue(Arrays.equals(new int[] { 13, 14 }, bagel.intArrayValue()));
        Assert.assertTrue(Arrays.equals(new long[] { 13, 14 }, bagel.longArrayValue()));
        Assert.assertTrue(Arrays.equals(new String[] { "13", "14" }, bagel.stringArrayValue()));
        Assert.assertTrue(Arrays.equals(new Class[] { String.class, double.class }, bagel.classArrayValue()));
        Assert.assertTrue(Arrays.equals(new GreekEnum[] { GreekEnum.GAMMA, GreekEnum.ALPHA }, bagel.enumArrayValue()));
        
        // The remaining need to be compared manually (not with Arrays)
        Assert.assertEquals(0, Float.compare((float) 13.00, bagel.floatArrayValue()[0]));
        Assert.assertEquals(0, Float.compare((float) 14.00, bagel.floatArrayValue()[1]));
        
        Assert.assertEquals(0, Double.compare(13.00, bagel.doubleArrayValue()[0]));
        Assert.assertEquals(0, Double.compare(14.00, bagel.doubleArrayValue()[1]));
        
        // Make sure Customizer is called when appropriate
        SimpleBeanCustomizer customizer = locator.getService(SimpleBeanCustomizer.class);
        
        Assert.assertFalse(customizer.getCustomizer12Called());
        Assert.assertEquals(13, root.customizer12(false, 13, 13L, (float) 13.00,
                13.00, (byte) 13, (short) 13, 'e', 1, 2, 3));
        Assert.assertTrue(customizer.getCustomizer12Called());
        
        Assert.assertFalse(customizer.getListenerCustomizerCalled());
        root.addListener(null, null, null, null, null, null, null);
        Assert.assertFalse(customizer.getListenerCustomizerCalled()); // because first item was null
        
        root.addListener(new boolean[] { true, false, true }, null, null, null, null, null, null);
        Assert.assertTrue(customizer.getListenerCustomizerCalled()); // because first item was not null
        
        BeanListenerInterfaceImpl interfaceArgument = new BeanListenerInterfaceImpl();
        root.customizer13(interfaceArgument);
        Assert.assertTrue(interfaceArgument.isCalled());
        
        WorkerClass worker = new WorkerClass();
        root.customizer14(worker);
        Assert.assertEquals(14, worker.returnFourteen());
    }
    
    /**
     * This is dom which allows lazy generation
     * 
     * @throws Exception
     */
    @Test // @org.junit.Ignore
    public void testLazyGeneration() throws Exception {
        PreCompiledTest.ensurePreCompilation();
        
        ServiceLocator locator = Utilities.createDomLocator(MyCustomizer.class);
        XmlService xmlService = locator.getService(XmlService.class);
        
        URL url = getClass().getClassLoader().getResource(FREETIME_FILE);
        
        XmlRootHandle<EntertainmentBean> rootHandle = xmlService.unmarshal(url.toURI(), EntertainmentBean.class);
        Assert.assertNotNull(rootHandle);
        
        EntertainmentBean root = rootHandle.getRoot();
        
        List<SportsBean> sports = root.getSports();
        Assert.assertNotNull(sports);
        Assert.assertEquals(1, sports.size());
        
        SportsBean sBean = sports.get(0);
        Assert.assertNotNull(sBean);
        
        List<FootballBean> football = sBean.getFootball();
        Assert.assertNotNull(football);
        Assert.assertEquals(1, football.size());
        
        FootballBean eaglesBean = football.get(0);
        
        Assert.assertEquals("Eagles", eaglesBean.getName());
    }
    
    private static class BeanListenerInterfaceImpl implements BeanListenerInterface {
        private boolean called;

        /* (non-Javadoc)
         * @see org.glassfish.hk2.xml.test.precompile.BeanListenerInterface#doSomething()
         */
        @Override
        public void doSomething() {
            called = true;
        }
        
        private boolean isCalled() {
            return called;
        }
        
    }

}
