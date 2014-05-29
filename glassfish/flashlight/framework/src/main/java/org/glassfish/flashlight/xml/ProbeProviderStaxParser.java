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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.glassfish.flashlight.xml;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLStreamException;
import static org.glassfish.flashlight.xml.XmlConstants.*;
import com.sun.logging.LogDomains;
import com.sun.enterprise.util.LocalStringManagerImpl;

/**
 * Read the XML file, parse it and return a list of ProbeProvider objects
 * @author bnevins
 */
public class ProbeProviderStaxParser extends StaxParser{

    private static final Logger logger =
        LogDomains.getLogger(ProbeProviderStaxParser.class, LogDomains.MONITORING_LOGGER);
    public final static LocalStringManagerImpl localStrings =
                            new LocalStringManagerImpl(ProbeProviderStaxParser.class);

    public ProbeProviderStaxParser(File f) throws XMLStreamException {
        super(f);
    }

    public ProbeProviderStaxParser(InputStream in) throws XMLStreamException {
        super(in);
    }

    public List<Provider> getProviders() {
        if(providers == null) {
            try {
                read();
            } 
            catch(Exception ex) {
                // normal 
                close();
            }
        }
        if (providers.isEmpty()) {
            // this line snatched from the previous implementation (DOM)
            logger.log(Level.SEVERE, "noProviderIdentifiedFromXML");
        }

        return providers;
    }

    @Override
    protected void read() throws XMLStreamException, EndDocumentException{
        providers = new ArrayList<Provider>();
        // move past the root -- "probe-providers".
        skipPast("probe-providers");

        while(true) {
            providers.add(parseProbeProvider());
        }
    }

    private Provider parseProbeProvider() throws XMLStreamException {
        if(!parser.getLocalName().equals(PROBE_PROVIDER)) {
            String errStr = localStrings.getLocalString("invalidStartElement",
                                "START_ELEMENT is supposed to be {0}" +
                                ", found: {1}", PROBE_PROVIDER, parser.getLocalName());
            throw new XMLStreamException(errStr);
        }
        
        Map<String,String> atts = parseAttributes();
        List<Probe> probes = parseProbes();

        return new Provider(atts.get(MODULE_PROVIDER_NAME),
                            atts.get(MODULE_NAME),
                            atts.get(PROBE_PROVIDER_NAME),
                            atts.get(PROBE_PROVIDER_CLASS),
                            probes );
    }

    private List<Probe> parseProbes() throws XMLStreamException {
        List<Probe> probes = new ArrayList<Probe>();

        boolean done = false;
        while(!done) {
            try {
                nextStart();

                if(parser.getLocalName().equals(PROBE))
                    probes.add(parseProbe());
                else
                    done = true;
            }
            catch (EndDocumentException ex) {
                // ignore -- this must be the last START_ELEMENT in the doc
                // that's normal
                done = true;
            }
        }
        return probes;
    }

    private Probe parseProbe() throws XMLStreamException {
        if(!parser.getLocalName().equals(PROBE)) {
            String errStr = localStrings.getLocalString("invalidStartElement",
                                "START_ELEMENT is supposed to be {0}" +
                                ", found: {1}", PROBE, parser.getLocalName());
            throw new XMLStreamException(errStr);
        }

        // for some unknown reason method is an element not an attribute
        // Solution -- use the last item if there are more than one

        List<ProbeParam> params = new ArrayList<ProbeParam>();
        Map<String,String> atts = parseAttributes();
        String method = null;
        String name = atts.get(PROBE_NAME);
        boolean self = Boolean.parseBoolean(atts.get(PROBE_SELF));
        boolean hidden = Boolean.parseBoolean(atts.get(PROBE_HIDDEN));


        boolean done = false;
        while(!done) {
            try {
                nextStart();
                String localName = parser.getLocalName();

                if(localName.equals(METHOD))
                    method = parser.getElementText();
                else if(localName.equals(PROBE_PARAM))
                    params.add(parseParam());
                else
                    done = true;
            }
            catch (EndDocumentException ex) {
                // ignore -- possibly normal -- but stop!
                done = true;
            }
        }
        return new Probe(name, method, params, self, hidden);
    }
    private ProbeParam parseParam() throws XMLStreamException {
        if(!parser.getLocalName().equals(PROBE_PARAM)){
            String errStr = localStrings.getLocalString("invalidStartElement",
                                "START_ELEMENT is supposed to be {0}" +
                                ", found: {1}", PROBE_PARAM, parser.getLocalName());
            throw new XMLStreamException(errStr);
        }

        Map<String,String> atts = parseAttributes();

        return new ProbeParam(atts.get(PROBE_PARAM_NAME), atts.get(PROBE_PARAM_TYPE));
    }

    private List<Provider> providers = null;
}

