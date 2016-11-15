package org.cheetahplatform.common.logging;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public abstract class AbstractPromLogger implements IPromLogger {
	private boolean enabled;

	protected AbstractPromLogger() {
		this.enabled = true;
	}

	@Override
	public final IStatus append(AuditTrailEntry entry) {
		if (!enabled) {
			return Status.OK_STATUS;
		}

		return doAppend(entry);
	}

	@Override
	public final IStatus append(Process process, ProcessInstance instance) {
		if (!enabled) {
			return Status.OK_STATUS;
		}

		return doAppend(process, instance);
	}

	protected abstract IStatus doAppend(AuditTrailEntry entry);

	protected abstract IStatus doAppend(Process process, ProcessInstance instance);

	@Override
	public final void log(AuditTrailEntry entry) {
		append(entry);
	}

	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
