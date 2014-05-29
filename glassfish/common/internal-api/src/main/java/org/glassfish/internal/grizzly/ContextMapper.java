/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2007-2010 Oracle and/or its affiliates. All rights reserved.
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

package org.glassfish.internal.grizzly;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.grizzly.tcp.Adapter;
import com.sun.grizzly.util.http.mapper.Mapper;
import org.jvnet.hk2.annotations.ContractProvided;
import org.jvnet.hk2.annotations.Service;

/**
 * Extended that {@link Mapper} that prevent the WebContainer to unregister the current {@link Mapper} configuration.
 *
 * @author Jeanfrancois Arcand
 */
@Service
@ContractProvided(Mapper.class)
public class ContextMapper extends Mapper {
    protected final Logger logger;
    protected Adapter adapter;
    // The id of the associated network-listener
    protected String id;

    public ContextMapper() {
        this(Logger.getAnonymousLogger());
    }

    public ContextMapper(final Logger logger) {
        this.logger = logger;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addWrapper(final String hostName, final String contextPath, final String path,
        final Object wrapper, final boolean jspWildCard, final String servletName,
        final boolean isEmptyPathSpecial) {
        super.addWrapper(hostName, contextPath, path, wrapper, jspWildCard,
                servletName, isEmptyPathSpecial);
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("Wrapper-Host: " + hostName + " contextPath " + contextPath
                + " wrapper " + wrapper + " path " + path + " jspWildcard " + jspWildCard +
                " servletName " + servletName + " isEmptyPathSpecial " + isEmptyPathSpecial);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void addHost(final String name, final String[] aliases,
        final Object host) {

        super.addHost(name, aliases, host);
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("Host-Host: " + name + " aliases " + Arrays.toString(aliases) + " host " + host);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addContext(final String hostName, final String path, final Object context,
        final String[] welcomeResources, final javax.naming.Context resources) {
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("Context-Host: " + hostName + " path " + path + " context " + context +
                " port " + getPort());
        }
        // The WebContainer is registering new Context. In that case, we must
        // clean all the previously added information, specially the
        // MappingData.wrapper info as this information cannot apply
        // to this Container.
        if (adapter != null && "org.apache.catalina.connector.CoyoteAdapter".equals(adapter.getClass().getName())) {
            removeContext(hostName, path);
        }
        super.addContext(hostName, path, context, welcomeResources, resources);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void removeHost(final String name) {
        // Do let the WebContainer deconfigire us.
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("Faking removal of host: " + name);
        }
    }

    public void setAdapter(final Adapter adapter) {
        this.adapter = adapter;
    }

    public Adapter getAdapter() {
        return adapter;
    }

    /**
     * Sets the id of the associated http-listener on this mapper.
     */
    public void setId(final String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
