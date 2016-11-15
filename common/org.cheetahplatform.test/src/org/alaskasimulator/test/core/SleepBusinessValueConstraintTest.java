/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.alaskasimulator.test.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.alaskasimulator.core.runtime.Game;
import org.alaskasimulator.core.runtime.action.Action;
import org.alaskasimulator.core.runtime.action.SleepBusinessValueConstraint;
import org.alaskasimulator.core.runtime.proxy.ActionConfigProxy;
import org.eclipse.core.runtime.AssertionFailedException;
import org.junit.Before;
import org.junit.Test;

public class SleepBusinessValueConstraintTest {
	private TestGame testGame;
	private Game game;

	@Before
	public void before() {
		testGame = new TestGame();
		testGame.setJourneyDuration(7);
		testGame.setDayDuration(720);
		testGame.setBudget(1000);
		testGame.createGameConfig();
		game = testGame.createGame();
		game.startJourney();
	}

	@Test
	public void calculateBusinessValue() {
		ActionConfigProxy actionConfigProxy = game.getConfig().findActionConfigProxy(TestGame.ACTIVITY_CONFIG_4_NAME);
		Action action = actionConfigProxy.createAction();
		action.setStartTime(0, 700);
		game.getPlan().insertPlanItem(action);
		action.execute();

		SleepBusinessValueConstraint sleepBusinessValueConstraint = new SleepBusinessValueConstraint(2, action, game);

		double adjustExpectedBusinessValue = sleepBusinessValueConstraint.adjustBusinessValue(1000, null);
		assertEquals("wrong bv", Double.doubleToLongBits(695.0), Double.doubleToLongBits(adjustExpectedBusinessValue));
	}

	@Test
	public void calculateExpectedBusinessValue() {
		ActionConfigProxy actionConfigProxy = game.getConfig().findActionConfigProxy(TestGame.ACTIVITY_CONFIG_4_NAME);
		Action action = actionConfigProxy.createAction();
		action.setStartTime(0, 700);
		game.getPlan().insertPlanItem(action);
		action.execute();

		SleepBusinessValueConstraint sleepBusinessValueConstraint = new SleepBusinessValueConstraint(2, action, game);

		double adjustExpectedBusinessValue = sleepBusinessValueConstraint.adjustExpectedBusinessValue(1000, null);
		assertEquals("wrong bv", Double.doubleToLongBits(695.0), Double.doubleToLongBits(adjustExpectedBusinessValue));
	}

	@Test(expected = AssertionFailedException.class)
	public void illegalAction() {
		new SleepBusinessValueConstraint(1, null, game);
	}

	@Test(expected = IllegalArgumentException.class)
	public void illegalApplicableDay() {
		ActionConfigProxy actionConfigProxy = game.getConfig().findActionConfigProxy(TestGame.ACTIVITY_CONFIG_2_NAME);

		new SleepBusinessValueConstraint(-1, actionConfigProxy.createAction(), game);
	}

	@Test(expected = AssertionFailedException.class)
	public void illegalGame() {
		ActionConfigProxy actionConfigProxy = game.getConfig().findActionConfigProxy(TestGame.ACTIVITY_CONFIG_2_NAME);
		new SleepBusinessValueConstraint(1, actionConfigProxy.createAction(), null);
	}

	@Test
	public void isRelevant() {
		ActionConfigProxy actionConfigProxy = game.getConfig().findActionConfigProxy(TestGame.ACTIVITY_CONFIG_2_NAME);
		Action action = actionConfigProxy.createAction();
		action.setStartTime(0, 0);
		game.getPlan().insertPlanItem(action);
		action.execute();
		SleepBusinessValueConstraint sleepBusinessValueConstraint = new SleepBusinessValueConstraint(2, action, game);

		boolean relevant = sleepBusinessValueConstraint.isRelevant(actionConfigProxy, game);
		assertFalse("not relevant", relevant);
		game.getCurrentTime().increaseDay(2);
		relevant = sleepBusinessValueConstraint.isRelevant(actionConfigProxy, game);
		assertTrue("should be relevant", relevant);
		game.getCurrentTime().increaseDay(1);
		relevant = sleepBusinessValueConstraint.isRelevant(actionConfigProxy, game);
		assertFalse("not relevant", relevant);
	}
}
