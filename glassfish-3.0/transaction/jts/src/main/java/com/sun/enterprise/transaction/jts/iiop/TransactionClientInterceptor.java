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

import org.omg.CORBA.LocalObject;
import org.omg.PortableInterceptor.ClientRequestInfo;
import org.omg.PortableInterceptor.ClientRequestInterceptor;
import com.sun.corba.ee.spi.presentation.rmi.StubAdapter;

import com.sun.enterprise.transaction.api.JavaEETransactionManager;

import org.jvnet.hk2.component.Habitat;

public class TransactionClientInterceptor extends LocalObject
        implements ClientRequestInterceptor, Comparable {

    private String name;
    private int order;

    private JavaEETransactionManager tm;

    /**
     * Create the interceptor.
     * @param the name of the interceptor.
     * @param the order in which the interceptor should be invoked.
     */
    public TransactionClientInterceptor(String name, int order, Habitat habitat) {
	this.name = name;
	this.order = order;
        tm = habitat.getComponent(JavaEETransactionManager.class);
    }

    public int compareTo(Object o) {
	int otherOrder = -1;
	if(o instanceof TransactionClientInterceptor) {
	    otherOrder = ((TransactionClientInterceptor)o).order;
	}
	if (order < otherOrder) {
	    return -1;
	} else if (order == otherOrder) {
	    return 0;
	}
	return 1;
    }

    /**
     * Return the name of the interceptor.
     * @return the name of the interceptor.
     */
    public String name() { 
	return name; 
    }


    public void send_request(ClientRequestInfo cri) {
	// Check if there is an exportable transaction on current thread
	Object target = cri.effective_target();
	if ( tm != null )
	    tm.checkTransactionExport(StubAdapter.isLocal(target));

    }

    public void destroy() {
    }

    public void send_poll(ClientRequestInfo cri) {
    }

    public void receive_reply(ClientRequestInfo cri) {
    }

    public void receive_exception(ClientRequestInfo cri) {
    }

    public void receive_other(ClientRequestInfo cri) {
    }
}

