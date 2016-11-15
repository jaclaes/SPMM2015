package org.cheetahplatform.common.logging;

import org.eclipse.core.runtime.IStatus;

public interface IPromWriter {
	/**
	 * Append the given entry.
	 * 
	 * @param entry
	 *            the entry to be logged
	 * @return the status, <code>null</code> or status code {@link IStatus#OK} if the logging could be performed
	 */
	IStatus append(AuditTrailEntry entry);

	/**
	 * Append the given instance, does <b>not</b> log the instance's audit trail entries.
	 * 
	 * @param instance
	 *            the process instance
	 * @param process
	 *            the process to which the instance should be appended
	 * @return the status, <code>null</code> or status code {@link IStatus#OK} if the logging could be performed
	 */
	IStatus append(Process process, ProcessInstance instance);

	/**
	 * Closes the writer and disposes all allocated resources.
	 */
	void close();

	String getProcessInstanceId();

}
