// $Id: Selection.java 17752 2009-10-15 01:19:21Z steve.ebersole@jboss.com $
// EJB3 Specification Copyright 2004-2009 Sun Microsystems, Inc.
package javax.persistence.criteria;

import javax.persistence.TupleElement;
import java.util.List;

/**
 * The <code>Selection</code> interface defines an item that is to be
 * returned in a query result.
 *
 * @param <X> the type of the selection item
 * @since Java Persistence 2.0
 */
public interface Selection<X> extends TupleElement<X> {
	/**
	 * Assigns an alias to the selection item.
	 * Once assigned, an alias cannot be changed or reassigned.
	 * Returns the same selection item.
	 *
	 * @param name alias
	 *
	 * @return selection item
	 */
	Selection<X> alias(String name);

	/**
	 * Whether the selection item is a compound selection.
	 *
	 * @return boolean indicating whether the selection is a compound
	 *         selection
	 */
	boolean isCompoundSelection();

	/**
	 * Return the selection items composing a compound selection.
	 * Modifications to the list do not affect the query.
	 *
	 * @return list of selection items
	 *
	 * @throws IllegalStateException if selection is not a
	 * compound selection
	 */
	List<Selection<?>> getCompoundSelectionItems();
}

