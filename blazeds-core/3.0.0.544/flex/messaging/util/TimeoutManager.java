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

import flex.messaging.log.Log;
import flex.messaging.log.LogCategories;

import edu.emory.mathcs.backport.java.util.concurrent.Future;
import edu.emory.mathcs.backport.java.util.concurrent.ScheduledThreadPoolExecutor;
import edu.emory.mathcs.backport.java.util.concurrent.ThreadFactory;
import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;

/**
 * This class provides a means of managing TimeoutCapable objects. It leverages 
 * facilities in the the Java concurrency package to provide a common utility 
 * for scheduling timeout Futures and managing the underlying worker thread pools.
 * 
 * @author neville
 */
public class TimeoutManager
{
    private static final String LOG_CATEGORY = LogCategories.TIMEOUT;
    
    private ScheduledThreadPoolExecutor timeoutService;
    
    public TimeoutManager()
    {
        this(null);
    }
    
    public TimeoutManager(ThreadFactory tf)
    {
        if (tf == null)
        {
            tf = new MonitorThreadFactory();
        }
        timeoutService = new ScheduledThreadPoolExecutor(1, tf);
    }
    
    public Future scheduleTimeout(TimeoutCapable t)
    {
        Future future = null;
        if (t.getTimeoutPeriod() > 0)
        {
            Runnable timeoutTask = new TimeoutTask(t);
	        future = timeoutService.schedule(timeoutTask, t.getTimeoutPeriod(), TimeUnit.MILLISECONDS);
	        t.setTimeoutFuture(future);
            if (t instanceof TimeoutAbstractObject)
            {
                TimeoutAbstractObject timeoutAbstract = (TimeoutAbstractObject)t;
                timeoutAbstract.setTimeoutManager(this);
                timeoutAbstract.setTimeoutTask(timeoutTask);
            } 
            if (Log.isDebug())
                Log.getLogger(LOG_CATEGORY).debug("TimeoutManager '" + System.identityHashCode(this) + "' has scheduled instance '" + 
                    System.identityHashCode(t) + "' of type '" + t.getClass().getName() + "' to be timed out in " + t.getTimeoutPeriod() + " milliseconds. Task queue size: "+ timeoutService.getQueue().size());            
        }
        return future;
    }
    
    public boolean unscheduleTimeout(TimeoutAbstractObject timeoutAbstract)
    {
        if (timeoutService.remove(timeoutAbstract.getTimeoutTask()))
        {
            if (Log.isDebug())
                Log.getLogger(LOG_CATEGORY).debug("TimeoutManager '" + System.identityHashCode(this) + "' has removed the timeout task for instance '" + 
                    System.identityHashCode(timeoutAbstract) + "' of type '" + timeoutAbstract.getClass().getName() + "' that has requested its timeout be cancelled. Task queue size: "+ timeoutService.getQueue().size());            
        }
        else
        {
            Future timeoutFuture = timeoutAbstract.getTimeoutFuture();
            timeoutFuture.cancel(false); // Don't interrupt it if it's running.
            if (Log.isDebug())
                Log.getLogger(LOG_CATEGORY).debug("TimeoutManager '" + System.identityHashCode(this) + "' cancelling timeout task for instance '" + 
                    System.identityHashCode(timeoutAbstract) + "' of type '" + timeoutAbstract.getClass().getName() + "' that has requested its timeout be cancelled. Task queue size: "+ timeoutService.getQueue().size());              
            if (timeoutFuture.isDone())
            {
                timeoutService.purge(); // Force the service to give up refs to task immediately rather than hanging on to them.
                if (Log.isDebug())
                    Log.getLogger(LOG_CATEGORY).debug("TimeoutManager '" + System.identityHashCode(this) + "' purged queue of any cancelled or completed tasks. Task queue size: "+ timeoutService.getQueue().size());
            }                        
        }
        return true;
    }
        
    public void shutdown()
    {
        timeoutService.shutdown();
    }
    
    class MonitorThreadFactory implements ThreadFactory
    {
        public Thread newThread(Runnable r)
        {
            Thread t = new Thread(r);
            t.setDaemon(true);
            t.setName("TimeoutManager");
            return t;
        }
    }
    
    class TimeoutTask implements Runnable
    {
        private TimeoutCapable timeoutObject;
        
        public TimeoutTask(TimeoutCapable timeoutObject)
        {
            this.timeoutObject = timeoutObject;
        }
        
        public void run()
        {
            long inactiveMillis = System.currentTimeMillis() - timeoutObject.getLastUse();
            if (inactiveMillis >= timeoutObject.getTimeoutPeriod())
        	{
                timeoutObject.timeout();
                if (Log.isDebug())
                    Log.getLogger(LOG_CATEGORY).debug("TimeoutManager '" + System.identityHashCode(TimeoutManager.this) + "' has run the timeout task for instance '" + 
                        System.identityHashCode(timeoutObject) + "' of type '" + timeoutObject.getClass().getName() + "'. Task queue size: "+ timeoutService.getQueue().size()); 
        	}
        	else
        	{
        	    // Reschedule timeout and store new Future for cancellation.
                timeoutObject.setTimeoutFuture(timeoutService.schedule(this, (timeoutObject.getTimeoutPeriod()-inactiveMillis), TimeUnit.MILLISECONDS));
                if (Log.isDebug())
                    Log.getLogger(LOG_CATEGORY).debug("TimeoutManager '" + System.identityHashCode(TimeoutManager.this) + "' has rescheduled a timeout for the active instance '" + 
                        System.identityHashCode(timeoutObject) + "' of type '" + timeoutObject.getClass().getName() + "'. Task queue size: "+ timeoutService.getQueue().size());
        	}
        }        
    }
}
