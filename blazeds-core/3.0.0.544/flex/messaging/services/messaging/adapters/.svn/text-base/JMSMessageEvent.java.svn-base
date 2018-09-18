package flex.messaging.services.messaging.adapters;

import java.util.EventObject;
import javax.jms.Message;

/**
 * Event dispatched to the JMSMessageListener when a JMS message is received
 * by the source.
 * 
 * @see flex.messaging.services.messaging.adapters.JMSMessageListener
 */
public class JMSMessageEvent extends EventObject
{
    private Message message;
    
    /**
     * Create a new JMSMessageEvent with the source and message.
     * 
     * @param source The source of the message.
     * @param jmsException The actual JMS message.
     */    
    JMSMessageEvent(JMSConsumer source, javax.jms.Message message)
    {
        super(source);
        this.message = message;
    }
    
    /**
     * Return the JMS message of the event.
     * 
     * @return The JMS message of the event.
     */
    public Message getJMSMessage()
    {
        return message;
    }
}