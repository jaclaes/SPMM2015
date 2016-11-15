package org.cheetahplatform.modeler.graph.command;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;

public class CreateEdgeCommand extends EdgeCommand {

	private String name;
	private final Node source;
	private final Node target;

	public CreateEdgeCommand(Graph graph, Edge edge, Node source, Node target, String name) {
		super(graph, edge);

		this.source = source;
		this.target = target;
		this.name = name;
	}

	@Override
	public void execute() {
		Edge edge = (Edge) element;
		edge.setSource(source);
		edge.setTarget(target);
		edge.setName(name);
		graph.addEdge(edge);

		AuditTrailEntry entry = createAuditrailEntry(CREATE_EDGE);
		addSourceNodeAttributes(entry, source);
		addTargetNodeAttributes(entry, target);
		log(entry);
	}

	@Override
	public void undo() {
		new DeleteEdgeCommand((Edge) element).execute();
	}

}
