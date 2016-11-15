package org.cheetahplatform.modeler.graph.command;

import java.util.Date;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.draw2d.geometry.Point;

public class CreateNodeCommand extends NodeCommand {

	private final Point location;
	private String name;
	private Date startTime;

	public CreateNodeCommand(Graph graph, Node node, Point location) {
		this(graph, node, location, null);
	}

	public CreateNodeCommand(Graph graph, Node node, Point location, String name) {
		super(graph, node);

		this.location = location;
		this.name = name;
	}

	@Override
	public void execute() {
		Node node = (Node) element;

		node.setLocation(location);
		graph.addNode(node);
		node.setName(name);

		AuditTrailEntry entry = createAuditrailEntry(CREATE_NODE);
		entry.setAttribute(X, location.x);
		entry.setAttribute(Y, location.y);
		if (startTime != null) {
			entry.setAttribute(ADD_NODE_START_TIME, startTime.getTime());
		}

		log(entry);
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@Override
	public void undo() {
		new DeleteNodeCommand((Node) element).execute();
	}

}
