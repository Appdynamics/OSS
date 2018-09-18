package flex.messaging.services.messaging.adapters;

import java.util.EventListener;

/**
 * An interface to be notified when a JMS message is received by the JMS
 * consumer. Implementations of this interface may add themselves as listeners
 * via <code>JMSConsumer.addJMSMessageListener</code>.
 */
public interface JMSMessageListener extends EventListener
{
    /**
     * Notification that a JMS message was received.
     * 
     * @param evt JMSMessageEvent to dispatch.
     */        
    public void messageReceived(JMSMessageEvent evt); 
}