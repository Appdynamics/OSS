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
package flex.messaging.io.amf;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This class represents a Flash Actionscript Object (typed or untyped)
 * <p>
 * Making a class rather than just deserializing to a HashMap was chosen for the following reasons:<br/>
 * 1) "types" are not going to be native to Hashmap, table, etc.<br/>
 * 2) it helps in making the deserializer/serializer reflexive.
 * </p>
 *
 * @author Jim Whitfield (jwhitfield@macromedia.com)
 * @author Peter Farland
 * @version 1.0
 */
public class ASObject extends HashMap
{
    static final long serialVersionUID = 1613529666682805692L;
    private boolean inHashCode = false;
    private boolean inToString = false;

    /**
     * the named type, if any
     */
    String namedType = null;

    /**
     * Create an Actionscript object
     */
    public ASObject()
    {
        super();
    }

    /**
     * Create a named Actionscript object
     */
    public ASObject(String name)
    {
        super();
        namedType = name;
    }

    /**
     * get the named type, if any.  (otherwise, return null, implying it is unnamed)
     */
    public String getType()
    {
        return namedType;
    }

    /**
     * Sets the named type.  <br/>
     * this operation is mostly meaningless on an object that came in off the wire,
     * but will be helpful for objects that will be serialized out to Flash.
     */
    public void setType(String type)
    {
        namedType = type;
    }

    public int hashCode()
    {
        int h = 0;
        if (!inHashCode)
        {
            inHashCode = true;
            try
            {
                Iterator i = entrySet().iterator();
                while (i.hasNext())
                {
                    h += i.next().hashCode();
                }
            }
            finally
            {
                inHashCode = false;
            }
        }
        return h;
    }

    public String toString()
    {
        String className = getClass().getName();
        int dotIndex = className.lastIndexOf('.');

        StringBuffer buffer = new StringBuffer();
        buffer.append(className.substring(dotIndex + 1));
        buffer.append("(").append(System.identityHashCode(this)).append(')');
        buffer.append('{');
        if (!inToString)
        {
            inToString = true;
            try
            {
                boolean pairEmitted = false;

                Iterator i = entrySet().iterator();
                while (i.hasNext())
                {
                    if (pairEmitted)
                    {
                        buffer.append(", ");
                    }
                    Map.Entry e = (Map.Entry) (i.next());
                    buffer.append(e.getKey()).append('=').append(e.getValue());
                    pairEmitted = true;
                }
            }
            finally
            {
                inToString = false;
            }
        }
        else
        {
            buffer.append("...");
        }
        buffer.append('}');
        return buffer.toString();
    }
}
