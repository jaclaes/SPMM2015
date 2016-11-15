package org.cheetahplatform.modeler.dialog;

import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.Assert;

public class DemoEntry extends AbstractDemonstratable {
	private final String processModelId;

	public DemoEntry(String name, String description, String processModelId) {
		super(name, description);
		Assert.isNotNull(processModelId);

		this.processModelId = processModelId;
	}

	@Override
	public List<IDemonstratable> getChildren() {
		return Collections.emptyList();
	}

	public String getProcessModelId() {
		return processModelId;
	}
}