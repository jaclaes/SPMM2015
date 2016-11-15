/*******************************************************************************
* This file is provided under the terms of the Eclipse Public License 1.0. 
* Any use, reproduction or distribution of the program
* constitutes recipient's acceptance of this agreement.
*******************************************************************************/
package org.alaskasimulator.test.fitnesse.core;

import java.util.Set;

import org.alaskasimulator.core.buildtime.ActionConfig;
import org.alaskasimulator.core.buildtime.Certainty;
import org.alaskasimulator.core.buildtime.DurationRange;
import org.alaskasimulator.core.buildtime.Location;
import org.alaskasimulator.core.buildtime.RouteConfig;
import org.alaskasimulator.core.buildtime.distribution.EqualDistribution;

import fit.ColumnFixture;

public class CreateRouteConfigTest extends ColumnFixture
{
	public String name;
	public double businessValue;
	public double cost;
	public double certainty;
	public double availability;
	public int minDuration;
	public int maxDuration;
	public String locationNameA;
	public String locationNameB;
	public int bookingDeadline;

	public boolean create() throws Exception
	{
		try
		{
			Location locA = (Location) AlaskaFitnesseTestHelper.getObject(locationNameA);
			Location locB = (Location) AlaskaFitnesseTestHelper.getObject(locationNameB);
			Certainty certainty1 = new Certainty(new EqualDistribution(certainty, 1.0));
			DurationRange durationRange = new DurationRange(minDuration, maxDuration);

			RouteConfig routeConfig = new RouteConfig(name, cost, businessValue, certainty1, availability,
					durationRange, locA, locB, bookingDeadline);
			AlaskaFitnesseTestHelper.setObject(routeConfig.getName(), routeConfig);
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}

	public int countRoutesFromA()
	{
		Set<ActionConfig> actionConfigs = ((Location) AlaskaFitnesseTestHelper.getObject(locationNameA)).getActionConfigs();
		int count = 0;
		for (ActionConfig actionConfig : actionConfigs)
		{
			if (actionConfig instanceof RouteConfig)
				count++;
		}
		return count;
	}
}
