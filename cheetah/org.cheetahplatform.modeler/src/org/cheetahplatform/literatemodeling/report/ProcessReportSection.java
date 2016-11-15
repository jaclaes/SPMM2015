package org.cheetahplatform.literatemodeling.report;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.Assert;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         11.05.2010
 */
public class ProcessReportSection implements IReportElement {

	private final String name;
	private final List<IReportElement> elements;

	/**
	 * @param sectionName
	 */
	public ProcessReportSection(String sectionName) {
		Assert.isNotNull(sectionName);
		Assert.isLegal(!sectionName.trim().isEmpty());
		this.name = sectionName;
		elements = new ArrayList<IReportElement>();
	}

	/**
	 * Returns the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	public void addElement(IReportElement element) {
		Assert.isNotNull(element);
		elements.add(element);
	}

	/**
	 * Returns the elements.
	 * 
	 * @return the elements
	 */
	public List<IReportElement> getElements() {
		return Collections.unmodifiableList(elements);
	}

	@Override
	public void collectElements(List<IReportElement> list) {
		list.add(this);
		for (IReportElement element : elements) {
			element.collectElements(list);
		}
	}

	@Override
	public IReportElementRenderer createRenderer(IReportElementRendererFactory factory) {
		return factory.createReportSectionRenderer(this);
	}
}
