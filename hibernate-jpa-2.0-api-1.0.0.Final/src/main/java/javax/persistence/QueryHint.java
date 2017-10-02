// $Id: QueryHint.java 17752 2009-10-15 01:19:21Z steve.ebersole@jboss.com $
// EJB3 Specification Copyright 2004-2009 Sun Microsystems, Inc.
package javax.persistence;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Used to supply a query property or hint to the {@link NamedQuery} or {@link
 * NamedNativeQuery} annotation.
 *
 * <p> Vendor-specific hints that are not recognized by a provider are ignored.
 *
 * @since Java Persistence 1.0
 */
@Target({ })
@Retention(RUNTIME)
public @interface QueryHint {
	/**
	 * Name of the hint.
	 */
	String name();

	/**
	 * Value of the hint.
	 */
	String value();
}
