package org.cheetahplatform.common.logging;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         10.08.2009
 */
public interface ILogListener {

	/**
	 * Log the given entry.
	 * 
	 * @param entry
	 *            the entry to be logged
	 */
	void log(AuditTrailEntry entry);

}
