// $Id: PersistenceContextType.java 17752 2009-10-15 01:19:21Z steve.ebersole@jboss.com $
// EJB3 Specification Copyright 2004-2009 Sun Microsystems, Inc.
package javax.persistence;

/**
 * Specifies whether a transaction-scoped or extended
 * persistence context is to be used in {@link PersistenceContext}.
 * If not specified, a transaction-scoped persistence context is used.
 *
 * @since Java Persistence 1.0
 */
public enum PersistenceContextType {

	/**
	 * Transaction-scoped persistence context
	 */
	TRANSACTION,

	/**
	 * Extended persistence context
	 */
	EXTENDED
}
