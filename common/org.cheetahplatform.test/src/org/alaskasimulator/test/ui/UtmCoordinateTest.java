/*******************************************************************************
* This file is provided under the terms of the Eclipse Public License 1.0. 
* Any use, reproduction or distribution of the program
* constitutes recipient's acceptance of this agreement.
*******************************************************************************/
package org.alaskasimulator.test.ui;

import org.alaskasimulator.core.buildtime.UtmCoordinate;
import org.eclipse.core.runtime.AssertionFailedException;
import org.junit.Test;

public class UtmCoordinateTest {
	@Test(expected = AssertionFailedException.class)
	public void illegalZoneTooLow() {
		new UtmCoordinate(-1, 500000, 0);
	}

	@Test(expected = AssertionFailedException.class)
	public void illegalZoneTooHigh() {
		new UtmCoordinate(61, 500000, 0);
	}

	@Test(expected = AssertionFailedException.class)
	public void illegalEastingTooLow() {
		new UtmCoordinate(60, 165000, 0);
	}

	@Test(expected = AssertionFailedException.class)
	public void illegalEastingTooHigh() {
		new UtmCoordinate(60, 834000, 0);
	}

}
