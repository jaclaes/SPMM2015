/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.alaskasimulator.test.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.alaskasimulator.core.runtime.Game;
import org.alaskasimulator.core.runtime.action.Action;
import org.alaskasimulator.core.runtime.proxy.AuxiliaryAccommodationConfigProxy;
import org.junit.Test;

public class AuxiliaryAccommodationTest {
	@Test
	public void execute() throws Exception {
		TestGame testGame = new TestGame();
		testGame.setJourneyDuration(2);
		testGame.setBudget(1000);
		testGame.createGameConfig();
		Game game = testGame.createGame();
		game.startJourney();

		assertEquals("No business value gained yet.", 0, game.getPlan().getBusinessValue(),0.001);
		Action action = game.getConfig().findActionConfigProxy(TestGame.ACTIVITY_CONFIG_2_NAME).createAction();
		action.setStartTime(0, 0);
		game.getPlan().insertPlanItem(action);
		action.execute();

		assertTrue("Should have some business value gained.", game.getPlan().getBusinessValue() > 0);
		Action accommodation = AuxiliaryAccommodationConfigProxy.getInstance(testGame.getGame()).createAction();
		accommodation.setStartTime(0, TestGame.STANDARD_DAY_DURATION);
		game.getPlan().insertPlanItem(accommodation);
		accommodation.execute();

		assertEquals("Should have lost all business value.", 0, game.getPlan().getBusinessValue(),0.001);
	}
}
