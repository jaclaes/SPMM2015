/*******************************************************************************
* This file is provided under the terms of the Eclipse Public License 1.0. 
* Any use, reproduction or distribution of the program
* constitutes recipient's acceptance of this agreement.
*******************************************************************************/
package org.alaskasimulator.test.core;

import static org.junit.Assert.assertEquals;

import java.util.List;

import junit.framework.Assert;

import org.alaskasimulator.core.buildtime.ActivityConfig;
import org.alaskasimulator.core.buildtime.Certainty;
import org.alaskasimulator.core.buildtime.distribution.EqualDistribution;
import org.alaskasimulator.core.runtime.Game;
import org.alaskasimulator.core.runtime.Time;
import org.alaskasimulator.core.runtime.action.Action;
import org.alaskasimulator.core.runtime.service.CertaintyService;
import org.alaskasimulator.core.runtime.service.ICertaintyService;
import org.alaskasimulator.core.runtime.service.IWeatherService;
import org.alaskasimulator.core.runtime.service.PartialServiceState;
import org.alaskasimulator.core.runtime.service.ServicePersistence;
import org.junit.Test;

public class CertaintyServiceTest
{

	@Test
	public void persist()
	{
		TestGame testGame = new TestGame();
		testGame.setJourneyDuration(2);
		testGame.setCertaintyActivity1(new Certainty(new EqualDistribution(0, 1)));
		testGame.createGameConfig();
		Game game = testGame.createGame();
		game.startJourney();
		ActivityConfig activityConfig = testGame.getActivityConfig1();
		Action action = game.getConfig().findActionConfigProxy(activityConfig).createAction();
		ICertaintyService service = new CertaintyService(game);

		ICertaintyService newService = new CertaintyService(game);
		double restoredCertainty = newService.getActionCertaintyToday(action);

		// set the persistence context, the service must save the data
		// immediately
		ServicePersistence servicePersistence = new ServicePersistence();
		service.setPersistenceContext(servicePersistence.createContext(ICertaintyService.class));
		double expectedCertaintyDay2 = service.getActionCertainty(action.getActionConfig(), new Time(testGame
				.getConfig(), 1, 0));
		double certainty = service.getActionCertaintyToday(action);
		Assert.assertFalse("Should be not be same - service state not restored.", certainty == restoredCertainty);

		List<PartialServiceState> states = servicePersistence.getState();

		for (PartialServiceState state : states)
		{
			newService.updateFrom(new PartialServiceState(IWeatherService.class, WeatherServiceTest
					.unescapeXml(state.getContent()), 0));
		}

		restoredCertainty = newService.getActionCertaintyToday(action);
		double restoredCertaintyDay2 = newService.getActionCertainty(action.getActionConfig(), new Time(testGame
				.getConfig(), 1, 0));

		assertEquals("Should be restored now.", certainty, restoredCertainty, 0.0);
		assertEquals("Should be restored now.", expectedCertaintyDay2, restoredCertaintyDay2, 0.0);
	}
}
