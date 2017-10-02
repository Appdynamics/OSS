// $Id: SqlResultSetMappings.java 17752 2009-10-15 01:19:21Z steve.ebersole@jboss.com $
// EJB3 Specification Copyright 2004-2009 Sun Microsystems, Inc.
package javax.persistence;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Is used to define one or more {@link SqlResultSetMapping} annotations.
 *
 * @since Java Persistence 1.0
 */
@Target({ TYPE })
@Retention(RUNTIME)
public @interface SqlResultSetMappings {
	/**
	 * One or more <code>SqlResultSetMapping</code> annotations.
	 */
	SqlResultSetMapping[] value();
}
