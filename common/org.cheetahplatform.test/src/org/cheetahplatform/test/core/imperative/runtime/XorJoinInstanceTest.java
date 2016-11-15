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
import org.cheetahplatform.core.imperative.modeling.routing.AndSplit;
import org.cheetahplatform.core.imperative.modeling.routing.XorJoin;
import org.cheetahplatform.core.imperative.modeling.routing.XorSplit;
import org.cheetahplatform.core.imperative.runtime.IImperativeNodeInstance;
import org.cheetahplatform.core.imperative.runtime.ImperativeProcessInstance;
import org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState;
import org.junit.Test;

public class XorJoinInstanceTest {
	private final ImperativeProcessInstance instance = new ImperativeProcessInstance(new ImperativeProcessSchema());

	@Test
	public void complexExample() throws Exception {
		ImperativeActivity activityA = new ImperativeActivity("A");
		ImperativeActivity activityB = new ImperativeActivity("B");
		ImperativeActivity activityC = new ImperativeActivity("C");
		ImperativeActivity activityD = new ImperativeActivity("D");
		ImperativeActivity activityE = new ImperativeActivity("E");
		AndSplit split1 = new AndSplit();
		XorSplit split2 = new XorSplit();
		AndJoin join1 = new AndJoin();
		XorJoin join2 = new XorJoin();

		activityA.addSuccessor(split1);
		split1.addSuccessor(split2);
		split1.addSuccessor(activityD);
		split2.addSuccessor(activityB);
		split2.addSuccessor(activityC);
		activityB.addSuccessor(join2);
		activityC.addSuccessor(join2);
		join2.addSuccessor(join1);
		activityD.addSuccessor(join1);
		join1.addSuccessor(activityE);

		IImperativeNodeInstance activityAInstance = activityA.instantiate(instance);
		activityAInstance.requestActivation();
		activityAInstance.launch();
		activityAInstance.complete();

		IImperativeNodeInstance split1Instance = activityAInstance.getSuccessors().get(0);
		IImperativeNodeInstance split2Instance = split1Instance.getSuccessors().get(0);
		IImperativeNodeInstance activityBInstance = split2Instance.getSuccessors().get(0);

		assertEquals(INodeInstanceState.ACTIVATED, split1Instance.getState());
		assertEquals(INodeInstanceState.ACTIVATED, split2Instance.getState());
		assertEquals(INodeInstanceState.ACTIVATED, activityBInstance.getState());
		assertEquals(INodeInstanceState.ACTIVATED, split2Instance.getSuccessors().get(1).getState());
		assertEquals(INodeInstanceState.ACTIVATED, split1Instance.getSuccessors().get(1).getState());

		activityBInstance.launch();
		activityBInstance.complete();

		assertEquals(INodeInstanceState.COMPLETED, activityBInstance.getState());
		assertEquals(INodeInstanceState.COMPLETED, split2Instance.getState());
		assertEquals(INodeInstanceState.COMPLETED, split1Instance.getState());
		assertEquals(INodeInstanceState.SKIPPED, split2Instance.getSuccessors().get(1).getState());
		assertEquals(INodeInstanceState.ACTIVATED, split1Instance.getSuccessors().get(1).getState());
		assertEquals(1, activityBInstance.getSuccessors().size());
		assertEquals(INodeInstanceState.COMPLETED, activityBInstance.getSuccessors().get(0).getState());
		assertEquals(1, activityBInstance.getSuccessors().get(0).getSuccessors().size());
		IImperativeNodeInstance join1Instance = activityBInstance.getSuccessors().get(0).getSuccessors().get(0);
		assertEquals(INodeInstanceState.CREATED, join1Instance.getState());

		split1Instance.getSuccessors().get(1).launch();
		split1Instance.getSuccessors().get(1).complete();
		assertEquals(INodeInstanceState.COMPLETED, join1Instance.getState());
		assertEquals(1, join1Instance.getSuccessors().size());
		assertEquals(INodeInstanceState.ACTIVATED, join1Instance.getSuccessors().get(0).getState());
	}

	@Test
	public void create() throws Exception {
		ImperativeActivity activityA = new ImperativeActivity("A");
		ImperativeActivity activityB = new ImperativeActivity("B");
		XorJoin join = new XorJoin();
		activityA.addSuccessor(join);
		activityB.addSuccessor(join);

		IImperativeNodeInstance activityInstanceA = activityA.instantiate(instance);
		IImperativeNodeInstance activityInstanceB = activityB.instantiate(instance);
		assertEquals(0, activityInstanceA.getSuccessors().size());
		assertEquals(0, activityInstanceB.getSuccessors().size());

		activityInstanceA.requestActivation();
		activityInstanceB.requestActivation();
		assertEquals(0, activityInstanceA.getSuccessors().size());
		assertEquals(0, activityInstanceB.getSuccessors().size());

		activityInstanceA.launch();
		activityInstanceB.launch();
		assertEquals(0, activityInstanceA.getSuccessors().size());
		assertEquals(0, activityInstanceB.getSuccessors().size());

		activityInstanceA.complete();
		activityInstanceB.complete();
		assertEquals(1, activityInstanceA.getSuccessors().size());
		assertEquals(1, activityInstanceB.getSuccessors().size());
		assertSame(activityInstanceA.getSuccessors().get(0), activityInstanceB.getSuccessors().get(0));
	}

	@Test
	public void joinActivation() throws Exception {
		ImperativeActivity activityA = new ImperativeActivity("A");
		ImperativeActivity activityB = new ImperativeActivity("B");
		XorJoin join = new XorJoin();
		join.addSuccessor(new ImperativeActivity("C"));
		activityA.addSuccessor(join);
		activityB.addSuccessor(join);

		IImperativeNodeInstance activityInstanceA = activityA.instantiate(instance);
		IImperativeNodeInstance activityInstanceB = activityB.instantiate(instance);

		activityInstanceA.requestActivation();
		activityInstanceB.requestActivation();

		activityInstanceA.launch();
		activityInstanceB.launch();

		activityInstanceA.complete();
		activityInstanceB.complete();

		IImperativeNodeInstance joinInstance = activityInstanceA.getSuccessors().get(0);
		assertEquals(INodeInstanceState.ACTIVATED, joinInstance.getSuccessors().get(0).getState());
	}

	@Test
	public void joinSkippedActivities() throws Exception {
		ImperativeActivity activityA = new ImperativeActivity("A");
		ImperativeActivity activityB = new ImperativeActivity("B");
		XorJoin join = new XorJoin();
		activityA.addSuccessor(join);
		activityB.addSuccessor(join);

		IImperativeNodeInstance activityInstanceA = activityA.instantiate(instance);
		IImperativeNodeInstance activityInstanceB = activityB.instantiate(instance);

		activityInstanceA.skip(true);
		activityInstanceB.skip(true);

		IImperativeNodeInstance joinInstance = activityInstanceA.getSuccessors().get(0);
		assertEquals(INodeInstanceState.SKIPPED, joinInstance.getState());
	}

	@Test
	public void joinSkippedAndCompletedActivities() throws Exception {
		ImperativeActivity activityA = new ImperativeActivity("A");
		ImperativeActivity activityB = new ImperativeActivity("B");
		XorJoin join = new XorJoin();
		XorSplit split = new XorSplit();
		split.addSuccessor(activityA);
		split.addSuccessor(activityB);
		activityA.addSuccessor(join);
		activityB.addSuccessor(join);

		IImperativeNodeInstance splitInstance = split.instantiate(instance);
		splitInstance.requestActivation();

		IImperativeNodeInstance activityInstanceA = splitInstance.getSuccessors().get(0);
		activityInstanceA.launch();
		activityInstanceA.complete();

		IImperativeNodeInstance joinInstance = activityInstanceA.getSuccessors().get(0);
		assertEquals(INodeInstanceState.COMPLETED, joinInstance.getState());
	}

	@Test
	public void joinSuccessorsActivation() throws Exception {
		ImperativeActivity activityA = new ImperativeActivity("A");
		ImperativeActivity activityB = new ImperativeActivity("B");
		ImperativeActivity activityC = new ImperativeActivity("C");
		XorJoin join = new XorJoin();
		join.addSuccessor(activityC);
		XorSplit split = new XorSplit();
		split.addSuccessor(activityA);
		split.addSuccessor(activityB);
		activityA.addSuccessor(join);
		activityB.addSuccessor(join);

		IImperativeNodeInstance splitInstance = split.instantiate(instance);
		splitInstance.requestActivation();

		IImperativeNodeInstance activityInstanceA = splitInstance.getSuccessors().get(0);
		activityInstanceA.launch();
		activityInstanceA.complete();

		IImperativeNodeInstance joinInstance = activityInstanceA.getSuccessors().get(0);
		IImperativeNodeInstance joinSuccessor = joinInstance.getSuccessors().get(0);
		assertEquals(INodeInstanceState.ACTIVATED, joinSuccessor.getState());
	}

	@Test
	public void joinSuccessorsSkipped() throws Exception {
		ImperativeActivity activityA = new ImperativeActivity("A");
		ImperativeActivity activityB = new ImperativeActivity("B");
		ImperativeActivity activityC = new ImperativeActivity("C");
		XorJoin join = new XorJoin();
		join.addSuccessor(activityC);
		XorSplit split = new XorSplit();
		split.addSuccessor(activityA);
		split.addSuccessor(activityB);
		activityA.addSuccessor(join);
		activityB.addSuccessor(join);

		IImperativeNodeInstance splitInstance = split.instantiate(instance);
		splitInstance.skip(true);

		IImperativeNodeInstance joinInstance = splitInstance.getSuccessors().get(0).getSuccessors().get(0);
		assertEquals(INodeInstanceState.SKIPPED, joinInstance.getState());
		IImperativeNodeInstance joinSuccessor = joinInstance.getSuccessors().get(0);
		assertEquals(INodeInstanceState.SKIPPED, joinSuccessor.getState());
	}

	@Test
	public void skipWithoutPropagate() throws Exception {
		ImperativeActivity activityB = new ImperativeActivity("A");
		XorJoin join = new XorJoin();
		join.addSuccessor(activityB);

		IImperativeNodeInstance joinInstance = join.instantiate(instance);
		joinInstance.skip(false);
		assertEquals(INodeInstanceState.SKIPPED, joinInstance.getState());
		assertEquals(0, joinInstance.getSuccessors().size());
	}

	@Test
	public void successorActivation() throws Exception {
		ImperativeActivity activityA = new ImperativeActivity("A");
		ImperativeActivity activityB = new ImperativeActivity("B");
		XorJoin join = new XorJoin();
		activityA.addSuccessor(join);
		activityB.addSuccessor(join);

		IImperativeNodeInstance activityInstanceA = activityA.instantiate(instance);
		IImperativeNodeInstance activityInstanceB = activityB.instantiate(instance);

		activityInstanceA.requestActivation();
		activityInstanceB.requestActivation();

		activityInstanceA.launch();
		activityInstanceB.launch();

		activityInstanceA.complete();
		activityInstanceB.complete();

		INodeInstanceState joinState = activityInstanceA.getSuccessors().get(0).getState();
		assertEquals(INodeInstanceState.COMPLETED, joinState);
	}

	@Test
	public void successorCreationWithoutActivation() throws Exception {
		ImperativeActivity activityA = new ImperativeActivity("A");
		ImperativeActivity activityB = new ImperativeActivity("B");
		XorJoin join = new XorJoin();
		activityA.addSuccessor(join);
		activityB.addSuccessor(join);

		IImperativeNodeInstance activityInstanceA = activityA.instantiate(instance);
		IImperativeNodeInstance activityInstanceB = activityB.instantiate(instance);

		activityInstanceA.requestActivation();
		activityInstanceB.requestActivation();

		activityInstanceA.launch();
		activityInstanceA.complete();

		INodeInstanceState joinState = activityInstanceA.getSuccessors().get(0).getState();
		assertEquals(INodeInstanceState.ACTIVATED, joinState);
	}
}
