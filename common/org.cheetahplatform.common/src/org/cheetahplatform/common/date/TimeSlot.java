/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.common.date;

import org.cheetahplatform.common.Assert;

public class TimeSlot {
	private static final String SEPARATOR = "-";
	private DateTime start;
	private DateTime end;

	public TimeSlot() {
		super();
	}

	public TimeSlot(DateTime start, DateTime end) {
		this.start = start;
		this.end = end;

		validate(start, end);
	}

	public TimeSlot(String input) {
		this(new DateTime(input.split(SEPARATOR)[0]), new DateTime(input.split(SEPARATOR)[1]));
	}

	public TimeSlot(TimeSlot existing) {
		this(existing.getStart(), existing.getEnd());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TimeSlot other = (TimeSlot) obj;
		if (end == null) {
			if (other.end != null)
				return false;
		} else if (end.compareTo(other.end) != 0)
			return false;
		if (start == null) {
			if (other.start != null)
				return false;
		} else if (start.compareTo(other.start) != 0)
			return false;
		return true;
	}

	public int getDurationInMinutes() {
		long startMinutes = start.getTimeInMilliseconds() / 60000;
		// end date is always exclusive --> increase by one to get the real duration
		long endMinutes = (end.getTimeInMilliseconds() + AbstractDate.EXCLUSIVE_OFFSET) / 60000;

		return (int) (endMinutes - startMinutes);
	}

	/**
	 * Returns the end.
	 * 
	 * @return the end
	 */
	public DateTime getEnd() {
		return end;
	}

	/**
	 * Returns the start.
	 * 
	 * @return the start
	 */
	public DateTime getStart() {
		return start;
	}

	public String getTime() {
		return start.getTime() + " - " + end.getTime();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((end == null) ? 0 : end.hashCode());
		result = prime * result + ((start == null) ? 0 : start.hashCode());
		return result;
	}

	public boolean includes(DateTime time) {
		return start.compareTo(time) <= 0 && end.compareTo(time) >= 0;
	}

	public boolean includesDay(Date date) {
		return date.betweenDays(new Date(start), new Date(end));
	}

	public boolean isParallelTo(TimeSlot other) {
		boolean overlapping1 = start.compareTo(other.getEnd()) < 0 && end.compareTo(other.getEnd()) > 0;
		boolean overlapping2 = end.compareTo(other.getStart()) > 0 && start.compareTo(other.getStart()) < 0;
		boolean equal = end.compareTo(other.getEnd()) == 0 && start.compareTo(other.getStart()) == 0;

		boolean isParallel = overlapping1 || overlapping2 || equal;
		return isParallel;
	}

	/**
	 * Sets the end.
	 * 
	 * @param end
	 *            the end to set
	 */
	public void setEnd(DateTime end) {
		validate(start, end);
		this.end = end;
	}

	/**
	 * Sets the start.
	 * 
	 * @param start
	 *            the start to set
	 */
	public void setStart(DateTime start) {
		validate(start, end);
		this.start = start;
	}

	@Override
	public String toString() {
		return start + " to " + end;
	}

	public String toStringRepresentation() {
		return start.toStringRepresentation() + SEPARATOR + end.toStringRepresentation();
	}

	private void validate(DateTime start, DateTime end) {
		Assert.isTrue(start == null || start.isInclusive(), "Start must be inclusive");
		Assert.isTrue(end == null || !end.isInclusive(), "End must be exlusive");

		if (start != null && end != null) {
			Assert.isTrue(start.compareTo(end) <= 0, "Start must be equal or before end");
		}
	}
}
