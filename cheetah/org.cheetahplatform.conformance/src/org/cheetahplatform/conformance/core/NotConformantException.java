package org.cheetahplatform.conformance.core;

import java.util.List;

public class NotConformantException extends Exception {

	private static final long serialVersionUID = -4766110954685062608L;

	private final List<ConformanceActivity> trace;

	public NotConformantException(List<ConformanceActivity> trace, String message) {
		super(message);
		this.trace = trace;
	}

	@Override
	public String getMessage() {
		String message = super.getMessage();
		message = "Trace: " + trace.toString() + "\n\n" + message;
		return message;
	}

}
