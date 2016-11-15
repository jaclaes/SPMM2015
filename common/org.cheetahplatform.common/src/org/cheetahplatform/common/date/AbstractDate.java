/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.common.date;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public abstract class AbstractDate implements Comparable<AbstractDate>, Serializable {
	private static final long serialVersionUID = -6115885905907394780L;
	public static final int EXCLUSIVE_OFFSET = 1;

	private Calendar calendar;
	private boolean inclusive;

	/**
	 * Calendar including time for inclusive / exclusive.
	 */
	private Calendar cachedTime;

	protected AbstractDate(AbstractDate initial, boolean inclusive) {
		this(inclusive);

		calendar.setTimeInMillis(initial.calendar.getTimeInMillis());
	}

	protected AbstractDate(boolean inclusive) {
		this.inclusive = inclusive;
		this.calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		long offset = 0;
		if (TimeZone.getDefault().inDaylightTime(new Date())) {
			offset = -3600000;
		}

		this.calendar.setTimeInMillis(offset);
	}

	protected void add(int field, int amount) {
		cachedTime = null;

		// need to work on the same time as the cached time, otherwise
		// unexpected behavior may occur
		if (!inclusive) {
			calendar.setTimeInMillis(calendar.getTimeInMillis() - EXCLUSIVE_OFFSET);
		}

		calendar.add(field, amount);

		if (!inclusive) {
			calendar.setTimeInMillis(calendar.getTimeInMillis() + EXCLUSIVE_OFFSET);
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!AbstractDate.class.isAssignableFrom(obj.getClass()))
			return false;
		AbstractDate other = (AbstractDate) obj;
		if (!getCachedTime().equals(other.getCachedTime()))
			return false;
		if (inclusive != other.inclusive)
			return false;
		return true;
	}

	/**
	 * Cached time, including the offset for exclusive times if necessary. Do not use this method for setting the time as the cached time
	 * may be reseted.
	 * 
	 * @return the cached time
	 */
	protected Calendar getCachedTime() {
		if (cachedTime == null) {
			cachedTime = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
			if (inclusive) {
				cachedTime.setTimeInMillis(calendar.getTimeInMillis());
			} else {
				cachedTime.setTimeInMillis(calendar.getTimeInMillis() - EXCLUSIVE_OFFSET);
			}
		}

		return cachedTime;
	}

	public long getTimeInMilliseconds() {
		return getCachedTime().getTimeInMillis();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + calendar.hashCode();
		result = prime * result + (inclusive ? 1231 : 1237);
		return result;
	}

	/**
	 * Returns the inclusive.
	 * 
	 * @return the inclusive
	 */
	public boolean isInclusive() {
		cachedTime = null;
		return inclusive;
	}

	/**
	 * Decreases the current time by the given {@link Duration}.
	 * 
	 * @param duration
	 *            the {@link Duration}
	 * @return
	 */
	public AbstractDate minus(Duration duration) {
		long toAdd = duration.getMilliseconds();
		setTimeInMilliseconds(calendar.getTimeInMillis() - toAdd);
		return this;
	}

	public AbstractDate plus(Duration duration) {
		long toAdd = duration.getMilliseconds();
		setTimeInMilliseconds(calendar.getTimeInMillis() + toAdd);
		return this;
	}

	protected void set(int field, int value) {
		cachedTime = null;
		calendar.set(field, value);
	}

	/**
	 * Sets the inclusive.
	 * 
	 * @param inclusive
	 *            the inclusive to set
	 */
	public void setInclusive(boolean inclusive) {
		this.inclusive = inclusive;
	}

	public void setTimeInMilliseconds(long timeInMilliseconds) {
		calendar.setTimeInMillis(timeInMilliseconds);
		cachedTime = null;
	}

	public Date toJavaUtilDate() {
		long offset = TimeZone.getDefault().getOffset(getCachedTime().getTimeInMillis());
		return new Date(getTimeInMilliseconds() - offset);
	}
}
