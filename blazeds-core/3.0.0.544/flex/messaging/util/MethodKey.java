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

import java.util.Arrays;

public class MethodKey
{
    private Class enclosingClass;
    private String methodName;
    private Class[] parameterTypes;
        
    public MethodKey(Class enclosingClass, String methodName, Class[] parameterTypes)
    {
        this.enclosingClass = enclosingClass;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
    }

    public boolean equals(Object object)
    {
        boolean result;
        if (this == object) 
        {
            result = true;
        }
        else if (object instanceof MethodKey)
        {
            MethodKey other = (MethodKey) object;
            result = 
                other.methodName.equals(this.methodName) &&
                other.parameterTypes.length == this.parameterTypes.length &&
                other.enclosingClass == this.enclosingClass &&
                Arrays.equals(other.parameterTypes, this.parameterTypes);
        }
        else
        {
            result = false;
        }
        return result;
    }

    public int hashCode()
    {
        // Don't consider parameter types in hashcode to speed up
        // calculation.
        return enclosingClass.hashCode() * 10003 +
            methodName.hashCode();
    }
}
