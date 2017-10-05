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

package com.sun.enterprise.transaction.jts.iiop;

import java.util.Properties;
import java.util.logging.Logger;
import java.util.logging.Level;

import com.sun.jts.pi.InterceptorImpl;
import com.sun.jts.jta.TransactionServiceProperties;
import com.sun.jts.CosTransactions.Configuration;

import org.glassfish.enterprise.iiop.api.IIOPInterceptorFactory;
import org.glassfish.api.admin.ProcessEnvironment;
import com.sun.enterprise.util.i18n.StringManager;
import com.sun.logging.LogDomains;

import org.jvnet.hk2.annotations.Inject;
import org.jvnet.hk2.annotations.Service;
import org.jvnet.hk2.component.Habitat;

import org.omg.CORBA.*;
import org.omg.IOP.Codec;
import org.omg.PortableInterceptor.Current;
import org.omg.PortableInterceptor.ClientRequestInterceptor;
import org.omg.PortableInterceptor.ORBInitInfo;
import org.omg.PortableInterceptor.ServerRequestInterceptor;

import com.sun.corba.ee.spi.costransactions.TransactionService;
import com.sun.corba.ee.spi.orbutil.ORBConstants;
import com.sun.corba.ee.spi.orbutil.closure.ClosureFactory;
import com.sun.corba.ee.spi.legacy.interceptor.ORBInitInfoExt;
import com.sun.corba.ee.impl.logging.POASystemException;
import com.sun.corba.ee.impl.txpoa.TSIdentificationImpl;

/**
 *
 * @author mvatkina
 */
@Service(name="TransactionIIOPInterceptorFactory")
public class TransactionIIOPInterceptorFactory implements IIOPInterceptorFactory{

    // The log message bundle is in com.sun.jts package
    private static Logger _logger =
            LogDomains.getLogger(InterceptorImpl.class, LogDomains.TRANSACTION_LOGGER);

    private static StringManager localStrings =
            StringManager.getManager(InterceptorImpl.class);

    private static String jtsClassName = "com.sun.jts.CosTransactions.DefaultTransactionService";

    private static Properties jtsProperties = new Properties();
    private static TSIdentificationImpl tsIdent = new TSIdentificationImpl();
    private static boolean txServiceInitialized = false;
    private InterceptorImpl interceptor = null;

    @Inject private Habitat habitat;
    @Inject private ProcessEnvironment processEnv;

    public ClientRequestInterceptor createClientRequestInterceptor(ORBInitInfo info, Codec codec) {
        if (!txServiceInitialized) {
            createInterceptor(info, codec);
        }

        return interceptor;
    }

    public ServerRequestInterceptor createServerRequestInterceptor(ORBInitInfo info, Codec codec) {
        if (!txServiceInitialized) {
            createInterceptor(info, codec);
        }

        return interceptor;
    }

    private void createInterceptor(ORBInitInfo info, Codec codec) {
        if( processEnv.getProcessType().isServer()) {
            try {
                System.setProperty(
                        InterceptorImpl.CLIENT_POLICY_CHECKING, String.valueOf(false));
            } catch ( Exception ex ) {
                _logger.log(Level.WARNING,"iiop.readproperty_exception",ex);
            }

            initJTSProperties(true);
        }

        try {
            // register JTS interceptors
            // first get hold of PICurrent to allocate a slot for JTS service.
            Current pic = (Current)info.resolve_initial_references("PICurrent");

            // allocate a PICurrent slotId for the transaction service.
            int[] slotIds = new int[2];
            slotIds[0] = info.allocate_slot_id();
            slotIds[1] = info.allocate_slot_id();

            interceptor = new InterceptorImpl(pic, codec, slotIds, null);
            // Get the ORB instance on which this interceptor is being
            // initialized
            com.sun.corba.ee.spi.orb.ORB theORB = ((ORBInitInfoExt)info).getORB();

            // Set ORB and TSIdentification: needed for app clients,
            // standalone clients.
            interceptor.setOrb(theORB);
            try {
                Class theJTSClass = Class.forName(jtsClassName);

                if (theJTSClass != null) {
                        try {
                        TransactionService jts = (TransactionService)theJTSClass.newInstance();
                        jts.identify_ORB(theORB, tsIdent, jtsProperties ) ;
                        interceptor.setTSIdentification(tsIdent);

                        // V2-XXX should jts.get_current() be called everytime
                        // resolve_initial_references is called ??
                        org.omg.CosTransactions.Current transactionCurrent =
                                jts.get_current();

                        theORB.getLocalResolver().register(
                                ORBConstants.TRANSACTION_CURRENT_NAME,
                                ClosureFactory.makeConstant(transactionCurrent));

                        // the JTS PI use this to call the proprietary hooks
                        theORB.getLocalResolver().register(
                                "TSIdentification", ClosureFactory.makeConstant(tsIdent));
                        txServiceInitialized = true;
                    } catch (Exception ex) {
                        throw new INITIALIZE("JTS Exception: " + ex,
                                POASystemException.JTS_INIT_ERROR,
                                CompletionStatus.COMPLETED_MAYBE);
                    }
                }
            } catch (ClassNotFoundException cnfe) {
                _logger.log(Level.SEVERE,"iiop.inittransactionservice_exception",cnfe);
            }


            // Add IOR Interceptor only for OTS tagged components
            TxIORInterceptor iorInterceptor = new TxIORInterceptor(codec, habitat);
            info.add_ior_interceptor(iorInterceptor);

        } catch (Exception e) {
            if(_logger.isLoggable(Level.FINE)){
                _logger.log(Level.FINE,"Exception registering JTS interceptors",e);
            }
            throw new RuntimeException(e.getMessage());
        }
    }

    private void initJTSProperties(boolean lateRegistration) {
        if (habitat != null) {
            jtsProperties = TransactionServiceProperties.getJTSProperties(habitat, true);
            if (_logger.isLoggable(Level.FINE)) {
                _logger.log(Level.FINE,
                            "++++ Server id: "
                            + jtsProperties.getProperty(ORBConstants.ORB_SERVER_ID_PROPERTY));
            }
            Configuration.setProperties(jtsProperties);
        }
    }

    private boolean isValueSet(String value) {
        return (value != null && !value.equals("") && !value.equals(" "));
    }
}
