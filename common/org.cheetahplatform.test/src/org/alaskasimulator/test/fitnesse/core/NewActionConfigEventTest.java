/*******************************************************************************
* This file is provided under the terms of the Eclipse Public License 1.0. 
* Any use, reproduction or distribution of the program
* constitutes recipient's acceptance of this agreement.
*******************************************************************************/
package org.alaskasimulator.test.fitnesse.core;

import org.alaskasimulator.core.buildtime.ActionConfig;
import org.alaskasimulator.core.buildtime.Location;
import org.alaskasimulator.core.buildtime.RouteConfig;
import org.alaskasimulator.core.buildtime.event.Event;
import org.alaskasimulator.core.buildtime.event.NewActionConfigEvent;
import org.eclipse.core.runtime.Assert;

import fit.ColumnFixture;

public class NewActionConfigEventTest extends ColumnFixture
{
	public String name;
	public String actionName;
	public String locationName;
	public double probability;

	public boolean create()
	{
		ActionConfig actionConfig = (ActionConfig) AlaskaFitnesseTestHelper.getObject(actionName);
		Assert.isNotNull(actionConfig, "Unknown action config: " + actionName);

		Location location = (Location) AlaskaFitnesseTestHelper.getObject(locationName);
		Assert.isNotNull(location, "Unknown location: " + locationName);

		Event event = null;
		if (actionConfig instanceof RouteConfig)
		{
			event = new NewActionConfigEvent(name, "", probability, (RouteConfig) actionConfig);
		}
		else
		{
			event = new NewActionConfigEvent(name, "", probability, actionConfig, location);
		}

		AlaskaFitnesseTestHelper.GAME_CONFIG.addEvent(event);
		return true;
	}
}
