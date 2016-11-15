package org.cheetahplatform.literatemodeling.report.html;

import org.cheetahplatform.literatemodeling.report.TextReportElement;
import org.eclipse.core.runtime.Assert;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         12.05.2010
 */
public class TextReportElementHtmlRenderer extends AbstractReportElementHtmlRenderer {

	private final TextReportElement element;

	/**
	 * @param element
	 */
	public TextReportElementHtmlRenderer(TextReportElement element) {
		Assert.isNotNull(element);
		this.element = element;
	}

	@Override
	public void render(StringBuilder builder) {
		builder.append("<h3>");
		builder.append(element.getTitle());
		builder.append("</h3>");
		builder.append("<pre>");
		builder.append(element.getText());
		builder.append("</pre>");
	}
}
