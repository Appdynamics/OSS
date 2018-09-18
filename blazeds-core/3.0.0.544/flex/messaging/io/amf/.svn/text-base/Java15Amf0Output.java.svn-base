package flex.messaging.io.amf;

import java.io.IOException;

import flex.messaging.io.SerializationContext;
import flex.messaging.io.PropertyProxyRegistry;

/**
 * A JDK 1.5 specific subclass of Amf0Output to handle Java 5 constructs 
 * such as enums.
 */
public class Java15Amf0Output extends Amf0Output
{
    public Java15Amf0Output(SerializationContext context)
    {
        super(context);
    }

    /**
     * Creates a new JDK 1.5 specific subclass of Amf3Output which is
     * initialized with the current SerializationContext, OutputStream and
     * debug trace settings to switch the version of the AMF protocol
     * mid-stream.
     */
    protected void createAMF3Output()
    {
        avmPlusOutput = new Java15Amf3Output(context);
        avmPlusOutput.setOutputStream(out);
        avmPlusOutput.setDebugTrace(trace);
    }

    public void writeObject(Object o) throws IOException
    {
        if (o != null && o instanceof Enum && PropertyProxyRegistry.getRegistry().getProxy(o.getClass()) == null)
        {
            Enum enumValue = (Enum)o;
            writeAMFString(enumValue.name());
        }
        else
        {
            super.writeObject(o);
        }
    }
}
