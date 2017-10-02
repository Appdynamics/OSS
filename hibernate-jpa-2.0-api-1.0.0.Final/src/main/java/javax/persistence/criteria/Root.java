// $Id: Root.java 17752 2009-10-15 01:19:21Z steve.ebersole@jboss.com $
// EJB3 Specification Copyright 2004-2009 Sun Microsystems, Inc.
package javax.persistence.criteria;

import javax.persistence.metamodel.EntityType;

/**
 * A root type in the from clause.
 * Query roots always reference entities.
 *
 * @param <X> the entity type referenced by the root
 * @since Java Persistence 2.0
 */
public interface Root<X> extends From<X, X> {

	/**
	 * Return the metamodel entity corresponding to the root.
	 *
	 * @return metamodel entity corresponding to the root
	 */
	EntityType<X> getModel();
}
