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

package org.glassfish.hk2.utilities;

import java.util.HashSet;
import java.util.Set;

import org.glassfish.hk2.api.Descriptor;
import org.glassfish.hk2.api.IndexedFilter;
import org.glassfish.hk2.api.PerLookup;
import org.glassfish.hk2.api.PopulatorPostProcessor;
import org.glassfish.hk2.api.ServiceLocator;

/**
 * This post-processor removes duplicate descriptors from the
 * set of descriptors being added to the service registry.
 * <p>
 * It is often the case when using a classpath that the same jar
 * file can appear on the path more than once.  For example this
 * is often done when patching.  However, if this jar contains
 * HK2 descriptor files in it, that can mean duplicate services
 * that are not intended to be duplicated.  This service removes
 * all duplicate descriptors from the set to be added to HK2
 * 
 * @author jwells
 *
 */
@PerLookup
public class DuplicatePostProcessor implements PopulatorPostProcessor {

    private final HashSet<DescriptorImpl> dupSet = new HashSet<DescriptorImpl>();

    /* (non-Javadoc)
     * @see org.glassfish.hk2.bootstrap.PopulatorPostProcessor#process(org.glassfish.hk2.utilities.DescriptorImpl)
     */
    @Override
    public DescriptorImpl process(ServiceLocator serviceLocator, DescriptorImpl descriptorImpl) {
        if (dupSet.contains(descriptorImpl)) {
            return null;
        }
        dupSet.add(descriptorImpl);
        
        Set<String> contracts = descriptorImpl.getAdvertisedContracts();
        String contract = null;
        for (String candidate : contracts) {
            if (candidate.equals(descriptorImpl.getImplementation())) {
                // Prefer this one over anything else
                contract = candidate;
                break;
            }
            
            contract = candidate;
        }
        
        final String fContract = contract;
        final String fName = descriptorImpl.getName();
        final DescriptorImpl fDescriptorImpl = descriptorImpl;
        
        if (serviceLocator.getBestDescriptor(new IndexedFilter() {

            @Override
            public boolean matches(Descriptor d) {
                return fDescriptorImpl.equals(d);
            }

            @Override
            public String getAdvertisedContract() {
                return fContract;
            }

            @Override
            public String getName() {
                return fName;
            }
            
        }) != null) {
            // Already in the locator, do not add again
            return null;
        }
        
        return descriptorImpl;
    }


}
