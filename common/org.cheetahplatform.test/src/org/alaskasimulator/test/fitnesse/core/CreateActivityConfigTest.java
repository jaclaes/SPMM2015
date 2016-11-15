/*******************************************************************************
* This file is provided under the terms of the Eclipse Public License 1.0. 
* Any use, reproduction or distribution of the program
* constitutes recipient's acceptance of this agreement.
*******************************************************************************/
package org.alaskasimulator.test.fitnesse.core;

import org.alaskasimulator.core.buildtime.ActivityConfig;
import org.alaskasimulator.core.buildtime.Certainty;
import org.alaskasimulator.core.buildtime.DurationRange;
import org.alaskasimulator.core.buildtime.distribution.EqualDistribution;

import fit.ColumnFixture;

public class CreateActivityConfigTest extends ColumnFixture
{
	public String name;
	public double businessValue;
	public double cost;
	public int bookingDeadline;
	public double certainty;
	public double availability;
	public int durationMin;
	public int durationMax;

	public boolean create()
	{
		try
		{
			DurationRange durationRange = new DurationRange(durationMin, durationMax);
			Certainty certainty1 = new Certainty(new EqualDistribution(certainty, 1.0));
			ActivityConfig activityConfig = new ActivityConfig(name, cost, businessValue, certainty1, availability,
					durationRange, bookingDeadline);
			AlaskaFitnesseTestHelper.setObject(name, activityConfig);
			return true;
		}
		catch (RuntimeException e)
		{
			return false;
		}
	}
}
