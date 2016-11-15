/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.modeler;

import static org.cheetahplatform.modeler.graph.command.AbstractGraphCommand.CREATE_EDGE;
import static org.cheetahplatform.test.modeler.AbstractGraphCommandTest.NODE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.util.ArrayList;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.decserflow.SelectionConstraintEdge;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;
import org.cheetahplatform.modeler.graph.command.CreateNodeCommand;
import org.cheetahplatform.modeler.graph.descriptor.IEdgeDescriptor;
import org.cheetahplatform.modeler.graph.descriptor.IGraphElementDescriptor;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.draw2d.geometry.Point;
import org.junit.Test;

public class CreateSelectionConstraintCommandTest {
	@Test
	public void execute() throws Exception {
		IEdgeDescriptor descriptor = (IEdgeDescriptor) EditorRegistry.getDescriptor(EditorRegistry.DECSERFLOW_SELECTION);
		ArrayList<IGraphElementDescriptor> descriptors = new ArrayList<IGraphElementDescriptor>();
		descriptors.add(descriptor);
		Graph graph = new Graph(descriptors);

		TestLogListener listener = new TestLogListener();
		graph.addLogListener(listener);
		Node node1 = NODE.createModel(graph);
		SelectionConstraintEdge edge = (SelectionConstraintEdge) descriptor.createModel(graph);
		edge.setMinimum(10);
		edge.setMaximum(100);
		new CreateNodeCommand(graph, node1, new Point()).execute();
		AbstractGraphCommand command = descriptor.createCreateEdgeCommand(graph, edge, node1, node1, null);
		command.execute();

		assertEquals(node1, edge.getTarget());
		assertEquals(node1, edge.getSource());
		assertEquals(2, listener.getEntries().size());
		AuditTrailEntry creationEntry = listener.getEntries().get(1);
		assertEquals(CREATE_EDGE, creationEntry.getEventType());

		graph = new Graph(descriptors);
		graph.addNode(node1);
		command = descriptor.createCreateEdgeCommand(graph, creationEntry);
		command.execute();

		assertEquals(1, graph.getEdges().size());
		SelectionConstraintEdge newEdge = (SelectionConstraintEdge) graph.getEdges().get(0);
		assertNotSame(edge, newEdge);

		assertEquals(node1, newEdge.getTarget());
		assertEquals(node1, newEdge.getSource());
		assertEquals(10, newEdge.getMinimum());
		assertEquals(100, newEdge.getMaximum());
	}
}
