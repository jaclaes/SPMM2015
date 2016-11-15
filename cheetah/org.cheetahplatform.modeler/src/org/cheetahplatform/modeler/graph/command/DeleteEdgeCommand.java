package org.cheetahplatform.modeler.graph.command;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.modeler.graph.model.Edge;

public class DeleteEdgeCommand extends EdgeCommand {

	private final Edge edge;

	public DeleteEdgeCommand(Edge edge) {
		super(edge.getGraph(), edge);

		this.edge = edge;
	}

	@Override
	public void execute() {
		edge.delete();

		AuditTrailEntry entry = createAuditrailEntry(DELETE_EDGE);
		addSourceNodeAttributes(entry, edge.getSource());
		addTargetNodeAttributes(entry, edge.getTarget());
		log(entry);
	}

	@Override
	public void undo() {
		AbstractGraphCommand command = edge.getDescriptor().createCreateEdgeCommand(getGraph(), edge, edge.getSource(), edge.getTarget(),
				null);
		command.execute();
	}

}
