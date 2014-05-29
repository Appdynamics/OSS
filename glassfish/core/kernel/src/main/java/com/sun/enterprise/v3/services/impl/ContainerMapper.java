/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2007-2012 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.enterprise.v3.services.impl;

import com.sun.appserv.server.util.Version;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.logging.Level;

import com.sun.enterprise.v3.server.HK2Dispatcher;
import com.sun.grizzly.ProtocolFilter;
import com.sun.grizzly.config.GrizzlyEmbeddedHttp;
import com.sun.grizzly.config.ContextRootInfo;
import com.sun.grizzly.config.FileCacheAware;
import com.sun.grizzly.config.dom.NetworkListener;
import com.sun.grizzly.tcp.Adapter;
import com.sun.grizzly.tcp.Request;
import com.sun.grizzly.tcp.Response;
import com.sun.grizzly.tcp.StaticResourcesAdapter;
import com.sun.grizzly.tcp.http11.GrizzlyAdapter;
import com.sun.grizzly.util.buf.ByteChunk;
import com.sun.grizzly.util.buf.CharChunk;
import com.sun.grizzly.util.buf.MessageBytes;
import com.sun.grizzly.util.buf.UDecoder;
import com.sun.grizzly.util.http.HttpRequestURIDecoder;
import com.sun.grizzly.util.http.mapper.Mapper;
import com.sun.grizzly.util.http.mapper.MappingData;
import com.sun.grizzly.util.http.MimeType;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.glassfish.api.container.Sniffer;
import org.glassfish.api.deployment.ApplicationContainer;
import org.glassfish.internal.grizzly.V3Mapper;
import org.jvnet.hk2.component.Habitat;

/**
 * Container's mapper which maps {@link ByteBuffer} bytes representation to an  {@link Adapter}, {@link
 * ApplicationContainer} and {@link ProtocolFilter} chain. The mapping result is stored inside {@link MappingData} which
 * is eventually shared with the {@link CoyoteAdapter}, which is the entry point with the Catalina Servlet Container.
 *
 * @author Jeanfrancois Arcand
 * @author Alexey Stashok
 */
@SuppressWarnings({"NonPrivateFieldAccessedInSynchronizedContext"})
public class ContainerMapper extends StaticResourcesAdapter  implements FileCacheAware {
    private final static String ROOT = "";
    private Mapper mapper;
    private GrizzlyEmbeddedHttp grizzlyEmbeddedHttp;
    private UDecoder urlDecoder;
    private String defaultHostName = "server";
    private final Habitat habitat;
    private final GrizzlyService grizzlyService;
    protected final static int MAPPING_DATA = 12;
    protected final static int MAPPED_ADAPTER = 13;

    private final ReentrantReadWriteLock mapperLock;
    
    // Make sure this value is always aligned with {@link org.apache.catalina.connector.CoyoteAdapter}
    // (@see org.apache.catalina.connector.CoyoteAdapter)
    private final static int MESSAGE_BYTES = 17;

    private final HK2Dispatcher hk2Dispatcher = new HK2Dispatcher();

    private String version;

    /**
     * Are we running multiple {@ Adapter} or {@link GrizzlyAdapter}
     */
    private boolean mapMultipleAdapter = false;

    public ContainerMapper(GrizzlyService service,
            /*GrizzlyEmbeddedHttp embeddedHttp,*/ NetworkListener networkListener) {
//        grizzlyEmbeddedHttp = embeddedHttp;
//        urlDecoder = embeddedHttp.getUrlDecoder();
        grizzlyService = service;
        mapperLock = service.obtainMapperLock(networkListener.getName());
        habitat = service.habitat;
        logger = GrizzlyEmbeddedHttp.logger();

        version = System.getProperty("product.name");
        if (version == null) {
            version = Version.getVersion();
        }
    }

    public void setEmbeddedHttp(GrizzlyEmbeddedHttp embeddedHttp) {
        this.grizzlyEmbeddedHttp = embeddedHttp;
        this.urlDecoder = embeddedHttp.getUrlDecoder();
    }

    
    /**
     * Set the default host that will be used when we map.
     *
     * @param defaultHost
     */
    protected void setDefaultHost(String defaultHost) {
        mapperLock.writeLock().lock();
        try {
            defaultHostName = defaultHost;
        } finally {
            mapperLock.writeLock().unlock();
        }
    }

    /**
     * Set the {@link V3Mapper} instance used for mapping the container and its associated {@link Adapter}.
     *
     * @param mapper
     */
    protected void setMapper(Mapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Configure the {@link V3Mapper}.
     */
    protected void configureMapper() {
        mapperLock.writeLock().lock();

        try {
            mapper.setDefaultHostName(defaultHostName);
            mapper.addHost(defaultHostName,new String[]{},null);
            mapper.addContext(defaultHostName,ROOT,
                    new ContextRootInfo(this,null),
                    new String[]{"index.html","index.htm"},null);
            // Container deployed have the right to override the default setting.
            Mapper.setAllowReplacement(true);
        } finally {
            mapperLock.writeLock().unlock();
        }
    }

    /**
     * Map the request to its associated {@link Adapter}.
     *
     * @param req
     * @param res
     *
     * @throws IOException
     */
    @Override
    public void service(Request req, Response res) throws Exception {
        long transactionTimeout = grizzlyEmbeddedHttp.getTransactionTimeout();
        if (transactionTimeout < 0) {
            transactionTimeout = Long.MAX_VALUE;
        }
        
//        grizzlyService.getServerReadyFuture().get(transactionTimeout, TimeUnit.MILLISECONDS);
        
        try {
            final Callable handler = lookupHandler(req, res);
            handler.call();
        } catch (Exception ex) {
            try {
                res.setStatus(500);
                if (logger.isLoggable(Level.WARNING)) {
                    logger.log(Level.WARNING, "Internal Server error: " + req.decodedURI(), ex);
                }
                customizedErrorPage(req, res);
            } catch (Exception ex2) {
                if (logger.isLoggable(Level.WARNING)) {
                    logger.log(Level.WARNING, "Unable to error page", ex2);
                }
            }
        } 
    }

    private Callable lookupHandler(final Request req, final Response res) throws Exception {
        MappingData mappingData;

        mapperLock.readLock().lock();
        
        try {
            // If we have only one Adapter deployed, invoke that Adapter
            // directly.
            // TODO: Not sure that will works with JRuby.
            if (!mapMultipleAdapter && mapper instanceof V3Mapper){
                // Remove the MappingData as we might delegate the request
                // to be serviced directly by the WebContainer
                req.setNote(MAPPING_DATA, null);
                Adapter a = ((V3Mapper)mapper).getAdapter();
                if (a != null) {
                    req.setNote(MAPPED_ADAPTER, a);
                    return new AdapterCallable(a, req, res);
//                    a.service(req, res);
//                    return;
                }
            }

            MessageBytes decodedURI = req.decodedURI();
            decodedURI.duplicate(req.requestURI());
            mappingData = (MappingData) req.getNote(MAPPING_DATA);
            if (mappingData == null) {
                mappingData = new MappingData();
                req.setNote(MAPPING_DATA, mappingData);
            } 
            Adapter adapter;
            
            String uriEncoding = (String) grizzlyEmbeddedHttp.getProperty("uriEncoding");
            HttpRequestURIDecoder.decode(decodedURI, urlDecoder, uriEncoding, null);

            final CharChunk decodedURICC = decodedURI.getCharChunk();
            final int semicolon = decodedURICC.indexOf(';', 0);

            // Map the request without any trailling.
            adapter = mapUriWithSemicolon(req, decodedURI, semicolon, mappingData);
            if (adapter == null || adapter instanceof ContainerMapper) {
                String ext = decodedURI.toString();
                String type = "";
                if (ext.indexOf(".") > 0) {
                    ext = "*" + ext.substring(ext.lastIndexOf("."));
                    type = ext.substring(ext.lastIndexOf(".") + 1);
                }

                if (!MimeType.contains(type) && !ext.equals("/")){
                    initializeFileURLPattern(ext);
                    mappingData.recycle();
                    adapter = mapUriWithSemicolon(req, decodedURI, semicolon, mappingData);
                } else {
//                    super.service(req, res);
//                    return;
                    return new SuperCallable(req, res);
                }
            }

            if (logger.isLoggable(Level.FINE)) {
                logger.log(Level.FINE, "Request: {0} was mapped to Adapter: {1}",
                        new Object[]{decodedURI.toString(), adapter});
            }

            // The Adapter used for servicing static pages doesn't decode the
            // request by default, hence do not pass the undecoded request.
            if (adapter == null || adapter instanceof ContainerMapper) {
//                super.service(req, res);
                return new SuperCallable(req, res);
            } else {
                req.setNote(MAPPED_ADAPTER, adapter);

                ContextRootInfo contextRootInfo = null;
                if (mappingData.context != null && mappingData.context instanceof ContextRootInfo) {
                    contextRootInfo = (ContextRootInfo) mappingData.context;
                }

                if (contextRootInfo == null) {
//                    adapter.service(req, res);
                    return new AdapterCallable(adapter, req, res);
                } else {
                    ClassLoader cl = null;
                    if (contextRootInfo.getContainer() instanceof ApplicationContainer){
                        cl = ((ApplicationContainer)contextRootInfo.getContainer()).getClassLoader();
                    }
//                    hk2Dispatcher.dispath(adapter, cl, req, res);
                    return new Hk2DispatcherCallable(adapter, cl, req, res);
                }
            }
        } finally {
            mapperLock.readLock().unlock();
        }         
    }
    
    private final static class AdapterCallable implements Callable {
        final Adapter adapter;
        final Request req;
        final Response res;

        public AdapterCallable(Adapter a, Request req, Response res) {
            this.adapter = a;
            this.req = req;
            this.res = res;
        }

        @Override
        public Object call() throws Exception {
            adapter.service(req, res);
            return null;
        }
    }

    private final class SuperCallable implements Callable {
        final Request req;
        final Response res;

        public SuperCallable(Request req, Response res) {
            this.req = req;
            this.res = res;
        }

        @Override
        public Object call() throws Exception {
            ContainerMapper.super.service(req, res);
            return null;
        }
    }
    
    private final class Hk2DispatcherCallable implements Callable {
        final Adapter adapter;
        final ClassLoader classloader;
        final Request req;
        final Response res;

        public Hk2DispatcherCallable(Adapter adapter, ClassLoader classloader,
                Request req, Response res) {
            this.adapter = adapter;
            this.classloader = classloader;
            this.req = req;
            this.res = res;
        }

        @Override
        public Object call() throws Exception {
            hk2Dispatcher.dispath(adapter, classloader, req, res);
            return null;
        }
    }

    private void initializeFileURLPattern(String ext) {
        for (Sniffer sniffer : grizzlyService.habitat.getAllByContract(Sniffer.class)) {
            boolean match = false;
            if (sniffer.getURLPatterns() != null) {

                for (String pattern : sniffer.getURLPatterns()) {
                    if (pattern.equalsIgnoreCase(ext)) {
                        match = true;
                        break;
                    }
                }

                Adapter adapter = this;
                if (match) {
                    adapter = grizzlyService.habitat.getComponent(SnifferAdapter.class);
                    ((SnifferAdapter) adapter).initialize(sniffer, this);
                    ContextRootInfo c = new ContextRootInfo(adapter, null);

                    mapperLock.readLock().unlock();
                    mapperLock.writeLock().lock();
                    try {
                        for (String pattern : sniffer.getURLPatterns()) {
                            for (String host : grizzlyService.hosts) {
                                mapper.addWrapper(host, ROOT, pattern, c,
                                        "*.jsp".equals(pattern) || "*.jspx".equals(pattern));
                            }
                        }
                    } finally {
                        mapperLock.writeLock().unlock();
                        mapperLock.readLock().lock();
                    }
                    
                    return;
                }
            }
        }
    }

    /**
     * Maps the decodedURI to the corresponding Adapter, considering that URI
     * may have a semicolon with extra data followed, which shouldn't be a part
     * of mapping process.
     *
     * @param req HTTP request
     * @param decodedURI URI
     * @param semicolonPos semicolon position. Might be <tt>0</tt> if position wasn't resolved yet (so it will be resolved in the method), or <tt>-1</tt> if there is no semicolon in the URI.
     * @param mappingData
     * @return
     * @throws Exception
     */
    final Adapter mapUriWithSemicolon(final Request req, final MessageBytes decodedURI,
            int semicolonPos, final MappingData mappingData) throws Exception {
        
        mapperLock.readLock().lock();
        
        try {
            final CharChunk charChunk = decodedURI.getCharChunk();
            final int oldEnd = charChunk.getEnd();

            if (semicolonPos == 0) {
                semicolonPos = decodedURI.indexOf(';');
            }

            MessageBytes localDecodedURI = decodedURI;
            if (semicolonPos >= 0) {
                charChunk.setEnd(semicolonPos);
                // duplicate the URI path, because Mapper may corrupt the attributes,
                // which follow the path
                localDecodedURI = (MessageBytes) req.getNote(MESSAGE_BYTES);
                if (localDecodedURI == null) {
                    localDecodedURI = MessageBytes.newInstance();
                    req.setNote(MESSAGE_BYTES, localDecodedURI);
                }
                localDecodedURI.duplicate(decodedURI);
            }


            try {
                return map(req, localDecodedURI, mappingData);
            } finally {
                charChunk.setEnd(oldEnd);
            }
        } finally {
            mapperLock.readLock().unlock();
        }
    }

    Adapter map(Request req, MessageBytes decodedURI, MappingData mappingData) throws Exception {
        if (mappingData == null) {
            mappingData = (MappingData) req.getNote(MAPPING_DATA);
        }
        // Map the request to its Adapter/Container and also it's Servlet if
        // the request is targetted to the CoyoteAdapter.
        mapper.map(req.serverName(), decodedURI, mappingData);
        ContextRootInfo contextRootInfo = null;
        if (mappingData.context != null && (mappingData.context instanceof ContextRootInfo 
                || mappingData.wrapper instanceof ContextRootInfo )) {
            if (mappingData.wrapper != null) {
                contextRootInfo = (ContextRootInfo) mappingData.wrapper;
            } else {
                contextRootInfo = (ContextRootInfo) mappingData.context;
            }
            return contextRootInfo.getAdapter();
        } else if (mappingData.context != null && mappingData.context.getClass()
            .getName().equals("com.sun.enterprise.web.WebModule")) {
            return ((V3Mapper) mapper).getAdapter();
        }
        return null;
    }

    /**
     * Recycle the mapped {@link Adapter} and this instance.
     *
     * @param req
     * @param res
     *
     * @throws Exception
     */
    @Override
    public void afterService(Request req, Response res) throws Exception {
        MappingData mappingData = (MappingData) req.getNote(MAPPING_DATA);
        try {
            Adapter adapter = (Adapter) req.getNote(MAPPED_ADAPTER);
            if (adapter != null) {
                adapter.afterService(req, res);
            }
            super.afterService(req, res);
        } finally {
            req.setNote(MAPPED_ADAPTER, null);
            if (mappingData != null){
                mappingData.recycle();
            }
        }
    }

    /**
     * Return an error page customized for GlassFish v3.
     *
     * @param req
     * @param res
     *
     * @throws Exception
     */
    @Override
    protected void customizedErrorPage(Request req, Response res) throws Exception {
        byte[] errorBody = null;
        if (res.getStatus() == 404){
            errorBody = HttpUtils.getErrorPage(Version.getVersion(),
                    "The requested resource () is not available.", "404");
        } else {
             errorBody = HttpUtils.getErrorPage(Version.getVersion(),
                     "Internal Error", "500");
        }

        ByteChunk chunk = new ByteChunk();
        chunk.setBytes(errorBody, 0, errorBody.length);
        res.setContentLength(errorBody.length);
        res.setContentType("text/html");
        if (!version.isEmpty()){
            res.addHeader("Server", version);
        }
        res.sendHeaders();
        res.doWrite(chunk);
    }

    public void register(String contextRoot, Collection<String> vs, Adapter adapter
            ,ApplicationContainer container) {

        if (logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE, "MAPPER({0}) REGISTER contextRoot: {1} adapter: {2} container: {3} port: {4}",
                    new Object[]{this, contextRoot, adapter, container, grizzlyEmbeddedHttp.getPort()});
        }
        /*
        * In the case of CoyoteAdapter, return, because the context will
        * have already been registered with the mapper by the connector's
        * MapperListener, in response to a JMX event
        */
        if (adapter.getClass().getName().equals("org.apache.catalina.connector.CoyoteAdapter")) {
            return;
        }

        mapMultipleAdapter = true;
        String ctx = getContextPath(contextRoot);
        String wrapper = getWrapperPath(ctx, contextRoot);
        ContextRootInfo c = new ContextRootInfo(adapter, container);
        for (String host : vs) {
            mapper.addContext(host, contextRoot,
                c, new String[0], null);
            if (adapter instanceof StaticResourcesAdapter){
                mapper.addWrapper(host,ctx,wrapper,c);
            }
        }
    }

    private String getWrapperPath(String ctx, String mapping) {
        if (mapping.indexOf("*.") > 0) {
            return mapping.substring(mapping.lastIndexOf("/") + 1);
        } else if (!ctx.equals("")) {
            return mapping.substring(ctx.length());
        } else {
            return mapping;
        }
    }

    private String getContextPath(String mapping) {
        String ctx = "";
        int slash = mapping.indexOf("/", 1);
        if (slash != -1) {
            ctx = mapping.substring(0, slash);
        } else {
            ctx = mapping;
        }

        if (ctx.startsWith("/*.") ||ctx.startsWith("*.") ) {
            if (ctx.indexOf("/") == ctx.lastIndexOf("/")){
                ctx = "";
            } else {
                ctx = ctx.substring(1);
            }
        }


        if (ctx.startsWith("/*") || ctx.startsWith("*")) {
            ctx = "";
        }

        // Special case for the root context
        if (ctx.equals("/")) {
            ctx = "";
        }

        return ctx;
    }

    public void unregister(String contextRoot) {
        if (logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE, "MAPPER ({0}) UNREGISTER contextRoot: {1}",
                    new Object[]{this, contextRoot});
        }
        for (String host : grizzlyService.hosts) {
            mapper.removeContext(host, contextRoot);
        }
    }

}
