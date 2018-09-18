package flex.messaging.io.amfx;

import java.io.OutputStream;

import flex.messaging.io.SerializationContext;
import flex.messaging.io.amf.AmfTrace;

public class Java15AmfxMessageSerializer extends AmfxMessageSerializer
{
    public void initialize(SerializationContext context, OutputStream out, AmfTrace trace)
    {
        amfxOut = new Java15AmfxOutput(context);
        amfxOut.setOutputStream(out);
        debugTrace = trace;
        isDebug = debugTrace != null;
        amfxOut.setDebugTrace(trace);
        
    }
}
