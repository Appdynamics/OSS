// $Id: $
// EJB3 Specification Copyright 2004-2009 Sun Microsystems, Inc.
package javax.persistence.metamodel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The <code>StaticMetamodel</code> annotation specifies that the class
 * is a metamodel class that represents the entity, mapped
 * superclass, or embeddable class designated by the value
 * element.
 *
 * @since Java Persistence 2.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface StaticMetamodel {
	/**
	 * Class being modeled by the annotated class.
	 */
	Class<?> value();
}
