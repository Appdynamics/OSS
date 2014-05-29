/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010-2011 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.enterprise.admin.util;

import com.sun.enterprise.config.serverbeans.Server;
import com.sun.enterprise.util.LocalStringManagerImpl;
import com.sun.enterprise.admin.remote.RemoteAdminCommand;

import com.sun.enterprise.config.serverbeans.Config;
import java.io.File;

import com.sun.logging.LogDomains;
import java.util.logging.Level;
import org.glassfish.api.ActionReport;
import org.glassfish.api.admin.*;
import org.glassfish.internal.api.Target;
import org.glassfish.config.support.CommandTarget;
import org.jvnet.hk2.component.Habitat;

import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

/**
 *
 */
public class ClusterOperationUtil {
    private static final Logger logger = LogDomains.getLogger(ClusterOperationUtil.class,
                                        LogDomains.ADMIN_LOGGER);
    private static final LocalStringManagerImpl strings =
                        new LocalStringManagerImpl(ClusterOperationUtil.class);

    //TODO : Begin temp fix for undoable commands
    private static List<Server> completedInstances = new ArrayList<Server>();

    public static List<Server> getCompletedInstances() {
        return completedInstances;
    }

    public static void clearInstanceList() {
        completedInstances.clear();
    }
    //TODO : End temp fix for undoable commands

    public static ActionReport.ExitCode replicateCommand(String commandName,
                                                   FailurePolicy failPolicy,
                                                   FailurePolicy offlinePolicy,
                                                   List<Server> instancesForReplication,
                                                   AdminCommandContext context,
                                                   ParameterMap parameters,
                                                   Habitat habitat) {
        return replicateCommand(commandName, failPolicy, offlinePolicy, 
                instancesForReplication, context, parameters, habitat, null);
    }

    /**
     * Replicates a given command on the given list of targets, optionally gathering
     * downloaded result payloads from the instance commands into a directory.
     * <p>
     * If intermediateDownloadDir is non-null, then any files returned from
     * the instances in the payload of the HTTP response will be stored in a
     * directory tree like this:
     * <pre>
     * ${intermediateDownloadDir}/
     *     ${instanceA}/
     *         file(s) returned from instance A
     *     ${instanceB}/
     *         file(s) returned from instance B
     *     ...
     * </pre>
     * where ${instanceA}, ${instanceB}, etc. are the names of the instances to
     * which the command was replicated.  This method does no further processing
     * on the downloaded files but leaves that to the calling command.
     */
    public static ActionReport.ExitCode replicateCommand(String commandName,
                                                   FailurePolicy failPolicy,
                                                   FailurePolicy offlinePolicy,
                                                   List<Server> instancesForReplication,
                                                   AdminCommandContext context,
                                                   ParameterMap parameters,
                                                   Habitat habitat,
                                                   final File intermediateDownloadDir) {

        ActionReport.ExitCode returnValue = ActionReport.ExitCode.SUCCESS;
        InstanceStateService instanceState = habitat.getComponent(InstanceStateService.class);
        validateIntermediateDownloadDir(intermediateDownloadDir);
        RemoteInstanceCommandHelper rich = new RemoteInstanceCommandHelper(habitat);
        Map<String, Future<InstanceCommandResult>> futures = new HashMap<String, Future<InstanceCommandResult>>();
        try {
            for(Server svr : instancesForReplication) {
                if (instanceState.getState(svr.getName()) == InstanceState.StateType.NEVER_STARTED) {
                    // Do not replicate commands to instances that have never been started.
                    continue;
                }
                Config scfg = svr.getConfig();
                if (!Boolean.valueOf(scfg.getDynamicReconfigurationEnabled())) {
                    // Do not replicate to servers for which dynamic configuration is disabled
                    ActionReport aReport = context.getActionReport().addSubActionsReport();
                    aReport.setActionExitCode(ActionReport.ExitCode.WARNING);
                    aReport.setMessage(strings.getLocalString("clusterutil.dynrecfgdisabled",
                            "WARNING: The command {0} was not replicated to instance {1} because the " +
                                    "dynamic-reconfiguration-enabled flag is set to false for config {2}", 
                            new Object[] {commandName, svr.getName(), scfg.getName()}));                  
                    instanceState.setState(svr.getName(), InstanceState.StateType.RESTART_REQUIRED, false);
                    instanceState.addFailedCommandToInstance(svr.getName(), commandName, parameters);
                    returnValue = ActionReport.ExitCode.WARNING;
                    continue;
                }
                String host = svr.getAdminHost();
                int port = rich.getAdminPort(svr);
                ActionReport aReport = context.getActionReport().addSubActionsReport();
                InstanceCommandResult aResult = new InstanceCommandResult();
                InstanceCommandExecutor ice =
                        new InstanceCommandExecutor(habitat, commandName, failPolicy, offlinePolicy,
                                svr, host, port, logger, parameters, aReport, aResult);
                if (CommandTarget.DAS.isValid(habitat, ice.getServer().getName()))
                    continue;
                if (intermediateDownloadDir != null) {
                    ice.setFileOutputDirectory(
                        subdirectoryForInstance(intermediateDownloadDir, ice));
                }
                Future<InstanceCommandResult> f = instanceState.submitJob(svr, ice, aResult);
                if(f==null) {
                    logger.severe(strings.getLocalString("clusterutil.instancehasnostate",
                            "Could not find state of instance registered in the state service"));
                    continue;
                }
                futures.put(svr.getName(), f);
                logger.fine(strings.getLocalString("dynamicreconfiguration.diagnostics.jobsubmitted",
                        "Successfully submitted command {0} for execution at instance {1}",
                          commandName, svr.getName()));
            }
        } catch (Exception ex) {
            ActionReport aReport = context.getActionReport().addSubActionsReport();
            ActionReport.ExitCode finalResult = FailurePolicy.applyFailurePolicy(failPolicy,
                    ActionReport.ExitCode.FAILURE);
            aReport.setActionExitCode(finalResult);
            aReport.setMessage(strings.getLocalString("clusterutil.replicationfailed",
                    "Error during command replication: {0}", ex.getLocalizedMessage()));
            context.getLogger().log(Level.SEVERE, strings.getLocalString("clusterutil.replicationfailed",
                    "Error during command replication: {0}", 
                    ex.getLocalizedMessage()));
            if(returnValue ==ActionReport.ExitCode.SUCCESS)
                returnValue = finalResult;
        }

        boolean gotFirstResponse = false;
        long maxWaitTime = RemoteAdminCommand.getReadTimeout();
        long timeBeforeAsadminTimeout = maxWaitTime;
        long waitStart = System.currentTimeMillis();
        for(String s : futures.keySet()) {
            ActionReport.ExitCode finalResult;
            try {
                logger.fine(strings.getLocalString("dynamicreconfiguration.diagnostics.waitingonjob",
                        "Waiting for command {0} to be completed at instance {1}", commandName, s));
                Future<InstanceCommandResult> aFuture = futures.get(s);
                InstanceCommandResult aResult = aFuture.get(maxWaitTime, TimeUnit.MILLISECONDS);
                long elapsedTime = System.currentTimeMillis() - waitStart;
                timeBeforeAsadminTimeout -= elapsedTime;
                if(!gotFirstResponse) {
                    maxWaitTime = elapsedTime * 4;
                    gotFirstResponse = true;
                }
                if( (maxWaitTime > timeBeforeAsadminTimeout) ||
                    (maxWaitTime < 60000) )
                    maxWaitTime = timeBeforeAsadminTimeout;
                InstanceCommandExecutor ice = (InstanceCommandExecutor) aResult.getInstanceCommand();
                if(ice.getReport().getActionExitCode() != ActionReport.ExitCode.FAILURE)
                    completedInstances.add(ice.getServer());
                finalResult = FailurePolicy.applyFailurePolicy(failPolicy, ice.getReport().getActionExitCode());
                if(returnValue == ActionReport.ExitCode.SUCCESS)
                    returnValue = finalResult;
                if(finalResult != ActionReport.ExitCode.SUCCESS) {
                    instanceState.setState(s, InstanceState.StateType.RESTART_REQUIRED, false);
                    instanceState.addFailedCommandToInstance(s, commandName, parameters);
                }
            } catch (Exception ex) {
                ActionReport aReport = context.getActionReport().addSubActionsReport();
                finalResult = ActionReport.ExitCode.FAILURE;
                finalResult = FailurePolicy.applyFailurePolicy(failPolicy, ActionReport.ExitCode.FAILURE);
                if(finalResult == ActionReport.ExitCode.FAILURE) {
                    if(ex instanceof TimeoutException)
                        aReport.setMessage(strings.getLocalString("clusterutil.timeoutwhilewaiting",
                            "Timed out while waiting for result from instance {0}", s));
                    else
                        aReport.setMessage(strings.getLocalString("clusterutil.exceptionwhilewaiting",
                            "Exception while waiting for result from instance {0} : {1}", s, ex.getLocalizedMessage()));
                }
                aReport.setActionExitCode(finalResult);
                if(returnValue == ActionReport.ExitCode.SUCCESS)
                    returnValue = finalResult;
                instanceState.setState(s, InstanceState.StateType.RESTART_REQUIRED, false);
                instanceState.addFailedCommandToInstance(s, commandName, parameters);
            }
        }
        return returnValue;
    }

    public static ActionReport.ExitCode replicateCommand(String commandName,
                                                   FailurePolicy failPolicy,
                                                   FailurePolicy offlinePolicy,
                                                   Collection<String> targetNames,
                                                   AdminCommandContext context,
                                                   ParameterMap parameters,
                                                   Habitat habitat) {
        return replicateCommand(commandName, failPolicy, offlinePolicy,
                targetNames, context, parameters, habitat, null);
    }

    /**
     * Replicates a given command on the given list of targets, optionally gathering
     * downloaded result payloads from the instance commands into a directory.
     * <p>
     * If intermediateDownloadDir is non-null, then any files returned from
     * the instances in the payload of the HTTP response will be stored in a
     * directory tree like this:
     * <pre>
     * ${intermediateDownloadDir}/
     *     ${instanceA}/
     *         file(s) returned from instance A
     *     ${instanceB}/
     *         file(s) returned from instance B
     *     ...
     * </pre>
     * where ${instanceA}, ${instanceB}, etc. are the names of the instances to
     * which the command was replicated.  This method does no further processing
     * on the downloaded files but leaves that to the calling command.
     */
    public static ActionReport.ExitCode replicateCommand(String commandName,
                                                   FailurePolicy failPolicy,
                                                   FailurePolicy offlinePolicy,
                                                   Collection<String> targetNames,
                                                   AdminCommandContext context,
                                                   ParameterMap parameters,
                                                   Habitat habitat,
                                                   File intermediateDownloadDir) {

        ActionReport.ExitCode result = ActionReport.ExitCode.SUCCESS;
        Target targetService = habitat.getComponent(Target.class);
        for(String t : targetNames) {
            if(CommandTarget.DAS.isValid(habitat, t) ||
                    CommandTarget.DOMAIN.isValid(habitat, t))
                continue;
            parameters.set("target", t);
            ActionReport.ExitCode returnValue = replicateCommand(commandName,
                    failPolicy, offlinePolicy, targetService.getInstances(t), context, parameters, habitat,
                    intermediateDownloadDir);
            if(!returnValue.equals(ActionReport.ExitCode.SUCCESS)) {
                result = returnValue;
            }
        }
        return result;
    }

    /**
     * Makes sure the intermediate download directory is null (meaning the calling
     * command does not care about any downloaded content from the instances) or
     * that the specified file is a valid place to store any downloaded files.
     * Create the directory if it does not already exist.
     *
     * @param dir the caller-specified File to check
     */
    private static void validateIntermediateDownloadDir(final File dir) {
        if (dir == null) {
            return;
        }
        if ( ! dir.exists()) {
            dir.mkdirs();
        } else {
            if (! dir.isDirectory() || ! dir.canWrite()) {
                throw new IllegalArgumentException(dir.getAbsolutePath());
            }
        }
    }

    private static File subdirectoryForInstance(final File dir,
            final InstanceCommandExecutor exec) {
        return new File(dir, exec.getServer().getName());
    }
}
