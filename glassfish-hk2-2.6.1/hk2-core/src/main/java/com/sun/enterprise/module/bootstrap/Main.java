/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.enterprise.module.bootstrap;

import java.io.File;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.List;

import org.glassfish.hk2.api.Descriptor;
import org.glassfish.hk2.api.DescriptorFileFinder;
import org.glassfish.hk2.api.DynamicConfiguration;
import org.glassfish.hk2.api.DynamicConfigurationService;
import org.glassfish.hk2.api.IndexedFilter;
import org.glassfish.hk2.api.MultiException;
import org.glassfish.hk2.api.PopulatorPostProcessor;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.BuilderHelper;
import org.glassfish.hk2.utilities.Binder;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;

import com.sun.enterprise.module.ModulesRegistry;
import com.sun.enterprise.module.common_impl.AbstractFactory;
import com.sun.enterprise.module.impl.HK2Factory;

/**
 * CLI entry point that will setup the module subsystem and delegate the main
 * execution to the first archive in its import list...
 * 
 * TODO: reusability of this class needs to be improved.
 * 
 * @author dochez
 */
public class Main {

	private ClassLoader parentClassLoader;

	public static final String DEFAULT_NAME = "default";

	public Main() {
	}

	public static void main(final String[] args) {
        HK2Factory.initialize();
        (new Main()).run(args);
	}

	public void run(final String[] args) {
		try {
			final Main main = this;
			Thread thread = new Thread() {
				public void run() {
					try {
						main.start(args);
					} catch (BootException e) {
						e.printStackTrace();
					} catch (RuntimeException e) {
						e.printStackTrace();
					}
				}
			};
			thread.start();
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	/**
	 * We need to determine which jar file has been used to load this class
	 * Using the getResourceURL we can get this information, after that, it is
	 * just a bit of detective work to get the file path for the jar file.
	 * 
	 * @return the path to the jar file containing this class. always returns
	 *         non-null.
	 * 
	 * @throws BootException
	 *             If failed to determine the bootstrap file name.
	 */
	protected File getBootstrapFile() throws BootException {
		try {
			return Which.jarFile(getClass());
		} catch (IOException e) {
			throw new BootException("Failed to get bootstrap path", e);
		}
	}

	/**
	 * Start the server from the command line
	 * 
	 * @param args
	 *            the command line arguments
	 */
	public void start(String[] args) throws BootException {
		
		File bootstrap = this.getBootstrapFile();
		File root = bootstrap.getAbsoluteFile().getParentFile();

		// root is the directory in which this bootstrap.jar is located
		// For most cases, this is the lib directory although this is completely
		// dependent on the usage of this facility.
		if (root == null) {
			throw new BootException("Cannot find root installation from "
					+ bootstrap);
		}

        // get the ModuleStartup implementation.
        ModulesRegistry mr = AbstractFactory.getInstance().createModulesRegistry();
		StartupContext context = new StartupContext(
				ArgumentManager.argsToMap(args));
		launch(mr, context.getPlatformMainServiceName(), context);
	}

    protected void defineParentClassLoader() throws BootException {
		parentClassLoader = AccessController
				.doPrivileged(new PrivilegedAction<ClassLoader>() {
					@Override
					public ClassLoader run() {
						return Main.this.getClass().getClassLoader();
					}
				});
	}

	protected ClassLoader getParentClassLoader() {
		return parentClassLoader;
	}

	/**
	 * Launches the module system and hand over the execution to the
	 * {@link ModuleStartup} implementation of the main module.
	 * 
	 * @param mainModuleName
	 *            The module that will provide {@link ModuleStartup}. If null,
	 *            one will be auto-discovered.
	 * @param context
	 *            startup context instance
	 * @return The ModuleStartup service
	 */
	public ModuleStartup launch(ModulesRegistry registry, String mainModuleName, StartupContext context)
			throws BootException {
		// now go figure out the start up service
        ServiceLocator serviceLocator = registry.createServiceLocator(DEFAULT_NAME);
        ModuleStartup startupCode = findStartupService(registry, serviceLocator, mainModuleName, context);
		launch(startupCode, context);
		return startupCode;
	}

	/**
	 * Return the ModuleStartup service configured to be used to start the
	 * system.
	 * 
	 * @param registry
	 * @param serviceLocator
	 * @param mainModuleName
	 * @param context
	 * @return
	 * @throws BootException
	 */
    @SuppressWarnings("unchecked")
    public ModuleStartup findStartupService(ModulesRegistry registry, ServiceLocator serviceLocator, String mainModuleName,
			StartupContext context) throws BootException {
        // THIS CODE HAS TO BE REVISITED. WHY IS IT SO DIFFERENT FROM EARLIER VERSION?

        // ACTUALLY THIS NEW CODE IS INCORRECT. IT INTERPRETS mainModuleName AS SERVICE NAME AS OPPOSED TO THE
        // THE NAME OF THE MODULE. THE SEMANTICS OF THE CALL ACTUALLY IS THAT USER CAN DIRECT US TO LOAD THE
        // ModuleStartup SERVICE FROM A PARTICULAR MODULE IF THEY WANT. THAT WAY THEY DON'T HAVE TO WORRY ABOUT
        // UNIQUENESS OF NAMES OF ModuleStartup
//    	if (mainModuleName == null && context.getPlatformMainServiceName() != null) {
//    		mainModuleName = context.getPlatformMainServiceName();
//    	}

        if (mainModuleName != null) {
            throw new UnsupportedOperationException("FIX ME");
        } else {
            try {
                final String mainServiceName = context.getPlatformMainServiceName();
                
                ModuleStartup retVal = serviceLocator.getService(ModuleStartup.class, mainServiceName);
                
                if (retVal == null) {
                    throw new BootException(String.format("Cannot find %s ModuleStartup", mainServiceName));
                }
                return retVal;
            } catch (MultiException e) {
                throw new BootException("Unable to load service", e);
            }
        }
	}

	public ServiceLocator createServiceLocator(ModulesRegistry mr,
                                               StartupContext context,
                                               List<PopulatorPostProcessor> postProcessors,
                                               DescriptorFileFinder descriptorFileFinder)
			throws BootException {
        // Why is this method not just delagting to ModulesRegistry.createServiceLocator(...)?
        // That will require changes to ModulesRegistry.createServiceLocator() to accept
        // postProcessors and desriptorFileFinder, but that's OK IMO.

		// set the parent class loader before we start loading modules
		defineParentClassLoader();

        ServiceLocator serviceLocator = mr.newServiceLocator();
		
		addDescriptorFileFinder(serviceLocator, descriptorFileFinder);
		
		DynamicConfigurationService dcs = serviceLocator
				.getService(DynamicConfigurationService.class);
		
		DynamicConfiguration config = dcs.createDynamicConfiguration();
		
		config.addActiveDescriptor(BuilderHelper
				.createConstantDescriptor(context));

		config.commit();

		final ClassLoader oldCL = AccessController
				.doPrivileged(new PrivilegedAction<ClassLoader>() {
					@Override
					public ClassLoader run() {
						ClassLoader cl = Thread.currentThread()
								.getContextClassLoader();
						Thread.currentThread().setContextClassLoader(
								getClass().getClassLoader());
						return cl;
					}
				});

		try {
            mr.populateServiceLocator(DEFAULT_NAME, serviceLocator, postProcessors);
            mr.populateConfig(serviceLocator);
		} finally {
			AccessController.doPrivileged(new PrivilegedAction<Object>() {
				@Override
				public Object run() {
					Thread.currentThread().setContextClassLoader(oldCL);
					return null;
				}
			});
		}
		return serviceLocator;
	}

    protected void launch(ModuleStartup startupCode, StartupContext context)
			throws BootException {
		startupCode.setStartupContext(context);
		startupCode.start();
	}

	public ServiceLocator getServiceLocator() {
		throw new RuntimeException("DON'T CALL THIS METHOD");
	}

	private void addDescriptorFileFinder(
            ServiceLocator serviceLocator, DescriptorFileFinder descriptorFileFinder) {
		
		if (descriptorFileFinder != null) {
			DynamicConfigurationService dcs = serviceLocator
					.getService(DynamicConfigurationService.class);
			DynamicConfiguration config = dcs.createDynamicConfiguration();

			config.addActiveDescriptor(BuilderHelper
					.createConstantDescriptor(descriptorFileFinder));

			config.commit();
		}
	}

	/**
	 * This filter matches against the name, including only
	 * matching a ModuleStartup with no name if name is
	 * null (unlike a normal "null" returned from name, which
	 * acts as a wildcard for the name)
	 * 
	 * @author jwells
	 *
	 */
	private static class MainFilter implements IndexedFilter {
	    private final String name;
	    
	    /**
	     * The name given here will match the ModuleStartup
	     * 
	     * @param name The name of the ModuleStartup to find.  If
	     * null this Filter will only match ModuleStartups with
	     * no name
	     */
	    private MainFilter(String name) {
	        this.name = name;
	    }

        /* (non-Javadoc)
         * @see org.glassfish.hk2.api.Filter#matches(org.glassfish.hk2.api.Descriptor)
         */
        @Override
        public boolean matches(Descriptor d) {
            if (name == null) {
                if (d.getName() == null) return true;
                
                return false;
            }
            
            return true;
        }

        /* (non-Javadoc)
         * @see org.glassfish.hk2.api.IndexedFilter#getAdvertisedContract()
         */
        @Override
        public String getAdvertisedContract() {
            return ModuleStartup.class.getName();
        }

        /* (non-Javadoc)
         * @see org.glassfish.hk2.api.IndexedFilter#getName()
         */
        @Override
        public String getName() {
            return name;
        }
	    
	}

}
