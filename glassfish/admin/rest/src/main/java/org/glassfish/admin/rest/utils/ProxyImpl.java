/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010 Oracle and/or its affiliates. All rights reserved.
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

package org.glassfish.admin.rest.utils;

import com.sun.enterprise.config.serverbeans.Domain;
import com.sun.enterprise.config.serverbeans.Server;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.glassfish.admin.rest.Util;
import org.glassfish.admin.rest.clientutils.MarshallingUtils;
import org.jvnet.hk2.component.Habitat;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

/**
 * @author Mitesh Meswani
 */
public abstract class ProxyImpl implements Proxy {

    @Override
    public Properties proxyRequest(UriInfo sourceUriInfo, Habitat habitat) {
        Properties proxiedResponse = new Properties();
        try {
            Domain domain = habitat.getComponent(Domain.class);
            String forwardInstanceName = extractTargetInstanceName(sourceUriInfo);
            Server forwardInstance = domain.getServerNamed(forwardInstanceName);
            if (forwardInstance != null) {
                UriBuilder forwardUriBuilder = constructForwardURLPath(sourceUriInfo);
                URI forwardURI = forwardUriBuilder.scheme("https").host(forwardInstance.getAdminHost()).port(forwardInstance.getAdminPort()).build(); //Host and Port are replaced to that of forwardInstanceName
                Client client = Util.getJerseyClient(habitat);
                WebResource.Builder resourceBuilder = client.resource(forwardURI).accept(MediaType.APPLICATION_JSON);
                ClientResponse response = resourceBuilder.get(ClientResponse.class); //TODO if the target server is down, we get ClientResponseException. Need to handle it
                ClientResponse.Status status = ClientResponse.Status.fromStatusCode(response.getStatus());
                if (status.getFamily() == javax.ws.rs.core.Response.Status.Family.SUCCESSFUL) {
                    String jsonDoc = response.getEntity(String.class);
                    Map responseMap = MarshallingUtils.buildMapFromDocument(jsonDoc);
                    Map resultExtraProperties = (Map) responseMap.get("extraProperties");
                    if (resultExtraProperties != null) {
                        Object entity = resultExtraProperties.get("entity");
                        if(entity != null) {
                            proxiedResponse.put("entity", entity);
                        }

                        @SuppressWarnings({"unchecked"}) Map<String, String> childResources = (Map<String, String>) resultExtraProperties.get("childResources");
                        for (Map.Entry<String, String> entry : childResources.entrySet()) {
                            String targetURL = null;
                            try {
                                URL originalURL = new URL(entry.getValue());
                                //Construct targetURL which has host+port of DAS and path from originalURL
                                targetURL = constructTargetURLPath(sourceUriInfo, originalURL).build().toASCIIString();
                            } catch (MalformedURLException e) {
                                //TODO There was an exception while parsing URL. Need to decide what to do. For now ignore the child entry
                            }
                            entry.setValue(targetURL);
                        }
                        proxiedResponse.put("childResources", childResources);
                    }
                    Object message = responseMap.get("message");
                    if(message != null) {
                        proxiedResponse.put("message", message);
                    }
                    Object properties = responseMap.get("properties");
                    if(properties != null) {
                        proxiedResponse.put("properties", properties);
                    }
                }
            } else { // server == null
                // TODO error to user. Can not locate server for whom data is being looked for

            }
        } catch (Exception ex) {
            throw new WebApplicationException(ex, Response.Status.INTERNAL_SERVER_ERROR);
        }
        return proxiedResponse;

    }

}
