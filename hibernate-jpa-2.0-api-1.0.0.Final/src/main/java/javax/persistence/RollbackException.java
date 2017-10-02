// $Id: RollbackException.java 17752 2009-10-15 01:19:21Z steve.ebersole@jboss.com $
// EJB3 Specification Copyright 2004-2009 Sun Microsystems, Inc.
package javax.persistence;

/**
 * Thrown by the persistence provider when
 * {@link EntityTransaction#commit() EntityTransaction.commit()} fails.
 *
 * @see javax.persistence.EntityTransaction#commit()
 * @since Java Persistence 1.0
 */
public class RollbackException extends PersistenceException {
	/**
	 * Constructs a new <code>RollbackException</code> exception
	 * with <code>null</code> as its detail message.
	 */
	public RollbackException() {
		super();
	}

	/**
	 * Constructs a new <code>RollbackException</code> exception
	 * with the specified detail message.
	 *
	 * @param message the detail message.
	 */
	public RollbackException(String message) {
		super( message );
	}

	/**
	 * Constructs a new <code>RollbackException</code> exception
	 * with the specified detail message and cause.
	 *
	 * @param message the detail message.
	 * @param cause the cause.
	 */
	public RollbackException(String message, Throwable cause) {
		super( message, cause );
	}

	/**
	 * Constructs a new <code>RollbackException</code> exception
	 * with the specified cause.
	 *
	 * @param cause the cause.
	 */
	public RollbackException(Throwable cause) {
		super(cause);
	}
}
