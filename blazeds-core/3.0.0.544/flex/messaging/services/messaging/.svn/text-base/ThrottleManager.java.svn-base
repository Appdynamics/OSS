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
package flex.messaging.services.messaging;

import java.util.HashMap;
import java.util.Map;

import flex.management.ManageableComponent;
import flex.management.runtime.messaging.services.messaging.ThrottleManagerControl;
import flex.messaging.MessageException;
import flex.messaging.config.ThrottleSettings;
import flex.messaging.log.LogCategories;
import flex.messaging.messages.Message;

/**
 * The ThrottleManager provides functionality to limit the frequency of messages 
 * routed through the system in message/second terms. Message frequency can be managed
 * on a per-client basis and also on a per-destination basis by tweaking different 
 * parameters. Each MessageDestination has one ThrottleManager. 
 * 
 * Message frequency can be throttled differently for incoming messages, which are messages
 * published by Flash/Flex producers, and for outgoing messages, which are messages  
 * consumed by Flash/Flex subscribers that may have bene produced by either Flash clients
 * or external message producers (such as data feeds, JMS publishers, etc).
 *  
 * @author neville
 */
public class ThrottleManager extends ManageableComponent
{
    public static final String LOG_CATEGORY = LogCategories.SERVICE_MESSAGE; 
        
    public static final String TYPE = "ThrottleManager";
    private static final Object classMutex = new Object();
    private static int instanceCount = 0;      
    
    private Map inboundClientMarks;
    private Map outboundClientMarks;
    private ThrottleMark inboundDestinationMark;
    private ThrottleMark outboundDestinationMark;
    private ThrottleSettings settings;
    
    public static final int RESULT_OK = 0;
    public static final int RESULT_IGNORE = 1;
    public static final int RESULT_REPLACE = 2;
    public static final int RESULT_ERROR = 3;

    // TODO: QUESTION: Seth, make this configurable?
    static final int MESSAGE_HISTORY_SIZE = 15;

    public ThrottleManager()
    {
        this(false);
    }
    
    public ThrottleManager(boolean enableManagement)
    {
        super(enableManagement);
        synchronized (classMutex)
        {
            super.setId(TYPE + ++instanceCount);
        }

        settings = new ThrottleSettings();
    }
     
    // This component's id should never be changed as it's generated internally
    public void setId(String id)
    {
        // No-op
    }
    
    public void setThrottleSettings(ThrottleSettings throttleSettings)
    {
        settings = throttleSettings;        
        if (settings.isDestinationThrottleEnabled())
        {
            inboundDestinationMark = new ThrottleMark(settings.getDestinationName());
            outboundDestinationMark = new ThrottleMark(settings.getDestinationName());
        }
        if (settings.isClientThrottleEnabled())
        {
            inboundClientMarks = new HashMap();
            outboundClientMarks = new HashMap();
        }
    }
    
    public int throttleIncomingMessage(Message msg)
    {
        // destination-level throttling comes before client-level, 
        // because if it fails then it doesn't matter what the client-level 
        // throttle reports
        int n = RESULT_OK;
        
        if (settings.getInboundPolicy() != ThrottleSettings.POLICY_NONE)
        {
            n = throttleDestinationLevel(msg, true);
	        if (n == RESULT_OK)
	        {
	            // client-level throttling allows the system to further refine a 
	            // different throttle for individual clients, which may be a subset 
	            // but never a superset of destination-level throttle settings
	            n = throttleClientLevel(msg, msg.getClientId(), true);                
	        }
        }
        return n;
    }
    
    public int throttleOutgoingMessage(Message msg, Object clientId)
    {
        int n = RESULT_OK;
        if (settings.getOutboundPolicy() != ThrottleSettings.POLICY_NONE)
        {
	        if (clientId == null)
	            n = throttleDestinationLevel(msg, false);
	        else
	            n = throttleClientLevel(msg, clientId, false);
        }
        return n;
    }
    
    private int throttleDestinationLevel(Message msg, boolean incoming)
    {
        int throttleResult = RESULT_OK;
        if (settings.isDestinationThrottleEnabled())
        {
            if (incoming)
            {
                try
                {
                    inboundDestinationMark.assertValid(msg, settings.getIncomingDestinationFrequency());
                }
                catch (RuntimeException e)
                {
                    String s = "Message throttled: Too many messages sent to destination " 
                        + inboundDestinationMark.id + " in too small of a time interval.  " + e.getMessage();
                    MessageException me = new MessageException(s);
                    try
                    {
                        throttleResult = handleError(settings.getInboundPolicy(), me);
                    }
                    catch (MessageException m)
                    {
                        throttleResult = RESULT_ERROR;
                        throw m;
                    }
                    finally
                    {
                        if (throttleResult != RESULT_OK && isManaged())
                            ((ThrottleManagerControl)getControl()).incrementDestinationIncomingMessageThrottleCount();
                    }                    
                }
            }
            else
            {
                try
                {
                    outboundDestinationMark.assertValid(msg, settings.getOutgoingDestinationFrequency());
                }
                catch (RuntimeException e)
                {
                    String s = "Message throttled: Too many messages routed by destination " 
                        + outboundDestinationMark.id + " in too small of a time interval";
                    MessageException me = new MessageException(s);
                    try
                    {
                        throttleResult = handleError(settings.getOutboundPolicy(), me);
                    }
                    catch (MessageException m)
                    {
                        throttleResult = RESULT_ERROR;
                        throw m;
                    }
                    finally
                    {
                        if (throttleResult != RESULT_OK && isManaged())
                            ((ThrottleManagerControl)getControl()).incrementDestinationOutgoingMessageThrottleCount();                            
                    }                    
                }
            }
        }
        return throttleResult;
    }

    private int throttleClientLevel(Message msg, Object clientId, boolean incoming)
    {
        int throttleResult = RESULT_OK;
    	if (settings.isClientThrottleEnabled())
        {
            ThrottleMark clientLevelMark = null;
	        if (incoming)
            {
	            if (inboundClientMarks.get(clientId) != null)
		            clientLevelMark = (ThrottleMark) inboundClientMarks.get(clientId);
		        else
		            clientLevelMark = new ThrottleMark(clientId);
	            try
	            {
	                clientLevelMark.assertValid(msg, settings.getIncomingClientFrequency());
	            }
	            catch (RuntimeException e)
                {
                    String s = "Message throttled: Too many messages sent by client " 
                        + clientId + " in too small of a time interval";
                    MessageException me = new MessageException(s);
                    try
                    {
                        throttleResult = handleError(settings.getInboundPolicy(), me);
                    }
                    catch (MessageException m)
                    {
                        throttleResult = RESULT_ERROR;
                        throw m;
                    }
                    finally
                    {
                        if ((throttleResult != RESULT_OK) && isManaged())
                            ((ThrottleManagerControl)getControl()).incrementClientIncomingMessageThrottleCount();
                    }                    
                }
	            finally
	            {
	                inboundClientMarks.put(clientId, clientLevelMark);
	            }
            }
	        else
	        {
	            if (outboundClientMarks.get(clientId) != null)
		            clientLevelMark = (ThrottleMark) outboundClientMarks.get(clientId);
		        else
		            clientLevelMark = new ThrottleMark(clientId);
	            try
	            {
	                clientLevelMark.assertValid(msg, settings.getOutgoingClientFrequency());
	            }
                catch (RuntimeException e)
                {
                    String s = "Message throttled: Too many messages sent to client " 
                        + clientId + " in too small of a time interval";
                    MessageException me = new MessageException(s);
                    try
                    {
                        throttleResult = handleError(settings.getOutboundPolicy(), me);
                    }
                    catch (MessageException m)
                    {
                        throttleResult = RESULT_ERROR;
                        throw m;
                    }
                    finally
                    {
                        if ((throttleResult != RESULT_OK) && isManaged())
                            ((ThrottleManagerControl)getControl()).incrementClientOutgoingMessageThrottleCount();
                    }
                }
	            finally
	            {
	                outboundClientMarks.put(clientId, clientLevelMark);
	            }
	        }
        }
        return throttleResult;
    }
    
    private int handleError(int policy, MessageException e)
    {
        int n = 0; 
        switch(policy)
        {
            case ThrottleSettings.POLICY_IGNORE:
                n = RESULT_IGNORE;
            	break;
            case ThrottleSettings.POLICY_REPLACE:
                // replace can only work as an outgoing option
                // (this is enforced at configuration time)
                n = RESULT_REPLACE;
            	break;
            case ThrottleSettings.POLICY_ERROR:
                throw e;
            default:
                break;
        }
        return n;
    }
    
    public void removeClientThrottleMark(Object clientId)
    {
        if (inboundClientMarks != null)
        {
            inboundClientMarks.remove(clientId);
        }
        if (outboundClientMarks != null)
        {
            outboundClientMarks.remove(clientId);
        }
    }
    
    protected String getLogCategory()
    {
        return LOG_CATEGORY;
    }
    
    class ThrottleMark
    {
        Object id;
        int messageCount;
        String lastMessageId;
        long [] lastMessageTimes;
        
        ThrottleMark(Object identifier)
        {
            id = identifier;
            messageCount = 0;
            lastMessageId = "-1";
            lastMessageTimes = new long[MESSAGE_HISTORY_SIZE];
        }
        
        void assertValid(Message msg, int frequency)
        {
            if (frequency > 0) 
            {
                int len = lastMessageTimes.length;
                // if we have enough messages to start testing
                if (messageCount >= len)
                {
                    // Time delay between this message and the last "N" messages
                    long interval = msg.getTimestamp() - 
                        lastMessageTimes[(messageCount - len) % len];
                    long rate = 1000*len/interval;
                    // If the rate is too high, we toss this message
                    // and do not record it so our history represents the
                    // rate of messages actually delivered.
                    if (rate > frequency)
                    {
                        throw new RuntimeException("actual frequency=" + rate + " max frequency=" + frequency);
                    }
                }
                lastMessageId = msg.getMessageId();
                lastMessageTimes[messageCount++ % len] = msg.getTimestamp();
            }
        }
    }
}
