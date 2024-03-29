/*
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
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
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
package org.glassfish.api.deployment;

import org.glassfish.api.Param;

import java.util.Properties;

/**
 * Parameters passed to a deployment command.
 *
 * @Author Jerome Dochez
 */
public class UndeployCommandParameters extends OpsParams {

    @Param(primary = true)
    public String name=null;

    @Param(optional=true)
    public String target = "server";

    @Param(optional=true, defaultValue="false")
    public Boolean keepreposdir;

    @Param(optional=true)
    public Boolean droptables;

    @Param(optional=true, defaultValue="false")
    public Boolean cascade;

    //used for internal purposes only, not to expose to user 
    @Param(optional=true, defaultValue="false")
    public Boolean ignoreCascade;

    @Param(optional=true, separator=':')
    public Properties properties=null;

    public String name() {
        return name;
    }

    public UndeployCommandParameters() {
    }

    public UndeployCommandParameters(String name) {
        this.name = name;
    }

    public String libraries() {
        throw new IllegalStateException("We need to be able to get access to libraries when undeploying");
    }
}
    
