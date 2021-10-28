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

package org.glassfish.hk2.configuration.hub.test;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.glassfish.hk2.api.ActiveDescriptor;
import org.glassfish.hk2.api.MultiException;
import org.glassfish.hk2.configuration.hub.api.Change;
import org.glassfish.hk2.configuration.hub.api.CommitFailedException;
import org.glassfish.hk2.configuration.hub.api.Hub;
import org.glassfish.hk2.configuration.hub.api.Instance;
import org.glassfish.hk2.configuration.hub.api.PrepareFailedException;
import org.glassfish.hk2.configuration.hub.api.RollbackFailedException;
import org.glassfish.hk2.configuration.hub.api.Type;
import org.glassfish.hk2.configuration.hub.api.WriteableBeanDatabase;
import org.glassfish.hk2.configuration.hub.api.WriteableType;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author jwells
 *
 */
public class HubTest extends HubTestBase {
    private final static String EMPTY_TYPE = "EmptyType";
    private final static String ONE_INSTANCE_TYPE = "OneInstanceType";
    private final static String TYPE_TWO = "TypeTwo";
    private final static String TYPE_THREE = "TypeThree";
    private final static String TYPE_FOUR = "TypeFour";
    private final static String TYPE_FIVE = "TypeFive";
    private final static String TYPE_SIX = "TypeSix";
    private final static String TYPE_SEVEN = "TypeSeven";
    private final static String TYPE_EIGHT = "TypeEight";
    private final static String TYPE_NINE = "TypeNine";
    private final static String TYPE_TEN = "TypeTen";
    private final static String TYPE_ELEVEN = "TypeEleven";
    private final static String TYPE_TWELVE = "TypeTwelve";
    
    private final static String NAME_PROPERTY = "name";
    private final static String OTHER_PROPERTY = "other";
    
    private final static String ALICE = "Alice";
    private final static String BOB = "Bob";
    private final static String CAROL = "Carol";
    private final static String DAVE = "Dave";
    
    private final static String OTHER_PROPERTY_VALUE1 = "value1";
    private final static String OTHER_PROPERTY_VALUE2 = "value2";
    
    public final static String PREPARE_FAIL_MESSAGE = "Expected prepare exception";
    public final static String COMMIT_FAIL_MESSAGE = "Expected commit exception";
    
    private Map<String, Object> oneFieldBeanLikeMap = new HashMap<String, Object>();
    
    @Before
    public void before() {
        super.before();
        
        oneFieldBeanLikeMap.put(NAME_PROPERTY, ALICE);
    }
    
    /**
     * Tests we can add an empty type to the database
     */
    @Test
    public void testAddEmptyType() {
        Assert.assertNull(hub.getCurrentDatabase().getType(EMPTY_TYPE));
        
        WriteableBeanDatabase wbd = hub.getWriteableDatabaseCopy();
        wbd.addType(EMPTY_TYPE);
        
        wbd.commit();
        
        try {
            Type emptyType = hub.getCurrentDatabase().getType(EMPTY_TYPE);
            
            Assert.assertNotNull(emptyType);
            Assert.assertEquals(0, emptyType.getInstances().size());
        }
        finally {
            // Cleanup
            wbd = hub.getWriteableDatabaseCopy();
            wbd.removeType(EMPTY_TYPE);
            wbd.commit();
        }
        
    }
    
    /**
     * Tests we can add an empty type to the database with a listener
     */
    @Test
    public void testAddEmptyTypeWithListener() {
        Assert.assertNull(hub.getCurrentDatabase().getType(EMPTY_TYPE));
        
        GenericBeanDatabaseUpdateListener listener = new GenericBeanDatabaseUpdateListener();
        ActiveDescriptor<?> listenerDescriptor = ServiceLocatorUtilities.addOneConstant(testLocator, listener);
        
        WriteableBeanDatabase wbd = null;
        
        try {
            Hub hub = testLocator.getService(Hub.class);
            
            wbd = hub.getWriteableDatabaseCopy();
            wbd.addType(EMPTY_TYPE);
        
            wbd.commit();
        
            Type emptyType = hub.getCurrentDatabase().getType(EMPTY_TYPE);
            
            Assert.assertNull(listener.getLastCommitMessage());
            List<Change> changes = listener.getLastSetOfChanges();
            
            Assert.assertEquals(1, changes.size());
            
            Change change = changes.get(0);
            
            Assert.assertEquals(Change.ChangeCategory.ADD_TYPE, change.getChangeCategory());
            Assert.assertEquals(emptyType.getName(), change.getChangeType().getName());
            Assert.assertEquals(0, change.getChangeType().getInstances().size());
            Assert.assertNull(change.getInstanceKey());
            Assert.assertNull(change.getInstanceValue());
            Assert.assertNull(change.getModifiedProperties());
            Assert.assertNull(change.getOriginalInstanceValue());
        }
        finally {
            // Cleanup
            if (wbd != null) {
                wbd = hub.getWriteableDatabaseCopy();
                wbd.removeType(EMPTY_TYPE);
                wbd.commit();
            }
            
            ServiceLocatorUtilities.removeOneDescriptor(testLocator, listenerDescriptor);
        }
        
    }
    
    /**
     * Tests adding a type with one instance
     */
    @Test
    public void addNewTypeWithOneInstance() {
        Assert.assertNull(hub.getCurrentDatabase().getType(ONE_INSTANCE_TYPE));
        
        GenericBeanDatabaseUpdateListener listener = new GenericBeanDatabaseUpdateListener();
        ActiveDescriptor<?> listenerDescriptor = ServiceLocatorUtilities.addOneConstant(testLocator, listener);
        
        WriteableBeanDatabase wbd = null;
        
        try {
        
            wbd = hub.getWriteableDatabaseCopy();
            WriteableType wt = wbd.addType(ONE_INSTANCE_TYPE);
            
            wt.addInstance(ALICE, oneFieldBeanLikeMap);
        
            Object commitMessage = new Object();
            wbd.commit(commitMessage);
        
            Type oneInstanceType = hub.getCurrentDatabase().getType(ONE_INSTANCE_TYPE);
            
            Assert.assertEquals(commitMessage, listener.getLastCommitMessage());
            List<Change> changes = listener.getLastSetOfChanges();
            
            Assert.assertEquals(2, changes.size());
            
            {
                Change typeChange = changes.get(0);
            
                Assert.assertEquals(Change.ChangeCategory.ADD_TYPE, typeChange.getChangeCategory());
                Assert.assertEquals(oneInstanceType.getName(), typeChange.getChangeType().getName());
                Assert.assertEquals(1, typeChange.getChangeType().getInstances().size());
                Assert.assertNull(typeChange.getInstanceKey());
                Assert.assertNull(typeChange.getInstanceValue());
                Assert.assertNull(typeChange.getModifiedProperties());
                Assert.assertNull(typeChange.getOriginalInstanceValue());
            }
            
            {
                Change instanceChange = changes.get(1);
            
                Assert.assertEquals(Change.ChangeCategory.ADD_INSTANCE, instanceChange.getChangeCategory());
                Assert.assertEquals(oneInstanceType.getName(), instanceChange.getChangeType().getName());
                Assert.assertEquals(1, instanceChange.getChangeType().getInstances().size());
                Assert.assertEquals(ALICE, instanceChange.getInstanceKey());
                Assert.assertEquals(oneFieldBeanLikeMap, instanceChange.getInstanceValue().getBean());
                Assert.assertNull(instanceChange.getModifiedProperties());
                Assert.assertNull(instanceChange.getOriginalInstanceValue());
            }
        }
        finally {
            // Cleanup
            if (wbd != null) {
                wbd = hub.getWriteableDatabaseCopy();
                wbd.removeType(ONE_INSTANCE_TYPE);
                wbd.commit();
            }
            
            ServiceLocatorUtilities.removeOneDescriptor(testLocator, listenerDescriptor);
        }
        
    }
    
    
    
    /**
     * Tests adding an instance to an existing a type
     */
    @Test
    public void addInstanceToExistingType() {
        addType(ONE_INSTANCE_TYPE);
        
        GenericBeanDatabaseUpdateListener listener = null;
        WriteableBeanDatabase wbd = null;
        ActiveDescriptor<?> listenerDescriptor = null;
        
        try {
            listener = new GenericBeanDatabaseUpdateListener();
            listenerDescriptor = ServiceLocatorUtilities.addOneConstant(testLocator, listener);
        
            wbd = hub.getWriteableDatabaseCopy();
            WriteableType wt = wbd.getWriteableType(ONE_INSTANCE_TYPE);
            Assert.assertNotNull(wt);
            
            wt.addInstance(ALICE, oneFieldBeanLikeMap);
        
            wbd.commit();
        
            Type oneInstanceType = hub.getCurrentDatabase().getType(ONE_INSTANCE_TYPE);
            
            List<Change> changes = listener.getLastSetOfChanges();
            
            Assert.assertEquals(1, changes.size());
            
            {
                Change instanceChange = changes.get(0);
            
                Assert.assertEquals(Change.ChangeCategory.ADD_INSTANCE, instanceChange.getChangeCategory());
                Assert.assertEquals(oneInstanceType.getName(), instanceChange.getChangeType().getName());
                Assert.assertEquals(1, instanceChange.getChangeType().getInstances().size());
                Assert.assertEquals(ALICE, instanceChange.getInstanceKey());
                Assert.assertEquals(oneFieldBeanLikeMap, instanceChange.getInstanceValue().getBean());
                Assert.assertNull(instanceChange.getModifiedProperties());
                Assert.assertNull(instanceChange.getOriginalInstanceValue());
            }
        }
        finally {
            // Cleanup
            if (listenerDescriptor != null) {
                ServiceLocatorUtilities.removeOneDescriptor(testLocator, listenerDescriptor);
            }
            
            if (wbd != null) {
                removeType(ONE_INSTANCE_TYPE);
            }
            
        }
    }
    
    /**
     * Tests adding an instance to an existing a type
     */
    @Test
    public void testModifyProperty() {
        GenericJavaBean oldBean = new GenericJavaBean(ALICE, OTHER_PROPERTY_VALUE1);
        addTypeAndInstance(TYPE_TWO, ALICE, oldBean);
        
        GenericBeanDatabaseUpdateListener listener = null;
        WriteableBeanDatabase wbd = null;
        ActiveDescriptor<?> listenerDescriptor = null;
        
        try {
            listener = new GenericBeanDatabaseUpdateListener();
            listenerDescriptor = ServiceLocatorUtilities.addOneConstant(testLocator, listener);
        
            wbd = hub.getWriteableDatabaseCopy();
            WriteableType wt = wbd.getWriteableType(TYPE_TWO);
            Assert.assertNotNull(wt);
            
            GenericJavaBean newBean = new GenericJavaBean(ALICE, OTHER_PROPERTY_VALUE2);
            PropertyChangeEvent[] result = wt.modifyInstance(ALICE, newBean,
                    new PropertyChangeEvent(newBean, OTHER_PROPERTY, OTHER_PROPERTY_VALUE1, OTHER_PROPERTY_VALUE2));
            
            Assert.assertEquals(1, result.length);
            Assert.assertEquals(result[0].getNewValue(), OTHER_PROPERTY_VALUE2);
            Assert.assertEquals(result[0].getOldValue(), OTHER_PROPERTY_VALUE1);
            Assert.assertEquals(result[0].getPropertyName(), OTHER_PROPERTY);
            Assert.assertEquals(result[0].getSource(), newBean);
        
            wbd.commit();
        
            Type typeTwo = hub.getCurrentDatabase().getType(TYPE_TWO);
            
            List<Change> changes = listener.getLastSetOfChanges();
            
            Assert.assertEquals(1, changes.size());
            
            {
                Change instanceChange = changes.get(0);
                ServiceLocatorUtilities.removeOneDescriptor(testLocator, listenerDescriptor);
                Assert.assertEquals(Change.ChangeCategory.MODIFY_INSTANCE, instanceChange.getChangeCategory());
                Assert.assertEquals(TYPE_TWO, instanceChange.getChangeType().getName());
                Assert.assertEquals(1, instanceChange.getChangeType().getInstances().size());
                Assert.assertEquals(ALICE, instanceChange.getInstanceKey());
                Assert.assertEquals(newBean, instanceChange.getInstanceValue().getBean());
                Assert.assertEquals(oldBean, instanceChange.getOriginalInstanceValue().getBean());
                
                List<PropertyChangeEvent> propertyChanges = instanceChange.getModifiedProperties();
                Assert.assertNotNull(propertyChanges);
                Assert.assertEquals(1, propertyChanges.size());
                
                PropertyChangeEvent pce = propertyChanges.get(0);
                
                Assert.assertEquals(OTHER_PROPERTY, pce.getPropertyName());
                Assert.assertEquals(OTHER_PROPERTY_VALUE1, pce.getOldValue());
                Assert.assertEquals(OTHER_PROPERTY_VALUE2, pce.getNewValue());
                Assert.assertEquals(newBean, pce.getSource());
            }
            
            typeTwo = hub.getCurrentDatabase().getType(TYPE_TWO);
            
            GenericJavaBean bean = (GenericJavaBean) typeTwo.getInstance(ALICE).getBean();
            
            Assert.assertEquals(ALICE, bean.getName());
            Assert.assertEquals(OTHER_PROPERTY_VALUE2, bean.getOther());
        }
        finally {
            // Cleanup
            if (listenerDescriptor != null) {
                ServiceLocatorUtilities.removeOneDescriptor(testLocator, listenerDescriptor);
            }
            
            if (wbd != null) {
                removeType(TYPE_TWO);
            }
            
        }
    }
    
    /**
     * Tests modifying a property with automatically generated change events
     */
    @Test
    public void testModifyPropertyWithAutomaticPropertyChangeEvent() {
        GenericJavaBean oldBean = new GenericJavaBean(ALICE, OTHER_PROPERTY_VALUE1);
        addTypeAndInstance(TYPE_TWO, ALICE, oldBean);
        
        GenericBeanDatabaseUpdateListener listener = null;
        WriteableBeanDatabase wbd = null;
        ActiveDescriptor<?> listenerDescriptor = null;
        
        try {
            listener = new GenericBeanDatabaseUpdateListener();
            listenerDescriptor = ServiceLocatorUtilities.addOneConstant(testLocator, listener);
        
            wbd = hub.getWriteableDatabaseCopy();
            WriteableType wt = wbd.getWriteableType(TYPE_TWO);
            Assert.assertNotNull(wt);
            
            GenericJavaBean newBean = new GenericJavaBean(ALICE, OTHER_PROPERTY_VALUE2);
            PropertyChangeEvent[] result = wt.modifyInstance(ALICE, newBean);
            
            Assert.assertEquals(1, result.length);
            Assert.assertEquals(result[0].getNewValue(), OTHER_PROPERTY_VALUE2);
            Assert.assertEquals(result[0].getOldValue(), OTHER_PROPERTY_VALUE1);
            Assert.assertEquals(result[0].getPropertyName(), OTHER_PROPERTY);
            Assert.assertEquals(result[0].getSource(), newBean);
        
            wbd.commit();
        
            Type typeTwo = hub.getCurrentDatabase().getType(TYPE_TWO);
            
            List<Change> changes = listener.getLastSetOfChanges();
            
            Assert.assertEquals(1, changes.size());
            
            {
                Change instanceChange = changes.get(0);
            
                Assert.assertEquals(Change.ChangeCategory.MODIFY_INSTANCE, instanceChange.getChangeCategory());
                Assert.assertEquals(TYPE_TWO, instanceChange.getChangeType().getName());
                Assert.assertEquals(1, instanceChange.getChangeType().getInstances().size());
                Assert.assertEquals(ALICE, instanceChange.getInstanceKey());
                Assert.assertEquals(newBean, instanceChange.getInstanceValue().getBean());
                Assert.assertEquals(oldBean, instanceChange.getOriginalInstanceValue().getBean());
                
                List<PropertyChangeEvent> propertyChanges = instanceChange.getModifiedProperties();
                Assert.assertNotNull(propertyChanges);
                Assert.assertEquals(1, propertyChanges.size());
                
                PropertyChangeEvent pce = propertyChanges.get(0);
                
                Assert.assertEquals(OTHER_PROPERTY, pce.getPropertyName());
                Assert.assertEquals(OTHER_PROPERTY_VALUE1, pce.getOldValue());
                Assert.assertEquals(OTHER_PROPERTY_VALUE2, pce.getNewValue());
                Assert.assertEquals(newBean, pce.getSource());
            }
            
            typeTwo = hub.getCurrentDatabase().getType(TYPE_TWO);
            
            GenericJavaBean bean = (GenericJavaBean) typeTwo.getInstance(ALICE).getBean();
            
            Assert.assertEquals(ALICE, bean.getName());
            Assert.assertEquals(OTHER_PROPERTY_VALUE2, bean.getOther());
        }
        finally {
            // Cleanup
            if (listenerDescriptor != null) {
                ServiceLocatorUtilities.removeOneDescriptor(testLocator, listenerDescriptor);
            }
            
            if (wbd != null) {
                removeType(TYPE_TWO);
            }
            
        }
    }
    
    /**
     * Tests findOrAddWriteableType and other accessors
     */
    @Test
    public void testFindOrAdd() {
        GenericJavaBean addedBean = new GenericJavaBean(ALICE, OTHER_PROPERTY_VALUE1);
        addTypeAndInstance(TYPE_TWO, ALICE, addedBean);
        
        WriteableBeanDatabase wbd = null;
        
        try {
            wbd = hub.getWriteableDatabaseCopy();
            
            GenericJavaBean gjb = (GenericJavaBean) wbd.getInstance(TYPE_TWO, ALICE).getBean();
            Assert.assertNotNull(gjb);
            Assert.assertEquals(addedBean, gjb);
        
            WriteableType wt = wbd.findOrAddWriteableType(TYPE_TWO);
            Assert.assertNotNull(wt);
            
            gjb = (GenericJavaBean) wt.getInstance(ALICE).getBean();
            Assert.assertNotNull(gjb);
            Assert.assertEquals(addedBean, gjb);
            
            WriteableType wt3 = wbd.findOrAddWriteableType(TYPE_THREE);
            Assert.assertNotNull(wt3);
            
            gjb = (GenericJavaBean) wt3.getInstance(ALICE);
            Assert.assertNull(gjb);
        }
        finally {
            // Cleanup
            
            if (wbd != null) {
                removeType(TYPE_TWO);
            }
            
        }
    }
    
    /**
     * Tests removing an instance
     */
    @Test
    public void testRemoveInstance() {
        addTypeAndInstance(TYPE_TWO, ALICE, new GenericJavaBean(ALICE, OTHER_PROPERTY_VALUE1));
        addTypeAndInstance(TYPE_TWO, BOB, new GenericJavaBean(BOB, OTHER_PROPERTY_VALUE1));
        
        GenericBeanDatabaseUpdateListener listener = null;
        WriteableBeanDatabase wbd = null;
        ActiveDescriptor<?> listenerDescriptor = null;
        
        try {
            listener = new GenericBeanDatabaseUpdateListener();
            listenerDescriptor = ServiceLocatorUtilities.addOneConstant(testLocator, listener);
        
            wbd = hub.getWriteableDatabaseCopy();
            WriteableType wt = wbd.getWriteableType(TYPE_TWO);
            Assert.assertNotNull(wt);
            
            GenericJavaBean removed = (GenericJavaBean) wt.removeInstance(ALICE).getBean();
            Assert.assertNotNull(removed);
            Assert.assertEquals(ALICE, removed.getName());
        
            wbd.commit();
        
            Type typeTwo = hub.getCurrentDatabase().getType(TYPE_TWO);
            
            List<Change> changes = listener.getLastSetOfChanges();
            
            Assert.assertEquals(1, changes.size());
            
            {
                Change instanceChange = changes.get(0);
            
                Assert.assertEquals(Change.ChangeCategory.REMOVE_INSTANCE, instanceChange.getChangeCategory());
                Assert.assertEquals(TYPE_TWO, instanceChange.getChangeType().getName());
                Assert.assertEquals(1, instanceChange.getChangeType().getInstances().size());
                Assert.assertEquals(ALICE, instanceChange.getInstanceKey());
                Assert.assertEquals(removed, instanceChange.getInstanceValue().getBean());
                Assert.assertNull(instanceChange.getModifiedProperties());
                Assert.assertNull(instanceChange.getOriginalInstanceValue());
            }
            
            typeTwo = hub.getCurrentDatabase().getType(TYPE_TWO);
            
            GenericJavaBean bean = (GenericJavaBean) typeTwo.getInstance(ALICE);
            Assert.assertNull(bean);
            
            // Make sure Bob is still there though!
            bean = (GenericJavaBean) typeTwo.getInstance(BOB).getBean();
            Assert.assertNotNull(bean);
            Assert.assertEquals(BOB, bean.getName());
        }
        finally {
            // Cleanup
            if (listenerDescriptor != null) {
                ServiceLocatorUtilities.removeOneDescriptor(testLocator, listenerDescriptor);
            }
            
            if (wbd != null) {
                removeType(TYPE_TWO);
            }
            
        }
    }
    
    /**
     * Tests removing an type that has current instances
     */
    @Test
    public void testRemoveTypeWithInstances() {
        addTypeAndInstance(TYPE_FOUR, ALICE, new GenericJavaBean(ALICE, OTHER_PROPERTY_VALUE1));
        addTypeAndInstance(TYPE_FOUR, BOB, new GenericJavaBean(BOB, OTHER_PROPERTY_VALUE1));
        
        GenericBeanDatabaseUpdateListener listener = null;
        WriteableBeanDatabase wbd = null;
        ActiveDescriptor<?> listenerDescriptor = null;
        
        try {
            listener = new GenericBeanDatabaseUpdateListener();
            listenerDescriptor = ServiceLocatorUtilities.addOneConstant(testLocator, listener);
        
            wbd = hub.getWriteableDatabaseCopy();
            
            Type removed = wbd.removeType(TYPE_FOUR);
            Assert.assertNotNull(removed);
            Assert.assertEquals(TYPE_FOUR, removed.getName());
        
            wbd.commit();
        
            Type typeFour = hub.getCurrentDatabase().getType(TYPE_FOUR);
            Assert.assertNull(typeFour);
            
            List<Change> changes = listener.getLastSetOfChanges();
            
            Assert.assertEquals(3, changes.size());
            
            boolean firstChangeWasAlice = false;
            {
                Change instanceChange = changes.get(0);
            
                Assert.assertEquals(Change.ChangeCategory.REMOVE_INSTANCE, instanceChange.getChangeCategory());
                Assert.assertEquals(TYPE_FOUR, instanceChange.getChangeType().getName());
                Assert.assertEquals(0, instanceChange.getChangeType().getInstances().size());
                if (instanceChange.getInstanceKey().equals(ALICE)) {
                    firstChangeWasAlice = true;
                }
                else if (instanceChange.getInstanceKey().equals(BOB)) {
                    firstChangeWasAlice = false;
                }
                else {
                    Assert.fail("Unknown instance name " + instanceChange.getInstanceKey());
                }
                
                Assert.assertNull(instanceChange.getModifiedProperties());
                Assert.assertNull(instanceChange.getOriginalInstanceValue());
            }
            
            {
                Change instanceChange = changes.get(1);
            
                Assert.assertEquals(Change.ChangeCategory.REMOVE_INSTANCE, instanceChange.getChangeCategory());
                Assert.assertEquals(TYPE_FOUR, instanceChange.getChangeType().getName());
                Assert.assertEquals(0, instanceChange.getChangeType().getInstances().size());
                if (firstChangeWasAlice) {
                    Assert.assertEquals(BOB, instanceChange.getInstanceKey());
                }
                else {
                    Assert.assertEquals(ALICE, instanceChange.getInstanceKey());
                }
                
                Assert.assertNull(instanceChange.getModifiedProperties());
                Assert.assertNull(instanceChange.getOriginalInstanceValue());
            }
            
            {
                Change instanceChange = changes.get(2);
            
                Assert.assertEquals(Change.ChangeCategory.REMOVE_TYPE, instanceChange.getChangeCategory());
                Assert.assertEquals(TYPE_FOUR, instanceChange.getChangeType().getName());
                Assert.assertEquals(0, instanceChange.getChangeType().getInstances().size());
                Assert.assertNull(instanceChange.getModifiedProperties());
                Assert.assertNull(instanceChange.getOriginalInstanceValue());
            }
            
            
        }
        finally {
            // Cleanup
            if (listenerDescriptor != null) {
                ServiceLocatorUtilities.removeOneDescriptor(testLocator, listenerDescriptor);
            }
        }
    }
    
    private static void checkInstances(HashSet<String> checkMe) {
        Assert.assertTrue(checkMe.contains(ALICE));
        Assert.assertTrue(checkMe.contains(BOB));
        Assert.assertTrue(checkMe.contains(CAROL));
        Assert.assertEquals(3, checkMe.size());
        
        checkMe.clear();
    }
    
    /**
     * Tests removing an type with multiple types and multiple instances
     */
    @Test
    public void testRemoveMultipleTypeWithMultipleInstances() {
        addTypeAndInstance(TYPE_EIGHT, ALICE, new GenericJavaBean(ALICE, OTHER_PROPERTY_VALUE1));
        addTypeAndInstance(TYPE_NINE, ALICE, new GenericJavaBean(ALICE, OTHER_PROPERTY_VALUE1));
        addTypeAndInstance(TYPE_TEN, ALICE, new GenericJavaBean(ALICE, OTHER_PROPERTY_VALUE1));
        addTypeAndInstance(TYPE_ELEVEN, ALICE, new GenericJavaBean(ALICE, OTHER_PROPERTY_VALUE1));
        
        GenericBeanDatabaseUpdateListener listener = null;
        WriteableBeanDatabase wbd = null;
        ActiveDescriptor<?> listenerDescriptor = null;
        
        try {
            listener = new GenericBeanDatabaseUpdateListener();
            
            wbd = hub.getWriteableDatabaseCopy();
            
            wbd.findOrAddWriteableType(TYPE_EIGHT).addInstance(BOB, new GenericJavaBean(BOB, OTHER_PROPERTY_VALUE1));
            wbd.findOrAddWriteableType(TYPE_NINE).addInstance(BOB, new GenericJavaBean(BOB, OTHER_PROPERTY_VALUE1));
            wbd.findOrAddWriteableType(TYPE_TEN).addInstance(BOB, new GenericJavaBean(BOB, OTHER_PROPERTY_VALUE1));
            
            wbd.findOrAddWriteableType(TYPE_EIGHT).addInstance(CAROL, new GenericJavaBean(CAROL, OTHER_PROPERTY_VALUE1));
            wbd.findOrAddWriteableType(TYPE_NINE).addInstance(CAROL, new GenericJavaBean(BOB, OTHER_PROPERTY_VALUE1));
            wbd.findOrAddWriteableType(TYPE_TEN).addInstance(CAROL, new GenericJavaBean(BOB, OTHER_PROPERTY_VALUE1));
            
            wbd.commit();
            
            listenerDescriptor = ServiceLocatorUtilities.addOneConstant(testLocator, listener);
            
            wbd = hub.getWriteableDatabaseCopy();
            
            Type removed8 = wbd.removeType(TYPE_EIGHT);
            Assert.assertNotNull(removed8);
            Assert.assertEquals(TYPE_EIGHT, removed8.getName());
            
            Type removed10 = wbd.removeType(TYPE_TEN);
            Assert.assertNotNull(removed10);
            Assert.assertEquals(TYPE_TEN, removed10.getName());
            
            Type removed9 = wbd.removeType(TYPE_NINE);
            Assert.assertNotNull(removed9);
            Assert.assertEquals(TYPE_NINE, removed9.getName());
            
            wbd.commit();
        
            Type typeEight = hub.getCurrentDatabase().getType(TYPE_EIGHT);
            Assert.assertNull(typeEight);
            
            Type typeNine = hub.getCurrentDatabase().getType(TYPE_NINE);
            Assert.assertNull(typeNine);
            
            Type typeTen = hub.getCurrentDatabase().getType(TYPE_TEN);
            Assert.assertNull(typeTen);
            
            List<Change> changes = listener.getLastSetOfChanges();
            
            Assert.assertEquals(12, changes.size());
            
            HashSet<String> instanceKeys = new HashSet<String>();
            {
                Change instanceChange = changes.get(0);
            
                Assert.assertEquals(Change.ChangeCategory.REMOVE_INSTANCE, instanceChange.getChangeCategory());
                Assert.assertEquals(TYPE_EIGHT, instanceChange.getChangeType().getName());
                Assert.assertEquals(0, instanceChange.getChangeType().getInstances().size());
                instanceKeys.add(instanceChange.getInstanceKey());
                Assert.assertNull(instanceChange.getModifiedProperties());
                Assert.assertNull(instanceChange.getOriginalInstanceValue());
            }
            
            {
                Change instanceChange = changes.get(1);
            
                Assert.assertEquals(Change.ChangeCategory.REMOVE_INSTANCE, instanceChange.getChangeCategory());
                Assert.assertEquals(TYPE_EIGHT, instanceChange.getChangeType().getName());
                Assert.assertEquals(0, instanceChange.getChangeType().getInstances().size());
                instanceKeys.add(instanceChange.getInstanceKey());
                Assert.assertNull(instanceChange.getModifiedProperties());
                Assert.assertNull(instanceChange.getOriginalInstanceValue());
            }
            
            {
                Change instanceChange = changes.get(2);
            
                Assert.assertEquals(Change.ChangeCategory.REMOVE_INSTANCE, instanceChange.getChangeCategory());
                Assert.assertEquals(TYPE_EIGHT, instanceChange.getChangeType().getName());
                Assert.assertEquals(0, instanceChange.getChangeType().getInstances().size());
                instanceKeys.add(instanceChange.getInstanceKey());
                Assert.assertNull(instanceChange.getModifiedProperties());
                Assert.assertNull(instanceChange.getOriginalInstanceValue());
            }
            
            checkInstances(instanceKeys);
            
            {
                Change instanceChange = changes.get(3);
            
                Assert.assertEquals(Change.ChangeCategory.REMOVE_TYPE, instanceChange.getChangeCategory());
                Assert.assertEquals(TYPE_EIGHT, instanceChange.getChangeType().getName());
                Assert.assertEquals(0, instanceChange.getChangeType().getInstances().size());
                Assert.assertNull(instanceChange.getModifiedProperties());
                Assert.assertNull(instanceChange.getOriginalInstanceValue());
            }
            
            {
                Change instanceChange = changes.get(4);
            
                Assert.assertEquals(Change.ChangeCategory.REMOVE_INSTANCE, instanceChange.getChangeCategory());
                Assert.assertEquals(TYPE_TEN, instanceChange.getChangeType().getName());
                Assert.assertEquals(0, instanceChange.getChangeType().getInstances().size());
                instanceKeys.add(instanceChange.getInstanceKey());
                Assert.assertNull(instanceChange.getModifiedProperties());
                Assert.assertNull(instanceChange.getOriginalInstanceValue());
            }
            
            {
                Change instanceChange = changes.get(5);
            
                Assert.assertEquals(Change.ChangeCategory.REMOVE_INSTANCE, instanceChange.getChangeCategory());
                Assert.assertEquals(TYPE_TEN, instanceChange.getChangeType().getName());
                Assert.assertEquals(0, instanceChange.getChangeType().getInstances().size());
                instanceKeys.add(instanceChange.getInstanceKey());
                Assert.assertNull(instanceChange.getModifiedProperties());
                Assert.assertNull(instanceChange.getOriginalInstanceValue());
            }
            
            {
                Change instanceChange = changes.get(6);
            
                Assert.assertEquals(Change.ChangeCategory.REMOVE_INSTANCE, instanceChange.getChangeCategory());
                Assert.assertEquals(TYPE_TEN, instanceChange.getChangeType().getName());
                Assert.assertEquals(0, instanceChange.getChangeType().getInstances().size());
                instanceKeys.add(instanceChange.getInstanceKey());
                Assert.assertNull(instanceChange.getModifiedProperties());
                Assert.assertNull(instanceChange.getOriginalInstanceValue());
            }
            
            checkInstances(instanceKeys);
            
            {
                Change instanceChange = changes.get(7);
            
                Assert.assertEquals(Change.ChangeCategory.REMOVE_TYPE, instanceChange.getChangeCategory());
                Assert.assertEquals(TYPE_TEN, instanceChange.getChangeType().getName());
                Assert.assertEquals(0, instanceChange.getChangeType().getInstances().size());
                Assert.assertNull(instanceChange.getModifiedProperties());
                Assert.assertNull(instanceChange.getOriginalInstanceValue());
            }
            
            {
                Change instanceChange = changes.get(8);
            
                Assert.assertEquals(Change.ChangeCategory.REMOVE_INSTANCE, instanceChange.getChangeCategory());
                Assert.assertEquals(TYPE_NINE, instanceChange.getChangeType().getName());
                Assert.assertEquals(0, instanceChange.getChangeType().getInstances().size());
                instanceKeys.add(instanceChange.getInstanceKey());
                Assert.assertNull(instanceChange.getModifiedProperties());
                Assert.assertNull(instanceChange.getOriginalInstanceValue());
            }
            
            {
                Change instanceChange = changes.get(9);
            
                Assert.assertEquals(Change.ChangeCategory.REMOVE_INSTANCE, instanceChange.getChangeCategory());
                Assert.assertEquals(TYPE_NINE, instanceChange.getChangeType().getName());
                Assert.assertEquals(0, instanceChange.getChangeType().getInstances().size());
                instanceKeys.add(instanceChange.getInstanceKey());
                Assert.assertNull(instanceChange.getModifiedProperties());
                Assert.assertNull(instanceChange.getOriginalInstanceValue());
            }
            
            {
                Change instanceChange = changes.get(10);
            
                Assert.assertEquals(Change.ChangeCategory.REMOVE_INSTANCE, instanceChange.getChangeCategory());
                Assert.assertEquals(TYPE_NINE, instanceChange.getChangeType().getName());
                Assert.assertEquals(0, instanceChange.getChangeType().getInstances().size());
                instanceKeys.add(instanceChange.getInstanceKey());
                Assert.assertNull(instanceChange.getModifiedProperties());
                Assert.assertNull(instanceChange.getOriginalInstanceValue());
            }
            
            checkInstances(instanceKeys);
            
            {
                Change instanceChange = changes.get(11);
            
                Assert.assertEquals(Change.ChangeCategory.REMOVE_TYPE, instanceChange.getChangeCategory());
                Assert.assertEquals(TYPE_NINE, instanceChange.getChangeType().getName());
                Assert.assertEquals(0, instanceChange.getChangeType().getInstances().size());
                Assert.assertNull(instanceChange.getModifiedProperties());
                Assert.assertNull(instanceChange.getOriginalInstanceValue());
            }
            
            
        }
        finally {
            // Cleanup
            if (listenerDescriptor != null) {
                ServiceLocatorUtilities.removeOneDescriptor(testLocator, listenerDescriptor);
            }
            
            removeType(TYPE_ELEVEN);
        }
    }
    
    /**
     * Tests that I can set, get and set again and get again
     */
    @Test
    public void testMetadataOnType() {
        addType(TYPE_FIVE);
        
        try {
            Type five = hub.getCurrentDatabase().getType(TYPE_FIVE);
            Assert.assertNull(five.getMetadata());
        
            Object o1 = new Object();
            five.setMetadata(o1);
        
            Assert.assertEquals(o1, five.getMetadata());
        
            Object o2 = new Object();
            five.setMetadata(o2);
        
            Assert.assertEquals(o2, five.getMetadata());
        
            five.setMetadata(null);
            Assert.assertNull(five.getMetadata());
        }
        finally {
            removeType(TYPE_FIVE);
        }
        
    }
    
    /**
     * Tests that I can set, get and set again and get again
     */
    @Test
    public void testMetadataOnInstance() {
        GenericBeanDatabaseUpdateListener listener = null;
        WriteableBeanDatabase wbd = null;
        ActiveDescriptor<?> listenerDescriptor = null;
        
        try {
            listener = new GenericBeanDatabaseUpdateListener();
            listenerDescriptor = ServiceLocatorUtilities.addOneConstant(testLocator, listener);
            
            wbd = hub.getWriteableDatabaseCopy();
            
            WriteableType wt = wbd.findOrAddWriteableType(TYPE_SIX);
            
            Object metadata = new Object();
            Instance added = wt.addInstance(ALICE, new GenericJavaBean(ALICE, OTHER_PROPERTY_VALUE1), metadata);
            Assert.assertNotNull(added);
            
            wbd.commit();
            
            List<Change> changes = listener.getLastSetOfChanges();
            
            Assert.assertEquals(2, changes.size());
            
            Change instanceChange = changes.get(1);
            
            Assert.assertEquals(Change.ChangeCategory.ADD_INSTANCE, instanceChange.getChangeCategory());
            Instance instance = instanceChange.getInstanceValue();
            Assert.assertNotNull(instance);
            
            Assert.assertEquals(metadata, instance.getMetadata());
            
            Instance directInstance = hub.getCurrentDatabase().getInstance(TYPE_SIX, ALICE);
            Assert.assertNotNull(directInstance);
            
            Assert.assertEquals(metadata, directInstance.getMetadata());
            
            Object metadata2 = new Object();
            directInstance.setMetadata(metadata2);
            
            Assert.assertEquals(metadata2, directInstance.getMetadata());
            
            directInstance.setMetadata(null);
            Assert.assertNull(directInstance.getMetadata());
        }
        finally {
            if (listenerDescriptor != null) {
                ServiceLocatorUtilities.removeOneDescriptor(testLocator, listenerDescriptor);
            }
            
            removeType(TYPE_SIX);
        }
        
    }
    
    /**
     * Tests that I can set, get and set again and get again
     */
    @Test
    public void testMetadataOnPropertyChange() {
        Object originalMetadata = new Object();
        GenericJavaBean oldBean = new GenericJavaBean(ALICE, OTHER_PROPERTY_VALUE1);
        addTypeAndInstance(TYPE_SEVEN, ALICE, oldBean, originalMetadata);
        
        GenericBeanDatabaseUpdateListener listener = null;
        WriteableBeanDatabase wbd = null;
        ActiveDescriptor<?> listenerDescriptor = null;
        
        try {
            listener = new GenericBeanDatabaseUpdateListener();
            listenerDescriptor = ServiceLocatorUtilities.addOneConstant(testLocator, listener);
            
            GenericJavaBean newBean = new GenericJavaBean(ALICE, OTHER_PROPERTY_VALUE2);
            
            wbd = hub.getWriteableDatabaseCopy();
            
            WriteableType wt = wbd.getWriteableType(TYPE_SEVEN);
            
            wt.modifyInstance(ALICE, newBean);
            
            wbd.commit();
            
            List<Change> changes = listener.getLastSetOfChanges();
            
            Assert.assertEquals(1, changes.size());
            
            Change instanceChange = changes.get(0);
            
            Assert.assertEquals(Change.ChangeCategory.MODIFY_INSTANCE, instanceChange.getChangeCategory());
            Instance instance = instanceChange.getInstanceValue();
            Instance originalInstance = instanceChange.getOriginalInstanceValue();
            
            Assert.assertNotNull(instance);
            Assert.assertNotNull(originalInstance);
            
            Assert.assertEquals(originalMetadata, instance.getMetadata());
            Assert.assertEquals(originalMetadata, originalInstance.getMetadata());
        }
        finally {
            if (listenerDescriptor != null) {
                ServiceLocatorUtilities.removeOneDescriptor(testLocator, listenerDescriptor);
            }
            
            removeType(TYPE_SEVEN);
        }
        
    }
    
    /**
     * Tests that all listeners are called, sunny day scenario
     */
    @Test
    public void testAllListenersPrepareAndCommitInvoked() {
        AbstractCountingListener listener1 = new AbstractCountingListener();
        AbstractCountingListener listener2 = new AbstractCountingListener();
        AbstractCountingListener listener3 = new AbstractCountingListener();
        
        LinkedList<ActiveDescriptor<?>> added = new LinkedList<ActiveDescriptor<?>>();
        added.add(ServiceLocatorUtilities.addOneConstant(testLocator, listener1));
        added.add(ServiceLocatorUtilities.addOneConstant(testLocator, listener2));
        added.add(ServiceLocatorUtilities.addOneConstant(testLocator, listener3));
        
        try {
            GenericJavaBean newBean = new GenericJavaBean();
            
            addTypeAndInstance(TYPE_TWELVE, DAVE, newBean);
            
            Assert.assertEquals(1, listener1.getNumPreparesCalled());
            Assert.assertEquals(1, listener1.getNumCommitsCalled());
            Assert.assertEquals(0, listener1.getNumRollbackCalled());
            
            Assert.assertEquals(1, listener2.getNumPreparesCalled());
            Assert.assertEquals(1, listener2.getNumCommitsCalled());
            Assert.assertEquals(0, listener2.getNumRollbackCalled());
            
            Assert.assertEquals(1, listener3.getNumPreparesCalled());
            Assert.assertEquals(1, listener3.getNumCommitsCalled());
            Assert.assertEquals(0, listener3.getNumRollbackCalled());
        }
        finally {
            for (ActiveDescriptor<?> removeMe : added) {
                ServiceLocatorUtilities.removeOneDescriptor(testLocator, removeMe);
            }
            
            removeType(TYPE_TWELVE);
        }
        
    }
    
    /**
     * Tests that all listeners are called, sunny day scenario
     */
    @Test
    public void testMiddleListenerThrowsExceptionInPrepare() {
        AbstractCountingListener listener1 = new AbstractCountingListener();
        PrepareFailListener listener2 = new PrepareFailListener();
        AbstractCountingListener listener3 = new AbstractCountingListener();
        
        LinkedList<ActiveDescriptor<?>> added = new LinkedList<ActiveDescriptor<?>>();
        added.add(ServiceLocatorUtilities.addOneConstant(testLocator, listener1));
        added.add(ServiceLocatorUtilities.addOneConstant(testLocator, listener2));
        added.add(ServiceLocatorUtilities.addOneConstant(testLocator, listener3));
        
        try {
            GenericJavaBean newBean = new GenericJavaBean();
            
            try {
                addTypeAndInstance(TYPE_TWELVE, DAVE, newBean);
                Assert.fail("Prepare threw exception, but commit succeeded");
            }
            catch (MultiException me) {
                Assert.assertTrue(me.toString().contains(PREPARE_FAIL_MESSAGE));
                
                boolean found = false;
                for (Throwable inner : me.getErrors()) {
                    if (inner instanceof PrepareFailedException) {
                        Assert.assertFalse("Should only be ONE instance of PrepareFailedException, but there is at least two in " + me, found);
                        found = true;
                    }
                }
                
                Assert.assertTrue(found);
            }
            
            Assert.assertEquals(1, listener1.getNumPreparesCalled());
            Assert.assertEquals(0, listener1.getNumCommitsCalled());
            Assert.assertEquals(1, listener1.getNumRollbackCalled());
            
            Assert.assertEquals(1, listener2.getNumPreparesCalled());
            Assert.assertEquals(0, listener2.getNumCommitsCalled());
            Assert.assertEquals(0, listener2.getNumRollbackCalled());
            
            Assert.assertEquals(0, listener3.getNumPreparesCalled());
            Assert.assertEquals(0, listener3.getNumCommitsCalled());
            Assert.assertEquals(0, listener3.getNumRollbackCalled());
        }
        finally {
            for (ActiveDescriptor<?> removeMe : added) {
                ServiceLocatorUtilities.removeOneDescriptor(testLocator, removeMe);
            }
            
            removeType(TYPE_TWELVE);
        }
    }
    
    /**
     * Tests that all listeners are called when one fails in commit
     */
    @Test
    public void testMiddleListenerThrowsExceptionInCommit() {
        AbstractCountingListener listener1 = new AbstractCountingListener();
        CommitFailListener listener2 = new CommitFailListener();
        AbstractCountingListener listener3 = new AbstractCountingListener();
        
        LinkedList<ActiveDescriptor<?>> added = new LinkedList<ActiveDescriptor<?>>();
        added.add(ServiceLocatorUtilities.addOneConstant(testLocator, listener1));
        added.add(ServiceLocatorUtilities.addOneConstant(testLocator, listener2));
        added.add(ServiceLocatorUtilities.addOneConstant(testLocator, listener3));
        
        try {
            GenericJavaBean newBean = new GenericJavaBean();
            
            try {
                addTypeAndInstance(TYPE_TWELVE, DAVE, newBean);
                Assert.fail("Commit threw exception, but commit succeeded");
            }
            catch (MultiException me) {
                Assert.assertTrue(me.toString().contains(COMMIT_FAIL_MESSAGE));
                
                boolean found = false;
                for (Throwable inner : me.getErrors()) {
                    if (inner instanceof CommitFailedException) {
                        Assert.assertFalse("Should only be ONE instance of CommitFailedException, but there is at least two in " + me, found);
                        found = true;
                    }
                }
                
                Assert.assertTrue(found);
            }
            
            Assert.assertEquals(1, listener1.getNumPreparesCalled());
            Assert.assertEquals(1, listener1.getNumCommitsCalled());
            Assert.assertEquals(0, listener1.getNumRollbackCalled());
            
            Assert.assertEquals(1, listener2.getNumPreparesCalled());
            Assert.assertEquals(1, listener2.getNumCommitsCalled());
            Assert.assertEquals(0, listener2.getNumRollbackCalled());
            
            Assert.assertEquals(1, listener3.getNumPreparesCalled());
            Assert.assertEquals(1, listener3.getNumCommitsCalled());
            Assert.assertEquals(0, listener3.getNumRollbackCalled());
        }
        finally {
            for (ActiveDescriptor<?> removeMe : added) {
                ServiceLocatorUtilities.removeOneDescriptor(testLocator, removeMe);
            }
            
            
            
            removeType(TYPE_TWELVE);
        }
    }
    
    /**
     * Tests that all listeners are called when one fails in prepare and
     * several others fail in rollback
     */
    @Test
    public void testAnExceptionInPrepareAndSeveralRollbacksAllGetReported() {
        RollbackFailListener listener1 = new RollbackFailListener();
        AbstractCountingListener listener2 = new AbstractCountingListener();
        RollbackFailListener listener3 = new RollbackFailListener();
        PrepareFailListener listener4 = new PrepareFailListener();
        
        LinkedList<ActiveDescriptor<?>> added = new LinkedList<ActiveDescriptor<?>>();
        added.add(ServiceLocatorUtilities.addOneConstant(testLocator, listener1));
        added.add(ServiceLocatorUtilities.addOneConstant(testLocator, listener2));
        added.add(ServiceLocatorUtilities.addOneConstant(testLocator, listener3));
        added.add(ServiceLocatorUtilities.addOneConstant(testLocator, listener4));
        
        try {
            GenericJavaBean newBean = new GenericJavaBean();
            
            try {
                addTypeAndInstance(TYPE_TWELVE, DAVE, newBean);
                Assert.fail("Prepare threw exception, but commit succeeded");
            }
            catch (MultiException me) {
                Assert.assertTrue(me.toString().contains(PREPARE_FAIL_MESSAGE));
                
                boolean found = false;
                int rollbackErrorsReported = 0;
                for (Throwable inner : me.getErrors()) {
                    if (inner instanceof PrepareFailedException) {
                        Assert.assertFalse("Should only be ONE instance of PrepareFailedException, but there is at least two in " + me, found);
                        found = true;
                    }
                    else if (inner instanceof RollbackFailedException) {
                        rollbackErrorsReported++;
                    }
                }
                
                Assert.assertTrue(found);
                Assert.assertEquals(2, rollbackErrorsReported);
            }
            
            Assert.assertEquals(1, listener1.getNumPreparesCalled());
            Assert.assertEquals(0, listener1.getNumCommitsCalled());
            Assert.assertEquals(1, listener1.getNumRollbackCalled());
            
            Assert.assertEquals(1, listener2.getNumPreparesCalled());
            Assert.assertEquals(0, listener2.getNumCommitsCalled());
            Assert.assertEquals(1, listener2.getNumRollbackCalled());
            
            Assert.assertEquals(1, listener3.getNumPreparesCalled());
            Assert.assertEquals(0, listener3.getNumCommitsCalled());
            Assert.assertEquals(1, listener3.getNumRollbackCalled());
            
            Assert.assertEquals(1, listener4.getNumPreparesCalled());
            Assert.assertEquals(0, listener4.getNumCommitsCalled());
            Assert.assertEquals(0, listener4.getNumRollbackCalled());
        }
        finally {
            for (ActiveDescriptor<?> removeMe : added) {
                ServiceLocatorUtilities.removeOneDescriptor(testLocator, removeMe);
            }
            
            removeType(TYPE_TWELVE);
        }
    }
    
    /**
     * Tests that all commit errors are reported
     */
    @Test
    public void testMultipleCommitErrorsAllGetReported() {
        CommitFailListener listener1 = new CommitFailListener();
        CommitFailListener listener2 = new CommitFailListener();
        
        LinkedList<ActiveDescriptor<?>> added = new LinkedList<ActiveDescriptor<?>>();
        added.add(ServiceLocatorUtilities.addOneConstant(testLocator, listener1));
        added.add(ServiceLocatorUtilities.addOneConstant(testLocator, listener2));
        
        try {
            GenericJavaBean newBean = new GenericJavaBean();
            
            try {
                addTypeAndInstance(TYPE_TWELVE, DAVE, newBean);
                Assert.fail("Prepare threw exception, but commit succeeded");
            }
            catch (MultiException me) {
                Assert.assertTrue(me.toString().contains(COMMIT_FAIL_MESSAGE));
                
                int commitErrorsReported = 0;
                for (Throwable inner : me.getErrors()) {
                    if (inner instanceof CommitFailedException) {
                        commitErrorsReported++;
                    }
                }
                
                Assert.assertEquals(2, commitErrorsReported);
            }
            
            Assert.assertEquals(1, listener1.getNumPreparesCalled());
            Assert.assertEquals(1, listener1.getNumCommitsCalled());
            Assert.assertEquals(0, listener1.getNumRollbackCalled());
            
            Assert.assertEquals(1, listener2.getNumPreparesCalled());
            Assert.assertEquals(1, listener2.getNumCommitsCalled());
            Assert.assertEquals(0, listener2.getNumRollbackCalled());
        }
        finally {
            for (ActiveDescriptor<?> removeMe : added) {
                ServiceLocatorUtilities.removeOneDescriptor(testLocator, removeMe);
            }
            
            removeType(TYPE_TWELVE);
        }
    }
    
    /**
     * Tests dumping the database to a String
     */
    @Test
    public void testDumpDatabase() {
        GenericJavaBean addedBean = new GenericJavaBean(ALICE, OTHER_PROPERTY_VALUE1);
        addTypeAndInstance(TYPE_TWO, ALICE, addedBean);
        
        try {
            String dbAsString = hub.getCurrentDatabase().dumpDatabaseAsString();
            Assert.assertTrue(dbAsString.contains(ALICE));
            Assert.assertTrue(dbAsString.contains(TYPE_TWO));
        }
        finally {
            removeType(TYPE_TWO);
        }
    }

}
