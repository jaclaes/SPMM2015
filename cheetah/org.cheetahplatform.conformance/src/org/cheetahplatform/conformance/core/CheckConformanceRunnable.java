package org.cheetahplatform.conformance.core;

import static org.cheetahplatform.conformance.Activator.PLUGIN_ID;

import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.List;

import org.cheetahplatform.conformance.Activator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.operation.IRunnableWithProgress;

public class CheckConformanceRunnable implements IRunnableWithProgress {

	private ITraceValidator firstValidator;
	private ITraceValidator secondValidator;
	private IStatus status;
	private String currentCheck;

	private int tracesToCheck;
	private int traceLength;

	public CheckConformanceRunnable(ITraceValidator firstValidator, ITraceValidator secondValidator) {
		this.firstValidator = firstValidator;
		this.secondValidator = secondValidator;

		this.tracesToCheck = 25000;
		this.traceLength = 10;
	}

	private void checkExecutionTraces() throws NotConformantException {
		// check execution, generate trace from first model, execute on second one
		currentCheck = MessageFormat.format("Generating trace from model {0}, replaying them on model {1}.", firstValidator.getModelName(),
				secondValidator.getModelName());
		List<ConformanceActivity> trace1 = firstValidator.generateTrace(traceLength);
		secondValidator.replayTrace(trace1);

		// and vice versa
		currentCheck = MessageFormat.format("Generating trace from model {0}, replaying them on model {1}.",
				secondValidator.getModelName(), firstValidator.getModelName());
		List<ConformanceActivity> trace2 = secondValidator.generateTrace(traceLength);
		firstValidator.replayTrace(trace2);
	}

	private void checkTermination() throws NotConformantException {
		// same procedure to check termination
		currentCheck = MessageFormat.format("Generating (possibly) terminating trace on model {0}, checking termination on model {1}.",
				firstValidator.getModelName(), secondValidator.getModelName());
		TerminatingConformanceTrace terminationTrace1 = firstValidator.generateTerminatingTrace(1);
		secondValidator.terminatesFor(terminationTrace1);

		currentCheck = MessageFormat.format("Generating (possibly) terminating trace on model {0}, checking termination on model {1}.",
				secondValidator.getModelName(), firstValidator.getModelName());
		TerminatingConformanceTrace terminationTrace2 = secondValidator.generateTerminatingTrace(1);
		firstValidator.terminatesFor(terminationTrace2);
	}

	/**
	 * @return the status
	 */
	public IStatus getStatus() {
		return status;
	}

	@Override
	public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
		monitor.beginTask("Checking traces", tracesToCheck);

		for (int i = 0; i < tracesToCheck; i++) {
			String message = MessageFormat.format("Trace {0} of {1}.", i * 4, tracesToCheck * 4);
			monitor.subTask(message);

			try {
				checkExecutionTraces();
				checkTermination();
			} catch (NotConformantException e) {
				String statusMessage = MessageFormat.format(
						"The process models seem not to be information equivalent, current operation:\n{0}\n\n{1}", currentCheck, e
								.getMessage());
				status = new Status(IStatus.WARNING, Activator.PLUGIN_ID, statusMessage);
				return;
			}

			monitor.worked(1);
			if (monitor.isCanceled()) {
				String cancelationMessage = MessageFormat.format("User aborted, checked {0} traces of {1} so far.", i * 4,
						tracesToCheck * 4);
				status = new Status(IStatus.WARNING, PLUGIN_ID, cancelationMessage);
				return;
			}
		}

		String message = MessageFormat.format("Checked {0} traces, no discrepancies found.", tracesToCheck * 4);
		status = new Status(IStatus.OK, Activator.PLUGIN_ID, message);
	}
}