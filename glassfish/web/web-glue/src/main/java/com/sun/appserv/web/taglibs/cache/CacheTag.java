/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2011 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.appserv.web.taglibs.cache;

import com.sun.appserv.util.cache.Cache;
import com.sun.logging.LogDomains;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * CacheTag is a JSP tag that allows server-side caching of JSP page
 * fragments. It lets you specify a timeout for how long the cached data
 * is valid. It also gives you programmatic control over key generation,
 * refreshing of the cache and whether the cached content should be served
 * or not.
 *
 * Usage Example:
 * <%@ taglib prefix="ias" uri="Sun ONE Application Server Tags" %> 
 * <ias:cache key="<%= cacheKey %>" usecached="<%= useCached %>"
 *            refresh="<%= reload %>" timeout="3600"> 
 *   ... expensive operation ...
 * </ias:cache> 
 */
public class CacheTag extends BodyTagSupport
{
    /**
     * Constants used to calculate the timeout
     */
    private static final int SECOND = 1;
    private static final int MINUTE = 60 * SECOND;
    private static final int HOUR = 60 * MINUTE;
    private static final int DAY = 24 * HOUR;

    /**
     * User specified key
     */
    private String _keyExpr;

    /**
     * The key into the cache. This is generated by suffixing the servlet
     * path with the key if one is specified or by a generated suffix.
     */
    private String _key;

    /**
     * Timeout for the cache entry.
     */
    private int _timeout = Constants.DEFAULT_JSP_CACHE_TIMEOUT;

    /**
     * This boolean specifies whether the cache should be forcibly refreshed
     * after the current request or not.
     */
    private boolean _refreshCache = false;

    /**
     * This boolean specifies whether the cached response must be sent
     * or the body should be evaluated. The cache is not refreshed.
     */
    private boolean _useCachedResponse = true;

    /**
     * This specifies the scope of the cache.
     */
    private int _scope = PageContext.APPLICATION_SCOPE;

    /**
     * The actual cache itself.
     */
    private Cache _cache;

    /**
     * The logger to use for logging ALL web container related messages.
     */
    private static final Logger _logger = LogDomains.getLogger(
        CacheTag.class, LogDomains.WEB_LOGGER);

    /**
     * The resource bundle containing the localized message strings.
     */
    private static final ResourceBundle _rb = _logger.getResourceBundle();

    // ---------------------------------------------------------------------
    // Tag logic

    /**
     * doStartTag is called every time the cache tag is encountered. By
     * the time this is called, the tag attributes are already set, but
     * the tag body has not been evaluated.
     * The cache key is generated here and the cache is obtained as well
     *
     * @throws JspException the standard exception thrown
     * @return EVAL_BODY_INCLUDE when nocache is specified so that the
     *         tag body is just evaluated into the output stream
     *         SKIP_BODY if the cached response is valid in which case
     *         it is just written to the output stream, hence there is
     *         nothing more to be done.
     *         EVAL_BODY_BUFFERED is the default return value which
     *         ensures that the BodyContent is created and the tag body
     *         is evaluated into it.
     */
    public int doStartTag()
        throws JspException
    {
        // default is EVAL_BODY_BUFFERED to ensure that BodyContent is created
        int ret = EVAL_BODY_BUFFERED;

        // generate the cache key using the user specified key. If no
        // key is specified, a position specific key suffix is used
        _key = CacheUtil.generateKey(_keyExpr, pageContext);

        if (_logger.isLoggable(Level.FINE))
            _logger.fine("CacheTag["+ _key +"]: Timeout = "+ _timeout);

        // if useCachedResponse is false, we do not check for any
        // cached response and just evaluate the tag body
        if (_useCachedResponse) {

            _cache = CacheUtil.getCache(pageContext, _scope);
            if (_cache == null)
                throw new JspException(_rb.getString("taglibs.cache.nocache"));

            // if refreshCache is true, we want to re-evaluate the
            // tag body and refresh the cached entry
            if (_refreshCache == false) {

                // check if an entry is present for the given key
                // if it is, check if it has expired or not
                CacheEntry entry = (CacheEntry)_cache.get(_key);

                if (entry != null && entry.isValid()) {

                    // valid cached entry, get cached response and
                    // write it to the output stream
                    String content = entry.getContent();

                    try {
                        pageContext.getOut().write(content);
                    } catch (java.io.IOException ex) {
                        throw new JspException(ex);
                    }
                         
                    // since cached response is already written, skip
                    // evaluation of the tag body. This also means that
                    // doAfterBody wont get called
                    ret = SKIP_BODY;
                }
            }
        } else {
            // since we dont want to use the cached response, just
            // return EVAL_BODY_INCLUDE which will evaluate the body
            // into the output stream. This will mean that this tag
            // will be treated as an IterationTag and BodyContent is
            // not created
            ret = EVAL_BODY_INCLUDE;
        }

        return ret;
    }

    /**
     * doAfterBody is called only if the body was evaluated. This would happen
     * if nocache is specified in which case this should do nothing
     * if there was no cached response in which case the response data
     * is obtained from the bodyContent and cached
     * if the response has expired in which case the cache is refreshed
     *
     * @throws JspException the standard exception thrown
     * @return always returns SKIP_BODY since we dont do any iteration
     */
    public int doAfterBody()
        throws JspException
    {
        // if useCachedResponse, update the cache with the new response
        // data. If it is false, the body has already been evaluated and
        // sent out, nothing more to be done.
        if (_useCachedResponse) {
            if (bodyContent != null) {

                // get the response as a string from bodyContent
                // and cache it for the specified timeout period
                String content = bodyContent.getString().trim();

                CacheEntry entry = new CacheEntry(content, _timeout);
                _cache.put(_key, entry);

                // write to body content to the enclosing writer as well
                try {
                    bodyContent.writeOut(bodyContent.getEnclosingWriter());
                } catch (java.io.IOException ex) {
                    throw new JspException(ex);
                }
            }
        }
        return SKIP_BODY;
    }

    /**
     * doEndTag just resets all the valiables in case the tag is reused
     *
     * @throws JspException the standard exception thrown
     * @return always returns EVAL_PAGE since we want the entire jsp evaluated
     */
    public int doEndTag()
        throws JspException
    {
        _key = null;
        _keyExpr = null;
        _timeout = Constants.DEFAULT_JSP_CACHE_TIMEOUT;
        _refreshCache = false;
        _useCachedResponse = true;
        _scope = PageContext.APPLICATION_SCOPE;
        _cache = null;

        return EVAL_PAGE;
    }

    // ---------------------------------------------------------------------
    // Attribute setters

    /**
     * This is used to set a user-defined key to store the response in
     * the cache.
     */
    public void setKey(String key) {
        if (key != null && key.length() > 0)
            _keyExpr = key;
    }

    /**
     * This sets the time for which the cached response is valid. The
     * cached entry is invalid after this time is past. If no unit is
     * specified, then the timeout is assumed to be in seconds. A
     * different unit can be specified by postfixing the timeout
     * value with the desired unit:
     *     s=seconds, m=minutes, h=hours, d=days
     */
    public void setTimeout(String timeout) {
        if (timeout != null) {
            try {
                _timeout = Integer.parseInt(timeout);
            } catch (NumberFormatException nfe) {
                // nfe indicated that the timeout has non-integers in it
                // try to parse it as 1sec, 1min, 1 hour and 1day formats
                int i = 0;
                while (i < timeout.length() &&
                       Character.isDigit(timeout.charAt(i)))
                    i++;

                if (i > 0) {
                    _timeout = Integer.parseInt(timeout.substring(0, i));

                    // mutiply timeout by the specified unit of time
                    char multiplier = timeout.charAt(i);
                    switch (multiplier) {
                        case 's' : _timeout *= SECOND;
                                   break;
                        case 'm' : _timeout *= MINUTE;
                                   break;
                        case 'h' : _timeout *= HOUR;
                                   break;
                        case 'd' : _timeout *= DAY;
                                   break;
                        default  : break;
                    }
                }
            }
        }
    }

    /**
     * This attribute is used to programmatically enable or disable the use
     * of the cached response.
     * If noCache is true, then the cached response is not sent, instead
     * the tag body is evaluated and sent out, the cache is not refreshed
     * either.
     */
    public void setNocache(boolean noCache) {
        if (noCache)
            _useCachedResponse = false;
    }

    /**
     * This attribute is used to programmatically refresh the cached
     * response.
     * If refresh is true, the cached response is not sent, instead the
     * tag body is evaluated and sent and the cache is refreshed with the
     * new response.
     */
    public void setRefresh(boolean refresh) {
        _refreshCache = refresh;
    }

    /**
     * Sets the scope of the cache.
     *
     * @param scope the scope of the cache
     *
     * @throws IllegalArgumentException if the specified scope is different
     * from request, session, and application
     */
    public void setScope(String scope) {
        _scope = CacheUtil.convertScope(scope);
    }
}