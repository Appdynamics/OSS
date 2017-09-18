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
 * @(#)Protocol.java	1.28 05/08/29
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package com.sun.mail.iap;

import java.util.Vector;
import java.util.Properties;
import java.io.*;
import java.net.*;
import com.sun.mail.util.*;

/**
 *
 * @version 1.28, 05/08/29
 * @author  John Mani
 * @author  Max Spivak
 */

public class Protocol {
    protected String host;
    private Socket socket;
    // in case we turn on TLS, we'll need these later
    protected boolean debug;
    protected boolean quote;
    protected PrintStream out;

    private TraceInputStream traceInput; // the Tracer
    private ResponseInputStream input;

    private TraceOutputStream traceOutput; // the Tracer
    private DataOutputStream output;

    private int tagCounter = 0;

    private Vector handlers = null; // response handlers

    private long timestamp;

    protected static final byte[] CRLF = { (byte)'\r', (byte)'\n'};
 
    /**
     * Constructor. <p>
     * 
     * Opens a connection to the given host at given port.
     *
     * @param host	host to connect to
     * @param port	portnumber to connect to
     * @param debug     debug mode
     * @param out	debug output stream
     * @param props     Properties object used by this protocol
     * @param prefix 	Prefix to prepend to property keys
     */
    public Protocol(String host, int port, boolean debug,
		    PrintStream out, Properties props, String prefix,
		    boolean isSSL) throws IOException, ProtocolException {
	this.host = host;
	this.debug = debug;
	this.out = out;

	socket = SocketFetcher.getSocket(host, port, props, prefix, isSSL);
	String s = props.getProperty("mail.debug.quote");
	quote = s != null && s.equalsIgnoreCase("true");

	initStreams(out);

	// Read server greeting
	processGreeting(readResponse());

        timestamp = System.currentTimeMillis();
 
    }

    private void initStreams(PrintStream out) throws IOException {
	traceInput = new TraceInputStream(socket.getInputStream(), out);
	traceInput.setTrace(debug);
	traceInput.setQuote(quote);
	input = new ResponseInputStream(traceInput);

	traceOutput = new TraceOutputStream(socket.getOutputStream(), out);
	traceOutput.setTrace(debug);
	traceOutput.setQuote(quote);
	output = new DataOutputStream(new BufferedOutputStream(traceOutput));
    }

    /**
     * Constructor for debugging.
     */
    public Protocol(InputStream in, OutputStream out, boolean debug)
				throws IOException {
	this.host = "localhost";
	this.debug = debug;
	this.quote = false;
	this.out = System.out;

	// XXX - inlined initStreams, won't allow later startTLS
	traceInput = new TraceInputStream(in, System.out);
	traceInput.setTrace(debug);
	traceInput.setQuote(quote);
	input = new ResponseInputStream(traceInput);

	traceOutput = new TraceOutputStream(out, System.out);
	traceOutput.setTrace(debug);
	traceOutput.setQuote(quote);
	output = new DataOutputStream(new BufferedOutputStream(traceOutput));

        timestamp = System.currentTimeMillis();
    }

    /**
     * Returns the timestamp.
     */
 
    public long getTimestamp() {
        return timestamp;
    }
 
    /**
     * Adds a response handler.
     */
    public synchronized void addResponseHandler(ResponseHandler h) {
	if (handlers == null)
	    handlers = new Vector();
	handlers.addElement(h);
    }

    /**
     * Removed the specified response handler.
     */
    public synchronized void removeResponseHandler(ResponseHandler h) {
	if (handlers != null)
	    handlers.removeElement(h);
    }

    /**
     * Notify response handlers
     */
    public void notifyResponseHandlers(Response[] responses) {
	if (handlers == null)
	    return;
	
	for (int i = 0; i < responses.length; i++) { // go thru responses
	    Response r = responses[i];

	    // skip responses that have already been handled
	    if (r == null)
		continue;

	    int size = handlers.size();
	    if (size == 0)
		return;
	    // Need to copy handlers list because handlers can be removed
	    // when handling a response.
	    Object[] h = new Object[size];
	    handlers.copyInto(h);

	    // dispatch 'em
	    for (int j = 0; j < size; j++)
		((ResponseHandler)h[j]).handleResponse(r);
	}
    }

    protected void processGreeting(Response r) throws ProtocolException {
	if (r.isBYE())
	    throw new ConnectionException(this, r);
    }

    /**
     * Return the Protocol's InputStream.
     */
    protected ResponseInputStream getInputStream() {
	return input;
    }

    /**
     * Return the Protocol's OutputStream
     */
    protected OutputStream getOutputStream() {
	return output;
    }

    /**
     * Returns whether this Protocol supports non-synchronizing literals
     * Default is false. Subclasses should override this if required
     */
    protected boolean supportsNonSyncLiterals() {
	return false;
    }

    public Response readResponse() 
		throws IOException, ProtocolException {
	return new Response(this);
    }

    public String writeCommand(String command, Argument args) 
		throws IOException, ProtocolException {
	String tag = "A" + Integer.toString(tagCounter++, 10); // unique tag

	output.writeBytes(tag + " " + command);
    
	if (args != null) {
	    output.write(' ');
	    args.write(this);
	}

	output.write(CRLF);
	output.flush();
	return tag;
    }

    /**
     * Send a command to the server. Collect all responses until either
     * the corresponding command completion response or a BYE response 
     * (indicating server failure).  Return all the collected responses.
     *
     * @param	command	the command
     * @param	args	the arguments
     * @return		array of Response objects returned by the server
     */
    public synchronized Response[] command(String command, Argument args) {
	Vector v = new Vector();
	boolean done = false;
	String tag = null;
	Response r = null;

	// write the command
	try {
	    tag = writeCommand(command, args);
	} catch (LiteralException lex) {
	    v.addElement(lex.getResponse());
	    done = true;
	} catch (Exception ex) {
	    // Convert this into a BYE response
	    v.addElement(Response.byeResponse(ex));
	    done = true;
	}

	while (!done) {
	    try {
		r = readResponse();
	    } catch (IOException ioex) {
		// convert this into a BYE response
		r = Response.byeResponse(ioex);
	    } catch (ProtocolException pex) {
		continue; // skip this response
	    }
		
	    v.addElement(r);

	    if (r.isBYE()) // shouldn't wait for command completion response
		done = true;

	    // If this is a matching command completion response, we are done
	    if (r.isTagged() && r.getTag().equals(tag))
		done = true;
	}

	Response[] responses = new Response[v.size()];
	v.copyInto(responses);
        timestamp = System.currentTimeMillis();
	return responses;
    }

    /**
     * Convenience routine to handle OK, NO, BAD and BYE responses.
     */
    public void handleResult(Response response) throws ProtocolException {
	if (response.isOK())
	    return;
	else if (response.isNO())
	    throw new CommandFailedException(response);
	else if (response.isBAD())
	    throw new BadCommandException(response);
	else if (response.isBYE()) {
	    disconnect();
	    throw new ConnectionException(this, response);
	}
    }

    /**
     * Convenience routine to handle simple IAP commands
     * that do not have responses specific to that command.
     */
    public void simpleCommand(String cmd, Argument args)
			throws ProtocolException {
	// Issue command
	Response[] r = command(cmd, args);

	// dispatch untagged responses
	notifyResponseHandlers(r);

	// Handle result of this command
	handleResult(r[r.length-1]);
    }

    /**
     * Start TLS on the current connection.
     * <code>cmd</code> is the command to issue to start TLS negotiation.
     * If the command succeeds, we begin TLS negotiation.
     */
    public void startTLS(String cmd) throws IOException, ProtocolException {
	simpleCommand(cmd, null);
	socket = SocketFetcher.startTLS(socket);
	initStreams(out);
    }

    /**
     * Disconnect.
     */
    protected synchronized void disconnect() {
	if (socket != null) {
	    try {
		socket.close();
	    } catch (IOException e) { }
	    socket = null;
	}
    }

    /**
     * Finalizer.
     */
    protected void finalize() throws Throwable {
	super.finalize();
	disconnect();
    }
}
