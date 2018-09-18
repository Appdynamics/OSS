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

import flex.messaging.io.BeanProxy;
import flex.messaging.io.SerializationContext;
import flex.messaging.util.XMLUtil;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * A deserializer of AMF protocol data.
 *
 * @author Peter Farland (pfarland@macromedia.com)
 *
 * @see ActionMessageOutput
 */
public abstract class AbstractAmfInput extends AmfIO implements ActionMessageInput
{
    protected BeanProxy beanProxy = new BeanProxy();

    protected DataInputStream in = null;

    /**
     * Construct a deserializer without connecting it to an input stream
     */
    public AbstractAmfInput(SerializationContext context)
    {
        super(context);
    }

    public void setInputStream(InputStream in)
    {
        this.in = new DataInputStream(in);
    }

    protected Object stringToDocument(String xml)
    {
        // FIXME: Temporary workaround for bug 194815
        if (xml != null && xml.indexOf('<') == -1)
            return xml;

        return XMLUtil.stringToDocument(xml, !(context.legacyXMLNamespaces));
    }
    
    //
    // java.io.ObjectInput IMPLEMENTATIONS
    //

    public int available() throws IOException
    {
        return in.available();
    }

    public void close() throws IOException
    {
        in.close();
    }

    public int read() throws IOException
    {
        return in.read();
    }

    public int read(byte[] bytes) throws IOException
    {
        return in.read(bytes);
    }

    public int read(byte[] bytes, int offset, int length) throws IOException
    {
        return in.read(bytes, offset, length);
    }

    public long skip(long n) throws IOException
    {
        return in.skip(n);
    }

    public int skipBytes(int n) throws IOException
    {
        return in.skipBytes(n);
    }

    //
    // java.io.DataInput IMPLEMENTATIONS
    //

    public boolean readBoolean() throws IOException
    {
        return in.readBoolean();
    }

    public byte readByte() throws IOException
    {
        return in.readByte();
    }

    public char readChar() throws IOException
    {
        return in.readChar();
    }

    public double readDouble() throws IOException
    {
        return in.readDouble();
    }

    public float readFloat() throws IOException
    {
        return in.readFloat();
    }

    public void readFully(byte[] bytes) throws IOException
    {
        in.readFully(bytes);
    }

    public void readFully(byte[] bytes, int offset, int length) throws IOException
    {
        in.readFully(bytes, offset, length);
    }

    public int readInt() throws IOException
    {
        return in.readInt();
    }

    /**
     * @deprecated
     */
    public String readLine() throws IOException
    {
        return in.readLine();
    }

    public long readLong() throws IOException
    {
        return in.readLong();
    }

    public short readShort() throws IOException
    {
        return in.readShort();
    }

    public int readUnsignedByte() throws IOException
    {
        return in.readUnsignedByte();
    }

    public int readUnsignedShort() throws IOException
    {
        return in.readUnsignedShort();
    }

    public String readUTF() throws IOException
    {
        return in.readUTF();
    }
}

