package flex.messaging.io.amfx;

import java.io.IOException;

import flex.messaging.io.SerializationContext;
import flex.messaging.io.amf.Amf3Output;
import flex.messaging.io.amf.Java15Amf3Output;
import flex.messaging.io.PropertyProxyRegistry;

public class Java15AmfxOutput extends AmfxOutput
{
    public Java15AmfxOutput(SerializationContext context)
    {
        super(context);
    }

    public void writeObject(Object o) throws IOException
    {
        if (o != null && o instanceof Enum && PropertyProxyRegistry.getRegistry().getProxy(o.getClass()) == null)
        {
            Enum enumValue = (Enum)o;
            writeString(enumValue.name());
        }
        else
        {
            super.writeObject(o);
        }
    }

    protected Amf3Output createAMF3Output()
    {
        return new Java15Amf3Output(context);
    }
}
