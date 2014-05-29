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
package com.sun.enterprise.admin.cli.cluster;

import com.sun.enterprise.universal.io.SmartFile;
import com.sun.enterprise.util.SystemPropertyConstants;
import com.sun.enterprise.util.io.FileListerRelative;
import com.sun.enterprise.util.io.FileUtils;
import com.sun.enterprise.util.zip.ZipFileException;
import com.sun.enterprise.util.zip.ZipWriter;
import java.io.*;
import java.net.*;
import org.glassfish.api.Param;
import org.glassfish.api.admin.CommandException;
import org.jvnet.hk2.annotations.Inject;
import org.jvnet.hk2.annotations.Scoped;
import org.jvnet.hk2.annotations.Service;
import org.jvnet.hk2.component.PerLookup;
import org.jvnet.hk2.component.Habitat;
import org.glassfish.internal.api.Globals;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author Rajiv Mordani
 * @author Byron Nevins
 */
@Service
@Scoped(PerLookup.class)
abstract class InstallNodeBaseCommand extends NativeRemoteCommandsBase {
    @Param(name = "archive", optional = true)
    private String archive;
    @Param(name = "installdir", optional = true, defaultValue = "${com.sun.aas.productRoot}")
    private String installDir;
    @Param(optional = true, defaultValue = "false")
    private boolean create;
    @Param(optional = true, defaultValue = "false")
    private boolean save;
    @Param(name = "force", optional = true, defaultValue = "false")
    private boolean force;
    @Inject
    private Habitat habitat;
    private String archiveName;
    private boolean delete = true;

    abstract void copyToHosts(File zipFile, ArrayList<String> binDirFiles) throws CommandException;
    abstract void precopy() throws CommandException;

    @Override
    protected void validate() throws CommandException {
        super.validate();
        Globals.setDefaultHabitat(habitat);

        installDir = resolver.resolve(installDir);
        if (!force) {
            for (String host : hosts) {
                if (checkIfNodeExistsForHost(host, installDir)) {
                    throw new CommandException(Strings.get("node.already.configured", host, installDir));
                }
            }
        }
        if(ok(archive)) {
            archive = SmartFile.sanitize(archive);
        }
    }

    @Override
    protected int executeCommand() throws CommandException {
        File zipFile = null;

        try {
            ArrayList<String> binDirFiles = new ArrayList<String>();
            precopy();
            zipFile = createZipFileIfNeeded(binDirFiles);
            copyToHosts(zipFile, binDirFiles);
        }
        catch (CommandException e) {
            throw e;
        }
        catch (Exception e) {
            throw new CommandException(e);
        }
        finally {
            if (!save && delete) {
                if (zipFile != null) {
                    if (!zipFile.delete())
                        zipFile.deleteOnExit();
                }
            }
        }

        return SUCCESS;
    }

    final String getInstallDir() {
        return installDir;
    }

    final String getArchiveName() {
        return archiveName;
    }

    final boolean getForce() {
        return force;
    }


    private File createZipFileIfNeeded(ArrayList<String> binDirFiles) throws IOException, ZipFileException {
        String baseRootValue = getSystemProperty(SystemPropertyConstants.PRODUCT_ROOT_PROPERTY);
        File installRoot = new File(baseRootValue);

        File zipFileLocation = null;
        File glassFishZipFile = null;

        if (archive != null) {
            archive = archive.replace('\\', '/');
            archiveName = archive.substring(archive.lastIndexOf("/") + 1, archive.length());
            zipFileLocation = new File(archive.substring(0, archive.lastIndexOf("/")));
            glassFishZipFile = new File(archive);
            if (glassFishZipFile.exists() && !create) {
                logger.finer("Found " + archive);
                delete = false;
                return glassFishZipFile;
            }
            else if (!zipFileLocation.canWrite()) {
                throw new IOException("Cannot write to " + archive);
            }
        }
        else {
            zipFileLocation = new File(".");
            if (!zipFileLocation.canWrite()) {
                zipFileLocation = new File(System.getProperty("java.io.tmpdir"));
            }
            glassFishZipFile = File.createTempFile("glassfish", ".zip", zipFileLocation);
            String filePath = glassFishZipFile.getCanonicalPath();
            filePath = filePath.replaceAll("\\\\", "/");
            archiveName = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length());
        }

        FileListerRelative lister = new FileListerRelative(installRoot);
        lister.keepEmptyDirectories();
        String[] files = lister.getFiles();

        List<String> resultFiles1 = Arrays.asList(files);
        ArrayList<String> resultFiles = new ArrayList<String>(resultFiles1);

        logger.finer("Number of files to be zipped = " + resultFiles.size());

        Iterator<String> iter = resultFiles.iterator();
        while (iter.hasNext()) {
            String fileName = iter.next();
            String fPath = fileName.substring(fileName.lastIndexOf("/") + 1);
            if (fPath.equals(glassFishZipFile.getName())) {
                logger.finer("Removing file = " + fileName);
                iter.remove();
                continue;
            }
            if (fileName.contains("domains") || fileName.contains("nodes")) {
                iter.remove();
            }
            else if (isFileWithinBinDirectory(fileName)) {
                binDirFiles.add(fileName);
            }
        }

        logger.finer("Final number of files to be zipped = " + resultFiles.size());

        String[] filesToZip = new String[resultFiles.size()];
        filesToZip = resultFiles.toArray(filesToZip);

        ZipWriter writer = new ZipWriter(FileUtils.safeGetCanonicalPath(glassFishZipFile), installRoot.toString(), filesToZip);
        writer.safeWrite();
        logger.info("Created installation zip " + FileUtils.safeGetCanonicalPath(glassFishZipFile));

        return glassFishZipFile;
    }

    /**
     * Determines if a file is under "bin" directory
     * @param file path to the file
     * @return true if file is under "bin" dir, false otherwise
     */
    private static boolean isFileWithinBinDirectory(String file) {
        boolean ret = false;
        File f = new File(file);
        String s = f.getParentFile().getName();
        if (s.equals("bin"))
            ret = true;
        return ret;
    }
    
    public static String toString(InputStream ins) throws IOException {
        StringWriter sw = new StringWriter();
        InputStreamReader reader = new InputStreamReader(ins);

        char[] buffer = new char[4096];
        int n;
        while ((n = reader.read(buffer)) >= 0)
            sw.write(buffer, 0, n);

        return sw.toString();
    }

    private String getIP(String host) throws CommandException {
        try {
            return InetAddress.getByName(host).getHostAddress();
        }
        catch (UnknownHostException e) {
            throw new CommandException(Strings.get("cantResolveIpAddress", host));
        }
    }
}
