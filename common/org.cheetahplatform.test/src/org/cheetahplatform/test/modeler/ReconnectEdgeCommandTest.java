/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.modeler;

import static org.cheetahplatform.modeler.graph.command.AbstractGraphCommand.RECONNECT_EDGE;
import static org.cheetahplatform.test.modeler.AbstractGraphCommandTest.EDGE;
import static org.cheetahplatform.test.modeler.AbstractGraphCommandTest.NODE;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.cheetahplatform.modeler.graph.descriptor.IGraphElementDescriptor;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.gef.commands.Command;
import org.junit.Test;

public class ReconnectEdgeCommandTest {
	@Test
	public void undo() throws Exception {
		Graph graph = new Graph(new ArrayList<IGraphElementDescriptor>());
		TestLogListener listener = new TestLogListener();
		graph.addLogListener(listener);
		Node node1 = NODE.createModel(graph);
		Node node2 = NODE.createModel(graph);
		Node node3 = NODE.createModel(graph);
		Edge edge = EDGE.createModel(graph);
		edge.setSource(node1);
		edge.setTarget(node2);
		graph.addEdge(edge);
		Command command = edge.getDescriptor().createReconnectEdgeCommand(edge, node1, node3);
		command.execute();

		assertEquals(node1, edge.getSource());
		assertEquals(node3, edge.getTarget());
		assertEquals(1, listener.getEntries().size());
		assertEquals(RECONNECT_EDGE, listener.getEntries().get(0).getEventType());

		command.undo();
		assertEquals(node1, edge.getSource());
		assertEquals(node2, edge.getTarget());
		assertEquals(2, listener.getEntries().size());
		assertEquals(RECONNECT_EDGE, listener.getEntries().get(1).getEventType());
	}
}
