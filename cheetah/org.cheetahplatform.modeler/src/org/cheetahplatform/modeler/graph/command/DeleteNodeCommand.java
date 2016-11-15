package org.cheetahplatform.modeler.graph.command;

import java.util.List;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.core.service.Services;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Node;

public class DeleteNodeCommand extends NodeCommand {
	private List<Edge> oldSourceConnections;
	private List<Edge> oldTargetConnections;

	public DeleteNodeCommand(Node node) {
		super(node.getGraph(), node);
	}

	@Override
	public void execute() {
		Node node = (Node) element;
		oldSourceConnections = getGraph().getSourceConnections(node);
		oldTargetConnections = getGraph().getTargetConnections(node);
		node.delete();

		AuditTrailEntry entry = createAuditrailEntry(DELETE_NODE);
		log(entry);

		Services.getModificationService().broadcastChange(element, DELETE_NODE);
	}

	@Override
	public void undo() {
		Node node = (Node) element;
		new CreateNodeCommand(getGraph(), node, node.getLocation(), node.getName()).execute();

		// reconnect the model
		for (Edge edge : oldSourceConnections) {
			Assert.isTrue(edge.getSource() == null);
			edge.setSource(node);
		}
		for (Edge edge : oldTargetConnections) {
			Assert.isTrue(edge.getTarget() == null);
			edge.setTarget(node);
		}
	}

}
