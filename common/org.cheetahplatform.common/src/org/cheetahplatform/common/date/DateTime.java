/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.common.date;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;

public class DateTime extends Date {

	private static final long serialVersionUID = 3006563979441120962L;
	private static final String INCLUSIVE_POSTFIX = "inclusive";
	private static final String EXCLUSIVE_POSTFIX = "exclusive";
	private static final String SEPARATOR = "_";
	private static final NumberFormat DECIMAL_FORMAT = new DecimalFormat("00");

	private static boolean extractInclusive(String input) {
		return input.endsWith(INCLUSIVE_POSTFIX);
	}

	private static java.util.Date extractTime(String input) {
		String timeString = input.substring(0, input.indexOf(SEPARATOR));
		long time = Long.parseLong(timeString);

		return new java.util.Date(time);
	}

	public DateTime() {
		super();

		setTimeInMilliseconds(System.currentTimeMillis());
	}

	public DateTime(Date dateTime) {
		super(dateTime.isInclusive());

		setTimeInMilliseconds(dateTime.getTimeInMilliseconds());
	}

	public DateTime(Date date, boolean inclusive) {
		super(date, inclusive);

		int offset = 0;
		if (!inclusive && !date.isInclusive()) {
			offset += EXCLUSIVE_OFFSET;
		}
		setTimeInMilliseconds(date.getTimeInMilliseconds() + offset);
	}

	public DateTime(int hour, int minute, boolean inclusive) {
		this(1970, 0, 1, hour, minute, inclusive);
	}

	public DateTime(int year, int month, int day, int hour, int minute, boolean inclusive) {
		super(year, month, day, inclusive);

		setHour(hour);
		setMinute(minute);
	}

	/**
	 * @param date
	 * @param inclusive
	 */
	public DateTime(java.util.Date date, boolean inclusive) {
		super(inclusive);

		int offset = 0;
		if (!inclusive) {
			offset += EXCLUSIVE_OFFSET;
		}
		setTimeInMilliseconds(date.getTime() + offset);
	}

	public DateTime(String timeAsString) {
		this(extractTime(timeAsString), extractInclusive(timeAsString));
	}

	@Override
	public int compareTo(AbstractDate o) {
		return getCachedTime().compareTo(o.getCachedTime());
	}

	public DateTime copy() {
		return new DateTime(this);
	}

	public void endOfDay() {
		truncateToDate();
		add(Calendar.HOUR, 24);
	}

	/**
	 * Calculates the difference between this {@link DateTime} and the given {@link DateTime}
	 * 
	 * @param dateTime
	 *            the {@link DateTime}
	 * @return the difference between the two {@link DateTime}
	 */
	public Duration getDifference(DateTime dateTime) {
		long time1 = getTimeInMilliseconds();
		long time2 = dateTime.getTimeInMilliseconds();
		long difference = Math.abs(time1 - time2);

		Duration duration = new Duration(0, 0);
		duration.setTimeInMilliseconds(difference);
		return duration;
	}

	public int getHour() {
		return getCachedTime().get(Calendar.HOUR_OF_DAY);
	}

	public int getMilliSeconds() {
		return getCachedTime().get(Calendar.MILLISECOND);
	}

	public int getMinute() {
		return getCachedTime().get(Calendar.MINUTE);
	}

	public int getSecond() {
		return getCachedTime().get(Calendar.SECOND);
	}

	public String getTime() {
		String hours = DECIMAL_FORMAT.format(getHour());
		String minutes = DECIMAL_FORMAT.format(getMinute());
		return hours + ":" + minutes;
	}

	public void increaseMinutesBy(int toIncrease) {
		add(Calendar.MINUTE, toIncrease);
	}

	public void setHour(int hour) {
		set(Calendar.HOUR_OF_DAY, hour);
	}

	public void setMilliSecond(int milliSecond) {
		set(Calendar.MILLISECOND, milliSecond);
	}

	public void setMinute(int minute) {
		set(Calendar.MINUTE, minute);
	}

	public void setSecond(int second) {
		set(Calendar.SECOND, second);
	}

	public void startOfDay() {
		truncateToDate();
	}

	public Date toDate() {
		return new Date(getYear(), getMonth(), getDay());
	}

	public DateTime toInclusive() {
		return new DateTime(this, true);
	}

	@Override
	public String toString() {
		return super.toString() + " " + getHour() + ":" + new DecimalFormat("00").format(getMinute()) + ":"
				+ new DecimalFormat("00").format(getSecond()) + ":" + new DecimalFormat("000").format(getMilliSeconds());
	}

	public String toStringRepresentation() {
		String asString = String.valueOf(getTimeInMilliseconds());
		if (isInclusive()) {
			return asString + SEPARATOR + INCLUSIVE_POSTFIX;
		}

		return asString + SEPARATOR + EXCLUSIVE_POSTFIX;
	}

}
