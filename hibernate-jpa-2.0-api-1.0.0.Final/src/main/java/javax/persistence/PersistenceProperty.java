// $Id: PersistenceProperty.java 17752 2009-10-15 01:19:21Z steve.ebersole@jboss.com $
// EJB3 Specification Copyright 2004-2009 Sun Microsystems, Inc.
package javax.persistence;

import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

/**
 * Describes a single container or persistence provider property.  Used in {@link
 * PersistenceContext}.
 * <p/>
 * Vendor specific properties may be included in the set of
 * properties, and are passed to the persistence provider by the
 * container when the entity manager is created. Properties that
 * are not recognized by a vendor will be ignored.
 *
 * @since Java Persistence 1.0
 */
@Target({ })
@Retention(RUNTIME)
public @interface PersistenceProperty {
	/**
	 * The name of the property
	 */
	String name();

	/**
	 * The value of the property
	 */
	String value();
}
