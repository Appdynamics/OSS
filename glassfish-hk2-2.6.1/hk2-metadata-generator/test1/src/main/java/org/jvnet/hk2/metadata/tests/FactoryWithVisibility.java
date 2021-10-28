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

package org.jvnet.hk2.metadata.tests;

import org.glassfish.hk2.api.DescriptorVisibility;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.api.PerLookup;
import org.glassfish.hk2.api.Visibility;
import org.jvnet.hk2.annotations.Service;

/**
 * @author jwells
 *
 */
@Service
@PerLookup
@Visibility(DescriptorVisibility.LOCAL)
public class FactoryWithVisibility implements Factory<String> {

    /* (non-Javadoc)
     * @see org.glassfish.hk2.api.Factory#provide()
     */
    @Override
    @Visibility(DescriptorVisibility.LOCAL)
    @PerLookup
    public String provide() {
        return "";
    }

    /* (non-Javadoc)
     * @see org.glassfish.hk2.api.Factory#dispose(java.lang.Object)
     */
    @Override
    public void dispose(String instance) {

    }

}
