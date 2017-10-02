// $Id: SecondaryTables.java 17752 2009-10-15 01:19:21Z steve.ebersole@jboss.com $
// EJB3 Specification Copyright 2004-2009 Sun Microsystems, Inc.
package javax.persistence;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Specifies multiple secondary tables for an entity.
 *
 * <pre>
 *    Example 1: Multiple secondary tables assuming primary key columns are named the same in all tables.
 *
 *    &#064;Entity
 *    &#064;Table(name="EMPLOYEE")
 *    &#064;SecondaryTables({
 *        &#064;SecondaryTable(name="EMP_DETAIL"),
 *        &#064;SecondaryTable(name="EMP_HIST")
 *    })
 *    public class Employee { ... }
 *
 *
 *    Example 2: Multiple secondary tables with differently named primary key columns.
 *
 *    &#064;Entity
 *    &#064;Table(name="EMPLOYEE")
 *    &#064;SecondaryTables({
 *        &#064;SecondaryTable(name="EMP_DETAIL",
 *            pkJoinColumns=&#064;PrimaryKeyJoinColumn(name="EMPL_ID")),
 *        &#064;SecondaryTable(name="EMP_HIST",
 *            pkJoinColumns=&#064;PrimaryKeyJoinColumn(name="EMPLOYEE_ID"))
 *    })
 *    public class Employee { ... }
 * </pre>
 *
 * @since Java Persistence 1.0
 */
@Target(TYPE)
@Retention(RUNTIME)

public @interface SecondaryTables {
	/**
	 * (Required) The secondary tables for an entity.
	 */
	SecondaryTable[] value();
}
