/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.alaskasimulator.test.core;

import static org.junit.Assert.assertEquals;

import org.alaskasimulator.core.runtime.Game;
import org.alaskasimulator.core.runtime.action.Action;
import org.alaskasimulator.core.runtime.proxy.ActionConfigProxy;
import org.alaskasimulator.core.runtime.proxy.AuxiliaryAccommodationConfigProxy;
import org.alaskasimulator.core.runtime.service.BusinessValueService;
import org.alaskasimulator.core.runtime.service.IBusinessValueConstraint;
import org.junit.Before;
import org.junit.Test;

public class BusinessValueServiceTest {
	private static class BusinessValueConstraint implements IBusinessValueConstraint {

		@Override
		public double adjustBusinessValue(double businessValue, Action action) {
			return businessValue * 1.1;
		}

		@Override
		public double adjustExpectedBusinessValue(double businessValue, ActionConfigProxy actionConfigProxy) {
			return businessValue * 1.1;
		}

		@Override
		public boolean isRelevant(ActionConfigProxy actionConfigProxy, Game game) {
			return true;
		}

	}

	private TestGame testGame;
	private Game game;

	@Test
	public void addRemoveConstraints() {
		BusinessValueService businessValueService = game.getServiceProvider().getBusinessValueService();
		ActionConfigProxy actionConfigProxy = game.getConfig().findActionConfigProxy(TestGame.ACTIVITY_CONFIG_2_NAME);
		double calculateExpectedBusinessValue = businessValueService.calculateExpectedBusinessValue(actionConfigProxy);
		assertEquals("wrong expected BV", 10, calculateExpectedBusinessValue,0.001);

		BusinessValueConstraint constraint = new BusinessValueConstraint();
		businessValueService.addBusinessValueConstraint(constraint);

		calculateExpectedBusinessValue = businessValueService.calculateExpectedBusinessValue(actionConfigProxy);
		assertEquals("wrong expected BV", 11, calculateExpectedBusinessValue,0.001);

		businessValueService.removeBusinessValueConstraint(constraint);
		calculateExpectedBusinessValue = businessValueService.calculateExpectedBusinessValue(actionConfigProxy);
		assertEquals("wrong expected BV", 10, calculateExpectedBusinessValue,0.001);
	}

	@Before
	public void before() {
		testGame = new TestGame();
		testGame.setJourneyDuration(2);
		testGame.setDayDuration(720);
		testGame.setBudget(1000);
		testGame.createGameConfig();
		game = testGame.createGame();
		game.startJourney();
	}

	@Test
	public void businessValueConstraintsTest() {
		BusinessValueService businessValueService = game.getServiceProvider().getBusinessValueService();
		businessValueService.addBusinessValueConstraint(new BusinessValueConstraint());

		ActionConfigProxy actionConfigProxy = game.getConfig().findActionConfigProxy(TestGame.ACTIVITY_CONFIG_2_NAME);
		double calculateExpectedBusinessValue = businessValueService.calculateExpectedBusinessValue(actionConfigProxy);
		assertEquals("wrong expected BV", 11, calculateExpectedBusinessValue,0.001);

		Action action = actionConfigProxy.createAction();
		action.setStartTime(0, 0);
		game.getPlan().insertPlanItem(action);
		action.execute();
		double calculateBusinessValue = action.getBusinessValue();
		assertEquals("wrong BV", 11, calculateBusinessValue,0.001);
	}

	@Test
	public void calculateBusinessValueOfAuxiliaryAction() throws Exception {

		assertEquals("No business value gained yet.", 0, game.getPlan().getBusinessValue(),0.001);
		Action action = game.getConfig().findActionConfigProxy(TestGame.ACTIVITY_CONFIG_2_NAME).createAction();
		action.setStartTime(0, 0);
		game.getPlan().insertPlanItem(action);
		action.execute();
		assertEquals("Should have some business value gained.", 10, game.getPlan().getBusinessValue(),0.001);

		Action action2 = game.getConfig().findActionConfigProxy(TestGame.ACTIVITY_CONFIG_3_NAME).createAction();
		action2.setStartTime(0, 130);
		game.getPlan().insertPlanItem(action2);
		action2.execute();
		assertEquals("Should have some business value gained.", 20, game.getPlan().getBusinessValue(),0.001);

		Action accommodation = AuxiliaryAccommodationConfigProxy.getInstance(testGame.getGame()).createAction();
		accommodation.setStartTime(0, 720);
		game.getPlan().insertPlanItem(accommodation);
		accommodation.execute();

		assertEquals("Should have lost all business value.", 0, game.getPlan().getBusinessValue(),0.001);
	}
}
