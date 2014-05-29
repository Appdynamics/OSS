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
package com.sun.enterprise.admin.cli.cluster;

import com.sun.enterprise.universal.process.Jps;
import com.sun.enterprise.universal.process.ProcessUtils;
import com.sun.enterprise.util.StringUtils;
import com.sun.enterprise.util.io.FileUtils;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jvnet.hk2.annotations.*;
import org.jvnet.hk2.component.*;
import org.glassfish.api.Param;
import org.glassfish.api.admin.*;

import com.sun.enterprise.admin.cli.*;
import com.sun.enterprise.admin.cli.remote.RemoteCommand;
import com.sun.enterprise.util.ObjectAnalyzer;

/**
 * Delete a local server instance.
 * Wipeout the node dir if it is the last instance under the node
 *
 * Performance Note:  getServerDirs().getServerDir() is all inlined by the JVM
 * because the class instance is immutable.
 *
 * @author Byron Nevins
 */
@Service(name = "delete-local-instance")
@Scoped(PerLookup.class)
public class DeleteLocalInstanceCommand extends LocalInstanceCommand {

    @Param(name = "instance_name", primary = true, optional = true)
    private String instanceName0;

    /** initInstance goes to great lengths to figure out the correct directory structure.
     * We don't care about such errors.  If the dir is not there -- then this is a
     * simple error about trying to delete an instance that doesn't exist...
     * Thank goodness for overriding methods!!
     * @throws CommandException
     */
    @Override
    protected void initInstance() throws CommandException {
        try {
            super.initInstance();
        }
        catch (CommandException e) {
            throw e;
        }
        catch (Exception e) {
            throw new CommandException(Strings.get("DeleteInstance.noInstance"));
        }
    }

    /**
     * We most definitely do not want to create directories for nodes here!!
     * @param f the directory to create
     */
    protected boolean mkdirs(File f) {
        return false;
    }

    @Override
    protected void validate()
            throws CommandException, CommandValidationException {
        instanceName = instanceName0;
        super.validate();
        if (!StringUtils.ok(getServerDirs().getServerName()))
            throw new CommandException(Strings.get("DeleteInstance.noInstanceName"));

        File dasProperties = getServerDirs().getDasPropertiesFile();

        if (dasProperties.isFile()) {
            setDasDefaults(dasProperties);
        }

        if (!getServerDirs().getServerDir().isDirectory())
            throw new CommandException(Strings.get("DeleteInstance.noWhack",
                    getServerDirs().getServerDir()));
    }

    /**
     */
    @Override
    protected int executeCommand()
            throws CommandException, CommandValidationException {
        if (isRunning()) {
            int prevpid = getPrevPid();
            Map<String, Integer> procs = Jps.getProcessTable();
            Set<Map.Entry<String,Integer>> set = procs.entrySet();
            Iterator<Map.Entry<String,Integer>> it = set.iterator();
            while(it.hasNext()) {
                Map.Entry<String,Integer> entry = it.next();
            }
            
            throw new CommandException(Strings.get("DeleteInstance.running"));
        }

        doRemote();
        whackFilesystem();
        return SUCCESS;
    }

    /**
     * Ask DAS to wipe it out from domain.xml
     * If DAS isn't running -- ERROR -- return right away with a thrown Exception
     * If DAS is running, and instance not registered on DAS, do not unregister instance -- OK
     * If DAS is running, and instance is registered on DAS, then unregister instance -- OK
     *      - If _unregister-instance is successful - OK
     *      - If _unregister-instance fails - ERROR - Exception thrown
     */
    private void doRemote() throws CommandException {
        if (!isDASRunning()) {
            String newString = Strings.get("DeleteInstance.remoteError",
                    programOpts.getHost(), "" + programOpts.getPort());
            throw new CommandException(newString);
        }

        if (isRegisteredToDas()) {
            RemoteCommand rc = new RemoteCommand("_unregister-instance", programOpts, env);
            rc.execute("_unregister-instance",
                    "--node", getServerDirs().getServerParentDir().getName(),
                    getServerDirs().getServerName());
        }
    }

    private boolean isDASRunning() {
        try {
            getUptime();
            return true;
        } catch (CommandException ex) {
            return false;
        }
    }

    /**
     * If the instance is not registered on DAS (server xml entry doesn't exist 
     * in domain.xml), the get command will throw a CommandException
     */
    private boolean isRegisteredToDas() {
        boolean isRegistered = false;
        RemoteCommand rc = null;
        String INSTANCE_DOTTED_NAME = "servers.server." + instanceName;
        try {
            rc = new RemoteCommand("get", this.programOpts, this.env);
            rc.executeAndReturnOutput("get", INSTANCE_DOTTED_NAME);
            isRegistered = true;
        } catch (CommandException ce) {
            isRegistered = false;
        }
        return isRegistered;
    }
}
