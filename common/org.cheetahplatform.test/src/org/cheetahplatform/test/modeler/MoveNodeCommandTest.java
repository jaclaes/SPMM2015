/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.modeler;

import static org.cheetahplatform.modeler.graph.command.AbstractGraphCommand.MOVE_NODE;
import static org.cheetahplatform.test.modeler.AbstractGraphCommandTest.NODE;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.cheetahplatform.modeler.graph.command.MoveNodeCommand;
import org.cheetahplatform.modeler.graph.descriptor.IGraphElementDescriptor;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.draw2d.geometry.Point;
import org.junit.Test;

public class MoveNodeCommandTest {
	@Test
	public void undo() throws Exception {
		Graph graph = new Graph(new ArrayList<IGraphElementDescriptor>());
		TestLogListener listener = new TestLogListener();
		graph.addLogListener(listener);
		Node node = NODE.createModel(graph);
		node.setLocation(new Point(10, 10));
		graph.addNode(node);
		MoveNodeCommand command = new MoveNodeCommand(node, new Point(10, -10));
		command.execute();

		assertEquals(new Point(20, 0), node.getLocation());
		assertEquals(1, listener.getEntries().size());
		assertEquals(MOVE_NODE, listener.getEntries().get(0).getEventType());

		command.undo();
		assertEquals(new Point(10, 10), node.getLocation());
		assertEquals(2, listener.getEntries().size());
		assertEquals(MOVE_NODE, listener.getEntries().get(1).getEventType());
	}
}
