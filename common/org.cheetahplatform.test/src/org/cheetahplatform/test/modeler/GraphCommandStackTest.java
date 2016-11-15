/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.modeler;

import static org.cheetahplatform.test.modeler.TestLogCommand.TEST;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.PromLogger;
import org.cheetahplatform.modeler.generic.GraphCommandStack;
import org.cheetahplatform.modeler.graph.descriptor.IGraphElementDescriptor;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.eclipse.gef.commands.CompoundCommand;
import org.junit.Test;

public class GraphCommandStackTest {

	@Test
	public void executeCompoundEvent() throws Exception {
		Graph graph = new Graph(new ArrayList<IGraphElementDescriptor>());
		TestLogListener listener = new TestLogListener();
		graph.addLogListener(listener);
		GraphCommandStack stack = new GraphCommandStack(graph);
		CompoundCommand command = new CompoundCommand();
		command.add(new TestLogCommand(graph));
		stack.execute(command);
		List<AuditTrailEntry> entries = listener.getEntries();
		assertEquals("Should have only one event as grouping makes sense only for more than 2 events", 1, entries.size());

		AuditTrailEntry entry = entries.get(0);
		assertEquals(TEST, entry.getEventType());
	}

	@Test
	public void executeCompoundEventWithMultipleEvents() throws Exception {
		Graph graph = new Graph(new ArrayList<IGraphElementDescriptor>());
		TestLogListener listener = new TestLogListener();
		graph.addLogListener(listener);
		GraphCommandStack stack = new GraphCommandStack(graph);
		CompoundCommand command = new CompoundCommand();
		command.add(new TestLogCommand(graph));
		command.add(new TestLogCommand(graph, "second event"));
		stack.execute(command);
		List<AuditTrailEntry> entries = listener.getEntries();
		assertEquals(4, entries.size());

		AuditTrailEntry entry = entries.get(0);
		assertEquals(PromLogger.GROUP_EVENT_START, entry.getEventType());

		entry = entries.get(1);
		assertEquals(TEST, entry.getEventType());

		entry = entries.get(2);
		assertEquals("second event", entry.getEventType());

		entry = entries.get(3);
		assertEquals(PromLogger.GROUP_EVENT_END, entry.getEventType());
	}

	@Test
	public void executeEvent() throws Exception {
		Graph graph = new Graph(new ArrayList<IGraphElementDescriptor>());
		TestLogListener listener = new TestLogListener();
		graph.addLogListener(listener);
		GraphCommandStack stack = new GraphCommandStack(graph);
		stack.execute(new TestLogCommand(graph));
		List<AuditTrailEntry> entries = listener.getEntries();
		assertEquals(1, entries.size());
		AuditTrailEntry entry = entries.get(0);
		assertEquals(TEST, entry.getEventType());
	}
}
