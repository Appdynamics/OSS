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

package org.glassfish.hk2.json.test.basic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.configuration.hub.api.BeanDatabase;
import org.glassfish.hk2.configuration.hub.api.Hub;
import org.glassfish.hk2.configuration.hub.api.Instance;
import org.glassfish.hk2.configuration.hub.api.Type;
import org.glassfish.hk2.json.api.JsonUtilities;
import org.glassfish.hk2.json.test.skillzbeans.JsonRootBean;
import org.glassfish.hk2.json.test.skillzbeans.SkillBean;
import org.glassfish.hk2.json.test.skillzbeans.SpecificSkillBean;
import org.glassfish.hk2.json.test.utilities.Utilities;
import org.glassfish.hk2.xml.api.XmlHk2ConfigurationBean;
import org.glassfish.hk2.xml.api.XmlRootHandle;
import org.glassfish.hk2.xml.api.XmlService;
import org.glassfish.hk2.xml.api.XmlServiceUtilities;
import org.glassfish.hk2.xml.spi.XmlServiceParser;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author jwells
 *
 */
public class JsonParserTest {
    private final static String SKILLZ_FILE = "skillz.json";
    
    public final static String SKILLZ = "skillz";
    
    public final static String WEB = "web";
    public final static String DB = "database";
    
    private final static String HTML = "html";
    private final static String CSS = "css";
    
    private final static String SQL = "sql";
    
    private final static String NAME_TAG = "name";
    private final static String YEARS_TAG = "years";
    
    /**
     * Tests a basic bean can be marshalled
     */
    @Test
    // @org.junit.Ignore
    public void testBasicMarshal() throws Exception {
        ServiceLocator locator = Utilities.enableLocator();
        Hub hub = locator.getService(Hub.class);
        
        XmlService jsonService = locator.getService(XmlService.class, JsonUtilities.JSON_SERVICE_NAME);
        Assert.assertNotNull(jsonService);
        
        URL url = getClass().getClassLoader().getResource(SKILLZ_FILE);
        URI uri = url.toURI();
        
        XmlRootHandle<JsonRootBean> skillBeanHandle = jsonService.unmarshal(uri, JsonRootBean.class);
        checkStandardDocument(skillBeanHandle, hub, locator);
        
    }
    
    /**
     * Tests a basic bean can be marshalled
     */
    @Test
    // @org.junit.Ignore
    public void testBasicUnmarshal() throws Exception {
        ServiceLocator locator = Utilities.enableLocator();
        Hub hub = locator.getService(Hub.class);
        
        XmlService jsonService = locator.getService(XmlService.class, JsonUtilities.JSON_SERVICE_NAME);
        
        XmlRootHandle<JsonRootBean> rootHandle = createStandardDocument(jsonService, hub, locator);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            rootHandle.marshal(baos);
        }
        finally {
            baos.close();
        }
        
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        
        XmlRootHandle<JsonRootBean> reupRoot = jsonService.unmarshal(bais, JsonRootBean.class);
        checkStandardDocument(reupRoot, hub, locator);
    }
    
    /**
     * Tests a basic bean can be marshalled
     */
    @Test
    // @org.junit.Ignore
    public void testConvertToXml() throws Exception {
        ServiceLocator locator = Utilities.enableLocator();
        XmlServiceUtilities.enableDomXmlService(locator);
        
        Hub hub = locator.getService(Hub.class);
        
        XmlService jsonService = locator.getService(XmlService.class, JsonUtilities.JSON_SERVICE_NAME);
        XmlService xmlService = locator.getService(XmlService.class, XmlServiceParser.STREAM_PARSING_SERVICE);
        
        XmlRootHandle<JsonRootBean> rootHandle = createStandardDocument(jsonService, hub, locator);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            xmlService.marshal(baos, rootHandle);
        }
        finally {
            baos.close();
        }
        
        XmlServiceUtilities.enableXmlService(locator);
        XmlService jaxbService = locator.getService(XmlService.class, XmlServiceParser.DEFAULT_PARSING_SERVICE);
        
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        try {
            XmlRootHandle<JsonRootBean> jaxbHandle = jaxbService.unmarshal(bais, JsonRootBean.class, false, false);
            
            checkStandardDocument(jaxbHandle, hub, locator);
        }
        finally {
            bais.close();
        }
        
    }
    
    private static XmlRootHandle<JsonRootBean> createStandardDocument(XmlService jsonService, Hub hub, ServiceLocator locator) {
       XmlRootHandle<JsonRootBean> retVal = jsonService.createEmptyHandle(JsonRootBean.class);
       
       retVal.addRoot();
       JsonRootBean root = retVal.getRoot();
       
       SkillBean skillBean = jsonService.createBean(SkillBean.class);
       root.setSkillz(skillBean);
       skillBean = root.getSkillz();
       
       skillBean.addWebBean(createSpecificSkillBean(jsonService, HTML, 5));
       skillBean.addWebBean(createSpecificSkillBean(jsonService, CSS, 3));
       
       skillBean.addDatabaseBean(createSpecificSkillBean(jsonService, SQL, 7));
       
       checkStandardDocument(retVal, hub, locator);
       
       return retVal;
    }
    
    private static SpecificSkillBean createSpecificSkillBean(XmlService jsonService, String name, int years) {
        SpecificSkillBean retVal = jsonService.createBean(SpecificSkillBean.class);
        
        retVal.setName(name);
        retVal.setYears(years);
        
        return retVal;
    }
    
    private static final String JSON_ROOT_TYPE = "/json-root-bean";
    private static final String JSON_ROOT_INSTANCE = "json-root-bean";
    
    private static final String SKILLZ_TYPE = JSON_ROOT_TYPE + "/skillz";
    private static final String SKILLZ_INSTANCE = JSON_ROOT_INSTANCE + ".skillz";
    
    private static final String WEB_TYPE = SKILLZ_TYPE + "/web";
    private static final String HTML_INSTANCE = SKILLZ_INSTANCE + ".html";
    private static final String CSS_INSTANCE = SKILLZ_INSTANCE + ".css";
    
    private static final String DB_TYPE = SKILLZ_TYPE + "/database";
    private static final String SQL_INSTANCE = SKILLZ_INSTANCE + ".sql";
    
    private static void checkStandardDocument(XmlRootHandle<JsonRootBean> skillBeanHandle, Hub hub, ServiceLocator locator) {
        JsonRootBean root = skillBeanHandle.getRoot();
        checkParent(root, null);
        
        Assert.assertNotNull(locator.getService(JsonRootBean.class));
        checkInHub(hub, JSON_ROOT_TYPE, JSON_ROOT_INSTANCE);
        
        SkillBean skillzBean = skillBeanHandle.getRoot().getSkillz();
        checkParent(skillzBean, root);
        
        Assert.assertNotNull(locator.getService(SkillBean.class));
        checkInHub(hub, SKILLZ_TYPE, SKILLZ_INSTANCE);
        
        List<SpecificSkillBean> specificsList = skillzBean.getWebBean();
        Assert.assertEquals(2, specificsList.size());
        
        for (int lcv = 0; lcv < specificsList.size(); lcv++) {
            SpecificSkillBean specific = specificsList.get(lcv);
            checkParent(specific, skillzBean);
            
            if (lcv == 0) {
                Assert.assertEquals(HTML, specific.getName());
                Assert.assertEquals(5, specific.getYears());
                
                Assert.assertNotNull(locator.getService(SpecificSkillBean.class, HTML));
                
                Map<String, Object> fields = new HashMap<String, Object>();
                fields.put(NAME_TAG, HTML);
                fields.put(YEARS_TAG, new Integer(5));
                
                checkInHub(hub, WEB_TYPE, HTML_INSTANCE, fields);
            }
            else if (lcv == 1) {
                Assert.assertEquals(CSS, specific.getName());
                Assert.assertEquals(3, specific.getYears());
                
                Assert.assertNotNull(locator.getService(SpecificSkillBean.class, CSS));
                
                Map<String, Object> fields = new HashMap<String, Object>();
                fields.put(NAME_TAG, CSS);
                fields.put(YEARS_TAG, new Integer(3));
                
                checkInHub(hub, WEB_TYPE, CSS_INSTANCE, fields);
            }
            else {
                Assert.fail("Unknown index " + lcv);
            }
        }
        
        SpecificSkillBean specifics[] = skillzBean.getDatabaseBean();
        Assert.assertEquals(1, specifics.length);
        
        for (int lcv = 0; lcv < specifics.length; lcv++) {
            SpecificSkillBean specific = specifics[lcv];
            checkParent(specific, skillzBean);
            
            Assert.assertEquals(SQL, specific.getName());
            Assert.assertEquals(7, specific.getYears());
            
            Assert.assertNotNull(locator.getService(SpecificSkillBean.class, SQL));
            
            Map<String, Object> fields = new HashMap<String, Object>();
            fields.put(NAME_TAG, SQL);
            fields.put(YEARS_TAG, new Integer(7));
            
            checkInHub(hub, DB_TYPE, SQL_INSTANCE, fields);
        }
    }
    
    @SuppressWarnings("unchecked")
    private static void checkInHub(Hub hub, String type, String instance, Map<String, Object> fields) {
        BeanDatabase bd = hub.getCurrentDatabase();
        
        Type bdType = bd.getType(type);
        Assert.assertNotNull("Did not find type of name " + type, bdType);
        
        Instance i = bdType.getInstance(instance);
        Assert.assertNotNull("Did not find instance of name " + instance + " in type " + type, i);
        
        if (fields == null || fields.isEmpty()) return;
        
        Map<String, Object> bean = (Map<String, Object>) i.getBean();
        Assert.assertNotNull(bean);
        
        for (Map.Entry<String, Object> entry : fields.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            
            Object fromBeanValue = bean.get(key);
            Assert.assertEquals(value, fromBeanValue);
        }
    }
    
    private static void checkInHub(Hub hub, String type, String instance) {
        checkInHub(hub, type, instance, null);
    }
    
    private static void checkParent(Object bean, Object parent) {
        Assert.assertNotNull(bean);
        
        Assert.assertTrue(bean instanceof XmlHk2ConfigurationBean);
        XmlHk2ConfigurationBean configBean = (XmlHk2ConfigurationBean) bean;
        
        XmlHk2ConfigurationBean parentBean = null;
        if (parent != null) {
            Assert.assertTrue(parent instanceof XmlHk2ConfigurationBean);
            parentBean = (XmlHk2ConfigurationBean) parent;
        }
        
        Assert.assertEquals(parentBean, configBean._getParent());
        
    }

}
