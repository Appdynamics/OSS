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
package flex.messaging.services.messaging.adapters;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.jms.JMSException;
import javax.naming.Context;

import edu.emory.mathcs.backport.java.util.concurrent.ConcurrentHashMap;

import flex.management.runtime.messaging.services.messaging.adapters.JMSAdapterControl;
import flex.messaging.Destination;
import flex.messaging.MessageClient;
import flex.messaging.MessageClientListener;
import flex.messaging.MessageDestination;
import flex.messaging.MessageException;
import flex.messaging.config.ConfigMap;
import flex.messaging.config.ConfigurationException;
import flex.messaging.log.Log;
import flex.messaging.log.LogCategories;
import flex.messaging.messages.CommandMessage;
import flex.messaging.messages.ErrorMessage;
import flex.messaging.messages.Message;
import flex.messaging.messages.MessagePerformanceInfo;
import flex.messaging.messages.MessagePerformanceUtils;
import flex.messaging.services.MessageService;
import flex.messaging.services.messaging.adapters.JMSSettings.DeliverySettings;

/**
 * This adapter for the MessageService integrates Flex messaging
 * with Java Message Service destinations.
 */
public class JMSAdapter extends MessagingAdapter implements JMSConfigConstants, JMSExceptionListener, JMSMessageListener, MessageClientListener 
{    
    static final String LOG_CATEGORY = LogCategories.SERVICE_MESSAGE_JMS;

    private static final String DURABLE_SUBSCRIBER_NAME_PREFIX = "FlexClient_";
    
    private Map consumerToClientId; 
    private Map messageClients;
    private LinkedList topicProducers;
    private Map topicConsumers;
    private LinkedList queueProducers;
    private Map queueConsumers;   

    // JMSAdapter properties
    private JMSSettings settings;
    
    private JMSAdapterControl controller;

    //--------------------------------------------------------------------------
    //
    // Constructor
    //
    //--------------------------------------------------------------------------
    
    /**
     * Constructs an unmanaged <code>JMSAdapter</code> instance.
     */
    public JMSAdapter()
    {
        this(false);
    }
    
    /**
     * Constructs a <code>JMSAdapter</code> instance.
     * 
     * @param enableManagement <code>true</code> if the <code>JMSAdapter</code> 
     * has a corresponding MBean control for management; otherwise <code>false</code>.
     */
    public JMSAdapter(boolean enableManagement)
    {
        super(enableManagement);    
        consumerToClientId = new ConcurrentHashMap();
        messageClients = new ConcurrentHashMap();
        topicProducers = new LinkedList();
        topicConsumers = new ConcurrentHashMap();
        queueProducers = new LinkedList();
        queueConsumers = new ConcurrentHashMap();
        settings = new JMSSettings();
    }

    //--------------------------------------------------------------------------
    //
    // Initialize, validate, start, and stop methods. 
    //
    //--------------------------------------------------------------------------
    
    /**
     * Initializes the <code>JMSAdapter</code> with the properties. 
     * 
     * @param properties Properties for the <code>JMSAdapter</code>.
     */
    public void initialize(String id, ConfigMap properties)
    {
        super.initialize(id, properties);
        
        if (properties == null || properties.size() == 0)
            return;
        
        // JMS specific properties
        jms(properties);        
    }
            
    /**
     * Verifies that the <code>JMSAdapter</code> is in valid state before
     * it is started. 
     */
    protected void validate()
    {        
        if (isValid())
            return;
        
        super.validate();
        
        if (settings.getConnectionFactory() == null)
        {
            // JMS connection factory of message destinations with JMS Adapters must be specified.            
            ConfigurationException ce = new ConfigurationException();
            ce.setMessage(MISSING_CONNECTION_FACTORY);
            throw ce;
        }
        
        if (settings.getDestinationJNDIName() == null)
        {
            // JNDI names for message destinations with JMS Adapters must be specified.
            ConfigurationException ce = new ConfigurationException();
            ce.setMessage(JMSConfigConstants.MISSING_DESTINATION_JNDI_NAME);
            throw ce;            
        }

        if (settings.getMessageType() == null)
        {
            // Unsupported JMS Message Type ''{0}''. Valid values are javax.jms.TextMessage and javax.jms.ObjectMessage. 
            ConfigurationException ce = new ConfigurationException();
            ce.setMessage(INVALID_JMS_MESSAGE_TYPE, new Object[] {null});
            throw ce;            
        }        
    }
    
    /**
     * Starts the adapter
     */
    public void start()
    {
        if (isStarted())
            return;

        super.start();        
        
        // Add JMS adapter as a MessageClient created listener so that its
        // JMS consumers can be associated with their message clients.         
        MessageClient.addMessageClientCreatedListener(this);        
    }
    
    /**
     * Stops the adapter. 
     */
    public void stop()
    {
        if (!isStarted())
            return;
        
        super.stop();
        
        stopConsumers(topicConsumers.values());
        stopConsumers(queueConsumers.values());
    }
    
    //--------------------------------------------------------------------------
    //
    // Public methods
    //         
    //--------------------------------------------------------------------------   
    
    /**
     * Casts the <code>Destination</code> into <code>MessageDestination</code>
     * and calls super.setDestination.
     * 
     * @param destination
     */
    public void setDestination(Destination destination)
    {
        Destination dest = (MessageDestination)destination;
        super.setDestination(dest);
    }
    
    /**
     * Gets the <code>JMSSettings</code> of the <code>JMSAdapter</code>.
     * 
     * @return <code>JMSSettings</code> of the <code>JMSAdapter</code>.
     */
    public JMSSettings getJMSSettings()
    {
        return settings;
    }
    
    /**
     * Sets the <code>JMSSettings</code> of the <code>JMSAdapter</code>.
     * 
     * @param jmsSettings  <code>JMSSettings</code> of the <code>JMSAdapter</code>.
     */
    public void setJMSSettings(JMSSettings jmsSettings)
    {
        this.settings = jmsSettings;
    }          

    /**
     * Returns the count of queue consumers managed by this adapter.
     *
     * @return The count of queue consumers managed by this adapter.
     */
    public int getQueueConsumerCount()
    {
        return queueConsumers.size();
    }
    
    /**
     * Returns the ids of all queue consumers.
     *
     * @return The ids of all queue consumers.
     */
    public String[] getQueueConsumerIds()
    {
        Set consumerIds = queueConsumers.keySet();
        if (consumerIds != null)
        {
            String[] ids = new String[consumerIds.size()];
            return (String[])consumerIds.toArray(ids);
        }
        return new String[0];
    }    
    
    /**
     * Returns the count of topic consumers currently managed by this adapter.
     *
     * @return The count of topic consumers currently managed by this adapter.
     */
    public int getTopicConsumerCount()
    {
        return topicConsumers.size();
    }
    
    /**
     * Returns the ids of all topic consumers.
     *
     * @return The ids of all topic consumers.
     */
    public String[] getTopicConsumerIds()
    {
        Set consumerIds = topicConsumers.keySet();
        if (consumerIds != null)
        {
            String[] ids = new String[consumerIds.size()];
            return (String[])consumerIds.toArray(ids);
        }
        return new String[0];
    }
    
    /**
     * Returns the count of topic producers currently managed by this adapter.
     *
     * @return The count of topic producers currently managed by this adapter.
     */
    public int getTopicProducerCount()
    {
        return topicProducers.size();
    }
    
    /**
     * Returns the count of queue producers currently managed by this adapter.
     *
     * @return The count of queue producers currently managed by this adapter.
     */
    public int getQueueProducerCount()
    {
        return queueProducers.size();
    }
    
    /**
     * Implements JMSExceptionListener. 
     * When a JMSConsumer receives a JMS exception from its underlying JMS 
     * connection, it dispatches a JMS exception event to pass the exception
     * to the JMS adapter.  
     */
    public void exceptionThrown(JMSExceptionEvent evt)
    {                
        JMSConsumer consumer = (JMSConsumer)evt.getSource();
        JMSException jmsEx = evt.getJMSException();
        
        // Client is unsubscribed because its corresponding JMS consumer for JMS destination ''{0}'' encountered an error during message delivery: {1}                    
        MessageException messageEx = new MessageException();
        messageEx.setMessage(JMSConfigConstants.CLIENT_UNSUBSCRIBE_DUE_TO_MESSAGE_DELIVERY_ERROR, new Object[] {consumer.getDestinationJndiName(), jmsEx.getMessage()});                                   
        removeConsumer(consumer, true, true, messageEx.createErrorMessage());        
    }
    
    /**
     * JMS adapter handles its subscriptions so this returns <code>true</code>. 
     */
    public boolean handlesSubscriptions()
    {
        return true;
    }
    
    /**
     * Publish a message to this adapter's JMS destination.
     */
    public Object invoke(Message message)
    {
        JMSProducer producer = null;

        // named Flex message props become JMS headers
        Map msgProps = message.getHeaders();
        msgProps.put(JMSConfigConstants.TIME_TO_LIVE, new Long(message.getTimeToLive()));

        if (settings.getDestinationType().equals(TOPIC))
        {
            synchronized (topicProducers)
            {
                if (topicProducers.size() < settings.getMaxProducers())
                {
                    producer = new JMSTopicProducer();
                    try
                    {
                        producer.initialize(settings);                        
                        producer.start();
                    }
                    catch (Exception e)
                    {
                        throw new MessageException(e);
                    }
                }
                else
                {
                    producer = (JMSProducer)topicProducers.removeFirst();
                }

                topicProducers.addLast(producer);
            }
        }
        else if (settings.getDestinationType().equals(QUEUE))
        {
            synchronized (queueProducers)
            {
                if (queueProducers.size() < settings.getMaxProducers())
                {
                    producer = new JMSQueueProducer();
                    try
                    {
                        producer.initialize(settings);
                        producer.start();
                    }
                    catch (Exception e)
                    {
                        throw new MessageException(e);
                    }
                }
                else
                {
                    producer = (JMSProducer)queueProducers.removeFirst();
                }

                queueProducers.addLast(producer);
            }
        }

        try
        {
            producer.sendMessage(message);
        }
        catch (JMSException jmsEx)
        {
            // At this point we give up on this producer, so we just
            // stop and remove it from the pool.
            if (settings.getDestinationType().equals(TOPIC))
            {
                synchronized (topicProducers)
                {
                    producer.stop();
                    topicProducers.remove(producer);
                }
            }
            else if (settings.getDestinationType().equals(QUEUE))
            {
                synchronized (queueProducers)
                {
                    producer.stop();
                    queueProducers.remove(producer);                        
                }
            }

            throw new MessageException(jmsEx);
        }

        return null;
    }

    /**
     * Handle a CommandMessage sent by this adapter's service.
     */
    public Object manage(CommandMessage commandMessage)
    {
        JMSConsumer consumer = null;
        Object clientId = commandMessage.getClientId();

        if (commandMessage.getOperation() == CommandMessage.SUBSCRIBE_OPERATION)
        {                               
            // Keep track of the selector expression. 
            Object selectorExpression = commandMessage.getHeaders().get(CommandMessage.SELECTOR_HEADER);

            // create a JMSConsumer for this destination and associate it with the client id
            if (settings.getDestinationType().equals(TOPIC))
            {
                if (topicConsumers.containsKey(clientId))
                {
                    consumer = (JMSConsumer)topicConsumers.get(clientId);
                    consumer.stop(true /* Unsubscribe durable subscription */);
                }
                // Create the consumer.
                consumer = new JMSTopicConsumer();
                consumer.initialize(settings);
                if (selectorExpression != null)
                    consumer.setSelectorExpression((String)selectorExpression);
                // Need to build a subscription name, in case durable subscriptions are used.
                ((JMSTopicConsumer)consumer).setDurableSubscriptionName(buildSubscriptionName(clientId));
                consumer.setMessageReceiver(buildMessageReceiver(consumer));

                // Add JMSAdapter as JMS exception and message listener.
                consumer.addJMSExceptionListener(this);
                consumer.addJMSMessageListener(this);
                topicConsumers.put(clientId, consumer);                    
                consumerToClientId.put(consumer, clientId);

                // JMSConsumer will be started once it's corresponding 
                // MessageClient is registed with JMSAdapter.                    
            }
            else if (settings.getDestinationType().equals(QUEUE))
            {
                if (queueConsumers.containsKey(clientId))
                {
                    consumer = (JMSConsumer)queueConsumers.get(clientId);
                    consumer.stop();
                }
                // Create the consumer.
                consumer = new JMSQueueConsumer();
                consumer.initialize(settings);
                if (selectorExpression != null)
                    consumer.setSelectorExpression((String)selectorExpression);
                consumer.setMessageReceiver(buildMessageReceiver(consumer));

                // Add JMSAdapter as JMS exception and message listener.                    
                consumer.addJMSExceptionListener(this);
                consumer.addJMSMessageListener(this);   
                queueConsumers.put(clientId, consumer); 
                consumerToClientId.put(consumer, clientId);

                // JMSConsumer will be started once it's corresponding 
                // MessageClient is registed with JMSAdapter.    
            }
        }

        else if (commandMessage.getOperation() == CommandMessage.UNSUBSCRIBE_OPERATION)
        {                
            // Determines if the durable subscription should be unsubscribed
            // when the JMS consumer is removed.
            boolean unsubscribe = true;

            boolean preserveDurable = false;
            if (commandMessage.getHeader(CommandMessage.PRESERVE_DURABLE_HEADER) != null)
                preserveDurable = ((Boolean)(commandMessage.getHeader(CommandMessage.PRESERVE_DURABLE_HEADER))).booleanValue();

            // Don't destroy a durable subscription if the MessageClient's session has been invalidated.
            // or this is a JMS durable connection that has requested to be undestroyed
            if (commandMessage.getHeader(CommandMessage.SUBSCRIPTION_INVALIDATED_HEADER) != null 
                    && ((Boolean)commandMessage.getHeader(CommandMessage.SUBSCRIPTION_INVALIDATED_HEADER)).booleanValue()
                    || preserveDurable)
                unsubscribe = false;

            removeConsumer((String)clientId, unsubscribe, false, null);
        }

        // CommandMessage.POLL_OPERATION handling is left to the Endpoint 
        // hence not handled by this adapter.

        return null;
    }
   
    /**
     * Implements MessageClientListener.
     * When a MessageClient is created, JMSAdapter looks up its JMSConsumers
     * and if there is a JMSConsumer that has the same clientId as MessageClient,
     * it adds the MessageClient to its list clients. This helps in keeping both 
     * sides of the bridge (MessageClient <--> JMSConsumer) notified when there's 
     * a failure on either side of the bridge. 
     * 
     * @param messageClient The newly created MessageClient.
     */
    public void messageClientCreated(MessageClient messageClient) 
    {
        Object clientId = messageClient.getClientId();       
        
        JMSConsumer consumer = null;
        if (topicConsumers.containsKey(clientId))
            consumer = (JMSConsumer)topicConsumers.get(clientId);
        else if (queueConsumers.containsKey(clientId))
            consumer = (JMSConsumer)queueConsumers.get(clientId);
        
        // If there is a JMSConsumer created for the same clientId, register
        // the MessageClient with JMSAdapter and start the consumer.
        if (consumer != null)        
        {
            messageClients.put(clientId, messageClient);            
            try
            {
                consumer.start();
            }
            catch (MessageException messageEx)
            {
                removeConsumer(consumer, true, true, messageEx.createErrorMessage());
            }
            catch (Exception ex)
            {
                removeConsumer(consumer, true, true, new MessageException(ex).createErrorMessage());
            }
        }
    }
    
    /**
     * Implements MessageClientListener. 
     * This is a no-op for because when a MessageClient is destroyed, 
     * an unsubcribe command is sent that is handled by the JMSAdapter, so no
     * further work is needed here.   
     * 
     * @param messageClient The MessageClient that was destroyed.
     */
    public void messageClientDestroyed(MessageClient messageClient)
    {
        // No-op
    }
    
    /**
     * Implements JMSMessageListener. 
     * When a JMSConsumer receives a JMS message, it dispatched a JMS message
     * event to pass the message to the JMS adapter. 
     */
    public void messageReceived(JMSMessageEvent evt)
    {
        JMSConsumer consumer = (JMSConsumer)evt.getSource();
        javax.jms.Message jmsMessage = evt.getJMSMessage();
        
        flex.messaging.messages.AsyncMessage flexMessage = convertToFlexMessage(jmsMessage, consumer);
        if (flexMessage != null)
        {
            MessagePerformanceUtils.markServerPostAdapterExternalTime(flexMessage);            
            ((MessageService)getDestination().getService()).serviceMessageFromAdapter(flexMessage, false);
        }
    } 
                    
    /**
     * Removes (unsubscribes) the specified consumer. By default, it removes
     * the durable subscription and pushes a generic error message to the client 
     * before MessageClient invalidation.
     *
     * @param clientId The identifier for the consumer to remove.
     */
    public void removeConsumer(String clientId)
    {
        // Client is unsubscribed because its corresponding JMS consumer has been removed from the JMS adapter.        
        MessageException messageEx = new MessageException();
        messageEx.setMessage(JMSConfigConstants.CLIENT_UNSUBSCRIBE_DUE_TO_CONSUMER_REMOVAL);                                                   
        removeConsumer(clientId, true, true, messageEx.createErrorMessage());
    }
        
    //--------------------------------------------------------------------------
    //
    // Protected and Private methods
    //             
    //--------------------------------------------------------------------------   

    /**
     * Removes (unsubscribes) the JMSConsumer associated with the clientId. 
     * 
     * @param clientId The clientId associated with the JMSConsumer to remove.
     * @param unsubscribe Whether to unsubscribe the durable subscription or not.
     * @param invalidate Whether to invalidate the MessageClient or not.
     * @param invalidateMessage A message to push to the client before consumer 
     * is removed and its MessageClient is invalidated. If the message is null, 
     * MessageClient is invalidated silently. 
     */
    protected void removeConsumer(String clientId, boolean unsubscribe, boolean invalidate, ErrorMessage invalidateMessage)
    {
        JMSConsumer consumer = null;                
        if (topicConsumers.containsKey(clientId))
            consumer = (JMSConsumer)topicConsumers.get(clientId);
        else if (queueConsumers.containsKey(clientId))
            consumer = (JMSConsumer)queueConsumers.get(clientId);
        
        removeConsumer(consumer, unsubscribe, invalidate, invalidateMessage);
    }
    
    /**
     * Removes (unsubscribes) the specified consumer. 
     * 
     * @param consumer The JMSConsumer instance to remove.
     * @param unsubscribe Whether to unsubscribe the durable subscription or not.
     * @param invalidate Whether to invalidate the MessageClient or not.
     * @param invalidateMessage A message to push to the client before consumer 
     * is removed and its MessageClient is invalidated. If the message is null, 
     * MessageClient is invalidated silently. 
     */
    protected void removeConsumer(JMSConsumer consumer, boolean unsubscribe, boolean invalidate, ErrorMessage invalidateMessage)
    {                      
        if (consumer == null)
            return;
        
        String clientId = (String)consumerToClientId.get(consumer);  
        if (clientId == null)
            return;
        
        if (Log.isInfo())
        {
            String logMessage = "JMS consumer for JMS destination '" + consumer.getDestinationJndiName() 
            + "' is being removed from the JMS adapter";

            if (invalidateMessage != null)
                logMessage += " due to the following error: " + invalidateMessage.faultString;

            Log.getLogger(JMSAdapter.LOG_CATEGORY).info(logMessage);
        }
        
        consumer.removeJMSExceptionListener(this);
        consumer.removeJMSMessageListener(this);
        consumer.stop(unsubscribe);            
        if (invalidate)
            invalidateMessageClient(consumer, invalidateMessage);
        if (consumer instanceof JMSTopicConsumer)
            topicConsumers.remove(clientId);
        else // assuming JMSQueueConsumer.
            queueConsumers.remove(clientId);  
        consumerToClientId.remove(consumer);
    }

    /**
     * Invoked automatically to allow the <code>JMSAdapter</code> to setup its corresponding
     * MBean control.
     * 
     * @param broker The <code>Destination</code> that manages this <code>JMSAdapter</code>.
     */
    protected void setupAdapterControl(Destination destination)
    {
        controller = new JMSAdapterControl(this, destination.getControl());        
        controller.register();
        setControl(controller);
    }
    
    /**
     * Builds a MessageReceiver for JMSConsumer from DeliverySettings. 
     * 
     * @return MessageReceiver configured for JMSConsumer per DeliverySettings.
     */
    private MessageReceiver buildMessageReceiver(JMSConsumer consumer)
    {       
        DeliverySettings deliverySettings = settings.getDeliverySettings();               
        if (deliverySettings.getMode().equals(JMSConfigConstants.ASYNC))
            return new AsyncMessageReceiver(consumer);        
        SyncMessageReceiver syncMessageReceiver = new SyncMessageReceiver(consumer); 
        syncMessageReceiver.setSyncReceiveIntervalMillis(deliverySettings.getSyncReceiveIntervalMillis());
        syncMessageReceiver.setSyncReceiveWaitMillis(deliverySettings.getSyncReceiveWaitMillis());   
        return syncMessageReceiver;
    }
    
    /**
     * Prefixes a clientId with DURABLE_SUBSCRIBER_NAME_PREFIX to build a 
     * subscription name to be used in JMSConsumers with durable connections. 
     */
    private String buildSubscriptionName(Object clientId)
    {
        return DURABLE_SUBSCRIBER_NAME_PREFIX + clientId.toString();   
    }
    
    /**
     * Convert from a <code>javax.jms.Message</code> type to a 
     * <code>flex.messaging.messages.AsyncMessage</code> type.
     * Supported types are <code>javax.jms.TextMessage</code> and 
     * <code>javax.jms.ObjectMessage</code>.
     */
    private flex.messaging.messages.AsyncMessage convertToFlexMessage(javax.jms.Message jmsMessage, JMSConsumer consumer)
    {
        flex.messaging.messages.AsyncMessage flexMessage = null;
        flexMessage = new flex.messaging.messages.AsyncMessage();
            
        String clientId = (String)consumerToClientId.get(consumer);
        if (clientId == null)
        {
            if (Log.isWarn())
                Log.getLogger(LOG_CATEGORY).warn("JMSAdapter encountered a null clientId during JMS to Flex message conversion");
            
            return null;
        }
        flexMessage.setClientId(clientId);
        
        
        flexMessage.setDestination(getDestination().getId());

        // Set JMSMessageID header as Flex messageId property.
        try
        {
            flexMessage.setMessageId(jmsMessage.getJMSMessageID());
        }
        catch (JMSException jmsEx)
        {
            if (Log.isWarn())
                Log.getLogger(LOG_CATEGORY).warn("JMSAdapter encountered an error while retrieving JMS message id during JMS to Flex message conversion: " + jmsEx.getMessage());              
        }

        // Set JMSTimestamp header as Flex timestamp property.
        try
        {
            flexMessage.setTimestamp(jmsMessage.getJMSTimestamp());
        }
        catch (JMSException jmsEx)
        {
            if (Log.isWarn())
                Log.getLogger(LOG_CATEGORY).warn("JMSAdapter encountered an error while retrieving JMS timestamp during JMS to Flex message conversion: " + jmsEx.getMessage());              
        }
        
        // Set JMS headers and Flex headers.
        if (settings.isPreserveJMSHeaders())
        {
            // Set standard JMS headers except JMSMessageId and JMSTimestamp, 
            // as they are already set on the Flex message directly.       
            try
            {
                flexMessage.setHeader(JMS_CORRELATION_ID, jmsMessage.getJMSCorrelationID());
                flexMessage.setHeader(JMS_DELIVERY_MODE, Integer.toString(jmsMessage.getJMSDeliveryMode()));
                flexMessage.setHeader(JMS_DESTINATION, jmsMessage.getJMSDestination().toString());
                flexMessage.setHeader(JMS_EXPIRATION, Long.toString(jmsMessage.getJMSExpiration()));
                flexMessage.setHeader(JMS_PRIORITY, Integer.toString(jmsMessage.getJMSPriority()));
                flexMessage.setHeader(JMS_REDELIVERED, Boolean.toString(jmsMessage.getJMSRedelivered()));
                flexMessage.setHeader(JMS_REPLY_TO, jmsMessage.getJMSReplyTo());
                flexMessage.setHeader(JMS_TYPE, jmsMessage.getJMSType());
            }
            catch (JMSException jmsEx)
            {
                // These should not cause errors to be pushed to Flash clients
                if (Log.isWarn())
                    Log.getLogger(LOG_CATEGORY).warn("JMSAdapter encountered an error while retrieving JMS headers during JMS to Flex conversion: " +  jmsEx.getMessage());                

            }
        }

        // Set JMS properties as Flex headers. 
        
        // While iterating through JMS message properties, build a message
        // performance object to send back to the client with the message 
        // properties starting with with MPI_HEADER_IN (if any).
        MessagePerformanceInfo mpi = null;

        try
        {
            for (Enumeration propEnum = jmsMessage.getPropertyNames(); propEnum.hasMoreElements();)
            {
                String propName = (String)propEnum.nextElement();
                try
                {
                    Object propValue = jmsMessage.getObjectProperty(propName);                
                    if (propName.startsWith(MessagePerformanceUtils.MPI_HEADER_IN))
                    {
                        if (mpi == null)
                            mpi = new MessagePerformanceInfo();                    
                        propName = propName.substring(MessagePerformanceUtils.MPI_HEADER_IN.length());                    
                        java.lang.reflect.Field field;
                        try
                        {
                            field = mpi.getClass().getField(propName);
                            field.set(mpi, propValue);                        
                        }
                        catch (Exception ignore)
                        {
                            // Simply don't set the property if the value cannot be retrieved.
                        }
                    }
                    else
                    {
                        flexMessage.setHeader(propName, propValue);                
                    }
                }
                catch (JMSException jmsEx)
                {
                    if (Log.isWarn())
                        Log.getLogger(LOG_CATEGORY).warn("JMSAdapter encountered an error while retrieving JMS properties during JMS to Flex conversion: " +  jmsEx.getMessage());                                
                }
            }

            if (mpi != null)
                flexMessage.setHeader(MessagePerformanceUtils.MPI_HEADER_IN, mpi);        
        }
        catch (JMSException jmsEx)
        {
            if (Log.isWarn())
                Log.getLogger(LOG_CATEGORY).warn("JMSAdapter encountered an error while retrieving JMS properties during JMS to Flex conversion: " +  jmsEx.getMessage());            
        }
        
        // Finally, set the JMS message body of the Flex message body.
        try
        {
            if (jmsMessage instanceof javax.jms.TextMessage)
            {
                javax.jms.TextMessage textMessage = (javax.jms.TextMessage)jmsMessage;
                flexMessage.setBody(textMessage.getText());
            }
            else if (jmsMessage instanceof javax.jms.ObjectMessage)
            {
                javax.jms.ObjectMessage objMessage = (javax.jms.ObjectMessage)jmsMessage;
                flexMessage.setBody(objMessage.getObject());
            }
        }
        catch (JMSException jmsEx)
        {
            if (Log.isWarn())
                Log.getLogger(LOG_CATEGORY).warn("JMSAdapter encountered an error while retrieving JMS message body during JMS to Flex conversion: " +  jmsEx.getMessage());                                            
        }

        return flexMessage;
    }
           
    /**
     * Invalidates the MessageClient associated with the JMSConsumer with the
     * supplied error message.
     * 
     * @param consumer The JMSConsumer whose MessageClient will be invalidated.
     * @param message The error message to push out before invalidating the 
     * MessageClient. If the message is null, MessageClient is invalidated 
     * silently. 
     */
    private void invalidateMessageClient(JMSConsumer consumer, flex.messaging.messages.Message message)
    {
        String clientId = (String)consumerToClientId.get(consumer);
        if (clientId != null && messageClients.containsKey(clientId))
        {
            MessageClient messageClient = (MessageClient)messageClients.get(clientId);

            if (Log.isInfo())
                Log.getLogger(JMSAdapter.LOG_CATEGORY).info("The corresponding MessageClient for JMS consumer for JMS destination '" 
                        + consumer.getDestinationJndiName() + "' is being invalidated");
            
            messageClient.invalidate(message);            
        }
    }
 
    /**
     *  Handle JMS specific configuration.
     */ 
    private void jms(ConfigMap properties)
    {
        ConfigMap jms = properties.getPropertyAsMap(JMS, null);
        if (jms != null)
        {
            String destType = jms.getPropertyAsString(DESTINATION_TYPE, defaultDestinationType);
            settings.setDestinationType(destType);

            String msgType = jms.getPropertyAsString(MESSAGE_TYPE, null);
            settings.setMessageType(msgType);

            String factory = jms.getPropertyAsString(CONNECTION_FACTORY, null);
            settings.setConnectionFactory(factory);

            ConfigMap connectionCredentials = jms.getPropertyAsMap(CONNECTION_CREDENTIALS, null);
            if (connectionCredentials != null)
            {
                String username = connectionCredentials.getPropertyAsString(USERNAME, null);
                settings.setConnectionUsername(username);                
                String password = connectionCredentials.getPropertyAsString(PASSWORD, null);                
                settings.setConnectionPassword(password);
            }
            
            ConfigMap deliverySettings = jms.getPropertyAsMap(DELIVERY_SETTINGS, null);
            if (deliverySettings != null)
            {
                // Get the default delivery settings.
                DeliverySettings ds = settings.getDeliverySettings();

                String mode = deliverySettings.getPropertyAsString(MODE, JMSConfigConstants.defaultMode);
                ds.setMode(mode);
                
                long receiveIntervalMillis = deliverySettings.getPropertyAsLong(SYNC_RECEIVE_INTERVAL_MILLIS, defaultSyncReceiveIntervalMillis);
                ds.setSyncReceiveIntervalMillis(receiveIntervalMillis);
                
                long receiveWaitMillis = deliverySettings.getPropertyAsLong(SYNC_RECEIVE_WAIT_MILLIS, defaultSyncReceiveWaitMillis);
                ds.setSyncReceiveWaitMillis(receiveWaitMillis);                
            }
            
            String destJNDI = jms.getPropertyAsString(DESTINATION_JNDI_NAME, null);
            settings.setDestinationJNDIName(destJNDI);

            String dest = jms.getPropertyAsString(DESTINATION_NAME, null);
            if (dest != null && Log.isWarn())
                Log.getLogger(LOG_CATEGORY).warn("The <destination-name> configuration option is deprecated and non-functional. Please remove this from your configuration file.");

            boolean durable = getDestination() instanceof MessageDestination ?
                ((MessageDestination) getDestination()).getServerSettings().isDurable() : false;
            settings.setDurableConsumers(durable);

            String deliveryMode = jms.getPropertyAsString(DELIVERY_MODE, null);
            settings.setDeliveryMode(deliveryMode);

            boolean preserveJMSHeaders = jms.getPropertyAsBoolean(PRESERVE_JMS_HEADERS, settings.isPreserveJMSHeaders());
            settings.setPreserveJMSHeaders(preserveJMSHeaders);
            
            String defPriority = jms.getPropertyAsString(MESSAGE_PRIORITY, null);
            if (defPriority != null && !defPriority.equalsIgnoreCase(DEFAULT_PRIORITY))
            {
                int priority = jms.getPropertyAsInt(MESSAGE_PRIORITY, settings.getMessagePriority());
                settings.setMessagePriority(priority);                    
            }
                        
            String ackMode = jms.getPropertyAsString(ACKNOWLEDGE_MODE, defaultAcknowledgeMode);
            settings.setAcknowledgeMode(ackMode);

            boolean transMode = jms.getPropertyAsBoolean(TRANSACTION_MODE, false);
            if (transMode && Log.isWarn())
                Log.getLogger(LOG_CATEGORY).warn("The <transacted-sessions> configuration option is deprecated and non-functional. Please remove this from your configuration file.");
              
            int maxProducers = jms.getPropertyAsInt(MAX_PRODUCERS, defaultMaxProducers);
            settings.setMaxProducers(maxProducers);

            // Retrieve any JNDI initial context environment properties.
            ConfigMap env = jms.getPropertyAsMap(INITIAL_CONTEXT_ENVIRONMENT, null);
            if (env != null)
            {
                List props = env.getPropertyAsList(PROPERTY, null);
                if (props != null)
                {
                    Class contextClass = Context.class;
                    Hashtable envProps = new Hashtable();
                    for (Iterator iter = props.iterator(); iter.hasNext();)
                    {
                        Object prop = iter.next();
                        if (prop instanceof ConfigMap)
                        {
                            ConfigMap pair = (ConfigMap)prop;
                            String name = pair.getProperty(NAME);
                            String value = pair.getProperty(VALUE);
                            if (name == null || value == null)
                            {
                                // A <property> element for the <initial-context-environment> settings for the ''{0}'' destination does not specify both <name> and <value> subelements.                                
                                MessageException messageEx = new MessageException();
                                messageEx.setMessage(MISSING_NAME_OR_VALUE, new Object[] {getDestination().getId()});
                                throw messageEx;
                            }
                            // If the name is a Context field, use the
                            // constant value rather than this literal name.
                            if (name.startsWith("Context."))
                            {
                                String fieldName = name.substring(name.indexOf(".") + 1);
                                java.lang.reflect.Field field = null;
                                try
                                {
                                    field = contextClass.getDeclaredField(fieldName);
                                }
                                catch (NoSuchFieldException nsfe)
                                {
                                    // A <property> element for the <initial-context-environment> settings for the ''{0}'' destination specifies an invalid javax.naming.Context field for its <name>: {1}
                                    MessageException messageEx = new MessageException();
                                    messageEx.setMessage(INVALID_CONTEXT_NAME, new Object[] {getDestination().getId(), fieldName});
                                    throw messageEx;
                                }
                                String fieldValue = null;
                                try
                                {
                                    fieldValue = (String)field.get(null);
                                }
                                catch (IllegalAccessException iae)
                                {
                                    // A <property> element for the <initial-context-environment> settings for the ''{0}'' destination specifies an inaccessible javax.naming.Context field for its <name>: {1}
                                    MessageException messageEx = new MessageException();
                                    messageEx.setMessage(INACCESIBLE_CONTEXT_NAME, new Object[] {getDestination().getId(), fieldName});
                                    throw messageEx;
                                }
                                envProps.put(fieldValue, value);
                            }
                            else
                            {
                                envProps.put(name, value);
                            }
                        }
                        else
                        {
                            // A <property> element for the <initial-context-environment> settings for the ''{0}'' destination does not specify both <name> and <value> subelements.                            
                            MessageException messageEx = new MessageException();
                            messageEx.setMessage(MISSING_NAME_OR_VALUE, new Object[] {getDestination().getId()});
                            throw messageEx;
                        }                        
                    }
                    settings.setInitialContextEnvironment(envProps);
                }
                else
                {
                    // The <initial-context-environment> settings for the ''{0}'' destination does not include any <property> subelements.                    
                    MessageException messageEx = new MessageException();
                    messageEx.setMessage(MISSING_PROPERTY_SUBELEMENT, new Object[] {getDestination().getId()});
                    throw messageEx;
                }
            }
        }
    }
    
    private void stopConsumers(Collection consumers)
    {
        Iterator itr = consumers.iterator();
        while (itr.hasNext())
        {
            JMSConsumer consumer = (JMSConsumer)itr.next();
            // Client is unsubscribed because its corresponding JMS consumer for JMS destination ''{0}'' has been stopped.
            MessageException me = new MessageException();
            me.setMessage(JMSConfigConstants.CLIENT_UNSUBSCRIBE_DUE_TO_CONSUMER_STOP, new Object[] {consumer.getDestinationJndiName()});
            consumer.stop(true);            
            invalidateMessageClient(consumer, me.createErrorMessage());             
        }
    }   
}
