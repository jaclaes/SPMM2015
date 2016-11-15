package org.cheetahplatform.literatemodeling.report;

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.graphics.Image;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         11.05.2010
 */
public class LiterateModelingAssociationReportElement extends AbstractSimpleReportElement {
	private final String title;
	private final String text;
	private final Image graph;

	/**
	 * Returns the title.
	 * 
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Returns the text.
	 * 
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text
	 * @param graph
	 */
	public LiterateModelingAssociationReportElement(String title, String text, Image graph) {
		Assert.isNotNull(title);
		Assert.isNotNull(text);
		Assert.isNotNull(graph);
		this.title = title;
		this.text = text;
		this.graph = graph;
	}

	/**
	 * Returns the graph.
	 * 
	 * @return the graph
	 */
	public Image getGraph() {
		return graph;
	}

	@Override
	public IReportElementRenderer createRenderer(IReportElementRendererFactory factory) {
		return factory.createLiterateModelingAssociationReportElementRenderer(this);
	}

}
