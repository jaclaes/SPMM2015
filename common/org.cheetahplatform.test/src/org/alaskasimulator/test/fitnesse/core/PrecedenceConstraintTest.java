/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.alaskasimulator.test.fitnesse.core;

import org.alaskasimulator.core.buildtime.ActionConfig;
import org.alaskasimulator.core.buildtime.constraint.PrecedenceConstraint;
import org.eclipse.core.runtime.Assert;

import fit.ColumnFixture;

public class PrecedenceConstraintTest extends ColumnFixture {
	public String prerequisite;
	public String action;

	public boolean createConstraint() {
		ActionConfig actionConfigA = (ActionConfig) AlaskaFitnesseTestHelper.getObject(prerequisite);
		Assert.isNotNull(actionConfigA, "Unknown action config: " + prerequisite);
		ActionConfig actionConfigB = (ActionConfig) AlaskaFitnesseTestHelper.getObject(action);
		Assert.isNotNull(actionConfigB, "Unknown action config: " + action);

		PrecedenceConstraint constraint = new PrecedenceConstraint(actionConfigA, actionConfigB);

		AlaskaFitnesseTestHelper.GAME_CONFIG.addConstraint(constraint);
		return true;
	}
}
