/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.alaskasimulator.test.core;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.alaskasimulator.core.buildtime.distribution.SpecificDistribution;
import org.alaskasimulator.core.buildtime.distribution.SpecificDistribution.SpecificDistributionEntry;
import org.cheetahplatform.test.TestHelper;
import org.junit.Test;


public class SpecificDistributionTest {
	@Test
	public void expected() {
		List<SpecificDistributionEntry> entries = new ArrayList<SpecificDistributionEntry>();
		entries.add(new SpecificDistributionEntry(0.5, 0.5));
		entries.add(new SpecificDistributionEntry(0.5, 0.9));

		SpecificDistribution distribution = new SpecificDistribution(entries);
		double result = distribution.getExpectedValue();
		assertEquals("Wrong expected value.", 0.7, result, 0.0001);
	}

	@Test
	public void density() {
		List<SpecificDistributionEntry> entries = new ArrayList<SpecificDistributionEntry>();
		entries.add(new SpecificDistributionEntry(0.4, 0.5));
		entries.add(new SpecificDistributionEntry(0.6, 0.9));
		SpecificDistribution distribution = new SpecificDistribution(entries);

		Map<Double, Double> xToDensity = new LinkedHashMap<Double, Double>();
		xToDensity.put(0.0, 0.0);
		xToDensity.put(0.499, 0.0);
		xToDensity.put(0.5, 0.4);
		xToDensity.put(0.5001, 0.0);
		xToDensity.put(0.899, 0.0);
		xToDensity.put(0.9, 0.6);
		xToDensity.put(0.9001, 0.0);

		for (Map.Entry<Double, Double> entry : xToDensity.entrySet()) {
			double result = distribution.getDensityAt(entry.getKey());
			assertEquals("Wrong density.", entry.getValue(), result,
					0.000000001);
		}
	}

	@Test
	public void variance() {
		List<SpecificDistributionEntry> entries = new ArrayList<SpecificDistributionEntry>();
		entries.add(new SpecificDistributionEntry(0.5, 0.5));
		entries.add(new SpecificDistributionEntry(0.5, 0.9));
		SpecificDistribution distribution = new SpecificDistribution(entries);

		double result = distribution.getVariance();
		assertEquals("Wrong variance.", 0.04, result, 0.0001);
	}

	@Test
	public void percentileLowerBound() {
		List<SpecificDistributionEntry> entries = new ArrayList<SpecificDistributionEntry>();
		entries.add(new SpecificDistributionEntry(1, 0.5));
		SpecificDistribution distribution = new SpecificDistribution(entries);
		assertEquals("Lower bound and percentile should match", distribution
				.getLowerBound(), distribution.getPercentile(0), 0.0001);
	}

	@Test
	public void percentileUpperBound() {
		List<SpecificDistributionEntry> entries = new ArrayList<SpecificDistributionEntry>();
		entries.add(new SpecificDistributionEntry(1, 1));
		SpecificDistribution distribution = new SpecificDistribution(entries);
		assertEquals("Upper bound and percentile should match", distribution
				.getUpperBound(), distribution.getPercentile(1), 0.0001);
	}

	@Test
	public void percentile() throws Exception {
		List<SpecificDistributionEntry> entries = new ArrayList<SpecificDistributionEntry>();
		entries.add(new SpecificDistributionEntry(0.2, 0.2));
		entries.add(new SpecificDistributionEntry(0.3, 0.5));
		entries.add(new SpecificDistributionEntry(0.5, 0.7));
		SpecificDistribution distribution = new SpecificDistribution(entries);

		TestHelper.approximateEqual(0.5, distribution.getPercentile(0.25));
		TestHelper.approximateEqual(0.5, distribution.getPercentile(0.5));
		TestHelper.approximateEqual(0.7, distribution.getPercentile(0.75));
	}
}
