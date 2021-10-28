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

package org.glassfish.hk2.xml.internal;

import java.beans.VetoableChangeListener;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.glassfish.hk2.api.ActiveDescriptor;
import org.glassfish.hk2.api.DynamicConfiguration;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.configuration.hub.api.Hub;
import org.glassfish.hk2.configuration.hub.api.WriteableBeanDatabase;
import org.glassfish.hk2.xml.api.XmlHandleTransaction;
import org.glassfish.hk2.xml.api.XmlHubCommitMessage;
import org.glassfish.hk2.xml.api.XmlRootCopy;
import org.glassfish.hk2.xml.api.XmlRootHandle;
import org.glassfish.hk2.xml.jaxb.internal.BaseHK2JAXBBean;
import org.glassfish.hk2.xml.spi.XmlServiceParser;

/**
 * @author jwells
 *
 */
public class XmlRootHandleImpl<T> implements XmlRootHandle<T> {
    private final XmlServiceImpl parent;
    private final Hub hub;
    private T root;
    private final ModelImpl rootNode;
    private URI rootURI;
    private final boolean advertised;
    private final boolean advertisedInHub;
    private final DynamicChangeInfo<T> changeControl;
    
    /* package */ XmlRootHandleImpl(
            XmlServiceImpl parent,
            Hub hub,
            T root,
            ModelImpl rootNode,
            URI rootURI,
            boolean advertised,
            boolean inHub,
            DynamicChangeInfo<T> changes) {
        this.parent = parent;
        this.hub = hub;
        this.root = root;
        this.rootNode = rootNode;
        this.rootURI = rootURI;
        this.advertised = advertised;
        this.advertisedInHub = inHub;
        this.changeControl = changes;
    }

    /* (non-Javadoc)
     * @see org.glassfish.hk2.xml.api.XmlRootHandle#getRoot()
     */
    @Override
    public T getRoot() {
        return root;
    }

    /* (non-Javadoc)
     * @see org.glassfish.hk2.xml.api.XmlRootHandle#getRootClass()
     */
    @SuppressWarnings("unchecked")
    @Override
    public Class<T> getRootClass() {
        return (Class<T>) rootNode.getOriginalInterfaceAsClass();
    }

    /* (non-Javadoc)
     * @see org.glassfish.hk2.xml.api.XmlRootHandle#getURI()
     */
    @Override
    public URI getURI() {
        return rootURI;
    }
    
    /* (non-Javadoc)
     * @see org.glassfish.hk2.xml.api.XmlRootHandle#isAdvertisedInLocator()
     */
    @Override
    public boolean isAdvertisedInLocator() {
        return advertised;
    }

    /* (non-Javadoc)
     * @see org.glassfish.hk2.xml.api.XmlRootHandle#isAdvertisedInHub()
     */
    @Override
    public boolean isAdvertisedInHub() {
        return advertisedInHub;
    }

    /* (non-Javadoc)
     * @see org.glassfish.hk2.xml.api.XmlRootHandle#overlay(org.glassfish.hk2.xml.api.XmlRootHandle)
     */
    @Override
    public void overlay(XmlRootHandle<T> newRoot) {
        if (!(newRoot instanceof XmlRootHandle)) {
            throw new IllegalArgumentException("newRoot must have been created by the same XmlService as this one");
        }
        XmlRootHandleImpl<T> newRootImpl = (XmlRootHandleImpl<T>) newRoot;
        if (newRootImpl.isAdvertisedInHub()) {
            throw new IllegalArgumentException("The newRoot must not be advertised in the Hub");
        }
        if (newRootImpl.isAdvertisedInLocator()) {
            throw new IllegalArgumentException("The newRoot must not be advertised as hk2 services");
        }
        if (root == null) {
            throw new IllegalArgumentException("This XmlRootHandle must have a root to be overlayed");
        }
        T newRootRoot = newRootImpl.getRoot(); 
        if (newRootRoot == null) {
            throw new IllegalArgumentException("The newRoot must have a root to overlay onto this root");
        }
        if (!(newRootRoot instanceof BaseHK2JAXBBean)) {
            throw new IllegalArgumentException("The newRoot has a root of an unknown type: " + newRootRoot.getClass().getName());
        }
        if (!newRootRoot.getClass().equals(root.getClass())) {
            throw new IllegalArgumentException("The two roots must have the same class as this root, instead it is of type " + newRootRoot.getClass().getName());
        }
        if (newRootRoot.equals(root)) {
            throw new IllegalArgumentException("Cannot overlay the same bean on top of itself");
        }
        
        BaseHK2JAXBBean newRootBase = (BaseHK2JAXBBean) newRootRoot;
        BaseHK2JAXBBean oldRootBase = (BaseHK2JAXBBean) root;
        
        boolean success = false;
        XmlHandleTransaction<T> handle = lockForTransaction();
        try {
            Differences differences = Utilities.getDiff(oldRootBase, newRootBase);
            
            if (!differences.getDifferences().isEmpty()) {
                Utilities.applyDiff(differences, changeControl);
            }
            
            success = true;
        }
        finally {
            if (success) {
                handle.commit();
            }
            else {
                handle.abandon();
            }
        }
    }
    
    /* (non-Javadoc)
     * @see org.glassfish.hk2.xml.api.XmlRootHandle#getXmlRootCopy()
     */
    @SuppressWarnings("unchecked")
    @Override
    public XmlRootCopy<T> getXmlRootCopy() {
        // In any case, the child should not be directly given the hub, as
        // it is not reflected in the hub
        DynamicChangeInfo<T> copyController =
                new DynamicChangeInfo<T>(changeControl.getJAUtilities(),
                        null,
                        false,
                        changeControl.getIdGenerator(),
                        null,
                        false,
                        changeControl.getServiceLocator());
        
        changeControl.getReadLock().lock();
        try {
            BaseHK2JAXBBean bean = (BaseHK2JAXBBean) root;
            if (bean == null) {
                return new XmlRootCopyImpl<T>(this, changeControl.getChangeNumber(), null);
            }
        
            BaseHK2JAXBBean copy;
            try {
                Map<ReferenceKey, BaseHK2JAXBBean> referenceMap = new HashMap<ReferenceKey, BaseHK2JAXBBean>();
                List<UnresolvedReference> unresolved = new LinkedList<UnresolvedReference>();
                
                copy = Utilities.doCopy(bean, copyController, null, this, referenceMap, unresolved);
                
                Utilities.fillInUnfinishedReferences(referenceMap, unresolved);
            }
            catch (RuntimeException re) {
                throw re;
            }
            catch (Throwable th) {
                throw new RuntimeException(th);
            }
        
            return new XmlRootCopyImpl<T>(this, changeControl.getChangeNumber(), (T) copy);
        }
        finally {
            changeControl.getReadLock().unlock();
        }
    }
    
    /* package */ long getRevision() {
        return changeControl.getChangeNumber();
    }

    /* (non-Javadoc)
     * @see org.glassfish.hk2.xml.api.XmlRootHandle#addRoot(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    @Override
    public void addRoot(T newRoot) {
        changeControl.getWriteLock().lock();
        try {
            if (root != null) {
                throw new IllegalStateException("An attempt was made to add a root to a handle that already has a root " + this);
            }
            if (!(newRoot instanceof BaseHK2JAXBBean)) {
                throw new IllegalArgumentException("The added bean must be from XmlService.createBean");
            }
            
            WriteableBeanDatabase wbd = null;
            if (advertisedInHub) {
                wbd = hub.getWriteableDatabaseCopy();
            }
            
            DynamicConfiguration config = null;
            if (advertised) {
                config = parent.getDynamicConfigurationService().createDynamicConfiguration();
            }
            
            List<ActiveDescriptor<?>> addedServices = new LinkedList<ActiveDescriptor<?>>();
            BaseHK2JAXBBean copiedRoot = Utilities._addRoot(rootNode,
                    newRoot,
                    changeControl,
                    parent.getClassReflectionHelper(),
                    wbd,
                    config,
                    addedServices,
                    this);
            
            if (config != null) {
                config.commit();
            }
            
            if (wbd != null) {
                wbd.commit(new XmlHubCommitMessage() {});
            }
            
            root = (T) copiedRoot;
            
            ServiceLocator locator = parent.getServiceLocator();
            for (ActiveDescriptor<?> added : addedServices) {
                // Ensures that the defaulters will run right away
                locator.getServiceHandle(added).getService();
            }
        }
        finally {
            changeControl.getWriteLock().unlock();
        }
    }

    /* (non-Javadoc)
     * @see org.glassfish.hk2.xml.api.XmlRootHandle#createAndAddRoot()
     */
    @SuppressWarnings("unchecked")
    @Override
    public void addRoot() {
        addRoot(parent.createBean((Class<T>) rootNode.getOriginalInterfaceAsClass()));
    }

    /* (non-Javadoc)
     * @see org.glassfish.hk2.xml.api.XmlRootHandle#deleteRoot()
     */
    @Override
    public T removeRoot() {
        throw new AssertionError("removeRoot not implemented");
    }
    
    /* (non-Javadoc)
     * @see org.glassfish.hk2.xml.api.XmlRootHandle#getReadOnlyRoot(boolean)
     */
    @Override
    public T getReadOnlyRoot(boolean representDefaults) {
        throw new AssertionError("getReadOnlyRoot not implemented");
    }
    
    /* package */ DynamicChangeInfo<T> getChangeInfo() {
        return changeControl;
    }
    
    /* (non-Javadoc)
     * @see org.glassfish.hk2.xml.api.XmlRootHandle#addChangeListener(java.beans.VetoableChangeListener)
     */
    @Override
    public void addChangeListener(VetoableChangeListener... listeners) {
        changeControl.addChangeListener(listeners);
    }

    /* (non-Javadoc)
     * @see org.glassfish.hk2.xml.api.XmlRootHandle#removeChangeListener(java.beans.VetoableChangeListener)
     */
    @Override
    public void removeChangeListener(VetoableChangeListener... listeners) {
        changeControl.removeChangeListener(listeners);
    }

    /* (non-Javadoc)
     * @see org.glassfish.hk2.xml.api.XmlRootHandle#getChangeListeners()
     */
    @Override
    public List<VetoableChangeListener> getChangeListeners() {
        return changeControl.getChangeListeners();
    }
    
    /* (non-Javadoc)
     * @see org.glassfish.hk2.xml.api.XmlRootHandle#lockForTransaction()
     */
    @Override
    public XmlHandleTransaction<T> lockForTransaction()
            throws IllegalStateException {
        if (changeControl == null) throw new IllegalStateException();
        
        return new XmlHandleTransactionImpl<T>(this, changeControl);
    }
    
    @Override
    public void startValidating() {
        if (changeControl == null) throw new IllegalStateException();
        
        Validator validator = changeControl.findOrCreateValidator();
        
        if (root == null) return;
        
        Set<ConstraintViolation<Object>> violations = validator.<Object>validate(root);
        if (violations == null || violations.isEmpty()) return;
        
        throw new ConstraintViolationException(violations);
    }
    
    /* (non-Javadoc)
     * @see org.glassfish.hk2.xml.api.XmlRootHandle#stopValidating()
     */
    @Override
    public void stopValidating() {
        if (changeControl == null) throw new IllegalStateException();
        changeControl.deleteValidator();
    }

    /* (non-Javadoc)
     * @see org.glassfish.hk2.xml.api.XmlRootHandle#isValidating()
     */
    @Override
    public boolean isValidating() {
        if (changeControl == null) throw new IllegalStateException();
        
        return (changeControl.findValidator() != null);
    }
    
    @Override
    public void marshal(OutputStream outputStream) throws IOException {
        marshal(outputStream, null);
    }
    
    /* (non-Javadoc)
     * @see org.glassfish.hk2.xml.api.XmlRootHandle#marshall(java.io.OutputStream)
     */
    @Override
    public void marshal(OutputStream outputStream, Map<String, Object> options) throws IOException {
        if (changeControl == null) {
            throw new IllegalStateException("marshall May only be called on a fully initialized root handle " + this);
        }
        
        changeControl.getReadLock().lock();
        try {
            XmlServiceParser parser = parent.getParser();
            if (parser == null) {
                XmlStreamImpl.marshall(outputStream, this);
                return;
            }
        
            parser.marshal(outputStream, this, options);
        }
        finally {
            changeControl.getReadLock().unlock();
        }
    }
    
    public Map<String, String> getPackageNamespace(Class<?> clazz) {
        return parent.getPackageNamespace(clazz);
    }
    
    @Override
    public String toString() {
        return "XmlRootHandleImpl(" + root + "," + rootNode + "," + rootURI + "," + System.identityHashCode(this) + ")";
    }

    
}
