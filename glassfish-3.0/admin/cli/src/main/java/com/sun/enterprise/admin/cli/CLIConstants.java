/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2008 Sun Microsystems, Inc. All rights reserved.
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
package com.sun.enterprise.admin.cli;

/**
 * Constants for use in this package and "sub" packages
 * @author bnevins
 */
public class CLIConstants {
    ////////////////////////////////////////////////////////////////////////////
    ///////       public                   /////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    public static final int     DEFAULT_ADMIN_PORT              = 4848;
    public static final String  DEFAULT_HOSTNAME                = "localhost";
    public static final String  EOL                             = System.getProperty("line.separator");

    // sent in as a System Property for restarts
    public static final String  RESTART_FLAG                    = "AS_RESTART";
    public static final long    WAIT_FOR_DAS_TIME_MS            = 10 * 60 * 1000; // 10 minutes
    public static final int     RESTART_EXIT_VALUE              = 10;
    public static final String  WALL_CLOCK_START_PROP           = "WALL_CLOCK_START";
    public static final boolean debugMode;
    public static final String  CLI_RECORD_ALL_COMMANDS_PROP    = "AS_LOGFILE";

    ////////////////////////////////////////////////////////////////////////////
    ///////       private                   ////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////

    private static final String CLI_DEBUG_MODE_PROP             = "AS_DEBUG";

    static {
        debugMode = Boolean.parseBoolean(System.getenv(CLI_DEBUG_MODE_PROP)) ||
                    Boolean.getBoolean(CLI_DEBUG_MODE_PROP);
    }

    private CLIConstants() {
       // no instances allowed!
    }
    public static final boolean debug() {
        return debugMode;
    }
}
