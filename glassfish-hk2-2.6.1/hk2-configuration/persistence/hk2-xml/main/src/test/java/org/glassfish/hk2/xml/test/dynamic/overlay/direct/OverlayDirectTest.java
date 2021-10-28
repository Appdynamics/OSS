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

package org.glassfish.hk2.xml.test.dynamic.overlay.direct;

import java.util.List;
import java.util.Map;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.configuration.hub.api.BeanDatabase;
import org.glassfish.hk2.configuration.hub.api.Change;
import org.glassfish.hk2.configuration.hub.api.Hub;
import org.glassfish.hk2.configuration.hub.api.Change.ChangeCategory;
import org.glassfish.hk2.configuration.hub.api.Instance;
import org.glassfish.hk2.configuration.hub.api.Type;
import org.glassfish.hk2.utilities.general.GeneralUtilities;
import org.glassfish.hk2.xml.api.XmlRootHandle;
import org.glassfish.hk2.xml.api.XmlService;
import org.glassfish.hk2.xml.test.beans.DiagnosticsBean;
import org.glassfish.hk2.xml.test.beans.DomainBean;
import org.glassfish.hk2.xml.test.dynamic.merge.MergeTest;
import org.glassfish.hk2.xml.test.dynamic.overlay.ChangeDescriptor;
import org.glassfish.hk2.xml.test.dynamic.overlay.OverlayUtilities;
import org.glassfish.hk2.xml.test.dynamic.rawsets.RawSetsTest;
import org.glassfish.hk2.xml.test.dynamic.rawsets.UpdateListener;
import org.glassfish.hk2.xml.test.utilities.Utilities;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author jwells
 *
 */
public class OverlayDirectTest {
    private final static String TERMINAL_DATA_A = "terminalDataA";
    private final static String TERMINAL_DATA_B = "terminalDataB";
    
    public final static String DIRECT_WITH_KEYED = "direct-with-keyed";
    public final static String DIRECT_WITH_UNKEYED = "direct-with-unkeyed";
    public final static String DIRECT_WITH_DIRECT = "direct-with-direct";
    public final static String DIRECT_TERMINAL = "direct-terminal";
    public final static String TERMINAL_DATA = "terminal-data";
    public final static String UNKEYED_TERMINAL = "unkeyed-terminal";
    public final static String UNKEYED_DATA = "unkeyed-data";
    
    private final static String DIRECT_WITH_KEYED_TYPE = OverlayUtilities.OROOT_TYPE_B + "/" + DIRECT_WITH_KEYED ;
    private final static String DIRECT_WITH_UNKEYED_TYPE = OverlayUtilities.OROOT_TYPE_B + "/" + DIRECT_WITH_UNKEYED ;
    private final static String DIRECT_WITH_DIRECT_TYPE = OverlayUtilities.OROOT_TYPE_B + "/" + DIRECT_WITH_DIRECT ;
    
    private final static String DIRECT_WITH_DIRECT_TERMINAL_TYPE = DIRECT_WITH_DIRECT_TYPE + "/" + DIRECT_TERMINAL;
    private final static String DIRECT_WITH_UNKEYED_TERMINAL_TYPE = DIRECT_WITH_UNKEYED_TYPE + "/" + UNKEYED_TERMINAL;
    
    private final static String DIRECT_WITH_DIRECT_INSTANCE = OverlayUtilities.OROOT_B + "." + DIRECT_WITH_DIRECT ;
    private final static String DIRECT_WITH_DIRECT_TERMINAL_INSTANCE = DIRECT_WITH_DIRECT_INSTANCE + "." + DIRECT_TERMINAL ;
    private final static String DIRECT_WITH_UNKEYED_INSTANCE = OverlayUtilities.OROOT_B + "." + DIRECT_WITH_UNKEYED;
    private final static String DIRECT_WITH_UNKEYED_TERMINAL_INSTANCE = DIRECT_WITH_UNKEYED_INSTANCE + ".*";
    
    
    /**
     * Tests adding a two-deep direct bean
     */
    @Test
    // @org.junit.Ignore
    public void testDirectWithDirectAdded() {
        ServiceLocator locator = Utilities.createLocator(UpdateListener.class);
        XmlService xmlService = locator.getService(XmlService.class);
        Hub hub = locator.getService(Hub.class);
        UpdateListener listener = locator.getService(UpdateListener.class);
        
        XmlRootHandle<OverlayRootBBean> originalHandle = createEmptyRoot(xmlService, true);
        XmlRootHandle<OverlayRootBBean> modifiedHandle = createEmptyRoot(xmlService, false);
        
        checkEmptyRootInHub(hub, locator);
        
        OverlayRootBBean modifiedRoot = modifiedHandle.getRoot();
        
        modifiedRoot.setDirectWithDirect(xmlService.createBean(DirectWithDirect.class));
        DirectWithDirect dwd = modifiedRoot.getDirectWithDirect();
        
        dwd.setDirectTerminal(xmlService.createBean(DirectTerminalBean.class));
        DirectTerminalBean dtb = dwd.getDirectTerminal();
        
        dtb.setTerminalData(TERMINAL_DATA_A);
        
        originalHandle.overlay(modifiedHandle);
        
        {
            // Check the bean itself
            OverlayRootBBean originalRoot = originalHandle.getRoot();
            Assert.assertNotNull(originalRoot.getDirectWithDirect());
            Assert.assertNull(originalRoot.getDirectWithKeyed());
            Assert.assertNull(originalRoot.getDirectWithUnkeyed());
        
            DirectWithDirect overlayDWD = originalRoot.getDirectWithDirect();
            DirectTerminalBean overlayDTB = overlayDWD.getDirectTerminal();
        
            Assert.assertNotNull(overlayDTB);
            Assert.assertEquals(TERMINAL_DATA_A, overlayDTB.getTerminalData());
        }
        
        {
            // Check the hub
            checkRootInHub(hub, locator);
            checkExists(hub, DIRECT_WITH_DIRECT_TYPE, DIRECT_WITH_DIRECT_INSTANCE, DirectWithDirect.class, locator);
            DirectTerminalBean service = checkExists(hub, DIRECT_WITH_DIRECT_TERMINAL_TYPE, DIRECT_WITH_DIRECT_TERMINAL_INSTANCE, DirectTerminalBean.class, locator);
            checkFieldInHub(hub, DIRECT_WITH_DIRECT_TERMINAL_TYPE, DIRECT_WITH_DIRECT_TERMINAL_INSTANCE, TERMINAL_DATA, TERMINAL_DATA_A);
            
            Assert.assertEquals(TERMINAL_DATA_A, service.getTerminalData());
        }
        
        List<Change> changes = listener.getChanges();
        
        OverlayUtilities.checkChanges(changes,
                new ChangeDescriptor(ChangeCategory.ADD_TYPE,
                        DIRECT_WITH_DIRECT_TYPE,    // type name
                        null,      // instance name
                        null
                )
                , new ChangeDescriptor(ChangeCategory.ADD_INSTANCE,
                        DIRECT_WITH_DIRECT_TYPE,    // type name
                        DIRECT_WITH_DIRECT_INSTANCE,      // instance name
                        null
                 )
                 , new ChangeDescriptor(ChangeCategory.ADD_TYPE,
                        DIRECT_WITH_DIRECT_TERMINAL_TYPE,    // type name
                        null,      // instance name
                        null
                 )
                 , new ChangeDescriptor(ChangeCategory.ADD_INSTANCE,
                         DIRECT_WITH_DIRECT_TERMINAL_TYPE,    // type name
                         DIRECT_WITH_DIRECT_TERMINAL_INSTANCE,      // instance name
                         null
                 )
                 , new ChangeDescriptor(ChangeCategory.MODIFY_INSTANCE,
                         OverlayUtilities.OROOT_TYPE_B,    // type name
                         OverlayUtilities.OROOT_B,      // instance name
                         null,
                         DIRECT_WITH_DIRECT
                 )
        );
    }
    
    /**
     * Tests removing a two-deep direct bean
     */
    @Test
    // @org.junit.Ignore
    public void testDirectWithDirectRemoved() {
        ServiceLocator locator = Utilities.createLocator(UpdateListener.class);
        XmlService xmlService = locator.getService(XmlService.class);
        Hub hub = locator.getService(Hub.class);
        UpdateListener listener = locator.getService(UpdateListener.class);
        
        XmlRootHandle<OverlayRootBBean> originalHandle = createEmptyRoot(xmlService, true);
        XmlRootHandle<OverlayRootBBean> modifiedHandle = createEmptyRoot(xmlService, false);
        
        OverlayRootBBean originalRoot = originalHandle.getRoot();
        
        originalRoot.setDirectWithDirect(xmlService.createBean(DirectWithDirect.class));
        DirectWithDirect dwd = originalRoot.getDirectWithDirect();
        
        dwd.setDirectTerminal(xmlService.createBean(DirectTerminalBean.class));
        DirectTerminalBean dtb = dwd.getDirectTerminal();
        
        dtb.setTerminalData(TERMINAL_DATA_A);
        
        {
            // Check pre-state of hub
            checkRootInHub(hub, locator);
            checkExists(hub, DIRECT_WITH_DIRECT_TYPE, DIRECT_WITH_DIRECT_INSTANCE, DirectWithDirect.class, locator);
            DirectTerminalBean service = checkExists(hub, DIRECT_WITH_DIRECT_TERMINAL_TYPE, DIRECT_WITH_DIRECT_TERMINAL_INSTANCE, DirectTerminalBean.class, locator);
            checkFieldInHub(hub, DIRECT_WITH_DIRECT_TERMINAL_TYPE, DIRECT_WITH_DIRECT_TERMINAL_INSTANCE, TERMINAL_DATA, TERMINAL_DATA_A);
            
            Assert.assertEquals(TERMINAL_DATA_A, service.getTerminalData());
        }
        
        originalHandle.overlay(modifiedHandle);
        
        {
            // Check the bean itself
            originalRoot = originalHandle.getRoot();
            Assert.assertNull(originalRoot.getDirectWithDirect());
            Assert.assertNull(originalRoot.getDirectWithKeyed());
            Assert.assertNull(originalRoot.getDirectWithUnkeyed());
        }
        
        {
            // Check the hub
            checkEmptyRootInHub(hub, locator);
        }
        
        List<Change> changes = listener.getChanges();
        
        OverlayUtilities.checkChanges(changes,
                new ChangeDescriptor(ChangeCategory.REMOVE_INSTANCE,
                        "/overlay-root-B/direct-with-direct",    // type name
                        "overlay-root-B.direct-with-direct",      // instance name
                        null
                )
                , new ChangeDescriptor(ChangeCategory.REMOVE_INSTANCE,
                        "/overlay-root-B/direct-with-direct/direct-terminal",    // type name
                        "overlay-root-B.direct-with-direct.direct-terminal",      // instance name
                        null
                )
                , new ChangeDescriptor(ChangeCategory.MODIFY_INSTANCE,
                         "/overlay-root-B",    // type name
                         "overlay-root-B",      // instance name
                         null,
                         "direct-with-direct"
                 )
        );
    }
    
    /**
     * Tests changing the data in a direct child two levels down
     */
    @Test
    // @org.junit.Ignore
    public void testDirectWithDirectChanged() {
        ServiceLocator locator = Utilities.createLocator(UpdateListener.class);
        XmlService xmlService = locator.getService(XmlService.class);
        Hub hub = locator.getService(Hub.class);
        UpdateListener listener = locator.getService(UpdateListener.class);
        
        XmlRootHandle<OverlayRootBBean> originalHandle = createEmptyRoot(xmlService, true);
        XmlRootHandle<OverlayRootBBean> modifiedHandle = createEmptyRoot(xmlService, false);
        
        {
            OverlayRootBBean originalRoot = originalHandle.getRoot();
        
            originalRoot.setDirectWithDirect(xmlService.createBean(DirectWithDirect.class));
            DirectWithDirect dwd = originalRoot.getDirectWithDirect();
        
            dwd.setDirectTerminal(xmlService.createBean(DirectTerminalBean.class));
            DirectTerminalBean dtb = dwd.getDirectTerminal();
        
            dtb.setTerminalData(TERMINAL_DATA_A);
        }
        
        {
            OverlayRootBBean modifiedRoot = modifiedHandle.getRoot();
        
            modifiedRoot.setDirectWithDirect(xmlService.createBean(DirectWithDirect.class));
            DirectWithDirect dwd = modifiedRoot.getDirectWithDirect();
        
            dwd.setDirectTerminal(xmlService.createBean(DirectTerminalBean.class));
            DirectTerminalBean dtb = dwd.getDirectTerminal();
        
            dtb.setTerminalData(TERMINAL_DATA_B);
        }
        
        {
            // Check pre-state of hub
            checkRootInHub(hub, locator);
            checkExists(hub, DIRECT_WITH_DIRECT_TYPE, DIRECT_WITH_DIRECT_INSTANCE, DirectWithDirect.class, locator);
            DirectTerminalBean service = checkExists(hub, DIRECT_WITH_DIRECT_TERMINAL_TYPE, DIRECT_WITH_DIRECT_TERMINAL_INSTANCE, DirectTerminalBean.class, locator);
            checkFieldInHub(hub, DIRECT_WITH_DIRECT_TERMINAL_TYPE, DIRECT_WITH_DIRECT_TERMINAL_INSTANCE, TERMINAL_DATA, TERMINAL_DATA_A);
            
            Assert.assertEquals(TERMINAL_DATA_A, service.getTerminalData());
        }
        
        originalHandle.overlay(modifiedHandle);
        
        {
            // Check the bean itself
            OverlayRootBBean originalRoot = originalHandle.getRoot();
            Assert.assertNotNull(originalRoot.getDirectWithDirect());
            Assert.assertNull(originalRoot.getDirectWithKeyed());
            Assert.assertNull(originalRoot.getDirectWithUnkeyed());
            
            DirectWithDirect dwd = originalRoot.getDirectWithDirect();
            DirectTerminalBean dtb = dwd.getDirectTerminal();
            Assert.assertNotNull(dtb);
            
            Assert.assertEquals(TERMINAL_DATA_B, dtb.getTerminalData());
        }
        
        {
            // Check the hub
            checkRootInHub(hub, locator);
            checkExists(hub, DIRECT_WITH_DIRECT_TYPE, DIRECT_WITH_DIRECT_INSTANCE, DirectWithDirect.class, locator);
            DirectTerminalBean service = checkExists(hub, DIRECT_WITH_DIRECT_TERMINAL_TYPE, DIRECT_WITH_DIRECT_TERMINAL_INSTANCE, DirectTerminalBean.class, locator);
            checkFieldInHub(hub, DIRECT_WITH_DIRECT_TERMINAL_TYPE, DIRECT_WITH_DIRECT_TERMINAL_INSTANCE, TERMINAL_DATA, TERMINAL_DATA_B);
            
            Assert.assertEquals(TERMINAL_DATA_B, service.getTerminalData());
        }
        
        List<Change> changes = listener.getChanges();
        
        OverlayUtilities.checkChanges(changes,
                new ChangeDescriptor(ChangeCategory.MODIFY_INSTANCE,
                         DIRECT_WITH_DIRECT_TERMINAL_TYPE,    // type name
                         DIRECT_WITH_DIRECT_TERMINAL_INSTANCE,      // instance name
                         null,
                         TERMINAL_DATA
                 )
        );
    }
    
    /**
     * Tests adding a two-deep direct bean
     */
    @Test
    // @org.junit.Ignore
    public void testDirectWithUnkeyedAdded() {
        ServiceLocator locator = Utilities.createLocator(UpdateListener.class);
        XmlService xmlService = locator.getService(XmlService.class);
        Hub hub = locator.getService(Hub.class);
        UpdateListener listener = locator.getService(UpdateListener.class);
        
        XmlRootHandle<OverlayRootBBean> originalHandle = createEmptyRoot(xmlService, true);
        XmlRootHandle<OverlayRootBBean> modifiedHandle = createEmptyRoot(xmlService, false);
        
        checkEmptyRootInHub(hub, locator);
        
        OverlayRootBBean modifiedRoot = modifiedHandle.getRoot();
        
        modifiedRoot.setDirectWithUnkeyed(xmlService.createBean(DirectWithUnkeyed.class));
        DirectWithUnkeyed dwu = modifiedRoot.getDirectWithUnkeyed();
        
        UnkeyedTerminalBean utb = dwu.addUnkeyedTerminal();
        utb.setUnkeyedData(TERMINAL_DATA_A);
        
        originalHandle.overlay(modifiedHandle);
        
        {
            // Check the bean itself
            OverlayRootBBean originalRoot = originalHandle.getRoot();
            Assert.assertNull(originalRoot.getDirectWithDirect());
            Assert.assertNull(originalRoot.getDirectWithKeyed());
            Assert.assertNotNull(originalRoot.getDirectWithUnkeyed());
        
            DirectWithUnkeyed overlayDWU = originalRoot.getDirectWithUnkeyed();
            UnkeyedTerminalBean overlayUTB = overlayDWU.getUnkeyedTerminal().get(0);
        
            Assert.assertNotNull(overlayUTB);
            Assert.assertEquals(TERMINAL_DATA_A, overlayUTB.getUnkeyedData());
        }
        
        {
            // Check the hub
            checkRootInHub(hub, locator);
            checkExists(hub, DIRECT_WITH_UNKEYED_TYPE, DIRECT_WITH_UNKEYED_INSTANCE, DirectWithUnkeyed.class, locator);
            UnkeyedTerminalBean service = checkExists(hub, DIRECT_WITH_UNKEYED_TERMINAL_TYPE, DIRECT_WITH_UNKEYED_TERMINAL_INSTANCE, UnkeyedTerminalBean.class, locator);
            checkFieldInHub(hub, DIRECT_WITH_UNKEYED_TERMINAL_TYPE, DIRECT_WITH_UNKEYED_TERMINAL_INSTANCE, UNKEYED_DATA, TERMINAL_DATA_A);
            
            Assert.assertEquals(TERMINAL_DATA_A, service.getUnkeyedData());
        }
        
        List<Change> changes = listener.getChanges();
        
        OverlayUtilities.checkChanges(changes,
                new ChangeDescriptor(ChangeCategory.ADD_TYPE,
                        DIRECT_WITH_UNKEYED_TYPE,    // type name
                        null,      // instance name
                        null
                )
                , new ChangeDescriptor(ChangeCategory.ADD_INSTANCE,
                        DIRECT_WITH_UNKEYED_TYPE,    // type name
                        DIRECT_WITH_UNKEYED_INSTANCE,      // instance name
                        null
                 )
                 , new ChangeDescriptor(ChangeCategory.ADD_TYPE,
                        DIRECT_WITH_UNKEYED_TERMINAL_TYPE,    // type name
                        null,      // instance name
                        null
                 )
                 , new ChangeDescriptor(ChangeCategory.ADD_INSTANCE,
                         DIRECT_WITH_UNKEYED_TERMINAL_TYPE,    // type name
                         DIRECT_WITH_UNKEYED_TERMINAL_INSTANCE,      // instance name
                         null
                 )
                 , new ChangeDescriptor(ChangeCategory.MODIFY_INSTANCE,
                         OverlayUtilities.OROOT_TYPE_B,    // type name
                         OverlayUtilities.OROOT_B,      // instance name
                         null,
                         DIRECT_WITH_UNKEYED
                 )
        );
    }
    
    /**
     * Tests changing the data in a direct child two levels down
     */
    @Test
    // @org.junit.Ignore
    public void testDirectWithUnkeyedChanged() {
        ServiceLocator locator = Utilities.createLocator(UpdateListener.class);
        XmlService xmlService = locator.getService(XmlService.class);
        Hub hub = locator.getService(Hub.class);
        UpdateListener listener = locator.getService(UpdateListener.class);
        
        XmlRootHandle<OverlayRootBBean> originalHandle = createEmptyRoot(xmlService, true);
        XmlRootHandle<OverlayRootBBean> modifiedHandle = createEmptyRoot(xmlService, false);
        
        {
            OverlayRootBBean originalRoot = originalHandle.getRoot();
        
            originalRoot.setDirectWithUnkeyed(xmlService.createBean(DirectWithUnkeyed.class));
            DirectWithUnkeyed dwu = originalRoot.getDirectWithUnkeyed();
        
            UnkeyedTerminalBean utb = dwu.addUnkeyedTerminal();
        
            utb.setUnkeyedData(TERMINAL_DATA_A);
        }
        
        {
            OverlayRootBBean modifiedRoot = modifiedHandle.getRoot();
        
            modifiedRoot.setDirectWithUnkeyed(xmlService.createBean(DirectWithUnkeyed.class));
            DirectWithUnkeyed dwu = modifiedRoot.getDirectWithUnkeyed();
        
            UnkeyedTerminalBean utb = dwu.addUnkeyedTerminal();
        
            utb.setUnkeyedData(TERMINAL_DATA_B);
        }
        
        {
            // Check pre-state of hub
            checkRootInHub(hub, locator);
            checkExists(hub, DIRECT_WITH_UNKEYED_TYPE, DIRECT_WITH_UNKEYED_INSTANCE, DirectWithUnkeyed.class, locator);
            UnkeyedTerminalBean service = checkExists(hub, DIRECT_WITH_UNKEYED_TERMINAL_TYPE, DIRECT_WITH_UNKEYED_TERMINAL_INSTANCE, UnkeyedTerminalBean.class, locator);
            checkFieldInHub(hub, DIRECT_WITH_UNKEYED_TERMINAL_TYPE, DIRECT_WITH_UNKEYED_TERMINAL_INSTANCE, UNKEYED_DATA, TERMINAL_DATA_A);
            
            Assert.assertEquals(TERMINAL_DATA_A, service.getUnkeyedData());
        }
        
        originalHandle.overlay(modifiedHandle);
        
        {
            // Check the bean itself
            OverlayRootBBean originalRoot = originalHandle.getRoot();
            Assert.assertNull(originalRoot.getDirectWithDirect());
            Assert.assertNull(originalRoot.getDirectWithKeyed());
            Assert.assertNotNull(originalRoot.getDirectWithUnkeyed());
            
            DirectWithUnkeyed dwu = originalRoot.getDirectWithUnkeyed();
            UnkeyedTerminalBean utb = dwu.getUnkeyedTerminal().get(0);
            Assert.assertNotNull(utb);
            
            Assert.assertEquals(TERMINAL_DATA_B, utb.getUnkeyedData());
        }
        
        {
            // Check the hub
            checkRootInHub(hub, locator);
            checkExists(hub, DIRECT_WITH_UNKEYED_TYPE, DIRECT_WITH_UNKEYED_INSTANCE, DirectWithUnkeyed.class, locator);
            UnkeyedTerminalBean service = checkExists(hub, DIRECT_WITH_UNKEYED_TERMINAL_TYPE, DIRECT_WITH_UNKEYED_TERMINAL_INSTANCE, UnkeyedTerminalBean.class, locator);
            checkFieldInHub(hub, DIRECT_WITH_UNKEYED_TERMINAL_TYPE, DIRECT_WITH_UNKEYED_TERMINAL_INSTANCE, UNKEYED_DATA, TERMINAL_DATA_B);
            
            Assert.assertEquals(TERMINAL_DATA_B, service.getUnkeyedData());
        }
        
        List<Change> changes = listener.getChanges();
        
        OverlayUtilities.checkChanges(changes,
                new ChangeDescriptor(ChangeCategory.MODIFY_INSTANCE,
                         DIRECT_WITH_UNKEYED_TERMINAL_TYPE,    // type name
                         DIRECT_WITH_UNKEYED_TERMINAL_INSTANCE,      // instance name
                         null,
                         UNKEYED_DATA
                 )
        );
    }
    
    /**
     * Tests removing a two-deep direct bean
     */
    @Test
    // @org.junit.Ignore
    public void testDirectWithUnkeyedRemoved() {
        ServiceLocator locator = Utilities.createLocator(UpdateListener.class);
        XmlService xmlService = locator.getService(XmlService.class);
        Hub hub = locator.getService(Hub.class);
        UpdateListener listener = locator.getService(UpdateListener.class);
        
        XmlRootHandle<OverlayRootBBean> originalHandle = createEmptyRoot(xmlService, true);
        XmlRootHandle<OverlayRootBBean> modifiedHandle = createEmptyRoot(xmlService, false);
        
        OverlayRootBBean originalRoot = originalHandle.getRoot();
        
        originalRoot.setDirectWithUnkeyed(xmlService.createBean(DirectWithUnkeyed.class));
        DirectWithUnkeyed dwu = originalRoot.getDirectWithUnkeyed();
        
        UnkeyedTerminalBean utb = dwu.addUnkeyedTerminal();
        
        utb.setUnkeyedData(TERMINAL_DATA_A);
        
        {
            // Check pre-state of hub
            checkRootInHub(hub, locator);
            checkExists(hub, DIRECT_WITH_UNKEYED_TYPE, DIRECT_WITH_UNKEYED_INSTANCE, DirectWithUnkeyed.class, locator);
            UnkeyedTerminalBean service = checkExists(hub, DIRECT_WITH_UNKEYED_TERMINAL_TYPE, DIRECT_WITH_UNKEYED_TERMINAL_INSTANCE, UnkeyedTerminalBean.class, locator);
            checkFieldInHub(hub, DIRECT_WITH_UNKEYED_TERMINAL_TYPE, DIRECT_WITH_UNKEYED_TERMINAL_INSTANCE, UNKEYED_DATA, TERMINAL_DATA_A);
            
            Assert.assertEquals(TERMINAL_DATA_A, service.getUnkeyedData());
        }
        
        originalHandle.overlay(modifiedHandle);
        
        {
            // Check the bean itself
            originalRoot = originalHandle.getRoot();
            Assert.assertNull(originalRoot.getDirectWithDirect());
            Assert.assertNull(originalRoot.getDirectWithKeyed());
            Assert.assertNull(originalRoot.getDirectWithUnkeyed());
        }
        
        {
            // Check the hub
            checkEmptyRootInHub(hub, locator);
        }
        
        List<Change> changes = listener.getChanges();
        
        OverlayUtilities.checkChanges(changes,
                new ChangeDescriptor(ChangeCategory.REMOVE_INSTANCE,
                        DIRECT_WITH_UNKEYED_TYPE,    // type name
                        DIRECT_WITH_UNKEYED_INSTANCE,      // instance name
                        null
                )
                , new ChangeDescriptor(ChangeCategory.REMOVE_INSTANCE,
                        DIRECT_WITH_UNKEYED_TERMINAL_TYPE,    // type name
                        DIRECT_WITH_UNKEYED_TERMINAL_INSTANCE,      // instance name
                        null
                )
                , new ChangeDescriptor(ChangeCategory.MODIFY_INSTANCE,
                        OverlayUtilities.OROOT_TYPE_B,    // type name
                        OverlayUtilities.OROOT_B,      // instance name
                        null,
                        DIRECT_WITH_UNKEYED
                 )
        );
    }
    
    /**
     * Tests changing a keyed direct child from one key to another
     */
    @Test
    // @org.junit.Ignore
    public void testChangeKeyedDirectChildWithOverlay() {
        ServiceLocator locator = Utilities.createLocator(UpdateListener.class);
        XmlService xmlService = locator.getService(XmlService.class);
        Hub hub = locator.getService(Hub.class);
        UpdateListener listener = locator.getService(UpdateListener.class);
        
        XmlRootHandle<DomainBean> rootHandle1 = xmlService.createEmptyHandle(DomainBean.class);
        rootHandle1.addRoot();
        
        DomainBean domain1 = rootHandle1.getRoot();
        
        DiagnosticsBean aliceBean = RawSetsTest.createDiagnosticsBean(xmlService, MergeTest.ALICE_NAME);
        domain1.setDiagnostics(aliceBean);
        
        XmlRootHandle<DomainBean> rootHandle2 = xmlService.createEmptyHandle(DomainBean.class, false, false);
        rootHandle2.addRoot();
        
        DomainBean domain2 = rootHandle2.getRoot();
        
        DiagnosticsBean bobBean = RawSetsTest.createDiagnosticsBean(xmlService, MergeTest.BOB_NAME);
        domain2.setDiagnostics(bobBean);
        
        {
            DiagnosticsBean checkBean = checkExists(hub, MergeTest.DIAGNOSTICS_TYPE,
                MergeTest.DOMAIN_INSTANCE + "." + MergeTest.ALICE_NAME,
                DiagnosticsBean.class, locator);
            
            Assert.assertEquals(MergeTest.ALICE_NAME, checkBean.getName());
            
            checkNotExistsInHub(hub, MergeTest.DIAGNOSTICS_TYPE, MergeTest.DOMAIN_INSTANCE + "." + MergeTest.BOB_NAME);
        }
        
        rootHandle1.overlay(rootHandle2);
        
        {
            checkNotExistsInHub(hub, MergeTest.DIAGNOSTICS_TYPE, MergeTest.DOMAIN_INSTANCE + "." + MergeTest.ALICE_NAME);
            
            DiagnosticsBean checkBean = checkExists(hub, MergeTest.DIAGNOSTICS_TYPE,
                MergeTest.DOMAIN_INSTANCE + "." + MergeTest.BOB_NAME,
                DiagnosticsBean.class, locator);
            
            Assert.assertEquals(MergeTest.BOB_NAME, checkBean.getName());
        }
        
        List<Change> changes = listener.getChanges();
        
        OverlayUtilities.checkChanges(changes,
                new ChangeDescriptor(ChangeCategory.REMOVE_INSTANCE,
                        MergeTest.DIAGNOSTICS_TYPE,    // type name
                        MergeTest.DOMAIN_INSTANCE + "." + MergeTest.ALICE_NAME,       // instance name
                        MergeTest.ALICE_NAME)
                , new ChangeDescriptor(ChangeCategory.ADD_INSTANCE,
                        MergeTest.DIAGNOSTICS_TYPE,    // type name
                        MergeTest.DOMAIN_INSTANCE + "." + MergeTest.BOB_NAME,       // instance name
                        MergeTest.BOB_NAME)
                , new ChangeDescriptor(ChangeCategory.MODIFY_INSTANCE,
                        MergeTest.DOMAIN_TYPE,    // type name
                        MergeTest.DOMAIN_INSTANCE,       // instance name
                        null,
                        "diagnostics") // prop changed
        );
    }
    
    private static XmlRootHandle<OverlayRootBBean> createEmptyRoot(XmlService xmlService, boolean advertise) {
        XmlRootHandle<OverlayRootBBean> retVal = xmlService.createEmptyHandle(OverlayRootBBean.class, advertise, advertise);
        retVal.addRoot();
        
        return retVal;
    }
    
    private static void checkExistsInHub(Hub hub, String type, String instance) {
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
    }
    
    private static <T> T checkExists(Hub hub, String type, String instance, Class<T> serviceClazz, ServiceLocator locator) {
        checkExistsInHub(hub, type, instance);
        
        T retVal = locator.getService(serviceClazz);
        
        Assert.assertNotNull(retVal);
        
        return retVal;
    }
    
    @SuppressWarnings("unchecked")
    private static void checkFieldInHub(Hub hub, String type, String instance, String field, Object value) {
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
        Assert.assertNotNull(i);
        
        Map<String, Object> bean = (Map<String, Object>) i.getBean();
        Assert.assertNotNull(bean);
        
        Object checkedValue = bean.get(field);
        Assert.assertTrue(GeneralUtilities.safeEquals(value, checkedValue));
    }
    
    private static void checkNotExistsInHub(Hub hub, String type, String instance) {
        BeanDatabase bd = hub.getCurrentDatabase();
        Type hubType = bd.getType(type);
        if (hubType == null) return;
        
        if (instance == null) {
            Assert.assertEquals(0, hubType.getInstances().size());
            return;
        }
        
        Instance i = hubType.getInstance(instance);
        Assert.assertNull(i);
    }
    
    private static void checkRootInHub(Hub hub, ServiceLocator locator) {
        checkExists(hub, OverlayUtilities.OROOT_TYPE_B, OverlayUtilities.OROOT_B, OverlayRootBBean.class, locator);
    }
    
    private static void checkEmptyRootInHub(Hub hub, ServiceLocator locator) {
        checkRootInHub(hub, locator);
        
        checkNotExistsInHub(hub, DIRECT_WITH_KEYED_TYPE, null);
        checkNotExistsInHub(hub, DIRECT_WITH_UNKEYED_TYPE, null);
        checkNotExistsInHub(hub, DIRECT_WITH_DIRECT_TYPE, DIRECT_WITH_DIRECT_INSTANCE);
        
        Assert.assertNull(locator.getService(DirectWithKeyed.class));
        Assert.assertNull(locator.getService(DirectWithUnkeyed.class));
        Assert.assertNull(locator.getService(DirectWithDirect.class));
        
        Assert.assertNull(locator.getService(KeyedTerminalBean.class));
        Assert.assertNull(locator.getService(UnkeyedTerminalBean.class));
        Assert.assertNull(locator.getService(DirectTerminalBean.class));
    }
}
