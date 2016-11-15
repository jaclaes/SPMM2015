package org.cheetahplatform.shared;

import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;

public class DeclarativeProcessInstanceHandle extends ProcessInstanceHandle {
	private boolean canTerminate;

	public DeclarativeProcessInstanceHandle(DeclarativeProcessInstance instance) {
		super(instance);

		this.canTerminate = instance.canTerminate().getResult();
	}

	/**
	 * @return the canTerminate
	 */
	public boolean canTerminate() {
		return canTerminate;
	}

}
