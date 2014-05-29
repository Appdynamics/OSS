/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
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

/*
 * ComponentVisitor.java
 *
 * Created on January 31, 2002, 11:29 AM
 */

package com.sun.enterprise.deployment.util;

import com.sun.enterprise.deployment.*;
import com.sun.enterprise.deployment.types.EjbReference;
import com.sun.enterprise.deployment.types.MessageDestinationReferencer;

/**
 * This class defines the protocol for visiting J2EE Component DOL  
 * related classes 
 *
 * @author  Jerome Dochez
 * @version 
 */
public interface ComponentVisitor extends DescriptorVisitor {

   /**
     * visits all entries within the component environment for which
     * isInjectable() == true
     * @param the InjectionCapable environment dependency
     */
    public void accept(InjectionCapable injectable);
    

   /**
     * visits an ejb reference for the last J2EE component visited
     * @param the ejb reference
     */
    public void accept(EjbReference ejbRef);
    
    /**
     * visits a role reference from the last  J2EE component visited
     * @param role reference
     */
    public void accept(RoleReference roleRef);
    
    /**
     * visits an environment property  for the last J2EE component visited
     * @param the environment property
     */
    public void accept(EnvironmentProperty envEntry);    

    /**
     * visits an resource reference for the last J2EE component visited
     * @param the resource reference
     */
    public void accept(ResourceReferenceDescriptor resRef);

    /**
     * visits an jms destination reference for the last J2EE component visited
     * @param the jms destination reference
     */
    public void accept(JmsDestinationReferenceDescriptor jmsDestRef);

    /**
     * visits an message destination reference for the last J2EE component visited
     * @param the message destination reference
     */
    public void accept(MessageDestinationReferenceDescriptor msgDestRef);

    /**
     * visits an message destination for the last J2EE component visited
     * @param the message destination
     */
    public void accept(MessageDestinationDescriptor msgDest);

    /**
     * visits a message destination referencer for the last J2EE component
     * visited.
     */
    public void accept(MessageDestinationReferencer msgDestReferencer);

    /**
     * visits a service reference for the last J2EE component visited.
     */
    public void accept(ServiceReferenceDescriptor serviceRef);
}

