// $Id: PersistenceContexts.java 17752 2009-10-15 01:19:21Z steve.ebersole@jboss.com $
// EJB3 Specification Copyright 2004-2009 Sun Microsystems, Inc.
package javax.persistence;

import static java.lang.annotation.ElementType.TYPE;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

/**
 * Declares one or more {@link PersistenceContext} annotations.
 * It is used to express a dependency on container-managed
 * entity manager persistence contexts.
 *
 *@see PersistenceContext
 *
 * @since Java Persistence 1.0
 */
@Target({TYPE})
@Retention(RUNTIME)
public @interface PersistenceContexts {

    /** (Required) One or more <code>PersistenceContext</code> annotations. */
    PersistenceContext[] value();

}
