/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.alaskasimulator.test.fitnesse.core;

import org.alaskasimulator.core.buildtime.ActionConfig;
import org.alaskasimulator.core.buildtime.constraint.MinConstraint;
import org.eclipse.core.runtime.Assert;

import fit.ColumnFixture;

public class MinConstraintTest extends ColumnFixture {
	public String actionName;
	public int min;

	public boolean createConstraint() {
		ActionConfig actionConfig = (ActionConfig) AlaskaFitnesseTestHelper.getObject(actionName);
		Assert.isNotNull(actionConfig, "Unknown action config: " + actionName);

		MinConstraint constraint = new MinConstraint(actionConfig, min);
		AlaskaFitnesseTestHelper.GAME_CONFIG.addConstraint(constraint);

		return true;
	}
}
