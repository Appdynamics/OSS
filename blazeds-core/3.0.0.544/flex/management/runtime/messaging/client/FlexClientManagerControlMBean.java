/**
 * 
 */
package flex.management.runtime.messaging.client;

import java.io.IOException;

import flex.management.BaseControlMBean;

/**
 * @author majacobs
 */
public interface FlexClientManagerControlMBean extends BaseControlMBean
{
    /**
     * @return An array of all ClientIds as strings.
     * @throws IOException
     */
    String[] getClientIds() throws IOException;
    
    /**
     * @param clientId
     * @return The number of subscriptions for the client with the cliendId
     * @throws IOException
     */
    Integer getClientSubscriptionCount(String clientId) throws IOException;
    
    /**
     * @param clientId
     * @return The number of sessions for the client with the cliendId
     * @throws IOException
     */    
    Integer getClientSessionCount(String clientId) throws IOException;
    
    /**
     * @param clientId
     * @return The last use by the client with the clientId
     * @throws IOException
     */
    Long getClientLastUse(String clientId) throws IOException;
    
    /**
     * @return The number of FlexClients.
     * @throws IOException
     */
    Integer getFlexClientCount() throws IOException;    
}
