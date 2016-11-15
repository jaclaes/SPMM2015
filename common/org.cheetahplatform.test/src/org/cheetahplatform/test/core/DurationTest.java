/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.core;

import static org.junit.Assert.assertEquals;

import org.cheetahplatform.common.AssertionFailedException;
import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.common.date.Duration;
import org.junit.Test;

public class DurationTest {
	@Test
	public void add() throws Exception {
		DateTime time = new DateTime(0, 0, true);
		assertEquals(0, time.getHour());
		Duration duration = new Duration(1, 0);
		time.plus(duration);

		assertEquals(1, time.getHour());
	}

	@Test
	public void bug() throws Exception {
		Duration duration = new Duration(new DateTime(2000, 7, 4, 0, 0, true), new DateTime(2009, 7, 4, 0, 0, true));
		assertEquals(9, duration.getYear());
	}

	@Test
	public void bug2() throws Exception {
		Duration duration = new Duration(1, 0);
		assertEquals(1, duration.getHour());

		long timeInMilliseconds = duration.getTimeInMilliseconds();
		assertEquals(3600000, timeInMilliseconds);
	}

	@Test(expected = AssertionFailedException.class)
	public void negativeDuration() throws Exception {
		new Duration(new DateTime(7, 0, true), new DateTime(0, 0, true));
	}

	@Test
	public void setMinute() throws Exception {
		Duration duration = new Duration(1, 0);
		assertEquals(1, duration.getHour());
		assertEquals(0, duration.getMinute());
		duration.setMinute(60);

		assertEquals(2, duration.getHour());
		assertEquals(0, duration.getMinute());
	}

	@Test
	public void simple() throws Exception {
		Duration duration = new Duration(new DateTime(0, 0, true), new DateTime(7, 0, true));
		assertEquals(7, duration.getHour());
	}
}
