package org.cheetahplatform.modeler.experiment.editor.xml;

public class ExpEditorMarshallerException extends Exception {

	private static final long serialVersionUID = -5258522512775540632L;

	private Throwable cause;

	public ExpEditorMarshallerException(final String message) {
		super(message);
	}

	public ExpEditorMarshallerException(final String message, final Throwable cause) {
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
