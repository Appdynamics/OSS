/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
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

/*
 * DirectoryScanner.java
 *
 *
 * Created on February 19, 2003, 10:17 AM
 */

package org.glassfish.deployment.autodeploy;

import com.sun.enterprise.util.LocalStringManagerImpl;
import java.io.File;
import java.util.Set;
import java.util.HashSet;
import java.util.logging.Logger;
import java.util.logging.Level;
import com.sun.logging.LogDomains;
import org.glassfish.deployment.common.DeploymentUtils;

/**
 * Implementation of Directory scanner for autodeployment  </br>
 * Providing functionality for scanning the input source directory  </br>
 * and return the list of deployable components for autodeployment.</br>
 * Provide the list of deployable modules/application, depending upon the "type" entry </br>
 * passed to getAllDeployableEntity(File autodeployDir, String type).
 *
 *@author vikas
 */
public class AutoDeployDirectoryScanner implements DirectoryScanner{
    
    private static final Logger sLogger=LogDomains.getLogger(DeploymentUtils.class, LogDomains.DPL_LOGGER);
    private static final LocalStringManagerImpl localStrings = new LocalStringManagerImpl(AutoDeployDirectoryScanner.class);
    
    public AutoDeployDirectoryScanner() {
    }
    
     public void deployedEntity(File autodeployDir, File deployedEntity) {
         try {
         AutoDeployedFilesManager adfm = AutoDeployedFilesManager.loadStatus(autodeployDir);
         adfm.setDeployedFileInfo(deployedEntity);
         adfm.writeStatus();
         } catch (Exception e) {
             printException(e);
             // Do nothing
         }

     }
     
     public void undeployedEntity(File autodeployDir, File undeployedEntity) {
         try {
         AutoDeployedFilesManager adfm = AutoDeployedFilesManager.loadStatus(autodeployDir);
         adfm.deleteDeployedFileInfo(undeployedEntity);
         adfm.writeStatus();
         } catch (Exception e) {
             printException(e);
             // Do nothing 
         }
     }
     
    /**
     * return true if any new deployable entity is  present in autodeployDir
     * @param autodeployDir
     * @return
     */
    public boolean hasNewDeployableEntity(File autodeployDir) {
        boolean newFilesExist=false;
            try {
                AutoDeployedFilesManager adfm = AutoDeployedFilesManager.loadStatus(autodeployDir);
                if(adfm.getFilesForDeployment(getListOfFiles(autodeployDir)).length > 0) {
                    //atleast one new file is there
                     newFilesExist=true;
                }
            } catch (Exception e) {
                printException(e);
                return false;
            }
        
        return newFilesExist;        
        
    }
    // this should never be called from system dir autodeploy code...
    public File[] getAllFilesForUndeployment(File autodeployDir, boolean includeSubdir) {

        try {
            AutoDeployedFilesManager adfm = AutoDeployedFilesManager.loadStatus(autodeployDir);
            return adfm.getFilesForUndeployment(getListOfFiles(autodeployDir, includeSubdir));
            } catch (Exception e) {
                printException(e);
                return new File[0];
            }
    }    
         
    /**
     * Get the list of all deployable files
     * @param autodeployDir
     * @return  */
    public File[] getAllDeployableModules(File autodeployDir, boolean includeSubDir) {
        
        AutoDeployedFilesManager adfm = null;
        try {
        adfm = AutoDeployedFilesManager.loadStatus(autodeployDir);
        } catch (Exception e) {
            printException(e);
            return new File[0];
        }
        
        return adfm.getFilesForDeployment(getListOfFiles(autodeployDir, includeSubDir));
    }

    protected void printException(Exception e) {
        sLogger.log(Level.SEVERE, e.getMessage(), e);
    }
    
    protected File[] getListOfFiles(File dir) {
        return getListOfFiles(dir, false);
    }
  
    protected File[] getListOfFiles(File dir, boolean includeSubDir) {
        return getListOfFilesAsSet(dir, includeSubDir).toArray(new File[0]);
    }
            
            
    static Set<File> getListOfFilesAsSet(File dir, boolean includeSubDir) {
        Set<File> result = new HashSet<File>();
        File[] dirFiles = dir.listFiles();
        for (File dirFile : dirFiles) {
            String name = dirFile.getName();
            String fileType = name.substring(name.lastIndexOf(".") + 1);
            if ( ! dirFile.isDirectory()) {
                if (fileType != null && !fileType.equals("") &&
                        ! typeIsMarkerType(fileType)) {
                    result.add(dirFile);
                    continue;
                }
            } else {
                if (! dirFile.getName().equals(AutoDeployer.STATUS_SUBDIR_PATH)) {
                    if (includeSubDir) {
                        result.addAll(getListOfFilesAsSet(dirFile, true));
                    } else {
                        result.add(dirFile);
                    }
                }
            }
        }
        return result;
    }    
    
    private static boolean typeIsMarkerType(String fileType) {
        for (String markerSuffix : AutoDeployConstants.MARKER_FILE_SUFFIXES) {
            if (fileType.endsWith(markerSuffix)) {
                return true;
            }
        }
        return false;
    }
}
