package org.cheetahplatform.literatemodeling.report.html;

import org.cheetahplatform.literatemodeling.report.ProcessReportSection;
import org.eclipse.core.runtime.Assert;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         12.05.2010
 */
public class ReportSectionHtmlRenderer extends AbstractReportElementHtmlRenderer {

	private final ProcessReportSection element;

	/**
	 * @param element
	 */
	public ReportSectionHtmlRenderer(ProcessReportSection element) {
		Assert.isNotNull(element);
		this.element = element;

	}

	@Override
	public void render(StringBuilder builder) {
		builder.append("<h2>");
		builder.append(element.getName());
		builder.append("</h2>");
	}

}
