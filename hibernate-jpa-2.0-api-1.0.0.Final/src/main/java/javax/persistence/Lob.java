// $Id: Lob.java 17752 2009-10-15 01:19:21Z steve.ebersole@jboss.com $
// EJB3 Specification Copyright 2004-2009 Sun Microsystems, Inc.
package javax.persistence;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Specifies that a persistent property or field should be persisted
 * as a large object to a database-supported large object type.
 *
 * <p> Portable applications should use the <code>Lob</code> annotation
 * when mapping to a database Lob type.  The <code>Lob</code>
 * annotation may be used in conjunction with the {@link Basic}
 * annotation or the {@link ElementCollection} annotation when the
 * element collection value is of basic type. A <code>Lob</code> may
 * be either a binary or character type.
 *
 * <p> The <code>Lob</code> type is inferred from the type of the
 * persistent field or property, and except for string and
 * character-based types defaults to Blob.
 * <pre>
 *
 *   Example 1:
 *
 *   &#064;Lob &#064;Basic(fetch=LAZY)
 *   &#064;Column(name="REPORT")
 *   protected String report;
 *
 *   Example 2:
 *
 *   &#064;Lob &#064;Basic(fetch=LAZY)
 *   &#064;Column(name="EMP_PIC", columnDefinition="BLOB NOT NULL")
 *   protected byte[] pic;
 *
 * </pre>
 *
 * @see Basic
 * @see ElementCollection
 *
 * @since Java Persistence 1.0
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface Lob {
}
