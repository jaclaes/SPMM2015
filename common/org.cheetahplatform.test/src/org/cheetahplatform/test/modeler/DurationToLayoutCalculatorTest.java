/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.modeler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.Map;

import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;
import org.cheetahplatform.modeler.graph.export.DurationToLayoutCalculator;
import org.eclipse.core.runtime.AssertionFailedException;
import org.junit.Before;
import org.junit.Test;

public class DurationToLayoutCalculatorTest extends LocaleSensitiveTest {

	private ProcessInstance instance;

	@Test
	public void averageDuration() {
		createEntry(AbstractGraphCommand.CREATE_NODE, 10, 1500);
		createEntry(AbstractGraphCommand.CREATE_NODE, 12, 1800);
		createEntry(AbstractGraphCommand.MOVE_NODE, 10, 2500);
		createEntry(AbstractGraphCommand.MOVE_NODE, 12, 3800);

		DurationToLayoutCalculator calculator = new DurationToLayoutCalculator(instance);
		double averageDurationToLayout = calculator.getAverageDurationToLayout();

		assertEquals(1500, averageDurationToLayout, 0.00001);
	}

	public AuditTrailEntry createEntry(String type, int id, long time) {
		AuditTrailEntry entry = new AuditTrailEntry(type);
		entry.setAttribute(new Attribute(AbstractGraphCommand.ID, id));
		entry.setTimestamp(new Date(time));
		instance.addEntry(entry);
		return entry;
	}

	@Test
	public void multipleLayoutEvents() {
		AuditTrailEntry createEntry = createEntry(AbstractGraphCommand.CREATE_NODE, 10, 1000);
		AuditTrailEntry createEntry2 = createEntry(AbstractGraphCommand.CREATE_NODE, 12, 1250);
		AuditTrailEntry layoutEvent = createEntry(AbstractGraphCommand.MOVE_NODE, 10, 1500);
		AuditTrailEntry layoutEvent2 = createEntry(AbstractGraphCommand.MOVE_NODE, 12, 2800);

		DurationToLayoutCalculator calculator = new DurationToLayoutCalculator(instance);
		Map<AuditTrailEntry, AuditTrailEntry> layoutEventsWithCreateEvents = calculator.getLayoutEventsWithCreateEvents();
		assertEquals(2, layoutEventsWithCreateEvents.size());
		assertTrue(layoutEventsWithCreateEvents.containsKey(layoutEvent));
		assertTrue(layoutEventsWithCreateEvents.containsKey(layoutEvent2));
		assertSame(createEntry, layoutEventsWithCreateEvents.get(layoutEvent));
		assertSame(createEntry2, layoutEventsWithCreateEvents.get(layoutEvent2));
	}

	@Test
	public void multipleLayoutEventsForSameNode() {
		AuditTrailEntry createEntry = createEntry(AbstractGraphCommand.CREATE_NODE, 10, 1000);
		AuditTrailEntry layoutEvent = createEntry(AbstractGraphCommand.MOVE_NODE, 10, 1500);
		AuditTrailEntry layoutEvent2 = createEntry(AbstractGraphCommand.MOVE_NODE, 10, 2500);

		DurationToLayoutCalculator calculator = new DurationToLayoutCalculator(instance);
		Map<AuditTrailEntry, AuditTrailEntry> layoutEventsWithCreateEvents = calculator.getLayoutEventsWithCreateEvents();
		assertEquals(2, layoutEventsWithCreateEvents.size());
		assertTrue(layoutEventsWithCreateEvents.containsKey(layoutEvent));
		assertTrue(layoutEventsWithCreateEvents.containsKey(layoutEvent2));
		assertSame(createEntry, layoutEventsWithCreateEvents.get(layoutEvent));
		assertSame(createEntry, layoutEventsWithCreateEvents.get(layoutEvent2));
	}

	@Test(expected = AssertionFailedException.class)
	public void nullProcessInstance() {
		new DurationToLayoutCalculator(null);
	}

	@Before
	public void setUp() {
		instance = new ProcessInstance("Instance");
	}

	@Test
	public void simpleCalculation() {
		AuditTrailEntry createEntry = createEntry(AbstractGraphCommand.CREATE_NODE, 10, 1000);
		AuditTrailEntry layoutEvent = createEntry(AbstractGraphCommand.MOVE_NODE, 10, 1500);

		DurationToLayoutCalculator calculator = new DurationToLayoutCalculator(instance);
		Map<AuditTrailEntry, AuditTrailEntry> layoutEventsWithCreateEvents = calculator.getLayoutEventsWithCreateEvents();
		assertEquals(1, layoutEventsWithCreateEvents.size());
		assertTrue(layoutEventsWithCreateEvents.containsKey(layoutEvent));
		assertSame(createEntry, layoutEventsWithCreateEvents.get(layoutEvent));
	}

	@Test
	public void simpleLayoutDurations() {
		createEntry(AbstractGraphCommand.CREATE_NODE, 10, 1000);
		createEntry(AbstractGraphCommand.CREATE_NODE, 15, 1400);
		AuditTrailEntry layoutEvent = createEntry(AbstractGraphCommand.DELETE_EDGE_BENDPOINT, 10, 1500);
		AuditTrailEntry layoutEvent2 = createEntry(AbstractGraphCommand.CREATE_EDGE_BENDPOINT, 15, 2800);

		DurationToLayoutCalculator calculator = new DurationToLayoutCalculator(instance);
		Map<AuditTrailEntry, Long> layoutDurations = calculator.getLayoutDurations();
		assertEquals(2, layoutDurations.size());
		assertEquals(new Long(500), layoutDurations.get(layoutEvent));
		assertEquals(new Long(1400), layoutDurations.get(layoutEvent2));
	}

	@Test
	public void simpleLayoutDurationsForSameNode() {
		createEntry(AbstractGraphCommand.CREATE_EDGE, 10, 1000);
		AuditTrailEntry layoutEvent = createEntry(AbstractGraphCommand.MOVE_EDGE_BENDPOINT, 10, 1500);
		AuditTrailEntry layoutEvent2 = createEntry(AbstractGraphCommand.MOVE_EDGE_LABEL, 10, 2800);

		DurationToLayoutCalculator calculator = new DurationToLayoutCalculator(instance);
		Map<AuditTrailEntry, Long> layoutDurations = calculator.getLayoutDurations();
		assertEquals(2, layoutDurations.size());
		assertEquals(new Long(500), layoutDurations.get(layoutEvent));
		assertEquals(new Long(1800), layoutDurations.get(layoutEvent2));
	}
}
