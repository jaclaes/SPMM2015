/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.alaskasimulator.test.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.alaskasimulator.core.buildtime.DurationRange;
import org.alaskasimulator.core.buildtime.GameConfig;
import org.alaskasimulator.core.buildtime.constraint.ExecutionTimeFrame;
import org.alaskasimulator.core.buildtime.constraint.TimeFrame;
import org.alaskasimulator.core.runtime.Game;
import org.alaskasimulator.core.runtime.TwentyFourHourTime;
import org.cheetahplatform.test.TestHelper;
import org.junit.Before;
import org.junit.Test;

public class ExecutionTimeFrameTest {
	private Game game;

	@Test
	public void allowExecutionOnMultipleDays() {
		ExecutionTimeFrame dayExecutionTimeFrame = new ExecutionTimeFrame(2, 3, 4);
		TwentyFourHourTime time = new TwentyFourHourTime(game, 2, 200);
		assertTrue(dayExecutionTimeFrame.allowsExecution(time));
		time = new TwentyFourHourTime(game, 3, 200);
		assertTrue(dayExecutionTimeFrame.allowsExecution(time));
		time = new TwentyFourHourTime(game, 4, 200);
		assertTrue(dayExecutionTimeFrame.allowsExecution(time));

		time = new TwentyFourHourTime(game, 1, 200);
		assertFalse(dayExecutionTimeFrame.allowsExecution(time));
		time = new TwentyFourHourTime(game, 5, 200);
		assertFalse(dayExecutionTimeFrame.allowsExecution(time));

	}

	@Test
	public void allowsExecution() {
		ExecutionTimeFrame dayExecutionTimeFrame = new ExecutionTimeFrame(3);
		TwentyFourHourTime time = new TwentyFourHourTime(game, 3, 200);
		assertTrue(dayExecutionTimeFrame.allowsExecution(time));
	}

	@Test
	public void allowsExecutionForDayAndTimeFrame() {
		ArrayList<TimeFrame> timeFrames = new ArrayList<TimeFrame>();
		timeFrames.add(new TimeFrame(new DurationRange(60), 800));
		timeFrames.add(new TimeFrame(new DurationRange(60), 1200));
		ExecutionTimeFrame executionTimeFrame = new ExecutionTimeFrame(timeFrames, 0, 3);
		assertTrue(executionTimeFrame.allowsExecution(new TwentyFourHourTime(game, 0, 830)));
		assertTrue(executionTimeFrame.allowsExecution(new TwentyFourHourTime(game, 3, 830)));
		assertTrue(executionTimeFrame.allowsExecution(new TwentyFourHourTime(game, 0, 1230)));
		assertTrue(executionTimeFrame.allowsExecution(new TwentyFourHourTime(game, 3, 1230)));
	}

	@Test
	public void allowsExecutionForTimeFrame() {
		TimeFrame timeFrame = new TimeFrame(new DurationRange(60), 100);
		ExecutionTimeFrame executionTimeFrame = new ExecutionTimeFrame(timeFrame);
		assertTrue(executionTimeFrame.allowsExecution(new TwentyFourHourTime(game, 0, 150)));
		assertTrue(executionTimeFrame.allowsExecution(new TwentyFourHourTime(game, 1, 150)));
		assertTrue(executionTimeFrame.allowsExecution(new TwentyFourHourTime(game, 2, 150)));
	}

	@Test
	public void doesNotAllowExecutionBesideTimeFrames() {
		List<TimeFrame> timeFrames = new ArrayList<TimeFrame>();
		timeFrames.add(new TimeFrame(new DurationRange(60), 800));
		timeFrames.add(new TimeFrame(new DurationRange(60), 1200));
		ExecutionTimeFrame executionTimeFrame = new ExecutionTimeFrame(timeFrames, 0, 3);
		assertFalse(executionTimeFrame.allowsExecution(new TwentyFourHourTime(game, 0, 759)));
		assertFalse(executionTimeFrame.allowsExecution(new TwentyFourHourTime(game, 0, 861)));
		assertFalse(executionTimeFrame.allowsExecution(new TwentyFourHourTime(game, 3, 861)));
		assertFalse(executionTimeFrame.allowsExecution(new TwentyFourHourTime(game, 3, 861)));
		assertFalse(executionTimeFrame.allowsExecution(new TwentyFourHourTime(game, 0, 1159)));
		assertFalse(executionTimeFrame.allowsExecution(new TwentyFourHourTime(game, 0, 1261)));
		assertFalse(executionTimeFrame.allowsExecution(new TwentyFourHourTime(game, 3, 1261)));
		assertFalse(executionTimeFrame.allowsExecution(new TwentyFourHourTime(game, 3, 1261)));
	}

	@Test
	public void getTextualRepresentation() {
		ExecutionTimeFrame dayExecutionTimeFrame = new ExecutionTimeFrame(3);
		assertEquals("on day 4", dayExecutionTimeFrame.getTextualRepresentation());

		dayExecutionTimeFrame = new ExecutionTimeFrame(3, 4);
		assertEquals("on days 4 and 5", dayExecutionTimeFrame.getTextualRepresentation());

		dayExecutionTimeFrame = new ExecutionTimeFrame(2, 3, 4, 5);
		assertEquals("on days 3, 4, 5 and 6", dayExecutionTimeFrame.getTextualRepresentation());

		List<TimeFrame> timeFrames = new ArrayList<TimeFrame>();
		timeFrames.add(new TimeFrame(new DurationRange(60), 480));
		dayExecutionTimeFrame = new ExecutionTimeFrame(timeFrames, 3);
		assertEquals("on day 4 at 8:00-9:00", dayExecutionTimeFrame.getTextualRepresentation());

		timeFrames = new ArrayList<TimeFrame>();
		timeFrames.add(new TimeFrame(new DurationRange(60), 480));
		timeFrames.add(new TimeFrame(new DurationRange(120), 780));
		dayExecutionTimeFrame = new ExecutionTimeFrame(timeFrames, 3);
		assertEquals("on day 4 at 8:00-9:00 and 13:00-15:00", dayExecutionTimeFrame.getTextualRepresentation());

		timeFrames = new ArrayList<TimeFrame>();
		timeFrames.add(new TimeFrame(new DurationRange(60), 480));
		timeFrames.add(new TimeFrame(new DurationRange(120), 780));
		timeFrames.add(new TimeFrame(new DurationRange(30), 960));
		dayExecutionTimeFrame = new ExecutionTimeFrame(timeFrames, 3);
		assertEquals("on day 4 at 8:00-9:00, 13:00-15:00 and 16:00-16:30", dayExecutionTimeFrame.getTextualRepresentation());
	}

	@Before
	public void setUp() {
		TestHelper.setLocaleToEnglish();
		GameConfig gameConfig = new GameConfig(1, "TestConfig", 5, 7, 700);
		game = new Game(gameConfig, "Player");
	}

	@Test
	public void tooEarly() {
		ExecutionTimeFrame dayExecutionTimeFrame = new ExecutionTimeFrame(3);
		TwentyFourHourTime time = new TwentyFourHourTime(game, 2, 700);
		assertFalse(dayExecutionTimeFrame.allowsExecution(time));
	}

	@Test
	public void tooLate() {
		ExecutionTimeFrame dayExecutionTimeFrame = new ExecutionTimeFrame(3);
		TwentyFourHourTime time = new TwentyFourHourTime(game, 4, 700);
		assertFalse(dayExecutionTimeFrame.allowsExecution(time));
	}
}
