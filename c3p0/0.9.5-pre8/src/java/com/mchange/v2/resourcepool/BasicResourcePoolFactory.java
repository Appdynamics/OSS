/*
 * Distributed as part of c3p0 0.9.5-pre8
 *
 * Copyright (C) 2014 Machinery For Change, Inc.
 *
 * Author: Steve Waldman <swaldman@mchange.com>
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of EITHER:
 *
 *     1) The GNU Lesser General Public License (LGPL), version 2.1, as 
 *        published by the Free Software Foundation
 *
 * OR
 *
 *     2) The Eclipse Public License (EPL), version 1.0
 *
 * You may choose which license to accept if you wish to redistribute
 * or modify this work. You may offer derivatives of this work
 * under the license you have chosen, or you may provide the same
 * choice of license which you have been offered here.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * You should have received copies of both LGPL v2.1 and EPL v1.0
 * along with this software; see the files LICENSE-EPL and LICENSE-LGPL.
 * If not, the text of these licenses are currently available at
 *
 * LGPL v2.1: http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 *  EPL v1.0: http://www.eclipse.org/org/documents/epl-v10.php 
 * 
 */

package com.mchange.v2.resourcepool;

import java.util.*;
import com.mchange.v2.async.*;

public class BasicResourcePoolFactory extends ResourcePoolFactory
{
    public static BasicResourcePoolFactory createNoEventSupportInstance( int num_task_threads )
    { return createNoEventSupportInstance( null, null, num_task_threads ); }

    public static BasicResourcePoolFactory createNoEventSupportInstance( AsynchronousRunner taskRunner, 
									 Timer timer )
    { return createNoEventSupportInstance( taskRunner, timer, ResourcePoolFactory.DEFAULT_NUM_TASK_THREADS ); }


    private static BasicResourcePoolFactory createNoEventSupportInstance( AsynchronousRunner taskRunner, 
									  Timer timer,
									  int default_num_task_threads )
    {
	return new BasicResourcePoolFactory( taskRunner, 
					     timer,
					     default_num_task_threads,
					     true );
    }

    int     start                         = -1;   //default to min
    int     min                           = 1;
    int     max                           = 12;
    int     inc                           = 3;
    int     retry_attempts                = -1;   //by default, retry acquisitions forever
    int     retry_delay                   = 1000; //1 second
    long    idle_resource_test_period     = -1;   //milliseconds, by default we don't test idle resources
    long    max_age                       = -1;   //milliseconds, by default resources never expire
    long    max_idle_time                 = -1;   //milliseconds, by default resources never expire
    long    excess_max_idle_time          = -1;   //milliseconds, by default resources never expire
    long    destroy_overdue_resc_time     = -1;   //milliseconds
    long    expiration_enforcement_delay  = -1;   //automatic, we come up with a reasonable default based on time params

    boolean break_on_acquisition_failure    = true;
    boolean debug_store_checkout_stacktrace = false;

    AsynchronousRunner taskRunner;
    boolean            taskRunner_is_external;

    RunnableQueue asyncEventQueue;
    boolean       asyncEventQueue_is_external;

    Timer   timer;
    boolean timer_is_external;

    int default_num_task_threads;

    Set liveChildren;



    //OLD
//      Set rqUsers = null;
//      SimpleRunnableQueue rq = null;

//      Set timerUsers = null;
//      Timer timer = null;
    //END OLD

    BasicResourcePoolFactory()
    { this( null, null, null ); }

    BasicResourcePoolFactory( AsynchronousRunner taskRunner, 
			      RunnableQueue asyncEventQueue,  
			      Timer timer )
    { this ( taskRunner, asyncEventQueue, timer, DEFAULT_NUM_TASK_THREADS ); }

    BasicResourcePoolFactory( int num_task_threads )
    { this ( null, null, null, num_task_threads ); }

    BasicResourcePoolFactory( AsynchronousRunner taskRunner, 
			      Timer timer,
			      int default_num_task_threads,
			      boolean no_event_support)
    { 
	this( taskRunner, null,  timer, default_num_task_threads );
	if (no_event_support)
	    asyncEventQueue_is_external = true; //if it's null, and external, it simply won't exist...
    }

    BasicResourcePoolFactory( AsynchronousRunner taskRunner, 
			      RunnableQueue asyncEventQueue,  
			      Timer timer,
			      int default_num_task_threads)
    {  
	this.taskRunner = taskRunner;
	this.taskRunner_is_external = ( taskRunner != null );

	this.asyncEventQueue = asyncEventQueue;
	this.asyncEventQueue_is_external = ( asyncEventQueue != null );

	this.timer = timer;
	this.timer_is_external = ( timer != null );

	this.default_num_task_threads = default_num_task_threads;
    }

    private void createThreadResources()
    {
	if (! taskRunner_is_external )
	    taskRunner = new ThreadPoolAsynchronousRunner( default_num_task_threads, true );
	if (! asyncEventQueue_is_external)
	    asyncEventQueue = new CarefulRunnableQueue( true, false );
	if (! timer_is_external )
	    timer = new Timer( true );

	this.liveChildren = new HashSet();
    }

    private void destroyThreadResources()
    {
	if (! taskRunner_is_external )
	    {
		taskRunner.close();
		taskRunner = null;
	    }
	if (! asyncEventQueue_is_external )
	    {
		asyncEventQueue.close();
		asyncEventQueue = null;
	    }
	if (! timer_is_external )
	    {
		timer.cancel();
		timer = null;
	    }

	this.liveChildren = null;
    }

//      synchronized RunnableQueue getSharedRunnableQueue( BasicResourcePool pool )
//      {
//  	if (rqUsers == null)
//  	    {
//  		rqUsers = new HashSet();
//  		rq = new SimpleRunnableQueue(true);
//  	    }
//  	rqUsers.add( pool );
//  	return rq;
//      }
    
//      synchronized Timer getTimer( BasicResourcePool pool )
//      {
//  	if (timerUsers == null)
//  	    {
//  		timerUsers = new HashSet();
//  		timer = new Timer( true );
//  	    }
//  	timerUsers.add( pool );
//  	return timer;
//      }

    synchronized void markBroken( BasicResourcePool pool )
    {
	//System.err.println("markBroken -- liveChildren: " + liveChildren);
	if (liveChildren != null) //keep this method idempotent!
	    {
		liveChildren.remove( pool );
		if (liveChildren.isEmpty())
		    destroyThreadResources();
	    }
//  	rqUsers.remove( pool );
//  	if (rqUsers.size() == 0)
//  	    {
//  		rqUsers = null;
//  		rq.close();
//  		rq = null;
//  	    }

//  	timerUsers.remove( pool );
//  	if (timerUsers.size() == 0)
//  	    {
//  		timerUsers = null;
//  		timer.cancel();
//  		timer = null;
//  	    }
    }
    
    /**
     * If start is less than min, it will
     * be ignored, and the pool will start
     * with min.
     */
    public synchronized void setStart( int start )
	throws ResourcePoolException
    { this.start = start; }

    public synchronized int getStart()
	throws ResourcePoolException
    { return start; } 

    public synchronized void setMin( int min )
	throws ResourcePoolException
    { this.min = min; }

    public synchronized int getMin()
	throws ResourcePoolException
    { return min; }

    public synchronized void setMax( int max )
	throws ResourcePoolException
    { this.max = max; }

    public synchronized int getMax()
	throws ResourcePoolException
    { return max; }

    public synchronized void setIncrement( int inc )
	throws ResourcePoolException
    { this.inc = inc; }

    public synchronized int getIncrement()
	throws ResourcePoolException
    { return inc; }

    public synchronized void setAcquisitionRetryAttempts( int retry_attempts )
	throws ResourcePoolException
    { this.retry_attempts = retry_attempts; }

    public synchronized int getAcquisitionRetryAttempts()
	throws ResourcePoolException
    { return retry_attempts; }

    public synchronized void setAcquisitionRetryDelay( int retry_delay )
	throws ResourcePoolException
    { this.retry_delay = retry_delay; }

    public synchronized int getAcquisitionRetryDelay()
	throws ResourcePoolException
    { return retry_delay; }

    public synchronized void setIdleResourceTestPeriod( long test_period )
    { this.idle_resource_test_period = test_period; }

    public synchronized long getIdleResourceTestPeriod()
    { return idle_resource_test_period; }

    public synchronized void setResourceMaxAge( long max_age )
	throws ResourcePoolException
    { this.max_age = max_age; }

    public synchronized long getResourceMaxAge()
	throws ResourcePoolException
    { return max_age; }

    public synchronized void setResourceMaxIdleTime( long millis )
	throws ResourcePoolException
    { this.max_idle_time = millis; }

    public synchronized long getResourceMaxIdleTime()
	throws ResourcePoolException
    { return max_idle_time; }

    public synchronized void setExcessResourceMaxIdleTime( long millis )
	throws ResourcePoolException
    { this.excess_max_idle_time = millis; }

    public synchronized long getExcessResourceMaxIdleTime()
	throws ResourcePoolException
    { return excess_max_idle_time; }

    public synchronized long getDestroyOverdueResourceTime()
	throws ResourcePoolException
    { return destroy_overdue_resc_time; }

    public synchronized void setDestroyOverdueResourceTime( long millis )
	throws ResourcePoolException
    { this.destroy_overdue_resc_time = millis; }

    public synchronized void setExpirationEnforcementDelay( long expiration_enforcement_delay )
	throws ResourcePoolException
    { this.expiration_enforcement_delay = expiration_enforcement_delay; }

    public synchronized long getExpirationEnforcementDelay()
	throws ResourcePoolException
    { return expiration_enforcement_delay; }

    public synchronized void setBreakOnAcquisitionFailure( boolean break_on_acquisition_failure )
	throws ResourcePoolException
    { this.break_on_acquisition_failure = break_on_acquisition_failure; }

    public synchronized boolean getBreakOnAcquisitionFailure()
	throws ResourcePoolException
    { return break_on_acquisition_failure; }

    public synchronized void setDebugStoreCheckoutStackTrace( boolean debug_store_checkout_stacktrace )
	throws ResourcePoolException
    { this.debug_store_checkout_stacktrace = debug_store_checkout_stacktrace; }

    public synchronized boolean getDebugStoreCheckoutStackTrace()
	throws ResourcePoolException
    { return debug_store_checkout_stacktrace; }

    public synchronized ResourcePool createPool(ResourcePool.Manager mgr)
	throws ResourcePoolException
    {
	if (liveChildren == null)
	    createThreadResources();
	//System.err.println("Created liveChildren: " + liveChildren);
	ResourcePool child = new BasicResourcePool( mgr, 
						    start, 
						    min, 
						    max, 
						    inc, 
						    retry_attempts, 
						    retry_delay, 
						    idle_resource_test_period,
						    max_age, 
						    max_idle_time,
						    excess_max_idle_time,
						    destroy_overdue_resc_time,
						    expiration_enforcement_delay,
						    break_on_acquisition_failure,
						    debug_store_checkout_stacktrace,
						    taskRunner,
						    asyncEventQueue,
						    timer,
						    this );
	liveChildren.add( child );
	return child;
    }
}







