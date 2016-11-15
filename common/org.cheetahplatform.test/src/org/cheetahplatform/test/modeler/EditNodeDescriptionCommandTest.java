/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.modeler;

import static org.cheetahplatform.test.modeler.AbstractGraphCommandTest.NODE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.modeler.decserflow.descriptor.EditNodeDescriptionCommand;
import org.cheetahplatform.modeler.graph.descriptor.IGraphElementDescriptor;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;
import org.junit.Test;

public class EditNodeDescriptionCommandTest {
	@Test
	public void setName() throws Exception {
		Graph graph = new Graph(new ArrayList<IGraphElementDescriptor>());
		TestLogListener listener = new TestLogListener();
		graph.addLogListener(listener);
		Node node = NODE.createModel(graph);
		graph.addNode(node);

		assertEquals(0, listener.getEntries().size());
		EditNodeDescriptionCommand command = new EditNodeDescriptionCommand(node, "new description");
		command.execute();
		assertEquals(1, listener.getEntries().size());
		AuditTrailEntry entry = listener.getEntries().get(0);
		assertTrue(entry.isAttributeDefined(EditNodeDescriptionCommand.ATTRIBUTE_DESCRIPTION));
		assertEquals("new description", entry.getAttribute(EditNodeDescriptionCommand.ATTRIBUTE_DESCRIPTION));
		assertEquals("new description", node.getProperty(EditNodeDescriptionCommand.ATTRIBUTE_DESCRIPTION));

		command.undo();
	}

	@Test
	public void undo() throws Exception {
		Graph graph = new Graph(new ArrayList<IGraphElementDescriptor>());
		TestLogListener listener = new TestLogListener();
		graph.addLogListener(listener);
		Node node = NODE.createModel(graph);
		graph.addNode(node);

		assertEquals(0, listener.getEntries().size());
		EditNodeDescriptionCommand command = new EditNodeDescriptionCommand(node, "new description");
		command.execute();
		command.undo();

		assertEquals(2, listener.getEntries().size());
		AuditTrailEntry entry = listener.getEntries().get(1);
		assertTrue(entry.isAttributeDefined(EditNodeDescriptionCommand.ATTRIBUTE_DESCRIPTION));
		assertEquals(null, entry.getAttribute(EditNodeDescriptionCommand.ATTRIBUTE_DESCRIPTION));
		assertEquals(null, node.getProperty(EditNodeDescriptionCommand.ATTRIBUTE_DESCRIPTION));
	}
}
