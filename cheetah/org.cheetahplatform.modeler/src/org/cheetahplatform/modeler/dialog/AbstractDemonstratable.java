package org.cheetahplatform.modeler.dialog;

import org.eclipse.core.runtime.Assert;

public abstract class AbstractDemonstratable implements IDemonstratable {
	private String name;
	private final String description;

	public AbstractDemonstratable(String name, String description) {
		Assert.isNotNull(name);
		Assert.isNotNull(description);
		this.name = name;
		this.description = description;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getName() {
		return name;
	}

}