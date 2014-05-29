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

package com.sun.enterprise.admin.cli.remote;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jvnet.hk2.component.*;
import com.sun.enterprise.module.*;
import com.sun.enterprise.module.single.StaticModulesRegistry;

import org.glassfish.api.admin.*;
import org.glassfish.api.admin.CommandModel.ParamModel;
import org.glassfish.common.util.admin.ManPageFinder;

import com.sun.appserv.management.client.prefs.LoginInfo;
import com.sun.appserv.management.client.prefs.LoginInfoStore;
import com.sun.appserv.management.client.prefs.LoginInfoStoreFactory;
import com.sun.appserv.management.client.prefs.StoreException;
import com.sun.enterprise.universal.i18n.LocalStringsImpl;
import com.sun.enterprise.admin.remote.RemoteAdminCommand;
import com.sun.enterprise.admin.cli.*;
import com.sun.enterprise.admin.cli.ProgramOptions.PasswordLocation;
import com.sun.enterprise.admin.util.*;
import com.sun.enterprise.admin.util.CommandModelData.ParamModelData;
import com.sun.enterprise.util.SystemPropertyConstants;

/**
 * A remote command handled by the asadmin CLI.
 */
public class RemoteCommand extends CLICommand {

    private static final LocalStringsImpl   strings =
            new LocalStringsImpl(RemoteCommand.class);

    // return output string rather than printing it
    private boolean                     returnOutput = false;
    private String                      output;
    private boolean                     returnAttributes = false;
    private Map<String, String>         attrs;
    private String                      usage;

    private String                      responseFormatType;
    private OutputStream                userOut;
    private File                        outputDir;

    private CLIRemoteAdminCommand       rac;

    /**
     * A special RemoteAdminCommand that overrides methods so that
     * we can handle the interactive requirements of a CLI command.
     */
    private class CLIRemoteAdminCommand extends RemoteAdminCommand {
        /**
         * Construct a new remote command object.  The command and arguments
         * are supplied later using the execute method in the superclass.
         */
        public CLIRemoteAdminCommand(String name, String host, int port,
                boolean secure, String user, String password, Logger logger,
                String authToken)
                throws CommandException {
            super(name, host, port, secure, user, password, logger, authToken, true /* prohibitDirectoryUploads */);
        }

        /**
         * If we're interactive, prompt for a new username and password.
         * Return true if we're successful in collecting new information
         * (and thus the caller should try the request again).
         */
        @Override
        protected boolean updateAuthentication() {
            Console cons;
            if (programOpts.isInteractive() &&
                    (cons = System.console()) != null) {
                // if appropriate, tell the user why authentication failed
                PasswordLocation pwloc = programOpts.getPasswordLocation();
                if (pwloc == PasswordLocation.PASSWORD_FILE) {
                    logger.fine(strings.get("BadPasswordFromFile",
                                                programOpts.getPasswordFile()));
                } else if (pwloc == PasswordLocation.LOGIN_FILE) {
                    try {
                        LoginInfoStore store =
                            LoginInfoStoreFactory.getDefaultStore();
                        logger.fine(strings.get("BadPasswordFromLogin",
                                        store.getName()));
                    } catch (StoreException ex) {
                        // ignore it
                    }
                }

                String user = null;
                // only prompt for a user name if the user name is set to
                // the default.  otherwise, assume the user specified the
                // correct username to begin with and all we need is the
                // password.
                if (programOpts.getUser() == null) {
                    cons.printf("%s ", strings.get("AdminUserPrompt"));
                    user = cons.readLine();
                    if (user == null)
                        return false;
                }
                String password;
                String puser = ok(user) ? user : programOpts.getUser();
                if (ok(puser))
                    password = readPassword(
                                strings.get("AdminUserPasswordPrompt", puser));
                else
                    password = readPassword(strings.get("AdminPasswordPrompt"));
                if (password == null)
                    return false;
                if (ok(user)) {      // if none entered, don't change
                    programOpts.setUser(user);
                    this.user = user;
                }
                programOpts.setPassword(password, PasswordLocation.USER);
                this.password = password;
                return true;
            }
            return false;
        }

        /**
         * Get from environment.
         */
        @Override
        protected String getFromEnvironment(String name) {
            return env.getStringOption(name);
        }

        /**
         * Called when a non-secure connection attempt fails and it appears
         * that the server requires a secure connection.
         * Tell the user that we're retrying.
         */
        @Override
        protected boolean retryUsingSecureConnection(String host, int port) {
            String msg = strings.get("ServerMaybeSecure", host, port + "");
            logger.info(msg);
            return true;
        }

        /**
         * Return the error message to be used in the AuthenticationException.
         * Subclasses can override to provide a more detailed message, for
         * example, indicating the source of the password that failed.
         */
        @Override
        protected String reportAuthenticationException() {
            String msg = null;
            PasswordLocation pwloc =
                programOpts.getPasswordLocation();
            if (pwloc == PasswordLocation.PASSWORD_FILE) {
                msg = strings.get("InvalidCredentialsFromFile",
                                    programOpts.getUser(),
                                    programOpts.getPasswordFile());
            } else if (pwloc == PasswordLocation.LOGIN_FILE) {
                try {
                    LoginInfoStore store =
                        LoginInfoStoreFactory.getDefaultStore();
                    msg = strings.get("InvalidCredentialsFromLogin",
                                        programOpts.getUser(),
                                        store.getName());
                } catch (StoreException ex) {
                    // ignore it
                }
            }

            if (msg == null)
                msg = strings.get("InvalidCredentials", programOpts.getUser());
            return msg;
        }
    }

    /**
     * A class loader for the "modules" directory.
     */
    private static ClassLoader moduleClassLoader;

    /**
     * A habitat just for finding man pages.
     */
    private static Habitat manHabitat;

    /**
     * Construct a new remote command object.  The command and arguments
     * are supplied later using the execute method in the superclass.
     */
    public RemoteCommand() throws CommandException {
        super();
    }

    /**
     * Construct a new remote command object.  The command and arguments
     * are supplied later using the execute method in the superclass.
     */
    public RemoteCommand(String name, ProgramOptions po, Environment env)
            throws CommandException {
        super(name, po, env);
    }

    /**
     * Construct a new remote command object.  The command and arguments
     * are supplied later using the execute method in the superclass.
     * This variant is used by the RemoteDeploymentFacility class to
     * control and capture the output.
     */
    public RemoteCommand(String name, ProgramOptions po, Environment env,
            String responseFormatType, OutputStream userOut)
            throws CommandException {
        this(name, po, env);
        this.responseFormatType = responseFormatType;
        this.userOut = userOut;
    }

    /**
     * Set the directory in which any returned files will be stored.
     * The default is the user's home directory.
     */
    public void setFileOutputDirectory(File dir) {
        outputDir = dir;
    }

    @Override
    protected void prepare()
            throws CommandException, CommandValidationException  {
        try {
            processProgramOptions();

            initializeAuth();

            /*
             * Now we have all the information we need to create
             * the remote admin command object.
             */
            initializeRemoteAdminCommand();

            if (responseFormatType != null)
                rac.setResponseFormatType(responseFormatType);
            if (userOut != null)
                rac.setUserOut(userOut);

            /*
             * If this is a help request, we don't need the command
             * metadata and we throw away all the other options and
             * fake everything else.
             */
            if (programOpts.isHelp()) {
                commandModel = helpModel();
                rac.setCommandModel(commandModel);
                return;
            }

            /*
             * Find the metadata for the command.
             */
            commandModel = rac.getCommandModel();
        } catch (CommandException cex) {
            logger.finer("RemoteCommand.prepare throws " + cex);
            throw cex;
        } catch (Exception e) {
            logger.finer("RemoteCommand.prepare throws " + e);
            throw new CommandException(e.getMessage());
        }
    }

    /**
     * If it's a help request, don't prompt for any missing options.
     */
    @Override
    protected void validate()
            throws CommandException, CommandValidationException  {
        if (programOpts.isHelp())
            return;
        super.validate();
    }

    /**
     * We do all our help processing in executeCommand.
     */
    @Override
    protected boolean checkHelp()
            throws CommandException, CommandValidationException {
        return false;
    }

    /**
     * Runs the command using the specified arguments.
     */
    @Override
    protected int executeCommand()
            throws CommandException, CommandValidationException {
        try {
            options.set("DEFAULT", operands);
            output = rac.executeCommand(options);
            if (returnAttributes)
                attrs = rac.getAttributes();
            else if (!returnOutput) {
                if (output.length() > 0) {
                    logger.info(output);
                }
            }
        } catch (CommandException ex) {
            // if a --help request failed, try to emulate it locally
            if (programOpts.isHelp()) {
                Reader r = getLocalManPage();
                if (r != null) {
                    try {
                        BufferedReader br = new BufferedReader(r);
                        PrintWriter pw = new PrintWriter(System.out);
                        char[] buf = new char[8192];
                        int cnt;
                        while ((cnt = br.read(buf)) > 0)
                            pw.write(buf, 0, cnt);
                        pw.flush();
                        return SUCCESS;
                    } catch (IOException ioex2) {
                        // ignore it and throw original exception
                    } finally {
                        try {
                            r.close();
                        } catch (IOException ioex3) {
                            // ignore it
                        }
                    }
                }
            }
            throw ex;
        }
        final Map<String,String> racAttrs = rac.getAttributes();
        String returnVal = racAttrs != null ? racAttrs.get("exit-code") : null;
        if(returnVal != null && "WARNING".equals(returnVal))
            return WARNING;
        return SUCCESS;
    }


    /**
     * Execute the command and return the output as a string
     * instead of writing it out.
     */
    public String executeAndReturnOutput(String... args)
            throws CommandException, CommandValidationException {
        /*
         * Tell the low level output processing to just save the output
         * string instead of writing it out.  Yes, this is pretty gross.
         */
        returnOutput = true;
        execute(args);
        returnOutput = false;
        return output;
    }

    /**
     * Execute the command and return the main attributes from the manifest
     * instead of writing out the output.
     */
    public Map<String, String> executeAndReturnAttributes(String... args)
            throws CommandException, CommandValidationException {
        /*
         * Tell the low level output processing to just save the attributes
         * instead of writing out the output.  Yes, this is pretty gross.
         */
        returnAttributes = true;
        execute(args);
        returnAttributes = false;
        return attrs;
    }

    /**
     * Get the usage text.
     * If we got usage information from the server, use it.
     *
     * @return usage text
     */
    public String getUsage() {
        if (usage == null) {
            if (rac == null) {
                /*
                 * We weren't able to initialize the RemoteAdminCommand
                 * object, probably because we failed to parse the program
                 * options.  With no ability to contact the remote server,
                 * we can't provide any command-specific usage information.
                 * Sigh.
                 */
                return strings.get("Usage.asadmin.full", getName());
            }
            usage = rac.getUsage();
        }
        if (usage == null)
            return super.getUsage();

        StringBuilder usageText = new StringBuilder();
        usageText.append(strings.get("Usage", strings.get("Usage.asadmin")));
        usageText.append(" ");
        usageText.append(usage);
        return usageText.toString();
    }

    /**
     * Get the man page from the server.  If the man page isn't
     * available, e.g., because the server is down, try to find
     * it locally by looking in the modules directory.
     */
    public BufferedReader getManPage() {
        try {
            initializeRemoteAdminCommand();
            rac.setCommandModel(helpModel());
            ParameterMap params = new ParameterMap();
            params.set("help", "true");
            String manpage = rac.executeCommand(params);
            return new BufferedReader(new StringReader(manpage));
        } catch (CommandException cex) {
            // ignore
        }

        /*
         * Can't find the man page remotely, try to find it locally.
         * XXX - maybe should only do this on connection failure
         */
        BufferedReader r = getLocalManPage();
        return r != null ? r : super.getManPage();
    }

    /**
     * Return a CommandModel that only includes the --help option.
     */
    private CommandModel helpModel() {
        CommandModelData cm = new CommandModelData(name);
        cm.add(new ParamModelData("help", boolean.class, true, "false", "?"));
        return cm;
    }

    /**
     * Try to find a local version of the man page for this command.
     */
    private BufferedReader getLocalManPage() {
        logger.fine(strings.get("NoRemoteManPage"));
        String cmdClass = getCommandClass(getName());
        ClassLoader mcl = getModuleClassLoader();
        if (cmdClass != null && mcl != null) {
            return ManPageFinder.getCommandManPage(getName(), cmdClass,
                                            Locale.getDefault(), mcl, logger);
        }
        return null;
    }

    private void initializeRemoteAdminCommand() throws CommandException {
        if (rac == null) {
            rac = new CLIRemoteAdminCommand(name,
                programOpts.getHost(), programOpts.getPort(),
                programOpts.isSecure(), programOpts.getUser(),
                programOpts.getPassword(), logger, programOpts.getAuthToken());
            rac.setFileOutputDirectory(outputDir);
            rac.setInteractive(programOpts.isInteractive());;
        }
    }

    private void initializeAuth() throws CommandException {
        LoginInfo li = null;

        try {
            LoginInfoStore store = LoginInfoStoreFactory.getDefaultStore();
            li = store.read(programOpts.getHost(), programOpts.getPort());
            if (li == null)
                return;
        } catch (StoreException se) {
            logger.finer(
                    "Login info could not be read from ~/.asadminpass file");
            return;
        }

        /*
         * If we don't have a user name, initialize it from .asadminpass.
         * In that case, also initialize the password unless it was
         * already specified (overriding what's in .asadminpass).
         *
         * If we already have a user name, and it's the same as what's
         * in .asadminpass, and we don't have a password, use the password
         * from .asadminpass.
         */
        if (programOpts.getUser() == null) {
            // not on command line and in .asadminpass
            logger.finer("Getting user name from ~/.asadminpass: " +
                                        li.getUser());
            programOpts.setUser(li.getUser());
            if (programOpts.getPassword() == null) {
                // not in passwordfile and in .asadminpass
                logger.finer(
                    "Getting password from ~/.asadminpass");
                programOpts.setPassword(li.getPassword(),
                    ProgramOptions.PasswordLocation.LOGIN_FILE);
            }
        } else if (programOpts.getUser().equals(li.getUser())) {
            if (programOpts.getPassword() == null) {
                // not in passwordfile and in .asadminpass
                logger.finer(
                    "Getting password from ~/.asadminpass");
                programOpts.setPassword(li.getPassword(),
                    ProgramOptions.PasswordLocation.LOGIN_FILE);
            }
        }
    }

    /**
     * Given a command name, return the name of the class that implements
     * that command in the server.
     */
    private static String getCommandClass(String cmdName) {
        Habitat h = getManHabitat();
        String cname = "org.glassfish.api.admin.AdminCommand";
        for (Inhabitant<?> command : h.getInhabitantsByContract(cname)) {
            for (String name : Inhabitants.getNamesFor(command, cname)) {
                if (name.equals(cmdName))
                    return command.typeName();
            }
        }
        return null;
    }

    /**
     * Return a Habitat used just for reading man pages from the
     * modules in the modules directory.
     */
    private static Habitat getManHabitat() {
        if (manHabitat != null)
            return manHabitat;
        ModulesRegistry registry =
                new StaticModulesRegistry(getModuleClassLoader());
        manHabitat = registry.createHabitat("default");
        return manHabitat;
    }

    /**
     * Return a ClassLoader that loads classes from all the modules
     * (jar files) in the <INSTALL_ROOT>/modules directory.
     */
    private static ClassLoader getModuleClassLoader() {
        if (moduleClassLoader != null)
            return moduleClassLoader;
        try {
            File installDir = new File(System.getProperty(
                                SystemPropertyConstants.INSTALL_ROOT_PROPERTY));
            File modulesDir = new File(installDir, "modules");
            moduleClassLoader = new DirectoryClassLoader(modulesDir,
                                            CLICommand.class.getClassLoader());
            return moduleClassLoader;
        } catch (IOException ioex) {
            return null;
        }
    }
}
