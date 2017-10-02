// $Id: CollectionJoin.java 17752 2009-10-15 01:19:21Z steve.ebersole@jboss.com $
// EJB3 Specification Copyright 2004-2009 Sun Microsystems, Inc.
package javax.persistence.criteria;

import java.util.Collection;
import javax.persistence.metamodel.CollectionAttribute;

/**
 * The <code>CollectionJoin</code> interface is the type of the result of
 * joining to a collection over an association or element
 * collection that has been specified as a <code>java.util.Collection</code>.
 *
 * @param <Z> the source type of the join
 * @param <E> the element type of the target <code>Collection</code>
 * @since Java Persistence 2.0
 */
public interface CollectionJoin<Z, E>
		extends PluralJoin<Z, Collection<E>, E> {

	/**
	 * Return the metamodel representation for the collection
	 * attribute.
	 *
	 * @return metamodel type representing the <code>Collection</code> that is
	 *         the target of the join
	 */
	CollectionAttribute<? super Z, E> getModel();
}
