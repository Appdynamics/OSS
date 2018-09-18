package flex.messaging.io.amf;

import java.io.OutputStream;

import flex.messaging.io.SerializationContext;

public class Java15AmfMessageSerializer extends AmfMessageSerializer
{
    public void initialize(SerializationContext context, OutputStream out, AmfTrace trace)
    {
        amfOut = new Java15Amf0Output(context);
        amfOut.setAvmPlus(version > 0);
        amfOut.setOutputStream(out);

        debugTrace = trace;
        isDebug = trace != null;
        amfOut.setDebugTrace(debugTrace);
    }
}
