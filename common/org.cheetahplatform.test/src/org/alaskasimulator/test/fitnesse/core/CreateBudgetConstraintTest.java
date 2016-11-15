/*******************************************************************************
* This file is provided under the terms of the Eclipse Public License 1.0. 
* Any use, reproduction or distribution of the program
* constitutes recipient's acceptance of this agreement.
*******************************************************************************/
package org.alaskasimulator.test.fitnesse.core;

import org.alaskasimulator.core.buildtime.constraint.BudgetConstraint;

import fit.ColumnFixture;

public class CreateBudgetConstraintTest extends ColumnFixture
{
	public double budget;

	public boolean createConstraint()
	{
		BudgetConstraint constraint = new BudgetConstraint(budget);
		AlaskaFitnesseTestHelper.GAME_CONFIG.addConstraint(constraint);

		return true;
	}
}
