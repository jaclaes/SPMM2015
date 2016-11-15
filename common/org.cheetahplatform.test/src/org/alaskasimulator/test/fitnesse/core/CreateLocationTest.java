/*******************************************************************************
* This file is provided under the terms of the Eclipse Public License 1.0. 
* Any use, reproduction or distribution of the program
* constitutes recipient's acceptance of this agreement.
*******************************************************************************/
package org.alaskasimulator.test.fitnesse.core;

import org.alaskasimulator.core.buildtime.Location;

import fit.ColumnFixture;

public class CreateLocationTest extends ColumnFixture
{
	public String name;

	public boolean create()
	{
		Location location = new Location(AlaskaFitnesseTestHelper.GAME_CONFIG, name);
		AlaskaFitnesseTestHelper.setObject(name, location);
		return true;
	}
}
