/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.modeler;

import static org.cheetahplatform.modeler.decserflow.descriptor.SelectionConstraintDescriptor.EDIT_SELECTION_CONSTRAINT;
import static org.cheetahplatform.modeler.decserflow.descriptor.SelectionConstraintDescriptor.MAXIMUM;
import static org.cheetahplatform.modeler.decserflow.descriptor.SelectionConstraintDescriptor.MINIMUM;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.decserflow.EditSelectionConstraintCommand;
import org.cheetahplatform.modeler.decserflow.SelectionConstraintEdge;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;
import org.cheetahplatform.modeler.graph.descriptor.IGraphElementDescriptor;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.junit.Test;

public class EditSelectionConstraintCommandTest {
	@Test
	public void createFromAuditTrailEntry() throws Exception {
		Graph graph = new Graph(new ArrayList<IGraphElementDescriptor>());
		TestLogListener listener = new TestLogListener();
		graph.addLogListener(listener);
		SelectionConstraintEdge constraint = (SelectionConstraintEdge) EditorRegistry.getDescriptor(EditorRegistry.DECSERFLOW_SELECTION)
				.createModel(graph);
		graph.addEdge(constraint);
		EditSelectionConstraintCommand command = new EditSelectionConstraintCommand(constraint, 1, 2);
		command.execute();
		constraint.unsetMaximum();
		constraint.unsetMinimum();
		assertFalse(constraint.hasMinimum());
		assertFalse(constraint.hasMaximum());

		AuditTrailEntry entry = listener.getEntries().get(0);
		AbstractGraphCommand restoredCommand = AbstractGraphCommand.createCommand(entry, graph);
		restoredCommand.execute();

		assertEquals(1, constraint.getMinimum());
		assertEquals(2, constraint.getMaximum());
	}

	@Test
	public void execute() throws Exception {
		Graph graph = new Graph(new ArrayList<IGraphElementDescriptor>());
		TestLogListener listener = new TestLogListener();
		graph.addLogListener(listener);
		SelectionConstraintEdge constraint = (SelectionConstraintEdge) EditorRegistry.getDescriptor(EditorRegistry.DECSERFLOW_SELECTION)
				.createModel(graph);
		EditSelectionConstraintCommand command = new EditSelectionConstraintCommand(constraint, 1, 2);
		command.execute();

		assertEquals(1, constraint.getMinimum());
		assertEquals(2, constraint.getMaximum());
		assertEquals(1, listener.getEntries().size());
		AuditTrailEntry entry = listener.getEntries().get(0);
		assertEquals(EDIT_SELECTION_CONSTRAINT, entry.getEventType());
		assertEquals(1, entry.getIntegerAttribute(MINIMUM));
		assertEquals(2, entry.getIntegerAttribute(MAXIMUM));

		command.undo();
		assertFalse(constraint.hasMinimum());
		assertFalse(constraint.hasMaximum());
	}
}
