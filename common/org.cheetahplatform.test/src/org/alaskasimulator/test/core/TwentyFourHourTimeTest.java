/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.alaskasimulator.test.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.alaskasimulator.core.Constants;
import org.alaskasimulator.core.runtime.Duration;
import org.alaskasimulator.core.runtime.Game;
import org.alaskasimulator.core.runtime.Time;
import org.alaskasimulator.core.runtime.TwentyFourHourTime;
import org.eclipse.core.runtime.AssertionFailedException;
import org.junit.Before;
import org.junit.Test;

public class TwentyFourHourTimeTest {

	private TestGame testGame;
	private Game game;

	@Before
	public void before() {
		testGame = new TestGame();
		testGame.setJourneyDuration(4);
		testGame.setDayDuration(720);
		testGame.setBudget(1000);
		testGame.createGameConfig();
		game = testGame.createGame();
		game.startJourney();
	}

	@Test
	public void compare() {
		TwentyFourHourTime twentyFourHourTime1 = new TwentyFourHourTime(game, 0, 0);
		TwentyFourHourTime twentyFourHourTime2 = new TwentyFourHourTime(game, 1, 0);
		assertTrue("should be less than 0", twentyFourHourTime1.compareTo(twentyFourHourTime2) < 0);
		assertTrue("should be greater 0", twentyFourHourTime2.compareTo(twentyFourHourTime1) > 0);

		twentyFourHourTime1 = new TwentyFourHourTime(game, 0, 600);
		twentyFourHourTime2 = new TwentyFourHourTime(game, 0, 601);
		assertTrue("should be less than 0", twentyFourHourTime1.compareTo(twentyFourHourTime2) < 0);
		assertTrue("should be greater 0", twentyFourHourTime2.compareTo(twentyFourHourTime1) > 0);

		twentyFourHourTime1 = new TwentyFourHourTime(game, 0, 1439);
		twentyFourHourTime2 = new TwentyFourHourTime(game, 1, 0);
		assertTrue("should be less than 0", twentyFourHourTime1.compareTo(twentyFourHourTime2) < 0);
		assertTrue("should be greater 0", twentyFourHourTime2.compareTo(twentyFourHourTime1) > 0);

		twentyFourHourTime1 = new TwentyFourHourTime(game, 2, 675);
		twentyFourHourTime2 = new TwentyFourHourTime(game, 2, 675);
		assertTrue("should be greater 0", twentyFourHourTime1.compareTo(twentyFourHourTime2) == 0);
		assertTrue("should be greater 0", twentyFourHourTime2.compareTo(twentyFourHourTime1) == 0);
	}

	@Test
	public void compareToUpfront() {
		TwentyFourHourTime twentyFourHourTime1 = new TwentyFourHourTime(game, Time.UPFRONT_PLANNING_DAY, 0);
		TwentyFourHourTime twentyFourHourTime2 = new TwentyFourHourTime(game, 0, 0);
		assertTrue(twentyFourHourTime1.compareTo(twentyFourHourTime2) < 0);
		assertTrue(twentyFourHourTime2.compareTo(twentyFourHourTime1) > 0);

		twentyFourHourTime1 = new TwentyFourHourTime(game, Time.UPFRONT_PLANNING_DAY, 0);
		twentyFourHourTime2 = new TwentyFourHourTime(game, Time.UPFRONT_PLANNING_DAY, 0);
		assertTrue(twentyFourHourTime1.compareTo(twentyFourHourTime2) == 0);
	}

	@Test
	public void difference() {
		TwentyFourHourTime twentyFourHourTime1 = new TwentyFourHourTime(game, 1, 400);
		TwentyFourHourTime twentyFourHourTime2 = new TwentyFourHourTime(game, 1, 188);

		Duration difference = twentyFourHourTime1.difference(twentyFourHourTime2);
		assertEquals("wrong difference", 212, difference.getMinutes());
		difference = twentyFourHourTime2.difference(twentyFourHourTime1);
		assertEquals("wrong difference", 212, difference.getMinutes());

		twentyFourHourTime1 = new TwentyFourHourTime(game, 1, 400);
		twentyFourHourTime2 = new TwentyFourHourTime(game, 0, 1240);

		difference = twentyFourHourTime1.difference(twentyFourHourTime2);
		assertEquals("wrong difference", 600, difference.getMinutes());
		difference = twentyFourHourTime2.difference(twentyFourHourTime1);
		assertEquals("wrong difference", 600, difference.getMinutes());

	}

	@Test
	public void fromTime() {
		Time time = new Time(game, 0, 0);
		TwentyFourHourTime twentyFourHourTime = new TwentyFourHourTime(game, time);
		assertEquals("wrong minuts", Constants.TIME_OFFSET_MINUTES, twentyFourHourTime.getMinutes());
		assertEquals("wrong day", 0, twentyFourHourTime.getDay());

		time = new Time(game, 1, 300);
		twentyFourHourTime = new TwentyFourHourTime(game, time);
		assertEquals("wrong minuts", Constants.TIME_OFFSET_MINUTES + 300, twentyFourHourTime.getMinutes());
		assertEquals("wrong day", 1, twentyFourHourTime.getDay());
	}

	@Test
	public void getFormattedString() {
		TwentyFourHourTime twentyFourHourTime = new TwentyFourHourTime(game, 0, 0);
		assertEquals("wrong string", "00:00 on day 1", twentyFourHourTime.getFormattedDateString());

		twentyFourHourTime = new TwentyFourHourTime(game, 1, 0);
		assertEquals("wrong string", "00:00 on day 2", twentyFourHourTime.getFormattedDateString());

		twentyFourHourTime = new TwentyFourHourTime(game, 0, 465);
		assertEquals("wrong string", "07:45 on day 1", twentyFourHourTime.getFormattedDateString());

		twentyFourHourTime = new TwentyFourHourTime(game, 1, 510);
		assertEquals("wrong string", "08:30 on day 2", twentyFourHourTime.getFormattedDateString());

		twentyFourHourTime = new TwentyFourHourTime(game, 0, 615);
		assertEquals("wrong string", "10:15 on day 1", twentyFourHourTime.getFormattedDateString());

		twentyFourHourTime = new TwentyFourHourTime(game, 0, 925);
		assertEquals("wrong string", "15:25 on day 1", twentyFourHourTime.getFormattedDateString());

		twentyFourHourTime = new TwentyFourHourTime(game, 0, 1320);
		assertEquals("wrong string", "22:00 on day 1", twentyFourHourTime.getFormattedDateString());

		twentyFourHourTime = new TwentyFourHourTime(game, 2, 1439);
		assertEquals("wrong string", "23:59 on day 3", twentyFourHourTime.getFormattedDateString());
	}

	@Test
	public void getHour() {
		TwentyFourHourTime twentyFourHourTime = new TwentyFourHourTime(game, 1, 0);
		assertEquals("wrong string", 0, twentyFourHourTime.getHour());

		twentyFourHourTime = new TwentyFourHourTime(game, 0, 465);
		assertEquals("wrong string", 7, twentyFourHourTime.getHour());

		twentyFourHourTime = new TwentyFourHourTime(game, 1, 510);
		assertEquals("wrong string", 8, twentyFourHourTime.getHour());

		twentyFourHourTime = new TwentyFourHourTime(game, 0, 615);
		assertEquals("wrong string", 10, twentyFourHourTime.getHour());

		twentyFourHourTime = new TwentyFourHourTime(game, 0, 1439);
		assertEquals("wrong string", 23, twentyFourHourTime.getHour());

	}

	@Test
	public void getMinuteOfHour() {
		TwentyFourHourTime twentyFourHourTime = new TwentyFourHourTime(game, 0, 465);
		assertEquals("wrong string", 45, twentyFourHourTime.getMinutesOfHour());

		twentyFourHourTime = new TwentyFourHourTime(game, 0, 615);
		assertEquals("wrong string", 15, twentyFourHourTime.getMinutesOfHour());

		twentyFourHourTime = new TwentyFourHourTime(game, 0, 8);
		assertEquals("wrong string", 8, twentyFourHourTime.getMinutesOfHour());
	}

	@Test(expected = AssertionFailedException.class)
	public void illegalGame() {
		new TwentyFourHourTime(null, 0, 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void negativeDay() {
		new TwentyFourHourTime(game, -2, 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void negativeMinutes() {
		new TwentyFourHourTime(game, 0, -1);
	}

	@Test
	public void plus() {
		TwentyFourHourTime twentyFourHourTime = new TwentyFourHourTime(game, 0, 0);
		Duration duration = new Duration(300);
		TwentyFourHourTime sum = twentyFourHourTime.plus(duration);
		assertEquals("wrong minutes", 300, sum.getMinutes());
		assertEquals("wrong day", 0, sum.getDay());

		duration = new Duration(800);
		sum = sum.plus(duration);
		assertEquals("wrong minutes", 1100, sum.getMinutes());
		assertEquals("wrong day", 0, sum.getDay());

		duration = new Duration(340);
		sum = sum.plus(duration);
		assertEquals("wrong minutes", 0, sum.getMinutes());
		assertEquals("wrong day", 1, sum.getDay());

		duration = new Duration(600);
		sum = sum.plus(duration);
		assertEquals("wrong minutes", 600, sum.getMinutes());
		assertEquals("wrong day", 1, sum.getDay());

		duration = new Duration(2880);
		sum = sum.plus(duration);
		assertEquals("wrong minutes", 600, sum.getMinutes());
		assertEquals("wrong day", 3, sum.getDay());

	}

	@Test(expected = IllegalArgumentException.class)
	public void tooHighDay() {
		new TwentyFourHourTime(game, 5, 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void tooHighMinutes() {
		// should be next day already
		new TwentyFourHourTime(game, 1, 24 * 60);
	}

	@Test
	public void totime() {
		Time time = new Time(game, 0, 0);
		TwentyFourHourTime twentyFourHourTime = new TwentyFourHourTime(game, time);
		assertTrue("should be equal", time.equals(twentyFourHourTime.toTime()));

		twentyFourHourTime = new TwentyFourHourTime(game, 0, 1200);
		Time newTime = twentyFourHourTime.toTime();
		assertEquals("wrong day", 0, newTime.getDay());
		assertEquals("wrong minutes", game.getDayDuration(), newTime.getMinute() + 1);
		assertEquals("wrong day duration", game.getDayDuration(), newTime.getDayDurationInMinutes());
		assertEquals("wrong journeyduration", game.getJourneyDuration(), newTime.getJourneyDurationInDays());

		twentyFourHourTime = new TwentyFourHourTime(game, 0, 50);
		newTime = twentyFourHourTime.toTime();
		assertEquals("wrong day", 0, newTime.getDay());
		assertEquals("wrong minutes", 0, newTime.getMinute());
		assertEquals("wrong day duration", game.getDayDuration(), newTime.getDayDurationInMinutes());
		assertEquals("wrong journeyduration", game.getJourneyDuration(), newTime.getJourneyDurationInDays());
	}
}
