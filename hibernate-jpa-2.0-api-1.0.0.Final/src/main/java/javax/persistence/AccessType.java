// $Id: AccessType.java 17752 2009-10-15 01:19:21Z steve.ebersole@jboss.com $
// EJB3 Specification Copyright 2004-2009 Sun Microsystems, Inc.
package javax.persistence;

/**
 * Is used with the {@link Access} annotation to specify an access
 * type to be applied to an entity class, mapped superclass, or
 * embeddable class, or to a specific attribute of such a class.
 *
 * @see Access
 *
 * @since Java Persistence 2.0
 */
public enum AccessType {

    /** Field-based access is used. */
    FIELD,

    /** Property-based access is used. */
    PROPERTY
}
