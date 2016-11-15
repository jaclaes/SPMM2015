package org.cheetahplatform.modeler.graph;

public class LackOfSynchronizationError implements ISyntaxError {
	String description;

	public LackOfSynchronizationError(String description) {
		this.description = description;
	}

	@Override
	public String getDescription() {
		return description;
	}
}
