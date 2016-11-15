/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.modeler;

import static org.cheetahplatform.modeler.graph.command.AbstractGraphCommand.CREATE_NODE;
import static org.cheetahplatform.modeler.graph.command.AbstractGraphCommand.DELETE_NODE;
import static org.cheetahplatform.test.modeler.AbstractGraphCommandTest.NODE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.cheetahplatform.modeler.graph.command.DeleteNodeCommand;
import org.cheetahplatform.modeler.graph.descriptor.IGraphElementDescriptor;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;
import org.junit.Test;

public class DeleteNodeCommandTest {
	@Test
	public void undo() throws Exception {
		Graph graph = new Graph(new ArrayList<IGraphElementDescriptor>());
		TestLogListener listener = new TestLogListener();
		graph.addLogListener(listener);
		Node node = NODE.createModel(graph);
		graph.addNode(node);
		DeleteNodeCommand command = new DeleteNodeCommand(node);
		command.execute();
		assertEquals(0, graph.getNodes().size());
		assertFalse(graph.getNodes().contains(node));
		assertEquals(1, listener.getEntries().size());
		assertEquals(DELETE_NODE, listener.getEntries().get(0).getEventType());

		command.undo();
		assertEquals(1, graph.getNodes().size());
		assertTrue(graph.getNodes().contains(node));
		assertEquals(2, listener.getEntries().size());
		assertEquals(CREATE_NODE, listener.getEntries().get(1).getEventType());
	}

	@Test
	public void undoBug() throws Exception {
		Graph graph = new Graph(new ArrayList<IGraphElementDescriptor>());
		TestLogListener listener = new TestLogListener();
		graph.addLogListener(listener);
		Node node1 = NODE.createModel(graph);
		Node node2 = NODE.createModel(graph);
		Edge edge = AbstractGraphCommandTest.EDGE.createModel(graph);
		edge.setSource(node1);
		edge.setTarget(node2);
		graph.addNode(node1);
		graph.addEdge(edge);

		DeleteNodeCommand command = new DeleteNodeCommand(node1);
		command.execute();

		assertEquals(0, graph.getNodes().size());
		assertNull(edge.getSource());
		assertFalse(graph.getNodes().contains(node1));
		assertEquals(1, listener.getEntries().size());
		assertEquals(DELETE_NODE, listener.getEntries().get(0).getEventType());

		command.undo();
		assertEquals(1, graph.getNodes().size());
		assertTrue(graph.getNodes().contains(node1));
		assertEquals(2, listener.getEntries().size());
		assertEquals(node1, edge.getSource());
		assertEquals(CREATE_NODE, listener.getEntries().get(1).getEventType());
	}
}
