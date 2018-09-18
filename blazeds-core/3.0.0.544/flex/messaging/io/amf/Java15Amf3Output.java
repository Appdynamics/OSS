package flex.messaging.io.amf;

import java.io.IOException;

import flex.messaging.io.SerializationContext;
import flex.messaging.io.PropertyProxyRegistry;

public class Java15Amf3Output extends Amf3Output
{
    public Java15Amf3Output(SerializationContext context)
    {
        super(context);
    }

    public void writeObject(Object o) throws IOException
    {
        // If there is a proxy for this, we'll write it as a custom object so that folks can
        // override the default serialization behavior for enums.
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
