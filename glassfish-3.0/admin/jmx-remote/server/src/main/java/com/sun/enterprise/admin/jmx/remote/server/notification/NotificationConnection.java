/*
 * The contents of this file are subject to the terms 
 * of the Common Development and Distribution License 
 * (the License).  You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the license at 
 * https://glassfish.dev.java.net/public/CDDLv1.0.html or
 * glassfish/bootstrap/legal/CDDLv1.0.txt.
 * See the License for the specific language governing 
 * permissions and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL 
 * Header Notice in each file and include the License file 
 * at glassfish/bootstrap/legal/CDDLv1.0.txt.  
 * If applicable, add the following below the CDDL Header, 
 * with the fields enclosed by brackets [] replaced by
 * you own identifying information: 
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * Copyright 2006 Sun Microsystems, Inc. All rights reserved.
 */

package com.sun.enterprise.admin.jmx.remote.server.notification;

import java.io.*;
import java.nio.channels.ClosedChannelException;
import java.net.SocketException;
import java.rmi.ConnectException;
import java.rmi.ConnectIOException;
import java.util.logging.Logger;

import javax.management.*;

import com.sun.enterprise.admin.jmx.remote.DefaultConfiguration;
import com.sun.enterprise.admin.jmx.remote.notification.SimpleQueue;
import com.sun.enterprise.admin.jmx.remote.notification.NotificationWrapper;

/**
 * A Connection class that represents a single client connection.
 * This class maintains a notification buffer that is used to buffer notifications
 * that needs to be sent to the client that is being represented by an object of this class.
 * A dispatch thread is started, to dispatch the buffered notifications to the client.
 */
public class NotificationConnection implements Runnable {
    private SimpleQueue que = new SimpleQueue();
    private int bufsiz = 0;
    private Thread dispatchThr = null;
    private long lastNotifTime = 0;

    private OutputStream out = null;

    private boolean exiting = false;
    private boolean dispatching = false;
    private boolean isIOException = false;

    private static final Logger logger = Logger.getLogger(
        DefaultConfiguration.JMXCONNECTOR_LOGGER);/*, 
        DefaultConfiguration.LOGGER_RESOURCE_BUNDLE_NAME );*/

    public NotificationConnection(OutputStream out, int bufsiz) {
        this.out = out;
        if (bufsiz <= DefaultConfiguration.NOTIF_MIN_BUFSIZ)
            this.bufsiz = DefaultConfiguration.NOTIF_MAX_BUFSIZ;
        else
            this.bufsiz = bufsiz;
        dispatchThr = new Thread(this);
        dispatchThr.start();
    }

    /**
     * Reinitialized this connection and restarts the dispatch thread.
     * This method is called everytime the client reconnects to the server
     * because the connection had dropped.
     */
    public void reinit(OutputStream out) {
        this.out = out;
        isIOException = false;
        dispatchThr = new Thread(this);
        dispatchThr.start();
    }

    /**
     * Returns true if an IOException had occurred while dispatching a notification
     * to the client.
     * If true, then the connection to the client may have dropped.
     * If true, the ServerNotificationManager suspends this connection, waiting for
     * the client to reconnect.
     */
    public boolean hasIOExceptionOccurred() {
        return isIOException;
    }

    private boolean isIdle() {
        boolean ret = ((System.currentTimeMillis() - lastNotifTime) >= DefaultConfiguration.NOTIF_WAIT_INTERVAL);
        return ret;
    }
    
    /**
     * Sends an empty notification to the client.
     * An empty notification is sent every 10 seconds.
     */
    public void fireWaitNotif() {
        if (!hasIOExceptionOccurred() && (que.size() < bufsiz) && !dispatching && isIdle()) {
            synchronized (que) {
                que.add(new NotificationWrapper(NotificationWrapper.WAIT, null, null));
                que.notify();
            }
        }
    }

    /**
     * Called by the ServerNotificationManager whenever a notification needs to be
     * sent to the client.
     */
    public void fireNotification(NotificationWrapper wrapr) {
        if (que.size() < bufsiz) {
            synchronized (que) {
                que.add(wrapr);
                que.notify();
            }
        }
    }

    /**
     * When the server-side connector webapp is shutdown by the servlet container,
     * the ServerNotificationManager calls this method.
     * All pending notifications are dropped.
     */
    public void close() {
        exiting = true;
        synchronized (que) {
            que.notify();
        }
        try {
            dispatchThr.join();
        } catch (InterruptedException intre) {
        }
        try {
            out.close();
        } catch (IOException ioe) {
            // XXX: Log it
        }
    }

    public boolean isExiting() {
        return exiting;
    }

    /**
     * The notifications dispatch thread.
     * The dispatch thread sends all pending notifications in the buffer to the client.
     * The dispatch thread exits, whenever an IOException occurs during actual dispatch
     * or whenever this connection is being closed (after a call to close())
     */
    public void run() {
        /* XXX: Even when we are exiting should we send the remaining notifications?
         *      OR just drop the remaining notifications ?
         *
         *     Currently we drop all the remaining notifications!!
         */
        while (!isExiting() && !hasIOExceptionOccurred()) {
            synchronized (que) {
                while (que.isEmpty() && !isExiting() && !hasIOExceptionOccurred()) {
                    try {
                        que.wait();
                    } catch (InterruptedException intre) {
                    }
                }
            }
            if (isExiting() || hasIOExceptionOccurred())
                break;
            dispatching = true;
            while (!que.isEmpty() && !isExiting() && !hasIOExceptionOccurred()) {
                NotificationWrapper wrapr = (NotificationWrapper) que.remove();
                try {
                    sendNotificationMsg(wrapr);
                } catch (IOException ioe) {
                    if (isExiting())
                        break;
                    // XXX: Log it; drop the notification
                    if (!isDisconnected(ioe))
                        break;
                    isIOException = true;
                    synchronized (this) {
                        this.notify();
                    }
                    break;
                }
            }
            lastNotifTime = System.currentTimeMillis();
            dispatching = false;
        }
    }

    private boolean isDisconnected(IOException ioe) {
        if (ioe instanceof ClosedChannelException ||
            ioe instanceof SocketException ||
            ioe instanceof ConnectException ||
            ioe instanceof ConnectIOException)
            return true;
        return false;
    }

    private void sendNotificationMsg(NotificationWrapper wrapr) throws IOException {
        ObjectOutputStream objout = new ObjectOutputStream(out);
        objout.writeObject(wrapr);
        objout.flush();
    }
}

