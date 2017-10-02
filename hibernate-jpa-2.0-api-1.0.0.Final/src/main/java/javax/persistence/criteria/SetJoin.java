// $Id: SetJoin.java 17752 2009-10-15 01:19:21Z steve.ebersole@jboss.com $
// EJB3 Specification Copyright 2004-2009 Sun Microsystems, Inc.
package javax.persistence.criteria;

import java.util.Set;
import javax.persistence.metamodel.SetAttribute;

/**
 * The <code>SetJoin</code> interface is the type of the result of
 * joining to a collection over an association or element
 * collection that has been specified as a <code>java.util.Set</code>.
 *
 * @param <Z> the source type of the join
 * @param <E> the element type of the target <code>Set</code>
 * @since Java Persistence 2.0
 */
public interface SetJoin<Z, E> extends PluralJoin<Z, Set<E>, E> {
	/**
	 * Return the metamodel representation for the set attribute.
	 *
	 * @return metamodel type representing the <code>Set</code> that is
	 *         the target of the join
	 */
	SetAttribute<? super Z, E> getModel();
}
