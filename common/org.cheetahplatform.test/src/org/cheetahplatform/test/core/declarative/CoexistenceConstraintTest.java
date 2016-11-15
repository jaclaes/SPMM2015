/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.core.declarative;

import org.cheetahplatform.common.AssertionFailedException;
import org.cheetahplatform.core.declarative.constraint.CoexistenceConstraint;
import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.junit.Test;

public class CoexistenceConstraintTest {

	@Test(expected = AssertionFailedException.class)
	public void constructorFailNull1() throws Exception {
		new CoexistenceConstraint(null, new DeclarativeActivity("A"));
	}

	@Test(expected = AssertionFailedException.class)
	public void constructorFailNull2() throws Exception {
		new CoexistenceConstraint(new DeclarativeActivity("A"), null);
	}

	@Test(expected = AssertionFailedException.class)
	public void constructorFailNull3() throws Exception {
		new CoexistenceConstraint(null, null);
	}

}
