// $Id: EntityExistsException.java 17752 2009-10-15 01:19:21Z steve.ebersole@jboss.com $
// EJB3 Specification Copyright 2004-2009 Sun Microsystems, Inc.
package javax.persistence;
/**
 * Thrown by the persistence provider when {@link EntityManager#persist(Object)
 * EntityManager.persist(Object)} is called and the entity already exists. The
 * current transaction, if one is active, will be marked for rollback.
 * <p>
 * If the entity already exists, the <code>EntityExistsException</code> may be thrown when
 * the persist operation is invoked, or the <code>EntityExistsException</code> or another
 * <code>PersistenceException</code> may be thrown at flush or commit time.
 * <p> The current transaction, if one is active, will be marked for rollback.
 *
 * @see javax.persistence.EntityManager#persist(Object)
 *
 * @since Java Persistence 1.0
 */
public class EntityExistsException extends PersistenceException {

    /**
     * Constructs a new <code>EntityExistsException</code> exception with
     * <code>null</code> as its detail message.
     */
    public EntityExistsException() {
        super();
    }

    /**
     * Constructs a new <code>EntityExistsException</code> exception with the
     * specified detail message.
     *
     * @param message
     *            the detail message.
     */
    public EntityExistsException(String message) {
        super(message);
    }

    /**
     * Constructs a new <code>EntityExistsException</code> exception with the
     * specified detail message and cause.
     *
     * @param message
     *            the detail message.
     * @param cause
     *            the cause.
     */
    public EntityExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new <code>EntityExistsException</code> exception with the
     * specified cause.
     *
     * @param cause
     *            the cause.
     */
    public EntityExistsException(Throwable cause) {
        super(cause);
    }
}
