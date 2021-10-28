/*
 * Copyright (c) 2016, 2018 Oracle and/or its affiliates. All rights reserved.
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

package org.glassfish.hk2.xml.test.dynamic.overlay;

import java.beans.PropertyChangeEvent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.glassfish.hk2.configuration.hub.api.Change;
import org.glassfish.hk2.configuration.hub.api.Hub;
import org.glassfish.hk2.configuration.hub.api.Instance;
import org.glassfish.hk2.configuration.hub.api.Type;
import org.glassfish.hk2.xml.api.XmlHk2ConfigurationBean;
import org.glassfish.hk2.xml.api.XmlRootHandle;
import org.junit.Assert;

/**
 * @author jwells
 *
 */
public class OverlayUtilities {
    public static final String OROOT_A = "overlay-root-A";
    public static final String OROOT_B = "overlay-root-B";
    
    public static final String A_LIST_CHILD = "unkeyed-leaf-list";
    public static final String A_ARRAY_CHILD = "unkeyed-leaf-array";
    public static final String NAME_TAG = "name";
    public static final String LEAF_LIST = "leaf-list";
    public static final String LEAF_ARRAY = "leaf-array";
    
    public static final String OROOT_TYPE = "/" + OROOT_A;
    public static final String LIST_TYPE = "/" + OROOT_A + "/" + A_LIST_CHILD;
    public static final String ARRAY_TYPE = "/" + OROOT_A + "/" + A_ARRAY_CHILD;
    public static final String OROOT_TYPE_B = "/" + OROOT_B;
    
    private static final String LEFT_PAREN = "(";
    private static final String RIGHT_PAREN = ")";
    
    public static String getStringVersionOfTree(OverlayRootABean root, boolean followList) {
        if (followList) {
            return getStringVersionOfTreeList(root);
        }
        
        return getStringVersionOfTreeArray(root);
    }
    
    private static String getStringVersionOfTreeList(OverlayRootABean root) {
        StringBuffer sb = new StringBuffer();
        
        for(UnkeyedLeafBean ulb : root.getUnkeyedLeafList()) {
            sb.append(ulb.getName());
            
            boolean wroteParen = false;
            if (!ulb.getListLeaf().isEmpty()) {
                sb.append(LEFT_PAREN);
                wroteParen = true;
            }
            for (UnkeyedLeafBean ulbc : ulb.getListLeaf()) {
                getStringVersionOfLeafList(ulbc, sb);
            }
            if (wroteParen) {
                sb.append(RIGHT_PAREN);
            }
        }
        
        return sb.toString();
    }
    
    private static void getStringVersionOfLeafList(UnkeyedLeafBean ulb, StringBuffer sb) {
        sb.append(ulb.getName());
        
        boolean wroteParen = false;
        if (!ulb.getListLeaf().isEmpty()) {
            sb.append(LEFT_PAREN);
            wroteParen = true;
        }
        for (UnkeyedLeafBean ulbc : ulb.getListLeaf()) {
            getStringVersionOfLeafList(ulbc, sb);
        }
        if (wroteParen) {
            sb.append(RIGHT_PAREN);
        }
    }
    
    private static String getStringVersionOfTreeArray(OverlayRootABean root) {
        StringBuffer sb = new StringBuffer();
        
        for(UnkeyedLeafBean ulb : root.getUnkeyedLeafArray()) {
            sb.append(ulb.getName());
            
            boolean wroteParen = false;
            if (ulb.getArrayLeaf().length > 0) {
                sb.append(LEFT_PAREN);
                wroteParen = true;
            }
            for (UnkeyedLeafBean ulbc : ulb.getArrayLeaf()) {
                getStringVersionOfLeafArray(ulbc, sb);
            }
            if (wroteParen) {
                sb.append(RIGHT_PAREN);
            }
        }
        
        return sb.toString();
    }
    
    private static void getStringVersionOfLeafArray(UnkeyedLeafBean ulb, StringBuffer sb) {
        sb.append(ulb.getName());
        
        boolean wroteParen = false;
        if (ulb.getArrayLeaf().length > 0) {
            sb.append(LEFT_PAREN);
            wroteParen = true;
        }
        for (UnkeyedLeafBean ulbc : ulb.getArrayLeaf()) {
            getStringVersionOfLeafArray(ulbc, sb);
        }
        if (wroteParen) {
            sb.append(RIGHT_PAREN);
        }
    }
    
    public static void generateOverlayRootABean(XmlRootHandle<OverlayRootABean> handle, String singleLetterLeafNames) {
        generateOverlayRootABean(handle, true, true, singleLetterNames(singleLetterLeafNames));
    }
    
    public static void generateOverlayRootABean(XmlRootHandle<OverlayRootABean> handle, boolean generateLists, boolean generateArrays, String singleLetterLeafNames) {
        generateOverlayRootABean(handle, generateLists, generateArrays, singleLetterNames(singleLetterLeafNames));
    }
    
    private static void generateOverlayRootABean(XmlRootHandle<OverlayRootABean> handle, boolean generateLists, boolean generateArrays, String... leafNames) {
        OverlayRootABean root = handle.getRoot();
        Assert.assertNull(root);
        
        handle.addRoot();
        
        root = handle.getRoot();
        
        createOverlay(root, null, generateLists, generateArrays, leafNames);
    }
    
    private static void createOverlay(OverlayRootABean root, UnkeyedLeafBean parent, boolean generateLists, boolean generateArrays, String leafNames[]) {
        UnkeyedLeafBean addedListChild = null;
        UnkeyedLeafBean addedArrayChild = null;
        
        for (int lcv = 0; lcv < leafNames.length; lcv++) {
            String leafName = leafNames[lcv];
            
            if (LEFT_PAREN.equals(leafName)) {
                String subList[] = getChildNames(leafNames, lcv);
                
                if (generateLists) {
                    createOverlay(root, addedListChild, generateLists, generateArrays, subList);
                }
                if (generateArrays) {
                    createOverlay(root, addedArrayChild, generateLists, generateArrays, subList);
                }
                
                lcv += subList.length + 1; // The extra one for the right paren
            }
            else if (RIGHT_PAREN.equals(leafName)) {
                // Ignore
            }
            else {
                if (parent == null) {
                    if (generateArrays) {
                        addedArrayChild = root.addUnkeyedLeafArray();
                        addedArrayChild.setName(leafName);
                    }
                    
                    if (generateLists) {
                        addedListChild = root.addUnkeyedLeafList();
                        addedListChild.setName(leafName);
                    }
                }
                else {
                    if (generateArrays) {
                        addedArrayChild = parent.addArrayLeaf();
                        addedArrayChild.setName(leafName);
                    }
                    
                    if (generateLists) {
                        addedListChild = parent.addListLeaf();
                        addedListChild.setName(leafName);
                    }
                }
            }
        }
    }
    
    private static String[] singleLetterNames(String parseMe) {
        if (parseMe == null) parseMe = "";
        
        String retVal[] = new String[parseMe.length()];
        for (int lcv = 0; lcv < parseMe.length(); lcv++) {
            retVal[lcv] = parseMe.substring(lcv, lcv + 1);
        }
        
        return retVal;
    }
    
    public static void checkSingleLetterOveralyRootA(XmlRootHandle<OverlayRootABean> handle, Hub hub, boolean generateLists, boolean generateArrays, String names) {
        checkSingleLetterOveralyRootA(handle.getRoot(), hub, generateLists, generateArrays, singleLetterNames(names));
    }
    
    private static void checkSingleLetterOveralyRootA(OverlayRootABean root, Hub hub, boolean generateLists, boolean generateArrays, String names[]) {
        String typeName = OROOT_TYPE;
        Type rootType = hub.getCurrentDatabase().getType(typeName);
        
        Map<String, Instance> rootInstance = rootType.getInstances();
        Assert.assertEquals(1, rootInstance.size());
        
        int childCount = 0;
        UnkeyedLeafBean currentListBean = null;
        UnkeyedLeafBean currentArrayBean = null;
        for (int lcv = 0; lcv < names.length; lcv++) {
            String name = names[lcv];
            
            if (LEFT_PAREN.equals(name)) {
                String childNames[] = getChildNames(names, lcv);
                
                lcv += childNames.length;
               
                if (generateLists) {
                    checkSingleLetterLeaf(currentListBean, hub, LIST_TYPE, generateLists, generateArrays, childNames);
                }
                
                if (generateArrays) {
                    checkSingleLetterLeaf(currentArrayBean, hub, ARRAY_TYPE, generateLists, generateArrays, childNames);
                }
            }
            else if (RIGHT_PAREN.equals(name)) {
                // Ignore it
            }
            else {
                if (generateLists) {
                    currentListBean = root.getUnkeyedLeafList().get(childCount);
                    checkLeafInHub(hub, LIST_TYPE, currentListBean, name, childCount);
                }
                
                if (generateArrays) {
                    currentArrayBean = root.getUnkeyedLeafArray()[childCount];
                    checkLeafInHub(hub, ARRAY_TYPE, currentArrayBean, name, childCount);
                }
                
                childCount++;
            }
        }
        
        // Now check hub sizes of children, make sure there are no extras
        if (generateLists) {
            Type listType = hub.getCurrentDatabase().getType(LIST_TYPE);
            if (listType == null) {
                Assert.assertEquals(0, childCount);
            }
            else {
                Map<String, Instance> listInstances = listType.getInstances();
            
                Assert.assertEquals(childCount, listInstances.size());
            }
        }
        
        if (generateArrays) {
            Type arrayType = hub.getCurrentDatabase().getType(ARRAY_TYPE);
            if (arrayType == null) {
                Assert.assertEquals(0, childCount);
            }
            else {
                Map<String, Instance> arrayInstances = arrayType.getInstances();
            
                Assert.assertEquals(childCount, arrayInstances.size());
            }
        }
    }
    
    private static void checkSingleLetterLeaf(UnkeyedLeafBean root, Hub hub, String parentType, boolean generateLists, boolean generateArrays, String names[]) {
        String listChildType = parentType + "/" + LEAF_LIST;
        String arrayChildType = parentType + "/" + LEAF_ARRAY;
        
        int childCount = 0;
        UnkeyedLeafBean currentListBean = null;
        UnkeyedLeafBean currentArrayBean = null;
        for (int lcv = 0; lcv < names.length; lcv++) {
            String name = names[lcv];
            
            if (LEFT_PAREN.equals(name)) {
                String childNames[] = getChildNames(names, lcv);
                
                lcv += childNames.length;
                
                if (generateLists) {
                    checkSingleLetterLeaf(currentListBean, hub, listChildType, generateLists, generateArrays, childNames);
                }
                
                if (generateArrays) {
                    checkSingleLetterLeaf(currentArrayBean, hub, arrayChildType, generateLists, generateArrays, childNames);
                }
            }
            else if (RIGHT_PAREN.equals(name)) {
                // Ignore it
            }
            else {
                if (generateLists) {
                    currentListBean = root.getListLeaf().get(childCount);
                    checkLeafInHub(hub, listChildType, currentListBean, name, childCount);
                }
                
                if (generateArrays) {
                    currentArrayBean = root.getArrayLeaf()[childCount];
                    checkLeafInHub(hub, arrayChildType, currentArrayBean, name, childCount);
                }
                
                childCount++;
            }
        }
        
        if (generateLists) {
            List<UnkeyedLeafBean> lBeans = root.getListLeaf();
            Assert.assertEquals("Number of entries in " + lBeans + " is wrong", childCount, lBeans.size());
        }
        
        if (generateArrays) {
            UnkeyedLeafBean aBeans[] = root.getArrayLeaf();
            Assert.assertEquals("Number of entries in " + Arrays.toString(aBeans) + " is wrong", childCount, aBeans.length);
        }
        
        String parentInstanceName = ((XmlHk2ConfigurationBean) root)._getInstanceName();
        
        // Now check hub sizes of children, make sure there are no extras
        if (generateLists) {
            Type listType = hub.getCurrentDatabase().getType(listChildType);
            if (listType == null) {
                Assert.assertEquals(0, childCount);
            }
            else {
                Map<String, Instance> listInstances = listType.getInstances();
            
                int myChildrenCount = 0;
                for (Map.Entry<String, Instance> me : listInstances.entrySet()) {
                    String candidateInstanceName = me.getKey();
                    
                    if (isDirectChildBasedOnInstanceName(parentInstanceName, candidateInstanceName)) {
                        myChildrenCount++;
                    }
                }
                
                Assert.assertEquals("The type " + listChildType + " had the wrong number of entries",
                        childCount, myChildrenCount);
            }
        }
        
        if (generateArrays) {
            Type arrayType = hub.getCurrentDatabase().getType(arrayChildType);
            if (arrayType == null) {
                Assert.assertEquals(0, childCount);
            }
            else {
                Map<String, Instance> arrayInstances = arrayType.getInstances();
                
                int myChildrenCount = 0;
                for (Map.Entry<String, Instance> me : arrayInstances.entrySet()) {
                    String candidateInstanceName = me.getKey();
                    
                    if (isDirectChildBasedOnInstanceName(parentInstanceName, candidateInstanceName)) {
                        myChildrenCount++;
                    }
                }
            
                Assert.assertEquals("The type " + arrayChildType + " had the wrong number of entries",
                        childCount, myChildrenCount);
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    private static void checkLeafInHub(Hub hub, String typeName, UnkeyedLeafBean bean, String expectedName, int parentIndex) {
        Assert.assertEquals("In type " + typeName + " we got wrong name at parent index " + parentIndex + " in bean " + bean,
                expectedName, bean.getName());
        
        Type type = hub.getCurrentDatabase().getType(typeName);
        
        Map<String, Instance> instances = type.getInstances();
        boolean found = false;
        for (Instance instance : instances.values()) {
            Map<String, Object> blm = (Map<String, Object>) instance.getBean();
            
            String instanceName = (String) blm.get(NAME_TAG);
            
            if (instanceName != null && instanceName.equals(expectedName)) {
                found = true;
                break;
            }
        }
        
        Assert.assertTrue("Did not find expectedName " + expectedName + " in hub type " + typeName + " in bean " + bean, found);
    }
    
    private final static String[] getChildNames(String names[], int leftParenIndex) {
        Assert.assertEquals(LEFT_PAREN, names[leftParenIndex]);
        
        Assert.assertTrue(names.length > leftParenIndex);
        
        int leftParenCount = 0;
        for (int dot = leftParenIndex + 1; dot < names.length; dot++) {
            String current = names[dot];
            
            if (current.equals(LEFT_PAREN)) {
                leftParenCount++;
            }
            else if (current.equals(RIGHT_PAREN)) {
                if (leftParenCount <= 0) {
                    // This is the terminal right paren, we can now get the substring
                    int retLen = dot - leftParenIndex - 1;
                    if (retLen <= 0) {
                        return new String[0];
                    }
                    
                    String retVal[] = new String[retLen];
                    System.arraycopy(names, leftParenIndex + 1, retVal, 0, retLen);
                    
                    return retVal;
                }
                
                leftParenCount--;
            }
        }
        
        throw new AssertionError("There was a left paren without a matching right paren in " + names);
    }
    
    private final static boolean isDirectChildBasedOnInstanceName(String parentInstanceName, String myInstanceName) {
        if (!myInstanceName.startsWith(parentInstanceName)) return false;
        
        String remainder = myInstanceName.substring(parentInstanceName.length());
        if (!remainder.startsWith(".")) return false;
        
        remainder = remainder.substring(1);
        int dotIndex = remainder.indexOf('.');
        return (dotIndex < 0);
    }
    
    @SuppressWarnings("unchecked")
    private static String instanceToName(Instance instance) {
        if (instance == null) return null;
        
        Map<String, Object> blm = (Map<String, Object>) instance.getBean();
        if (blm == null) return null;
        
        return (String) blm.get("name");
    }
    
    private static String getChangeDescription(Change change) {
        StringBuffer sb = new StringBuffer("" + change.getChangeCategory() + " type=");
        
        Type type = change.getChangeType();
        sb.append(type.getName() + " ");
        
        switch (change.getChangeCategory()) {
        case REMOVE_INSTANCE:
        {
            String instanceKey = change.getInstanceKey();
            
            Instance instanceRemoved = change.getInstanceValue();
            String instanceName = instanceToName(instanceRemoved);
            
            sb.append(" removedName=" + instanceName + " instanceKey=" + instanceKey);
        }
            break;
            
        case ADD_INSTANCE:
        {
            String instanceKey = change.getInstanceKey();
            
            Instance instanceAdded = change.getInstanceValue();
            String instanceName = instanceToName(instanceAdded);
            
            sb.append(" addedName=" + instanceName + " instanceKey=" + instanceKey);
        }
            break;
        case MODIFY_INSTANCE:
        {
            String instanceKey = change.getInstanceKey();
            
            Instance originalMod = change.getOriginalInstanceValue();
            Instance changedMod = change.getInstanceValue();
            String originalName = instanceToName(originalMod);
            String changedName = instanceToName(changedMod);
            
            sb.append(" originalName=" + originalName + " newName=" + changedName + " instanceKey=" + instanceKey + " propsChanged=[");
            
            boolean firstTime = true;
            List<PropertyChangeEvent> propsChanged = change.getModifiedProperties();
            for (PropertyChangeEvent pce : propsChanged) {
                if (firstTime) {
                    sb.append(pce.getPropertyName());
                    firstTime = false;
                }
                else {
                    sb.append("," + pce.getPropertyName());
                }
            }
            
            sb.append("]");
            
        }
            break;
        case ADD_TYPE:
        case REMOVE_TYPE:
        default:
            break;
        }
        
        return sb.toString();
    }
    
    private static String getAssertString(List<Change> changes, ChangeDescriptor... changeDescriptors) {
        StringBuffer received = new StringBuffer("\n");
        int count = 0;
        for (Change change : changes) {
            received.append("  " + count + ". " + getChangeDescription(change) + "\n");
            count++;
        }
        
        StringBuffer expected = new StringBuffer("\n");
        for (int lcv = 0; lcv < changeDescriptors.length; lcv++) {
            expected.append("  " + lcv + ". " + changeDescriptors[lcv] + "\n");
        }
        
        return "Expected Changes were: " + expected + "Recieved changes are: " + received;
    }
    
    public static void checkChanges(List<Change> changes, ChangeDescriptor... changeDescriptors) {
        if (changes.size() != changeDescriptors.length) {
            Assert.fail(getAssertString(changes, changeDescriptors));
        }
        
        LinkedHashSet<Integer> usedDescriptors = new LinkedHashSet<Integer>();
        HashMap<Integer, List<String>> errors = new HashMap<Integer, List<String>>();
        for (int lcv = 0; lcv < changeDescriptors.length; lcv++) {
            ChangeDescriptor cd = changeDescriptors[lcv];
            
            for (int inner = 0; inner < changes.size(); inner++) {
                if (usedDescriptors.contains(inner)) continue;
                
                Change change = changes.get(inner);
                    
                String isSame = cd.check(change);
                if (isSame == null) {
                    usedDescriptors.add(inner);
                }
                else {
                    List<String> eList = errors.get(inner);
                    if (eList == null) {
                        eList = new LinkedList<String>();
                        errors.put(inner, eList);
                    }
                    eList.add(isSame);
                }
            }
        }
        
        StringBuffer sb = new StringBuffer();
        if (!errors.isEmpty()) {
            for (Map.Entry<Integer, List<String>> errorEntry : errors.entrySet()) {
                Integer changeKey = errorEntry.getKey();
                if (usedDescriptors.contains(changeKey)) continue;
                
                sb.append("Errors for Change " + errorEntry.getKey() + " (" + changes.get(changeKey) + ")\n");
                
                int lcv = 0;
                for (String eError : errorEntry.getValue()) {
                    sb.append("  " + lcv + ". " + eError + "\n");
                    lcv++;
                }
            }
        }
        
        Assert.assertEquals(getAssertString(changes, changeDescriptors) + " usedDescriptors=" + usedDescriptors + "\n" + sb.toString(),
                changes.size(), usedDescriptors.size());
    }
}
