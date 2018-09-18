/*************************************************************************
 * 
 * ADOBE CONFIDENTIAL
 * __________________
 * 
 *  [2002] - [2007] Adobe Systems Incorporated 
 *  All Rights Reserved.
 * 
 * NOTICE:  All information contained herein is, and remains
 * the property of Adobe Systems Incorporated and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Adobe Systems Incorporated
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Adobe Systems Incorporated.
 */
package flex.messaging.config;

/**
 * This configuration class is derived from optional properties that
 * may be supplied in the &lt;properties&gt; section of a destination.
 * It exists to capture properties related to message thottling in a way
 * that simplifies the ThrottleManager's usage of the configuration.
 * 
 * @author neville
 */
public class ThrottleSettings
{   
    /** Integer value of no policy **/
    public static final int POLICY_NONE = 0;
    /** Integer value of error policy **/
    public static final int POLICY_ERROR = 1;
    /** Integer value of ignore policy **/
    public static final int POLICY_IGNORE = 2;
    /** Integer value of replace policy **/
    public static final int POLICY_REPLACE = 3;
    
    /** @exclude **/
    public static final String POLICY_NONE_STRING = "NONE";
    /** @exclude **/
    public static final String POLICY_ERROR_STRING = "ERROR";
    /** @exclude **/
    public static final String POLICY_IGNORE_STRING = "IGNORE";
    /** @exclude **/
    public static final String POLICY_REPLACE_STRING = "REPLACE";
    
    /** @exclude **/
    public static final String ELEMENT_INBOUND = "throttle-inbound";
    /** @exclude **/
    public static final String ELEMENT_OUTBOUND = "throttle-outbound";
    /** @exclude **/
    public static final String ELEMENT_POLICY = "policy";
    /** @exclude **/
    public static final String ELEMENT_DEST_FREQ = "max-frequency";
    /** @exclude **/
    public static final String ELEMENT_CLIENT_FREQ = "max-client-frequency";
 
    private String destinationName;
    private int inClientMessagesPerSec;
    private int inDestinationMessagesPerSec;
    private int outClientMessagesPerSec;
    private int outDestinationMessagesPerSec;
    private int inPolicy;
    private int outPolicy;
    
    /**
     * Creates a <code>ThrottleSettings</code> instance with default settings.
     */
    public ThrottleSettings()
    {
        inPolicy = POLICY_NONE;
        outPolicy = POLICY_NONE;
        inClientMessagesPerSec = 0;
        inDestinationMessagesPerSec = 0;
        outDestinationMessagesPerSec = 0;
    }
    
    /** @exclude **/        
    public int parsePolicy(String s)
    {
        int n;
        if (s.equalsIgnoreCase(POLICY_IGNORE_STRING))
        {
            n = POLICY_IGNORE;
        }
        else if (s.equalsIgnoreCase(POLICY_ERROR_STRING))
        {
            n = POLICY_ERROR;
        }
        else if (s.equalsIgnoreCase(POLICY_REPLACE_STRING))
        {
            n = POLICY_REPLACE;
        }
        else
        {
            ConfigurationException ex = new ConfigurationException();
            ex.setMessage("Unsupported throttle policy '" + s + "'");
            throw ex;
        }
        return n;
    }
    
    /**
     * Returns whether client throttling is enabled. 
     * 
     * @return <code>true</code> if the incoming client frequency or outgoing
     * client frequency is greater than zero; otherwise <code>false</code>.
     */
    public boolean isClientThrottleEnabled()
    {
        if (getIncomingClientFrequency() > 0 || getOutgoingClientFrequency() > 0)
        {
            return true;
        }
        return false;
    }
    
    /**
     * Returns whether destination throttling is enabled.
     * 
     * @return <code>true</code> if incoming destination frequency or outgoing
     * destination frequency is greater than zero; otherwise <code>false</code>.
     */
    public boolean isDestinationThrottleEnabled()
    {
        if (getIncomingDestinationFrequency() > 0 || getOutgoingDestinationFrequency() > 0)
        {
            return true;
        }
        return false;
    }
    
    /**
     * Returns the inbound throttle policy.
     * 
     * @return the inbound throttle policy.
     */
    public int getInboundPolicy()
    {
        return inPolicy;
    }
    
    /**
     * Sets inbound throttle policy. The inbound policy may be ERROR or IGNORE.  
     * 
     * @param inPolicy 
     */
    public void setInboundPolicy(int inPolicy)
    {
        if (inPolicy == POLICY_REPLACE)
        {
            ConfigurationException ex = new ConfigurationException();
            ex.setMessage("The REPLACE throttle policy applies to outbound throttling only");
            throw ex;
        }
        this.inPolicy = inPolicy;
    }
    
    /**
     * Returns the outbound throttle policy.
     * 
     * @return the outbound throttle policy.
     */
    public int getOutboundPolicy()
    {
        return outPolicy;
    }
    
    /**
     * Sets the outbound throttle policy. The outbound policy may be ERROR, 
     * IGNORE, or REPLACE.
     * 
     * @param outPolicy
     */
    public void setOutboundPolicy(int outPolicy)
    {
        this.outPolicy = outPolicy;
    }
    
    /**
     * Returns the destination name for <code>ThrottleSettings</code>.
     * 
     * @return the destination name for <code>ThrottleSettings</code>.
     */
    public String getDestinationName()
    {
        return destinationName;
    }
    
    /**
     * Sets the destination name for <code>ThrottleSettings</code>. This is set
     * automatically when <code>NetworkSettings</code> is assigned to a destination.
     * 
     * @param destinationName
     */
    public void setDestinationName(String destinationName)
    {
        this.destinationName = destinationName;
    }
    
    /**
     * Returns incoming client frequency (max-client-frequency).
     * 
     * @return incoming client frequency (max-client-frequency).
     */
    public int getIncomingClientFrequency()
    {
        return inClientMessagesPerSec;
    }
    
    /**
     * Sets incoming client frequency (max-client-frequency). 
     * Optional. Default value is 0.
     * 
     * @param n
     */
    public void setIncomingClientFrequency(int n)
    {
        this.inClientMessagesPerSec = n;
    }
    
    /**
     * Returns incoming destination frequency (max-frequency).
     * 
     * @return incoming destination frequency (max-frequency).
     */
    public int getIncomingDestinationFrequency()
    {
        return inDestinationMessagesPerSec;
    }
    
    /**
     * Sets incoming destination frequency (max-frequency). 
     * Optional. Default value is 0. 
     * 
     * @param n
     */
    public void setIncomingDestinationFrequency(int n)
    {
        this.inDestinationMessagesPerSec = n;
    }
    
    /**
     * Returns outgoing client frequency (max-client-frequency). 
     * 
     * @return outgoing client frequency (max-client-frequency).
     */
    public int getOutgoingClientFrequency()
    {
        return outClientMessagesPerSec;
    }
    
    /**
     * Sets outgoing client frequency (max-client-frequency). 
     * Optional. Default value is 0.
     * 
     * @param n
     */
    public void setOutgoingClientFrequency(int n)
    {
        this.outClientMessagesPerSec = n;
    }

    /**
     * Returns outgoing destination frequency (max-frequency). 
     * 
     * @return outgoing destination frequency (max-frequency). 
     */
    public int getOutgoingDestinationFrequency()
    {
        return outDestinationMessagesPerSec;
    }
    
    /**
     * Sets outgoing destination frequency (max-frequency). 
     * Optional. Default value is 0.
     * 
     * @param n
     */
    public void setOutgoingDestinationFrequency(int n)
    {
        this.outDestinationMessagesPerSec = n;
    }    
}
