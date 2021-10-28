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

package org.glassfish.hk2.xml.jaxb.internal;

import java.beans.PropertyChangeEvent;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.bind.annotation.XmlTransient;
import javax.xml.namespace.QName;

import org.glassfish.hk2.api.ActiveDescriptor;
import org.glassfish.hk2.api.Customizer;
import org.glassfish.hk2.api.MultiException;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.configuration.hub.api.WriteableBeanDatabase;
import org.glassfish.hk2.configuration.hub.api.WriteableType;
import org.glassfish.hk2.utilities.general.GeneralUtilities;
import org.glassfish.hk2.utilities.reflection.ClassReflectionHelper;
import org.glassfish.hk2.utilities.reflection.Logger;
import org.glassfish.hk2.utilities.reflection.ReflectionHelper;
import org.glassfish.hk2.xml.api.XmlHk2ConfigurationBean;
import org.glassfish.hk2.xml.api.XmlRootHandle;
import org.glassfish.hk2.xml.api.XmlService;
import org.glassfish.hk2.xml.internal.ChildType;
import org.glassfish.hk2.xml.internal.DynamicChangeInfo;
import org.glassfish.hk2.xml.internal.ModelImpl;
import org.glassfish.hk2.xml.internal.ModelPropertyType;
import org.glassfish.hk2.xml.internal.NamespaceBeanLikeMapImpl;
import org.glassfish.hk2.xml.internal.ParentedModel;
import org.glassfish.hk2.xml.internal.QNameUtilities;
import org.glassfish.hk2.xml.internal.Utilities;
import org.glassfish.hk2.xml.internal.XmlDynamicChange;
import org.glassfish.hk2.xml.internal.XmlRootHandleImpl;

/**
 * @author jwells
 *
 */
@XmlTransient
public abstract class BaseHK2JAXBBean implements XmlHk2ConfigurationBean, Serializable {
    private static final long serialVersionUID = 8149986319033910297L;

    private final static boolean DEBUG_GETS_AND_SETS = AccessController.doPrivileged(new PrivilegedAction<Boolean>() {
        @Override
        public Boolean run() {
            return Boolean.parseBoolean(
                System.getProperty("org.jvnet.hk2.properties.xmlservice.jaxb.getsandsets", "false"));
        }
            
    });
    
    private final static String EMPTY = "";
    public final static char XML_PATH_SEPARATOR = '/';
    
    /**
     * All fields, including child lists and direct children for beans
     * with no namespace specified (##default)
     */
    private final NamespaceBeanLikeMap nBeanLikeMap = new NamespaceBeanLikeMapImpl();
    
    /**
     * All children whose type has an identifier.  First key is the xml parameter name, second
     * key is the identifier of the specific child.  Used in lookup operations.  Works
     * as a cache, may not be completely accurate and must be flushed on remove
     * operations
     */
    private final Map<QName, Map<String, BaseHK2JAXBBean>> keyedChildrenCache = new ConcurrentHashMap<QName, Map<String, BaseHK2JAXBBean>>();
    
    /** The model for this, including lists of all children property names */
    // private UnparentedNode model;
    
    /** The parent of this instance, or null if this is a root (or has not been fully initialized yet) */
    private XmlHk2ConfigurationBean parent;
    
    /** My own namespace, which is determined either by my parent or by my root value */
    private String selfNamespace;
    
    /** My own XmlTag, which is determined either by my parent or by my root value */
    private String selfXmlTag;
    
    /** The full instance name this takes, with names from keyed children or ids from unkeyed multi children */
    private String instanceName;
    
    /** The value of my key field, if I have one */
    private String keyValue;
    
    /** The global classReflectionHelper, which minimizes reflection */
    private ClassReflectionHelper classReflectionHelper;
    
    /** My own full xmlPath from root */
    private String xmlPath = EMPTY;
    
    /**
     * This object contains the tree locks
     * Once this has been set then all other fields should have been set,
     * and at that point this object is ready for its life as an
     * in-memory node in a tree hierarchy
     */
    private volatile transient DynamicChangeInfo<?> changeControl;
    
    /**
     * The root of this bean, or null if there is no root
     * for this bean
     */
    private volatile transient XmlRootHandleImpl<?> root;
    
    /**
     * If true this bean has been given to the user to
     * add/remove or call getters or setters.  This can
     * happen in two ways, the first being via being parsed
     * from an XML file, the other is via dynamic creation
     */
    private boolean active = false;
    
    /**
     * The descriptor that this bean is advertised with
     */
    private transient ActiveDescriptor<?> selfDescriptor;
    
    /**
     * The cost to add this bean to a tree, which
     * is 1 plus the add cost of all children.  This
     * value is transient and is only calculated under
     * locks and may not be accurate after intended use.
     * The add cost and the remove cost are the same
     */
    private transient int addCost = -1;
    
    /**
     * A map from the namespace prefix to the namespace URI
     */
    private final Map<String, String> prefixToNamespaceMap = new HashMap<String, String>();
    
    /**
     * A map from the namespace URI to the prefix
     */
    private final Map<String, String> namespaceToPrefixMap = new HashMap<String, String>();
    
    /**
     * For JAXB and Serialization
     */
    public BaseHK2JAXBBean() {
        
    }
    
    @Override
    public void _setProperty(String propName, Object propValue) {
        _setProperty(XmlService.DEFAULT_NAMESPACE, propName, propValue);
    }
    
    @Override
    public void _setProperty(String propNamespace, String propName, Object propValue) {
        _setProperty(propNamespace, propName, propValue, true);
    }
    
    public void _setProperty(String propNamespace, String propName, Object propValue, boolean changeInHub) {
        _setProperty(propNamespace, propName, propValue, changeInHub, false);
    }
    
    public void _setProperty(QName qName, Object propValue) {
        String namespace = QNameUtilities.getNamespace(qName);
        String propName = qName.getLocalPart();
        
        _setProperty(namespace, propName, propValue);
    }
    
    public void _setProperty(QName qName, Object propValue, boolean changeInHub) {
        String namespace = QNameUtilities.getNamespace(qName);
        String propName = qName.getLocalPart();
        
        _setProperty(namespace, propName, propValue, changeInHub);
    }
    
    @SuppressWarnings("unchecked")
    public void _setProperty(String propNamespace, String propName, Object propValue, boolean changeInHub, boolean rawSet) {
        if (propNamespace == null || propName == null) throw new IllegalArgumentException(
                "properyName or propertyNamespace may not be null");
        
        if (DEBUG_GETS_AND_SETS) {
            // Hidden behind static because of potential expensive toString costs
            Logger.getLogger().debug("XmlService setting property " + propName + " to " + propValue + " in " + this + " rawSet=" + rawSet);
        }
        
        if (propValue != null && (propValue instanceof List)) {
            // All lists are unmodifiable and ArrayLists
            if (propValue instanceof ArrayList) {
                propValue = Collections.unmodifiableList((ArrayList<Object>) propValue);
            }
            else {
                propValue = Collections.unmodifiableList(new ArrayList<Object>((List<Object>) propValue));
            }
        }
        
        if (changeControl == null) {
            if (active) {
                synchronized (this) {
                    nBeanLikeMap.setValue(propNamespace, propName, propValue);
                }
            }
            else {
                nBeanLikeMap.setValue(propNamespace, propName, propValue);
            }
        }
        else {
            boolean doAdd = false;
            boolean doRemove = false;
            boolean doModify = false;
            boolean doDirectReplace = false;
            Object currentValue = null;
            
            String directCurrentKey = null;
            String directNewKey = null;
            
            if (!rawSet) {
                changeControl.getReadLock().lock();
                
                try {
                    currentValue = nBeanLikeMap.getValue(propNamespace, propName);
                
                    // If both null this goes, or if they are somehow exactly the same
                    if (currentValue == propValue) return;
                
                    ModelImpl model = _getModel();
                    ParentedModel childModel = model.getChild(propNamespace, propName);
                    if (childModel != null) {
                        if (ChildType.DIRECT.equals(childModel.getChildType())) {
                            if (currentValue == null && propValue != null) {
                                // This is an add
                                doAdd = true;
                                
                                QName keyQName = childModel.getChildModel().getKeyProperty();
                                String childkeyNamespace = keyQName == null ? null : keyQName.getNamespaceURI();
                                String childKeyProperty = keyQName == null ? null : keyQName.getLocalPart();
                                if (childKeyProperty != null) {
                                    directNewKey = (String) ((BaseHK2JAXBBean) propValue)._getProperty(childkeyNamespace, childKeyProperty);
                                }
                            }
                            else if (currentValue != null && propValue == null) {
                                // This is a remove
                                doRemove = true;
                            }
                            else {
                                // Both beans are different
                                QName keyQName = childModel.getChildModel().getKeyProperty();
                                String childkeyNamespace = keyQName == null ? null : keyQName.getNamespaceURI();
                                String childKeyProperty = keyQName == null ? null : keyQName.getLocalPart();
                                if (childKeyProperty != null) {
                                    directCurrentKey = (String) ((BaseHK2JAXBBean) currentValue)._getProperty(childkeyNamespace, childKeyProperty);
                                    directNewKey = (String) ((BaseHK2JAXBBean) propValue)._getProperty(childkeyNamespace, childKeyProperty);
                                    
                                    if (GeneralUtilities.safeEquals(directCurrentKey, directNewKey)) {
                                        doModify = true;
                                    }
                                    else {
                                        doDirectReplace = true;
                                    }
                                }
                                else {
                                    doModify = true;
                                }
                            }
                        }
                        else {
                            // Direct modification of a child
                            doModify = true;
                        }
                    }
                }
                finally {
                    changeControl.getReadLock().unlock();
                }
            }
            
            if (doAdd) {
                _doAdd(propNamespace, propName, propValue, directNewKey, -1);
                return;
            }
            if (doRemove) {
                _doRemove(propNamespace, propName, null, -1, currentValue);
                return;
            }
            if (doModify) {
                _doModify(propNamespace, propName, currentValue, propValue);
                return;
            }
            if (doDirectReplace) {
                changeControl.getWriteLock().lock();
                try {
                    boolean success = false;
                    changeControl.startOrContinueChange(this);
                    try {
                        _doRemove(propNamespace, propName, directCurrentKey, -1, currentValue, false);
                        _doAdd(propNamespace, propName, propValue, directNewKey, -1, true);
                        success = true;
                    }
                    finally {
                        changeControl.endOrDeferChange(success);
                    }
                }
                finally {
                    changeControl.getWriteLock().unlock();
                }
                return;
            }
            
            QName keyQName = _getModel().getKeyProperty();
            String keyPropertyNamespace = keyQName == null ? null : QNameUtilities.getNamespace(keyQName);
            String keyProperty = keyQName == null ? null : keyQName.getLocalPart();
            if (keyProperty != null && propName.equals(keyProperty) && (keyValue != null) &&
                    keyPropertyNamespace != null && propNamespace.equals(keyPropertyNamespace)) {
                throw new IllegalArgumentException("The key property of a bean (" + keyProperty + ") may not be changed from " +
                  keyValue + " to " + propValue);
            }
            
            changeControl.getWriteLock().lock();
            try {
                boolean success = false;
                
                changeControl.startOrContinueChange(this);
                try {
                    if (!rawSet) {
                        Object oValue = nBeanLikeMap.getValue(propNamespace, propName);
                       
                        Utilities.invokeVetoableChangeListeners(changeControl, this,
                            oValue, propValue, propName, classReflectionHelper);
                    }
                
                    if (changeInHub) {
                        changeInHubDirect(propNamespace, propName, propValue);
                    }
                    
                    nBeanLikeMap.backup();
                
                    nBeanLikeMap.setValue(propNamespace, propName, propValue);
                    
                    success = true;
                }
                finally {
                    changeControl.endOrDeferChange(success);
                }
            }
            finally {
                changeControl.getWriteLock().unlock();
            }
        }
    }
    
    public void _setProperty(String propNamespace, String propName, byte propValue) {
        if (propName == null) throw new IllegalArgumentException("properyName may not be null");
        
        _setProperty(propNamespace, propName, (Byte) propValue);
    }
    
    public void _setProperty(String propNamespace, String propName, boolean propValue) {
        if (propName == null) throw new IllegalArgumentException("properyName may not be null");
        
        _setProperty(propNamespace, propName, (Boolean) propValue);
    }
    
    public void _setProperty(String propNamespace, String propName, char propValue) {
        if (propName == null) throw new IllegalArgumentException("properyName may not be null");
        
        _setProperty(propNamespace, propName, (Character) propValue);
    }
    
    public void _setProperty(String propNamespace, String propName, short propValue) {
        if (propName == null) throw new IllegalArgumentException("properyName may not be null");
        
        _setProperty(propNamespace, propName, (Short) propValue);
    }
    
    public void _setProperty(String propNamespace, String propName, int propValue) {
        if (propName == null) throw new IllegalArgumentException("properyName may not be null");
        
        _setProperty(propNamespace, propName, (Integer) propValue);
    }
    
    public void _setProperty(String propNamespace, String propName, float propValue) {
        if (propName == null) throw new IllegalArgumentException("properyName may not be null");
        
        _setProperty(propNamespace, propName, (Float) propValue);
    }
    
    public void _setProperty(String propNamespace, String propName, long propValue) {
        if (propName == null) throw new IllegalArgumentException("properyName may not be null");
        
        _setProperty(propNamespace, propName, (Long) propValue);
    }
    
    public void _setProperty(String propNamespace, String propName, double propValue) {
        if (propName == null) throw new IllegalArgumentException("properyName may not be null");
        
        _setProperty(propNamespace, propName, (Double) propValue);
    }
    
    private Object _getProperty(String propNamespace, String propName, Class<?> expectedClass) {
        return _getProperty(propNamespace, propName, expectedClass, null);
    }
    
    private Object _getProperty(String propNamespace, String propName, Class<?> expectedClass, ParentedModel parentNode) {
        if (propNamespace == null) throw new IllegalArgumentException("propNamespace must not be null");
        
        boolean isSet;
        Object retVal;
        boolean doDefaulting = active ? true : false;
        
        if (changeControl == null) {
            if (active) {
                synchronized (this) {
                    isSet = nBeanLikeMap.isSet(propNamespace, propName);
                    retVal = nBeanLikeMap.getValue(propNamespace, propName);
                }
            }
            else {
                isSet = nBeanLikeMap.isSet(propNamespace, propName);
                retVal = nBeanLikeMap.getValue(propNamespace, propName);
            }
        }
        else {
            changeControl.getReadLock().lock();
            try {
                doDefaulting = true;
                isSet = nBeanLikeMap.isSet(propNamespace, propName);
                retVal = nBeanLikeMap.getValue(propNamespace, propName);
            }
            finally {
                changeControl.getReadLock().unlock();
            }
        }
        
        if (doDefaulting && (retVal == null) && !isSet) {
            if (expectedClass != null) {
                retVal = Utilities.getDefaultValue(_getModel().getDefaultChildValue(propNamespace, propName), expectedClass, prefixToNamespaceMap);
            }
            else if (parentNode != null) {
                switch (parentNode.getChildType()) {
                case LIST:
                    retVal = Collections.EMPTY_LIST;
                    break;
                case ARRAY:
                    Class<?> cType = parentNode.getChildModel().getOriginalInterfaceAsClass();
                    retVal = Array.newInstance(cType, 0);
                    break;
                case DIRECT:
                default:
                    break;
                
                }
                
            }
        }
        
        if (DEBUG_GETS_AND_SETS) {
            // Hidden behind static because of potential expensive toString costs
            Logger.getLogger().debug("XmlService getting property " + propName + "=" + retVal + " in " + this);
        }
        
        return retVal;
    }
    
    public Object _getProperty(QName qName) {
        String propNamespace = QNameUtilities.getNamespace(qName);
        String propName = qName.getLocalPart();
        
        return _getProperty(propNamespace, propName);
    }
    
    /**
     * Called by proxy
     * 
     * @param propName Property of child or non-child element or attribute
     * @return Value
     */
    @Override
    public Object _getProperty(String propNamespace, String propName) {
        ModelImpl model = _getModel();
        ModelPropertyType mpt = model.getModelPropertyType(propNamespace, propName);
        
        switch(mpt) {
        case FLAT_PROPERTY:
            return _getProperty(propNamespace, propName, model.getNonChildType(propNamespace, propName));
        case TREE_ROOT:
            ParentedModel parent = model.getChild(propNamespace, propName);
            
            return _getProperty(parent.getChildXmlNamespace(), parent.getChildXmlTag(), null, parent);
        case UNKNOWN:
        default:
            throw new AssertionError("Unknown type " + mpt + " for " + propName + " in " + this);
        }
    }
    
    @Override
    public Object _getProperty(String propName) {
        return _getProperty(XmlService.DEFAULT_NAMESPACE, propName);
    }
    
    /**
     * Called by proxy
     * 
     * @param propName
     * @return
     */
    public boolean _getPropertyZ(String propNamespace, String propName) {
        return (Boolean) _getProperty(propNamespace, propName, boolean.class);
    }
    
    /**
     * Called by proxy
     * 
     * @param propName
     * @return
     */
    public byte _getPropertyB(String propNamespace, String propName) {
        return (Byte) _getProperty(propNamespace, propName, byte.class);
    }
    
    /**
     * Called by proxy
     * 
     * @param propName
     * @return
     */
    public char _getPropertyC(String propNamespace, String propName) {
        return (Character) _getProperty(propNamespace, propName, char.class);
    }
    
    /**
     * Called by proxy
     * 
     * @param propName
     * @return
     */
    public short _getPropertyS(String propNamespace, String propName) {
        return (Short) _getProperty(propNamespace, propName, short.class);
    }
    
    /**
     * Called by proxy
     * 
     * @param propName
     * @return
     */
    public int _getPropertyI(String propNamespace, String propName) {
        return (Integer) _getProperty(propNamespace, propName, int.class);
    }
    
    /**
     * Called by proxy
     * 
     * @param propName
     * @return
     */
    public float _getPropertyF(String propNamespace, String propName) {
        return (Float) _getProperty(propNamespace, propName, float.class);
    }
    
    /**
     * Called by proxy
     * 
     * @param propName
     * @return
     */
    public long _getPropertyJ(String propNamespace, String propName) {
        return (Long) _getProperty(propNamespace, propName, long.class);
    }
    
    /**
     * Called by proxy
     * 
     * @param propName
     * @return
     */
    public double _getPropertyD(String propNamespace, String propName) {
        return (Double) _getProperty(propNamespace, propName, double.class);
    }
    
    @SuppressWarnings("unchecked")
    private Object internalLookup(String propNamespace, String propName, String keyValue) {
        QName propertyQName = QNameUtilities.createQName(propNamespace, propName);
        
        // First look in the cache
        Object retVal = null;
        
        Map<String, BaseHK2JAXBBean> byName;
        byName = keyedChildrenCache.get(propertyQName);
        if (byName != null) {
            retVal = byName.get(keyValue);
        }
        
        if (retVal != null) {
            // Found it in cache!
            return retVal;
        }
        
        // Now do it the hard way
        Object prop = _getProperty(propNamespace, propName);
        if (prop == null) return null;  // Just not found
        
        if (prop instanceof List) {
            for (BaseHK2JAXBBean child : (List<BaseHK2JAXBBean>) prop) {
                if (GeneralUtilities.safeEquals(keyValue, child._getKeyValue())) {
                    // Add it to the cache
                    if (byName == null) {
                        byName = new ConcurrentHashMap<String, BaseHK2JAXBBean>();
                        
                        keyedChildrenCache.put(propertyQName, byName);
                    }
                    
                    byName.put(keyValue, child);
                    
                    // and return
                    return child;
                }
            }
        }
        else if (prop.getClass().isArray()) {
            for (Object childRaw : (Object[]) prop) {
                BaseHK2JAXBBean child = (BaseHK2JAXBBean) childRaw;
                
                if (GeneralUtilities.safeEquals(keyValue, child._getKeyValue())) {
                    // Add it to the cache
                    if (byName == null) {
                        byName = new ConcurrentHashMap<String, BaseHK2JAXBBean>();
                        
                        keyedChildrenCache.put(propertyQName, byName);
                    }
                    
                    byName.put(keyValue, child);
                    
                    // and return
                    return child;
                }
            }
        }
        
        // Just not found
        return null;
    }
    
    @Override
    public Object _lookupChild(String propName, String keyValue) {
        return _lookupChild(XmlService.DEFAULT_NAMESPACE, propName, keyValue);
    }
    
    @Override
    public Object _lookupChild(String propNamespace, String propName, String keyValue) {
        if (changeControl == null) {
            return internalLookup(propNamespace, propName, keyValue);
        }
        
        changeControl.getReadLock().lock();
        try {
            return internalLookup(propNamespace, propName, keyValue);
        }
        finally {
            changeControl.getReadLock().unlock();
        }
    }
    
    public Object _doAdd(String propNamespace, String childProperty, Object rawChild, String childKey, int index) {
        return _doAdd(propNamespace, childProperty, rawChild, childKey, index, true);
    }
    
    public Object _doAdd(String propNamespace, String childProperty, Object rawChild, String childKey, int index, boolean changeList) {
        if (changeControl == null) {
            return Utilities.internalAdd(this, propNamespace, childProperty, rawChild, childKey, index, null, XmlDynamicChange.EMPTY, new LinkedList<ActiveDescriptor<?>>(), changeList);
        }
        
        changeControl.getWriteLock().lock();
        try {
            Object oldValue = nBeanLikeMap.getValue(propNamespace, childProperty);
            
            LinkedList<ActiveDescriptor<?>> addedServices = new LinkedList<ActiveDescriptor<?>>();
            Object retVal;
            boolean success = false;
            XmlDynamicChange change = changeControl.startOrContinueChange(this);
            try {
                retVal = Utilities.internalAdd(this, propNamespace, childProperty, rawChild, childKey, index, changeControl, change, addedServices, changeList);
                
                Object newValue = nBeanLikeMap.getValue(propNamespace, childProperty);
                
                Utilities.invokeVetoableChangeListeners(changeControl, this,
                        oldValue, newValue, childProperty, classReflectionHelper);
                
                success = true;
            }
            finally {
                changeControl.endOrDeferChange(success);
            }
            
            ServiceLocator locator = changeControl.getServiceLocator();
            for (ActiveDescriptor<?> added : addedServices) {
                // Ensures that any defaulting listeners are run
                locator.getServiceHandle(added).getService();
            }
            
            return retVal;
        }
        finally {
            changeControl.getWriteLock().unlock();
        }
    }
    
    private void _doModify(String propNamespace, String propName, Object currentValue, Object newValue) {
        if (root == null) {
            throw new IllegalStateException("A direct set will only work on a rooted bean");
        }
        
        changeControl.getWriteLock().lock();
        try {
            boolean success = false;
            XmlDynamicChange change = changeControl.startOrContinueChange(this);
            try {
                Utilities.internalModifyChild(this, propNamespace, propName, currentValue, newValue, root, changeControl, change);
                
                success = true;
            }
            finally {
                changeControl.endOrDeferChange(success);
            }
            
        }
        finally {
            changeControl.getWriteLock().unlock();
        }
        
    }
    
    public Object _invokeCustomizedMethod(String methodName, Class<?>[] params, Object[] values) {
        if (DEBUG_GETS_AND_SETS) {
            // Hidden behind static because of potential expensive toString costs
            Logger.getLogger().debug("XmlService invoking customized method " + methodName +
                    " with params " + Arrays.toString(params) + " adn values " + Arrays.toString(values));
        }
        Class<?> tClass = getClass();
        Customizer customizer = tClass.getAnnotation(Customizer.class);
        if (customizer == null) {
            throw new RuntimeException("Method " + methodName + " was called on class " + tClass.getName() +
                    " with no customizer, failing");
        }
        
        Class<?> cClassArray[] = customizer.value();
        String cNameArray[] = customizer.name();
        
        if (cNameArray.length > 0 && cClassArray.length != cNameArray.length) {
            throw new RuntimeException("The @Customizer annotation must have the value and name arrays be of equal size.  " +
              "The class array is of size " + cClassArray.length + " while the name array is of size " + cNameArray.length +
              " for class " + tClass.getName());
        }
        
        LinkedList<Throwable> errors = new LinkedList<Throwable>();
        for (int lcv = 0; lcv < cClassArray.length; lcv++) {
            Class<?> cClass = cClassArray[lcv];
            String cName = (cNameArray.length == 0) ? null : cNameArray[lcv] ;
            
            Object cService = null;
            if (cName == null || "".equals(cName)) {
                cService = changeControl.getServiceLocator().getService(cClass);
            }
            else {
                cService = changeControl.getServiceLocator().getService(cClass, cName);
            }
        
            if (cService == null) {
                if (customizer.failWhenMethodNotFound()) {
                    errors.add(new RuntimeException("Method " + methodName + " was called on class " + tClass.getName() +
                        " but service " + cClass.getName() + " with name " + cName + " was not found"));
                }
            
                continue;
            }
        
            
            ModelImpl model = _getModel();
            Class<?> topInterface = (model == null) ? null : model.getOriginalInterfaceAsClass() ;
            
            Method cMethod = Utilities.findSuitableCustomizerMethod(cClass, methodName, params, topInterface);
            if (cMethod == null) {
                if (customizer.failWhenMethodNotFound()) {
                    errors.add(new RuntimeException("No customizer method with name " + methodName + " was found on customizer " + cClass.getName() +
                            " with parameters " + Arrays.toString(params) + " for bean " + tClass.getName()));
                }
                
                continue;
            }
             
            boolean useAlt = false;
            if (cMethod.getParameterTypes().length == (params.length + 1)) useAlt = true;
            
            if (useAlt) {
                Object altValues[] = new Object[values.length + 1];
                altValues[0] = this;
                for (int lcv2 = 0; lcv2 < values.length; lcv2++) {
                    altValues[lcv2 + 1] = values[lcv2];
                }
            
                values = altValues;
            }
        
            try {
                return ReflectionHelper.invoke(cService, cMethod, values, false);
            }
            catch (RuntimeException re) {
                throw re;
            }
            catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
        
        if (errors.isEmpty()) {
            return null;
        }
        
        throw new MultiException(errors);
    }
    
    public int _invokeCustomizedMethodI(String methodName, Class<?>[] params, Object[] values) {
        return ((Integer) _invokeCustomizedMethod(methodName, params, values)).intValue();
    }
    
    public long _invokeCustomizedMethodJ(String methodName, Class<?>[] params, Object[] values) {
        return ((Long) _invokeCustomizedMethod(methodName, params, values)).longValue();
    }
    
    public boolean _invokeCustomizedMethodZ(String methodName, Class<?>[] params, Object[] values) {
        return ((Boolean) _invokeCustomizedMethod(methodName, params, values)).booleanValue();
    }
    
    public byte _invokeCustomizedMethodB(String methodName, Class<?>[] params, Object[] values) {
        return ((Byte) _invokeCustomizedMethod(methodName, params, values)).byteValue();
    }
    
    public char _invokeCustomizedMethodC(String methodName, Class<?>[] params, Object[] values) {
        return ((Character) _invokeCustomizedMethod(methodName, params, values)).charValue();
    }
    
    public short _invokeCustomizedMethodS(String methodName, Class<?>[] params, Object[] values) {
        return ((Short) _invokeCustomizedMethod(methodName, params, values)).shortValue();
    }
    
    public float _invokeCustomizedMethodF(String methodName, Class<?>[] params, Object[] values) {
        return ((Float) _invokeCustomizedMethod(methodName, params, values)).floatValue();
    }
    
    public double _invokeCustomizedMethodD(String methodName, Class<?>[] params, Object[] values) {
        return ((Double) _invokeCustomizedMethod(methodName, params, values)).doubleValue();
    }
    
    public Object _doRemove(String propNamespace, String childProperty, String childKey, int index, Object child) {
        return _doRemove(propNamespace, childProperty, childKey, index, child, true);
    }
    
    public Object _doRemove(String propNamespace, String childProperty, String childKey, int index, Object child, boolean changeList) {
        QName childPropQName = QNameUtilities.createQName(propNamespace, childProperty);
        
        if (changeControl == null) {
            Object retVal = Utilities.internalRemove(this, propNamespace, childProperty, childKey, index, child, null, XmlDynamicChange.EMPTY, changeList);
            
            if (retVal != null) {
                keyedChildrenCache.remove(childPropQName);
            }
            
            return retVal;
        }
        
        changeControl.getWriteLock().lock();
        try {
            XmlDynamicChange xmlDynamicChange = changeControl.startOrContinueChange(this);
            
            Object retVal;
            boolean success = false;
            try {
                Object oldVal = nBeanLikeMap.getValue(propNamespace, childProperty);
                
                retVal = Utilities.internalRemove(this, propNamespace, childProperty, childKey, index, child, changeControl, xmlDynamicChange, changeList);
                
                Object newVal = nBeanLikeMap.getValue(propNamespace, childProperty);
                
                Utilities.invokeVetoableChangeListeners(changeControl, this,
                        oldVal, newVal, childProperty, classReflectionHelper);
                
                success = true;
            }
            finally {
                changeControl.endOrDeferChange(success);
            }
            
            if (retVal != null) {
                keyedChildrenCache.remove(childPropQName);
            }
            
            return retVal;
        }
        finally {
            changeControl.getWriteLock().unlock();
        }
    }
    
    public boolean _doRemoveZ(String propNamespace, String childProperty, String childKey, int index, Object child) {
        Object retVal = _doRemove(propNamespace, childProperty, childKey, index, child);
        return (retVal != null);
    }

    public boolean _hasProperty(String propNamespace, String propName) {
        if (changeControl == null) {
            if (active) {
                synchronized (this) {
                    return nBeanLikeMap.isSet(propNamespace, propName);
                }
            }
            
            return nBeanLikeMap.isSet(propNamespace, propName);
        }
        
        changeControl.getReadLock().lock();
        try {
            return nBeanLikeMap.isSet(propNamespace, propName);
        }
        finally {
            changeControl.getReadLock().unlock();
        }
    }

    /* (non-Javadoc)
     * @see org.glassfish.hk2.xml.api.XmlHk2ConfigurationBean#getBeanLikeMap()
     */
    @Override
    public Map<String, Object> _getBeanLikeMap() {
        if (changeControl == null) {
            if (active) {
                synchronized (this) {
                    return Collections.unmodifiableMap(nBeanLikeMap.getBeanLikeMap(namespaceToPrefixMap));
                }
            }
            return Collections.unmodifiableMap(nBeanLikeMap.getBeanLikeMap(namespaceToPrefixMap));
        }
        
        changeControl.getReadLock().lock();
        try {
            return Collections.unmodifiableMap(nBeanLikeMap.getBeanLikeMap(namespaceToPrefixMap));
        }
        finally {
            changeControl.getReadLock().unlock();
        }
    }
    
    public Map<QName, Object> _getQNameMap() {
        if (changeControl == null) {
            if (active) {
                synchronized (this) {
                    return Collections.unmodifiableMap(nBeanLikeMap.getQNameMap());
                }
            }
            return Collections.unmodifiableMap(nBeanLikeMap.getQNameMap());
        }
        
        changeControl.getReadLock().lock();
        try {
            return Collections.unmodifiableMap(nBeanLikeMap.getQNameMap());
        }
        finally {
            changeControl.getReadLock().unlock();
        }
        
    }
    
    /* (non-Javadoc)
     * @see org.glassfish.hk2.xml.api.XmlHk2ConfigurationBean#getParent()
     */
    @Override
    public XmlHk2ConfigurationBean _getParent() {
        return parent;
    }
    
    /**
     * When this is called all of the parents can be
     * found and so the namespaces can be determined
     * 
     * @param parent
     */
    public void _setParent(XmlHk2ConfigurationBean parent) {
        this.parent = parent;
    }
    
    public void _setSelfXmlTag(String selfNamespace, String selfXmlTag) {
        this.selfNamespace = selfNamespace;
        this.selfXmlTag = selfXmlTag;
    }
    
    public String _getSelfNamespace() {
        return selfNamespace;
    }
    
    public String _getSelfXmlTag() {
        return selfXmlTag;
    }

    /* (non-Javadoc)
     * @see org.glassfish.hk2.xml.api.XmlHk2ConfigurationBean#_getXmlPath()
     */
    @Override
    public String _getXmlPath() {
        return xmlPath;
    }
    
    public void _setInstanceName(String name) {
        instanceName = name;
    }

    /* (non-Javadoc)
     * @see org.glassfish.hk2.xml.api.XmlHk2ConfigurationBean#_getInstanceName()
     */
    @Override
    public String _getInstanceName() {
        return instanceName;
    }
    
    public void _setKeyValue(String key) {
        keyValue = key;
    }
    
    /* (non-Javadoc)
     * @see org.glassfish.hk2.xml.api.XmlHk2ConfigurationBean#_getKeyPropertyName()
     */
    @Override
    public QName _getKeyPropertyName() {
        return _getModel().getKeyProperty();
    }
    
    public void _setClassReflectionHelper(ClassReflectionHelper helper) {
        this.classReflectionHelper = helper;
    }
    
    @Override
    public String _getKeyValue() {
        return keyValue;
    }
    
    private static String calculateXmlPath(BaseHK2JAXBBean leaf) {
        LinkedList<String> stack = new LinkedList<String>();
        while (leaf != null) {
            stack.addFirst(leaf._getSelfXmlTag());
            
            leaf = (BaseHK2JAXBBean) leaf._getParent();
        }
        
        StringBuffer sb = new StringBuffer();
        for (String component : stack) {
            sb.append(XML_PATH_SEPARATOR + component);
        }
        
        return sb.toString();
    }
    
    public void _setDynamicChangeInfo(XmlRootHandleImpl<?> root, DynamicChangeInfo<?> change) {
        _setDynamicChangeInfo(root, change, true);
    }
    
    /**
     * Once this is set the dynamic change protocol is in effect,
     * and all paths can be calculated
     * 
     * @param change The change control object
     * @param doXmlPathCalculation if true then this should calculate the xml path at this time
     * (all the parent information must be correct).  If this is false it is assumed that this
     * is some sort of copy operation where the xmlPath has been pre-calculated and does not
     * need to be modified
     */
    public void _setDynamicChangeInfo(XmlRootHandleImpl<?> root, DynamicChangeInfo<?> change, boolean doXmlPathCalculation) {
        if (doXmlPathCalculation) {
            xmlPath = calculateXmlPath(this);
        }
        
        changeControl = change;
        this.root = root;
        
        Utilities.calculateNamespaces(this, root, prefixToNamespaceMap);
        
        // Reverse the prefixToNamespace for the namespaceToPrefix map
        for (Map.Entry<String, String> entry : prefixToNamespaceMap.entrySet()) {
            namespaceToPrefixMap.put(entry.getValue(), entry.getKey());
        }
        
        if (changeControl != null) active = true;
    }
    
    /**
     * Once this has been set the bean is considered active, and
     * so defaulting can happen on the bean
     */
    public void _setActive() {
        active = true;
    }
    
    /**
     * Read lock must be held
     * 
     * @return The set of all children tags
     */
    public Set<QName> _getChildrenXmlTags() {
        HashSet<QName> retVal = new HashSet<QName>(_getModel().getKeyedChildren());
        retVal.addAll(_getModel().getUnKeyedChildren());
        
        return retVal;
    }
    
    /**
     * This copy method ONLY copies non-child and
     * non-parent and optionally reference fields and so is
     * not a full copy.  The children and parent and
     * reference and lock information need to be filled
     * in later so as not to have links from one tree into
     * another.  The read lock of copyMe should be held
     * 
     * @param copyMe The non-null bean to copy FROM
     */
    public void _shallowCopyFrom(BaseHK2JAXBBean copyMe, boolean copyReferences) {
        selfNamespace = copyMe.selfNamespace;
        selfXmlTag = copyMe.selfXmlTag;
        instanceName = copyMe.instanceName;
        keyValue = copyMe.keyValue;
        xmlPath = copyMe.xmlPath;
        
        nBeanLikeMap.shallowCopy(copyMe.nBeanLikeMap, copyMe._getModel(), copyReferences);
    }
    
    /**
     * Called under write lock
     * 
     * @param propName The name of the property to change
     * @param propValue The new value of the property
     */
    public boolean _changeInHub(String propNamespace, String propName, Object propValue, WriteableBeanDatabase wbd) {
        Object oldValue = nBeanLikeMap.getValue(propNamespace, propName);
        if (GeneralUtilities.safeEquals(oldValue, propValue)) {
            // Calling set, but the value was not in fact changed
            return false;
        }
        
        WriteableType wt = wbd.getWriteableType(xmlPath);
        
        HashMap<String, Object> modified = new HashMap<String, Object>(nBeanLikeMap.getBeanLikeMap(namespaceToPrefixMap));
        modified.put(propName, propValue);
            
        wt.modifyInstance(instanceName, modified);
            
        return true;
    }
    
    /**
     * Called under write lock
     * 
     * @param propName The name of the property to change
     * @param propValue The new value of the property
     */
    public boolean _changeInHub(List<PropertyChangeEvent> events, WriteableBeanDatabase wbd) {
        WriteableType wt = wbd.getWriteableType(xmlPath);
        HashMap<String, Object> modified = new HashMap<String, Object>(nBeanLikeMap.getBeanLikeMap(namespaceToPrefixMap));
        List<PropertyChangeEvent> effectiveChanges = new ArrayList<PropertyChangeEvent>(events.size());
        
        for (PropertyChangeEvent event : events) {
            String propName = event.getPropertyName();
            Object oldValue = event.getOldValue();
            Object newValue = event.getNewValue();
            
            if (GeneralUtilities.safeEquals(oldValue, newValue)) {
                continue;
            }
            
            effectiveChanges.add(event);
            modified.put(propName, newValue);   
        }
        
        boolean madeAChange = !effectiveChanges.isEmpty();
        if (madeAChange) {
            wt.modifyInstance(instanceName, modified, effectiveChanges.toArray(new PropertyChangeEvent[effectiveChanges.size()]));
        }
            
        return madeAChange;
    }
    
    /**
     * Write lock MUST be held
     * @param propName
     * @param propValue
     */
    private void changeInHubDirect(String propNamespace, String propName, Object propValue) {
        if (changeControl == null) return;
        
        XmlDynamicChange xmlDynamicChange = changeControl.startOrContinueChange(this);
        
        boolean success = false;
        try {
            WriteableBeanDatabase wbd = xmlDynamicChange.getBeanDatabase();
            
            if (wbd == null) {
                success = true;
                return;
            }
            
            _changeInHub(propNamespace, propName, propValue, wbd);
            
            success = true;
        }
        finally {
            changeControl.endOrDeferChange(success);
        }
        
    }
    
    /**
     * Gets the change control information for this bean
     * 
     * @return the change control information for this bean
     */
    public DynamicChangeInfo<?> _getChangeControl() {
        return changeControl;
    }
    
    /**
     * Returns the reflection helper for this bean
     * 
     * @return The reflection helper for this bean
     */
    public ClassReflectionHelper _getClassReflectionHelper() {
        return classReflectionHelper;
    }
    
    public void _setSelfDescriptor(ActiveDescriptor<?> selfDescriptor) {
        this.selfDescriptor = selfDescriptor;
    }
    
    @Override
    public ActiveDescriptor<?> _getSelfDescriptor() {
        return selfDescriptor;
    }
    
    /**
     * Write lock must be held
     */
    public void __activateChange() {
        nBeanLikeMap.restoreBackup(true);
        addCost = -1;
    }
    
    /**
     * Write lock must be held
     */
    public void __rollbackChange() {
        nBeanLikeMap.restoreBackup(false);
    }
    
    /**
     * Gets the root associated with this bean.  If this bean
     * has no associated root this will return null
     * 
     * @return The root of this bean, or null if this bean
     * is not associated with a root
     */
    @Override
    public XmlRootHandle<?> _getRoot() {
        return root;
    }
    
    @Override
    public boolean _isSet(String propName) {
        return _isSet(XmlService.DEFAULT_NAMESPACE, propName);
    }
    
    @Override
    public boolean _isSet(String propNamespace, String propName) {
        if (changeControl == null) {
            if (active) {
                synchronized (this) {
                    return nBeanLikeMap.isSet(propNamespace, propName);
                }
            }
            
            return nBeanLikeMap.isSet(propNamespace, propName);
        }
        
        changeControl.getReadLock().lock();
        try {
            return nBeanLikeMap.isSet(propNamespace, propName);
        }
        finally {
            changeControl.getReadLock().unlock();
        }
        
    }
    
    public void __setAddCost(int addCost) {
        this.addCost = addCost;
    }
    
    public int __getAddCost() {
        return addCost;
    }
    
    @SuppressWarnings("unchecked")
    public void __fixAlias(String propNamespace, String propName, String baseName) {
        Object propNameValueRaw = nBeanLikeMap.getValue(propNamespace, propName);
        if (propNameValueRaw == null) return;
        if (!(propNameValueRaw instanceof List)) {
            throw new AssertionError("Aliasing with XmlElements only works with List type.  Found " + propNameValueRaw);
        }
        
        List<Object> propNameValue = (List<Object>) propNameValueRaw;
        if (propNameValue.isEmpty()) return;
        
        Object baseNamePropertyRaw = nBeanLikeMap.getValue(propNamespace, baseName);
        if (baseNamePropertyRaw == null) {
            baseNamePropertyRaw = new ArrayList<Object>(propNameValue.size());
            nBeanLikeMap.setValue(propNamespace, baseName, baseNamePropertyRaw);
        }
        
        if (!(baseNamePropertyRaw instanceof List)) {
            throw new AssertionError("Aliasing with XmlElements only works with List type.  Found " + baseNamePropertyRaw);
        }
        
        List<Object> baseNameProperty = (List<Object>) baseNamePropertyRaw;
        
        baseNameProperty.addAll(propNameValue);
    }
    
    @Override
    public String toString() {
        return "BaseHK2JAXBBean(XmlPath=" + xmlPath +
                ",instanceName=" + instanceName +
                ",keyValue=" + keyValue +
                ",model=" + _getModel() + 
                "," + System.identityHashCode(this) + ")";
    }
}
