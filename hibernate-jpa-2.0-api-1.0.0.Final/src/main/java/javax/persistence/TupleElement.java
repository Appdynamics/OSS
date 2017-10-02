// $Id: $
// EJB3 Specification Copyright 2004-2009 Sun Microsystems, Inc.
package javax.persistence;

/**
 * The <code>TupleElement</code> interface defines an element that is returned in
 * a query result tuple.
 *
 * @param <X> the type of the element
 * @see Tuple
 * @since Java Persistence 2.0
 */
public interface TupleElement<X> {
	/**
	 * Return the Java type of the tuple element.
	 *
	 * @return the Java type of the tuple element
	 */
	Class<? extends X> getJavaType();

	/**
	 * Return the alias assigned to the tuple element or null,
	 * if no alias has been assigned.
	 *
	 * @return alias
	 */
	String getAlias();
}
