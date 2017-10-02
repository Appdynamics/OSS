// $Id: NamedNativeQueries.java 17752 2009-10-15 01:19:21Z steve.ebersole@jboss.com $
// EJB3 Specification Copyright 2004-2009 Sun Microsystems, Inc.
package javax.persistence;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Used to specify multiple native SQL named queries.  Query names
 * are scoped to the persistence unit.  The <code>NamedNativeQueries</code>
 * annotation can be applied to an entity or mapped superclass.
 *
 * @see NamedNativeQuery
 *
 * @since Java Persistence 1.0
 */
@Target({TYPE})
@Retention(RUNTIME)
public @interface NamedNativeQueries {

    /** (Required) Array of <code>NamedNativeQuery</code> annotations. */
    NamedNativeQuery[] value ();
}
