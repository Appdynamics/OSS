/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2009-2010 Oracle and/or its affiliates. All rights reserved.
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

package org.glassfish.enterprise.iiop.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.sun.enterprise.config.serverbeans.Config;
import com.sun.enterprise.config.serverbeans.Configs;
import com.sun.enterprise.config.serverbeans.IiopListener;
import com.sun.enterprise.config.serverbeans.IiopService;
import com.sun.enterprise.config.serverbeans.Server;
import com.sun.enterprise.config.serverbeans.ServerRef;
import com.sun.grizzly.config.dom.NetworkListener;
import com.sun.grizzly.config.dom.ThreadPool;
import org.glassfish.api.admin.ProcessEnvironment;
import org.glassfish.api.admin.ProcessEnvironment.ProcessType;
import org.glassfish.api.admin.ServerEnvironment;
import org.glassfish.enterprise.iiop.api.GlassFishORBLifeCycleListener;
import org.glassfish.enterprise.iiop.api.IIOPInterceptorFactory;
import org.glassfish.internal.api.ClassLoaderHierarchy;
import org.jvnet.hk2.annotations.Inject;
import org.jvnet.hk2.annotations.Service;
import org.jvnet.hk2.component.Habitat;
import org.jvnet.hk2.component.PostConstruct;
import org.omg.CORBA.ORB;

/**
 * @author Mahesh Kannan
 *         Date: Jan 15, 2009
 */
@Service
public class IIOPUtils implements PostConstruct {

    private static IIOPUtils _me;

    @Inject
    Habitat habitat ;

    @Inject
    private ClassLoaderHierarchy clHierarchy;

    @Inject
    private ProcessEnvironment processEnv;

    private ProcessType processType;

    // The following info is only available for ProcessType.Server
    private Collection<ThreadPool> threadPools;
    private IiopService iiopService;
    private Collection<ServerRef> serverRefs;
    private Configs configs;

    // Set during init
    private ORB defaultORB;

    //private GlassFishORBManager gfORBMgr;

    public void postConstruct() {

        processType = processEnv.getProcessType();

        if( processEnv.getProcessType().isServer()) {

            iiopService = habitat.getComponent(IiopService.class);
            final Collection<ThreadPool> threadPool = iiopService.getParent(Config.class).getThreadPools().getThreadPool();
            final Collection<NetworkListener> listeners = habitat.getAllByContract(NetworkListener.class);
            final Set<String> names = new TreeSet<String>();
            threadPools = new ArrayList<ThreadPool>();
            for (NetworkListener listener : listeners) {
                names.add(listener.getThreadPool());
            }
            for (ThreadPool pool : threadPool) {
                if(!names.contains(pool.getName())) {
                    threadPools.add(pool);
                }
            }
            serverRefs  = habitat.getAllByContract(ServerRef.class);
            configs     = habitat.getComponent(Configs.class);
        }

        IIOPUtils.initMe(this);

    }

    private static void initMe(IIOPUtils utils) {
        _me = utils;
    }

    public static IIOPUtils getInstance() {
        return _me;
    }


    public static void setInstance(IIOPUtils utils) {
        _me = utils;
    }

    /*
    void setGlassFishORBManager(GlassFishORBManager orbMgr) {
        gfORBMgr = orbMgr;
    }

    GlassFishORBManager getGlassFishORBManager() {
        return gfORBMgr;
    }
    */

    public ClassLoader getCommonClassLoader() {
        return clHierarchy.getCommonClassLoader();
    }

    private void assertServer() {
        if ( !processType.isServer() ) {
            throw new IllegalStateException("Only available in Server mode");
        }
    }

    public IiopService getIiopService() {
        assertServer();
        return iiopService;
    }

    public Collection<ThreadPool> getAllThreadPools() {
        assertServer();
        return threadPools;
    }

    public Collection<ServerRef> getServerRefs() {
        assertServer();
        return serverRefs;
    }

    public List<IiopListener> getIiopListeners() {
        assertServer();
        return iiopService.getIiopListener();
    }

    public Collection<IIOPInterceptorFactory> getAllIIOPInterceptrFactories() {
        return habitat.getAllByContract(IIOPInterceptorFactory.class);
    }

    public Collection<GlassFishORBLifeCycleListener> getGlassFishORBLifeCycleListeners() {
        return habitat.getAllByContract(GlassFishORBLifeCycleListener.class);
    }

    public ProcessType getProcessType() {
        return processType;
    }

    public Habitat getHabitat() {
        return habitat;
    }


    public void setORB(ORB orb) {
        defaultORB = orb;
    }

    // For internal use only.  All other modules should use orb-connector
    // GlassFishORBHelper to acquire default ORB.
    public ORB getORB() {
        return defaultORB;
    }

}
