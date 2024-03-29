/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *
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

package org.glassfish.appclient.server.core.jws;

import com.sun.logging.LogDomains;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import org.glassfish.appclient.server.core.jws.servedcontent.Content;
import org.glassfish.appclient.server.core.jws.servedcontent.DynamicContent;
import org.glassfish.appclient.server.core.jws.servedcontent.FixedContent;
import org.glassfish.appclient.server.core.jws.servedcontent.SimpleDynamicContentImpl;
import org.glassfish.appclient.server.core.jws.servedcontent.StaticContent;

/**
 * Encapsulates the logic related to choosing which of the two possible
 * config files to use and handling the data within the selected file.
 */
class ClientJNLPConfigData {

    private static final String CONFIG_FILE_NAME = "client-jnlp-config.properties";
    private File previousConfigFileUsed = null;
    private File configFileFromDomain;
    private File configFileFromInstall;
    private long lastModified = -1;

    private Logger logger = LogDomains.getLogger(ClientJNLPConfigData.class, LogDomains.ACC_LOGGER);

    /**
     * Easier for the caller if we return empty lists rather than nulls if we cannot
     * locate either config file.
     */
    private List<XPathToDeveloperProvidedContentRefs> xPathsToDevContentRefs = Collections.EMPTY_LIST;
    private List<CombinedXPath> xPathsToCombinedContent = Collections.EMPTY_LIST;

    
    ClientJNLPConfigData(final File installConfigDir, final File domainConfigDir) {
        super();
        configFileFromInstall = new File(installConfigDir, CONFIG_FILE_NAME);
        configFileFromDomain = new File(domainConfigDir, CONFIG_FILE_NAME);
        ensureCurrent();
    }

    /**
     * Makes sure that the in-memory data is up-to-date wrt the on-disk
     * properties file - whether that's the domain's config file or the
     * installation's config file.
     */
    private void ensureCurrent() {
        final File configFile = chooseConfigFile();
        if (configFile == null) {
            lastModified = -1;
            xPathsToDevContentRefs = Collections.EMPTY_LIST;
            xPathsToCombinedContent = Collections.EMPTY_LIST;
            return;
        }
        if (lastModified < configFile.lastModified() || previousConfigFileUsed != configFile) {
            processConfigFile(configFile);
        }
    }

    /**
     * Chooses which config file to use, preferring the domain's config
     * file (if present and readable) and falling back to the
     * installation's file.
     * <p>
     * The method logs a warning if finds but cannot read the domain's
     * config file (we assume in that case that the administrator intended for that file
     * to be used but, alas, we cannot read it).  It logs a severe error
     * if there is no JNLP config file for the domain and it cannot locate
     * or read the installation's file, which should always be usable.
     *
     * @return the config file to be used; null if neither the domain's nor
     * the installation's config file exists and is readable
     */
    private File chooseConfigFile() {
        File result = checkExistenceAndReadability(configFileFromDomain, Level.WARNING);
        if (result == null) {
            result = checkExistenceAndReadability(configFileFromInstall, Level.SEVERE);
            if (result == null && !configFileFromInstall.exists()) {
                /*
                 * Complain separately if the installation's config file
                 * does not exist.
                 */
                logErrorNonExistentConfig(configFileFromInstall);
            }
        }
        if (result != null && previousConfigFileUsed != null && result != previousConfigFileUsed) {
            logUsingDifferentConfigFile(result);
        }
        return result;
    }

    private File checkExistenceAndReadability(final File f, final Level logLevel) {
        File result = null;
        if (f.exists()) {
            if (f.canRead()) {
                result = f;
            } else {
                logErrorConfigExistsButCannotRead(logLevel, f);
            }
        }
        return result;
    }

    private void logErrorConfigExistsButCannotRead(final Level level, final File f) {
        logger.log(level, "enterprise.deployment.appclient.jws.clientJNLPConfigFileUnreadable", f.getAbsolutePath());
    }

    private void logErrorNonExistentConfig(final File f) {
        logger.log(Level.SEVERE, "enterprise.deployment.appclient.jws.clientJNLPConfigFileMissing", f.getAbsolutePath());
    }

    private void logUsingDifferentConfigFile(final File f) {
        logger.log(Level.FINE, "enterprise.deployment.appclient.jws.clientJNLPConfigChangeFile", f.getAbsolutePath());
    }

    List<XPathToDeveloperProvidedContentRefs> xPathsToDevContentRefs() {
        ensureCurrent();
        return xPathsToDevContentRefs;
    }

    List<CombinedXPath> xPathsToCombinedContent() {
        ensureCurrent();
        return xPathsToCombinedContent;
    }

    private void processConfigFile(final File configFile) {
        final Properties p = new Properties();
        try {
            p.load(new BufferedInputStream(new FileInputStream(configFile)));
            final List<XPathToDeveloperProvidedContentRefs> newRefsToContent = prepareRefsToContent(p);
            final List<CombinedXPath> newCombinedXPaths = prepareCombinedXPaths(p);
            /*
             * Now that everything seems to have worked, assign the newly-computed
             * values to the fields.
             */
            xPathsToDevContentRefs = newRefsToContent;
            xPathsToCombinedContent = newCombinedXPaths;
            lastModified = configFile.lastModified();
            previousConfigFileUsed = configFile;
            logger.log(Level.FINE, "enterprise.deployment.appclient.jws.clientJNLPConfigLoad", configFile.getAbsolutePath());
        } catch (Exception e) {
            final String fmt = logger.getResourceBundle().getString("enterprise.deployment.appclient.jws.clientJNLPConfigProcError");
            final String msg = MessageFormat.format(fmt, configFile.getAbsolutePath());
            logger.log(Level.SEVERE, msg, e);
        }
    }

    private List<XPathToDeveloperProvidedContentRefs> prepareRefsToContent(final Properties p) {
        final List<XPathToDeveloperProvidedContentRefs> result =
                XPathToDeveloperProvidedContentRefs.parse(p);
        return result;
    }

    private List<CombinedXPath> prepareCombinedXPaths(final Properties p) {
        final List<CombinedXPath> result = CombinedXPath.parse(p);
        return result;
    }

    
}
