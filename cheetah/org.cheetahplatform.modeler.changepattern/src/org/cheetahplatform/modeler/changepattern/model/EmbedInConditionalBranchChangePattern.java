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
public class EmbedInConditionalBranchChangePattern extends AbstractChangePattern {

	public static final String EMBED_IN_CONDITIONAL_BRANCH_CHANGE_PATTERN = "EMBED_IN_CONDITIONAL_BRANCH";

	/**
	 * @param graph
	 * @param incomingEdge
	 * @param outgoingEdge
	 */
	public EmbedInConditionalBranchChangePattern(Graph graph, Edge incomingEdge, Edge outgoingEdge) {
		super(graph);

		int x = incomingEdge.getSource().getBounds().getTopRight().getCopy().translate(20, 0).x;
		int y = incomingEdge.getTarget().getBounds().getTopLeft().y;

		Point xorSplitLocation = new Point(x, y);
		Point xorJoinLocation = outgoingEdge.getSource().getBounds().getTopRight().getCopy().translate(60, 0);

		Node xorSplit = addXorGatewayCommand(graph, xorSplitLocation);
		Node xorJoin = addXorGatewayCommand(graph, xorJoinLocation);
		addAddEdgeCommand(graph, xorSplit, xorJoin);
		addAddEdgeCommand(graph, xorSplit, incomingEdge.getTarget());
		addAddEdgeCommand(graph, outgoingEdge.getSource(), xorJoin);
		add(new ReconnectEdgeCommand(incomingEdge, incomingEdge.getSource(), xorSplit));
		add(new ReconnectEdgeCommand(outgoingEdge, xorJoin, outgoingEdge.getTarget()));

		Point incomingCutOff = incomingEdge.getSource().getBounds().getRight();
		moveHorizontally(incomingCutOff.x, new Point(44, 0), new ArrayList<Edge>());

		xorJoinLocation.getCopy().translate(24, 0).translate(20, 0).translate(incomingCutOff.getCopy().getNegated());
		moveHorizontally(outgoingEdge.getSource().getBounds().getRight().x, new Point(44, 0), new ArrayList<Edge>());
	}

	@Override
	public String getName() {
		return EMBED_IN_CONDITIONAL_BRANCH_CHANGE_PATTERN;
	}
}
