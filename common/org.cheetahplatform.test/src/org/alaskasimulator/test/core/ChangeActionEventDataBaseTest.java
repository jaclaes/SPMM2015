/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.alaskasimulator.test.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.alaskasimulator.core.buildtime.ActionConfig;
import org.alaskasimulator.core.buildtime.ActivityConfig;
import org.alaskasimulator.core.buildtime.Certainty;
import org.alaskasimulator.core.buildtime.DurationRange;
import org.alaskasimulator.core.buildtime.GameConfig;
import org.alaskasimulator.core.buildtime.Location;
import org.alaskasimulator.core.buildtime.TemporalActionConfig;
import org.alaskasimulator.core.buildtime.constraint.BudgetConstraint;
import org.alaskasimulator.core.buildtime.event.ChangeActionConfigEvent;
import org.alaskasimulator.core.buildtime.event.ChangeActionEventDatabase;
import org.alaskasimulator.core.buildtime.event.ChangeTemporalActionConfigEvent;
import org.alaskasimulator.core.runtime.Game;
import org.alaskasimulator.core.runtime.proxy.ActionConfigProxy;
import org.alaskasimulator.core.runtime.proxy.GameConfigProxy;
import org.alaskasimulator.core.runtime.proxy.LocationProxy;
import org.alaskasimulator.core.runtime.proxy.TemporalActionConfigProxy;
import org.junit.Before;
import org.junit.Test;

public class ChangeActionEventDataBaseTest {
	private ActivityConfig actionConfig1;
	private Location location;
	private LocationProxy locationProxy;
	private ActionConfigProxy actionConfigProxy;
	private ChangeActionEventDatabase dataBase;
	private ChangeActionConfigEvent event;
	private GameConfigProxy gameConfigProxy;

	@Test(expected = IllegalStateException.class)
	public void addEventTwice() {
		dataBase.addEvent(event);
		dataBase.addEvent(event);
	}

	@Test
	public void expireMultipleEvents() {
		Certainty certaintyEvent = new Certainty();
		ChangeActionConfigEvent event2 = new ChangeActionConfigEvent("event", "info", "desc", 1.0, actionConfig1, 155.0, 166.0,
				certaintyEvent, 1.0);
		ChangeActionConfigEvent event3 = new ChangeActionConfigEvent("event", "info", "desc", 1.0, actionConfig1, 255.0, 266.0,
				certaintyEvent, 0.25);

		actionConfigProxy.setCost(event.getCost());
		actionConfigProxy.setAvailability(event.getAvailability());
		actionConfigProxy.setCertainty(event.getCertainty());
		actionConfigProxy.setMaxBusinessValue(event.getBusinessValue());
		dataBase.addEvent(event);

		actionConfigProxy.setCost(event2.getCost());
		actionConfigProxy.setAvailability(event2.getAvailability());
		actionConfigProxy.setCertainty(event2.getCertainty());
		actionConfigProxy.setMaxBusinessValue(event2.getBusinessValue());
		dataBase.addEvent(event2);

		actionConfigProxy.setCost(event3.getCost());
		actionConfigProxy.setAvailability(event3.getAvailability());
		actionConfigProxy.setCertainty(event3.getCertainty());
		actionConfigProxy.setMaxBusinessValue(event3.getBusinessValue());
		dataBase.addEvent(event3);

		dataBase.expire(event3, actionConfigProxy);
		dataBase.expire(event2, actionConfigProxy);

		assertEquals("wrong value", event.getAvailability(), actionConfigProxy.getAvailability(),0.001);
		assertEquals("wrong value", event.getCost(), actionConfigProxy.getCost(),0.001);
		assertEquals("wrong value", event.getBusinessValue(), actionConfigProxy.getMaxBusinessValue(),0.001);
		assertEquals("wrong value", event.getCertainty(), actionConfigProxy.getCertainty());

		dataBase.expire(event, actionConfigProxy);

		assertEquals("wrong value", actionConfig1.getAvailability(), actionConfigProxy.getAvailability(),0.001);
		assertEquals("wrong value", actionConfig1.getCost(), actionConfigProxy.getCost(),0.001);
		assertEquals("wrong value", actionConfig1.getMaxBusinessValue(), actionConfigProxy.getMaxBusinessValue(),0.001);
		assertSame("wrong value", actionConfig1.getCertainty(), actionConfigProxy.getCertainty());
	}

	@Test
	public void expireOverlappingEvents() {
		ChangeActionConfigEvent event2 = new ChangeActionConfigEvent("event", "info", "desc", 1, actionConfig1, 2.3, 4.0, new Certainty(),
				1.0);

		actionConfigProxy.setCost(event.getCost());
		actionConfigProxy.setAvailability(event.getAvailability());
		actionConfigProxy.setCertainty(event.getCertainty());
		actionConfigProxy.setMaxBusinessValue(event.getBusinessValue());
		dataBase.addEvent(event);

		actionConfigProxy.setCost(event2.getCost());
		actionConfigProxy.setAvailability(event2.getAvailability());
		actionConfigProxy.setCertainty(event2.getCertainty());
		actionConfigProxy.setMaxBusinessValue(event2.getBusinessValue());
		dataBase.addEvent(event2);

		dataBase.expire(event, actionConfigProxy);
		assertEquals("wrong value", event2.getAvailability(), actionConfigProxy.getAvailability(), 0.0001);
		assertEquals("wrong value", event2.getCost(), actionConfigProxy.getCost(), 0.0001);
		assertEquals("wrong value", event2.getBusinessValue(), actionConfigProxy.getMaxBusinessValue(), 0.0001);
		assertEquals("wrong value", event2.getCertainty(), actionConfigProxy.getCertainty());

		dataBase.expire(event2, actionConfigProxy);

		assertEquals("wrong value", actionConfig1.getAvailability(), actionConfigProxy.getAvailability(), 0.0001);
		assertEquals("wrong value", actionConfig1.getCost(), actionConfigProxy.getCost(), 0.0001);
		assertEquals("wrong value", actionConfig1.getMaxBusinessValue(), actionConfigProxy.getMaxBusinessValue(), 0.0001);
		assertSame("wrong value", actionConfig1.getCertainty(), actionConfigProxy.getCertainty());
	}

	@Test
	public void expirePartlyMultipleEvents() {
		ChangeActionConfigEvent costChangeEvent = new ChangeActionConfigEvent("event", "info", "desc", 1.0, actionConfig1, 100.0, null,
				null, null);
		ChangeActionConfigEvent businessValueChangeEvent = new ChangeActionConfigEvent("event", "info", "desc", 1.0, actionConfig1, null,
				50.0, null, null);

		costChangeEvent.setValues(actionConfigProxy);
		dataBase.addEvent(costChangeEvent);
		assertEquals("wrong value", actionConfig1.getAvailability(), actionConfigProxy.getAvailability(),0.001);
		assertEquals("wrong value", costChangeEvent.getCost(), actionConfigProxy.getCost(),0.001);
		assertEquals("wrong value", actionConfig1.getMaxBusinessValue(), actionConfigProxy.getMaxBusinessValue(),0.001);
		assertSame("wrong value", actionConfig1.getCertainty(), actionConfigProxy.getCertainty());

		businessValueChangeEvent.setValues(actionConfigProxy);
		dataBase.addEvent(businessValueChangeEvent);
		assertEquals("wrong value", actionConfig1.getAvailability(), actionConfigProxy.getAvailability(),0.001);
		assertEquals("wrong value", costChangeEvent.getCost(), actionConfigProxy.getCost(),0.001);
		assertEquals("wrong value", businessValueChangeEvent.getBusinessValue(), actionConfigProxy.getMaxBusinessValue(),0.001);
		assertSame("wrong value", actionConfig1.getCertainty(), actionConfigProxy.getCertainty());

		dataBase.expire(businessValueChangeEvent, actionConfigProxy);
		assertEquals("wrong value", actionConfig1.getAvailability(), actionConfigProxy.getAvailability(),0.001);
		assertEquals("wrong value", costChangeEvent.getCost(), actionConfigProxy.getCost(),0.001);
		assertEquals("wrong value", actionConfig1.getMaxBusinessValue(), actionConfigProxy.getMaxBusinessValue(),0.001);
		assertSame("wrong value", actionConfig1.getCertainty(), actionConfigProxy.getCertainty());

		dataBase.expire(costChangeEvent, actionConfigProxy);
		assertEquals("wrong value", actionConfig1.getAvailability(), actionConfigProxy.getAvailability(),0.001);
		assertEquals("wrong value", actionConfig1.getCost(), actionConfigProxy.getCost(),0.001);
		assertEquals("wrong value", actionConfig1.getMaxBusinessValue(), actionConfigProxy.getMaxBusinessValue(),0.001);
		assertSame("wrong value", actionConfig1.getCertainty(), actionConfigProxy.getCertainty());

	}

	@Test
	public void expirePartlyOverlappingEvents() {
		ChangeActionConfigEvent costChangeEvent = new ChangeActionConfigEvent("event", "info", "desc", 1.0, actionConfig1, 100.0, null,
				null, null);
		ChangeActionConfigEvent businessValueChangeEvent = new ChangeActionConfigEvent("event", "info", "desc", 1.0, actionConfig1, null,
				50.0, null, null);

		costChangeEvent.setValues(actionConfigProxy);
		dataBase.addEvent(costChangeEvent);
		assertEquals("wrong value", actionConfig1.getAvailability(), actionConfigProxy.getAvailability(),0.001);
		assertEquals("wrong value", costChangeEvent.getCost(), actionConfigProxy.getCost(),0.001);
		assertEquals("wrong value", actionConfig1.getMaxBusinessValue(), actionConfigProxy.getMaxBusinessValue(),0.001);
		assertSame("wrong value", actionConfig1.getCertainty(), actionConfigProxy.getCertainty());

		businessValueChangeEvent.setValues(actionConfigProxy);
		dataBase.addEvent(businessValueChangeEvent);
		assertEquals("wrong value", actionConfig1.getAvailability(), actionConfigProxy.getAvailability(),0.001);
		assertEquals("wrong value", costChangeEvent.getCost(), actionConfigProxy.getCost(),0.001);
		assertEquals("wrong value", businessValueChangeEvent.getBusinessValue(), actionConfigProxy.getMaxBusinessValue(),0.001);
		assertSame("wrong value", actionConfig1.getCertainty(), actionConfigProxy.getCertainty());

		dataBase.expire(costChangeEvent, actionConfigProxy);
		assertEquals("wrong value", actionConfig1.getAvailability(), actionConfigProxy.getAvailability(),0.001);
		assertEquals("wrong value", actionConfig1.getCost(), actionConfigProxy.getCost(),0.001);
		assertEquals("wrong value", businessValueChangeEvent.getBusinessValue(), actionConfigProxy.getMaxBusinessValue(),0.001);
		assertSame("wrong value", actionConfig1.getCertainty(), actionConfigProxy.getCertainty());

		dataBase.expire(businessValueChangeEvent, actionConfigProxy);
		assertEquals("wrong value", actionConfig1.getAvailability(), actionConfigProxy.getAvailability(),0.001);
		assertEquals("wrong value", actionConfig1.getCost(), actionConfigProxy.getCost(),0.001);
		assertEquals("wrong value", actionConfig1.getMaxBusinessValue(), actionConfigProxy.getMaxBusinessValue(),0.001);
		assertSame("wrong value", actionConfig1.getCertainty(), actionConfigProxy.getCertainty());
	}

	@Test
	public void expireSingleEvent() {
		dataBase.addEvent(event);

		actionConfigProxy.setCost(55);
		actionConfigProxy.setAvailability(0.75);
		actionConfigProxy.setCertainty(new Certainty());
		actionConfigProxy.setMaxBusinessValue(66);
		((TemporalActionConfigProxy) actionConfigProxy).setDurationRange(new DurationRange(22));

		dataBase.expire(event, actionConfigProxy);

		assertEquals("wrong value", actionConfig1.getAvailability(), actionConfigProxy.getAvailability(),0.001);
		assertEquals("wrong value", actionConfig1.getCost(), actionConfigProxy.getCost(),0.001);
		assertEquals("wrong value", actionConfig1.getMaxBusinessValue(), actionConfigProxy.getMaxBusinessValue(),0.001);
		assertSame("wrong value", actionConfig1.getCertainty(), actionConfigProxy.getCertainty());
		assertSame("wrong value", ((TemporalActionConfig) actionConfig1).getDurationRange(),
				((TemporalActionConfigProxy) actionConfigProxy).getDurationRange());
	}

	@Test(expected = IllegalStateException.class)
	public void expireUnregisteredEvent() {
		dataBase.expire(event, actionConfigProxy);
	}

	@Test
	public void multipleEventsTest() {
		ChangeActionConfigEvent costChangeEvent = new ChangeActionConfigEvent("event", "info", "desc", 1.0, actionConfig1, 100.0, null,
				null, null);
		ChangeActionConfigEvent businessValueChangeEvent = new ChangeActionConfigEvent("event", "info", "desc", 1.0, actionConfig1, null,
				50.0, null, null);
		ChangeActionConfigEvent certaintyChangeEvent = new ChangeActionConfigEvent("event", "info", "desc", 1.0, actionConfig1, null, null,
				new Certainty(), null);
		ChangeActionConfigEvent availabilityChangeEvent = new ChangeActionConfigEvent("event", "info", "desc", 1.0, actionConfig1, null,
				null, null, 0.123);

		costChangeEvent.setValues(actionConfigProxy);
		dataBase.addEvent(costChangeEvent);
		assertEquals("wrong value", actionConfig1.getAvailability(), actionConfigProxy.getAvailability(),0.001);
		assertEquals("wrong value", costChangeEvent.getCost(), actionConfigProxy.getCost(),0.001);
		assertEquals("wrong value", actionConfig1.getMaxBusinessValue(), actionConfigProxy.getMaxBusinessValue(),0.001);
		assertSame("wrong value", actionConfig1.getCertainty(), actionConfigProxy.getCertainty());

		businessValueChangeEvent.setValues(actionConfigProxy);
		dataBase.addEvent(businessValueChangeEvent);
		assertEquals("wrong value", actionConfig1.getAvailability(), actionConfigProxy.getAvailability(),0.001);
		assertEquals("wrong value", costChangeEvent.getCost(), actionConfigProxy.getCost(),0.001);
		assertEquals("wrong value", businessValueChangeEvent.getBusinessValue(), actionConfigProxy.getMaxBusinessValue(),0.001);
		assertSame("wrong value", actionConfig1.getCertainty(), actionConfigProxy.getCertainty());

		certaintyChangeEvent.setValues(actionConfigProxy);
		dataBase.addEvent(certaintyChangeEvent);
		assertEquals("wrong value", actionConfig1.getAvailability(), actionConfigProxy.getAvailability(),0.001);
		assertEquals("wrong value", costChangeEvent.getCost(), actionConfigProxy.getCost(),0.001);
		assertEquals("wrong value", businessValueChangeEvent.getBusinessValue(), actionConfigProxy.getMaxBusinessValue(),0.001);
		assertEquals("wrong value", certaintyChangeEvent.getCertainty(), actionConfigProxy.getCertainty());

		availabilityChangeEvent.setValues(actionConfigProxy);
		dataBase.addEvent(availabilityChangeEvent);
		assertEquals("wrong value", availabilityChangeEvent.getAvailability(), actionConfigProxy.getAvailability(),0.001);
		assertEquals("wrong value", costChangeEvent.getCost(), actionConfigProxy.getCost(),0.001);
		assertEquals("wrong value", businessValueChangeEvent.getBusinessValue(), actionConfigProxy.getMaxBusinessValue(),0.001);
		assertEquals("wrong value", certaintyChangeEvent.getCertainty(), actionConfigProxy.getCertainty());

		dataBase.expire(availabilityChangeEvent, actionConfigProxy);
		assertEquals("wrong value", actionConfig1.getAvailability(), actionConfigProxy.getAvailability(),0.001);
		assertEquals("wrong value", costChangeEvent.getCost(), actionConfigProxy.getCost(),0.001);
		assertEquals("wrong value", businessValueChangeEvent.getBusinessValue(), actionConfigProxy.getMaxBusinessValue(),0.001);
		assertEquals("wrong value", certaintyChangeEvent.getCertainty(), actionConfigProxy.getCertainty());

		dataBase.expire(costChangeEvent, actionConfigProxy);
		assertEquals("wrong value", actionConfig1.getAvailability(), actionConfigProxy.getAvailability(),0.001);
		assertEquals("wrong value", actionConfig1.getCost(), actionConfigProxy.getCost(),0.001);
		assertEquals("wrong value", businessValueChangeEvent.getBusinessValue(), actionConfigProxy.getMaxBusinessValue(),0.001);
		assertEquals("wrong value", certaintyChangeEvent.getCertainty(), actionConfigProxy.getCertainty());

		dataBase.expire(businessValueChangeEvent, actionConfigProxy);
		assertEquals("wrong value", actionConfig1.getAvailability(), actionConfigProxy.getAvailability(),0.001);
		assertEquals("wrong value", actionConfig1.getCost(), actionConfigProxy.getCost(),0.001);
		assertEquals("wrong value", actionConfig1.getMaxBusinessValue(), actionConfigProxy.getMaxBusinessValue(),0.001);
		assertEquals("wrong value", certaintyChangeEvent.getCertainty(), actionConfigProxy.getCertainty());

		dataBase.expire(certaintyChangeEvent, actionConfigProxy);
		assertEquals("wrong value", actionConfig1.getAvailability(), actionConfigProxy.getAvailability(),0.001);
		assertEquals("wrong value", actionConfig1.getCost(), actionConfigProxy.getCost(),0.001);
		assertEquals("wrong value", actionConfig1.getMaxBusinessValue(), actionConfigProxy.getMaxBusinessValue(),0.001);
		assertEquals("wrong value", actionConfig1.getCertainty(), actionConfigProxy.getCertainty());
	}

	@Test
	public void overlappingTemporalActionEvents() {
		TemporalActionConfigProxy temporalActionConfigProxy = (TemporalActionConfigProxy) actionConfigProxy;
		ChangeTemporalActionConfigEvent changeTemporalActionConfigEvent1 = new ChangeTemporalActionConfigEvent("event", "info", "desc",
				1.0, actionConfig1, null, null, null, null, null, new DurationRange(10, 20));
		ChangeTemporalActionConfigEvent changeTemporalActionConfigEvent2 = new ChangeTemporalActionConfigEvent("event", "info", "desc",
				1.0, actionConfig1, null, null, null, null, null, new DurationRange(30, 40));

		changeTemporalActionConfigEvent1.setValues(temporalActionConfigProxy);
		dataBase.addEvent(changeTemporalActionConfigEvent1);
		assertEquals("wrong value", actionConfig1.getAvailability(), actionConfigProxy.getAvailability(),0.001);
		assertEquals("wrong value", actionConfig1.getCost(), actionConfigProxy.getCost(),0.001);
		assertEquals("wrong value", actionConfig1.getMaxBusinessValue(), actionConfigProxy.getMaxBusinessValue(),0.001);
		assertSame("wrong value", actionConfig1.getCertainty(), actionConfigProxy.getCertainty());
		assertSame("wrong value", changeTemporalActionConfigEvent1.getDurationRange(), temporalActionConfigProxy.getDurationRange());

		changeTemporalActionConfigEvent2.setValues(temporalActionConfigProxy);
		dataBase.addEvent(changeTemporalActionConfigEvent2);
		assertEquals("wrong value", actionConfig1.getAvailability(), actionConfigProxy.getAvailability(),0.001);
		assertEquals("wrong value", actionConfig1.getCost(), actionConfigProxy.getCost(),0.001);
		assertEquals("wrong value", actionConfig1.getMaxBusinessValue(), actionConfigProxy.getMaxBusinessValue(),0.001);
		assertSame("wrong value", actionConfig1.getCertainty(), actionConfigProxy.getCertainty());
		assertSame("wrong value", changeTemporalActionConfigEvent2.getDurationRange(), temporalActionConfigProxy.getDurationRange());

		dataBase.expire(changeTemporalActionConfigEvent1, temporalActionConfigProxy);
		assertEquals("wrong value", actionConfig1.getAvailability(), actionConfigProxy.getAvailability(),0.001);
		assertEquals("wrong value", actionConfig1.getCost(), actionConfigProxy.getCost(),0.001);
		assertEquals("wrong value", actionConfig1.getMaxBusinessValue(), actionConfigProxy.getMaxBusinessValue(),0.001);
		assertSame("wrong value", actionConfig1.getCertainty(), actionConfigProxy.getCertainty());
		assertSame("wrong value", changeTemporalActionConfigEvent2.getDurationRange(), temporalActionConfigProxy.getDurationRange());

		dataBase.expire(changeTemporalActionConfigEvent2, temporalActionConfigProxy);
		assertEquals("wrong value", actionConfig1.getAvailability(), actionConfigProxy.getAvailability(),0.001);
		assertEquals("wrong value", actionConfig1.getCost(), actionConfigProxy.getCost(),0.001);
		assertEquals("wrong value", actionConfig1.getMaxBusinessValue(), actionConfigProxy.getMaxBusinessValue(),0.001);
		assertSame("wrong value", actionConfig1.getCertainty(), actionConfigProxy.getCertainty());
		assertSame("wrong value", actionConfig1.getDurationRange(), temporalActionConfigProxy.getDurationRange());
	}

	@Before
	public void setUp() {
		GameConfig gameConfig = new GameConfig(0, "Testgame", 15, 3, 1440);
		gameConfig.addConstraint(new BudgetConstraint(20));
		location = new Location(gameConfig, "Testort");

		Game game = new Game(gameConfig, "name");
		gameConfigProxy = game.getConfig();
		locationProxy = gameConfigProxy.findLocation(location);
		actionConfig1 = new ActivityConfig("ActivityConfig1", 5, 24, new Certainty(), 1.0, new DurationRange(60), ActionConfig.NOT_BOOKABLE);
		location.addActionConfig(actionConfig1);
		actionConfigProxy = actionConfig1.createProxy(locationProxy);
		dataBase = new ChangeActionEventDatabase();
		event = new ChangeActionConfigEvent("event", "info", "desc", 1.0, actionConfig1, 55.0, 66.0, new Certainty(), 0.75);
	}

	@Test
	public void temporalActionEvent() {
		TemporalActionConfigProxy temporalActionConfigProxy = (TemporalActionConfigProxy) actionConfigProxy;
		ChangeTemporalActionConfigEvent changeTemporalActionConfigEvent1 = new ChangeTemporalActionConfigEvent("event", "info", "desc",
				1.0, actionConfig1, null, null, null, null, null, new DurationRange(10, 20));
		ChangeTemporalActionConfigEvent changeTemporalActionConfigEvent2 = new ChangeTemporalActionConfigEvent("event", "info", "desc",
				1.0, actionConfig1, null, null, null, null, null, new DurationRange(30, 40));

		changeTemporalActionConfigEvent1.setValues(temporalActionConfigProxy);
		dataBase.addEvent(changeTemporalActionConfigEvent1);
		assertEquals("wrong value", actionConfig1.getAvailability(), actionConfigProxy.getAvailability(),0.001);
		assertEquals("wrong value", actionConfig1.getCost(), actionConfigProxy.getCost(),0.001);
		assertEquals("wrong value", actionConfig1.getMaxBusinessValue(), actionConfigProxy.getMaxBusinessValue(),0.001);
		assertSame("wrong value", actionConfig1.getCertainty(), actionConfigProxy.getCertainty());
		assertSame("wrong value", changeTemporalActionConfigEvent1.getDurationRange(), temporalActionConfigProxy.getDurationRange());

		changeTemporalActionConfigEvent2.setValues(temporalActionConfigProxy);
		dataBase.addEvent(changeTemporalActionConfigEvent2);
		assertEquals("wrong value", actionConfig1.getAvailability(), actionConfigProxy.getAvailability(),0.001);
		assertEquals("wrong value", actionConfig1.getCost(), actionConfigProxy.getCost(),0.001);
		assertEquals("wrong value", actionConfig1.getMaxBusinessValue(), actionConfigProxy.getMaxBusinessValue(),0.001);
		assertSame("wrong value", actionConfig1.getCertainty(), actionConfigProxy.getCertainty());
		assertSame("wrong value", changeTemporalActionConfigEvent2.getDurationRange(), temporalActionConfigProxy.getDurationRange());

		dataBase.expire(changeTemporalActionConfigEvent2, temporalActionConfigProxy);
		assertEquals("wrong value", actionConfig1.getAvailability(), actionConfigProxy.getAvailability(),0.001);
		assertEquals("wrong value", actionConfig1.getCost(), actionConfigProxy.getCost(),0.001);
		assertEquals("wrong value", actionConfig1.getMaxBusinessValue(), actionConfigProxy.getMaxBusinessValue(),0.001);
		assertSame("wrong value", actionConfig1.getCertainty(), actionConfigProxy.getCertainty());
		assertSame("wrong value", changeTemporalActionConfigEvent1.getDurationRange(), temporalActionConfigProxy.getDurationRange());

		dataBase.expire(changeTemporalActionConfigEvent1, temporalActionConfigProxy);
		assertEquals("wrong value", actionConfig1.getAvailability(), actionConfigProxy.getAvailability(),0.001);
		assertEquals("wrong value", actionConfig1.getCost(), actionConfigProxy.getCost(),0.001);
		assertEquals("wrong value", actionConfig1.getMaxBusinessValue(), actionConfigProxy.getMaxBusinessValue(),0.001);
		assertSame("wrong value", actionConfig1.getCertainty(), actionConfigProxy.getCertainty());
		assertSame("wrong value", actionConfig1.getDurationRange(), temporalActionConfigProxy.getDurationRange());
	}
}
