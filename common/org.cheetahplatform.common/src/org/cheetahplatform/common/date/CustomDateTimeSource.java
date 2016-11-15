/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.common.date;

import java.util.Date;


public class CustomDateTimeSource implements IDateTimeSource {

	private static DateTime TIME;

	public CustomDateTimeSource() {
		TIME = new DateTime(new Date(), true);
	}

	@Override
	public DateTime getCurrentTime(boolean inclusive) {
		return new DateTime(TIME, inclusive);
	}

	public void increaseTime(Duration duration) {
		TIME.plus(duration);
	}

	public void setTime(DateTime startTime) {
		TIME = startTime;
	}
}
