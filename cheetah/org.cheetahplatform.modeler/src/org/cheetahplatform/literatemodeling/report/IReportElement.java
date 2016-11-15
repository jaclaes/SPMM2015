package org.cheetahplatform.literatemodeling.report;

import java.util.List;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         12.05.2010
 */
public interface IReportElement {
	void collectElements(List<IReportElement> elements);

	IReportElementRenderer createRenderer(IReportElementRendererFactory factory);
}
