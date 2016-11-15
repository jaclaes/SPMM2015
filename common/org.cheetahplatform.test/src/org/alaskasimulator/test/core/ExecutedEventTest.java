/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.alaskasimulator.test.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.alaskasimulator.core.buildtime.ActionConfig;
import org.alaskasimulator.core.buildtime.ActivityConfig;
import org.alaskasimulator.core.buildtime.Certainty;
import org.alaskasimulator.core.buildtime.DurationRange;
import org.alaskasimulator.core.buildtime.GameConfig;
import org.alaskasimulator.core.buildtime.LocalWeatherCharacteristics;
import org.alaskasimulator.core.buildtime.Location;
import org.alaskasimulator.core.buildtime.UtmCoordinate;
import org.alaskasimulator.core.buildtime.event.Event;
import org.alaskasimulator.core.runtime.ExecutedEvent;
import org.alaskasimulator.core.runtime.Game;
import org.alaskasimulator.core.runtime.Time;
import org.alaskasimulator.core.runtime.TwentyFourHourTime;
import org.alaskasimulator.core.runtime.proxy.ActionConfigProxy;
import org.alaskasimulator.core.runtime.proxy.GameConfigProxy;
import org.junit.Before;
import org.junit.Test;

public class ExecutedEventTest {
	private static class DummyEvent extends Event {
		private final ActionConfig config;

		public DummyEvent(ActionConfig config, DurationRange expiration) {
			super("name", "shortInfo", "desc", 1.0, expiration);
			this.config = config;
		}

		@Override
		public void execute(GameConfigProxy gameConfigProxy) {
			// does nothing
		}

		@Override
		public void expire(GameConfigProxy gameConfigProxy) {
			// does nothing
		}

		@Override
		public ActionConfig getActionConfig() {
			return config;
		}

		@Override
		public double getAvailability() {
			return 0;
		}

		@Override
		public boolean influencesAvailability() {
			return false;
		}

	}

	private Game game;

	private ActionConfigProxy activityAProxy;

	private ActivityConfig activityConfigA;

	private ActionConfigProxy activityBProxy;

	@Test
	public void compareWithUnexpirableEvent() {
		DummyEvent event = new DummyEvent(activityConfigA, null);
		ExecutedEvent executedEvent = new ExecutedEvent(game, event, new Time(game, 0, 0));
		assertTrue(executedEvent.influences(activityAProxy, new TwentyFourHourTime(game, 0, 640)));
	}

	@Test
	public void influencesActionConfig() {
		DummyEvent event = new DummyEvent(activityConfigA, new DurationRange(100));
		ExecutedEvent executedEvent = new ExecutedEvent(game, event, new Time(game, 0, 0));
		assertTrue(executedEvent.influences(activityAProxy, new TwentyFourHourTime(game, 0, 540)));
	}

	@Test
	public void influencesAfterEventExpiration() {
		DummyEvent event = new DummyEvent(activityConfigA, new DurationRange(100));
		ExecutedEvent executedEvent = new ExecutedEvent(game, event, new Time(game, 0, 000));
		assertFalse(executedEvent.influences(activityAProxy, new TwentyFourHourTime(game, 0, 640)));
	}

	@Test
	public void influencesBeforeEventExecution() {
		DummyEvent event = new DummyEvent(activityConfigA, new DurationRange(100));
		ExecutedEvent executedEvent = new ExecutedEvent(game, event, new Time(game, 0, 400));
		assertFalse(executedEvent.influences(activityAProxy, new TwentyFourHourTime(game, 0, 540)));
	}

	@Test
	public void influencingOtherActionConfigs() {
		DummyEvent event = new DummyEvent(activityConfigA, new DurationRange(100));
		ExecutedEvent executedEvent = new ExecutedEvent(game, event, new Time(game, 0, 0));
		assertTrue(executedEvent.influences(activityAProxy, new TwentyFourHourTime(game, 0, 540)));
		assertFalse(executedEvent.influences(activityBProxy, new TwentyFourHourTime(game, 0, 540)));
	}

	@Before
	public void setUp() {
		GameConfig gameConfig = new GameConfig(1, "ResponseTestConfig", 5, 5, 700);
		Location locationA = new Location(gameConfig, "Location A", new UtmCoordinate(1, 266000, 0));
		LocalWeatherCharacteristics weatherCharacteristics = new LocalWeatherCharacteristics(0.3, 0.3);
		locationA.setWeatherCharacteristics(weatherCharacteristics);
		activityConfigA = new ActivityConfig("Activity A", 10.0, 100, new Certainty(1.0), 1.0, new DurationRange(100), 4);
		locationA.addActionConfig(activityConfigA);
		ActivityConfig activityConfigB = new ActivityConfig("Activity B", 10.0, 100, new Certainty(1.0), 1.0, new DurationRange(100), 4);
		locationA.addActionConfig(activityConfigB);

		game = new Game(gameConfig, "player");
		game.startJourney();

		activityAProxy = game.getConfig().findActionConfigProxy(activityConfigA);
		activityBProxy = game.getConfig().findActionConfigProxy(activityConfigB);
	}
}
