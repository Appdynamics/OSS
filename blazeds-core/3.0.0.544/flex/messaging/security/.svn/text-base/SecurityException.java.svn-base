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
package flex.messaging.security;

import flex.messaging.MessageException;
import flex.messaging.messages.ErrorMessage;
import flex.messaging.messages.Message;
import flex.messaging.util.ResourceLoader;

/**
 * SecurityException is a localizable exception type that is used to represent
 * client authentication, client authorization and general server-related security
 * errors. It defines a set of supported error code values as constants suffixed
 * with _CODE.
 * 
 * @author Peter Farland
 * @author Seth Hodgson
 */
public class SecurityException extends MessageException
{
    static final long serialVersionUID = -3168212117963624230L;

    // Error code constants.
    public static final String CLIENT_AUTHENTICATION_CODE = "Client.Authentication";
    public static final String CLIENT_AUTHORIZATION_CODE = "Client.Authorization";
    public static final String SERVER_AUTHENTICATION_CODE = "Server.Authentication";
    public static final String SERVER_AUTHORIZATION_CODE = "Server.Authorization";
    
    private Message failingMessage;
    
    /**
     * Create a SecurityException that will use the default ResourceLoader
     * for error codes.
     */
    public SecurityException()
    {
        super();
    }
    
    /**
     * Create a SecurityException that will use the specified ResourceLoader
     * for error codes.
     */
    public SecurityException(ResourceLoader resourceLoader)
    {
        super(resourceLoader);
    }
    
    public Message getFailingMessage()
    {
        return failingMessage;
    }

    public void setFailingMessage(Message failingMessage)
    {
        this.failingMessage = failingMessage;
    }

    public ErrorMessage createErrorMessage()
    {
        ErrorMessage msg = super.createErrorMessage();
        if (failingMessage != null)
        {
            msg.setCorrelationId(failingMessage.getMessageId());
            msg.setDestination(failingMessage.getDestination());
        }
        return msg;
    }
    
    
    
}
