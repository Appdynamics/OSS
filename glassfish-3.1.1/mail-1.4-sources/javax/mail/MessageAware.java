/*
 * The contents of this file are subject to the terms 
 * of the Common Development and Distribution License 
 * (the "License").  You may not use this file except 
 * in compliance with the License.
 * 
 * You can obtain a copy of the license at 
 * glassfish/bootstrap/legal/CDDLv1.0.txt or 
 * https://glassfish.dev.java.net/public/CDDLv1.0.html. 
 * See the License for the specific language governing 
 * permissions and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL 
 * HEADER in each file and include the License file at 
 * glassfish/bootstrap/legal/CDDLv1.0.txt.  If applicable, 
 * add the following below this CDDL HEADER, with the 
 * fields enclosed by brackets "[]" replaced with your 
 * own identifying information: Portions Copyright [yyyy] 
 * [name of copyright owner]
 */

/*
 * @(#)MessageAware.java	1.6 05/08/29
 *
 * Copyright 1998-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package javax.mail;

/**
 * An interface optionally implemented by <code>DataSources</code> to
 * supply information to a <code>DataContentHandler</code> about the
 * message context in which the data content object is operating.
 *
 * @see javax.mail.MessageContext
 * @see javax.activation.DataSource
 * @see javax.activation.DataContentHandler
 * @since	JavaMail 1.1
 */
public interface MessageAware {
    /**
     * Return the message context.
     */
    public MessageContext getMessageContext();
}
