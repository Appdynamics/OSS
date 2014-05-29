/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright (c) 2010 Oracle and/or its affiliates. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 * 
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 * 
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 * 
 * Contributor(s):
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

package org.glassfish.common.util.admin;

import com.sun.enterprise.util.LocalStringManagerImpl;
import com.sun.logging.LogDomains;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jvnet.hk2.annotations.Scoped;
import org.jvnet.hk2.annotations.Service;
import org.jvnet.hk2.component.Singleton;

/**
 * Coordinates generation and consumption of very-limited-use authentication tokens.
 * <p>
 * Some DAS commands submit admin commands to be run elsewhere - either in
 * another process on the same host or, via ssh, to another host.  Given that it
 * is already executing, the DAS command in progress has already been authenticated (if
 * required).  Therefore we want the soon-to-be submitted commands to also
 * be authenticated, but we do not want to send the username and/or password
 * information that was used to authenticate the currently-running DAS command
 * to the other process for it to use.
 * <p>
 * Instead, the currently-running DAS command can use this service to obtain
 * a one-time authentication token.  The DAS command then includes the token,
 * rather than username/password credentials, in the submitted command.
 * <p>
 * This service records which tokens have been given out but not yet used up.
 * When an admin request arrives with a token, the AdminAdapter consults this
 * service to see if the token is valid and, if so, the AdminAdapter
 * allows the request to run.
 * <p>
 * We allow each token to be used twice, once for retrieving the command
 * metadata and then the second time to execute the command.
 * <p>
 * Tokens have a limited life as measured in time also.  If a token is created
 * but not fully consumed before it expires, then this manager considers the
 * token invalid and removes it from the collection of known valid tokens.
 *
 *                              NOTE
 *
 * Commands that trigger other commands on multiple hosts - such as
 * start-cluster - will need to reuse the authentication token more than twice.
 * For such purposes the code using the token can append a "+" to the token.
 * When such a token is used, this manager does NOT decrement the remaining
 * number of uses.  Rather, it only refreshes the token's expiration time.
 *
 * @author Tim Quinn
 */
@Service
@Scoped(Singleton.class)
public class AuthTokenManager {

    public static final String AUTH_TOKEN_OPTION_NAME = "_authtoken";

    private static final String SUPPRESSED_TOKEN_OUTPUT = "????";

    private final static int TOKEN_SIZE = 10;

    private final static long TOKEN_EXPIRATION_IN_MS = 60 * 1000;

    private final SecureRandom rng = new SecureRandom();

    private final Map<String,TokenInfo> liveTokens = new HashMap<String,TokenInfo>();

    private final Logger logger = LogDomains.getLogger(AuthTokenManager.class,
            LogDomains.ADMIN_LOGGER);

    private final static char REUSE_TOKEN_MARKER = '+';

    private static final LocalStringManagerImpl localStrings =
            new LocalStringManagerImpl(AuthTokenManager.class);

    /* hex conversion stolen shamelessly from Bill's LocalPasswordImpl - maybe refactor to share later */
    private static final char[] hex = {
        '0', '1', '2', '3', '4', '5', '6', '7',
        '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };

    private class TokenInfo {
        private final String token;
        private int usesRemaining = 2; // each token is used once to get metadata, once to execute
        private long expiration = System.currentTimeMillis() + (TOKEN_EXPIRATION_IN_MS);

        private TokenInfo(final String value) {
            this.token = value;
        }

        private synchronized boolean use(final boolean isBeingReused, final long now) {
            if (isUsedUp(now)) {
                final String msg = localStrings.getLocalString("AuthTokenInvalid",
                        "Use of auth token {2} attempted but token is invalid; usesRemaining = {0,number,integer}, expired = {1}",
                        new Integer(usesRemaining), Boolean.toString(expiration <= now),
                        logger.isLoggable(Level.FINER) ? token : SUPPRESSED_TOKEN_OUTPUT);
                
                return false;
            }
            if ( ! isBeingReused) {
                usesRemaining--;
            }
            logger.log(Level.FINER,
                        "Use of auth token {0} OK; isBeingReused = {2}; remaining uses = {1,number,integer}",
                        new Object[] {token, new Integer(usesRemaining), Boolean.toString(isBeingReused)});
            expiration += (TOKEN_EXPIRATION_IN_MS);
            return true;
        }

        private boolean isUsedUp(final long now) {
            return usesRemaining <= 0 || expiration <= now;
        }
    }

    /**
     * Creates a new limited use authentication token.
     * @return auth token
     */
    public String createToken() {
        final byte[] newToken = new byte[TOKEN_SIZE];
        rng.nextBytes(newToken);
        final String token = toHex(newToken);
        liveTokens.put(token, new TokenInfo(token));
        logger.log(Level.FINER, "Auth token {0} created", token);
        return token;
    }

    /**
     * Records the use of an authentication token by an admin request.
     * <p>
     * Just to make it easier for callers, the token value can have any number
     * of trailing reuse markers.  This simplifies the code in RemoteAdminCommand
     * which actually sends two requests for each command: one to retrieve
     * metadata and one to execute the command.  It might be that the command
     * itself might be reusing the token, in which case it will already have
     * appened a reuse marker to it.  Then the code which sends the metadata
     * request can freely append the marker again without having to check if
     * it is already present.
     *
     * @param token the token consumed, with 0 or more cppies of the reuse marker appended
     * @return true if the token was valid (and had remaining uses on it); false otherwise
     */
    public boolean consumeToken(final String token) {
        final long now = System.currentTimeMillis();
        final int firstReuseMarker = token.indexOf(REUSE_TOKEN_MARKER);
        final boolean isReusedToken = (firstReuseMarker != -1);
        final String tokenAsRecorded = (isReusedToken ? token.substring(0, firstReuseMarker) : token);

        final TokenInfo ti = liveTokens.get(tokenAsRecorded);
        if (ti == null) {
            logger.log(Level.WARNING,
                    localStrings.getLocalString(
                        "AuthTokenNonexistent",
                        "Attempt to use non-existent auth token {0}",
                        logger.isLoggable(Level.FINER) ? tokenAsRecorded : SUPPRESSED_TOKEN_OUTPUT)
                        );
        }
        final boolean result =  ti != null && ti.use(isReusedToken, now);

        retireExpiredTokens(now);

        return result;
    }

    public static String markTokenForReuse(final String token) {
        return token + REUSE_TOKEN_MARKER;
    }
    
    private synchronized void retireExpiredTokens(final long now) {
        for (Iterator<Map.Entry<String,TokenInfo>> it = liveTokens.entrySet().iterator(); it.hasNext(); ) {
            final Map.Entry<String,TokenInfo> entry = it.next();
            if (entry.getValue().isUsedUp(now)) {
                logger.log(Level.FINER, "Auth token {0} being retired during scan", entry.getValue().token);
                it.remove();
            }
        }
    }

    /**
     * Convert the byte array to a hex string.
     */
    private static String toHex(byte[] b) {
        char[] bc = new char[b.length * 2];
        for (int i = 0, j = 0; i < b.length; i++) {
            byte bb = b[i];
            bc[j++] = hex[(bb >> 4) & 0xF];
            bc[j++] = hex[bb & 0xF];
        }
        return new String(bc);
    }
}
