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

package org.glassfish.deployment.common;

import com.sun.logging.LogDomains;
import java.io.File;
import java.net.URI;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.glassfish.api.deployment.DeploymentContext;

/**
 * Records artifacts generated during deployment that need
 * to be included inside the generated app client JAR so they are accessible
 * on the runtime classpath.
 * <p>
 * An example: jaxrpc classes from web services deployment
 * <p>
 * Important node:  Artifacts added to this manager are NOT downloaded to
 * the client as separate files.  In contrast, then are added to the
 * generated client JAR file.  That generated JAR, along with other JARs needed
 * by the client, are downloaded.
 * <p>
 * A Deployer that needs to request for files to be downloaded to the client
 * as part of the payload in the http command response should instead use
 * DownloadableArtifactsManager.
 *
 * An instance of this class can be stored in the deployment
 * context's transient app metadata so the various deployers can add to the
 * same collection and so the app client deployer can find it and
 * act on its contents.
 * <p>
 * Because other modules should add their artifacts before the app client
 * deployer processes them, the <code>add</code> methods do not permit
 * further additions once the {@link #artifacts} method has been invoked.
 *
 * @author tjuinn
 */
public class ClientArtifactsManager {

    private boolean isArtifactSetConsumed = false;
    
    private static final String CLIENT_ARTIFACTS_KEY = "ClientArtifacts";
    
    private static final Logger logger =
            LogDomains.getLogger(ClientArtifactsManager.class, LogDomains.DPL_LOGGER);
    
    private final Map<URI,Artifacts.FullAndPartURIs> artifacts =
            new HashMap<URI,Artifacts.FullAndPartURIs>();

    /**
     * Retreives the client artifacts store from the provided deployment 
     * context, creating one and storing it back into the DC if none is 
     * there yet.
     * 
     * @param dc the deployment context to hold the ClientArtifactsManager object
     * @return the ClientArtifactsManager object from the deployment context (created
     * and stored in the DC if needed)
     */
    public static ClientArtifactsManager get(final DeploymentContext dc) {
        synchronized (dc) {
            ClientArtifactsManager result = dc.getTransientAppMetaData(
                    CLIENT_ARTIFACTS_KEY, ClientArtifactsManager.class);

            if (result == null) {
                result = new ClientArtifactsManager();
                dc.addTransientAppMetaData(CLIENT_ARTIFACTS_KEY, result);
            }
            return result;
        }
    }

    /**
     * Adds a new artifact to the collection of artifacts to be included in the
     * client facade JAR file so they can be delivered to the client during a
     * download.
     *
     * @param baseURI absolute URI of the base directory within which the
     * artifact lies
     * @param artifactURI absolute or relative URI for the artifact itself
     * @throws IllegalStateException if invokes after the accumulated artifacts have been consumed
     */
    public void add(final URI baseURI, final URI artifactURI) {
        final URIPair uris = new URIPair(baseURI, artifactURI);
        if (isArtifactSetConsumed) {
            throw new IllegalStateException(
                    formattedString("enterprise.deployment.backend.appClientArtifactOutOfOrder",
                        uris.absoluteURI.toASCIIString())
                    );
        } else {
            Artifacts.FullAndPartURIs existingArtifact =
                    artifacts.get(uris.relativeURI);
            if (existingArtifact != null) {
                throw new IllegalArgumentException(
                        formattedString("enterprise.deployment.backend.appClientArtifactCollision",
                            uris.relativeURI.toASCIIString(),
                            uris.absoluteURI.toASCIIString(),
                            existingArtifact.getFull().toASCIIString())
                        );
            }
            final File f = new File(uris.absoluteURI);
            if ( ! f.exists() || ! f.canRead()) {
                throw new IllegalArgumentException(
                        formattedString("enterprise.deployment.backend.appClientArtifactMissing",
                            uris.relativeURI.toASCIIString(),
                            uris.absoluteURI.toASCIIString())
                        );
            }
            final Artifacts.FullAndPartURIs newArtifact =
                    new Artifacts.FullAndPartURIs(
                    uris.absoluteURI, uris.relativeURI);
            artifacts.put(uris.relativeURI, newArtifact);
        }
    }

    /**
     * Adds a new artifact to the collection of artifacts to be added to the
     * client facade JAR file so they can be delivered to the client during a
     * download.
     *
     * @param baseFile File for the base directory within which the artifact lies
     * @param artifactFile File for the artifact itself
     * @throws IllegalStateException if invoked after the accumulated artifacts have been consumed
     */
    public void add(final File baseFile, final File artifactFile) {
        add(baseFile.toURI(), artifactFile.toURI());
    }

    /**
     * Adds all artifacts in Collection to those to be added to the client
     * facade JAR.
     *
     * @param baseFile File for the base directory within which each artifact lies
     * @param artifactFiles Collection of File objects for the artifacts to be included
     * @throws IllegalStateException if invoked after the accumulated artifacts have been consumed
     */
    public void addAll(final File baseFile, final Collection<File> artifactFiles) {
        for (File f : artifactFiles) {
            add(baseFile, f);
        }
    }

    public boolean contains(final URI baseURI, final URI artifactURI) {
        return artifacts.containsKey(artifactURI);
    }

    public boolean contains(final File baseFile, final File artifactFile) {
        return contains(baseFile.toURI(), artifactFile.toURI());
    }
    /**
     * Returns the set (in unmodifiable form) of FullAndPartURIs for the
     * accumulated artifacts.
     * <p>
     * Note: Intended for use only by the app client deployer.
     *
     * @return all client artifacts reported by various deployers
     */
    public Collection<Artifacts.FullAndPartURIs> artifacts() {
        isArtifactSetConsumed = true;
        return Collections.unmodifiableCollection(artifacts.values());
    }

    private String formattedString(final String key, final Object... args) {
        final String format = logger.getResourceBundle().getString(key);
        return MessageFormat.format(format, args);
    }

    private static class URIPair {
        private final URI relativeURI;
        private final URI absoluteURI;

        private URIPair(final URI baseURI, final URI artifactURI) {
            if (artifactURI.isAbsolute()) {
                absoluteURI = artifactURI;
                relativeURI = baseURI.relativize(absoluteURI);
            } else {
                relativeURI = artifactURI;
                absoluteURI = baseURI.resolve(relativeURI);
            }
        }
    }
}
