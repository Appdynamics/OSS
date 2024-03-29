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
package com.sun.enterprise.security.cli;

import java.util.Enumeration;
import java.util.List;

import org.glassfish.api.admin.AdminCommand;
import org.glassfish.api.admin.AdminCommandContext;
import org.glassfish.api.I18n;
import org.glassfish.api.Param;
import org.glassfish.api.ActionReport;
import org.jvnet.hk2.annotations.Service;
import org.jvnet.hk2.annotations.Scoped;
import org.jvnet.hk2.annotations.Inject;
import org.jvnet.hk2.component.PerLookup;
import com.sun.enterprise.config.serverbeans.Configs;
import com.sun.enterprise.config.serverbeans.Config;
import com.sun.enterprise.util.LocalStringManagerImpl;
import com.sun.enterprise.config.serverbeans.AuthRealm;
import com.sun.enterprise.security.auth.realm.file.FileRealm;
import com.sun.enterprise.security.auth.realm.BadRealmException;
import com.sun.enterprise.security.auth.realm.NoSuchRealmException;
import org.jvnet.hk2.config.types.Property;
import com.sun.enterprise.config.serverbeans.SecurityService;

/**
 * Change Admin Password Command
 *
 * Usage: change-admin-password [--user admin_user] [--terse=false] 
 *        [--echo=false] [--host localhost] [--port 4848|4849] 
 *        [--secure | -s]
 *
 * @author Nandini Ektare
 */

@Service(name="change-admin-password")
@Scoped(PerLookup.class)
@I18n("change.admin.password")
public class ChangeAdminPassword implements AdminCommand {
    
    final private static LocalStringManagerImpl localStrings = 
        new LocalStringManagerImpl(ChangeAdminPassword.class);    

    @Param(name="password", password=true)
    String oldpassword;
   
    @Param(name="newpassword", password=true)
    String newpassword;

    @Param(name="username", primary=true)
    String userName;

    @Inject
    Configs configs;

    private final static String ADMIN_REALM = "admin-realm";
    /**
     * Executes the command with the command parameters passed as Properties
     * where the keys are the paramter names and the values the parameter values
     *
     * @param context information
     */
    public void execute(AdminCommandContext context) {
        
        final ActionReport report = context.getActionReport();

        List <Config> configList = configs.getConfig();
        Config config = configList.get(0);
        SecurityService securityService = config.getSecurityService();

        AuthRealm fileAuthRealm = null;        
        for (AuthRealm authRealm : securityService.getAuthRealm()) {            
            if (authRealm.getName().equals(ADMIN_REALM)) {                
                fileAuthRealm = authRealm;            
                break;
            }
        }        

        if (fileAuthRealm == null) {
            report.setMessage(localStrings.getLocalString(
                "change.admin.password.adminrealmnotfound", "Server " +
                "Error: There is no admin realm to perform this operation"));
            report.setActionExitCode(ActionReport.ExitCode.FAILURE);
            return;                                            
        }
        
        // Get FileRealm class name, match it with what is expected.
        String fileRealmClassName = fileAuthRealm.getClassname();
        
        // Report error if provided impl is not the one expected
        if (fileRealmClassName != null && 
            !fileRealmClassName.equals(
                "com.sun.enterprise.security.auth.realm.file.FileRealm")) {
            report.setMessage(
                localStrings.getLocalString(
                    "change.admin.password.adminrealmnotsupported",
                    "Configured admin realm is not supported."));
            report.setActionExitCode(ActionReport.ExitCode.FAILURE);
            return;                
        }

        // ensure we have the file associated with the authrealm
        String keyFile = null;
        for (Property fileProp : fileAuthRealm.getProperty()) {
            if (fileProp.getName().equals("file"))
                keyFile = fileProp.getValue();
        }
        if (keyFile == null) {
            report.setMessage(
                localStrings.getLocalString(
                    "change.admin.password.keyfilenotfound",
                    "There is no physical file associated with admin realm"));
            report.setActionExitCode(ActionReport.ExitCode.FAILURE);
            return;                                            
        }
                            
        // We have the right impl so let's get to updating existing user 
        FileRealm fr = null;
        try {
            fr = new FileRealm(keyFile);            
        } catch(BadRealmException e) {
            report.setMessage(
                localStrings.getLocalString(
                    "change.admin.password.realmcorrupted",
                    "Configured admin realm is corrupted.") +
                "  " + e.getLocalizedMessage());
            report.setActionExitCode(ActionReport.ExitCode.FAILURE);
            report.setFailureCause(e);
            return;
        } catch(NoSuchRealmException e) {
            report.setMessage(
                localStrings.getLocalString(
                    "change.admin.password.realmnotsupported",
                    "Configured admin realm does not exist.") +
                "  " + e.getLocalizedMessage());
            report.setActionExitCode(ActionReport.ExitCode.FAILURE);
            report.setFailureCause(e);
            return;
        }

        //now updating admin user password
        try {
            Enumeration en = fr.getGroupNames(userName);            
            int size = 0;
            while (en.hasMoreElements()) { 
                size++; 
                en.nextElement(); 
            }            
            String[] groups = new String[size];            
            en = fr.getGroupNames(userName);            
            for (int i = 0; i < size; i++) {
                groups[i] = (String) en.nextElement();
            }
            fr.updateUser(userName, newpassword, groups);
            fr.writeKeyFile(keyFile);
            report.setActionExitCode(ActionReport.ExitCode.SUCCESS);            
        } catch (Exception e) {
            report.setMessage(
                localStrings.getLocalString(
                    "change.admin.password.userupdatefailed",
                    "Password change failed for user named {0}", userName) +
                "  " + e.getLocalizedMessage());
            report.setActionExitCode(ActionReport.ExitCode.FAILURE);
            report.setFailureCause(e);
        }        
    }        
}
