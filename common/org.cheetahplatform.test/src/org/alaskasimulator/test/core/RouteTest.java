/*******************************************************************************
* This file is provided under the terms of the Eclipse Public License 1.0. 
* Any use, reproduction or distribution of the program
* constitutes recipient's acceptance of this agreement.
*******************************************************************************/
package org.alaskasimulator.test.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.alaskasimulator.core.buildtime.ActionConfig;
import org.alaskasimulator.core.buildtime.Certainty;
import org.alaskasimulator.core.buildtime.DurationRange;
import org.alaskasimulator.core.buildtime.GameConfig;
import org.alaskasimulator.core.buildtime.Location;
import org.alaskasimulator.core.buildtime.RouteConfig;
import org.alaskasimulator.core.runtime.Game;
import org.alaskasimulator.core.runtime.action.Action;
import org.alaskasimulator.core.runtime.proxy.RouteConfigProxy;
import org.junit.Test;

public class RouteTest
{
	private Location locationA;
	private Location locationB;
	private Game game;
	private GameConfig gameConfig;
	private RouteConfig routeConfig;

	@Test
	public void execute()
	{
		createGame();
		game.startJourney();

		RouteConfigProxy routeConfigProxy = (RouteConfigProxy) game.getConfig().findActionConfigProxy(routeConfig);
		Action route = routeConfigProxy.createAction();
		route.setStartTime(0,0);
		game.getPlan().insertPlanItem(route);
		assertTrue("Route should be executable.", route.isExecutable());
		route.execute();

		assertEquals("Should now be at \"Sagzahn\"", game.getConfig().findLocation(locationB), game
				.getCurrentLocation());
	}

	@Test(expected = IllegalArgumentException.class)
	public void illegalConstructor()
	{
		Location location = new Location(new GameConfig(), "Sonnwendjoch");
		new RouteConfig("Route", 10, 10, new Certainty(), 1, new DurationRange(1), location, location,
				ActionConfig.NOT_BOOKABLE);
	}

	private void createGame()
	{
		gameConfig = new GameConfig();
		locationA = new Location(gameConfig, "Sonnwendjoch");
		locationB = new Location(gameConfig, "Sagzahn");
		routeConfig = new RouteConfig("Wanderweg", 0, 600, new Certainty(), 1, new DurationRange(15, 30), locationA,
				locationB, ActionConfig.NOT_BOOKABLE);
		locationA.addActionConfig(routeConfig);
		locationB.addActionConfig(routeConfig);

		game = new Game(gameConfig, "John Doe");
	}
}
