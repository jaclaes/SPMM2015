package org.cheetahplatform.core.declarative.constraint;

import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;

public abstract class AbstractDeclarativeExecutionConstraintWithOneActivity extends AbstractDeclarativeConstraintWithOneActivity {

	private static final long serialVersionUID = 7881668690006457936L;

	protected AbstractDeclarativeExecutionConstraintWithOneActivity(DeclarativeActivity activity) {
		super(activity);
	}

	public boolean canTerminate(DeclarativeProcessInstance processInstance) {
		return true;
	}

}
