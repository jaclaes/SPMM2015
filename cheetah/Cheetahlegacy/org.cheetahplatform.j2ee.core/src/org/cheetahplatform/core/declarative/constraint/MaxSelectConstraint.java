package org.cheetahplatform.core.declarative.constraint;

import java.text.MessageFormat;

import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;

public class MaxSelectConstraint extends SelectionConstraint {

	private static final long serialVersionUID = 1378796504551799397L;

	public MaxSelectConstraint(DeclarativeActivity activity, int maximum) {
		super(activity, NO_MINIMUM_SET, maximum);
	}

	@Override
	public String getDescription() {
		return MessageFormat.format("Activity ''{0}'' must be executed at most {1} time(s).", activity.getName(), maximum);
	}

}
