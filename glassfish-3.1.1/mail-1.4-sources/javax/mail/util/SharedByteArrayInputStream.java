/*
 * The contents of this file are subject to the terms 
 * of the Common Development and Distribution License 
 * (the "License").  You may not use this file except 
 * in compliance with the License.
 * 
 * You can obtain a copy of the license at 
 * glassfish/bootstrap/legal/CDDLv1.0.txt or 
 * https://glassfish.dev.java.net/public/CDDLv1.0.html. 
 * See the License for the specific language governing 
 * permissions and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL 
 * HEADER in each file and include the License file at 
 * glassfish/bootstrap/legal/CDDLv1.0.txt.  If applicable, 
 * add the following below this CDDL HEADER, with the 
 * fields enclosed by brackets "[]" replaced with your 
 * own identifying information: Portions Copyright [yyyy] 
 * [name of copyright owner]
 */

/*
 * @(#)SharedByteArrayInputStream.java	1.4 05/08/29
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package javax.mail.util;

import java.io.*;
import javax.mail.internet.SharedInputStream;

/**
 * A ByteArrayInputStream that implements the SharedInputStream interface,
 * allowing the underlying byte array to be shared between multiple readers.
 *
 * @version 1.4, 05/08/29
 * @author  Bill Shannon
 * @since JavaMail 1.4
 */

public class SharedByteArrayInputStream extends ByteArrayInputStream
				implements SharedInputStream {
    /**
     * Position within shared buffer that this stream starts at.
     */
    protected int start = 0;

    /**
     * Create a SharedByteArrayInputStream representing the entire
     * byte array.
     *
     * @param	buf	the byte array
     */
    public SharedByteArrayInputStream(byte[] buf) {
	super(buf);
    }

    /**
     * Create a SharedByteArrayInputStream representing the part
     * of the byte array from <code>offset</code> for <code>length</code>
     * bytes.
     *
     * @param	buf	the byte array
     * @param	offset	offset in byte array to first byte to include
     * @param	length	number of bytes to include
     */
    public SharedByteArrayInputStream(byte[] buf, int offset, int length) {
	super(buf, offset, length);
	start = offset;
    }

    /**
     * Return the current position in the InputStream, as an
     * offset from the beginning of the InputStream.
     *
     * @return  the current position
     */
    public long getPosition() {
	return pos - start;
    }

    /**
     * Return a new InputStream representing a subset of the data
     * from this InputStream, starting at <code>start</code> (inclusive)
     * up to <code>end</code> (exclusive).  <code>start</code> must be
     * non-negative.  If <code>end</code> is -1, the new stream ends
     * at the same place as this stream.  The returned InputStream
     * will also implement the SharedInputStream interface.
     *
     * @param	start	the starting position
     * @param	end	the ending position + 1
     * @return		the new stream
     */
    public InputStream newStream(long start, long end) {
	if (start < 0)
	    throw new IllegalArgumentException("start < 0");
	if (end == -1)
	    end = count - this.start;
	return new SharedByteArrayInputStream(buf,
				this.start + (int)start, (int)(end - start));
    }
}
