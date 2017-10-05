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

package com.sun.enterprise.admin.servermgmt.pe;

import com.sun.enterprise.admin.servermgmt.InstanceException;
import com.sun.enterprise.admin.servermgmt.InstancesManager;
//import com.sun.enterprise.admin.servermgmt.launch.LaunchConstants;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import com.sun.enterprise.util.io.FileUtils;
import com.sun.enterprise.util.i18n.StringManager;

import com.sun.enterprise.admin.servermgmt.DomainException;
import com.sun.enterprise.admin.servermgmt.DomainConfig;
import com.sun.enterprise.admin.servermgmt.RepositoryConfig;
import com.sun.enterprise.admin.servermgmt.RepositoryException;
import com.sun.enterprise.admin.servermgmt.DomainsManager;
import com.sun.enterprise.admin.servermgmt.RepositoryNameValidator;
import com.sun.enterprise.admin.servermgmt.DomainXmlEventListener;
import com.sun.enterprise.admin.servermgmt.RepositoryManager;
import com.sun.enterprise.admin.servermgmt.util.DomainXmlSAXParser;

//import com.sun.enterprise.admin.common.Status;
import com.sun.enterprise.admin.util.TokenValue;

import com.sun.enterprise.admin.util.TokenValueSet;
//import com.sun.enterprise.config.serverbeans.ServerValidationHandler;
import org.xml.sax.helpers.DefaultHandler;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.BitSet;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import javax.xml.transform.OutputKeys;
import org.xml.sax.EntityResolver;

public class PEDomainsManager extends RepositoryManager 
    implements DomainsManager
{
    /**
     * i18n strings manager object
     */
    private static final StringManager strMgr = 
        StringManager.getManager(PEDomainsManager.class);
    private static final String NAME_DELIMITER = ",";
    
    /* These properties are public interfaces, handle with care */
    public static final String PROFILEPROPERTY_DOMAINXML_STYLESHEETS = "domain.xml.style-sheets";
    public static final String PROFILEPROPERTY_DOMAINXML_TOKENVALUES = "domain.xml.token-values";
    /* These properties are public interfaces, handle with care */
    
    public PEDomainsManager()
    {
        super();
    }
    
    //PE does not require that an admin user / password is available at start-domain time.
    //SE/SEE does require it.
    public BitSet getDomainFlags()
    {
        BitSet bs = new BitSet();        
        bs.set(DomainConfig.K_FLAG_START_DOMAIN_NEEDS_ADMIN_USER, false);
        return bs;
    }
    
    public void validateDomain(DomainConfig domainConfig, boolean domainExists)
        throws DomainException
    {
        try {
            checkRepository(domainConfig, domainExists, domainExists);
        } catch (RepositoryException ex) {
            throw new DomainException(ex);
        }
    }

    /*
    public void validateAdminUserAndPassword(DomainConfig domainConfig) 
        throws DomainException
    {
        try {
            validateAdminUserAndPassword(domainConfig, getDomainUser(domainConfig),
                getDomainPasswordClear(domainConfig));
        } catch (RepositoryException ex) {
            throw new DomainException(ex);
        }
    }
    */
    public void validateMasterPassword(DomainConfig domainConfig) 
        throws DomainException
    {
        try {
            validateMasterPassword(domainConfig, getMasterPasswordClear(domainConfig));
        } catch (RepositoryException ex) {
            throw new DomainException(ex);
        }
    }
    
    public void createDomain(DomainConfig domainConfig) 
        throws DomainException
    {
        PEFileLayout layout = getFileLayout(domainConfig);
        
        try {
            new RepositoryNameValidator(strMgr.getString("domainsRoot")).
                    checkValidXmlToken(
                        layout.getRepositoryRootDir().getAbsolutePath());
            layout.createRepositoryRoot();        
            new PEDomainConfigValidator().validate(domainConfig);
            checkRepository(domainConfig, false);
        } catch (Exception ex) {
            throw new DomainException(ex);
        }
        
        try {            
            String masterPassword = getMasterPasswordClear(domainConfig);
            layout.createRepositoryDirectories();
            createDomainXml(domainConfig);
            createDomainXmlEvents(domainConfig);
            //createScripts(domainConfig);
            createServerPolicyFile(domainConfig);
            createAdminKeyFile(domainConfig, getDomainUser(domainConfig), 
                getDomainPasswordClear(domainConfig));
            createKeyFile(domainConfig, getDomainUser(domainConfig),
                getDomainPasswordClear(domainConfig));
            createAppClientContainerXml(domainConfig);
            createIndexFile(domainConfig);
            createDefaultWebXml(domainConfig);
            createLoginConf(domainConfig);
            createWssServerConfig(domainConfig);
            createWssServerConfigOld(domainConfig);
            createSSLCertificateDatabase(domainConfig, masterPassword);                                     
            changeMasterPasswordInMasterPasswordFile(domainConfig, masterPassword, 
                saveMasterPassword(domainConfig));
            createPasswordAliasKeystore(domainConfig, masterPassword);
            createLoggingProperties(domainConfig);
            //createTimerWal(domainConfig);
            //createTimerDbn(domainConfig);
            //createMQInstance(domainConfig);
            //createJBIInstance(getDefaultInstance(), domainConfig);
            //if (layout.getDerbyEjbTimerSqlFile().exists()) //will be cleaned up once derby integrates, 05/19/2005
                //handleDerby(domainConfig);
            setPermissions(domainConfig);
        } catch (DomainException de) {
            //rollback
            FileUtils.liquidate(getDomainDir(domainConfig));
            throw de;
        } catch (Exception ex) {
            //rollback
            FileUtils.liquidate(getDomainDir(domainConfig));
            throw new DomainException(ex);
        }
    }
    /**
     */
    protected void createJBIInstance(String instanceName, 
                   DomainConfig domainConfig) throws DomainException
    {        
        try {
            getFileLayout(domainConfig).createJBIDomainDirectories();
            super.createJBIInstance(instanceName, domainConfig);

        } catch (Exception ex) {
            throw new DomainException(ex);
        }
    }
    /**
     * Sets the permissions for the domain directory, its config directory,
     * startserv/stopserv scripts etc.
     */
    protected void setPermissions(DomainConfig domainConfig) throws DomainException
    {        
        final PEFileLayout layout = getFileLayout(domainConfig);
        try {
            //4958533
            chmod("-R u+x ", layout.getBinDir());
            chmod("-R g-rwx,o-rwx ", layout.getConfigRoot());
            //4958533
        } catch (Exception e) {
            throw new DomainException(
                strMgr.getString("setPermissionError"), e);
        }   
    }

    public void deleteDomain(DomainConfig domainConfig) 
        throws DomainException
    {               
        try {
            deleteRepository(domainConfig);
        } catch (Exception e) {
            throw new DomainException(e);
        }
    }
    /*
    public void startDomain(DomainConfig domainConfig) 
        throws DomainException
    {                        
        try {
            checkRepository(domainConfig);            
            String[] options = getInteractiveOptions(
                (String)domainConfig.get(DomainConfig.K_USER), 
                (String)domainConfig.get(DomainConfig.K_PASSWORD),
                (String)domainConfig.get(DomainConfig.K_MASTER_PASSWORD),
                (HashMap)domainConfig.get(DomainConfig.K_EXTRA_PASSWORDS));            
            getInstancesManager(domainConfig).startInstance(options, (String[])null, getEnvProps(domainConfig));            
        } catch (Exception e) {
            throw new DomainException(e);
        }
    }

    public void stopDomain(DomainConfig domainConfig) 
        throws DomainException
    {                        
        try {
            checkRepository(domainConfig);            
            getInstancesManager(domainConfig).stopInstance();
        } catch (Exception e) {
            throw new DomainException(e);
        }
    }

    public String[] listDomainsAndStatus(DomainConfig domainConfig)
        throws DomainException
    {
        try {
            return listDomainsAndStatusAsString(domainConfig);
        } catch (Exception e) {
            throw new DomainException(e);
        }        
    }
    */
    /**
     * Lists all the domains.
     */
    public String[] listDomains(DomainConfig domainConfig)
        throws DomainException
    {        
        try {
            return listRepository(domainConfig); 
        } catch (Exception e) {
            throw new DomainException(e);
        }        
    }


    protected void createDomainXmlEvents(DomainConfig domainConfig) 
        throws DomainException {
            try {
                final PEFileLayout layout = getFileLayout(domainConfig);
                final File domainXml = layout.getDomainConfigFile();
                DomainXmlSAXParser parser = new DomainXmlSAXParser();
                try {
                    parser.parse(domainXml,layout.getDtdFile());
                }
                catch(Exception e) {
                    throw new DomainException(
                        strMgr.getString("domainXmlNotParsed"), e);
                }
                String className = parser.getDomainXmlEventListenerClass();
                if(className!=null) {
                    DomainXmlEventListener listener = (DomainXmlEventListener) Class.forName(className).newInstance();
                    listener.handleCreateEvent(domainConfig);
                }
            }
            catch(Exception e) {
                throw new DomainException(
                    strMgr.getString("domainXmlEventsNotCreated"), e);
            }
    }

    /**
     * The EEDomains manager needs to have an augmented set of tokens
     */
    protected TokenValueSet getDomainXmlTokens(DomainConfig domainConfig) {
        return PEDomainXmlTokens.getTokenValueSet(domainConfig);
    }
    
    protected void createDomainXml(DomainConfig domainConfig) 
        throws DomainException
    {
        try
        {
            final PEFileLayout layout = getFileLayout(domainConfig);
            final File dx = layout.getDomainConfigFile();
            final File dxt, tr;
            TokenValueSet tokens = getDomainXmlTokens(domainConfig);
            String tn = (String)domainConfig.get(DomainConfig.K_TEMPLATE_NAME);
            if((tn == null)||(tn.equals(""))) {
                //use profiles in this case
                dxt = layout.getDomainXmlTemplate();
                
                /* Comment out for V3, till decision is taken on profiles*/
                /*
                final String p = domainConfig.getProfile();
                final File pf = layout.getProfileFolder(p);
                System.out.println("pf = " + pf.getAbsolutePath() + " " + pf.getName() );
                if (!pf.exists()) {
                    final String key = "profileNotFound";
                    final String path = pf.getAbsolutePath();
                    final String name = domainConfig.getRepositoryName();
                    final String msg = strMgr.getString(key, p, path, name);
                    throw new IllegalArgumentException(msg);
                }
                if (new File(pf, layout.DOMAIN_XML_FILE).exists()) {
                // takes care of the default profiles that we bundle.
                    tr = new File(pf, layout.DOMAIN_XML_FILE);
                }
                else { //now transform for generic xsl
                    tr = invokeGenericXmlTemplateProcessing(dxt, domainConfig, 
                        PROFILEPROPERTY_DOMAINXML_STYLESHEETS, layout.getConfigRoot());
                    addTokenValuesIfAny(tokens, layout, p, this.PROFILEPROPERTY_DOMAINXML_TOKENVALUES);
                }*/
                tr = new File(layout.getTemplatesDir(), layout.DOMAIN_XML_FILE);
                generateFromTemplate(tokens, tr, dx);
            }
            else {
                dxt = layout.getDomainXmlTemplate(tn);
                generateFromTemplate(tokens, dxt, dx);
            }
        }
        catch(Exception e)
        {
            throw new DomainException(
                strMgr.getString("domainXmlNotCreated"), e);
        }
    }

    protected void createScripts(DomainConfig domainConfig)
        throws DomainException
    {
        final TokenValueSet tokens = PEScriptsTokens.getTokenValueSet(domainConfig);
        createStartServ(domainConfig, tokens);
        createStopServ(domainConfig, tokens);
    }

    void createStartServ(DomainConfig domainConfig, 
        TokenValueSet  tokens) throws DomainException
    {
        try
        {
            final PEFileLayout  layout = getFileLayout(domainConfig);
            final File startServTemplate = layout.getStartServTemplate();
            final File startServ = layout.getStartServ();
            generateFromTemplate(tokens, startServTemplate, startServ);
        }
        catch (Exception e)
        {
            throw new DomainException(
                strMgr.getString("startServNotCreated"), e);
        }
    }

    void createStopServ(DomainConfig domainConfig, 
        TokenValueSet  tokens) throws DomainException
    {
        try
        {
            final PEFileLayout  layout = getFileLayout(domainConfig);
            final File stopServTemplate = layout.getStopServTemplate();
            final File stopServ = layout.getStopServ();
            generateFromTemplate(tokens, stopServTemplate, stopServ);
            //final File killServ = layout.getKillServTemplate();
            //generateFromTemplate(new TokenValueSet(), 
		//layout.getKillServTemplate(), layout.getKillServ());
        }
        catch (Exception e)
        {
            throw new DomainException(
                strMgr.getString("stopServNotCreated"), e);
        }
    } 

    protected void createAppClientContainerXml(
        DomainConfig domainConfig) throws DomainException
    {
        try
        {
            final PEFileLayout layout = getFileLayout(domainConfig);
            final File accXmlTemplate = 
                layout.getAppClientContainerXmlTemplate();
            final File accXml = layout.getAppClientContainerXml();
            TokenValueSet tokens = PEAccXmlTokens.getTokenValueSet(domainConfig);
            generateFromTemplate(tokens, accXmlTemplate, accXml);
        }
        catch(Exception e)
        {
            throw new DomainException(strMgr.getString("accXmlNotCreated"), e);
        }
    }

    protected void createIndexFile(
        DomainConfig domainConfig) throws DomainException
    {
        final PEFileLayout layout = getFileLayout(domainConfig);
        final File src = layout.getIndexFileTemplate();
        final File dest = layout.getIndexFile();
        try
        {
            final TokenValueSet tokens = IndexHtmlTokens.getTokenValueSet(domainConfig);
            generateFromTemplate(tokens, src, dest);
            handleLocalizedIndexHtmls(layout, tokens);
        }
        catch (IOException ioe)
        {
            throw new DomainException(
                strMgr.getString("indexFileNotCreated"), ioe);
        }
    }

    protected void createLoggingProperties(DomainConfig domainConfig) 
            throws DomainException
    {
        final PEFileLayout layout = getFileLayout(domainConfig);
        final File src = layout.getLoggingPropertiesTemplate();
        final File dest = layout.getLoggingProperties();
        try
        {
            generateFromTemplate(new TokenValueSet(), src, dest);
        }
        catch (IOException ioe)
        {
            throw new DomainException(
                strMgr.getString("loggingPropertiesNotCreated"), ioe);
        }
    }
    
    
    private void handleLocalizedIndexHtmls(PEFileLayout layout, TokenValueSet tokens) {
        Locale locale = Locale.getDefault();
        if (Locale.ENGLISH.getLanguage().equals(locale.getLanguage()))
            return; //don't do anything in the case of English "language", not just locale
        //rename the existing index.file first
        File src  = layout.getNonEnglishIndexFileTemplate(locale);
        File dest = layout.getIndexFile();
        if (src.exists()) {
            dest.renameTo(layout.getEnglishIndexFile());
            dest = layout.getIndexFile();
            try {
                generateFromTemplate(tokens, src, dest);
            } catch(IOException e) {
                String one = strMgr.getString("problemCopyingIndexHtml", src.getAbsolutePath(), dest.getAbsolutePath());
                System.out.println(one);
            }
        } else {
            String two = strMgr.getString("localeFileNotFound", locale, src.getAbsolutePath());
            System.out.println(two);
        }
    }
    
    protected void createDefaultWebXml(
        DomainConfig domainConfig) throws DomainException
    {
        final PEFileLayout layout = getFileLayout(domainConfig);
        final File src = layout.getDefaultWebXmlTemplate();
        final File dest = layout.getDefaultWebXml();
        try
        {
            FileUtils.copy(src, dest);
        }
        catch (IOException ioe)
        {
            throw new DomainException(
                strMgr.getString("defaultWebXmlNotCreated"), ioe);
        }
    }
    
    protected void createLoginConf(
        RepositoryConfig config) throws DomainException
    {
        final PEFileLayout layout = getFileLayout(config);
        final File src = layout.getLoginConfTemplate();
        final File dest = layout.getLoginConf();
        try
        {
            FileUtils.copy(src, dest);
        }
        catch (IOException ioe)
        {
            throw new DomainException(
                strMgr.getString("loginConfNotCreated"), ioe);
        }
    }    

    protected void createWssServerConfigOld(RepositoryConfig config)
        throws DomainException
    {
        final PEFileLayout layout = getFileLayout(config);
        final File src = layout.getWssServerConfigOldTemplate();
        final File dest = layout.getWssServerConfigOld();
        try
        {
            FileUtils.copy(src, dest);
        }
        catch (IOException ioe)
        {
            throw new DomainException(
                strMgr.getString("wssserverconfignotcreated"), ioe);
        }

    }

    protected void createWssServerConfig(RepositoryConfig config)
        throws DomainException
    {
        final PEFileLayout layout = getFileLayout(config);
        final File src = layout.getWssServerConfigTemplate();
        final File dest = layout.getWssServerConfig();
        try
        {
            FileUtils.copy(src, dest);
        }
        catch (IOException ioe)
        {
            throw new DomainException(
                strMgr.getString("wssserverconfignotcreated"), ioe);
        }

    }

    protected File getDomainDir(DomainConfig domainConfig)
    {
        return getRepositoryDir(domainConfig);
    }

    protected File getDomainRoot(DomainConfig domainConfig)
    {
        return getRepositoryRootDir(domainConfig);
    }

    String getDefaultInstance()
    {
        return PEFileLayout.DEFAULT_INSTANCE_NAME;
    }
     
    /** Returns the domain user from the domainConfig.
     *  @param domainConfig that represents the domain configuration
     *  @return String representing the domain user if the given map contains
     *  it, null otherwise
    */

    protected static String getDomainUser(DomainConfig domainConfig) 
    {
        return ( (String) domainConfig.get(DomainConfig.K_USER) );
    }
    
    /** Returns the domain user's password in cleartext from the domainConfig.
     *  @param domainConfig that represents the domain configuration
     *  @return String representing the domain user password if the 
     *  given map contains it, null otherwise
    */

    protected static String getDomainPasswordClear(DomainConfig domainConfig) 
    {
        return ( (String) domainConfig.get(DomainConfig.K_PASSWORD) );
    } 
    
    protected static String getMasterPasswordClear(DomainConfig domainConfig)
    {
        return ((String)domainConfig.get(DomainConfig.K_MASTER_PASSWORD));
    }
    
    protected static String getNewMasterPasswordClear(DomainConfig domainConfig)
    {
        return ((String)domainConfig.get(DomainConfig.K_NEW_MASTER_PASSWORD));
    }
    
    protected static boolean saveMasterPassword(DomainConfig domainConfig) {
        Boolean b = (Boolean)domainConfig.get(DomainConfig.K_SAVE_MASTER_PASSWORD);
        return b.booleanValue();
    }
    
    /**
     * Changes the master password for the domain
     */    
    public void changeMasterPassword(DomainConfig config) throws DomainException
    {                                      
        try {
            String oldPass = getMasterPasswordClear(config);
            String newPass = getNewMasterPasswordClear(config);                        
            
            //Change the password of the keystore alias file
            changePasswordAliasKeystorePassword(config, oldPass, newPass);
            
            //Change the password of the keystore and truststore
            changeSSLCertificateDatabasePassword(config, oldPass, newPass);

            //Change the password in the masterpassword file or delete the file if it is 
            //not to be saved.
            changeMasterPasswordInMasterPasswordFile(config, newPass, saveMasterPassword(config));
        } catch (Exception ex) {
            throw new DomainException(
                strMgr.getString("masterPasswordNotChanged"), ex);
        }
    }
    public String[] getExtraPasswordOptions(DomainConfig config)
        throws DomainException
    {
        return null;
    }
    /*
    public void stopDomainForcibly(DomainConfig domainConfig, int timeout) 
        throws DomainException 
    {
        try {
            checkRepository(domainConfig);
            InstancesManager domainMgr = getInstancesManager(domainConfig);

            boolean stopped = false;
            
            if(timeout > 0)
                stopped = domainMgr.stopInstanceWithinTime(timeout);
            
            if (!stopped) domainMgr.killRelatedProcesses(); 
        } catch (Exception e) {
            throw new DomainException(e);
        }        
    }

    protected Properties getEnvProps(DomainConfig dc)
    {
        Properties p = new Properties();

        p.setProperty(SystemPropertyConstants.INSTANCE_ROOT_PROPERTY,
                    dc.getRepositoryRoot() + File.separator + dc.getRepositoryName());
        
        // this will be set again by ASLauncher.  It doesn't hurt to do it here too
        p.setProperty("domain.name", dc.getRepositoryName());
      
        p.setProperty(LaunchConstants.PROCESS_NAME_PROP, "as9-server");
        
        return p;
    }

    private boolean isDomainNotRunning(DomainConfig domainConfig) 
        throws InstanceException
    {
        InstancesManager instMgr = getInstancesManager(domainConfig);
        
        return (Status.kInstanceNotRunningCode ==
                instMgr.getInstanceStatus());
    }
    */
    
    private File invokeGenericXmlTemplateProcessing(final File base, final DomainConfig
        dc, final String propName, final File destDir) 
        throws ProfileTransformationException {
        final PEFileLayout layout                 = getFileLayout(dc);
        final String profileName                  = dc.getProfile();
        final File profileFolder                  = layout.getProfileFolder(profileName);
        String msg = strMgr.getString("genericProfileHandlingStarts", profileFolder.getName());
        System.out.println(msg);
        msg = strMgr.getString("genericXmlProcessing", base, profileFolder.getName(), propName);
        System.out.println(msg);
        final File propsFile = layout.getProfilePropertiesFile(profileName);
        final List<File> styleSheets = getStyleSheetList(profileFolder, propsFile, propName);
        final Properties p = new Properties();
    //    p.setProperty(OutputKeys.DOCTYPE_PUBLIC, ServerValidationHandler.SERVER_DTD_PUBLIC_ID);
    //    p.setProperty(OutputKeys.DOCTYPE_SYSTEM, ServerValidationHandler.SERVER_DTD_SYSTEM_ID);
        //final EntityResolver er = new ServerValidationHandler();
        final EntityResolver er = new DefaultHandler();
        final ProfileTransformer px = new ProfileTransformer(base, styleSheets, destDir, er, p);
        final File transformed = px.transform();
        return ( transformed );
    }
    
    private static List<File> getStyleSheetList(final File pf,
        final File propsFile, final String p) throws RuntimeException {
        /* Implementation note: I have chosen to throw a runtime exception here
         * because if this fails, there is no way to recover. It almost
         * certainly means that there is something wrong with the installation.
         */
        if (! propsFile.exists() || ! propsFile.canRead()) {
            final String msg = strMgr.getString("profilePropertiesMissing", propsFile.getAbsolutePath());
            throw new IllegalArgumentException(msg);
        }
        try {
            final String ssNames    = readProfilePropertyValue(propsFile, p);
            final StringTokenizer tk = new StringTokenizer(ssNames, NAME_DELIMITER);
            final List<File> list    = new ArrayList<File>(tk.countTokens());
            while (tk.hasMoreTokens()) {
                final File ss = new File(pf, tk.nextToken().trim());
                if (! ss.exists()) {
                    final String msg = strMgr.getString("styleSheetNotFound", ss.getAbsolutePath());
                    throw new RuntimeException(msg);
                }
                list.add(ss);
            }
            return ( list );
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    private static String readProfilePropertyValue(final File propsFile, final
        String propName) throws IOException {
        /* Could be optimized, but it should not be done prematurely. This
         * method could be called multiple times while doing profile processing,
         * but it is OK to load the properties every time, because I don't have
         * data to suggest that that should not be done. I don't think we will
         * run into performance issues with respect to this.
         */
        final Properties ps = new Properties();
        String propValue = null;
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(propsFile));
        try {
            ps.load(bis);
            propValue = ps.getProperty(propName);
            if (propValue == null) {
                propValue = "";
            }
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (final IOException ee) {
                    // Have to squelch
                }
            }
        }
        return ( propValue ); 
    }
    
    private static void addTokenValuesIfAny(final TokenValueSet tokens, 
        final PEFileLayout layout, final String profileName, final String propName) 
        throws IOException {
        final File propsFile      = layout.getProfilePropertiesFile(profileName);
        final String tvsAsString  = readProfilePropertyValue(propsFile, propName);
        final Set<TokenValue> tvs = s2TV(tvsAsString);
        tokens.addAll(tvs);
    }
    
    private static Set<TokenValue> s2TV(final String nvpairs) {
        final Set<TokenValue> set = new HashSet<TokenValue>();
        final StringTokenizer st = new StringTokenizer(nvpairs, NAME_DELIMITER);
        String msg;
        while (st.hasMoreTokens()) {
            String tvs = st.nextToken();
            if (tvs == null || tvs.indexOf("=") == -1 || tvs.length() < 3) {
                msg = strMgr.getString("invalidToken", tvs);
            }
            tvs = tvs.trim();
            final int e2 = tvs.indexOf("=");
            final String t = tvs.substring(0, e2);
            final String v = tvs.substring(e2+1);
            msg = strMgr.getString("processingToken", new String[] {t, v});
            System.out.println(msg);
            final TokenValue tv = new TokenValue(t, v); //default delimiter
            set.add(tv);
        }
        return ( set );
    }

}
