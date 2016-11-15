/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.common.modeling;

import org.cheetahplatform.common.date.Duration;

public interface IActivity {
	/**
	 * Determine the expected duration for the activity.
	 * 
	 * @return the expected duration
	 */
	Duration getExpectedDuration();
}
