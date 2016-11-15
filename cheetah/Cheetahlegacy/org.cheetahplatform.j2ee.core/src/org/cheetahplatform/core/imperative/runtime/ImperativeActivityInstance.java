/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.imperative.runtime;

import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.core.common.modeling.IActivityInstance;
import org.cheetahplatform.core.imperative.modeling.IImperativeNode;

public class ImperativeActivityInstance extends AbstractImperativeNodeInstance implements IActivityInstance {
	private static final long serialVersionUID = -3683420544382033846L;

	private DateTime startTime;
	private DateTime endTime;

	public ImperativeActivityInstance(ImperativeProcessInstance processInstance, IImperativeNode activity) {
		super(processInstance, activity);
	}

	public void accept(INodeInstanceVisitor visitor) {
		visitor.visitActivity(this);
		visitSuccessors(visitor);
	}

	@Override
	public void complete() {
		super.complete();

		ensureSuccessorsCreated();
		for (IImperativeNodeInstance successor : successorInstances) {
			successor.requestActivation();
		}

		endTime = new DateTime();
		processInstance.requestTermination();
	}

	public DateTime getEndTime() {
		return endTime;
	}

	public DateTime getStartTime() {
		return startTime;
	}

	@Override
	public void skip(boolean propagate) {
		super.skip(propagate);

		if (!propagate) {
			processInstance.requestTermination();
			return;
		}

		ensureSuccessorsCreated();
		for (IImperativeNodeInstance instance : successorInstances) {
			instance.skip(propagate);
		}

		if (propagate) {
			processInstance.requestTermination();
		}
	}
}
