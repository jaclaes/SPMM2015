/*******************************************************************************
* This file is provided under the terms of the Eclipse Public License 1.0. 
* Any use, reproduction or distribution of the program
* constitutes recipient's acceptance of this agreement.
*******************************************************************************/
package org.alaskasimulator.test.fitnesse.core;

import org.alaskasimulator.core.buildtime.AccommodationConfig;
import org.alaskasimulator.core.buildtime.Certainty;
import org.alaskasimulator.core.buildtime.distribution.EqualDistribution;

import fit.ColumnFixture;

public class CreateAccommodationConfigTest extends ColumnFixture
{
	public String name;
	public double businessValue;
	public double cost;
	public double certainty;
	public double availability;
	public int bookingDeadline;
	public String stornoInfo;

	public boolean create()
	{

		try
		{
			Certainty certainty1 = new Certainty(new EqualDistribution(certainty, 1.0));
			AccommodationConfig accommodationConfig = new AccommodationConfig(name, cost, businessValue, certainty1,
					availability, bookingDeadline);

			AlaskaFitnesseTestHelper.setObject(accommodationConfig.getName(), accommodationConfig);
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}
}
