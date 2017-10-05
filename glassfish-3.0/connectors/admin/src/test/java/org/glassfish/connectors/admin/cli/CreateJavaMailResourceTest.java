/*
 *
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2009 Sun Microsystems, Inc. All rights reserved.
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

package org.glassfish.connectors.admin.cli;

import com.sun.enterprise.config.serverbeans.*;
import com.sun.enterprise.util.SystemPropertyConstants;
import com.sun.enterprise.v3.common.PropsFileActionReporter;
import com.sun.logging.LogDomains;
import org.glassfish.api.ActionReport;
import org.glassfish.api.admin.AdminCommandContext;
import org.glassfish.api.admin.CommandRunner;
import org.glassfish.api.admin.ParameterMap;
import org.glassfish.server.ServerEnvironmentImpl;
import org.glassfish.tests.utils.ConfigApiTest;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.jvnet.hk2.component.Habitat;
import org.jvnet.hk2.config.ConfigSupport;
import org.jvnet.hk2.config.DomDocument;
import org.jvnet.hk2.config.SingleConfigCode;
import org.jvnet.hk2.config.TransactionFailure;
import java.beans.PropertyVetoException;


public class CreateJavaMailResourceTest extends ConfigApiTest {

    private Habitat habitat;
    private Resources resources;
    private ParameterMap parameters;
    private AdminCommandContext context;
    private CommandRunner cr;

    public DomDocument getDocument(Habitat habitat) {
        return new TestDocument(habitat);
    }

    public String getFileName() {
        return "DomainTest";
    }

    @Before
    public void setUp() {
        habitat = getHabitat();
        resources = habitat.getComponent(Resources.class);
        assertTrue(resources != null);
        parameters = new ParameterMap();
        context = new AdminCommandContext(
                LogDomains.getLogger(ServerEnvironmentImpl.class, LogDomains.ADMIN_LOGGER),
                new PropsFileActionReporter());
        cr = habitat.getComponent(CommandRunner.class);
        assertTrue(cr != null);
    }

    @After
    public void tearDown() throws TransactionFailure {
        ConfigSupport.apply(new SingleConfigCode<Resources>() {
            public Object run(Resources param) throws PropertyVetoException, TransactionFailure {
                Resource target = null;
                for (Resource resource : param.getResources()) {
                    if (resource instanceof MailResource) {
                        MailResource r = (MailResource) resource;
                        if (r.getJndiName().equals("mail/MyMailSession") ||
                                r.getJndiName().equals("dupRes")) {
                            target = resource;
                            break;
                        }
                    }
                }
                if (target != null) {
                    param.getResources().remove(target);
                }
                return null;
            }
        }, resources);
    }

    /**
     * Test of execute method, of class CreateJavaMailResource.
     * asadmin create-javamail-resource --mailuser=test --mailhost=localhost
     * --fromaddress=test@sun.com mail/MyMailSession
     */
    @Test
    public void testExecuteSuccess() {
        parameters.set("mailhost", "localhost");
        parameters.set("mailuser", "test");
        parameters.set("fromaddress", "test@sun.com");
        parameters.set("jndi_name", "mail/MyMailSession");
        CreateJavaMailResource command = habitat.getComponent(CreateJavaMailResource.class);
        assertTrue(command != null);
        cr.getCommandInvocation("create-javamail-resource", context.getActionReport()).parameters(parameters).execute(command);
        assertEquals(ActionReport.ExitCode.SUCCESS, context.getActionReport().getActionExitCode());
        boolean isCreated = false;
        for (Resource resource : resources.getResources()) {
            if (resource instanceof MailResource) {
                MailResource r = (MailResource) resource;
                if (r.getJndiName().equals("mail/MyMailSession")) {
                    assertEquals("localhost", r.getHost());
                    assertEquals("test", r.getUser());
                    assertEquals("test@sun.com", r.getFrom());
                    assertEquals("true", r.getEnabled());
                    assertEquals("false", r.getDebug());
                    assertEquals("imap", r.getStoreProtocol());
                    assertEquals("com.sun.mail.imap.IMAPStore", r.getStoreProtocolClass());
                    assertEquals("smtp", r.getTransportProtocol());
                    assertEquals("com.sun.mail.smtp.SMTPTransport", r.getTransportProtocolClass());
                    isCreated = true;
                    logger.fine("MailResource config bean mail/MyMailSession is created.");
                    break;
                }
            }
        }
        assertTrue(isCreated);
        logger.fine("msg: " + context.getActionReport().getMessage());
        Servers servers = habitat.getComponent(Servers.class);
        boolean isRefCreated = false;
        for (Server server : servers.getServer()) {
            if (server.getName().equals(SystemPropertyConstants.DEFAULT_SERVER_INSTANCE_NAME)) {
                for (ResourceRef ref : server.getResourceRef()) {
                    if (ref.getRef().equals("mail/MyMailSession")) {
                        assertEquals("true", ref.getEnabled());
                        isRefCreated = true;
                        break;
                    }
                }
            }
        }
        assertTrue(isRefCreated);
    }

    /**
     * Test of execute method, of class CreateJavaMailResource.
     * asadmin create-javamail-resource --mailuser=test --mailhost=localhost
     * --fromaddress=test@sun.com dupRes
     * asadmin create-javamail-resource --mailuser=test --mailhost=localhost
     * --fromaddress=test@sun.com dupRes
     */
    @Test
    public void testExecuteFailDuplicateResource() {
        parameters.set("mailhost", "localhost");
        parameters.set("mailuser", "test");
        parameters.set("fromaddress", "test@sun.com");
        parameters.set("jndi_name", "dupRes");
        CreateJavaMailResource command1 = habitat.getComponent(CreateJavaMailResource.class);
        assertTrue(command1 != null);
        cr.getCommandInvocation("create-javamail-resource", context.getActionReport()).parameters(parameters).execute(command1);
        assertEquals(ActionReport.ExitCode.SUCCESS, context.getActionReport().getActionExitCode());
        boolean isCreated = false;
        for (Resource resource : resources.getResources()) {
            if (resource instanceof MailResource) {
                MailResource jr = (MailResource) resource;
                if (jr.getJndiName().equals("dupRes")) {
                    isCreated = true;
                    logger.fine("MailResource config bean dupRes is created.");
                    break;
                }
            }
        }
        assertTrue(isCreated);

        CreateJavaMailResource command2 = habitat.getComponent(CreateJavaMailResource.class);
        cr.getCommandInvocation("create-javamail-resource", context.getActionReport()).parameters(parameters).execute(command2);
        assertEquals(ActionReport.ExitCode.FAILURE, context.getActionReport().getActionExitCode());
        int numDupRes = 0;
        for (Resource resource : resources.getResources()) {
            if (resource instanceof MailResource) {
                MailResource jr = (MailResource) resource;
                if (jr.getJndiName().equals("dupRes")) {
                    numDupRes = numDupRes + 1;
                }
            }
        }
        assertEquals(1, numDupRes);
        logger.fine("msg: " + context.getActionReport().getMessage());
    }

    /**
     * Test of execute method, of class CreateJavaMailResource when enabled has no value
     * asadmin create-javamail-resource --mailuser=test --mailhost=localhost
     * --fromaddress=test@sun.com  --enabled=false --debug=true
     * --storeprotocol=pop
     * --storeprotocolclass=com.sun.mail.pop.POPStore
     * --transprotocol=lmtp
     * --transprotocolclass=com.sun.mail.lmtop.LMTPTransport
     * mail/MyMailSession
     */
    @Test
    public void testExecuteWithOptionalValuesSet() {
        parameters.set("mailhost", "localhost");
        parameters.set("mailuser", "test");
        parameters.set("fromaddress", "test@sun.com");
        parameters.set("enabled", "false");
        parameters.set("debug", "true");
        parameters.set("storeprotocol", "pop");
        parameters.set("storeprotocolclass", "com.sun.mail.pop.POPStore");
        parameters.set("transprotocol", "lmtp");
        parameters.set("transprotocolclass", "com.sun.mail.lmtp.LMTPTransport");
        parameters.set("jndi_name", "mail/MyMailSession");
        CreateJavaMailResource command = habitat.getComponent(CreateJavaMailResource.class);
        assertTrue(command != null);
        cr.getCommandInvocation("create-javamail-resource", context.getActionReport()).parameters(parameters).execute(command);
        assertEquals(ActionReport.ExitCode.SUCCESS, context.getActionReport().getActionExitCode());
        boolean isCreated = false;
        for (Resource resource : resources.getResources()) {
            if (resource instanceof MailResource) {
                MailResource r = (MailResource) resource;
                if (r.getJndiName().equals("mail/MyMailSession")) {
                    assertEquals("false", r.getEnabled());
                    assertEquals("true", r.getDebug());
                    assertEquals("pop", r.getStoreProtocol());
                    assertEquals("com.sun.mail.pop.POPStore", r.getStoreProtocolClass());
                    assertEquals("lmtp", r.getTransportProtocol());
                    assertEquals("com.sun.mail.lmtp.LMTPTransport", r.getTransportProtocolClass());
                    isCreated = true;
                    break;
                }
            }
        }
        assertTrue(isCreated);
        logger.fine("msg: " + context.getActionReport().getMessage());
    }
}
