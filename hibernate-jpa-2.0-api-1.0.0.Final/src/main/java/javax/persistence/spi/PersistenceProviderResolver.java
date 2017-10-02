// $Id: PersistenceProviderResolver.java 17752 2009-10-15 01:19:21Z steve.ebersole@jboss.com $
// EJB3 Specification Copyright 2004-2009 Sun Microsystems, Inc.
package javax.persistence.spi;

import java.util.List;

/**
 * Determine the list of persistence providers available in the
 * runtime environment.
 *
 * <p> Implementations must be thread-safe.
 *
 * <p> Note that the <code>getPersistenceProviders</code> method can potentially
 * be called many times: it is recommended that the implementation
 * of this method make use of caching.
 *
 * @see PersistenceProvider
 * @since Java Persistence 2.0
 */
public interface PersistenceProviderResolver {
	/**
	 * Returns a list of the <code>PersistenceProvider</code> implementations
	 * available in the runtime environment.
	 *
	 * @return list of the persistence providers available
	 *         in the environment
	 */
	List<PersistenceProvider> getPersistenceProviders();

	/**
	 * Clear cache of providers.
	 */
	void clearCachedProviders();
}
