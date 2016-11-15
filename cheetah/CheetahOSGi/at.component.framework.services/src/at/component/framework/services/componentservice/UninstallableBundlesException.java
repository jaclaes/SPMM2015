package at.component.framework.services.componentservice;

public class UninstallableBundlesException extends Exception {

	private String message;

	private static final long serialVersionUID = 1L;

	public UninstallableBundlesException(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
