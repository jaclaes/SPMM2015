/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.core.imperative.runtime;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.cheetahplatform.common.AssertionFailedException;
import org.cheetahplatform.core.imperative.modeling.ImperativeActivity;
import org.cheetahplatform.core.imperative.modeling.ImperativeProcessSchema;
import org.cheetahplatform.core.imperative.modeling.routing.XorSplit;
import org.cheetahplatform.core.imperative.runtime.IImperativeNodeInstance;
import org.cheetahplatform.core.imperative.runtime.ImperativeProcessInstance;
import org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState;
import org.junit.Test;

public class XorSplitInstanceTest {
	private final ImperativeProcessInstance instance = new ImperativeProcessInstance(new ImperativeProcessSchema());

	@Test
	public void activateNested() throws Exception {
		XorSplit split1 = new XorSplit();
		XorSplit split2 = new XorSplit();
		split1.addSuccessor(split2);
		split1.addSuccessor(new ImperativeActivity("A"));
		split2.addSuccessor(new ImperativeActivity("B"));
		split2.addSuccessor(new ImperativeActivity("C"));

		IImperativeNodeInstance split1Instance = split1.instantiate(instance);
		split1Instance.requestActivation();
		List<IImperativeNodeInstance> successors = split1Instance.getSuccessors();
		assertEquals(INodeInstanceState.ACTIVATED, successors.get(0).getState());
		assertEquals(INodeInstanceState.ACTIVATED, successors.get(1).getState());
		List<IImperativeNodeInstance> split2Instance = successors.get(0).getSuccessors();
		assertEquals(INodeInstanceState.ACTIVATED, split2Instance.get(0).getState());
		assertEquals(INodeInstanceState.ACTIVATED, split2Instance.get(1).getState());
	}

	@Test
	public void launchActivity() throws Exception {
		XorSplit split = new XorSplit();
		split.addSuccessor(new ImperativeActivity("A"));
		split.addSuccessor(new ImperativeActivity("B"));

		IImperativeNodeInstance splitInstance = split.instantiate(instance);
		splitInstance.requestActivation();

		IImperativeNodeInstance successorA = splitInstance.getSuccessors().get(0);
		successorA.launch();

		assertEquals(INodeInstanceState.LAUNCHED, successorA.getState());
		assertEquals(INodeInstanceState.SKIPPED, splitInstance.getSuccessors().get(1).getState());
	}

	@Test
	public void launchActivityAlternating() throws Exception {
		XorSplit split = new XorSplit();
		split.addSuccessor(new ImperativeActivity("A"));
		split.addSuccessor(new ImperativeActivity("B"));

		IImperativeNodeInstance splitInstance = split.instantiate(instance);
		splitInstance.requestActivation();

		IImperativeNodeInstance successorA = splitInstance.getSuccessors().get(0);
		IImperativeNodeInstance successorB = splitInstance.getSuccessors().get(1);

		successorA.launch();
		successorA.requestActivation();
		assertEquals(INodeInstanceState.ACTIVATED, successorA.getState());
		assertEquals(INodeInstanceState.ACTIVATED, successorB.getState());
		successorB.launch();
		assertEquals(INodeInstanceState.SKIPPED, successorA.getState());
		assertEquals(INodeInstanceState.LAUNCHED, successorB.getState());
	}

	@Test(expected = AssertionFailedException.class)
	public void launchActivityAlternatingFail() throws Exception {
		XorSplit split = new XorSplit();
		split.addSuccessor(new ImperativeActivity("A"));
		split.addSuccessor(new ImperativeActivity("B"));

		IImperativeNodeInstance splitInstance = split.instantiate(instance);
		splitInstance.requestActivation();

		IImperativeNodeInstance successorA = splitInstance.getSuccessors().get(0);
		IImperativeNodeInstance successorB = splitInstance.getSuccessors().get(1);
		successorA.launch();
		successorB.requestActivation();
	}

	@Test
	public void launchNested() throws Exception {
		XorSplit split1 = new XorSplit();
		XorSplit split2 = new XorSplit();
		split1.addSuccessor(split2);
		split1.addSuccessor(new ImperativeActivity("A"));
		split2.addSuccessor(new ImperativeActivity("B"));
		split2.addSuccessor(new ImperativeActivity("C"));

		IImperativeNodeInstance split1Instance = split1.instantiate(instance);
		split1Instance.requestActivation();
		IImperativeNodeInstance split2Instance = split1Instance.getSuccessors().get(0);
		split2Instance.getSuccessors().get(0).launch();

		assertEquals(INodeInstanceState.LAUNCHED, split2Instance.getSuccessors().get(0).getState());
		assertEquals(INodeInstanceState.SKIPPED, split2Instance.getSuccessors().get(1).getState());
		assertEquals(INodeInstanceState.SKIPPED, split1Instance.getSuccessors().get(1).getState());
		assertEquals(INodeInstanceState.LAUNCHED, split1Instance.getState());
		assertEquals(INodeInstanceState.LAUNCHED, split2Instance.getState());
	}

	@Test
	public void launchNestedAlternating() throws Exception {
		XorSplit split1 = new XorSplit();
		XorSplit split2 = new XorSplit();
		split1.addSuccessor(split2);
		split1.addSuccessor(new ImperativeActivity("A"));
		split2.addSuccessor(new ImperativeActivity("B"));
		split2.addSuccessor(new ImperativeActivity("C"));

		IImperativeNodeInstance split1Instance = split1.instantiate(instance);
		split1Instance.requestActivation();
		IImperativeNodeInstance split2Instance = split1Instance.getSuccessors().get(0);
		split2Instance.getSuccessors().get(0).launch();
		split2Instance.getSuccessors().get(0).requestActivation();
		split1Instance.getSuccessors().get(1).launch();

		assertEquals(INodeInstanceState.SKIPPED, split2Instance.getSuccessors().get(0).getState());
		assertEquals(INodeInstanceState.SKIPPED, split2Instance.getSuccessors().get(1).getState());
		assertEquals(INodeInstanceState.LAUNCHED, split1Instance.getSuccessors().get(1).getState());
		assertEquals(INodeInstanceState.LAUNCHED, split1Instance.getState());
		assertEquals(INodeInstanceState.SKIPPED, split2Instance.getState());
	}

	@Test
	public void simpleActivation() throws Exception {
		XorSplit split = new XorSplit();
		split.addSuccessor(new ImperativeActivity("A"));
		split.addSuccessor(new ImperativeActivity("B"));

		IImperativeNodeInstance splitInstance = split.instantiate(instance);
		assertEquals(0, splitInstance.getSuccessors().size());
		splitInstance.requestActivation();
		assertEquals(2, splitInstance.getSuccessors().size());

		for (IImperativeNodeInstance successor : splitInstance.getSuccessors()) {
			assertEquals(INodeInstanceState.ACTIVATED, successor.getState());
		}
	}

}
