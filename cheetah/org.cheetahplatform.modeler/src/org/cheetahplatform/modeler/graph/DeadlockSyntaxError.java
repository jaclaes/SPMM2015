package org.cheetahplatform.modeler.graph;

public class DeadlockSyntaxError implements ISyntaxError {
	private String description;

	public DeadlockSyntaxError(String description) {
		this.description = description;
	}

	@Override
	public String getDescription() {
		return description;
	}
}
