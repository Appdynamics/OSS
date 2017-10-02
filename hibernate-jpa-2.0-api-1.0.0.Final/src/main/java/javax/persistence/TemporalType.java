// $Id: TemporalType.java 17752 2009-10-15 01:19:21Z steve.ebersole@jboss.com $
// EJB3 Specification Copyright 2004-2009 Sun Microsystems, Inc.
package javax.persistence;

/**
 * Type used to indicate a specific mapping of <code>java.util.Date</code>
 * or <code>java.util.Calendar</code>.
 *
 * @since Java Persistence 1.0
 */
public enum TemporalType {
	/**
	 * Map as <code>java.sql.Date</code>
	 */
	DATE,

	/**
	 * Map as <code>java.sql.Time</code>
	 */
	TIME,

	/**
	 * Map as <code>java.sql.Timestamp</code>
	 */
	TIMESTAMP
}
