package org.cheetahplatform.modeler.decserflow;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.modeler.graph.command.AbstractReconnectEdgeCommand;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Node;

public class ReconnectSingleActivityConstraintEdgeCommand extends AbstractReconnectEdgeCommand {

	public ReconnectSingleActivityConstraintEdgeCommand(Edge edge, Node source, Node target) {
		super(edge, source, target);
	}

	@Override
	public void execute() {
		// create the entry before to capture the name of the edge before reconnecting
		AuditTrailEntry entry = createAuditrailEntry(RECONNECT_EDGE);

		Edge edge = getEdge();
		Node newSource = source;
		oldSource = edge.getSource();

		boolean oldAndNewSourceAreNull = oldSource == null && source == null;
		if (oldAndNewSourceAreNull || oldSource != null && oldSource.equals(source)) {
			newSource = target;
		}

		edge.setTarget(newSource);

		addSourceNodeAttributes(entry, newSource);
		addTargetNodeAttributes(entry, newSource);
		log(entry);
	}

}
