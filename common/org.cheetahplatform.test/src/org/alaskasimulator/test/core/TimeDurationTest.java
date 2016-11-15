/*******************************************************************************
* This file is provided under the terms of the Eclipse Public License 1.0. 
* Any use, reproduction or distribution of the program
* constitutes recipient's acceptance of this agreement.
*******************************************************************************/
package org.alaskasimulator.test.core;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.alaskasimulator.core.buildtime.GameConfig;
import org.alaskasimulator.core.runtime.Duration;
import org.alaskasimulator.core.runtime.Time;
import org.junit.Test;

public class TimeDurationTest
{
	private int journeyDuration = 7;
	private int dayDuration = 120;

	@Test
	public void increaseMinute()
	{
		Time time = new Time(getGameConfig(), 0, 0);
		int minutes = time.getMinute();
		assertEquals(0, minutes);
		int interval = Math.abs(new Random().nextInt(dayDuration));
		time.increaseMinutes(interval);
		minutes = time.getMinute();
		assertEquals(interval, minutes);
	}

	@Test
	public void increaseDay()
	{
		for (int x = 100; x > 0; x--)
		{
			Time time = new Time(getGameConfig(), 0, 70);
			int day = time.getDay();
			assertEquals(0, day);
			int interval = Math.abs(1 + new Random().nextInt(journeyDuration - 1));
			time.increaseDay(interval);
			day = time.getDay();
			assertEquals(interval, day);
			assertEquals(0, time.getMinute());
		}
	}

	@Test
	public void initializeTime()
	{
		Time time = new Time(getGameConfig(), 0, 0);
		assertEquals(time.getDayDurationInMinutes(), getGameConfig().getDayDuration());
		assertEquals(time.getJourneyDurationInDays(), getGameConfig().getJourneyDuration());

		new Time(getGameConfig(), journeyDuration - 1, dayDuration - 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void increaseMinutesIllegal()
	{
		Time time = new Time(getGameConfig(), new Random().nextInt(journeyDuration), new Random().nextInt(dayDuration));
		time.increaseMinutes(-new Random().nextInt(dayDuration) - 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void increaseDaysIllegal()
	{
		Time time = new Time(getGameConfig(), new Random().nextInt(journeyDuration), new Random().nextInt(dayDuration));
		time.increaseDay(-new Random().nextInt(journeyDuration));
	}

	@Test
	public void compareTo()
	{
		Time time = new Time(getGameConfig(), new Random().nextInt(journeyDuration), new Random().nextInt(dayDuration));
		assertEquals(new Time(time), time);
		assertEquals(new Time(time).hashCode(), time.hashCode());
	}

	@Test(expected = IllegalArgumentException.class)
	public void initializeDuration()
	{
		new Duration(-new Random().nextInt(dayDuration) - 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void illegalTimePlusDuration()
	{
		Time time = new Time(getGameConfig(), journeyDuration - 1, dayDuration);
		time.plus(new Duration(1 + new Random().nextInt(dayDuration)));
	}

	@Test
	public void timePlusDuration()
	{
		Time startTime = new Time(getGameConfig(), 1, 50);
		Time endTime = startTime.plus(new Duration(4 * startTime.getDayDurationInMinutes() + 90));
		assertEquals(6, endTime.getDay());
		assertEquals(19, endTime.getMinute());
	}

	private GameConfig getGameConfig()
	{
		return new GameConfig(0, "testConfig", 0, journeyDuration, dayDuration);
	}
}
