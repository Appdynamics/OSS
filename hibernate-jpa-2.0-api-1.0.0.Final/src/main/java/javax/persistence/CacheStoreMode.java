// $Id: Cache.java 16130 2009-03-10 14:28:07Z hardy.ferentschik $
// EJB3 Specification Copyright 2004-2009 Sun Microsystems, Inc.
package javax.persistence;

/**
 * Used as the value of the
 * <code>javax.persistence.cache.storeMode</code> property to specify
 * the behavior when data is read from the database and when data is
 * committed into the database.
 *
 * @since Java Persistence 2.0
 */
public enum CacheStoreMode {

    /**
     * Insert/update entity data into cache when read
     * from database and when committed into database:
     * this is the default behavior. Does not force refresh
     * of already cached items when reading from database.
     */
    USE,

    /**
     * Don't insert into cache.
     */
    BYPASS,

    /**
     * Insert/update entity data into cache when read
     * from database and when committed into database.
     * Forces refresh of cache for items read from database.
     */
    REFRESH
}
