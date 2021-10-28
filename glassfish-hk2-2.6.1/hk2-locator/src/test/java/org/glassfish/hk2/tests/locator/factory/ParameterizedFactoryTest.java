/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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

package org.glassfish.hk2.tests.locator.factory;

import junit.framework.Assert;
import org.glassfish.hk2.api.Descriptor;
import org.glassfish.hk2.api.DynamicConfiguration;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.api.FactoryDescriptors;
import org.glassfish.hk2.api.MultiException;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.tests.locator.utilities.LocatorHelper;
import org.glassfish.hk2.tests.locator.utilities.TestModule;
import org.glassfish.hk2.utilities.AbstractActiveDescriptor;
import org.glassfish.hk2.utilities.ActiveDescriptorBuilder;
import org.glassfish.hk2.utilities.BuilderHelper;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.hk2.utilities.reflection.ParameterizedTypeImpl;
import org.junit.Test;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Test for complex parameterized type factories.
 */
public class ParameterizedFactoryTest {

    private static final List<String>  STRING_LIST  = new LinkedList<String> ();
    private static final List<Integer> INTEGER_LIST = new LinkedList<Integer> ();

    @Test
    public void testParameterizedFactory() {
        ServiceLocator locator = LocatorHelper.create("testParameterizedFactory", new TestModule(){
            @Override
            public void configure(DynamicConfiguration config) {
                config.bind(BuilderHelper.link(FooFactory.class).to(Foo.class).buildFactory());
                config.bind(BuilderHelper.link(BarFactory.class).to(Bar.class).buildFactory());
                config.bind(BuilderHelper.link(Injectee.class).in(Singleton.class).build());
            }
        });

        FooFactory fooFactory
                = locator.getService((new TypeLiteral<Factory<Foo>>() {}).getType());
        Assert.assertNotNull(fooFactory);

        Foo foo = fooFactory.provide();
        Assert.assertNotNull(foo);

        foo = locator.getService(Foo.class);
        Assert.assertNotNull(foo);

        // -----

        BarFactory barfactory
                = locator.getService((new TypeLiteral<Factory<Bar>>() {}).getType());
        Assert.assertNotNull(barfactory);

        Bar bar = barfactory.provide();
        Assert.assertNotNull(bar);

        bar = locator.getService(Bar.class);
        Assert.assertNotNull(bar);

        // -----

        Injectee injectee = locator.getService(Injectee.class);
        Assert.assertNotNull(injectee);
        Assert.assertTrue(injectee.getInjectedFoo() instanceof FooImpl);
        Assert.assertTrue(injectee.getInjectedBar() instanceof BarImpl);
    }

    @Test
    public void testGenericFactories() {
        ServiceLocator locator = LocatorHelper.create("testGenericFactories", new TestModule(){

            @Override
            public void configure(DynamicConfiguration config) {
                config.bind(createConstantFactoryDescriptor(getFactory(STRING_LIST), (new TypeLiteral<List<String>>() {}).getType()));
                config.bind(createConstantFactoryDescriptor(getFactory(INTEGER_LIST), (new TypeLiteral<List<Integer>>() {}).getType()));
                config.bind(BuilderHelper.link(Injectee2.class).in(Singleton.class).build());
            }
        });

        Factory<List<String>> stringListFactory
                = locator.getService((new TypeLiteral<Factory<List<String>>>() {}).getType());

        Assert.assertNotNull(stringListFactory);

        List<String> stringList = stringListFactory.provide();

        Assert.assertSame(STRING_LIST, stringList);

        // -----

        Factory<List<Integer>> integerListFactory
                = locator.getService((new TypeLiteral<Factory<List<Integer>>>() {}).getType());

        Assert.assertNotNull(integerListFactory);

        List<Integer> integerList = integerListFactory.provide();

        Assert.assertSame(INTEGER_LIST, integerList);

        // -----

        Injectee2 injectee = locator.getService(Injectee2.class);
        Assert.assertNotNull(injectee);

        stringList = injectee.getInjectedStringList();

        Assert.assertSame(STRING_LIST, stringList);

        stringList = injectee.getStringListFromProvider();

        Assert.assertSame(STRING_LIST, stringList);

        integerList = injectee.getInjectedIntegerList();

        Assert.assertSame(INTEGER_LIST, integerList);  // fails here ...  integerList == STRING_LIST

        integerList = injectee.getIntegerListFromProvider();

        Assert.assertSame(INTEGER_LIST, integerList);
    }

    /**
     * Tests that the proxy creation process is able to resolve the concrete
     * type from a register contract (must be only one) if the type is not
     * resolved from factory generic.
     */
    @Test
    public void testGenericFactoriesUsingProxy() {
        ServiceLocator locator = LocatorHelper.create();
        AbstractBinder hk2binder = new AbstractBinder() {
            @Override
            protected void configure() {
                bindFactory(getFactory(new FooImpl()))
                        .to(FooImpl.class)
                        .proxy(true)
                        .in(ProxiableSingleton.class);

                bindFactory(getFactory(Collections.singletonList(new FooImpl())))
                        .to(new TypeLiteral<ArrayList<FooImpl>>() {})
                        .proxy(true)
                        .in(ProxiableSingleton.class);

                bindAsContract(InjecteeClassProxy.class);
            }
        };
        ServiceLocatorUtilities.bind(locator, hk2binder);

        InjecteeClassProxy service = locator.getService(InjecteeClassProxy.class);
        Assert.assertNotNull(service.getFoo());
        Assert.assertNotNull(service.getFooFactory());
        Assert.assertNotNull(service.getFooList());
        Assert.assertNotNull(service.getFooListFactory());
    }

    /**
     * Tests must fail because type resolver is not able to resolver the concrete
     * type from generic factory and there are registered more than one contract.
     */
    @Test(expected = MultiException.class)
    public void testFailGenericFactoriesUsingProxyWithMultipleContracts() {
        ServiceLocator locator = LocatorHelper.create();
        AbstractBinder hk2binder = new AbstractBinder() {
            @Override
            protected void configure() {
                bindFactory(getFactory(new FooImpl()))
                        .to(FooImpl.class)
                        .to(String.class)
                        .proxy(true)
                        .in(ProxiableSingleton.class);

                bindAsContract(InjecteeClassProxy.class);
            }
        };
        ServiceLocatorUtilities.bind(locator, hk2binder);
        locator.getService(InjecteeClassProxy.class);
    }

    // ----- inner test classes ---------------------------------------------

    public static interface Foo {
    }

    public static class FooImpl implements Foo {
    }

    public static interface Bar {
    }

    public static class BarImpl implements Bar {
    }

    public static abstract class AbstractFactory<T> extends EmptyClass1 implements Factory<T> {
        @Override
        public void dispose(T instance) {
        }
    }

    public static abstract class AbstractFactory2<T> extends AbstractFactory<T> {
        @Override
        public T provide() {
            return get();
        }

        public abstract T get();
    }

    public static class FooFactory extends AbstractFactory<Foo> implements EmptyInterface3<Foo, String, Boolean>{
        @Override
        public Foo provide() {
            return new FooImpl();
        }
    }

    public static class BarFactory extends AbstractFactory2<Bar> {
        @Override
        public Bar get() {
            return new BarImpl();
        }
    }

    public static class BasicFactory<T> implements Factory<T> {
        private T value;

        protected BasicFactory(T value) {
            this.value = value;
        }

        @Override
        public T provide() {
            return value;
        }

        @Override
        public void dispose(T instance) {
            //do nothing
        }
    }

    public static interface EmptyInterface1<T> {
    }

    public static interface EmptyInterface2<T, U> {
    }

    public static interface EmptyInterface3<T, U, V> {
    }

    public static class EmptyClass1 extends EmptyClass2<Integer> {
    }

    public static class EmptyClass2<T> implements EmptyInterface2<String, T> {
    }

    @Singleton
    public static class InjecteeClassProxy {
        @Inject
        FooImpl foo;

        @Inject
        Factory<FooImpl> fooFactory;

        @Inject
        ArrayList<FooImpl> fooList;

        @Inject
        Factory<ArrayList<FooImpl>> fooListFactory;

        public FooImpl getFoo() {
            return foo;
        }

        public Factory<FooImpl> getFooFactory() {
            return fooFactory;
        }

        public ArrayList<FooImpl> getFooList() {
            return fooList;
        }

        public Factory<ArrayList<FooImpl>> getFooListFactory() {
            return fooListFactory;
        }
    }

    @Singleton
    public static class Injectee {
        @Inject
        Foo injectedFoo;

        @Inject
        Bar injectedBar;

        public Foo getInjectedFoo() {
            return injectedFoo;
        }

        public Bar getInjectedBar() {
            return injectedBar;
        }
    }

    @Singleton
    public static class Injectee2 {
        @Inject
        List<String> stringList;

        @Inject
        Provider<List<String>> stringListProvider;

        @Inject
        List<Integer> integerList;

        @Inject
        Provider<List<Integer>> integerListProvider;

        public List<String> getInjectedStringList() {
            return stringList;
        }

        public List<String> getStringListFromProvider() {
            return stringListProvider.get();
        }

        public List<Integer> getInjectedIntegerList() {
            return integerList;
        }

        public List<Integer> getIntegerListFromProvider() {
            return integerListProvider.get();
        }
    }

    // ----- static helper methods ------------------------------------------

    public static <T> Factory<T> getFactory(T initialValue) {
        return new BasicFactory<T>(initialValue);
    }

    public static <T> FactoryDescriptors createConstantFactoryDescriptor(
            Factory<T> factory,
            Type... types) {

        final AbstractActiveDescriptor<?> serviceDescriptor = BuilderHelper.createConstantDescriptor(factory);
        final ActiveDescriptorBuilder  factoryBuilder    = BuilderHelper.activeLink(factory.getClass());

        serviceDescriptor.addContractType(factory.getClass());

        for (Type type : types) {
            factoryBuilder.to(type);
            serviceDescriptor.addContractType(new ParameterizedTypeImpl(Factory.class, type));
        }

        final Descriptor factoryDescriptor = factoryBuilder.buildFactory();

        FactoryDescriptors f = new FactoryDescriptors(){
            @Override
            public Descriptor getFactoryAsAService() {
                return serviceDescriptor;
            }

            @Override
            public Descriptor getFactoryAsAFactory() {
                return factoryDescriptor;
            }

            public String toString() {
                return "FactoryDescriptorsImpl(\n" +
                        serviceDescriptor + ",\n" +
                        factoryDescriptor + ",\n\t" +
                        System.identityHashCode(this) + ")";
            }
        };

        return f;
    }
}
