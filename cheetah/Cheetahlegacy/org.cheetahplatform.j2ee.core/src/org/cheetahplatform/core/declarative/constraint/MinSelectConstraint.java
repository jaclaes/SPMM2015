package org.cheetahplatform.core.declarative.constraint;

import java.text.MessageFormat;

import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;

public class MinSelectConstraint extends SelectionConstraint {

	private static final long serialVersionUID = -7606840134506089813L;

	protected MinSelectConstraint() {
		super(null, 0, 0); // required by hibernate
	}

	public MinSelectConstraint(DeclarativeActivity activity, int minimum) {
		super(activity, minimum, NO_MAXIMUM_SET);
	}

	@Override
	public String getDescription() {
		return MessageFormat.format("Activity ''{0}'' must be executed at least {1} time(s).", activity.getName(), minimum);
	}

}
