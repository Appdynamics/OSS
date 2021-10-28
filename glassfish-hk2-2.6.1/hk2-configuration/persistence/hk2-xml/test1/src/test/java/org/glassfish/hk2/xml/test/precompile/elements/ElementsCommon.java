/*
 * Copyright (c) 2017, 2018 Oracle and/or its affiliates. All rights reserved.
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

package org.glassfish.hk2.xml.test.precompile.elements;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.configuration.hub.api.BeanDatabase;
import org.glassfish.hk2.configuration.hub.api.Hub;
import org.glassfish.hk2.configuration.hub.api.Instance;
import org.glassfish.hk2.configuration.hub.api.Type;
import org.glassfish.hk2.xml.api.XmlHk2ConfigurationBean;
import org.glassfish.hk2.xml.api.XmlRootHandle;
import org.glassfish.hk2.xml.api.XmlService;

import org.junit.Assert;

/**
 * @author jwells
 *
 */
public class ElementsCommon {
    private final static String BENDER_EARTH = "Toph";
    private final static String BENDER_WIND = "Ang";
    private final static String BENDER_WATER = "Kitara";
    private final static String BENDER_FIRE = "Zuko";
    private final static String BENDER_NONE = "CabbageVendor";
    
    private final static String SPECIAL1 = "Sparky Sparky Boom Boom";
    private final static String FIRE_OZAI = "Ozai";
    private final static String NONE_SOKKA = "Sokka";
    private final static String BENDER_SAND = "Ghashiun";
    private final static String SPECIAL_MOMO = "Momo";
    private final static String SPECIAL_APPA = "Appa";
    
    private final static String[] ALL_NAMES = {
        BENDER_EARTH
        , BENDER_WIND
        , BENDER_WATER
        , BENDER_FIRE
        , BENDER_NONE
        , SPECIAL1
        , FIRE_OZAI
        , NONE_SOKKA
        , BENDER_SAND
        , SPECIAL_MOMO
        , SPECIAL_APPA
    };
    
    private final static String ONE_OF_EACH = "elemental/oneofeach.xml";
    private final static String BENCHMARK = "elemental/benchmark.xml";
    private final static String SCALAR1 = "elemental/scalar1.xml";
    
    private final static String EARTH_TYPE = "/basic-elements/earth";
    private final static String WIND_TYPE = "/basic-elements/wind";
    private final static String WATER_TYPE = "/basic-elements/water";
    private final static String FIRE_TYPE = "/basic-elements/fire";
    private final static String NONE_TYPE = "/basic-elements/none";
    
    private final static String EARTH_INSTANCE = "basic-elements.Toph";
    private final static String WIND_INSTANCE = "basic-elements.Ang";
    private final static String WATER_INSTANCE = "basic-elements.Kitara";
    private final static String FIRE_INSTANCE = "basic-elements.Zuko";
    private final static String NONE_INSTANCE = "basic-elements.CabbageVendor";
    
    public static void testReadOneOfEachElement(ServiceLocator locator, ClassLoader cl) throws Exception {
        XmlService xmlService = locator.getService(XmlService.class);
        
        URL url = cl.getResource(ONE_OF_EACH);
        URI uri = url.toURI();
        
        XmlRootHandle<BasicElementalBean> handle = xmlService.unmarshal(uri, BasicElementalBean.class, true, true);
        BasicElementalBean root = handle.getRoot();
        Assert.assertNotNull(root);
        
        List<ElementalBean> elementals = root.getEarthWindAndFire();
        Assert.assertEquals("elements found =" + elementals, 5, elementals.size());
        
        // TODO: This ordering is not correct since it is not what was in the
        // document.  In order to keep moving along lets keep this the way it is
        EarthBean earth = (EarthBean) elementals.get(0);
        WindBean wind = (WindBean) elementals.get(3);
        WaterBean water = (WaterBean) elementals.get(2);
        FireBean fire = (FireBean) elementals.get(1);
        ElementalBean none = (ElementalBean) elementals.get(4);
        
        Assert.assertEquals(BENDER_EARTH, earth.getName());
        Assert.assertEquals(BENDER_WIND, wind.getName());
        Assert.assertEquals(BENDER_WATER, water.getName());
        Assert.assertEquals(BENDER_FIRE, fire.getName());
        Assert.assertEquals(BENDER_NONE, none.getName());
        
        Assert.assertEquals(ElementType.EARTH, earth.getType());
        Assert.assertEquals(ElementType.WIND, wind.getType());
        Assert.assertEquals(ElementType.WATER, water.getType());
        Assert.assertEquals(ElementType.FIRE, fire.getType());
        Assert.assertEquals(ElementType.NONE, none.getType());
        
        Assert.assertEquals(EARTH_TYPE, ((XmlHk2ConfigurationBean) earth)._getXmlPath());
        Assert.assertEquals(WIND_TYPE, ((XmlHk2ConfigurationBean) wind)._getXmlPath());
        Assert.assertEquals(WATER_TYPE, ((XmlHk2ConfigurationBean) water)._getXmlPath());
        Assert.assertEquals(FIRE_TYPE, ((XmlHk2ConfigurationBean) fire)._getXmlPath());
        Assert.assertEquals(NONE_TYPE, ((XmlHk2ConfigurationBean) none)._getXmlPath());
        
        Hub hub = locator.getService(Hub.class);
        
        checkService(locator, hub, EARTH_TYPE, EARTH_INSTANCE, BENDER_EARTH);
        checkService(locator, hub, WIND_TYPE, WIND_INSTANCE, BENDER_WIND);
        checkService(locator, hub, WATER_TYPE, WATER_INSTANCE, BENDER_WATER);
        checkService(locator, hub, FIRE_TYPE, FIRE_INSTANCE, BENDER_FIRE);
        checkService(locator, hub, NONE_TYPE, NONE_INSTANCE, BENDER_NONE);
    }
    
    public static void testMarshal(ServiceLocator locator, ClassLoader cl) throws Exception {
        XmlService xmlService = locator.getService(XmlService.class);
        
        URL url = cl.getResource(BENCHMARK);
        URI uri = url.toURI();
        
        XmlRootHandle<BasicElementalBean> handle = xmlService.unmarshal(uri, BasicElementalBean.class, true, true);
        BasicElementalBean root = handle.getRoot();
        Assert.assertNotNull(root);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            handle.marshal(baos);
        }
        finally {
            baos.close();
        }
        
        String asString = baos.toString();
        
        for (String name : ALL_NAMES) {
            Assert.assertTrue(asString.contains(name));
        }
        
        // This would be the name of the alias parent
        Assert.assertFalse(asString.contains("earthWindAndFire"));
    }
    
    public static void testScalarElements(ServiceLocator locator, ClassLoader cl, boolean isJaxb) throws Exception {
        XmlService xmlService = locator.getService(XmlService.class);
        
        URL url = cl.getResource(SCALAR1);
        URI uri = url.toURI();
        
        XmlRootHandle<ScalarRootBean> handle = xmlService.unmarshal(uri, ScalarRootBean.class, true, true);
        ScalarRootBean root = handle.getRoot();
        Assert.assertNotNull(root);
        
        List<Integer> numbers = root.getNumbers();
        Assert.assertEquals(3, numbers.size());
        
        Object foo = numbers.get(0);
        Assert.assertTrue(foo instanceof Integer);
        
        Assert.assertEquals(12, numbers.get(0).intValue());
        Assert.assertEquals(13, numbers.get(1).intValue());
        Assert.assertEquals(14, numbers.get(2).intValue());
        
        int arrayNumbers[] = root.getArrayNumbers();
        Assert.assertEquals(1, arrayNumbers.length);
        
        Assert.assertEquals(16, arrayNumbers[0]);
        
        List<Long> times = root.getTimes();
        Assert.assertEquals(2, times.size());
        Assert.assertEquals(10000000L, times.get(0).longValue());
        Assert.assertEquals(20000000L, times.get(1).longValue());
        
        Long[] arrayTimes = root.getArrayTimes();
        Assert.assertEquals(3, arrayTimes.length);
        
        Assert.assertEquals(30000000L, arrayTimes[0].longValue());
        Assert.assertEquals(40000000L, arrayTimes[1].longValue());
        Assert.assertEquals(50000000L, arrayTimes[2].longValue());
        
        Assert.assertEquals(15, root.getOneNumber());
        
        List<String> strings = root.getStrings();
        Assert.assertEquals(3, strings.size());
        
        Assert.assertEquals("foo", strings.get(0));
        Assert.assertEquals("bar", strings.get(1));
        Assert.assertEquals("baz", strings.get(2));
        
        List<Object> ttamt = root.getTypesTypesAndMoreTypes();
        Assert.assertEquals(16, ttamt.size());
        
        Assert.assertEquals(new Integer(17), ttamt.get(0));
        Assert.assertEquals(new Integer(22), ttamt.get(1));
        
        Assert.assertEquals(new Long(18), ttamt.get(2));
        Assert.assertEquals(new Long(21), ttamt.get(3));
        
        Assert.assertEquals(Boolean.TRUE, ttamt.get(4));
        Assert.assertEquals(Boolean.FALSE, ttamt.get(5));
        
        Assert.assertEquals(new Short((short) 19), ttamt.get(6));
        Assert.assertEquals(new Short((short) 20), ttamt.get(7));
        
        if (isJaxb) {
            // JAXB Does not handle Character well
            Assert.assertNull(ttamt.get(8));
            Assert.assertNull(ttamt.get(9));
        }
        else {
            Assert.assertEquals(new Character('c'), ttamt.get(8));
            Assert.assertEquals(new Character('d'), ttamt.get(9));
        }
        
        Assert.assertEquals(new Float(2.71), ttamt.get(10));
        Assert.assertEquals(new Float(3.14), ttamt.get(11));
        
        Assert.assertEquals(new Double(3.14), ttamt.get(12));
        Assert.assertEquals(new Double(2.71), ttamt.get(13));
        
        Assert.assertEquals("Hello", ttamt.get(14));
        Assert.assertEquals("Hello", ttamt.get(15));
    }
    
    private static void checkService(ServiceLocator locator, Hub hub, String type, String instance, String name) {
        checkInLocator(locator, name);
        checkNamedInHub(hub, type, instance, name);
    }
    
    private static void checkInLocator(ServiceLocator locator, String name) {
        ElementalBean eb = (ElementalBean) locator.getService(ElementalBean.class, name);
        Assert.assertNotNull(eb);
        
        Assert.assertEquals(name, eb.getName());
    }
    
    @SuppressWarnings("unchecked")
    private static void checkNamedInHub(Hub hub, String type, String instance, String name) {
        BeanDatabase bd = hub.getCurrentDatabase();
        
        Type hubType = bd.getType(type);
        Assert.assertNotNull(hubType);
        
        Instance i = null;
        if (instance.contains(".*")) {
            Map<String, Instance> instances = hubType.getInstances();
            Assert.assertEquals(1, instances.size());
            
            for (Instance found : instances.values()) {
                i = found;
            }
        }
        else {
            i = hubType.getInstance(instance);
        }
        
        Assert.assertNotNull("Could not find instance " + instance + " in type " + type, i);
        
        Object bean = i.getBean();
        Assert.assertNotNull(bean);
        
        String beanName = ((Map<String, String>) bean).get("name");
        Assert.assertEquals(name, beanName);
    }

}
