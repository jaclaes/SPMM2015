/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.modeler;

import static org.cheetahplatform.modeler.graph.command.AbstractGraphCommand.MOVE_EDGE_LABEL;
import static org.cheetahplatform.test.modeler.AbstractGraphCommandTest.EDGE;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.cheetahplatform.modeler.graph.command.MoveEdgeLabelCommand;
import org.cheetahplatform.modeler.graph.descriptor.IGraphElementDescriptor;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.eclipse.draw2d.geometry.Point;
import org.junit.Test;

public class MoveEdgeLabelCommandTest {
	@Test
	public void undo() throws Exception {
		Graph graph = new Graph(new ArrayList<IGraphElementDescriptor>());
		TestLogListener listener = new TestLogListener();
		graph.addLogListener(listener);
		Edge edge = EDGE.createModel(graph);
		graph.addEdge(edge);
		MoveEdgeLabelCommand command = new MoveEdgeLabelCommand(edge.getLabel(), new Point(10, -10));
		command.execute();

		assertEquals(new Point(10, -10), edge.getLabel().getOffset());
		assertEquals(1, listener.getEntries().size());
		assertEquals(MOVE_EDGE_LABEL, listener.getEntries().get(0).getEventType());

		command.undo();
		assertEquals(new Point(0, 0), edge.getLabel().getOffset());
		assertEquals(2, listener.getEntries().size());
		assertEquals(MOVE_EDGE_LABEL, listener.getEntries().get(1).getEventType());
	}

}
