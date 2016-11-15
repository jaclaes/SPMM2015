package org.cheetahplatform.literatemodeling.report;

import org.cheetahplatform.literatemodeling.model.LiterateModel;
import org.cheetahplatform.literatemodeling.model.MultiElementLiterateModelingAssociation;
import org.cheetahplatform.literatemodeling.model.NodeLiterateModelingAssociation;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         02.06.2010
 */
public class GraphElementRendererFactory {

	public NodeLiterateModelingAssociationGraphRenderer createNodeAssociationRenderer(NodeLiterateModelingAssociation association,
			LiterateModel model) {
		return new NodeLiterateModelingAssociationGraphRenderer(association, model);
	}

	/**
	 * @param edgeLiterateModelingAssociation
	 * @param model
	 * @return
	 */
	public IGraphElementRenderer createEdgeAssociationRenderer(EdgeLiterateModelingAssociation association, LiterateModel model) {
		return new EdgeLiterateModelingAssociationGraphRenderer(model, association);
	}

	/**
	 * @param multiElementLiterateModelingAssociation
	 * @param model
	 */
	public IGraphElementRenderer createMultiElementGraphElementRenderer(
			MultiElementLiterateModelingAssociation multiElementLiterateModelingAssociation, LiterateModel model) {
		return new MultiElementGraphRenderer(multiElementLiterateModelingAssociation, model);
	}
}
