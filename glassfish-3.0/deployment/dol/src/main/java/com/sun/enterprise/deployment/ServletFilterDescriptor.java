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
package com.sun.enterprise.deployment;

import com.sun.enterprise.deployment.web.InitializationParameter;
import com.sun.enterprise.deployment.web.ServletFilter;

import java.util.Collection;
import java.util.Vector;

/**
 * Deployment object representing the servlet filter spec
 * @author Martin D. Flynn
 */
public class ServletFilterDescriptor
    extends Descriptor
    implements ServletFilter
{

    /** class name */
    private String className = "";

    /** display name */
    private String displayName = "";

    /** filter name */
    private String filterName = "";

    /** initialization parameters */
    private Vector initParms = new Vector();

    /** async supported */
    private Boolean asyncSupported = null;

    /* ----
    */

    /** generic constructor */
    public ServletFilterDescriptor() {
	super("", ""/*description*/);
	this.setClassName("");
    }

    /** constructor specifying descriptor name (Filter name) & displayName */
    public ServletFilterDescriptor(String className, String name) {
	super(name, ""/*description*/);
	this.setClassName(className);
    }

    /* ----
    */

    /** set class name */
    public void setClassName(String name) {
	this.className = (name != null)? name : "";
    }

    /** get class name */
    public String getClassName() {
	if (this.className == null) {
	    this.className = "";
	}
	return this.className;
    }

    /** set display name */
    public void setDisplayName(String name) {
	this.displayName = (name != null)? name : "";
    }

    /** get display name */
    public String getDisplayName() {
	String n = this.displayName;
	if ((n == null) || n.equals("")) {
	    n = this.getName();
	}
	return n;
    }

    public boolean hasSetDisplayName() {
        return (displayName != null && displayName.length() > 0);
    }

    /** set filter name */
    public void setName(String filterName) {
        this.filterName = filterName;
    }

    /** get filter name */
    public String getName() {
	if ((filterName == null) || filterName.equals("")) {
	    String c = this.getClassName();
	    int p = c.lastIndexOf('.');
	    filterName = (p < 0)? c : c.substring(p + 1);
	}
	return filterName;
    }

    /* ----
    */

    /* set initialization parameters */
    public void setInitializationParameters(Collection c) {
	this.initParms.clear();
	this.initParms.addAll(c);
    }

    /* get initialization parameters */
    public Vector getInitializationParameters() {
	return (Vector)this.initParms.clone();
    }

    /* add a single initialization parameter */
    public void addInitializationParameter(InitializationParameter ref) {
	this.initParms.addElement(ref);
    }
    
    /* add a single initialization parameter */
    public void addInitializationParameter(EnvironmentProperty ref) {
	addInitializationParameter((InitializationParameter) ref);
    }    

    /* remove a single initialization parameter */
    public void removeInitializationParameter(InitializationParameter ref) {
	this.initParms.removeElement(ref);
    }

    /* set asyncSupported */
    public void setAsyncSupported(Boolean asyncSupported) {
        this.asyncSupported = asyncSupported;
    }

    public Boolean isAsyncSupported() {
        return asyncSupported;
    }

    /* ----
    */

    /** compare equals */
    public boolean equals(Object obj) {
        //Should allow a filter with different name mapping
        //to the same class.
        if (obj instanceof ServletFilter) {
	    if (this.getClassName().equals(
                        ((ServletFilter)obj).getClassName())
                    && this.getName().equals(
                            ((ServletFilter)obj).getName())) {
                return true;
            }
        }

        return false;
    }

    public int hashCode() {
        int result = 17;
        result = 37*result + getClassName().hashCode();
        result = 37*result + getName().hashCode();
        return result;
    }
}
