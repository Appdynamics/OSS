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

package com.sun.enterprise.naming.impl;

import org.jvnet.hk2.annotations.Service;
import org.jvnet.hk2.annotations.Scoped;
import org.jvnet.hk2.annotations.Inject;

import org.jvnet.hk2.component.*;

import org.glassfish.api.invocation.InvocationManager;
import org.glassfish.api.invocation.ComponentInvocation;

import org.glassfish.api.naming.GlassfishNamingManager;
import org.glassfish.api.naming.JNDIBinding;
import org.glassfish.api.naming.NamingObjectProxy;
import javax.naming.InitialContext;

import com.sun.enterprise.naming.util.LogFacade;
import com.sun.hk2.component.*;

import org.omg.CORBA.ORB;
import java.rmi.RemoteException;
import java.rmi.Remote;

import javax.naming.*;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.*;



/**
 * This is the manager that handles all naming operations including
 * publishObject as well as binding environment props, resource and ejb
 * references in the namespace.
 */

@Service
@Scoped(Singleton.class)
public final class  GlassfishNamingManagerImpl
        implements GlassfishNamingManager {

    static Logger _logger = LogFacade.getLogger();

    public static final String IIOPOBJECT_FACTORY =
            "com.sun.enterprise.naming.util.IIOPObjectFactory";

    private static final int JAVA_COMP_LENGTH = "java:comp".length();
    private static final int JAVA_MODULE_LENGTH = "java:module".length();

    @Inject
    Habitat habitat;

    //@Inject
    volatile InvocationManager invMgr=null;

    private InitialContext initialContext;
    private Context cosContext;

    private NameParser nameParser = new SerialNameParser();

    private Map componentNamespaces;
    private Map<String, Map> appNamespaces;
    private Map<AppModuleKey, Map> moduleNamespaces;
    private Map<String, ComponentIdInfo> componentIdInfo;


    public GlassfishNamingManagerImpl() throws NamingException {
        this(new InitialContext());
    }

    //Used only for Junit Testing
    void setInvocationManager(final InvocationManager invMgr) {
        this.invMgr = invMgr;
    }

    /**
     * Create the naming manager. Creates a new initial context.
     */
    public GlassfishNamingManagerImpl(InitialContext ic)
            throws NamingException {
        initialContext = ic;
        componentNamespaces = new Hashtable();
        appNamespaces = new HashMap<String, Map>();
        moduleNamespaces = new HashMap<AppModuleKey, Map>();
        componentIdInfo = new HashMap<String, ComponentIdInfo>();

        JavaURLContext.setNamingManager(this);
    }


    /**
     * Get the initial naming context.
     */
    public Context getInitialContext() {
        return initialContext;
    }


    public NameParser getNameParser() {
        return nameParser;
    }

    
    public Remote initializeRemoteNamingSupport(ORB orb) throws NamingException {

        Remote remoteProvider = null;

        try {
            // Now that we have an ORB, initialize the CosNaming service
            // and set it on the server's naming service.

            Hashtable cosNamingEnv = new Hashtable();

            cosNamingEnv.put("java.naming.factory.initial",
                             "com.sun.jndi.cosnaming.CNCtxFactory");

            cosNamingEnv.put("java.naming.corba.orb", orb);

            cosContext = new InitialContext(cosNamingEnv);

            ProviderManager pm = ProviderManager.getProviderManager();

            // Initialize RemoteSerialProvider.  This allows access to the naming
            // service from clients.
            remoteProvider = pm.initRemoteProvider(orb);

        } catch(RemoteException re) {
            NamingException ne = new NamingException("Exception during remote naming initialization");
            ne.initCause(ne);
            throw ne;
        }

        return remoteProvider;
    }

    private Context getCosContext() {
        return cosContext;
    }

    /**
     * Publish a name in the naming service.
     *
     * @param name   Name that the object is bound as.
     * @param obj    Object that needs to be bound.
     * @param rebind flag
     * @throws javax.naming.NamingException if there is a naming exception.
     */
    public void publishObject(String name, Object obj, boolean rebind)
            throws NamingException {
        Name nameobj = new CompositeName(name);
        publishObject(nameobj, obj, rebind);
    }

    /**
     * Publish a name in the naming service.
     *
     * @param name   Name that the object is bound as.
     * @param obj    Object that needs to be bound.
     * @param rebind flag
     * @throws javax.naming.NamingException if there is a naming exception.
     */
    public void publishObject(Name name, Object obj, boolean rebind)
            throws NamingException {

        Object serialObj = obj;

        if (rebind) {
            initialContext.rebind(name, serialObj);
        } else {
            initialContext.bind(name, serialObj);
        }
    }

   
    public void publishCosNamingObject(String name, Object obj, boolean rebind)
            throws NamingException {


        Name nameObj = new CompositeName(name);

        // Create any COS naming sub-contexts in name
        // that don't already exist.
        createSubContexts(nameObj, getCosContext());

        if (rebind) {
            getCosContext().rebind(name, obj);
        } else {
            getCosContext().bind(name, obj);
        }

        // Bind a reference to it in the SerialContext using
        // the same name. This is needed to allow standalone clients
        // to lookup the object using the same JNDI name.
        // It is also used from bindObjects while populating ejb-refs in
        // the java:comp namespace.
        Object serialObj = new Reference("reference",
                    new StringRefAddr("url", name.toString()),
                    IIOPOBJECT_FACTORY, null);

        publishObject(name, serialObj, rebind);

    }

    public void unpublishObject(String name)
            throws NamingException {


        initialContext.unbind(name);
    }

    /**
     * Remove an object from the naming service.
     *
     * @param name Name that the object is bound as.
     * @throws Exception
     */
    public void unpublishCosNamingObject(String name)
            throws NamingException {


        try {
            getCosContext().unbind(name);
        } catch(NamingException cne) {
           _logger.log(Level.WARNING, "Error during CosNaming.unbind for " + name); 
        }


        initialContext.unbind(name);
    }


    /**
     * Remove an object from the naming service.
     *
     * @param name Name that the object is bound as.
     * @throws Exception
     */
    public void unpublishObject(Name name) throws NamingException {
        this.unpublishObject(name.toString());
    }

    /**
     * Create any sub-contexts in name that don't already exist.
     *
     * @param name    Name containing sub-contexts to create
     * @param rootCtx in which sub-contexts should be created
     * @throws Exception
     */
    private void createSubContexts(Name name, Context rootCtx) throws NamingException {

        int numSubContexts = name.size() - 1;
        Context currentCtx = rootCtx;
        if (_logger.isLoggable(Level.FINE)) {
            _logger.log(Level.FINE, "Creating sub contexts for " + name);
        }

        for (int subCtxIndex = 0; subCtxIndex < numSubContexts; subCtxIndex++) {
            String subCtxName = name.get(subCtxIndex);
            try {

                Object obj = currentCtx.lookup(subCtxName);

                if (obj == null) {
                    // @@@ thought it should throw NameNotFound when
                    // context doesn't exist...
                    if (_logger.isLoggable(Level.FINE)) {
                        _logger.log(Level.FINE, "name == null");
                    }
                    // Doesn't exist so create it.
                    Context newCtx = currentCtx.createSubcontext(subCtxName);
                    currentCtx = newCtx;
                } else if (obj instanceof Context) {
                    // OK -- no need to create it.
                    currentCtx = (Context) obj;
                } else {
                    // Context name clashes with existing object.
                    throw new NameAlreadyBoundException(subCtxName);
                }
            }
            catch (NameNotFoundException e) {
                _logger.log(Level.FINE, "name not found", e);

                // Doesn't exist so create it.
                Context newCtx = currentCtx.createSubcontext(subCtxName);
                currentCtx = newCtx;
            }
        } // End for -- each sub-context

        return;
    }

    private Map getComponentNamespace(String componentId)
            throws NamingException {
        // Note: HashMap is not synchronized. The namespace is populated
        // at deployment time by a single thread, and then on there are
        // no structural modifications (i.e. no keys added/removed).
        // So the namespace doesnt need to be synchronized.
        Map namespace = (Map) componentNamespaces.get(componentId);
        if (namespace == null) {
            namespace = new HashMap();
            componentNamespaces.put(componentId, namespace);

            // put entries for java:, java:comp and java:comp/env
            JavaURLContext jc = new JavaURLContext("java:", null);
            namespace.put("java:", jc);
            namespace.put("java:/", jc);
            JavaURLContext jcc = new JavaURLContext("java:comp", null);

            namespace.put("java:comp", jcc);
            namespace.put("java:comp/", jcc);
            JavaURLContext jccEnv = new JavaURLContext("java:comp/env", null);
            namespace.put("java:comp/env", jccEnv);
            namespace.put("java:comp/env/", jccEnv);
        }

        return namespace;
    }

    private Map getModuleNamespace(AppModuleKey appModuleKey)
            throws NamingException {

         if( (appModuleKey.getAppName() == null) ||
             (appModuleKey.getModuleName() == null) ) {
             throw new NamingException("Invalid appModuleKey " + appModuleKey);
         }

        // Note: HashMap is not synchronized. The namespace is populated
        // at deployment time by a single thread, and then on there are
        // no structural modifications (i.e. no keys added/removed).
        // So the namespace doesnt need to be synchronized.
        Map namespace = moduleNamespaces.get(appModuleKey);
        if (namespace == null) {
            namespace = new Hashtable();
            moduleNamespaces.put(appModuleKey, namespace);

            // put entries for java:, java:comp and java:comp/env
            JavaURLContext jc = new JavaURLContext("java:", null);
            namespace.put("java:", jc);
            namespace.put("java:/", jc);

            JavaURLContext jMod = new JavaURLContext("java:module", null);
            namespace.put("java:module", jMod);
            namespace.put("java:module/", jMod);
            JavaURLContext jModEnv = new JavaURLContext("java:module/env", null);
            namespace.put("java:module/env", jModEnv);
            namespace.put("java:module/env/", jModEnv);

        }

        return namespace;
    }

     private Map getAppNamespace(String appName)
            throws NamingException {

         if( appName == null ) {
             throw new NamingException("Null appName");
         }

        // Note: HashMap is not synchronized. The namespace is populated
        // at deployment time by a single thread, and then on there are
        // no structural modifications (i.e. no keys added/removed).
        // So the namespace doesnt need to be synchronized.
        Map namespace = appNamespaces.get(appName);
        if (namespace == null) {
            namespace = new Hashtable();
            appNamespaces.put(appName, namespace);

            // put entries for java:, java:comp and java:comp/env
            JavaURLContext jc = new JavaURLContext("java:", null);
            namespace.put("java:", jc);
            namespace.put("java:/", jc);

                 JavaURLContext jApp = new JavaURLContext("java:app", null);
            namespace.put("java:app", jApp);
            namespace.put("java:app/", jApp);
            JavaURLContext jAppEnv = new JavaURLContext("java:app/env", null);
            namespace.put("java:app/env", jAppEnv);
            namespace.put("java:app/env/", jAppEnv);

        }

        return namespace;
    }


    private Map getNamespace(String componentId, String logicalJndiName) throws NamingException {

        ComponentIdInfo info = componentIdInfo.get(componentId);
        
        return (info != null) ?  getNamespace(info.appName, info.moduleName, componentId, logicalJndiName) :
                getComponentNamespace(componentId);
    }

    private Map getNamespace(String appName, String moduleName,
                             String componentId, String logicalJndiName) throws NamingException {

        Map namespace = null;

        if( logicalJndiName.startsWith("java:comp") ) {
            namespace = getComponentNamespace(componentId);
        } else if ( logicalJndiName.startsWith("java:module")) {
            namespace = getModuleNamespace(new AppModuleKey(appName, moduleName));
        } else if ( logicalJndiName.startsWith("java:app")) {
            namespace = getAppNamespace(appName);
        }  else {
            // return component namespace
            namespace = getComponentNamespace(componentId);
        }

        return namespace;
    }

    /**
     * This method binds them in a java:namespace.
     */

    private void bindToNamespace(Map namespace, String logicalJndiName, Object value)
            throws NamingException {


        if (_logger.isLoggable(Level.FINE)) {
            _logger.log(Level.FINE,
                    "naming.bind Binding name:{0}",
                    new Object[]{logicalJndiName});
        }

        if (namespace.put(logicalJndiName, value) != null) {
            _logger.log(Level.WARNING,
                    "naming.alreadyexists" +
                            "Reference name [{0}] already exists for {1}",
                    new Object[]{logicalJndiName, namespace.toString()});
        }

        bindIntermediateContexts(namespace, logicalJndiName);
    }

    private boolean existsInNamespace(Map namespace, String logicalJndiName) {

        return namespace.containsKey(logicalJndiName);

    }

    /**
     * This method enumerates the env properties, ejb and resource references
     * etc for a J2EE component and binds them in the component's java:comp
     * namespace.
     */
    public void bindToComponentNamespace(String appName, String moduleName,
                                         String componentId,  boolean treatComponentAsModule,
                                         Collection<? extends JNDIBinding> bindings)
            throws NamingException {

        // These are null in rare cases, e.g. default web app.
        if( (appName != null) && (moduleName != null) ) {

            ComponentIdInfo info = new ComponentIdInfo();
            info.appName = appName;
            info.moduleName = moduleName;
            info.componentId = componentId;
            info.treatComponentAsModule = treatComponentAsModule;

            componentIdInfo.put(componentId, info);
        }

        for (JNDIBinding binding : bindings) {

            String logicalJndiName = binding.getName();

            Map namespace = null;

            if( treatComponentAsModule && logicalJndiName.startsWith("java:comp")) {
                logicalJndiName = logicalCompJndiNameToModule(logicalJndiName);
            }
            
            if ( logicalJndiName.startsWith("java:comp")) {

                namespace = getComponentNamespace(componentId);


            } else if ( logicalJndiName.startsWith("java:module")) {

                namespace = getModuleNamespace(new AppModuleKey(appName, moduleName));

            } else if ( logicalJndiName.startsWith("java:app")) {

                namespace = getAppNamespace(appName);
            }

            if( namespace != null ) {
                if( !existsInNamespace(namespace, logicalJndiName)) {
                    bindToNamespace(namespace, logicalJndiName, binding.getValue());
                }
            }
        }
    }

    private String logicalCompJndiNameToModule(String logicalCompName) {
        String tail = logicalCompName.substring(JAVA_COMP_LENGTH);
        String logicalModuleJndiName = "java:module" + tail;
        return logicalModuleJndiName;
    }

    private String logicalModuleJndiNameToComp(String logicalModuleName) {
        String tail = logicalModuleName.substring(JAVA_MODULE_LENGTH);
        String logicalCompJndiName = "java:comp" + tail;
        return logicalCompJndiName;
    }


    public void bindToAppNamespace(String appName, Collection<? extends JNDIBinding> bindings)
            throws NamingException {

          for (JNDIBinding binding : bindings) {
            String logicalJndiName = binding.getName();

              if ( logicalJndiName.startsWith("java:app")) {
                    Map namespace = getAppNamespace(appName);
                    bindToNamespace(namespace, binding.getName(), binding.getValue());
              }
          }

    }

    private void bindIntermediateContexts(Map namespace, String name)
            throws NamingException {
        // for each component of name, put an entry into namespace
        String partialName = null;
        if( name.startsWith("java:comp/") ) {
            partialName = "java:comp";
        } else if( name.startsWith("java:module/")) {
            partialName = "java:module";
        } else if( name.startsWith("java:app/")) {
            partialName = "java:app";
        } else {
            throw new NamingException("Invalid environment namespace name : " + name);
        }

        name = name.substring((partialName + "/").length());
        StringTokenizer toks = new StringTokenizer(name, "/", false);

        while (toks.hasMoreTokens()) {
            String tok = toks.nextToken();
            partialName = partialName + "/" + tok;
            if (namespace.get(partialName) == null) {

                namespace.put(partialName,
                        new JavaURLContext(partialName, null));
            }
        }
    }


    /**
     * This method enumerates the env properties, ejb and resource references
     * and unbinds them from the java:comp namespace.
     */
    public void unbindComponentObjects(String componentId) throws NamingException {
        componentNamespaces.remove(componentId); // remove local namespace cache
        componentIdInfo.remove(componentId);
    }

    public void unbindAppObjects(String appName) throws NamingException {

        appNamespaces.remove(appName);
        Iterator moduleEntries = moduleNamespaces.entrySet().iterator();
        while( moduleEntries.hasNext() ) {
            Map.Entry entry = (Map.Entry) moduleEntries.next();

            if( ((AppModuleKey) entry.getKey()).getAppName().equals(appName)) {
                moduleEntries.remove();    
            }
        }
    }


    /**
     * Recreate a context for java:comp/env or one of its sub-contexts given the
     * context name.
     */
    public Context restoreJavaCompEnvContext(String contextName)
            throws NamingException {
        if (!contextName.startsWith("java:")) {
            throw new NamingException("Invalid context name [" + contextName
                    + "]. Name must start with java:");
        }

        return new JavaURLContext(contextName, null);
    }

    public Object lookup(String name) throws NamingException {
        return lookup(name, (SerialContext)null);
    }

    /**
     * This method is called from SerialContext class. The serialContext
     * instance that was created by the appclient's Main class is passed so that
     * stickiness is preserved. Called from javaURLContext.lookup, for java:comp
     * names.
     */
    public Object lookup(String name, SerialContext serialContext)
            throws NamingException {
        _logger.fine("serialcontext in GlassfishNamingManager.." + serialContext);
        Context ic = null;

        if (serialContext != null) {
            ic = serialContext;
        } else {
            ic = initialContext;
        }

        // initialContext is used as ic in case of PE while
        // serialContext is used as ic in case of EE/SE
        if (_logger.isLoggable(Level.FINE))
            _logger.log(Level.FINE, "GlassfishNamingManager : looking up name : "
                    + name);

        // Get the component id and namespace to lookup
        String componentId = getComponentId();

        return lookup(componentId, name, ic);
    }

    /**
     * Lookup object for a particular componentId and name.
     */
    public Object lookup(String componentId, String name) throws NamingException {

        return lookup(componentId, name, initialContext);

    }

    private Object lookup(String componentId, String name, Context ctx) throws NamingException {

        ComponentIdInfo info = componentIdInfo.get(componentId);
        String logicalJndiName = name;
        boolean replaceName =  (info != null) && (info.treatComponentAsModule)
                && name.startsWith("java:comp");
        if( replaceName ) {
            logicalJndiName = logicalCompJndiNameToModule(name);
        }

        Map namespace = getNamespace(componentId, logicalJndiName);

        Object obj = namespace.get(logicalJndiName);

        if (obj == null)
            throw new NameNotFoundException("No object bound to name " + name);

        if (obj instanceof NamingObjectProxy) {
            NamingObjectProxy namingProxy = (NamingObjectProxy) obj;
            obj = namingProxy.create(ctx);
        } else if( obj instanceof Context ) {
            // Need to preserve the original prefix so that further operations on the
            // context maintain the correct external view. In the case of a replaced java:comp,
            // create a new equivalent javaURLContext and return that.
            if( replaceName ) {
                obj = new JavaURLContext(name, null);
            }
        }

        return obj;

    }


    public NamingEnumeration list(String name) throws NamingException {
        ArrayList list = listNames(name);
        return new NamePairsEnum(this, list.iterator());
    }

    public NamingEnumeration listBindings(String name) throws NamingException {
        ArrayList list = listNames(name);
        return new BindingsIterator(this, list.iterator());
    }

    private ArrayList listNames(String name) throws NamingException {
        // Get the component id and namespace to lookup
        String componentId = getComponentId();

        ComponentIdInfo info = componentIdInfo.get(componentId);
        String logicalJndiName = name;

        boolean replaceName =  (info != null) && (info.treatComponentAsModule)
                && name.startsWith("java:comp");
        if( replaceName ) {
            logicalJndiName = logicalCompJndiNameToModule(name);
        }

        Map namespace = getNamespace(componentId, logicalJndiName);

        Object obj = namespace.get(logicalJndiName);

        if (obj == null)
            throw new NameNotFoundException("No object bound to name " + name);

        if (!(obj instanceof JavaURLContext))
            throw new NotContextException(name + " cannot be listed");

        // This iterates over all names in entire component namespace,
        // so its a little inefficient. The alternative is to store
        // a list of bindings in each javaURLContext instance.
        ArrayList list = new ArrayList();
        Iterator itr = namespace.keySet().iterator();
        if (!logicalJndiName.endsWith("/"))
            logicalJndiName = logicalJndiName + "/";
        while (itr.hasNext()) {
            String key = (String) itr.next();
            // Check if key begins with name and has only 1 component extra
            // (i.e. no more slashes)
            // Make sure keys reflect the original prefix in the case of comp->module
            // replacement
            if (key.startsWith(logicalJndiName) && key.indexOf('/', logicalJndiName.length()) == -1) {
                String toAdd = replaceName ? logicalModuleJndiNameToComp(key) : key;
                list.add(toAdd);
            }
        }
        return list;
    }

    /**
     * Get the component id from the Invocation Manager.
     *
     * @return the component id as a string.
     */
    private String getComponentId() throws NamingException {
        String id = null;

        ComponentInvocation ci;
        if (invMgr==null) {
            ci= habitat.getByContract(InvocationManager.class).getCurrentInvocation();
        } else {
            ci= invMgr.getCurrentInvocation();
        }
        
        if (ci == null) {
            throw new NamingException("Invocation exception: Got null ComponentInvocation ");
        }

        try {
            id = ci.getComponentId();
            if (id == null) {
                NamingException nameEx = new NamingException(
                        "Invocation exception: ComponentId is null");
                throw nameEx;
            }
        } catch (Throwable th) {
            NamingException ine = new NamingException("Invocation exception: " + th);
            ine.initCause(th);
            throw ine;
        }

        return id;
    }

    private boolean isCOSNamingObj(Object obj) {
        return ((obj instanceof java.rmi.Remote) || (obj instanceof org.omg.CORBA.Object));
    }


    private static class AppModuleKey {

        private String app;
        private String module;

        public AppModuleKey(String appName, String moduleName) {

            app = appName;
            module = moduleName;

        }

        public boolean equals(Object o) {
            boolean equal = false;
            if( (o != null) && (o instanceof AppModuleKey) ) {
                AppModuleKey other = (AppModuleKey) o;
                if( app.equals(other.app) && module.equals(other.module) ) {
                    equal = true;
                }
            }
            return equal;
        }

        public int hashCode() {
            return app.hashCode();
        }

        public String getAppName() { return app; }
        public String getModuleName() { return module; }

        public String toString()
            { return "appName = " + app + " , module = " + module; }
    }

    private static class ComponentIdInfo {

        String appName;
        String moduleName;
        String componentId;
        boolean treatComponentAsModule;

         public String toString()
            { return "appName = " + appName + " , module = " + moduleName +
                    " , componentId = " + componentId
                    + ", treatComponentAsModule = " + treatComponentAsModule; }
    }

}

// Class for enumerating bindings
class BindingsIterator implements NamingEnumeration {
    GlassfishNamingManagerImpl nm;

    Iterator names;

    BindingsIterator(GlassfishNamingManagerImpl nm, Iterator names) {
        this.nm = nm;
        this.names = names;
    }

    public boolean hasMoreElements() {
        return names.hasNext();
    }

    public boolean hasMore() throws NamingException {
        return hasMoreElements();
    }

    public Object nextElement() {
        if (names.hasNext()) {
            try {
                String name = (String) names.next();
                return new Binding(name, nm.lookup(name));
            } catch (Exception ex) {
                throw new RuntimeException("Exception during lookup: " + ex);
            }
        } else
            return null;
    }

    public Object next() throws NamingException {
        return nextElement();
    }

    // New API for JNDI 1.2
    public void close() throws NamingException {
        throw new OperationNotSupportedException("close() not implemented");
    }
}
