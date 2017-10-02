// $Id: UniqueConstraint.java 17752 2009-10-15 01:19:21Z steve.ebersole@jboss.com $
// EJB3 Specification Copyright 2004-2009 Sun Microsystems, Inc.
package javax.persistence;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Specifies that a unique constraint is to be included in
 * the generated DDL for a primary or secondary table.
 *
 * <pre>
 *    Example:
 *    &#064;Entity
 *    &#064;Table(
 *        name="EMPLOYEE",
 *        uniqueConstraints=
 *            &#064;UniqueConstraint(columnNames={"EMP_ID", "EMP_NAME"})
 *    )
 *    public class Employee { ... }
 * </pre>
 *
 * @since Java Persistence 1.0
 */
@Target({ })
@Retention(RUNTIME)
public @interface UniqueConstraint {
	/**
	 * (Optional) Constraint name.  A provider-chosen name will be chosen
	 * if a name is not specified.
	 *
	 * @since Java Persistence 2.0
	 */
	String name() default "";

	/**
	 * (Required) An array of the column names that make up the constraint.
	 */
	String[] columnNames();
}
