// $Id: LoadState.java 17752 2009-10-15 01:19:21Z steve.ebersole@jboss.com $
// EJB3 Specification Copyright 2004-2009 Sun Microsystems, Inc.
package javax.persistence.spi;

/**
 * Load states returned by the {@link ProviderUtil} SPI methods.
 *
 * @since Java Persistence 2.0
 */
public enum LoadState {
	/**
	 * The state of the element is known to have been loaded.
	 */
	LOADED,
	/**
	 * The state of the element is known not to have been loaded.
	 */
	NOT_LOADED,
	/**
	 * The load state of the element cannot be determined.
	 */
	UNKNOWN
}
