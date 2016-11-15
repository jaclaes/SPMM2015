/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.alaskasimulator.test.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.cheetahplatform.common.AssertionFailedException;
import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.junit.Test;

public class AuditTrailEntryTest {
	@Test
	public void containsAttribute() {
		AuditTrailEntry entry = new AuditTrailEntry();
		String name = "My Attribute";
		String value = "Some string value";
		assertFalse("Should not contain any attributes.", entry
				.isAttributeDefined(name));

		entry.setAttribute(name, value);
		assertTrue("Should contain this attribute now.", entry
				.isAttributeDefined(name));
	}

	@Test
	public void getAttributes() {
		AuditTrailEntry entry = new AuditTrailEntry();
		String name1 = "My Attribute1";
		String value1 = "Some string value";
		String name2 = "My Attribute2";
		String value2 = "Some string value";

		entry.setAttribute(name1, value1);
		entry.setAttribute(name2, value2);

		assertEquals("Wrong attribute amount.", 2, entry.getAttributes().size());

		for (Attribute attribute : entry.getAttributes()) {
			if (attribute.getName().equals(name1)) {
				assertEquals("Wrong content.", value1, attribute.getContent());
			} else if (attribute.getName().equals(name2)) {
				assertEquals("Wrong content.", value2, attribute.getContent());
			} else {
				fail("Unknown attribute:" + attribute.getName());
			}
		}
	}

	@Test
	public void getIntegerAttribute() {
		AuditTrailEntry entry = new AuditTrailEntry();
		String name = "name";
		entry.setAttribute(name, "2");

		int value = entry.getIntegerAttribute(name);
		assertEquals("Wrong content.", 2, value);
	}

	@Test(expected = AssertionFailedException.class)
	public void getIntegerAttributeFail() {
		AuditTrailEntry entry = new AuditTrailEntry();
		String name = "name";
		entry.setAttribute(name, "2_");

		entry.getIntegerAttribute(name);
	}

}
