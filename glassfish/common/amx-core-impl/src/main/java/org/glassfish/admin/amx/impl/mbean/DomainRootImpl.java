/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
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
package org.glassfish.admin.amx.impl.mbean;

import java.util.Map;
import java.util.List;

import org.glassfish.admin.amx.base.*;

import org.glassfish.admin.amx.core.AMXValidator;

import org.glassfish.admin.amx.util.FeatureAvailability;
import com.sun.appserv.server.util.Version;

import org.glassfish.admin.amx.impl.util.Issues;
import org.glassfish.admin.amx.impl.util.ObjectNameBuilder;

import javax.management.ObjectName;
import javax.management.MBeanServer;


import com.sun.enterprise.universal.io.SmartFile;

import com.sun.enterprise.universal.Duration;

import java.io.File;
import org.glassfish.server.ServerEnvironmentImpl;

import org.glassfish.admin.amx.impl.util.InjectedValues;

import java.util.Set;
import org.glassfish.admin.amx.impl.util.ImplUtil;
import org.glassfish.admin.amx.monitoring.MonitoringRoot;
import org.glassfish.admin.amx.util.CollectionUtil;
import org.glassfish.admin.amx.util.MapUtil;
import org.glassfish.admin.amx.util.jmx.JMXUtil;

/**
 */
public class DomainRootImpl extends AMXImplBase // implements DomainRoot
{

    private final String mAppserverDomainName;
    private final File mInstanceRoot;
    private volatile ComplianceMonitor mCompliance;

    public DomainRootImpl() {
        super(null, DomainRoot.class);
        mInstanceRoot = new File(System.getProperty("com.sun.aas.instanceRoot"));
        mAppserverDomainName = mInstanceRoot.getName();
    }

    public void stopDomain() {
        getDomainRootProxy().getRuntime().stopDomain();
    }

    public ObjectName getQueryMgr() {
        return child(Query.class);
    }

    public ObjectName getJ2EEDomain() {
        return child("J2EEDomain");
    }

    public ObjectName getMonitoringRoot() {
        return child(MonitoringRoot.class);
    }

    public ObjectName getPathnames() {
        return child(Pathnames.class);
    }

    public ObjectName getSystemStatus() {
        return child(SystemStatus.class);
    }

    public ObjectName getBulkAccess() {
        return child(BulkAccess.class);
    }

    protected ObjectName preRegisterHook(final MBeanServer server,
            final ObjectName selfObjectName)
            throws Exception {
        // DomainRoot has not yet been registered; any MBeans that exist are non-compliant
        // because they cannot have a Parent.
        final Set<ObjectName> existing = JMXUtil.queryAllInDomain(server, selfObjectName.getDomain());
        if (existing.size() != 0) {
            ImplUtil.getLogger().info(
                    "MBeans exist in AMX domain prior to DomainRoot (violates Parent requirement): " +
                        CollectionUtil.toString(existing, ", "));
        }

        return selfObjectName;
    }

    public void preRegisterDone()
            throws Exception {
        super.preRegisterDone();
    }

    @Override
    protected void postRegisterHook(final Boolean registrationSucceeded) {
        super.postRegisterHook(registrationSucceeded);

        // Start compliance after everything else; it uses key MBeans like Paths
        //turning off ComplianceMonitor for now to help embedded runs.
        if (registrationSucceeded.booleanValue()) {
            // start compliance monitoring immediately, even before children are registered
            mCompliance = ComplianceMonitor.getInstance(getDomainRootProxy());
            mCompliance.start();
        } 
    }

    public Map<ObjectName, List<String>> getComplianceFailures() {
        final Map<ObjectName, AMXValidator.ProblemList> failures =
                mCompliance.getComplianceFailures();
        final Map<ObjectName, List<String>> result = MapUtil.newMap();

        for (final ObjectName failed : failures.keySet()) {
            final AMXValidator.ProblemList problems = failures.get(failed);
            result.put(failed, problems.getProblems());
        }

        return result;
    }

    public String getAppserverDomainName() {
        return (mAppserverDomainName);
    }

    @Override
    protected final void registerChildren() {
        super.registerChildren();
        //System.out.println("Registering children of DomainRoot");
        final ObjectName self = getObjectName();
        final ObjectNameBuilder objectNames =
                new ObjectNameBuilder(getMBeanServer(), self);

        ObjectName childObjectName = null;
        Object mbean = null;
        final MBeanServer server = getMBeanServer();

        /**
        Follow this order: some later MBeans might depend on others.
         */
        childObjectName = objectNames.buildChildObjectName(Pathnames.class);
        mbean = new PathnamesImpl(self);
        registerChild(mbean, childObjectName);

        childObjectName = objectNames.buildChildObjectName(Query.class);
        mbean = new QueryMgrImpl(self);
        registerChild(mbean, childObjectName);

        childObjectName = objectNames.buildChildObjectName(Tools.class);
        mbean = new ToolsImpl(self);
        registerChild(mbean, childObjectName);

        childObjectName = objectNames.buildChildObjectName(BulkAccess.class);
        mbean = new BulkAccessImpl(self);
        registerChild(mbean, childObjectName);

        childObjectName = objectNames.buildChildObjectName(Sample.class);
        mbean = new SampleImpl(self);
        registerChild(mbean, childObjectName);

        // after registering Ext, other MBeans can depend on the above ones egs Paths, Query
        childObjectName = objectNames.buildChildObjectName(Ext.class);
        mbean = new ExtImpl(self);
        registerChild(mbean, childObjectName);

        // Monitoring MBeans can rely on all the prior MBeans
        childObjectName = objectNames.buildChildObjectName(MonitoringRoot.class);
        mbean = new MonitoringRootImpl(self);
        registerChild(mbean, childObjectName);
    }

    public boolean getAMXReady() {
        // just block until ready, no need to support polling
        waitAMXReady();
        return true;
    }

    public void waitAMXReady() {
        FeatureAvailability.getInstance().waitForFeature(FeatureAvailability.AMX_READY_FEATURE, this.getClass().getName());
    }

    public String getDebugPort() {
        Issues.getAMXIssues().notDone("DomainRootImpl.getDebugPort");
        return "" + 9999;
    }

    public String getApplicationServerFullVersion() {
        return Version.getFullVersion();
    }

    public String getInstanceRoot() {
        return SmartFile.sanitize("" + System.getProperty("com.sun.aas.instanceRoot"));
    }

    public String getDomainDir() {
        return SmartFile.sanitize(mInstanceRoot.toString());
    }

    public String getConfigDir() {
        return getDomainDir() + "/" + "config";
    }

    public String getInstallDir() {
        return SmartFile.sanitize("" + System.getProperty("com.sun.aas.installRoot"));
    }

    public Object[] getUptimeMillis() {
        final ServerEnvironmentImpl env = InjectedValues.getInstance().getServerEnvironment();

        final long elapsed = System.currentTimeMillis() - env.getStartupContext().getCreationTime();
        final Duration duration = new Duration(elapsed);

        return new Object[]{
                    elapsed, duration.toString()
                };
    }
}












