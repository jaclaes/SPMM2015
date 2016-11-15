/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.literatemodeling;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.literatemodeling.report.IReportElement;
import org.cheetahplatform.literatemodeling.report.ProcessReportSection;
import org.cheetahplatform.literatemodeling.report.TextReportElement;
import org.eclipse.core.runtime.AssertionFailedException;
import org.junit.Test;

public class ProcessReportSectionTest {

	@Test
	public void addElement() {
		ProcessReportSection processReportSection = new ProcessReportSection("section");
		TextReportElement textReportElement = new TextReportElement("title", "text");
		processReportSection.addElement(textReportElement);
		List<IReportElement> elements = processReportSection.getElements();
		assertEquals(1, elements.size());
		assertSame(textReportElement, elements.get(0));
	}

	@Test
	public void collectElements() {
		ProcessReportSection section = new ProcessReportSection("section");
		TextReportElement element = new TextReportElement("t", "t");
		section.addElement(element);
		List<IReportElement> elements = new ArrayList<IReportElement>();
		section.collectElements(elements);

		assertEquals(2, elements.size());
		assertSame(section, elements.get(0));
		assertSame(element, elements.get(1));
	}

	@Test
	public void collectElementsOnSeveralLevels() {
		ProcessReportSection section = new ProcessReportSection("section");
		ProcessReportSection subsection = new ProcessReportSection("subsection");
		section.addElement(subsection);
		TextReportElement element = new TextReportElement("t", "t");
		subsection.addElement(element);
		List<IReportElement> elements = new ArrayList<IReportElement>();
		section.collectElements(elements);

		assertEquals(3, elements.size());
		assertSame(section, elements.get(0));
		assertSame(subsection, elements.get(1));
		assertSame(element, elements.get(2));
	}

	@Test(expected = IllegalArgumentException.class)
	public void createSectionWithEmptyName() {
		new ProcessReportSection(" ");
	}

	@Test(expected = AssertionFailedException.class)
	public void createSectionWithNullName() {
		new ProcessReportSection(null);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void tryToModifyElementListIllegaly() {
		ProcessReportSection processReportSection = new ProcessReportSection("section");
		processReportSection.getElements().add(new TextReportElement("t", "t"));
	}

}
