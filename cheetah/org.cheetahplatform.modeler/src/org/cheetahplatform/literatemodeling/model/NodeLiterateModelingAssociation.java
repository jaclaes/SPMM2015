package org.cheetahplatform.literatemodeling.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cheetahplatform.literatemodeling.report.GraphElementRendererFactory;
import org.cheetahplatform.literatemodeling.report.IGraphElementRenderer;
import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.text.ITextSelection;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         22.04.2010
 */
public class NodeLiterateModelingAssociation extends AbstractSingleElementLiterateModelingAssociation {
	private final Node node;

	/**
	 * Returns the graphElement.
	 * 
	 * @return the graphElement
	 */
	@Override
	public Node getGraphElement() {
		return node;
	}

	public NodeLiterateModelingAssociation(ITextSelection selection, Node graphElement) {
		this(selection.getOffset(), selection.getLength(), graphElement);
	}

	public NodeLiterateModelingAssociation(int offset, int length, Node graphElement) {
		super(offset, length);
		Assert.isNotNull(graphElement);
		this.node = graphElement;
	}

	@Override
	public IGraphElementRenderer createGraphElementRenderer(GraphElementRendererFactory graphElementRendererFactory, LiterateModel model) {
		return graphElementRendererFactory.createNodeAssociationRenderer(this, model);
	}

	@Override
	public List<GraphElement> getGraphElements() {
		List<GraphElement> nodes = new ArrayList<GraphElement>();
		nodes.add(node);
		return Collections.unmodifiableList(nodes);
	}
}
