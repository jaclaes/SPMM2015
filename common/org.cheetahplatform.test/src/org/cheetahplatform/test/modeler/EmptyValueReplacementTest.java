/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.modeler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.modeler.graph.export.EmptyValueReplacement;
import org.junit.Before;
import org.junit.Test;

public class EmptyValueReplacementTest {

	private EmptyValueReplacement valueReplacement;

	@Test
	public void applies() {
		Attribute attribute = new Attribute("someKey", "");
		Attribute attribute2 = new Attribute("someKey", " ");
		Attribute attribute3 = new Attribute("someKey", "   ");
		Attribute attribute4 = new Attribute("someKey", "value");
		Attribute attribute5 = new Attribute("someKey", " value ");
		Attribute attribute6 = new Attribute("someKey", null);

		assertTrue(valueReplacement.applies(attribute));
		assertTrue(valueReplacement.applies(attribute2));
		assertTrue(valueReplacement.applies(attribute3));
		assertTrue(valueReplacement.applies(attribute6));
		assertFalse(valueReplacement.applies(attribute4));
		assertFalse(valueReplacement.applies(attribute5));
	}

	@Test
	public void replace() {
		Attribute attribute = new Attribute("someKey", "");
		Attribute attribute2 = new Attribute("someKey", " ");
		Attribute attribute3 = new Attribute("someKey", "   ");
		Attribute attribute4 = new Attribute("someKey", null);

		assertEquals(String.valueOf(0), valueReplacement.getReplacedContent(attribute));
		assertEquals(String.valueOf(0), valueReplacement.getReplacedContent(attribute2));
		assertEquals(String.valueOf(0), valueReplacement.getReplacedContent(attribute3));
		assertEquals(String.valueOf(0), valueReplacement.getReplacedContent(attribute4));
	}

	@Before
	public void setUp() {
		valueReplacement = new EmptyValueReplacement();
	}
}
