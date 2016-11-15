/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.common.modeling;

import org.cheetahplatform.common.date.DateTime;


public interface IActivityInstance extends INodeInstance {
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
}
