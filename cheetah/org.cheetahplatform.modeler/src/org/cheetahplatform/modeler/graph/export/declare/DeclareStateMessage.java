package org.cheetahplatform.modeler.graph.export.declare;

public class DeclareStateMessage {
	private String state;

	private String message;

	public DeclareStateMessage(String state, String message) {
		this.state = state;
		this.message = message;
	}

	public final String getMessage() {
		return message;
	}

	public final String getState() {
		return state;
	}
}
