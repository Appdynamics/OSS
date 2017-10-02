// $Id: Bindable.java 17752 2009-10-15 01:19:21Z steve.ebersole@jboss.com $
// EJB3 Specification Copyright 2004-2009 Sun Microsystems, Inc.
package javax.persistence.metamodel;

/**
 * Instances of the type <code>Bindable</code> represent object or attribute types
 * that can be bound into a {@link javax.persistence.criteria.Path Path}.
 *
 * @param <T>  The type of the represented object or attribute
 * @since Java Persistence 2.0
 */
public interface Bindable<T> {
	public static enum BindableType {
		/**
		 * Single-valued attribute type
		 */
		SINGULAR_ATTRIBUTE,

		/**
		 * Multi-valued attribute type
		 */
		PLURAL_ATTRIBUTE,

		/**
		 * Entity type
		 */
		ENTITY_TYPE
	}

	/**
	 * Return the bindable type of the represented object.
	 *
	 * @return bindable type
	 */
	BindableType getBindableType();

	/**
	 * Return the Java type of the represented object.
	 * If the bindable type of the object is <code>PLURAL_ATTRIBUTE</code>,
	 * the Java element type is returned. If the bindable type is
	 * <code>SINGULAR_ATTRIBUTE</code> or <code>ENTITY_TYPE</code>,
	 * the Java type of the
	 * represented entity or attribute is returned.
	 *
	 * @return Java type
	 */
	Class<T> getBindableJavaType();
}
