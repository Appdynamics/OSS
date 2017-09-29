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

package org.jvnet.hk2.internal;

import java.io.Serializable;
import java.util.Comparator;

import org.glassfish.hk2.api.Descriptor;

/**
 * @author jwells
 *
 */
public class DescriptorComparator implements Comparator<Descriptor>, Serializable {

    /**
     * Added for serialization
     */
    private static final long serialVersionUID = 4454509124508404602L;

    /* (non-Javadoc)
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(Descriptor o1, Descriptor o2) {
        int o1Ranking = o1.getRanking();
        int o2Ranking = o2.getRanking();
        
        if (o1Ranking < o2Ranking) return 1;
        if (o1Ranking > o2Ranking) return -1;
        
        long o1LocatorId = o1.getLocatorId().longValue();
        long o2LocatorId = o2.getLocatorId().longValue();
        
        if (o1LocatorId < o2LocatorId) return 1;
        if (o1LocatorId > o2LocatorId) return -1;
        
        long o1ServiceId = o1.getServiceId().longValue();
        long o2ServiceId = o2.getServiceId().longValue();
        
        if (o1ServiceId > o2ServiceId) return 1;
        if (o1ServiceId < o2ServiceId) return -1;
        
        return 0;
    }

}
