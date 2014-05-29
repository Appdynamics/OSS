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
package com.sun.enterprise.v3.admin;

import com.sun.enterprise.admin.util.ClusterOperationUtil;
import com.sun.enterprise.admin.util.InstanceStateService;
import com.sun.enterprise.config.serverbeans.Domain;
import com.sun.enterprise.config.serverbeans.Cluster;
import com.sun.enterprise.module.common_impl.LogHelper;

import java.io.*;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.locks.Lock;

import org.glassfish.admin.payload.PayloadFilesManager;

import org.glassfish.api.ActionReport;
import org.glassfish.api.Async;
import org.glassfish.api.Param;
import org.glassfish.api.admin.*;
import org.glassfish.common.util.admin.CommandModelImpl;
import org.glassfish.common.util.admin.MapInjectionResolver;
import org.glassfish.common.util.admin.UnacceptableValueException;
import org.glassfish.common.util.admin.ManPageFinder;
import org.glassfish.config.support.CommandTarget;
import org.glassfish.config.support.GenericCrudCommand;
import org.glassfish.config.support.TargetType;
import org.glassfish.internal.api.*;

import org.jvnet.hk2.annotations.Inject;
import org.jvnet.hk2.annotations.Service;
import org.jvnet.hk2.annotations.Scoped;
import org.jvnet.hk2.component.*;
import com.sun.hk2.component.InjectionResolver;

import com.sun.enterprise.universal.collections.ManifestUtils;
import com.sun.enterprise.universal.glassfish.AdminCommandResponse;
import com.sun.enterprise.util.LocalStringManagerImpl;
import com.sun.enterprise.v3.common.XMLContentActionReporter;
import com.sun.logging.LogDomains;

/**
 * Encapsulates the logic needed to execute a server-side command (for example,
 * a descendant of AdminCommand) including injection of argument values into the
 * command.
 *
 * @author dochez
 * @author tjquinn
 * @author Bill Shannon
 */
@Service
public class CommandRunnerImpl implements CommandRunner {

    private static final Logger logger =
            LogDomains.getLogger(CommandRunnerImpl.class, LogDomains.ADMIN_LOGGER);
    private final InjectionManager injectionMgr = new InjectionManager();
    @Inject
    private Habitat habitat;
    @Inject
    private ServerContext sc;
    @Inject
    private Domain domain;
    @Inject
    private ServerEnvironment serverEnv;
    @Inject
    private ProcessEnvironment processEnv;
    @Inject
    private InstanceStateService state;
    @Inject
    private AdminCommandLock adminLock;
    private static final String ASADMIN_CMD_PREFIX = "AS_ADMIN_";
    private static final LocalStringManagerImpl adminStrings =
            new LocalStringManagerImpl(CommandRunnerImpl.class);

    /**
     * Returns an initialized ActionReport instance for the passed type or
     * null if it cannot be found.
     *
     * @param name actiopn report type name
     * @return uninitialized action report or null
     */
    public ActionReport getActionReport(String name) {
        return habitat.getComponent(ActionReport.class, name);
    }

    /**
     * Retuns the command model for a command name.
     *
     * @param commandName command name
     * @param logger logger to log any error messages
     * @return model for this command (list of parameters,etc...),
     *          or null if command is not found
     */
    public CommandModel getModel(String commandName, Logger logger) {
        AdminCommand command = null;
        try {
            command = habitat.getComponent(AdminCommand.class, commandName);
        } catch (ComponentException e) {
            logger.log(Level.SEVERE, "Cannot instantiate " + commandName, e);
            return null;
        }
        return command == null ? null : getModel(command);
    }

    /**
     * Obtain and return the command implementation defined by
     * the passed commandName.
     *
     * @param commandName command name as typed by users
     * @param report report used to communicate command status back to the user
     * @param logger logger to log
     * @return command registered under commandName or null if not found
     */
    public AdminCommand getCommand(String commandName, ActionReport report,
            Logger logger) {

        AdminCommand command = null;
        try {
            command = habitat.getComponent(AdminCommand.class, commandName);
        } catch (ComponentException e) {
            e.printStackTrace();
            report.setFailureCause(e);
        }
        if (command == null) {
            String msg;

            if (!ok(commandName)) {
                msg = adminStrings.getLocalString("adapter.command.nocommand",
                        "No command was specified.");
            } else {
                // this means either a non-existent command or
                // an ill-formed command
                if (habitat.getInhabitant(AdminCommand.class, commandName)
                        == null) // somehow it's in habitat
                {
                    msg = adminStrings.getLocalString("adapter.command.notfound", "Command {0} not found", commandName);
                } else {
                    msg = adminStrings.getLocalString("adapter.command.notcreated",
                            "Implementation for the command {0} exists in "
                            + "the system, but it has some errors, "
                            + "check server.log for details", commandName);
                }
            }
            report.setMessage(msg);
            report.setActionExitCode(ActionReport.ExitCode.FAILURE);
            LogHelper.getDefaultLogger().info(msg);
            return null;
        }

        Scoped scoped = command.getClass().getAnnotation(Scoped.class);
        if (scoped == null) {
            String msg = adminStrings.getLocalString("adapter.command.noscope",
                    "Implementation for the command {0} exists in the "
                    + "system,\nbut it has no @Scoped annotation", commandName);
            report.setMessage(msg);
            report.setActionExitCode(ActionReport.ExitCode.FAILURE);
            LogHelper.getDefaultLogger().info(msg);
            command = null;
        } else if (scoped.value() == Singleton.class) {
            // check that there are no parameters for this command
            CommandModel model = getModel(command);
            if (model.getParameters().size() > 0) {
                String msg =
                        adminStrings.getLocalString("adapter.command.hasparams",
                        "Implementation for the command {0} exists in the "
                        + "system,\nbut it's a singleton that also has "
                        + "parameters", commandName);
                report.setMessage(msg);
                report.setActionExitCode(ActionReport.ExitCode.FAILURE);
                LogHelper.getDefaultLogger().info(msg);
                command = null;
            }
        }

        return command;
    }

    /**
     * Obtain a new command invocation object.
     * Command invocations can be configured and used
     * to trigger a command execution.
     *
     * @param name name of the requested command to invoke
     * @param report where to place the status of the command execution
     * @return a new command invocation for that command name
     */
    public CommandInvocation getCommandInvocation(String name,
            ActionReport report) {
        return new ExecutionContext(name, report);
    }

    private ActionReport.ExitCode injectParameters(final CommandModel model, final AdminCommand command,
            final InjectionResolver<Param> injector,
            final AdminCommandContext context) {

        ActionReport report = context.getActionReport();
        report.setActionDescription(model.getCommandName() + " command");
        report.setActionExitCode(ActionReport.ExitCode.SUCCESS);
        try {
            GenericCrudCommand c = GenericCrudCommand.class.cast(command);
            c.setInjectionResolver(injector);
        } catch (ClassCastException e) {
            // do nothing.
        }

        // inject
        try {
            injectionMgr.inject(command, injector);
        } catch (UnsatisfiedDependencyException e) {
            Param param = e.getAnnotation(Param.class);
            CommandModel.ParamModel paramModel = null;
            for (CommandModel.ParamModel pModel : model.getParameters()) {
                if (pModel.getParam().equals(param)) {
                    paramModel = pModel;
                    break;
                }
            }
            String errorMsg;
            final String usage = getUsageText(command, model);
            if (paramModel != null) {
                String paramName = paramModel.getName();
                String paramDesc = paramModel.getLocalizedDescription();

                if (param.primary()) {
                    errorMsg = adminStrings.getLocalString("commandrunner.operand.required",
                            "Operand required.");
                } else if (param.password()) {
                    errorMsg = adminStrings.getLocalString("adapter.param.missing.passwordfile",
                            "{0} command requires the passwordfile "
                            + "parameter containing {1} entry.",
                            model.getCommandName(), paramName);
                } else if (paramDesc != null) {
                    errorMsg = adminStrings.getLocalString("admin.param.missing",
                            "{0} command requires the {1} parameter ({2})",
                            model.getCommandName(), paramName, paramDesc);

                } else {
                    errorMsg = adminStrings.getLocalString("admin.param.missing.nodesc",
                            "{0} command requires the {1} parameter",
                            model.getCommandName(), paramName);
                }
            } else {
                errorMsg = adminStrings.getLocalString("admin.param.missing.nofound",
                        "Cannot find {1} in {0} command model, file a bug",
                        model.getCommandName(), e.getUnsatisfiedName());
            }
            logger.severe(errorMsg);
            report.setActionExitCode(ActionReport.ExitCode.FAILURE);
            report.setMessage(errorMsg);
            report.setFailureCause(e);
            ActionReport.MessagePart childPart =
                    report.getTopMessagePart().addChild();
            childPart.setMessage(usage);
            return report.getActionExitCode();
        } catch (ComponentException e) {
            // If the cause is UnacceptableValueException -- we want the message
            // from it.  It is wrapped with a less useful Exception.

            Exception exception = e;
            Throwable cause = e.getCause();
            if (cause != null
                    && (cause instanceof UnacceptableValueException
                    || cause instanceof IllegalArgumentException)) {
                // throw away the wrapper.
                exception = (Exception) cause;
            }
            logger.log(Level.SEVERE, "invocation.exception", exception);
            report.setActionExitCode(ActionReport.ExitCode.FAILURE);
            report.setMessage(exception.getMessage());
            report.setFailureCause(exception);
            ActionReport.MessagePart childPart =
                    report.getTopMessagePart().addChild();
            childPart.setMessage(getUsageText(command, model));
            return report.getActionExitCode();
        }
        return report.getActionExitCode();
    }

    /**
     * Executes the provided command object.
     *
     * @param model model of the command (used for logging and reporting)
     * @param command the command service to execute
     * @param context the AdminCommandcontext that has the payload and report
     */
    private ActionReport doCommand(
            final CommandModel model,
            final AdminCommand command,
            final AdminCommandContext context) {

        ActionReport report = context.getActionReport();
        report.setActionDescription(model.getCommandName() + " AdminCommand");

        // We need to set context CL to common CL before executing
        // the command. See issue #5596
        final AdminCommand wrappedComamnd = new AdminCommand() {

            public void execute(AdminCommandContext context) {
                Thread thread = Thread.currentThread();
                ClassLoader origCL = thread.getContextClassLoader();
                ClassLoader ccl = sc.getCommonClassLoader();
                if (origCL != ccl) {
                    try {
                        thread.setContextClassLoader(ccl);
                        command.execute(context);
                    } finally {
                        thread.setContextClassLoader(origCL);
                    }
                } else {
                    command.execute(context);
                }
            }
        };

        // the command may be an asynchronous command, so we need to check
        // for the @Async annotation.
        Async async = command.getClass().getAnnotation(Async.class);
        if (async == null) {
            try {
                wrappedComamnd.execute(context);
            } catch (Throwable e) {
                logger.log(Level.SEVERE,
                        adminStrings.getLocalString("adapter.exception",
                        "Exception in command execution : ", e), e);
                report.setMessage(e.toString());
                report.setActionExitCode(ActionReport.ExitCode.FAILURE);
                report.setFailureCause(e);
            }
        } else {
            Thread t = new Thread() {

                public void run() {
                    try {
                        wrappedComamnd.execute(context);
                    } catch (RuntimeException e) {
                        logger.log(Level.SEVERE, e.getMessage(), e);
                    }
                }
            };
            t.setPriority(async.priority());
            t.start();
            report.setActionExitCode(ActionReport.ExitCode.SUCCESS);
            report.setMessage(
                    adminStrings.getLocalString("adapter.command.launch",
                    "Command {0} was successfully initiated asynchronously.",
                    model.getCommandName()));
        }
        return context.getActionReport();
    }

    /**
     * Get the usage-text of the command.
     * Check if <command-name>.usagetext is defined in LocalString.properties.
     * If defined, then use the usagetext from LocalString.properties else
     * generate the usagetext from Param annotations in the command class.
     *
     * @param command class
     * @param model command model
     * @return usagetext
     */
    static String getUsageText(AdminCommand command, CommandModel model) {
        StringBuffer usageText = new StringBuffer();

        String usage;
        if (ok(usage = model.getUsageText())) {
            usageText.append(
                    adminStrings.getLocalString("adapter.usage", "Usage: "));
            usageText.append(usage);
            return usageText.toString();
        } else {
            return generateUsageText(model);
        }
    }

    /**
     * Generate the usage-text from the annotated Param in the command class.
     *
     * @param model command model
     * @return generated usagetext
     */
    private static String generateUsageText(CommandModel model) {
        StringBuffer usageText = new StringBuffer();
        usageText.append(
                adminStrings.getLocalString("adapter.usage", "Usage: "));
        usageText.append(model.getCommandName());
        usageText.append(" ");
        StringBuffer operand = new StringBuffer();
        for (CommandModel.ParamModel pModel : model.getParameters()) {
            final Param param = pModel.getParam();
            final String paramName =
                    pModel.getName().toLowerCase(Locale.ENGLISH);
            // skip "hidden" options
            if (paramName.startsWith("_")) {
                continue;
            }
            // do not want to display password as an option
            if (param.password()) {
                continue;
            }
            // do not want to display obsolete options
            if (param.obsolete()) {
                continue;
            }
            final boolean optional = param.optional();
            final Class<?> ftype = pModel.getType();
            Object fvalue = null;
            String fvalueString = null;
            try {
                fvalue = param.defaultValue();
                if (fvalue != null) {
                    fvalueString = fvalue.toString();
                }
            } catch (Exception e) {
                // just leave it as null...
            }
            // this is a param.
            if (param.primary()) {
                if (optional) {
                    operand.append("[").append(paramName).append("] ");
                } else {
                    operand.append(paramName).append(" ");
                }
                continue;
            }

            if (optional) {
                usageText.append("[");
            }

            usageText.append("--").append(paramName);
            if (ok(param.defaultValue())) {
                usageText.append("=").append(param.defaultValue());
            } else if (ftype.isAssignableFrom(String.class)) {
                // check if there is a default value assigned
                if (ok(fvalueString)) {
                    usageText.append("=").append(fvalueString);
                } else {
                    usageText.append("=").append(paramName);
                }
            } else if (ftype.isAssignableFrom(Boolean.class)) {
                // note: There is no defaultValue for this param.  It might
                // hava  value -- but we don't care -- it isn't an official
                // default value.
                usageText.append("=").append("true|false");
            } else {
                usageText.append("=").append(paramName);
            }

            if (optional) {
                usageText.append("] ");
            } else {
                usageText.append(" ");
            }
        }
        usageText.append(operand);
        return usageText.toString();
    }

    public void getHelp(AdminCommand command, ActionReport report) {

        CommandModel model = getModel(command);
        report.setActionDescription(model.getCommandName() + " help");

        // XXX - this is a hack for now.  if the request mapped to an
        // XMLContentActionReporter, that means we want the command metadata.
        if (report instanceof XMLContentActionReporter) {
            getMetadata(command, model, report);
        } else {
            report.setMessage(model.getCommandName() + " - "
                    + model.getLocalizedDescription());
            report.getTopMessagePart().addProperty("SYNOPSIS",
                    encodeManPage(new BufferedReader(new StringReader(
                    getUsageText(command, model)))));
            for (CommandModel.ParamModel param : model.getParameters()) {
                addParamUsage(report, param);
            }
            report.setActionExitCode(ActionReport.ExitCode.SUCCESS);
        }
    }

    /**
     * Return the metadata for the command.  We translate the parameter
     * and operand information to parts and properties of the ActionReport,
     * which will be translated to XML elements and attributes by the
     * XMLContentActionReporter.
     *
     * @param command the command
     * @param model the CommandModel describing the command
     * @param report	the (assumed to be) XMLContentActionReporter
     */
    private void getMetadata(AdminCommand command, CommandModel model,
            ActionReport report) {
        ActionReport.MessagePart top = report.getTopMessagePart();
        ActionReport.MessagePart cmd = top.addChild();
        // <command name="name">
        cmd.setChildrenType("command");
        cmd.addProperty("name", model.getCommandName());
        if (model.unknownOptionsAreOperands()) {
            cmd.addProperty("unknown-options-are-operands", "true");
        }
        String usage = model.getUsageText();
        if (ok(usage)) {
            cmd.addProperty("usage", usage);
        }
        CommandModel.ParamModel primary = null;
        // for each parameter add
        // <option name="name" type="type" short="s" default="default"
        //   acceptable-values="list"/>
        for (CommandModel.ParamModel p : model.getParameters()) {
            Param param = p.getParam();
            if (param.primary()) {
                primary = p;
                continue;
            }
            ActionReport.MessagePart ppart = cmd.addChild();
            ppart.setChildrenType("option");
            ppart.addProperty("name", p.getName());
            ppart.addProperty("type", typeOf(p));
            ppart.addProperty("optional", Boolean.toString(param.optional()));
            if (param.obsolete()) // don't include it if it's false
            {
                ppart.addProperty("obsolete", "true");
            }
            String paramDesc = p.getLocalizedDescription();
            if (ok(paramDesc)) {
                ppart.addProperty("description", paramDesc);
            }
            if (ok(param.shortName())) {
                ppart.addProperty("short", param.shortName());
            }
            if (ok(param.defaultValue())) {
                ppart.addProperty("default", param.defaultValue());
            }
            if (ok(param.acceptableValues())) {
                ppart.addProperty("acceptable-values", param.acceptableValues());
            }
            if (ok(param.alias())) {
                ppart.addProperty("alias", param.alias());
            }
        }

        // are operands allowed?
        if (primary != null) {
            // for the operand(s), add
            // <operand type="type" min="0/1" max="1"/>
            ActionReport.MessagePart primpart = cmd.addChild();
            primpart.setChildrenType("operand");
            primpart.addProperty("name", primary.getName());
            primpart.addProperty("type", typeOf(primary));
            primpart.addProperty("min",
                    primary.getParam().optional() ? "0" : "1");
            primpart.addProperty("max", primary.getParam().multiple()
                    ? "unlimited" : "1");
            String desc = primary.getLocalizedDescription();
            if (ok(desc)) {
                primpart.addProperty("description", desc);
            }
        }
    }

    /**
     * Map a Java type to one of the types supported by the asadmin client.
     * Currently supported types are BOOLEAN, FILE, PROPERTIES, PASSWORD, and
     * STRING.  (All of which should be defined constants on some class.)
     *
     * @param p the Java type
     * @return	the string representation of the asadmin type
     */
    private static String typeOf(CommandModel.ParamModel p) {
        Class t = p.getType();
        if (t == Boolean.class || t == boolean.class) {
            return "BOOLEAN";
        } else if (t == File.class || t == File[].class) {
            return "FILE";
        } else if (t == Properties.class) { // XXX - allow subclass?
            return "PROPERTIES";
        } else if (p.getParam().password()) {
            return "PASSWORD";
        } else {
            return "STRING";
        }
    }

    /**
     * Return an InputStream for the man page for the named command.
     */
    public static BufferedReader getManPage(String commandName,
            CommandModel model) {
        Class clazz = model.getCommandClass();
        if (clazz == null) {
            return null;
        }
        return ManPageFinder.getCommandManPage(commandName, clazz.getName(),
                Locale.getDefault(), clazz.getClassLoader(), logger);
    }

    private void addParamUsage(
            ActionReport report,
            CommandModel.ParamModel model) {
        Param param = model.getParam();
        if (param != null) {
            // this is a param.
            String paramName = model.getName().toLowerCase(Locale.ENGLISH);
            // skip "hidden" options
            if (paramName.startsWith("_")) {
                return;
            }
            // do not want to display password in the usage
            if (param.password()) {
                return;
            }
            // do not want to display obsolete options
            if (param.obsolete()) {
                return;
            }
            if (param.primary()) {
                // if primary then it's an operand
                report.getTopMessagePart().addProperty(paramName + "_operand",
                        model.getLocalizedDescription());
            } else {
                report.getTopMessagePart().addProperty(paramName,
                        model.getLocalizedDescription());
            }
        }
    }

    private static boolean ok(String s) {
        return s != null && s.length() > 0;
    }

    /**
     * Validate the paramters with the Param annotation.  If parameter is
     * not defined as a Param annotation then it's an invalid option.
     * If parameter's key is "DEFAULT" then it's a operand.
     *
     * @param model command model
     * @param parameters parameters from URL
     * @throws ComponentException if option is invalid
     */
    static void validateParameters(final CommandModel model,
            final ParameterMap parameters) throws ComponentException {

        // loop through parameters and make sure they are
        // part of the Param declared field
        for (Map.Entry<String, List<String>> entry : parameters.entrySet()) {
            String key = entry.getKey();

            // to do, we should validate meta-options differently.
            if (key.equals("DEFAULT") || key.startsWith(ASADMIN_CMD_PREFIX)) {
                continue;
            }

            // help and Xhelp are meta-options that are handled specially
            if (key.equals("help") || key.equals("Xhelp")) {
                continue;
            }

            // check if key is a valid Param Field
            boolean validOption = false;
            // loop through the Param field in the command class
            // if either field name or the param name is equal to
            // key then it's a valid option
            for (CommandModel.ParamModel pModel : model.getParameters()) {
                validOption = pModel.isParamId(key);
                if (validOption) {
                    break;
                }
                if (pModel.getParam().password()) {
                    validOption = pModel.isParamId(
                            ASADMIN_CMD_PREFIX + key.toUpperCase(Locale.ENGLISH));
                    if (validOption) {
                        break;
                    }
                }
            }
            if (!validOption) {
                throw new ComponentException(" Invalid option: " + key);
            }
        }
    }

    /**
     * Check if the variable, "skipParamValidation" is defined in the command
     * class.  If defined and set to true, then parameter validation will be
     * skipped from that command.
     * This is used mostly for command referencing.  For example the
     * list-applications command references list-components command and you
     * don't want to define the same params from the class that implements
     * list-components.
     *
     * @param command - AdminCommand class
     * @return true if to skip param validation, else return false.
     */
    static boolean skipValidation(AdminCommand command) {
        try {
            final Field f =
                    command.getClass().getDeclaredField("skipParamValidation");
            f.setAccessible(true);
            if (f.getType().isAssignableFrom(boolean.class)) {
                return f.getBoolean(command);
            }
        } catch (NoSuchFieldException e) {
            return false;
        } catch (IllegalAccessException e) {
            return false;
        }
        //all else return false
        return false;
    }

    private static String encodeManPage(BufferedReader br) {
        if (br == null) {
            return null;
        }

        try {
            String line;
            StringBuilder sb = new StringBuilder();

            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append(ManifestUtils.EOL_TOKEN);
            }
            return sb.toString();
        } catch (Exception ex) {
            return null;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioex) {
                }
            }
        }
    }

    private static CommandModel getModel(AdminCommand command) {

        if (command instanceof CommandModelProvider) {
            return ((CommandModelProvider) command).getModel();
        } else {
            return new CommandModelImpl(command.getClass());
        }
    }

    /**
     * Called from ExecutionContext.execute.
     */
    private void doCommand(ExecutionContext inv, AdminCommand command) {

        if (command == null) {
            command = getCommand(inv.name(), inv.report(), logger);
            if (command == null) {
                return;
            }
        }

        CommandModel model;
        try {
            CommandModelProvider c = CommandModelProvider.class.cast(command);
            model = c.getModel();
        } catch (ClassCastException e) {
            model = new CommandModelImpl(command.getClass());
        }
        UploadedFilesManager ufm = null;
        ActionReport report = inv.report();
        ParameterMap parameters;
        final AdminCommandContext context = new AdminCommandContext(
                LogDomains.getLogger(command.getClass(), LogDomains.ADMIN_LOGGER),
                report, inv.inboundPayload(), inv.outboundPayload());

        List<RuntimeType> runtimeTypes = new ArrayList<RuntimeType>();
        FailurePolicy fp = null;
        Set<CommandTarget> targetTypesAllowed = new HashSet<CommandTarget>();
        ActionReport.ExitCode preSupplementalReturn = ActionReport.ExitCode.SUCCESS;
        ActionReport.ExitCode postSupplementalReturn = ActionReport.ExitCode.SUCCESS;

        // If this glassfish installation does not have stand alone instances / clusters at all, then
        // lets not even look Supplemental command and such. A small optimization
        boolean doReplication = false;
        if ((domain.getServers().getServer().size() > 1) || (domain.getClusters().getCluster().size() != 0)) {
            doReplication = true;
        } else {
            logger.fine(adminStrings.getLocalString("dynamicreconfiguration.diagnostics.devmode",
                    "The GlassFish environment does not have any clusters or instances present; Replication is turned off"));
        }

        try {
            try {
                /*
                 * Extract any uploaded files and build a map from parameter names
                 * to the corresponding extracted, uploaded file.
                 */
                ufm = new UploadedFilesManager(inv.report, logger,
                        inv.inboundPayload());

                if (inv.typedParams() != null) {
                    logger.fine(adminStrings.getLocalString("dynamicreconfiguration.diagnostics.delegatedcommand",
                            "This command is a delegated command. Dynamic reconfiguration will be bypassed"));
                    InjectionResolver<Param> injectionTarget =
                            new DelegatedInjectionResolver(model, inv.typedParams(),
                            ufm.optionNameToFileMap());
                    if (injectParameters(model, command, injectionTarget, context).equals(ActionReport.ExitCode.SUCCESS)) {
                        inv.setReport(doCommand(model, command, context));
                    }
                    return;
                }

                parameters = inv.parameters();
                if (parameters == null) {
                    // no parameters, pass an empty collection
                    parameters = new ParameterMap();
                }

                if (isSet(parameters, "help") || isSet(parameters, "Xhelp")) {
                    BufferedReader in = getManPage(model.getCommandName(), model);
                    String manPage = encodeManPage(in);

                    if (manPage != null && isSet(parameters, "help")) {
                        inv.report().getTopMessagePart().addProperty("MANPAGE", manPage);
                    } else {
                        report.getTopMessagePart().addProperty(
                                AdminCommandResponse.GENERATED_HELP, "true");
                        getHelp(command, report);
                    }
                    return;
                }

                try {
                    if (!skipValidation(command)) {
                        validateParameters(model, parameters);
                    }
                } catch (ComponentException e) {
                    // If the cause is UnacceptableValueException -- we want the message
                    // from it.  It is wrapped with a less useful Exception.

                    Exception exception = e;
                    Throwable cause = e.getCause();
                    if (cause != null
                            && (cause instanceof UnacceptableValueException)) {
                        // throw away the wrapper.
                        exception = (Exception) cause;
                    }
                    logger.severe(exception.getMessage());
                    report.setActionExitCode(ActionReport.ExitCode.FAILURE);
                    report.setMessage(exception.getMessage());
                    report.setFailureCause(exception);
                    ActionReport.MessagePart childPart =
                            report.getTopMessagePart().addChild();
                    childPart.setMessage(getUsageText(command, model));
                    return;
                }

                // initialize the injector and inject
                InjectionResolver<Param> injectionMgr =
                        new MapInjectionResolver(model, parameters,
                        ufm.optionNameToFileMap());
                if (!injectParameters(model, command, injectionMgr, context).equals(ActionReport.ExitCode.SUCCESS)) {
                    return;
                }

                logger.fine(adminStrings.getLocalString("dynamicreconfiguration.diagnostics.injectiondone",
                        "Parameter mapping, validation, injection completed successfully; Starting paramater injection"));

                // Read cluster annotation attributes
                org.glassfish.api.admin.ExecuteOn clAnnotation = model.getClusteringAttributes();
                if (clAnnotation == null) {
                    runtimeTypes.add(RuntimeType.DAS);
                    runtimeTypes.add(RuntimeType.INSTANCE);
                    fp = FailurePolicy.Error;
                } else {
                    if (clAnnotation.value().length == 0) {
                        runtimeTypes.add(RuntimeType.DAS);
                        runtimeTypes.add(RuntimeType.INSTANCE);
                    } else {
                        for (RuntimeType t : clAnnotation.value()) {
                            runtimeTypes.add(t);
                        }
                    }
                    if (clAnnotation.ifFailure() == null) {
                        fp = FailurePolicy.Error;
                    } else {
                        fp = clAnnotation.ifFailure();
                    }
                }

                String targetName = parameters.getOne("target");
                if (targetName == null || model.getModelFor("target").getParam().obsolete()) {
                    targetName = "server";
                }

                logger.fine(adminStrings.getLocalString("dynamicreconfiguration.diagnostics.target",
                        "@ExecuteOn parsing and default settings done; Current target is " + targetName));

                if (serverEnv.isDas()) {

                    // Check if the command allows this target type; first read the annotation
                    //TODO : See is @TargetType can also be moved to the CommandModel
                    TargetType tgtTypeAnnotation = command.getClass().getAnnotation(TargetType.class);
                    if (tgtTypeAnnotation != null) {
                        for (CommandTarget c : tgtTypeAnnotation.value()) {
                            targetTypesAllowed.add(c);
                        }
                    };
                    //If not @TargetType, default it
                    if (targetTypesAllowed.size() == 0) {
                        targetTypesAllowed.add(CommandTarget.DAS);
                        targetTypesAllowed.add(CommandTarget.STANDALONE_INSTANCE);
                        targetTypesAllowed.add(CommandTarget.CLUSTER);
                        targetTypesAllowed.add(CommandTarget.CONFIG);
                    }

                    // If the target is "server" and the command is not marked for DAS,
                    // add DAS to RuntimeTypes; This is important because those class of CLIs that
                    // do not always have to be run on DAS followed by applicable instances
                    // will have @ExecuteOn(RuntimeType.INSTANCE) and they have to be run on DAS
                    // ONLY if the target is "server"
                    if (CommandTarget.DAS.isValid(habitat, targetName)
                            && !runtimeTypes.contains(RuntimeType.DAS)) {
                        runtimeTypes.add(RuntimeType.DAS);
                    }

                    logger.fine(adminStrings.getLocalString("dynamicreconfiguration.diagnostics.runtimeTypes",
                            "RuntimeTypes are : " + runtimeTypes.toString()));
                    logger.fine(adminStrings.getLocalString("dynamicreconfiguration,diagnostics.targetTypes",
                            "TargetTypes are : " + targetTypesAllowed.toString()));

                    // Check if the target is valid
                    //Is there a server or a cluster or a config with given name ?
                    if ((!CommandTarget.DOMAIN.isValid(habitat, targetName))
                            && (domain.getServerNamed(targetName) == null)
                            && (domain.getClusterNamed(targetName) == null)
                            && (domain.getConfigNamed(targetName) == null)) {
                        report.setActionExitCode(ActionReport.ExitCode.FAILURE);
                        report.setMessage(adminStrings.getLocalString("commandrunner.executor.invalidtarget",
                                "Unable to find a valid target with name {0}", targetName));
                        return;
                    }
                    //Does this command allow this target type
                    boolean isTargetValidType = false;
                    Iterator<CommandTarget> it = targetTypesAllowed.iterator();
                    while (it.hasNext()) {
                        if (it.next().isValid(habitat, targetName)) {
                            isTargetValidType = true;
                            break;
                        }
                    }
                    if (!isTargetValidType) {
                        StringBuilder validTypes = new StringBuilder();
                        it = targetTypesAllowed.iterator();
                        while (it.hasNext()) {
                            validTypes.append(it.next().getDescription() + ", ");
                        }
                        report.setActionExitCode(ActionReport.ExitCode.FAILURE);
                        report.setMessage(adminStrings.getLocalString("commandrunner.executor.invalidtargettype",
                                "Target {0} is not a supported type. Command {1} supports these types of targets only : {2}",
                                targetName, model.getCommandName(), validTypes.toString()));
                        return;
                    }
                    //If target is a clustered instance and the allowed types does not allow operations on clustered
                    //instance, return error
                    if ((CommandTarget.CLUSTERED_INSTANCE.isValid(habitat, targetName))
                            && (!targetTypesAllowed.contains(CommandTarget.CLUSTERED_INSTANCE))) {
                        Cluster c = domain.getClusterForInstance(targetName);
                        report.setActionExitCode(ActionReport.ExitCode.FAILURE);
                        report.setMessage(adminStrings.getLocalString("commandrunner.executor.instanceopnotallowed",
                                "The {0} command is not allowed on instance {1} because it is part of cluster {2}",
                                model.getCommandName(), targetName, c.getName()));
                        return;
                    }
                    logger.fine(adminStrings.getLocalString("dynamicreconfiguration.diagnostics.replicationvalidationdone",
                            "All @ExecuteOn attribute and type validation completed successfully. Starting replication stages"));
                }


                /**
                 * We're finally ready to actually execute the command instance.
                 * Acquire the appropriate lock.
                 */
                Lock lock = null;
                boolean lockTimedOut = false;
                try {
                    // XXX: The owner of the lock should not be hardcoded.  The
                    //      value is not used yet. 
                    lock = adminLock.getLock(command, "asadmin");

                    // If command is undoable, then invoke prepare method
                    if (command instanceof UndoableCommand) {
                        UndoableCommand uCmd = (UndoableCommand) command;
                        logger.fine(adminStrings.getLocalString("dynamicreconfiguration.diagnostics.prepareunodable",
                                "Command execution stage 1 : Calling prepare for undoable command " + inv.name()));
                        if (!uCmd.prepare(context, parameters).equals(ActionReport.ExitCode.SUCCESS)) {
                            report.setActionExitCode(ActionReport.ExitCode.FAILURE);
                            report.setMessage(adminStrings.getLocalString("commandrunner.executor..errorinprepare",
                                    "The command {0} cannot be completed because the preparation for the command failed "
                                    + "indicating potential issues : {1}", model.getCommandName(), report.getMessage()));
                            return;
                        }
                    }

                    ClusterOperationUtil.clearInstanceList();
                    // Run Supplemental commands that have to run before this command on this instance type
                    SupplementalCommandExecutor supplementalExecutor;
                    if (doReplication && (supplementalExecutor = habitat.getComponent(SupplementalCommandExecutor.class,
                            "SupplementalCommandExecutorImpl")) != null) {
                        logger.fine(adminStrings.getLocalString("dynamicreconfiguration.diagnostics.presupplemental",
                                "Command execution stage 2 : Call pre supplemental commands for " + inv.name()));
                        preSupplementalReturn = supplementalExecutor.execute(model.getCommandName(),
                                Supplemental.Timing.Before, context, parameters, ufm.optionNameToFileMap());
                        if (preSupplementalReturn.equals(ActionReport.ExitCode.FAILURE)) {
                            report.setActionExitCode(preSupplementalReturn);
                            report.setMessage(adminStrings.getLocalString("commandrunner.executor.supplementalcmdfailed",
                                    "A supplemental command failed; cannot proceed further"));
                            return;
                        }
                        //Run main command if it is applicable for this instance type
                        if ((runtimeTypes.contains(RuntimeType.ALL))
                                || (serverEnv.isDas()
                                && (CommandTarget.DOMAIN.isValid(habitat, targetName) || runtimeTypes.contains(RuntimeType.DAS)))
                                || (serverEnv.isInstance() && runtimeTypes.contains(RuntimeType.INSTANCE))) {
                            logger.fine(adminStrings.getLocalString("dynamicreconfiguration.diagnostics.maincommand",
                                    "Command execution stage 3 : Calling main command implementation for " + inv.name()));
                            report = doCommand(model, command, context);
                            inv.setReport(report);
                        }

                        if (!FailurePolicy.applyFailurePolicy(fp,
                                report.getActionExitCode()).equals(ActionReport.ExitCode.FAILURE)) {
                            //Run Supplemental commands that have to be run after this command on this instance type
                            logger.fine(adminStrings.getLocalString("dynamicreconfiguration.diagnostics.postsupplemental",
                                    "Command execution stage 4 : Call post supplemental commands for " + inv.name()));
                            postSupplementalReturn = supplementalExecutor.execute(model.getCommandName(),
                                    Supplemental.Timing.After, context, parameters, ufm.optionNameToFileMap());
                            if (postSupplementalReturn.equals(ActionReport.ExitCode.FAILURE)) {
                                report.setActionExitCode(postSupplementalReturn);
                                report.setMessage(adminStrings.getLocalString("commandrunner.executor.supplementalcmdfailed",
                                        "A supplemental command failed; cannot proceed further"));
                                return;
                            }
                        }
                    } else {
                        report = doCommand(model, command, context);
                    }
                    inv.setReport(report);

                } catch (AdminCommandLockTimeoutException ex) {
                    lockTimedOut = true;
                    String lockTime = formatSuspendDate(ex.getTimeOfAcquisition());
                    String logMsg = "Command: " + model.getCommandName()
                            + " failed to acquire a command lock.  REASON: time out "
                            + "(current lock acquired on " + lockTime + ")";
                    logger.warning(logMsg);
                    String msg = adminStrings.getLocalString("lock.timeout",
                            "Command timed out.  Unable to acquire a lock to access "
                            + "the domain.  Another command acquired exclusive access "
                            + "to the domain on {0}.  Retry the command at a later "
                            + "time.", lockTime);
                    report.setMessage(msg);
                    report.setActionExitCode(ActionReport.ExitCode.FAILURE);
                } catch (AdminCommandLockException ex) {
                    lockTimedOut = true;
                    String lockTime = formatSuspendDate(ex.getTimeOfAcquisition());
                    String lockMsg = ex.getMessage();
                    String logMsg;

                    logMsg = "Command: " + model.getCommandName()
                            + " was blocked.  The domain was suspended by a "
                            + "user on:" + lockTime;

                    if (lockMsg != null && lockMsg != "") {
                        logMsg += " Reason: " + lockMsg;
                    }

                    logger.warning(logMsg);
                    String msg = adminStrings.getLocalString("lock.notacquired",
                            "The command was blocked.  The domain was suspended by "
                            + "a user on {0}.", lockTime);

                    if (lockMsg != null && lockMsg != "") {
                        msg += " "
                                + adminStrings.getLocalString("lock.reason", "Reason:")
                                + " " + lockMsg;
                    }

                    report.setMessage(msg);
                    report.setActionExitCode(ActionReport.ExitCode.FAILURE);
                } finally {
                    // command is done, release the lock
                    if (lock != null && lockTimedOut == false) {
                        lock.unlock();
                    }
                }

            } catch (Exception ex) {
                logger.log(Level.SEVERE, "", ex);
                report.setActionExitCode(ActionReport.ExitCode.FAILURE);
                report.setMessage(ex.getMessage());
                report.setFailureCause(ex);
                ActionReport.MessagePart childPart =
                        report.getTopMessagePart().addChild();
                childPart.setMessage(getUsageText(command, model));
                return;
            }

            /*
             * Command execution completed; If this is DAS and the command succeeded,
             * time to replicate; At this point we will get the appropriate ClusterExecutor
             * and give it complete control; We will let the executor take care all considerations
             * (like FailurePolicy settings etc)
             * and just give the final execution results which we will set as is in the Final
             * Action report returned to the caller.
             */

            if (processEnv.getProcessType().isEmbedded()) {
                return;
            }
            if (preSupplementalReturn == ActionReport.ExitCode.WARNING
                    || postSupplementalReturn == ActionReport.ExitCode.WARNING) {
                report.setActionExitCode(ActionReport.ExitCode.WARNING);
            }
            if (doReplication
                    && (!FailurePolicy.applyFailurePolicy(fp, report.getActionExitCode()).equals(ActionReport.ExitCode.FAILURE))
                    && (serverEnv.isDas())
                    && (runtimeTypes.contains(RuntimeType.INSTANCE) || runtimeTypes.contains(RuntimeType.ALL))) {
                logger.fine(adminStrings.getLocalString("dynamicreconfiguration.diagnostics.startreplication",
                        "Command execution stages completed on DAS; Starting replication on remote instances"));
                ClusterExecutor executor = null;
                // This try-catch block is a fix for 13838
                try {
                    if (model.getClusteringAttributes() != null && model.getClusteringAttributes().executor() != null) {
                        executor = habitat.getComponent(model.getClusteringAttributes().executor());
                    } else {
                        executor = habitat.getComponent(ClusterExecutor.class, "GlassFishClusterExecutor");
                    }
                } catch (UnsatisfiedDependencyException usdepex) {
                    String err = adminStrings.getLocalString("commandrunner.clusterexecutor.notinitialized",
                            "Unable to get an instance of ClusterExecutor; Cannot dynamically reconfigure instances");
                    logger.warning(err);
                }
                if (executor != null) {
                    report.setActionExitCode(executor.execute(model.getCommandName(), command, context, parameters));
                    if (report.getActionExitCode().equals(ActionReport.ExitCode.FAILURE)) {
                        report.setMessage(adminStrings.getLocalString("commandrunner.executor.errorwhilereplication",
                                "An error occurred during replication"));
                    }
                }
            }
            if (report.getActionExitCode().equals(ActionReport.ExitCode.FAILURE)) {
                // If command is undoable, then invoke undo method method
                if (command instanceof UndoableCommand) {
                    UndoableCommand uCmd = (UndoableCommand) command;
                    logger.fine(adminStrings.getLocalString("dynamicreconfiguration.diagnostics.undo",
                            "Command execution failed; calling undo() for command " + inv.name()));
                    uCmd.undo(context, parameters, ClusterOperationUtil.getCompletedInstances());
                }
            } else {
                //TODO : Is there a better way of doing this ? Got to look into it
                if ("_register-instance".equals(model.getCommandName())) {
                    state.addServerToStateService(parameters.getOne("DEFAULT"));
                }
                if ("_unregister-instance".equals(model.getCommandName())) {
                    state.removeInstanceFromStateService(parameters.getOne("DEFAULT"));
                }
            }
        } finally {
            if (ufm != null) {
                ufm.close();
            }
        }

    }

    /*
     * Some private classes used in the implementation of CommandRunner.
     */
    /**
     * ExecutionContext is a CommandInvocation, which
     * defines a command excecution context like the requested
     * name of the command to execute, the parameters of the command, etc.
     */
    class ExecutionContext implements CommandInvocation {

        protected final String name;
        protected ActionReport report;
        protected ParameterMap params;
        protected CommandParameters paramObject;
        protected Payload.Inbound inbound;
        protected Payload.Outbound outbound;

        private ExecutionContext(String name, ActionReport report) {
            this.name = name;
            this.report = report;
        }

        public CommandInvocation parameters(CommandParameters paramObject) {
            this.paramObject = paramObject;
            return this;
        }

        public CommandInvocation parameters(ParameterMap params) {
            this.params = params;
            return this;
        }

        public CommandInvocation inbound(Payload.Inbound inbound) {
            this.inbound = inbound;
            return this;
        }

        public CommandInvocation outbound(Payload.Outbound outbound) {
            this.outbound = outbound;
            return this;
        }

        public void execute() {
            execute(null);
        }

        private ParameterMap parameters() {
            return params;
        }

        private CommandParameters typedParams() {
            return paramObject;
        }

        private String name() {
            return name;
        }

        ActionReport report() {
            return report;
        }

        private void setReport(ActionReport ar) {
            report = ar;
        }

        private Payload.Inbound inboundPayload() {
            return inbound;
        }

        private Payload.Outbound outboundPayload() {
            return outbound;
        }

        public void execute(AdminCommand command) {
            CommandRunnerImpl.this.doCommand(this, command);

            // bnevins
            ActionReport r = report;
        }
    }

    /**
     * An InjectionResolver that uses an Object as the source of
     * the data to inject.
     */
    private static class DelegatedInjectionResolver
            extends InjectionResolver<Param> {

        private final CommandModel model;
        private final CommandParameters parameters;
        private final MultiMap<String, File> optionNameToUploadedFileMap;

        public DelegatedInjectionResolver(CommandModel model,
                CommandParameters parameters,
                final MultiMap<String, File> optionNameToUploadedFileMap) {
            super(Param.class);
            this.model = model;
            this.parameters = parameters;
            this.optionNameToUploadedFileMap = optionNameToUploadedFileMap;

        }

        @Override
        public boolean isOptional(AnnotatedElement element, Param annotation) {
            String name = CommandModel.getParamName(annotation, element);
            CommandModel.ParamModel param = model.getModelFor(name);
            return param.getParam().optional();
        }

        @Override
        public <V> V getValue(Object component, Inhabitant<?> onBehalfOf, AnnotatedElement target, Type genericType, Class<V> type) throws ComponentException {

            // look for the name in the list of parameters passed.
            if (target instanceof Field) {
                Field targetField = (Field) target;
                try {
                    Field sourceField =
                            parameters.getClass().getField(targetField.getName());
                    targetField.setAccessible(true);
                    Object paramValue = sourceField.get(parameters);
                    /*
                     * If this field is a File, then replace the param value
                     * (which is whatever the client supplied on the command) with
                     * the actual absolute path of the uploaded and extracted
                     * file if, in fact, the file was uploaded.
                     */
                    // XXX - doesn't handle multiple File operands
                    final String paramFileValue =
                            MapInjectionResolver.getUploadedFileParamValue(
                            targetField.getName(),
                            targetField.getType(),
                            optionNameToUploadedFileMap);
                    if (paramFileValue != null) {
                        paramValue = new File(paramFileValue);
                    }
                    /*
                    if (paramValue==null) {
                    return convertStringToObject(target, type,
                    param.defaultValue());
                    }
                     */
                    // XXX temp fix, to revisit
                    if (paramValue != null) {
                        checkAgainstAcceptableValues(target,
                                paramValue.toString());
                    }
                    return type.cast(paramValue);
                } catch (IllegalAccessException e) {
                } catch (NoSuchFieldException e) {
                }
            }
            return null;
        }

        private static void checkAgainstAcceptableValues(
                AnnotatedElement target, String paramValueStr) {
            Param param = target.getAnnotation(Param.class);
            String acceptable = param.acceptableValues();
            String paramName = CommandModel.getParamName(param, target);

            if (ok(acceptable) && ok(paramValueStr)) {
                String[] ss = acceptable.split(",");

                for (String s : ss) {
                    if (paramValueStr.equals(s.trim())) {
                        return;         // matched, value is good
                    }
                }

                // didn't match any, error
                throw new UnacceptableValueException(
                        adminStrings.getLocalString(
                        "adapter.command.unacceptableValue",
                        "Invalid parameter: {0}.  Its value is {1} "
                        + "but it isn''t one of these acceptable values: {2}",
                        paramName,
                        paramValueStr,
                        acceptable));
            }
        }
    }

    /**
     * Is the boolean valued parameter specified?
     * If so, and it has a value, is the value "true"?
     */
    private static boolean isSet(ParameterMap params, String name) {
        String val = params.getOne(name);
        if (val == null) {
            return false;
        }
        return val.length() == 0 || Boolean.valueOf(val).booleanValue();
    }

    /**
     * Encapsulates handling of files uploaded to the server in the payload
     * of the incoming HTTP request.
     * <p>
     * Extracts any such files from the payload into a temporary directory
     * under the domain's applications directory.  (Putting them there allows
     * the deployment processing to rename the uploaded archive to another location
     * under the applications directory, rather than having to copy them.)
     */
    private class UploadedFilesManager {

        private final ActionReport report;
        private final Logger logger;
        /**
         * maps option names as sent with each uploaded file to the corresponding
         * extracted files
         */
        private MultiMap<String, File> optionNameToFileMap;

        /*
         * PFM needs to be a field so it is not gc-ed before the
         * UploadedFilesManager is closed.
         */
        private PayloadFilesManager.Temp payloadFilesMgr = null;

        private UploadedFilesManager(final ActionReport report,
                final Logger logger,
                final Payload.Inbound inboundPayload) throws IOException, Exception {
            this.logger = logger;
            this.report = report;
            extractFiles(inboundPayload);
        }

        private MultiMap<String, File> optionNameToFileMap() {
            return optionNameToFileMap;
        }

        private void close() {
            if (payloadFilesMgr != null) {
                payloadFilesMgr.cleanup();
            }
        }

        private void extractFiles(final Payload.Inbound inboundPayload)
                throws Exception {
            if (inboundPayload == null) {
                return;
            }

            final File uniqueSubdirUnderApplications = chooseTempDirParent();
            payloadFilesMgr = new PayloadFilesManager.Temp(
                    uniqueSubdirUnderApplications,
                    report,
                    logger);

            /*
             * Extract the files into the temp directory.
             */
            final Map<File, Properties> payloadFiles =
                    payloadFilesMgr.processPartsExtended(inboundPayload);

            /*
             * Prepare the map of command options names to corresponding
             * uploaded files.
             */
            optionNameToFileMap = new MultiMap<String, File>();
            for (Map.Entry<File, Properties> e : payloadFiles.entrySet()) {
                final String optionName = e.getValue().getProperty("data-request-name");
                if (optionName != null) {
                    logger.finer("UploadedFilesManager: map " + optionName
                            + " to " + e.getKey());
                    optionNameToFileMap.add(optionName, e.getKey());
                }
            }
        }

        private File chooseTempDirParent() throws IOException {
            final File appRoot = new File(domain.getApplicationRoot());

            /*
             * Apparently during embedded runs the applications directory
             * might not be present already.  Create it if needed.
             */
            if (!appRoot.isDirectory()) {
                appRoot.mkdirs();
            }

            return appRoot;
        }
    }

    /** 
     * Format the lock acquisition time.
     */
    private String formatSuspendDate(Date lockTime) {
        if (lockTime != null) {
            String DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss z";
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
            return sdf.format(lockTime);
        } else {
            return new String(
                    adminStrings.getLocalString("lock.timeoutunavailable",
                    "<<Date is unavailable>>"));
        }
    }
}
