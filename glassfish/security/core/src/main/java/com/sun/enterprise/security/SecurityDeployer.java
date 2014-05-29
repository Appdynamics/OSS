/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2006-2010 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.enterprise.security;

import com.sun.enterprise.security.web.integration.WebSecurityManagerFactory;
import com.sun.enterprise.security.web.integration.WebSecurityManager;
import org.glassfish.api.deployment.DeploymentContext;
import org.glassfish.api.deployment.MetaData;
import org.glassfish.api.deployment.OpsParams;
import org.glassfish.deployment.common.DeploymentException;
import org.glassfish.deployment.common.SimpleDeployer;
import org.glassfish.deployment.common.DummyApplication;
import com.sun.enterprise.deployment.WebBundleDescriptor;
import com.sun.enterprise.deployment.EjbBundleDescriptor;
import com.sun.enterprise.deployment.Application;
import com.sun.enterprise.security.util.IASSecurityException;
import org.glassfish.internal.api.ServerContext;
import com.sun.logging.LogDomains;


import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.glassfish.api.event.EventListener.Event;
import org.glassfish.api.event.EventListener;
import org.glassfish.api.event.EventTypes;
import org.glassfish.api.event.Events;
import org.glassfish.internal.deployment.Deployment;
import org.jvnet.hk2.annotations.Inject;
import org.jvnet.hk2.annotations.Service;
import org.jvnet.hk2.component.Habitat;
import org.jvnet.hk2.component.PostConstruct;
import org.glassfish.internal.data.ApplicationInfo;
import org.glassfish.api.invocation.InvocationManager;
import org.glassfish.api.invocation.RegisteredComponentInvocationHandler;
import org.glassfish.internal.data.ModuleInfo;

/**
 * Security Deployer which generate and clean the security policies
 *
 */
@Service(name = "Security")
public class SecurityDeployer extends SimpleDeployer<SecurityContainer, DummyApplication> implements PostConstruct {

    private static final Logger _logger = LogDomains.getLogger(SecurityDeployer.class, LogDomains.SECURITY_LOGGER);
    @Inject
    private ServerContext serverContext;
    @Inject
    private Habitat habitat;
    @Inject
    private WebSecurityManagerFactory wsmf;
    private EventListener listener = null;
    private static WebSecurityDeployerProbeProvider websecurityProbeProvider = new WebSecurityDeployerProbeProvider();
    private static EjbSecurityPolicyProbeProvider ejbProbeProvider = new EjbSecurityPolicyProbeProvider();

    private class AppDeployEventListener implements EventListener {

        public void event(Event event) {
            Application app = null;
            if (Deployment.MODULE_LOADED.equals(event.type())) {
                ModuleInfo moduleInfo = (ModuleInfo) event.hook();
                if (moduleInfo instanceof ApplicationInfo) {
                    return;
                }
                WebBundleDescriptor webBD = (WebBundleDescriptor) moduleInfo.getMetaData(WebBundleDescriptor.class);
                loadPolicy(webBD, false);
            } else if (Deployment.APPLICATION_LOADED.equals(event.type())) {
                ApplicationInfo appInfo = (ApplicationInfo) event.hook();
                app = appInfo.getMetaData(Application.class);
                if (app == null) {
                    // this is not a Java EE module, just return
                    return;
                }

                Set<WebBundleDescriptor> webDesc = app.getWebBundleDescriptors();
                linkPolicies(app, webDesc);
                commitEjbs(app);
                if (webDesc != null && !webDesc.isEmpty()) {
                    //Register the WebSecurityComponentInvocationHandler
                    RegisteredComponentInvocationHandler handler = habitat.getComponent(RegisteredComponentInvocationHandler.class, "webSecurityCIH");
                    if (handler != null) {
                        handler.register();
                    }
                }

            } else if (WebBundleDescriptor.AFTER_SERVLET_CONTEXT_INITIALIZED_EVENT.equals(event.type())) {
                commitPolicy((WebBundleDescriptor) event.hook());
            }
        }
    };

    // creates security policy if needed
    @Override
    protected void generateArtifacts(DeploymentContext dc)
            throws DeploymentException {
        OpsParams params = dc.getCommandParameters(OpsParams.class);
        if (params.origin != OpsParams.Origin.deploy) {
            return;
        }

        String appName = params.name();

        try {
            Application app = dc.getModuleMetaData(Application.class);
            Set<WebBundleDescriptor> webDesc = app.getWebBundleDescriptors();
            if (webDesc == null) {
                return;
            }

            for (WebBundleDescriptor webBD : webDesc) {
                loadPolicy(webBD, false);
            }

        } catch (Exception se) {
            String msg = "Error in generating security policy for " + appName;
            throw new DeploymentException(msg, se);
        }
    }

    // removes security policy if needed
    @Override
    protected void cleanArtifacts(DeploymentContext dc)
            throws DeploymentException {
        removePolicy(dc);
        SecurityUtil.removeRoleMapper(dc);
    }

    @Override
    public DummyApplication load(SecurityContainer container, DeploymentContext context) {
        return new DummyApplication();
    }

    @Override
    public void unload(DummyApplication container, DeploymentContext context) {
        OpsParams params = context.getCommandParameters(OpsParams.class);
        cleanSecurityContext(params.name());
    }

    /**
     * Translate Web Bundle Policy
     * @param webBD
     * @param remove boolean indicated whether any existing policy statements
     * are removed form context before translation
     * @throws DeploymentException
     */
    private void loadPolicy(WebBundleDescriptor webBD, boolean remove)
            throws DeploymentException {
        try {
            if (webBD != null) {
                if (remove) {
                    String cid = SecurityUtil.getContextID(webBD);
                    WebSecurityManager wsm = wsmf.getManager(cid, null, true);
                    if (wsm != null) {
                        wsm.release();
                    }
                }
                wsmf.createManager(webBD, true, serverContext);
            }

        } catch (Exception se) {
            String msg = "Error in generating security policy for " +
                    webBD.getModuleDescriptor().getModuleName();
            throw new DeploymentException(msg, se);
        }
    }

    /**
     * puts Web Bundle Policy In Service, repeats translation is Descriptor
     * indicate policy was changed by ContextListener.
     * @param webBD
     * @throws DeploymentException
     */
    private void commitPolicy(WebBundleDescriptor webBD)
            throws DeploymentException {
        try {
            if (webBD != null) {
                if (webBD.isPolicyModified()) {
                    // redo policy translation for web module
                    loadPolicy(webBD, true);
                }
                String cid = SecurityUtil.getContextID(webBD);
                websecurityProbeProvider.policyCreationStartedEvent(cid);
                SecurityUtil.generatePolicyFile(cid);
                websecurityProbeProvider.policyCreationEndedEvent(cid);
                websecurityProbeProvider.policyCreationEvent(cid);
                
            }
        } catch (Exception se) {
            String msg = "Error in generating security policy for " +
                    webBD.getModuleDescriptor().getModuleName();
            throw new DeploymentException(msg, se);
        }
    }

    /**
     * commits ejb policy contexts.
     * This should occur in EjbApplication, being done here until
     * issue with ejb-ejb31-singleton-multimoduleApp.ear is resolved   
     * @param ejbs
     */
    private void commitEjbs(Application app) throws DeploymentException {
        Set<EjbBundleDescriptor> ejbDescriptors = app.getEjbBundleDescriptors();
        try {
            for (EjbBundleDescriptor ejbBD : ejbDescriptors) {
                String pcid = SecurityUtil.getContextID(ejbBD);
                ejbProbeProvider.policyCreationStartedEvent(pcid);
                SecurityUtil.generatePolicyFile(pcid);
                ejbProbeProvider.policyCreationEndedEvent(pcid);
                ejbProbeProvider.policyCreationEvent(pcid);
               
            }
        } catch (Exception se) {
            String msg = "Error in committing security policy for ejbs of " +
                    app.getRegistrationName();
            throw new DeploymentException(msg, se);
        }
    }

    /**
     * Links the policy contexts of the application
     *
     * @param app
     * @param webs
     */
    private void linkPolicies(Application app,
            Collection<WebBundleDescriptor> webs) throws DeploymentException {
        try {

            String linkName = null;
            boolean lastInService = false;
            for (WebBundleDescriptor wbd : webs) {
                String name = SecurityUtil.getContextID(wbd);
                lastInService =
                        SecurityUtil.linkPolicyFile(name, linkName, lastInService);
                linkName = name;
            }

            Set<EjbBundleDescriptor> ejbs = app.getEjbBundleDescriptors();
            for (EjbBundleDescriptor ejbd : ejbs) {
                String name = SecurityUtil.getContextID(ejbd);
                lastInService =
                        SecurityUtil.linkPolicyFile(name, linkName, lastInService);
                linkName = name;
            }
            // extra commit (see above)

        } catch (IASSecurityException se) {
            String msg = "Error in linking security policy for " + app.getRegistrationName();
            throw new DeploymentException(msg, se);
        }
    }

    private void removePolicy(DeploymentContext dc)
            throws DeploymentException {
        OpsParams params = dc.getCommandParameters(OpsParams.class);
        if (params.origin != OpsParams.Origin.undeploy
                && params.origin != OpsParams.Origin.deploy) {
            return;
        }
        String appName = params.name();
        //Monitoring 

        //Remove policy files only if managers are not destroyed by cleanup
        try {
            String[] webcontexts = wsmf.getContextsForApp(appName, false);
            if (webcontexts != null) {
                for (int i = 0; i < webcontexts.length; i++) {
                    if (webcontexts[i] != null) {
                        websecurityProbeProvider.policyDestructionStartedEvent(webcontexts[i]);
                        SecurityUtil.removePolicy(webcontexts[i]);
                        websecurityProbeProvider.policyDestructionEndedEvent(webcontexts[i]);
                        websecurityProbeProvider.policyDestructionEvent(webcontexts[i]);
                    }
                }
            }
        } catch (IASSecurityException ex) {
            String msg = "Error in removing security policy for " + appName;
            _logger.log(Level.WARNING, msg, ex);
            throw new DeploymentException(msg, ex);
        }

        //Destroy the managers if present
        cleanSecurityContext(appName);

        /* From V2 but keep commented until need is discovered
        //remove any remaining policy
        //This is to address the bug where the CONTEXT_ID in
        //WebSecurityManagerFactory is not properly populated.
        //We force the sub-modules to be removed in this case.
        //This should not impact undeploy performance on DAS.
        //This needs to be fixed better later.
        String policyRootDir = System.getProperty(
        "com.sun.enterprise.jaccprovider.property.repository");
        if (policyRootDir != null) {
        List<String> contextIds = new ArrayList<String>();
        File policyDir = new File(policyRootDir + File.separator + appName);
        if (policyDir.exists()) {
        File[] policies = policyDir.listFiles();
        for (int i = 0; i < policies.length; i++) {
        if (policies[i].isDirectory()) {
        contextIds.add(appName + '/' + policies[i].getName());
        }
        }
        } else {
        //we tried.  give up now.
        }
        if (contextIds.size() > 0) {
        for (String cId : contextIds) {
        SecurityUtil.removePolicy(cId);
        }
        }
        }*/
    }

    public MetaData getMetaData() {
        return new MetaData(false, null, new Class[]{Application.class});
    }

    /**
     * Clean security policy generated at deployment time.
     * NOTE: This routine calls destroy on the WebSecurityManagers,
     * but that does not cause deletion of the underlying policy (files).
     * The underlying policy is deleted when removePolicy
     * (in AppDeployerBase and WebModuleDeployer) is called.
     * @param appName  the app name
     */
    private boolean cleanSecurityContext(String appName) {
        boolean cleanUpDone = false;
        ArrayList<WebSecurityManager> managers =
                wsmf.getManagersForApp(appName, false);
        for (int i = 0; managers !=
                null && i < managers.size(); i++) {
            try {
               
                
                websecurityProbeProvider.securityManagerDestructionStartedEvent(appName);
                managers.get(i).destroy();
                websecurityProbeProvider.securityManagerDestructionEndedEvent(appName);
                websecurityProbeProvider.securityManagerDestructionEvent(appName);
                cleanUpDone =
                        true;
            } catch (javax.security.jacc.PolicyContextException pce) {
                // log it and continue
                _logger.log(Level.WARNING,
                        "Unable to destroy WebSecurityManager",
                        pce);
            }

        }
        return cleanUpDone;
    }

    public static List<EventTypes> getDeploymentEvents() {
        ArrayList<EventTypes> events = new ArrayList<EventTypes>();
        events.add(Deployment.APPLICATION_PREPARED);
        return events;
    }

    public void postConstruct() {
        listener = new AppDeployEventListener();
        Events events = habitat.getByContract(Events.class);
        events.register(listener);
    }

}

