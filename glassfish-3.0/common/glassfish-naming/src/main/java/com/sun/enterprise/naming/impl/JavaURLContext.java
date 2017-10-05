/*
 * The contents of this file are subject to the terms 
 * of the Common Development and Distribution License 
 * (the License).  You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the license at 
 * https://glassfish.dev.java.net/public/CDDLv1.0.html or
 * glassfish/bootstrap/legal/CDDLv1.0.txt.
 * See the License for the specific language governing 
 * permissions and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL 
 * Header Notice in each file and include the License file 
 * at glassfish/bootstrap/legal/CDDLv1.0.txt.  
 * If applicable, add the following below the CDDL Header, 
 * with the fields enclosed by brackets [] replaced by
 * you own identifying information: 
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * Copyright 2006 Sun Microsystems, Inc. All rights reserved.
 */
package com.sun.enterprise.naming.impl;

import com.sun.enterprise.naming.util.LogFacade;
import com.sun.enterprise.naming.impl.GlassfishNamingManagerImpl;

import javax.naming.*;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.glassfish.internal.api.Globals;
import org.glassfish.api.naming.ComponentNamingUtil;
import org.jvnet.hk2.component.Habitat;
import org.glassfish.api.admin.ProcessEnvironment;
import org.glassfish.api.admin.ProcessEnvironment.ProcessType;

/**
 * This class is a context implementation for the java:comp namespace.
 * The context determines the component id from the invocation manager
 * of the component that is invoking the method and then looks up the
 * object in that component's local namespace.
 */

public final class JavaURLContext implements Context, Cloneable {

    static Logger _logger = LogFacade.getLogger();

    private static final boolean debug = false;

    private static GlassfishNamingManagerImpl namingManager;

    private Hashtable myEnv;
    //private Context ctx; XXX not needed ?
    private String myName = "";

    private SerialContext serialContext = null;

    /**
     * Create a context with the specified environment.
     */
    public JavaURLContext(Hashtable environment) throws NamingException {
        myEnv = (environment != null) ? (Hashtable) (environment.clone())
                : null;
    }

    /**
     * Create a context with the specified name+environment.
     * Called only from GlassfishNamingManager.
     */
    public JavaURLContext(String name, Hashtable env)
            throws NamingException {
        this(env);
        this.myName = name;
    }

    /**
     * this constructor is called from SerialContext class
     */
    public JavaURLContext(Hashtable env, SerialContext serialContext)
            throws NamingException {
        this(env);
        this.serialContext = serialContext;
    }

    static void setNamingManager(GlassfishNamingManagerImpl mgr) {
        namingManager = mgr;
    }

    /**
     * add SerialContext to preserve stickiness to cloned instance
     * why clone() : to avoid the case of multiple threads modifying
     * the context returned for ctx.lookup("java:com/env/ejb")
     */
    /**  STICKY context not enabled
    public JavaURLContext addStickyContext(SerialContext serialContext)
            throws NamingException {
        try {
            JavaURLContext jCtx = (JavaURLContext) this.clone();
            jCtx.serialContext = serialContext;
            return jCtx;
        } catch (java.lang.CloneNotSupportedException ex) {
            NamingException ne = new NamingException(
                    "problem with cloning javaURLContext instance");
            ne.initCause(ex);
            throw ne;
        }
    }
    **/

    /**
     * Lookup an object in the serial context.
     *
     * @return the object that is being looked up.
     * @throws NamingException if there is a naming exception.
     */
    public Object lookup(String name) throws NamingException {
        if (_logger.isLoggable(Level.FINE))
            _logger.log(Level.FINE, "In javaURLContext.lookup, name = " + name
                    + " serialcontext..." + serialContext);

        if (name.equals("")) {
            /**
             * javadocs for Context.lookup: If name is empty, returns a new
             * instance of this context (which represents the same naming
             * context as this context, but its environment may be modified
             * independently and it may be accessed concurrently).
             */
            return new JavaURLContext(myName, myEnv);
        }

        String fullName = name;
        if (!myName.equals("")) {
            if (myName.equals("java:"))
                fullName = myName + name;
            else
                fullName = myName + "/" + name;
        }

        try {
            Object obj = null;
            // If we know for sure it's an entry within an environment namespace
            if (fullName.startsWith("java:comp/env/") ||
                fullName.startsWith("java:module/env/") ||
                fullName.startsWith("java:app/env/") ) {
                // refers to a dependency defined by the application
                obj = namingManager.lookup(fullName, serialContext);
            } else {
                // It's either an application-defined dependency in a java:
                // namespace or a special EE platform object.
                // Check for EE platform objects first to prevent overriding.  
                obj = NamedNamingObjectManager.tryNamedProxies(name);
                if (obj == null) {
                    // try GlassfishNamingManager
                    obj = namingManager.lookup(fullName, serialContext);
                }
            }

            if( obj == null ) {
                throw new NamingException("No object found for " + name);
            }

            return obj;
        } catch (NamingException ex) {

            Habitat habitat = Globals.getDefaultHabitat();
            ProcessEnvironment processEnv = habitat.getComponent(ProcessEnvironment.class);
            if( fullName.startsWith("java:app/") &&
                processEnv.getProcessType() == ProcessType.ACC ) {

                // This could either be an attempt by an app client to access a portable
                // remote session bean JNDI name via the java:app namespace or a lookup of
                // an application-defined java:app environment dependency.  Try them in
                // that order.

                Context ic = namingManager.getInitialContext();
                String appName = (String) namingManager.getInitialContext().lookup("java:app/AppName");

                Object obj = null;

                if( !fullName.startsWith("java:app/env/")) {
                    try {

                        // Translate the java:app name into the equivalent java:global name so that
                        // the lookup will be resolved by the server.
                        String newPrefix = "java:global/" + appName + "/";

                        int javaAppLength = "java:app/".length();
                        String globalLookup = newPrefix + fullName.substring(javaAppLength);

                        obj = ic.lookup(globalLookup);

                    } catch(NamingException javaappenvne) {
                        _logger.log(Level.FINE, "Trying global version of java:app ejb lookup",
                                javaappenvne);
                    }
                }

                if( obj == null ) {
                   ComponentNamingUtil compNamingUtil = habitat.getByContract(ComponentNamingUtil.class);
                   String internalGlobalJavaAppName =
                    compNamingUtil.composeInternalGlobalJavaAppName(appName, fullName);

                    obj = ic.lookup(internalGlobalJavaAppName);
                }

                if( obj == null ) {
                    throw new NamingException("No object found for " + name);
                }

                return obj;
               
            }

            throw ex;
        } catch (Exception ex) {
            throw (NamingException) (new NameNotFoundException(
                    "No object bound for " + fullName)).initCause(ex);
        }
    }

    /**
     * Lookup a name in either the cosnaming or serial context.
     *
     * @return the object that is being looked up.
     * @throws NamingException if there is a naming exception.
     */
    public Object lookup(Name name) throws NamingException {
        // Flat namespace; no federation; just call string version
        return lookup(name.toString());
    }

    /**
     * Bind an object in the namespace. Binds the reference to the
     * actual object in either the cosnaming or serial context.
     *
     * @throws NamingException if there is a naming exception.
     */
    public void bind(String name, Object obj) throws NamingException {
        throw new NamingException("java:comp namespace cannot be modified");
    }

    /**
     * Bind an object in the namespace. Binds the reference to the
     * actual object in either the cosnaming or serial context.
     *
     * @throws NamingException if there is a naming exception.
     */
    public void bind(Name name, Object obj) throws NamingException {
        throw new NamingException("java:comp namespace cannot be modified");
    }

    /**
     * Rebind an object in the namespace. Rebinds the reference to the
     * actual object in either the cosnaming or serial context.
     *
     * @throws NamingException if there is a naming exception.
     */
    public void rebind(String name, Object obj) throws NamingException {
        throw new NamingException("java:comp namespace cannot be modified");
    }

    /**
     * Rebind an object in the namespace. Rebinds the reference to the
     * actual object in either the cosnaming or serial context.
     *
     * @throws NamingException if there is a naming exception.
     */
    public void rebind(Name name, Object obj) throws NamingException {
        throw new NamingException("java:comp namespace cannot be modified");
    }

    /**
     * Unbind an object from the namespace.
     *
     * @throws NamingException if there is a naming exception.
     */
    public void unbind(String name) throws NamingException {
        throw new NamingException("java:comp namespace cannot be modified");
    }

    /**
     * Unbind an object from the namespace.
     *
     * @throws NamingException if there is a naming exception.
     */
    public void unbind(Name name) throws NamingException {
        throw new NamingException("java:comp namespace cannot be modified");
    }

    /**
     * The rename operation is not supported by this context. It throws
     * an OperationNotSupportedException.
     */
    public void rename(String oldname, String newname) throws NamingException {
        throw new NamingException("java:comp namespace cannot be modified");
    }

    /**
     * The rename operation is not supported by this context. It throws
     * an OperationNotSupportedException.
     */
    public void rename(Name oldname, Name newname)
            throws NamingException {
        throw new NamingException("java:comp namespace cannot be modified");
    }

    /**
     * The destroySubcontext operation is not supported by this context.
     * It throws an OperationNotSupportedException.
     */
    public void destroySubcontext(String name) throws NamingException {
        throw new NamingException("java:comp namespace cannot be modified");
    }

    /**
     * The destroySubcontext operation is not supported by this context.
     * It throws an OperationNotSupportedException.
     */
    public void destroySubcontext(Name name) throws NamingException {
        throw new NamingException("java:comp namespace cannot be modified");
    }

    public Context createSubcontext(String name) throws NamingException {
        throw new NamingException("java:comp namespace cannot be modified");
    }

    public Context createSubcontext(Name name) throws NamingException {
        throw new NamingException("java:comp namespace cannot be modified");
    }


    /**
     * Lists the contents of a context or subcontext. The operation is
     * delegated to the serial context.
     *
     * @return an enumeration of the contents of the context.
     * @throws NamingException if there is a naming exception.
     */
    public NamingEnumeration list(String name)
            throws NamingException {
        if (name.equals("")) {
            // listing this context
            if (namingManager == null)
                throw new NamingException();
            return namingManager.list(myName);
        }

        // Check if 'name' names a context
        Object target = lookup(name);
        if (target instanceof Context) {
            return ((Context) target).list("");
        }
        throw new NotContextException(name + " cannot be listed");
    }

    /**
     * Lists the contents of a context or subcontext. The operation is
     * delegated to the serial context.
     *
     * @return an enumeration of the contents of the context.
     * @throws NamingException if there is a naming exception.
     */
    public NamingEnumeration list(Name name)
            throws NamingException {
        // Flat namespace; no federation; just call string version
        return list(name.toString());
    }

    /**
     * Lists the bindings of a context or subcontext. The operation is
     * delegated to the serial context.
     *
     * @return an enumeration of the bindings of the context.
     * @throws NamingException if there is a naming exception.
     */
    public NamingEnumeration listBindings(String name)
            throws NamingException {
        if (name.equals("")) {
            // listing this context
            if (namingManager == null)
                throw new NamingException();
            return namingManager.listBindings(myName);
        }

        // Perhaps 'name' names a context
        Object target = lookup(name);
        if (target instanceof Context) {
            return ((Context) target).listBindings("");
        }
        throw new NotContextException(name + " cannot be listed");
    }

    /**
     * Lists the bindings of a context or subcontext. The operation is
     * delegated to the serial context.
     *
     * @return an enumeration of the bindings of the context.
     * @throws NamingException if there is a naming exception.
     */
    public NamingEnumeration listBindings(Name name)
            throws NamingException {
        // Flat namespace; no federation; just call string version
        return listBindings(name.toString());
    }

    /**
     * This context does not treat links specially. A lookup operation is
     * performed.
     */
    public Object lookupLink(String name) throws NamingException {
        // This flat context does not treat links specially
        return lookup(name);
    }

    /**
     * This context does not treat links specially. A lookup operation is
     * performed.
     */
    public Object lookupLink(Name name) throws NamingException {
        // Flat namespace; no federation; just call string version
        return lookupLink(name.toString());
    }

    /**
     * Return the name parser for the specified name.
     *
     * @return the NameParser instance.
     * @throws NamingException if there is an exception.
     */
    public NameParser getNameParser(String name)
            throws NamingException {
        if (namingManager == null)
            throw new NamingException();
        return namingManager.getNameParser();
    }

    /**
     * Return the name parser for the specified name.
     *
     * @return the NameParser instance.
     * @throws NamingException if there is an exception.
     */
    public NameParser getNameParser(Name name) throws NamingException {
        // Flat namespace; no federation; just call string version
        return getNameParser(name.toString());
    }

    public String composeName(String name, String prefix)
            throws NamingException {
        Name result = composeName(new CompositeName(name),
                new CompositeName(prefix));
        return result.toString();
    }

    public Name composeName(Name name, Name prefix)
            throws NamingException {
        Name result = (Name) (prefix.clone());
        result.addAll(name);
        return result;
    }

    /**
     * Add a property to the environment.
     */
    public Object addToEnvironment(String propName, Object propVal)
            throws NamingException {
        if (myEnv == null) {
            myEnv = new Hashtable(5, 0.75f);
        }
        return myEnv.put(propName, propVal);
    }

    /**
     * Remove a property from the environment.
     */
    public Object removeFromEnvironment(String propName)
            throws NamingException {
        if (myEnv == null) {
            return null;
        }
        return myEnv.remove(propName);
    }

    /**
     * Get the context's environment.
     */
    public Hashtable getEnvironment() throws NamingException {
        if (myEnv == null) {
            // Must return non-null
            myEnv = new Hashtable(3, 0.75f);
        }
        return myEnv;
    }

    /**
     * New JNDI 1.2 operation.
     */
    public void close() throws NamingException {
        myEnv = null;
    }

    /**
     * Return the name of this context within the namespace.  The name
     * can be passed as an argument to (new InitialContext()).lookup()
     * to reproduce this context.
     */
    public String getNameInNamespace() throws NamingException {
        return myName;
    }
}


