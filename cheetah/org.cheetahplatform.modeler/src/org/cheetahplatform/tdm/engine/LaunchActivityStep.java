package org.cheetahplatform.tdm.engine;

import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.core.declarative.constraint.ConstraintEvaluation;
import org.cheetahplatform.core.declarative.runtime.DeclarativeActivityInstance;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;

public class LaunchActivityStep implements ITDMStep {

	private TDMActivity activity;
	private ActivityNotExecutableCause cause;

	public LaunchActivityStep(TDMActivity activity) {
		this.activity = activity;
	}

	@Override
	public void execute(DeclarativeProcessInstance instance) throws TDMTestFailedException {
		ConstraintEvaluation evaluation = instance.isExecutable(activity.getActivity());
		if (!evaluation.getResult()) {
			cause = new ActivityNotExecutableCause(activity, evaluation.getConstraint());
			throw new TDMTestFailedException(this);
		}

		DeclarativeActivityInstance activityInstance = activity.getActivity().instantiate(instance);
		activityInstance.requestActivation();
		activityInstance.launch();

		activity.setActivityInstance(activityInstance);
	}

	@Override
	public ITDMFailureCause getFailure() {
		return cause;
	}

	@Override
	public Object getSource() {
		return activity.getActivityUiModel();
	}

	@Override
	public DateTime getStartTime() {
		return activity.getStartTime();
	}
}
