package org.cheetahplatform.modeler.changepattern.model;

import java.util.ArrayList;

import org.cheetahplatform.modeler.graph.command.ReconnectEdgeCommand;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.draw2d.geometry.Point;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         21.06.2010
 */
public class EmbedInLoopChangePattern extends AbstractChangePattern {

	public static final String EMBED_IN_LOOP_CHANGE_PATTERN = "EMBED_IN_LOOP";

	public EmbedInLoopChangePattern(Graph graph, Edge incomingEdge, Edge outgoingEdge) {
		super(graph);

		int x = incomingEdge.getSource().getBounds().getTopRight().getCopy().translate(20, 0).x;
		int y = incomingEdge.getTarget().getBounds().getTopLeft().y;
		Point loopJoinLocation = new Point(x, y);
		Point loopSplitLocation = outgoingEdge.getSource().getBounds().getTopRight().getCopy().translate(60, 0);

		Node loopJoin = addXorGatewayCommand(graph, loopJoinLocation);
		Node loopSplit = addXorGatewayCommand(graph, loopSplitLocation);
		addAddEdgeCommand(graph, loopSplit, loopJoin);
		addAddEdgeCommand(graph, loopJoin, incomingEdge.getTarget());
		addAddEdgeCommand(graph, outgoingEdge.getSource(), loopSplit);
		add(new ReconnectEdgeCommand(incomingEdge, incomingEdge.getSource(), loopJoin));
		add(new ReconnectEdgeCommand(outgoingEdge, loopSplit, outgoingEdge.getTarget()));

		Point incomingCutOff = incomingEdge.getSource().getBounds().getRight();
		moveHorizontally(incomingCutOff.x, new Point(44, 0), new ArrayList<Edge>());

		loopSplitLocation.getCopy().translate(24, 0).translate(20, 0).translate(incomingCutOff.getCopy().getNegated());
		moveHorizontally(outgoingEdge.getSource().getBounds().getRight().x, new Point(44, 0), new ArrayList<Edge>());
	}

	@Override
	public String getName() {
		return EMBED_IN_LOOP_CHANGE_PATTERN;
	}
}
