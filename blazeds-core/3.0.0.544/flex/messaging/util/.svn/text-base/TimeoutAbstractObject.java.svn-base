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
package flex.messaging.util;

import edu.emory.mathcs.backport.java.util.concurrent.Future;

/**
 * This class defines the default implementation of TimeoutCapable,
 * providing the default behavior for an object that is capable of timing
 * out where that time out mechanism is managed by TimeoutManager.
 */
public abstract class TimeoutAbstractObject
	implements TimeoutCapable
{
    private long lastUse;
    private volatile boolean timeoutCanceled;
    private TimeoutManager timeoutManager;
    private Runnable timeoutTask;
    private Future timeoutFuture;
    private long timeoutPeriod;
    private final Object lock = new Object();
    
    public void cancelTimeout()
    {
        if (timeoutCanceled)
            return;
        
        boolean purged = false;
        if ((timeoutManager != null) && (timeoutTask != null) && (timeoutFuture != null))
            purged = timeoutManager.unscheduleTimeout(this);
        
        if (!purged && (timeoutFuture != null))
        {
            timeoutFuture.cancel(false);
        }
        
        timeoutCanceled = true;
    }
    
    public long getLastUse()
    {
        synchronized (lock)
        {
            return lastUse;
        }
    }
    
    public void setLastUse(long lastUse)
    {
        synchronized (lock)
        {
            this.lastUse = lastUse;
        }
    }
    
    public void updateLastUse()
    {
        synchronized (lock)
        {
            this.lastUse = System.currentTimeMillis();
        }
    }
    
    public TimeoutManager getTimeoutManager()
    {
        synchronized (lock)
        {
            return timeoutManager;
        }
    }
    
    public void setTimeoutManager(TimeoutManager timeoutManager)
    {
        synchronized (lock)
        {
            this.timeoutManager = timeoutManager;
        }
    }    
    
    public Runnable getTimeoutTask()
    {
        synchronized (lock)
        {
            return timeoutTask;
        }
    }
    
    public void setTimeoutTask(Runnable timeoutTask)
    {
        synchronized (lock)
        {
            this.timeoutTask = timeoutTask;
        }
    }
    
    public Future getTimeoutFuture()
    {
        synchronized (lock)
        {
            return timeoutFuture;
        }
    }
    
    public void setTimeoutFuture(Future timeoutFuture)
    {
        synchronized (lock)
        {
            this.timeoutFuture = timeoutFuture;
        }
    }
    
    public long getTimeoutPeriod()
    {
        synchronized (lock)
        {
            return timeoutPeriod;
        }
    }
    
    public void setTimeoutPeriod(long timeoutPeriod)
    {
        synchronized (lock)
        {
            this.timeoutPeriod = timeoutPeriod;
        }
    }
}
