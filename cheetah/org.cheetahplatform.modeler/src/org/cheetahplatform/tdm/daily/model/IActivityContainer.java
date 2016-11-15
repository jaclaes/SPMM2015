/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm.daily.model;

import java.util.List;

import org.cheetahplatform.common.date.Date;

public interface IActivityContainer {
	List<Activity> getActivities();

	/**
	 * Determine the width available for activities.
	 * 
	 * @return the available width
	 */
	int getAvailableWidth();

	Date getDate();
}
