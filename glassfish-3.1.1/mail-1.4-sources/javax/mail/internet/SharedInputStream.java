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
 * @(#)SharedInputStream.java	1.4 05/08/29
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package javax.mail.internet;

import java.io.*;

/**
 * An InputStream that is backed by data that can be shared by multiple
 * readers may implement this interface.  This allows users of such an
 * InputStream to determine the current position in the InputStream, and
 * to create new InputStreams representing a subset of the data in the
 * original InputStream.  The new InputStream will access the same
 * underlying data as the original, without copying the data. <p>
 *
 * Note that implementations of this interface must ensure that the
 * <code>close</code> method does not close any underlying stream
 * that might be shared by multiple instances of <code>SharedInputStream</code>
 * until all shared instances have been closed.
 *
 * @version 1.4, 05/08/29
 * @author  Bill Shannon
 * @since JavaMail 1.2
 */

public interface SharedInputStream {
    /**
     * Return the current position in the InputStream, as an
     * offset from the beginning of the InputStream.
     *
     * @return	the current position
     */
    public long getPosition();

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
    public InputStream newStream(long start, long end);
}
