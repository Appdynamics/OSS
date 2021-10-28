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

package org.jvnet.hk2.spring.bridge.internal;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.jvnet.hk2.spring.bridge.api.SpringIntoHK2Bridge;
import org.springframework.beans.factory.BeanFactory;

/**
 * @author jwells
 *
 */
@Singleton
public class SpringIntoHK2BridgeImpl implements SpringIntoHK2Bridge {
    @Inject
    private ServiceLocator serviceLocator;

    /* (non-Javadoc)
     * @see org.jvnet.hk2.spring.bridge.api.SpringIntoHK2Bridge#bridgeSpringBeanFactory(org.springframework.beans.factory.BeanFactory)
     */
    @Override
    public void bridgeSpringBeanFactory(BeanFactory beanFactory) {
        SpringToHK2JITResolver resolver = new SpringToHK2JITResolver(serviceLocator, beanFactory);
        
        ServiceLocatorUtilities.addOneConstant(serviceLocator, resolver);
    }

}
