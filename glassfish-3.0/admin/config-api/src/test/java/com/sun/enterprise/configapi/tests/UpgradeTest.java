/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2009 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 *
 * Contributor(s):
 *
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
 *
 */

package com.sun.enterprise.configapi.tests;

import java.util.logging.Logger;
import java.util.List;
import java.util.ArrayList;

import com.sun.enterprise.config.serverbeans.Application;
import com.sun.enterprise.config.serverbeans.Applications;
import com.sun.enterprise.config.serverbeans.Domain;
import com.sun.enterprise.config.serverbeans.J2eeApplication;
import com.sun.enterprise.config.serverbeans.Module;
import com.sun.enterprise.config.serverbeans.Config;
import com.sun.grizzly.config.dom.ThreadPool;
import org.glassfish.api.admin.config.ConfigurationUpgrade;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 * Simple test for the domain.xml upgrade scenario
 *
 * @author Jerome Dochez
 */
public class UpgradeTest extends ConfigApiTest {

    @Before
    public void setup() {
        Domain domain = getHabitat().getComponent(Domain.class);
        assertTrue(domain!=null);
        
        // perform upgrade
        for (ConfigurationUpgrade upgrade : getHabitat().getAllByContract(ConfigurationUpgrade.class)) {
            Logger.getAnonymousLogger().info("running upgrade " + upgrade.getClass());    
        }
    }

    @Test
    public void threadPools() {
        List<String> names = new ArrayList<String>();
        for (ThreadPool pool : getHabitat().getComponent(Config.class).getThreadPools().getThreadPool()) {
            names.add(pool.getName());
        }
        assertTrue(names.contains("http-thread-pool") && names.contains("thread-pool-1"));
    }

    private void verify(String name) {
        assertTrue("Should find thread pool named " + name, getHabitat().getComponent(ThreadPool.class, name) != null);
    }
    @Test
    public void applicationUpgrade() {
        Applications apps = getHabitat().getComponent(Applications.class);
        assertTrue(apps!=null);
        for (Application app : apps.getApplications()) {
            assertTrue(app.getEngine().isEmpty());
            assertTrue(app.getModule().size()==1);
            for (Module module : app.getModule()) {
                assertTrue(module.getName().equals(app.getName()));
                assertTrue(!module.getEngines().isEmpty());
            }
        }
    }

    @Test
    public void j2eeApplicationUpgrade() {
        J2eeApplication application = getHabitat().getComponent(J2eeApplication.class);
        assertTrue("/foo/bar".equals(application.getLocation()));
    }
}
