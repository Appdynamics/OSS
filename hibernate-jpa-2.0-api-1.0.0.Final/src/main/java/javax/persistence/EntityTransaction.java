// $Id: EntityTransaction.java 17752 2009-10-15 01:19:21Z steve.ebersole@jboss.com $
// EJB3 Specification Copyright 2004-2009 Sun Microsystems, Inc.
package javax.persistence;

/**
 * Interface used to control transactions on resource-local entity
 * managers.  The {@link EntityManager#getTransaction
 * EntityManager.getTransaction()} method returns the
 * <code>EntityTransaction</code> interface.
 *
 * @since Java Persistence 1.0
 */
public interface EntityTransaction {

     /**
      * Start a resource transaction.
      * @throws IllegalStateException if <code>isActive()</code> is true
      */
     public void begin();

     /**
      * Commit the current resource transaction, writing any
      * unflushed changes to the database.
      * @throws IllegalStateException if <code>isActive()</code> is false
      * @throws RollbackException if the commit fails
      */
     public void commit();

     /**
      * Roll back the current resource transaction.
      * @throws IllegalStateException if <code>isActive()</code> is false
      * @throws PersistenceException if an unexpected error
      *         condition is encountered
      */
     public void rollback();

     /**
      * Mark the current resource transaction so that the only
      * possible outcome of the transaction is for the transaction
      * to be rolled back.
      * @throws IllegalStateException if <code>isActive()</code> is false
      */
     public void setRollbackOnly();

     /**
      * Determine whether the current resource transaction has been
      * marked for rollback.
      * @return boolean indicating whether the transaction has been
      *         marked for rollback
      * @throws IllegalStateException if <code>isActive()</code> is false
      */
     public boolean getRollbackOnly();

     /**
      * Indicate whether a resource transaction is in progress.
      * @return boolean indicating whether transaction is
      *         in progress
      * @throws PersistenceException if an unexpected error
      *         condition is encountered
      */
     public boolean isActive();
}
