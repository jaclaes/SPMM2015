/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.modeler;

import static org.cheetahplatform.modeler.graph.command.AbstractGraphCommand.MOVE_EDGE_BENDPOINT;
import static org.cheetahplatform.test.modeler.AbstractGraphCommandTest.EDGE;
import static org.cheetahplatform.test.modeler.AbstractGraphCommandTest.NODE;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.cheetahplatform.modeler.graph.command.MoveEdgeBendPointCommand;
import org.cheetahplatform.modeler.graph.descriptor.IGraphElementDescriptor;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.draw2d.geometry.Point;
import org.junit.Test;

public class MoveEdgeBendPointTest {
	@Test
	public void undo() throws Exception {
		Graph graph = new Graph(new ArrayList<IGraphElementDescriptor>());
		TestLogListener listener = new TestLogListener();
		graph.addLogListener(listener);
		Node node = NODE.createModel(graph);
		Edge edge = EDGE.createModel(graph);
		graph.addNode(node);
		graph.addEdge(edge);
		edge.addBendPoint(new Point(20, 10), 0);
		MoveEdgeBendPointCommand command = new MoveEdgeBendPointCommand(edge, 0, new Point(50, 100));
		command.execute();

		assertEquals(1, edge.getBendPoints().size());
		assertEquals(new Point(50, 100), edge.getBendPoint(0));
		assertEquals(1, listener.getEntries().size());
		assertEquals(MOVE_EDGE_BENDPOINT, listener.getEntries().get(0).getEventType());

		command.undo();
		assertEquals(1, edge.getBendPoints().size());
		assertEquals(new Point(20, 10), edge.getBendPoint(0));
		assertEquals(2, listener.getEntries().size());
		assertEquals(MOVE_EDGE_BENDPOINT, listener.getEntries().get(1).getEventType());
	}
}
