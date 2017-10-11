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
package com.sun.enterprise.tools.verifier.tests.ejb.session;

import com.sun.enterprise.tools.verifier.tests.ejb.EjbTest;
import com.sun.enterprise.deployment.*;
import com.sun.enterprise.tools.verifier.*;
import com.sun.enterprise.tools.verifier.tests.ejb.EjbCheck;
import com.sun.enterprise.tools.verifier.tests.*;


/** 
 * Session Bean's bean-managed transaction demarcation test.  
 *
 *  The enterprise bean with bean-managed transaction demarcation must 
 *  be a Session bean.
 */
public class TransactionTypeBeanManaged extends EjbTest implements EjbCheck { 


    /** 
     * Session Bean's bean-managed transaction demarcation test.  
     *
     *  The enterprise bean with bean-managed transaction demarcation must 
     *  be a Session bean.
     *     
     * @param descriptor the Enterprise Java Bean deployment descriptor
     *   
     * @return <code>Result</code> the results for this assertion
     */
    public Result check(EjbDescriptor descriptor) {

	Result result = getInitializedResult();
	ComponentNameConstructor compName = getVerifierContext().getComponentNameConstructor();

	String transactionType = descriptor.getTransactionType();
	if (EjbSessionDescriptor.BEAN_TRANSACTION_TYPE.equals(transactionType)) {
	    if (descriptor instanceof EjbSessionDescriptor) {
		result.addGoodDetails(smh.getLocalString
				      ("tests.componentNameConstructor",
				       "For [ {0} ]",
				       new Object[] {compName.toString()}));
		result.passed(smh.getLocalString
			      (getClass().getName() + ".passed",
			       "[ {0} ] is valid transactionType within Session bean [ {1} ]",
			       new Object[] {transactionType, descriptor.getName()}));
	    } else if (descriptor instanceof EjbEntityDescriptor) {
		result.addErrorDetails(smh.getLocalString
				       ("tests.componentNameConstructor",
					"For [ {0} ]",
					new Object[] {compName.toString()}));
		result.failed(smh.getLocalString
			      (getClass().getName() + ".failedException",
			       "Error: [ {0} ] is not valid transactionType within entity bean [ {1} ]",
			       new Object[] {transactionType, descriptor.getName()}));
	    } 
	    return result;
	} else if (EjbSessionDescriptor.CONTAINER_TRANSACTION_TYPE.equals(transactionType)) {
	    result.addNaDetails(smh.getLocalString
				("tests.componentNameConstructor",
				 "For [ {0} ]",
				 new Object[] {compName.toString()}));
	    result.notApplicable(smh.getLocalString
				 (getClass().getName() + ".notApplicable",
				  "Expected [ {0} ] transaction demarcation, but [ {1} ] bean has [ {2} ] transaction demarcation.",
				  new Object[] {EjbSessionDescriptor.BEAN_TRANSACTION_TYPE,descriptor.getName(),EjbSessionDescriptor.CONTAINER_TRANSACTION_TYPE}));
	    return result;
	} else {
	    result.addNaDetails(smh.getLocalString
				("tests.componentNameConstructor",
				 "For [ {0} ]",
				 new Object[] {compName.toString()}));
	    result.notApplicable(smh.getLocalString
				 (getClass().getName() + ".notApplicable",
				  "Expected [ {0} ] transaction demarcation, but [ {1} ] bean has [ {2} ] transaction demarcation.",
				  new Object[] {EjbSessionDescriptor.BEAN_TRANSACTION_TYPE,descriptor.getName(),transactionType}));
	    return result;
	} 
    }
}