/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012 Oracle and/or its affiliates. All rights reserved.
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
package org.glassfish.admin.mbeanserver;

import com.sun.logging.LogDomains;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.security.AccessControlException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.*;
import javax.management.remote.MBeanServerForwarder;
import org.glassfish.internal.api.AdminAccessController;

/**
 * Allows per-access security checks on MBean attribute set/get and other
 * invoked operations.
 * <p>
 * This class wraps the normal GlassFish MBeanServer with a security checker.
 * If control reaches this class then the incoming connection has already
 * authenticated successfully.  This class decides, depending on exactly what 
 * the request wants to do and what MBean is involved, whether to allow
 * the current request or not.  If so, it delegates to the real MBeanServer; if
 * not it throws an exception.
 * <p>
 * Currently we allow all access to non-AMX MBeans.  This permits, for example,
 * the normal operations to view JVM performance characteristics.  If the
 * attempted access concerns an AMX MBean and we're running in the DAS then
 * we allow it - it's OK to adjust configuration via JMX to the DAS.  But if
 * this is a non-DAS instance we make sure the operation on the AMX MBean is 
 * read-only before  allowing it.
 * 
 * @author tjquinn
 */
public class AdminAuthorizedMBeanServer {
    
    private static final Logger mLogger = LogDomains.getLogger(AdminAuthorizedMBeanServer.class, LogDomains.JMX_LOGGER);

    private static final Set<String> RESTRICTED_METHOD_NAMES = new HashSet<String>(Arrays.asList(
            "setAttribute",
            "setAttributes"
        ));
    
    
    private static class Handler implements InvocationHandler {
        
        private final MBeanServer mBeanServer;
        private final boolean isInstance;
        private final BootAMX bootAMX;
        
        private Handler(final MBeanServer mbs, final boolean isInstance, final BootAMX bootAMX) {
            this.mBeanServer = mbs;
            this.isInstance = isInstance;
            this.bootAMX = bootAMX;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (isAllowed(method, args)) {
                return method.invoke(mBeanServer, args);
            } else {
                final String format = mLogger.getResourceBundle().getString("jmx.noaccess");
                final String objNameString = objectNameString(args);
                final String operationImpact = impactToString(operationImpact(args));
                final String msg = MessageFormat.format(format, operationName(args),
                        objNameString, AdminAccessController.Access.READONLY, operationImpact);
                mLogger.log(Level.FINE, 
                        "Disallowing access to {0} operation {1} because the impact is declared as {2}", 
                        new Object[]{
                            objNameString, 
                            operationName(args), 
                            operationImpact}
                        );
                throw new AccessControlException(msg);
            }
        }
        
        private String operationName(final Object[] args) {
            return ((objectNameString(args) == null) || (args.length < 2) || (args[1] == null) ? "null" : (String) args[1]);
        }
        
        private String objectNameString(Object[] args) {
            return (args == null || args.length == 0 || ( ! (args[0] instanceof ObjectName))) ? null : ((ObjectName) args[0]).toString();
        }
        
        private boolean isAllowed(
                final Method method,
                final Object[] args) throws InstanceNotFoundException, IntrospectionException, ReflectionException, NoSuchMethodException {
            /*
             * Allow access if this is the DAS (not an instance) or if the
             * request does not affect an AMX MBean or if the request is
             * read-only.
             */
            return ( ! isInstance) 
                    || isAMX(args)
                    || isReadonlyRequest(method, args);
        }
        
        private boolean isAMX(final Object[] args) {
            return (args == null) 
                    || (args[0] == null)
                    || ( ! (args[0] instanceof ObjectName))
                    || ( ! isAMX((ObjectName) args[0]));
        }
        
        private boolean isAMX(final ObjectName objectName) {
            final String amxDomain = amxDomain();
            return (objectName == null || amxDomain == null) ? false : amxDomain.equals(objectName.getDomain());
        }
        
        private String amxDomain() {
            return bootAMX.bootAMX().getDomain();
        }

        private boolean isReadonlyRequest(final Method method, final Object[] args) throws InstanceNotFoundException, IntrospectionException, ReflectionException, NoSuchMethodException {
            if (RESTRICTED_METHOD_NAMES.contains(method.getName())) {
                return false;
            }
            
            return ( ! method.getName().equals("invoke"))
                    || (operationImpact(args) == MBeanOperationInfo.INFO);
        }
        
        private int operationImpact(final Object[] args) throws InstanceNotFoundException, IntrospectionException, ReflectionException, NoSuchMethodException {
            final ObjectName objectName = (ObjectName) args[0];
            final String operationName = (String) args[1];
            final String[] signature = (String[]) args[3];
            final MBeanInfo info = mBeanServer.getMBeanInfo(objectName);
            if (info != null) {

                /*
                * Find the matching operation.  
                */
                for (MBeanOperationInfo opInfo : info.getOperations()) {
                    if (opInfo.getName().equals(operationName) && isSignatureEqual(opInfo.getSignature(), signature)) {
                        return opInfo.getImpact();
                    }
                }
                /*
                * No matching operation.
                */
                throw new NoSuchMethodException(operationName);
            }
            return MBeanOperationInfo.UNKNOWN;
        }
        
        private static String impactToString(final int impact) {
            String result;
            switch (impact) {
                case MBeanOperationInfo.ACTION: result = "action"; break;
                case MBeanOperationInfo.ACTION_INFO : result = "action_info"; break;
                case MBeanOperationInfo.INFO : result = "info" ; break;
                case MBeanOperationInfo.UNKNOWN : result = "unknown"; break;
                default: result = "?";
            }
            return result;
                
        }
        
        private boolean isSignatureEqual(final MBeanParameterInfo[] declaredMBeanParams, final String[] calledSig) {
            if (declaredMBeanParams.length != calledSig.length) {
                return false;
            }
            
            for (int i = 0; i < declaredMBeanParams.length; i++) {
                if (! declaredMBeanParams[i].getType().equals(calledSig[i])) {
                    return false;
                }
            }
            return true;
        }
    }
        
    private static AdminAccessController.Access getAccess() {
        /*
         * Temp workaround.  Still working on this.
         */
        
        return AdminAccessController.Access.FULL;
        
//        AdminAccessController.Access result = JMXAccessInfo.getAccess();
//        if (result == null) {
//            result = AdminAccessController.Access.READONLY;
//        }
//        return result;
    }
    
    /**
     * Returns an MBeanServer that will check security and then forward requests
     * to the real MBeanServer.
     * 
     * @param mbs the real MBeanServer to which to delegate
     * @return the security-checking wrapper around the MBeanServer
     */
    public static MBeanServerForwarder newInstance(final MBeanServer mbs, final boolean isInstance,
            final BootAMX bootAMX) {
        final Handler handler = new Handler(mbs, isInstance, bootAMX);
        
        return (MBeanServerForwarder) Proxy.newProxyInstance(
                MBeanServerForwarder.class.getClassLoader(),
                new Class[] {MBeanServerForwarder.class},
                handler);
    }
    
}
