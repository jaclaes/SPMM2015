/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.core;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.cheetahplatform.common.AssertionFailedException;
import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.common.date.TimeSlot;
import org.junit.Test;

public class TimeSlotTest {
	@Test
	public void isNonParallel() throws Exception {
		TimeSlot timeSlot1 = new TimeSlot(new DateTime(0, 0, true), new DateTime(0, 1, false));
		TimeSlot timeSlot2 = new TimeSlot(new DateTime(0, 1, true), new DateTime(0, 2, false));
		assertFalse(timeSlot1.isParallelTo(timeSlot2));

		timeSlot2 = new TimeSlot(new DateTime(0, 130, true), new DateTime(0, 230, false));
		assertFalse(timeSlot1.isParallelTo(timeSlot2));
	}

	@Test
	public void isParallelEqual() throws Exception {
		TimeSlot timeSlot1 = new TimeSlot(new DateTime(0, 0, true), new DateTime(0, 10, false));
		assertTrue(timeSlot1.isParallelTo(timeSlot1));
	}

	@Test
	public void isParallelOverlapping() throws Exception {
		TimeSlot timeSlot1 = new TimeSlot(new DateTime(0, 0, true), new DateTime(0, 11, false));
		TimeSlot timeSlot2 = new TimeSlot(new DateTime(0, 10, true), new DateTime(0, 20, false));
		assertTrue(timeSlot1.isParallelTo(timeSlot2));
		assertTrue(timeSlot2.isParallelTo(timeSlot1));
	}

	@Test
	public void toStringRepresentation() throws Exception {
		TimeSlot slot = new TimeSlot(new DateTime(new Date(0), true), new DateTime(new Date(0), false));
		String stringRepresentation = slot.toStringRepresentation();
		assertEquals("0_inclusive-0_exclusive", stringRepresentation);

		TimeSlot timeSlotFromString = new TimeSlot(stringRepresentation);
		assertEquals(slot, timeSlotFromString);
	}

	@Test(expected = AssertionFailedException.class)
	public void validateEndBeforeStart() throws Exception {
		new TimeSlot(new DateTime(0, 0, true), new DateTime(0, 0, false));
	}

	@Test(expected = AssertionFailedException.class)
	public void validateEndExclusive() throws Exception {
		new TimeSlot(new DateTime(0, 0, true), new DateTime(1, 0, true));
	}

	@Test(expected = AssertionFailedException.class)
	public void validateFail1() throws Exception {
		new TimeSlot(new DateTime(0, 0, true), new DateTime(0, 0, false));
	}

	@Test(expected = AssertionFailedException.class)
	public void validateFail2() throws Exception {
		new TimeSlot(new DateTime(1, 0, true), new DateTime(0, 0, false));
	}

	@Test(expected = AssertionFailedException.class)
	public void validateStartInclusive() throws Exception {
		new TimeSlot(new DateTime(0, 0, false), new DateTime(1, 0, false));
	}
}
