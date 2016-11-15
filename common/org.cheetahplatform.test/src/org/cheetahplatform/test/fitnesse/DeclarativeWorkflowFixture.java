/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.fitnesse;

import org.cheetahplatform.common.date.Duration;

import fit.ActionFixture;

public class DeclarativeWorkflowFixture extends ActionFixture {

	protected Duration extractDuration(String timeString) {
		String[] splittedTimeString = timeString.split(":");
		int hours = Integer.parseInt(splittedTimeString[0]);
		int minutes = Integer.parseInt(splittedTimeString[1]);
	
		Duration duration = new Duration(hours, minutes);
		return duration;
	}

}
