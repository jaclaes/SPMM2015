/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.tdd;

import static org.cheetahplatform.tdm.daily.figure.DayTimeLineFigure.END_HOUR;
import static org.cheetahplatform.tdm.daily.figure.DayTimeLineFigure.START_HOUR;
import static org.junit.Assert.assertEquals;

import org.cheetahplatform.common.date.Date;
import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.common.date.TimeSlot;
import org.cheetahplatform.tdm.daily.editpart.PixelTimeConverter;
import org.cheetahplatform.tdm.daily.figure.DayTimeLineFigure;
import org.junit.Test;

public class PixelTimeConverterTest {
	private static final int YEAR = 2009;
	private static final int MONTH = 0;
	private static final int DAY = 1;

	@Test
	public void computeDurationFullOverlap() throws Exception {
		int height = PixelTimeConverter.computeHeight(new Date(YEAR, MONTH, DAY + 1), timeSlot(DAY, 20, 0, DAY + 2, 2, 0));
		assertEquals(height, DayTimeLineFigure.HOUR_HEIGHT * 24);
	}

	@Test
	public void computeDurationPartialDayEnd() throws Exception {
		int height = PixelTimeConverter.computeHeight(new Date(YEAR, MONTH, DAY + 1), timeSlot(DAY, 20, 0, DAY + 1, 2, 0));
		assertEquals(height, DayTimeLineFigure.HOUR_HEIGHT * 2);
	}

	@Test
	public void computeDurationPartialDayStart() throws Exception {
		int height = PixelTimeConverter.computeHeight(new Date(YEAR, MONTH, DAY), timeSlot(DAY, 20, 0, DAY + 1, 2, 0));
		assertEquals(height, DayTimeLineFigure.HOUR_HEIGHT * 4);
	}

	@Test
	public void computeDurationWrongDay() throws Exception {
		int height = PixelTimeConverter.computeHeight(new Date(YEAR, MONTH, DAY + 1), timeSlot(1, 0, 2, 0));
		assertEquals("Wrong day - should have no height.", 0, height);
	}

	@Test
	public void computeYRelative() throws Exception {
		int actual = PixelTimeConverter.computeYRelative(new Date(YEAR, MONTH, DAY), timeSlot(DAY, 2, 0, DAY, 20, 0));
		assertEquals(DayTimeLineFigure.HOUR_HEIGHT * 2, actual);
	}

	@Test
	public void computeYRelativeStart() throws Exception {
		int actual = PixelTimeConverter.computeYRelative(new Date(YEAR, MONTH, DAY + 1), timeSlot(DAY, 2, 0, DAY + 2, 20, 0));
		assertEquals(0, actual);
	}

	private TimeSlot timeSlot(int startHour, int startMinute, int endHour, int endMinute) {
		return timeSlot(DAY, startHour, startMinute, DAY, endHour, endMinute);
	}

	private TimeSlot timeSlot(int startDay, int startHour, int startMinute, int endDay, int endHour, int endMinute) {
		DateTime start = new DateTime(YEAR, MONTH, startDay, startHour, startMinute, true);
		DateTime end = new DateTime(YEAR, MONTH, endDay, endHour, endMinute, false);

		return new TimeSlot(start, end);
	}

	@Test
	public void trimToDayBug() throws Exception {
		TimeSlot original = timeSlot(DAY, 17, 45, DAY, 24, 0);
		TimeSlot slot = PixelTimeConverter.trimToDay(new Date(YEAR, MONTH, DAY), original);

		assertEquals(original, slot);
	}

	@Test
	public void trimToDayFullOverlap() throws Exception {
		TimeSlot original = timeSlot(DAY, 2, 0, DAY + 2, 2, 0);
		TimeSlot slot = PixelTimeConverter.trimToDay(new Date(YEAR, MONTH, DAY + 1), original);
		TimeSlot expected = timeSlot(DAY + 1, START_HOUR, 0, DAY + 1, END_HOUR, 0);

		assertEquals(expected, slot);
	}

	@Test
	public void trimToDayOutOfRange() throws Exception {
		TimeSlot original = timeSlot(1, 0, 2, 0);
		TimeSlot slot = PixelTimeConverter.trimToDay(new Date(YEAR, MONTH, DAY + 1), original);
		assertEquals(null, slot);
	}

	@Test
	public void trimToDayPartialOverlapEnd() throws Exception {
		TimeSlot original = timeSlot(DAY, 2, 0, DAY + 1, 2, 0);
		TimeSlot slot = PixelTimeConverter.trimToDay(new Date(YEAR, MONTH, DAY + 1), original);
		TimeSlot expected = timeSlot(DAY + 1, START_HOUR, 0, DAY + 1, 2, 0);
		assertEquals(expected, slot);
	}

	@Test
	public void trimToDayPartialOverlapStart() throws Exception {
		TimeSlot original = timeSlot(DAY, 2, 0, DAY + 1, 2, 0);
		TimeSlot slot = PixelTimeConverter.trimToDay(new Date(YEAR, MONTH, DAY), original);
		TimeSlot expected = timeSlot(DAY, 2, 0, DAY, END_HOUR, 0);
		assertEquals(expected, slot);
	}

	@Test
	public void trimToDaySameDay() throws Exception {
		TimeSlot original = timeSlot(1, 0, 2, 0);
		TimeSlot slot = PixelTimeConverter.trimToDay(new Date(YEAR, MONTH, DAY), original);
		assertEquals(original, slot);
	}
}
