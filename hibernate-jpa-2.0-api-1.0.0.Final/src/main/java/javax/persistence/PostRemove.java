// $Id: PostRemove.java 17752 2009-10-15 01:19:21Z steve.ebersole@jboss.com $
// EJB3 Specification Copyright 2004-2009 Sun Microsystems, Inc.
package javax.persistence;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Is used to specify callback methods for the corresponding
 * lifecycle event. This annotation may be applied to methods
 * of an entity class, a mapped superclass, or a callback
 * listener class.
 *
 * @since Java Persistence 1.0
 */
@Target({METHOD})
@Retention(RUNTIME)
public @interface PostRemove {
}
