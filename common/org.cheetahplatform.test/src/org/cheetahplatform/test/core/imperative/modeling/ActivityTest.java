/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.core.imperative.modeling;

import static org.junit.Assert.assertEquals;
import junit.framework.Assert;

import org.cheetahplatform.common.AssertionFailedException;
import org.cheetahplatform.core.imperative.modeling.AbstractNode;
import org.cheetahplatform.core.imperative.modeling.ImperativeActivity;
import org.cheetahplatform.core.imperative.modeling.routing.AndSplit;
import org.junit.Test;

public class ActivityTest {
	@Test
	public void addNode() throws Exception {
		AbstractNode activity = new ImperativeActivity("A");
		activity.addSuccessor(new AndSplit());

		Assert.assertEquals(1, activity.getSuccessors().size());
	}

	@Test(expected = AssertionFailedException.class)
	public void addNodeFail() throws Exception {
		AbstractNode activity = new ImperativeActivity("A");
		activity.addSuccessor(new AndSplit());
		activity.addSuccessor(new AndSplit());
	}

	@Test
	public void checkPredecessor() throws Exception {
		AbstractNode activityA = new ImperativeActivity("A");
		AbstractNode activityB = new ImperativeActivity("B");

		assertEquals(0, activityB.getPredecessors().size());
		activityA.addSuccessor(activityB);
		assertEquals(1, activityB.getPredecessors().size());

	}
}
