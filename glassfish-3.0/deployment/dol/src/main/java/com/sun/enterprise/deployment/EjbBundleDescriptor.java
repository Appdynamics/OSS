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

import com.sun.enterprise.deployment.node.ejb.EjbBundleNode;
import com.sun.enterprise.deployment.runtime.IASPersistenceManagerDescriptor;
import com.sun.enterprise.deployment.runtime.PersistenceManagerInUse;
import com.sun.enterprise.deployment.runtime.common.SecurityRoleMapping;
import com.sun.enterprise.deployment.util.*;
import com.sun.enterprise.deployment.types.*;
import com.sun.enterprise.util.LocalStringManagerImpl;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * I represent all the configurable deployment information contained in
 * an EJB JAR.
 *
 * @author Danny Coward
 */

public class EjbBundleDescriptor extends BundleDescriptor implements WritableJndiNameEnvironment,
        EjbReferenceContainer, ResourceEnvReferenceContainer,
        ResourceReferenceContainer, ServiceReferenceContainer,
            MessageDestinationReferenceContainer {
 
    public final static String SPEC_VERSION = "2.1";
   
    private long uniqueId;    
    private Set<EjbDescriptor> ejbs = new HashSet<EjbDescriptor>();
    private Set<RelationshipDescriptor> relationships = new HashSet<RelationshipDescriptor>();
    private String relationshipsDescription;
    private String ejbClientJarUri;
    
    // list of configured persistence manager
    private Vector configured_pms = null;
    private PersistenceManagerInUse pm_inuse = null;
    
    // the resource (database) to be used for persisting CMP EntityBeans
    // the same resource is used for all beans in this ejb jar.
    private ResourceReferenceDescriptor cmpResourceReference;

    // Application exceptions defined for the ejbs in this module.
    private Map<String, EjbApplicationExceptionInfo> applicationExceptions =
        new HashMap<String, EjbApplicationExceptionInfo>();

    private static LocalStringManagerImpl localStrings =
	    new LocalStringManagerImpl(EjbBundleDescriptor.class);

    static Logger _logger = DOLUtils.getDefaultLogger();
    
    private List<SecurityRoleMapping> roleMaps = new ArrayList<SecurityRoleMapping>();

    // All interceptor classes defined within this ejb module, keyed by
    // interceptor class name.
    private Map<String, EjbInterceptor> interceptors = 
        new HashMap<String, EjbInterceptor>();
        
    private LinkedList<InterceptorBindingDescriptor> interceptorBindings =
        new LinkedList<InterceptorBindingDescriptor>();

    // EJB module level dependencies 
    private Set<EnvironmentProperty> environmentProperties =
              new HashSet<EnvironmentProperty>();
    private Set<EjbReference> ejbReferences =
              new HashSet<EjbReference>();
    private Set<JmsDestinationReferenceDescriptor> jmsDestReferences =
              new HashSet<JmsDestinationReferenceDescriptor>();
    private Set<MessageDestinationReferenceDescriptor> messageDestReferences =
              new HashSet<MessageDestinationReferenceDescriptor>();
    private Set<ResourceReferenceDescriptor> resourceReferences =
              new HashSet<ResourceReferenceDescriptor>();
    private Set<ServiceReferenceDescriptor> serviceReferences =
              new HashSet<ServiceReferenceDescriptor>();
    private Set<EntityManagerFactoryReferenceDescriptor>
              entityManagerFactoryReferences =
              new HashSet<EntityManagerFactoryReferenceDescriptor>();
    private Set<EntityManagerReferenceDescriptor>
              entityManagerReferences =
              new HashSet<EntityManagerReferenceDescriptor>();
    private Set<DataSourceDefinitionDescriptor> datasourceDefinitionDescs =
            new HashSet<DataSourceDefinitionDescriptor>();


    /** 
    * Constructs an ejb bundle descriptor with no ejbs.
    */
    public EjbBundleDescriptor() {
    }

    /**
     * True if EJB version is 2.x.  This is the default
     * for any new modules.
     */
    // XXX
    // this method is not true anymore now we have ejb3.0, keep this 
    // method as it is for now, will revisit once ejb30 persistence 
    // is implemented
    public boolean isEJB20() {
        return !isEJB11();
    }
    
    /**
     * True if EJB version is 1.x.
     */
    public boolean isEJB11() {
        return getSpecVersion().startsWith("1");
    }

    /**
     * @return the default version of the deployment descriptor
     * loaded by this descriptor
     */
    public String getDefaultSpecVersion() {
        return EjbBundleNode.SPEC_VERSION;
    }

    /**
    * Return the emptry String or the entry name of the ejb client JAR
    * in my archive if I have one.
    */
    public String getEjbClientJarUri() {
	if (this.ejbClientJarUri == null) {
	    this.ejbClientJarUri = "";
	}
	return this.ejbClientJarUri;
    }

    public boolean isEmpty() {
        return ejbs.isEmpty();
    }

    /**
    * Sets the ejb client JAR entry name.
    */
    
    public void setEjbClientJarUri(String ejbClientJarUri) {
	this.ejbClientJarUri = ejbClientJarUri;

    }

    public void addApplicationException(EjbApplicationExceptionInfo appExc) {
        applicationExceptions.put(appExc.getExceptionClassName(), appExc);
    }

    public Map<String, EjbApplicationExceptionInfo> getApplicationExceptions() {
        return new HashMap<String, EjbApplicationExceptionInfo>(applicationExceptions);
    }
     
    /**
    * Return the set of NamedDescriptors that I have.
    */
    public Collection getNamedDescriptors() {
	Collection namedDescriptors = new Vector();
        for (EjbDescriptor ejbDescriptor : this.getEjbs()) {
            namedDescriptors.add(ejbDescriptor);
            namedDescriptors.addAll(super.getNamedDescriptorsFrom(ejbDescriptor));
        }
	return namedDescriptors;
    }
    
    /**
    * Return all the named descriptors I have together with the descriptor
    * that references each one in a Vector of NameReferencePairs.
    */
    
    public Vector<NamedReferencePair> getNamedReferencePairs() {
	Vector<NamedReferencePair> pairs = new Vector<NamedReferencePair>();
        for (EjbDescriptor ejbDescriptor : this.getEjbs()) {
            pairs.add(NamedReferencePair.createEjbPair(ejbDescriptor,
                    ejbDescriptor));
            pairs.addAll(super.getNamedReferencePairsFrom(ejbDescriptor));
        }
	return pairs;
    } 
    
    /**
    * Return the set of references to resources held by ejbs defined in this module.
    */
    public Set<ResourceReferenceDescriptor> getEjbResourceReferenceDescriptors() {
	Set<ResourceReferenceDescriptor> resourceReferences = new HashSet<ResourceReferenceDescriptor>();
	for (Iterator itr = this.getEjbs().iterator(); itr.hasNext();) {
	    EjbDescriptor ejbDescriptor = (EjbDescriptor) itr.next();
	    resourceReferences.addAll(ejbDescriptor.getResourceReferenceDescriptors());
	}
	return resourceReferences;
    }
    
    /**
    * Return true if I reference other ejbs, false else.
    */
    public boolean hasEjbReferences() {
	for (Iterator itr = this.getEjbs().iterator(); itr.hasNext();) {
	    EjbDescriptor nextEjbDescriptor = (EjbDescriptor) itr.next();
	    if (!nextEjbDescriptor.getEjbReferenceDescriptors().isEmpty()) {
		return true;
	    }
	}
	return false;
    }

    /**
    * Return the Set of ejb descriptors that I have.
    */
    public Set<EjbDescriptor> getEjbs() {
	return this.ejbs;
    }
    
    /**
    * Returns true if I have an ejb descriptor by that name.
    */
    public boolean hasEjbByName(String name) {
	for (Iterator itr = this.getEjbs().iterator(); itr.hasNext();) {
	    Descriptor next = (Descriptor) itr.next();
	    if (next.getName().equals(name)) {
		return true;
	    }
	}
	return false;
    }
    
    /**
    * Returns an ejb descriptor that I have by the same name, otherwise 
    * throws an IllegalArgumentException
    */
    public EjbDescriptor getEjbByName(String name) {
        return getEjbByName(name, false);
    }

   /**
    * Returns an ejb descriptor that I have by the same name. 
    * Create a DummyEjbDescriptor if requested, otherwise
    * throws an IllegalArgumentException
    */
    public EjbDescriptor getEjbByName(String name, boolean isCreateDummy) {
       for (EjbDescriptor next : this.getEjbs()) {
           if (next.getName().equals(name)) {
               return next;
           }
       }

        if (!isCreateDummy) {   
            throw new IllegalArgumentException(localStrings.getLocalString(
                "enterprise.deployment.exceptionbeanbundle",
                "Referencing error: this bundle has no bean of name: {0}",
                    name));
        }

        // there could be cases where the annotation defines the ejb component
        // and the ejb-jar.xml just uses it 
        // we have to create a dummy version of the ejb descriptor in this 
        // case as we process xml before annotations.
        _logger.log(Level.FINE, "enterprise.deployment_dummy_ejb_descriptor",
                        new Object[] {name});
        DummyEjbDescriptor dummyEjbDesc = new DummyEjbDescriptor();
        dummyEjbDesc.setName(name);
        addEjb(dummyEjbDesc);
        return dummyEjbDesc;
    }

    /**
     * Returns all ejb descriptors that has a give Class name.
     * It returns an empty array if no ejb is found.
     */
    public EjbDescriptor[] getEjbByClassName(String className) {
        ArrayList<EjbDescriptor> ejbList = new ArrayList<EjbDescriptor>();
	for (Object ejb : this.getEjbs()) {
            if (ejb instanceof EjbDescriptor) {
                EjbDescriptor ejbDesc = (EjbDescriptor)ejb;
                if (className.equals(ejbDesc.getEjbClassName())) {
                    ejbList.add(ejbDesc);
                }
            }
	}
        return ejbList.toArray(new EjbDescriptor[ejbList.size()]);
    }
    
    /**
     * Returns all ejb descriptors that has a given Class name as
     * the web service endpoint interface.
     * It returns an empty array if no ejb is found.
     */
    public EjbDescriptor[] getEjbBySEIName(String className) {
        ArrayList<EjbDescriptor> ejbList = new ArrayList<EjbDescriptor>();
	for (Object ejb : this.getEjbs()) {
            if (ejb instanceof EjbDescriptor) {
                EjbDescriptor ejbDesc = (EjbDescriptor)ejb;
                if (className.equals(ejbDesc.getWebServiceEndpointInterfaceName())) {
                    ejbList.add(ejbDesc);
                }
            }
	}
        return ejbList.toArray(new EjbDescriptor[ejbList.size()]);
    }
    
    /**
    * Add an ejb to me.
    */
    public void addEjb(EjbDescriptor ejbDescriptor) {
	ejbDescriptor.setEjbBundleDescriptor(this);
	this.getEjbs().add(ejbDescriptor);
	
    }
    
    /**
    * Remove the given ejb descriptor from my (by equality).
    */
    
    public void removeEjb(EjbDescriptor ejbDescriptor) {
	ejbDescriptor.setEjbBundleDescriptor(null);
	this.getEjbs().remove(ejbDescriptor);

    }
     

    /**
     * Called only from EjbDescriptor.replaceEjbDescriptor, in wizard mode.
     */
    void replaceEjb(EjbDescriptor oldEjbDescriptor, EjbDescriptor newEjbDescriptor) {
	oldEjbDescriptor.setEjbBundleDescriptor(null);
	this.getEjbs().remove(oldEjbDescriptor);
	newEjbDescriptor.setEjbBundleDescriptor(this);
	this.getEjbs().add(newEjbDescriptor);
	//  no need to notify listeners in wizard mode ??
    }
    
    /**
     * @return true if this bundle descriptor contains at least one CMP
     * EntityBean
     */
    public boolean containsCMPEntity() {
        
        Set ejbs = getEjbs();
        if (ejbs==null)
            return false;
        for (Iterator ejbsItr = ejbs.iterator();ejbsItr.hasNext();) {
            if (ejbsItr.next() instanceof EjbCMPEntityDescriptor) {
                return true;
            }
        }
        return false;
    }

    public void addInterceptor(EjbInterceptor interceptor) {
        EjbInterceptor ic =
            getInterceptorByClassName(interceptor.getInterceptorClassName());
        if (ic == null) {
            interceptor.setEjbBundleDescriptor(this);
            interceptors.put(interceptor.getInterceptorClassName(), interceptor);
        }                 
    }
    
    public EjbInterceptor getInterceptorByClassName(String className) {

        return interceptors.get(className);

    }

    public boolean hasInterceptors() {

        return (interceptors.size() > 0);

    }

    public Set<EjbInterceptor> getInterceptors() {

        return new HashSet<EjbInterceptor>(interceptors.values());

    }

    public void prependInterceptorBinding(InterceptorBindingDescriptor binding)
    {
        interceptorBindings.addFirst(binding);
    }

    public void appendInterceptorBinding(InterceptorBindingDescriptor binding)
    {
        interceptorBindings.addLast(binding);
    }

    public List<InterceptorBindingDescriptor> getInterceptorBindings() {
        return new LinkedList<InterceptorBindingDescriptor>
            (interceptorBindings);
    }

    public void setInterceptorBindings(List<InterceptorBindingDescriptor>
                                       bindings) {
        interceptorBindings = new LinkedList<InterceptorBindingDescriptor>();
        interceptorBindings.addAll(bindings);
    }

    /**
    * Checks whether the role references my ejbs have reference roles that I have.
    */
    
    public boolean areResourceReferencesValid() {
	// run through each of the ejb's role references, checking that the roles exist in this bundle
	for (Iterator itr = this.getEjbs().iterator(); itr.hasNext();) {
	    EjbDescriptor ejbDescriptor = (EjbDescriptor) itr.next();
	    for (Iterator roleRefs = ejbDescriptor.getRoleReferences().iterator(); roleRefs.hasNext();) {
		RoleReference roleReference = (RoleReference) roleRefs.next();
		Role referredRole = roleReference.getRole();
		if (!referredRole.getName().equals("") 
		    && !super.getRoles().contains(referredRole) ) {
			
		    _logger.log(Level.FINE,localStrings.getLocalString(
			   "enterprise.deployment.badrolereference",
			   "Warning: Bad role reference to {0}", new Object[] {referredRole}));
		    _logger.log(Level.FINE,"Roles:  "+ this.getRoles());
		    return false;
		}
	    }
	}
	return true;
    }
    
    /**
    * Removes the given com.sun.enterprise.deployment.Role object from me.
    */
    public void removeRole(Role role) {
	if (super.getRoles().contains(role)) {
	    for (Iterator itr = this.getEjbs().iterator(); itr.hasNext();) {
		EjbDescriptor ejbDescriptor = (EjbDescriptor) itr.next();
		ejbDescriptor.removeRole(role);
	    }
	    super.removeRole(role);
	}
    }
    
    /**
    * Returns true if I have Roles to which method permissions have been assigned.
    */
    public boolean hasPermissionedRoles() {
	for (Iterator itr = this.getEjbs().iterator(); itr.hasNext();) {
	    EjbDescriptor nextEjbDescriptor = (EjbDescriptor) itr.next();
	    if (!nextEjbDescriptor.getPermissionedMethodsByPermission().isEmpty()) {
		return true;
	    }
	}
	return false;
    }
    
    /**
    * Return true if any of my ejb's methods have been assigned transaction attributes.
    */
    public boolean hasContainerTransactions() {
	for (Iterator itr = this.getEjbs().iterator(); itr.hasNext();) {
	    EjbDescriptor nextEjbDescriptor = (EjbDescriptor) itr.next();
	    if (!nextEjbDescriptor.getMethodContainerTransactions().isEmpty()) {
		return true;
	    }
	}
	return false;
    }
    
    /**
    * Return true if I have roles, permissioned roles or container transactions.
    */
    public boolean hasAssemblyInformation() {
	return (!this.getRoles().isEmpty())
		|| this.hasPermissionedRoles()
		    || this.hasContainerTransactions();
    
    }
    
    /**
     * Add a RelationshipDescriptor which describes a CMR field
     * between a bean/DO/entityRef in this ejb-jar.
     */
    public void addRelationship(RelationshipDescriptor relDesc)
    {
        relationships.add(relDesc);

    }

    /**
     * Add a RelationshipDescriptor which describes a CMR field
     * between a bean/DO/entityRef in this ejb-jar.
     */
    public void removeRelationship(RelationshipDescriptor relDesc)
    {
        relationships.remove(relDesc);

    }

 
    /**
     * EJB2.0: get description for <relationships> element.
     */
    public String getRelationshipsDescription() {
	if ( relationshipsDescription == null )
	    relationshipsDescription = "";
	return relationshipsDescription;
    }
 
    /**
     * EJB2.0: set description for <relationships> element.
     */
    public void setRelationshipsDescription(String relationshipsDescription) {
	this.relationshipsDescription = relationshipsDescription;
    }
    

    /**
     * Get all relationships in this ejb-jar.
     * @return a Set of RelationshipDescriptors.
     */
    public Set<RelationshipDescriptor> getRelationships()
    {
        return relationships;
    }

    public boolean hasRelationships()
    {
	return (relationships.size() > 0);
    }

    /**
     * Returns true if given relationship is already part of this
     * ejb-jar.
     */
    public boolean hasRelationship(RelationshipDescriptor rd) {
        return relationships.contains(rd);
    }

    /**
     * Return the Resource I use for CMP.
     */
    public ResourceReferenceDescriptor getCMPResourceReference() {
	return this.cmpResourceReference;
    }
    
    /**
     * Sets the resource reference I use for CMP.
     */
    public void setCMPResourceReference(ResourceReferenceDescriptor resourceReference) {
	this.cmpResourceReference = resourceReference;

    }
    


    public Descriptor getDescriptorByName(String name)
    {        
        try {
            return getEjbByName(name);
        } catch(IllegalArgumentException iae) {
            // Bundle doesn't contain ejb with the given name.
            return null;
        }
    }

    /**
    * Returns my name.
    */

    public String getName() {
	if ("".equals(super.getName())) {
	    super.setName("Ejb1");
	}
	return super.getName();
    }
    
    private void doMethodDescriptorConversions() throws Exception {
 	for (Iterator itr = this.getEjbs().iterator(); itr.hasNext();) {
 	    EjbDescriptor ejbDescriptor = (EjbDescriptor) itr.next();
 	    ejbDescriptor.doMethodDescriptorConversions();
 	}
    }
    
        // START OF IASRI 4645310 
    /**
     * Sets the unique id for a stand alone ejb module. It traverses through 
     * all the ejbs in this stand alone module and sets the unique id for
     * each of them. The traversal order is done in ascending element order.
     *
     * <p> Note: This method will not be called for application.
     *
     * @param    id    unique id for stand alone module
     */
    public void setUniqueId(long id) 
    {
        this.uniqueId  = id;

        // First sort the beans in alphabetical order.
        EjbDescriptor[] descs = ejbs.toArray(new EjbDescriptor[ejbs.size()]);


        // The sorting algorithm used by this api is a modified mergesort.
        // This algorithm offers guaranteed n*log(n) performance, and 
        // can approach linear performance on nearly sorted lists. 
        Arrays.sort(descs, 
            new Comparator<EjbDescriptor>() {
                public int compare(EjbDescriptor o1, EjbDescriptor o2) {
                    return o2.getName().compareTo(o1.getName());
                }
            }
        );

        for (int i=0; i<descs.length; i++)
        {
            // 2^16 beans max per stand alone module
            descs[i].setUniqueId( (id | i) );
        }
    }

    /**
     * Returns the unique id used in a stand alone ejb module.
     * For application, this will return zero.
     *
     * @return    the unique if used in stand alone ejb module
     */
    public long getUniqueId()
    {
        return uniqueId;
    }

    public static int getIdFromEjbId(long ejbId)
    {
	long id = ejbId >> 32;	
	return (int)id;
    }
    
    /**
     * @return true if this bundle descriptor defines web service clients
     */
    public boolean hasWebServiceClients() {
        for (EjbDescriptor next : getEjbs()) {
            Collection serviceRefs = next.getServiceReferenceDescriptors();
            if (!(serviceRefs.isEmpty())) {
                return true;
            }
        }
        return false;
    }  
    
    /**
     * @return a set of service-ref from ejbs contained in this bundle this bundle or empty set
     * if none
     */
    public Set<ServiceReferenceDescriptor> getEjbServiceReferenceDescriptors() {
        Set<ServiceReferenceDescriptor> serviceRefs = new OrderedSet<ServiceReferenceDescriptor>();
        for (EjbDescriptor next : getEjbs()) {
            serviceRefs.addAll(next.getServiceReferenceDescriptors());
        }
        return serviceRefs;        
    }    
    
    /** 
    * Returns a formatted String representing my state.
    */
    public void print(StringBuffer toStringBuffer) {
	toStringBuffer.append("EjbBundleDescriptor\n");
        super.print(toStringBuffer);
        if (cmpResourceReference!=null) {
            toStringBuffer.append("\ncmp resource ");
            cmpResourceReference.print(toStringBuffer);
        }
	toStringBuffer.append("\nclient JAR ").append(this.getEjbClientJarUri());
        for (Descriptor o : this.getEjbs()) {
            toStringBuffer.append("\n------------\n");
            o.print(toStringBuffer);
            toStringBuffer.append("\n------------");
        }
    }
    
    /** 
     * visit the descriptor and all sub descriptors with a DOL visitor implementation
     * 
     * @param aVisitor a visitor to traverse the descriptors
     */
    public void visit(DescriptorVisitor aVisitor) {
        if (aVisitor instanceof EjbBundleVisitor) {
            visit((EjbBundleVisitor) aVisitor);
        } else {
            super.visit(aVisitor);
        }
    }    

    /** 
     * visit the descriptor and all sub descriptors with a DOL visitor implementation
     * 
     * @param aVisitor a visitor to traverse the descriptors
     */
    public void visit(EjbBundleVisitor aVisitor) {
        aVisitor.accept(this);
        EjbVisitor ejbVisitor = aVisitor.getEjbVisitor();
        if (ejbVisitor != null) {
            for (EjbDescriptor anEjb : this.getEjbs()) {
                anEjb.visit(ejbVisitor);
            }
        }
        if (hasRelationships()) {
            for (Iterator itr = getRelationships().iterator();itr.hasNext();) {
                RelationshipDescriptor rd = (RelationshipDescriptor) itr.next();
                aVisitor.accept(rd);
            }
        }
        for (WebService aWebService : getWebServices().getWebServices()) {
            aVisitor.accept(aWebService);
        }
        for (MessageDestinationDescriptor msgDestDescriptor : getMessageDestinations()) {
            aVisitor.accept(msgDestDescriptor);
        }

        // Ejb-jar level dependencies

        // Visit all injectables first.  In some cases, basic type information
        // has to be derived from target inject method or inject field.
        for(InjectionCapable injectable : getInjectableResources(this)) {
            aVisitor.accept(injectable);
        }

        Set ejbRefs = getEjbReferenceDescriptors();
        for (Iterator itr = ejbRefs.iterator();itr.hasNext();) {
            aVisitor.accept((EjbReference) itr.next());
        }

        for (Iterator itr=getResourceReferenceDescriptors().iterator();
             itr.hasNext();) {
            ResourceReferenceDescriptor next =
                (ResourceReferenceDescriptor) itr.next();
            aVisitor.accept(next);
        }

        for (Iterator itr=getJmsDestinationReferenceDescriptors().iterator();
             itr.hasNext();) {
            JmsDestinationReferenceDescriptor next =
                (JmsDestinationReferenceDescriptor) itr.next();
            aVisitor.accept(next);
        }

        Set msgDestRefs = getMessageDestinationReferenceDescriptors();
        for (Iterator itr = msgDestRefs.iterator();itr.hasNext();) {
            aVisitor.accept((MessageDestinationReferencer) itr.next());
        }

        for (Iterator itr = getMessageDestinations().iterator();
                itr.hasNext();) {
            MessageDestinationDescriptor msgDestDescriptor =
                (MessageDestinationDescriptor)itr.next();
            aVisitor.accept(msgDestDescriptor);
        }

        Set serviceRefs = getServiceReferenceDescriptors();
        for (Iterator itr = serviceRefs.iterator();itr.hasNext();) {
            aVisitor.accept((ServiceReferenceDescriptor) itr.next());
        }
    }
 
    /**
     * @return the module type for this bundle descriptor
     */
    public XModuleType getModuleType() {
        return XModuleType.EJB;
    }  
    
    public void setPersistenceManagerInuse(String id,String ver)
    {
	pm_inuse=new PersistenceManagerInUse(id, ver);
        if (_logger.isLoggable(Level.FINE))
	    _logger.fine("***IASEjbBundleDescriptor"
                + ".setPersistenceManagerInUse done -#- ");
    }
    
    public void setPersistenceManagerInUse(PersistenceManagerInUse inuse) {
	pm_inuse = inuse;
    }
        
    public PersistenceManagerInUse getPersistenceManagerInUse()
    {
        return pm_inuse;
    }
                
    public void addPersistenceManager(IASPersistenceManagerDescriptor pmDesc)
    {
        if (configured_pms==null) {
            configured_pms=new Vector();
        }
        configured_pms.add(pmDesc);
        if (_logger.isLoggable(Level.FINE))
            _logger.fine("***IASEjbBundleDescriptor"
               + ".addPersistenceManager done -#- ");
    }
        
    public IASPersistenceManagerDescriptor getPreferredPersistenceManager()
    {
        boolean debug = _logger.isLoggable(Level.FINE);

        if (configured_pms == null || configured_pms.size() == 0) {
            // return the default persistence manager descriptor
            return null;
        }

        String pminuse_id 	= pm_inuse.get_pm_identifier().trim();
        String pminuse_ver  = pm_inuse.get_pm_version().trim();
        if (debug) {
             _logger.fine("IASPersistenceManagerDescriptor.getPreferred - inid*" + 
                pminuse_id.trim() + "*"); 
             _logger.fine("IASPersistenceManagerDescriptor.getPreferred - inver*" + 
                pminuse_ver.trim() + "*"); 
        }

        int size = configured_pms.size();
        for(int i = 0; i < size; i++) {
            IASPersistenceManagerDescriptor pmdesc=(IASPersistenceManagerDescriptor)configured_pms.elementAt(i);
	    String pmdesc_id 	= pmdesc.getPersistenceManagerIdentifier();
	    String pmdesc_ver 	= pmdesc.getPersistenceManagerVersion();

            if (debug) {
	        _logger.fine("IASPersistenceManagerDescriptor.getPreferred - pmid*" + 
                    pmdesc_id.trim() + "*"); 
	        _logger.fine("IASPersistenceManagerDescriptor.getPreferred - pmver*" + 
                    pmdesc_ver.trim() + "*"); 
            }


            if( ((pmdesc_id.trim()).equals(pminuse_id)) && 
                ((pmdesc_ver.trim()).equals(pminuse_ver)) ) {

                if (debug)
		    _logger.fine("***IASEjbBundleDescriptor.getPreferredPersistenceManager done -#- ");

                return pmdesc;
	    }
	}
	throw new IllegalArgumentException(localStrings.getLocalString(
	   "enterprise.deployment.nomatchingpminusefound",
	   "No PersistenceManager found that matches specified PersistenceManager in use."));
    }

    public Vector getPersistenceManagers()
    {
        if (_logger.isLoggable(Level.FINE))
	    _logger.fine("***IASEjbBundleDescriptor.getPersistenceManagers done -#- ");
	return configured_pms;
    }
    
    public void addSecurityRoleMapping(SecurityRoleMapping roleMapping) {
        roleMaps.add(roleMapping);
    }

    public List<SecurityRoleMapping> getSecurityRoleMappings() {
        return roleMaps;
    }

    /**
     * {@inheritDoc}
     */
    @Override public Collection<? extends PersistenceUnitDescriptor>
            findReferencedPUs() {
        Collection<PersistenceUnitDescriptor> pus =
                new HashSet<PersistenceUnitDescriptor>();
        for (EjbDescriptor ejb : getEjbs()) {
            pus.addAll(findReferencedPUsViaPURefs(ejb));
            pus.addAll(findReferencedPUsViaPCRefs(ejb));
        }
        return pus;
    }

/**
     * Returns the generated XML directory feturn the set of ejb references this ejb declares.
     */
    public Set<EjbReference> getEjbReferenceDescriptors() {
        return ejbReferences;
    }

    /**
     * Adds a reference to another ejb to me.
     */

    public void addEjbReferenceDescriptor(EjbReference ejbReference) {
        ejbReferences.add(ejbReference);
        ejbReference.setReferringBundleDescriptor(this);
    }

    public void removeEjbReferenceDescriptor(EjbReference ejbReference) {
        ejbReferences.remove(ejbReference);
    }

    /**
     * Return a reference to another ejb by the same name or throw an IllegalArgumentException.
     */
    public EjbReference getEjbReferenceByName(String name) {
        return (EjbReferenceDescriptor) getEjbReference(name);
    }

    public EjbReference getEjbReference(String name) {
        for (EjbReference er : getEjbReferenceDescriptors()) {
            if (er.getName().equals(name)) {
                return er;
            }
        }
        throw new IllegalArgumentException(localStrings.getLocalString(
                "enterprise.deployment.exceptionapphasnoejbrefbyname",
                "This ejb jar [{0}] has no ejb reference by the name of [{1}] ", new Object[]{getName(), name}));
    }

    public Set<ServiceReferenceDescriptor> getServiceReferenceDescriptors() {
        return serviceReferences;
    }

    public void addServiceReferenceDescriptor(ServiceReferenceDescriptor
            serviceRef) {
        serviceRef.setBundleDescriptor(this);
        serviceReferences.add(serviceRef);
    }

    public void removeServiceReferenceDescriptor(ServiceReferenceDescriptor
            serviceRef) {
        serviceReferences.remove(serviceRef);
    }

    /**
     * Looks up an service reference with the given name.
     * Throws an IllegalArgumentException if it is not found.
     */
    public ServiceReferenceDescriptor getServiceReferenceByName(String name) {
        for (Iterator itr = this.getServiceReferenceDescriptors().iterator();
             itr.hasNext();) {
            ServiceReferenceDescriptor srd = (ServiceReferenceDescriptor)
                    itr.next();
            if (srd.getName().equals(name)) {
                return srd;
            }
        }
        throw new IllegalArgumentException("No service ref of name " + name);
    }

    public Set<MessageDestinationReferenceDescriptor> getMessageDestinationReferenceDescriptors() {
        return messageDestReferences;
    }

    public void addMessageDestinationReferenceDescriptor
            (MessageDestinationReferenceDescriptor messageDestRef) {
        messageDestRef.setReferringBundleDescriptor(this);
        messageDestReferences.add(messageDestRef);
    }

    public void removeMessageDestinationReferenceDescriptor
            (MessageDestinationReferenceDescriptor msgDestRef) {
        messageDestReferences.remove(msgDestRef);
    }

    /**
     * Looks up an message destination reference with the given name.
     * Throws an IllegalArgumentException if it is not found.
     */
    public MessageDestinationReferenceDescriptor
        getMessageDestinationReferenceByName(String name) {

        for (MessageDestinationReferenceDescriptor mdr : messageDestReferences) {
            if (mdr.getName().equals(name)) {
                return mdr;
            }
        }
        throw new IllegalArgumentException("No message destination ref of name " + name);
    }

    /**
     * Return the set of JMS destination references this ejb declares.
     */
    public Set<JmsDestinationReferenceDescriptor> getJmsDestinationReferenceDescriptors() {
        return jmsDestReferences;
    }

    public void addJmsDestinationReferenceDescriptor(JmsDestinationReferenceDescriptor jmsDestReference) {
        jmsDestReferences.add(jmsDestReference);
    }

    public void removeJmsDestinationReferenceDescriptor(JmsDestinationReferenceDescriptor jmsDestReference) {
        jmsDestReferences.remove(jmsDestReference);
    }

    /**
     * Return a reference to another ejb by the same name or throw an IllegalArgumentException.
     */
    public JmsDestinationReferenceDescriptor getJmsDestinationReferenceByName(String name) {
        for (Iterator itr = this.getJmsDestinationReferenceDescriptors().iterator(); itr.hasNext();) {
            JmsDestinationReferenceDescriptor jdr = (JmsDestinationReferenceDescriptor) itr.next();
            if (jdr.getName().equals(name)) {
                return jdr;

            }
        }
        throw new IllegalArgumentException("No resource env ref of name " + name);
    }


    /**
     * Return the set of resource references this ejb declares.
     */
    public Set<ResourceReferenceDescriptor> getResourceReferenceDescriptors() {
        return resourceReferences;
    }
    /**
     * Adds a resource reference to me.
     */
    public void addResourceReferenceDescriptor(ResourceReferenceDescriptor resourceReference) {
        resourceReferences.add(resourceReference);
    }

    /**
     * Removes the given resource reference from me.
     */
    public void removeResourceReferenceDescriptor(ResourceReferenceDescriptor resourceReference) {
        resourceReferences.remove(resourceReference);
    }

    /**
     * Return the resource object corresponding to the supplied name or throw an illegal argument exception.
     */
    public ResourceReferenceDescriptor getResourceReferenceByName(String name) {
        for (Iterator itr = this.getResourceReferenceDescriptors().iterator(); itr.hasNext();) {
            ResourceReferenceDescriptor next = (ResourceReferenceDescriptor) itr.next();
            if (next.getName().equals(name)) {
                return next;
            }
        }
        throw new IllegalArgumentException("no resource ref of name " + name);
    }

    /**
     * Returns the environment property object searching on the supplied key.
     * throws an illegal argument exception if no such environment property exists.
     */
    public EnvironmentProperty getEnvironmentPropertyByName(String name) {
        for (Iterator itr = this.getEnvironmentProperties().iterator(); itr.hasNext();) {
            EnvironmentProperty ev = (EnvironmentProperty) itr.next();
            if (ev.getName().equals(name)) {
                return ev;
            }
        }
        throw new IllegalArgumentException("no env-entry of name " + name);
    }

    /**
     * Return a copy of the structure holding the environment properties.
     */
    public Set<EnvironmentProperty> getEnvironmentProperties() {
        return environmentProperties;
    }

    public void addEnvironmentProperty(EnvironmentProperty environmentProperty) {
        this.environmentProperties.add(environmentProperty);
    }

    /**
     * Removes the given environment property from me.
     */

    public void removeEnvironmentProperty(EnvironmentProperty environmentProperty) {
        this.getEnvironmentProperties().remove(environmentProperty);

    }

    public Set<EntityManagerFactoryReferenceDescriptor>
        getEntityManagerFactoryReferenceDescriptors() {

        return entityManagerFactoryReferences;
    }

    /**
     * Return the entity manager factory reference descriptor corresponding to
     * the given name.
     */
    public EntityManagerFactoryReferenceDescriptor
        getEntityManagerFactoryReferenceByName(String name) {
        for (EntityManagerFactoryReferenceDescriptor next :
                getEntityManagerFactoryReferenceDescriptors()) {
            if (next.getName().equals(name)) {
                return next;
            }
        }
        throw new IllegalArgumentException("No entity manager factory reference of name " + name);
    }

    public void addEntityManagerFactoryReferenceDescriptor
            (EntityManagerFactoryReferenceDescriptor reference) {
        reference.setReferringBundleDescriptor(this);
        this.entityManagerFactoryReferences.add(reference);
    }

    public Set<EntityManagerReferenceDescriptor>
        getEntityManagerReferenceDescriptors() {

        return entityManagerReferences;
    }

    /**
     * Return the entity manager factory reference descriptor corresponding to
     * the given name.
     */
    public EntityManagerReferenceDescriptor
        getEntityManagerReferenceByName(String name) {
        for (EntityManagerReferenceDescriptor next :
                getEntityManagerReferenceDescriptors()) {

            if (next.getName().equals(name)) {
                return next;
            }
        }
        throw new IllegalArgumentException("No entity manager reference of name " + name);
    }

    public void addEntityManagerReferenceDescriptor
            (EntityManagerReferenceDescriptor reference) {
        reference.setReferringBundleDescriptor(this);
        this.getEntityManagerReferenceDescriptors().add(reference);
    }

    public Set<DataSourceDefinitionDescriptor> getDataSourceDefinitionDescriptors() {
        return datasourceDefinitionDescs;
    }


    public void addDataSourceDefinitionDescriptor(DataSourceDefinitionDescriptor reference) {
        for(Iterator itr = this.getDataSourceDefinitionDescriptors().iterator(); itr.hasNext();){
            DataSourceDefinitionDescriptor desc = (DataSourceDefinitionDescriptor)itr.next();
            if(desc.getName().equals(reference.getName())){
                throw new IllegalStateException(
                        localStrings.getLocalString("exceptionapplicationduplicatedatasourcedefinition",
                                "This application [{0}] cannot have datasource definitions of same name : [{1}]",
                                getName(), reference.getName()));
            }
        }
        getDataSourceDefinitionDescriptors().add(reference);
    }

    public void removeDataSourceDefinitionDescriptor(DataSourceDefinitionDescriptor reference) {
        this.getDataSourceDefinitionDescriptors().remove(reference);
    }

     public List<InjectionCapable> getInjectableResourcesByClass(String className) {
        return (getInjectableResourcesByClass(className, this));
    }

    public InjectionInfo getInjectionInfoByClass(Class clazz) {
        return (getInjectionInfoByClass(clazz, this));
    }

    //
    // There is still some redundant DOL processing of the modules that can result in these
    // being called so just treat them as no-ops.
    //

    public Set<LifecycleCallbackDescriptor>
        getPostConstructDescriptors() {
        return new HashSet<LifecycleCallbackDescriptor>();
    }

    public void addPostConstructDescriptor(LifecycleCallbackDescriptor
        postConstructDesc) {
        // no-op
    }

    public LifecycleCallbackDescriptor
        getPostConstructDescriptorByClass(String className) {
        return null;
    }

    public Set<LifecycleCallbackDescriptor> getPreDestroyDescriptors() {
        return new HashSet<LifecycleCallbackDescriptor>();
    }

    public void addPreDestroyDescriptor(LifecycleCallbackDescriptor preDestroyDesc) {
        // no-op
    }

    public LifecycleCallbackDescriptor getPreDestroyDescriptorByClass(String className) {
        return null;
    }

    //
    //  end no-op PostConstruct/PreDestroy methods
    //





}
