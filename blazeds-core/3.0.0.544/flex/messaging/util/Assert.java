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

/**
 * Simple assert class which provides functionality similar to the assert API
 * of JDK 1.4.
 *
 * Enable as follows:
 * java -Dassert
 */
public class Assert {
    public static final boolean enableAssert = (System.getProperty("assert") != null);

    public static void testAssertion(boolean expr) {
        if (enableAssert && !expr) {
            throw new AssertionFailedError();
        }
    }

    public static void testAssertion(boolean expr, String message)
    {
        if (enableAssert && !expr) {
            throw new AssertionFailedError(message);
        }
    }

}

