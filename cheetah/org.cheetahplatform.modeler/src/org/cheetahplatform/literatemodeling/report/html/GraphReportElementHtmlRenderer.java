package org.cheetahplatform.literatemodeling.report.html;

import org.cheetahplatform.literatemodeling.report.GraphReportElement;
import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.graphics.Image;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         12.05.2010
 */
public class GraphReportElementHtmlRenderer extends AbstractReportElementHtmlRenderer {
	private final GraphReportElement element;

	/**
	 * @param element
	 */
	public GraphReportElementHtmlRenderer(GraphReportElement element) {
		Assert.isNotNull(element);
		this.element = element;
	}

	@Override
	public void render(StringBuilder builder) {
		builder.append("<h3>" + element.getTitle() + "</h3><br>");
		Image image = element.getGraph();
		String imageName = String.valueOf(System.currentTimeMillis()) + ".png";
		saveImage(imageName, image);
		addImageToHtml(builder, imageName);
	}
}
