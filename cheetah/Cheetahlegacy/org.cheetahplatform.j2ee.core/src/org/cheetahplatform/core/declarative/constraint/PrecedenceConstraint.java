package org.cheetahplatform.core.declarative.constraint;

import java.util.List;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;
import org.cheetahplatform.core.declarative.runtime.IDeclarativeNodeInstance;
import org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState;

public class PrecedenceConstraint extends AbstractDeclarativeExecutionConstraintWithTwoActivities {

	private static final long serialVersionUID = 4509613293236305594L;

	@SuppressWarnings("unused")
	private PrecedenceConstraint() {
		super();// hibernate
	}

	/**
	 * Creates a new constraint.
	 * 
	 * @param activity1
	 *            the first activity
	 * @param activity2
	 *            the second activity
	 */
	public PrecedenceConstraint(DeclarativeActivity activity1, DeclarativeActivity activity2) {
		super(activity1, activity2);

		Assert.isNotNull(activity1);
		Assert.isNotNull(activity2);
	}

	public String getDescription() {
		return "'" + activity1.getName() + "' has to be executed before '" + activity2.getName() + "' can be executed.";
	}

	public boolean isExecutable(DeclarativeActivity toCheck, DeclarativeProcessInstance processInstance) {
		if (!toCheck.equals(activity2)) {
			return true;
		}
		List<IDeclarativeNodeInstance> nodeInstances = processInstance.getNodeInstances(activity1);
		for (IDeclarativeNodeInstance instance : nodeInstances) {
			if (!instance.getState().equals(INodeInstanceState.COMPLETED))
				continue;

			return true;
		}
		return false;
	}
}
