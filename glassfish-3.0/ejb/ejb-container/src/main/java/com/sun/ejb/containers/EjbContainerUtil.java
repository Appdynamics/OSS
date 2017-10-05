/*
 * 
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 2008 Sun Microsystems, Inc. All rights reserved.
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
package com.sun.ejb.containers;

import com.sun.enterprise.transaction.api.JavaEETransactionManager;
import com.sun.enterprise.container.common.spi.util.ComponentEnvManager;
import com.sun.enterprise.container.common.spi.util.InjectionManager;
import com.sun.enterprise.config.serverbeans.EjbContainer;
import com.sun.enterprise.deployment.EjbDescriptor;
import org.glassfish.internal.api.ServerContext;
import org.glassfish.server.ServerEnvironmentImpl;
import com.sun.enterprise.admin.monitor.callflow.Agent;
import org.glassfish.api.invocation.ComponentInvocation;
import org.glassfish.api.invocation.InvocationManager;
import org.glassfish.api.naming.GlassfishNamingManager;
import org.jvnet.hk2.annotations.Contract;
import org.jvnet.hk2.component.Habitat;


import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.Synchronization;
import java.util.Timer;
import java.util.Vector;
import java.util.logging.Logger;

import org.glassfish.enterprise.iiop.api.GlassFishORBHelper;
import org.glassfish.flashlight.provider.ProbeProviderFactory;

/**
 * @author Mahesh Kannan
 *         Date: Feb 10, 2008
 */
@Contract
public interface EjbContainerUtil {

    public String TIMER_SERVICE_APP_NAME = "ejb-timer-service-app";

    public String TIMER_SERVICE_UPGRADED = "ejb-timer-service-upgraded";

    public String TIMER_RESOURCE_JNDI = "jdbc/__TimerPool";

    public long MINIMUM_TIMER_DELIVERY_INTERVAL = 1000;

    public GlassFishORBHelper getORBHelper();
    
    public Habitat getDefaultHabitat();

    public  Logger getLogger();

    public  void setEJBTimerService(EJBTimerService es);

    public  EJBTimerService getEJBTimerService();

    public  void registerContainer(BaseContainer container);

    public  void unregisterContainer(BaseContainer container);

    public  BaseContainer getContainer(long id);

    public  EjbDescriptor getDescriptor(long id);

    public  ClassLoader getClassLoader(long id);

    public  Timer getTimer();

    public  void setInsideContainer(boolean bool);

    public  boolean isInsideContainer();

    public  InvocationManager getInvocationManager();

    public  InjectionManager getInjectionManager();

    public  GlassfishNamingManager getGlassfishNamingManager();

    public  ComponentEnvManager getComponentEnvManager();

    public  ComponentInvocation getCurrentInvocation();

    public JavaEETransactionManager getTransactionManager();

    public ServerContext getServerContext();

    public  ContainerSynchronization getContainerSync(Transaction jtx)
        throws RollbackException, SystemException;

    public void removeContainerSync(Transaction tx);

    public void registerPMSync(Transaction jtx, Synchronization sync)
        throws RollbackException, SystemException;

    public EjbContainer getEjbContainer();

    public ServerEnvironmentImpl getServerEnvironment();

    public Agent getCallFlowAgent();
    
    public Vector getBeans(Transaction jtx);
    
    public Object getActiveTxCache(Transaction jtx);
    
    public void setActiveTxCache(Transaction jtx, Object cache);

    public void addWork(Runnable task);

    public String getHAPersistenceType();

    public EjbDescriptor ejbIdToDescriptor(long ejbId);

    public boolean isEJBLite();

    public boolean isEmbeddedServer();

    public ProbeProviderFactory getProbeProviderFactory();

}
