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
 */
package org.glassfish.admin.rest.resources;

import java.util.HashMap;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.sun.enterprise.util.LocalStringManagerImpl;

import org.glassfish.admin.rest.provider.OptionsResult;
import org.glassfish.admin.rest.provider.MethodMetaData;
import org.glassfish.admin.rest.Constants;
import org.glassfish.admin.rest.ResourceUtil;
import org.glassfish.admin.rest.RestService;
import org.glassfish.api.ActionReport;

public class IiopListenerDeleteSslResource {

public IiopListenerDeleteSslResource() {
__resourceUtil = new ResourceUtil();
}
@DELETE
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_FORM_URLENCODED})
public Response executeCommand(HashMap<String, String> data) {
try {
if (data.containsKey("error")) {
String errorMessage = localStrings.getLocalString("rest.request.parsing.error", "Unable to parse the input entity. Please check the syntax.");
return __resourceUtil.getResponse(400, /*parsing error*/
 errorMessage, requestHeaders, uriInfo);
}

if (commandParams != null) {
//formulate parent-link attribute for this command resource
//Parent link attribute may or may not be the id/target attribute
if (isLinkedToParent) {
__resourceUtil.resolveParentParamValue(commandParams, uriInfo);
}

data.putAll(commandParams);
}

__resourceUtil.adjustParameters(data);

__resourceUtil.purgeEmptyEntries(data);

ActionReport actionReport = __resourceUtil.runCommand(commandName, data, RestService.getHabitat());

ActionReport.ExitCode exitCode = actionReport.getActionExitCode();

if (exitCode == ActionReport.ExitCode.SUCCESS) {
String successMessage = localStrings.getLocalString("rest.request.success.message",
"{0} of {1} executed successfully.", new Object[] {commandMethod, uriInfo.getAbsolutePath()});
return __resourceUtil.getResponse(200, /*200 - ok*/
 successMessage, requestHeaders, uriInfo);
}

String errorMessage = actionReport.getMessage();
return __resourceUtil.getResponse(400, /*400 - bad request*/
 errorMessage, requestHeaders, uriInfo);
} catch (Exception e) {
throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR);
}
}
//Handle POST request without any entity(input).
//Do not care what the Content-Type is.
@DELETE
public Response executeCommand() {
try {
return executeCommand(new HashMap<String, String>());
} catch (Exception e) {
throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR);
}
}
//hack-1 : support delete method for html
//Currently, browsers do not support delete method. For html media,
//delete operations can be supported through POST. Redirect html
//client POST request for delete operation to DELETE method.

//In case of delete command reosurce, we will also create post method
//which simply forwards the request to delete method. Only in case of
//html client delete request is routed through post. For other clients
//delete request is directly handled by delete method.
@POST
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_FORM_URLENCODED})
public Response hack(HashMap<String, String> data) {
if ((data.containsKey("operation")) &&
(data.get("operation").equals("__deleteoperation"))) {
data.remove("operation");
}
return executeCommand(data);
}
@GET
@Produces({MediaType.TEXT_HTML, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public org.glassfish.admin.rest.provider.CommandResourceGetResult get() {
try {
return new org.glassfish.admin.rest.provider.CommandResourceGetResult(resourceName, commandName, commandDisplayName, commandMethod, commandAction, options());
} catch (Exception e) {
throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR);
}
}
@OPTIONS
@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML, MediaType.APPLICATION_XML})
public OptionsResult options() {
OptionsResult optionsResult = new OptionsResult(resourceName);
try {
//command method metadata
MethodMetaData methodMetaData = __resourceUtil.getMethodMetaData(
commandName, commandParams, Constants.MESSAGE_PARAMETER, RestService.getHabitat(), RestService.logger);
//GET meta data
optionsResult.putMethodMetaData("GET", new MethodMetaData());
optionsResult.putMethodMetaData(commandMethod, methodMetaData);
} catch (Exception e) {
throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR);
}

return optionsResult;
}

public final static LocalStringManagerImpl localStrings = new LocalStringManagerImpl(ResourceUtil.class);
@Context
protected HttpHeaders requestHeaders;
@Context
protected UriInfo uriInfo;

private static final String resourceName = "IiopListenerDeleteSsl";
private static final String commandName = "delete-ssl";
private static final String commandDisplayName = "delete-ssl";
private static final String commandMethod = "DELETE";
private static final String commandAction = "Delete";
private HashMap<String, String> commandParams =
new HashMap<String, String>() {{
put("id","$parent");
put("type","iiop-listener");
}};
private static final boolean isLinkedToParent = true;
private ResourceUtil __resourceUtil;
}
