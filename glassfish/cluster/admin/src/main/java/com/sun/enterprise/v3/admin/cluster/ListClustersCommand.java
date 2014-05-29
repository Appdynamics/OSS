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

package com.sun.enterprise.v3.admin.cluster;

import com.sun.enterprise.admin.util.InstanceStateService;
import com.sun.enterprise.config.serverbeans.*;
import com.sun.enterprise.util.cluster.InstanceInfo;
import com.sun.enterprise.util.StringUtils;
import org.glassfish.api.ActionReport;
import org.glassfish.api.I18n;
import org.glassfish.api.Param;
import org.glassfish.api.admin.AdminCommand;
import org.glassfish.api.admin.AdminCommandContext;
import org.glassfish.api.admin.CommandLock;
import org.glassfish.api.admin.InstanceState;
import org.glassfish.api.admin.ServerEnvironment;
import org.glassfish.api.admin.config.ReferenceContainer;
import org.jvnet.hk2.annotations.Inject;
import org.jvnet.hk2.annotations.Scoped;
import org.jvnet.hk2.annotations.Service;
import org.jvnet.hk2.component.Habitat;
import org.jvnet.hk2.component.PerLookup;
import org.jvnet.hk2.component.PostConstruct;

import java.util.List;
import java.util.LinkedList;
import java.util.logging.Logger;
import static com.sun.enterprise.v3.admin.cluster.Constants.*;
import com.sun.enterprise.admin.util.RemoteInstanceCommandHelper;

/**
 *  This is a remote command that lists the clusters.
 * Usage: list-clusters

 * @author Bhakti Mehta
 * @author Byron Nevins
 */
@Service(name = "list-clusters")
@Scoped(PerLookup.class)
@CommandLock(CommandLock.LockType.NONE)
@I18n("list.clusters.command")
public final class ListClustersCommand implements AdminCommand, PostConstruct {

    @Inject
    private Habitat habitat;
    @Inject
    Domain domain;
    @Inject
    InstanceStateService stateService;
    private RemoteInstanceCommandHelper helper;

    private static final String NONE = "Nothing to list.";
    private static final String EOL = "\n";

    @Param(optional = true, primary = true, defaultValue = "domain")
    String whichTarget;

    @Inject
    private Clusters allClusters;

    private ActionReport report ;

    @Override
    public void postConstruct() {
        helper = new RemoteInstanceCommandHelper(habitat);
    }

    public void execute(AdminCommandContext context) {
        report = context.getActionReport();
        report.setActionExitCode(ActionReport.ExitCode.SUCCESS);
        Logger logger = context.getLogger();
        ActionReport.MessagePart top = report.getTopMessagePart();

        List<Cluster> clusterList = null;
        //Fix for issue 13057 list-clusters doesn't take an operand
        //defaults to domain
        if (whichTarget.equals("domain" )) {
            Clusters clusters = domain.getClusters();
            clusterList = clusters.getCluster();
        } else {

            clusterList = createClusterList();

            if (clusterList == null) {
                fail(Strings.get("list.instances.badTarget", whichTarget));
                return;
            }
        }
        StringBuilder sb = new StringBuilder();
        if (clusterList.size() < 1) {
            sb.append(NONE);
        }


        int timeoutInMsec = 2000;

        //List the cluster and also the state
        //A cluster is a three-state entity and
        //list-cluster should return one of the following:

        //running (all instances running)
        //not running (no instance running)
        //partially running (at least 1 instance is not running)


        for (Cluster cluster : clusterList) {
            boolean atleastOneInstanceRunning = false;
            boolean allInstancesRunning = true;
            List<InstanceInfo> infos = new LinkedList<InstanceInfo>();
            String clusterName = cluster.getName();

            List<Server> servers = cluster.getInstances();

            for (Server server : servers) {
                String name = server.getName();

                if (name != null) {
                    ActionReport tReport = habitat.getComponent(ActionReport.class, "html");
                    InstanceInfo ii = new InstanceInfo(
                            habitat, server, helper.getAdminPort(server), server.getAdminHost(),
                            clusterName, logger, timeoutInMsec, tReport, stateService);
                    infos.add(ii);
                }
            }
            for(InstanceInfo ii : infos) {
                InstanceState.StateType state = (ii.isRunning()) ?
                        (stateService.setState(ii.getName(), InstanceState.StateType.RUNNING, false)) :
                        (stateService.setState(ii.getName(), InstanceState.StateType.NO_RESPONSE, false));
                allInstancesRunning &= ii.isRunning();
                if (ii.isRunning()) {
                    atleastOneInstanceRunning = true;
                }
            }

            String display;
            String value;

            if (servers.isEmpty() || !atleastOneInstanceRunning) {
                display = InstanceState.StateType.NOT_RUNNING.getDisplayString();
                value = InstanceState.StateType.NOT_RUNNING.getDescription();
            }
            else if (allInstancesRunning) {
                display = InstanceState.StateType.RUNNING.getDisplayString();
                value = InstanceState.StateType.RUNNING.getDescription();
            }
            else {
                display = PARTIALLY_RUNNING_DISPLAY;
                value = PARTIALLY_RUNNING;
            }

            sb.append(clusterName).append(display).append(EOL);
            top.addProperty(clusterName, value);
        }
        String output = sb.toString();
        //Fix for isue 12885
        report.setMessage(output.substring(0,output.length()-1 ));
    }

    /*
    * if target was junk then return all the clusters
    */
    private List<Cluster> createClusterList() {
        // 1. no whichTarget specified
        if (!StringUtils.ok(whichTarget))
            return allClusters.getCluster();

        ReferenceContainer rc = domain.getReferenceContainerNamed(whichTarget);
        // 2. Not a server or a cluster. Could be a config or a Node
        if (rc == null) {
            return getClustersForNodeOrConfig();
        }
        else if (rc.isServer()) {
            Server s =((Server) rc);
            List<Cluster> cl = new LinkedList<Cluster>();
            cl.add(s.getCluster());
            return  cl;
        }
        else if (rc.isCluster()) {
            Cluster cluster = (Cluster) rc;
            List<Cluster> cl = new LinkedList<Cluster>();
            cl.add(cluster);
            return cl;
        }
        else
            return null;
    }

     private List<Cluster> getClustersForNodeOrConfig() {
        if (whichTarget == null)
            throw new NullPointerException("impossible!");

        List<Cluster> list = getClustersForNode();

        if (list == null)
            list = getClustersForConfig();

        return list;
    }

     private List<Cluster> getClustersForNode() {
        boolean foundNode = false;
        Nodes nodes = domain.getNodes();

        if (nodes != null) {
            List<Node> nodeList = nodes.getNode();
            if (nodeList != null) {
                for (Node node : nodeList) {
                    if (whichTarget.equals(node.getName())) {
                        foundNode = true;
                        break;
                    }
                }
            }
        }
         if (!foundNode)
             return null;
         else
             return domain.getClustersOnNode(whichTarget);
    }

     private List<Cluster> getClustersForConfig() {
        Config config = domain.getConfigNamed(whichTarget);

        if (config == null)
            return null;

        List<ReferenceContainer> rcs = domain.getReferenceContainersOf(config);
        List<Cluster> clusters = new LinkedList<Cluster>();

        for (ReferenceContainer rc : rcs)
            if (rc.isCluster())
                clusters.add((Cluster) rc);

        return clusters;
    }

     private void fail(String s) {
        report.setActionExitCode(ActionReport.ExitCode.FAILURE);
        report.setMessage(s);
    }
}
