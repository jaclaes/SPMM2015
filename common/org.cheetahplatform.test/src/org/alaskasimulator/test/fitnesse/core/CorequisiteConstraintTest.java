/*******************************************************************************
* This file is provided under the terms of the Eclipse Public License 1.0. 
* Any use, reproduction or distribution of the program
* constitutes recipient's acceptance of this agreement.
*******************************************************************************/
package org.alaskasimulator.test.fitnesse.core;

import org.alaskasimulator.core.buildtime.ActionConfig;
import org.alaskasimulator.core.buildtime.constraint.CoExistenceConstraint;
import org.eclipse.core.runtime.Assert;

import fit.ColumnFixture;

public class CorequisiteConstraintTest extends ColumnFixture
{
	public String actionNameA;
	public String actionNameB;

	public boolean createConstraint()
	{
		ActionConfig actionConfigA = (ActionConfig) AlaskaFitnesseTestHelper.getObject(actionNameA);
		Assert.isNotNull(actionConfigA, "Unknown action config: " + actionNameA);
		ActionConfig actionConfigB = (ActionConfig) AlaskaFitnesseTestHelper.getObject(actionNameB);
		Assert.isNotNull(actionConfigB, "Unknown action config: " + actionNameB);

		CoExistenceConstraint constraint = new CoExistenceConstraint(actionConfigA, actionConfigB);

		AlaskaFitnesseTestHelper.GAME_CONFIG.addConstraint(constraint);
		return true;
	}

}
