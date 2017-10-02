// $Id: FieldResult.java 17752 2009-10-15 01:19:21Z steve.ebersole@jboss.com $
// EJB3 Specification Copyright 2004-2009 Sun Microsystems, Inc.
package javax.persistence;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Is used to map the columns specified in the SELECT list
 * of the query to the properties or fields of the entity class.
 *
 * <pre>
 *
 * Example:
 *   Query q = em.createNativeQuery(
 *       "SELECT o.id AS order_id, " +
 *           "o.quantity AS order_quantity, " +
 *           "o.item AS order_item, " +
 *         "FROM Order o, Item i " +
 *         "WHERE (order_quantity > 25) AND (order_item = i.id)",
 *       "OrderResults");
 *
 *   &#064;SqlResultSetMapping(name="OrderResults",
 *       entities={
 *           &#064;EntityResult(entityClass=com.acme.Order.class, fields={
 *               &#064;FieldResult(name="id", column="order_id"),
 *               &#064;FieldResult(name="quantity", column="order_quantity"),
 *               &#064;FieldResult(name="item", column="order_item")})
 *       })
 * </pre>
 *
 * @since Java Persistence 1.0
 */
@Target({})
@Retention(RUNTIME)

public @interface FieldResult {

    /** Name of the persistent field or property of the class. */
    String name();

    /**
     * Name of the column in the SELECT clause - i.e., column
     * aliases, if applicable.
     */
    String column();
}
