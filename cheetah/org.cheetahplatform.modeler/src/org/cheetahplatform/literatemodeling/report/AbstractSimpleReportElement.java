package org.cheetahplatform.literatemodeling.report;

import java.util.List;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         12.05.2010
 */
public abstract class AbstractSimpleReportElement implements IReportElement {
	@Override
	public void collectElements(List<IReportElement> elements) {
		elements.add(this);
	}
}