package org.cheetahplatform.core.declarative.constraint;

import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         24.06.2009
 */
public abstract class AbstractDeclarativeTerminationConstraintWithTwoActivities extends AbstractDeclarativeConstraintWithTwoActivities {
	private static final long serialVersionUID = 5388735837809005495L;

	protected AbstractDeclarativeTerminationConstraintWithTwoActivities() {
		super();
	}

	protected AbstractDeclarativeTerminationConstraintWithTwoActivities(DeclarativeActivity activity1, DeclarativeActivity activity2) {
		super(activity1, activity2);
	}

	public boolean isExecutable(DeclarativeActivity activity, DeclarativeProcessInstance processInstance) {
		return true;
	}
}
