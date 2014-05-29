/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2011 Oracle and/or its affiliates. All rights reserved.
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

package org.glassfish.admin.amx.intf.config;

import java.io.File;
import java.util.Map;

@Deprecated
public interface Application
        extends AbstractModule, Libraries {


    public Resources getResources();

    public String getLocation();

    public String getDirectoryDeployed();

    public String getEnabled();

    public void setEnabled(String param1);

    public String getDescription();

    public void setDescription(String param1);

    public String getObjectType();

    public void setObjectType(String param1);

    public String getContextRoot();

    public void setContextRoot(String param1);

    public String getAvailabilityEnabled();

    public void setAvailabilityEnabled(String param1);

    public String getAsyncReplication();

    public void setAsyncReplication(String param1);

    public Map<String, Module> getModule();

    public Module getModule(String param1);

    public Map<String, Engine> getEngine();

    public Map<String, WebServiceEndpoint> getWebServiceEndpoint();


    public String getLibraries();

    public void setLibraries(String param1);

    public void setLocation(String param1);

    public void setDirectoryDeployed(String param1);

    public void setResources(Resources param1);

    //public List getDeployProperties();

    public Map getModulePropertiesMap();

    public boolean isStandaloneModule();

    public boolean containsSnifferType(String param1);

    public boolean isLifecycleModule();

    public boolean isOSGiModule();

    public void recordFileLocations(File param1, File param2);

    public File application();

    public File deploymentPlan();

}
