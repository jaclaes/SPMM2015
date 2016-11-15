/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.modeler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.modeler.graph.export.TimeStampValueReplacement;
import org.junit.Before;
import org.junit.Test;

public class TimeStampValueReplacementTest {

	private TimeStampValueReplacement valueReplacement;

	@Test
	public void applies() {
		Attribute attribute = new Attribute(CommonConstants.ATTRIBUTE_TIMESTAMP, "");
		Attribute attribute2 = new Attribute("somethingElse", "");
		Attribute attribute3 = new Attribute("", "");
		Attribute attribute4 = new Attribute(CommonConstants.ATTRIBUTE_TIMESTAMP + " ", "");

		assertTrue(valueReplacement.applies(attribute));
		assertTrue(valueReplacement.applies(attribute4));
		assertFalse(valueReplacement.applies(attribute2));
		assertFalse(valueReplacement.applies(attribute3));
	}

	@Test
	public void replaceContent() {
		Attribute attribute = new Attribute(CommonConstants.ATTRIBUTE_TIMESTAMP, "1257776984853");

		Date actual = new Date(1257776984853l);
		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

		assertEquals(valueReplacement.getReplacedContent(attribute), format.format(actual));
	}

	@Before
	public void setUp() {
		valueReplacement = new TimeStampValueReplacement();
	}

}
