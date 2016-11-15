/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.alaskasimulator.test.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
import org.alaskasimulator.core.buildtime.constraint.PrecedenceConstraint;
import org.alaskasimulator.core.runtime.Game;
import org.alaskasimulator.core.runtime.action.Action;
import org.alaskasimulator.core.runtime.action.ActivityAction;
import org.alaskasimulator.core.runtime.proxy.ActionConfigProxy;
import org.cheetahplatform.test.TestHelper;
import org.junit.Before;
import org.junit.Test;

public class PrecedenceConstraintTest {

	private Game game;
	private ActionConfigProxy activityBProxy;
	private ActivityConfig activityConfigA;
	private ActivityConfig activityConfigB;
	private ActionConfigProxy activityAProxy;

	@Test
	public void allowsExecutionAB() {
		PrecedenceConstraint constraint = new PrecedenceConstraint(activityConfigA, activityConfigB);
		ActivityAction actionA = TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		ActivityAction actionB = TestHelper.addActivityToPlan(activityBProxy, 0, 0);

		assertTrue(constraint.allowsExecution(actionA));
		assertTrue(constraint.allowsExecution(actionB));
	}

	@Test
	public void allowsExecutionB() {
		PrecedenceConstraint constraint = new PrecedenceConstraint(activityConfigA, activityConfigB);
		ActivityAction actionB = TestHelper.addActivityToPlan(activityBProxy, 0, 0);

		Failure failure = new Failure();
		assertFalse(constraint.allowsExecution(actionB, failure));
		assertEquals(failure.getType(), Failure.TYPE_PRECEDENCE_CONSTRAINT);
		assertTrue(failure.getOrigins().contains(actionB));
	}

	@Test
	public void ignoreCancelledActions() {
		PrecedenceConstraint constraint = new PrecedenceConstraint(activityConfigA, activityConfigB);
		Action actionB = TestHelper.addActivityToPlan(activityBProxy, 0, 300);
		List<Failure> failures = constraint.validatePlan(game, new HashMap<String, Object>());
		assertFalse(failures.isEmpty());
		actionB.cancel();
		failures = constraint.validatePlan(game, new HashMap<String, Object>());
		assertTrue(failures.isEmpty());
	}

	@Before
	public void setUp() {
		GameConfig gameConfig = new GameConfig(1, "TestConfig", 5, 5, 700);
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
}
