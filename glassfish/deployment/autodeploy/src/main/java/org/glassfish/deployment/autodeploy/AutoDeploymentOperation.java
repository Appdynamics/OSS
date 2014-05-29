/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2008-2011 Oracle and/or its affiliates. All rights reserved.
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

package org.glassfish.deployment.autodeploy;

import java.io.File;
import java.util.Properties;

import org.glassfish.api.admin.AdminCommand;
import org.glassfish.deployment.autodeploy.AutoDeployer.AutodeploymentStatus;
import org.glassfish.deployment.common.DeploymentProperties;
import org.jvnet.hk2.annotations.Inject;
import org.jvnet.hk2.annotations.Scoped;
import org.jvnet.hk2.annotations.Service;
import org.jvnet.hk2.component.Habitat;
import org.jvnet.hk2.component.PerLookup;

/**
 * Performs a single auto-deployment operation for a single file.
 * <p>
 * Note - Use the newInstance static method to obtain a fully-injected operation;
 * it is safer and more convenient than using the no-arg constructor and then 
 * invoking init yourself.
 * 
 * @author tjquinn
 */
@Service
@Scoped(PerLookup.class)
public class AutoDeploymentOperation extends AutoOperation {

    /**
     * Creates a fully-injected, ready-to-use AutoDeploymentOperation object.
     * @param habitat
     * @param renameOnSuccess
     * @param file
     * @param enabled
     * @param virtualServer
     * @param forceDeploy
     * @param verify
     * @param preJspCompilation
     * @param target
     * @return the injected, initialized AutoDeploymentOperation
     */
    static AutoDeploymentOperation newInstance(
            Habitat habitat,
            boolean renameOnSuccess,
            File file, 
            boolean enabled,
            String virtualServer,
            boolean forceDeploy,
            boolean verify,
            boolean preJspCompilation,
            String target) {
        
        AutoDeploymentOperation o = 
                (AutoDeploymentOperation) habitat.getComponent(AutoDeploymentOperation.class);
        
        o.init(renameOnSuccess, file, enabled, virtualServer, forceDeploy, verify, preJspCompilation, target);
        return o;
    }  
    
    private boolean renameOnSuccess;
    
    private static final String COMMAND_NAME = "deploy";
    
    @Inject(name=COMMAND_NAME)
    private AdminCommand deployCommand;
    
    @Inject
    private AutodeployRetryManager retryManager;
    
    /**
     * Completes the initialization of the object.
     * @param renameOnSuccess
     * @param file
     * @param enabled
     * @param virtualServer
     * @param forceDeploy
     * @param verify
     * @param preJspCompilation
     * @param target
     * @return the object itself for convenience
     */
    protected AutoDeploymentOperation init (
            boolean renameOnSuccess,
            File file, 
            boolean enabled,
            String virtualServer,
            boolean forceDeploy,
            boolean verify,
            boolean preJspCompilation,
            String target) {
        
        super.init(file, getDeployActionProperties(
                        file, 
                        enabled, 
                        virtualServer, 
                        forceDeploy,
                        verify,
                        preJspCompilation,
                        target),
                        COMMAND_NAME,
                        deployCommand);
        this.renameOnSuccess = renameOnSuccess;
        return this;
    }
    
    /**
     * {@inheritDoc}
     */
    protected String getMessageString(AutodeploymentStatus ds, File file) {
        return localStrings.getLocalString(
                ds.deploymentMessageKey, 
                ds.deploymentDefaultMessage, 
                file);
    }

    /**
     * {@inheritDoc}
     */
    protected void markFiles(AutodeploymentStatus ds, File file) {
        /*
         * One reason an auto-deployment may fail is if the auto-deployed
         * file is a directory and the directory's contents were not yet
         * complete when the autodeployer detected a change in the top-level
         * directory file's timestamp.  Retry a failed autodeploy of a
         * directory for the prescribed retry period or until it succeeds.
         *
         * Similarly, a JAR file - or any legitimate application file - might
         * be copied over a period of time, so an initial attempt to deploy it
         * might fail because the file is incomplete but a later attempt might
         * work.
         */
        if (ds != AutodeploymentStatus.SUCCESS && ds != AutodeploymentStatus.WARNING) {
            try {
                retryManager.recordFailedDeployment(file);
            } catch (AutoDeploymentException ex) {
                /*
                 * The retry manager has concluded that this most recent
                 * failure should be the last one.
                 */
                markDeployFailed(file);
                retryManager.endMonitoring(file);
            }
        } else {
            retryManager.recordSuccessfulDeployment(file);
            if (ds.status) {
                if (renameOnSuccess) {
                    markDeployed(file);
                }
            } else {
                markDeployFailed(file);
            }
        }
    }
    
    // Methods for creating operation status file(s)
    private void markDeployed(File f) {
        try {
            deleteAllMarks(f);
            getDeployedFile(f).createNewFile();
        } catch (Exception e) { 
            //ignore 
        }
    }
    
    private void markDeployFailed(File f) {
        try {
            deleteAllMarks(f);
            getDeployFailedFile(f).createNewFile();
        } catch (Exception e) { 
            //ignore 
        }
    }
    
    private static Properties getDeployActionProperties(
            File deployablefile,
            boolean enabled,
            String virtualServer,
            boolean forceDeploy,
            boolean verify,
            boolean jspPreCompilation,
            String target){
        
        DeploymentProperties dProps = new DeploymentProperties();
        dProps.setPath(deployablefile.getAbsolutePath());
//        dProps.setUpload(false);
        dProps.setEnabled(enabled);
        if (virtualServer != null) {
            dProps.setVirtualServers(virtualServer);
        }
        dProps.setForce(forceDeploy);
        dProps.setVerify(verify);
        dProps.setPrecompileJSP(jspPreCompilation);
//        dProps.setResourceAction(DeploymentProperties.RES_DEPLOYMENT);
//        dProps.setResourceTargetList(target);

        dProps.setProperty(DeploymentProperties.LOG_REPORTED_ERRORS, "false");
        return (Properties)dProps;
    }
}
