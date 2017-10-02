// $Id: JoinType.java 17752 2009-10-15 01:19:21Z steve.ebersole@jboss.com $
// EJB3 Specification Copyright 2004-2009 Sun Microsystems, Inc.
package javax.persistence.criteria;

/**
 * Defines the three types of joins.
 *
 * Right outer joins and right outer fetch joins are not required
 * to be supported in Java Persistence 2.0.  Applications that use
 * <code>RIGHT</code> join types will not be portable.
 *
 * @since Java Persistence 2.0
 */
public enum JoinType {
	/**
	 * Inner join.
	 */
	INNER,

	/**
	 * Left outer join.
	 */
	LEFT,

	/**
	 * Right outer join.
	 */
	RIGHT
}
