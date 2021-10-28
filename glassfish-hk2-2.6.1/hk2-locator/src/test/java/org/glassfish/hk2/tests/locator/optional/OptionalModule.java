/*
 * Copyright (c) 2012, 2018 Oracle and/or its affiliates. All rights reserved.
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

package org.glassfish.hk2.tests.locator.optional;

import java.util.Optional;
import javax.inject.Singleton;

import org.glassfish.hk2.api.DynamicConfiguration;
import org.glassfish.hk2.tests.locator.utilities.TestModule;
import org.glassfish.hk2.utilities.BuilderHelper;

/**
 * @author jwells
 *
 */
public class OptionalModule implements TestModule {

    /* (non-Javadoc)
     * @see org.glassfish.hk2.api.Module#configure(org.glassfish.hk2.api.Configuration)
     */
    @Override
    public void configure(DynamicConfiguration configurator) {
        // This guy explicitly does NOT define the OptionalService,
        // to ensure that optional injection points work

        configurator.bind(BuilderHelper.link(SimpleService.class).build());
        configurator.bind(BuilderHelper.link(InjectedManyTimes.class).in(Singleton.class).build());
        configurator.bind(BuilderHelper.link(NullWidgetFactory.class).to(Widget.class).buildFactory());
        
        Optional<String> providedOptional = Optional.of("testvalue");
        configurator.bind(BuilderHelper.createConstantDescriptor(providedOptional, null, Optional.class));
        
    }

}
