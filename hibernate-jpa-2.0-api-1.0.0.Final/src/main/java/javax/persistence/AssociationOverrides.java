// $Id: AssociationOverrides.java 17752 2009-10-15 01:19:21Z steve.ebersole@jboss.com $
// EJB3 Specification Copyright 2004-2009 Sun Microsystems, Inc.
package javax.persistence;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Used to override mappings of multiple relationship properties or fields.
 *
 * <pre>
 *
 *    Example:
 *
 *    &#064;MappedSuperclass
 *    public class Employee {
 *
 *        &#064;Id protected Integer id;
 *        &#064;Version protected Integer version;
 *        &#064;ManyToOne protected Address address;
 *        &#064;OneToOne protected Locker locker;
 *
 *        public Integer getId() { ... }
 *        public void setId(Integer id) { ... }
 *        public Address getAddress() { ... }
 *        public void setAddress(Address address) { ... }
 *        public Locker getLocker() { ... }
 *        public void setLocker(Locker locker) { ... }
 *        ...
 *    }
 *
 *    &#064;Entity
 *    &#064;AssociationOverrides({
 *        &#064;AssociationOverride(
 *                   name="address",
 *                   joinColumns=&#064;JoinColumn("ADDR_ID")),
 *        &#064;AttributeOverride(
 *                   name="locker",
 *                   joinColumns=&#064;JoinColumn("LCKR_ID"))
 *        })
 *    public PartTimeEmployee { ... }
 * </pre>
 *
 *@see AssociationOverride
 *
 * @since Java Persistence 1.0
 */
@Target({TYPE, METHOD, FIELD})
@Retention(RUNTIME)

public @interface AssociationOverrides {

    /**
     *(Required) The association override mappings that are to be
     * applied to the relationship field or property .
     */
    AssociationOverride[] value();
}
