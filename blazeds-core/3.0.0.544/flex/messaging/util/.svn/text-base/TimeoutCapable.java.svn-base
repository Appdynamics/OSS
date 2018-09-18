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
 * This interface defines the constract for an object that can time 
 * out, wher ethe timeout mechanism is provided by the TimeoutManager
 * class.
 * 
 * @author neville
 */
public interface TimeoutCapable
{
    /**
     * Revoke the timeout task, removing it from future evaluation and execution.
     */
    void cancelTimeout();
    
    /**
     * Determine the timestamp of this object's last use, where "last use" should
     * denote all tasks that eliminate idleness.
     */
    long getLastUse();

    /**
     * Determine the time, in milliseconds, that this object is allowed to idle
     * before having its timeout method invoked.
     */
    long getTimeoutPeriod();

    /**
     * Set the Future used to provide access to the Runnable task that invokes
     * the timeout method. A Future is used instead of a Runnable so that it may
     * be cancelled according to the Java concurrency standard. 
     */
    void setTimeoutFuture(Future future);

    /**
     * Inform the object that it has timed out.
     */
    void timeout();
}
