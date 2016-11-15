/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.literatemodeling;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.List;

import org.cheetahplatform.literatemodeling.report.ProcessReport;
import org.cheetahplatform.literatemodeling.report.ProcessReportSection;
import org.eclipse.core.runtime.AssertionFailedException;
import org.junit.Test;

public class ProcessReportTest {

	@Test
	public void addSection() {
		ProcessReport processReport = new ProcessReport("report");
		ProcessReportSection section = processReport.createSection("section1");

		List<ProcessReportSection> sections = processReport.getSections();

		assertEquals(1, sections.size());
		assertSame(section, sections.get(0));
	}

	@Test(expected = IllegalArgumentException.class)
	public void createIllegalReportEmptyString() {
		new ProcessReport(" ");
	}

	@Test(expected = AssertionFailedException.class)
	public void createIllegalReportNull() {
		new ProcessReport(null);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void tryToAddElementToSections() {
		new ProcessReport("report").getSections().add(new ProcessReportSection("section"));
	}
}
