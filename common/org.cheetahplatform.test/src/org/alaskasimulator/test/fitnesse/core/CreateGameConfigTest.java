/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.alaskasimulator.test.fitnesse.core;

import org.alaskasimulator.core.buildtime.GameConfig;

import fit.ColumnFixture;

public class CreateGameConfigTest extends ColumnFixture {
	public String name;
	public int bookingDuration;
	public int journeyDuration;
	public int dayDuration;
	public int minimumSleepAmount;
	public String sleepPenaltyMode;

	private static final String MINIMUM_SLEEP = "MINIMUM_SLEEP";
	private static final String BUSINESS_VALUE_CONSTRAINT = "BUSINESS_VALUE_CONSTRAINT";

	public boolean create() {
		AlaskaFitnesseTestHelper.reset();

		try {
			AlaskaFitnesseTestHelper.GAME_CONFIG = new GameConfig(0, name, bookingDuration, journeyDuration, dayDuration);
			if (sleepPenaltyMode.equals(BUSINESS_VALUE_CONSTRAINT)) {
				AlaskaFitnesseTestHelper.GAME_CONFIG.setBusinessValueConstraintSleepPenalty();
			} else if (sleepPenaltyMode.equals(MINIMUM_SLEEP)) {
				AlaskaFitnesseTestHelper.GAME_CONFIG.setMinimumSleepSleepPenalty(minimumSleepAmount);
			} else
				throw new IllegalArgumentException("Unknown sleepPenaltyMode: " + sleepPenaltyMode);

			return true;
		} catch (RuntimeException e) {
			return false;
		}
	}
}
