/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.modeler;

import static org.cheetahplatform.modeler.decserflow.descriptor.SelectionConstraintDescriptor.MAXIMUM;
import static org.cheetahplatform.modeler.decserflow.descriptor.SelectionConstraintDescriptor.MINIMUM;
import static org.cheetahplatform.modeler.graph.command.AbstractGraphCommand.CREATE_EDGE;
import static org.cheetahplatform.test.modeler.AbstractGraphCommandTest.NODE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.decserflow.SelectionConstraintEdge;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;
import org.cheetahplatform.modeler.graph.descriptor.IGraphElementDescriptor;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;
import org.junit.Test;

public class SelectionConstraintDescriptorTest {
	@Test
	public void logAdditionalInfo() throws Exception {
		Graph graph = new Graph(new ArrayList<IGraphElementDescriptor>());
		TestLogListener listener = new TestLogListener();
		graph.addLogListener(listener);
		Node node = NODE.createModel(graph);
		SelectionConstraintEdge edge = (SelectionConstraintEdge) EditorRegistry.getDescriptor(EditorRegistry.DECSERFLOW_SELECTION)
				.createModel(graph);
		edge.setMinimum(10);
		edge.setMaximum(20);

		AbstractGraphCommand command = edge.getDescriptor().createCreateEdgeCommand(graph, edge, node, node, null);
		command.execute();

		assertEquals(1, graph.getEdges().size());
		assertEquals(1, listener.getEntries().size());
		assertTrue(graph.getEdges().contains(edge));
		AuditTrailEntry entry = listener.getEntries().get(0);
		assertEquals(CREATE_EDGE, entry.getEventType());
		assertEquals(10, entry.getIntegerAttribute(MINIMUM));
		assertEquals(20, entry.getIntegerAttribute(MAXIMUM));
	}
}
