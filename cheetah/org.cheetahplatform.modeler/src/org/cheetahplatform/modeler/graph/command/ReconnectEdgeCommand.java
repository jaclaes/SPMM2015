package org.cheetahplatform.modeler.graph.command;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Node;

public class ReconnectEdgeCommand extends AbstractReconnectEdgeCommand {

	public ReconnectEdgeCommand(Edge edge, Node source, Node target) {
		super(edge, source, target);
	}

	@Override
	public void execute() {
		// create the entry before to capture the name of the edge before reconnecting
		AuditTrailEntry entry = createAuditrailEntry(RECONNECT_EDGE);

		Edge edge = (Edge) element;
		oldSource = edge.getSource();
		oldTarget = edge.getTarget();
		edge.setSource(source);
		edge.setTarget(target);

		addSourceNodeAttributes(entry, source);
		addTargetNodeAttributes(entry, target);

		if (oldSource != null && oldSource.equals(source)) {
			addOldTargetNodeAttribute(entry, oldTarget);
		} else {
			addOldSourceNodeAttribute(entry, oldSource);
		}

		log(entry);
	}
}
