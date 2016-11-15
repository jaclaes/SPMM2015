package org.cheetahplatform.modeler.changepattern.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
public class ParallelInsertChangePattern extends AbstractChangePattern {

	public static final String PARALLEL_INSERT_CHANGE_PATTERN = "PARALLEL_INSERT";

	public ParallelInsertChangePattern(Graph graph, Edge incomingEdge, Edge outgoingEdge, String name, Date startTime) {
		super(graph);
		Node source = incomingEdge.getSource();
		Node firstNode = incomingEdge.getTarget();
		Node lastNode = outgoingEdge.getSource();

		int x = incomingEdge.getSource().getBounds().getTopRight().getCopy().translate(20, 0).x;
		int y = incomingEdge.getTarget().getBounds().getTopLeft().y;

		Point andSplitLocation = new Point(x, y);
		Point andJoinLocation = lastNode.getBounds().getTopRight().getCopy().translate(60, 0);
		Point activityLocation = andSplitLocation.getCopy().translate(50, 70);

		Node activity = addAddActivityCommand(graph, name, activityLocation, startTime);
		Node andSplit = addAddAndGatewayCommand(graph, andSplitLocation);
		Node andJoin = addAddAndGatewayCommand(graph, andJoinLocation);

		addAddEdgeCommand(graph, andSplit, activity);
		addAddEdgeCommand(graph, activity, andJoin);

		ReconnectEdgeCommand reconnectEdgeCommand = new ReconnectEdgeCommand(incomingEdge, source, andSplit);
		add(reconnectEdgeCommand);

		ReconnectEdgeCommand reconnectEdgeCommand2 = new ReconnectEdgeCommand(outgoingEdge, andJoin, outgoingEdge.getTarget());
		add(reconnectEdgeCommand2);

		addAddEdgeCommand(graph, andSplit, firstNode);
		addAddEdgeCommand(graph, lastNode, andJoin);

		Point incomingCutOff = source.getBounds().getRight();
		moveHorizontally(incomingCutOff.x, new Point(50, 0), new ArrayList<Edge>());

		andJoinLocation.getCopy().translate(50, 0).translate(incomingCutOff.getCopy().getNegated());
		moveHorizontally(lastNode.getBounds().getRight().x, new Point(44, 0), new ArrayList<Edge>());

		moveVertically(andSplitLocation.getCopy().translate(0, 24).y, new Point(0, 70));
	}

	@Override
	protected List<IGraphOptimizer> getGraphOptimizers() {
		List<IGraphOptimizer> optimizers = new ArrayList<IGraphOptimizer>();
		optimizers.add(new AndJoinOptimizer(graph));
		return optimizers;
	}

	@Override
	public String getName() {
		return PARALLEL_INSERT_CHANGE_PATTERN;
	}
}
