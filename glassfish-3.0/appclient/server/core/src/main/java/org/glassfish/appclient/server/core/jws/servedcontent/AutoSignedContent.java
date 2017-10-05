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

package org.glassfish.appclient.server.core.jws.servedcontent;

import java.io.File;
import java.io.IOException;

/**
 * Represents otherwise fixed content that must be automatically signed
 * if it does not yet exist or if the underlying unsigned file has changed
 * since the signed version was created.
 *
 * @author tjquinn
 */
public class AutoSignedContent extends Content.Adapter implements StaticContent {

    private final File unsignedFile;
    private final File signedFile;
    private final String userProvidedAlias;
    private final ASJarSigner jarSigner;

    public AutoSignedContent(final File unsignedFile,
            final File signedFile, 
            final String userProvidedAlias,
            final ASJarSigner jarSigner) {
        this.unsignedFile = unsignedFile;
        this.signedFile = signedFile;
        this.userProvidedAlias = userProvidedAlias;
        this.jarSigner = jarSigner;
    }

    /**
     * Returns a File object for where the signed file will be once it is
     * created.  Note that any use of the File returned by this method MUST
     * be preceded by an invocation of isAvailable.
     *
     * @return File for where the signed copy of the file will reside
     * @throws IOException
     */
    public File file() throws IOException {
        return signedFile;
    }

    /**
     * Reports whether the signed content is available.  As a side-effect, this
     * method will create the signed file if it does not exist or is obsolete.
     * @return
     * @throws IOException
     */
    @Override
    public boolean isAvailable() throws IOException {
        if ( ! isSignedFileReady()) {
            try {
                createSignedFile();
            } catch (Exception e) {
                throw new IOException(e);
            }
        }
        return super.isAvailable();
    }


    private boolean isSignedFileReady() {
        return signedFile.exists() &&
                (signedFile.lastModified() >= unsignedFile.lastModified());
    }

    private void createSignedFile() throws Exception {
        /*
         * The code that instantiated this auto-signed content decides where
         * the signed file will reside.  It might not have wanted to create
         * the containing directory ahead of time.
         */
        signedFile.getParentFile().mkdirs();
        jarSigner.signJar(unsignedFile, signedFile, userProvidedAlias);
    }

    @Override
    public String toString() {
        return "AutoSignedContent:" + signedFile.getAbsolutePath();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AutoSignedContent other = (AutoSignedContent) obj;
        if (this.unsignedFile != other.unsignedFile && (this.unsignedFile == null || !this.unsignedFile.equals(other.unsignedFile))) {
            return false;
        }
        if (this.signedFile != other.signedFile && (this.signedFile == null || !this.signedFile.equals(other.signedFile))) {
            return false;
        }
        if ((this.userProvidedAlias == null) ? (other.userProvidedAlias != null) : !this.userProvidedAlias.equals(other.userProvidedAlias)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + (this.unsignedFile != null ? this.unsignedFile.hashCode() : 0);
        hash = 83 * hash + (this.signedFile != null ? this.signedFile.hashCode() : 0);
        hash = 83 * hash + (this.userProvidedAlias != null ? this.userProvidedAlias.hashCode() : 0);
        return hash;
    }
}
