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
import org.cheetahplatform.modeler.graph.export.LikertValueReplacement;
import org.junit.Before;
import org.junit.Test;

public class LikertValueReplacementTest {
	private LikertValueReplacement likertValueReplacement;

	@Test
	public void applies() {
		Attribute attribute = new Attribute("Overall, I am very familiar with the BPMN.", "strongly agree");
		Attribute attribute2 = new Attribute("Overall, I am very familiar with the BPMN. ", "strongly agree");
		Attribute attribute3 = new Attribute(" Overall, I am very familiar with the BPMN.", "strongly agree");
		Attribute attribute4 = new Attribute("Overall, I am the BPMN.", "strongly agree");

		assertTrue(likertValueReplacement.applies(attribute));
		assertTrue(likertValueReplacement.applies(attribute2));
		assertTrue(likertValueReplacement.applies(attribute3));
		assertFalse(likertValueReplacement.applies(attribute4));
	}

	@Test
	public void getReplacement() {
		Attribute attribute = new Attribute("Overall, I am very familiar with the BPMN.", "strongly agree");
		Attribute attribute2 = new Attribute("Overall, I am very familiar with the BPMN.", "agree");
		Attribute attribute3 = new Attribute("Overall, I am very familiar with the BPMN.", "strongly disagree");

		assertEquals("7", likertValueReplacement.getReplacedContent(attribute));
		assertEquals("6", likertValueReplacement.getReplacedContent(attribute2));
		assertEquals("1", likertValueReplacement.getReplacedContent(attribute3));
	}

	@Before
	public void setUp() {
		likertValueReplacement = new LikertValueReplacement("Overall, I am very familiar with the BPMN.");
		likertValueReplacement.addValue("strongly agree", 7);
		likertValueReplacement.addValue("agree", 6);
		likertValueReplacement.addValue("somewhat agree", 5);
		likertValueReplacement.addValue("neutral", 4);
		likertValueReplacement.addValue("somewhat disagree", 3);
		likertValueReplacement.addValue("disagree", 2);
		likertValueReplacement.addValue("strongly disagree", 1);
	}
}
