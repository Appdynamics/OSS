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
 * @(#)Transport.java	1.38 05/08/29
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package javax.mail;

import java.io.IOException;
import java.net.*;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import javax.mail.event.*;

/**
 * An abstract class that models a message transport.
 * Subclasses provide actual implementations. <p>
 *
 * Note that <code>Transport</code> extends the <code>Service</code>
 * class, which provides many common methods for naming transports,
 * connecting to transports, and listening to connection events.
 *
 * @author John Mani
 * @author Max Spivak
 * @author Bill Shannon
 * @version 1.38, 05/08/29
 * 
 * @see javax.mail.Service
 * @see javax.mail.event.ConnectionEvent
 * @see javax.mail.event.TransportEvent
 */

public abstract class Transport extends Service {

    /**
     * Constructor.
     *
     * @param	session Session object for this Transport.
     * @param	urlname	URLName object to be used for this Transport
     */
    public Transport(Session session, URLName urlname) {
	super(session, urlname);
    }

    /**
     * Send a message.  The message will be sent to all recipient
     * addresses specified in the message (as returned from the
     * <code>Message</code> method <code>getAllRecipients</code>),
     * using message transports appropriate to each address.  The
     * <code>send</code> method calls the <code>saveChanges</code>
     * method on the message before sending it. <p>
     *
     * If any of the recipient addresses is detected to be invalid by
     * the Transport during message submission, a SendFailedException
     * is thrown. Clients can get more detail about the failure by examining
     * the exception. Whether or not the message is still sent succesfully to
     * any valid addresses depends on the Transport implementation. See 
     * SendFailedException for more details. Note also that success does 
     * not imply that the message was delivered to the ultimate recipient,
     * as failures may occur in later stages of delivery.  Once a Transport 
     * accepts a message for delivery to a recipient, failures that occur later
     * should be reported to the user via another mechanism, such as
     * returning the undeliverable message. <p>
     *
     * @param	msg	the message to send
     * @exception	SendFailedException if the message could not
     *			be sent to some or any of the recipients.
     * @exception	MessagingException
     * @see		Message#saveChanges
     * @see		Message#getAllRecipients
     * @see		#send(Message, Address[])
     * @see		javax.mail.SendFailedException
     */
    public static void send(Message msg) throws MessagingException {
	msg.saveChanges(); // do this first
	send0(msg, msg.getAllRecipients());
    }

    /**
     * Send the message to the specified addresses, ignoring any
     * recipients specified in the message itself. The
     * <code>send</code> method calls the <code>saveChanges</code>
     * method on the message before sending it. <p>
     *
     * @param	msg	the message to send
     * @param	addresses the addresses to which to send the message
     * @exception	SendFailedException if the message could not
     *			be sent to some or any of the recipients.
     * @exception	MessagingException
     * @see		Message#saveChanges
     * @see             #send(Message)
     * @see		javax.mail.SendFailedException
     */
    public static void send(Message msg, Address[] addresses) 
		throws MessagingException {

	msg.saveChanges();
	send0(msg, addresses);
    }

    // send, but without the saveChanges
    private static void send0(Message msg, Address[] addresses) 
		throws MessagingException {

	if (addresses == null || addresses.length == 0)
	    throw new SendFailedException("No recipient addresses");

	/*
	 * protocols is a hashtable containing the addresses
	 * indexed by address type
	 */
	Hashtable protocols = new Hashtable();

	// Vectors of addresses
	Vector invalid = new Vector();
	Vector validSent = new Vector();
	Vector validUnsent = new Vector();

	for (int i = 0; i < addresses.length; i++) {
	    // is this address type already in the hashtable?
	    if (protocols.containsKey(addresses[i].getType())) {
		Vector v = (Vector)protocols.get(addresses[i].getType());
		v.addElement(addresses[i]);
	    } else {
		// need to add a new protocol
		Vector w = new Vector();
		w.addElement(addresses[i]);
		protocols.put(addresses[i].getType(), w);
	    }
	}

	int dsize = protocols.size();
	if (dsize == 0)
	    throw new SendFailedException("No recipient addresses");

	Session s = (msg.session != null) ? msg.session :
		     Session.getDefaultInstance(System.getProperties(), null);
	Transport transport;

	/*
	 * Optimize the case of a single protocol.
	 */
	if (dsize == 1) {
	    transport = s.getTransport(addresses[0]);
	    try {
		transport.connect();
		transport.sendMessage(msg, addresses);
	    } finally {
		transport.close();
	    }
	    return;
	}

	/*
	 * More than one protocol.  Have to do them one at a time
	 * and collect addresses and chain exceptions.
	 */
	MessagingException chainedEx = null;
	boolean sendFailed = false;

	Enumeration e = protocols.elements();
	while (e.hasMoreElements()) {
	    Vector v = (Vector)e.nextElement();
	    Address[] protaddresses = new Address[v.size()];
	    v.copyInto(protaddresses);

	    // Get a Transport that can handle this address type.
	    if ((transport = s.getTransport(protaddresses[0])) == null) {
		// Could not find an appropriate Transport ..
		// Mark these addresses invalid.
		for (int j = 0; j < protaddresses.length; j++)
		    invalid.addElement(protaddresses[j]);
		continue;
	    }
	    try {
		transport.connect();
		transport.sendMessage(msg, protaddresses);
	    } catch (SendFailedException sex) {
		sendFailed = true;
		// chain the exception we're catching to any previous ones
		if (chainedEx == null)
		    chainedEx = sex;
		else
		    chainedEx.setNextException(sex);

		// retrieve invalid addresses
		Address[] a = sex.getInvalidAddresses();
		if (a != null)
		    for (int j = 0; j < a.length; j++) 
			invalid.addElement(a[j]);

		// retrieve validSent addresses
		a = sex.getValidSentAddresses();
		if (a != null)
		    for (int k = 0; k < a.length; k++) 
			validSent.addElement(a[k]);

		// retrieve validUnsent addresses
		Address[] c = sex.getValidUnsentAddresses();
		if (c != null)
		    for (int l = 0; l < c.length; l++) 
			validUnsent.addElement(c[l]);
	    } catch (MessagingException mex) {
		sendFailed = true;
		// chain the exception we're catching to any previous ones
		if (chainedEx == null)
		    chainedEx = mex;
		else
		    chainedEx.setNextException(mex);
	    } finally {
		transport.close();
	    }
	}

	// done with all protocols. throw exception if something failed
	if (sendFailed || invalid.size() != 0 || validUnsent.size() != 0) { 
	    Address[] a = null, b = null, c = null;

	    // copy address vectors into arrays
	    if (validSent.size() > 0) {
		a = new Address[validSent.size()];
		validSent.copyInto(a);
	    }
	    if (validUnsent.size() > 0) {
		b = new Address[validUnsent.size()];
		validUnsent.copyInto(b);
	    }
	    if (invalid.size() > 0) {
		c = new Address[invalid.size()];
		invalid.copyInto(c);
	    }
	    throw new SendFailedException("Sending failed", chainedEx, 
					  a, b, c);
	}
    }

    /**
     * Send the Message to the specified list of addresses. An appropriate
     * TransportEvent indicating the delivery status is delivered to any 
     * TransportListener registered on this Transport. Also, if any of
     * the addresses is invalid, a SendFailedException is thrown.
     * Whether or not the message is still sent succesfully to
     * any valid addresses depends on the Transport implementation. <p>
     *
     * Unlike the static <code>send</code> method, the <code>sendMessage</code>
     * method does <em>not</em> call the <code>saveChanges</code> method on
     * the message; the caller should do so.
     *
     * @param msg	The Message to be sent
     * @param addresses	array of addresses to send this message to
     * @see 		javax.mail.event.TransportEvent
     * @exception SendFailedException if the send failed because of
     *			invalid addresses.
     * @exception MessagingException if the connection is dead or not in the 
     * 				connected state
     */
    public abstract void sendMessage(Message msg, Address[] addresses) 
				throws MessagingException;

    // Vector of Transport listeners
    private Vector transportListeners = null;

    /**
     * Add a listener for Transport events. <p>
     *
     * The default implementation provided here adds this listener
     * to an internal list of TransportListeners.
     *
     * @param l         the Listener for Transport events
     * @see             javax.mail.event.TransportEvent
     */
    public synchronized void addTransportListener(TransportListener l) {
	if (transportListeners == null)
	    transportListeners = new Vector();
	transportListeners.addElement(l);
    }

    /**
     * Remove a listener for Transport events. <p>
     *
     * The default implementation provided here removes this listener
     * from the internal list of TransportListeners.
     *
     * @param l         the listener
     * @see             #addTransportListener
     */
    public synchronized void removeTransportListener(TransportListener l) {
	if (transportListeners != null)
	    transportListeners.removeElement(l);
    }

    /**
     * Notify all TransportListeners. Transport implementations are
     * expected to use this method to broadcast TransportEvents.<p>
     *
     * The provided default implementation queues the event into
     * an internal event queue. An event dispatcher thread dequeues
     * events from the queue and dispatches them to the registered
     * TransportListeners. Note that the event dispatching occurs
     * in a separate thread, thus avoiding potential deadlock problems.
     */
    protected void notifyTransportListeners(int type, Address[] validSent,
					    Address[] validUnsent,
					    Address[] invalid, Message msg) {
	if (transportListeners == null)
	    return;
	
	TransportEvent e = new TransportEvent(this, type, validSent, 
					      validUnsent, invalid, msg);
	queueEvent(e, transportListeners);
    }
}
