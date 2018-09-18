package flex.messaging.services.messaging.adapters;

import java.util.EventObject;
import javax.jms.JMSException;

/**
 * Event dispatched to the JMSExceptionListener when a JMS exception is encountered
 * by the source.
 * 
 * @see flex.messaging.services.messaging.adapters.JMSExceptionListener
 */
public class JMSExceptionEvent extends EventObject
{
    private JMSException jmsException;
    
    /**
     * Create a new JMSExceptionEvent with the source and exception.
     * 
     * @param source The source of the exception.
     * @param jmsException The actual JMS exception.
     */
    JMSExceptionEvent(JMSConsumer source, JMSException jmsException)
    {
        super(source);
        this.jmsException = jmsException;
    }
    
    /**
     * Return the JMS exception of the event.
     * 
     * @return The JMS exception of the event.
     */
    public JMSException getJMSException()
    {
        return jmsException;
    }    
}