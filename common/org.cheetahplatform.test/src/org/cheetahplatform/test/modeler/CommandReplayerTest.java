/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.modeler;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.common.logging.PromLogger;
import org.cheetahplatform.modeler.generic.GraphCommandStack;
import org.cheetahplatform.modeler.graph.CommandReplayer;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;
import org.cheetahplatform.modeler.graph.command.CreateNodeCommand;
import org.cheetahplatform.modeler.graph.descriptor.IGraphElementDescriptor;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.CompoundCommand;
import org.junit.Test;

public class CommandReplayerTest {
	@Test
	public void replayCompoundCommand() throws Exception {
		List<IGraphElementDescriptor> descriptors = new ArrayList<IGraphElementDescriptor>();
		DummyNodeDescriptor descriptor = new DummyNodeDescriptor("node", "id", true);
		descriptors.add(descriptor);
		Graph graph = new Graph(descriptors);

		GraphCommandStack stack = new GraphCommandStack();
		stack.setGraph(graph);
		CompoundCommand command = new CompoundCommand();

		TestLogListener listener = new TestLogListener();
		graph.addLogListener(listener);
		command.add(new CreateNodeCommand(graph, descriptor.createModel(graph), new Point()));
		command.add(new CreateNodeCommand(graph, descriptor.createModel(graph), new Point()));
		stack.execute(command);

		List<AuditTrailEntry> entries = listener.getEntries();
		assertEquals(4, entries.size());
		assertEquals(2, graph.getNodes().size());

		ProcessInstance instance = new ProcessInstance();
		Graph replayedGraph = new Graph(descriptors);
		TestLogListener replayListener = new TestLogListener();
		replayedGraph.addLogListener(replayListener);
		instance.addEntries(listener.getEntries());
		CommandReplayer replayer = new CommandReplayer(new GraphCommandStack(replayedGraph), replayedGraph, instance);
		assertEquals(0, replayedGraph.getNodes().size());
		replayer.executeNextCommand();
		assertEquals("Should have executed the compound command.", 2, replayedGraph.getNodes().size());

		List<AuditTrailEntry> replayedEntries = replayListener.getEntries();
		assertEquals(4, replayedEntries.size());

		AuditTrailEntry entry = replayedEntries.get(0);
		assertEquals(PromLogger.GROUP_EVENT_START, entry.getEventType());

		entry = replayedEntries.get(1);
		assertEquals(AbstractGraphCommand.CREATE_NODE, entry.getEventType());

		entry = replayedEntries.get(2);
		assertEquals(AbstractGraphCommand.CREATE_NODE, entry.getEventType());

		entry = replayedEntries.get(3);
		assertEquals(PromLogger.GROUP_EVENT_END, entry.getEventType());

	}
}
