package org.cheetahplatform.tdm.engine;

import java.text.MessageFormat;

import org.cheetahplatform.core.declarative.constraint.ConstraintEvaluation;
import org.cheetahplatform.core.declarative.constraint.IDeclarativeConstraint;
import org.cheetahplatform.tdm.TDMConstants;
import org.cheetahplatform.tdm.daily.model.ExecutionAssertion;

public class ExecutionAssertionFailedCause extends AbstractTDMFailureCause {

	private final ConstraintEvaluation evaluation;
	private final TDMExecutionAssertion assertion;

	public ExecutionAssertionFailedCause(TDMExecutionAssertion assertion, ConstraintEvaluation evaluation) {
		this.assertion = assertion;
		this.evaluation = evaluation;
	}

	@Override
	protected IDeclarativeConstraint getConstraintCausingFailure() {
		return evaluation.getConstraint();
	}

	@Override
	public String getProblem() {
		ExecutionAssertion activityUiModel = assertion.getActivityUiModel();
		String problem = "";

		if (evaluation.getResult()) {
			String rawMessage = "''{0}'' should have not been executable.";
			problem = MessageFormat.format(rawMessage, activityUiModel.getName());
		} else {
			String rawMessage = "''{0}'' is not executable:\n\n{1}";
			problem = MessageFormat.format(rawMessage, activityUiModel.getName(), evaluation.getConstraint().getDescription());
		}

		return problem;
	}

	@Override
	public void revealFailure() {
		assertion.getActivityUiModel().getWorkspace().selectExclusively(assertion.getActivityUiModel());
	}

	@Override
	public void visualizeFailureInTest() {
		ExecutionAssertion activityUiModel = assertion.getActivityUiModel();
		activityUiModel.setBackgroundColor(TDMConstants.COLOR_TEST_FAILED);
		activityUiModel.setTooltip(getProblem());
	}

}
