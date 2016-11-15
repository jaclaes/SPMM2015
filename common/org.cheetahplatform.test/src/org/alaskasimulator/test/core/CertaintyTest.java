/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.alaskasimulator.test.core;

import static org.junit.Assert.assertTrue;

import org.alaskasimulator.core.buildtime.AccommodationConfig;
import org.alaskasimulator.core.buildtime.ActionConfig;
import org.alaskasimulator.core.buildtime.Certainty;
import org.alaskasimulator.core.buildtime.GameConfig;
import org.alaskasimulator.core.buildtime.Location;
import org.alaskasimulator.core.buildtime.distribution.EqualDistribution;
import org.alaskasimulator.core.runtime.Game;
import org.alaskasimulator.core.runtime.Time;
import org.alaskasimulator.core.runtime.action.Action;
import org.alaskasimulator.core.runtime.proxy.ActionConfigProxy;
import org.alaskasimulator.core.runtime.service.ICertaintyService;
import org.junit.Test;

public class CertaintyTest {
	private GameConfig gameConfig;
	private Location location;
	private ActionConfig actionConfig;
	private Certainty certainty;

	@Test
	public void boundedWeatherInfluence() {
		certainty = new Certainty(new EqualDistribution(0.4, 0.5), 0.3);
		createGameConfig();

		Game game;
		int rep = 1000;
		while (rep-- > 0) {
			game = new Game(gameConfig, "baba");
			game.startJourney();
			ActionConfigProxy actionConfigProxy = game.getConfig().findActionConfigProxy(actionConfig);
			ICertaintyService service = game.getServiceProvider().getCertaintyService();
			double certainty = service.getActionCertainty(actionConfigProxy, new Time(game));
			assertTrue("Certainty should be limited by low influence factor of weather", certainty >= 0.1);
			assertTrue("Certainty should be limited by low influence factor of weather", certainty <= 0.8);
		}
	}

	private void createGameConfig() {
		gameConfig = new GameConfig();
		gameConfig.setBusinessValueConstraintSleepPenalty();
		location = new Location(gameConfig, "OrtA");
		actionConfig = new AccommodationConfig("Hotel", 5, 100, certainty, 1.0, ActionConfig.NOT_BOOKABLE);
		location.addActionConfig(actionConfig);
	}

	@Test
	public void defaultCertainty() {
		double weatherInfluence = 0.2;// * new Random().nextDouble();
		certainty = new Certainty(new EqualDistribution(0.3, 0.3), weatherInfluence);
		createGameConfig();

		Game game;

		int rep = 10000;
		while (rep-- > 0) {
			game = new Game(gameConfig, "baba");
			game.startJourney();
			Action action = game.getConfig().findActionConfigProxy(actionConfig).createAction();
			ICertaintyService service = game.getServiceProvider().getCertaintyService();

			double certainty = service.getActionCertainty(action.getActionConfig(), new Time(game));
			assertTrue(certainty + " >= " + 0.2, certainty >= 0.1999);
			assertTrue(certainty + " <= " + 0.4, certainty <= 0.4001);
			action.setStartTime(0, game.getDayDuration() - 1);
			game.getPlan().insertPlanItem(action);
			action.execute();

			certainty = service.getActionCertainty(action.getActionConfig(), new Time(game, 1, 0));
			assertTrue(certainty + " >= " + 0.2, certainty >= 0.1999);
			assertTrue(certainty + " <= " + 0.4, certainty <= 0.4001);
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void illegalConstructor1() {
		new Certainty(-1.0001);
	}

	@Test(expected = IllegalArgumentException.class)
	public void illegalConstructor2() {
		new Certainty(1.00001);
	}
}
