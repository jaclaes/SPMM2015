package org.cheetahplatform.core.declarative.constraint;

import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;

public abstract class AbstractDeclarativeTerminationConstraintWithOneActivity extends AbstractDeclarativeConstraintWithOneActivity {

	private static final long serialVersionUID = -7570468975311461590L;

	protected AbstractDeclarativeTerminationConstraintWithOneActivity(DeclarativeActivity activity) {
		super(activity);
	}

	public boolean isExecutable(DeclarativeActivity activity, DeclarativeProcessInstance processInstance) {
		return true;
	}

}
