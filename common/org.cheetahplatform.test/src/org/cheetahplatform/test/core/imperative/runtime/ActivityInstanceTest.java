/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.core.imperative.runtime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.cheetahplatform.common.AssertionFailedException;
import org.cheetahplatform.core.common.modeling.INodeInstance;
import org.cheetahplatform.core.imperative.modeling.AbstractNode;
import org.cheetahplatform.core.imperative.modeling.ImperativeActivity;
import org.cheetahplatform.core.imperative.modeling.ImperativeProcessSchema;
import org.cheetahplatform.core.imperative.modeling.routing.XorJoin;
import org.cheetahplatform.core.imperative.runtime.IImperativeNodeInstance;
import org.cheetahplatform.core.imperative.runtime.ImperativeActivityInstance;
import org.cheetahplatform.core.imperative.runtime.ImperativeProcessInstance;
import org.cheetahplatform.core.imperative.runtime.routing.AbstractJoinInstance;
import org.cheetahplatform.core.imperative.runtime.routing.XorJoinInstance;
import org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState;
import org.junit.Test;

public class ActivityInstanceTest {
	private final ImperativeProcessInstance instance = new ImperativeProcessInstance(new ImperativeProcessSchema());

	@Test
	public void addPredecessorAutomatically() throws Exception {
		AbstractNode activityA = new ImperativeActivity("A");
		AbstractNode activityB = new ImperativeActivity("B");
		activityA.addSuccessor(activityB);

		IImperativeNodeInstance instanceA = instance.getNodeRegistry().getInstance(activityA);
		instanceA.requestActivation();
		instanceA.launch();
		instanceA.complete();

		IImperativeNodeInstance instanceAFromInstanceBsPredecessors = instanceA.getSuccessors().get(0).getPredecessors().get(0);
		assertSame(instanceA, instanceAFromInstanceBsPredecessors);
	}

	@Test
	public void addPredecessors() throws Exception {
		ImperativeActivity activityA = new ImperativeActivity("A");
		ImperativeActivity activityB = new ImperativeActivity("B");
		XorJoin join = new XorJoin("C");
		ImperativeActivityInstance activityInstanceA1 = new ImperativeActivityInstance(instance, activityA);
		ImperativeActivityInstance activityInstanceA2 = new ImperativeActivityInstance(instance, activityA);
		ImperativeActivityInstance activityInstanceB = new ImperativeActivityInstance(instance, activityB);
		AbstractJoinInstance joinInstance = new XorJoinInstance(instance, join);

		joinInstance.addPredecessor(activityInstanceA1);
		assertEquals(1, joinInstance.getPredecessors().size());
		joinInstance.addPredecessor(activityInstanceA2);
		assertEquals(1, joinInstance.getPredecessors().size());
		joinInstance.addPredecessor(activityInstanceB);
		assertEquals(2, joinInstance.getPredecessors().size());
	}

	@Test
	public void cancel() throws Exception {
		AbstractNode activityA = new ImperativeActivity("A");
		INodeInstance activityInstance = activityA.instantiate(instance);
		activityInstance.requestActivation();
		assertEquals(INodeInstanceState.ACTIVATED, activityInstance.getState());
		activityInstance.launch();
		assertEquals(INodeInstanceState.LAUNCHED, activityInstance.getState());
		activityInstance.cancel();
		assertEquals(INodeInstanceState.ACTIVATED, activityInstance.getState());
	}

	@Test(expected = AssertionFailedException.class)
	public void cancelFail() throws Exception {
		AbstractNode activityA = new ImperativeActivity("A");
		INodeInstance activityInstance = activityA.instantiate(instance);

		activityInstance.requestActivation();
		assertEquals(INodeInstanceState.ACTIVATED, activityInstance.getState());
		activityInstance.launch();
		assertEquals(INodeInstanceState.LAUNCHED, activityInstance.getState());
		activityInstance.complete();
		assertEquals(INodeInstanceState.COMPLETED, activityInstance.getState());
		activityInstance.cancel();
	}

	@Test
	public void checkPredecessor() throws Exception {
		AbstractNode activityA = new ImperativeActivity("A");
		AbstractNode activityB = new ImperativeActivity("B");
		activityA.addSuccessor(activityB);

		IImperativeNodeInstance instanceA = instance.getNodeRegistry().getInstance(activityA);
		IImperativeNodeInstance instanceB = instance.getNodeRegistry().getInstance(activityB);
		instanceA.addPredecessor(instanceB);

		assertEquals(0, instanceB.getPredecessors().size());
		instanceB.addPredecessor(instanceA);
		assertEquals(1, instanceB.getPredecessors().size());
	}

	@Test
	public void complete() throws Exception {
		AbstractNode activityA = new ImperativeActivity("A");
		AbstractNode activityB = new ImperativeActivity("B");
		activityA.addSuccessor(activityB);

		IImperativeNodeInstance instanceA = instance.getNodeRegistry().getInstance(activityA);
		assertEquals(0, instanceA.getSuccessors().size());
		instanceA.requestActivation();
		instanceA.launch();
		instanceA.complete();
		assertEquals(1, instanceA.getSuccessors().size());
		assertEquals(INodeInstanceState.ACTIVATED, instanceA.getSuccessors().get(0).getState());
	}

	@Test
	public void skipAndPropagate() throws Exception {
		AbstractNode activityA = new ImperativeActivity("A");
		AbstractNode activityB = new ImperativeActivity("B");
		activityA.addSuccessor(activityB);

		IImperativeNodeInstance instanceA = instance.getNodeRegistry().getInstance(activityA);
		assertEquals(0, instanceA.getSuccessors().size());
		instanceA.skip(true);
		assertEquals(1, instanceA.getSuccessors().size());
		assertEquals(INodeInstanceState.SKIPPED, instanceA.getSuccessors().get(0).getState());
	}

	@Test
	public void skipWithoutPropagate() throws Exception {
		AbstractNode activityA = new ImperativeActivity("A");
		AbstractNode activityB = new ImperativeActivity("B");
		activityA.addSuccessor(activityB);

		IImperativeNodeInstance instanceA = instance.getNodeRegistry().getInstance(activityA);
		assertEquals(0, instanceA.getSuccessors().size());
		instanceA.skip(false);
		assertEquals(0, instanceA.getSuccessors().size());
	}
}
