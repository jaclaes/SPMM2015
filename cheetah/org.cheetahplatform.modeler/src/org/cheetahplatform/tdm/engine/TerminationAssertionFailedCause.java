package org.cheetahplatform.tdm.engine;

import java.text.MessageFormat;

import org.cheetahplatform.core.declarative.constraint.ConstraintEvaluation;
import org.cheetahplatform.core.declarative.constraint.IDeclarativeConstraint;
import org.cheetahplatform.tdm.TDMConstants;

public class TerminationAssertionFailedCause extends AbstractTDMFailureCause {

	private final TDMTerminationAssertion assertion;
	private final ConstraintEvaluation canTerminate;

	public TerminationAssertionFailedCause(TDMTerminationAssertion assertion, ConstraintEvaluation canTerminate) {
		this.assertion = assertion;
		this.canTerminate = canTerminate;
	}

	@Override
	protected IDeclarativeConstraint getConstraintCausingFailure() {
		return canTerminate.getConstraint();
	}

	@Override
	public String getProblem() {
		String problem = "";
		if (!canTerminate.getResult()) {
			problem = "It should have been possible to terminate the process instance, but was not due to the following reason:\n\n{0}";
			problem = MessageFormat.format(problem, canTerminate.getConstraint().getDescription());
		} else {
			problem = "The process instance can be terminated at this point in time although it should not be possible.";
		}

		return problem;
	}

	@Override
	public void revealFailure() {
		assertion.getActivityUiModel().getWorkspace().selectExclusively(assertion.getActivityUiModel());
	}

	@Override
	public void visualizeFailureInTest() {
		assertion.getActivityUiModel().setBackgroundColor(TDMConstants.COLOR_TEST_FAILED);
		assertion.getActivityUiModel().setTooltip(getProblem());
	}

}
