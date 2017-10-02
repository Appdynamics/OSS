// $Id: TransactionRequiredException.java 17752 2009-10-15 01:19:21Z steve.ebersole@jboss.com $
// EJB3 Specification Copyright 2004-2009 Sun Microsystems, Inc.
package javax.persistence;

/**
 * Thrown by the persistence provider when a transaction is required but is not
 * active.
 *
 * @since Java Persistence 1.0
 */
public class TransactionRequiredException extends PersistenceException {
	/**
	 * Constructs a new <code>TransactionRequiredException</code> exception with
	 * <code>null</code> as its detail message.
	 */
	public TransactionRequiredException() {
		super();
	}

	/**
	 * Constructs a new <code>TransactionRequiredException</code> exception with
	 * the specified detail message.
	 *
	 * @param message the detail message.
	 */
	public TransactionRequiredException(String message) {
		super( message );
	}
}
