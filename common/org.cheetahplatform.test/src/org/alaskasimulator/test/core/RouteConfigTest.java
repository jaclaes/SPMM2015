/*******************************************************************************
* This file is provided under the terms of the Eclipse Public License 1.0. 
* Any use, reproduction or distribution of the program
* constitutes recipient's acceptance of this agreement.
*******************************************************************************/
package org.alaskasimulator.test.core;

import org.alaskasimulator.core.buildtime.ActionConfig;
import org.alaskasimulator.core.buildtime.Certainty;
import org.alaskasimulator.core.buildtime.DurationRange;
import org.alaskasimulator.core.buildtime.GameConfig;
import org.alaskasimulator.core.buildtime.Location;
import org.alaskasimulator.core.buildtime.RouteConfig;
import org.alaskasimulator.core.runtime.Game;
import org.alaskasimulator.core.runtime.proxy.LocationProxy;
import org.junit.Test;

public class RouteConfigTest
{

	@Test(expected = IllegalArgumentException.class)
	public void createProxyIllegal()
	{
		GameConfig config = new GameConfig();
		Location schwaz = new Location(config, "Schwaz");
		Location lamsenjoch = new Location(config, "Lamsenjoch");
		Location plumsjoch = new Location(config, "Plumsjoch");

		RouteConfig route = new RouteConfig("My nice route.", 0, 1000, new Certainty(), 1.0, new DurationRange(10),
				lamsenjoch, plumsjoch, ActionConfig.NOT_BOOKABLE);
		lamsenjoch.addActionConfig(route);
		plumsjoch.addActionConfig(route);

		Game game = new Game(config, "Max Mutzke");
		game.startJourney();
		LocationProxy proxy = game.getConfig().findLocation(schwaz);
		route.createProxy(proxy);
	}
}
