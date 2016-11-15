/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.modeler;

import static org.cheetahplatform.modeler.graph.command.AbstractGraphCommand.CREATE_EDGE;
import static org.cheetahplatform.modeler.graph.command.AbstractGraphCommand.DELETE_EDGE;
import static org.cheetahplatform.test.modeler.AbstractGraphCommandTest.EDGE;
import static org.cheetahplatform.test.modeler.AbstractGraphCommandTest.NODE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.cheetahplatform.modeler.graph.command.DeleteEdgeCommand;
import org.cheetahplatform.modeler.graph.descriptor.IGraphElementDescriptor;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;
import org.junit.Test;

public class DeleteEdgeCommandTest {
	@Test
	public void undo() throws Exception {
		Graph graph = new Graph(new ArrayList<IGraphElementDescriptor>());
		TestLogListener listener = new TestLogListener();
		graph.addLogListener(listener);
		Node node = NODE.createModel(graph);
		Edge edge = EDGE.createModel(graph);
		edge.setSource(node);
		edge.setTarget(node);
		DeleteEdgeCommand command = new DeleteEdgeCommand(edge);
		command.execute();

		assertEquals(0, graph.getEdges().size());
		assertFalse(graph.getEdges().contains(edge));
		assertEquals(1, listener.getEntries().size());
		assertEquals(DELETE_EDGE, listener.getEntries().get(0).getEventType());

		command.undo();
		assertEquals(1, graph.getEdges().size());
		assertEquals(2, listener.getEntries().size());
		assertTrue(graph.getEdges().contains(edge));
		assertEquals(CREATE_EDGE, listener.getEntries().get(1).getEventType());
	}
}
