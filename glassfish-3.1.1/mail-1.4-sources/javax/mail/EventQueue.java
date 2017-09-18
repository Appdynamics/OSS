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
 * @(#)EventQueue.java	1.8 05/11/22
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package javax.mail;

import java.io.*;
import java.util.Vector;
import javax.mail.event.MailEvent;

/**
 * Package private class used by Store & Folder to dispatch events.
 * This class implements an event queue, and a dispatcher thread that
 * dequeues and dispatches events from the queue.
 *
 * Pieces stolen from sun.misc.Queue.
 *
 * @author	Bill Shannon
 */
class EventQueue implements Runnable {

    class QueueElement {
	QueueElement next = null;
	QueueElement prev = null;
	MailEvent event = null;
	Vector vector = null;

	QueueElement(MailEvent event, Vector vector) {
	    this.event = event;
	    this.vector = vector;
	}
    }

    private QueueElement head = null;
    private QueueElement tail = null;
    private Thread qThread;

    public EventQueue() {
	qThread = new Thread(this, "JavaMail-EventQueue");
	qThread.setDaemon(true);  // not a user thread
	qThread.start();
    }

    /**
     * Enqueue an event.
     */
    public synchronized void enqueue(MailEvent event, Vector vector) {
	QueueElement newElt = new QueueElement(event, vector);

	if (head == null) {
	    head = newElt;
	    tail = newElt;
	} else {
	    newElt.next = head;
	    head.prev = newElt;
	    head = newElt;
	}
	notify();
    }

    /**
     * Dequeue the oldest object on the queue.
     * Used only by the run() method.
     *
     * @return    the oldest object on the queue.
     * @exception java.lang.InterruptedException if another thread has
     *              interrupted this thread.
     */
    private synchronized QueueElement dequeue()
				throws InterruptedException {
	while (tail == null)
	    wait();
	QueueElement elt = tail;
	tail = elt.prev;
	if (tail == null) {
	    head = null;
	} else {
	    tail.next = null;
	}
	elt.prev = elt.next = null;
	return elt;
    }

    /**
     * Pull events off the queue and dispatch them.
     */
    public void run() {
	QueueElement qe;

	try {
	    loop:
	    while ((qe = dequeue()) != null) {
		MailEvent e = qe.event;
		Vector v = qe.vector;

		for (int i = 0; i < v.size(); i++)
		    try {
			e.dispatch(v.elementAt(i));
		    } catch (Throwable t) {
			if (t instanceof InterruptedException)
			    break loop;
			// ignore anything else thrown by the listener
		    }

		qe = null; e = null; v = null;
	    }
	} catch (InterruptedException e) {
	    // just die
	}
    }

    /**
     * Stop the dispatcher so we can be destroyed.
     */
    void stop() {
	if (qThread != null) {
	    qThread.interrupt();	// kill our thread
	    qThread = null;
	}
    }
}
