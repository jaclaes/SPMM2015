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

import org.alaskasimulator.core.buildtime.distribution.IDistribution;
import org.alaskasimulator.core.buildtime.distribution.TriangleDistribution;
import org.cheetahplatform.test.TestHelper;
import org.junit.Test;


public class TriangleDistributionTest {
	@Test
	public void expected() {
		TriangleDistribution distribution = new TriangleDistribution(0.5, 0.8,
				0.6);
		double result = distribution.getExpectedValue();

		assertEquals("Wrong expected value.", 1.9 / 3, result, 0.001);
	}

	@Test
	public void density() {
		TriangleDistribution distribution = new TriangleDistribution(0.2, 0.9,
				0.3);
		Map<Double, Double> xToDensity = new HashMap<Double, Double>();
		xToDensity.put(0.199, 0.0);
		xToDensity.put(0.2, 0.0);
		xToDensity.put(0.25, 1.428571428);
		xToDensity.put(0.3, 2.857142857);
		xToDensity.put(0.31, 2.809523810);
		xToDensity.put(0.7, 0.952380953);
		xToDensity.put(0.9, 0.0);
		xToDensity.put(0.91, 0.0);

		for (Map.Entry<Double, Double> entry : xToDensity.entrySet()) {
			double result = distribution.getDensityAt(entry.getKey());
			assertEquals("Wrong density.", entry.getValue(), result, 0.0001);
		}
	}

	@Test
	public void variance() {
		TriangleDistribution distribution = new TriangleDistribution(0.4, 0.6,
				0.5);
		double result = distribution.getVariance();
		assertEquals("Wrong variance.", 0.001666666, result, 0.001);
	}

	@Test
	public void percentileBounds() {
		Random r = new Random();
		for (int i = 0; i < 100; i++) {
			double a = r.nextDouble();
			double b = a + (1 - a) * r.nextDouble();
			double c = b + (1 - b) * r.nextDouble();
			IDistribution distribution = new TriangleDistribution(a, c, b);
			assertEquals("Lower bound and percentile should match",
					distribution.getLowerBound(),
					distribution.getPercentile(0), 0.001);
			assertEquals("Upper bound and percentile should match",
					distribution.getUpperBound(),
					distribution.getPercentile(1), 0.001);
		}
	}

	@Test
	public void percentile() throws Exception {
		TriangleDistribution distribution = new TriangleDistribution(0.25,
				0.75, 0.5);

		TestHelper.approximateEqual(0.426, distribution.getPercentile(0.25));
		TestHelper.approximateEqual(0.5, distribution.getPercentile(0.5));
		TestHelper.approximateEqual(0.573, distribution.getPercentile(0.75));
	}
}
