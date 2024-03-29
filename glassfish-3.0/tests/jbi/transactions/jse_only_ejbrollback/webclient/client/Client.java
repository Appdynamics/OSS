/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2009 Sun Microsystems, Inc. All rights reserved.
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
 *
 */

package client;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.naming.InitialContext;
import javax.transaction.UserTransaction;
import javax.transaction.Status;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import javax.sql.DataSource;

import javax.xml.ws.*;
import endpoint.ejb.*;

public class Client extends HttpServlet {

       //@WebServiceRef(name="sun-web.serviceref/HelloEJBService")
       HelloEJBService service = new HelloEJBService();

       @Resource(mappedName="jdbc/__default") private DataSource ds;

       public void doGet(HttpServletRequest req, HttpServletResponse resp) 
		throws javax.servlet.ServletException {
           doPost(req, resp);
       }

       public void doPost(HttpServletRequest req, HttpServletResponse resp)
              throws javax.servlet.ServletException {
            UserTransaction ut = null;
            // Create Table with name CUSTOMER_rb. This name will be used in the EJB
	    String tableName = "CUSTOMER_rb";
	    Connection con = null;
            try {
	        con = ds.getConnection();
		con.setAutoCommit(true);
	        createTable(con, tableName);
                ut = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
                ut.begin();

                System.out.println(" Service is :" + service);
		Hello port = service.getHelloEJBPort();
                
		String ret = port.sayHello("Appserver Tester !");
                System.out.println("Return value from webservice:"+ret);
		System.out.println("**** rollbacking transaction");
	//	ut.rollback();
		ut.setRollbackOnly();
		if(ut.getStatus() != Status.STATUS_MARKED_ROLLBACK) {
                    ret += "FAILED";
		}
		if(isDataPresent(con, tableName)) {
                    ret += "FAILED";
                }
                PrintWriter out = resp.getWriter();
                resp.setContentType("text/html");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>TestServlet</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<p>");
                out.println("So the RESULT OF EJB webservice IS :");
                out.println("</p>");
                out.println("[" + ret + "]");
                out.println("</body>");
                out.println("</html>");
		dropTable(con, tableName);
            } catch(Exception e) {
                e.printStackTrace();
	    }finally {
	         try {
                    if(con != null) con.close();
                 } catch (SQLException se) {}
	    }
       }

       // use this table in the EJB webservice
       private void createTable(Connection con, String tableName) throws Exception {
	    System.out.println("**** auto commit = " + con.getAutoCommit());
            PreparedStatement pStmt =
            con.prepareStatement("CREATE TABLE "+tableName+" (NAME VARCHAR(30) NOT NULL PRIMARY KEY, EMAIL VARCHAR(30))");
            pStmt.executeUpdate();
       }

       private void dropTable(Connection con, String tableName) throws Exception {
            PreparedStatement pStmt = con.prepareStatement("DROP TABLE "+tableName);
            pStmt.executeUpdate();
       }

       // Check whether the EJB webservice has updated the data in the table.
       private boolean isDataPresent(Connection con, String tableName) throws Exception {
	    PreparedStatement pStmt = con.prepareStatement("SELECT NAME, EMAIL FROM "+tableName);
	    ResultSet rs = pStmt.executeQuery();
	    while(rs.next()) {
		String db_Name  = rs.getString(1);
		String db_Email = rs.getString(2);
		System.out.println("NAME="+db_Name+", EMAIL="+db_Email);
		rs.close();
		return true;
	    }
	    return false;
       }
}
