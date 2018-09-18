package flex.messaging.io;

import flex.messaging.io.amf.translator.ASTranslator;
import flex.messaging.io.amf.translator.decoder.EnumDecoder;

public class Java15TypeMarshaller extends ASTranslator
{
    private static EnumDecoder enumDecoder = new EnumDecoder();

    public Object createInstance(Object source, Class desiredClass)
    {
        if (desiredClass.isEnum())
        {
            Object instance = enumDecoder.createShell(source, desiredClass);
            return instance;
        }
        else
        {
            return super.createInstance(source, desiredClass);
        }
    }

    public Object convert(Object source, Class desiredClass)
    {
        if (desiredClass.isEnum())
        {
            return enumDecoder.decodeObject(source, desiredClass);
        }
        else 
        {
            return super.convert(source, desiredClass);
        }
    }
    
}
