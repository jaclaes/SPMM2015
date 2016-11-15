/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.literatemodeling;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;

import org.cheetahplatform.literatemodeling.report.AbstractSimpleReportElement;
import org.cheetahplatform.literatemodeling.report.GraphReportElement;
import org.cheetahplatform.literatemodeling.report.IReportElement;
import org.eclipse.core.runtime.AssertionFailedException;
import org.eclipse.swt.graphics.Image;
import org.junit.Test;

public class GraphReportElementTest {
	@Test
	public void collectElements() {
		AbstractSimpleReportElement graphReportElement = new GraphReportElement(new Image(null, 100, 100), "title");
		ArrayList<IReportElement> elements = new ArrayList<IReportElement>();
		graphReportElement.collectElements(elements);
		assertEquals(1, elements.size());
		assertSame(graphReportElement, elements.get(0));
	}

	@Test(expected = IllegalArgumentException.class)
	public void createWithEmptyTitle() {
		new GraphReportElement(new Image(null, 100, 100), " ");
	}

	@Test(expected = AssertionFailedException.class)
	public void createWithNullImage() {
		new GraphReportElement(null, "title");
	}

	@Test(expected = AssertionFailedException.class)
	public void createWithNullTitle() {
		new GraphReportElement(new Image(null, 100, 100), null);
	}
}
