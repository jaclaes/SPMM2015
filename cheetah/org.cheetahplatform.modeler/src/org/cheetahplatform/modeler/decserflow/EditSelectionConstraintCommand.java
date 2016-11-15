package org.cheetahplatform.modeler.decserflow;

import static org.cheetahplatform.modeler.decserflow.descriptor.SelectionConstraintDescriptor.EDIT_SELECTION_CONSTRAINT;
import static org.cheetahplatform.modeler.decserflow.descriptor.SelectionConstraintDescriptor.MAXIMUM;
import static org.cheetahplatform.modeler.decserflow.descriptor.SelectionConstraintDescriptor.MINIMUM;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.modeler.graph.command.EdgeCommand;

public class EditSelectionConstraintCommand extends EdgeCommand {

	private final int minimum;
	private final int maximum;
	private int oldMinimum;
	private int oldMaximum;

	public EditSelectionConstraintCommand(SelectionConstraintEdge constraint, int minimum, int maximum) {
		super(constraint.getGraph(), constraint);

		this.minimum = minimum;
		this.maximum = maximum;
	}

	@Override
	public void execute() {
		SelectionConstraintEdge constraint = (SelectionConstraintEdge) element;
		oldMinimum = constraint.getMinimum();
		oldMaximum = constraint.getMaximum();
		constraint.setMinimum(minimum);
		constraint.setMaximum(maximum);

		AuditTrailEntry entry = createAuditrailEntry(EDIT_SELECTION_CONSTRAINT);
		entry.setAttribute(MINIMUM, minimum);
		entry.setAttribute(MAXIMUM, maximum);
		log(entry);
	}

	@Override
	public void undo() {
		EditSelectionConstraintCommand command = new EditSelectionConstraintCommand((SelectionConstraintEdge) element, oldMinimum,
				oldMaximum);
		command.execute();
	}

}
