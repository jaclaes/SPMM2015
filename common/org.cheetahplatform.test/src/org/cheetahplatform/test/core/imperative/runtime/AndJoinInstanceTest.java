/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.core.imperative.runtime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.cheetahplatform.core.imperative.modeling.ImperativeActivity;
import org.cheetahplatform.core.imperative.modeling.ImperativeProcessSchema;
import org.cheetahplatform.core.imperative.modeling.routing.AndJoin;
import org.cheetahplatform.core.imperative.runtime.IImperativeNodeInstance;
import org.cheetahplatform.core.imperative.runtime.ImperativeProcessInstance;
import org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState;
import org.junit.Test;

public class AndJoinInstanceTest {
	private final ImperativeProcessInstance instance = new ImperativeProcessInstance(new ImperativeProcessSchema());

	@Test
	public void cascadingComplete() throws Exception {
		ImperativeActivity activityA = new ImperativeActivity("A");
		ImperativeActivity activityB = new ImperativeActivity("B");
		AndJoin join = new AndJoin();
		activityA.addSuccessor(join);
		activityB.addSuccessor(join);
		join.addSuccessor(new ImperativeActivity("C"));

		IImperativeNodeInstance activityInstanceA = activityA.instantiate(instance);
		IImperativeNodeInstance activityInstanceB = activityB.instantiate(instance);

		activityInstanceA.requestActivation();
		activityInstanceB.requestActivation();

		activityInstanceA.launch();
		activityInstanceB.launch();

		activityInstanceA.complete();
		IImperativeNodeInstance joinInstance = activityInstanceA.getSuccessors().get(0);
		assertEquals(0, joinInstance.getSuccessors().size());
		assertEquals(INodeInstanceState.CREATED, joinInstance.getState());

		activityInstanceB.complete();
		assertEquals(1, joinInstance.getSuccessors().size());
		assertEquals(INodeInstanceState.COMPLETED, joinInstance.getState());
		assertEquals(INodeInstanceState.ACTIVATED, joinInstance.getSuccessors().get(0).getState());
	}

	@Test
	public void cascadingSkip() throws Exception {
		ImperativeActivity activityA = new ImperativeActivity("A");
		ImperativeActivity activityB = new ImperativeActivity("B");
		AndJoin join = new AndJoin();
		activityA.addSuccessor(join);
		activityB.addSuccessor(join);
		join.addSuccessor(new ImperativeActivity("C"));

		IImperativeNodeInstance activityInstanceA = activityA.instantiate(instance);
		IImperativeNodeInstance activityInstanceB = activityB.instantiate(instance);

		activityInstanceA.skip(true);
		IImperativeNodeInstance joinInstance = activityInstanceA.getSuccessors().get(0);
		assertEquals(0, joinInstance.getSuccessors().size());
		assertEquals(INodeInstanceState.CREATED, joinInstance.getState());

		activityInstanceB.skip(true);
		assertEquals(1, joinInstance.getSuccessors().size());
		assertEquals(INodeInstanceState.SKIPPED, joinInstance.getState());
		assertEquals(INodeInstanceState.SKIPPED, joinInstance.getSuccessors().get(0).getState());
	}

	@Test
	public void complete() throws Exception {
		ImperativeActivity activityA = new ImperativeActivity("A");
		ImperativeActivity activityB = new ImperativeActivity("B");
		AndJoin join = new AndJoin();
		activityA.addSuccessor(join);
		activityB.addSuccessor(join);

		IImperativeNodeInstance activityInstanceA = activityA.instantiate(instance);
		IImperativeNodeInstance activityInstanceB = activityB.instantiate(instance);

		activityInstanceA.requestActivation();
		activityInstanceB.requestActivation();

		activityInstanceA.launch();
		activityInstanceB.launch();

		assertEquals(0, activityInstanceA.getSuccessors().size());
		assertEquals(0, activityInstanceB.getSuccessors().size());

		activityInstanceA.complete();
		assertEquals(1, activityInstanceA.getSuccessors().size());
		IImperativeNodeInstance joinInstance = activityInstanceA.getSuccessors().get(0);
		assertEquals(INodeInstanceState.CREATED, joinInstance.getState());

		activityInstanceB.complete();
		assertEquals(1, activityInstanceB.getSuccessors().size());
		assertEquals(INodeInstanceState.COMPLETED, joinInstance.getState());

		assertSame(joinInstance, activityInstanceB.getSuccessors().get(0));
	}

	@Test
	public void skipAndPropagate() throws Exception {
		ImperativeActivity activityA = new ImperativeActivity("A");
		ImperativeActivity activityB = new ImperativeActivity("B");
		AndJoin join = new AndJoin();
		activityA.addSuccessor(join);
		activityB.addSuccessor(join);

		IImperativeNodeInstance activityInstanceA = activityA.instantiate(instance);
		IImperativeNodeInstance activityInstanceB = activityB.instantiate(instance);

		activityInstanceA.requestActivation();
		activityInstanceB.requestActivation();

		activityInstanceA.skip(true);
		assertEquals(1, activityInstanceA.getSuccessors().size());
		IImperativeNodeInstance joinInstance = activityInstanceA.getSuccessors().get(0);
		assertEquals(INodeInstanceState.CREATED, joinInstance.getState());

		activityInstanceB.skip(true);
		assertEquals(1, activityInstanceB.getSuccessors().size());
		assertEquals(INodeInstanceState.SKIPPED, joinInstance.getState());

		assertSame(joinInstance, activityInstanceB.getSuccessors().get(0));
	}

	@Test
	public void skipWithoutPropagate() throws Exception {
		ImperativeActivity activityA = new ImperativeActivity("A");
		AndJoin join = new AndJoin();
		join.addSuccessor(activityA);

		IImperativeNodeInstance joinInstance = join.instantiate(instance);

		assertEquals(0, joinInstance.getSuccessors().size());
		joinInstance.skip(false);
		assertEquals(INodeInstanceState.SKIPPED, joinInstance.getState());
		assertEquals(0, joinInstance.getSuccessors().size());
	}
}
