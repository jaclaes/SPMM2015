/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.alaskasimulator.test.fitnesse.core;

import org.alaskasimulator.core.buildtime.ActionConfig;
import org.alaskasimulator.core.buildtime.distribution.EqualDistribution;
import org.alaskasimulator.core.runtime.proxy.ActivityConfigProxy;

import fit.ColumnFixture;

public class CheckActivityTest extends ColumnFixture {
	public String name;

	public double getBusinessValue() {
		ActivityConfigProxy activity = getActvityProxy();
		return activity.getMaxBusinessValue();
	}

	private ActivityConfigProxy getActvityProxy() {
		return (ActivityConfigProxy) AlaskaFitnesseTestHelper.GAME.getConfig().findActionConfigProxy(
				(ActionConfig) AlaskaFitnesseTestHelper.getObject(name));
	}

	public double getCost() {
		ActivityConfigProxy activity = getActvityProxy();
		return activity.getCost();
	}

	public double getCertainty() {
		ActivityConfigProxy activity = getActvityProxy();
		EqualDistribution distribution = (EqualDistribution) activity.getCertainty().getLocalDistribution();
		return distribution.getLowerBound();
	}

	public double getAvailability() {
		ActivityConfigProxy activity = getActvityProxy();
		return activity.getAvailability();
	}

	public int getDurationMax() {
		ActivityConfigProxy activity = getActvityProxy();
		return activity.getDurationRange().getMaxMinutes();
	}
}
