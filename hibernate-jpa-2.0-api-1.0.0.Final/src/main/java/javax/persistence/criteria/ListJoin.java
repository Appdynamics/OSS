// $Id: ListJoin.java 17752 2009-10-15 01:19:21Z steve.ebersole@jboss.com $
// EJB3 Specification Copyright 2004-2009 Sun Microsystems, Inc.
package javax.persistence.criteria;

import java.util.List;
import javax.persistence.metamodel.ListAttribute;

/**
 * The <code>ListJoin</code> interface is the type of the result of
 * joining to a collection over an association or element
 * collection that has been specified as a <code>java.util.List</code>.
 *
 * @param <Z> the source type of the join
 * @param <E> the element type of the target List
 * @since Java Persistence 2.0
 */
public interface ListJoin<Z, E>
		extends PluralJoin<Z, List<E>, E> {

	/**
	 * Return the metamodel representation for the list attribute.
	 *
	 * @return metamodel type representing the <code>List</code> that is
	 *         the target of the join
	 */
	ListAttribute<? super Z, E> getModel();

	/**
	 * Create an expression that corresponds to the index of
	 * the object in the referenced association or element
	 * collection.
	 * This method must only be invoked upon an object that
	 * represents an association or element collection for
	 * which an order column has been defined.
	 *
	 * @return expression denoting the index
	 */
	Expression<Integer> index();
}