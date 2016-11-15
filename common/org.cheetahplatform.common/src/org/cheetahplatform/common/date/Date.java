/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.common.date;

import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MILLISECOND;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.SECOND;

import java.util.Calendar;

public class Date extends AbstractDate {
	private static final long serialVersionUID = -6078611140491910023L;

	public static DateTime dateOnly(java.util.Date date) {
		DateTime dateOnly = new DateTime();
		dateOnly.setTimeInMilliseconds(date.getTime());
		dateOnly.truncateToDate();
		return dateOnly;
	}

	/**
	 * Determine the latest timepoint of the week, i.e., sunday, 23:59.
	 * 
	 * @param date
	 *            the date
	 * @return the latest timepoint
	 */
	public static DateTime weekEnd(Date date) {
		DateTime end = weekStart(date);
		end.plus(new Duration(7, 0, 0, false));
		return end;
	}

	/**
	 * Determine the week's start day for this day, i.e., the first day of the week in which the given day occurs.
	 * 
	 * @param date
	 *            the day
	 * @return the corresponding week's start day
	 */
	public static DateTime weekStart(Date date) {
		Calendar copy = (Calendar) date.getCachedTime().clone();
		int dayOfWeek = copy.get(Calendar.DAY_OF_WEEK);
		int toSubtract = (dayOfWeek + 5) % 7;
		copy.add(Calendar.DAY_OF_MONTH, -toSubtract);
		DateTime dateTime = new DateTime(copy.getTime(), date.isInclusive());
		dateTime.truncateToDate();
		return dateTime;
	}

	public Date() {
		super(true);
		setTimeInMilliseconds(System.currentTimeMillis());
	}

	protected Date(boolean inclusive) {
		super(inclusive);
	}

	public Date(Date date) {
		this(date, true);
	}

	protected Date(Date date, boolean inclusive) {
		super(date, inclusive);

		truncateToDate();
	}

	public Date(int year, int month, int day) {
		this(year, month, day, true);

		truncateToDate();
	}

	/**
	 * Construct a new date.
	 * 
	 * @param year
	 *            the year
	 * @param month
	 *            month, from 0 to 11
	 * @param day
	 *            the day
	 */
	protected Date(int year, int month, int day, boolean inclusive) {
		this(inclusive);

		set(Calendar.YEAR, year);
		set(Calendar.MONTH, month);
		set(Calendar.DAY_OF_MONTH, day);
		truncateToDate();
	}

	/**
	 * Checks whether this date is between the given dates - checks the days only!
	 * 
	 * @param startDate
	 *            the start date
	 * @param endDate
	 *            the end date
	 * @return <code>true</code> if this date is between the given dates, <code>false</code> otherwise
	 */
	public boolean betweenDays(Date startDate, Date endDate) {
		return startDate.compareTo(this) <= 0 && endDate.compareTo(this) >= 0;
	}

	@Override
	public int compareTo(AbstractDate o) {
		if (sameDay((Date) o)) {
			return 0;
		}

		if (getCachedTime().getTimeInMillis() > o.getCachedTime().getTimeInMillis()) {
			return 1;
		}

		return -1;
	}

	public int getDay() {
		return getCachedTime().get(Calendar.DAY_OF_MONTH);
	}

	public int getDifferenceInDays(Date date) {
		long difference = Math.abs(getCachedTime().getTimeInMillis() - date.getTimeInMilliseconds());
		long timeInSeconds = difference / 1000;
		long timeInHours = timeInSeconds / 3600;
		long timeInDays = timeInHours / 24;

		return (int) timeInDays;
	}

	public int getDifferenceInWeeks(Date date) {
		return getDifferenceInDays(date) / 7;
	}

	public int getMonth() {
		return getCachedTime().get(Calendar.MONTH);
	}

	public int getWeek() {
		return getCachedTime().get(Calendar.WEEK_OF_YEAR);
	}

	public int getYear() {
		return getCachedTime().get(Calendar.YEAR);
	}

	public void increaseDaysBy(int amount) {
		add(Calendar.DAY_OF_MONTH, amount);
	}

	public boolean sameDay(Date date) {
		return date.getYear() == getYear() && date.getMonth() == getMonth() && date.getDay() == getDay();
	}

	public boolean sameWeek(Date other) {
		return getYear() == other.getYear() && getWeek() == other.getWeek();
	}

	public void setDay(int day) {
		set(Calendar.DAY_OF_MONTH, day);
	}

	@Override
	public String toString() {
		return getDay() + "." + (getMonth() + 1) + "." + getYear();
	}

	public void truncateToDate() {
		set(HOUR_OF_DAY, 0);
		set(MINUTE, 0);
		set(SECOND, 0);
		set(MILLISECOND, 0);
	}

}
