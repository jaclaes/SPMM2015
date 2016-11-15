/*******************************************************************************
* This file is provided under the terms of the Eclipse Public License 1.0. 
* Any use, reproduction or distribution of the program
* constitutes recipient's acceptance of this agreement.
*******************************************************************************/
package org.alaskasimulator.test.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.alaskasimulator.core.buildtime.AccommodationConfig;
import org.alaskasimulator.core.buildtime.ActionConfig;
import org.alaskasimulator.core.buildtime.ActivityConfig;
import org.alaskasimulator.core.buildtime.Certainty;
import org.alaskasimulator.core.buildtime.DurationRange;
import org.alaskasimulator.core.buildtime.GameConfig;
import org.alaskasimulator.core.buildtime.Location;
import org.alaskasimulator.core.buildtime.RouteConfig;
import org.junit.Test;

public class LocationTest
{

	@Test
	public void create()
	{
		GameConfig config = new GameConfig();
		assertEquals("Wrong amount of locations.", 0, config.getLocations().size());

		new Location(config, "Lamsenjoch");
		assertEquals("Wrong amount of locations.", 1, config.getLocations().size());
	}

	@Test
	public void getActivities()
	{
		GameConfig config = new GameConfig();
		Location location1 = new Location(config, "Lamsenjoch");
		Location location2 = new Location(config, "Plumsjoch");

		ActivityConfig activityConfig = new ActivityConfig("A1", 10, 10, new Certainty(), 1, new DurationRange(10),
				ActionConfig.NOT_BOOKABLE);
		location1.addActionConfig(activityConfig);
		AccommodationConfig accommodationConfig = new AccommodationConfig("Accommodation 1", 10, 10, new Certainty(),
				1.0, ActionConfig.NOT_BOOKABLE);
		location1.addActionConfig(accommodationConfig);
		RouteConfig routeConfig = new RouteConfig("Highway", 1, 1, new Certainty(), 1, new DurationRange(10),
				location1, location2, ActionConfig.NOT_BOOKABLE);
		location1.addActionConfig(routeConfig);
		location2.addActionConfig(routeConfig);

		assertTrue("Activity should be in this location", location1.getActionConfigs().contains(activityConfig));
		assertTrue("Accommodation should be in this location", location1.getActionConfigs().contains(
				accommodationConfig));
		assertTrue("Route should be in this location", location1.getActionConfigs().contains(routeConfig));

		assertEquals("Wrong amount of actions", 3, location1.getActionConfigs().size());
		assertEquals("Wrong amount of actions", 1, location2.getActionConfigs().size());
	}

}
