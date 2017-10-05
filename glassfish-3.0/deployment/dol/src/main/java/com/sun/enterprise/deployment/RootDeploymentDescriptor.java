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

import com.sun.enterprise.deployment.util.DOLUtils;
import com.sun.enterprise.deployment.util.ModuleDescriptor;
import com.sun.enterprise.deployment.util.XModuleType;

import javax.enterprise.deploy.shared.ModuleType;
import java.util.*;
import java.util.logging.Level;

/**
 * This descriptor contains all common information amongst root element 
 * of the J2EE Deployment Descriptors (application, ejb-jar, web-app, 
 * connector...). 
 *
 * @author Jerome Dochez
 */
public abstract class RootDeploymentDescriptor extends Descriptor {

     // the spec versions we should start to look at annotations
    private final static double ANNOTATION_RAR_VER = 1.6;
    private final static double ANNOTATION_EJB_VER = 3.0;
    private final static double ANNOTATION_WAR_VER = 2.5;
    private final static double ANNOTATION_CAR_VER = 5.0;

    /**
     * each module is uniquely identified with a moduleID
     */
    protected String moduleID;
    
    /**
     * version of the specification loaded by this descriptor
     */
    private String specVersion;

    /**
     * optional index string to disambiguate when serveral extensions are
     * part of the same module
     */
    private String index=null;
    
    /**
     * class loader associated to this module to load classes 
     * contained in the archive file
     */
    protected transient ClassLoader classLoader = null;

    /**
     * Extensions for this module descriptor, keyed by type, indexed using the instance's index
     */
    protected Map<Class<? extends RootDeploymentDescriptor>, List<RootDeploymentDescriptor>> extensions =
            new HashMap<Class<? extends RootDeploymentDescriptor>, List<RootDeploymentDescriptor>>();

        /**
     * contains the information for this module (like it's module name)
     */
    protected ModuleDescriptor moduleDescriptor;

    private boolean fullFlag = false;
    private boolean fullAttribute = false;
    private final static List<?> emptyList = new ArrayList();

    /**
     * Construct a new RootDeploymentDescriptor 
     */
    public RootDeploymentDescriptor() {
        super();
    }
    
    /**
     * Construct a new RootDeploymentDescriptor with a name and description
     */
    public RootDeploymentDescriptor(String name, String description) {
        super(name, description);
    }
    
    /**
     * each module is uniquely identified with a moduleID
     * @param moduleID for this module
     */
    public void setModuleID(String moduleID) {
        this.moduleID = moduleID;
    }
    
    /**
     * @return the module ID for this module descriptor
     */
    public abstract String getModuleID();

    /**
     * @return the default version of the deployment descriptor
     * loaded by this descriptor
     */
    public abstract String getDefaultSpecVersion();

    /**
     * Return true if this root deployment descriptor does not describe anything
     * @return true if this root descriptor is empty
     */
    public abstract boolean isEmpty();

        
    /**
     * @return the specification version of the deployment descriptor
     * loaded by this descriptor
     */
    public String getSpecVersion() {
        if (specVersion == null) {
            specVersion = getDefaultSpecVersion();
        } 
        try {
            Double.parseDouble(specVersion); 
        } catch (NumberFormatException nfe) {
            DOLUtils.getDefaultLogger().log(Level.WARNING, "invalidSpecVersion",
                new Object[] {specVersion, getDefaultSpecVersion()});
            specVersion = getDefaultSpecVersion();
        }

        return specVersion;
    }
    
    /**
     * Sets the specification version of the deployment descriptor
     * @param specVersion version number
     */
    public void setSpecVersion(String specVersion) {
        this.specVersion = specVersion;
    }      
    
    /**
     * @return the module type for this bundle descriptor
     */
    public abstract XModuleType getModuleType();

    /**
     * Sets the class loader for this application
     */
    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }
    
    /**               
     * @return the class loader associated with this module
     */
    public abstract ClassLoader getClassLoader();

    /**
     * sets the display name for this bundle
     */
    public void setDisplayName(String name) {
        super.setName(name);
    }
    
    /** 
     * @return the display name
     */
    public String getDisplayName() {
        return super.getName();
    }
    
    /**
     * as of J2EE1.4, get/setName are deprecated, 
     * people should use the set/getDisplayName or 
     * the set/getModuleID.
     */
    public void setName(String name) {
        setModuleID(name);
    }
    
    /**
     * as of J2EE1.4, get/setName are deprecated, 
     * people should use the set/getDisplayName or 
     * the set/getModuleID.
     * note : backward compatibility
     */     
    public String getName() {
        if (getModuleID()!=null) {
            return getModuleID();
        } else {
            return getDisplayName();
        }
    }
        
    public void setSchemaLocation(String schemaLocation) {
        addExtraAttribute("schema-location", schemaLocation);
    }
    
    public String getSchemaLocation() {
        return (String) getExtraAttribute("schema-location");
    }

   /**
     * @return the module descriptor for this bundle
     */
    public ModuleDescriptor getModuleDescriptor() {
        if (moduleDescriptor==null) {
            moduleDescriptor = new ModuleDescriptor();
            moduleDescriptor.setModuleType(getModuleType());
            moduleDescriptor.setDescriptor(this);
        }
        return moduleDescriptor;
    }

    /**
     * Sets the module descriptor for this bundle
     * @param descriptor for the module
     */
    public void setModuleDescriptor(ModuleDescriptor descriptor) {
        moduleDescriptor = descriptor;
        for (List<RootDeploymentDescriptor> extByType : this.extensions.values()) {
            if (extByType!=null) {
                for (RootDeploymentDescriptor ext : extByType) {
                    ext.setModuleDescriptor(descriptor);
                }
            }

        }
    }    
    
    /**
     * @return true if this module is an application object 
     */
    public abstract boolean isApplication();
    
    /**
     * print a meaningful string for this object
     */
    public void print(StringBuffer toStringBuffer) {
        super.print(toStringBuffer);
        toStringBuffer.append("\n Module Type = ").append(getModuleType());
        toStringBuffer.append("\n Module spec version = ").append(getSpecVersion());
        if (moduleID!=null) 
            toStringBuffer.append("\n Module ID = ").append(moduleID);
        if (getSchemaLocation()!=null)
            toStringBuffer.append("\n Client SchemaLocation = ").append(getSchemaLocation());
    }

    /**
     * This method returns all the extensions deployment descriptors in the scope
     * @return an unmodifiable collection of extensions or empty collection if none.
     */
    public Collection<RootDeploymentDescriptor> getExtensionsDescriptors() {
        ArrayList<RootDeploymentDescriptor> flattened = new ArrayList<RootDeploymentDescriptor>();
        for (List<? extends RootDeploymentDescriptor> extensionsByType : extensions.values()) {
            flattened.addAll(extensionsByType);
        }
        return Collections.unmodifiableCollection(flattened);
    }

    /**
     * This method returns all extensions of the passed type in the scope
     * @param type requested extension type
     * @return an unmodifiable collection of extensions or empty collection if none.
     */
    public <T extends RootDeploymentDescriptor> Collection<T> getExtensionsDescriptors(Class<T> type) {
        if (extensions.containsKey(type)) {
            return Collections.unmodifiableCollection((Collection<T>) extensions.get(type));
        } else {
            return (Collection<T>) emptyList;
        }
    }

    /**
     * This method returns one extension of the passed type in the scope with the right index
     * @param type requested extension type
     * @param index is the instance index
     * @return an unmodifiable collection of extensions or empty collection if none.
     */
    public <T extends RootDeploymentDescriptor>  T getExtensionsDescriptors(Class<? extends RootDeploymentDescriptor> type, String index) {
        for (T extension : (Collection<T>) getExtensionsDescriptors(type)) {
            String extensionIndex = ((RootDeploymentDescriptor)extension).index;
            if (index==null) {
                if (extensionIndex==null) {
                    return extension;
                }
            } else {
                if (index.equals(extensionIndex)) {
                    return extension;
                }
            }
        }
        return null;
    }

    public synchronized <T extends RootDeploymentDescriptor> void addExtensionDescriptor(Class<? extends RootDeploymentDescriptor> type, T instance, String index) {
        List<RootDeploymentDescriptor> values;
        if (extensions.containsKey(type)) {
            values = extensions.get(type);
        } else {
            values = new ArrayList<RootDeploymentDescriptor>();
            extensions.put(type, values);
        }
        ((RootDeploymentDescriptor)instance).index = index;
        values.add(instance);

    }

    /**
     * Sets the full flag of the bundle descriptor. Once set, the annotations
     * of the classes contained in the archive described by this bundle
     * descriptor will be ignored.
     * @param flag a boolean to set or unset the flag
     */
     public void setFullFlag(boolean flag) {
         fullFlag=flag;
     }

    /**
     * Sets the full attribute of the deployment descriptor
     * @param value the full attribute
     */
    public void setFullAttribute(String value) {
        fullAttribute = Boolean.valueOf(value);
    }

    /**
     * Get the full attribute of the deployment descriptor
     * @return the full attribute
     */
    public boolean isFullAttribute() {
        return fullAttribute;
    }

    /**
     * @ return true for following cases:
     *   1. When the full attribute is true. This attribute only applies to
     *      ejb module with schema version equal or later than 3.0;
            web module and schema version equal or later than than 2.5;
            appclient module and schema version equal or later than 5.0.
     *   2. When it's been tagged as "full" when processing annotations.
     *   3. When DD has a version which doesn't allowed annotations.
     *   return false otherwise.
     */
    public boolean isFullFlag() {
        // if the full attribute is true or it's been tagged as full,
        // return true
        if (fullAttribute == true || fullFlag == true) {
            return true;
        }
        return isDDWithNoAnnotationAllowed();
    }


    /**
     * @ return true for following cases:
     *   a. ejb module and schema version earlier than 3.0;
     *   b. web module and schema version earlier than 2.5;
     *   c. appclient module and schema version earlier than 5.0.
     *   d. connector module and schema version earlier than 1.6
     */
    public boolean isDDWithNoAnnotationAllowed() {
        XModuleType mType = getModuleType();

        double specVersion = Double.parseDouble(getSpecVersion());

            // we do not process annotations for earlier versions of DD
            if ( (mType.equals(XModuleType.EJB) &&
                  specVersion < ANNOTATION_EJB_VER) ||
                 (mType.equals(XModuleType.WAR) &&
                  specVersion < ANNOTATION_WAR_VER) ||
                 (mType.equals(XModuleType.CAR) &&
                  specVersion < ANNOTATION_CAR_VER)  ||
                 (mType.equals(XModuleType.RAR) &&
                  specVersion < ANNOTATION_RAR_VER)) {
                return true;
            } else {
                return false;
            }
    }    

}

