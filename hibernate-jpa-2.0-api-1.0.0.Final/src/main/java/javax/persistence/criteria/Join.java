// $Id: Join.java 17752 2009-10-15 01:19:21Z steve.ebersole@jboss.com $
// EJB3 Specification Copyright 2004-2009 Sun Microsystems, Inc.
package javax.persistence.criteria;

import javax.persistence.metamodel.Attribute;

/**
 * A join to an entity, embeddable, or basic type.
 *
 * @param <Z> the source type of the join
 * @param <X> the target type of the join
 * @since Java Persistence 2.0
 */
public interface Join<Z, X> extends From<Z, X> {
	/**
	 * Return the metamodel attribute corresponding to the join.
	 *
	 * @return metamodel attribute corresponding to the join
	 */
	Attribute<? super Z, ?> getAttribute();

	/**
	 * Return the parent of the join.
	 *
	 * @return join parent
	 */
	From<?, Z> getParent();

	/**
	 * Return the join type.
	 *
	 * @return join type
	 */
	JoinType getJoinType();
}
