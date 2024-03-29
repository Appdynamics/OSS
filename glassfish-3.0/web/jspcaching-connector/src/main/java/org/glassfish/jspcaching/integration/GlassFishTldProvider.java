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

package org.glassfish.jspcaching.integration;

import java.net.*;
import java.text.MessageFormat;
import java.util.*;
import java.util.logging.*;
import java.util.regex.Pattern;

import org.glassfish.api.web.TldProvider;
import org.glassfish.internal.api.ServerContext;
import org.jvnet.hk2.annotations.Scoped;
import org.jvnet.hk2.annotations.Service;
import org.jvnet.hk2.annotations.Inject;
import org.jvnet.hk2.component.PostConstruct;
import org.jvnet.hk2.component.Singleton;
import com.sun.enterprise.config.serverbeans.Config;
import com.sun.enterprise.config.serverbeans.WebContainer;
import com.sun.enterprise.util.net.JarURIPattern;
import com.sun.enterprise.module.Module;
import com.sun.enterprise.module.ModulesRegistry;
import com.sun.logging.LogDomains;
import com.sun.appserv.web.taglibs.cache.CacheTag;

/**
 * Implementation of TldProvider for JSP caching taglib.
 */

@Service(name="jspCachingTld")
@Scoped(Singleton.class)
public class GlassFishTldProvider
        implements TldProvider, PostConstruct {

    private static final Logger logger =
        LogDomains.getLogger(GlassFishTldProvider.class,
            LogDomains.WEB_LOGGER);

    private static final ResourceBundle rb = logger.getResourceBundle();

    @Inject
    ServerContext serverContext;

    @Inject
    ModulesRegistry registry;

    private Map<URI, List<String>> tldMap =
        new HashMap<URI, List<String>>();

    /**
     * Gets the name of this TldProvider
     */
    public String getName() {
        return "jspCachingTld";
    }

    /**
     * Gets a mapping from JAR files to their TLD resources.
     */
    public Map<URI, List<String>> getTldMap() {
        return (tldMap == null) ? null :
            (Map<URI, List<String>>)((HashMap)tldMap).clone();
    }

    /**
     * Gets a mapping from JAR files to their TLD resources
     * that are known to contain listener declarations
     */
    public Map<URI, List<String>> getTldListenerMap() {
        return getTldMap();
    }
 
    public void postConstruct() {
        /*
         * Check whether JSP caching has been enabled
         */        
        Config cfg = serverContext.getDefaultHabitat().getComponent(
            Config.class);
        WebContainer webContainer = cfg.getWebContainer();
        if (webContainer == null) {
            return;
        }
        if (!Boolean.valueOf(webContainer.getJspCachingEnabled())) {
            return;
        }

        /*
         * JSP caching has been enabled
         */
        Class jspCachingImplClass = CacheTag.class;
        URI[] uris = null;
        Module m = null;
        if (jspCachingImplClass != null) {
            m = registry.find(jspCachingImplClass);
        }
        if (m != null) {
            uris = m.getModuleDefinition().getLocations();
        } else {
            ClassLoader classLoader = getClass().getClassLoader();
            if (classLoader instanceof URLClassLoader) {
                URL[] urls = ((URLClassLoader)classLoader).getURLs();
                if (urls != null && urls.length > 0) {
                    uris = new URI[urls.length];
                    for (int i = 0; i < urls.length; i++) {
                        try {
                            uris[i] = urls[i].toURI();
                        } catch(URISyntaxException e) {
                            String msg = rb.getString("tldProvider.ignoreUrl");
                            msg = MessageFormat.format(msg, urls[i]);
                            logger.log(Level.WARNING, msg, e);
                        }
                    }
                }
            } else {
                logger.log(Level.WARNING,
                    "taglibs.unableToDetermineTldResources",
                    new Object[] {"JSP Caching", classLoader,
                        GlassFishTldProvider.class.getName()});
            }
        }

        if (uris != null && uris.length > 0) {
            Pattern pattern = Pattern.compile("META-INF/.*\\.tld");
            for (URI uri : uris) {
                List entries =  JarURIPattern.getJarEntries(uri, pattern);
                if (entries != null && entries.size() > 0) {
                    tldMap.put(uri, entries);
                }
            }
        }
    }
}
