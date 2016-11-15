/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.core;

import static org.junit.Assert.assertEquals;

import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.common.date.Duration;
import org.junit.Test;

public class DateTimeTest {
	@Test
	public void add1() throws Exception {
		DateTime time = new DateTime(1970, 0, 2, 0, 0, false);

		time.plus(new Duration(24, 0));
		assertEquals(1970, time.getYear());
		assertEquals(0, time.getMonth());
		assertEquals(2, time.getDay());
		assertEquals(23, time.getHour());
		assertEquals(59, time.getMinute());
	}

	@Test
	public void add2() throws Exception {
		DateTime time = new DateTime(1, 0, true);
		assertEquals(1, time.getHour());
	}

	@Test
	public void bug() throws Exception {
		DateTime dateTime = new DateTime(1970, 0, 1, 0, 0, true);
		assertEquals(1970, dateTime.getYear());
	}

	@Test
	public void compareTo() throws Exception {
		int actual = new DateTime(1, 0, true).compareTo(new DateTime(1, 0, true));
		assertEquals(0, actual);

		actual = new DateTime(1, 0, false).compareTo(new DateTime(1, 0, true));
		assertEquals(-1, actual);

		actual = new DateTime(1, 0, true).compareTo(new DateTime(1, 0, false));
		assertEquals(1, actual);
	}

	@Test
	public void exclusive() throws Exception {
		DateTime time = new DateTime(1970, 0, 1, 0, 00, false);

		assertEquals(1969, time.getYear());
		assertEquals(11, time.getMonth());
		assertEquals(31, time.getDay());
		assertEquals(23, time.getHour());
		assertEquals(59, time.getMinute());
	}

	@Test
	public void inclusive() throws Exception {
		DateTime time = new DateTime(1970, 0, 1, 24, 00, true);

		assertEquals(1970, time.getYear());
		assertEquals(0, time.getMonth());
		assertEquals(2, time.getDay());
		assertEquals(0, time.getHour());
		assertEquals(0, time.getMinute());
	}

	@Test
	public void minus() throws Exception {
		DateTime time = new DateTime(1970, 0, 3, 0, 0, false);

		time.minus(new Duration(24, 0));
		assertEquals(1970, time.getYear());
		assertEquals(0, time.getMonth());
		assertEquals(1, time.getDay());
		assertEquals(23, time.getHour());
		assertEquals(59, time.getMinute());
	}

	@Test
	public void noDifference() throws Exception {
		DateTime time1 = new DateTime(1970, 0, 1, 0, 00, false);
		DateTime time2 = new DateTime(1970, 0, 1, 0, 00, false);

		Duration difference = time1.getDifference(time2);
		assertEquals(0l, difference.getTimeInMilliseconds());
	}

	@Test
	public void oneDayDifference() throws Exception {
		DateTime time1 = new DateTime(1970, 0, 1, 0, 00, false);
		DateTime time2 = new DateTime(1970, 0, 2, 0, 00, false);

		Duration difference = time1.getDifference(time2);
		assertEquals(24 * 60 * 60 * 1000, difference.getTimeInMilliseconds());
	}

	@Test
	public void oneHourDifference() throws Exception {
		DateTime time1 = new DateTime(1970, 0, 1, 0, 00, false);
		DateTime time2 = new DateTime(1970, 0, 1, 1, 00, false);

		Duration difference = time1.getDifference(time2);
		assertEquals(60 * 60 * 1000, difference.getTimeInMilliseconds());
	}

	@Test
	public void oneMinuteDifference() throws Exception {
		DateTime time1 = new DateTime(1970, 0, 1, 0, 1, false);
		DateTime time2 = new DateTime(1970, 0, 1, 0, 0, false);

		Duration difference = time1.getDifference(time2);
		assertEquals(60 * 1000, difference.getTimeInMilliseconds());
	}

	@Test
	public void setDay() throws Exception {
		DateTime time = new DateTime(1970, 0, 1, 24, 00, false);
		assertEquals(1, time.getDay());
		time.setDay(2);
		assertEquals(1, time.getDay());

		time = new DateTime(1970, 0, 1, 0, 00, true);
		time.setDay(3);
		assertEquals(3, time.getDay());
	}

	@Test
	public void toExclusive() throws Exception {
		DateTime dateTime = new DateTime(1970, 0, 1, 0, 0, true);
		DateTime exclusive = new DateTime(dateTime, false);

		assertEquals(1969, exclusive.getYear());
		assertEquals(11, exclusive.getMonth());
		assertEquals(31, exclusive.getDay());
	}

	@Test
	public void toStringRepresentation() throws Exception {
		DateTime time = new DateTime(2010, 9, 7, 9, 19, true);
		String timeAsString = time.toStringRepresentation();
		assertEquals("1286443140000_inclusive", timeAsString);

		DateTime timeFromString = new DateTime(timeAsString);
		assertEquals(time, timeFromString);
	}

	@Test
	public void toStringRepresentationExclusive() throws Exception {
		DateTime time = new DateTime(2010, 9, 7, 9, 19, false);
		String timeAsString = time.toStringRepresentation();
		assertEquals("1286443139999_exclusive", timeAsString);

		DateTime timeFromString = new DateTime(timeAsString);
		assertEquals(time, timeFromString);
	}
}
