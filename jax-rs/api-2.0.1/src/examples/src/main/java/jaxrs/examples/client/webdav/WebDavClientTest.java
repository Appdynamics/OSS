/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2011-2013 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * http://glassfish.java.net/public/CDDL+GPL_1_1.html
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
package jaxrs.examples.client.webdav;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

/**
 * Test of WebDAV Client API Extension.
 *
 * @author Marek Potociar
 */
public class WebDavClientTest {

    private WebDavClient createClient() {
        return new WebDavClient();
    }

    static class TestFeature implements Feature {

        @Override
        public boolean configure(FeatureContext context) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    public void fluentUseCases() {
        WebDavClient client = createClient();

        TestFeature testFeature = new TestFeature();
        client.register(testFeature);

        client.target("http://examples.jaxrs.com/webdav/");
        client.target("http://examples.jaxrs.com/webdav/").register(testFeature);
        client.target("http://examples.jaxrs.com/webdav/").request().property("foo", "bar");
        client.target("http://examples.jaxrs.com/webdav/").request().buildGet().property("foo", "bar");

        // HTTP
        client.target("http://examples.jaxrs.com/webdav/").request().get();
        client.target("http://examples.jaxrs.com/webdav/").request().async().get();
        client.target("http://examples.jaxrs.com/webdav/").request().buildGet().invoke();
        client.target("http://examples.jaxrs.com/webdav/").request().buildGet().submit();
        // WebDAV
        client.target("http://examples.jaxrs.com/webdav/").request().search(null);
        client.target("http://examples.jaxrs.com/webdav/").request().async().search(null);
        client.target("http://examples.jaxrs.com/webdav/").request().buildSearch(null).invoke();
        client.target("http://examples.jaxrs.com/webdav/").request().buildSearch(null).submit();

        // HTTP
        client.target("http://examples.jaxrs.com/webdav/").path("123").request().get();
        client.target("http://examples.jaxrs.com/webdav/").path("123").request().async().get();
        client.target("http://examples.jaxrs.com/webdav/").path("123").request().buildGet().invoke();
        client.target("http://examples.jaxrs.com/webdav/").path("123").request().buildGet().submit();
        // WebDAV
        client.target("http://examples.jaxrs.com/webdav/").path("123").request().search(null);
        client.target("http://examples.jaxrs.com/webdav/").path("123").request().async().search(null);
        client.target("http://examples.jaxrs.com/webdav/").path("123").request().buildSearch(null).invoke();
        client.target("http://examples.jaxrs.com/webdav/").path("123").request().buildSearch(null).submit();

        // HTTP
        client.target("http://examples.jaxrs.com/webdav/").path("123").request("text/plain").get();
        client.target("http://examples.jaxrs.com/webdav/").path("123").request("text/plain").async().get();
        client.target("http://examples.jaxrs.com/webdav/").path("123").request("text/plain").buildGet().invoke();
        client.target("http://examples.jaxrs.com/webdav/").path("123").request("text/plain").buildGet().submit();
        // WebDAV
        client.target("http://examples.jaxrs.com/webdav/").path("123").request("text/plain").search(null);
        client.target("http://examples.jaxrs.com/webdav/").path("123").request("text/plain").async().search(null);
        client.target("http://examples.jaxrs.com/webdav/").path("123").request("text/plain").buildSearch(null).invoke();
        client.target("http://examples.jaxrs.com/webdav/").path("123").request("text/plain").buildSearch(null).submit();

        // HTTP
        client.target("http://examples.jaxrs.com/webdav/").path("123").request("text/plain").header("custom-name", "custom_value").get();
        client.target("http://examples.jaxrs.com/webdav/").path("123").request("text/plain").header("custom-name", "custom_value").async().get();
        client.target("http://examples.jaxrs.com/webdav/").path("123").request("text/plain").header("custom-name", "custom_value").buildGet().invoke();
        client.target("http://examples.jaxrs.com/webdav/").path("123").request("text/plain").header("custom-name", "custom_value").buildGet().submit();
        // WebDAV
        client.target("http://examples.jaxrs.com/webdav/").path("123").request("text/plain").header("custom-name", "custom_value").search(null);
        client.target("http://examples.jaxrs.com/webdav/").path("123").request("text/plain").header("custom-name", "custom_value").async().search(null);
        client.target("http://examples.jaxrs.com/webdav/").path("123").request("text/plain").header("custom-name", "custom_value").buildSearch(null).invoke();
        client.target("http://examples.jaxrs.com/webdav/").path("123").request("text/plain").header("custom-name", "custom_value").buildSearch(null).submit();
    }
}
