// $Id: DiscriminatorType.java 17752 2009-10-15 01:19:21Z steve.ebersole@jboss.com $
// EJB3 Specification Copyright 2004-2009 Sun Microsystems, Inc.
package javax.persistence;

/**
 * Defines supported types of the discriminator column.
 *
 * @since Java Persistence 1.0
 */
public enum DiscriminatorType {

    /**
     * String as the discriminator type.
     */
    STRING,

    /**
     * Single character as the discriminator type.
     */
    CHAR,

    /**
     * Integer as the discriminator type.
     */
    INTEGER
}
