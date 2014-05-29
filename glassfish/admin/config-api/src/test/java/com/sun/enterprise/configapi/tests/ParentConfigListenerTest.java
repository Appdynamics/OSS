/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2009-2010 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.enterprise.configapi.tests;

import com.sun.grizzly.config.dom.NetworkListener;
import com.sun.grizzly.config.dom.NetworkListeners;
import com.sun.hk2.component.ConstructorWomb;
import org.glassfish.tests.utils.Utils;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.jvnet.hk2.component.Habitat;
import org.jvnet.hk2.config.ConfigSupport;
import org.jvnet.hk2.config.ObservableBean;
import org.jvnet.hk2.config.SingleConfigCode;
import org.jvnet.hk2.config.TransactionFailure;
import org.jvnet.hk2.config.Transactions;

import java.util.Collection;

/**
 * This test will ensure that when a class is injected with a parent bean and a child
 * is added to the parent, anyone injected will that parent will be notified
 * correctly.
 *
 * User: Jerome Dochez
 */
public class ParentConfigListenerTest extends ConfigApiTest {

    Habitat habitat;

    public String getFileName() {
        return "DomainTest";
    }

    @Before
    public void setup() {
        habitat = Utils.getNewHabitat(this);
    }


    @Test
    public void addHttpListenerTest() throws TransactionFailure {


        ConstructorWomb<NetworkListenersContainer> womb = new ConstructorWomb<NetworkListenersContainer>(
            NetworkListenersContainer.class, habitat, null);
        NetworkListenersContainer container = womb.get(null);

        ConfigSupport.apply(new SingleConfigCode<NetworkListeners>() {

            public Object run(NetworkListeners param) throws TransactionFailure {
                NetworkListener newListener = param.createChild(NetworkListener.class);
                newListener.setName("Funky-Listener");
                newListener.setPort("8078");
                param.getNetworkListener().add(newListener);
                return null;
            }
        }, container.httpService);

        getHabitat().getComponent(Transactions.class).waitForDrain();
        assertTrue(container.received);
        ObservableBean bean = (ObservableBean) ConfigSupport.getImpl(container.httpService);

        // let's check that my newly added listener is available in the habitat.
        Collection<NetworkListener> networkListeners = habitat.getAllByContract(NetworkListener.class);
        boolean found = false;
        for (NetworkListener nl : networkListeners) {
            System.out.println("Network listener " + nl.getName());
            if (nl.getName().equals("Funky-Listener")) {
                found=true;
            }
        }
        Assert.assertTrue("Newly added listener not found", found);
        // direct access.
        NetworkListener nl = habitat.getComponent(NetworkListener.class, "Funky-Listener");
        Assert.assertTrue("Direct access to newly added listener failed", nl!=null);
        bean.removeListener(container);
    }
}
