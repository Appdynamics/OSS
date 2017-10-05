/*
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
package com.sun.enterprise.security.cli;

import java.util.List;
import java.util.ArrayList;

import org.glassfish.api.admin.AdminCommand;
import org.glassfish.api.admin.AdminCommandContext;
import org.glassfish.api.I18n;
import org.glassfish.api.Param;
import org.glassfish.api.ActionReport;
import org.jvnet.hk2.annotations.Service;
import org.jvnet.hk2.annotations.Scoped;
import org.jvnet.hk2.annotations.Inject;
import org.jvnet.hk2.component.PerLookup;
import org.jvnet.hk2.config.types.Property;
import com.sun.enterprise.config.serverbeans.Configs;
import com.sun.enterprise.config.serverbeans.Config;
import com.sun.enterprise.util.LocalStringManagerImpl;
import com.sun.enterprise.config.serverbeans.AuthRealm;
import com.sun.enterprise.security.auth.realm.file.FileRealm;
import com.sun.enterprise.security.auth.realm.BadRealmException;
import com.sun.enterprise.security.auth.realm.NoSuchRealmException;
import com.sun.enterprise.security.auth.realm.Realm;
import com.sun.enterprise.config.serverbeans.SecurityService;
import com.sun.enterprise.security.common.Util;

/**
 * Create File User Command
 * Usage: create-file-user [--terse=false] [--echo=false] [--interactive=true] 
 *        [--host localhost] [--port 4848|4849] [--secure | -s] 
 *        [--user admin_user] [--userpassword admin_passwd] 
 *        [--passwordfile file_name] [--groups user_groups[:user_groups]*] 
 *        [--authrealmname authrealm_name] [--target target(Default server)] 
 *        username 
 *
 * @author Nandini Ektare
 */

@Service(name="create-file-user")
@Scoped(PerLookup.class)
@I18n("create.file.user")
public class CreateFileUser implements AdminCommand {
    
    final private static LocalStringManagerImpl localStrings = 
        new LocalStringManagerImpl(CreateFileUser.class);    

    @Param(name="groups", optional=true, separator=':')
    List<String> groups = new ArrayList<String>(0); //by default, an empty list is better than a null

    @Param(name="userpassword", password=true)
    String userpassword;

    @Param(name="authrealmname", optional=true)
    String authRealmName;
    
    @Param(optional=true)
    String target;

    @Param(name="username", primary=true)
    String userName;

    @Inject
    Configs configs;

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

        // ensure we have the file authrealm
        AuthRealm fileAuthRealm = null;
        
        if (authRealmName == null) 
            authRealmName = securityService.getDefaultRealm();        
        
        for (AuthRealm authRealm : securityService.getAuthRealm()) {            
            if (authRealm.getName().equals(authRealmName)) {
                fileAuthRealm = authRealm;            
                break;
            }
        }       
                
        if (fileAuthRealm == null) {
            report.setMessage(localStrings.getLocalString(
                "create.file.user.filerealmnotfound",
                "File realm {0} does not exist", 
                authRealmName));
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
                    "create.file.user.realmnotsupported",
                    "Configured file realm {0} is not supported.", 
                    fileRealmClassName));
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
                localStrings.getLocalString("create.file.user.keyfilenotfound",
                "There is no physical file associated with this file realm {0} ", 
                authRealmName));
            report.setActionExitCode(ActionReport.ExitCode.FAILURE);
            return;                                            
        }
        // Now get all inputs ready. userid and groups are straightforward but
        // password is tricky. It is stored in the file passwordfile passed 
        // through the CLI options. It is stored under the name 
        // AS_ADMIN_USERPASSWORD. Fetch it from there.
        String password = userpassword; // fetchPassword(report);
        if (password == null) {
            report.setMessage(localStrings.getLocalString(
               "create.file.user.keyfilenotreadable", "Password for user {0} " +
               "has to be specified in --userpassword option or supplied " +
               "through AS_ADMIN_USERPASSWORD property in the file specified " +
               "in --passwordfile option", userName));
            report.setActionExitCode(ActionReport.ExitCode.FAILURE);
            return;
        }
        
        // We have the right impl so let's get to checking existing user and 
        // adding one if one does not exist

        FileRealm fr = null;
        try {
            fr = new FileRealm(keyFile);            
        } catch(BadRealmException e) {
            report.setMessage(
                localStrings.getLocalString(
                    "create.file.user.realmcorrupted",
                    "Configured file realm {0} is corrupted.", authRealmName) +
                "  " + e.getLocalizedMessage());
            report.setActionExitCode(ActionReport.ExitCode.FAILURE);
            report.setFailureCause(e);
            return;
        } catch(NoSuchRealmException e) {
            report.setMessage(
                localStrings.getLocalString(
                    "create.file.user.realmnotsupported",
                    "Configured file realm {0} does not exist.", authRealmName) +
                "  " + e.getLocalizedMessage());
            report.setActionExitCode(ActionReport.ExitCode.FAILURE);
            report.setFailureCause(e);
            return;
        }
        
        // now adding user
        try {
            CreateFileUser.handleAdminGroup(authRealmName, groups);
            String[] groups1 = groups.toArray(new String[groups.size()]); 
            fr.addUser(userName, password, groups1);
            if(Util.isEmbeddedServer()) {
                fr.writeKeyFile(Util.writeConfigFileToTempDir(keyFile).getAbsolutePath());
            }
            else {
                fr.writeKeyFile(keyFile);
            }
            refreshRealm(authRealmName);
        } catch (Exception e) {
            report.setMessage(
                localStrings.getLocalString("create.file.user.useraddfailed",
                "Adding User {0} to the file realm {1} failed", 
                userName, authRealmName) + "  " + e.getLocalizedMessage() );
            report.setActionExitCode(ActionReport.ExitCode.FAILURE);
            report.setFailureCause(e);
        }        
    }

    /* private String fetchPassword(ActionReport report) {
       String password = null;
       if (userpassword != null && passwordFile != null)
           return password;
       if (userpassword != null)
           password = userpassword;
       if (passwordFile != null) {
           File passwdFile = new File(passwordFile);
           InputStream is = null;
           try {
               is = new BufferedInputStream(new FileInputStream(passwdFile));
               Properties prop = new Properties();
               prop.load(is);
               for (Enumeration e=prop.propertyNames(); e.hasMoreElements();) {
                   String entry = (String)e.nextElement();
                   if (entry.equals("AS_ADMIN_USERPASSWORD")) {
                       password = prop.getProperty(entry);
                       break;
                   }
               }
           } catch(Exception e) {
               report.setFailureCause(e);
           } finally {
               try {
                   if (is != null)
                       is.close();
               } catch(final Exception ignore){}
           }
       }
       return password;
   } */
   public static void refreshRealm(String realmName){
      if(realmName != null && realmName.length()  >0){
         try{
            Realm realm = Realm.getInstance(realmName);
       
	    if(realm != null){
	       realm.refresh();
	    }
         }catch(com.sun.enterprise.security.auth.realm.NoSuchRealmException nre){
            //	    _logger.fine("Realm: "+realmName+" is not configured");
	 }catch(com.sun.enterprise.security.auth.realm.BadRealmException bre){
            //	    _logger.fine("Realm: "+realmName+" is not configured");
	 }
      }
  }
    static void handleAdminGroup(String lr, List<String> lg) {
        String fr = "admin-realm";   //this should be a constant defined at a central place -- the name of realm for admin
        String fg = "asadmin";       //this should be a constant defined at a central place -- fixed name of admin group
        if (fr.equals(lr)) {
            lg.clear();             //basically, we are ignoring the group specified on command line when it's admin realm
            lg.add(fg);
        }
    }
}
