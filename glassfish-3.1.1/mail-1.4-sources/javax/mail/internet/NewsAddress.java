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
 * @(#)NewsAddress.java	1.8 05/08/29
 *
 * Copyright 1998-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package javax.mail.internet;

import java.util.Vector;
import java.util.StringTokenizer;
import javax.mail.*;

/**
 * This class models an RFC1036 newsgroup address.
 *
 * @author Bill Shannon
 * @author John Mani
 */

public class NewsAddress extends Address {

    protected String newsgroup;
    protected String host;	// may be null

    private static final long serialVersionUID = -4203797299824684143L;

    /**
     * Default constructor.
     */
    public NewsAddress() { }

    /**
     * Construct a NewsAddress with the given newsgroup.
     *
     * @param newsgroup	the newsgroup
     */
    public NewsAddress(String newsgroup) {
	this(newsgroup, null);
    }

    /**
     * Construct a NewsAddress with the given newsgroup and host.
     *
     * @param newsgroup	the newsgroup
     * @param host	the host
     */
    public NewsAddress(String newsgroup, String host) {
	this.newsgroup = newsgroup;
	this.host = host;
    }

    /**
     * Return the type of this address.  The type of a NewsAddress
     * is "news".
     */
    public String getType() {
	return "news";
    }

    /**
     * Set the newsgroup.
     *
     * @param	newsgroup	the newsgroup
     */
    public void setNewsgroup(String newsgroup) {
	this.newsgroup = newsgroup;
    }

    /**
     * Get the newsgroup.
     *
     * @return	newsgroup
     */
    public String getNewsgroup() {
	return newsgroup;
    }

    /**
     * Set the host.
     *
     * @param	host	the host
     */
    public void setHost(String host) {
	this.host = host;
    }

    /**
     * Get the host.
     *
     * @return	host
     */
    public String getHost() {
	return host;
    }

    /**
     * Convert this address into a RFC 1036 address.
     *
     * @return		newsgroup
     */
    public String toString() {
	return newsgroup;
    }

    /**
     * The equality operator.
     */
    public boolean equals(Object a) {
	if (!(a instanceof NewsAddress))
	    return false;

	NewsAddress s = (NewsAddress)a;
	return newsgroup.equals(s.newsgroup) &&
	    ((host == null && s.host == null) ||
	     (host != null && s.host != null && host.equalsIgnoreCase(s.host)));
    }

    /**
     * Compute a hash code for the address.
     */
    public int hashCode() {
	int hash = 0;
	if (newsgroup != null)
	    hash += newsgroup.hashCode();
	if (host != null)
	    hash += host.toLowerCase().hashCode();
	return hash;
    }

    /**
     * Convert the given array of NewsAddress objects into
     * a comma separated sequence of address strings. The
     * resulting string contains only US-ASCII characters, and
     * hence is mail-safe.
     *
     * @param addresses	array of NewsAddress objects
     * @exception   	ClassCastException, if any address object in the
     *              	given array is not a NewsAddress objects. Note
     *              	that this is a RuntimeException.
     * @return	    	comma separated address strings
     */
    public static String toString(Address[] addresses) {
	if (addresses == null || addresses.length == 0)
	    return null;

	StringBuffer s = 
		new StringBuffer(((NewsAddress)addresses[0]).toString());
	for (int i = 1; i < addresses.length; i++)
	    s.append(",").append(((NewsAddress)addresses[i]).toString());
	
	return s.toString();
    }

    /**
     * Parse the given comma separated sequence of newsgroup into
     * NewsAddress objects.
     *
     * @param newsgroups	comma separated newsgroup string
     * @return			array of NewsAddress objects
     * @exception		AddressException if the parse failed
     */
    public static NewsAddress[] parse(String newsgroups) 
				throws AddressException {
	// XXX - verify format of newsgroup name?
	StringTokenizer st = new StringTokenizer(newsgroups, ",");
	Vector nglist = new Vector();
	while (st.hasMoreTokens()) {
	    String ng = st.nextToken();
	    nglist.addElement(new NewsAddress(ng));
	}
	int size = nglist.size();
	NewsAddress[] na = new NewsAddress[size];
	if (size > 0)
	    nglist.copyInto(na);
	return na;
    }
}
