/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.imperative.runtime;

import static org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState.ACTIVATED;
import static org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState.COMPLETED;
import static org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState.SKIPPED;

import java.util.List;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.core.common.modeling.INodeInstance;
import org.cheetahplatform.core.common.modeling.IProcessInstance;
import org.cheetahplatform.core.common.modeling.ITerminationListener;
import org.cheetahplatform.core.imperative.modeling.IImperativeNode;
import org.cheetahplatform.core.imperative.modeling.ImperativeProcessSchema;
import org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState;

public abstract class AbstractBoxInstance extends AbstractImperativeNodeInstance {

	private class SubprocessLaunchListener implements INodeInstanceStateChangeListener {

		private static final long serialVersionUID = -754384437062934145L;

		public void stateChanged(IImperativeNodeInstance instance) {
			INodeInstanceState newState = instance.getState();
			if (newState.equals(ACTIVATED) || newState.equals(SKIPPED)) {
				cancelSequenceSelection();
			} else if (newState.equals(COMPLETED)) {
				complete();
			}
		}
	}

	private class TerminationListener implements ITerminationListener {

		public void terminated(IProcessInstance instance) {
			ensureSuccessorsCreated();
			for (INodeInstance successor : successorInstances) {
				successor.requestActivation();
			}
		}

	}

	private static final long serialVersionUID = -2905780040609083584L;

	protected ImperativeProcessInstance instance;

	protected AbstractBoxInstance(ImperativeProcessInstance processInstance, IImperativeNode node) {
		super(processInstance, node);
	}

	protected void cancelSequenceSelection() {
		getStartNodeSuccessor().skip(true);
		instance = null;

		if (!state.equals(ACTIVATED)) {
			testAndSet(ACTIVATED);
		}
	}

	/**
	 * @return the instance
	 */
	public ImperativeProcessInstance getSelectedSubProcess() {
		return instance;
	}

	private IImperativeNodeInstance getStartNodeSuccessor() {
		List<IImperativeNodeInstance> startNodeSuccessors = instance.getStartInstance().getSuccessors();
		Assert.isTrue(startNodeSuccessors.size() == 1);
		IImperativeNodeInstance startNodeSuccessor = startNodeSuccessors.get(0);
		return startNodeSuccessor;
	}

	@Override
	public void launch() {
		Assert.isNotNull(instance, "Cannot launch the box if no subprocess instance has been selected");
		super.launch();
	}

	public void selectSubProcess(ImperativeProcessSchema schema) {
		Assert.isTrue(instance == null, "Instance already selected");

		instance = schema.instantiate();
		instance.addTerminationListener(new TerminationListener());
		IImperativeNodeInstance startNodeSuccessor = getStartNodeSuccessor();
		startNodeSuccessor.addNodeInstanceChangeListener(new SubprocessLaunchListener());

		launch();
	}

}
