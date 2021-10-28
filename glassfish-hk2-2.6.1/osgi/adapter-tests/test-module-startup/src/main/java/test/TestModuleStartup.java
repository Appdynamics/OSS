/*
 * Copyright (c) 2015, 2018 Oracle and/or its affiliates. All rights reserved.
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

package test;

import org.jvnet.hk2.annotations.Service;

import com.sun.enterprise.module.bootstrap.ModuleStartup;
import com.sun.enterprise.module.bootstrap.StartupContext;

@Service
public class TestModuleStartup implements ModuleStartup {

	public static boolean wasCalled;
	
	@Override
	public void setStartupContext(StartupContext context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start() {
		setWasCalled();
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

        private synchronized static void setWasCalled() {
          wasCalled=true; 
        }
}
