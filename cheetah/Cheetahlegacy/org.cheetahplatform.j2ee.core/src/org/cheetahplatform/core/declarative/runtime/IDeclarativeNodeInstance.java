/**
 * 
 */
package org.cheetahplatform.core.declarative.runtime;

import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.core.common.modeling.INodeInstance;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         24.06.2009
 */
public interface IDeclarativeNodeInstance extends INodeInstance {
	/**
	 * Determine the timepoint when the instance ended (exclusive).
	 * 
	 * @return the end time point
	 */
	DateTime getEndTime();

	/**
	 * Determine the timepoint when the instance started (inclusive).
	 * 
	 * @return the start time point
	 */
	DateTime getStartTime();

	/**
	 * Sets the endTime.
	 * 
	 * @param endTime
	 *            the endTime to set
	 */
	void setEndTime(DateTime endTime);

	/**
	 * Sets the startTime.
	 * 
	 * @param startTime
	 *            the startTime to set
	 */
	void setStartTime(DateTime startTime);
}
