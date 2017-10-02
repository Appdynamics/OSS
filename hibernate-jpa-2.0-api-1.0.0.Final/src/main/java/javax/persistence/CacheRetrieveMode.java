// $Id: Cache.java 16130 2009-03-10 14:28:07Z hardy.ferentschik $
// EJB3 Specification Copyright 2004-2009 Sun Microsystems, Inc.
package javax.persistence;

/**
 * Used as the value of the
 * <code>javax.persistence.cache.retrieveMode</code> property to
 * specify the behavior when data is retrieved by the
 * <code>find</code> methods and by queries.
 *
 * @since Java Persistence 2.0
 */
public enum CacheRetrieveMode {

    /**
     * Read entity data from the cache: this is
     * the default behavior.
     */
    USE,

    /**
     * Bypass the cache: get data directly from
     * the database.
     */
    BYPASS
}

