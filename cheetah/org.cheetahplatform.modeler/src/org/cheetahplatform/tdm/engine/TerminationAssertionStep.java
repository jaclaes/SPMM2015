package org.cheetahplatform.tdm.engine;

import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.core.declarative.constraint.ConstraintEvaluation;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;

public class TerminationAssertionStep implements ITDMStep {

	private TDMTerminationAssertion assertion;
	private DateTime time;
	private ITDMFailureCause cause;

	public TerminationAssertionStep(TDMTerminationAssertion assertion, DateTime time) {
		this.assertion = assertion;
		this.time = time;
	}

	@Override
	public void execute(DeclarativeProcessInstance instance) throws TDMTestFailedException {
		ConstraintEvaluation canTerminate = instance.canTerminate();
		boolean shouldCanTerminate = assertion.canTerminate();

		if (canTerminate.getResult() ^ shouldCanTerminate) {
			cause = new TerminationAssertionFailedCause(assertion, canTerminate);
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
