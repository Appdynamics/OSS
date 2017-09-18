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
 * @(#)QuotaAwareStore.java	1.3 06/03/10
 *
 * Copyright 1997-2006 Sun Microsystems, Inc. All Rights Reserved.
 */

package javax.mail;

/**
 * An interface implemented by Stores that support quotas.
 * The {@link #getQuota getQuota} and {@link #setQuota setQuota} methods
 * support the quota model defined by the IMAP QUOTA extension.
 * Refer to <A HREF="http://www.ietf.org/rfc/rfc2087.txt">RFC 2087</A>
 * for more information. <p>
 *
 * @since JavaMail 1.4
 */
public interface QuotaAwareStore {
    /**
     * Get the quotas for the named quota root.
     * Quotas are controlled on the basis of a quota root, not
     * (necessarily) a folder.  The relationship between folders
     * and quota roots depends on the server.  Some servers
     * might implement a single quota root for all folders owned by
     * a user.  Other servers might implement a separate quota root
     * for each folder.  A single folder can even have multiple
     * quota roots, perhaps controlling quotas for different
     * resources.
     *
     * @param	root	the name of the quota root
     * @return		array of Quota objects
     * @exception MessagingException	if the server doesn't support the
     *					QUOTA extension
     */
    Quota[] getQuota(String root) throws MessagingException;

    /**
     * Set the quotas for the quota root specified in the quota argument.
     * Typically this will be one of the quota roots obtained from the
     * <code>getQuota</code> method, but it need not be.
     *
     * @param	quota	the quota to set
     * @exception MessagingException	if the server doesn't support the
     *					QUOTA extension
     */
    void setQuota(Quota quota) throws MessagingException;
}
