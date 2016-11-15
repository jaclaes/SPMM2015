/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.core.imperative.runtime;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.cheetahplatform.core.imperative.modeling.AbstractNode;
import org.cheetahplatform.core.imperative.modeling.ImperativeActivity;
import org.cheetahplatform.core.imperative.modeling.ImperativeProcessSchema;
import org.cheetahplatform.core.imperative.modeling.routing.AndSplit;
import org.cheetahplatform.core.imperative.modeling.routing.XorSplit;
import org.cheetahplatform.core.imperative.runtime.IImperativeNodeInstance;
import org.cheetahplatform.core.imperative.runtime.ImperativeProcessInstance;
import org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState;
import org.junit.Test;

public class AndSplitInstanceTest {
	private final ImperativeProcessInstance instance = new ImperativeProcessInstance(new ImperativeProcessSchema());

	@Test
	public void activate() throws Exception {
		AndSplit andSplit = new AndSplit();
		AbstractNode activityB = new ImperativeActivity("A");
		andSplit.addSuccessor(activityB);
		AbstractNode activityC = new ImperativeActivity("B");
		andSplit.addSuccessor(activityC);

		IImperativeNodeInstance andSplitInstance = andSplit.instantiate(instance);
		assertEquals(0, andSplitInstance.getSuccessors().size());
		assertEquals(INodeInstanceState.CREATED, andSplitInstance.getState());

		andSplitInstance.requestActivation();
		assertEquals(2, andSplitInstance.getSuccessors().size());
		assertEquals(INodeInstanceState.ACTIVATED, andSplitInstance.getState());
		assertEquals(INodeInstanceState.ACTIVATED, andSplitInstance.getSuccessors().get(0).getState());
		assertEquals(INodeInstanceState.ACTIVATED, andSplitInstance.getSuccessors().get(1).getState());
	}

	@Test
	public void activateAfterLaunch() throws Exception {
		AndSplit andSplit = new AndSplit();
		AbstractNode activityB = new ImperativeActivity("A");
		andSplit.addSuccessor(activityB);
		AbstractNode activityC = new ImperativeActivity("B");
		andSplit.addSuccessor(activityC);

		IImperativeNodeInstance andSplitInstance = andSplit.instantiate(instance);
		andSplitInstance.requestActivation();
		andSplitInstance.getSuccessors().get(0).launch();
		andSplitInstance.getSuccessors().get(0).requestActivation();

		assertEquals(INodeInstanceState.ACTIVATED, andSplitInstance.getState());
		assertEquals(INodeInstanceState.ACTIVATED, andSplitInstance.getSuccessors().get(0).getState());
		assertEquals(INodeInstanceState.ACTIVATED, andSplitInstance.getSuccessors().get(1).getState());
	}

	@Test
	public void cascadeActivation() throws Exception {
		XorSplit xorSplit = new XorSplit();
		AndSplit andSplit = new AndSplit();
		xorSplit.addSuccessor(andSplit);
		AbstractNode activityA = new ImperativeActivity("A");
		andSplit.addSuccessor(activityA);
		AbstractNode activityB = new ImperativeActivity("B");
		andSplit.addSuccessor(activityB);
		xorSplit.addSuccessor(new ImperativeActivity("C"));

		IImperativeNodeInstance xorSplitInstance = xorSplit.instantiate(instance);
		xorSplitInstance.requestActivation();
		assertEquals(2, xorSplitInstance.getSuccessors().size());
		assertEquals(INodeInstanceState.ACTIVATED, xorSplitInstance.getState());
		IImperativeNodeInstance andSplitInstance = xorSplitInstance.getSuccessors().get(0);
		assertEquals(INodeInstanceState.ACTIVATED, andSplitInstance.getState());
		assertEquals(INodeInstanceState.ACTIVATED, andSplitInstance.getSuccessors().get(0).getState());
		assertEquals(INodeInstanceState.ACTIVATED, andSplitInstance.getSuccessors().get(1).getState());
		assertEquals(INodeInstanceState.ACTIVATED, xorSplitInstance.getSuccessors().get(1).getState());
	}

	@Test
	public void cascadeLaunchReactivate() throws Exception {
		XorSplit xorSplit = new XorSplit();
		AndSplit andSplit = new AndSplit();
		xorSplit.addSuccessor(andSplit);
		AbstractNode activityA = new ImperativeActivity("A");
		andSplit.addSuccessor(activityA);
		AbstractNode activityB = new ImperativeActivity("B");
		andSplit.addSuccessor(activityB);
		xorSplit.addSuccessor(new ImperativeActivity("C"));

		IImperativeNodeInstance xorSplitInstance = xorSplit.instantiate(instance);
		xorSplitInstance.requestActivation();
		IImperativeNodeInstance andSplitInstance = xorSplitInstance.getSuccessors().get(0);
		andSplitInstance.getSuccessors().get(0).launch();
		andSplitInstance.getSuccessors().get(0).cancel();

		assertEquals(INodeInstanceState.ACTIVATED, xorSplitInstance.getState());
		assertEquals(INodeInstanceState.ACTIVATED, xorSplitInstance.getSuccessors().get(1).getState());
		assertEquals(INodeInstanceState.ACTIVATED, andSplitInstance.getState());
		assertEquals(INodeInstanceState.ACTIVATED, andSplitInstance.getSuccessors().get(0).getState());
		assertEquals(INodeInstanceState.ACTIVATED, andSplitInstance.getSuccessors().get(1).getState());
	}

	@Test
	public void cascadeSkip1() throws Exception {
		XorSplit xorSplit = new XorSplit();
		AndSplit andSplit = new AndSplit();
		xorSplit.addSuccessor(andSplit);
		AbstractNode activityA = new ImperativeActivity("A");
		andSplit.addSuccessor(activityA);
		AbstractNode activityB = new ImperativeActivity("B");
		andSplit.addSuccessor(activityB);
		xorSplit.addSuccessor(new ImperativeActivity("C"));

		IImperativeNodeInstance xorSplitInstance = xorSplit.instantiate(instance);
		xorSplitInstance.requestActivation();
		IImperativeNodeInstance andSplitInstance = xorSplitInstance.getSuccessors().get(0);
		xorSplitInstance.getSuccessors().get(1).launch();
		assertEquals(INodeInstanceState.LAUNCHED, xorSplitInstance.getState());
		assertEquals(INodeInstanceState.LAUNCHED, xorSplitInstance.getSuccessors().get(1).getState());
		assertEquals(INodeInstanceState.SKIPPED, andSplitInstance.getState());
		assertEquals(INodeInstanceState.SKIPPED, andSplitInstance.getSuccessors().get(0).getState());
		assertEquals(INodeInstanceState.SKIPPED, andSplitInstance.getSuccessors().get(1).getState());
	}

	@Test
	public void cascadeSkip2() throws Exception {
		XorSplit xorSplit = new XorSplit();
		AndSplit andSplit = new AndSplit();
		xorSplit.addSuccessor(andSplit);
		AbstractNode activityA = new ImperativeActivity("A");
		andSplit.addSuccessor(activityA);
		AbstractNode activityB = new ImperativeActivity("B");
		andSplit.addSuccessor(activityB);
		xorSplit.addSuccessor(new ImperativeActivity("C"));

		IImperativeNodeInstance xorSplitInstance = xorSplit.instantiate(instance);
		xorSplitInstance.requestActivation();
		IImperativeNodeInstance andSplitInstance = xorSplitInstance.getSuccessors().get(0);
		andSplitInstance.getSuccessors().get(0).launch();

		assertEquals(INodeInstanceState.LAUNCHED, xorSplitInstance.getState());
		assertEquals(INodeInstanceState.SKIPPED, xorSplitInstance.getSuccessors().get(1).getState());
		assertEquals(INodeInstanceState.LAUNCHED, andSplitInstance.getState());
		assertEquals(INodeInstanceState.LAUNCHED, andSplitInstance.getSuccessors().get(0).getState());
		assertEquals(INodeInstanceState.ACTIVATED, andSplitInstance.getSuccessors().get(1).getState());
	}

	@Test
	public void complete() {
		AndSplit connector = new AndSplit();
		AbstractNode activityB = new ImperativeActivity("B");
		connector.addSuccessor(activityB);
		AbstractNode activityC = new ImperativeActivity("C");
		connector.addSuccessor(activityC);

		IImperativeNodeInstance connectorInstance = connector.instantiate(instance);
		connectorInstance.requestActivation();
		List<IImperativeNodeInstance> successors = connectorInstance.getSuccessors();
		assertEquals(2, successors.size());

		for (IImperativeNodeInstance nodeInstance : successors) {
			assertEquals(INodeInstanceState.ACTIVATED, nodeInstance.getState());
		}
	}

	@Test
	public void completeActivities() throws Exception {
		AndSplit andSplit = new AndSplit();
		AbstractNode activityB = new ImperativeActivity("A");
		andSplit.addSuccessor(activityB);
		AbstractNode activityC = new ImperativeActivity("B");
		andSplit.addSuccessor(activityC);

		IImperativeNodeInstance andSplitInstance = andSplit.instantiate(instance);
		andSplitInstance.requestActivation();
		andSplitInstance.getSuccessors().get(0).launch();
		andSplitInstance.getSuccessors().get(0).complete();
		assertEquals(INodeInstanceState.COMPLETED, andSplitInstance.getState());
		assertEquals(INodeInstanceState.COMPLETED, andSplitInstance.getSuccessors().get(0).getState());
		assertEquals(INodeInstanceState.ACTIVATED, andSplitInstance.getSuccessors().get(1).getState());
	}

	@Test
	public void completionOfBothActivities() throws Exception {
		AndSplit andSplit = new AndSplit();
		AbstractNode activityB = new ImperativeActivity("A");
		andSplit.addSuccessor(activityB);
		AbstractNode activityC = new ImperativeActivity("B");
		andSplit.addSuccessor(activityC);

		IImperativeNodeInstance andSplitInstance = andSplit.instantiate(instance);
		andSplitInstance.requestActivation();
		andSplitInstance.getSuccessors().get(0).launch();
		andSplitInstance.getSuccessors().get(0).complete();
		assertEquals(INodeInstanceState.COMPLETED, andSplitInstance.getState());
		andSplitInstance.getSuccessors().get(1).launch();
		andSplitInstance.getSuccessors().get(1).complete();
		assertEquals(INodeInstanceState.COMPLETED, andSplitInstance.getState());
	}

	@Test
	public void finalStateChange() throws Exception {
		AbstractNode activityA = new ImperativeActivity("A");
		AndSplit connector = new AndSplit();
		activityA.addSuccessor(connector);
		AbstractNode activityB = new ImperativeActivity("B");
		connector.addSuccessor(activityB);
		AbstractNode activityC = new ImperativeActivity("C");
		connector.addSuccessor(activityC);

		IImperativeNodeInstance activityInstanceA = instance.getNodeRegistry().getInstance(activityA);
		assertEquals(0, activityInstanceA.getSuccessors().size());

		activityInstanceA.requestActivation();
		assertEquals(0, activityInstanceA.getSuccessors().size());
		activityInstanceA.launch();
		assertEquals(0, activityInstanceA.getSuccessors().size());
		activityInstanceA.complete();

		assertEquals(1, activityInstanceA.getSuccessors().size());
	}

	@Test
	public void launch() throws Exception {
		AndSplit andSplit = new AndSplit();
		AbstractNode activityB = new ImperativeActivity("A");
		andSplit.addSuccessor(activityB);
		AbstractNode activityC = new ImperativeActivity("B");
		andSplit.addSuccessor(activityC);

		IImperativeNodeInstance andSplitInstance = andSplit.instantiate(instance);
		andSplitInstance.requestActivation();
		andSplitInstance.getSuccessors().get(0).launch();
		assertEquals(INodeInstanceState.LAUNCHED, andSplitInstance.getState());
		assertEquals(INodeInstanceState.LAUNCHED, andSplitInstance.getSuccessors().get(0).getState());
		assertEquals(INodeInstanceState.ACTIVATED, andSplitInstance.getSuccessors().get(1).getState());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void launchFail() throws Exception {
		AndSplit andSplit = new AndSplit();
		AbstractNode activityB = new ImperativeActivity("A");
		andSplit.addSuccessor(activityB);
		AbstractNode activityC = new ImperativeActivity("B");
		andSplit.addSuccessor(activityC);

		IImperativeNodeInstance andSplitInstance = andSplit.instantiate(instance);
		andSplitInstance.requestActivation();
		andSplitInstance.launch();
	}

	@Test
	public void launchTwoActivties() throws Exception {
		AndSplit andSplit = new AndSplit();
		AbstractNode activityA = new ImperativeActivity("A");
		andSplit.addSuccessor(activityA);
		AbstractNode activityB = new ImperativeActivity("B");
		andSplit.addSuccessor(activityB);
		AbstractNode activityC = new ImperativeActivity("C");
		andSplit.addSuccessor(activityC);

		IImperativeNodeInstance andSplitInstance = andSplit.instantiate(instance);
		andSplitInstance.requestActivation();
		assertEquals(3, andSplitInstance.getSuccessors().size());
		andSplitInstance.getSuccessors().get(0).launch();
		andSplitInstance.getSuccessors().get(1).launch();
		assertEquals(INodeInstanceState.LAUNCHED, andSplitInstance.getState());
		assertEquals(INodeInstanceState.LAUNCHED, andSplitInstance.getSuccessors().get(0).getState());
		assertEquals(INodeInstanceState.LAUNCHED, andSplitInstance.getSuccessors().get(1).getState());
		assertEquals(INodeInstanceState.ACTIVATED, andSplitInstance.getSuccessors().get(2).getState());
	}

	@Test
	public void nonFinalStateChange() throws Exception {
		AbstractNode activityA = new ImperativeActivity("A");
		AndSplit connector = new AndSplit();
		activityA.addSuccessor(connector);
		AbstractNode activityB = new ImperativeActivity("B");
		connector.addSuccessor(activityB);
		AbstractNode activityC = new ImperativeActivity("C");
		connector.addSuccessor(activityC);

		IImperativeNodeInstance activityInstanceA = instance.getNodeRegistry().getInstance(activityA);
		assertEquals(0, activityInstanceA.getSuccessors().size());
	}

	@Test
	public void skipAndPropagate() {
		AndSplit connector = new AndSplit();
		AbstractNode activityB = new ImperativeActivity("B");
		connector.addSuccessor(activityB);
		AbstractNode activityC = new ImperativeActivity("C");
		connector.addSuccessor(activityC);

		IImperativeNodeInstance connectorInstance = connector.instantiate(instance);
		connectorInstance.skip(true);
		List<IImperativeNodeInstance> successors = connectorInstance.getSuccessors();
		assertEquals(2, successors.size());

		for (IImperativeNodeInstance nodeInstance : successors) {
			assertEquals(INodeInstanceState.SKIPPED, nodeInstance.getState());
		}
	}

	@Test
	public void skipWithoutPropagate() {
		AndSplit split = new AndSplit();
		AbstractNode activityB = new ImperativeActivity("B");
		split.addSuccessor(activityB);
		AbstractNode activityC = new ImperativeActivity("C");
		split.addSuccessor(activityC);

		IImperativeNodeInstance connectorInstance = split.instantiate(instance);
		connectorInstance.skip(false);
		List<IImperativeNodeInstance> successors = connectorInstance.getSuccessors();
		assertEquals("Should have two successors as splits propagate skips anyway.", 2, successors.size());
	}
}
