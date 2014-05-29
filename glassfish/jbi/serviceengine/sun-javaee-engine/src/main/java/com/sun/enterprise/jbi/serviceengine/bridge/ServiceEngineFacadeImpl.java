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

package com.sun.enterprise.jbi.serviceengine.bridge;
import com.sun.enterprise.deployment.ServiceRefPortInfo;
import com.sun.enterprise.jbi.serviceengine.ServiceEngineException;
import com.sun.enterprise.jbi.serviceengine.core.EndpointRegistry;
import com.sun.enterprise.jbi.serviceengine.bridge.transport.JAXRPCClientTransportFactory;
import com.sun.enterprise.jbi.serviceengine.bridge.transport.JBITransportPipe;
import com.sun.xml.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.ws.api.pipe.Pipe;
import com.sun.xml.ws.api.WSBinding;
import javax.xml.namespace.QName;
import java.net.URL;


/**
 *
 * @author Manisha Umbarje
 */
public class ServiceEngineFacadeImpl implements ServiceEngineFacade {
    
    private static ServiceEngineFacadeImpl facade = new ServiceEngineFacadeImpl();
    
    
    /** Creates a new instance of ServiceEngineFacade */
    private ServiceEngineFacadeImpl() {
    }
    
    public static ServiceEngineFacadeImpl getInstance() {
        return facade;
    }
    
    public Object getTransportFactory(ServiceRefPortInfo portInfo,
            boolean oneWay) throws ServiceEngineException {
        
        return new JAXRPCClientTransportFactory(portInfo);
    }
    
    public void handleWebServiceEndpointEvent(String endpointRelativeURI,
            boolean jbiEnabled) throws ServiceEngineException {
        EndpointHelper.getInstance().toggleEndpointStatus(endpointRelativeURI,
                jbiEnabled);
    }
    
    public Pipe createJBITransportPipe(WSBinding binding, 
                                       URL wsdlLocation, 
                                       QName service,
                                       WSDLPort wsdlPort) {
        return new JBITransportPipe(binding, wsdlLocation, service, wsdlPort);
    }

    public boolean hasConsumerEP(QName serviceName, String endpointName) {
        return EndpointRegistry.getInstance().hasConsumerEP(serviceName, endpointName);
    }
}
