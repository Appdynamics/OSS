/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2015 Oracle and/or its affiliates. All rights reserved.
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

package org.jvnet.hk2.guice.bridge.api;

import org.glassfish.hk2.api.ServiceLocator;
import org.jvnet.hk2.guice.bridge.internal.HK2ToGuiceTypeListenerImpl;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

/**
 * This is an implementation of com.google.inject.Module that should
 * be used if one wishes to inject HK2 services into Guice services
 * 
 * @author jwells
 *
 */
public class HK2IntoGuiceBridge extends AbstractModule {
    private final ServiceLocator locator;
    
    /**
     * Creates the {@link HK2IntoGuiceBridge} TypeLocator that must
     * be bound into the Module with a call to bindListener.  The
     * ServiceLocator will be consulted at this time for any types
     * Guice cannot find.  If this type is found in the ServiceLocator
     * then that service will be instantiated by hk2
     * 
     * @param locator The non-null locator that should be used to discover
     * services
     */
    public HK2IntoGuiceBridge(ServiceLocator locator) {
        this.locator = locator;
    }

    @Override
    protected void configure() {
        bindListener(Matchers.any(), new HK2ToGuiceTypeListenerImpl(locator));
    }
}
