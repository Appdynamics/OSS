package flex.messaging.services.messaging.adapters;

import java.util.EventListener;

/**
 * An interface to be notified when a JMS exception is encountered by the JMS
 * consumer. Implementations of this interface may add themselves as listeners
 * via <code>JMSConsumer.addJMSExceptionListener</code>.
 */
public interface JMSExceptionListener extends EventListener
{
    /**
     * Notification that a JMS exception was encountered.
     * 
     * @param evt JMSExceptionEvent to dispatch.
     */    
    public void exceptionThrown(JMSExceptionEvent evt); 
}