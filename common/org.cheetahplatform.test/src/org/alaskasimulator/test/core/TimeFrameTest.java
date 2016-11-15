/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.alaskasimulator.test.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.alaskasimulator.core.buildtime.DurationRange;
import org.alaskasimulator.core.buildtime.GameConfig;
import org.alaskasimulator.core.buildtime.constraint.TimeFrame;
import org.alaskasimulator.core.runtime.Game;
import org.alaskasimulator.core.runtime.TwentyFourHourTime;
import org.eclipse.core.runtime.AssertionFailedException;
import org.junit.Before;
import org.junit.Test;

public class TimeFrameTest {
	private GameConfig gameConfig;
	private Game game;

	@Test
	public void getTextualRepresentation() {
		TimeFrame timeFrame = new TimeFrame(new DurationRange(60), 120);
		assertEquals("2:00-3:00", timeFrame.getTextualRepresentation());

		timeFrame = new TimeFrame(new DurationRange(0), 120);
		assertEquals("2:00", timeFrame.getTextualRepresentation());
	}

	@Test(expected = AssertionFailedException.class)
	public void illegalDuration() {
		new TimeFrame(null, 100);
	}

	@Test(expected = IllegalArgumentException.class)
	public void illegalStartTime() {
		new TimeFrame(new DurationRange(100), -1);
	}

	@Test
	public void inTimeFrame() {
		TimeFrame timeFrame = new TimeFrame(new DurationRange(60), 120);
		assertTrue(timeFrame.isInTimeFrame(new TwentyFourHourTime(game, 0, 150)));
	}

	@Test
	public void noMatterWhichDay() {
		TimeFrame timeFrame = new TimeFrame(new DurationRange(60), 120);
		assertTrue(timeFrame.isInTimeFrame(new TwentyFourHourTime(game, 0, 120)));
		assertTrue(timeFrame.isInTimeFrame(new TwentyFourHourTime(game, 1, 180)));
	}

	@Before
	public void setUp() {
		gameConfig = new GameConfig();
		game = new Game(gameConfig, "player");
	}

	@Test
	public void tooEarly() {
		TimeFrame timeFrame = new TimeFrame(new DurationRange(60), 120);
		assertFalse(timeFrame.isInTimeFrame(new TwentyFourHourTime(game, 0, 119)));
	}

	@Test
	public void tooLate() {
		TimeFrame timeFrame = new TimeFrame(new DurationRange(60), 120);
		assertFalse(timeFrame.isInTimeFrame(new TwentyFourHourTime(game, 0, 181)));
	}
}
