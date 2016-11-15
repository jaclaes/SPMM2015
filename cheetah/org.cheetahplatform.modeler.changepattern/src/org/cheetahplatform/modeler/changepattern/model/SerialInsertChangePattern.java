package org.cheetahplatform.modeler.changepattern.model;

import java.util.ArrayList;
import java.util.Date;

import org.cheetahplatform.modeler.graph.command.ReconnectEdgeCommand;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.core.runtime.Assert;
import org.eclipse.draw2d.geometry.Point;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         16.06.2010
 */
public class SerialInsertChangePattern extends AbstractChangePattern {
	public static final String SERIAL_INSERT_CHANGE_PATTERN = "SERIAL_INSERT";

	public SerialInsertChangePattern(Graph graph, Edge edge, String activityName, Date startTime) {
		super(graph);
		Assert.isNotNull(edge);
		Assert.isNotNull(activityName);

		Node source = edge.getSource();
		Point location = source.getBounds().getTopRight().getCopy().translate(20, 0);
		Node activity = addAddActivityCommand(graph, activityName, location, startTime);

		Node oldTarget = edge.getTarget();
		ReconnectEdgeCommand reconnectEdgeCommand = new ReconnectEdgeCommand(edge, edge.getSource(), activity);
		add(reconnectEdgeCommand);

		addAddEdgeCommand(graph, activity, oldTarget);

		Point delta = getActivitySize().translate(20, 0);
		delta.y = 0;
		int cutOff = source.getBounds().getRight().x;
		moveHorizontally(cutOff, delta, new ArrayList<Edge>());
	}

	@Override
	public String getName() {
		return SERIAL_INSERT_CHANGE_PATTERN;
	}
}
