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
 * @(#)Quota.java	1.6 05/08/29
 *
 * Copyright 2000-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package javax.mail;

import java.util.Vector;

/**
 * This class represents a set of quotas for a given quota root.
 * Each quota root has a set of resources, represented by the
 * <code>Quota.Resource</code> class.  Each resource has a name
 * (for example, "STORAGE"), a current usage, and a usage limit.
 * See RFC 2087.
 *
 * @since JavaMail 1.4
 * @version 1.6, 05/08/29
 * @author  Bill Shannon
 */

public class Quota {

    /**
     * An individual resource in a quota root.
     *
     * @since JavaMail 1.4
     */
    public static class Resource {
	/** The name of the resource. */
	public String name;
	/** The current usage of the resource. */
	public long usage;
	/** The usage limit for the resource. */
	public long limit;

	/**
	 * Construct a Resource object with the given name,
	 * usage, and limit.
	 *
	 * @param	name	the resource name
	 * @param	usage	the current usage of the resource
	 * @param	limit	the usage limit for the resource
	 */
	public Resource(String name, long usage, long limit) {
	    this.name = name;
	    this.usage = usage;
	    this.limit = limit;
	}
    }

    /**
     * The name of the quota root.
     */
    public String quotaRoot;

    /**
     * The set of resources associated with this quota root.
     */
    public Quota.Resource[] resources;

    /**
     * Create a Quota object for the named quotaroot with no associated
     * resources.
     *
     * @param	quotaRoot	the name of the quota root
     */
    public Quota(String quotaRoot) {
	this.quotaRoot = quotaRoot;
    }

    /**
     * Set a resource limit for this quota root.
     *
     * @param	name	the name of the resource
     * @param	limit	the resource limit
     */
    public void setResourceLimit(String name, long limit) {
	if (resources == null) {
	    resources = new Quota.Resource[1];
	    resources[0] = new Quota.Resource(name, 0, limit);
	    return;
	}
	for (int i = 0; i < resources.length; i++) {
	    if (resources[i].name.equalsIgnoreCase(name)) {
		resources[i].limit = limit;
		return;
	    }
	}
	Quota.Resource[] ra = new Quota.Resource[resources.length + 1];
	System.arraycopy(resources, 0, ra, 0, resources.length);
	ra[ra.length - 1] = new Quota.Resource(name, 0, limit);
	resources = ra;
    }
}
