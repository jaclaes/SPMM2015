package org.cheetahplatform.literatemodeling.report;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cheetahplatform.literatemodeling.model.LiterateModel;
import org.cheetahplatform.literatemodeling.model.MultiElementLiterateModelingAssociation;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.graphics.Image;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         02.06.2010
 */
public class MultiElementGraphRenderer extends AbstractGraphElementRenderer  {

	private final MultiElementLiterateModelingAssociation association;

	/**
	 * @param multiElementLiterateModelingAssociation
	 * @param model
	 */
	public MultiElementGraphRenderer(MultiElementLiterateModelingAssociation association, LiterateModel model) {
		super(model);
		Assert.isNotNull(association);
		this.association = association;
	}

	@Override
	protected Set<Node> getNodes() {
		Set<Node> allNodesToBeAdded = new HashSet<Node>();
		List<Node> nodes = association.getNodes();
		allNodesToBeAdded.addAll(nodes);
		for (Node node : nodes) {
			allNodesToBeAdded.addAll(getNeighboringNodes(node));
		}
		return allNodesToBeAdded;
	}

	@Override
	public IReportElement render() {
		List<Node> nodes = association.getNodes();
		for (Node node : nodes) {
			Node nodeCopy = addNode(node);
			addNeighboringNodes(node, nodeCopy);
			selectGraphelement(nodeCopy);
		}

		List<Edge> edges = association.getEdges();
		for (Edge edge : edges) {
			addEdge(edge);
		}

		Image image = renderImage();

		String description = model.getTextForAssociation(association);
		return new LiterateModelingAssociationReportElement(association.getName(), description, image);

	}
}
