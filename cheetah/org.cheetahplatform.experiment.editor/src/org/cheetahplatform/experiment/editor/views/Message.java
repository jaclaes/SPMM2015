package org.cheetahplatform.experiment.editor.views;

public class Message {
	public enum Type {
		ERROR, WARNING, INFO
	};

	private Type type;
	private String modelName;
	private String message;

	public Message(Type type, String modelName, String message) {
		this.type = type;
		this.modelName = modelName;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public String getModelName() {
		return modelName;
	}

	public Type getType() {
		return type;
	}

}
