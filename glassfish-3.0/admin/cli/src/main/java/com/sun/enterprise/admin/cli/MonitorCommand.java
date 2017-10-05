/*
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

package com.sun.enterprise.admin.cli;

import java.io.*;
import java.util.*;
import java.util.logging.*;
import org.jvnet.hk2.annotations.*;
import org.jvnet.hk2.component.*;
import com.sun.enterprise.admin.cli.*;
import com.sun.enterprise.admin.cli.remote.RemoteCommand;
import com.sun.enterprise.admin.cli.util.*;
import com.sun.enterprise.universal.i18n.LocalStringsImpl;

/**
 * A local Monitor Command (this will call the remote 'monitor' command).
 * The reason for having to implement this as local is to interpret the options
 * --interval and --filename(TBD) options.
 * 
 * @author Prashanth
 * @author Bill Shannon
 */
@Service(name = "monitor")
@Scoped(PerLookup.class)
public class MonitorCommand extends CLICommand {

    private int interval = 30 * 1000;    // default 30 seconds
    private String type;
    private String filter;
    private File fileName;

    private static final String INTERVAL = "interval";
    private static final String TYPE = "type";
    private static final String FILTER = "filter";
    private static final String FILENAME = "filename";

    private static final LocalStringsImpl strings =
            new LocalStringsImpl(MonitorCommand.class);

    /**
     */
    @Override
    protected void prepare()
            throws CommandException, CommandValidationException {
        Set<ValidOption> opts = new LinkedHashSet<ValidOption>();
        addOption(opts, TYPE, '\0', "STRING", true, null);
        addOption(opts, FILENAME, '\0', "STRING", false, null);
        addOption(opts, INTERVAL, '\0', "STRING", false, "30");
        addOption(opts, FILTER, '\0', "STRING", false, null);
        addOption(opts, "help", '?', "BOOLEAN", false, "false");
        commandOpts = Collections.unmodifiableSet(opts);
        operandName = "target";
        operandType = "STRING";
        operandMin = 0;
        operandMax = 1;

        processProgramOptions();
    }

    /**
     * The validate method validates that the type and quantity of
     * parameters and operands matches the requirements for this
     * command.  The validate method supplies missing options from
     * the environment.  It also supplies passwords from the password
     * file or prompts for them if interactive.
     */
    @Override
    protected void validate()
            throws CommandException, CommandValidationException {
        super.validate();
        String sinterval = getOption(INTERVAL);
        if (ok(sinterval))
            interval = Integer.parseInt(sinterval) * 1000;
        type = getOption(TYPE);
        filter = getOption(FILTER);
        //fileName = new File(getOption(FILENAME));
    }

    @Override
    protected int executeCommand() 
            throws CommandException, CommandValidationException {
        // Based on interval, loop the subject to print the output
        Timer timer = new Timer();
        try {
            MonitorTask monitorTask = new MonitorTask(timer, getRemoteArgs(),
                            programOpts, env, type, filter, fileName);
            timer.scheduleAtFixedRate(monitorTask, 0, interval);

            boolean done = false;
            // detect if a q or Q key is entered
            while (!done) {
                // The native doesn't exist yet
                //final char c = new CliUtil().getKeyboardInput();
                //final char c = 'p';
                final String str = new BufferedReader(
                                new InputStreamReader(System.in)).readLine(); 
                if (str.equals("q") || str.equals("Q")) {
                    timer.cancel();
                    done = true;
                    String exceptionMessage = monitorTask.getExceptionMessage();
                    if (exceptionMessage != null) {
                        throw new CommandException(exceptionMessage);
                    }
                } else if (str.equals("h") || str.equals("H")) {
                    monitorTask.displayDetails();
                }
            }
        } catch (Exception e) {
            timer.cancel();
            throw new CommandException(
                strings.get("monitorCommand.errorRemote", e.getMessage()));
        }
        return 0;
    }

    private String[] getRemoteArgs() {
        List<String> list = new ArrayList<String>(5);
        list.add("monitor");
 
        if (ok(type)) {
            list.add("--type");
            list.add(type);
        }
        if (ok(filter)) {
            list.add("--filter");
            list.add(filter);
        }
        return list.toArray(new String[list.size()]);
    }
}
