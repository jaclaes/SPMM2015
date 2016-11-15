package org.cheetahplatform.shared;

import org.cheetahplatform.core.imperative.modeling.ImperativeActivity;

public class ActivityHandle extends TypedHandle {

	public ActivityHandle(ImperativeActivity activity) {
		super(activity.getCheetahId(), activity.getName(), activity.getType());
	}

	public ActivityHandle(long id, String name, String type) {
		super(id, name, type);
	}

	public ActivityHandle(String type) {
		this(0, "", type);
	}

}
