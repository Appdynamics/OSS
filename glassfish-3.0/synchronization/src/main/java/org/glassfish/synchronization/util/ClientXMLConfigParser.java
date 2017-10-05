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
package org.glassfish.synchronization.util;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.glassfish.synchronization.central.MainServerConfig;
import org.glassfish.synchronization.client.ClientConfig;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author Behrooz Khorashadi
 *
 */
public class ClientXMLConfigParser {
	public static final String config_str = "clientconfig";
	public static final String configs_str = "clientconfigs";
	public static List<ClientConfig>  parseClientXML(File f) 
										throws ParserConfigurationException, 
											   SAXException, IOException {
		List<ClientConfig> ret = new LinkedList<ClientConfig>();
		DocumentBuilder builder = 
			DocumentBuilderFactory.newInstance().newDocumentBuilder(); 
		Document doc = builder.parse(f);
		NodeList configs = doc.getElementsByTagName("clientconfig");
		int length = configs.getLength();
		for (int i = 0; i < length; i++) {
			ret.add(new ClientConfig((Element)configs.item(i)));
		}
		return ret;
	}
	public static void main(String[] args) {
		File f = new File(args[0]);
		try {
			parseClientXML(f);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static MainServerConfig parseServerXML(File f) 
				throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilder builder = 
			DocumentBuilderFactory.newInstance().newDocumentBuilder(); 
		Document doc = builder.parse(f);
		NodeList configs = doc.getElementsByTagName("server");
		return new MainServerConfig((Element)configs.item(0));
	}
}