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

/* SunOneBasicX509TrustManager.java
 * $Id: NoCertCheckX509TrustManager.java,v 1.4 2005/12/25 04:26:33 tcfujii Exp $
 * $Revision: 1.4 $
 * $Date: 2005/12/25 04:26:33 $
 * Indentation Information:
 * 0. Please (try to) preserve these settings.
 * 1. Tabs are preferred over spaces.
 * 2. In vi/vim -
 *		:set tabstop=4 :set shiftwidth=4 :set softtabstop=4
 * 3. In S1 Studio -
 *		1. Tools->Options->Editor Settings->Java Editor->Tab Size = 4
 *		2. Tools->Options->Indentation Engines->Java Indentation Engine->Expand Tabs to Spaces = False.
 *		3. Tools->Options->Indentation Engines->Java Indentation Engine->Number of Spaces per Tab = 4.
 */

package com.sun.enterprise.admin.jmx.remote.https;

/**
 * An implementation of {@link X509TrustManager} that provides basic support for Trust Management.
 * This implementation does not prompt for confirmation of the server certificate, but 
 * blindly accepts it and enters it into the .asadmintruststore.
 */
public class NoCertCheckX509TrustManager extends SunOneBasicX509TrustManager {
		     
	/**
     * Creates an instance of the NoCertCheckX509TrustManager
     * @param alias The toString() of the alias object concatenated with a date/time stamp is used as 
     * the alias of the trusted server certificate in the client side .asadmintruststore. When null
     * only a date / timestamp is used as an alias.
     */   
	public NoCertCheckX509TrustManager(Object alias) {		
        super(alias, null);
	}
	
    /**
     * Creates an instance of the NoCertCheckX509TrustManager
     * A date/time stamp is used of the trusted server certificate in the client side 
     *.asadmintruststore
     */
    public NoCertCheckX509TrustManager() {
        super();
    }
    
    /**
     *
     * @return true if the cert should be displayed and the user asked to confirm it. A 
     * return valie of false indicates that the cert will be implicitly trusted and 
     * added to the asadmin truststore.
     */    
    protected boolean promptForConfirmation()
    {
        return false;
    }        
}
