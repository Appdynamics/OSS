/*
 *
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2008 Sun Microsystems, Inc. All rights reserved.
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

package test.mdb;

import org.testng.annotations.Test;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import test.admincli.util.*;

@Test(sequential = true)
public class MDBTests {
    private boolean execReturn = false;
    private String APPCLIENT = System.getProperty("APPCLIENT");
    private String ASADMIN = System.getProperty("ASADMIN");
    private String cmd;
    private String mdbApp= "ejb-ejb30-hello-mdbApp";

    @Parameters({ "BATCH_FILE1" })
    @Test
    public void createJMSRscTest(String batchFile1) throws Exception {
        cmd = ASADMIN + " multimode --file " + batchFile1;
        execReturn = RtExec.execute(cmd);
        Assert.assertEquals(execReturn, true, "Create JMS resource failed ...");
    }
    
    @Parameters({ "MDB_APP_DIR" })
    @Test(dependsOnMethods = { "createJMSRscTest" })
    public void deployJMSAppTest(String mdbAppDir) throws Exception {
        cmd = ASADMIN + " deploy  --retrieve=" + mdbAppDir
                + " " + mdbAppDir + mdbApp + ".ear ";
        //System.out.println("CMD = "+cmd);
        execReturn = RtExec.execute(cmd);
        Assert.assertEquals(execReturn, true, "Deploy the mdb app failed ... ");
    }

    @Parameters({ "MDB_APP_DIR" })
    @Test(dependsOnMethods = { "deployJMSAppTest" })
    public void runJMSAppTest(String mdbAppDir) throws Exception {
        cmd = APPCLIENT+" -client "+mdbAppDir+mdbApp+"Client.jar ";
//           + "-name ejb-ejb30-hello-mdbClient " ;
        execReturn = RtExec.execute(cmd);
        Assert.assertEquals(execReturn, true, "Run appclient against JMS APP failed ...");
    }

    @Test (dependsOnMethods = { "runJMSAppTest" })
    public void undeployJMSAppTest() throws Exception {
        cmd = ASADMIN + " undeploy " + mdbApp;
        //System.out.println("CMD = "+cmd);
        execReturn = RtExec.execute(cmd);
        Assert.assertEquals(execReturn, true, "UnDeploy the mdb app failed ... ");
    }

    @Parameters({ "BATCH_FILE2" })
    @Test (dependsOnMethods = { "undeployJMSAppTest" })
    public void deleteJMSRscTest(String batchFile2) throws Exception {
        cmd = ASADMIN + " multimode --file " + batchFile2;
        execReturn = RtExec.execute(cmd);
        Assert.assertEquals(execReturn, true, "Delete JMD Resource failed ...");
    }
    
}
