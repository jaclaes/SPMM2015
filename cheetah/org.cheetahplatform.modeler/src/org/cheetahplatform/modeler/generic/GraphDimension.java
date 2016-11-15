package org.cheetahplatform.modeler.generic;

import java.util.List;

import org.cheetahplatform.modeler.ViewerWithAccessibleLightweightSystem;
import org.cheetahplatform.modeler.graph.editpart.EdgeEditPart;
import org.cheetahplatform.modeler.graph.editpart.EdgeLabelEditPart;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.draw2d.Bendpoint;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalViewer;

/**
 * 
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         07.06.2010
 */
public class GraphDimension {

	private static final int MARGIN_HEIGHT = 20;
	private static final int MARGIN_WIDTH = 20;
	private Rectangle bounds;
	private final Graph graph;
	private final GraphicalViewer outputViewer;

	/**
	 * @param outputViewer
	 */

	public GraphDimension(Graph graph, ViewerWithAccessibleLightweightSystem outputViewer) {
		this.graph = graph;
		this.outputViewer = outputViewer;
		this.bounds = new Rectangle();

		// call this! Necessary for calculating size - if not called edges are not layouted
		outputViewer.getLightweightSystem().getRootFigure().validate();
		processNodes();
		processEdges();
	}

	public Dimension getDimension() {
		Dimension difference = bounds.getSize().getCopy();
		difference.expand(MARGIN_WIDTH, MARGIN_HEIGHT);
		return difference;
	}

	public Point getTranslateValue() {
		return bounds.getTopLeft().getNegated().translate(new Point(MARGIN_WIDTH / 2, MARGIN_HEIGHT / 2));
	}

	private void processEdges() {
		List<Edge> edges = graph.getEdges();
		for (Edge edge : edges) {
			if (edge.getName() != null) {
				EdgeEditPart edgeEditPart = (EdgeEditPart) outputViewer.getEditPartRegistry().get(edge);
				List children = edgeEditPart.getChildren();
				if (!children.isEmpty()) {
					EdgeLabelEditPart edgeLabelEditPart = (EdgeLabelEditPart) children.get(0);
					IFigure figure = edgeLabelEditPart.getFigure();
					updateImageSize(figure.getBounds());
				}
			}

			List<Bendpoint> bendPoints = edge.getBendPoints();
			if (bendPoints != null) {
				for (Bendpoint bendpoint : bendPoints) {
					Rectangle rectangle = new Rectangle(bendpoint.getLocation(), new Dimension(1, 1));
					updateImageSize(rectangle);
				}
			}
		}
	}

	private void processNodes() {
		for (Node node : graph.getNodes()) {
			updateImageSize(node.getBounds());
		}
	}

	private void updateImageSize(Rectangle boundsToAdd) {
		if (bounds == null) {
			bounds = boundsToAdd;
			return;
		}

		bounds = bounds.getUnion(boundsToAdd);
	}
}
