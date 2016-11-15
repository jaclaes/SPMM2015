/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Locale;

import org.cheetahplatform.common.date.Date;
import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.common.date.Duration;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class DateTest {

	/**
	 * Need to set the locale to at_de, as some tests depend on this setting.
	 */
	private static Locale DEFAULT_LOCALE;

	@AfterClass
	public static void afterClass() {
		Locale.setDefault(DEFAULT_LOCALE);
	}

	@BeforeClass
	public static void beforeClass() {
		DEFAULT_LOCALE = Locale.getDefault();
		Locale.setDefault(Locale.GERMANY);
	}

	@Test
	public void compareTo() throws Exception {
		int actual = new Date(1980, 1, 1).compareTo(new Date(1980, 1, 1));
		assertEquals(0, actual);

		actual = new Date(1980, 1, 1).compareTo(new Date(1980, 1, 2));
		assertEquals(-1, actual);

		actual = new Date(1980, 1, 2).compareTo(new Date(1980, 1, 1));
		assertEquals(1, actual);
	}

	@Test
	public void constructor() throws Exception {
		Date date = new Date(1970, 0, 1);
		boolean hasMilliSeconds = (date.getTimeInMilliseconds() % 1000) != 0;
		assertFalse(hasMilliSeconds);
	}

	@Test
	public void sameWeekSameDate() throws Exception {
		Date date1 = new Date(2009, 5, 29);
		Date date2 = new Date(date1);
		date2.plus(new Duration(7, 0, 0, false));
		assertTrue(date1.sameWeek(date2));

		date2.plus(new Duration(0, 0, 1, false));
		assertFalse(date1.sameWeek(date2));
	}

	@Test
	public void sameWeekSameWeek() throws Exception {
		Date date = new Date();
		assertTrue(date.sameWeek(date));
	}

	@Test
	public void weekEnd() throws Exception {
		Date date = new Date(2009, 7, 10);
		DateTime end = Date.weekEnd(date);
		assertEquals(2009, end.getYear());
		assertEquals(7, end.getMonth());
		assertEquals(16, end.getDay());
		assertEquals(23, end.getHour());
		assertEquals(59, end.getMinute());
		assertEquals(59, end.getSecond());
		assertEquals(999, end.getMilliSeconds());
	}

	@Test
	public void weekStartSameDay() throws Exception {
		Date actual = Date.weekStart(new Date(2009, 5, 29));

		assertEquals(2009, actual.getYear());
		assertEquals(5, actual.getMonth());
		assertEquals(29, actual.getDay());
	}

	@Test
	public void weekStartSunday() throws Exception {
		DateTime actual = Date.weekStart(new Date(2009, 6, 5));

		assertEquals(2009, actual.getYear());
		assertEquals(5, actual.getMonth());
		assertEquals(29, actual.getDay());
		assertEquals(0, actual.getHour());
		assertEquals(0, actual.getMinute());
		assertEquals(0, actual.getMilliSeconds());
	}

	@Test
	public void weekStartSundayBug() throws Exception {
		DateTime actual = Date.weekStart(new Date());
		assertEquals(0, actual.getHour());
		assertEquals(0, actual.getMinute());
		assertEquals(0, actual.getMilliSeconds());
	}
}
