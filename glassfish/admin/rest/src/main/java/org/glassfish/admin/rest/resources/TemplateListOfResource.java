/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2009-2011 Oracle and/or its affiliates. All rights reserved.
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

package org.glassfish.admin.rest.resources;

import java.net.HttpURLConnection;
import com.sun.enterprise.util.LocalStringManagerImpl;
import com.sun.jersey.api.core.ResourceContext;
import com.sun.jersey.multipart.FormDataMultiPart;
import org.glassfish.admin.rest.ResourceUtil;
import org.glassfish.admin.rest.RestService;
import org.glassfish.admin.rest.Util;
import org.glassfish.admin.rest.provider.MethodMetaData;
import org.glassfish.admin.rest.results.ActionReportResult;
import org.glassfish.admin.rest.results.OptionsResult;
import org.glassfish.admin.rest.utils.xml.RestActionReporter;
import org.glassfish.api.ActionReport;
import org.glassfish.api.admin.RestRedirect;
import org.jvnet.hk2.component.Habitat;
import org.jvnet.hk2.config.ConfigBeanProxy;
import org.jvnet.hk2.config.ConfigModel;
import org.jvnet.hk2.config.Dom;
import org.jvnet.hk2.config.DomDocument;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.glassfish.admin.rest.Util.*;

/**
 * @author Ludovic Champenois ludo@dev.java.net
 * @author Rajeshwar Patil
 */
public abstract class TemplateListOfResource {
    private final static String QUERY_PARAMETERS = "queryParameters";
    private final static String MESSAGE_PARAMETERS = "messageParameters";

    @Context
    protected HttpHeaders requestHeaders;

    @Context
    protected UriInfo uriInfo;
    
    @Context
    protected ResourceContext resourceContext;

    @Context
    protected Habitat habitat;

    protected List<Dom> entity;
    protected Dom parent;
    protected String tagName;

    public final static LocalStringManagerImpl localStrings = new LocalStringManagerImpl(TemplateListOfResource.class);

    @GET
    @Produces({"text/html;qs=2", MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response get(@QueryParam("expandLevel") @DefaultValue("1") int expandLevel) {
        return Response.ok().entity(buildActionReportResult()).build();
/*
        List<Dom> domList = new ArrayList();
        List<Dom> entities = getEntity();
        if (entities==null){
            return new GetResultList(domList, getPostCommand(), getCommandResourcesPaths(), options());//empty dom list
        }
        Iterator iterator = entities.iterator();
        ConfigBean e;
        while (iterator.hasNext()) {
            e = (ConfigBean) iterator.next();
            domList.add(e);
        }

        return new GetResultList(domList, getPostCommand(), getCommandResourcesPaths(), options());
*/
    }

    @POST
    //create
    @Produces({"text/html;qs=2",
            MediaType.APPLICATION_JSON,
            MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
            MediaType.APPLICATION_FORM_URLENCODED})
    public Response createResource(HashMap<String, String> data) {
        try {
            if (data.containsKey("error")) {
                String errorMessage = localStrings.getLocalString("rest.request.parsing.error",
                        "Unable to parse the input entity. Please check the syntax.");
                ActionReportResult arr = ResourceUtil.getActionReportResult(ActionReport.ExitCode.FAILURE, errorMessage, requestHeaders, uriInfo);
                return Response.status(400).entity(arr).build();
            }

            ResourceUtil.purgeEmptyEntries(data);

            //Command to execute
            String commandName = getPostCommand();
            String resourceToCreate = uriInfo.getAbsolutePath() + "/";

            if (null != commandName) {
                ResourceUtil.adjustParameters(data); //adjusting for DEFAULT is required only while executing a CLI command
                if (data.containsKey("name")) {
                    resourceToCreate += data.get("name");
                } else {
                    resourceToCreate += data.get("DEFAULT");
                }
                String typeOfResult = ResourceUtil.getResultType(requestHeaders);
                RestActionReporter actionReport = ResourceUtil.runCommand(commandName, data, habitat, typeOfResult);

                ActionReport.ExitCode exitCode = actionReport.getActionExitCode();
                if (exitCode != ActionReport.ExitCode.FAILURE) {
                    String successMessage =
                            localStrings.getLocalString("rest.resource.create.message",
                                    "\"{0}\" created successfully.", resourceToCreate);
                    ActionReportResult arr = ResourceUtil.getActionReportResult(actionReport, successMessage, requestHeaders, uriInfo);
                    return Response.ok(arr).build();
                }

                String errorMessage = getErrorMessage(data, actionReport);
                ActionReportResult arr = ResourceUtil.getActionReportResult(actionReport, errorMessage, requestHeaders, uriInfo);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(arr).build();
            } else {
                ActionReportResult arr = ResourceUtil.getActionReportResult(ActionReport.ExitCode.FAILURE, "No CRUD Create possible.", requestHeaders, uriInfo);
                return Response.status(HttpURLConnection.HTTP_INTERNAL_ERROR).entity(arr).build();


            }
        }catch (Exception e) {
            throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response post(FormDataMultiPart formData) {
        /* data passed to the generic command running
       *
       * */
        HashMap<String, String> data = TemplateResource.createDataBasedOnForm(formData);
        Response response = createResource(data, data.get("name")); //execute the deploy command with a copy of the file locally
        TemplateResource.deleteDataBasedOnForm(formData);
        return response;

    }

    @OPTIONS
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML, MediaType.APPLICATION_XML})
    public Response options() {
        return Response.ok().entity(buildActionReportResult()).build();
    }

    public void setEntity(List<Dom> p) {
        entity = p;
    }

    public List<Dom> getEntity() {
        return entity;
    }

    public void setParentAndTagName(Dom parent, String tagName) {
        this.parent = parent;
        this.tagName = tagName;
        if (parent!=null)
            entity = parent.nodeElements(tagName);

    }

    /**
     * allows for remote files to be put in a tmp area and we pass the
     * local location of this file to the corresponding command instead of the content of the file
     * * Yu need to add  enctype="multipart/form-data" in the form
     * for ex:  <form action="http://localhost:4848/management/domain/applications/application" method="post" enctype="multipart/form-data">
     * then any param of type="file" will be uploaded, stored locally and the param will use the local location
     * on the server side (ie. just the path)
     */
    public String getPostCommand() {
        ConfigModel.Property p = parent.model.getElement(tagName);

        if (p==null){ //"*"
            ConfigModel.Property childElement = parent.model.getElement("*");
            if (childElement!=null) {
                ConfigModel.Node node = (ConfigModel.Node) childElement;
                ConfigModel childModel = node.getModel();
                List<ConfigModel> subChildConfigModels = ResourceUtil.getRealChildConfigModels(childModel, parent.document);
                for (ConfigModel subChildConfigModel : subChildConfigModels) {
                    if (subChildConfigModel.getTagName().equals(tagName)) {
                                return ResourceUtil.getCommand(RestRedirect.OpType.POST, subChildConfigModel);
                    }
                }

                }
        }else {
            ConfigModel.Node n = (ConfigModel.Node) p;
           return ResourceUtil.getCommand(RestRedirect.OpType.POST, n.getModel());
        }

        return null;
    }

    public String[][] getCommandResourcesPaths() {
        return new String[][]{};
    }

    public static Class<? extends ConfigBeanProxy> getElementTypeByName(Dom parentDom, String elementName)
            throws ClassNotFoundException {

        DomDocument document = parentDom.document;
        ConfigModel.Property a = parentDom.model.getElement(elementName);
        if (a != null) {
            if (a.isLeaf()) {
                //  : I am not too sure, but that should be a String @Element
                return null;
            } else {
                ConfigModel childModel = ((ConfigModel.Node) a).getModel();
                return (Class<? extends ConfigBeanProxy>) childModel.classLoaderHolder.get().loadClass(childModel.targetTypeName);
            }
        }
        // global lookup
        ConfigModel model = document.getModelByElementName(elementName);
        if (model != null) {
            return (Class<? extends ConfigBeanProxy>) model.classLoaderHolder.get().loadClass(model.targetTypeName);
        }

        return null;
    }

    protected ActionReportResult buildActionReportResult() {
        if (entity == null) {//wrong resource
            String errorMessage = localStrings.getLocalString("rest.resource.erromessage.noentity",
                    "Resource not found.");
            return ResourceUtil.getActionReportResult(ActionReport.ExitCode.FAILURE, errorMessage, requestHeaders, uriInfo);
        }
        RestActionReporter ar = new RestActionReporter();
        final String typeKey = (decode(getName(uriInfo.getPath(), '/')));
        ar.setActionDescription(typeKey);

        OptionsResult optionsResult = new OptionsResult(Util.getResourceName(uriInfo));
        Map<String, MethodMetaData> mmd = getMethodMetaData();
        optionsResult.putMethodMetaData("GET", mmd.get("GET"));
        optionsResult.putMethodMetaData("POST", mmd.get("POST"));

        ResourceUtil.addMethodMetaData(ar, mmd);
        ar.getExtraProperties().put("childResources", ResourceUtil.getResourceLinks(getEntity(), uriInfo));
        ar.getExtraProperties().put("commands", ResourceUtil.getCommandLinks(getCommandResourcesPaths()));

        // FIXME:  I'd rather not keep using OptionsResult, but I don't have the time at this point to do it "right."  This is
        // an internal impl detail, so it can wait
        return new ActionReportResult(ar, optionsResult);
    }

    //called in case of POST on application resource (deployment).
    //resourceToCreate is the name attribute if provided.
    private Response createResource(HashMap<String, String> data, String resourceToCreate) {
        try {
            if (data.containsKey("error")) {
                String errorMessage = localStrings.getLocalString("rest.request.parsing.error",
                        "Unable to parse the input entity. Please check the syntax.");
                return Response.status(400).entity(ResourceUtil.getActionReportResult(ActionReport.ExitCode.FAILURE, errorMessage, requestHeaders, uriInfo)).build();
            }

            ResourceUtil.purgeEmptyEntries(data);

            //Command to execute
            String commandName = getPostCommand();
            ResourceUtil.defineDefaultParameters(data);

            if ((resourceToCreate == null) || (resourceToCreate.equals(""))) {
                String newResourceName = data.get("DEFAULT");
                if (newResourceName.contains("/")) {
                    newResourceName = Util.getName(newResourceName, '/');
                } else {
                    if (newResourceName.contains("\\")) {
                        newResourceName = Util.getName(newResourceName, '\\');
                    }
                }
                resourceToCreate = uriInfo.getAbsolutePath() + "/" + newResourceName;
            } else {
                resourceToCreate = uriInfo.getAbsolutePath() + "/" + resourceToCreate;
            }

            if (null != commandName) {
                String typeOfResult = ResourceUtil.getResultType(requestHeaders);
                ActionReport actionReport = ResourceUtil.runCommand(commandName, data, habitat, typeOfResult);

                ActionReport.ExitCode exitCode = actionReport.getActionExitCode();
                if (exitCode != ActionReport.ExitCode.FAILURE) {
                    String successMessage = localStrings.getLocalString("rest.resource.create.message",
                            "\"{0}\" created successfully.", new Object[]{resourceToCreate});
                    return Response.ok().entity(ResourceUtil.getActionReportResult(actionReport, successMessage, requestHeaders, uriInfo)).build();
                }

                String errorMessage = getErrorMessage(data, actionReport);
                return Response.status(400).entity(ResourceUtil.getActionReportResult(actionReport, errorMessage, requestHeaders, uriInfo)).build();
            }
            String message = localStrings.getLocalString("rest.resource.post.forbidden",
                    "POST on \"{0}\" is forbidden.", new Object[]{resourceToCreate});
            return Response.status(403).entity(ResourceUtil.getActionReportResult(ActionReport.ExitCode.FAILURE, message, requestHeaders, uriInfo)).build();

        } catch (Exception e) {
            throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    private Map<String, MethodMetaData> getMethodMetaData() {
        Map<String, MethodMetaData> map = new TreeMap<String, MethodMetaData>();
        //GET meta data
        map.put("GET", new MethodMetaData());

        //POST meta data
        String command = getPostCommand();
        if (command != null) {
            MethodMetaData postMethodMetaData = ResourceUtil.getMethodMetaData(command, habitat, RestService.logger);
            if (Util.getResourceName(uriInfo).equals("Application")) {
                postMethodMetaData.setIsFileUploadOperation(true);
            }
            map.put("POST", postMethodMetaData);
        } /*else {
            ConfigModel.Node prop = (ConfigModel.Node) parent.model.getElement(tagName);
            if (prop == null) { //maybe null when Element ("*") is used
                try {
                    ConfigModel.Node prop2 = (ConfigModel.Node) parent.model.getElement("*");

                    ConfigModel childModel = prop2.getModel();
                    Class<?> subType = childModel.classLoaderHolder.get().loadClass(childModel.targetTypeName); ///  a should be the typename
                    List<ConfigModel> lcm = parent.document.getAllModelsImplementing(subType);
                    if (lcm == null) { //https://glassfish.dev.java.net/issues/show_bug.cgi?id=12654
                        lcm = new ArrayList<ConfigModel>();
                        lcm.add(childModel);
                    }
                    for (ConfigModel cmodel : lcm) {
                        if (cmodel.getTagName().equals(tagName)) {
                            MethodMetaData postMethodMetaData = ResourceUtil.getMethodMetaData2(parent,
                                    cmodel, Constants.MESSAGE_PARAMETER);
                            postMethodMetaData.setDescription("Update");
                            map.put("POST", postMethodMetaData);
                        }
                    }
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            } else {
                MethodMetaData postMethodMetaData = ResourceUtil.getMethodMetaData2(parent,
                        prop.getModel(), Constants.MESSAGE_PARAMETER);
                postMethodMetaData.setDescription("Update");
                map.put("POST", postMethodMetaData);
            }

        }*/

        return map;
    }

    private String getErrorMessage(HashMap<String, String> data, ActionReport ar) {
        String message;
        //error info
        message = ar.getMessage();

        /*if (data.isEmpty()) {
            try {
                //usage info
                message = ar.getTopMessagePart().getChildren().get(0).getMessage();
            } catch (Exception e) {
                message = ar.getMessage();
            }
        }*/
        return message;
    }
}
