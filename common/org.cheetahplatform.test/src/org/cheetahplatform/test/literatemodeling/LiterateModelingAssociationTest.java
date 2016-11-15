/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.literatemodeling;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.cheetahplatform.literatemodeling.model.ILiterateModelingAssociation;
import org.cheetahplatform.literatemodeling.model.NodeLiterateModelingAssociation;
import org.eclipse.core.runtime.AssertionFailedException;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Color;
import org.junit.Test;

public class LiterateModelingAssociationTest {

	@Test
	public void decreaseLength() {
		ILiterateModelingAssociation association = new NodeLiterateModelingAssociation(15, 10, new DummyNode());
		association.decreaseLength(8);
		assertEquals(2, association.getLength());
	}

	@Test(expected = IllegalArgumentException.class)
	public void decreaseLengthIllegalLenght() {
		ILiterateModelingAssociation association = new NodeLiterateModelingAssociation(15, 10, new DummyNode());
		association.decreaseLength(-1);
	}

	@Test
	public void decreaseOffset() {
		ILiterateModelingAssociation association = new NodeLiterateModelingAssociation(15, 10, new DummyNode());
		association.decreaseOffset(10);
		assertEquals(5, association.getOffset());
		assertEquals(10, association.getLength());
	}

	@Test(expected = IllegalArgumentException.class)
	public void decreaseOffsetIllegalLength() {
		ILiterateModelingAssociation association = new NodeLiterateModelingAssociation(15, 10, new DummyNode());
		association.decreaseOffset(-1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void decreaseOffsetTooLongLength() {
		ILiterateModelingAssociation association = new NodeLiterateModelingAssociation(15, 10, new DummyNode());
		association.decreaseOffset(15);
		association.decreaseOffset(16);
	}

	@Test
	public void getEnd() {
		ILiterateModelingAssociation association = new NodeLiterateModelingAssociation(15, 10, new DummyNode());
		assertEquals(25, association.getEnd());
	}

	@Test
	public void getStyleRange() {
		DummyNode element = new DummyNode();
		ILiterateModelingAssociation association = new NodeLiterateModelingAssociation(15, 7, element);
		Color foreground = new Color(null, 255, 0, 0);
		Color background = new Color(null, 255, 0, 255);

		StyleRange styleRange = association.getStyleRange(foreground, background);
		assertEquals(foreground, styleRange.foreground);
		assertEquals(background, styleRange.background);
		assertEquals(15, styleRange.start);
		assertEquals(7, styleRange.length);

		foreground.dispose();
		background.dispose();
	}

	@Test(expected = AssertionFailedException.class)
	public void graphElementMustNotBeNull() {
		new NodeLiterateModelingAssociation(5, 5, null);
	}

	@Test
	public void increaseLength() {
		ILiterateModelingAssociation association = new NodeLiterateModelingAssociation(15, 10, new DummyNode());
		association.increaseLength(20);
		assertEquals(30, association.getLength());
	}

	@Test(expected = IllegalArgumentException.class)
	public void increaseLengthIllegalLength() {
		ILiterateModelingAssociation association = new NodeLiterateModelingAssociation(15, 10, new DummyNode());
		association.increaseLength(-1);
	}

	@Test
	public void isAfter() {
		DummyNode element = new DummyNode();
		ILiterateModelingAssociation association = new NodeLiterateModelingAssociation(15, 10, element);
		assertFalse(association.isAfter(7));
		assertFalse(association.isAfter(14));
		assertFalse(association.isAfter(15));
		assertFalse(association.isAfter(24));
		assertTrue(association.isAfter(25));
		assertTrue(association.isAfter(30));
	}

	@Test
	public void isBefore() {
		DummyNode element = new DummyNode();
		ILiterateModelingAssociation association = new NodeLiterateModelingAssociation(15, 7, element);
		assertTrue(association.isBefore(7));
		assertTrue(association.isBefore(14));
		assertFalse(association.isBefore(15));
		assertFalse(association.isBefore(21));
		assertFalse(association.isBefore(30));
	}

	@Test
	public void matchesGraphElement() {
		DummyNode element = new DummyNode();
		ILiterateModelingAssociation association = new NodeLiterateModelingAssociation(15, 7, element);
		assertTrue(association.matches(element));
		assertFalse(association.matches(new DummyNode()));
		assertFalse(association.matches(null));
	}

	@Test
	public void moveBack() {
		DummyNode element = new DummyNode();
		ILiterateModelingAssociation association = new NodeLiterateModelingAssociation(15, 7, element);
		association.increaseOffset(5);
		assertEquals(20, association.getOffset());
		assertEquals(7, association.getLength());
	}

	@Test(expected = IllegalArgumentException.class)
	public void moveBackIllegalIndex() {
		DummyNode element = new DummyNode();
		ILiterateModelingAssociation association = new NodeLiterateModelingAssociation(15, 7, element);
		association.increaseOffset(-1);
	}

	@Test(expected = NullPointerException.class)
	public void selectionMustNotBeNull() {
		new NodeLiterateModelingAssociation(null, new DummyNode());
	}
}
