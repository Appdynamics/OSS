// $Id: Embeddable.java 17752 2009-10-15 01:19:21Z steve.ebersole@jboss.com $
// EJB3 Specification Copyright 2004-2009 Sun Microsystems, Inc.
package javax.persistence;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Defines a class whose instances are stored as an intrinsic
 * part of an owning entity and share the identity of the entity.
 * Each of the persistent properties or fields of the embedded
 * object is mapped to the database table for the entity.
 *
 * <p> Note that the {@link Transient} annotation may be used to
 * designate the non-persistent state of an embeddable class.
 *
 * <pre>
 *
 *    Example 1:
 *
 *    &#064;Embeddable public class EmploymentPeriod {
 *       &#064;Temporal(DATE) java.util.Date startDate;
 *       &#064;Temporal(DATE) java.util.Date endDate;
 *      ...
 *    }
 *
 *    Example 2:
 *
 *    &#064;Embeddable public class PhoneNumber {
 *        protected String areaCode;
 *        protected String localNumber;
 *        &#064;ManyToOne PhoneServiceProvider provider;
 *        ...
 *     }
 *
 *    &#064;Entity public class PhoneServiceProvider {
 *        &#064;Id protected String name;
 *         ...
 *     }
 *
 *    Example 3:
 *
 *    &#064;Embeddable public class Address {
 *       protected String street;
 *       protected String city;
 *       protected String state;
 *       &#064;Embedded protected Zipcode zipcode;
 *    }
 *
 *    &#064;Embeddable public class Zipcode {
 *       protected String zip;
 *       protected String plusFour;
 *     }


 * </pre>
 *
 * @since Java Persistence 1.0
 */
@Documented
@Target({TYPE})
@Retention(RUNTIME)
public @interface Embeddable {
}
