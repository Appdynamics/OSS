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

import com.sun.enterprise.util.OS;
import com.sun.enterprise.util.io.FileUtils;
import java.io.*;
import java.net.*;
import java.util.*;
import java.security.KeyStore;

import org.glassfish.api.admin.CommandException;
import com.sun.enterprise.admin.cli.remote.RemoteCommand;
import com.sun.enterprise.security.store.PasswordAdapter;
import com.sun.enterprise.universal.i18n.LocalStringsImpl;
import com.sun.enterprise.universal.io.SmartFile;
import com.sun.enterprise.universal.process.Jps;
import com.sun.enterprise.universal.process.ProcessUtils;
import com.sun.enterprise.universal.xml.MiniXmlParser;
import com.sun.enterprise.universal.xml.MiniXmlParserException;
import com.sun.enterprise.util.HostAndPort;
import com.sun.enterprise.util.io.ServerDirs;

/**
 * A class that's supposed to capture all the behavior common to operation
 * on a "local" server.
 * It's getting fairly complicated thus the "section headers" comments.
 * This class plays two roles, <UL><LI>a place for putting common code - which
 * are final methods.  A parent class that is communicating with its own unknown
 * sub-classes.  These are non-final methods
 *
 * @author Byron Nevins
 */
public abstract class LocalServerCommand extends CLICommand {
    ////////////////////////////////////////////////////////////////
    /// Section:  protected methods that are OK to override
    ////////////////////////////////////////////////////////////////
    /**
     * Override this method and return false to turn-off the file validation.
     * E.g. it demands that config/domain.xml be present.  In special cases like
     * Synchronization -- this is how you turn off the testing.
     * @return true - do the checks, false - don't do the checks
     */
    protected boolean checkForSpecialFiles() {
        return true;
    }

    ////////////////////////////////////////////////////////////////
    /// Section:  protected methods that are notOK to override.
    ////////////////////////////////////////////////////////////////
    /**
     * Returns the admin address of the local domain. Note that this method
     * should be called only when you own the domain that is available on
     * an accessible file system.
     *
     * @return HostAndPort object with admin server address
     * @throws CommandException in case of parsing errors
     */
    protected final HostAndPort getAdminAddress() throws CommandException {
        // default:  DAS which always has the name "server"
        return getAdminAddress("server");
    }

    /**
     * Returns the admin address of a particular server. Note that this method
     * should be called only when you own the server that is available on
     * an accessible file system.
     *
     * @return HostAndPort object with admin server address
     * @throws CommandException in case of parsing errors
     */
    protected final HostAndPort getAdminAddress(String serverName)
            throws CommandException {

        try {
            MiniXmlParser parser = new MiniXmlParser(getDomainXml(), serverName);
            List<HostAndPort> addrSet = parser.getAdminAddresses();

            if (addrSet.size() > 0)
                return addrSet.get(0);
            else
                throw new CommandException(strings.get("NoAdminPort"));
        }
        catch (MiniXmlParserException ex) {
            throw new CommandException(strings.get("NoAdminPortEx", ex), ex);
        }
    }

    protected final void setServerDirs(ServerDirs sd) {
        serverDirs = sd;
    }

    protected final void setLocalPassword() {
        String pw = serverDirs == null ? null : serverDirs.getLocalPassword();

        if (ok(pw)) {
            programOpts.setPassword(pw,
                    ProgramOptions.PasswordLocation.LOCAL_PASSWORD);
            logger.finer("Using local password");
        }
        else
            logger.finer("Not using local password");
    }

    protected final void unsetLocalPassword() {
        programOpts.setPassword(null,
                ProgramOptions.PasswordLocation.LOCAL_PASSWORD);
    }

    protected final void resetServerDirs() throws IOException {
        serverDirs = serverDirs.refresh();
    }

    protected final ServerDirs getServerDirs() {
        return serverDirs;
    }

    protected final File getDomainXml() {
        return serverDirs.getDomainXml();
    }

    /**
     * Checks if the create-domain was created using --savemasterpassword flag
     * which obtains security by obfuscation! Returns null in case of failure
     * of any kind.
     * @return String representing the password from the JCEKS store named
     *          master-password in domain folder
     */
    protected final String readFromMasterPasswordFile() {
        File mpf = getMasterPasswordFile();
        if (mpf == null)
            return null;   // no master password  saved
        try {
            PasswordAdapter pw = new PasswordAdapter(mpf.getAbsolutePath(),
                    "master-password".toCharArray()); // fixed key
            return pw.getPasswordForAlias("master-password");
        }
        catch (Exception e) {
            logger.finer("master password file reading error: "
                    + e.getMessage());
            return null;
        }
    }

    protected final boolean verifyMasterPassword(String mpv) {
        //issue : 14971, should ideally use javax.net.ssl.keyStore and
        //javax.net.ssl.keyStoreType system props here but they are
        //unavailable to asadmin start-domain hence falling back to
        //cacerts.jks instead of keystore.jks. Since the truststore
        //is less-likely to be Non-JKS

        return loadAndVerifyKeystore(getJKS(),mpv);
    }

    protected boolean loadAndVerifyKeystore(File jks,String mpv) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(jks);
            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            ks.load(fis, mpv.toCharArray());
            return true;
        }
        catch (Exception e) {
            logger.finer(e.getMessage());
            return false;
        }
        finally {
            try {
                if (fis != null)
                    fis.close();
            }
            catch (IOException ioe) {
                // ignore, I know ...
            }
        }
    }

    /**
     * Get the master password, either from a password file or
     * by asking the user.
     */
    protected final String getMasterPassword() throws CommandException {
        // Sets the password into the launcher info.
        // Yes, returning master password as a string is not right ...
        final int RETRIES = 3;
        long t0 = System.currentTimeMillis();
        String mpv = passwords.get(CLIConstants.MASTER_PASSWORD);
        if (mpv == null) { //not specified in the password file
            mpv = "changeit";  //optimization for the default case -- see 9592
            if (!verifyMasterPassword(mpv)) {
                mpv = readFromMasterPasswordFile();
                if (!verifyMasterPassword(mpv)) {
                    mpv = retry(RETRIES);
                }
            }
        }
        else { // the passwordfile contains AS_ADMIN_MASTERPASSWORD, use it
            if (!verifyMasterPassword(mpv))
                mpv = retry(RETRIES);
        }
        long t1 = System.currentTimeMillis();
        logger.finer("Time spent in master password extraction: "
                + (t1 - t0) + " msec");       //TODO
        return mpv;
    }

    /**
     * See if the server is alive and is the one at the specified directory.
     *
     * @return true if it's the DAS at this domain directory
     */
    protected final boolean isThisServer(File ourDir, String directoryKey) {
        if (!ok(directoryKey))
            throw new NullPointerException();

        ourDir = getUniquePath(ourDir);
        logger.finer("Check if server is at location " + ourDir);

        try {
            RemoteCommand cmd =
                    new RemoteCommand("__locations", programOpts, env);
            Map<String, String> attrs =
                    cmd.executeAndReturnAttributes(new String[]{"__locations"});
            String theirDirPath = attrs.get(directoryKey);
            logger.finer("Remote server has root directory " + theirDirPath);

            if (ok(theirDirPath)) {
                File theirDir = getUniquePath(new File(theirDirPath));
                return theirDir.equals(ourDir);
            }
            return false;
        }
        catch (Exception ex) {
            return false;
        }
    }

    /**
     * There is sometimes a need for subclasses to know if a
     * <code> local domain </code> is running. An example of such a command is
     * change-master-password command. The stop-domain command also needs to
     * know if a domain is running <i> without </i> having to provide user
     * name and password on command line (this is the case when I own a domain
     * that has non-default admin user and password) and want to stop it
     * without providing it.
     * <p>
     * In such cases, we need to know if the domain is running and this method
     * provides a way to do that.
     *
     * @return boolean indicating whether the server is running
     */
    protected final boolean isRunning(String host, int port) {
        Socket server = null;
        try {
            server = new Socket(host, port);
            return true;
        }
        catch (Exception ex) {
            logger.finer("\nisRunning got exception: " + ex);
            return false;
        }
        finally {
            if (server != null) {
                try {
                    server.close();
                }
                catch (IOException ex) {
                }
            }
        }
    }

    /**
     * convenience method for the local machine
     */
    protected final boolean isRunning(int port) {
        return isRunning(null, port);
    }

    /**
     * Is the server still running?
     * This is only called when we're hanging around waiting for the server to die.
     * Byron Nevins, Nov 7, 2010 - Check to see if the process itself is still running
     * We use OS tools to figure this out.  See ProcessUtils for details.
     * Failover to the JPS check if necessary
     */
    protected boolean isRunning() {
        int pp = getPrevPid();

        if (pp < 0)
            return isRunningByCheckingForPidFile();

        Boolean b = ProcessUtils.isProcessRunning(pp);

        if (b == null) // this means it couldn't find out!
            return isRunningUsingJps();
        else
            return b.booleanValue();
    }

    protected final void waitForRestart(File pwFile, long oldTimeStamp, long uptimeOldServer) throws CommandException {
        if (oldTimeStamp <= 0 || !usingLocalPassword())
            waitForRestartRemote(uptimeOldServer);
        else
            waitForRestartLocal(pwFile, oldTimeStamp, uptimeOldServer);
    }

    // todo move prevpid to ServerDirs ???
    protected final int getPrevPid() {
        try {
            File prevPidFile = new File(getServerDirs().getPidFile().getPath() + ".prev");

            if (!prevPidFile.canRead())
                return -1;

            String pids = FileUtils.readSmallFile(prevPidFile).trim();
            return Integer.parseInt(pids);
        }
        catch (Exception ex) {
            return -1;
        }
    }

    /**
     * Is the server still running?
     * This is only called when we're hanging around waiting for the server to die.
     * Byron Nevins, Nov 7, 2010 - Check to see if the process itself is still running
     * We use jps to check
     * If there are any problems fall back to the previous implementation of
     * isRunning() which looks for the pidfile to get deleted
     */
    private final boolean isRunningUsingJps() {
        int pp = getPrevPid();

        if (pp < 0)
            return isRunningByCheckingForPidFile();

        return Jps.isPid(pp);
    }

    /**
     * Is the server still running?
     * This is only called when we're hanging around waiting for the server to die.
     */
    private boolean isRunningByCheckingForPidFile() {
        File pf = getServerDirs().getPidFile();

        if (pf != null) {
            return pf.exists();
        }
        else
            return isRunning(programOpts.getHost(), // remote case
                    programOpts.getPort());
    }

    /**
     * Wait for the local server to restart.
     */
    private void waitForRestartLocal(File pwFile, long oldTimeStamp, long uptimeOldServer) throws CommandException {
        // we are using local-password for authentication to the local server.  We need
        // to use the NEW password that will be soon generated.  After that we can
        // do Uptime calls to make sure V3 is ready to receive commands

        if (!usingLocalPassword())
            throw new CommandException("Internal Error - waitForRestartLocal should "
                    + "not be called unless using local password authentication.");

        long end = CLIConstants.WAIT_FOR_DAS_TIME_MS
                + System.currentTimeMillis();

        while (System.currentTimeMillis() < end) {
            // when the server has restarted the passwordfile will be different
            // don't waste time reading the file again and again, just look
            // for the time stamp to change.
            // Careful -- there is a slice of time where the file does not exist!
            try {
                long newTimeStamp = pwFile.lastModified(); // could be 0L
                logger.finer("Checking timestamp of local-password.  "
                        + "old: " + oldTimeStamp + ", new: " + newTimeStamp);

                if (newTimeStamp > oldTimeStamp) {
                    // Server has restarted but may not be quite ready to handle commands
                    // automated tests would have issues if we returned right here...
                    resetServerDirs();
                    programOpts.setPassword(getServerDirs().getLocalPassword(), ProgramOptions.PasswordLocation.LOCAL_PASSWORD);
                    waitForRestartRemote(uptimeOldServer);
                    return;
                }
                Thread.sleep(CLIConstants.RESTART_CHECK_INTERVAL_MSEC);
            }
            catch (Exception e) {
                // continue
            }
        }
        // if we get here -- we timed out
        throw new CommandException(strings.get("restartDomain.noGFStart"));
    }

    /**
     * Wait for the remote server to restart.
     */
    private void waitForRestartRemote(long uptimeOldServer) throws CommandException {
        long end = CLIConstants.WAIT_FOR_DAS_TIME_MS
                + System.currentTimeMillis();

        while (System.currentTimeMillis() < end) {
            try {
                Thread.sleep(CLIConstants.RESTART_CHECK_INTERVAL_MSEC);
                long up = getUptime();
                logger.finer("oldserver-uptime, newserver-uptime = " + uptimeOldServer + " --- " + up);

                if (up > 0 && up < uptimeOldServer) {
                    return;
                }
            }
            catch (Exception e) {
                // continue
            }
        }
        // if we get here -- we timed out
        throw new CommandException(strings.get("restartDomain.noGFStart"));
    }

    /**
     * Get uptime from the server.
     */
    protected final long getUptime() throws CommandException {
        RemoteCommand cmd = new RemoteCommand("uptime", programOpts, env);
        String up = cmd.executeAndReturnOutput("uptime", "--milliseconds").trim();
        long up_ms = parseUptime(up);

        if (up_ms <= 0) {
            throw new CommandException(strings.get("restart.dasNotRunning"));
        }

        logger.finer("server uptime: " + up_ms);
        return up_ms;
    }
    /**
     * See if the server is restartable
     * As of March 2011 -- this only returns false if a passwordfile argument was given
     * when the server started -- but it is no longer available - i.e. the user
     * deleted it or made it unreadable.
     */
    protected final boolean isRestartable() throws CommandException {
        // false negative is worse than false positive.
        // there is one and only one case where we return false
        RemoteCommand cmd = new RemoteCommand("_get-runtime-info", programOpts, env);
        Map<String, String> atts = cmd.executeAndReturnAttributes("_get-runtime-info");

        if (atts != null) {
            String val = atts.get("restartable_value");

            if (ok(val) && val.equals("false"))
                return false;
        }
        return true;
    }

    ////////////////////////////////////////////////////////////////
    /// Section:  private methods
    ////////////////////////////////////////////////////////////////
    /**
     * The remote uptime command returns a string like:
     * Uptime: 10 minutes, 53 seconds, Total milliseconds: 653859\n
     * We find that last number and extract it.
     * XXX - this is pretty gross, and fragile
     */
    private long parseUptime(String up) {
        try {
            return Long.parseLong(up);
        }
        catch (Exception e) {
            return 0;
        }
    }

    private boolean usingLocalPassword() {
        return programOpts.getPasswordLocation() == ProgramOptions.PasswordLocation.LOCAL_PASSWORD;
    }

    private final File getJKS() {
        if (serverDirs == null)
            return null;

        File mp = new File(new File(serverDirs.getServerDir(), "config"), "cacerts.jks");
        if (!mp.canRead())
            return null;
        return mp;
    }

    protected File getMasterPasswordFile() {

        if (serverDirs == null)
            return null;

        File mp = new File(serverDirs.getServerDir(), "master-password");
        if (!mp.canRead())
            return null;

        return mp;
    }

    private String retry(int times) throws CommandException {
        String mpv;
        // prompt times times
        for (int i = 0; i < times; i++) {
            // XXX - I18N
            String prompt = strings.get("mp.prompt", (times - i));
            mpv = super.readPassword(prompt);
            if (mpv == null)
                throw new CommandException(strings.get("no.console"));
            // ignore retries :)
            if (verifyMasterPassword(mpv))
                return mpv;
            if (i < (times - 1))
                logger.info(strings.get("retry.mp"));
            // make them pay for typos?
            //Thread.currentThread().sleep((i+1)*10000);
        }
        throw new CommandException(strings.get("mp.giveup", times));
    }

    private File getUniquePath(File f) {
        try {
            f = f.getCanonicalFile();
        }
        catch (IOException ioex) {
            f = SmartFile.sanitize(f);
        }
        return f;
    }
    ////////////////////////////////////////////////////////////////
    /// Section:  private variables
    ////////////////////////////////////////////////////////////////
    private ServerDirs serverDirs;
    private static final LocalStringsImpl strings =
            new LocalStringsImpl(LocalDomainCommand.class);
}
