/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * 
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2009 Sun Microsystems, Inc. All rights reserved.
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
 */

package org.glassfish.appclient.client.acc;

import com.sun.enterprise.module.ModulesRegistry;
import com.sun.enterprise.module.bootstrap.StartupContext;
import com.sun.enterprise.module.single.StaticModulesRegistry;
import com.sun.enterprise.naming.impl.ClientNamingConfiguratorImpl;
import com.sun.hk2.component.ExistingSingletonInhabitant;
import java.net.URISyntaxException;
import java.util.Collection;
import org.glassfish.api.admin.ProcessEnvironment;
import org.glassfish.api.naming.ClientNamingConfigurator;
import org.glassfish.internal.api.Globals;
import org.jvnet.hk2.component.Habitat;
import org.jvnet.hk2.component.Inhabitant;

/**
 * Encapsulates details of preparing the HK2 habitat while also providing
 * a "main" HK2 module the HK2 bootstrap logic can start.
 * <p>
 * The HK2 habitat must be initialized before any AppClientContainer can be
 * created, because each ACC is an HK2 service so it can use injection.  The
 * AppClientContainerBuilder uses the habitat directly (without injection)
 * to create new ACCs. Part of initializing the habitat involves (at least
 * currently) finding and starting a "main HK2 module."  This class serves
 * that purpose, even though this class is not the main program.  To support
 * embedded ACCs we do not assume we provide the actual main program, but we
 * seem to need to offer a main module to HK2.  So this class implements
 * ModuleStartup even though it does little.
 *
 * @author tjquinn
 */
public class ACCModulesManager /*implements ModuleStartup*/ {

    private static Habitat habitat = null;

    public synchronized static void initialize(final ClassLoader loader) throws URISyntaxException {
        /*
         * The habitat might have been initialized earlier.  Currently
         * we use a single habitat for the JVM.  
         */
        if (habitat == null) {
            habitat = prepareHabitat(
                    loader);

            /*
             * Set up the default habitat in Globals as soon as we know
             * which habitat we'll use.
             */
            Globals.setDefaultHabitat(habitat);

            /*
             * Remove any already-loaded startup context so we can replace it
             * with the ACC one.
             */
            habitat.removeAllByType(StartupContext.class);
            
            StartupContext startupContext = new ACCStartupContext();
            habitat.add(new ExistingSingletonInhabitant(StartupContext.class, startupContext));
            /*
             * Following the example from AppServerStartup, remove any
             * pre-loaded lazy inhabitant for ProcessEnvironment that exists
             * from HK2's scan for services.  Then add in
             * an ACC ProcessEnvironment.
             */
            Inhabitant<ProcessEnvironment> inh =
                    habitat.getInhabitantByType(ProcessEnvironment.class);
            if (inh!=null) {
                habitat.remove(inh);
            }
            habitat.add(new ExistingSingletonInhabitant<ProcessEnvironment>
                    (new ProcessEnvironment(ProcessEnvironment.ProcessType.ACC)));

            ProcessEnvironment env = habitat.getComponent(ProcessEnvironment.class);

            /*
             * Create the ClientNamingConfigurator used by naming.
             */
            ClientNamingConfigurator cnc = new ClientNamingConfiguratorImpl();
            habitat.add(new ExistingSingletonInhabitant<ClientNamingConfigurator>(
                    ClientNamingConfigurator.class, cnc));
       }
    }


    static Habitat getHabitat() {
        return habitat;
    }

    public static <T> T getComponent(Class<T> c) {
        return habitat.getComponent(c);
    }

    /**
     * Sets up the HK2 habitat.
     * <p>
     * Must be invoked at least once before an AppClientContainerBuilder
     * returns a new AppClientContainer to the caller.
     * @param classLoader
     * @param logger
     * @throws com.sun.enterprise.module.bootstrap.BootException
     * @throws java.net.URISyntaxException
     */
    private static Habitat prepareHabitat(
            final ClassLoader loader) {
        /*
         * Initialize the habitat.
         */
        ModulesRegistry registry = new StaticModulesRegistry(loader);
        Habitat h = registry.createHabitat("default");
        return h;
    }
}
