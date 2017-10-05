/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2008 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 *
 * Contributor(s):
 *
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 *
 *
 * This file incorporates work covered by the following copyright and
 * permission notice:
 *
 * Copyright 2004 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



package org.apache.catalina.connector;


import java.io.IOException;
import java.io.Writer;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.HashMap;
import java.util.logging.*;

import javax.servlet.http.Cookie;

import com.sun.grizzly.tcp.ActionCode;
import org.apache.catalina.Globals;
import org.apache.catalina.Session;
import org.apache.catalina.connector.ClientAbortException;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.util.RequestUtil;
import com.sun.grizzly.util.buf.ByteChunk;
import com.sun.grizzly.util.buf.C2BConverter;



/**
 * The buffer used by Tomcat response. This is a derivative of the Tomcat 3.3
 * OutputBuffer, with the removal of some of the state handling (which in 
 * Coyote is mostly the Processor's responsability).
 *
 * @author Costin Manolache
 * @author Remy Maucherat
 */
public class OutputBuffer extends Writer
    implements ByteChunk.ByteOutputChannel {

    private static Logger log = Logger.getLogger(OutputBuffer.class.getName());

    // -------------------------------------------------------------- Constants

    private static final String SET_COOKIE_HEADER = "Set-Cookie";
    public static final String DEFAULT_ENCODING = 
        com.sun.grizzly.tcp.Constants.DEFAULT_CHARACTER_ENCODING;
    public static final int DEFAULT_BUFFER_SIZE = 8*1024;
    static final int debug = 0;


    // ----------------------------------------------------- Instance Variables


    /**
     * The byte buffer.
     */
    private ByteChunk bb;


    /**
     * State of the output buffer.
     */
    private int state = 0;
    private boolean initial = true;


    /**
     * Number of bytes written.
     */
    private int bytesWritten = 0;


    /**
     * Number of chars written.
     */
    private int charsWritten = 0;


    /**
     * Flag which indicates if the output buffer is closed.
     */
    private boolean closed = false;


    /**
     * Do a flush on the next operation.
     */
    private boolean doFlush = false;


    /**
     * Byte chunk used to output bytes.
     */
    private ByteChunk outputChunk = new ByteChunk();


    /**
     * Encoding to use.
     */
    private String enc;


    /**
     * Encoder is set.
     */
    private boolean gotEnc = false;


    /**
     * List of encoders.
     */
    protected HashMap encoders = new HashMap();


    /**
     * Current char to byte converter.
     */
    protected C2BConverter conv;


    /**
     * Associated Coyote response.
     */
    private com.sun.grizzly.tcp.Response response;
    private Response coyoteResponse;

    /**
     * Suspended flag. All output bytes will be swallowed if this is true.
     */
    private boolean suspended = false;


    // ----------------------------------------------------------- Constructors


    /**
     * Default constructor. Allocate the buffer with the default buffer size.
     */
    public OutputBuffer() {

        this(DEFAULT_BUFFER_SIZE);

    }


    // START S1AS8 4861933
    public OutputBuffer(boolean chunkingDisabled) {
        this(DEFAULT_BUFFER_SIZE, chunkingDisabled);
    }
    // END S1AS8 4861933


    /**
     * Alternate constructor which allows specifying the initial buffer size.
     * 
     * @param size Buffer size to use
     */
    public OutputBuffer(int size) {
        // START S1AS8 4861933
        /*
        bb = new ByteChunk(size);
        bb.setLimit(size);
        bb.setByteOutputChannel(this);
        cb = new CharChunk(size);
        cb.setCharOutputChannel(this);
        cb.setLimit(size);
        */
        this(size, false);
        // END S1AS8 4861933
    }


    // START S1AS8 4861933
    public OutputBuffer(int size, boolean chunkingDisabled) {
        bb = new ByteChunk(size);
        if (!chunkingDisabled) {
            bb.setLimit(size);
        }
        bb.setByteOutputChannel(this);
    }
    // END S1AS8 4861933


    // ------------------------------------------------------------- Properties


    /**
     * Associated Coyote response.
     * 
     * @param response Associated Coyote response
     */
    public void setResponse(com.sun.grizzly.tcp.Response response) {
	this.response = response;
    }


    public void setCoyoteResponse(Response coyoteResponse) {
        this.coyoteResponse = coyoteResponse;
        setResponse((com.sun.grizzly.tcp.Response) coyoteResponse.getCoyoteResponse());
    }


    /**
     * Get associated Coyote response.
     * 
     * @return the associated Coyote response
     */
    public com.sun.grizzly.tcp.Response getResponse() {
        return this.response;
    }


    /**
     * Is the response output suspended ?
     * 
     * @return suspended flag value
     */
    public boolean isSuspended() {
        return this.suspended;
    }


    /**
     * Set the suspended flag.
     * 
     * @param suspended New suspended flag value
     */
    public void setSuspended(boolean suspended) {
        this.suspended = suspended;
    }


    // --------------------------------------------------------- Public Methods


    /**
     * Recycle the output buffer.
     */
    public void recycle() {

	if (log.isLoggable(Level.FINE))
            log.fine("recycle()");

        initial = true;
        bytesWritten = 0;
        charsWritten = 0;

        bb.recycle(); 
        closed = false;
        suspended = false;

        if (conv!= null) {
            conv.recycle();
        }

        gotEnc = false;
        enc = null;
    }


    /**
     * Close the output buffer. This tries to calculate the response size if 
     * the response has not been committed yet.
     * 
     * @throws IOException An underlying IOException occurred
     */
    public void close()
        throws IOException {

        if (closed)
            return;
        if (suspended)
            return;

        if ((!response.isCommitted()) 
            && (response.getContentLength() == -1)) {
            // If this didn't cause a commit of the response, the final content
            // length can be calculated
            if (!response.isCommitted()) {
                response.setContentLength(bb.getLength());
            }
        }

        doFlush(false);
        closed = true;

        response.finish();

    }


    /**
     * Flush bytes or chars contained in the buffer.
     * 
     * @throws IOException An underlying IOException occurred
     */
    public void flush()
        throws IOException {
        doFlush(true);
    }


    /**
     * Flush bytes or chars contained in the buffer.
     * 
     * @throws IOException An underlying IOException occurred
     */
    protected void doFlush(boolean realFlush)
        throws IOException {

        if (suspended)
            return;

        doFlush = true;
        if (initial){
            addSessionCookieWithJvmRoute();
            response.sendHeaders();
            initial = false;
        }
        if (bb.getLength() > 0) {
            bb.flushBuffer();
        }
        doFlush = false;

        if (realFlush) {
            response.action(ActionCode.ACTION_CLIENT_FLUSH, response);
            // If some exception occurred earlier, or if some IOE occurred
            // here, notify the servlet with an IOE
            if (response.isExceptionPresent()) {
                throw new ClientAbortException
                    (response.getErrorException());
            }
        }

    }


    // ------------------------------------------------- Bytes Handling Methods


    /** 
     * Sends the buffer data to the client output, checking the
     * state of Response and calling the right interceptors.
     * 
     * @param buf Byte buffer to be written to the response
     * @param off Offset
     * @param cnt Length
     * 
     * @throws IOException An underlying IOException occurred
     */
    public void realWriteBytes(byte buf[], int off, int cnt)
	throws IOException {

        if (log.isLoggable(Level.FINE))
            log.fine("realWrite(b, " + off + ", " + cnt + ") " + response);

        if (closed)
            return;
        if (response == null)
            return;

        // If we really have something to write
        if (cnt > 0) {
            addSessionCookieWithJvmRoute();
            // real write to the adapter
            outputChunk.setBytes(buf, off, cnt);
            try {
                response.doWrite(outputChunk);
            } catch (IOException e) {
                // An IOException on a write is almost always due to
                // the remote client aborting the request.  Wrap this
                // so that it can be handled better by the error dispatcher.
                throw new ClientAbortException(e);
            }
        }

    }


    public void write(byte b[], int off, int len) throws IOException {

        if (suspended)
            return;

        writeBytes(b, off, len);

    }


    private void writeBytes(byte b[], int off, int len) 
        throws IOException {

        if (closed)
            return;
        if (log.isLoggable(Level.FINE))
            log.fine("write(b,off,len)");

        bb.append(b, off, len);
        bytesWritten += len;

        // if called from within flush(), then immediately flush
        // remaining bytes
        if (doFlush) {
            bb.flushBuffer();
        }

    }


    // XXX Char or byte ?
    public void writeByte(int b)
        throws IOException {

        if (suspended)
            return;

        bb.append( (byte)b );
        bytesWritten++;

    }


    // ------------------------------------------------- Chars Handling Methods


    public void write(int c)
        throws IOException {

        if (suspended)
            return;

        checkConverter();
        conv.convert((char) c);
        charsWritten++;
    }


    public void write(char c[])
        throws IOException {

        if (suspended)
            return;

        write(c, 0, c.length);

    }


    public void write(char c[], int off, int len)
        throws IOException {

        if (suspended)
            return;

        checkConverter();
        conv.convert(c, off, len);
        charsWritten += len;
    }


    /**
     * Append a string to the buffer
     */
    public void write(String s, int off, int len)
        throws IOException {

        if (suspended)
            return;

        charsWritten += len;
        if (s==null)
            s="null";
        checkConverter();
        conv.convert(s, off, len);
    }


    public void write(String s)
        throws IOException {

        if (suspended)
            return;

        if (s == null)
            s = "null";
        checkConverter();
        conv.convert(s);
    } 


    public void setEncoding(String s) {
        enc = s;
    }

    public void checkConverter() 
        throws IOException {

        if (!gotEnc)
            setConverter();

    }


    protected void setConverter() 
        throws IOException {

        if (response != null)
            enc = response.getCharacterEncoding();

        if (log.isLoggable(Level.FINE))
            log.fine("Got encoding: " + enc);

        gotEnc = true;
        if (enc == null)
            enc = DEFAULT_ENCODING;
        conv = (C2BConverter) encoders.get(enc);
        if (conv == null) {
            if (Globals.IS_SECURITY_ENABLED){
                try{
                    conv = (C2BConverter)AccessController.doPrivileged(
                            new PrivilegedExceptionAction(){

                                public Object run() throws IOException{
                                    return C2BConverter.getInstance(bb, enc);
                                }

                            }
                    );              
                }catch(PrivilegedActionException ex){
                    Exception e = ex.getException();
                    if (e instanceof IOException)
                        throw (IOException)e; 
                    
                    if (log.isLoggable(Level.FINE))
                        log.fine("setConverter: " + ex.getMessage());
                }
            } else {
                conv = C2BConverter.getInstance(bb, enc);
            }
            encoders.put(enc, conv);

        }
    }

    
    // --------------------  BufferedOutputStream compatibility


    /**
     * Real write - this buffer will be sent to the client
     */
    public void flushBytes()
        throws IOException {

        if (log.isLoggable(Level.FINE))
            log.fine("flushBytes() " + bb.getLength());
        bb.flushBuffer();

    }


    public int getBytesWritten() {
        return bytesWritten;
    }


    public int getCharsWritten() {
        return charsWritten;
    }


    public int getContentWritten() {
        return bytesWritten + charsWritten;
    }


    /** 
     * True if this buffer hasn't been used ( since recycle() ) -
     * i.e. no chars or bytes have been added to the buffer.  
     */
    public boolean isNew() {
        return (bytesWritten == 0) && (charsWritten == 0);
    }


    public void setBufferSize(int size) {
        if (size > bb.getLimit()) {
            bb.setLimit(size);
        }
    }


    public void reset() {

        bb.recycle();
        bytesWritten = 0;
        charsWritten = 0;
        gotEnc = false;
        enc = null;
        initial = true;

    }


    public int getBufferSize() {
        return bb.getLimit();
    }

    
    /**
     * Adds JSESSIONID cookie whose value includes jvmRoute if necessary.
     */
    private void addSessionCookieWithJvmRoute() {
        Request req = (Request) coyoteResponse.getRequest();
        if (req.isRequestedSessionIdFromURL()) {
            return;
        }
 
        StandardContext ctx = (StandardContext) coyoteResponse.getContext();
        if (ctx == null || ctx.getJvmRoute() == null || !ctx.getCookies()) {
            return;
        }
    
        Session sess = req.getSessionInternal(false);
        if (sess == null) {
            return;
        }

        // Create JSESSIONID cookie that includes jvmRoute
        Cookie cookie = new Cookie(ctx.getSessionCookieName(),
                                   sess.getIdInternal() + "." +
                                   ctx.getJvmRoute());
        req.configureSessionCookie(cookie);
        response.addHeader(SET_COOKIE_HEADER,
                           coyoteResponse.getCookieString(cookie));
    }

    // START PWC 6512276
    /**
     * Are there any pending writes waiting to be flushed?
     */
    public boolean hasData() {
        if (!suspended && (initial || (bb.getLength() > 0))) {
            return true;
        }

        return false;
    }
    // END PWC 6512276
}
