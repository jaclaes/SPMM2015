package org.cheetahplatform.modeler.changepattern.model;

import java.util.ArrayList;
import java.util.Date;

import org.cheetahplatform.modeler.graph.command.ReconnectEdgeCommand;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.draw2d.geometry.Point;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         17.06.2010
 */
public class ConditionalInsertChangePattern extends AbstractChangePattern {
	public static final String CONDITIONAL_INSERT_CHANGE_PATTERN = "CONDITIONAL_INSERT";

	public ConditionalInsertChangePattern(Graph graph, Edge edge, String name, Date startTime) {
		super(graph);

		Node source = edge.getSource();

		Point xorSplitLocation = source.getBounds().getTopRight().getCopy().translate(20, 0);
		Node xorSplit = addXorGatewayCommand(graph, xorSplitLocation);

		Point activityLocation = xorSplitLocation.getCopy().translate(24, 0).translate(20, 50);
		Node activity = addAddActivityCommand(graph, name, activityLocation, startTime);

		Point xorJoinLocation = activityLocation.getCopy().translate(getActivitySize().x, 0).translate(20, -50);
		Node xorJoin = addXorGatewayCommand(graph, xorJoinLocation);

		addAddEdgeCommand(graph, xorSplit, xorJoin);
		addAddEdgeCommand(graph, xorSplit, activity);
		addAddEdgeCommand(graph, activity, xorJoin);

		Node oldTarget = edge.getTarget();

		add(new ReconnectEdgeCommand(edge, edge.getSource(), xorSplit));

		addAddEdgeCommand(graph, xorJoin, oldTarget);

		Point delta = xorJoinLocation.getCopy().translate(24, 0).translate(20, 0).translate(source.getBounds().getRight().getNegated());
		delta.y = 0; 
		moveHorizontally(source.getBounds().getRight().x, delta, new ArrayList<Edge>());
	}

	@Override
	public String getName() {
		return CONDITIONAL_INSERT_CHANGE_PATTERN;
	}
}
