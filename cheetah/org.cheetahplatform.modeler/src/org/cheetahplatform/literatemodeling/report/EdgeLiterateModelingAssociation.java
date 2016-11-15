package org.cheetahplatform.literatemodeling.report;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cheetahplatform.literatemodeling.model.AbstractSingleElementLiterateModelingAssociation;
import org.cheetahplatform.literatemodeling.model.LiterateModel;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.text.ITextSelection;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         02.06.2010
 */
public class EdgeLiterateModelingAssociation extends AbstractSingleElementLiterateModelingAssociation {
	private final Edge edge;

	/**
	 * Returns the graphElement.
	 * 
	 * @return the graphElement
	 */
	@Override
	public Edge getGraphElement() {
		return edge;
	}

	@Override
	public List<GraphElement> getGraphElements() {
		List<GraphElement> edges = new ArrayList<GraphElement>();
		edges.add(edge);
		return Collections.unmodifiableList(edges);
	}

	public EdgeLiterateModelingAssociation(ITextSelection selection, Edge graphElement) {
		super(selection);
		Assert.isNotNull(graphElement);
		this.edge = graphElement;
	}

	public EdgeLiterateModelingAssociation(int offset, int length, Edge graphElement) {
		super(offset, length);
		Assert.isNotNull(graphElement);
		this.edge = graphElement;
	}

	@Override
	public IGraphElementRenderer createGraphElementRenderer(GraphElementRendererFactory factory, LiterateModel model) {
		return factory.createEdgeAssociationRenderer(this, model);
	}
}
