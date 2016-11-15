package org.cheetahplatform.literatemodeling.report.html;

import org.cheetahplatform.literatemodeling.report.GraphReportElement;
import org.cheetahplatform.literatemodeling.report.IReportElementRenderer;
import org.cheetahplatform.literatemodeling.report.IReportElementRendererFactory;
import org.cheetahplatform.literatemodeling.report.LiterateModelingAssociationReportElement;
import org.cheetahplatform.literatemodeling.report.ProcessReportSection;
import org.cheetahplatform.literatemodeling.report.TextReportElement;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         12.05.2010
 */
public class HtmlReportElementRenderFactory implements IReportElementRendererFactory {

	@Override
	public IReportElementRenderer createGraphReportElementRenderer(GraphReportElement element) {
		return new GraphReportElementHtmlRenderer(element);
	}

	@Override
	public IReportElementRenderer createLiterateModelingAssociationReportElementRenderer(LiterateModelingAssociationReportElement element) {
		return new LiterateModelingAssociationHtmlReportElementRenderer(element);
	}

	@Override
	public IReportElementRenderer createReportSectionRenderer(ProcessReportSection element) {
		return new ReportSectionHtmlRenderer(element);
	}

	@Override
	public IReportElementRenderer createTextReportElementRenderer(TextReportElement element) {
		return new TextReportElementHtmlRenderer(element);
	}

}
