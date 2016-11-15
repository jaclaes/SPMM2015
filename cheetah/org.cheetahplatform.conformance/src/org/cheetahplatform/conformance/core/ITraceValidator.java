package org.cheetahplatform.conformance.core;

import java.util.List;

public interface ITraceValidator {

	int MAXIMUM_TERMINATION_TRACE_LENGTH = 100;

	/**
	 * Try to generate a trace which terminates.
	 * 
	 * @param minimumTraceLength
	 *            the minimum length of the trace to be generated (is not required to be obeyed, if not possible)
	 * 
	 * @return a terminating trace, if could be generated
	 */
	TerminatingConformanceTrace generateTerminatingTrace(int minimumTraceLength);

	/**
	 * Generate a trace of the given length, possibly shorter if it is not possible to produce a trace of the given length.
	 * 
	 * @param traceLength
	 *            the maximum length of the trace
	 * @return the generated trace
	 */
	List<ConformanceActivity> generateTrace(int traceLength);

	/**
	 * Return the wrapped model's name.
	 * 
	 * @return the name
	 */
	String getModelName();

	/**
	 * Replay the given trace.
	 * 
	 * @param trace
	 *            the trace to be replayed
	 * @throws NotConformantException
	 *             if the trace could not be replayed
	 */
	void replayTrace(List<ConformanceActivity> trace) throws NotConformantException;

	/**
	 * Determine whether the validator terminates for the given trace.
	 * 
	 * @param trace
	 *            the trace
	 * @throws NotConformantException
	 *             if the validator cannot terminate for the given trace, whilst specified in the given trace to do so
	 */
	void terminatesFor(TerminatingConformanceTrace trace) throws NotConformantException;

}
