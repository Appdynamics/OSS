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
package com.sun.enterprise.server.logging.logviewer.backend;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.*;
import javax.management.Attribute;
import javax.management.AttributeList;

import com.sun.enterprise.util.StringUtils;
import com.sun.enterprise.util.SystemPropertyConstants;

/**
 * <p>
 * LogFilter will be used by Admin Too Log Viewer Front End to filter the
 * records. LogMBean delegates the getLogRecordsUsingQuery method to this
 * class static method.
 *
 * @AUTHOR: Hemanth Puttaswamy and Ken Paulsen
 * @AUTHOR: Carla Mott
 */
public class LogFilter {

    // This is the name of the Results Attribute that we send out to the
    // Admin front end.
    private static final String RESULTS_ATTRIBUTE = "Results";


    private static final String NV_SEPARATOR = ";";

    /**
     *  The public method that Log Viewer Front End will be calling on.
     *  The query will be run on records starting from the fromRecord.
     *  If any of the elements for the query is null, then that element will
     *  not be used for the query.  If the user is interested in viewing only
     *  records whose Log Level is SEVERE and WARNING, then the query would
     *  look like:
     *
     *  fromDate = null, toDate = null, logLevel = WARNING, onlyLevel = false,
     *  listOfModules = null, nameValueMap = null.
     *
     *  @param  logFileName     The LogFile to use to run the query. If null
     *                          the current server.log will be used. This is
     *                          not the absolute file name, just the fileName
     *                          needs to be passed. We will use the parent 
     *                          directory of the previous server.log to
     *                          build the absolute file name. 
     *  @param  fromRecord      The location within the LogFile
     *  @param  next            True to get the next set of results, false to
     *                          get the previous set
     *  @param  forward         True to search forward through the log file
     *  @param  requestedCount  The # of desired return values
     *  @param  fromDate        The lower bound date
     *  @param  toDate          The upper bound date
     *  @param  logLevel        The minimum log level to display
     *  @param  onlyLevel       True to only display messsage for "logLevel"
     *  @param  listOfModules   List of modules to match
     *  @param  nameValueMap    NVP's to match
     *
     *  @return
     */
    public static AttributeList getLogRecordsUsingQuery(
        String logFileName, Long fromRecord, Boolean next, Boolean forward,
        Integer requestedCount, Date fromDate, Date toDate,
        String logLevel, Boolean onlyLevel, List listOfModules,
        Properties nameValueMap) 
    {
        LogFile logFile = null;
        if( ( logFileName != null )
          &&( logFileName.length() != 0 ) ) 
        {
            logFile = getLogFile( logFileName );
        } else {
            logFile = getLogFile( );
        } 
        boolean forwd = (forward == null) ? true : forward.booleanValue();
        boolean nxt = (next == null) ? true : next.booleanValue();
        long reqCount = (requestedCount == null) ?
            logFile.getIndexSize() : requestedCount.intValue();
        long startingRecord;
        if (fromRecord == -1) {
            // In this case next/previous (before/after) don't mean much since
            // we don't have a reference record number.  So set before/after
            // according to the direction.
            nxt = forwd;

            // We +1 for reverse so that we see the very end of the file (the
            // query will not go past the "startingRecord", so we have to put
            // it after the end of the file)
            startingRecord = forwd ?
                (-1) :((logFile.getLastIndexNumber()+1)*logFile.getIndexSize());
        } else {
            startingRecord = fromRecord.longValue();
            if (startingRecord < -1) {
                
                throw new IllegalArgumentException(
                    "fromRecord must be greater than 0!");
            }
        }

        // TO DO: If the fromRecord count is zero and the fromDate entry is
        // non-null, then the system should take advantage of file Indexing.
        // It should move the file position to the marker where the DateTime
        // query matches.
        try {
            return fetchRecordsUsingQuery(logFile, startingRecord, nxt, forwd,
                reqCount, fromDate, toDate, logLevel,
                onlyLevel.booleanValue(), listOfModules, nameValueMap);
        } catch (Exception ex) {
            System.err.println( "Exception in fetchRecordsUsingQuer.." + ex );       
            // FIXME: Handle this correctly...
            throw new RuntimeException(ex);
        }
    }


    /**
     *  Internal method that will be called from getLogRecordsUsingQuery()
     */
    protected static AttributeList fetchRecordsUsingQuery(
        LogFile logFile, long startingRecord, boolean next, boolean forward, 
        long requestedCount, Date fromDate, Date toDate, String logLevel, 
        boolean onlyLevel, List listOfModules, Properties nameValueMap) 
    {
        // If !next, then set to search in reverse
        boolean origForward = forward;
        if (next) {
            startingRecord++;
            forward = true;
        } else {
            forward = false;
        }

        // Setup forward/reverse stuff
        int inc = 1;
        int start = 0;  // depends on length of results (for reverse)
        int end = -1;   // depends on length of results (for forward)
        long searchChunkIncrement = requestedCount;
        if (!forward) {
            inc = -1;
            // Move back to find records before the startingRecord
            // -1 because we still want to see the starting record (only if in
            // "next" mode)
            startingRecord -=
                ((next) ? (searchChunkIncrement-1) : (searchChunkIncrement));
            if (startingRecord < 0) {
                // Don't go past the original startingRecord
                searchChunkIncrement += startingRecord;
                startingRecord = 0;
            }
        }

        // Make sure the module names are correct
        //updateModuleList(listOfModules);

        // Keep pulling records to search through until we get enough matches
        List results = new ArrayList();
        List records = null;
        LogFile.LogEntry entry = null;
        while (results.size() < requestedCount) {
            // The following will always return unfiltered forward records
            records = logFile.getLogEntries(
                startingRecord, searchChunkIncrement);
            if (records == null) {
                break;
            }

            // Determine end/start
            if (forward) {
                end = records.size();
            } else {
                start = records.size()-1;
            }

            // Loop through the records, filtering and storing the matches
            for (int count=start; 
                (count != end) && (results.size() < requestedCount); 
                count += inc) 
            {
                entry = (LogFile.LogEntry)records.get(count);
                if (allChecks(entry, fromDate, toDate, logLevel, onlyLevel,
                        listOfModules, nameValueMap)) {
                    results.add(entry);
                }
            }

            // Update startingRecord / searchChunkIncrement & check for finish
            if (forward) {
                // If the record size is smaller than requested, then there
                // are no more records.
                if (records.size() < searchChunkIncrement) {
                    break;
                }

                // Get starting record BEFORE updating searchChunkIncrement to
                // skip all the records we already saw
                startingRecord += searchChunkIncrement*inc;
                searchChunkIncrement = requestedCount-results.size();
            } else {
                // If we already searched from 0, then there are no more
                if (startingRecord == 0) {
                    break;
                }

                // Get starting record AFTER updating searchChunkIncrement
                searchChunkIncrement = requestedCount-results.size();
                startingRecord += searchChunkIncrement*inc;
                if (startingRecord < 1) {
                    searchChunkIncrement += startingRecord;
                    startingRecord = 0;
                }
            }
        }

        // Deal with previous&forward or next&reverse
        if (next ^ origForward) {
            List reversedResults = new ArrayList();
            // Reverse the results
            for (int count=results.size()-1; count>-1; count--) {
                reversedResults.add(results.get(count));
            }
            results = reversedResults;
        }

        // Return the matches.  If this is less than requested, then there are
        // no more.
        return convertResultsToTheStructure( results );
    }

    /**
     *  This method converts the results to the appropriate structure for
     *  LogMBean to return to the Admin Front End.
     *
     *  AttributeList Results contain 2 attributes 
     *
     *  Attribute 1: Contains the Header Information, that lists out all the
     *               Field Names and Positions
     *  Attribute 2: Contains the actual Results, Each Log record is an entry
     *               of this result. The LogRecord itself is an ArrayList of
     *               all fields.
     *
     */
    private static AttributeList convertResultsToTheStructure( List results ) {
        if( results == null ) { return null; }
        AttributeList resultsInTemplate = new AttributeList( );
        resultsInTemplate.add( LogRecordTemplate.getHeader( ) );
        Iterator iterator = results.iterator( ) ; 
        ArrayList listOfResults = new ArrayList( ); 
        Attribute resultsAttribute =  new Attribute( RESULTS_ATTRIBUTE,
            listOfResults );
        resultsInTemplate.add( resultsAttribute );
        while( iterator.hasNext() ) { 
            LogFile.LogEntry entry = (LogFile.LogEntry) iterator.next();
            ArrayList logRecord = new ArrayList( );
            logRecord.add( new Long(entry.getRecordNumber()) );
            logRecord.add( entry.getLoggedDateTime() );
            logRecord.add( entry.getLoggedLevel() );
            logRecord.add( entry.getLoggedProduct() );
            logRecord.add( entry.getLoggedLoggerName() );
            logRecord.add( entry.getLoggedNameValuePairs() );
            logRecord.add( entry.getMessageId() );
            logRecord.add( entry.getLoggedMessage() );
            listOfResults.add( logRecord );
        }
        return resultsInTemplate;
    }      
   
    


    /**
     *  This provides access to the LogFile object.
     */
    public static LogFile getLogFile() {
        return _logFile;
    }

    /**
     * This fetches or updates logFileCache entries.
     * 
     * _REVISIT_: We may want to limit the entries here as each logFile
     * takes up so much of memory to maintain indexes
     */
    public static LogFile getLogFile( String fileName ) {
        // No need to check for null or zero length string as the
        // test is already done before.
        if (fileName.contains("${com.sun.aas.instanceRoot}")){
            String instanceRoot = System.getProperty("com.sun.aas.instanceRoot");
            String f = fileName.replace("${com.sun.aas.instanceRoot}",instanceRoot );
            fileName = f;
        }
        String logFileName = fileName.trim( );
        LogFile logFile = (LogFile) logFileCache.get( fileName );
        String parent = null;
        if( logFile == null ) {
            try { 
                // First check if the fileName provided is an absolute filename
                // if yes, then we don't have to construct the parent element
                // path with the parent.
                if( new File( fileName ).exists( ) ) {
                    logFile = new LogFile( fileName );
                    logFileCache.put( fileName, logFile );
                    return logFile;
                }
                                                                                
                // If the absolute path is not provided, the burden of
                // constructing the parent path falls on us. We try
                // using the default parent path used for the current LogFile.
                // assume the user specified the path from the instance root and that is the parent

                parent = System.getProperty(
                        SystemPropertyConstants.INSTANCE_ROOT_PROPERTY );

            } catch( Exception e ) {
                System.err.println( "Exception " + e + 
                    "thrown in Logviewer backend" );
            }
            if( parent != null ) {
                // Just use the parent directory from the other server.log
                // file.
                String[] logFileNameParts = { parent, logFileName }; 
                logFileName = StringUtils.makeFilePath( 
                    logFileNameParts , false );
            } 
            logFile = new LogFile( logFileName );
            logFileCache.put( fileName, logFile ); 
        }
        return logFile;
    }


    /**
     *
     */
    public static synchronized void setLogFile(LogFile logFile) {
        _logFile=logFile;
    }


    /**
     *  Utility method to replace the Module Names with their actual logger
     *  names.
     */
    protected static void updateModuleList(List listOfModules) {
        if (listOfModules == null) {
            return;
        }
        Iterator iterator = listOfModules.iterator();
        /*
        int index = 0;
        while (iterator.hasNext()) {
            String[] loggerNames = ModuleToLoggerNameMapper.getLoggerNames(
                ((String)iterator.next()).trim());
            if (loggerNames!=null && loggerNames.length>0) {
               listOfModules.set(index, loggerNames[0]);  //todo: support multiple loggers per module
            }
            index++;
        }
        */
    }


    /**
     *  This method accepts the first line of the Log Record and checks
     *  to see if it matches the query.
     */
    protected static boolean allChecks(LogFile.LogEntry entry,
            Date fromDate, Date toDate, String queryLevel, boolean onlyLevel,
            List listOfModules, Properties nameValueMap) {

        if ((dateTimeCheck(entry.getLoggedDateTime(), fromDate, toDate))
           && (levelCheck(entry.getLoggedLevel(), queryLevel, onlyLevel))
           && (moduleCheck(entry.getLoggedLoggerName(), listOfModules))
           && (nameValueCheck(entry.getLoggedNameValuePairs(), nameValueMap)))
        {
            return true;
        }

        return false;
    }


    protected static boolean dateTimeCheck(Date loggedDateTime,
            Date fromDateTime, Date toDateTime) 
    {
        if ((fromDateTime == null) || (toDateTime == null)) {
            // If user doesn't specify fromDate and toDate, then S/He is
            // not interested in DateTime filter
            return true;
        }
        // Now do a range check
        if (!(loggedDateTime.before(fromDateTime) ||
	        loggedDateTime.after(toDateTime))) { 
            return true;
        }

        return false;
    }


    protected static boolean levelCheck(
        final String loggedLevel,
        final String queryLevelIn,
        final boolean isOnlyLevelFlag) 
    {
        // If queryLevel is null, that means user is not interested in
        // running the query on the Log Level field.
        if (queryLevelIn == null) {
            return true;
        }
        final String queryLevel = queryLevelIn.trim();
        
        if (isOnlyLevelFlag) {
            // This means the user is interested in seeing log messages whose
            // log level is equal to what is specified
            if (loggedLevel.equals(queryLevel)) {
                return true;
            }
        } else {
// FIXME: rework this...
            for (int idx=0; idx<LOG_LEVELS.length; idx++) {
                if (loggedLevel.equals(LOG_LEVELS[idx])) {
                    return true;
                }
                if (LOG_LEVELS[idx].equals(queryLevel)) {
                    break;
                }
            }
        }
        return false;
    }

    protected static boolean moduleCheck(String loggerName, List modules) {
        if ((modules == null) || (modules.size() == 0)) {
            return true;
        }

        Iterator iterator = modules.iterator();
        while (iterator.hasNext()) {
            if (loggerName.startsWith((String)iterator.next())) {
                return true;
            }
        }
        return false;
    }

    protected static boolean nameValueCheck(String loggedNameValuePairs,
            Properties queriedNameValueMap) {
        if ( (queriedNameValueMap == null) || ( queriedNameValueMap.size() == 0)) {
            return true;
        }
        if (loggedNameValuePairs == null) {
            // We didn't match the name values...
            return false;
        }
        StringTokenizer nvListTokenizer = 
            new StringTokenizer( loggedNameValuePairs, NV_SEPARATOR );
        while( nvListTokenizer.hasMoreTokens( ) ) {
            String nameandvalue = nvListTokenizer.nextToken( );
            StringTokenizer nvToken = new StringTokenizer( nameandvalue, "=" );
            if (nvToken.countTokens() < 2)
                continue;
            String loggedName = nvToken.nextToken( );
            String loggedValue = nvToken.nextToken( );

            // Reset the iterator to start from the first entry AGAIN
            // FIXME: Is there any other cleaner way to reset the iterator 
            // position to zero than recreating a new iterator everytime
            Iterator queriedNameValueMapIterator = 
                queriedNameValueMap.entrySet().iterator( );

            while( queriedNameValueMapIterator.hasNext( ) ) {
                Map.Entry entry = 
                    (Map.Entry) queriedNameValueMapIterator.next();
                if(entry.getKey().equals( loggedName ) ) {
                    Object value = entry.getValue( );
                    // We have a key with multiple values to match.
                    // This will happen if the match condition is like
                    // _ThreadID=10 or _ThreadID=11 
                    // _REVISIT_: There is an opportunity to improve performance
                    // for this search.
                    Iterator iterator = ((java.util.List) value).iterator( );
                    while( iterator.hasNext( ) ) {
                        if( ((String)iterator.next()).equals( 
                            loggedValue ) ) 
                        {
                            return true;  
                        } 
                    }
                }
            }
        }
        return false;
    }


    protected static final String[] LOG_LEVELS = { "SEVERE", "WARNING",
        "INFO", "CONFIG", "FINE", "FINER", "FINEST" };

    private static SimpleDateFormat SIMPLE_DATE_FORMAT =
        new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    private static String[] serverLogElements = 
        {System.getProperty("com.sun.aas.instanceRoot"), "logs", "server.log"};

    private static String pL = 
            System.getProperty("com.sun.aas.processLauncher");
    private static String verboseMode=
            System.getProperty("com.sun.aas.verboseMode", "false");
    private static String defaultLogFile = 
            System.getProperty("com.sun.aas.defaultLogFile");
    private static LogFile _logFile =
      ( pL != null && !verboseMode.equals("true") && defaultLogFile != null ) ?
         new LogFile( defaultLogFile ) :
         new LogFile( StringUtils.makeFilePath( serverLogElements, false ) );
    private static Hashtable logFileCache = new Hashtable( );
}
