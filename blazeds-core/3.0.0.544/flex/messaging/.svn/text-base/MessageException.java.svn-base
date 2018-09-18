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
package flex.messaging;

import flex.messaging.messages.ErrorMessage;
import flex.messaging.util.ResourceLoader;

import java.util.Map;

public class MessageException extends LocalizedException
{
    static final long serialVersionUID = 3310842114461162689L;

    protected String code;
    protected Map extendedData;
    protected ErrorMessage errorMessage;

    /**
     * Default constructor. 
     */
    public MessageException()
    {
    }

    /**
     * Constructor with <code>ResourceLoader</code>. 
     * 
     * @param loader
     */
    public MessageException(ResourceLoader loader)
    {
        super(loader);
    }
    
    /**
     * Constructor with a message.
     * 
     * @param message
     */
    public MessageException(String message)
    {
        setMessage(message);
    }

    /**
     * Constructor with message and <code>Throwable</code>.
     * 
     * @param message
     * @param t
     */
    public MessageException(String message, Throwable t)
    {
        setMessage(message);
        setRootCause(t);
    }

    /**
     * Constructor with <code>Throwable</code>. 
     * 
     * @param t
     */
    public MessageException(Throwable t)
    {
        String rootMessage = t.getMessage();
        if (rootMessage == null)
            rootMessage = t.toString();
        setMessage(rootMessage);
        setRootCause(t);
    }

    /**
     * Returns the code of the exception. 
     * 
     * @return Code of the exception.
     */
    public String getCode()
    {
        return code;
    }

    /**
     * Sets the code of the exception.
     * 
     * @param code Code of the exception. 
     */
    public void setCode(String code)
    {
        this.code = code;
    }

    /**
     * Returns the extended data of the exception.
     * 
     * @return The extended data of the exception. 
     */
    public Map getExtendedData()
    {
        return extendedData;
    }

    /**
     * Sets the extended data of the exception. 
     * 
     * @param extendedData The extended data of the exception.
     */
    public void setExtendedData(Map extendedData)
    {
        this.extendedData = extendedData;
    }

    /**
     * Returns the error message of the exception. 
     * 
     * @return The error message of the exception.
     */
    public ErrorMessage getErrorMessage()
    {
        if (errorMessage == null)
        {
            errorMessage = createErrorMessage();
        }
        return errorMessage;
    }

    /**
     * Sets the error message of the exception.
     * 
     * @param errorMessage The error message of the exception.
     */
    public void setErrorMessage(ErrorMessage errorMessage)
    {
        this.errorMessage = errorMessage;
    }

    /** @exclude **/
    public Object getRootCauseErrorMessage()
    {
        // FIXME: serialize number field.
        if (rootCause != null)
        {
            if (rootCause instanceof MessageException)
            {
                return ((MessageException)rootCause).createErrorMessage();
            }
            else
            {
                return rootCause;
            }
        }

        return null;
    }

    /**
     * Creates an error message from the exception. 
     * 
     * @return The error message. 
     */
    public ErrorMessage createErrorMessage()
    {
        ErrorMessage msg = new ErrorMessage();
        if (code == null)
        {
            msg.faultCode = "Server.Processing";
        }
        else
        {
            msg.faultCode = code;
        }
        msg.faultString = message;
        msg.faultDetail = details;
        msg.rootCause = getRootCauseErrorMessage();
        if (extendedData != null)
        {
            msg.extendedData = extendedData;
        }
        return msg;
    }
    
    protected ResourceLoader getResourceLoader()
    {
        if (resourceLoader == null)
            resourceLoader = MessageBroker.getSystemSettings().getResourceLoader();
        
        return resourceLoader;
    }
}
