package org.cheetahplatform.tdm.engine;

import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.core.declarative.constraint.ConstraintEvaluation;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;

public class ExecutionAssertionStep implements ITDMStep {

	protected TDMExecutionAssertion assertion;
	protected ExecutionAssertionFailedCause cause;
	private DateTime time;

	public ExecutionAssertionStep(TDMExecutionAssertion assertion, DateTime time) {
		this.assertion = assertion;
		this.time = time;
	}

	@Override
	public void execute(DeclarativeProcessInstance instance) throws TDMTestFailedException {
		ConstraintEvaluation evaluation = instance.isExecutable(assertion.getActivity());
		if (evaluation.getResult() ^ assertion.isExecutable()) {
			cause = new ExecutionAssertionFailedCause(assertion, evaluation);
			throw new TDMTestFailedException(this);
		}
	}

	@Override
	public ITDMFailureCause getFailure() {
		return cause;
	}

	@Override
	public Object getSource() {
		return assertion.getActivityUiModel();
	}

	@Override
	public DateTime getStartTime() {
		return time;
	}

}
