// $Id: Order.java 17752 2009-10-15 01:19:21Z steve.ebersole@jboss.com $
// EJB3 Specification Copyright 2004-2009 Sun Microsystems, Inc.
package javax.persistence.criteria;

/**
 * An object that defines an ordering over the query results.
 *
 * @since Java Persistence 2.0
 */
public interface Order {
	/**
	 * Switch the ordering.
	 *
	 * @return a new <code>Order</code> instance with the reversed ordering
	 */
	Order reverse();

	/**
	 * Whether ascending ordering is in effect.
	 *
	 * @return boolean indicating whether ordering is ascending
	 */
	boolean isAscending();

	/**
	 * Return the expression that is used for ordering.
	 *
	 * @return expression used for ordering
	 */
	Expression<?> getExpression();
}
