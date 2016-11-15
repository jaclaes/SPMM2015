package org.cheetahplatform.literatemodeling.report;

import java.util.HashSet;
import java.util.Set;

import org.cheetahplatform.literatemodeling.model.LiterateModel;
import org.cheetahplatform.literatemodeling.model.NodeLiterateModelingAssociation;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.graphics.Image;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         01.06.2010
 */
public class NodeLiterateModelingAssociationGraphRenderer extends AbstractGraphElementRenderer {
	private final NodeLiterateModelingAssociation association;

	public NodeLiterateModelingAssociationGraphRenderer(NodeLiterateModelingAssociation association, LiterateModel model) {
		super(model);
		Assert.isNotNull(association);
		this.association = association;
	}

	@Override
	public IReportElement render() {
		Node originalNode = association.getGraphElement();
		Node node = addNode(originalNode);
		selectGraphelement(node);

		addNeighboringNodes(originalNode, node);
		Image image = renderImage();

		String description = model.getTextForAssociation(association);
		return new LiterateModelingAssociationReportElement(association.getGraphElement().getNameNullSafe(), description, image);
	}

	@Override
	protected Set<Node> getNodes() {
		Set<Node> nodes = new HashSet<Node>();
		nodes.add(association.getGraphElement());
		nodes.addAll(getNeighboringNodes(association.getGraphElement()));
		return nodes;
	}
}
