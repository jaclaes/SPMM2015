package org.cheetahplatform.literatemodeling.report;

import java.util.HashSet;
import java.util.Set;

import org.cheetahplatform.literatemodeling.model.LiterateModel;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.graphics.Image;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         02.06.2010
 */
public class EdgeLiterateModelingAssociationGraphRenderer extends AbstractGraphElementRenderer {
	private final EdgeLiterateModelingAssociation association;

	/**
	 * @param model
	 * @param association
	 */
	public EdgeLiterateModelingAssociationGraphRenderer(LiterateModel model, EdgeLiterateModelingAssociation association) {
		super(model);
		Assert.isNotNull(association);
		this.association = association;
	}

	@Override
	public IReportElement render() {
		Edge edge = association.getGraphElement();

		Edge edgeCopy = addEdge(edge);
		selectGraphelement(edgeCopy);

		Image image = renderImage();

		String description = model.getTextForAssociation(association);
		return new LiterateModelingAssociationReportElement(association.getGraphElement().getNameNullSafe(), description, image);
	}

	@Override
	protected Set<Node> getNodes() {
		Set<Node> nodes = new HashSet<Node>();
		Edge edge = association.getGraphElement();
		Node source = edge.getSource();
		Node target = edge.getTarget();
		nodes.add(source);
		nodes.add(target);
		return nodes;
	}
}
