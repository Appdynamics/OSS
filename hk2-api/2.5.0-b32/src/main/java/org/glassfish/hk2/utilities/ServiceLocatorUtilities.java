/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012-2015 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package org.glassfish.hk2.utilities;

import java.io.IOException;
import java.io.PrintStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Singleton;

import org.glassfish.hk2.api.ActiveDescriptor;
import org.glassfish.hk2.api.AnnotationLiteral;
import org.glassfish.hk2.api.Context;
import org.glassfish.hk2.api.Descriptor;
import org.glassfish.hk2.api.DuplicateServiceException;
import org.glassfish.hk2.api.DynamicConfiguration;
import org.glassfish.hk2.api.DynamicConfigurationService;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.api.FactoryDescriptors;
import org.glassfish.hk2.api.Filter;
import org.glassfish.hk2.api.Immediate;
import org.glassfish.hk2.api.ImmediateController;
import org.glassfish.hk2.api.IndexedFilter;
import org.glassfish.hk2.api.InheritableThread;
import org.glassfish.hk2.api.MultiException;
import org.glassfish.hk2.api.PerLookup;
import org.glassfish.hk2.api.PerThread;
import org.glassfish.hk2.api.Populator;
import org.glassfish.hk2.api.ServiceHandle;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.ServiceLocatorFactory;
import org.glassfish.hk2.api.ImmediateController.ImmediateServiceState;
import org.glassfish.hk2.internal.ImmediateHelper;
import org.glassfish.hk2.internal.InheritableThreadContext;
import org.glassfish.hk2.internal.PerThreadContext;

/**
 * This is a set of useful utilities for working with {@link ServiceLocator}.
 *
 * @author jwells
 */
public abstract class ServiceLocatorUtilities {
    private final static String DEFAULT_LOCATOR_NAME = "default";

    private final static Singleton SINGLETON = new SingletonImpl();
    private final static PerLookup PER_LOOKUP = new PerLookupImpl();
    private final static PerThread PER_THREAD = new PerThreadImpl();
    private final static InheritableThread INHERITABLE_THREAD = new InheritableThreadImpl();
    private final static Immediate IMMEDIATE = new ImmediateImpl();

    /**
     * This method will add the ability to use the {@link PerThread} scope to
     * the given locator.  If the locator already has a {@link Context} implementation
     * that handles the {@link PerThread} scope this method does nothing.
     *
     * @param locator The non-null locator to enable the PerThread scope on
     * @throws MultiException if there were errors when committing the service
     */
    public static void enablePerThreadScope(ServiceLocator locator) {
        try {
            addClasses(locator, true, PerThreadContext.class);
        }
        catch (MultiException me) {
            if (!isDupException(me)) throw me;
        }
    }

    /**
     * This method will add the ability to use the {@link InheritableThread}
     * scope to the given locator. If the locator already has a {@link Context}
     * implementation that handles the {@link InheritableThread} scope this
     * method does nothing.
     *
     * @param locator The non-null locator to enable the PerThread scope on
     * @throws MultiException if there were errors when committing the service
     */
    public static void enableInheritableThreadScope(ServiceLocator locator) {
        try {
            addClasses(locator, true, InheritableThreadContext.class);
        }
        catch (MultiException me) {
            if (!isDupException(me)) throw me;
        }
    }


    /**
     * This method will add the ability to use the {@link Immediate} scope to
     * the given locator.  If the locator already has a {@link Context} implementation
     * that handles the {@link Immediate} scope this method does nothing.
     * <p>
     * This implementation of {@link Immediate} scope will use a separate thread for
     * instantiating services.  Any failures from {@link Immediate} scoped services
     * will be given to the current set of {@link ImmediateErrorHandler} implementations
     *
     * @param locator The non-null locator to enable the Immediate scope on
     * @throws MultiException if there were errors when committing the service
     */
    public static void enableImmediateScope(ServiceLocator locator) {
        ImmediateController controller = enableImmediateScopeSuspended(locator);
        controller.setImmediateState(ImmediateServiceState.RUNNING);
    }
    
    /**
     * This method will add the ability to use the {@link Immediate} scope to
     * the given locator.  If the locator already has a {@link Context} implementation
     * that handles the {@link Immediate} scope this method does nothing.  The Immediate
     * scope will start in the suspended state, allowing the caller to customize the
     * Immediate scope using the {@link ImmediateController}
     * <p>
     *
     * @param locator The non-null locator to enable the Immediate scope on
     * @return The ImmediateController that can be used to further configure
     * the Immediate scope
     * @throws MultiException if there were errors when committing the service
     */
    public static ImmediateController enableImmediateScopeSuspended(ServiceLocator locator) {
        try {
            addClasses(locator, true, ImmediateContext.class, ImmediateHelper.class);
        }
        catch (MultiException me) {
            if (!isDupException(me)) throw me;
        }
        
        return locator.getService(ImmediateController.class);
    }

    /**
     * This method will bind all of the binders given together in a
     * single config transaction.
     *
     * @param locator The non-null locator to use for the configuration action
     * @param binders The non-null list of binders to be added to the locator
     * @throws MultiException if any error was encountered while binding services
     */
    public static void bind(ServiceLocator locator, Binder... binders) {
        DynamicConfigurationService dcs = locator.getService(DynamicConfigurationService.class);
        DynamicConfiguration config = dcs.createDynamicConfiguration();

        for (Binder binder : binders) {
            binder.bind(config);
        }

        config.commit();
    }

    /**
     * This method will create or find a ServiceLocator with the given name and
     * bind all of the binders given together in a single config transaction.
     *
     * @param name The non-null name of the locator to use for the configuration action
     * @param binders The non-null list of binders to be added to the locator
     * @return The service locator that was either found or created
     * @throws MultiException if any error was encountered while binding services
     */
    public static ServiceLocator bind(String name, Binder... binders) {
        ServiceLocatorFactory factory = ServiceLocatorFactory.getInstance();

        ServiceLocator locator = factory.create(name);
        bind(locator, binders);

        return locator;
    }

    /**
     * This method will create or find a ServiceLocator with the name "default" and
     * bind all of the binders given together in a single config transaction.
     *
     * @param binders The non-null list of binders to be added to the locator
     * @return The service locator that was either found or created
     * @throws MultiException if any error was encountered while binding services
     */
    public static ServiceLocator bind(Binder... binders) {
        return bind(DEFAULT_LOCATOR_NAME, binders);
    }

    /**
     * This method adds one existing object to the given service locator.  The caller
     * of this will not get a chance to customize the descriptor that goes into the
     * locator, and hence must rely completely on the analysis of the system to determine
     * the set of contracts and metadata associated with the descriptor.  The same algorithm
     * is used in this method as in the {@link BuilderHelper#createConstantDescriptor(Object)}
     * method.
     *
     * @param locator The non-null locator to add this descriptor to
     * @param constant The non-null constant to add to the service locator
     * @return The descriptor that was added to the service locator
     */
    public static <T> ActiveDescriptor<T> addOneConstant(ServiceLocator locator, Object constant) {
        if (locator == null || constant == null) throw new IllegalArgumentException();

        return addOneDescriptor(locator, BuilderHelper.createConstantDescriptor(constant), false);
    }

    /**
     * This method adds factory constants to the given locator.  None of the constants
     * may be null.  The returned list will contain the FactoryDescriptors added to
     * the locator.  So while the factories are constant valued, the provide method return
     * values are NOT, and will be invoked according to their normal hk2 lifecycle
     *
     * @param locator The non-null locator to add these factory constants to
     * @param constants The constant factories to add to the locator.  None of the constants
     * in this array may be null
     * @return The descriptors that were added to the service locator.  Will not return null, but
     * may return an empty list (if the constants array was zero length)
     */
    @SuppressWarnings("unchecked")
    public static List<FactoryDescriptors> addFactoryConstants(ServiceLocator locator, Factory<?>... constants) {
        if (locator == null) throw new IllegalArgumentException();

        DynamicConfigurationService dcs = locator.getService(DynamicConfigurationService.class);
        DynamicConfiguration cd = dcs.createDynamicConfiguration();

        LinkedList<FactoryDescriptors> intermediateState = new LinkedList<FactoryDescriptors>();
        for (Factory<?> factoryConstant : constants) {
            if (factoryConstant == null) {
                throw new IllegalArgumentException("One of the factories in " + Arrays.toString(constants) + " is null");
            }

            // This is a trick.  We get the information from the DynamicConfiguration, but then abandon it
            // so that we can replace the factories with constant descriptors instead
            FactoryDescriptors fds = cd.addActiveFactoryDescriptor((Class<Factory<Object>>) factoryConstant.getClass());
            intermediateState.add(fds);
        }

        // Abandon previous dynamic configuration (never commit it)
        cd = dcs.createDynamicConfiguration();

        LinkedList<FactoryDescriptors> retVal = new LinkedList<FactoryDescriptors>();
        int lcv = 0;
        for (FactoryDescriptors fds : intermediateState) {
            final ActiveDescriptor<?> provideMethod = (ActiveDescriptor<?>) fds.getFactoryAsAFactory();
            final Factory<?> constant = constants[lcv++];

            Descriptor constantDescriptor = BuilderHelper.createConstantDescriptor(constant);
            Descriptor addProvideMethod = new DescriptorImpl(provideMethod);

            FactoryDescriptorsImpl fdi = new FactoryDescriptorsImpl(constantDescriptor, addProvideMethod);
            retVal.add(cd.bind(fdi));
        }

        cd.commit();

        return retVal;
    }

    /**
     * This method adds one existing object to the given service locator.  The caller
     * of this will not get a chance to customize the descriptor that goes into the
     * locator, and hence must rely completely on the analysis of the system to determine
     * the set of contracts and metadata associated with the descriptor.  The same algorithm
     * is used in this method as in the {@link BuilderHelper#createConstantDescriptor(Object)}
     * method.
     *
     * @param locator The non-null locator to add this descriptor to
     * @param constant The non-null constant to add to the service locator
     * @param name The name this descriptor should take (may be null)
     * @param contracts The full set of contracts this descriptor should take.  If this
     * field is the empty set then the contracts will be automatically discovered
     * @return The descriptor that was added to the service locator
     */
    public static <T> ActiveDescriptor<T> addOneConstant(ServiceLocator locator, Object constant, String name, Type... contracts) {
        if (locator == null || constant == null) throw new IllegalArgumentException();

        return addOneDescriptor(locator, BuilderHelper.createConstantDescriptor(constant, name, contracts), false);
    }

    /**
     * It is very often the case that one wishes to add a single descriptor to
     * a service locator.  This method adds that one descriptor.  If the descriptor
     * is an {@link ActiveDescriptor} and is reified, it will be added as an
     * {@link ActiveDescriptor}.  Otherwise it will be bound as a {@link Descriptor}.
     * A deep copy will be made of the descriptor passed in
     *
     * @param locator The non-null locator to add this descriptor to
     * @param descriptor The non-null descriptor to add to this locator
     * @return The descriptor that was added to the system
     * @throws MultiException On a commit failure
     */
    public static <T> ActiveDescriptor<T> addOneDescriptor(ServiceLocator locator, Descriptor descriptor) {
        return addOneDescriptor(locator, descriptor, true);
    }

    /**
     * It is very often the case that one wishes to add a single descriptor to
     * a service locator.  This method adds that one descriptor.  If the descriptor
     * is an {@link ActiveDescriptor} and is reified, it will be added as an
     * {@link ActiveDescriptor}.  Otherwise it will be bound as a {@link Descriptor}.
     *
     * @param locator The non-null locator to add this descriptor to
     * @param descriptor The non-null descriptor to add to this locator
     * @param requiresDeepCopy If true a deep copy will be made of the
     * key.  If false then the Descriptor will be used as is, and it
     * is the responsibility of the caller to ensure that the fields
     * of the Descriptor never change (with the exception of any
     * writeable fields, such as ranking)
     * @return The descriptor that was added to the system
     * @throws MultiException On a commit failure
     */
    @SuppressWarnings("unchecked")
    public static <T> ActiveDescriptor<T> addOneDescriptor(ServiceLocator locator, Descriptor descriptor,
            boolean requiresDeepCopy) {
        DynamicConfigurationService dcs = locator.getService(DynamicConfigurationService.class);
        DynamicConfiguration config = dcs.createDynamicConfiguration();

        ActiveDescriptor<T> retVal;
        if (descriptor instanceof ActiveDescriptor) {
            ActiveDescriptor<T> active = (ActiveDescriptor<T>) descriptor;

            if (active.isReified()) {
                retVal = config.addActiveDescriptor(active, requiresDeepCopy);
            }
            else {
                retVal = config.bind(descriptor, requiresDeepCopy);
            }

        }
        else {
            retVal = config.bind(descriptor, requiresDeepCopy);
        }

        config.commit();

        return retVal;
    }

    /**
     * Adds the given factory descriptors to the service locator
     *
     * @param locator The locator to add the factories to.  May not be null
     * @param factories The list of factory descriptors to add to the system.  May not be null
     * @return a list of the FactoryDescriptor descriptors that were added to the service locator
     * @throws MultiException On a commit failure
     */
    public static List<FactoryDescriptors> addFactoryDescriptors(ServiceLocator locator, FactoryDescriptors...factories) {
        return addFactoryDescriptors(locator, true, factories);
    }

    /**
     * Adds the given factory descriptors to the service locator
     *
     * @param locator The locator to add the factories to.  May not be null
     * @param requiresDeepCopy This is false ONLY if every one of the factories given to this method can be used without a copy
     * @param factories The list of factory descriptors to add to the system.  May not be null
     * @return A list of the FactoryDescriptor descriptors that were added to the service locator
     * @throws MultiException On a commit failure
     */
    public static List<FactoryDescriptors> addFactoryDescriptors(ServiceLocator locator, boolean requiresDeepCopy, FactoryDescriptors... factories) {
        if (factories == null || locator == null) throw new IllegalArgumentException();

        List<FactoryDescriptors> retVal = new ArrayList<FactoryDescriptors>(factories.length);

        DynamicConfigurationService dcs = locator.getService(DynamicConfigurationService.class);
        DynamicConfiguration config = dcs.createDynamicConfiguration();

        for (FactoryDescriptors factory : factories) {
            FactoryDescriptors addMe = config.bind(factory, requiresDeepCopy);

            retVal.add(addMe);
        }

        config.commit();

        return retVal;
    }
    
    /**
     * It is very often the case that one wishes to add classes that hk2
     * will automatically analyze for contracts and qualifiers to
     * a service locator.  This method adds those classes.
     * <p>
     * If the class to add implements {@link Factory} then two descriptors
     * will be added, one for the {@link Factory} class itself, and one for
     * the {@link Factory#provide()} method of the factory.  In the output
     * list the descriptor for the {@link Factory} will be added first, followed
     * by the descriptor for the {@link Factory#provide()} method
     *
     * @param locator The non-null locator to add this descriptor to
     * @param toAdd The classes to add to the locator.  If a class in this list implements
     * {@link Factory} then two descriptors will be added for that class
     * @return The list of descriptors added to the system.  Will not return null but
     * may return an empty list
     * @throws MultiException On a commit failure.  If idempotent is true the commit failure
     * may be due to duplicate descriptors found in the locator
     */
    @SuppressWarnings("unchecked")
    public static List<ActiveDescriptor<?>> addClasses(ServiceLocator locator, boolean idempotent, Class<?>... toAdd) {
        DynamicConfigurationService dcs = locator.getService(DynamicConfigurationService.class);
        DynamicConfiguration config = dcs.createDynamicConfiguration();

        LinkedList<ActiveDescriptor<?>> retVal = new LinkedList<ActiveDescriptor<?>>();
        for (Class<?> addMe : toAdd) {
            if (Factory.class.isAssignableFrom(addMe)) {
                FactoryDescriptors fds = config.addActiveFactoryDescriptor((Class<Factory<Object>>) addMe);
                if (idempotent) {
                    config.addIdempotentFilter(BuilderHelper.createDescriptorFilter(fds.getFactoryAsAService(), false));
                    config.addIdempotentFilter(BuilderHelper.createDescriptorFilter(fds.getFactoryAsAFactory(), false));
                }
                retVal.add((ActiveDescriptor<?>) fds.getFactoryAsAService());
                retVal.add((ActiveDescriptor<?>) fds.getFactoryAsAFactory());
            }
            else {
                ActiveDescriptor<?> ad = config.addActiveDescriptor(addMe);
                if (idempotent) {
                    config.addIdempotentFilter(BuilderHelper.createDescriptorFilter(ad, false));
                }
                retVal.add(ad);
            }
        }

        config.commit();

        return retVal;
    }

    /**
     * It is very often the case that one wishes to add classes that hk2
     * will automatically analyze for contracts and qualifiers to
     * a service locator.  This method adds those classes.
     * <p>
     * If the class to add implements {@link Factory} then two descriptors
     * will be added, one for the {@link Factory} class itself, and one for
     * the {@link Factory#provide()} method of the factory.  In the output
     * list the descriptor for the {@link Factory} will be added first, followed
     * by the descriptor for the {@link Factory#provide()} method
     *
     * @param locator The non-null locator to add this descriptor to
     * @param toAdd The classes to add to the locator.  If a class in this list implements
     * {@link Factory} then two descriptors will be added for that class
     * @return The list of descriptors added to the system.  Will not return null but
     * may return an empty list
     * @throws MultiException On a commit failure
     */
    public static List<ActiveDescriptor<?>> addClasses(ServiceLocator locator, Class<?>... toAdd) {
        return addClasses(locator, false, toAdd);
    }

    /* package */ static String getBestContract(Descriptor d) {
        String impl = d.getImplementation();
        Set<String> contracts = d.getAdvertisedContracts();
        if (contracts.contains(impl)) return impl;

        for (String candidate : contracts) {
            return candidate;
        }

        return impl;
    }

    /**
     * Finds a descriptor in the given service locator.  If the descriptor has the serviceId and
     * locatorId set then it will first attempt to use those values to get the exact descriptor
     * described by the input descriptor.  Failing that (or if the input descriptor does not have
     * those values set) then it will use the equals algorithm of {@DescriptorImpl} to determine
     * the equality of the descriptor.
     *
     * @param locator The non-null locator in which to find the descriptor
     * @param descriptor The non-null descriptor to search for
     * @return The descriptor as found in the locator, or null if no such descriptor could be found
     */
    @SuppressWarnings("unchecked")
    public static <T> ActiveDescriptor<T> findOneDescriptor(ServiceLocator locator, Descriptor descriptor) {
        if (locator == null || descriptor == null) throw new IllegalArgumentException();

        if (descriptor.getServiceId() != null && descriptor.getLocatorId() != null) {
            ActiveDescriptor<T> retVal = (ActiveDescriptor<T>) locator.getBestDescriptor(
                    BuilderHelper.createSpecificDescriptorFilter(descriptor));

            if (retVal != null) return retVal;

            // Fall back to DescriptorImpl.equals
        }

        final DescriptorImpl di;
        if (descriptor instanceof DescriptorImpl) {
            di = (DescriptorImpl) descriptor;
        }
        else {
            di = new DescriptorImpl(descriptor);
        }

        final String contract = getBestContract(descriptor);
        final String name = descriptor.getName();

        ActiveDescriptor<T> retVal = (ActiveDescriptor<T>) locator.getBestDescriptor(new IndexedFilter() {

            @Override
            public boolean matches(Descriptor d) {
                return di.equals(d);
            }

            @Override
            public String getAdvertisedContract() {
                return contract;
            }

            @Override
            public String getName() {
                return name;
            }

        });

        return retVal;
    }

    /**
     * This method will attempt to remove descriptors matching the passed in descriptor from
     * the given locator.  If the descriptor has its locatorId and serviceId values set then
     * only a descriptor matching those exact locatorId and serviceId will be removed.  Otherwise
     * any descriptor that returns true from the {@link DescriptorImpl#equals(Object)} method
     * will be removed from the locator.  Note that if more than one descriptor matches they
     * will all be removed.  Hence more than one descriptor may be removed by this method.
     *
     * @param locator The non-null locator to remove the descriptor from
     * @param descriptor The non-null descriptor to remove from the locator
     */
    public static void removeOneDescriptor(ServiceLocator locator, Descriptor descriptor) {
        removeOneDescriptor(locator, descriptor, false);
    }

    /**
     * This method will attempt to remove descriptors matching the passed in descriptor from
     * the given locator.  If the descriptor has its locatorId and serviceId values set then
     * only a descriptor matching those exact locatorId and serviceId will be removed.  Otherwise
     * any descriptor that returns true from the {@link DescriptorImpl#equals(Object)} method
     * will be removed from the locator.  Note that if more than one descriptor matches they
     * will all be removed.  Hence more than one descriptor may be removed by this method.
     *
     * @param locator The non-null locator to remove the descriptor from
     * @param descriptor The non-null descriptor to remove from the locator
     * @param includeAliasDescriptors If set to true all {@link AliasDescriptor}s that point
     * to any descriptors found by filter will also be removed
     */
    public static void removeOneDescriptor(ServiceLocator locator, Descriptor descriptor, boolean includeAliasDescriptors) {
        if (locator == null || descriptor == null) throw new IllegalArgumentException();

        DynamicConfigurationService dcs = locator.getService(DynamicConfigurationService.class);
        DynamicConfiguration config = dcs.createDynamicConfiguration();

        if (descriptor.getLocatorId() != null && descriptor.getServiceId() != null) {
            Filter destructionFilter = BuilderHelper.createSpecificDescriptorFilter(descriptor);

            config.addUnbindFilter(destructionFilter);

            if (includeAliasDescriptors == true) {
                List<ActiveDescriptor<?>> goingToDie = locator.getDescriptors(destructionFilter);
                if (!goingToDie.isEmpty()) {
                    AliasFilter af = new AliasFilter(goingToDie);

                    config.addUnbindFilter(af);
                }
            }

            config.commit();

            return;
        }

        // Must use second algorithm, which is not as precise, but which still mainly works
        final DescriptorImpl di;
        if (descriptor instanceof DescriptorImpl) {
            di = (DescriptorImpl) descriptor;
        }
        else {
            di = new DescriptorImpl(descriptor);
        }

        Filter destructionFilter = new Filter() {

            @Override
            public boolean matches(Descriptor d) {
                return di.equals(d);
            }

        };

        config.addUnbindFilter(destructionFilter);

        if (includeAliasDescriptors == true) {
            List<ActiveDescriptor<?>> goingToDie = locator.getDescriptors(destructionFilter);
            if (!goingToDie.isEmpty()) {
                AliasFilter af = new AliasFilter(goingToDie);

                config.addUnbindFilter(af);
            }
        }

        config.commit();
    }

    /**
     * Removes all the descriptors from the given locator that match the
     * given filter
     *
     * @param locator The non-null locator to remove the descriptors from
     * @param filter The non-null filter which will determine what descriptors to remove
     */
    public static void removeFilter(ServiceLocator locator, Filter filter) {
        removeFilter(locator, filter, false);
    }

    /**
     * Removes all the descriptors from the given locator that match the
     * given filter
     *
     * @param locator The non-null locator to remove the descriptors from
     * @param filter The non-null filter which will determine what descriptors to remove
     * @param includeAliasDescriptors If set to true all {@link AliasDescriptor}s that point
     * to any descriptors found by filter will also be removed
     */
    public static void removeFilter(ServiceLocator locator, Filter filter, boolean includeAliasDescriptors) {
        if (locator == null || filter == null) throw new IllegalArgumentException();

        DynamicConfigurationService dcs = locator.getService(DynamicConfigurationService.class);
        DynamicConfiguration config = dcs.createDynamicConfiguration();

        config.addUnbindFilter(filter);

        if (includeAliasDescriptors == true) {
            List<ActiveDescriptor<?>> goingToDie = locator.getDescriptors(filter);
            if (!goingToDie.isEmpty()) {
                AliasFilter af = new AliasFilter(goingToDie);

                config.addUnbindFilter(af);
            }
        }

        config.commit();
    }

    /**
     * Returns the best service matching the passed in fully qualified
     * class name of the service
     *
     * @param locator The locator to find the service in
     * @param className The fully qualified class name of the service
     * @return The found service, or null if there is no service with this class name
     */
    @SuppressWarnings("unchecked")
    public static <T> T getService(ServiceLocator locator, String className) {
        if (locator == null || className == null) throw new IllegalArgumentException();

        ActiveDescriptor<T> ad = (ActiveDescriptor<T>) locator.getBestDescriptor(BuilderHelper.createContractFilter(className));
        if (ad == null) return null;

        return locator.getServiceHandle(ad).getService();
    }

    /**
     * Returns the service in this service locator given the current descriptor.
     *
     * @param locator The non-null locator in which to get the service associated with
     * this descriptor
     * @param descriptor The non-null descriptor to find the corresponding service for
     * @return The service object
     */
    @SuppressWarnings("unchecked")
    public static <T> T getService(ServiceLocator locator, Descriptor descriptor) {
        if (locator == null || descriptor == null) throw new IllegalArgumentException();

        Long locatorId = descriptor.getLocatorId();
        if (locatorId != null &&
                (locatorId.longValue() == locator.getLocatorId()) &&
                (descriptor instanceof ActiveDescriptor)) {
            return locator.getServiceHandle((ActiveDescriptor<T>) descriptor).getService();

        }

        ActiveDescriptor<T> found = findOneDescriptor(locator, descriptor);
        if (found == null) return null;

        return locator.getServiceHandle(found).getService();
    }

    /**
     * This method returns a {@link DynamicConfiguration} for use with adding
     * and removing services to the given {@link ServiceLocator}.
     *
     * @param locator A non-null locator to get a DynamicConfiguration for
     * @return A non-null DynamicConfiguration object that can be used to add
     * or remove services to the passed in locator
     * @throws IllegalStateException If there was an error retrieving the
     * {@link DynamicConfigurationService} for this locator
     */
    public static DynamicConfiguration createDynamicConfiguration(ServiceLocator locator)
        throws IllegalStateException {
        if (locator == null) throw new IllegalArgumentException();

        DynamicConfigurationService dcs = locator.getService(DynamicConfigurationService.class);
        if (dcs == null) throw new IllegalStateException();

        return dcs.createDynamicConfiguration();
    }

    /**
     * This method will first attempt to find a service corresponding to the type and qualifiers
     * passed in to the method, and if one is found simply returns it.  If no service is found
     * in the locator this method will call {@link ServiceLocator#createAndInitialize(Class)} on
     * the class (ignoring the qualifiers) in order to create an instance of the service.
     *
     * @param locator The service locator to search for the service with
     * @param type The non-null type of object to either find or create
     * @param qualifiers The set of qualifiers that should be associated with the service
     * @return An instance of type as either found in the locator or automatically created,
     * injected and post-constructed.  Note that the return value CAN be null IF the service
     * was found in the service locator and the context in which the service is in allows for
     * null services.
     * @throws MultiException On a failure from any of the underlying operations
     */
    public static <T> T findOrCreateService(ServiceLocator locator, Class<T> type, Annotation... qualifiers) throws MultiException {
        if (locator == null || type == null) throw new IllegalArgumentException();

        ServiceHandle<T> retVal = locator.getServiceHandle(type, qualifiers);
        if (retVal == null) {
            return locator.createAndInitialize(type);
        }

        return retVal.getService();
    }

    /**
     * Gets one value from a metadata field from the given descriptor
     *
     * @param d The non-null descriptor from which to get the first value in the given field
     * @param field The non-null field to get the first value of
     * @return The first value in the given field, or null if no such field exists in the descriptor
     */
    public static String getOneMetadataField(Descriptor d, String field) {
        Map<String, List<String>> metadata = d.getMetadata();
        List<String> values = metadata.get(field);
        if (values == null || values.isEmpty()) return null;

        return values.get(0);
    }

    /**
     * Gets one value from a metadata field from the given service handle
     *
     * @param h The non-null service handle from which to get the first value in the given field
     * @param field The non-null field to get the first value of
     * @return The first value in the given field, or null if no such field exists in the descriptor
     */
    public static String getOneMetadataField(ServiceHandle<?> h, String field) {
        return getOneMetadataField(h.getActiveDescriptor(), field);
    }

    /**
     * This method is often the first line of a stand-alone client that wishes to use HK2.
     * It creates a ServiceLocator with the given name (or a randomly generated name if
     * null is passed in) and then populates that service locator with services found in
     * the META-INF/hk2-locator/default files that can be found with the classloader that
     * loaded HK2 (usually the system classloader).
     *
     * @param name The name of the service locator to create.  If there is already a service
     * locator of this name this method will use that one to populate.  If this is null
     * a randomly assigned name will be given to the service locator, and it will not be
     * tracked by the system.  If this is NOT null then this service locator can be found
     * with {@link ServiceLocatorFactory#find(String)}.
     * @return A service locator that has been populated with services
     * @throws MultiException If there was a failure when populating or creating the ServiceLocator
     */
    public static ServiceLocator createAndPopulateServiceLocator(String name) throws MultiException {
        ServiceLocator retVal = ServiceLocatorFactory.getInstance().create(name);

        DynamicConfigurationService dcs = retVal.getService(DynamicConfigurationService.class);
        Populator populator = dcs.getPopulator();

        try {
            populator.populate();
        }
        catch (IOException e) {
            throw new MultiException(e);
        }

        return retVal;
    }

    /**
     * This method is often the first line of a stand-alone client that wishes to use HK2.
     * It creates a ServiceLocator with a randomly generated name and then populates that
     * service locator with services found in the META-INF/hk2-locator/default files that
     * can be found with the classloader that loaded HK2 (usually the system classloader).
     *
     * @return A service locator that has been populated with services
     * @throws MultiException If there was a failure when populating or creating the ServiceLocator
     */
    public static ServiceLocator createAndPopulateServiceLocator() {
        return createAndPopulateServiceLocator(null);
    }

    private static class AliasFilter implements Filter {
        private final Set<String> values = new HashSet<String>();

        private AliasFilter(List<ActiveDescriptor<?>> bases) {
            for (ActiveDescriptor<?> base : bases) {
                String val = base.getLocatorId() + "." + base.getServiceId();
                values.add(val);
            }
        }

        @Override
        public boolean matches(Descriptor d) {
            List<String> mAliasVals = d.getMetadata().get(AliasDescriptor.ALIAS_METADATA_MARKER);
            if (mAliasVals == null || mAliasVals.isEmpty()) return false;

            String aliasVal = mAliasVals.get(0);

            return values.contains(aliasVal);
        }

    }

    /**
     * This method will cause lookup operations to throw exceptions when
     * exceptions are encountered in underlying operations such as
     * classloading.  This method is idempotent.  This method works
     * by adding {@link RethrowErrorService} to the service registry
     * <p>
     * Do not use this methods in secure applications where callers to lookup
     * should not be given the information that they do NOT have access
     * to a service
     *
     * @param locator The service locator to enable lookup exceptions on.  May not be null
     */
    public static void enableLookupExceptions(ServiceLocator locator) {
        if (locator == null) throw new IllegalArgumentException();
        
        try {
            addClasses(locator, true, RethrowErrorService.class);
        }
        catch (MultiException me) {
            if (!isDupException(me)) throw me;
        }
    }

    /**
     * This method will enable the default topic distribution service.
     * <p>
     * The default distribution service distributes messages on the
     * same thread as the caller of {@link org.glassfish.hk2.api.messaging.Topic#publish(Object)}
     * and (TBD security policy).  Objects to be distributed to will be
     * held with SoftReferences, and hence if they go out of scope they
     * will not be distributed to.  Only services created AFTER the topic
     * distribution service is enabled will be distributed to.
     * <p>
     * This method is idempotent, so that if there is already a
     * TopicDistributionService with the default name is available this method
     * will do nothing
     *
     * @param locator The service locator to enable topic distribution on.  May not be null
     * @deprecated Use ExtrasUtilities.enableTopicDistribution.  This method WILL BE REMOVED
     * in the next version of hk2
     */
    public static void enableTopicDistribution(ServiceLocator locator) {
        throw new AssertionError("ServiceLocatorUtilities.enableTopicDistribution method has been removed, use ExtrasUtilities.enableTopicDistribution");
    }

    /**
     * Dumps all descriptors in this ServiceLocator to stderr
     *
     * @param locator The non-null locator to dump to stderr
     */
    public static void dumpAllDescriptors(ServiceLocator locator) {
        dumpAllDescriptors(locator, System.err);
    }

    /**
     * Dumps all descriptors in this ServiceLocator to the given PrintStream
     *
     * @param locator The non-null locator to dump
     * @param output The non-null PrintStream to print the descriptors to
     */
    public static void dumpAllDescriptors(ServiceLocator locator, PrintStream output) {
        if (locator == null || output == null) throw new IllegalArgumentException();

        List<ActiveDescriptor<?>> all = locator.getDescriptors(BuilderHelper.allFilter());

        for (ActiveDescriptor<?> d : all) {
            output.println(d.toString());
        }
    }

    /**
     * Returns a {@link Singleton} {@link Annotation} implementation
     *
     * @return a {@link Singleton} {@link Annotation} implementation
     */
    public static Singleton getSingletonAnnotation() { return SINGLETON; }

    /**
     * Returns a {@link PerLookup} {@link Annotation} implementation
     *
     * @return a {@link PerLookup} {@link Annotation} implementation
     */
    public static PerLookup getPerLookupAnnotation() { return PER_LOOKUP; }

    /**
     * Returns a {@link PerThread} {@link Annotation} implementation
     *
     * @return a {@link PerThread} {@link Annotation} implementation
     */
    public static PerThread getPerThreadAnnotation() {
        return PER_THREAD;
    }

    /**
     * Returns a {@link InheritableThread} {@link Annotation} implementation
     *
     * @return a {@link InheritableThread} {@link Annotation} implementation
     */
    public static InheritableThread getInheritableThreadAnnotation() {
        return INHERITABLE_THREAD;
    }

    /**
     * Returns a {@link Immediate} {@link Annotation} implementation
     *
     * @return a {@link Immediate} {@link Annotation} implementation
     */
    public static Immediate getImmediateAnnotation() { return IMMEDIATE; }
    
    private static boolean isDupException(MultiException me) {
        boolean atLeastOne = false;
        
        for (Throwable error : me.getErrors()) {
            atLeastOne = true;
            
            if (!(error instanceof DuplicateServiceException)) return false;
        }
        
        return atLeastOne;
    }

    private static class ImmediateImpl extends AnnotationLiteral<Immediate> implements Immediate {
        private static final long serialVersionUID = -4189466670823669605L;
    }

    private static class PerLookupImpl extends AnnotationLiteral<PerLookup> implements PerLookup {
        private static final long serialVersionUID = 6554011929159736762L;
    }

    private static class PerThreadImpl extends AnnotationLiteral<PerThread> implements PerThread {
        private static final long serialVersionUID = 521793185589873261L;
    }

    private static class InheritableThreadImpl extends AnnotationLiteral<InheritableThread> implements InheritableThread {
        private static final long serialVersionUID = -3955786566272090916L;
    }

    private static class SingletonImpl extends AnnotationLiteral<Singleton> implements Singleton {
        private static final long serialVersionUID = -2425625604832777314L;
    }
}
