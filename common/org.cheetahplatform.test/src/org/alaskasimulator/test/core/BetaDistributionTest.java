/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.alaskasimulator.test.core;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.alaskasimulator.core.buildtime.distribution.BetaDistribution;
import org.cheetahplatform.test.TestHelper;
import org.junit.Test;

public class BetaDistributionTest {
	@Test
	public void density() {
		BetaDistribution distribution = new BetaDistribution(12, 12);

		double result = distribution.getDensityAt(0.2);
		assertEquals("Wrong density.", 0.02854320927, result, 0.001);
		result = distribution.getDensityAt(0.5);
		assertEquals("Wrong density.", 3.868326187, result, 0.001);
		result = distribution.getDensityAt(1);
		assertEquals("Wrong density.", 0, result, 0.001);
	}

	@Test
	public void expectedValue() {
		BetaDistribution distribution = new BetaDistribution(5, 20);
		double result = distribution.getExpectedValue();

		assertEquals("Wrong expected value.", 0.2, result, 0.001);
	}

	@Test
	public void percentile() throws Exception {
		BetaDistribution distribution = new BetaDistribution(2, 2);
		TestHelper.approximateEqual(0.33, distribution.getPercentile(0.25));
		TestHelper.approximateEqual(0.5, distribution.getPercentile(0.50));
		TestHelper.approximateEqual(0.68, distribution.getPercentile(0.75));
	}

	@Test
	public void percentileBounds() {
		Random r = new Random();
		for (int i = 0; i < 100; i++) {
			double a = 1 + r.nextDouble() * 10;
			double b = 1 + r.nextDouble() * 10;
			BetaDistribution distribution = new BetaDistribution(a, b);
			assertEquals("Lower bound and percentile should match", distribution.getLowerBound(), distribution.getPercentile(0), 0.001);
			assertEquals("Upper bound and percentile should match", distribution.getUpperBound(), distribution.getPercentile(1), 0.001);
		}
	}

	@Test
	public void variance() {
		BetaDistribution distribution = new BetaDistribution(2, 6);
		double result = distribution.getVariance();

		assertEquals("Wrong variance.", (double) 1 / 48, result, 0.001);
	}

}
