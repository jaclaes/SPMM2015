package org.cheetahplatform.tdm.engine;

import java.text.MessageFormat;

import org.cheetahplatform.core.declarative.constraint.IDeclarativeConstraint;
import org.cheetahplatform.tdm.TDMConstants;

public class ActivityNotExecutableCause extends AbstractTDMFailureCause {

	private TDMActivity activity;
	private final IDeclarativeConstraint constraint;

	public ActivityNotExecutableCause(TDMActivity activity, IDeclarativeConstraint constraint) {
		this.activity = activity;
		this.constraint = constraint;
	}

	@Override
	protected IDeclarativeConstraint getConstraintCausingFailure() {
		return constraint;
	}

	@Override
	public String getProblem() {
		String problem = "''{0}'' cannot be executed:\n\n{1}";
		problem = MessageFormat.format(problem, activity.getActivity().getName(), constraint.getDescription());
		return problem;
	}

	@Override
	public void revealFailure() {
		activity.getActivityUiModel().getWorkspace().selectExclusively(activity.getActivityUiModel());
	}

	@Override
	public void visualizeFailureInTest() {
		activity.getActivityUiModel().setBackgroundColor(TDMConstants.COLOR_TEST_FAILED);
		activity.getActivityUiModel().setTooltip(getProblem());
	}

}
