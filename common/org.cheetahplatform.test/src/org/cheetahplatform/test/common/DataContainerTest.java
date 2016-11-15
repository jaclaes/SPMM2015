/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.common;

import static org.junit.Assert.assertEquals;

import org.cheetahplatform.common.AssertionFailedException;
import org.cheetahplatform.common.logging.DataContainer;
import org.junit.Test;

public class DataContainerTest {

	@Test
	public void getAttributeSafely() throws Exception {
		DataContainer container = new DataContainer();
		String attribute = container.getAttributeSafely("undefined Attribute");
		assertEquals("", attribute);
	}

	@Test(expected = AssertionFailedException.class)
	public void getUndefinedAttribute() throws Exception {
		DataContainer container = new DataContainer();
		container.getAttribute("undefined Attribute");
	}

}
