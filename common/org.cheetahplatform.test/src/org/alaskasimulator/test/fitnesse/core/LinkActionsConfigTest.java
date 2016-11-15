/*******************************************************************************
* This file is provided under the terms of the Eclipse Public License 1.0. 
* Any use, reproduction or distribution of the program
* constitutes recipient's acceptance of this agreement.
*******************************************************************************/
package org.alaskasimulator.test.fitnesse.core;

import org.alaskasimulator.core.buildtime.ActionConfig;
import org.alaskasimulator.core.buildtime.Location;
import org.eclipse.core.runtime.Assert;

import fit.ColumnFixture;

public class LinkActionsConfigTest extends ColumnFixture
{
	public String actionName;
	public String locationName;

	public boolean linkAction()
	{
		Location location = (Location) AlaskaFitnesseTestHelper.getObject(locationName);
		Assert.isNotNull(location, "Unknown location: " + locationName);

		ActionConfig action = (ActionConfig) AlaskaFitnesseTestHelper.getObject(actionName);
		Assert.isNotNull(action, "Unknown action: " + actionName);

		location.addActionConfig(action);
		return true;
	}

	public int countActions()
	{
		Location location = (Location) AlaskaFitnesseTestHelper.getObject(locationName);
		Assert.isNotNull(location, "Unknown location: " + locationName);

		return location.getActionConfigs().size();
	}
}
