// $Id: PersistenceUnitTransactionType.java 17752 2009-10-15 01:19:21Z steve.ebersole@jboss.com $
// EJB3 Specification Copyright 2004-2009 Sun Microsystems, Inc.
package javax.persistence.spi;

/**
 * Specifies whether entity managers created by the {@link
 * javax.persistence.EntityManagerFactory} will be JTA or
 * resource-local entity managers.
 *
 * @since Java Persistence 1.0
 */
public enum PersistenceUnitTransactionType {
	/**
	 * JTA entity managers will be created.
	 */
	JTA,

	/**
	 * Resource-local entity managers will be created.
	 */
	RESOURCE_LOCAL
}
