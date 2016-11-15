/*******************************************************************************
* This file is provided under the terms of the Eclipse Public License 1.0. 
* Any use, reproduction or distribution of the program
* constitutes recipient's acceptance of this agreement.
*******************************************************************************/
package org.alaskasimulator.test.fitnesse.core;

import org.alaskasimulator.core.buildtime.ActionConfig;
import org.alaskasimulator.core.buildtime.constraint.MutualExclusionConstraint;
import org.eclipse.core.runtime.Assert;

import fit.ColumnFixture;

public class MutualExclusionTest extends ColumnFixture
{
	public String actionName1;
	public String actionName2;

	public boolean createConstraint()
	{
		ActionConfig config1 = (ActionConfig) AlaskaFitnesseTestHelper.getObject(actionName1);
		Assert.isNotNull(config1, "Unknown action config: " + actionName1);

		ActionConfig config2 = (ActionConfig) AlaskaFitnesseTestHelper.getObject(actionName2);
		Assert.isNotNull(config2, "Unknown action config: " + actionName2);

		MutualExclusionConstraint constraint = new MutualExclusionConstraint(config1, config2);
		AlaskaFitnesseTestHelper.GAME_CONFIG.addConstraint(constraint);

		return true;
	}
}
