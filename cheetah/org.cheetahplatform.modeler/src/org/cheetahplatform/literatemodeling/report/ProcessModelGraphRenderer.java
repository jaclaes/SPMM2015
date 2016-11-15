package org.cheetahplatform.literatemodeling.report;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cheetahplatform.literatemodeling.model.LiterateModel;
import org.cheetahplatform.modeler.graph.GraphicalGraphViewerWithFlyoutPalette;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.graphics.Image;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         01.06.2010
 */
public class ProcessModelGraphRenderer extends AbstractGraphElementRenderer {
	private final GraphicalGraphViewerWithFlyoutPalette viewer;

	public ProcessModelGraphRenderer(LiterateModel model, GraphicalGraphViewerWithFlyoutPalette viewer) {
		super(model);
		Assert.isNotNull(viewer);
		this.viewer = viewer;
	}

	@Override
	protected Set<Node> getNodes() {
		List<Node> nodes = viewer.getGraph().getNodes();

		return new HashSet<Node>(nodes);
	}

	/**
	 * @param viewer
	 * @return
	 */
	@Override
	public IReportElement render() {
		Graph graph = viewer.getGraph();
		// do not just set the content of the viewer to force translation of nodes
		for (Node node : graph.getNodes()) {
			addNode(node);
		}
		for (Edge edge : graph.getEdges()) {
			addEdge(edge);
		}
		Image image = renderImage();
		return new GraphReportElement(image, "Process Model");
	}
}
