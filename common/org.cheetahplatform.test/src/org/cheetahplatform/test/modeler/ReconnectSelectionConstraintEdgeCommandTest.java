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

import org.cheetahplatform.modeler.decserflow.ReconnectSingleActivityConstraintEdgeCommand;
import org.cheetahplatform.modeler.graph.descriptor.IGraphElementDescriptor;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;
import org.junit.Test;

public class ReconnectSelectionConstraintEdgeCommandTest {
	@Test
	public void execute() throws Exception {
		Graph graph = new Graph(new ArrayList<IGraphElementDescriptor>());
		TestLogListener listener = new TestLogListener();
		graph.addLogListener(listener);
		Node node1 = NODE.createModel(graph);
		Node node2 = NODE.createModel(graph);
		Edge edge = EDGE.createModel(graph);
		edge.setSource(node1);
		graph.addEdge(edge);
		ReconnectSingleActivityConstraintEdgeCommand command = new ReconnectSingleActivityConstraintEdgeCommand(edge, node1, node2);
		command.execute();

		assertEquals(node2, edge.getTarget());
		assertEquals(1, listener.getEntries().size());
		assertEquals(RECONNECT_EDGE, listener.getEntries().get(0).getEventType());

		command.undo();
		assertEquals(node1, edge.getSource());
		assertEquals(2, listener.getEntries().size());
		assertEquals(RECONNECT_EDGE, listener.getEntries().get(1).getEventType());
	}
}
