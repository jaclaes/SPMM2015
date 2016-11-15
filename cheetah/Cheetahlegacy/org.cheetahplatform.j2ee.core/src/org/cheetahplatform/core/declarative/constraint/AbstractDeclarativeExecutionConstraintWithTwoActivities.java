package org.cheetahplatform.core.declarative.constraint;

import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         24.06.2009
 */
public abstract class AbstractDeclarativeExecutionConstraintWithTwoActivities extends AbstractDeclarativeConstraintWithTwoActivities {
	private static final long serialVersionUID = -6162238455138679741L;

	protected AbstractDeclarativeExecutionConstraintWithTwoActivities() {
		super();
	}

	protected AbstractDeclarativeExecutionConstraintWithTwoActivities(DeclarativeActivity activity1, DeclarativeActivity activity2) {
		super(activity1, activity2);
	}

	public boolean canTerminate(DeclarativeProcessInstance processInstance) {
		return true;
	}
}
