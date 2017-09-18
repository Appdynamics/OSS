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
 * @(#)MessageContext.java	1.6 05/08/29
 *
 * Copyright 1998-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package javax.mail;

/**
 * The context in which a piece of Message content is contained.  A
 * <code>MessageContext</code> object is returned by the
 * <code>getMessageContext</code> method of the
 * <code>MessageAware</code> interface.  <code>MessageAware</code> is
 * typically implemented by <code>DataSources</code> to allow a
 * <code>DataContentHandler</code> to pass on information about the
 * context in which a data content object is operating.
 *
 * @see javax.mail.MessageAware
 * @see javax.activation.DataSource
 * @see javax.activation.DataContentHandler
 * @since	JavaMail 1.1
 */
public class MessageContext {
    private Part part;

    /**
     * Create a MessageContext object describing the context of the given Part.
     */
    public MessageContext(Part part) {
	this.part = part;
    }

    /**
     * Return the Part that contains the content.
     *
     * @return	the containing Part, or null if not known
     */
    public Part getPart() {
	return part;
    }

    /**
     * Return the Message that contains the content.
     * Follows the parent chain up through containing Multipart
     * objects until it comes to a Message object, or null.
     *
     * @return	the containing Message, or null if not known
     */
    public Message getMessage() {
	try {
	    return getMessage(part);
	} catch (MessagingException ex) {
	    return null;
	}
    }

    /**
     * Return the Message containing an arbitrary Part.
     * Follows the parent chain up through containing Multipart
     * objects until it comes to a Message object, or null.
     *
     * @return	the containing Message, or null if none
     * @see javax.mail.BodyPart#getParent
     * @see javax.mail.Multipart#getParent
     */
    private static Message getMessage(Part p) throws MessagingException {
	while (p != null) {
	    if (p instanceof Message)
		return (Message)p;
	    BodyPart bp = (BodyPart)p;
	    Multipart mp = bp.getParent();
	    if (mp == null)	// MimeBodyPart might not be in a MimeMultipart
		return null;
	    p = mp.getParent();
	}
	return null;
    }

    /**
     * Return the Session we're operating in.
     *
     * @return	the Session, or null if not known
     */
    public Session getSession() {
	Message msg = getMessage();
	return msg != null ? msg.session : null;
    }
}
