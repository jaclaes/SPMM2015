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
import java.util.HashMap;
import java.util.List;

import org.alaskasimulator.core.Failure;
import org.alaskasimulator.core.buildtime.ActivityConfig;
import org.alaskasimulator.core.buildtime.Certainty;
import org.alaskasimulator.core.buildtime.DurationRange;
import org.alaskasimulator.core.buildtime.GameConfig;
import org.alaskasimulator.core.buildtime.LocalWeatherCharacteristics;
import org.alaskasimulator.core.buildtime.Location;
import org.alaskasimulator.core.buildtime.UtmCoordinate;
import org.alaskasimulator.core.buildtime.constraint.ExecutionTimeConstraint;
import org.alaskasimulator.core.buildtime.constraint.ExecutionTimeFrame;
import org.alaskasimulator.core.buildtime.constraint.TimeFrame;
import org.alaskasimulator.core.runtime.Game;
import org.alaskasimulator.core.runtime.action.ActivityAction;
import org.alaskasimulator.core.runtime.proxy.ActionConfigProxy;
import org.cheetahplatform.test.TestHelper;
import org.eclipse.core.runtime.AssertionFailedException;
import org.junit.Before;
import org.junit.Test;

public class ExecutionTimeConstraintTest {
	private Game game;
	private ActionConfigProxy activityBProxy;
	private ActivityConfig activityConfigA;
	private ActivityConfig activityConfigB;
	private ActionConfigProxy activityAProxy;

	@Test
	public void affects() {
		ExecutionTimeConstraint constraint = new ExecutionTimeConstraint(activityConfigA);
		assertTrue(constraint.affects(activityConfigA));
		assertFalse(constraint.affects(activityConfigB));
	}

	@Test
	public void allowsExecution() {
		ExecutionTimeConstraint executionTimeConstraint = new ExecutionTimeConstraint(activityConfigA);
		ExecutionTimeFrame timeFrame = new ExecutionTimeFrame(3);
		executionTimeConstraint.addExecutionTimeFrame(timeFrame);
		ActivityAction actionA = TestHelper.addActivityToPlan(activityAProxy, 3, 100);
		ActivityAction actionB = TestHelper.addActivityToPlan(activityBProxy, 3, 300);
		assertTrue(executionTimeConstraint.allowsExecution(actionA));
		assertTrue(executionTimeConstraint.allowsExecution(actionB));
	}

	@Test
	public void complexExecutionConstraint() {
		ExecutionTimeConstraint executionTimeConstraint = new ExecutionTimeConstraint(activityConfigA);
		ArrayList<TimeFrame> timeFrames = new ArrayList<TimeFrame>();
		timeFrames.add(new TimeFrame(new DurationRange(200), 100 + 60 * 8));
		timeFrames.add(new TimeFrame(new DurationRange(200), 400 + 60 * 8));
		ExecutionTimeFrame timeFrame = new ExecutionTimeFrame(timeFrames, 1, 3);
		executionTimeConstraint.addExecutionTimeFrame(timeFrame);
		TestHelper.addActivityToPlan(activityAProxy, 1, 150);
		TestHelper.addActivityToPlan(activityAProxy, 1, 450);
		TestHelper.addActivityToPlan(activityAProxy, 3, 150);
		TestHelper.addActivityToPlan(activityAProxy, 3, 450);
		List<Failure> failures = executionTimeConstraint.validatePlan(game, new HashMap<String, Object>());
		assertTrue(failures.isEmpty());

	}

	@Test
	public void denyExecution() {
		ExecutionTimeConstraint executionTimeConstraint = new ExecutionTimeConstraint(activityConfigA);
		ExecutionTimeFrame timeFrame = new ExecutionTimeFrame(3);
		executionTimeConstraint.addExecutionTimeFrame(timeFrame);
		ActivityAction actionA = TestHelper.addActivityToPlan(activityAProxy, 1, 100);
		ActivityAction actionB = TestHelper.addActivityToPlan(activityBProxy, 4, 300);
		assertFalse(executionTimeConstraint.allowsExecution(actionA));
		assertTrue(executionTimeConstraint.allowsExecution(actionB));
	}

	@Test
	public void doesNotAffectOtherActions() {
		ExecutionTimeConstraint executionTimeConstraint = new ExecutionTimeConstraint(activityConfigA);
		ExecutionTimeFrame timeFrame = new ExecutionTimeFrame(3);
		executionTimeConstraint.addExecutionTimeFrame(timeFrame);
		TestHelper.addActivityToPlan(activityBProxy, 2, 200);
		List<Failure> failures = executionTimeConstraint.validatePlan(game, new HashMap<String, Object>());
		assertTrue(failures.isEmpty());
	}

	@Test
	public void errorInPlan() {
		ExecutionTimeConstraint executionTimeConstraint = new ExecutionTimeConstraint(activityConfigA);
		ExecutionTimeFrame timeFrame = new ExecutionTimeFrame(3);
		executionTimeConstraint.addExecutionTimeFrame(timeFrame);
		TestHelper.addActivityToPlan(activityAProxy, 2, 200);
		List<Failure> failures = executionTimeConstraint.validatePlan(game, new HashMap<String, Object>());
		assertFalse(failures.isEmpty());
	}

	@Test
	public void getDescription() {
		ExecutionTimeConstraint executionTimeConstraint = new ExecutionTimeConstraint(activityConfigA);
		ExecutionTimeFrame timeFrame = new ExecutionTimeFrame(3);
		executionTimeConstraint.addExecutionTimeFrame(timeFrame);
		assertEquals("'Activity A' can be executed on day 4.", executionTimeConstraint.getDescription());
		executionTimeConstraint.addExecutionTimeFrame(new ExecutionTimeFrame(4));
		assertEquals("'Activity A' can be executed on day 4 and on day 5.", executionTimeConstraint.getDescription());
		executionTimeConstraint.addExecutionTimeFrame(new ExecutionTimeFrame(5));
		assertEquals("'Activity A' can be executed on day 4, on day 5 and on day 6.", executionTimeConstraint.getDescription());
	}

	@Test(expected = AssertionFailedException.class)
	public void illegalAction() {
		new ExecutionTimeConstraint(null);
	}

	@Test
	public void multipleErrorsInPlan() {
		ExecutionTimeConstraint executionTimeConstraint = new ExecutionTimeConstraint(activityConfigA);
		ExecutionTimeFrame timeFrame = new ExecutionTimeFrame(3);
		executionTimeConstraint.addExecutionTimeFrame(timeFrame);
		TestHelper.addActivityToPlan(activityAProxy, 2, 200);
		TestHelper.addActivityToPlan(activityAProxy, 4, 200);
		List<Failure> failures = executionTimeConstraint.validatePlan(game, new HashMap<String, Object>());
		assertFalse(failures.isEmpty());
		assertEquals(2, failures.size());
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
		game = new Game(gameConfig, "Player");
		game.startJourney();

		activityAProxy = game.getConfig().findActionConfigProxy(activityConfigA);
		activityBProxy = game.getConfig().findActionConfigProxy(activityConfigB);
	}

	@Test
	public void validPlan() {
		ExecutionTimeConstraint executionTimeConstraint = new ExecutionTimeConstraint(activityConfigA);
		ExecutionTimeFrame timeFrame = new ExecutionTimeFrame(2);
		executionTimeConstraint.addExecutionTimeFrame(timeFrame);
		TestHelper.addActivityToPlan(activityAProxy, 2, 200);
		List<Failure> failures = executionTimeConstraint.validatePlan(game, new HashMap<String, Object>());
		assertTrue(failures.isEmpty());
	}

}
