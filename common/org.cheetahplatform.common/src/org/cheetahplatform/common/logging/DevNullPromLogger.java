package org.cheetahplatform.common.logging;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class DevNullPromLogger implements IPromLogger {

	@Override
	public IStatus append(AuditTrailEntry entry) {
		return Status.OK_STATUS;
	}

	@Override
	public IStatus append(Process process, ProcessInstance instance) {
		return Status.OK_STATUS;
	}

	@Override
	public void close() {
		// ignore
	}

	@Override
	public String getProcessInstanceId() {
		return null;
	}

	@Override
	public void log(AuditTrailEntry entry) {
		// ignore
	}

	@Override
	public void setEnabled(boolean enabled) {
		// ignore
	}

}
