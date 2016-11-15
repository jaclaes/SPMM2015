/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.alaskasimulator.test.core;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.alaskasimulator.core.buildtime.distribution.EqualDistribution;
import org.alaskasimulator.core.buildtime.distribution.IDistribution;
import org.cheetahplatform.test.TestHelper;
import org.junit.Test;


public class EqualDistributionTest {
	@Test
	public void expectedValue() {
		EqualDistribution distribution = new EqualDistribution(0.25, 0.75);
		double result = distribution.getExpectedValue();

		assertEquals("Wrong expected value.", 0.5, result,0.001);
	}

	@Test
	public void density() {
		EqualDistribution distribution = new EqualDistribution(0.2, 0.8);

		Map<Double, Double> xToDensity = new HashMap<Double, Double>();
		xToDensity.put(0.0, 0.0);
		xToDensity.put(0.199, 0.0);
		xToDensity.put(0.2, 1 / 0.6);
		xToDensity.put(0.8, 1 / 0.6);
		xToDensity.put(0.81, 0.0);
		xToDensity.put(1.0, 0.0);

		for (Map.Entry<Double, Double> entry : xToDensity.entrySet()) {
			double result = distribution.getDensityAt(entry.getKey());
			assertEquals("Wrong density.", entry.getValue(), result, 0.0001);
		}
	}

	@Test
	public void variance() {
		EqualDistribution distribution = new EqualDistribution(0.0, 1.0);
		double result = distribution.getVariance();

		assertEquals("Wrong variance.", (double) 1 / 12, result,0.001);
	}

	@Test
	public void percentileBounds() {
		Random r = new Random();
		for (int i = 0; i < 100; i++) {
			double a = r.nextDouble();
			double b = a + (1 - a) * r.nextDouble();
			IDistribution distribution = new EqualDistribution(a, b);
			assertEquals("Lower bound and percentile should match", distribution.getLowerBound(), distribution.getPercentile(0),0.001);
			assertEquals("Upper bound and percentile should match", distribution.getUpperBound(), distribution.getPercentile(1),0.001);
		}
	}

	@Test
	public void percentile() throws Exception {
		IDistribution distribution = new EqualDistribution(0.25, 0.75);

		TestHelper.approximateEqual(0.375, distribution.getPercentile(0.25));
		TestHelper.approximateEqual(0.5, distribution.getPercentile(0.5));
		TestHelper.approximateEqual(0.625, distribution.getPercentile(0.75));

		distribution = new EqualDistribution(0.5, 0.5);
		TestHelper.approximateEqual(0.5, distribution.getPercentile(0.25));
		TestHelper.approximateEqual(0.5, distribution.getPercentile(0.5));
		TestHelper.approximateEqual(0.5, distribution.getPercentile(0.75));
	}

}
