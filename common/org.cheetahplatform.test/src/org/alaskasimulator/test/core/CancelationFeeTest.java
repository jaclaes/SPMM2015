/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.alaskasimulator.test.core;

import static org.junit.Assert.assertEquals;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.alaskasimulator.core.buildtime.CancelationFee;
import org.alaskasimulator.core.buildtime.Fee;
import org.eclipse.core.runtime.AssertionFailedException;
import org.junit.Test;

public class CancelationFeeTest {
	@Test
	public void add() {
		CancelationFee cancelFee = createFee();

		List<Fee> fees = cancelFee.getFees();
		assertEquals("Wrong size.", 4, fees.size());

		Map<Integer, Double> expected = new LinkedHashMap<Integer, Double>();
		expected.put(1, 1.0);
		expected.put(4, 0.95);
		expected.put(10, 0.8);
		expected.put(20, 0.3);

		int index = 0;
		for (Entry<Integer, Double> entry : expected.entrySet()) {
			Fee fee = fees.get(index);

			assertEquals((int) entry.getKey(), fee.getDayBefore());
			assertEquals(entry.getValue(), fee.getCancelFeePercent(), 0.0001);

			index++;
		}
	}

	private CancelationFee createFee() {
		CancelationFee cancelFee = new CancelationFee();
		cancelFee.addFee(4, 0.95);
		cancelFee.addFee(20, 0.3);
		cancelFee.addFee(1, 1.0);
		cancelFee.addFee(10, 0.8);
		return cancelFee;
	}

	@Test
	public void getFee() {
		int cost = 100;

		Map<Integer, Double> expected = new LinkedHashMap<Integer, Double>();
		CancelationFee cancelFee = createFee();
		expected.put(0, 100.0);
		expected.put(1, 100.0);
		expected.put(2, 95.0);
		expected.put(4, 95.0);
		expected.put(5, 80.0);
		expected.put(6, 80.0);
		expected.put(9, 80.0);
		expected.put(10, 80.0);
		expected.put(20, 30.0);
		expected.put(21, 0.0);
		expected.put(100, 0.0);

		for (Entry<Integer, Double> entry : expected.entrySet()) {
			double cancelationFee = cancelFee.getCancelationFee(entry.getKey(), cost);
			assertEquals("Wrong refunding.", entry.getValue(), cancelationFee, 0.0001);
		}
	}

	@Test
	public void getFeeSingle() {
		int cost = 100;
		CancelationFee cancelFee = new CancelationFee();
		cancelFee.addFee(10, 0.5);

		assertEquals("Wrong refunding.", 50.0, cancelFee.getCancelationFee(10, cost), 0.0001);
		assertEquals("Wrong refunding.", 50.0, cancelFee.getCancelationFee(9, cost), 0.0001);
		assertEquals("Wrong refunding.", 0.0, cancelFee.getCancelationFee(11, cost), 0.0001);
	}

	@Test
	public void getNoFee() {
		double cost = 100;
		CancelationFee cancelFee = new CancelationFee();

		assertEquals("Should get total cost.", 0, cancelFee.getCancelationFee(0, cost), 0.0001);
	}

	@Test(expected = AssertionFailedException.class)
	public void IllegalFee() {
		CancelationFee cancelFee = new CancelationFee();
		cancelFee.addFee(10, 1.1);
	}
}
