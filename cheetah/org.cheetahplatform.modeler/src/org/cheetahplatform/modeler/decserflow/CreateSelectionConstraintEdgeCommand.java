package org.cheetahplatform.modeler.decserflow;

import static org.cheetahplatform.modeler.decserflow.descriptor.SelectionConstraintDescriptor.MAXIMUM;
import static org.cheetahplatform.modeler.decserflow.descriptor.SelectionConstraintDescriptor.MINIMUM;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.modeler.graph.command.CreateEdgeCommand;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;

public class CreateSelectionConstraintEdgeCommand extends CreateEdgeCommand {

	private static final int NO_MINIMUM = -1;
	private static final int NO_MAXIMUM = -1;

	private int minimum;
	private int maximum;

	public CreateSelectionConstraintEdgeCommand(Graph graph, Edge edge, Node source, Node target, String name) {
		this(graph, edge, source, target, name, NO_MINIMUM, NO_MAXIMUM);
	}

	public CreateSelectionConstraintEdgeCommand(Graph graph, Edge edge, Node source, Node target, String name, int minimum, int maximum) {
		super(graph, edge, source, target, name);

		this.minimum = minimum;
		this.maximum = maximum;
	}

	@Override
	protected void log(AuditTrailEntry entry) {
		SelectionConstraintEdge constraint = (SelectionConstraintEdge) getEdge();
		if (minimum != NO_MINIMUM) {
			constraint.setMinimum(minimum);
		}
		if (maximum != NO_MAXIMUM) {
			constraint.setMaximum(maximum);
		}

		entry.setAttribute(MINIMUM, constraint.getMinimum());
		entry.setAttribute(MAXIMUM, constraint.getMaximum());

		super.log(entry);
	}

}
