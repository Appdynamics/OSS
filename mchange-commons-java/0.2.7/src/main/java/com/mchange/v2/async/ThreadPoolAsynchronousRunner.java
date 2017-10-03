/*
 * Distributed as part of mchange-commons-java 0.2.7
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

package com.mchange.v2.async;

import java.util.*;
import com.mchange.v2.log.*;

import java.io.StringWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Random;
import com.mchange.v2.io.IndentedWriter;
import com.mchange.v2.util.ResourceClosedException;

public final class ThreadPoolAsynchronousRunner implements AsynchronousRunner
{
    final static MLogger logger = MLog.getLogger( ThreadPoolAsynchronousRunner.class );

    final static int POLL_FOR_STOP_INTERVAL                       = 5000; //milliseconds

    final static int DFLT_DEADLOCK_DETECTOR_INTERVAL              = 10000; //milliseconds
    final static int DFLT_INTERRUPT_DELAY_AFTER_APPARENT_DEADLOCK = 60000; //milliseconds
    final static int DFLT_MAX_INDIVIDUAL_TASK_TIME                = 0;     //milliseconds, <= 0 means don't enforce a max task time

    final static int DFLT_MAX_EMERGENCY_THREADS                   = 10;

    final static long PURGE_EVERY = 500L;

    int deadlock_detector_interval;
    int interrupt_delay_after_apparent_deadlock;
    int max_individual_task_time;

    int        num_threads;
    boolean    daemon;
    HashSet    managed;
    HashSet    available;
    LinkedList pendingTasks;

    Random rnd = new Random();

    Timer myTimer;
    boolean should_cancel_timer;

    TimerTask deadlockDetector = new DeadlockDetector();
    TimerTask replacedThreadInterruptor = null;

    Map stoppedThreadsToStopDates = new HashMap();

    String threadLabel;

    private ThreadPoolAsynchronousRunner( int num_threads, 
					  boolean daemon, 
					  int max_individual_task_time,
					  int deadlock_detector_interval, 
					  int interrupt_delay_after_apparent_deadlock,
					  Timer myTimer,
					  boolean should_cancel_timer,
					  String threadLabel )
    {
        this.num_threads = num_threads;
        this.daemon = daemon;
        this.max_individual_task_time = max_individual_task_time;
        this.deadlock_detector_interval = deadlock_detector_interval;
        this.interrupt_delay_after_apparent_deadlock = interrupt_delay_after_apparent_deadlock;
        this.myTimer = myTimer;
        this.should_cancel_timer = should_cancel_timer;

	this.threadLabel = threadLabel;

        recreateThreadsAndTasks();

        myTimer.schedule( deadlockDetector, deadlock_detector_interval, deadlock_detector_interval );

    }

    private ThreadPoolAsynchronousRunner( int num_threads, 
					  boolean daemon, 
					  int max_individual_task_time,
					  int deadlock_detector_interval, 
					  int interrupt_delay_after_apparent_deadlock,
					  Timer myTimer,
					  boolean should_cancel_timer)
    {
	this( num_threads, 
	      daemon, 
	      max_individual_task_time,
	      deadlock_detector_interval, 
	      interrupt_delay_after_apparent_deadlock,
	      myTimer,
	      should_cancel_timer,
	      null );
    }

    public ThreadPoolAsynchronousRunner( int num_threads, 
					 boolean daemon, 
					 int max_individual_task_time,
					 int deadlock_detector_interval, 
					 int interrupt_delay_after_apparent_deadlock,
					 Timer myTimer,
					 String threadLabel )
    {
        this( num_threads, 
	      daemon, 
	      max_individual_task_time,
	      deadlock_detector_interval, 
	      interrupt_delay_after_apparent_deadlock,
	      myTimer, 
	      false,
	      threadLabel);
    }

    public ThreadPoolAsynchronousRunner( int num_threads, 
					 boolean daemon, 
					 int max_individual_task_time,
					 int deadlock_detector_interval, 
					 int interrupt_delay_after_apparent_deadlock,
					 Timer myTimer )
    {
        this( num_threads, 
	      daemon, 
	      max_individual_task_time,
	      deadlock_detector_interval, 
	      interrupt_delay_after_apparent_deadlock,
	      myTimer, 
	      false );
    }

    public ThreadPoolAsynchronousRunner( int num_threads, 
					 boolean daemon, 
					 int max_individual_task_time,
					 int deadlock_detector_interval, 
					 int interrupt_delay_after_apparent_deadlock,
					 String threadLabel )
    {
        this( num_threads, 
	      daemon, 
	      max_individual_task_time,
	      deadlock_detector_interval, 
	      interrupt_delay_after_apparent_deadlock,
	      new Timer( true ), 
	      true,
	      threadLabel );
    }

    public ThreadPoolAsynchronousRunner( int num_threads, 
					 boolean daemon, 
					 int max_individual_task_time,
					 int deadlock_detector_interval, 
					 int interrupt_delay_after_apparent_deadlock )
    {
        this( num_threads, 
	      daemon, 
	      max_individual_task_time,
	      deadlock_detector_interval, 
	      interrupt_delay_after_apparent_deadlock,
	      new Timer( true ), 
	      true );
    }

    public ThreadPoolAsynchronousRunner( int num_threads, boolean daemon, Timer sharedTimer, String threadLabel )
    { 
        this( num_threads, 
	      daemon, 
	      DFLT_MAX_INDIVIDUAL_TASK_TIME, 
	      DFLT_DEADLOCK_DETECTOR_INTERVAL, 
	      DFLT_INTERRUPT_DELAY_AFTER_APPARENT_DEADLOCK, 
	      sharedTimer, 
	      false,
	      threadLabel ); 
    }

    public ThreadPoolAsynchronousRunner( int num_threads, boolean daemon, Timer sharedTimer )
    { 
        this( num_threads, 
	      daemon, 
	      DFLT_MAX_INDIVIDUAL_TASK_TIME, 
	      DFLT_DEADLOCK_DETECTOR_INTERVAL, 
	      DFLT_INTERRUPT_DELAY_AFTER_APPARENT_DEADLOCK, 
	      sharedTimer, 
	      false ); 
    }

    public ThreadPoolAsynchronousRunner( int num_threads, boolean daemon )
    { 
        this( num_threads, 
	      daemon, 
	      DFLT_MAX_INDIVIDUAL_TASK_TIME, 
	      DFLT_DEADLOCK_DETECTOR_INTERVAL, 
	      DFLT_INTERRUPT_DELAY_AFTER_APPARENT_DEADLOCK, 
	      new Timer( true ), 
	      true ); 
    }

    public synchronized void postRunnable(Runnable r)
    {
        try
        {
            pendingTasks.add( r );
            this.notifyAll();
            
            if (Debug.DEBUG && logger.isLoggable(MLevel.FINEST))
                logger.log(MLevel.FINEST, this + ": Adding task to queue -- " + r);
        }
        catch ( NullPointerException e )
        {
            //e.printStackTrace();
            if ( Debug.DEBUG )
            {
                if ( logger.isLoggable( MLevel.FINE ) )
                    logger.log( MLevel.FINE, "NullPointerException while posting Runnable -- Probably we're closed.", e );
            }
            throw new ResourceClosedException("Attempted to use a ThreadPoolAsynchronousRunner in a closed or broken state.");
        }
    }

    public synchronized int getThreadCount()
    { return managed.size(); }

    public void close( boolean skip_remaining_tasks )
    {
        synchronized ( this )
        {
            if (managed == null) return;
            deadlockDetector.cancel();
            //replacedThreadInterruptor.cancel();
            if (should_cancel_timer)
                myTimer.cancel();
            myTimer = null;
            for (Iterator ii = managed.iterator(); ii.hasNext(); )
            { 
                PoolThread stopMe = (PoolThread) ii.next();
                stopMe.gentleStop();
                if (skip_remaining_tasks)
                    stopMe.interrupt();
            }
            managed = null;

            if (!skip_remaining_tasks)
            {
                for (Iterator ii = pendingTasks.iterator(); ii.hasNext(); )
                {
                    Runnable r = (Runnable) ii.next();
                    new Thread(r).start();
                    ii.remove();
                }
            }
            available = null;
            pendingTasks = null;
        }
    }

    public void close()
    { close( true ); }

    public synchronized int getActiveCount()
    { return managed.size() - available.size(); }

    public synchronized int getIdleCount()
    { return available.size(); }

    public synchronized int getPendingTaskCount()
    { return pendingTasks.size(); }

    public synchronized String getStatus()
    { 
        /*
	  StringBuffer sb = new StringBuffer( 512 );
	  sb.append( this.toString() );
	  sb.append( ' ' );
	  appendStatusString( sb );
	  return sb.toString();
         */

        return getMultiLineStatusString();
    }

    // done reflectively for jdk 1.3/1.4 compatability
    public synchronized String getStackTraces()
    { return getStackTraces(0); }

    // protected by ThreadPoolAsynchronousRunner.this' lock
    // BE SURE CALLER OWNS ThreadPoolAsynchronousRunner.this' lock
    private String getStackTraces(int initial_indent)
    {
	assert Thread.holdsLock( this );

        if (managed == null)
            return null;

        try
        {
            Method m = Thread.class.getMethod("getStackTrace", null);

            StringWriter sw = new StringWriter(2048);
            IndentedWriter iw = new IndentedWriter( sw );
            for (int i = 0; i < initial_indent; ++i)
                iw.upIndent();
            for (Iterator ii = managed.iterator(); ii.hasNext(); )
            {
                Object poolThread = ii.next();
                Object[] stackTraces = (Object[]) m.invoke( poolThread, null );
		printStackTraces( iw, poolThread, stackTraces );
            }
            for (int i = 0; i < initial_indent; ++i)
                iw.downIndent();
            iw.flush(); // useless, but I feel better
            String out = sw.toString();
            iw.close(); // useless, but I feel better;
            return out;
        }
        catch (NoSuchMethodException e)
        {
            if ( logger.isLoggable( MLevel.FINE ) )
                logger.fine( this + ": stack traces unavailable because this is a pre-Java 1.5 VM.");
            return null;
        }
        catch (Exception e)
        {
            if ( logger.isLoggable( MLevel.FINE ) )
                logger.log( MLevel.FINE, this + ": An Exception occurred while trying to extract PoolThread stack traces.", e);
            return null;
        }
    }

    // no pre-synchronization required
    private String getJvmStackTraces(int initial_indent)
    {
        try
        {
            Method m = Thread.class.getMethod("getAllStackTraces", null);
	    Map threadMap = (Map) m.invoke( null, null );

            StringWriter sw = new StringWriter(2048);
            IndentedWriter iw = new IndentedWriter( sw );
            for (int i = 0; i < initial_indent; ++i)
                iw.upIndent();
            for (Iterator ii = threadMap.entrySet().iterator(); ii.hasNext(); )
            {
		Map.Entry entry = (Map.Entry) ii.next();
                Object poolThread = entry.getKey();
                Object[] stackTraces = (Object[]) entry.getValue();
		printStackTraces( iw, poolThread, stackTraces );
            }
            for (int i = 0; i < initial_indent; ++i)
                iw.downIndent();
            iw.flush(); // useless, but I feel better
            String out = sw.toString();
            iw.close(); // useless, but I feel better;
            return out;
        }
        catch (NoSuchMethodException e)
        {
            if ( logger.isLoggable( MLevel.FINE ) )
                logger.fine( this + ": JVM stack traces unavailable because this is a pre-Java 1.5 VM.");
            return null;
        }
        catch (Exception e)
        {
            if ( logger.isLoggable( MLevel.FINE ) )
                logger.log( MLevel.FINE, this + ": An Exception occurred while trying to extract PoolThread stack traces.", e);
            return null;
        }
    }

    // no pre-synchronization required
    private void printStackTraces(IndentedWriter iw, Object thread, Object[] stackTraces) throws IOException
    {
	iw.println( thread );
	iw.upIndent();
	for (int i = 0, len = stackTraces.length; i < len; ++i)
	    iw.println( stackTraces[i] );
	iw.downIndent();
    }

    public synchronized String getMultiLineStatusString()
    { return this.getMultiLineStatusString(0); }

    // protected by ThreadPoolAsynchronousRunner.this' lock
    // BE SURE CALLER OWNS ThreadPoolAsynchronousRunner.this' lock
    private String getMultiLineStatusString(int initial_indent)
    {
        try
        {
            StringWriter sw = new StringWriter(2048);
            IndentedWriter iw = new IndentedWriter( sw );

            for (int i = 0; i < initial_indent; ++i)
                iw.upIndent();

            if (managed == null)
            {
                iw.print("[");
                iw.print( this );
                iw.println(" closed.]");
            }
            else
            {
                HashSet active = (HashSet) managed.clone();
                active.removeAll( available );

                iw.print("Managed Threads: ");
                iw.println( managed.size() );
                iw.print("Active Threads: ");
                iw.println( active.size() );
                iw.println("Active Tasks: ");
                iw.upIndent();
                for (Iterator ii = active.iterator(); ii.hasNext(); )
                {
                    PoolThread pt = (PoolThread) ii.next();
                    iw.println( pt.getCurrentTask() );
		    iw.upIndent();
                    iw.print( "on thread: ");
                    iw.println( pt.getName() );
		    iw.downIndent();
                }
                iw.downIndent();
                iw.println("Pending Tasks: ");
                iw.upIndent();
                for (int i = 0, len = pendingTasks.size(); i < len; ++i)
                    iw.println( pendingTasks.get( i ) );
                iw.downIndent();
            }

            for (int i = 0; i < initial_indent; ++i)
                iw.downIndent();
            iw.flush(); // useless, but I feel better
            String out = sw.toString();
            iw.close(); // useless, but I feel better;
            return out;
        }
        catch (IOException e)
        {
            if (logger.isLoggable( MLevel.WARNING ))
                logger.log( MLevel.WARNING, "Huh? An IOException when working with a StringWriter?!?", e);
            throw new RuntimeException("Huh? An IOException when working with a StringWriter?!? " + e);
        }
    }

    // protected by ThreadPoolAsynchronousRunner.this' lock
    // BE SURE CALLER OWNS ThreadPoolAsynchronousRunner.this' lock
    private void appendStatusString( StringBuffer sb )
    {
        if (managed == null)
            sb.append( "[closed]" );
        else
        {
            HashSet active = (HashSet) managed.clone();
            active.removeAll( available );
            sb.append("[num_managed_threads: ");
            sb.append( managed.size() );
            sb.append(", num_active: ");
            sb.append( active.size() );
            sb.append("; activeTasks: ");
            boolean first = true;
            for (Iterator ii = active.iterator(); ii.hasNext(); )
            {
                if (first)
                    first = false;
                else
                    sb.append(", ");
                PoolThread pt = (PoolThread) ii.next();
                sb.append( pt.getCurrentTask() );
                sb.append( " (");
                sb.append( pt.getName() );
                sb.append(')');
            }
            sb.append("; pendingTasks: ");
            for (int i = 0, len = pendingTasks.size(); i < len; ++i)
            {
                if (i != 0) sb.append(", ");
                sb.append( pendingTasks.get( i ) );
            }
            sb.append(']');
        }
    }

    // protected by ThreadPoolAsynchronousRunner.this' lock
    // BE SURE CALLER OWNS ThreadPoolAsynchronousRunner.this' lock (or is ctor)
    private void recreateThreadsAndTasks()
    {
        if ( this.managed != null)
        {
            Date aboutNow = new Date();
            for (Iterator ii = managed.iterator(); ii.hasNext(); )
            {
                PoolThread pt = (PoolThread) ii.next();
                pt.gentleStop();
                stoppedThreadsToStopDates.put( pt, aboutNow );
                ensureReplacedThreadsProcessing();
            }
        }

        this.managed = new HashSet();
        this.available = new HashSet();
        this.pendingTasks = new LinkedList();
        for (int i = 0; i < num_threads; ++i)
        {
            Thread t = new PoolThread(i, daemon);
            managed.add( t );
            available.add( t );
            t.start();
        }
    }

    // protected by ThreadPoolAsynchronousRunner.this' lock
    // BE SURE CALLER OWNS ThreadPoolAsynchronousRunner.this' lock
    private void processReplacedThreads()
    {
        long about_now = System.currentTimeMillis();
        for (Iterator ii = stoppedThreadsToStopDates.keySet().iterator(); ii.hasNext(); )
        {
            PoolThread pt = (PoolThread) ii.next();
            if (! pt.isAlive())
                ii.remove();
            else
            {
                Date d = (Date) stoppedThreadsToStopDates.get( pt );
                if ((about_now - d.getTime()) > interrupt_delay_after_apparent_deadlock)
                {
                    if (logger.isLoggable(MLevel.WARNING))
                        logger.log(MLevel.WARNING, 
                                        "Task " + pt.getCurrentTask() + " (in deadlocked PoolThread) failed to complete in maximum time " +
                                        interrupt_delay_after_apparent_deadlock + "ms. Trying interrupt().");
                    pt.interrupt();
                    ii.remove();
                }
                //else keep waiting...
            }
            if (stoppedThreadsToStopDates.isEmpty())
                stopReplacedThreadsProcessing();
        }
    }

    // protected by ThreadPoolAsynchronousRunner.this' lock
    // BE SURE CALLER OWNS ThreadPoolAsynchronousRunner.this' lock
    private void ensureReplacedThreadsProcessing()
    {
        if (replacedThreadInterruptor == null)
        {
            if (logger.isLoggable( MLevel.FINE ))
                logger.fine("Apparently some threads have been replaced. Replacement thread processing enabled.");

            this.replacedThreadInterruptor = new ReplacedThreadInterruptor();
            int replacedThreadProcessDelay = interrupt_delay_after_apparent_deadlock / 4;
            myTimer.schedule( replacedThreadInterruptor, replacedThreadProcessDelay, replacedThreadProcessDelay );
        }
    }

    // protected by ThreadPoolAsynchronousRunner.this' lock
    // BE SURE CALLER OWNS ThreadPoolAsynchronousRunner.this' lock
    private void stopReplacedThreadsProcessing()
    {
        if (this.replacedThreadInterruptor != null)
        {
            this.replacedThreadInterruptor.cancel();
            this.replacedThreadInterruptor = null;

            if (logger.isLoggable( MLevel.FINE ))
                logger.fine("Apparently all replaced threads have either completed their tasks or been interrupted(). " +
                "Replacement thread processing cancelled.");
        }
    }

    // protected by ThreadPoolAsynchronousRunner.this' lock
    // BE SURE CALLER OWNS ThreadPoolAsynchronousRunner.this' lock
    private void shuttingDown( PoolThread pt )
    {
        if (managed != null && managed.contains( pt )) //we are not closed, and this was a thread in the current pool, not a replaced thread
        {
            managed.remove( pt );
            available.remove( pt );
            PoolThread replacement = new PoolThread( pt.getIndex(), daemon );
            managed.add( replacement );
            available.add( replacement );
            replacement.start();
        }
    }


    class PoolThread extends Thread
    {
        // protected by ThreadPoolAsynchronousRunner.this' lock
        Runnable currentTask;

        // protected by ThreadPoolAsynchronousRunner.this' lock
        boolean should_stop;

        // post ctor immutable
        int index;

        // not shared. only accessed by the PoolThread itself
        TimerTask maxIndividualTaskTimeEnforcer = null;

        PoolThread(int index, boolean daemon)
        {
            this.setName( (threadLabel == null ? this.getClass().getName() : threadLabel) + "-#" + index);
            this.setDaemon( daemon );
            this.index = index;
        }

        public int getIndex()
        { return index; }

        // protected by ThreadPoolAsynchronousRunner.this' lock
        // BE SURE CALLER OWNS ThreadPoolAsynchronousRunner.this' lock
        void gentleStop()
        { should_stop = true; }

        // protected by ThreadPoolAsynchronousRunner.this' lock
        // BE SURE CALLER OWNS ThreadPoolAsynchronousRunner.this' lock
        Runnable getCurrentTask()
        { return currentTask; }

        // no need to sync. data not shared
        private /* synchronized */  void setMaxIndividualTaskTimeEnforcer()
        {
            this.maxIndividualTaskTimeEnforcer = new MaxIndividualTaskTimeEnforcer( this );
            myTimer.schedule( maxIndividualTaskTimeEnforcer, max_individual_task_time );
        }

        // no need to sync. data not shared
        private /* synchronized */ void cancelMaxIndividualTaskTimeEnforcer()
        {
            this.maxIndividualTaskTimeEnforcer.cancel();
            this.maxIndividualTaskTimeEnforcer = null;
        }

        // no need to sync. Timer threadsafe, no other data access
        private void purgeTimer()
        { 
	    myTimer.purge(); 
	    if ( logger.isLoggable( MLevel.FINER ) )
		logger.log(MLevel.FINER, this.getClass().getName() + " -- PURGING TIMER");
	}

        public void run()
        {
	    long checkForPurge = rnd.nextLong();

            try
            {
                thread_loop:
                    while (true)
                    {
                        Runnable myTask;
                        synchronized ( ThreadPoolAsynchronousRunner.this )
                        {
                            while ( !should_stop && pendingTasks.size() == 0 )
                                ThreadPoolAsynchronousRunner.this.wait( POLL_FOR_STOP_INTERVAL );
                            if (should_stop) 
                                break thread_loop;

                            if (! available.remove( this ) )
                                throw new InternalError("An unavailable PoolThread tried to check itself out!!!");
                            myTask = (Runnable) pendingTasks.remove(0);
                            currentTask = myTask;
                        }
                        try
                        { 
                            if (max_individual_task_time > 0)
                                setMaxIndividualTaskTimeEnforcer();
                            myTask.run(); 
                        }
                        catch ( RuntimeException e )
                        {
                            if ( logger.isLoggable( MLevel.WARNING ) )
                                logger.log(MLevel.WARNING, this + " -- caught unexpected Exception while executing posted task.", e);
                            //e.printStackTrace();
                        }
                        finally
                        {
                            if ( maxIndividualTaskTimeEnforcer != null )
				{
				    cancelMaxIndividualTaskTimeEnforcer();

				    // we stochastically purge the timer roughly every PURGE_EVERY cancels
				    // math below is an inline, fast, pseudorandom long generator.
				    // see com.mchange.v2.util.XORShiftRandomUtils
				    checkForPurge ^= (checkForPurge << 21);
				    checkForPurge ^= (checkForPurge >>> 35);
				    checkForPurge ^= (checkForPurge << 4);
				    if ( (checkForPurge % PURGE_EVERY ) == 0 )
					purgeTimer();
				}

                            synchronized ( ThreadPoolAsynchronousRunner.this )
                            {
                                if (should_stop)
                                    break thread_loop;

                                if ( available != null && ! available.add( this ) )
                                    throw new InternalError("An apparently available PoolThread tried to check itself in!!!");
                                currentTask = null;
                            }
                        }
                    }
            }
            catch ( InterruptedException exc )
            {
//              if ( Debug.TRACE > Debug.TRACE_NONE )
//              System.err.println(this + " interrupted. Shutting down.");

                if ( Debug.TRACE > Debug.TRACE_NONE && logger.isLoggable( MLevel.FINE ) )
                    logger.fine(this + " interrupted. Shutting down.");
            }
            catch (RuntimeException re)
            {
                if (logger.isLoggable(MLevel.WARNING))
                    logger.log(MLevel.WARNING, "An unexpected RuntimException is implicated in the closing of " + this, re);
                throw re;
            }
            catch (Error err)
            {
                if (logger.isLoggable(MLevel.WARNING))
                    logger.log(MLevel.WARNING, 
                               "An Error forced the closing of " + this + 
                               ". Will attempt to reconstruct, but this might mean that something bad is happening.", 
                               err);
                throw err;
            }
            finally
            {
                synchronized ( ThreadPoolAsynchronousRunner.this )
                { ThreadPoolAsynchronousRunner.this.shuttingDown( this ); }
            }
        }
    }

    class DeadlockDetector extends TimerTask
    {
        LinkedList last = null;
        LinkedList current = null;

        public void run()
        {

            boolean run_stray_tasks = false;
            synchronized ( ThreadPoolAsynchronousRunner.this )
            { 
                if (pendingTasks.size() == 0)
                {
                    last = null;
		    if ( logger.isLoggable( MLevel.FINEST ) )
			logger.log( MLevel.FINEST, this + " -- Running DeadlockDetector[Exiting. No pending tasks.]");
                    return;
                }

                current = (LinkedList) pendingTasks.clone();
		if ( logger.isLoggable( MLevel.FINEST ) )
		    logger.log( MLevel.FINEST, this + " -- Running DeadlockDetector[last->" + last + ",current->" + current + ']');

                if ( current.equals( last ) )
                {
                    //System.err.println(this + " -- APPARENT DEADLOCK!!! Creating emergency threads for unassigned pending tasks!");
                    if ( logger.isLoggable( MLevel.WARNING ) )
                    {
                        logger.warning(this + " -- APPARENT DEADLOCK!!! Creating emergency threads for unassigned pending tasks!");
                        StringWriter sw = new StringWriter( 4096 );
                        PrintWriter pw = new PrintWriter( sw );
                        //StringBuffer sb = new StringBuffer( 512 );
                        //appendStatusString( sb );
                        //System.err.println( sb.toString() );
                        pw.print( this );
                        pw.println( " -- APPARENT DEADLOCK!!! Complete Status: ");
                        pw.print( ThreadPoolAsynchronousRunner.this.getMultiLineStatusString( 1 ) );
                        pw.println("Pool thread stack traces:"); 
                        String stackTraces = getStackTraces( 1 );
                        if (stackTraces == null)
                            pw.println("\t[Stack traces of deadlocked task threads not available.]");
                        else
                            pw.print( stackTraces ); //already has an end-of-line
                        pw.flush(); //superfluous, but I feel better
                        logger.warning( sw.toString() );
                        pw.close(); //superfluous, but I feel better
                    }
		    if ( logger.isLoggable( MLevel.FINEST ) )
		    {
                        StringWriter sw = new StringWriter( 4096 );
                        PrintWriter pw = new PrintWriter( sw );
                        pw.print( this );
                        pw.println( " -- APPARENT DEADLOCK extra info, full JVM thread dump: ");
                        String stackTraces = getJvmStackTraces( 1 );
                        if (stackTraces == null)
                            pw.println("\t[Full JVM thread dump not available.]");
                        else
                            pw.print( stackTraces ); //already has an end-of-line
                        pw.flush(); //superfluous, but I feel better
                        logger.finest( sw.toString() );
                        pw.close(); //superfluous, but I feel better
		    }
                    recreateThreadsAndTasks();
                    run_stray_tasks = true;
                }
            }
            if (run_stray_tasks)
            {
                AsynchronousRunner ar = new ThreadPerTaskAsynchronousRunner( DFLT_MAX_EMERGENCY_THREADS, max_individual_task_time );
                for ( Iterator ii = current.iterator(); ii.hasNext(); )
                    ar.postRunnable( (Runnable) ii.next() );
                ar.close( false ); //tell the emergency runner to close itself when its tasks are complete
                last = null;
            }
            else
                last = current;

            // under some circumstances, these lists seem to hold onto a lot of memory... presumably this
            // is when long pending task lists build up for some reason... nevertheless, let's dereference
            // things as soon as possible. [Thanks to Venkatesh Seetharamaiah for calling attention to this
            // issue, and for documenting the source of object retention.]
            current = null;
        }
    }

    class MaxIndividualTaskTimeEnforcer extends TimerTask
    {
        PoolThread pt;
        Thread     interruptMe;
        String     threadStr;
        String     fixedTaskStr;

        MaxIndividualTaskTimeEnforcer(PoolThread pt)
        { 
            this.pt = pt;
            this.interruptMe = pt;
            this.threadStr = pt.toString();
            this.fixedTaskStr = null;
        }

        MaxIndividualTaskTimeEnforcer(Thread interruptMe, String threadStr, String fixedTaskStr)
        { 
            this.pt = null; 
            this.interruptMe = interruptMe;
            this.threadStr = threadStr;
            this.fixedTaskStr = fixedTaskStr;
        }

        public void run() 
        { 
            String taskStr;

            if (fixedTaskStr != null)
                taskStr = fixedTaskStr;
            else if (pt != null)
            {
                synchronized (ThreadPoolAsynchronousRunner.this)
                { taskStr = String.valueOf( pt.getCurrentTask() ); }
            }
            else
                taskStr = "Unknown task?!";

            if (logger.isLoggable( MLevel.WARNING ))
                logger.warning("A task has exceeded the maximum allowable task time. Will interrupt() thread [" + threadStr
                                + "], with current task: " + taskStr);

            interruptMe.interrupt(); 

            if (logger.isLoggable( MLevel.WARNING ))
                logger.warning("Thread [" + threadStr + "] interrupted.");
        } 
    }

    //not currently used...
    private void runInEmergencyThread( final Runnable r )
    {
        final Thread t = new Thread( r );
        t.start();
        if (max_individual_task_time > 0)
        {
            TimerTask maxIndividualTaskTimeEnforcer = new MaxIndividualTaskTimeEnforcer(t, t + " [One-off emergency thread!!!]", r.toString());
            myTimer.schedule( maxIndividualTaskTimeEnforcer, max_individual_task_time );
        }
    }

    class ReplacedThreadInterruptor extends TimerTask
    {
        public void run()
        {
            synchronized (ThreadPoolAsynchronousRunner.this)
            { processReplacedThreads(); }
        }
    }
}
