/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.alaskasimulator.test.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.alaskasimulator.core.buildtime.GameConfig;
import org.alaskasimulator.core.runtime.Time;
import org.junit.Test;

public class TimeTest {
	private GameConfig config;
	private Time time;
	private String timeAsString;
	private Time timeFromString;

	@Test
	public void compareToDifferentDay() {
		config = new GameConfig(0, "My game", 0, 5, 700);
		Time time1 = new Time(config, 0, 0);
		Time time2 = new Time(config, 1, 0);
		assertTrue(time1.compareTo(time2) < 0);
		assertTrue(time2.compareTo(time1) > 0);
	}

	@Test
	public void compareToSameDay() {
		config = new TestGame().createGameConfig();
		Time time1 = new Time(config, 0, 0);
		Time time2 = new Time(config, 0, 1);
		assertTrue(time1.compareTo(time2) < 0);
		assertTrue(time2.compareTo(time1) > 0);
	}

	@Test
	public void compareToUpfrontPlanning() {
		config = new GameConfig(0, "My game", 0, 5, 700);
		Time time1 = new Time(config, Time.UPFRONT_PLANNING_DAY, 0);
		Time time2 = new Time(config, 0, 0);
		assertTrue(time1.compareTo(time2) < 0);
		assertTrue(time2.compareTo(time1) > 0);

		// minutes do not matter in upfront planning
		time1 = new Time(config, Time.UPFRONT_PLANNING_DAY, 0);
		time2 = new Time(config, Time.UPFRONT_PLANNING_DAY, 55);
		assertTrue(time1.compareTo(time2) == 0);
	}

	private void createTime(int day) {
		config = new TestGame().createGameConfig();
		time = new Time(config, day, 0);
		timeAsString = time.toString();
		timeFromString = Time.fromString(timeAsString, config);
	}

	@Test
	public void fromString() {
		createTime(0);

		assertEquals("Wrong day.", time.getDay(), timeFromString.getDay());
		assertEquals("Wrong day duration.", time.getDayDurationInMinutes(), timeFromString.getDayDurationInMinutes());
		assertEquals("Wrong minute.", time.getMinute(), timeFromString.getMinute());
		assertEquals("Wrong journey duration.", time.getJourneyDurationInDays(), timeFromString.getJourneyDurationInDays());
	}

	@Test(expected = RuntimeException.class)
	public void fromStringFail() {
		createTime(10);
		timeAsString = "some non-time string";
	}

	@Test
	public void fromStringUpfrontplanningPhase() {
		createTime(Time.UPFRONT_PLANNING_DAY);

		assertEquals("Wrong day.", time.getDay(), timeFromString.getDay());
	}

}
