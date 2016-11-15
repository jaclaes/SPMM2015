/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.literatemodeling.report;

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.graphics.Image;

public class GraphReportElement extends AbstractSimpleReportElement  {
	private final Image graph;
	private final String title;

	/**
	 * @param graph
	 * @param title
	 */
	public GraphReportElement(Image graph, String title) {
		Assert.isNotNull(graph);
		Assert.isNotNull(title);
		Assert.isLegal(!title.trim().isEmpty());
		this.graph = graph;
		this.title = title;
	}

	/**
	 * Return the graph.
	 * 
	 * @return the graph
	 */
	public Image getGraph() {
		return graph;
	}

	/**
	 * Return the title.
	 * 
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	@Override
	public IReportElementRenderer createRenderer(IReportElementRendererFactory factory) {
		return factory.createGraphReportElementRenderer(this);
	}
}
