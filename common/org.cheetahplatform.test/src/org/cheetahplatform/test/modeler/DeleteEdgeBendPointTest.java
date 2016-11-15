/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.modeler;

import static org.cheetahplatform.modeler.graph.command.AbstractGraphCommand.CREATE_EDGE_BENDPOINT;
import static org.cheetahplatform.modeler.graph.command.AbstractGraphCommand.DELETE_EDGE_BENDPOINT;
import static org.cheetahplatform.test.modeler.AbstractGraphCommandTest.EDGE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;

import org.cheetahplatform.modeler.graph.command.DeleteEdgeBendPointCommand;
import org.cheetahplatform.modeler.graph.descriptor.IGraphElementDescriptor;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.eclipse.draw2d.geometry.Point;
import org.junit.Test;

public class DeleteEdgeBendPointTest {
	@Test
	public void undo() throws Exception {
		Graph graph = new Graph(new ArrayList<IGraphElementDescriptor>());
		TestLogListener listener = new TestLogListener();
		graph.addLogListener(listener);
		Edge edge = EDGE.createModel(graph);
		edge.addBendPoint(new Point(50, 60), 0);
		DeleteEdgeBendPointCommand command = new DeleteEdgeBendPointCommand(edge, 0);
		command.execute();

		assertNull(edge.getBendPoints());
		assertEquals(1, listener.getEntries().size());
		assertEquals(DELETE_EDGE_BENDPOINT, listener.getEntries().get(0).getEventType());

		command.undo();
		assertEquals(1, edge.getBendPoints().size());
		assertEquals(new Point(50, 60), edge.getBendPoint(0));
		assertFalse(graph.getEdges().contains(edge));
		assertEquals(CREATE_EDGE_BENDPOINT, listener.getEntries().get(1).getEventType());
	}
}
