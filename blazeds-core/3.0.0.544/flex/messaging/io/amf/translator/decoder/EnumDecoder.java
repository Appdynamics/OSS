package flex.messaging.io.amf.translator.decoder;

public class EnumDecoder extends ActionScriptDecoder
{
    public boolean hasShell()
    {
        return true;
    }

    public Object createShell(Object encodedObject, Class desiredClass)
    {
        if (encodedObject instanceof Enum)
        {
            return encodedObject;
        }
        else
        {
            @SuppressWarnings("unchecked")
            Enum value = Enum.valueOf(desiredClass, encodedObject.toString());
            return value;
        }
    }

    public Object decodeObject(Object shell, Object encodedObject, Class desiredClass)
    {
        if (shell == null || encodedObject == null)
            return null;

        return shell;
    }
}