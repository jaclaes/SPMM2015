/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.alaskasimulator.test.core;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.alaskasimulator.core.Failure;
import org.alaskasimulator.core.buildtime.ActivityConfig;
import org.alaskasimulator.core.buildtime.Certainty;
import org.alaskasimulator.core.buildtime.DurationRange;
import org.alaskasimulator.core.buildtime.GameConfig;
import org.alaskasimulator.core.buildtime.LocalWeatherCharacteristics;
import org.alaskasimulator.core.buildtime.Location;
import org.alaskasimulator.core.buildtime.UtmCoordinate;
import org.alaskasimulator.core.buildtime.constraint.ExecutionTimeFrame;
import org.alaskasimulator.core.buildtime.constraint.TimeFrame;
import org.alaskasimulator.core.buildtime.constraint.TimeLagConstraint;
import org.alaskasimulator.core.runtime.Game;
import org.alaskasimulator.core.runtime.action.ActivityAction;
import org.alaskasimulator.core.runtime.proxy.ActionConfigProxy;
import org.cheetahplatform.test.TestHelper;
import org.junit.Before;
import org.junit.Test;

public class TimeLagConstraintTest extends Assert {
	private ActivityConfig activityConfigA;
	private ActivityConfig activityConfigB;
	private ActivityConfig activityConfigC;
	private Game game;
	private ActionConfigProxy activityAProxy;
	private ActionConfigProxy activityBProxy;
	private ActionConfigProxy activityCProxy;

	@Test
	public void affects() {
		TimeLagConstraint constraint = getConstraint(TimeLagConstraint.TIME_LAG_MODE_START_START);

		assertTrue(constraint.affects(activityConfigA));
		assertTrue(constraint.affects(activityConfigB));
		assertFalse(constraint.affects(activityConfigC));
	}

	@Test
	public void allowsExecutionEE1() {
		TimeLagConstraint constraint = getConstraint(TimeLagConstraint.TIME_LAG_MODE_END_END);
		ActivityAction actionA = TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		ActivityAction actionB = TestHelper.addActivityToPlan(activityBProxy, 1, 74);

		assertTrue(constraint.allowsExecution(actionA));
		Failure failure = new Failure();
		assertFalse(constraint.allowsExecution(actionB, failure));
		assertTrue(failure.getType().equals(Failure.TYPE_TIME_LAG_CONSTRAINT));
		assertTrue(failure.getOrigins().contains(actionB));
	}

	@Test
	public void allowsExecutionEE2() {
		TimeLagConstraint constraint = getConstraint(TimeLagConstraint.TIME_LAG_MODE_END_END);
		ActivityAction actionA = TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		ActivityAction actionC = TestHelper.addActivityToPlan(activityCProxy, 0, 60);
		ActivityAction actionB = TestHelper.addActivityToPlan(activityBProxy, 1, 75);

		assertTrue(constraint.allowsExecution(actionA));
		assertTrue(constraint.allowsExecution(actionC));
		assertTrue(constraint.allowsExecution(actionB));
	}

	@Test
	public void allowsExecutionEE3() {
		TimeLagConstraint constraint = getConstraint(TimeLagConstraint.TIME_LAG_MODE_END_END);
		ActivityAction actionA = TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		ActivityAction actionB = TestHelper.addActivityToPlan(activityBProxy, 2, 425);

		assertTrue(constraint.allowsExecution(actionA));
		assertTrue(constraint.allowsExecution(actionB));
	}

	@Test
	public void allowsExecutionEE4() {
		TimeLagConstraint constraint = getConstraint(TimeLagConstraint.TIME_LAG_MODE_END_END);
		ActivityAction actionA = TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		ActivityAction actionC = TestHelper.addActivityToPlan(activityCProxy, 1, 15);
		ActivityAction actionB = TestHelper.addActivityToPlan(activityBProxy, 2, 426);

		assertTrue(constraint.allowsExecution(actionA));
		assertTrue(constraint.allowsExecution(actionC));
		assertFalse(constraint.allowsExecution(actionB));
	}

	@Test
	public void allowsExecutionEE5() {
		TimeLagConstraint constraint = getConstraint(TimeLagConstraint.TIME_LAG_MODE_END_END);
		ActivityAction actionA = TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		ActivityAction actionB = TestHelper.addActivityToPlan(activityBProxy, 2, 701);

		assertTrue(constraint.allowsExecution(actionA));
		assertFalse(constraint.allowsExecution(actionB));
	}

	@Test
	public void allowsExecutionES1() {
		TimeLagConstraint constraint = getConstraint(TimeLagConstraint.TIME_LAG_MODE_END_START);
		ActivityAction actionA = TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		ActivityAction actionB = TestHelper.addActivityToPlan(activityBProxy, 1, 174);

		assertTrue(constraint.allowsExecution(actionA));
		Failure failure = new Failure();
		assertFalse(constraint.allowsExecution(actionB, failure));
		assertTrue(failure.getType().equals(Failure.TYPE_TIME_LAG_CONSTRAINT));
		assertTrue(failure.getOrigins().contains(actionB));
	}

	@Test
	public void allowsExecutionES2() {
		TimeLagConstraint constraint = getConstraint(TimeLagConstraint.TIME_LAG_MODE_END_START);
		ActivityAction actionA = TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		ActivityAction actionC = TestHelper.addActivityToPlan(activityCProxy, 0, 60);
		ActivityAction actionB = TestHelper.addActivityToPlan(activityBProxy, 1, 175);

		assertTrue(constraint.allowsExecution(actionA));
		assertTrue(constraint.allowsExecution(actionC));
		assertTrue(constraint.allowsExecution(actionB));
	}

	@Test
	public void allowsExecutionES3() {
		TimeLagConstraint constraint = getConstraint(TimeLagConstraint.TIME_LAG_MODE_END_START);
		ActivityAction actionA = TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		ActivityAction actionB = TestHelper.addActivityToPlan(activityBProxy, 2, 525);

		assertTrue(constraint.allowsExecution(actionA));
		assertTrue(constraint.allowsExecution(actionB));
	}

	@Test
	public void allowsExecutionES4() {
		TimeLagConstraint constraint = getConstraint(TimeLagConstraint.TIME_LAG_MODE_END_START);
		ActivityAction actionA = TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		ActivityAction actionC = TestHelper.addActivityToPlan(activityCProxy, 1, 15);
		ActivityAction actionB = TestHelper.addActivityToPlan(activityBProxy, 2, 526);

		assertTrue(constraint.allowsExecution(actionA));
		assertTrue(constraint.allowsExecution(actionC));
		assertFalse(constraint.allowsExecution(actionB));
	}

	@Test
	public void allowsExecutionES5() {
		TimeLagConstraint constraint = getConstraint(TimeLagConstraint.TIME_LAG_MODE_END_START);
		ActivityAction actionA = TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		ActivityAction actionB = TestHelper.addActivityToPlan(activityBProxy, 2, 701);

		assertTrue(constraint.allowsExecution(actionA));
		assertFalse(constraint.allowsExecution(actionB));
	}

	@Test
	public void allowsExecutionSE1() {
		TimeLagConstraint constraint = getConstraint(TimeLagConstraint.TIME_LAG_MODE_START_END);
		ActivityAction actionA = TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		ActivityAction actionB = TestHelper.addActivityToPlan(activityBProxy, 1, 74);

		assertTrue(constraint.allowsExecution(actionA));
		assertTrue(constraint.allowsExecution(actionB));
	}

	@Test
	public void allowsExecutionSE2() {
		TimeLagConstraint constraint = getConstraint(TimeLagConstraint.TIME_LAG_MODE_START_END);
		ActivityAction actionA = TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		ActivityAction actionC = TestHelper.addActivityToPlan(activityCProxy, 0, 60);
		ActivityAction actionB = TestHelper.addActivityToPlan(activityBProxy, 1, 75);

		assertTrue(constraint.allowsExecution(actionA));
		assertTrue(constraint.allowsExecution(actionC));
		assertTrue(constraint.allowsExecution(actionB));
	}

	@Test
	public void allowsExecutionSE3() {
		TimeLagConstraint constraint = getConstraint(TimeLagConstraint.TIME_LAG_MODE_START_END);
		ActivityAction actionA = TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		ActivityAction actionB = TestHelper.addActivityToPlan(activityBProxy, 2, 325);

		assertTrue(constraint.allowsExecution(actionA));
		assertTrue(constraint.allowsExecution(actionB));
	}

	@Test
	public void allowsExecutionSE4() {
		TimeLagConstraint constraint = getConstraint(TimeLagConstraint.TIME_LAG_MODE_START_END);
		ActivityAction actionA = TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		ActivityAction actionC = TestHelper.addActivityToPlan(activityCProxy, 1, 15);
		ActivityAction actionB = TestHelper.addActivityToPlan(activityBProxy, 2, 326);

		assertTrue(constraint.allowsExecution(actionA));
		assertTrue(constraint.allowsExecution(actionC));
		assertFalse(constraint.allowsExecution(actionB));
	}

	@Test
	public void allowsExecutionSE5() {
		TimeLagConstraint constraint = getConstraint(TimeLagConstraint.TIME_LAG_MODE_START_END);
		ActivityAction actionA = TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		ActivityAction actionB = TestHelper.addActivityToPlan(activityBProxy, 2, 601);

		assertTrue(constraint.allowsExecution(actionA));
		assertFalse(constraint.allowsExecution(actionB));
	}

	@Test
	public void allowsExecutionSS1() {
		TimeLagConstraint constraint = getConstraint(TimeLagConstraint.TIME_LAG_MODE_START_START);
		ActivityAction actionA = TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		ActivityAction actionB = TestHelper.addActivityToPlan(activityBProxy, 1, 74);

		assertTrue(constraint.allowsExecution(actionA));
		Failure failure = new Failure();
		assertFalse(constraint.allowsExecution(actionB, failure));
		assertTrue(failure.getType().equals(Failure.TYPE_TIME_LAG_CONSTRAINT));
		assertTrue(failure.getOrigins().contains(actionB));
	}

	@Test
	public void allowsExecutionSS2() {
		TimeLagConstraint constraint = getConstraint(TimeLagConstraint.TIME_LAG_MODE_START_START);
		ActivityAction actionA = TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		ActivityAction actionC = TestHelper.addActivityToPlan(activityCProxy, 0, 60);
		ActivityAction actionB = TestHelper.addActivityToPlan(activityBProxy, 1, 75);

		assertTrue(constraint.allowsExecution(actionA));
		assertTrue(constraint.allowsExecution(actionC));
		assertTrue(constraint.allowsExecution(actionB));
	}

	@Test
	public void allowsExecutionSS3() {
		TimeLagConstraint constraint = getConstraint(TimeLagConstraint.TIME_LAG_MODE_START_START);
		ActivityAction actionA = TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		ActivityAction actionB = TestHelper.addActivityToPlan(activityBProxy, 2, 425);

		assertTrue(constraint.allowsExecution(actionA));
		assertTrue(constraint.allowsExecution(actionB));
	}

	@Test
	public void allowsExecutionSS4() {
		TimeLagConstraint constraint = getConstraint(TimeLagConstraint.TIME_LAG_MODE_START_START);
		ActivityAction actionA = TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		ActivityAction actionC = TestHelper.addActivityToPlan(activityCProxy, 1, 15);
		ActivityAction actionB = TestHelper.addActivityToPlan(activityBProxy, 2, 426);

		assertTrue(constraint.allowsExecution(actionA));
		assertTrue(constraint.allowsExecution(actionC));
		assertFalse(constraint.allowsExecution(actionB));
	}

	@Test
	public void allowsExecutionSS5() {
		TimeLagConstraint constraint = getConstraint(TimeLagConstraint.TIME_LAG_MODE_START_START);
		ActivityAction actionA = TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		ActivityAction actionB = TestHelper.addActivityToPlan(activityBProxy, 2, 701);

		assertTrue(constraint.allowsExecution(actionA));
		assertFalse(constraint.allowsExecution(actionB));
	}

	private TimeLagConstraint getConstraint(String lagMode) {
		TimeFrame frame1 = new TimeFrame(new DurationRange(0), 75);
		TimeFrame frame2 = new TimeFrame(new DurationRange(0), 425);
		List<TimeFrame> timeFrameList1 = new ArrayList<TimeFrame>();
		timeFrameList1.add(frame1);
		List<TimeFrame> timeFrameList2 = new ArrayList<TimeFrame>();
		timeFrameList2.add(frame2);
		TimeLagConstraint constraint = new TimeLagConstraint(activityConfigA, activityConfigB, new ExecutionTimeFrame(timeFrameList1, 1),
				new ExecutionTimeFrame(timeFrameList2, 2), lagMode);
		return constraint;
	}

	@Before
	public void setUp() {
		GameConfig gameConfig = new GameConfig(1, "ResponseTestConfig", 5, 5, 700);
		Location locationA = new Location(gameConfig, "Location A", new UtmCoordinate(1, 266000, 0));
		LocalWeatherCharacteristics weatherCharacteristics = new LocalWeatherCharacteristics(0.3, 0.3);
		locationA.setWeatherCharacteristics(weatherCharacteristics);
		activityConfigA = new ActivityConfig("Activity A", 10.0, 100, new Certainty(1.0), 1.0, new DurationRange(100), 4);
		locationA.addActionConfig(activityConfigA);
		activityConfigB = new ActivityConfig("Activity B", 10.0, 100, new Certainty(1.0), 1.0, new DurationRange(100), 4);
		locationA.addActionConfig(activityConfigB);
		activityConfigC = new ActivityConfig("Activity C", 10.0, 100, new Certainty(1.0), 1.0, new DurationRange(100), 4);
		locationA.addActionConfig(activityConfigC);
		game = new Game(gameConfig, "Player");
		game.startJourney();

		activityAProxy = game.getConfig().findActionConfigProxy(activityConfigA);
		activityBProxy = game.getConfig().findActionConfigProxy(activityConfigB);
		activityCProxy = game.getConfig().findActionConfigProxy(activityConfigC);
	}
}
