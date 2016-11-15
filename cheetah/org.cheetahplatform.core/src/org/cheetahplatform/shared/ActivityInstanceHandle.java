package org.cheetahplatform.shared;

import org.cheetahplatform.core.common.modeling.INodeInstance;
import org.cheetahplatform.core.common.modeling.ProcessInstance;
import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;

public class ActivityInstanceHandle extends TypedHandle {
	private ProcessInstanceHandle parent;

	public ActivityInstanceHandle(DeclarativeActivity activity, ProcessInstance parent) {
		this(activity.getCheetahId(), activity.getName(), activity.getType(), parent);
	}

	public ActivityInstanceHandle(INodeInstance instance, ProcessInstance parent) {
		this(instance.getCheetahId(), instance.getNode().getName(), instance.getNode().getType(), parent);
	}

	public ActivityInstanceHandle(long id, String name, String type, ProcessInstance parent) {
		super(id, name, type);

		this.parent = new ProcessInstanceHandle(parent);
	}

	/**
	 * @return the parent
	 */
	public ProcessInstanceHandle getParent() {
		return parent;
	}

}
