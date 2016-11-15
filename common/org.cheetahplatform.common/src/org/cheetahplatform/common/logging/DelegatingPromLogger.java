package org.cheetahplatform.common.logging;

import org.eclipse.core.runtime.IStatus;

public class DelegatingPromLogger extends AbstractPromLogger {

	private IPromLogger delegate;

	public DelegatingPromLogger(IPromLogger delegate) {
		this.delegate = delegate;
	}

	@Override
	public void close() {
		delegate.close();
	}

	@Override
	protected IStatus doAppend(AuditTrailEntry entry) {
		return delegate.append(entry);
	}

	@Override
	protected IStatus doAppend(Process process, ProcessInstance instance) {
		return delegate.append(process, instance);
	}

	@Override
	public String getProcessInstanceId() {
		return delegate.getProcessInstanceId();
	}

	public void setDelegate(IPromLogger delegate) {
		this.delegate = delegate;
	}

}
