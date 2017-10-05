/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2009 Sun Microsystems, Inc. All rights reserved.
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
 * UtilHandlers.java
 *
 * Created on August 31, 2006, 2:36 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.glassfish.admingui.common.handlers;

import org.glassfish.admingui.common.util.GuiUtil;

import javax.faces.component.UIViewRoot;
import com.sun.jsftemplating.annotation.Handler;
import com.sun.jsftemplating.annotation.HandlerInput;
import com.sun.jsftemplating.annotation.HandlerOutput;
import com.sun.jsftemplating.layout.LayoutDefinitionManager;
//import com.sun.jsftemplating.layout.LayoutViewHandler;
import com.sun.jsftemplating.layout.ViewRootUtil;
import com.sun.jsftemplating.layout.descriptors.LayoutElement;
import com.sun.jsftemplating.layout.descriptors.handler.HandlerContext;
import com.sun.jsftemplating.layout.descriptors.handler.HandlerDefinition;
import com.sun.jsftemplating.util.FileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.GregorianCalendar;
import java.util.Map;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.Iterator;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

/**
 *
 * @author Jennifer Chou
 */
public class UtilHandlers {
    
    /** Creates a new instance of UtilHandlers */
    public UtilHandlers() {
    }
    

    
    
    /**
     *	<p> Adds the specified (signed) amount of time to the given calendar 
     *      field, based on the calendar's rules and returns the resulting Date.
     *      See <code>java.util.GregorianCalendar</code> add(int field, int amount). </p>
     *
     *  <p> Input value: "Field" -- Type: <code>Integer</code> 
     *          - <code>java.util.Calendar</code> field</p>
     *  <p> Input value: "Amount" -- Type: <code>Integer</code>
     *          - the amount of date or time to be added to the field.</p>
     *  <p> Output value: "Date" -- Type: <code>java.util.Date</code></p>
     *	@param	handlerCtx	The HandlerContext.
     */
    @Handler(id="calendarAdd",
    	input={
	    @HandlerInput(name="Field", type=Integer.class, required=true),
            @HandlerInput(name="Amount", type=Integer.class, required=true)},
        output={
            @HandlerOutput(name="Date", type=java.util.Date.class)})
    public static void calendarAdd(HandlerContext handlerCtx) {
        int field = ((Integer) handlerCtx.getInputValue("Field")).intValue();
        int amount = ((Integer) handlerCtx.getInputValue("Amount")).intValue();
        GregorianCalendar cal = new GregorianCalendar();
        cal.add(field, amount);
        handlerCtx.setOutputValue("Date", cal.getTime());        
    }
    
    /**
     *	<p> Creates a new File instance by converting the given pathname string 
     *      into an abstract pathname. If the given string is the empty string, 
     *      then the result is the empty abstract pathname. </p>
     *
     *  <p> Input value: "Pathname" -- Type: <code>String</code> 
     *  <p> Output value: "File" -- Type: <code>java.io.File</code></p>
     *	@param	handlerCtx	The HandlerContext.
     */
    @Handler(id="getFile",
    	input={
	    @HandlerInput(name="Pathname", type=String.class, required=true)},
        output={
            @HandlerOutput(name="File", type=File.class)})
    public static void getFile(HandlerContext handlerCtx) {
        String pathname = (String) handlerCtx.getInputValue("Pathname");
        handlerCtx.setOutputValue("File", pathname != null ? new File(pathname) : null);        
    }

    /**
     *	<p> This handler serves a resource via JSFTemplating's FileStreamer.</p>
     */
    @Handler(id="gf.serveResource",
    	input={
	    @HandlerInput(name="path", type=String.class, required=true)},
	output={
	    @HandlerOutput(name="content", type=String.class)})
    public static void serveResource(HandlerContext ctx) throws java.io.IOException {
	/*
	  JSF 2.0 impl sets the writer before the render response phase (in
	  apply request values).  So we can't control the output of an Ajax
	  request. :(  Therefor the following is commented out.
	 
	    LayoutViewHandler.serveResource(
		ctx.getFacesContext(), (String) ctx.getInputValue("path"));
	 */
	String path = (String) ctx.getInputValue("path");
	int idx = path.lastIndexOf("://");
	String port = null;
	if (idx != -1) {
	    // Strip off protocol
	    path = path.substring(idx + 3);

	    // Now looks like: host.domain:port/resource
// FIXME: port 80 may be omitted (or 443 for https)
	    if ((idx = path.indexOf(':')) != -1) {
		path = path.substring(idx + 1);
		if ((idx = path.indexOf('/')) != -1) {
		    port = path.substring(0, idx);
		    path = path.substring(idx);
		}
	    }
	}
	URL url = FileUtil.searchForFile(path, null);
	if ((url == null) && (port != null)) {
	    // Attempt to read from localhost
	    path = "http://localhost:" + port + path;
	    try {
		url = new URL(path);
	    } catch (MalformedURLException ex) {
		url = null;
	    }
	}
	String content = "";
	if (url != null) {
            try{
                content = new String(FileUtil.readFromURL(url));
            } catch (FileNotFoundException fnfe) {
                //
            }
	}

	// Set the output
	ctx.setOutputValue("content", content);
    }
    
    /**
     *	<p> Returns the name of the file or directory denoted by this abstract 
     *      pathname. This is just the last name in the pathname's name sequence. 
     *      If the pathname's name sequence is empty, then the empty string is returned. </p>
     *
     *  <p> Input value: "File" -- Type: <code>java.io.File</code> 
     *  <p> Output value: "Name" -- Type: <code>String</code></p>
     *	@param	handlerCtx	The HandlerContext.
     */
    @Handler(id="fileGetName",
    	input={
	    @HandlerInput(name="File", type=File.class, required=true)},
        output={
            @HandlerOutput(name="Name", type=String.class)})
    public static void fileGetName(HandlerContext handlerCtx) {
        File file = (File) handlerCtx.getInputValue("File");
        String name = file != null ? file.getName() : "" ;
        handlerCtx.setOutputValue("Name", name != null ? name : "");        
    }
    
    /**
     *	<p> Returns the value to which the input map maps the input key. </p>
     *
     *  <p> Input value: "Map" -- Type: <code>java.util.Map</code> 
     *  <p> Input value: "Key" -- Type: <code>Object</code>
     *  <p> Output value: "Value" -- Type: <code>Object</code></p>
     *	@param	handlerCtx	The HandlerContext.
     */
    @Handler(id="mapGet",
    	input={
	    @HandlerInput(name="Map", type=Map.class, required=true),
            @HandlerInput(name="Key", type=Object.class, required=true)},
        output={
            @HandlerOutput(name="Value", type=Object.class)})
    public static void mapGet(HandlerContext handlerCtx) {
        Map map = (Map) handlerCtx.getInputValue("Map");
        Object key = (Object) handlerCtx.getInputValue("Key");
        handlerCtx.setOutputValue("Value", (Object) map.get(key));        
    }

    /**
     * <p> Adds the given value to a <code>List</code></p>
     * <p> Input value: "list" -- Type: <code>java.util.List</code>
     * <p> Input value: "value" -- Type: <code>java.lang.Object</code>
     * 
     * @param handlerCtx The HandlerContext
     */
    @Handler(id="listAdd",
    	input={
            @HandlerInput(name="list", type=List.class, required=true),
            @HandlerInput(name="value", type=Object.class, required=true),
            @HandlerInput(name="index", type=Integer.class)
        }
    )
    public static void listAdd(HandlerContext handlerCtx) {

        List list = (List)handlerCtx.getInputValue("list");
        Integer index = (Integer)handlerCtx.getInputValue("index");
        if (index == null)
            list.add(handlerCtx.getInputValue("value"));
        else{
            list.add(index, handlerCtx.getInputValue("value"));
        }
    }

    /**
     *	<p> Compare if 2 objects is equal </p>
     *
     *  <p> Input value: "obj1" -- Type: <code>Object</code> 
     *  <p> Input value: "obj2" -- Type: <code>Object</code>
     *  <p> Output value: "equal" -- Type: <code>Object</code></p>
     *	@param	handlerCtx	The HandlerContext.
     */
    @Handler(id="compare",
    	input={
	    @HandlerInput(name="obj1", type=Object.class, required=true),
            @HandlerInput(name="obj2", type=Object.class, required=true)},
        output={
            @HandlerOutput(name="objEqual", type=Boolean.class)})
    public static void compare(HandlerContext handlerCtx) {
        boolean ret = false;
        Object obj1 = (Object) handlerCtx.getInputValue("obj1");
        Object obj2 = (Object) handlerCtx.getInputValue("obj2");
        if (obj1 != null){
            ret = obj1.equals(obj2);
        }else{
            if (obj2 == null)
                ret = true;
        }
        handlerCtx.setOutputValue("objEqual", ret);        
    }
    
    /**
     * <p> This method displays the save successful message when the page refresh.
     * @param handlerCtx The HandlerContext.
     */
   @Handler(id="prepareSuccessfulMsg")
    public static void prepareSuccessful(HandlerContext handlerCtx){
        GuiUtil.prepareSuccessful(handlerCtx);
    }

    /** 
     * <p> This method sets the attributes that will be used by the alert component
     *     display the message to user.
     *     If type is not specifed, it will be 'info' by default.
     * <p> Input value: "summary" -- Type: <code>java.lang.String</code></p>
     * <p> Input value: "type" -- Type: <code>java.lang.String</code></p>
     * <p> Input value: "detail" -- Type: <code>java.lang.String</code></p>
     * @param handlerCtx The HandlerContext.
     */
     @Handler(id="prepareAlertMsg",
     input={
        @HandlerInput(name="summary", type=String.class, required=true),
        @HandlerInput(name="type",  type=String.class),
        @HandlerInput(name="detail",  type=String.class)
      })
    public static void prepareAlertMsg(HandlerContext handlerCtx){
        String summary = (String) handlerCtx.getInputValue("summary");
        String type = (String) handlerCtx.getInputValue("type");
        String detail = (String) handlerCtx.getInputValue("detail");
        GuiUtil.prepareAlert(handlerCtx, type, summary, detail);
    }
     
    /**
     * <p> This method decodes a String using "UTF-8" as default
     * if scheme is not specified.
     */
     @Handler(id="decodeString",
     input={
        @HandlerInput(name="str", type=String.class, required=true),
        @HandlerInput(name="scheme", type=String.class)},
     output={
        @HandlerOutput(name="output", type=String.class)
	    })
    public static void decodeString(HandlerContext handlerCtx) {
        String str = (String) handlerCtx.getInputValue("str");
        String scheme = (String) handlerCtx.getInputValue("scheme");
        if (GuiUtil.isEmpty(str)){
            handlerCtx.setOutputValue("output", "");
            return;
        }
        
        if (GuiUtil.isEmpty(scheme))
            scheme = "UTF-8";
        try{
            String output=URLDecoder.decode(str, scheme);
            handlerCtx.setOutputValue("output", output);
        }catch(UnsupportedEncodingException ex) {
            ex.printStackTrace();
            handlerCtx.setOutputValue("output", str);
        }
     }

    
    @Handler(id="roundTo2DecimalPoint",
    input={
        @HandlerInput(name="input", type=Double.class)},
    output={
        @HandlerOutput(name="output", type=String.class)
    })
    public static void roundTo2DecimalPoint(HandlerContext handlerCtx) {
        DecimalFormat df= new DecimalFormat();
        df.setMaximumFractionDigits(2);
        try{
            Double input = (Double) handlerCtx.getInputValue("input");
            String output = (input==null)? "": df.format(input);
            handlerCtx.setOutputValue("output", output);
        }catch (Exception ex){
            ex.printStackTrace();
            handlerCtx.setOutputValue("output", "");
        }
    }

    /*
     * Add an empty string as the first element to the list.
     * This is useful as the labels/values of a dropdown list, where user is not required
     * to select a value in the list.  eg. virtualServer in the deployment screen,
     * defaultWebModule in the server etc.
     */

    @Handler(id = "addEmptyFirstElement",
    input = {
        @HandlerInput(name = "in", type = List.class, required = true)},
    output = {
        @HandlerOutput(name = "out", type = List.class)
    })
    public static void addEmptyFirstElement(HandlerContext handlerCtx) {
        List<String> in = (List) handlerCtx.getInputValue("in");
        ArrayList ar = null;
        if (in == null){
             ar = new ArrayList();
        }else{
             ar = new ArrayList(in);
        }
        ar.add(0, "");
        handlerCtx.setOutputValue("out", ar);
    }



    @Handler(id = "getListBoxOptions",
    input = {
        @HandlerInput(name = "availableList", type = List.class, required = true),
        @HandlerInput(name = "selectedCommaString", type = String.class, required = true),
        @HandlerInput(name = "addEmptyFirstChoice", type = String.class)},
    output = {
        @HandlerOutput(name = "availableListResult", type = List.class),
        @HandlerOutput(name = "selectedOptions", type = String[].class)
    })
    public static void getListBoxOptions(HandlerContext handlerCtx) {
        String selectedCommaString = (String) handlerCtx.getInputValue("selectedCommaString");
        List<String> availableList = (List) handlerCtx.getInputValue("availableList");
        String addEmptyFirstChoice = (String) handlerCtx.getInputValue("addEmptyFirstChoice");

        String[] selectedOptions = null;
        if (addEmptyFirstChoice != null){
            if (availableList == null){
                availableList = new ArrayList();
            }
            availableList.add(0, "");
        }
        if (availableList != null && (availableList.size() > 0) ) {
            selectedOptions = GuiUtil.stringToArray(selectedCommaString, ",");
            if (selectedOptions != null && !(selectedOptions.length > 0)) {
                //None is selected by default
                selectedOptions = new String[]{availableList.get(0)};
            }
        }
        handlerCtx.setOutputValue("availableListResult", availableList);
        handlerCtx.setOutputValue("selectedOptions", selectedOptions);
    }



    @Handler(id = "convertArrayToCommaString",
    input = {
        @HandlerInput(name = "array", type = String[].class, required = true)},
    output = {
        @HandlerOutput(name = "commaString", type = String.class)})
    public static void convertArrayToString(HandlerContext handlerCtx) {
        String[] array = (String[])handlerCtx.getInputValue("array");
        String commaString = "";
		if( (array != null) && array.length > 0 ) {
			commaString = GuiUtil.arrayToString(array, ",");
		}
        handlerCtx.setOutputValue("commaString", commaString);
    }
    
    @Handler(id = "convertListToCommaString",
    input = {
        @HandlerInput(name = "list", type = List.class, required = true)},
    output = {
        @HandlerOutput(name = "commaString", type = String.class)})
    public static void convertListToCommaString(HandlerContext handlerCtx) {
        List list = (List)handlerCtx.getInputValue("list");
        String commaString = "";
		if( (list != null) && list.size() > 0 ) {
			commaString = GuiUtil.listToString(list, ",");
		}
        handlerCtx.setOutputValue("commaString", commaString);
    }

   /**
     *	<p> This handler takes in a string with delimiter and returns list
     */
    @Handler(id="convertStringtoList",
         input={
            @HandlerInput(name="str", type=String.class),
            @HandlerInput(name="delimiter", type=String.class)
            },
        output = {
            @HandlerOutput(name = "result", type = List.class)
            })
    public static void convertStringtoListHandler(HandlerContext handlerCtx) {
        List result = convertStringToList((String) handlerCtx.getInputValue("str"),
                (String) handlerCtx.getInputValue("delimiter"));
        handlerCtx.setOutputValue("result", result);
    }

    private static List<String> convertStringToList(String str, String delimiter) {
        List<String> result = new ArrayList();
        if (str != null) {
            if (delimiter == null) {
                delimiter = ",";
            }
            StringTokenizer tokens = new StringTokenizer(str, delimiter);
            while (tokens.hasMoreTokens()) {
                String token = tokens.nextToken().trim();
                result.add(token);
            }
        }
        return result;
    }

    @Handler(id="convertStringToMap",
         input={
            @HandlerInput(name="str", type=String.class),
            @HandlerInput(name="delimiter", type=String.class)
            },
        output = {
            @HandlerOutput(name = "result", type = Map.class)
            })
    public static void convertStringToMap(HandlerContext handlerCtx) {
        Map<String, String> output = new HashMap<String,String>();
        List<String> list = convertStringToList((String) handlerCtx.getInputValue("str"),
                (String) handlerCtx.getInputValue("delimiter"));

        for (String item : list) {
            String[] parts = item.split("=");
            String key = parts[0];
            String value = "";
            if (parts.length > 1) {
                value = parts[1];
            }
            output.put(key, value);
        }

        handlerCtx.setOutputValue("result", output);
    }

    
     //This is the reserve of the above method.
    //We want to separator and display each jar in one line in the text box.
    @Handler(id = "formatStringsforDisplay",
    input = {
        @HandlerInput(name = "string", type = String.class, required = true)},
    output = {
        @HandlerOutput(name = "formattedString", type = String.class)})
    public static void formatStringsforDisplay(HandlerContext handlerCtx) {
        
        String values = (String) handlerCtx.getInputValue("string");
        if (values == null || GuiUtil.isEmpty(values.trim())) {
            handlerCtx.setOutputValue("formattedString", "");
        } else {
            String s1 = values.trim().replaceAll("\\.jar:", "\\.jar\\$\\{path.separator\\}");
            String s2 = s1.replaceAll("\\.jar;", "\\.jar\\$\\{path.separator\\}");
            String[] strArray = s2.split("\\$\\{path.separator\\}");
            String result = "";
            for (String s : strArray) {
                result = result + s + "\n";
            }

            handlerCtx.setOutputValue("formattedString", result.trim());


        }
    }
    
    //This converts any tab/NL etc to ${path.separator} before passing to backend for setting.
    //In domain.xml, it will be written out like  c:foo.jar${path.separator}c:bar.jar
    @Handler(id = "formatPathSeperatorStringsforSaving",
    input = {
        @HandlerInput(name = "string", type = String.class, required = true)},
    output = {
        @HandlerOutput(name = "formattedString", type = String.class)})
    public static void formatPathSeperatorStringsforSaving(HandlerContext handlerCtx) {
        String values = (String) handlerCtx.getInputValue("string");
        String token = "";
        if ((values != null) &&
                (values.toString().trim().length() != 0)) {
            Iterator it = GuiUtil.parseStringList(values, "\t\n\r\f").iterator();
            while (it.hasNext()) {
                String nextToken = (String) it.next();
                token += nextToken + PATH_SEPARATOR;
            }
            int end = token.length() - PATH_SEPARATOR.length();
            if (token.lastIndexOf(PATH_SEPARATOR) == end) {
                token = token.substring(0, end);
            }
        }
        handlerCtx.setOutputValue("formattedString", token);
    }    

    /**
     *
     */
    @Handler(id="addHandler",
    input={
        @HandlerInput(name="id", type=String.class, required=true),
        @HandlerInput(name="desc", type=String.class),
        @HandlerInput(name="class", type=String.class, required=true),
        @HandlerInput(name="method", type=String.class, required=true)
	})
    public static void addHandler(HandlerContext handlerCtx) {
	String id = (String) handlerCtx.getInputValue("id");
	String desc = (String) handlerCtx.getInputValue("desc");
	String cls = (String) handlerCtx.getInputValue("class");
	String meth = (String) handlerCtx.getInputValue("method");
	HandlerDefinition def = new HandlerDefinition(id);
	def.setHandlerMethod(cls, meth);
	if (desc != null) {
	    def.setDescription(desc);
	}
	LayoutDefinitionManager.addGlobalHandlerDefinition(def);
    }


    /**
     *	<p> A utility handler that resembles the for() method in Java.  Handler inside the for loop will be executed
     *  in a loop.  start index is specified by "start",  till less than "end".
     * eg. forLoop(start="1"  end="3" varName="foo"){}, handler inside the {} will be executed 2 times.
     *
     *  <p> Input value: "start" -- Type: <code>Integer</code> Start index, default to Zero is not specified
     *  <p> Input value: "end" -- Type: <code>Integer</code> End index.
     *  <p> Input value: "varName" -- Type: <code>String</code>  Variable to be replaced in the for loop by the index.
     *	@param	handlerCtx	The HandlerContext.
     */
    @Handler(id="forLoop",
    	input={
	    @HandlerInput(name="start", type=Integer.class),
        @HandlerInput(name="end", type=Integer.class, required=true),
        @HandlerInput(name="varName", type=String.class, required=true)}
        )
    public static boolean forLoop(HandlerContext handlerCtx) {

        Integer startInt = (Integer) handlerCtx.getInputValue("start");
        int start = (startInt == null) ? 0 : startInt.intValue();
        int end = ((Integer) handlerCtx.getInputValue("end")).intValue();
        String varName = ((String) handlerCtx.getInputValue("varName"));

        List<com.sun.jsftemplating.layout.descriptors.handler.Handler> handlers = handlerCtx.getHandler().getChildHandlers();
		if (handlers.size() > 0) {
            LayoutElement elt = handlerCtx.getLayoutElement();
            Map<String, Object> requestMap = handlerCtx.getFacesContext().getExternalContext().getRequestMap();
            for(int ix=start;  ix<=end; ix++){
                requestMap.put(varName, ix);
                //ignore whats returned by the handler.
                elt.dispatchHandlers(handlerCtx, handlers);
		    }
		}
        return false;
    }

    @Handler(id = "convertStrToBoolean",
    input = {
        @HandlerInput(name = "str", type = String.class, required = true)},
    output = {
        @HandlerOutput(name = "out", type = Boolean.class)})
    public static void convertStrToBoolean(HandlerContext handlerCtx) {

        String str = (String) handlerCtx.getInputValue("str");
        handlerCtx.setOutputValue("out", "true".equals(str));
    }

    /**
     *	<p> This method returns a new UIViewRoot with the basic JSFT settings
     *	    from the current ViewRoot.  If you intend to set this before the
     *	    current view is created (in an effort to swap out the UIViewRoot),
     *	    you should do so during the initPage event (take care to only do
     *	    this during the first request, or you might lose all child
     *	    components).</p>
     */
    @Handler(id = "createDefaultViewRoot",
	output = {
	    @HandlerOutput(name="viewRoot", type=UIViewRoot.class)})
    public static void createDefaultViewRoot(HandlerContext handlerCtx) {
	UIViewRoot oldVR = handlerCtx.getFacesContext().getViewRoot();
	UIViewRoot newVR = new UIViewRoot();
	newVR.setViewId(oldVR.getViewId());
	ViewRootUtil.setLayoutDefinitionKey(newVR, ViewRootUtil.getLayoutDefinitionKey(oldVR));
	newVR.setLocale(oldVR.getLocale());
	newVR.setRenderKitId(oldVR.getRenderKitId());
        handlerCtx.setOutputValue("viewRoot", newVR);
    }

    private static final String PATH_SEPARATOR = "${path.separator}";
}
