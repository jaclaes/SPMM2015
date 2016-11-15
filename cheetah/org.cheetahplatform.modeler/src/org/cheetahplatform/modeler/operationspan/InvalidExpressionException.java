package org.cheetahplatform.modeler.operationspan;

public class InvalidExpressionException extends Exception {

	/**
	 * serialVersionId
	 */
	private static final long serialVersionUID = -1857542054852631266L;
	private Throwable cause;

	public InvalidExpressionException(final String message) {
		super(message);
	}

	public InvalidExpressionException(final String message, final Throwable cause) {
		super(message);
		// don't pass the cause to super, to allow compilation against JCL
		// Foundation
		this.cause = cause;
	}

	@Override
	public Throwable getCause() {
		return cause;
	}

}