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
import org.cheetahplatform.literatemodeling.report.LiterateModelingAssociationReportElement;
import org.eclipse.core.runtime.AssertionFailedException;
import org.eclipse.swt.graphics.Image;
import org.junit.Test;

public class LiterateModelingAssociationReportElementTest {
	@Test
	public void collectElements() {
		IReportElement reportElement = new LiterateModelingAssociationReportElement("title", "text", new Image(null, 100, 100));
		List<IReportElement> elements = new ArrayList<IReportElement>();
		reportElement.collectElements(elements);
		assertEquals(1, elements.size());
		assertSame(reportElement, elements.get(0));
	}

	@Test(expected = AssertionFailedException.class)
	public void createIllegalElementNullImage() {
		new LiterateModelingAssociationReportElement("t", "t", null);
	}

	@Test(expected = AssertionFailedException.class)
	public void createIllegalElementNullText() {
		new LiterateModelingAssociationReportElement("t", null, new Image(null, 100, 100));
	}

	@Test(expected = AssertionFailedException.class)
	public void createIllegalElementNullTitle() {
		new LiterateModelingAssociationReportElement(null, "t", new Image(null, 100, 100));
	}
}
