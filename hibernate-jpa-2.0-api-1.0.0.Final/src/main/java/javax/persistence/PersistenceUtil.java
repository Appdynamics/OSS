// $Id: PersistenceUtil.java 17752 2009-10-15 01:19:21Z steve.ebersole@jboss.com $
// EJB3 Specification Copyright 2004-2009 Sun Microsystems, Inc.
package javax.persistence;

/**
 * Utility interface between the application and the persistence
 * provider(s).
 * <p/>
 * The <code>PersistenceUtil</code> interface instance obtained from the
 * {@link Persistence} class is used to determine the load state of an
 * entity or entity attribute regardless of which persistence
 * provider in the environment created the entity.
 *
 * @since Java Persistence 2.0
 */
public interface PersistenceUtil {
	/**
	 * Determine the load state of a given persistent attribute.
	 *
	 * @param entity entity containing the attribute
	 * @param attributeName name of attribute whose load state is
	 * to be determined
	 *
	 * @return false if entity's state has not been loaded or
	 *         if the attribute state has not been loaded, else true
	 */
	public boolean isLoaded(Object entity, String attributeName);

	/**
	 * Determine the load state of an entity.
	 * This method can be used to determine the load state
	 * of an entity passed as a reference.  An entity is
	 * considered loaded if all attributes for which
	 * <code>FetchType.EAGER</code> has been specified have been loaded.
	 * <p> The <code>isLoaded(Object, String)</code> method should be used to
	 * determine the load state of an attribute.
	 * Not doing so might lead to unintended loading of state.
	 *
	 * @param entity whose load state is to be determined
	 *
	 * @return false if the entity has not been loaded, else true
	 */
	public boolean isLoaded(Object entity);
}
