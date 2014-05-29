/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010 Oracle and/or its affiliates. All rights reserved.
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

package org.glassfish.gms.admin;

import com.sun.enterprise.config.serverbeans.Domain;
import com.sun.enterprise.ee.cms.core.GMSConstants;
import com.sun.enterprise.ee.cms.core.GroupManagementService;
import com.sun.logging.LogDomains;
import org.glassfish.api.ActionReport;
import org.glassfish.api.Param;
import org.glassfish.api.admin.*;
import org.glassfish.gms.bootstrap.GMSAdapterService;
import org.jvnet.hk2.annotations.Inject;
import org.jvnet.hk2.annotations.Scoped;
import org.jvnet.hk2.annotations.Service;
import org.jvnet.hk2.component.Habitat;
import org.jvnet.hk2.component.PerLookup;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


@Service(name = "_gms-announce-after-start-cluster-command")
@Supplemental(value = "start-cluster", on = Supplemental.Timing.After, ifFailure = FailurePolicy.Warn)
@Scoped(PerLookup.class)
public class GMSAnnounceAfterStartClusterCommand implements AdminCommand {

    private static final Logger logger = LogDomains.getLogger(
        GMSAnnounceAfterStartClusterCommand.class, LogDomains.GMS_LOGGER);

    @Inject
    private ServerEnvironment env;
    @Inject
    private Habitat habitat;
    @Param(optional = false, primary = true)
    private String clusterName;
    @Inject
    private Domain domain;
    @Inject
    private CommandRunner runner;
    @Param(optional = true, defaultValue = "false")
    private boolean verbose;

    @Inject
    GMSAdapterService gmsAdapterService;


    @Override
    public void execute(AdminCommandContext context) {
        ActionReport report = context.getActionReport();
        announceGMSGroupStartupComplete(clusterName, report);
    }

    static public void announceGMSGroupStartupComplete(String clusterName, ActionReport report) {
        if (report != null) {
            GMSAnnounceSupplementalInfo gmsInfo = report.getResultType(GMSAnnounceSupplementalInfo.class);
            if (gmsInfo != null && gmsInfo.gmsInitiated) {
                List<String> members = null;
                GMSConstants.groupStartupState groupStartupState = GMSConstants.groupStartupState.COMPLETED_FAILED;

                logger.log(Level.INFO, "after.start", new Object [] {
                    report.getActionExitCode(), members, gmsInfo.clusterMembers
                });
                switch (report.getActionExitCode()) {
                    case SUCCESS:
                        // all instances started
                        members = gmsInfo.clusterMembers;
                        groupStartupState = GMSConstants.groupStartupState.COMPLETED_SUCCESS;
                        break;

                    case FAILURE:      // all instances failed.  should be in members list.
                        members = gmsInfo.clusterMembers;
                        groupStartupState = GMSConstants.groupStartupState.COMPLETED_FAILED;
                        break;

                    case WARNING:
                        // at least one instance started

                        // list differs based on report action result.

                        // list of failed for non SUCCESS result.  List of succeeded for SUCCESS.
                        // this is better approach than parsing report.getMessage() for failed instances.
                        // todo:  get this list to be the members that failed during start-cluster.
                        members = (List<String>)report.getResultType(List.class);


                        groupStartupState = GMSConstants.groupStartupState.COMPLETED_FAILED;
                        break;

                    default:
                }
                try {
                    if (gmsInfo.gms != null) {
                        if (members == null) {
                            members = new LinkedList<String>();
                        }

                        gmsInfo.gms.announceGroupStartup(clusterName, groupStartupState, members);
                    }
                } catch (Throwable t) {
                    // ensure gms group startup announcement does not interfere with starting cluster.
                    logger.log(Level.WARNING, "group.start.exception",
                        t.getLocalizedMessage());
                }
            }
        }
    }

}
