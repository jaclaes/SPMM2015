/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.common.date;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.cheetahplatform.common.Assert;


public class Duration extends DateTime {

	private static final long serialVersionUID = -5685759688020752171L;
	private static final DateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss:SS");

	public Duration(DateTime duration) {
		super(duration);
	}

	public Duration(DateTime start, DateTime end) {
		super(0, 0, true);

		Assert.isTrue(end.compareTo(start) >= 0);
		long difference = end.getTimeInMilliseconds() - start.getTimeInMilliseconds();
		setTimeInMilliseconds(difference);
	}

	public Duration(int hours, int minutes) {
		super(hours, minutes, true);
	}

	public Duration(int days, int hours, int minutes) {
		this(days, hours, minutes, true);
	}

	public Duration(int days, int hours, int minutes, boolean inclusive) {
		super(24 * days + hours, minutes, inclusive);
	}

	@Override
	public int compareTo(AbstractDate o) {
		return getCachedTime().compareTo(o.getCachedTime());
	}

	public int getDurationInWeeks() {
		long differenceInSeconds = getTimeInMilliseconds() / 1000;
		long differenceInDays = differenceInSeconds / (24 * 60 * 60);
		return (int) (differenceInDays / 7);
	}

	public long getMilliseconds() {
		return getCachedTime().getTimeInMillis();
	}

	@Override
	public int getYear() {
		return super.getYear() - 1970;
	}

	@Override
	public String toString() {
		return FORMAT.format(getCachedTime().getTime());
	}
}
