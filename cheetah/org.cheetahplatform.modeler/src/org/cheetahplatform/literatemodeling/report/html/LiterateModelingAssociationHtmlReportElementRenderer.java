package org.cheetahplatform.literatemodeling.report.html;

import org.cheetahplatform.literatemodeling.report.LiterateModelingAssociationReportElement;
import org.eclipse.core.runtime.Assert;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         14.05.2010
 */
public class LiterateModelingAssociationHtmlReportElementRenderer extends AbstractReportElementHtmlRenderer {

	private final LiterateModelingAssociationReportElement element;

	public LiterateModelingAssociationHtmlReportElementRenderer(LiterateModelingAssociationReportElement element) {
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

		String imageName = "association" + System.currentTimeMillis() + ".png";
		saveImage(imageName, element.getGraph());
		addImageToHtml(builder, imageName);
	}
}
