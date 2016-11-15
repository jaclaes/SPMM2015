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
public class ProcessReport {

	private final String name;
	private String shortDescription;
	private final List<ProcessReportSection> sections;

	/**
	 * Sets the shortDescription.
	 * 
	 * @param shortDescription
	 *            the shortDescription to set
	 */
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	/**
	 * Returns the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the shortDescription.
	 * 
	 * @return the shortDescription
	 */
	public String getShortDescription() {
		return shortDescription;
	}

	/**
	 * @param name
	 */
	public ProcessReport(String name) {
		Assert.isNotNull(name);
		Assert.isLegal(!name.trim().isEmpty());
		this.name = name;
		sections = new ArrayList<ProcessReportSection>();
	}

	/**
	 * @param string
	 * @return
	 */
	public ProcessReportSection createSection(String sectionName) {
		ProcessReportSection section = new ProcessReportSection(sectionName);
		sections.add(section);
		return section;
	}

	/**
	 * @return
	 * 
	 */
	public List<ProcessReportSection> getSections() {
		return Collections.unmodifiableList(sections);
	}

	/**
	 * @return
	 * 
	 */
	public List<IReportElement> getReportElements() {
		List<IReportElement> elements = new ArrayList<IReportElement>();
		for (ProcessReportSection section : sections) {
			section.collectElements(elements);
		}
		return elements;
	}
}
