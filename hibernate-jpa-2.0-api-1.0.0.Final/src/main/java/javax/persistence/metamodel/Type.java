// $Id: Type.java 17752 2009-10-15 01:19:21Z steve.ebersole@jboss.com $
// EJB3 Specification Copyright 2004-2009 Sun Microsystems, Inc.
package javax.persistence.metamodel;

/**
 * Instances of the type <code>Type</code> represent persistent object
 * or attribute types.
 *
 * @param <X>  The type of the represented object or attribute
 * @since Java Persistence 2.0
 */
public interface Type<X> {
	public static enum PersistenceType {
		/**
		 * Entity
		 */
		ENTITY,

		/**
		 * Embeddable class
		 */
		EMBEDDABLE,

		/**
		 * Mapped superclass
		 */
		MAPPED_SUPERCLASS,

		/**
		 * Basic type
		 */
		BASIC
	}

	/**
	 * Return the persistence type.
	 *
	 * @return persistence type
	 */
	PersistenceType getPersistenceType();

	/**
	 * Return the represented Java type.
	 *
	 * @return Java type
	 */
	Class<X> getJavaType();
}
