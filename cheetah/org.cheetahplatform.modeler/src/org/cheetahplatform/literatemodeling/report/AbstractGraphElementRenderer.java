package org.cheetahplatform.literatemodeling.report;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.cheetahplatform.literatemodeling.LiterateModelingConstants;
import org.cheetahplatform.literatemodeling.model.LiterateModel;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.ModelerConstants;
import org.cheetahplatform.modeler.ViewerWithAccessibleLightweightSystem;
import org.cheetahplatform.modeler.generic.GenericEditPartFactory;
import org.cheetahplatform.modeler.generic.GraphCommandStack;
import org.cheetahplatform.modeler.generic.GraphDimension;
import org.cheetahplatform.modeler.generic.GraphEditDomain;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.core.runtime.Assert;
import org.eclipse.draw2d.Bendpoint;
import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.ShortestPathConnectionRouter;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editparts.ScalableRootEditPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;

import com.swtdesigner.SWTResourceManager;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         01.06.2010
 */
public abstract class AbstractGraphElementRenderer implements IGraphElementRenderer {
	private ViewerWithAccessibleLightweightSystem outputViewer;
	private Graph graph;
	private GraphDimension dimension;
	private final Map<Node, Node> processedNodes = new HashMap<Node, Node>();
	protected final LiterateModel model;

	private final Map<Edge, Edge> processedEdges = new HashMap<Edge, Edge>();

	public AbstractGraphElementRenderer(LiterateModel model) {
		Assert.isNotNull(model);
		this.model = model;
	}

	protected Edge addEdge(Edge edge) {
		Node source = edge.getSource();
		Node target = edge.getTarget();
		Node sourceCopy = addNode(source);
		Node targetCopy = addNode(target);
		Edge edgeCopy = addEdge(edge, sourceCopy, targetCopy);
		return edgeCopy;
	}

	protected Edge addEdge(Edge originalEdge, Node source, Node target) {
		if (processedEdges.containsKey(originalEdge)) {
			return processedEdges.get(originalEdge);
		}
		Edge edge = new Edge(source.getParent(), EditorRegistry.getEdgeDescriptors(EditorRegistry.BPMN).get(0));
		edge.setSource(source);
		edge.setTarget(target);
		getGraph().addEdge(edge);

		if (originalEdge.getName() != null) {
			edge.setName(originalEdge.getName());
			edge.getLabel().setOffset(originalEdge.getLabel().getLocation());
		}

		List<Bendpoint> bendPoints = originalEdge.getBendPoints();
		if (bendPoints != null) {
			for (Bendpoint bendpoint : bendPoints) {
				edge.addBendPoint(bendpoint.getLocation(), bendPoints.indexOf(bendpoint));
			}
		}

		processedEdges.put(originalEdge, edge);
		return edge;
	}

	protected void addNeighboringNodes(Node originalNode, Node node) {
		List<? extends Object> sourceConnections = originalNode.getSourceConnections();
		for (Object object : sourceConnections) {
			Edge edge = (Edge) object;
			Node target = edge.getTarget();
			Node targetNode = addNode(target);
			addEdge(edge, node, targetNode);
		}

		List<? extends Object> targetConnections = originalNode.getTargetConnections();
		for (Object object : targetConnections) {
			Edge edge = (Edge) object;
			Node target = edge.getSource();
			Node sourceNode = addNode(target);
			addEdge(edge, sourceNode, node);
		}
	}

	protected Node addNode(Node originalNode) {
		if (processedNodes.containsKey(originalNode)) {
			return processedNodes.get(originalNode);
		}

		Node node = new Node(getGraph(), originalNode.getDescriptor());
		node.setName(originalNode.getName());
		node.setLocation(originalNode.getLocation());
		getGraph().addNode(node);

		processedNodes.put(originalNode, node);
		return node;
	}

	/**
	 * Returns the graph.
	 * 
	 * @return the graph
	 */
	protected Graph getGraph() {
		if (graph == null) {
			initialize();
		}
		return graph;
	}

	private GraphDimension getGraphDimension() {
		if (dimension == null) {
			dimension = new GraphDimension(getGraph(), getOutputViewer());
		}
		return dimension;
	}

	protected Set<Node> getNeighboringNodes(Node node) {
		Set<Node> neigbors = new HashSet<Node>();

		List<? extends Object> sourceConnections = node.getSourceConnections();
		for (Object object : sourceConnections) {
			Edge edge = (Edge) object;
			neigbors.add(edge.getTarget());
		}

		List<? extends Object> targetConnections = node.getTargetConnections();
		for (Object object : targetConnections) {
			Edge edge = (Edge) object;
			neigbors.add(edge.getSource());
		}

		return neigbors;
	}

	protected abstract Set<Node> getNodes();

	/**
	 * Returns the outputViewer.
	 * 
	 * @return the outputViewer
	 */
	protected ViewerWithAccessibleLightweightSystem getOutputViewer() {
		if (outputViewer == null) {
			initialize();
		}
		return outputViewer;
	}

	private void initialize() {
		outputViewer = new ViewerWithAccessibleLightweightSystem();
		outputViewer.setEditDomain(new GraphEditDomain());
		outputViewer.setEditPartFactory(new GenericEditPartFactory());

		graph = new Graph(EditorRegistry.getDescriptors(EditorRegistry.BPMN));
		outputViewer.setContents(graph);
		((GraphCommandStack) outputViewer.getEditDomain().getCommandStack()).setGraph(graph);

		ScalableRootEditPart rootEditPart = (ScalableRootEditPart) outputViewer.getRootEditPart();
		ConnectionLayer layer = (ConnectionLayer) rootEditPart.getLayer(LayerConstants.CONNECTION_LAYER);
		ShortestPathConnectionRouter router = new ShortestPathConnectionRouter((IFigure) ((Layer) rootEditPart.getContentPane())
				.getChildren().get(0));
		router.setSpacing(10);
		layer.setConnectionRouter(router);

		// reset some values in the root edit part, as they are inherited by children - not allowed to be null
		IFigure rootFigure = ((AbstractGraphicalEditPart) outputViewer.getRootEditPart()).getFigure();
		rootFigure.setFont(SWTResourceManager.getFont("Calibri", 10, SWT.NONE)); //$NON-NLS-1$
		rootFigure.setForegroundColor(SWTResourceManager.getColor(0, 0, 0));
	}

	protected Image renderImage() {
		Dimension size = getGraphDimension().getDimension();
		IFigure root = getOutputViewer().getLightweightSystem().getRootFigure();
		root.setSize(size);

		for (Node node : getGraph().getNodes()) {
			Point newLocation = node.getLocation().getCopy().translate(getGraphDimension().getTranslateValue());
			node.setLocation(newLocation);
		}

		List<Edge> edges = getGraph().getEdges();
		for (Edge edge : edges) {
			List<Bendpoint> bendPoints = edge.getBendPoints();

			if (bendPoints != null) {
				for (Bendpoint bendpoint : bendPoints) {
					Point newLocation = bendpoint.getLocation().getCopy().translate(getGraphDimension().getTranslateValue());
					edge.moveBendPoint(bendPoints.indexOf(bendpoint), newLocation);
				}
			}
		}

		Image image = new Image(null, size.width, size.height);
		GC gc = new GC(image);
		getOutputViewer().getLightweightSystem().paint(gc);
		gc.dispose();
		return image;
	}

	protected void selectGraphelement(GraphElement element) {
		element.setProperty(ModelerConstants.PROPERTY_BACKGROUND_COLOR, LiterateModelingConstants.SELECTION_COLOR.getRGB());
	}
}