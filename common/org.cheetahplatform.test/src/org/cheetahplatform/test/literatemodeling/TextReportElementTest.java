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
import org.cheetahplatform.literatemodeling.report.TextReportElement;
import org.eclipse.core.runtime.AssertionFailedException;
import org.junit.Test;

public class TextReportElementTest {

	@Test
	public void collectElements() {
		IReportElement reportElement = new TextReportElement("title", "text");
		List<IReportElement> elements = new ArrayList<IReportElement>();
		reportElement.collectElements(elements);
		assertEquals(1, elements.size());
		assertSame(reportElement, elements.get(0));
	}

	@Test(expected = IllegalArgumentException.class)
	public void createElementEmptyTitle() {
		new TextReportElement(" ", "string");
	}

	@Test(expected = AssertionFailedException.class)
	public void createElementNullText() {
		new TextReportElement("title", null);
	}

	@Test(expected = AssertionFailedException.class)
	public void createElementNullTitle() {
		new TextReportElement(null, "String");
	}
}
