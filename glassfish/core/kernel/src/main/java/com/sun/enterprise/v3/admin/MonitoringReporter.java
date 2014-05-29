/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2011 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.enterprise.v3.admin;

import com.sun.enterprise.admin.util.ClusterOperationUtil;
import com.sun.enterprise.config.serverbeans.*;
import com.sun.enterprise.config.serverbeans.Domain;
import static com.sun.enterprise.util.StringUtils.ok;
import com.sun.enterprise.v3.common.ActionReporter;
import com.sun.enterprise.v3.common.PlainTextActionReporter;
import com.sun.enterprise.v3.common.PropsFileActionReporter;
import java.lang.reflect.Proxy;
import java.util.*;
import org.glassfish.api.ActionReport;
import org.glassfish.api.admin.*;
import org.glassfish.api.admin.ExecuteOn;
import org.glassfish.api.admin.RuntimeType;
import org.glassfish.external.statistics.Statistic;
import org.glassfish.external.statistics.Stats;
import org.glassfish.external.statistics.impl.StatisticImpl;
import org.glassfish.internal.api.*;
import org.jvnet.hk2.component.*;
import static org.glassfish.api.ActionReport.ExitCode.FAILURE;
import static org.glassfish.api.ActionReport.ExitCode.SUCCESS;
import org.glassfish.api.admin.AdminCommandContext;
import org.glassfish.flashlight.MonitoringRuntimeDataRegistry;
import org.jvnet.hk2.annotations.*;
import org.jvnet.hk2.component.PerLookup;
import static com.sun.enterprise.util.SystemPropertyConstants.MONDOT;
import static com.sun.enterprise.util.SystemPropertyConstants.SLASH;


/**
 *
 * @author Byron Nevins
 * First breathed life on November 6, 2010
 * The copyright says 1997 because one method in here has code moved verbatim
 * from GetCommand.java which started life in 1997
 *
 * Note: what do you suppose is the worst possible name for a TreeNode class?
 * Correct!  TreeNode!  Clashing names is why we have to explicitly use this ghastly
 * name:  org.glassfish.flashlight.datatree.TreeNode all over the place...
 */
@Service(name = "MonitoringReporter")
@Scoped(PerLookup.class)
@ExecuteOn({RuntimeType.DAS, RuntimeType.INSTANCE})
public class MonitoringReporter extends V2DottedNameSupport {

    public enum OutputType {

        GET, LIST
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("\nPattern=[").append(pattern).append("]").append('\n');

        if (!targets.isEmpty()) {
            for (Server server : targets) {
                if (server != null)
                    sb.append("Server=[").append(server.getName()).append("]").append('\n');
            }
        }
        else
            sb.append("No Targets");

        return sb.toString();
    }
    ///////////////////////////////////////////////////////////////////////
    ////////////////////////  The API Methods  ///////////////////////////
    ///////////////////////////////////////////////////////////////////////

    public void prepareGet(AdminCommandContext c, String arg) {
        prepare(c, arg, OutputType.GET);
    }

    public void prepareList(AdminCommandContext c, String arg) {
        prepare(c, arg, OutputType.LIST);
    }

    public void execute() {
        // TODO remove?  make it an exception???
        if (hasError())
            return;

        runLocally();
        runRemotely();
    }

    ///////////////////////////////////////////////////////////////////////
    ////////////////////////  ALL PRIVATE BELOW ///////////////////////////
    ///////////////////////////////////////////////////////////////////////
    private void prepare(AdminCommandContext c, String arg, OutputType type) {
        outputType = type;
        context = c;
        prepareReporter();
        // DAS runs the show on this command.  If we are running in an
        // instance -- that means we should call runLocally() AND it also
        // means that the pattern is already perfect!

        if (isDas())
            prepareDas(arg);
        else
            prepareInstance(arg);
    }

    /**
     * The stock ActionReport we get is too inefficient.  Replace it with PlainText
     * note that we might be called with HTML or XML or JSON or others!
     */
    private void prepareReporter() {
        reporter = (ActionReporter) context.getActionReport();

        if (reporter instanceof PlainTextActionReporter) {
            // already setup correctly - don't change it!!
            plainReporter = (PlainTextActionReporter) reporter;
        }
        else if (reporter instanceof PropsFileActionReporter) {
            plainReporter = new PlainTextActionReporter();
            reporter = plainReporter;
            context.setActionReport(plainReporter);
        }
        else {
            plainReporter = null;
        }
    }

    private void prepareDas(String arg) {
        // TODO throw an exception if any errors????
        try {
            setSuccess();
            userarg = arg;

            if (!validate())
                return;
        }
        catch (Exception e) {
            setError(Strings.get("admin.get.monitoring.unknown", e.getMessage()));
            reporter.setFailureCause(e);
        }
    }

    private void prepareInstance(String arg) {
        // TODO throw an exception if any errors!
        pattern = arg;
    }

    // mostly just copied over from old "get" implementation
    // That's why it is excruciatingly unreadable...
    private void runLocally() {

        // don't run if this is DAS **and** DAS is not in the server list.
        // otherwise we are in an instance and definitely want to run!
        if (isDas() && !dasIsInList())
            return;

        // say the pattern is "something" -->
        // we want "server.something" for DAS and "i1.server.something" for i1
        // Yes -- this is difficult to get perfect!!!  What if user entered
        //"server.something"?

        String localPattern = prependServerDot(pattern);
        org.glassfish.flashlight.datatree.TreeNode tn = datareg.get(serverEnv.getInstanceName());

        if (tn == null) {
            // No monitoring data, so nothing to list
            // officially this is considered a "success"
            setSuccess(Strings.get("admin.get.monitoring.empty"));
            return;
        }

        List<org.glassfish.flashlight.datatree.TreeNode> ltn = tn.getNodes(localPattern);
        boolean singleStat = false;

        if (ltn == null || ltn.isEmpty()) {
            org.glassfish.flashlight.datatree.TreeNode parent = tn.getPossibleParentNode(localPattern);

            if (parent != null) {
                ltn = new ArrayList<org.glassfish.flashlight.datatree.TreeNode>(1);
                ltn.add(parent);
                singleStat = true;
            }
        }

        if (!singleStat)
            localPattern = null; // signal to method call below.  localPattern was already used above...

        if (outputType == OutputType.GET)
            doGet(localPattern, ltn);
        else if (outputType == OutputType.LIST)
            doList(localPattern, ltn);

        if (plainReporter != null) {
            plainReporter.appendMessage(cliOutput.toString());
        }
    }

    // Byron Nevins -- copied from original implementation
    private void doGet(String localPattern, List<org.glassfish.flashlight.datatree.TreeNode> ltn) {
        TreeMap map = new TreeMap();

        for (org.glassfish.flashlight.datatree.TreeNode tn1 : sortTreeNodesByCompletePathName(ltn)) {
            if (!tn1.hasChildNodes()) {
                insertNameValuePairs(map, tn1, localPattern);
            }
        }

        ActionReport.MessagePart topPart = reporter.getTopMessagePart();
        Iterator it = map.keySet().iterator();

        while (it.hasNext()) {
            Object obj = it.next();
            String line = obj.toString();
            line = line.replace(SLASH, "/") + " = " + map.get(obj);

            if (plainReporter != null)
                cliOutput.append(line).append('\n');
            else {
                ActionReport.MessagePart part = topPart.addChild();
                part.setMessage(line);
            }
        }
        setSuccess();
    }

    private void doList(String localPattern, List<org.glassfish.flashlight.datatree.TreeNode> ltn) {
        // list means only print things that have children.  Don't print the children.
        ActionReport.MessagePart topPart = reporter.getTopMessagePart();

        for (org.glassfish.flashlight.datatree.TreeNode tn1 : ltn) {
            if (tn1.hasChildNodes()) {
                String line = tn1.getCompletePathName();

                if (plainReporter != null)
                    cliOutput.append(line).append('\n');
                else {
                    ActionReport.MessagePart part = topPart.addChild();
                    part.setMessage(line);
                }
            }
        }
        setSuccess();
    }

    /**
     * This can be a bit confusing.  It is sort of like a recursive call.
     * GetCommand will be called on the instance.  BUT -- the pattern arg will
     * just have the actual pattern -- the target name will NOT be in there!
     * So "runLocally" will be called on the instance.  this method will ONLY
     * run on DAS (guaranteed!)
     */
    private void runRemotely() {
        if (!isDas())
            return;

        List<Server> remoteServers = getRemoteServers();

        if (remoteServers.isEmpty())
            return;

        try {
            ParameterMap paramMap = new ParameterMap();
            paramMap.set("monitor", "true");
            paramMap.set("DEFAULT", pattern);
            ClusterOperationUtil.replicateCommand("get", FailurePolicy.Error, FailurePolicy.Warn, remoteServers,
                    context, paramMap, habitat);
        }
        catch (Exception ex) {
            setError(Strings.get("admin.get.monitoring.remote.error", getNames(remoteServers)));
        }
    }

    private String prependServerDot(String s) {
        // note -- we are now running in either DAS or an instance and we are going to gather up
        // data ONLY for this server.  I.e. the DAS dispatching has already happened.
        // we really need this pattern to start with the instance-name (DAS's instance-name is "server"

        // Issue#15054
        // this is pretty intricate but this is what we want to happen for these samples:
        // asadmin get -m network.thread-pool.totalexecutedtasks-count ==> ERROR no target
        // asadmin get -m server.network.thread-pool.totalexecutedtasks-count ==> OK, return DAS's data
        // asadmin get -m *.network.thread-pool.totalexecutedtasks-count ==> OK return DAS and instances' data
        // asadmin get -m i1.network.thread-pool.totalexecutedtasks-count ==> OK return data for i1

        final String namedot = serverEnv.getInstanceName() + ".";

        if(s.startsWith(namedot))
            return s;

        return namedot + s;
    }

    private boolean validate() {
        if (datareg == null) {
            setError(Strings.get("admin.get.no.monitoring"));
            return false;
        }

        if (!initPatternAndTargets())
            return false;

        return true;
    }

    /*
     * VERY VERY complicated to get this right!
     */
    private boolean initPatternAndTargets() {
        Server das = domain.getServerNamed("server");
        String targetName = null;

        // no DAS in here!
        List<Server> allServers = targetService.getAllInstances();

        allServers.add(das);

        // 0 decode special things
        // \\ == literal backslash and \ is escaping next char
        userarg = handleEscapes(userarg); // too complicated to do in-line

        // MONDOT, SLASH should be replaced with literals
        userarg = userarg.replace(MONDOT, ".").replace(SLASH, "/");

        // 1.  nothing
        // 2.  *
        // 3.  *.   --> which is a weird input but let's accept it anyway!
        // 4   .   --> very weird but we'll take it
        if (!ok(userarg)
                || userarg.equals("*")
                || userarg.equals(".")
                || userarg.equals("*.")) {
            // By definition this means ALL servers and ALL data
            targets = allServers;
            pattern = "*";
            return true;
        }

        // 5.   *..
        // 6.   *.<something>
        if (userarg.startsWith("*.")) {
            targets = allServers;

            // note: it can NOT be just "*." -- there is something at posn #2 !!
            pattern = userarg.substring(2);

            // "*.." is an error
            if (pattern.startsWith(".")) {
                String specificError = Strings.get("admin.get.monitoring.nodoubledot");
                setError(Strings.get("admin.get.monitoring.invalidpattern", specificError));
                return false;
            }
            return true;
        }

        // 7.  See 14685 for an example -->  "*jsp*"
        if (userarg.startsWith("*")) {
            targets = allServers;
            pattern = userarg.substring(1);
            return true;
        }

        // Another example:
        // servername*something*
        // IT 14778
        // note we will NOT support serv*something getting resolved to server*something
        // that's too crazy.  They have to enter a reasonable name

        // we are looking for, e.g. instance1*foo.goo*
        // target is instance1  pattern is *foo.goo*
        // instance1.something is handled below
        String re = "[^\\.]+\\*.*";

        if (userarg.matches(re)) {
            int index = userarg.indexOf("*");

            if (index < 0) { // can't happen!!
                setError(Strings.get("admin.get.monitoring.invalidtarget", userarg));
                return false;
            }
            targetName = userarg.substring(0, index);
            pattern = userarg.substring(index);
        }

        if (targetName == null) {
            int index = userarg.indexOf(".");

            if (index >= 0) {
                targetName = userarg.substring(0, index);

                if (userarg.length() == index + 1) {
                    // 8. <servername>.
                    pattern = "*";
                }
                else
                    // 9. <servername>.<pattern>
                    pattern = userarg.substring(index + 1);
            }
            else {
                // no dots in userarg
                // 10. <servername>
                targetName = userarg;
                pattern = "*";
            }
        }

        // note that "server" is hard-coded everywhere in GF code.  We're stuck with it!!

        if (targetName.equals("server") || targetName.equals("server-config")) {
            targets.add(das);
            return true;
        }

        // targetName is either 1 or more instances or garbage!
        targets = targetService.getInstances(targetName);

        if (targets.isEmpty()) {
            setError(Strings.get("admin.get.monitoring.invalidtarget", userarg));
            return false;
        }

        return true;
    }

    private void insertNameValuePairs(
            TreeMap map, org.glassfish.flashlight.datatree.TreeNode tn1, String exactMatch) {
        String name = tn1.getCompletePathName();
        Object value = tn1.getValue();
        if (tn1.getParent() != null) {
            map.put(tn1.getParent().getCompletePathName() + DOTTED_NAME,
                    tn1.getParent().getCompletePathName());
        }
        if (value instanceof Stats) {
            for (Statistic s : ((Stats) value).getStatistics()) {
                String statisticName = s.getName();
                if (statisticName != null) {
                    statisticName = s.getName().toLowerCase();
                }
                addStatisticInfo(s, name + "." + statisticName, map);
            }
        }
        else if (value instanceof Statistic) {
            addStatisticInfo(value, name, map);
        }
        else {
            map.put(name, value);
        }

        // IT 8985 bnevins
        // Hack to get single stats.  The code above above would take a lot of
       // time to unwind.  For development speed we just remove unwanted items
        // after the fact...
        if (exactMatch != null) {
            NameValue nv = getIgnoreBackslash(map, exactMatch);
            map.clear();

            if (nv != null) {
                map.put(nv.name, nv.value);
            }
        }
    }

    /*
     * bnevins, 1-11-11
     * Note that we can not GUESS where to put the backslash into 'pattern'.
     * If so -- we could simply add it into pattern and do a get on the HashMap.
     * Instead we have to get each and every key in the map, remove backslashes
     * and compare.
     */
    private NameValue getIgnoreBackslash(TreeMap map, String pattern) {

        if(pattern == null)
            return null;

        Object match = map.get(pattern);

        if(match != null)
            return new NameValue(pattern, match);

        pattern = pattern.replace("\\", "");
        match = map.get(pattern);

        if(match != null)
            return new NameValue(pattern, match);

        // No easy match...

        Set<Map.Entry> elems = map.entrySet();

        for(Map.Entry elem : elems) {
            String key = elem.getKey().toString();

            if(key == null)
                continue;

            String name = key.replace("\\", "");

            if(pattern.equals(name))
                return new NameValue(key, elem.getValue());
        }
        return null;
    }

    private void addStatisticInfo(Object value, String name, TreeMap map) {
        Map<String, Object> statsMap;
        // Most likely we will get the proxy of the StatisticImpl,
        // reconvert that so you can access getStatisticAsMap method
        if (Proxy.isProxyClass(value.getClass())) {
            statsMap = ((StatisticImpl) Proxy.getInvocationHandler(value)).getStaticAsMap();
        }
        else {
            statsMap = ((StatisticImpl) value).getStaticAsMap();
        }
        for (String attrName : statsMap.keySet()) {
            Object attrValue = statsMap.get(attrName);
            map.put(name + "-" + attrName, attrValue);
        }
    }

    private void setError(String msg) {
        reporter.setActionExitCode(FAILURE);
        appendStatusMessage(msg);
        clear();
    }

    private void setSuccess() {
        reporter.setActionExitCode(SUCCESS);
    }

    private void setSuccess(String msg) {
        setSuccess();
        appendStatusMessage(msg);
    }

    private void appendStatusMessage(String newMessage) {
        if (plainReporter != null)
            cliOutput.append(newMessage).append('\n');
        else {
            String oldMessage = reporter.getMessage();

            if (oldMessage == null)
                reporter.setMessage(newMessage);
            else
                reporter.appendMessage("\n" + newMessage);
        }
    }

    private boolean hasError() {
        //return reporter.hasFailures();
        return reporter.getActionExitCode() == FAILURE;
    }

    private void clear() {
        targets = Collections.emptyList();
        pattern = "";
    }

    private List<Server> getRemoteServers() {
        // only call on DAS !!!
        if (!isDas())
            throw new RuntimeException("Internal Error"); // todo?

        List<Server> notdas = new ArrayList<Server>(targets.size());
        String dasName = serverEnv.getInstanceName();

        for (Server server : targets) {
            if (!dasName.equals(server.getName()))
                notdas.add(server);
        }

        return notdas;
    }

    private boolean dasIsInList() {
        return getRemoteServers().size() != targets.size();
    }

    private String getNames(List<Server> list) {
        boolean first = true;
        String ret = "";

        for (Server server : list) {
            if (first)
                first = false;
            else
                ret += ", ";

            ret += server.getName();
        }
        return ret;
    }

    private static String handleEscapes(String s) {
        // replace double backslash with backslash
        // simply remove single backslash
        // there is probably a much better, and very very complicated way to do
        // this with regexp.  I don't care - it is only done once for each time
        // a user runs a get -m comand.
        final String UNLIKELY_STRING = "___~~~~$$$$___";
        return s.replace("\\\\", UNLIKELY_STRING).replace("\\", "").replace(UNLIKELY_STRING, "\\");
    }

    private boolean isDas() {
        return serverEnv.isDas();
    }

    /*
     * Surprise!  The variables are down here.  All the variables are private.
     * That means they are an implementation detail and are hidden at the bottom
     * of the file.
     */
    List<Server> targets = new ArrayList<Server>();
    private PlainTextActionReporter plainReporter;
    private ActionReporter reporter;
    private AdminCommandContext context;
    private String pattern;
    private String userarg;
    @Inject(optional = true)
    private MonitoringRuntimeDataRegistry datareg;
    @Inject
    private Domain domain;
    @Inject
    private Target targetService;
    @Inject
    ServerEnvironment serverEnv;
    @Inject
    Habitat habitat;
    private OutputType outputType;
    private final static String DOTTED_NAME = ".dotted-name";
    private final StringBuilder cliOutput = new StringBuilder();

    private static class NameValue {
        String name;
        Object value;

        private NameValue(String s, Object o) {
            name = s;
            value = o;
        }
    }
}
