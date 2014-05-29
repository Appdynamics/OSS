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

package com.sun.enterprise.admin.cli;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import org.glassfish.api.Param;
import org.jvnet.hk2.annotations.*;
import org.jvnet.hk2.component.*;
import org.glassfish.api.admin.*;
import com.sun.enterprise.admin.cli.remote.*;
import com.sun.enterprise.universal.i18n.LocalStringsImpl;

/**
 * THe restart-domain command.
 * The local portion of this command is only used to block until:
 * <ul><li>the old server dies
 * <li>the new server starts
 * </ul>
 * Tactics:
 * <ul>
 * <li>Get the uptime for the current server
 * <li>start the remote Restart command
 * <li>Call uptime in a loop until the uptime number is less than
 * the original uptime
 *
 * @author bnevins
 * @author Bill Shannon
 */
@Service(name = "restart-domain")
@Scoped(PerLookup.class)
public class RestartDomainCommand extends StopDomainCommand {

    @Param(name = "debug", optional = true)
    private Boolean debug;

    @Inject
    private Habitat habitat;
    private static final LocalStringsImpl strings =
            new LocalStringsImpl(RestartDomainCommand.class);

    /**
     * Execute the restart-domain command.
     */
    @Override
    protected void doCommand()
            throws CommandException {
        
        if(!isRestartable())
            throw new CommandException(Strings.get("restartDomain.notRestartable"));

        // find out how long the server has been up
        long uptimeOldServer = getUptime();  // may throw CommandException

        // run the remote restart-domain command and throw away the output
        RemoteCommand cmd =
                new RemoteCommand("restart-domain", programOpts, env);
        String oldpw = programOpts.getPassword();


        File pwFile = getServerDirs().getLocalPasswordFile();
        long stamp = -1;

        if (pwFile != null)
            stamp = pwFile.lastModified();

        if (debug != null)
            cmd.executeAndReturnOutput("restart-domain", "--debug", debug.toString());
        else
            cmd.executeAndReturnOutput("restart-domain");

        waitForRestart(pwFile, stamp, uptimeOldServer);

        logger.info(strings.get("restartDomain.success"));
    }

    /**
     * If the server isn't running, try to start it.
     */
    @Override
    protected int dasNotRunning(boolean local) throws CommandException {
        if (!local)
            throw new CommandException(
                Strings.get("restart.dasNotRunningNoRestart"));
        logger.warning(strings.get("restart.dasNotRunning"));
        CLICommand cmd = habitat.getComponent(CLICommand.class, "start-domain");
        /*
         * Collect the arguments that also apply to start-domain.
         * The start-domain CLICommand object will already have the
         * ProgramOptions injected into it so we don't need to worry
         * about them here.
         *
         * Usage: asadmin [asadmin-utility-options] start-domain
         *      [-v|--verbose[=<verbose(default:false)>]]
         *      [--upgrade[=<upgrade(default:false)>]]
         *      [--debug[=<debug(default:false)>]] [--domaindir <domaindir>]
         *      [-?|--help[=<help(default:false)>]] [domain_name]
         *
         * Only --debug, --domaindir, and the operand apply here.
         */
        List<String> opts = new ArrayList<String>();
        opts.add("start-domain");
        if (debug != null) {
            opts.add("--debug");
            opts.add(debug.toString());
        }
        if (domainDirParam != null) {
            opts.add("--domaindir");
            opts.add(domainDirParam);
            // XXX - would this be better?
            //opts.add(getDomainRootDir().toString());
        }
        if (getDomainName() != null)
            opts.add(getDomainName());

        return cmd.execute(opts.toArray(new String[opts.size()]));
    }
}
