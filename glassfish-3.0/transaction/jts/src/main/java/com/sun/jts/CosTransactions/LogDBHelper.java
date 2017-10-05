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
 * Copyright 2004-2005 Sun Microsystems, Inc.  All rights reserved.
 * Use is subject to license terms.
 */

package com.sun.jts.CosTransactions;

import java.util.Map;
import java.util.HashMap;
import java.sql.*;
import javax.sql.*;
import javax.naming.*;
import java.lang.reflect.Method;

import java.util.logging.Logger;
import java.util.logging.Level;
import com.sun.logging.LogDomains;
import com.sun.jts.utils.LogFormatter;

/** The LogDBHelper class takes care of writing the transaction logs
  *  into database.
  * @author Sun Micro Systems, Inc
*/

class LogDBHelper {

    String resName = "jdbc/TxnDS";
    DataSource ds = null;
    Method getNonTxConnectionMethod = null;
    static final String insertStatement = 
	         System.getProperty("com.sun.jts.dblogging.insertquery",
                 "insert into  txn_log_table values ( ? , ? , ? )");
    static final String deleteStatement = 
	         System.getProperty("com.sun.jts.dblogging.deletequery",
                 "delete from txn_log_table where localtid = ? and servername = ? ");
    static final String selectStatement = 
	         System.getProperty("com.sun.jts.dblogging.selectquery",
                 "select * from txn_log_table where servername = ? ");
    static Logger _logger = LogDomains.getLogger(LogDBHelper.class, LogDomains.TRANSACTION_LOGGER);
    static LogDBHelper _instance = new LogDBHelper();

    static LogDBHelper getInstance() {
        return _instance;
    }

    LogDBHelper() {
        if (Configuration.getPropertyValue(Configuration.DB_LOG_RESOURCE) != null) {
            resName = Configuration.getPropertyValue(Configuration.DB_LOG_RESOURCE);
        }
        try {
            InitialContext ctx = new InitialContext();
            ds = (DataSource)ctx.lookup(resName);
	    Class cls = ds.getClass();
	    getNonTxConnectionMethod = cls.getMethod("getNonTxConnection", null);

        } catch (Throwable t) {
            _logger.log(Level.SEVERE,"jts.unconfigured_db_log_resource",resName);
            _logger.log(Level.SEVERE,"",t);
        }
    }


    boolean addRecord(long localTID, byte[] data) {
        if (ds != null) {
            Connection conn = null;
            PreparedStatement prepStmt1 = null;    
            try {
                conn = ds.getConnection();
                prepStmt1 = conn.prepareStatement(insertStatement);
                prepStmt1.setLong(1,localTID);
                prepStmt1.setString(2,Configuration.getServerName());
                prepStmt1.setBytes(3,data);
                prepStmt1 .executeUpdate();
                return true;
            } catch (Throwable ex) {
                _logger.log(Level.SEVERE,"jts.exception_in_db_log_resource",ex);
                return false;
            } finally {
                try {
                if (prepStmt1 != null) 
                    prepStmt1.close();
                if (conn != null)
                    conn.close();
                } catch (Exception ex1) {
                    _logger.log(Level.SEVERE,"jts.exception_in_db_log_resource",ex1);
                }
            }
        }
        return false;
    }

    boolean deleteRecord(long localTID) {
        if (ds != null) {
            Connection conn = null;
            PreparedStatement prepStmt1 = null;    
            try {
		 // To avoid compile time dependency to get NonTxConnection
		conn = (Connection)(getNonTxConnectionMethod.invoke(ds, null)); 
                prepStmt1 = conn.prepareStatement(deleteStatement);
                prepStmt1.setLong(1,localTID);
                prepStmt1.setString(2,Configuration.getServerName());
                prepStmt1 .executeUpdate();
                return true;
            } catch (Exception ex) {
                _logger.log(Level.SEVERE,"jts.exception_in_db_log_resource",ex);
                return false;
            } finally {
                try {
                if (prepStmt1 != null) 
                    prepStmt1.close();
                if (conn != null)
                    conn.close();
                } catch (Exception ex1) {
                    _logger.log(Level.SEVERE,"jts.exception_in_db_log_resource",ex1);
                }
            }
        }
        return false;
    }

    Map getGlobalTIDMap() {
        Map gtidMap = new HashMap();
        if (ds != null) {
            Connection conn = null;
            PreparedStatement prepStmt1 = null;    
            ResultSet rs = null;
            try {
                conn = ds.getConnection();
                prepStmt1 = conn.prepareStatement(selectStatement);
                prepStmt1.setString(1,Configuration.getServerName());
                rs = prepStmt1.executeQuery();
                while (rs.next()) {
                    Long localTID = new Long(rs.getLong(1));
                    byte[] gtridbytes = rs.getBytes(3);
                    gtidMap.put(GlobalTID.fromTIDBytes(rs.getBytes(3)), localTID);
                }
            } catch (Exception ex) {
                _logger.log(Level.SEVERE,"jts.exception_in_db_log_resource",ex);
            } finally {
                try {
                if (rs != null) 
                    rs.close();
                if (prepStmt1 != null) 
                    prepStmt1.close();
                if (conn != null)
                    conn.close();
                } catch (Exception ex1) {
                    _logger.log(Level.SEVERE,"jts.exception_in_db_log_resource",ex1);
                }
            }
        }
        return gtidMap;
    }

    
}
