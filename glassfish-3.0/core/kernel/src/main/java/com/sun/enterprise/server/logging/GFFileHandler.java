/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
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
 */
package com.sun.enterprise.server.logging;

import com.sun.enterprise.admin.monitor.callflow.Agent;
import org.glassfish.internal.api.ServerContext;
import com.sun.enterprise.util.io.FileUtils;
import com.sun.enterprise.v3.common.BooleanLatch;
import com.sun.enterprise.v3.logging.AgentFormatterDelegate;
import org.glassfish.server.ServerEnvironmentImpl;
import com.sun.logging.LogDomains;
import org.glassfish.api.logging.Task;
import org.glassfish.config.support.TranslatedConfigView;
import org.jvnet.hk2.annotations.ContractProvided;
import org.jvnet.hk2.annotations.Inject;
import org.jvnet.hk2.annotations.Scoped;
import org.jvnet.hk2.annotations.Service;
import org.jvnet.hk2.component.PostConstruct;
import org.jvnet.hk2.component.PreDestroy;
import org.jvnet.hk2.component.Singleton;
import com.sun.appserv.server.util.Version;


import java.io.*;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;
import java.util.ResourceBundle;
import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.*;

/**
 * GFFileHandler publishes formatted log Messages to a FILE.
 * 
 * @AUTHOR: Jerome Dochez
 * @AUTHOR: Carla Mott
 */
@Service
@Scoped(Singleton.class)
@ContractProvided(java.util.logging.Handler.class)
public class GFFileHandler extends StreamHandler implements PostConstruct, PreDestroy {


    @Inject
    ServerContext serverContext;

    @Inject
    ServerEnvironmentImpl env;

    @Inject(optional=true)
    Agent agent;

    @Inject
    Version version;

    // This is a OutputStream to keep track of number of bytes
    // written out to the stream
    private MeteredStream meter;
 
    private static final String LOGS_DIR = "logs";
    private String logFileName = "server.log";
    private String absoluteServerLogName = null;

    private File absoluteFile = null;

    private int flushFrequency =1;



    private static final String LOGGING_MAX_HISTORY_FILES = "com.sun.enterprise.server.logging.max_history_files";

    // For now the mimimum rotation value is 0.5 MB.
    private static final int MINIMUM_FILE_ROTATION_VALUE = 500000;

    // Initially the LogRotation will be off until the domain.xml value is read.
    private int limitForFileRotation = 0;

    private BlockingQueue<LogRecord> pendingRecords = new ArrayBlockingQueue<LogRecord>(5000);

    // Rotation can be done in 3 ways
    // 1. Based on the Size: Rotate when some Threshold number of bytes are 
    //    written to server.log
    // 2. Based on the Time: Rotate ever 'n' minutes, mostly 24 hrs
    // 3. Rotate now
    // For mechanisms 2 and 3 we will use this flag. The rotate() will always
    // be fired from the publish( ) method for consistency
    private AtomicBoolean rotationRequested = new AtomicBoolean(false);

    private static final String LOG_ROTATE_DATE_FORMAT =
        "yyyy-MM-dd'T'HH-mm-ss";

    private static final SimpleDateFormat logRotateDateFormatter =
        new SimpleDateFormat( LOG_ROTATE_DATE_FORMAT );

    private BooleanLatch done = new BooleanLatch();
    private Thread pump;
   
    // We maintain a list of the last MAX_RECENT_ERRORS WARNING
    // or SEVERE error messages that were logged. The DAS (or any other 
    // client) can then obtain these messages through the ServerRuntimeMBean
    // and determine if the server instance (or Node Agent) is in an 
    // error state.
    private static final int MAX_RECENT_ERRORS = 4;
    private static ArrayBlockingQueue<LogRecord> recentErrors = new ArrayBlockingQueue<LogRecord>(MAX_RECENT_ERRORS) {

        public void put(LogRecord logRecord) throws InterruptedException {
            if (remainingCapacity()==0) {
               take();
            }
            super.put(logRecord);
        }
        
    };


    public static Iterator<LogRecord> getRecentErrorMessages() {
        Vector<LogRecord> v = new Vector<LogRecord>();
        recentErrors.drainTo(v);
        return v.iterator();
    }
        
    public static void clearRecentErrorMessages() {
        recentErrors.clear();
    }

    public void postConstruct() {

        LogManager manager = LogManager.getLogManager();
        String cname = getClass().getName();
        
        String filename = TranslatedConfigView.getTranslatedValue(manager.getProperty(cname + ".file")).toString();
            
        File serverLog = new File(filename);
        absoluteServerLogName = filename;
        if (!serverLog.isAbsolute()) {
            serverLog = new File(env.getDomainRoot(), filename);
            absoluteServerLogName= env.getDomainRoot() + File.separator + filename;
        }
        changeFileName(serverLog);

        Long rotationTimeLimitValue = 0L;
        try {
            rotationTimeLimitValue = Long.parseLong(manager.getProperty(cname + ".rotationTimelimitInMinutes"));
        } catch (NumberFormatException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE,
                    "Cannot read rotationTimelimitInMinutes property from logging config file");
        }

        if (rotationTimeLimitValue != 0) {

            Task rotationTask = new Task() {
                public Object run() {
                    rotate();
                    return null;
                }
            };

            // If there is a value specified for the rotation based on
            // time we set that first, if not then we will fall back to
            // size based rotation

            LogRotationTimer.getInstance().startTimer(
                    new LogRotationTimerTask(rotationTask,
                            rotationTimeLimitValue));
            // Disable the Size Based Rotation if the Time Based
            // Rotation is set.
            setLimitForRotation(0);
        } else {
            Integer rotationLimitAttrValue = 0;

            try {
                rotationLimitAttrValue = Integer.parseInt(manager.getProperty(cname + ".rotationLimitInBytes"));
            } catch (NumberFormatException e) {
                Logger.getAnonymousLogger().log(Level.SEVERE,
                    "Cannot read rotationLimitInBytes property from logging config file");
            } 
            // We set the LogRotation limit here. The rotation limit is the
            // Threshold for the number of bytes in the log file after which
            // it will be rotated.
            setLimitForRotation(rotationLimitAttrValue);
        }

        setLevel( Level.ALL );
        String ff = manager.getProperty(cname+".flushFrequency");
        if (ff != null)
            flushFrequency = Integer.parseInt(manager.getProperty(cname+".flushFrequency"));
        if (flushFrequency <= 0)
            flushFrequency = 1;

        String formatterName = manager.getProperty(cname + ".formatter");
        if (formatterName==null || UniformLogFormatter.class.getName().equals(formatterName)) {

            // set the formatter
            if (agent!=null) {
                setFormatter( new UniformLogFormatter(new AgentFormatterDelegate(agent)) );
            } else {
                setFormatter( new UniformLogFormatter());
            }
        } else {
            try {
                setFormatter((Formatter) this.getClass().getClassLoader().loadClass(formatterName).newInstance());
            } catch (InstantiationException e) {
                Logger.getAnonymousLogger().log(Level.SEVERE, "Cannot instantiate formatter " + formatterName,e);
            } catch (IllegalAccessException e) {
                Logger.getAnonymousLogger().log(Level.SEVERE, "Cannot instantiate formatter " + formatterName,e);
            } catch (ClassNotFoundException e) {
                Logger.getAnonymousLogger().log(Level.SEVERE, "Cannot load formatter class " + formatterName,e);
            }
        }

        // start the Queue consummer thread.
        pump = new Thread() {
            public void run() {
                try {
                    while (!done.isSignalled()) {
                        log();
                    }
                } catch (RuntimeException e) {

                }               
            }
        };
        pump.start();
        LogRecord lr = new LogRecord(Level.INFO, "Running GlassFish Version: "+version.getFullVersion());
        this.publish(lr);

    }

    public void preDestroy() {
        // stop the Queue consummer thread.
        LogDomains.getLogger(ServerEnvironmentImpl.class, LogDomains.ADMIN_LOGGER).fine("Logger handler killed");
        done.tryReleaseShared(1);
        pump.interrupt();

        // drain and return
        final int size = pendingRecords.size();
        if (size>0) {
            Collection<LogRecord> records = new ArrayList<LogRecord>(size);
            pendingRecords.drainTo(records, size);
            for (LogRecord record : records) {
                super.publish(record);
            }
        }
        
    }
    /**
     *  This method is invoked from LogManager.reInitializeLoggers() to
     *  change the location of the file.
     */
    void changeFileName( File file ) {
        // If the file name is same as the current file name, there
        // is no need to change the filename
        if( file.equals(absoluteFile) ) {
            return;
        }
        synchronized( this ) { 
            super.flush( );
            super.close();
            try {
                openFile( file );
                absoluteFile = file;
            } catch( IOException ix ) {
                new ErrorManager().error( 
                    "FATAL ERROR: COULD NOT OPEN LOG FILE. " +
                    "Please Check to make sure that the directory for " +
                    "Logfile exists. Currently reverting back to use the " +
                    " default server.log", ix, ErrorManager.OPEN_FAILURE );
                try {
                    // Reverting back to the old server.log
                    openFile(absoluteFile);
                } catch( Exception e ) {
                    new ErrorManager().error( 
                        "FATAL ERROR: COULD NOT RE-OPEN SERVER LOG FILE. ", e,
                        ErrorManager.OPEN_FAILURE ); 
                }
            }
        }
    }


    /**
     * A simple getter to access the current log file written by
     * this FileHandler.
     */
    File getCurrentLogFile( ) {
        return absoluteFile;
    }

    /**
     *  A package private method to set the limit for File Rotation.
     */
    synchronized void setLimitForRotation( int rotationLimitInBytes ) {
//        if ((rotationLimitInBytes == 0) ||
//	        (rotationLimitInBytes >= MINIMUM_FILE_ROTATION_VALUE )) {
            limitForFileRotation = rotationLimitInBytes;
//        }
    }
    


    // NOTE: This private class is copied from java.util.logging.FileHandler
    // A metered stream is a subclass of OutputStream that
    //   (a) forwards all its output to a target stream
    //   (b) keeps track of how many bytes have been written
    private final class MeteredStream extends OutputStream {
        OutputStream out;
        long written;

        MeteredStream(OutputStream out, long written) {
            this.out = out;
            this.written = written;
        }

        public void write(int b) throws IOException {
            out.write(b);
            written++;
        }

        public void write(byte buff[]) throws IOException {
            out.write(buff);
            written += buff.length;
        }

        public void write(byte buff[], int off, int len) throws IOException {
            out.write(buff,off,len);
            written += len;
        }

        public void flush() throws IOException {
            out.flush();
        }

        public void close() throws IOException {
            out.close();
        }
    }

    /**
     *  Creates the file and initialized MeteredStream and passes it on to
     *  Superclass (java.util.logging.StreamHandler). 
     */
    private void openFile( File file ) throws IOException {
        // check that the parent directory exists.
        File parent = file.getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }
        FileOutputStream fout = new FileOutputStream( file, true );
        BufferedOutputStream bout = new BufferedOutputStream( fout );
        meter = new MeteredStream( bout, file.length() ); 
        setOutputStream( meter );
    } 

    /**
     * Request Rotation called from Rotation Timer Task or LogMBean
     */
    void requestRotation( ) {
        rotationRequested.set(true);
    }

    /**
     * cleanup the history log file based on JVM system property "max_history_files".
     * 
     * If it is defined with valid number, we only keep that number of history logfiles;
     * If "max_history_files" is defined without value, then default that number to be 10;
     * If "max_history_files" is defined with value 0, no histry log file will
     * be keeped; and server.log is the only log file. 
     */
    public void cleanUpHistoryLogFiles() {
        String nStr = System.getProperty(LOGGING_MAX_HISTORY_FILES);
        if (nStr==null) return;

        int maxHistryFiles = 10;
        if (!"".equals(nStr)) {
            try {
                maxHistryFiles = Integer.parseInt(nStr);
            } catch (NumberFormatException e) {};
        }
        if (maxHistryFiles<0) return;

        File   dir  = absoluteFile.getParentFile();
        if (dir==null) return;

        File[] 	fset = dir.listFiles();
        ArrayList candidates = new ArrayList();
        for (int i=0; fset!=null && i<fset.length; i++) {
            if ( !logFileName.equals(fset[i].getName()) &&
                 fset[i].isFile() && 
                 fset[i].getName().startsWith(logFileName) ) {
                 candidates.add(fset[i].getAbsolutePath() );
            }
        }
        if (candidates.size() <= maxHistryFiles) return;

        Object[] pathes = candidates.toArray();
        java.util.Arrays.sort(pathes);
        try {
            for (int i=0; i<pathes.length-maxHistryFiles; i++) {
		new File((String)pathes[i]).delete();
            }
        } catch (Exception e) {
            new ErrorManager().error("FATAL ERROR: COULD NOT DELETE LOG FILE..", 
                                     e, ErrorManager.GENERIC_FAILURE );
        }
    }


    /**
     * A Simple rotate method to close the old file and start the new one
     * when the limit is reached.
     */
    public void rotate( ) {
        final GFFileHandler thisInstance = this;
        java.security.AccessController.doPrivileged(
            new java.security.PrivilegedAction() {
                public Object run( ) {
                    thisInstance.flush( );
                    thisInstance.close();
                    try {
                        File oldFile = absoluteFile;
                        StringBuffer renamedFileName =  new StringBuffer( absoluteFile + "_" );
                        logRotateDateFormatter.format(
                            new Date(), renamedFileName,
                            new FieldPosition( 0 ) );
                        File rotatedFile = new File( renamedFileName.toString() );
                        boolean renameSuccess = oldFile.renameTo( rotatedFile );
                        if( !renameSuccess ) {
                            // If we don't succeed with file rename which
                            // most likely can happen on Windows because
                            // of multiple file handles opened. We go through
                            // Plan B to copy bytes explicitly to a renamed 
                            // file.
                            FileUtils.copy(absoluteFile,rotatedFile);
                            File freshServerLogFile = getLogFileName();
                            // We do this to make sure that server.log
                            // contents are flushed out to start from a 
                            // clean file again after the rename..
                            FileOutputStream fo = 
                                new FileOutputStream( freshServerLogFile );
                            fo.close( );
                        }
                        openFile(getLogFileName());
                        absoluteFile = getLogFileName();                        
                        // This will ensure that the log rotation timer
                        // will be restarted if there is a value set
                        // for time based log rotation
                        LogRotationTimer.getInstance( ).restartTimer( );

                        cleanUpHistoryLogFiles();
                    } catch( IOException ix ) {
                        publish(new LogRecord(Level.SEVERE,
                            "Error, could not rotate log : " + ix.getMessage()));
                    }
                    return null;
                }
            }
        );
    }


    /**
     * Retrieves the LogRecord from our Queue and store them in the file
     *
     */
    public void log() {

        LogRecord record;

        // take is blocking so we take one record off the queue
        try {
            record = pendingRecords.take();
            super.publish(record);
            if (record.getLevel().intValue()>=Level.WARNING.intValue()) {
                recentErrors.offer(record);
            }
        } catch (InterruptedException e) {
            return;
        }

        // now try to read more.  we end up blocking on the above take call if nothing is in the queue
        Vector<LogRecord> v = new Vector<LogRecord>();
        int msgs = pendingRecords.drainTo(v, flushFrequency );
        for(int j=0;j<msgs; j++) {
             super.publish(v.get(j));
             if (v.get(j).getLevel().intValue()>=Level.WARNING.intValue()) {
                recentErrors.offer(v.get(j));
            }
        }


        flush();       
         if ( ( rotationRequested.get() )
            || ( ( limitForFileRotation > 0 )
                &&  ( meter.written >= limitForFileRotation ) ) )
        {
            // If we have written more than the limit set for the
            // file, or rotation requested from the Timer Task or LogMBean
            // start fresh with a new file after renaming the old file.
            synchronized(rotationRequested) {
                rotate( );
                rotationRequested.set(false);
            }
        }

    }
    /**
     * Publishes the logrecord storing it in our queue
     */ 
    public void publish( LogRecord record ) {

        // the queue has shutdown, we are not processing any more records
        if (done.isSignalled()) {
            return;
        }

        try {
            pendingRecords.add(record);
        } catch(IllegalStateException e) {
            // queue is full, start waiting.
            try {
                pendingRecords.put(record);
            } catch (InterruptedException e1) {
                // too bad, record is lost...
            }
        }
    }

    protected File getLogFileName() {
//        return new File(new File(env.getDomainRoot(),LOGS_DIR), logFileName);
        return new File(absoluteServerLogName);
        
    }
}

