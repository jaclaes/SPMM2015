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
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.cheetahplatform.modeler.graph.command.CreateNodeCommand;
import org.cheetahplatform.modeler.graph.descriptor.IGraphElementDescriptor;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.draw2d.geometry.Point;
import org.junit.Test;

public class CreateNodeCommandTest {
	@Test
	public void undo() throws Exception {
		Graph graph = new Graph(new ArrayList<IGraphElementDescriptor>());
		TestLogListener listener = new TestLogListener();
		graph.addLogListener(listener);
		Node node = NODE.createModel(graph);
		CreateNodeCommand command = new CreateNodeCommand(graph, node, new Point(1, 2));
		command.execute();

		assertEquals(1, graph.getNodes().size());
		assertEquals(1, listener.getEntries().size());
		assertTrue(graph.getNodes().contains(node));
		assertEquals(CREATE_NODE, listener.getEntries().get(0).getEventType());

		command.undo();
		assertEquals(0, graph.getNodes().size());
		assertFalse(graph.getNodes().contains(node));
		assertEquals(2, listener.getEntries().size());
		assertEquals(DELETE_NODE, listener.getEntries().get(1).getEventType());
	}
}
