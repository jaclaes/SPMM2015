package org.cheetahplatform.literatemodeling.report;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         12.05.2010
 */
public interface IReportElementRendererFactory {

	/**
	 * @param graphReportElement
	 * @return
	 */
	IReportElementRenderer createGraphReportElementRenderer(GraphReportElement element);

	/**
	 * @return
	 */
	IReportElementRenderer createLiterateModelingAssociationReportElementRenderer(LiterateModelingAssociationReportElement element);

	/**
	 * @return
	 */
	IReportElementRenderer createReportSectionRenderer(ProcessReportSection element);

	/**
	 * @return
	 */
	IReportElementRenderer createTextReportElementRenderer(TextReportElement element);

}
