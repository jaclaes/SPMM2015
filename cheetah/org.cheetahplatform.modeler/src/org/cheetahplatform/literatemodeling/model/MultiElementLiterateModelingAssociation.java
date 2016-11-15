package org.cheetahplatform.literatemodeling.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cheetahplatform.literatemodeling.report.GraphElementRendererFactory;
import org.cheetahplatform.literatemodeling.report.IGraphElementRenderer;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.text.ITextSelection;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         02.06.2010
 */
public class MultiElementLiterateModelingAssociation extends AbstractLiterateModelingAssociation {
	private final String name;
	private final List<GraphElement> elements;

	/**
	 * @param textSelection
	 * @param selectedElements
	 */
	public MultiElementLiterateModelingAssociation(String name, int offset, int length, List<GraphElement> selectedElements) {
		super(offset, length);
		Assert.isNotNull(name);
		Assert.isLegal(!name.trim().isEmpty());
		this.name = name;
		Assert.isNotNull(selectedElements);
		this.elements = selectedElements;
	}

	/**
	 * @param textSelection
	 * @param selectedElements
	 */
	public MultiElementLiterateModelingAssociation(String name, ITextSelection textSelection, List<GraphElement> selectedElements) {
		super(textSelection);
		Assert.isNotNull(name);
		Assert.isLegal(!name.trim().isEmpty());
		this.name = name;
		Assert.isNotNull(selectedElements);
		this.elements = selectedElements;
	}

	@Override
	public IGraphElementRenderer createGraphElementRenderer(GraphElementRendererFactory factory, LiterateModel model) {
		return factory.createMultiElementGraphElementRenderer(this, model);
	}

	public List<Edge> getEdges() {
		List<Edge> edges = new ArrayList<Edge>();
		for (GraphElement element : getGraphElements()) {
			if (element instanceof Edge) {
				edges.add((Edge) element);
			}
		}
		return edges;
	}

	/**
	 * Returns the elements.
	 * 
	 * @return the elements
	 */
	@Override
	public List<GraphElement> getGraphElements() {
		return Collections.unmodifiableList(elements);
	}

	/**
	 * Returns the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	public List<Node> getNodes() {
		List<Node> nodes = new ArrayList<Node>();
		for (GraphElement element : getGraphElements()) {
			if (element instanceof Node) {
				nodes.add((Node) element);
			}
		}
		return nodes;
	}

	@Override
	public boolean isSingleLiterateModelingAssociation() {
		return false;
	}

	@Override
	public boolean matches(GraphElement element) {
		return elements.contains(element);
	}

	@Override
	public void removeGraphElement(GraphElement graphElement) {
		elements.remove(graphElement);
	}
}
